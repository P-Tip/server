package com.ptip.service;

import com.ptip.mapper.HaksikMapper;
import com.ptip.models.Haksik;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public List<Haksik> getHaksikbyRange(LocalDate startdate, LocalDate enddate) {
        List<Haksik> haksikList = haksikMapper.getHaksikByRange(startdate.toString(), enddate.toString());
        return (haksikList != null) ? haksikList : Collections.emptyList();
    }

    public List<Haksik> getUpcomingHaksik(LocalTime now, LocalDate today, LocalDate tomorrow) {


        List<Haksik> haksikList = haksikMapper.getHaksikByRange(today.toString(), tomorrow.toString());
        LocalTime firstHaksikEndTime = LocalTime.of(9, 30);
        LocalTime secondHaksikEndTime = LocalTime.of(14, 0);

        List<Haksik> upcomingHaksikList = new ArrayList<>();

        // 요일 확인 (월요일과 금요일에 아침 메뉴가 제공되지 않음)
        int todayDayOfWeek = today.getDayOfWeek().getValue();

        if (todayDayOfWeek > 5) {
            return upcomingHaksikList;
        }

        if (now.isBefore(firstHaksikEndTime)){
            if (todayDayOfWeek == 1 || todayDayOfWeek == 5){
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(today.toString()) && haksik.getHaksik_type().equals("lunch"))
                        .forEach(upcomingHaksikList::add);
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(tomorrow.toString()) && haksik.getHaksik_type().equals("breakfast"))
                        .forEach(upcomingHaksikList::add);
            }
            else {
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(today.toString()) && haksik.getHaksik_type().equals("breakfast"))
                        .forEach(upcomingHaksikList::add);
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(today.toString()) && haksik.getHaksik_type().equals("lunch"))
                        .forEach(upcomingHaksikList::add);
            }
        }
        else if (now.isAfter(firstHaksikEndTime) && now.isBefore(secondHaksikEndTime)){
            haksikList.stream()
                    .filter(haksik -> haksik.getDate().equals(today.toString()) && haksik.getHaksik_type().equals("lunch"))
                    .forEach(upcomingHaksikList::add);
            if (todayDayOfWeek !=4){
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(tomorrow.toString()) && haksik.getHaksik_type().equals("breakfast"))
                        .forEach(upcomingHaksikList::add);
            }else {
                haksikList.stream()
                        .filter(haksik -> haksik.getDate().equals(tomorrow.toString()) && haksik.getHaksik_type().equals("lunch"))
                        .forEach(upcomingHaksikList::add);
            }

        }
        else if (now.isAfter(secondHaksikEndTime)){
            haksikList.stream()
                    .filter(haksik -> haksik.getDate().equals(tomorrow.toString()) && haksik.getHaksik_type().equals("breakfast"))
                    .forEach(upcomingHaksikList::add);
            haksikList.stream()
                    .filter(haksik -> haksik.getDate().equals(tomorrow.toString()) && haksik.getHaksik_type().equals("lunch"))
                    .forEach(upcomingHaksikList::add);

        }

        return upcomingHaksikList;
    }

}
