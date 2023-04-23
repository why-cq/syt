package com.why.yygh.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.why.yygh.common.exception.YyghException;
import com.why.yygh.common.result.Result;
import com.why.yygh.model.hosp.Hospital;
import com.why.yygh.model.hosp.HospitalSet;
import com.why.yygh.service.HospitalSetService;
import com.why.yygh.vo.hosp.HospitalQueryVo;
import com.why.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Random;

@Api("医院请求控制类")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    // 1.获取所有的医院列
    @GetMapping
    @ApiOperation("获取所有的医院列表")
    public List<HospitalSet> findall() {
        List<HospitalSet> list = hospitalSetService.list();
        return list;
    }

    // 2.逻辑删除医院设置
    @DeleteMapping("{id}")
    @ApiOperation("逻辑删除医院设置")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);

        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //3 条件查询带分页
    @ApiOperation("条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {

        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);

        //构造查询条件(可能不存在)
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(hospitalSetQueryVo.getHosname()), HospitalSet::getHosname, hospitalSetQueryVo.getHosname());
        queryWrapper.eq(!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()), HospitalSet::getHoscode, hospitalSetQueryVo.getHoscode());

        // 调用方法实现分页查询

        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);

        return Result.ok(hospitalSetPage);


    }

    // 4.添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置转状态为1   当为0时不能使用
        hospitalSet.setStatus(1);

        // 设置签名秘钥
        Random random = new Random();
        String rowSingKey = System.currentTimeMillis() + "" + random.nextInt(1000);

        hospitalSet.setSignKey(DigestUtils.md5Hex(rowSingKey));
        boolean ok = hospitalSetService.save(hospitalSet);
        if (ok) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 5.根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        try {
            int x = 1 / 0;
        } catch (YyghException e) {
            throw new YyghException("失败", 201);
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);

    }

    // 6.修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean ok = hospitalSetService.updateById(hospitalSet);
        if (ok) {
            return Result.ok();
        } else {
            return Result.fail();
        }

    }

    // 7.批量删除医院
    @ApiOperation("批量删除医院")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        boolean ok = hospitalSetService.removeByIds(ids);
        return Result.ok();

    }


}
