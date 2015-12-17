package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.service.user.UserService;
import com.bitcoin.blockchain.api.util.SecurityUtil;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2014-12-29.
 */
@Path("/user")
@Api(value="/user", description = "Operations on users")
@Produces({"application/json"})
@RestController
public class UserController {

    @Autowired
    public UserService service;

    @POST
    @Path("/auth")
    @ApiOperation(value="Authenticate user", notes="Authenticates the user and returns a list of owned wallets", response=UserLoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/auth", method = RequestMethod.POST)
    public UserLoginResponse login(@ApiParam(value = "Login descriptor", required = true) @RequestBody @Valid UserLoginRequest request) {
        UserLoginResponse response = service.login(request);
        List<V2WalletDescriptor> secureWallets = new ArrayList<V2WalletDescriptor>();
        if (response.getWallets() != null && response.getWallets().size() > 0) {
            List<V2WalletDescriptor> wallets = response.getWallets();
            for (int i = 0; i < wallets.size(); i++) {
                secureWallets.add(SecurityUtil.process(wallets.get(i)));
            }
        }
        response.setWallets(secureWallets);
        if (response.user != null) {
            SecurityUtil.process(response.user);
        }
        return response;
    }

    @POST
    @Path("/pin")
    @ApiOperation(value="Sets the PIN", notes="Sets the PIN", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @RequestMapping(value = "/api/v2/user/pin", method = RequestMethod.POST)
    @ResponseBody
    public Response setPin(@ApiParam(value = "PIN object", required = true) @RequestBody @Valid UserPin pin) {
        return service.setPin(pin);
    }

    @POST
    @Path("/auth/code")
    @ApiOperation(value="Authenticate user with 2FA code", notes="Validates user's 2FA code and returns a list of owned wallets", response=UserLoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @Secured("ROLE_USER")
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/auth/code", method = RequestMethod.POST)
    public UserLoginResponse authCode(@ApiParam(value = "Login descriptor", required = true) @RequestBody @Valid UserLoginRequest request) {
        UserLoginResponse response = service.login2FA(request);
        List<V2WalletDescriptor> secureWallets = new ArrayList<V2WalletDescriptor>();
        if (response.getWallets() != null && response.getWallets().size() > 0) {
            List<V2WalletDescriptor> wallets = response.getWallets();
            for (int i = 0; i < wallets.size(); i++) {
                secureWallets.add(SecurityUtil.process(wallets.get(i)));
            }
        }
        response.setWallets(secureWallets);
        if (response.user != null) {
            SecurityUtil.process(response.user);
        }
        return response;
    }

    @POST
    @Path("/account/verify/init")
    @ApiOperation(value="Request a verification link", notes="Requests a verification link", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/account/verify/init", method = RequestMethod.POST)
    public Response initAccountVerify(@ApiParam(value = "Account request descriptor", required = true) @RequestBody @Valid AccountRequest request) {
        return service.initAccountVerify(request);
    }

    @POST
    @Path("/account/verify")
    @ApiOperation(value="Verify user account", notes="Verifies user account", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/account/verify", method = RequestMethod.POST)
    public Response accountVerify(@ApiParam(value = "Account verification descriptor", required = true) @RequestBody @Valid AccountVerifyRequest request) {
        return service.accountVerify(request);
    }

    @POST
    @Path("/password/reset")
    @ApiOperation(value="Request Password Reset", notes="Sends a password reset link", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/password/reset", method = RequestMethod.POST)
    public Response resetPassword(@ApiParam(value = "Password reset request descriptor", required = true) @RequestBody @Valid AccountRequest request) {
        return service.resetPassword(request);
    }

    @POST
    @Path("/password/reset/init")
    @ApiOperation(value="Begin password reset operation", notes="Begins password reset operation", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/password/reset/init", method = RequestMethod.POST)
    public Response resetPasswordInit(@ApiParam(value = "Password reset request init descriptor", required = true) @RequestBody @Valid TokenRequest request) {
        return service.resetPasswordInit(request);
    }

    @POST
    @Path("/password/reset/token")
    @ApiOperation(value="Send 2FA code to verification token's user phone via either 2FA application or SMS", notes="Send 2FA code to verification token's user phone via either 2FA application or SMS", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/password/reset/token", method = RequestMethod.POST)
    public Response requestVerificationToken2FACode(@ApiParam(value = "Token request descriptor", required = true) @RequestBody @Valid SecondFactorCodeDeliveryRequest request) {
        return service.requestVerificationToken2FACode(request);
    }

    @POST
    @Path("/password/reset/code")
    @ApiOperation(value="Verify user token's 2FA code", notes="Verify user's token 2FA code", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/password/reset/code", method = RequestMethod.POST)
    public Response verifyResetPasswordCode(@ApiParam(value = "Tokens 2FA descriptor", required = true) @RequestBody @Valid SecondFactorTokenRequest request) {
        return service.verifyResetPasswordCode(request);
    }

    @POST
    @Path("/password/reset/confirm")
    @ApiOperation(value="Confirm password reset", notes="Confirms password reset", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/password/reset/confirm", method = RequestMethod.POST)
    public Response confirmResetPassword(@ApiParam(value = "Password reset confirmation descriptor", required = true) @RequestBody @Valid PasswordResetConfirmationRequest request) {
        return service.confirmResetPassword(request);
    }

    @POST
    @Path("/auth/code/token")
    @ApiOperation(value="Send auth token to user's phone phone via either 2FA application or SMS", notes="Sends auth token to user's phone via either 2FA application or SMS", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @Secured("ROLE_USER")
    @ResponseBody
    @RequestMapping(value = "/api/v2/user/auth/code/token", method = RequestMethod.POST)
    public Response requestToken(@ApiParam(value = "Token request descriptor", required = true) @RequestBody @Valid Token2FARequest request) {
        return service.requestToken(request);
    }

    @POST
    @Path("/")
    @ApiOperation(value="Create a user", notes="Creates a user object", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
            })
    @RequestMapping(value = "/api/v2/user", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public Response create(@ApiParam(value = "User object", required = true) @RequestBody @Valid User user) {
        return service.create(user);
    }

    @GET
    @Path("/")
    @ApiOperation(value="List users", notes="Lists all users", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
    })
    @RequestMapping(value = "/api/v2/user", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public Response list() {
        return service.getAll();
    }

    @DELETE
    @Path("/")
    @ApiOperation(value="Delete a user", notes="Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
    })
    @RequestMapping(value = "/api/v2/user", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public void delete(@ApiParam(value = "User object", required = true) @RequestBody @Valid User user) {
        service.delete(user);
    }
}