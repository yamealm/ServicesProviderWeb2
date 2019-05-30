package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.ejbs.PreferencesEJB;
import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.models.Account;
import com.alodiga.services.provider.commons.models.AccountBalance;
import com.alodiga.services.provider.commons.models.AccountHasProfile;
import com.alodiga.services.provider.commons.models.AccountLimitTopUp;
import com.alodiga.services.provider.commons.models.Address;
import com.alodiga.services.provider.commons.models.City;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.County;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Period;
import com.alodiga.services.provider.commons.models.PreferenceFieldEnum;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.models.TinType;
import com.alodiga.services.provider.commons.utils.Constants;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.commons.utils.Mail;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.commons.utils.ServicesProviderMails;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;



public class AdminAccountController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprises;
    private Combobox cmbLanguages;
    private Combobox cmbPeriods;
    private Combobox cmbType;

    private Combobox cmbCountries;
    private Combobox cmbStates;
    private Combobox cmbCounties;
    private Combobox cmbCities;
    private Combobox cmbTinTypes;
    private Combobox cmbProfiles;
    private Label lblLogin;
    private Textbox txtFullName;
    private Textbox txtBusinessName;
    private Textbox txtIdentifier;
    private Textbox txtnunDepTrans;

    private Textbox txtEmail;
    private Textbox txtCountryCode;
    private Textbox txtRegionCode;
    private Textbox txtPhoneNumber;
    private Textbox txtFacebookAccount;
    private Textbox txtTwitterAccount;
    private Textbox txtStateName;
    private Textbox txtCountyName;
    private Textbox txtCityName;
    private Textbox txtAddress;
    private Textbox txtZipCode;
    private Textbox txtUrl;
    private Textbox txtCustomServiceId;
    private Textbox txtWebUserId;
    private Textbox txtPassword;
    private Textbox txtAccountPassword;
    private Checkbox cbxEnabled;
    private Checkbox cbxType;
    private Row rowPassword;
    private Row rowAccountPassword;
    private Row rowCreditLimit;
    private Button btnSave;
    private Button btnGeneratePassword;
    private UserEJB userEJB;
    private UtilsEJB utilsEJB;
