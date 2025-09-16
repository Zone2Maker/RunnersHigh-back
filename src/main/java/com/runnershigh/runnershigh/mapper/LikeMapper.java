package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.feed.RemoveLikeReqDto;
import com.runnershigh.runnershigh.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {
   int addLike(Like like);
   int removeLike(RemoveLikeReqDto removeLikeReqDto);
}
