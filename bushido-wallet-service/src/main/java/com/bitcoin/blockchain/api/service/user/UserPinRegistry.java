package com.bitcoin.blockchain.api.service.user;

import com.bitcoin.blockchain.api.domain.UserPin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-12-17.
 */
public class UserPinRegistry {

    private List<UserPin> userPins;

    public UserPinRegistry() {

        userPins = new ArrayList<UserPin>();
    }

    public boolean isRegistered(UserPin pin) {
        for (int i = 0; i < userPins.size(); i++) {
            if (userPins.get(i).username.equals(pin.username)) {
                return true;
            }
        }
        return false;
    }

    public void add(UserPin pin) {
        if (!isRegistered(pin)) {
            userPins.add(pin);
        }
    }

    public UserPin get(String username) {
        for (int i = 0; i < userPins.size(); i++) {
            if (userPins.get(i).username.equals(username)) {
                return userPins.get(i);
            }
        }
        return null;
    }
}
