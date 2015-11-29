package com.bitcoin.blockchain.api.web;

import com.bitcoin.blockchain.api.core.bip32.Hash;
import com.bitcoin.blockchain.api.domain.TopUpNotification;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import com.bitcoin.blockchain.api.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jesion on 2015-10-23.
 */
@Controller
public class DotpayController {

    @Autowired
    public V2WalletService service;

    @Value("${app.dotpay.pin}")
    private String pin;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/dotpay/notify", method= RequestMethod.POST)
    @ResponseBody
    public String receive(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("Processing notification request: " + request.toString());
            final Map<String, String[]> params = request.getParameterMap();
            if (params != null && params.size() > 0) {
                final String sigStringReceived = getValue(params.get("signature"), "signature");
                final String id = getValue(params.get("id"), "id");
                final String operationNumber = getValue(params.get("operation_number"), "operation_number");
                final String operationType = getValue(params.get("operation_type"), "operation_type");
                final String operationStatus = getValue(params.get("operation_status"), "operation_status");
                final String operationAmount = getValue(params.get("operation_amount"), "operation_amount");
                final String operationCurrency =  getValue(params.get("operation_currency"), "operation_currency");
                final String operationWithdrawalAmount = getValue(params.get("operation_withdrawal_amount"), "operation_withdrawal_amount");
                final String operationCommissionAmount = getValue(params.get("operation_commission_amount"), "operation_commission_amount");
                final String operationOriginalAmount = getValue(params.get("operation_original_amount"), "operation_original_amount");
                final String operationOriginalCurrency = getValue(params.get("operation_original_currency"), "operation_original_currency");
                final String operationDateTime = getValue(params.get("operation_datetime"), "operation_datetime");
                final String operationRelatedNumber = getValue(params.get("operation_related_number"), "operation_related_number");
                final String control = getValue(params.get("control"), "control");
                final String description = getValue(params.get("description"), "description");
                final String email = getValue(params.get("email"), "email");
                final String pInfo = getValue(params.get("p_info"), "p_info");
                final String pEmail = getValue(params.get("p_email"), "p_email");
                final String channel = getValue(params.get("channel"), "channel");
                final String channelCountry = getValue(params.get("channel_country"), "channel_country");
                final String geoipCountry = getValue(params.get("geoip_country"), "geoip_country");
                final StringBuilder sigInputBuilder = new StringBuilder();
                sigInputBuilder.append(pin);
                sigInputBuilder.append(id);
                sigInputBuilder.append(operationNumber);
                sigInputBuilder.append(operationType);
                sigInputBuilder.append(operationStatus);
                sigInputBuilder.append(operationAmount);
                sigInputBuilder.append(operationCurrency);
                if (operationWithdrawalAmount != null) {
                    sigInputBuilder.append(operationWithdrawalAmount);
                }
                if (operationCommissionAmount != null) {
                    sigInputBuilder.append(operationCommissionAmount);
                }
                sigInputBuilder.append(operationOriginalAmount);
                sigInputBuilder.append(operationOriginalCurrency);
                sigInputBuilder.append(operationDateTime);
                if (operationRelatedNumber != null) {
                    sigInputBuilder.append(operationRelatedNumber);
                }
                sigInputBuilder.append(control);
                sigInputBuilder.append(description);
                sigInputBuilder.append(email);
                sigInputBuilder.append(pInfo);
                sigInputBuilder.append(pEmail);
                sigInputBuilder.append(channel);
                if (channelCountry != null) {
                    sigInputBuilder.append(channelCountry);
                }
                if (geoipCountry != null) {
                    sigInputBuilder.append(geoipCountry);
                }
                log.info("Constructed sig base: " + sigInputBuilder.toString());
                final String sigStringGenerated = ByteUtil.toHex(new Hash(sigInputBuilder.toString()).sha256());
                log.info("Generating sig: " + sigStringGenerated);
                if (sigStringGenerated != null && sigStringGenerated.equals(sigStringReceived) == true) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
                    Date date = null;
                    try {
                        date = df.parse(operationDateTime);
                    } catch (ParseException e) {

                    }
                    service.handleTopUpNotification(new TopUpNotification(
                            Integer.parseInt(id),
                            operationNumber,
                            operationStatus,
                            operationType,
                            new BigDecimal(operationAmount),
                            operationCurrency,
                            operationWithdrawalAmount != null ? new BigDecimal(operationWithdrawalAmount) : BigDecimal.ZERO,
                            operationCommissionAmount != null ? new BigDecimal(operationCommissionAmount) : BigDecimal.ZERO,
                            new BigDecimal(operationOriginalAmount),
                            operationOriginalCurrency,
                            date,
                            operationRelatedNumber,
                            control,
                            description,
                            email,
                            pInfo,
                            pEmail,
                            channel,
                            channelCountry,
                            geoipCountry,
                            sigStringGenerated
                    ));
                    return "OK";
                } else {
                    log.error("Failed to match signature received: " + sigStringReceived + " with generated one: " + sigStringGenerated);
                }
            } else {
                log.error("Parameters map was null or had no entries: " + params);
            }
        } catch (Exception e) {
            log.error("Error while handling dotpay notification: " + e.toString());
        }
        return null;
    }

    private String getValue(String[] param, String name) {
        if (param != null) {
            if (param.length > 0) {
                log.info("Decoded param: " + name + " with value " + param[0]);
                return param[0];
            }
        }
        log.error("Unable to decode param: " + name);
        return null;
    }
}
