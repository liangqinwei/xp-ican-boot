package com.xp.ican.dto.Req.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserReq {

    @NotBlank(message = "name不能为空")
    private String name;

    @NotBlank(message = "description不能为空")
    private String pwd;

}
