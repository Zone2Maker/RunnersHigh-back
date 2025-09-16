package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.feed.RemoveLikeReqDto;
import com.runnershigh.runnershigh.entity.Like;
import com.runnershigh.runnershigh.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {

    @Autowired
    private LikeMapper likeMapper;

    public int addLike(Like like) {
        return likeMapper.addLike(like);
    }

    public int removeLike(RemoveLikeReqDto removeLikeReqDto) {
        return likeMapper.removeLike(removeLikeReqDto);
    }
}
