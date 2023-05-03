package com.example.springboot.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import java.util.Comparator;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springboot.common.annotation.AutoLog;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.*;
import com.example.springboot.service.IUserService;
import com.example.springboot.service.impl.DynamicServiceImpl;
import com.example.springboot.service.impl.UserServiceImpl;
import com.example.springboot.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.service.IFollowerService;

import org.springframework.web.bind.annotation.RestController;

import static cn.dev33.satoken.util.SaResult.data;

/**
* <p>
*  前端控制器
* </p>
*
* @author 计科1901武泊帆
* @since 2023-04-27
*/
@RestController
@Slf4j
@RequestMapping("/follower")
public class FollowerController {

    @Resource
    private IFollowerService followerService;
    @Resource
    IUserService userService;

    @AutoLog("新增关注")
    @PostMapping
    public Result save(@RequestBody Follower follower) {
        // 点一次成功  再点一次取消
        User user = SessionUtils.getUser();
            follower.setUserId(user.getId());
            try {
                followerService.save(follower);
                // 加积分
                DynamicServiceImpl.createFellowMessage(follower.getUserId(), "关注");
            } catch (DuplicateKeyException e) {
                // 唯一冲突就删除
                followerService.remove(new UpdateWrapper<Follower>().eq("user_id", user.getId()).eq("follower_id", follower.getFollowerId()));
            }
            return Result.success();
        }


    @AutoLog("编辑关注列表")
    @PutMapping
    public Result update(@RequestBody Follower follower) {
        followerService.updateById(follower);
        return Result.success();
    }

