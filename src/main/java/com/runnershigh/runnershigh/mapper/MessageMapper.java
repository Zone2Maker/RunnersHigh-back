package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.message.GetLastReadMessageIdReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
import com.runnershigh.runnershigh.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    int addMessage(Message message);
    int getLastReadMessageId(GetLastReadMessageIdReqDto getLastReadMessageIdReqDto);
    List<GetMessageListRespDto> getMessageList(GetMessageListReqDto getMessageListReqDto);
}
