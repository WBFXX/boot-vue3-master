package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Follower;
import com.example.springboot.mapper.FollowerMapper;
import com.example.springboot.service.IFollowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 计科1901武泊帆
 * @since 2023-04-27
 */
@Service
public class FollowerServiceImpl extends ServiceImpl<FollowerMapper, Follower> implements IFollowerService {

@Autowired
FollowerMapper followerMapper;

    @Override
    public int countFollow(int userId) {
        //构造查询条件，user_id = userId
        QueryWrapper<Follower> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        //调用selectCount方法，返回记录数
        return Math.toIntExact(followerMapper.selectCount(wrapper));
    }


    @Override
    public int countFans(int userId) {
        //构造查询条件，follower_id = userId
        QueryWrapper<Follower> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", userId);
        //调用selectCount方法，返回记录数
        return Math.toIntExact(followerMapper.selectCount(wrapper));
    }
}