    @AutoLog("删除关注列表")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        followerService.removeById(id);
        return Result.success();
    }

    @AutoLog("批量删除关注列表")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        followerService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(followerService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(followerService.getById(id));
    }

    /**
     * 关注/粉丝列表表单
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commonPage")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Follower> queryWrapper = new QueryWrapper<Follower>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(followerService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 关注最多的人
     */
    @GetMapping("/maxCount")
    @SaIgnore// 不登录就可以查询
    public Result maxCount(){
        List<Follower> followerList = followerService.list();
        List<Follower> maxCountLists = new ArrayList<>();
        List<User> userList = userService.list();
        for (Follower list : followerList) {
            int follow =  followerService.countFollow(list.getFollowerId());
            int fans = followerService.countFans(list.getFollowerId());
            list.setFansCount(fans);
            list.setFollowCount(follow);
            // 查出所有被关注用户得信息
            userList.stream().filter(user -> user.getId().equals(list.getFollowerId())).findFirst().ifPresent(list::setUser);

        }
        //使用一个HashMap来存储followerId和Follower对象的映射关系
        Map<Integer, Follower> map = new HashMap<>();
//遍历followerList
        for (Follower follower : followerList) {
            //获取当前元素的followerId
            int followerId = follower.getFollowerId();
            //如果map中已经存在该followerId，就跳过该元素
            if (map.containsKey(followerId)) {
                continue;
            }
            //否则，将该元素加入到map中
            map.put(followerId, follower);
        }
//将map转换为list
        followerList = new ArrayList<>(map.values());
//使用Comparator接口对followerList按照fansCount降序排序
        followerList.sort((o1, o2) -> o2.getFansCount() - o1.getFansCount());
//使用subList方法获取前6个元素，如果followerList的大小小于6，就返回全部元素
        maxCountLists = followerList.subList(0, Math.min(6, followerList.size()));
//返回maxCountLists作为结果
        return Result.success(maxCountLists);


    }

    /**
     * 关注列表
     * @param type
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/followPage")
    public Result findPage(@RequestParam(defaultValue = "") String type,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        List<User> userList = userService.list();
        QueryWrapper<Follower> queryWrapper = new QueryWrapper<Follower>().orderByDesc("user_id");
//        queryWrapper.like(!"".equals(name), "id", userList.stream().filter(user -> user.getName().equals(name)).collect(Collectors.toList()));
//        queryWrapper.in(!"".equals(name), "id", userList.stream().filter(user -> user.getName().equals(name)).map(User::getId).collect(Collectors.toList()));
        User currentUser = SessionUtils.getUser();  // 获取当前登录的用户信息
        if ("user".equals(type) ) {  // 如果type是user，表示筛选用户自己的数据
            queryWrapper.eq("user_id", currentUser.getId());  // select * from  dynamic where user_id = xxx
        }
        Page<Follower> page = followerService.page(new Page<>(pageNum, pageSize), queryWrapper);


        List<Follower> lists = followerService.list();
        List<Follower> filteredLists = new ArrayList<>();


        List<Follower> records = page.getRecords();

        for (Follower list : records) {
            int follow =  followerService.countFollow(list.getFollowerId());
            int fans = followerService.countFans(list.getFollowerId());
            list.setFansCount(fans);
            list.setFollowCount(follow);
            // 查出所有被关注用户得信息
            userList.stream().filter(user -> user.getId().equals(list.getFollowerId())).findFirst().ifPresent(list::setUser);
            //如果关注得人是当前登录人得id，则把他加入到list当中
            if ( currentUser != null && list.getUserId().equals(currentUser.getId())) {
                //如果 userId==currentUser.id，就把 list 添加到新的列表中
                filteredLists.add(list);
            }
        }
        page.setRecords(filteredLists);
        return Result.success(page);
    }
//        Page<Follower> page = followerService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<Praise> praiseList = praiseService.list();
//        List<Collect> collectList = collectService.list();
//        List<Comments> commentsList = commentsService.list();
//        List<Follower> fansAndFollowList = followerService.list();

//        List<Follower> records = page.getRecords();
//        List<User> userList = userService.list();
//        for (Follower record : records) {
//            // 查出用户信息
//            userList.stream().filter(user -> user.getId().equals(record.getUserId())).findFirst().ifPresent(record::setUser);
//
//            // 查出
//            int followCount = (int) fansAndFollowList.stream().filter(follow -> follow.getUserId().equals(record.getId())).count();//关注数量
//            record.setFollowCount(followCount);
//            int fansCount = (int) fansAndFollowList.stream().filter(fans -> fans.getFollowerId().equals(record.getId())).count();//粉丝数量
//            record.setFollowCount(fansCount);
//            log.info("当前查询id为:" + record.getId() + ";当前fansCount="+fansCount+";当前followCount=" + followCount);
//
//        }
//        return Result.success(page);
//    }

    /**
     * 粉丝列表
     * @param type
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/fansPage")
    public Result fansPage(@RequestParam(defaultValue = "") String type,
                           @RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        List<User> userList = userService.list();
        QueryWrapper<Follower> queryWrapper = new QueryWrapper<Follower>().orderByDesc("user_id");
        User currentUser = SessionUtils.getUser();  // 获取当前登录的用户信息
        if ("user".equals(type) ) {  // 如果type是user，表示筛选用户自己的数据
            if (currentUser != null) {
                queryWrapper.eq("follower_id", currentUser.getId());  // select * from  dynamic where user_id = xxx
            }
        }
        Page<Follower> page = followerService.page(new Page<>(pageNum, pageSize), queryWrapper);


        List<Follower> filteredLists = new ArrayList<>();


        List<Follower> records = page.getRecords();

        for (Follower list : records) {
            int follow =  followerService.countFollow(list.getUserId());
            int fans = followerService.countFans(list.getUserId());
            list.setFansCount(fans);
            list.setFollowCount(follow);
            // 查出所有粉丝得用户信息
            userList.stream().filter(user -> user.getId().equals(list.getUserId())).findFirst().ifPresent(list::setUser);
            //如果被关注得人是当前登录人得id，则把他加入到list当中
            if ( currentUser != null && list.getFollowerId().equals(currentUser.getId())) {
                //如果 userId==currentUser.id，就把 list 添加到新的列表中
                filteredLists.add(list);
            }
        }
        //把和我相关得数据放到page中
        page.setRecords(filteredLists);

        return Result.success(page);
    }

    /**
     * 粉丝/关注数量
     * @param type
     * @return
     */
    @GetMapping("/count")
    public Result count(@RequestParam(defaultValue = "") String type) {

        QueryWrapper<Follower> queryWrapper = new QueryWrapper<Follower>().orderByDesc("user_id");
        User currentUser = SessionUtils.getUser();  // 获取当前登录的用户信息
        if ("user".equals(type) ) {  // 如果type是user，表示筛选用户自己的数据
            if (currentUser != null) {
                queryWrapper.eq("user_id", currentUser.getId());  // select * from  dynamic where user_id = xxx
            }
        }
        List<Follower> lists = followerService.list();
        List<Follower> filteredLists = new ArrayList<>();
        List<User> userList = userService.list();

        for (Follower list : lists) {
            int follow =  followerService.countFollow(list.getUserId());
            int fans = followerService.countFans(list.getUserId());
            list.setFansCount(fans);
            list.setFollowCount(follow);
            // 查出用户信息
            userList.stream().filter(user -> user.getId().equals(list.getFollowerId())).findFirst().ifPresent(list::setUser);

            if ( currentUser != null && list.getUserId().equals(currentUser.getId())) {
                //如果 userId==currentUser.id，就把 list 添加到新的列表中
                filteredLists.add(list);
            }
        }

        return Result.success(filteredLists.isEmpty()? lists:filteredLists);
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Follower> list = followerService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Follower信息表", "UTF-8");
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
        List<Follower> list = reader.readAll(Follower.class);

        followerService.saveBatch(list);
        return Result.success();
    }

}
