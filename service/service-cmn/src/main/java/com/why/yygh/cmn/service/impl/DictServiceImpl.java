package com.why.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.why.yygh.cmn.listener.DictListener;
import com.why.yygh.cmn.mapper.DictMapper;
import com.why.yygh.cmn.service.DictService;
import com.why.yygh.model.cmn.Dict;
import com.why.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    // 导出excel文档,把数据库中的字典导出到用户端
    @Override
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo,DictEeVo.class);
            dictEeVoList.add(dictEeVo);

        }
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 导入数据字典
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean isChildren(Long id) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(Dict::getParentId, id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;

    }
}
