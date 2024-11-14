package com.ptip.service;

import com.ptip.mapper.AwardMapper;
import com.ptip.models.Department;
import com.ptip.models.Program;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class AwardService {

    @Autowired
    private AwardMapper awardMapper;

    public List<Program> searchById(Integer id) {
        return awardMapper.searchById(id);
    }

    public List<Department> filterByFirstConsonant(String consonant) {
        return awardMapper.filterByFirstConsonant(consonant);
    }

    public List<Program> searchPrograms(String name, Integer point, String department) {
        return awardMapper.searchPrograms(name, point, department);
    }
}
