package com.ptip.service;

import com.ptip.mapper.HaksikMapper;
import com.ptip.models.Haksik;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HaksikService {
    private final HaksikMapper haksikMapper;

    public HaksikService(HaksikMapper haksikMapper) {
        this.haksikMapper = haksikMapper;
    }

    public List<Haksik> getHaksikByDate(String date) {
        List<Haksik> haksikList = haksikMapper.getHaksikByDate(date);
        return (haksikList != null) ? haksikList : Collections.emptyList();
    }

    public List<Haksik> getHaksikbyRange(String startdate, String enddate) {
        List<Haksik> haksikList = haksikMapper.getHaksikByRange(startdate, enddate);
        return (haksikList != null) ? haksikList : Collections.emptyList();
    }

    public List<Haksik> getUpcomingHaksik(LocalTime now, String today, String tomorrow) {
        List<Haksik> haksikList = haksikMapper.getHaksikByRange(today, tomorrow);
        LocalTime firstHaksikEndTime = LocalTime.of(9, 30);
        LocalTime secondHaksikEndTime = LocalTime.of(14, 0);

        List<Haksik> upcomingHaksikList = new ArrayList<>();

        // today와 tomorrow를 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate todayDate = LocalDate.parse(today, formatter);
        LocalDate tomorrowDate = LocalDate.parse(tomorrow, formatter);

        // 요일 확인 (월요일과 금요일에 아침 메뉴가 제공되지 않음)
        int todayDayOfWeek = todayDate.getDayOfWeek().getValue(); // 1 = 월요일, 5 = 금요일
        int tomorrowDayOfWeek = tomorrowDate.getDayOfWeek().getValue();

        // 현재 시간이 아침 식사 시간 전일 경우
        if (now.isBefore(firstHaksikEndTime)) {

            upcomingHaksikList.add(haksikList.get(0));
            upcomingHaksikList.add(haksikList.get(1));
        }
        // 현재 시간이 점심 시간 전일 경우
        else if (now.isAfter(firstHaksikEndTime) && now.isBefore(secondHaksikEndTime)) {
            // 내일 아침이 제공되지 않는다면 점심만 제공
            if (todayDayOfWeek == 1) {
                upcomingHaksikList.add(haksikList.get(0));
                upcomingHaksikList.add(haksikList.get(1));
            }else{
                upcomingHaksikList.add(haksikList.get(1));
                upcomingHaksikList.add(haksikList.get(2));
            }
        }
        // 현재 시간이 점심 시간 이후일 경우
        else {
            // 내일 아침이 제공되지 않는다면 점심만 제공
            if (tomorrowDayOfWeek == 5) {
                upcomingHaksikList.add(haksikList.get(2));
            }else {
                upcomingHaksikList.add(haksikList.get(2));
                upcomingHaksikList.add(haksikList.get(3));
            }
        }

        return (upcomingHaksikList != null) ? upcomingHaksikList : Collections.emptyList();
    }

}
