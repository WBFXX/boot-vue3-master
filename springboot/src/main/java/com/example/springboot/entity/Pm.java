package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.example.springboot.common.LDTConfig;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
* <p>
* 
* </p>
*
* @author 计科1901武泊帆
* @since 2023-05-04
*/
@Getter
@Setter
@ApiModel(value = "Pm对象", description = "")
public class Pm implements Serializable {

private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 发送者
    @ApiModelProperty("发送者")
    @Alias("发送者")
    private Integer fromId;

    // 接收者
    @ApiModelProperty("接收者")
    @Alias("接收者")
    private Integer toId;

    // 聊天内容
    @ApiModelProperty("聊天内容")
    @Alias("聊天内容")
    private String content;

    // 发送时间
    @ApiModelProperty("发送时间")
    @Alias("发送时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LDTConfig.CmzLdtDeSerializer.class)
    @JsonSerialize(using = LDTConfig.CmzLdtSerializer.class)
    private LocalDateTime createTime;
//
//    @TableField(exist = false)  // 数据库不存在这个字段
//    private User User;
}