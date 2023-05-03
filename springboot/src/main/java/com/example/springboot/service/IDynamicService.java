package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ikun
 * @since 2023-02-28
 */
public interface IDynamicService extends IService<Dynamic> {

    void updateView(Integer id);

    List<Dynamic> selectList(QueryWrapper<Dynamic> queryWrapper);
}
