package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@SpringBootTest(classes = DaoTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SpeakerDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SpeakerDao dao;

    private int getSpeakerCount() throws SQLException {
        return jdbcTemplate.query("select count(*) from speaker", rs->{
            rs.next();
            return rs.getInt(1);
        });
    }

    private Collection<Speaker> getTestSpeakers() {
        return Arrays.asList(TestData.EGOROV, TestData.TOLKACHEV, TestData.BORISOV, TestData.VALEEV);
    }

    @Test
    void saveSpeakers() throws SQLException {
        int speakerCount = getSpeakerCount();

        Collection<Speaker> testSpeakers =  getTestSpeakers();
        dao.saveSpeakers(testSpeakers);
        assertEquals(speakerCount + getTestSpeakers().size(), getSpeakerCount());
    }

    @Test
    void getSpeakers() throws SQLException {
        Collection<Speaker> testSpeakers = getTestSpeakers();
        Set<Speaker> speakers = dao.getSpeakers();
        assertNotSame(testSpeakers, speakers);
    }

    @Test
    void getSpeakersByConference(@Autowired ConferenceDao conferenceDao,
                                 @Autowired TalkDao talkDao) throws SQLException {
        final Conference conference = conferenceDao.getConferences().stream().findFirst().get();
        Set<Speaker> speakers = dao.getSpeakersByConference(conference);
        System.out.println(speakers);
    }
}