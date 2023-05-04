package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.*;
import com.example.springboot.mapper.DynamicMapper;
import com.example.springboot.mapper.FollowerMapper;
import com.example.springboot.mapper.MessagesMapper;
import com.example.springboot.mapper.PmMapper;
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
        // 拼装content
        User user = SessionUtils.getUser();
        String content = user.getName() + opr + "了你" + "</a>";
        MessagesMapper messagesMapper = SpringContextUtil.getBean(MessagesMapper.class);
        Messages messages = new Messages();
        messages.setContent(content);
        messages.setUserId(userId);  // 动态的主人的id
        messages.setTime(DateUtil.now());
        messagesMapper.insert(messages);
    }

    public static void createPmMessage(Integer userId, String opr) {
        // 拼装content
        User user = SessionUtils.getUser();
        String content = user.getName() + opr + "了你" + "</a>";
        MessagesMapper messagesMapper = SpringContextUtil.getBean(MessagesMapper.class);
        Messages messages = new Messages();
        messages.setContent(content);
        messages.setUserId(userId);  // 被私信人的id
        messages.setTime(DateUtil.now());
        messagesMapper.insert(messages);
    }


//    public static void createPmMessage(Integer userId, String opr) {
//        PmMapper pmMapper = SpringContextUtil.getBean(PmMapper.class);
//
//
//        Pm pm = pmMapper.lambdaQuery().eq(Pm::getFromId, userId).one();
//        // 拼装content
//        User user = SessionUtils.getUser();
//        String content = user.getName() + opr + "了你" + "</a>";
//        MessagesMapper messagesMapper = SpringContextUtil.getBean(MessagesMapper.class);
//        Messages messages = new Messages();
//        messages.setContent(content);
//        messages.setUserId(user.getId());  // 回复的主人的id
//        messages.setTime(DateUtil.now());
//        messagesMapper.insert(messages);
//    }


}
