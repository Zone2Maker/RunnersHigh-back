package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface DiaryMapper {
    int addDiary(Diary diary);
    int updateDiary(Diary diary);
    int deleteDiary(Integer diaryId);
    List<LocalDate> getActiveListByUserIdAndDate(Integer userId, String startDate, String endDate);
    Optional<Diary> getDiaryByUserIdAndDate(Integer userId, String diaryDate);
}
