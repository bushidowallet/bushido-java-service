package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.domain.Organization;
import com.bitcoin.blockchain.api.domain.Response;
import com.bitcoin.blockchain.api.service.organization.OrganizationService;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.*;

/**
 * Created by Jesion on 2015-01-20.
 */
@Path("/organization")
@Api(value="/organization", description = "Operations on organizations")
@Produces({"application/json"})
@RestController
public class OrganizationController {

    @Autowired
    public OrganizationService service;

    @POST
    @Path("/")
    @ApiOperation(value="Create an organization", notes="Creates an organization. ApiKey value ignored. Restricted to admins only.", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
    })
    @RequestMapping(value = "/api/v2/organization", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public Response create(@ApiParam(value = "Organization object", required = true) @RequestBody @Valid Organization organization) {
        return service.create(organization);
    }

    @GET
    @Path("/")
    @ApiOperation(value="List organizations", notes="Lists all organizations. Restricted to admins only.", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
    })
    @RequestMapping(value = "/api/v2/organization", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public Response list() {
        return service.getAll();
    }

    @DELETE
    @Path("/")
    @ApiOperation(value="Delete an organization", notes="Deletes an organization. Restricted to admins only.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Invalid Credentials")
    })
    @RequestMapping(value = "/api/v2/organization", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public void delete(@ApiParam(value = "Organization object", required = true) @RequestBody @Valid Organization organization) {
        service.delete(organization);
    }
}

