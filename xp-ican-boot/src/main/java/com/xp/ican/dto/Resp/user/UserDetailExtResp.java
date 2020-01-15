package com.xp.ican.dto.Resp.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserDetailExtResp implements Serializable {

    private static final long serialVersionUID=-12345345634L;

    private String userName;

    private String nickName;

    private String userFace;

    private String telephone;

    private String mail;

    private String remark;

    private Date create_time;

    private List<Long> roles;

    private List<String> permissions;

}
