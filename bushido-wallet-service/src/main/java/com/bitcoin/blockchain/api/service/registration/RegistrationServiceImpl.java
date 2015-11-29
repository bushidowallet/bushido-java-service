package com.bitcoin.blockchain.api.service.registration;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.Error;
import com.bitcoin.blockchain.api.service.organization.OrganizationService;
import com.bitcoin.blockchain.api.service.user.UserService;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Jesion on 2015-06-01.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${app.trialcode}")
    private String apptrialcode;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    UserService userService;

    @Autowired
    V2WalletService walletService;

    public Response createOrganization(RegOrganization org) {
        Response reg = validateTrialCode(org.regCode);
        if (reg.getPayload().equals(true)) {
            return organizationService.create(org);
        } else {
            reg.addError(new Error(18));
        }
        return reg;
    }

    public Response createUser(RegUser user) {
        Response reg = validateTrialCode(user.regCode);
        if (reg.getPayload().equals(true)) {
            return userService.create(user.username,
                user.password,
                user.organization,
                user.email,
                user.phone,
                user.countryCode,
                user.firstName,
                user.lastName
            );
        } else {
            reg.addError(new Error(18));
        }
        return reg;
    }

    public Response createWallet(RegV2WalletDescriptor wallet) {
        Response reg = validateTrialCode(wallet.regCode);
        Response r = new Response();
        if (reg.getPayload().equals(true)) {
            UserLoginResponse w = walletService.create(wallet);
            if (w.getErrors() == null || w.getErrors().size() == 0) {
                r.setPayload(true);
            }
        } else {
            r.addError(new Error(18));
        }
        return r;
    }

    public Response validateTrialCode(String trialCode) {
        Response r = new Response();
        if (apptrialcode.equals(trialCode)) {
            r.setPayload(true);
        } else {
            r.setPayload(false);
        }
        return r;
    }
}




