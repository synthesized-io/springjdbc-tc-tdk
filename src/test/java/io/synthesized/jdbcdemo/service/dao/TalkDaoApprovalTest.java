package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import io.synthesized.jdbcdemo.domain.Talk;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

@SpringBootTest(classes = DaoTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TalkDaoApprovalTest {
    @Autowired
    private TalkDao dao;

    @Autowired
    ConferenceDao conferenceDao;

    private Conference conference;

    @BeforeEach
    void init() throws SQLException {
        conference = conferenceDao.getConferences().iterator().next();
    }

    @Test
    void getTalksByConference() {
        Objects.equals("a", "b");
        //Approval test
        Set<Talk> talks = dao.getTalksByConference(conference);
        JsonApprovals.verifyAsJson(talks);
    }

}
