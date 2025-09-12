package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
import com.runnershigh.runnershigh.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    int addMessage(Message message);
    List<GetMessageListRespDto> getMessageList(int page, int offset, int crewId, int userId);
}
