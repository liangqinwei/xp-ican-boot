package com.xp.ican.dto.Req;

import com.xp.ican.common.retention.MyDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
