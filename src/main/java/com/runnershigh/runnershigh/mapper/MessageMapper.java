package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    int saveMessage(Message message);
    List<GetMessageRespDto> getMessageList(GetMessageListReqDto getMessageListReqDto);
}
