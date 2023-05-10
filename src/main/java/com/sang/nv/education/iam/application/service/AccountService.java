package com.sang.nv.education.iam.application.service;


import com.sang.common.captcha.dto.CaptchaDTO;
import com.sang.commonmodel.auth.UserAuthority;
import com.sang.nv.education.iam.application.dto.request.Auth.LoginRequest;
import com.sang.nv.education.iam.application.dto.request.Auth.LogoutRequest;
import com.sang.nv.education.iam.application.dto.request.Auth.RefreshTokenRequest;
import com.sang.nv.education.iam.application.dto.request.User.EmailForgotPasswordRequest;
import com.sang.nv.education.iam.application.dto.request.User.ForgotPasswordRequest;
import com.sang.nv.education.iam.application.dto.response.AuthToken;
import com.sang.nv.education.iam.domain.User;
import org.springframework.messaging.MessagingException;

public interface AccountService {

    AuthToken login(LoginRequest request);

    AuthToken loginClient(LoginRequest request);

    AuthToken refreshToken(RefreshTokenRequest request);

    User myProfile();

    String currentUser();

    UserAuthority myAuthorities();

    void logout(LogoutRequest request);

    void forgotPassword(EmailForgotPasswordRequest request) throws MessagingException, javax.mail.MessagingException;

    void resetPassword(ForgotPasswordRequest request);


    CaptchaDTO refreshCaptcha();
}
