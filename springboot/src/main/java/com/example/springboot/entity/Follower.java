package com.example.springboot.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;
import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.example.springboot.common.LDTConfig;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* 
* </p>
*
* @author 计科1901武泊帆
* @since 2023-04-27
*/
@Getter
@Setter
@ApiModel(value = "Follower对象", description = "")
public class Follower implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 	关注者id
    @ApiModelProperty("	关注者id")
    @Alias("	关注者id")
    private Integer userId;

    // 	被关注者id
    @ApiModelProperty("	被关注者id")
    @Alias("	被关注者id")
    private Integer followerId;

    // 删除关注
    @ApiModelProperty("删除关注")
    @Alias("删除关注")
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    // 创建时间
    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    @TableField(fill = FieldFill.INSERT)
//    @JsonDeserialize(using = LDTConfig.CmzLdtDeSerializer.class)
//    @JsonSerialize(using = LDTConfig.CmzLdtSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone= "GMT+8")
    private LocalDateTime createTime;

    @TableField(exist = false)  // 数据库不存在这个字段
    private User user;
    @TableField(exist = false)  // 数据库不存在这个字段
    private Integer fansCount;  // 评论
    @TableField(exist = false)  // 数据库不存在这个字段
    private Integer followCount;  // 评论


}