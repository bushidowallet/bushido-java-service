package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.service.registration.RegistrationService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Jesion on 2015-06-01.
 */
@Path("/registration")
@Api(value="/registration", description = "Registration management")
@Produces({"application/json"})
@RestController
public class RegistrationController {

    @Autowired
    RegistrationService service;

    @POST
    @Path("/organization")
    @ApiOperation(value="Handle client registration, create organization", notes="Handles client registration. Creates organization", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @RequestMapping(value = "/api/v2/registration/organization", method = RequestMethod.POST)
    @ResponseBody
    public Response createOrganization(@ApiParam(value = "Organization object", required = true) @RequestBody @Valid RegOrganization reg) {
        return service.createOrganization(reg);
    }

    @POST
    @Path("/user")
    @ApiOperation(value="Handle client registration, creates a user", notes="Handles client registration, creates a user", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @RequestMapping(value = "/api/v2/registration/user", method = RequestMethod.POST)
    @ResponseBody
    public Response createUser(@ApiParam(value = "User object", required = true) @RequestBody @Valid RegUser user) {
        return service.createUser(user);
    }

    @POST
    @Path("/pin")
    @ApiOperation(value="Handle client registration, creates a PIN", notes="Handles client registration, creates a PIN", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @RequestMapping(value = "/api/v2/registration/pin", method = RequestMethod.POST)
    @ResponseBody
    public Response setPin(@ApiParam(value = "PIN object", required = true) @RequestBody @Valid RegUserPin pin) {
        return service.setPin(pin);
    }

    @POST
    @Path("/wallet")
    @ApiOperation(value="Handle client registration, creates a wallet", notes="Handles client registration, creates a wallet", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @RequestMapping(value = "/api/v2/registration/wallet", method = RequestMethod.POST)
    @ResponseBody
    public Response createWallet(@ApiParam(value = "Wallet descriptor", required = true) @RequestBody @Valid RegV2WalletDescriptor wallet) {
        return service.createWallet(wallet);
    }
}
