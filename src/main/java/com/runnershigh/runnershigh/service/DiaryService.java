package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.diary.AddDiaryReqDto;
import com.runnershigh.runnershigh.entity.Diary;
import com.runnershigh.runnershigh.repository.DiaryRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    public ApiRespDto<?> addDiary(AddDiaryReqDto addDiaryReqDto, PrincipalUser principalUser) {
        if(!Objects.equals(principalUser.getUserId(), addDiaryReqDto.getUserId())) {
            return new ApiRespDto<>("failed", "일지 작성 권한이 없습니다.", null);
        }

        Optional<Diary> optionalDiary = diaryRepository.addDiary(addDiaryReqDto.toEntity());

        if(optionalDiary.isEmpty()) {
            return new ApiRespDto<>("failed", "서버 오류로 일지 등록에 실패했습니다.", null);
        }

        return new ApiRespDto<>("success", "일지를 등록했습니다.", optionalDiary.get());
    }

    public ApiRespDto<?> getActiveListByUserIdAndDate(Integer year, Integer month, PrincipalUser principalUser) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<LocalDate> activeList = diaryRepository.getActiveListByUserIdAndDate(principalUser.getUserId(),
                                                    startDate.toString(), endDate.toString());

        return new ApiRespDto<>("success", "일지 작성 날짜를 조회했습니다.", activeList);
    }

    public ApiRespDto<?> getDiaryByUserIdAndDate(String dateStr, PrincipalUser principalUser) {
        LocalDate requestedDate = LocalDate.parse(dateStr);
        Optional<Diary> optionalDiary = diaryRepository.getDiaryByUserIdAndDate(principalUser.getUserId(), dateStr);

        if(optionalDiary.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 일자에 등록된 일지가 없습니다.", null);
        }

        return new ApiRespDto<>("success", "일지를 조회했습니다.", optionalDiary.get());
    }
}
