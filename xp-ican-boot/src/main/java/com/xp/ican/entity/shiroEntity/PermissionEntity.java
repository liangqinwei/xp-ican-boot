package com.xp.ican.entity.shiroEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "permission")
public class PermissionEntity extends Model<PermissionEntity> {

    private static final long serialVersionUID=-1234554321L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("permission")
    private String permission;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("description")
    private String description;

    @TableField("create_time")
    private Date create_time;

    @TableField("update_time")
    private Date update_time;

}
