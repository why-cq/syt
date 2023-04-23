package com.why.yygh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.why.yygh.mapper.HospitalSetMapper;
import com.why.yygh.model.hosp.HospitalSet;
import com.why.yygh.service.HospitalSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet> implements HospitalSetService{

    @Autowired
    private HospitalSetMapper hospitalSetMapper;
}
