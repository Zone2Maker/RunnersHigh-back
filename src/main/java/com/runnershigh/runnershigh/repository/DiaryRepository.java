package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.entity.Diary;
import com.runnershigh.runnershigh.mapper.DiaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DiaryRepository {

    @Autowired
    private DiaryMapper diaryMapper;

    public Optional<Diary> addDiary(Diary diary) {
        int result = diaryMapper.addDiary(diary);
        if(result == 0){
            return Optional.empty();
        }
        return Optional.of(diary);
    }

    public List<LocalDate> getActiveListByUserIdAndDate(Integer userId, String startDate, String endDate) {
        return diaryMapper.getActiveListByUserIdAndDate(userId, startDate, endDate);
    }

    public Optional<Diary> getDiaryByUserIdAndDate(Integer userId, String date){
        return diaryMapper.getDiaryByUserIdAndDate(userId, date);
    }
}
