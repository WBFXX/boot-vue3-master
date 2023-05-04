package com.example.springboot.service.impl;

import com.example.springboot.entity.Pm;
import com.example.springboot.mapper.PmMapper;
import com.example.springboot.service.IPmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 计科1901武泊帆
 * @since 2023-05-04
 */
@Service
public class PmServiceImpl extends ServiceImpl<PmMapper, Pm> implements IPmService {

}
