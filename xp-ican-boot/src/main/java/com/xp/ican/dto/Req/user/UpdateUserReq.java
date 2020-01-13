package com.xp.ican.dto.Req.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateUserReq {

    @NotEmpty(message = "用户ID不能为空")
    private Long id;

    private String nickname;

    private String userface;

    private String telephone;

    private String mail;

    private String remark;

}
