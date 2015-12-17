package com.bitcoin.blockchain.api.service.user;

import com.authy.AuthyApiClient;
import com.authy.api.Hash;
import com.authy.api.Token;
import com.authy.api.Tokens;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.Error;
import com.bitcoin.blockchain.api.mail.SendGridManager;
import com.bitcoin.blockchain.api.persistence.*;
import com.bitcoin.blockchain.api.service.v2wallet.V2Wallet;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import com.bitcoin.blockchain.api.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by Jesion on 2014-12-29.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserDAO userDAO;

    @Autowired
    public UserVerificationDAO userVerificationDAO;

    @Autowired
    public VerificationTokenDAO verificationTokenDAO;

    @Autowired
    public OrganizationDAO organizationDAO;

    @Autowired
    public MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    public V2WalletDAO walletDAO;

    @Autowired
    public SaltSource saltSource;

    @Autowired
    public V2WalletService walletService;

    @Autowired
    public SendGridManager sendGrid;

    @Autowired
    public UserPinRegistry pinRegistry;

    private AuthyApiClient client;

    @Value("${app.authy.apikey}")
    private String authyapikey;

    @Value("${app.email}")
    private String email;

    @Value("${app.url}")
    private String url;

    @Value("${app.env}")
    private String env;

    @PostConstruct
    public void init() {
        client = new AuthyApiClient(authyapikey);
    }

    public UserLoginResponse login(UserLoginRequest request) {
        final UserLoginResponse operation = new UserLoginResponse();
        final UserDAOImpl.UserInfo info = checkCredentials(request.userIdOrEmail, request.credential);
        if (info.user != null) {
            final UserVerification uv = userVerificationDAO.get(info.user.getId());
            if (uv != null) {
                if (uv.hasVerification(UserVerification.UserVerificationType.emailVerified.name())) {
                    if (info.valid == true) {
                        if (info.user.has2FAEnabled == false) {
                            List<PersistedV2WalletDescriptor> persistedWallets = walletDAO.getUserWallets(info.user.username);
                            List<V2WalletDescriptor> wallets = new ArrayList<V2WalletDescriptor>();
                            for (int i = 0; i < persistedWallets.size(); i++) {
                                V2WalletDescriptor wallet = persistedWallets.get(i).toBase();
                                LoginResponse walletRes = walletService.login(wallet, request.credential);
                                wallets.add(walletRes.getPayload());
                            }
                            operation.setWallets(wallets);
                            operation.user = info.user.toBase();
                        } else {
                            User u = new User();
                            u.username = info.user.username;
                            u.has2FAEnabled = info.user.has2FAEnabled;
                            operation.user = u;
                        }
                    } else {
                        operation.addError(new Error(9));
                    }
                } else {
                    operation.addError(new Error(Error.USER_EMAIL_NOT_VERIFIED));
                }
            } else {
                operation.addError(new Error(Error.USER_NOT_VERIFIED));
            }
        } else {
            operation.addError(new Error(Error.USER_NOT_FOUND));
        }
        return operation;
    }

    public UserLoginResponse login2FA(UserLoginRequest request) {
        final UserLoginResponse operation = new UserLoginResponse();
        final PersistedUser user = userDAO.getByUserIdOrEmail(request.userIdOrEmail);
        if (checkCode(user.authProviderId, request.credential) == true) {
            List<PersistedV2WalletDescriptor> persistedWallets = walletDAO.getUserWallets(user.username);
            List<V2WalletDescriptor> wallets = new ArrayList<V2WalletDescriptor>();
            for (int i = 0; i < persistedWallets.size(); i++) {
                V2WalletDescriptor walletDesc = persistedWallets.get(i).toBase();
                V2Wallet wallet = walletService.getWallet(walletDesc.key);
                wallets.add(wallet.getDescriptor(true));
            }
            operation.setWallets(wallets);
            operation.user = user.toBase();
        } else {
            operation.addError(new Error(9));
        }
        return operation;
    }

    public Response requestToken(Token2FARequest request) {
        final Response r = new Response();
        final PersistedUser u = userDAO.get(request.username);
        if (u != null) {
            deliverToken(r, u, request.enforceSms);
        }
        return r;
    }

    private void deliverToken(Response r, PersistedUser u, boolean enforceSms) {
        final Map<String, String> o = new HashMap<String, String>();
        o.put("force", Boolean.toString(enforceSms));
        final Hash h = client.getUsers().requestSms(u.authProviderId, o);
        if (h.isSuccess()) {
            r.setPayload(true);
        }
    }

    private boolean checkCode(int authProviderUserId, String code) {
        if (code == null) {
            return false;
        }
        Tokens tokens = client.getTokens();
        Token t = tokens.verify(authProviderUserId, code);
        boolean isOk = t.isOk();
        System.out.println("2FA token is ok: " + isOk);
        return t.isOk();
    }

    private void sendAccountVerificationMessage(PersistedUser u, String message) {
        final VerificationToken t = VerificationToken.create(VerificationToken.VerificationTokenType.emailVerification, u.getId(), false);
        verificationTokenDAO.create(new PersistedVerificationToken(t));
        final StringBuilder link = new StringBuilder();
        link.append("<a href=");
        link.append(url);
        link.append("/confirm.html?");
        link.append("t=");
        link.append(t.token);
        link.append(">");
        link.append(url);
        link.append("/confirm.html?");
        link.append("t=");
        link.append(t.token);
        link.append("</a>");
        if (message == null) {
            message = "Welcome to Bushido.";
        }
        String html = "Hello!<br/><br/>" + message + " Click on the link below, in order to verify your account:<br/><br/> " + link.toString() + "<br/><br/>Bushido Team.";
        sendGrid.send(u.email, email, "Bushido Account Verification", html);
    }

    public synchronized Response resetPassword(AccountRequest request) {
        final Response r = new Response();
        final PersistedUser u = userDAO.getByEmail(request.email);
        if (u != null) {
            VerificationToken t = verificationTokenDAO.getActiveToken(u, VerificationToken.VerificationTokenType.lostPassword);
            if (t == null) {
                t = VerificationToken.create(VerificationToken.VerificationTokenType.lostPassword, u.getId(), u.has2FAEnabled);
                verificationTokenDAO.create(new PersistedVerificationToken(t));
            }
            final StringBuilder link = new StringBuilder();
            link.append("<a href=");
            link.append(url);
            link.append("/password.html?");
            link.append("t=");
            link.append(t.token);
            link.append(">");
            link.append(url);
            link.append("/password.html?");
            link.append("t=");
            link.append(t.token);
            link.append("</a>");
            String html = "Hello!<br/><br/>You have recently requested to change your Bushido password. Click on the link below, in order to begin the password change procedure:<br/><br/> " + link.toString() + "<br/><br/>Bushido Team.";
            sendGrid.send(u.email, email, "Bushido Password Reset Link", html);
        } else {
            r.addError(new Error(Error.USER_BY_EMAIL_NOT_FOUND));
        }
        return r;
    }

    public Response requestVerificationToken2FACode(SecondFactorCodeDeliveryRequest request) {
        final Response r = new Response();
        final PersistedVerificationToken t = verificationTokenDAO.get(request.token);
        if (t != null) {
            if (t.hasExpired() == false) {
                if (t.secondFactorRequired == true) {
                    final PersistedUser u = userDAO.getById(t.userId);
                    if (u != null) {
                        deliverToken(r, u, request.enforceSms);
                    } else {
                        r.addError(new Error(Error.USER_BY_ID_NOT_FOUND));
                    }
                } else {
                    r.addError(new Error(Error.SECOND_FACTOR_NOT_REQUIRED));
                }
            } else {
                r.addError(new Error(Error.TOKEN_EXPIRED));
            }
        } else {
            r.addError(new Error(Error.TOKEN_NOT_FOUND));
        }
        return r;
    }

    public Response initAccountVerify(AccountRequest request) {
        final Response r = new Response();
        final PersistedUser u = userDAO.getByEmail(request.email);
        if (u != null) {
            if (userVerificationDAO.isVerified(u.getId(), UserVerification.UserVerificationType.emailVerified.name()) == false) {
                sendAccountVerificationMessage(u, "You have recently requested an Account Verification link to be sent over.");
                r.setPayload(true);
            } else {
                r.addError(new Error(Error.USER_ALREADY_VERIFIED));
            }
        } else {
            r.addError(new Error(Error.USER_BY_EMAIL_NOT_FOUND));
        }
        return r;
    }

    public Response accountVerify(AccountVerifyRequest request) {
        final Response r = new Response();
        final PersistedVerificationToken t = verificationTokenDAO.get(request.token);
        if (t != null) {
            if (t.hasExpired() == false) {
                final PersistedUser u = userDAO.getById(t.userId);
                if (request.email != null && u.email.equals(request.email)) {
                    userVerificationDAO.add(u.getId(), UserVerification.UserVerificationType.emailVerified.name());
                    r.setPayload(true);
                } else {
                    r.addError(new Error(Error.EMAILS_DONT_MATCH));
                }
            } else {
                r.addError(new Error(Error.TOKEN_EXPIRED));
            }
        } else {
            r.addError(new Error(Error.TOKEN_NOT_FOUND));
        }
        return r;
    }

    public Response verifyResetPasswordCode(SecondFactorTokenRequest request) {
        final Response r = new Response();
        final PersistedVerificationToken t = verificationTokenDAO.get(request.token);
        if (t != null) {
            if (t.hasExpired() == false) {
                if (t.secondFactorRequired == true) {
                    final PersistedUser u = userDAO.getById(t.userId);
                    if (u != null) {
                        if (checkCode(u.authProviderId, request.code) == true) {
                            verificationTokenDAO.secondFactorComplete(t);
                            r.setPayload(true);
                        } else {
                            r.addError(new Error(Error.SECOND_FACTOR_FAILED));
                        }
                    } else {
                        r.addError(new Error(Error.USER_BY_ID_NOT_FOUND));
                    }
                } else {
                    r.addError(new Error(Error.SECOND_FACTOR_NOT_REQUIRED));
                }
            } else {
                r.addError(new Error(Error.TOKEN_EXPIRED));
            }
        } else {
            r.addError(new Error(Error.TOKEN_NOT_FOUND));
        }
        return r;
    }

    public Response resetPasswordInit(TokenRequest request) {
        final Response r = new Response();
        final VerificationToken t = verificationTokenDAO.get(request.token);
        if (t != null) {
            if (t.hasExpired() == false) {
                final PersistedUser u = userDAO.getById(t.userId);
                if (u != null) {
                    r.setPayload(u.has2FAEnabled);
                } else {
                    r.addError(new Error(Error.TOKEN_USER_NOT_FOUND));
                }
            } else {
                r.addError(new Error(Error.TOKEN_EXPIRED));
            }
        } else {
            r.addError(new Error(Error.TOKEN_NOT_FOUND));
        }
        return r;
    }

    public Response confirmResetPassword(PasswordResetConfirmationRequest request) {
        final Response r = new Response();
        final VerificationToken t = verificationTokenDAO.get(request.token);
        if (t != null) {
            if (t.hasExpired() == false) {
                final PersistedUser u = userDAO.getById(t.userId);
                if (u != null) {
                    if (u.has2FAEnabled == false) {
                        updatePassword(u, request.password, r);
                    } else {
                        if (t.secondFactorComplete == true) {
                            updatePassword(u, request.password, r);
                        } else {
                            r.addError(new Error(Error.SECOND_FACTOR_REQUIRED_NOT_COMPLETE));
                        }
                    }
                } else {
                    r.addError(new Error(Error.TOKEN_USER_NOT_FOUND));
                }
            } else {
                r.addError(new Error(Error.TOKEN_EXPIRED));
            }
        } else {
            r.addError(new Error(Error.TOKEN_NOT_FOUND));
        }
        return r;
    }

    private Response updatePassword(PersistedUser user, String password, Response response) {
        userDAO.updatePassword(user, password);
        response.setPayload(true);
        return response;
    }

    private UserDAOImpl.UserInfo checkCredentials(String userIdOrEmail, String password) {
        return userDAO.isValid(userIdOrEmail, password);
    }

    /**
     * REGISTRATION
     *
     * @param username
     * @param password
     * @param organization
     * @param email
     * @return
     */
    public Response create(String username, String password, String organization, String email, String phone, String countryCode, String firstName, String lastName) {
        Response op = new Response();
        if (userDAO.hasUser(username) == false) {
            if (userDAO.hasUserWithEmail(email) == false) {
                if (organizationDAO.hasOrganization(organization) == true) {
                    PersistedUser user = new PersistedUser(username, password, Arrays.asList("ROLE_USER"), organization, email, phone, countryCode);
                    user.firstName = firstName;
                    user.lastName = lastName;
                    createPasswordHash(user);
                    com.authy.api.User authyUser = client.getUsers().createUser(user.email, user.phone, user.countryCode);
                    user.authProviderId = authyUser.getId();
                    userDAO.create(user);
                    op.setPayload(true);
                    sendAccountVerificationMessage(user, null);
                } else {
                    op.addError(new Error(11));
                }
            } else {
                op.addError(new Error(Error.USER_BY_EMAIL_FOUND));
            }
        } else {
            op.addError(new Error(7));
        }
        return op;
    }

    public Response setPin(UserPin pin) {
        final Response op = new Response();
        if (userDAO.hasUser(pin.username) == false) {
            if (pinRegistry.isRegistered(pin) == false) {
                pinRegistry.add(pin);
            } else {
                op.addError(new Error(Error.PIN_ALREADY_SET));
            }
        } else {
            op.addError(new Error(Error.USER_NOT_FOUND));
        }
        return op;
    }

    /**
     * REST
     *
     * @param user
     * @return
     */
    public Response create(User user) {
        Response op = new Response();
        if (userDAO.hasUser(user.username) == false) {
            if (userDAO.hasUserWithEmail(user.email) == false) {
                createPasswordHash(user);
                user.roles = Arrays.asList("ROLE_USER");
                user.has2FAEnabled = false;
                PersistedUser u = new PersistedUser(user);
                com.authy.api.User authyUser = client.getUsers().createUser(u.email, u.phone, u.countryCode);
                u.authProviderId = authyUser.getId();
                userDAO.create(u);
                sendAccountVerificationMessage(u, null);
                op.setPayload(true);
            } else {
                op.addError(new Error(Error.USER_BY_EMAIL_FOUND));
            }
        } else {
            op.addError(new Error(7));
        }
        return op;
    }

    public Response getAll() {
        List<PersistedUser> all = userDAO.getAll();
        List<User> allBase = new ArrayList<User>();
        for (PersistedUser user : all) {
            allBase.add(user.toBase());
        }
        return new Response(all);
    }

    public void delete(User user) {
        userDAO.delete(new PersistedUser(user));
    }

    private void createPasswordHash(User user) {
        user.passwordHash = passwordEncoder.encodePassword(user.password, saltSource.getSalt(UserUtil.toUser(user)));
        user.password = null;
    }
}
