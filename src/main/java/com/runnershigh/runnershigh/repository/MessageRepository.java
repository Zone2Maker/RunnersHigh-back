package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.message.GetLastReadMessageIdReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
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

    private Optional<Message> addMessage(Message message) {
        try {
            messageMapper.addMessage(message);
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
        return Optional.of();
    }

    public int getLastReadMessageId(GetLastReadMessageIdReqDto getLastReadMessageIdReqDto) {
        return messageMapper.getLastReadMessageId(getLastReadMessageIdReqDto);
    }

    public List<GetMessageListRespDto> getMessageList(GetMessageListReqDto getMessageListReqDto) {
        return messageMapper.getMessageList(getMessageListReqDto);
    }

}