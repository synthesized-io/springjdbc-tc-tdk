package io.synthesized.jdbcdemo.service.dao;

import io.synthesized.jdbcdemo.domain.Conference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DaoTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ConferenceDaoTest {

    @Autowired
    DataSource ds;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConferenceDao dao;

    private int getConferenceCount() throws SQLException {
        return jdbcTemplate.query("select count(*) from conference",
                rs -> {
                    rs.next();
                    return rs.getInt(1);
                });
    }

    private Collection<Conference> getTestConferences() {
        return Arrays.asList(TestData.JPOINT, TestData.JOKER);
    }

    @Test
    void saveConferences() throws SQLException {
        Collection<Conference> testConferences = getTestConferences();
        var confCount = getConferenceCount();
        dao.saveConferences(testConferences);
        assertEquals(testConferences.size() + confCount, getConferenceCount());
    }

    @Test
    void getConferences() throws SQLException {
        Set<Conference> conferences = dao.getConferences();
        assertTrue(getConferenceCount() > 0);
        assertEquals(getConferenceCount(), conferences.size());
    }
}