package com.ptip.service;

import com.ptip.Mapper.HaksikMapper;
import com.ptip.models.Haksik;
import org.springframework.stereotype.Service;

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


}
