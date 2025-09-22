package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.entity.Message;
import com.runnershigh.runnershigh.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageRepository {
    @Autowired
    private MessageMapper messageMapper;

    public Optional<Message> saveMessage(Message message) {
        try {
            messageMapper.saveMessage(message);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of(message);
    }

    public List<GetMessageRespDto> getMessageList(Integer crewId, Integer userId, Long cursorMessageId, Integer size) {
        return messageMapper.getMessageList(crewId, userId, cursorMessageId, size);
    }

    public Optional<GetMessageRespDto> getMessageByMessageId(int messageId) {
        return messageMapper.getMessageByMessageId(messageId);
    }

    public Integer getUnreadMessageCount(Integer crewId, Integer userId) {
        return messageMapper.getUnreadMessageCount(crewId, userId);
    }
}