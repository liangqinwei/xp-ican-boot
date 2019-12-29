package com.xp.ican.entity.shiroEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "permission_role")
public class PermissionRoleRefEntity extends Model<PermissionRoleRefEntity> {

    private static final long serialVersionUID = -1234554321L;//序列化版本ID

    @TableId(value = "id",type= IdType.AUTO)
    private Long id;

    @TableField("rid")
    private Long uid;


    @TableField("pid")
    private Long rid;


    @TableField("create_time")
    private Date create_time;

    @TableField("update_time")
    private Date update_time;
}
