package com.xp.ican.entity.shiroEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.Date;

@Data
@TableName("user")
public class UserEntity extends Model<UserEntity> {

    private static final long serialVersionUID=-1234554321L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String userName;

    @TableField("nickname")
    private String nickName;

    @TableField("password")
    private String passWord;

    @TableField("userface")
    private String userFace;

    @TableField("telephone")
    private String telephone;

    @TableField("mail")
    @Email(message = "邮箱格式不正确")
    private String mail;

    @TableField("isDelete")
    private Integer isDelete;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private Date create_time;

    @TableField("update_time")
    private Date update_time;

}
