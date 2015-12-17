package com.bitcoin.blockchain.api.service.registration;

import com.bitcoin.blockchain.api.domain.*;

/**
 * Created by Jesion on 2015-06-01.
 */
public interface RegistrationService {

    Response createOrganization(RegOrganization reg);

    Response createUser(RegUser user);

    Response setPin(RegUserPin pin);

    Response createWallet(RegV2WalletDescriptor wallet);

    Response validateTrialCode(String trialCode);
}
