package com.example.springboot.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.example.springboot.common.annotation.AutoLog;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import com.example.springboot.service.impl.DynamicServiceImpl;
import com.example.springboot.utils.SessionUtils;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.service.IPmService;
import com.example.springboot.entity.Pm;

import org.springframework.web.bind.annotation.RestController;

/**
* <p>
*  前端控制器
* </p>
*
* @author 计科1901武泊帆
* @since 2023-05-04
*/
@RestController
@RequestMapping("/pm")
public class PmController {

    @Resource
    private IPmService pmService;

    @Resource
    IUserService userService;


    // 新增私信管理
    @AutoLog("新增私信管理")
    @PostMapping
    public Result save(@RequestBody Pm pm) throws Exception {

        // 保存私信对象到数据库
        try {
            pmService.save(pm);
            DynamicServiceImpl.createPmMessage(pm.getFromId(),"私信");
        } catch (Exception e) {
            // 抛出自定义异常
            throw new Exception("发送私信失败");
        }



        // 返回成功结果
        return Result.success();
    }

    @AutoLog("编辑私信管理")
    @PutMapping
    public Result update(@RequestBody Pm pm) {
        pmService.updateById(pm);
        return Result.success();
    }

    @AutoLog("删除私信管理")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        pmService.removeById(id);
        return Result.success();
    }

    @AutoLog("批量删除私信管理")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        pmService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(pmService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {


        return Result.success(pmService.getById(id));
    }

    @SaIgnore
    @GetMapping("/{fid}/{tid}")
    public Result findPm(@PathVariable Integer fid,
                         @PathVariable Integer tid
                         ) {
        List<Pm> pmLists = pmService.list().stream().filter(pl-> (pl.getFromId().equals(fid)&&pl.getToId().equals(tid))||
                        (pl.getToId().equals(fid)&&pl.getFromId().equals(tid)))
                .collect(Collectors.toList());

        User fuser = userService.getById(fid);

        User tuser = userService.getById(tid);
// 创建一个Map对象来封装响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("pmLists", pmLists);
        data.put("fuser", fuser);
        data.put("tuser", tuser);

        // 返回成功结果和响应数据
        return Result.success(data);
    }



    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Pm> queryWrapper = new QueryWrapper<Pm>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(pmService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Pm> list = pmService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Pm信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
    * excel 导入
    * @param file
    * @throws Exception
    */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Pm> list = reader.readAll(Pm.class);

        pmService.saveBatch(list);
        return Result.success();
    }

}
