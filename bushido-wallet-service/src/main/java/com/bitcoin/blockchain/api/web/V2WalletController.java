package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import com.bitcoin.blockchain.api.util.SecurityUtil;
import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Jesion on 2015-01-13.
 */
@Path("/wallet")
@Api(value="/wallet", description = "Operations on wallets")
@Produces({"application/json"})
@RestController
public class V2WalletController {

    @Autowired
    public V2WalletService service;

    @POST
    @Path("/auth")
    @ApiOperation(value="Login to wallet", notes="Checks whether user has permissions to access wallet", response=LoginResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/auth", method = RequestMethod.POST)
    public LoginResponse login(@ApiParam(value = "Wallet descriptor", required = true) @RequestBody @Valid LoginRequest request) {
        LoginResponse response = service.login(request.descriptor, request.password);
        if (response.getPayload() != null) {
            V2WalletDescriptor desc = (V2WalletDescriptor) response.getPayload();
            SecurityUtil.process(desc);
        }
        return response;
    }

    @POST
    @Path("/")
    @ApiOperation(value="Create wallet", notes="Creates a wallet instance", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public UserLoginResponse create(@ApiParam(value = "Wallet descriptor", required = true) @RequestBody @Valid V2WalletDescriptor wallet) {
        return service.create(wallet);
    }

    @DELETE
    @Path("/")
    @ApiOperation(value="Delete wallet", notes="Deletes a wallet instance. Restricted to admins only", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet", method = RequestMethod.DELETE)
    @Secured("ROLE_ADMIN")
    public Response delete(@ApiParam(value = "Wallet descriptor", required = true) @RequestBody @Valid V2WalletDescriptor wallet) {
        return service.delete(wallet);
    }

    @GET
    @Path("/")
    @ApiOperation(value="List wallets", notes="Lists all wallets. Restricted to admins only", response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public List<V2WalletDescriptor> get() {
        return service.getAll();
    }

    @GET
    @Path("/{key}")
    @ApiOperation(value="Get wallet", notes="Gets a specific wallet", response=V2WalletDescriptor.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Wallet not found"),
            @ApiResponse(code = 400, message = "Bad request")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/{key}", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public V2WalletDescriptor get(@ApiParam(value = "Wallet key (identifier)", required = true) @PathParam("key") @PathVariable("key") String key,
                                     @ApiParam(access = "internal") @RequestBody String body,
                                     @ApiParam(access = "internal") HttpServletRequest request,
                                     @ApiParam(access = "internal") HttpServletResponse response) {
        if (key != null && key.length() > 0) {
            V2WalletDescriptor wallet = service.get(key);
            if (wallet != null) {
                return SecurityUtil.process(wallet);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    @POST
    @Path("/topup/init")
    @ApiOperation(value="Initialize top-up process", notes="Initializes top-up process", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/topup/init", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Response initTopUp(@ApiParam(value = "Wallet descriptor", required = true) @RequestBody @Valid V2WalletDescriptor wallet) {
        String control = service.initTopUp(wallet);
        return new Response(control);
    }

    @GET
    @Path("/{key}/spend/all/{address}/generate")
    @ApiOperation(value="Generate Tx spending all UTXOs", notes="Generates Tx spending all UTXOs", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad request")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/{key}/spend/all/{address}/generate", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response spendAllUTXOGenerate(@ApiParam(value = "Wallet key (identifier)", required = true) @PathParam("key") @PathVariable("key") String key,
                                 @ApiParam(value = "Receiving address", required = true) @PathParam("address") @PathVariable("address") String address,
                                 @ApiParam(access = "internal") @RequestBody String body,
                                 @ApiParam(access = "internal") HttpServletRequest request,
                                 @ApiParam(access = "internal") HttpServletResponse response) throws Exception {
        if (key != null && key.length() > 0) {
            return service.spendAllUTXO(key, address);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    @GET
    @Path("/{key}/spend/all/{address}/push")
    @ApiOperation(value="Push Tx spending all UTXOs", notes="Pushes Tx spending all UTXOs", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad request")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/{key}/spend/all/{address}/push", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response spendAllUTXOPush(@ApiParam(value = "Wallet key (identifier)", required = true) @PathParam("key") @PathVariable("key") String key,
                                         @ApiParam(value = "Receiving address", required = true) @PathParam("address") @PathVariable("address") String address,
                                         @ApiParam(access = "internal") @RequestBody String body,
                                         @ApiParam(access = "internal") HttpServletRequest request,
                                         @ApiParam(access = "internal") HttpServletResponse response) throws Exception {
        if (key != null && key.length() > 0) {
            return service.spendAllUTXOPush(key, address);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    @GET
    @Path("/keys")
    @ApiOperation(value="Get wallet keys", notes="Gets public keys.", response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/keys", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response getKeys(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key,
                            @ApiParam(value = "Account", required = true) @QueryParam("account") @RequestParam(value="account") int account) {
        return service.getKeys(key, account);
    }

    @GET
    @Path("/transactions/unspentoutputs")
    @ApiOperation(value="Get wallet's unspent outputs", notes="Gets wallet's unspent outputs.", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/unspentoutputs", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response getUnspentOutputs(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key) {
        return service.getUnspentOutputs(key);
    }

    @GET
    @Path("/transactions/keys")
    @ApiOperation(value="Get transactions keys", notes="Gets public keys used on transactions", response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/keys", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response getTransactionsKeys(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key,
                            @ApiParam(value = "Account", required = true) @QueryParam("account") @RequestParam(value="account") int account) {
        return service.getTransactionsKeys(key, account);
    }

    @GET
    @Path("/transactions/keys/dt")
    @ApiOperation(value="Get transactions keys for DataTables", notes="Gets public keys used on transactions in a format that is required for DataTables component", response=TransactionsKeysDTDataProvider.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/keys/dt", method = RequestMethod.GET)
    /*Temp disabled, DataTables lacking support*/
    /*@Secured("ROLE_USER")*/
    public TransactionsKeysDTDataProvider getTransactionsKeysDt(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key,
                                        @ApiParam(value = "Account", required = true) @QueryParam("account") @RequestParam(value="account") int account) {

        final Response data = service.getTransactionsKeys(key, account);
        final List<V2Key> keys = (List<V2Key>) data.getPayload();
        return new TransactionsKeysDTDataProvider(keys, keys != null ? keys.size() : 0, keys != null ? keys.size() : 0, 1);
    }

    @POST
    @Path("/{key}")
    @ApiOperation(value="Add account to wallet", notes="Adds account to wallet. Restricted to admins only", response=Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/{key}", method = RequestMethod.POST)
    @Secured("ROLE_USER")
    public Response addAccount(@ApiParam(value = "Wallet key (identifier)", required = true) @PathParam("key") @PathVariable("key") String key,
                                  @ApiParam(value = "Account object", required = true) @RequestBody @Valid V2WalletAccount account,
                                  @ApiParam(access = "internal") HttpServletRequest request,
                                  @ApiParam(access = "internal") HttpServletResponse response) {
        //TODO: In service layer, check if user has permission to modify this wallet
        //TODO: Check if account id is correct
        final Response r = service.addAccount(key, account);
        if (r.getPayload() != null) {
            V2WalletDescriptor wallet = (V2WalletDescriptor) r.getPayload();
            r.setPayload(SecurityUtil.process(wallet));
        }
        return r;
    }

    @GET
    @Path("/transactions/outgoing")
    @ApiOperation(value="Get outgoing wallet transactions", notes="Gets outgoing transactions (spending outputs) with current statuses", response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/outgoing", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response getOutgoingTransactions(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key) {
        return service.getSpendingTransactions(key);
    }

    @GET
    @Path("/transactions/outgoing/dt")
    @ApiOperation(value="Get outgoing wallet transactions for DataTables", notes="Gets outgoing transactions (spending outputs) with current statuses in a format that is required for DataTables component", response=TransactionsDTDataProvider.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/outgoing/dt", method = RequestMethod.GET)
    /*@Secured("ROLE_USER")*/
    public TransactionsDTDataProvider getOutgoingTransactionsDt(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key) {
        final Response data = service.getSpendingTransactions(key);
        final List<Transaction> transactions = (List<Transaction>) data.getPayload();
        return new TransactionsDTDataProvider(transactions, transactions != null ? transactions.size() : 0, transactions != null ? transactions.size() : 0, 1);
    }

    @GET
    @Path("/transactions")
    @ApiOperation(value="Get wallet transactions", notes="Gets transactions with current statuses", response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public Response getTransactions(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key,
                                    @ApiParam(value = "Account", required = true) @QueryParam("account") @RequestParam(value="account") int account) {
        return service.getTransactions(key, account);
    }

    @GET
    @Path("/transactions/dt")
    @ApiOperation(value="Get wallet transactions for DataTables", notes="Gets transactions with current statuses in a format that is required for DataTables component", response=TransactionsDTDataProvider.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @ResponseBody
    @RequestMapping(value = "/api/v2/wallet/transactions/dt", method = RequestMethod.GET)
    /*Temp disabled, DataTables lacking support*/
    /*@Secured("ROLE_USER")*/
    public TransactionsDTDataProvider getTransactionsDt(@ApiParam(value = "Wallet key", required = true) @QueryParam("key") @RequestParam(value="key") String key,
                                                        @ApiParam(value = "Account", required = true) @QueryParam("account") @RequestParam(value="account") int account) {
        final Response data = service.getTransactions(key, account);
        final List<Transaction> transactions = (List<Transaction>) data.getPayload();
        return new TransactionsDTDataProvider(transactions, transactions != null ? transactions.size() : 0, transactions != null ? transactions.size() : 0, 1);
    }

    private class TransactionsDTDataProvider extends DTDataProvider {

        public List<Transaction> data;

        public TransactionsDTDataProvider(List<Transaction> data, int recordsTotal, int recordsFiltered, int draw) {
            super(recordsTotal, recordsFiltered, draw);
            this.data = data;
        }

        public TransactionsDTDataProvider() {
            super();
        }
    }

    private class TransactionsKeysDTDataProvider extends DTDataProvider {

        public List<V2Key> data;

        public TransactionsKeysDTDataProvider(List<V2Key> data, int recordsTotal, int recordsFiltered, int draw) {
            super(recordsTotal, recordsFiltered, draw);
            this.data = data;
        }

        public TransactionsKeysDTDataProvider() {

        }
    }

    private abstract class DTDataProvider {

        public int draw;
        public int recordsTotal;
        public int recordsFiltered;

        public DTDataProvider(int recordsTotal, int recordsFiltered, int draw) {
            this.recordsTotal = recordsTotal;
            this.recordsFiltered = recordsFiltered;
            this.draw = draw;
        }

        public DTDataProvider() {

        }
    }
}
