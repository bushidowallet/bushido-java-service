package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.domain.ChainMessage;
import com.bitcoin.blockchain.api.domain.Response;
import com.bitcoin.blockchain.api.domain.Tx;
import com.bitcoin.blockchain.api.service.transaction.TransactionService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;

/**
 * Created by Jesion on 2015-03-19.
 */
@Path("/tx")
@Api(value="/tx", description = "Operations on transactions")
@Produces({"application/json"})
@RestController
public class TransactionController {

    @Autowired
    public TransactionService service;

    @POST
    @Path("/pushtx")
    @ApiOperation(value="Push Tx", notes="Pushes Tx on the wire", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/tx/pushtx", method = RequestMethod.POST)
    public Response pushtx(@ApiParam(value = "Tx hex", required = true) @RequestBody @Valid Tx tx) {

        return service.push(tx.hex);
    }

    @POST
    @Path("/notifytx")
    @ApiOperation(value="Notify Tx", notes="Notifies about Tx confirmed", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/tx/notifytx", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Response notifyConfirmed(@ApiParam(value = "Payload", required = true) @RequestBody @Valid ChainMessage tx,
                                    @ApiParam(access = "internal") HttpServletRequest request,
                                    @ApiParam(access = "internal") HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return service.notifyConfirmed(tx);
    }
}
