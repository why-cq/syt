package com.why.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.why.yygh.cmn.mapper.DictMapper;
import com.why.yygh.cmn.service.DictService;
import com.why.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    //根据数据id查询子数据列表
    public List<Dict> findChildData(Long id) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(Dict::getParentId, id);
        List<Dict> dicts = baseMapper.selectList(queryWrapper);
        for (Dict dict : dicts) {
            Long dictId = dict.getId();
            boolean ok = isChildren(dictId);
            dict.setHasChildren(ok);

        }

        //应为我们多了一个属性所以我们要遍历出来所有的子数据,然后把多出来的属性加上
        return dicts;


    }

    private boolean isChildren(Long id){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(Dict::getParentId, id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;

    }
}
