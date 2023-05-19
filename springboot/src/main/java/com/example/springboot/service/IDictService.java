package com.example.springboot.service;

import com.example.springboot.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 武泊帆
 * @since 2023-02-02
 */
public interface IDictService extends IService<Dict> {

    List<Dict> findIcons();
}
