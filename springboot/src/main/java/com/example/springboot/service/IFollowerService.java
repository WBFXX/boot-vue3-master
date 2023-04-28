package com.example.springboot.service;

import com.example.springboot.entity.Follower;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 计科1901武泊帆
 * @since 2023-04-27
 */
public interface IFollowerService extends IService<Follower> {


    int countFollow(int userId);

    int countFans(int userId);
}
