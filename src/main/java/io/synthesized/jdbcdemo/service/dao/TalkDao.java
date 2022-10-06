package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@AllArgsConstructor
public class TalkDao {
    private final JdbcTemplate jdbcTemplate;

    public void updateTalk(Talk talk) {
        jdbcTemplate.execute("update talk set name = ?, conferenceid = ?, status = ?, feedback = ? where " +
                "id = ?", (PreparedStatementCallback<?>) updateTalk -> {
            updateTalk.setString(1, talk.getName());
            updateTalk.setInt(2, talk.getConference().getId());
            updateTalk.setString(3, talk.getStatus().toString());
            updateTalk.setString(4, talk.getFeedback());
            updateTalk.setInt(5, talk.getId());
            updateTalk.execute();
            return null;
        });
    }


    public void insertTalks(Collection<Talk> talks) {
        jdbcTemplate.execute("insert into talk(id, name, conferenceid, status, feedback) values (?, ?, ?, ?, ?)",
                (PreparedStatementCallback<?>) insertTalk -> {
                    jdbcTemplate.execute("insert into talkspeakers(talkid, speakerid) values(?, ?)",
                            (PreparedStatementCallback<?>) insertTalkSpeakers -> {
                                for (Talk talk : talks) {
                                    insertTalk.setInt(1, talk.getId());
                                    insertTalk.setString(2, talk.getName());
                                    insertTalk.setInt(3, talk.getConference().getId());
                                    insertTalk.setString(4, talk.getStatus().toString());
                                    insertTalk.setString(5, talk.getFeedback());
                                    insertTalk.execute();
                                    for (Speaker speaker : talk.getSpeakers()) {
                                        insertTalkSpeakers.setInt(1, talk.getId());
                                        insertTalkSpeakers.setInt(2, speaker.getId());
                                        insertTalkSpeakers.execute();
                                    }
                                }
                                return null;
                            });
                    return null;
                });
    }

    private Talk createTalk(ResultSet talkSet, ResultSet speakerSet) throws SQLException {
        Set<Speaker> speakers = new HashSet<>();
        while (speakerSet.next()) {
            speakers.add(new Speaker(
                    speakerSet.getInt("id"),
                    speakerSet.getString("name")));
        }
        return new Talk(
                talkSet.getInt("id"),
                talkSet.getString("name"),
                new Conference(talkSet.getInt("conferenceid"),
                        talkSet.getString("conferencename")),
                Status.valueOf(talkSet.getString("status")),
                talkSet.getString("feedback"),
                Collections.unmodifiableSet(speakers)
        );
    }

    public Talk getTalkById(int id) {
        final AtomicReference<Talk> result = new AtomicReference<>();
        jdbcTemplate.execute("select talk.id id, talk.name \"name\", talk.status, talk.feedback, " +
                "conferenceid, conference.name conferencename " +
                "from talk  inner join conference on conferenceid = conference.id " +
                "where talk.id = ?", (PreparedStatementCallback<?>) selectTalks -> {
            jdbcTemplate.execute("select speaker.id id, speaker.name \"name\" " +
                    "from talkspeakers inner join speaker on speaker.id = speakerid " +
                    "where talkid = ?", (PreparedStatementCallback<?>) selectSpeakers -> {
                selectTalks.setInt(1, id);
                try (ResultSet talkSet = selectTalks.executeQuery()) {
                    while (talkSet.next()) {
                        selectSpeakers.setInt(1, talkSet.getInt("id"));
                        ResultSet speakerSet = selectSpeakers.executeQuery();
                        result.set(createTalk(talkSet, speakerSet));
                    }
                }
                return null;
            });
            return null;
        });
        return result.get();
    }

    public Set<Talk> getTalksByConference(Conference conference) {
        Set<Talk> result = new HashSet<>();
        jdbcTemplate.execute("select talk.id id, talk.name \"name\", talk.status, talk.feedback, " +
                "conferenceid, conference.name conferencename " +
                "from talk inner join conference on conferenceid = conference.id " +
                "where conferenceid = ?", (PreparedStatementCallback<?>) selectTalks -> {
            jdbcTemplate.execute("select speaker.id id, speaker.name \"name\" " +
                    "from talkspeakers inner join speaker on speaker.id = speakerid " +
                    "where talkid = ?", (PreparedStatementCallback<?>) selectSpeakers -> {
                selectTalks.setInt(1, conference.getId());
                try (ResultSet talkSet = selectTalks.executeQuery()) {
                    while (talkSet.next()) {
                        selectSpeakers.setInt(1, talkSet.getInt("id"));
                        ResultSet speakerSet = selectSpeakers.executeQuery();
                        result.add(createTalk(talkSet, speakerSet));
                    }
                }
                return null;
            });
            return null;
        });
        return result;
    }
}
