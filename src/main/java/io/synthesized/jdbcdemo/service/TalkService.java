package io.synthesized.jdbcdemo.service;

import io.synthesized.jdbcdemo.domain.Status;
import io.synthesized.jdbcdemo.domain.Talk;
import io.synthesized.jdbcdemo.service.dao.TalkDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TalkService {
    private final TalkDao talkDao;

    public void changeStatus(int talkId, Status status) {
        Talk talk = talkDao.getTalkById(talkId);
        String feedback = talk.getFeedback();
        if (status == Status.REJECTED && (feedback == null || feedback.isBlank())) {
            throw new IllegalStateException("Cannot reject a talk without feedback");
        }
        talkDao.updateTalk(talk.withStatus(status));
    }
}
