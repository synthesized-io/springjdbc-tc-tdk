package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Speaker;
import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.TalkService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;

@SpringBootTest(classes = DaoTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TalkDaoTest {
    @Autowired
    private TalkDao dao;

    @Autowired
    ConferenceDao conferenceDao;

    @Autowired
    SpeakerDao speakerDao;

    @Autowired
    TalkService service;

    private Conference conference;
    private Speaker speaker;
    private Talk talk;

    @BeforeEach
    void init() throws SQLException {
/*
        var speakerId = TDK.generate("public.talk")
                .with("speaker_id",  speaker.getId())
                .one().asIntegerPair();
*/
        //var id = conferennceDao.insert("foobar", "foobar");

        conference = conferenceDao.getConferences().iterator().next();
        speaker = speakerDao.getSpeakers().iterator().next();
        talk = dao.getTalksByConference(conference).iterator().next();
    }



    @Test
    void rejectInReviewWithFeedback() {
        //Arrange
        dao.updateTalk(talk.withStatus(Status.IN_REVIEW).withFeedback("feedback"));
        //Act
        service.changeStatus(talk.getId(), Status.REJECTED);
        //Assert
        Assertions.assertThat(dao.getTalkById(talk.getId()).getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    void doNotRejectInReviewWithoutFeedback() {
        //Arrange
        dao.updateTalk(talk.withStatus(Status.IN_REVIEW).withFeedback(""));
        //Act, Assert
        Assertions.assertThatThrownBy(() ->
                service.changeStatus(talk.getId(), Status.REJECTED)).hasMessageContaining("feedback");
        Assertions.assertThat(dao.getTalkById(talk.getId()).getStatus()).isEqualTo(Status.IN_REVIEW);
    }



}