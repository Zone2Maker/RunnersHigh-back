package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {
   int insertLike(Like like);
   int deleteLike(Like like);
}
