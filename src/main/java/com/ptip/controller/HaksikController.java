package com.ptip.controller;


import com.ptip.models.Haksik;
import com.ptip.service.HaksikService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/haksik")
public class HaksikController {

    private HaksikService haksikService;

    private ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

    // 현재 시간 (한국 시간)
    private ZonedDateTime nowInKorea = ZonedDateTime.now(koreaZoneId);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public HaksikController(HaksikService haksikService) {
        this.haksikService = haksikService;
    }

    @GetMapping("/{date}")
    public List<Haksik> byDate(@PathVariable("date") String date){
        System.out.println(date);
        return haksikService.getHaksikByDate(date);
    }

    @GetMapping("/today")
    public List<Haksik> todayHaksik(){
        LocalDate today = nowInKorea.toLocalDate();
        return haksikService.getHaksikByDate(today.format(dateFormatter));
    }

    @GetMapping("/thisweek")
    public List<Haksik> thisWeekHaksik(){
        LocalDate today = nowInKorea.toLocalDate();
        LocalDate monday =  today.with(DayOfWeek.MONDAY);
        LocalDate friday = today.with(DayOfWeek.FRIDAY);
        return haksikService.getHaksikbyRange(monday.format(dateFormatter),friday.format(dateFormatter));

    }

    @GetMapping("/upcoming")
    public List<Haksik> upcomingHaksik(){
        LocalTime now = nowInKorea.toLocalTime();
        LocalDate today = nowInKorea.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);
        return haksikService.getUpcomingHaksik(now,today.format(dateFormatter),tomorrow.format(dateFormatter));
    }

}
