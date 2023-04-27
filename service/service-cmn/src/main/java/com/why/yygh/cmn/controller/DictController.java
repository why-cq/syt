package com.why.yygh.cmn.controller;

import com.why.yygh.cmn.service.DictService;
import com.why.yygh.common.result.Result;
import com.why.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api("数据字典接口")
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;


    //1.根据数据id查询子数据列表
    @GetMapping("findChildData/{id}")
    @ApiOperation(value = "根据数据id查询子数据列表")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);


    }

    //2.导出excel文档,把数据库中的字典导出到用户端
    @ApiOperation("导出excel文档")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }

    //3.导入Excel数据到数据库
    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }



}