//    private TransactionEJB transactionEJB;
    private PreferencesEJB preferencesEJB = null;
    private AccessControlEJB accessEJB = null;
    private String newPassword = null;
    private String Password = null;
    private Intbox intCreditLimit;
    private Intbox intMaxTranference;
    private Intbox intMaxTopUp;
    private Row rowMaxTransference;
    private float maxBalance = 0f;
    private float maxAmountTopUp = 0f;
    Map params = null;
    private Account accountParam;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.account");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.account");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            preferencesEJB = (PreferencesEJB) EJBServiceLocator.getInstance().get(EjbConstants.PREFERENCES_EJB);
            accessEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

     private void loadProfiles(Boolean isAdd) {
        List<Profile> profiles = new ArrayList<Profile>();
        try {
            request.setParam(Profile.DISTRIBUTOR);
            profiles.add(accessEJB.loadProfile(request));
            if (!isAdd) {// Is update
                profiles = accessEJB.getProfiles(request);
                cmbProfiles.setDisabled(true);
            }
            cmbProfiles.getItems().clear();
            for (Profile profile : profiles) {
                    Comboitem item = new Comboitem();
                    item.setValue(profile);
                    item.setLabel(profile.getName());
                    item.setParent(cmbProfiles);
                    if (!isAdd) {
                        List<AccountHasProfile> ahps = accountParam.getAccountHasProfiles();
                        for (int y = 0; y < ahps.size(); y++) {
                            Profile p = ahps.get(y).getProfile();
                            if (p.getId().equals(profile.getId()) && ahps.get(y).getEndingDate() == null) {
                                cmbProfiles.setSelectedItem(item);
                            }
                        }
                    }
            }
        } catch (EmptyListException ex) {
            this.showMessage("error.empty.list", true, ex);
        } catch (Exception ex) {
            this.showMessage("error.general", true, ex);
        }
    }

    private void loadEnterprises(Enterprise enterprise) {
        List<Enterprise> enterprises = null;
        try {
            cmbEnterprises.getItems().clear();
            enterprises = utilsEJB.getEnterprises();
            for (Enterprise e : enterprises) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbEnterprises);
                if (enterprise != null && e.getId().equals(enterprise.getId())) {
                    cmbEnterprises.setSelectedItem(cmbItem);
                }
            }
            if (enterprise == null) {
                cmbEnterprises.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadTinTypes(TinType tinType) {
        List<TinType> tinTypes = null;
        Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
        try {
            cmbTinTypes.getItems().clear();

            tinTypes = utilsEJB.getTinTypesByEnterprise(enterprise.getId());
            for (TinType tt : tinTypes) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(tt.getName());
                cmbItem.setDescription(tt.getPrefix());
                cmbItem.setValue(tt);
                cmbItem.setParent(cmbTinTypes);
                if (tinType != null && tt.getId().equals(tinType.getId())) {
                    cmbTinTypes.setSelectedItem(cmbItem);
                }
            }
            if (tinType == null) {
                cmbTinTypes.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadLanguages(Language language) {
        List<Language> languages = null;

        try {
            cmbLanguages.getItems().clear();

            languages = utilsEJB.getLanguages();
            for (Language l : languages) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(l.getDescription());
                cmbItem.setDescription(l.getIso());
                cmbItem.setValue(l);
                cmbItem.setParent(cmbLanguages);
                if (language != null && l.getId().equals(language.getId())) {
                    cmbLanguages.setSelectedItem(cmbItem);
                }
            }
            if (language == null) {
                cmbLanguages.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    private void loadPeriods(Period  period) {
        List<Period> periods = null;

        try {
            cmbPeriods.getItems().clear();

            periods = utilsEJB.getPeriods();
            for (Period p : periods) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(p.getName());
                cmbItem.setDescription(p.getDescription());
                cmbItem.setValue(p);
                cmbItem.setParent(cmbPeriods);
                    if (period != null && p.getId().equals(period.getId())) {
                    cmbPeriods.setSelectedItem(cmbItem);
                }
                if (period == null) {
                cmbPeriods.setSelectedIndex(0);
            }
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    @Override
    public void clearFields() {
    }

    private void loadFields(Account account) {
        if (account != null) {//UPDATE
            cmbEnterprises.setDisabled(true);
            cmbTinTypes.setDisabled(true);
            txtIdentifier.setReadonly(true);
            txtPassword.setValue(account.getPassword());
            rowPassword.setVisible(true);
            txtAccountPassword.setValue(account.getAccountPassword());
            rowAccountPassword.setVisible(true);
            cbxEnabled.setChecked(account.getEnabled());

            cbxType.setChecked(account.getEnabled());
            showMaxBalanceTranferece();
            showMaxAmountTopUp();
            loadEnterprises(account.getEnterprise());
            loadLanguages(account.getLanguage());
            loadTinTypes(account.getTinType());
            loadPeriods(account.getPeriod());
            loadProfiles(false);
            txtFullName.setText(account.getFullName());
            txtBusinessName.setText(account.getBusinessName());
            lblLogin.setValue(account.getLogin());
            txtIdentifier.setText(account.getIdentifier());
            txtEmail.setText(account.getEmail());
            txtUrl.setText(account.getUrl());
            txtCustomServiceId.setText(account.getCustumerServiceIdSisac().toString());
            txtWebUserId.setText(account.getWebUserIdSisac().toString());
            txtFacebookAccount.setText(account.getFacebookAccount());
            txtTwitterAccount.setText(account.getTwitterAccount());
            txtAddress.setText(account.getAddress().getAddress());
            txtZipCode.setText(account.getAddress().getZipCode());
            loadCountries(account.getAddress().getCountry());
            loadPhoneNumber(account.getPhoneNumber());
            if(!cbxType.isChecked()){
                intCreditLimit.setText(account.getCrediLimit().toString());
            }
            else {
                rowCreditLimit.setVisible(false);
            }

        } else {//ADDING

            loadEnterprises(null);
            loadLanguages(null);
            loadTinTypes(null);
            loadPeriods(null);
            loadProfiles(true);
            loadCountryCode();
        }
    }

    public void blockFields() {
        cmbEnterprises.setDisabled(true);
        cmbTinTypes.setDisabled(true);
        txtIdentifier.setReadonly(true);
        rowPassword.setVisible(false);
        cbxEnabled.setDisabled(true);
        cbxType.setDisabled(true);
        cmbLanguages.setDisabled(true);
        btnGeneratePassword.setDisabled(true);
        txtFullName.setReadonly(true);
        txtBusinessName.setReadonly(true);
        txtIdentifier.setReadonly(true);
        txtEmail.setReadonly(true);
        txtFacebookAccount.setReadonly(true);
        txtTwitterAccount.setReadonly(true);
        txtAddress.setReadonly(true);
        txtZipCode.setReadonly(true);
        txtUrl.setReadonly(true);
        txtCustomServiceId.setReadonly(true);
        txtWebUserId.setReadonly(true);
        cmbCountries.setDisabled(true);
        cmbStates.setDisabled(true);
        cmbCounties.setDisabled(true);
        cmbCities.setDisabled(true);
        txtStateName.setReadonly(true);
        txtCityName.setReadonly(true);
        txtCountyName.setReadonly(true);
        txtCountryCode.setReadonly(true);
        txtRegionCode.setReadonly(true);
        txtPhoneNumber.setReadonly(true);
        intCreditLimit.setReadonly(true);
        intMaxTranference.setReadonly(true);
        btnSave.setVisible(false);
    }

    private Account setAccountBalance(Account account) throws GeneralException, NullParameterException {
        List<AccountBalance> accountBalances = new ArrayList<AccountBalance>();
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAccount(account);
        accountBalance.setBalance(maxBalance);
        accountBalance.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
        accountBalances.add(accountBalance);
        if (accountParam == null) {
            account.setAccountBalances(accountBalances);
        } else {
            boolean balanceChange = false;
            float currentMax = 0f;//transactionEJB.getMaxBalanceByAccount(accountParam.getId());
            if (currentMax != maxBalance) { // Max balance has changed
                balanceChange = true;
                accountBalances = accountParam.getAccountBalances();
                for (AccountBalance ab : accountBalances) {
                    if (ab.getEndingDate() == null) {
                        ab.setEndingDate(new Timestamp(new java.util.Date().getTime()));
                    }
                }
            }
            if (balanceChange) {
                account.getAccountBalances().add(accountBalance);
            }
        }
        return account;
    }

     private Account setAccountLimitTopUp(Account account) throws GeneralException, NullParameterException {
        List<AccountLimitTopUp> accountLimitTopUps = new ArrayList<AccountLimitTopUp>();
        AccountLimitTopUp limitTopUp = new AccountLimitTopUp();
        limitTopUp.setAccount(account);
        maxAmountTopUp = intMaxTopUp.getValue().floatValue();
        limitTopUp.setCountTopUp(maxAmountTopUp);
        limitTopUp.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
        accountLimitTopUps.add(limitTopUp);
        if (accountParam == null) {
            account.setLimitTopUps(accountLimitTopUps);
        } else {
            boolean limitChange = false;
            float currentMax = 0f;// transactionEJB.getMaxBalanceByAccount(accountParam.getId());
            if (currentMax != maxAmountTopUp) { // Max balance has changed
                limitChange = true;
                accountLimitTopUps = accountParam.getLimitTopUps();
                for (AccountLimitTopUp ab : accountLimitTopUps) {
                    if (ab.getEndingDate() == null) {
                        ab.setEndingDate(new Timestamp(new java.util.Date().getTime()));
                    }
                }
            }
            if (limitChange) {
                account.getLimitTopUps().add(limitTopUp);
            }
        }
        return account;
    }

    private void showMaxBalanceTranferece() {
        rowMaxTransference.setVisible(true);
        if (accountParam != null) {
//            try {
//                intMaxTranference.setValue(transactionEJB.getMaxBalanceByAccount(accountParam.getId()).intValue());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Logger.getLogger(AdminAccountController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } else {
            intMaxTranference.setValue(0);
        }
    }

    private void showMaxAmountTopUp() {
        if (accountParam != null) {
//            try {
//                intMaxTopUp.setValue(transactionEJB.getlimitTopUpByAccount(accountParam.getId()).intValue());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Logger.getLogger(AdminAccountController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } else {
            intMaxTopUp.setValue(0);
        }
    }

    public Boolean validateEmpty() throws GeneralException{
        if (cmbEnterprises.getSelectedItem() == null) {
            cmbEnterprises.setFocus(true);
            this.showMessage("sp.error.enteprise.notSelected", true, null);
        } else if (txtFullName.getText().isEmpty()) {
            txtFullName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtBusinessName.getText().isEmpty()) {
            txtBusinessName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbTinTypes.getSelectedItem() == null) {
            cmbTinTypes.setFocus(true);
            this.showMessage("sp.error.tinType.notSelected.single", true, null);
        } else if (txtIdentifier.getText().isEmpty()) {
            txtIdentifier.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtCountryCode.getText().isEmpty()) {
            txtCountryCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtRegionCode.getText().isEmpty()) {
            txtRegionCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPhoneNumber.getText().isEmpty()) {
            txtPhoneNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtUrl.getText().isEmpty()) {
            txtUrl.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtCustomServiceId.getText().isEmpty()) {
            txtCustomServiceId.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtWebUserId.getText().isEmpty()) {
            txtWebUserId.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbLanguages.getSelectedItem() == null) {
            cmbLanguages.setFocus(true);
            this.showMessage("sp.error.language.notSelected", true, null);
        } else if (txtCountryCode.isReadonly() && !Utils.validatePhoneNumberUSA(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText())) {
            this.showMessage("sp.error.invalidPhoneNumber", true, null);
        } else if (!cmbStates.isVisible() && txtStateName.getText().isEmpty()) {
            txtStateName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (!cmbCities.isVisible() && txtCityName.getText().isEmpty()) {
            txtCityName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAddress.getText().isEmpty()) {
            txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (intMaxTopUp.getText().isEmpty()) {
            intMaxTopUp.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtZipCode.getText().isEmpty()) {
            txtZipCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (intCreditLimit.getText().isEmpty() && !cbxType.isChecked()) {
            intCreditLimit.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (rowMaxTransference.isVisible() && !validateMaxBalance()) {
        } else {
            return true;
        }
        return false;

    }


    private boolean validateMaxBalance() throws GeneralException {

        maxBalance = intMaxTranference.getValue().floatValue();
        Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
        try {

            float preferenceBalance = 0f;
            params = new HashMap<String, Object>();
            params.put(QueryConstants.PARAM_ENTERPRISE_ID, enterprise.getId());
            request.setParams(params);
            Map<Long, String> preferences = preferencesEJB.getLastPreferenceValues(request);
            String value = preferences.get(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_LIMIT.getId());
            preferenceBalance = Float.parseFloat(value);
            if (preferenceBalance >= maxBalance) {
                return true;
            } else {
                this.showMessage(Labels.getLabel("sp.error.maxBalanceExceed") + " " + preferenceBalance, true, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralException(ex.getMessage());
        }
        return false;
    }


    private void save(Account _account) {
        Period period = new Period();
      float balan = 0;
      float credit=0;
       Date date = new Date();
       java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());


        try {

            Account account = new Account();
            if (_account != null) {
                account = _account;
                if (newPassword != null && !newPassword.isEmpty()) {
                    account.setAccountPassword(newPassword);
                }
            } else {

                String login = AccessControl.getRandomLogin(GeneralUtils.getRamdomNumber(Constants.MAX_LOGIN_DIGITS));
                account.setLogin(login);
                Password =GeneralUtils.getRamdomPassword(Constants.MAX_PASSWORD_DIGITS);
                String password2 = GeneralUtils.encryptMD5(Password);
                account.setAccountPassword(password2);
                account.setPassword(password2);
                account.setEnterprise((Enterprise) cmbEnterprises.getSelectedItem().getValue());
                account.setTinType((TinType) cmbTinTypes.getSelectedItem().getValue());
                account.setIdentifier(txtIdentifier.getText());
                account.setCreationDate(new Timestamp(new java.util.Date().getTime()));
                account.setLastBillingDate(timeStampDate);
                account.setNexBillingDate(getDateBell(((Period)cmbPeriods.getSelectedItem().getValue()).getDays()));
                account.setBalance(balan);
                if(!cbxType.isChecked()){
                    account.setCrediLimit(Float.parseFloat(intCreditLimit.getText()));
                } else {
                    account.setCrediLimit(null);
                }
                
            }
            account.setFullName(txtFullName.getText());
            account.setBusinessName(txtBusinessName.getText());
            account.setEmail(txtEmail.getText());
            account.setPhoneNumber(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText());
            account.setUrl(txtUrl.getText());
            account.setAddress(getAddress());
            account.setLanguage((Language) cmbLanguages.getSelectedItem().getValue());
            account.setPeriod((Period) cmbPeriods.getSelectedItem().getValue());
            account.setIsPrePaid(cbxType.isChecked());
            account.setEnabled(cbxEnabled.isChecked());
            account.setFacebookAccount(txtFacebookAccount.getText());
            account.setTwitterAccount(txtTwitterAccount.getText());
            account.setCustumerServiceIdSisac(Long.parseLong(txtCustomServiceId.getText()));
            account.setWebUserIdSisac(Long.parseLong(txtWebUserId.getText()));
            // AccountBalance
            account = this.setAccountBalance(account);
            account = this.setAccountLimitTopUp(account);
            account = this.setProfile(account);
            request.setParam(account);
            account = userEJB.saveAccount(request);
            sms(account);
            accountParam = account;
            eventType = WebConstants.EVENT_EDIT;

//            BalanceHistory balanceHistory = new BalanceHistory();
//            balanceHistory.setAccount(account);
//            balanceHistory.setOldAmount(0f);
//            balanceHistory.setCurrentAmount(0f);
//            balanceHistory.setDate(new Timestamp(new java.util.Date().getTime()));
//            balanceHistory.setVersion(1);
//            request.setParam(balanceHistory);
//            balanceHistory = transactionEJB.saveBalanceHistory(request);


            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            //showError(ex);
        }
    }

     private Account setProfile(Account account) {
        if (accountParam == null) {// Is new
            List<AccountHasProfile> accountHasProfiles = new ArrayList<AccountHasProfile>();
            AccountHasProfile accountHasProfile = new AccountHasProfile();
            accountHasProfile.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
            accountHasProfile.setProfile((Profile) cmbProfiles.getSelectedItem().getValue());
            accountHasProfile.setAccount(account);
            accountHasProfiles.add(accountHasProfile);
            account.setAccountHasProfiles(accountHasProfiles);
        } else {// Is update
            List<AccountHasProfile> ahProfiles = accountParam.getAccountHasProfiles();
            boolean profileChanged = false;
            Profile currentProfile = (Profile) cmbProfiles.getSelectedItem().getValue();

            for (int i = 0; i < ahProfiles.size(); i++) {
                Profile p = ahProfiles.get(i).getProfile();
                if (ahProfiles.get(i).getEndingDate() == null && !p.getId().equals(currentProfile.getId())) {
                    ahProfiles.get(i).setEndingDate(new Timestamp(new java.util.Date().getTime()));
                    profileChanged = true;
                }
            }

            if (profileChanged) {
                AccountHasProfile ahProfile = new AccountHasProfile();
                ahProfile.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
                ahProfile.setAccount(account);
                ahProfile.setProfile((Profile) cmbProfiles.getSelectedItem().getValue());
                ahProfiles.add(ahProfile);
                account.setAccountHasProfiles(ahProfiles);
            } else {
                account.setAccountHasProfiles(accountParam.getAccountHasProfiles());
            }
        }
        return account;
    }
    

    private void sms(Account account){
        try {
            //******************************************************
            System.out.println("si llego la cuenta ");
            System.out.println("    : " + account.getEmail() + "");
            //******************************************************
            
            Mail mail = new Mail();
            mail.setBody(null);
            
            mail = ServicesProviderMails.getAccountRegistrationMail(account,Password);
            sendMail(mail);

            if(mail!=null){
                System.out.println("se envio ");

            }else{
                System.out.println("no se envio");
            }


        } catch (Exception ex) {
            Logger.getLogger(AdminAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }





  /******************************************************************************************/


 public static void sendMail(Mail mail) throws MessagingException {
        try {

            if (mail == null) {
                throw new NullParameterException("Parameter mail cannot be null");
            }
            if (mail.getEnterprise() != null) {
                mail.setFrom(mail.getEnterprise().getInfoEmail());
            }
            if (mail.getFrom() == null || "".equals(mail.getFrom())) {
                throw new NullParameterException("Parameter mail.getFrom cannot be null");
            }

            if (mail.getTo() == null || mail.getTo().isEmpty()) {
                throw new NullParameterException("Parameter mail.getTo cannot be null");
            }

            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", Constants.SMTP_SERVER);

            // Preparamos la sesion
            Session session = Session.getInstance(props, null);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail.getFrom()));

            // Lista de destinos
            InternetAddress[] addresses = new InternetAddress[mail.getTo().size()];
            for (int i = 0; i < addresses.length; i++) {
                addresses[i] = new InternetAddress((String) mail.getTo().get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject(mail.getSubject());
            message.setContent(mail.getBody(), "text/html");
            if (mail.getBcc() != null) {
                InternetAddress[] bccs = new InternetAddress[mail.getBcc().size()];
                for (int i = 0; i < bccs.length; i++) {
                    bccs[i] = new InternetAddress((String) mail.getBcc().get(i));
                }
                message.setRecipients(Message.RecipientType.BCC, bccs);
            }
            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();

            mbp1.setContent(mail.getBody(), "text/html");
            //mbp1.setText(mail.getBody(), "text/html");

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            if (mail.getAttachments() != null) {
                // create the second message part
                MimeBodyPart mbp2 = new MimeBodyPart();
                for (int i = 0; i < mail.getAttachments().size(); i++) {
                    // attach the file to the message
                    FileDataSource fds = new FileDataSource(mail.getAttachments().get(i));
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(fds.getName());
                    mp.addBodyPart(mbp2);
                }
            }

            if (mail.getDataHandlers() != null) {
                // create the second message part
                MimeBodyPart mbp2 = new MimeBodyPart();
                for (int i = 0; i < mail.getDataHandlers().size(); i++) {
                    // attach the file to the message
                    mbp2.setDataHandler(mail.getDataHandlers().get(i));
                    mbp2.setFileName(mail.getSubject().toString() + i + "." + typeFile(mail.getDataHandlers().get(i).getContentType()));
                    mp.addBodyPart(mbp2);
                }
            }
            // add the Multipart to the message
            message.setContent(mp);
            // set the Date: header
            message.setSentDate(new Date());

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect();
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("Error sending email", e);
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public static String typeFile(String type) {
        String ext = new String();
        if (type.compareTo("application/vnd.ms-excel") == 0) {
            ext = "xls";
        }
        return ext;
    }














    /******************************************************************************************/



   private Timestamp  getDateBell(Integer dis) {
   //    Date date = new Date();

      if(dis!=0){
      Calendar cal = CalendarT.addDays(null, dis);
       Date now = new Date(cal.getTimeInMillis());
       java.sql.Timestamp timeStampDate = new Timestamp(now.getTime());


     
       return timeStampDate;

      }else{

      Calendar cal = CalendarT.addDays(null, 0);
        Date now = new Date(cal.getTimeInMillis());
        java.sql.Timestamp timeStampDate = new Timestamp(now.getTime());
       return timeStampDate;
      }

   }


    private Address getAddress() {

        Address address = new Address();
        Country country = (Country) cmbCountries.getSelectedItem().getValue();
        address.setCountry(country);
        if (cmbStates.isVisible()) {// Country have states
            address.setState((State) cmbStates.getSelectedItem().getValue());
        } else {
            address.setStateName(txtStateName.getText());
        }
        if (cmbCounties.getSelectedIndex() != -1) {
            address.setCounty((County) cmbCounties.getSelectedItem().getValue());
        }
        if (cmbCities.isVisible()) {// State have cities
            address.setCity((City) cmbCities.getSelectedItem().getValue());
        } else {
            address.setCityName(txtCityName.getText());
        }
        address.setAddress(txtAddress.getText());
        address.setZipCode(txtZipCode.getText());
        return address;
    }

    public void onClick$btnSave() {
        try {
            if (validateEmpty()) {
                save(accountParam);

            }
        } catch (GeneralException ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(accountParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(accountParam);
                break;
            case WebConstants.EVENT_ADD:
                loadFields(null);
                break;
            default:
                break;
        }
    }

    private void loadCountries(Country country) {
        try {
            List<Country> countries = new ArrayList<Country>();
            request.setParam(Country.USA);
            countries.add(utilsEJB.loadCountry(request));
            for (int i = 0; i < countries.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(countries.get(i));
                item.setLabel(countries.get(i).getName());
                item.setParent(cmbCountries);
                if (countries.get(i).getId().equals(country.getId())) {
                    cmbCountries.setSelectedItem(item);
                }
            }
            loadStates(country, accountParam.getAddress().getState());
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    private void loadStates(Country country, State state) {

        if (state == null) {
            cmbStates.setVisible(false);
            txtStateName.setVisible(true);
            txtStateName.setText(accountParam.getAddress().getStateName());
            cmbCities.setVisible(false);
            txtCityName.setVisible(true);
            txtCityName.setText(accountParam.getAddress().getCityName());
        } else {
            try {
                cmbCounties.getItems().clear();
                cmbStates.getItems().clear();
                params = new HashMap();
                params.put(QueryConstants.PARAM_COUNTRY_ID, country.getId());
                List<State> states = null;
                request.setParams(params);
                states = utilsEJB.getStateByCountry(request);
                for (int i = 0; i < states.size(); i++) {
                    Comboitem item = new Comboitem();
                    item.setValue(states.get(i));
                    item.setLabel(states.get(i).getName());
                    item.setParent(cmbStates);
                    if (states.get(i).getId().equals(state.getId())) {
                        cmbStates.setSelectedItem(item);
                    }
                }
                if (accountParam.getAddress().getCounty() != null) {
                    loadCounties(state, accountParam.getAddress().getCounty());
                } else if (accountParam.getAddress().getCity() != null) {
                    loadCities(state, null, accountParam.getAddress().getCity());
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }
    }

    private void loadCounties(State state, County county) {

        try {
            cmbCounties.getItems().clear();
            params = new HashMap();
            params.put(QueryConstants.PARAM_STATE_ID, state.getId());
            request.setParams(params);
            List<County> counties = utilsEJB.getCountiesByState(request);
            for (int i = 0; i < counties.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(counties.get(i));
                item.setLabel(counties.get(i).getName());
                item.setParent(cmbCounties);
                if (counties.get(i).getId().equals(county.getId())) {
                    cmbCounties.setSelectedItem(item);
                }
            }
            loadCities(state, county, accountParam.getAddress().getCity());
        } catch (EmptyListException ex) {
            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadCities(State state, County county, City city) {
        try {
            cmbCities.getItems().clear();
            List<City> cities = null;
            params = new HashMap();
            request.setParams(params);
            if (county != null) {
                params.put(QueryConstants.PARAM_COUNTY_ID, county.getId());
                cities = utilsEJB.getCitiesByCounty(request);
            } else if (state != null) {
                params.put(QueryConstants.PARAM_STATE_ID, state.getId());
                cities = utilsEJB.getCitiesByState(request);
            }
            for (int i = 0; i < cities.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(cities.get(i));
                item.setLabel(cities.get(i).getName());
                item.setParent(cmbCities);
                if (cities.get(i).getId().equals(city.getId())) {
                    cmbCities.setSelectedItem(item);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnGeneratePassword() {
        try {
            newPassword = GeneralUtils.encryptMD5(GeneralUtils.getRamdomPassword(8));
            txtAccountPassword.setValue(newPassword);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadPhoneNumber(String phoneNumber) {

        txtCountryCode.setText(phoneNumber.substring(0, 1));
        txtRegionCode.setText(phoneNumber.substring(1, 4));
        txtPhoneNumber.setText(phoneNumber.substring(4, 11));
    }

    public void loadCountryCode() {
        if (cmbEnterprises.getSelectedItem() != null) {
            Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
            if (enterprise.getId().equals(Enterprise.ALODIGA_USA)) {
                txtCountryCode.setReadonly(true);
                txtCountryCode.setText("" + Constants.USA_CODE);
            } else {
                txtCountryCode.setReadonly(false);
                txtCountryCode.setRawValue(null);
            }
            loadTinTypes(null);
        }
    }

    public void onClick$btnReferenceScript1() {

        Executions.getCurrent().sendRedirect("/docs/script_referencia1.txt", "_blank");
    }

    public void onClick$btnReferenceScript2() {
        Executions.getCurrent().sendRedirect("/docs/script_referencia2.txt", "_blank");
    }

    public void onClick$cbxType(){
        if(!cbxType.isChecked()){
                rowCreditLimit.setVisible(true);
                rowMaxTransference.setVisible(false);
           }
            else {
                rowCreditLimit.setVisible(false);
                rowMaxTransference.setVisible(true);
            }
    }
}
