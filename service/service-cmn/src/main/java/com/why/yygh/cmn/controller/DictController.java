package com.why.yygh.cmn.controller;

import com.why.yygh.cmn.service.DictService;
import com.why.yygh.common.result.Result;
import com.why.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("数据字典接口")
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;


    //根据数据id查询子数据列表
    @GetMapping("findChildData/{id}")
    @ApiOperation(value = "根据数据id查询子数据列表")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);


    }

}
