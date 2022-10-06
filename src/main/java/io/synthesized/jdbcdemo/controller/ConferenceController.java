package io.synthesized.jdbcdemo.controller;

import io.synthesized.jdbcdemo.service.dao.ConferenceDao;
import io.synthesized.jdbcdemo.domain.Conference;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConferenceController.URI)
public class ConferenceController {
    public static final String URI = "/conference";
    private final ConferenceDao conferenceDao;

    @PutMapping
    public void saveConference(Conference conference) throws SQLException {
        conferenceDao.saveConferences(Collections.singleton(conference));
    }

    @GetMapping
    public Set<Conference> getConferences() throws SQLException {
        return conferenceDao.getConferences();
    }
}
