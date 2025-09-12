package com.runnershigh.runnershigh.repository;

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
        return likeMapper.insertLike(like);
    }

    public int removeLike(Like like) {
        return likeMapper.deleteLike(like);
    }
}
