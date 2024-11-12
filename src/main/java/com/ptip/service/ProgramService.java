package com.ptip.service;

import com.ptip.mapper.ProgramMapper;
import com.ptip.models.Program;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ProgramService {

    @Autowired
    private ProgramMapper programMapper;

    public List<Program> searchById(Integer id) {
        return programMapper.searchById(id);
    }

    public List<Program> searchPrograms(String name, Integer point, String department) {
        return programMapper.searchPrograms(name, point, department);
    }
}
