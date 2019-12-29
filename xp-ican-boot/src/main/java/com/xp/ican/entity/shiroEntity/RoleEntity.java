package com.xp.ican.entity.shiroEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "role")
public class RoleEntity extends Model<RoleEntity> {

    private static final long serialVersionUID = -1234554321L;//序列化版本ID

    @TableId(value = "id",type= IdType.AUTO)
    private Long id;

    @TableField("role")
    private String role;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("description")
    private String description;

    @TableField("create_time")
    private Date create_time;

    @TableField("update_time")
    private Date update_time;
}
