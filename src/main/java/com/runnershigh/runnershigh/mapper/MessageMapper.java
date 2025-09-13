package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MessageMapper {
    int saveMessage(Message message);
    List<GetMessageRespDto> getMessageList(int crewId, int userId, int cursorMessageId, int size);
    Optional<GetMessageRespDto> getMessageByMessageId(int messageId);
}
