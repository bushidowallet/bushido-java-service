package com.bitcoin.blockchain.api.service.user;

import com.bitcoin.blockchain.api.domain.*;

/**
 * Created by Jesion on 2014-12-29.
 */
public interface UserService {

    public UserLoginResponse login(UserLoginRequest request);

    public UserLoginResponse login2FA(UserLoginRequest request);

    public Response requestToken(Token2FARequest request);

    public Response create(String username, String password, String organization, String email, String phone, String countryCode, String firstName, String lastName);

    public Response create(User user);

    public Response createPin(UserPin pin);

    public Response getAll();

    public void delete(User user);

    public Response resetPassword(AccountRequest request);

    public Response confirmResetPassword(PasswordResetConfirmationRequest request);

    public Response resetPasswordInit(TokenRequest request);

    public Response verifyResetPasswordCode(SecondFactorTokenRequest request);

    public Response requestVerificationToken2FACode(SecondFactorCodeDeliveryRequest request);

    public Response accountVerify(AccountVerifyRequest request);

    public Response initAccountVerify(AccountRequest request);
}
