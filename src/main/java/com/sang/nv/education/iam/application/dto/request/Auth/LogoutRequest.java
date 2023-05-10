package com.sang.nv.education.iam.application.dto.request.Auth;

import com.sang.commonmodel.dto.request.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequest extends Request {

    private String deviceId;

    private String deviceToken;

    private String refreshToken;
}
