package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Dynamic;
import com.example.springboot.entity.Follower;
import com.example.springboot.entity.Messages;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.DynamicMapper;
import com.example.springboot.mapper.FollowerMapper;
import com.example.springboot.mapper.MessagesMapper;
import com.example.springboot.service.IDynamicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.SessionUtils;
import com.example.springboot.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ikun
 * @since 2023-02-28
 */
@Service
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements IDynamicService {
    @Autowired
    private BaseMapper<Dynamic> baseMapper;

    @Resource
    DynamicMapper dynamicMapper;

    // 更新浏览量+1
    @Override
    public void updateView(Integer id) {
        dynamicMapper.updateView(id);
    }

    @Override
    public List<Dynamic> selectList(QueryWrapper<Dynamic> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 发送消息通知
     * @param dynamicId
     * @param opr
     */
    public static void createMessage(Integer dynamicId, String opr) {
        DynamicMapper dMapper = SpringContextUtil.getBean(DynamicMapper.class);
        Dynamic dynamic = dMapper.selectById(dynamicId);
        // 拼装content
        User user = SessionUtils.getUser();
        String content = user.getName() + opr + "了你的动态" + " <a target='_blank' href='/front/detail?id=" + dynamicId + "'>" + dynamic.getName() + "</a>";
        MessagesMapper messagesMapper = SpringContextUtil.getBean(MessagesMapper.class);
        Messages messages = new Messages();
        messages.setContent(content);
        messages.setUserId(dynamic.getUserId());  // 动态的主人的id
        messages.setTime(DateUtil.now());
        messagesMapper.insert(messages);
    }
    public static void createFellowMessage(Integer userId, String opr) {
        DynamicMapper dMapper = SpringContextUtil.getBean(DynamicMapper.class);
        Dynamic dynamic = dMapper.selectById(userId);
        // 拼装content
        User user = SessionUtils.getUser();
        String content = user.getName() + opr + "了你的动态" + "</a>";
        MessagesMapper messagesMapper = SpringContextUtil.getBean(MessagesMapper.class);
        Messages messages = new Messages();
        messages.setContent(content);
        messages.setUserId(user.getId());  // 动态的主人的id
        messages.setTime(DateUtil.now());
        messagesMapper.insert(messages);
    }
}
