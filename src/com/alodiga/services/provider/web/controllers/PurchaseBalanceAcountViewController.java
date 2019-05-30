//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
//import com.alodiga.services.provider.commons.ejbs.PreferencesEJB;
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.InvalidCreditCardException;
//import com.alodiga.services.provider.commons.exceptions.InvalidPaymentInfoException;
//import com.alodiga.services.provider.commons.exceptions.MaxAmountBalanceException;
//import com.alodiga.services.provider.commons.exceptions.MaxAmountDailyException;
//import com.alodiga.services.provider.commons.exceptions.MaxAmountPerTransactionException;
//import com.alodiga.services.provider.commons.exceptions.MinAmountBalanceException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.exceptions.PaymentDeclinedException;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.exceptions.TransactionCanceledException;
//import com.alodiga.services.provider.commons.exceptions.TransactionNotAvailableException;
//import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.managers.ContentManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Address;
//import com.alodiga.services.provider.commons.models.City;
//import com.alodiga.services.provider.commons.models.Country;
//import com.alodiga.services.provider.commons.models.County;
//import com.alodiga.services.provider.commons.models.CreditcardType;
//import com.alodiga.services.provider.commons.models.PaymentInfo;
//import com.alodiga.services.provider.commons.models.PaymentPatner;
//import com.alodiga.services.provider.commons.models.PaymentType;
//import com.alodiga.services.provider.commons.models.PreferenceFieldEnum;
//import com.alodiga.services.provider.commons.models.State;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionStatus;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.services.models.WSConstants;
//import com.alodiga.services.provider.commons.utils.Constants;
//import com.alodiga.services.provider.commons.utils.CreditCardUtils;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zul.Longbox;
//import org.zkoss.zul.Textbox;
//import java.util.Date;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Grid;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Window;
//import org.zkoss.zk.ui.event.EventQueues;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Checkbox;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Vlayout;
//
//public class PurchaseBalanceAcountViewController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private TransactionEJB transactionEJB;
//    private Longbox lgxRechargeAmount;
//    //private Combobox cmbPaymentInfo;
//    private Combobox cmbCCType;
//    private Combobox cmbCCDateMonth;
//    private Combobox cmbCCDateYear;
//    // private Combobox cmbCCCountry;
//    private Label lblcurrency;
//    private Textbox txtCCNumber;
//    private Textbox txtCCName;
//    private Textbox txtCCCode;
//    private Vlayout vltExistingPayment;
//    private Vlayout vltNewPayment;
//    private Account currentAccount;
//    private PreferencesEJB preferencesEJB;
//    private CustomerEJB customerEJB;
//    private Button btnAdd;
//    private Transaction transaction;
//    private PaymentInfo paymentInfo;
//    private String currencySymbol;
//    private Textbox txtAddress;
//    private Textbox txtZipCode;
//    private Textbox txtStateName;
//    private Textbox txtCityName;
//    private Combobox cmbCountries;
//    private Combobox cmbStates;
//    private Combobox cmbCounties;
//    private Combobox cmbCities;
//    private Vlayout vltPurchase;
//    private Vlayout vltSummary;
//    private Checkbox cbxTerms;
//    private Listbox lbxPaymentOptions;
//    private Button btnExistingPaymentPurchase;
//    private Button btnNewPaymentPurchase;
//    private Button btnNewPaymentCancel;
//    private Label lblInvTransactionDate;
//    private Label lblInvTransactionNumber;
//    private Label lblInvTotalAmount;
//    private Label lblInvTotal;
//    private Label lblInvCCType;
//    private Label lblInvCCName;
//    private Label lblInvCCNumber;
//    private Label lblInvCCAddres;
//    private Label lblInvCCZipCode;
//    private Transaction successFulTransaction;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//
//        initialize();
//        loadData();
//    }
//
//    @Override
//    public void initialize() {
//        try {
//            super.initialize();
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
//            currentAccount = AccessControl.loadCurrentAccount();
//            currencySymbol = currentAccount.getEnterprise().getCurrency().getSymbol();
//            lgxRechargeAmount.setValue(new Long(0));
//            lgxRechargeAmount.setFocus(true);
//            cbxTerms.setChecked(false);
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void clearFields() {
//    }
//
//    public String ccNumberformater(String ccNumber) {
//        char ccToArray[] = ccNumber.toCharArray();
//        String star = "*";
//        String result = "";
//        int length = ccToArray.length;
//        for (int i = 0; i < length; i++) {
//            if (i < 5 || i > length - 4) {
//                result += ccToArray[i];
//            } else {
//                result += star;
//            }
//        }
//        return result;
//    }
//
//    public void loadCmbCCType() {
//        EJBRequest requestCCT = new EJBRequest();
//        requestCCT.setParams(new HashMap<String, Object>());
//        List<CreditcardType> creditcardTypes;
//        try {
//            creditcardTypes = transactionEJB.getCreditcardTypes(request);
//            if (creditcardTypes != null && !creditcardTypes.isEmpty()) {
//                for (CreditcardType creditcardType : creditcardTypes) {
//                    Comboitem item = new Comboitem();
//                    item.setValue(creditcardType);
//                    item.setLabel(creditcardType.getName());
//                    item.setParent(cmbCCType);
//                }
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private void loadCmbCCDateMonth() {
//        for (int i = 1; i <= 12; i++) {
//            Month month = new Month(i);
//            Comboitem item = new Comboitem();
//            item.setValue(month);
//            item.setLabel(month.getName());
//            item.setParent(cmbCCDateMonth);
//        }
//    }
//
//    private void loadCmbCCDateYear() {
//        Date today = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy");
//        int year = Integer.parseInt(format.format(today));
//        for (int i = 0; i <= 20; i++) {
//            Comboitem item = new Comboitem();
//            item.setValue(year);
//            item.setLabel("" + year);
//            item.setParent(cmbCCDateYear);
//            year++;
//        }
//    }
//
//    public boolean validateDate() {
//        Month month = (Month) cmbCCDateMonth.getSelectedItem().getValue();
//        SimpleDateFormat format1 = new SimpleDateFormat("MM");
//        Date ccdate = null;
//        try {
//            ccdate = format1.parse(month.getMonth());
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }
//        int monthValidInt = Integer.parseInt(format1.format(ccdate));
//        boolean valid = true;
//        Date today = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy");
//        int yearNow = Integer.parseInt(format.format(today));
//        int yearSeleted = Integer.parseInt(cmbCCDateYear.getValue());
//        Calendar todayMont = Calendar.getInstance();
//        int monthNow1 = todayMont.get(Calendar.MONTH) + 1;
//
//        if (yearSeleted < yearNow) {
//            this.showMessage("sp.error.dateSelectInvalid.Invalid", true, null);
//            valid = false;
//        }
//        if (yearSeleted == yearNow) {
//            if (monthValidInt < monthNow1) {
//                this.showMessage("sp.error.dateSelectInvalid.Invalid", true, null);
//                valid = false;
//            }
//        }
//        return valid;
//    }
//
//    public void loadData() {
//        lblcurrency.setValue(currencySymbol);
//        preferencesEJB = (PreferencesEJB) EJBServiceLocator.getInstance().get(EjbConstants.PREFERENCES_EJB);
//        loadPaymentMethods();
//        loadCmbCCDateMonth();
//        loadCmbCCDateYear();
//        loadCmbCCType();
//
//    }
//
//    public void onClick$btnAdd() {
//        btnAdd.setVisible(false);
//        vltExistingPayment.setVisible(false);
//        vltNewPayment.setVisible(true);
//    }
//
//    public void onClick$btnCancel() {
//        txtAddress.setValue("");
//        txtCCCode.setValue("");
//        txtCCName.setValue("");
//        txtCCNumber.setValue("");
//        txtZipCode.setValue("");
//
//        cmbCCDateMonth.setSelectedIndex(-1);
//        cmbCCDateYear.setSelectedIndex(-1);
//        cmbCCType.setSelectedIndex(-1);
//        this.clearMessage();
//        lgxRechargeAmount.setValue(new Long(0));
//        loadPaymentMethods();
//        btnAdd.setVisible(true);
//        lgxRechargeAmount.setFocus(true);
//    }
//
//    private PaymentInfo getPaymentInfo() throws ParseException {
//        PaymentInfo _paymentInfo = null;
//
//        if (txtCCNumber.getText().isEmpty()) {
//            txtCCNumber.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCCCode.getText().isEmpty()) {
//            txtCCCode.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCCCode.getText().length() < 3) {
//            txtCCCode.focus();
//            this.showMessage("sp.error.invalidSecurityCode", true, null);
//        } else if (txtCCNumber.getText().length() < 14) {
//            txtCCNumber.focus();
//            this.showMessage("sp.error.rechargeCreditCardNumber", true, null);
//        } else if (txtCCName.getText().isEmpty()) {
//            txtCCName.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCType.getSelectedIndex() < 0) {
//            cmbCCType.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCDateYear.getSelectedIndex() < 0) {
//            cmbCCDateYear.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCDateMonth.getSelectedIndex() < 0) {
//            cmbCCDateMonth.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtAddress.getText().isEmpty()) {
//            txtAddress.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtZipCode.getText().length() < 5) {
//            txtZipCode.focus();
//            this.showMessage("sp.error.zipCode", true, null);
//        } else if (!cbxTerms.isChecked()) {
//            this.showMessage("sp.error.field.terms", true, null);
//        } else if (!validateNumbers()) {
//            //Nothing
//        } else if (!validateDate()) {
//            //Nothing
//        } else if (!validateAddress()) {
//            //Nothing
//        } else {
//
//            Address address = getAddress();
//            try {
//                address = customerEJB.saveAddress(address);
//            } catch (NullParameterException ex) {
//                ex.printStackTrace();
//            } catch (GeneralException ex) {
//                 ex.printStackTrace();
//            }
//            _paymentInfo = new PaymentInfo();
//            _paymentInfo.setAddress(address);
//            _paymentInfo.setBeginningDate(new Timestamp((new Date()).getTime()));
//            _paymentInfo.setCreditCardCvv(txtCCCode.getValue());
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//            Month month = (Month) cmbCCDateMonth.getSelectedItem().getValue();
//            int year = (Integer) cmbCCDateYear.getSelectedItem().getValue();
//            Date ccdate = format.parse("01-" + month.getMonth() + "-" + year);
//            Timestamp ccTimestamp = new Timestamp(ccdate.getTime());
//            _paymentInfo.setCreditCardDate(ccTimestamp);
//            _paymentInfo.setCreditCardName(txtCCName.getValue());
//            _paymentInfo.setCreditCardNumber(txtCCNumber.getValue());
//            _paymentInfo.setCreditcardType((CreditcardType) cmbCCType.getSelectedItem().getValue());
//            PaymentType paymentType = new PaymentType();
//            paymentType.setId(PaymentType.CREDIT_CARD);
//            _paymentInfo.setPaymentType(paymentType);
//            PaymentPatner patner = new PaymentPatner();
//            patner.setId(PaymentPatner.AUTHORIZE);
//            _paymentInfo.setPaymentPatner(patner);
//            _paymentInfo.setAccount(currentAccount);
//        }
//
//
//
//        return _paymentInfo;
//    }
//
//    private void purchaseBalance(PaymentInfo paymentInfo) throws ParseException {
//
//        this.clearMessage();
//        if (lgxRechargeAmount.getText().isEmpty() || lgxRechargeAmount.getValue() <= 0) {
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//            return;
//        }
//        if (validatePreference() && validateMaxBalanceTransaction()) {
//
//            try {
//                transaction = new Transaction();
//                transaction.setPaymentInfo(paymentInfo);
//                transaction.setAccount(currentAccount);
//                Float amount = Float.parseFloat(lgxRechargeAmount.getValue().toString());
//                transaction.setCreationDate(new Timestamp((new java.util.Date().getTime())));
//                transaction.setTotalAmount(amount);
//                transaction.setTotalTax(0F);
//                TransactionStatus transactionStatus = ContentManager.getInstance().getTransactionStatusById(TransactionStatus.PROCESSED);
//                transaction.setTransactionStatus(transactionStatus);
//
//                TransactionType type = new TransactionType();
//                type.setId(TransactionType.PURCHASE_BALANCE);
//                transaction.setTransactionType(type);
//                transaction.setPaymentInfo(paymentInfo);
//                transaction.setTransactionType(type);
//                EJBRequest requestTrans = new EJBRequest();
//                Map<String, Object> reqParams = new HashMap<String, Object>();
//                reqParams.put(QueryConstants.PARAM_TRANSACTION, transaction);
//                reqParams.put(WSConstants.PARAM_LANGUAGE_ID, 2);
//                requestTrans.setParams(reqParams);
//                Map<String, Object> mapResultWebRecharge = new HashMap<String, Object>();
//                mapResultWebRecharge = transactionEJB.purchaseBalance(transaction);
//                transaction = (Transaction) mapResultWebRecharge.get(Constants.RESPONSE_TRANSACTION);
//                paymentInfo = transaction.getPaymentInfo();
////                if (paymentInfo.getId() == null) {
////                    EJBRequest requestPi = new EJBRequest();
////                    requestPi.setParam(paymentInfo);
////                    transactionEJB.savePaymentInfo(requestPi);
////                }
//                vltPurchase.setVisible(false);
//                vltSummary.setVisible(true);
//                lblInvTransactionDate.setValue(GeneralUtils.date2String(transaction.getCreationDate(), GeneralUtils.FORMAT_DATE_USA));
//                lblInvTransactionNumber.setValue(transaction.getId().toString());
//                lblInvTotal.setValue(transaction.getRecharge().getTotalAmount() + " " + currencySymbol);
//                lblInvCCType.setValue(paymentInfo.getCreditcardType().getName());
//                lblInvCCName.setValue(paymentInfo.getCreditCardName());
//                lblInvCCNumber.setValue(CreditCardUtils.hideCreditCardNumber(paymentInfo.getCreditCardNumber()));
//                lblInvCCAddres.setValue(paymentInfo.getAddress().getAddress());
//                lblInvCCZipCode.setValue(paymentInfo.getAddress().getZipCode());
//                printBalanceByAccount();
//                successFulTransaction = transaction;
//                this.showMessage("sp.purchase.product.success", false, null);
//            } catch (InvalidCreditCardException ex) {
//                this.showMessage("sp.error.invalid.creditCard", true, ex);
//            } catch (RegisterNotFoundException ex) {
//                this.showMessage("sp.error.empty.list", true, ex);
//            } catch (NullParameterException ex) {
//                this.showMessage("sp.error.empty.list", true, ex);
//            } catch (TransactionCanceledException ex) {
//                this.showMessage("sp.error.purchase.transactionCanceled", true, ex);
//            } catch (MaxAmountBalanceException ex) {
//                this.showMessage("sp.error.amountBalance", true, ex);
//            } catch (MinAmountBalanceException ex) {
//                this.showMessage("sp.error.amountBalance", true, ex);
//            } catch (TransactionNotAvailableException ex) {
//                this.showMessage("sp.error.transactionNotAvailable", true, ex);
//            } catch (PaymentDeclinedException ex) {
//                this.showMessage("sp.error.declined.creditCard", true, ex);
//            } catch (InvalidPaymentInfoException ex) {
//               this.showMessage("sp.error.invalid.creditCard", true, ex);
//            } catch (MaxAmountPerTransactionException ex) {
//                this.showMessage("sp.error.maxAmountPerTransaction", true, ex);
//            } catch (MaxAmountDailyException ex) {
//                this.showMessage("sp.error.maxAmountDaily", true, ex);
//            } catch (GeneralException ex) {
//                this.showMessage("sp.error.general", true, ex);
//            } catch (Exception ex) {
//                this.showMessage("sp.error.general", true, ex);
//            }
//        }
//
//    }
//
//    private void showBalance() {
//        EventQueues.lookup("loadSummaryData", EventQueues.APPLICATION, true).publish(new Event(""));
//    }
//
//    @Override
//    public void loadPermission(AbstractSPEntity clazz) throws Exception {
//    }
//
//    private Boolean validatePaymentInfo() {
//
//        if (lgxRechargeAmount.getText().isEmpty()) {
//            lgxRechargeAmount.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCCNumber.getText().isEmpty()) {
//            txtCCNumber.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCCCode.getText().isEmpty()) {
//            txtCCCode.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCCCode.getText().length() < 3) {
//            txtCCCode.focus();
//            this.showMessage("sp.error.invalidSecurityCode", true, null);
//        } else if (txtCCNumber.getText().length() < 14) {
//
//            txtCCNumber.focus();
//            this.showMessage("sp.error.rechargeCreditCardNumber", true, null);
//        } else if (txtCCName.getText().isEmpty()) {
//
//            txtCCName.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCType.getSelectedIndex() < 0) {
//
//            cmbCCType.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCDateYear.getSelectedIndex() < 0) {
//
//            cmbCCDateYear.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (cmbCCDateMonth.getSelectedIndex() < 0) {
//            cmbCCDateMonth.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtAddress.getText().isEmpty()) {
//            txtAddress.focus();
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtZipCode.getText().length() < 5) {
//            txtZipCode.focus();
//            this.showMessage("sp.error.zipCode", true, null);
//        } else if (!cbxTerms.isChecked()) {
//            this.showMessage("sp.error.field.terms", true, null);
//        } else if (!cbxTerms.isChecked()) {
//            this.showMessage("sp.error.field.terms", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void blockFields() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    private class Month {
//
//        private String name;
//        int month;
//        String labelPrefix = "sp.common.month";
//
//        public Month(int m) {
//            this.month = m;
//            this.name = Labels.getLabel(labelPrefix + m);
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getMonth() {
//            return (this.month >= 10 ? "0" : "") + this.month;
//        }
//    }
//
//    public Boolean validateMaxBalanceTransaction() {
//        Float balanceAccount = 0f;
//        Float MaxBalanceTransactionByaccount = 0f;
//        Boolean valid = true;
//        try {
//            MaxBalanceTransactionByaccount = transactionEJB.getMaxBalanceByAccount(currentAccount.getId());
//        } catch (NullParameterException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        } catch (GeneralException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//        try {
//            balanceAccount = transactionEJB.getCurrentBalanceByAccount(currentAccount.getId());
//        } catch (NullParameterException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        } catch (GeneralException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//        if ((lgxRechargeAmount.getValue() + balanceAccount) > MaxBalanceTransactionByaccount) {
//            this.showMessage("sp.error.amountBalance", true, null);
//            valid = false;
//        }
//        return valid;
//    }
//
//    private boolean validateNumbers() {
//        boolean valid = false;
//        if (!GeneralUtils.isNumeric(txtCCNumber.getText())) {
//            txtCCNumber.setFocus(true);
//            this.showMessage("sp.error.invalid.valueNumber", true, null);
//        } else if (!GeneralUtils.isNumeric(txtCCCode.getText())) {
//            txtCCCode.setFocus(true);
//            this.showMessage("sp.error.invalid.valueNumber", true, null);
//        } else if (!GeneralUtils.isNumeric(txtZipCode.getText())) {
//            txtZipCode.setFocus(true);
//            this.showMessage("sp.error.invalid.valueNumber", true, null);
//        } else {
//            valid = true;
//        }
//        return valid;
//    }
//
//    public boolean validatePreference() {
//        boolean valid = true;
//        EJBRequest prefRequest = new EJBRequest();
//
//        prefRequest.setParams(new HashMap<String, Object>());
//        prefRequest.getParams().put(QueryConstants.PARAM_ENTERPRISE_ID, currentAccount.getEnterprise().getId());
//        Map<Long, String> preferences = new HashMap<Long, String>();
//        try {
//            preferences = preferencesEJB.getLastPreferenceValues(prefRequest);
//        } catch (GeneralException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        } catch (RegisterNotFoundException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        } catch (NullParameterException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        } catch (EmptyListException ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//        Float maxAmountPerTransaction = Float.parseFloat(preferences.get(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_LIMIT.getId()));
//        if (lgxRechargeAmount.getValue() > maxAmountPerTransaction) {
//            this.showMessage("sp.error.maxAmountPerTransaction", true, null);
//            valid = false;
//        }
//
//        boolean transactionAvailable = preferences.get(PreferenceFieldEnum.DISABLED_TRANSACTION.getId()).equals("1");
//        if (!transactionAvailable) {
//            this.showMessage("sp.error.transactionNotAvailable", true, null);
//            valid = false;
//        }
//        return valid;
//    }
//
//    public void printBalanceByAccount() {
//
//        Float currentBalance = 0f;
//        try {
//            currentBalance = transactionEJB.getCurrentBalanceByAccount(currentAccount.getId());
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//        try {
//            showBalance();
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//
//    }
//
//    private Address getAddress() {
//
//        Address address = new Address();
//
//        Country country = (Country) cmbCountries.getSelectedItem().getValue();
//        address.setCountry(country);
//        if (cmbStates.isVisible()) {// Country have states
//            address.setState((State) cmbStates.getSelectedItem().getValue());
//        } else {
//            address.setStateName(txtStateName.getText());
//        }
//        if (cmbCounties.getSelectedIndex() != -1) {
//            address.setCounty((County) cmbCounties.getSelectedItem().getValue());
//        } else {
//            address.setCountyName("NO APPLY");
//        }
//        if (cmbCities.isVisible()) {// State have cities
//            address.setCity((City) cmbCities.getSelectedItem().getValue());
//        } else {
//            address.setCityName(txtCityName.getText());
//        }
//        address.setAddress(txtAddress.getText());
//        address.setZipCode(txtZipCode.getText());
//        return address;
//    }
//
//    private boolean validateAddress() {
//        boolean valid = false;
//
//        if (!cmbStates.isVisible() && txtStateName.getText().isEmpty()) {
//            txtStateName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (!cmbCities.isVisible() && txtCityName.getText().isEmpty()) {
//            txtCityName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtAddress.getText().isEmpty()) {
//            txtAddress.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtZipCode.getText().isEmpty()) {
//            txtZipCode.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else {
//            valid = true;
//        }
//        return valid;
//    }
//
//    public void loadPaymentMethods() {
//        List<PaymentInfo> paymentInfos;
//        lbxPaymentOptions.getItems().clear();
//        try {
//            paymentInfos = transactionEJB.getPaymentInfoByAccountId(currentAccount.getId(), true);
//            for (PaymentInfo pInfo : paymentInfos) {
//                Listitem item = new Listitem();
//                item.setValue(pInfo);
//                item.appendChild(new Listcell(pInfo.getCreditCardName()));
//                item.appendChild(new Listcell(pInfo.getCreditcardType().getName()));
//                item.appendChild(new Listcell(CreditCardUtils.hideCreditCardNumber(pInfo.getCreditCardNumber())));
//                item.appendChild(desactivateButton(item));
//                item.setParent(lbxPaymentOptions);
//            }
//            btnExistingPaymentPurchase.setVisible(true);
//
//        } catch (EmptyListException ex) {
//
//            btnNewPaymentCancel.setVisible(false);
//            btnNewPaymentPurchase.setVisible(true);
//            btnExistingPaymentPurchase.setVisible(false);
//            vltExistingPayment.setVisible(false);
//            vltNewPayment.setVisible(true);
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//
//        }
//    }
//
//    private Listcell desactivateButton(final Listitem listItem) {
//
//        Listcell cell = new Listcell();
//        cell.setValue("");
//        final Button button = new Button();
//        button.setTooltiptext(Labels.getLabel("sp.common.actions.drop"));
//        button.setClass("open orange");
//        button.setImage("/images/icon-disable.png");
//        button.addEventListener("onClick", new EventListener() {
//
//            public void onEvent(Event event) throws Exception {
//                desactivatePayment(listItem);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
//
//    private void desactivatePayment(Listitem listItem) {
//        try {
//            PaymentInfo pInfo = (PaymentInfo) listItem.getValue();
//            pInfo.setEndingDate(new Timestamp(new java.util.Date().getTime()));
//            request.setParam(pInfo);
//            transactionEJB.savePaymentInfo(request);
//            loadPaymentMethods();
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void onClick$btnNewPaymentPurchase() {
//        try {
//            this.clearMessage();
//            if (validatePaymentInfo() && validateAddress()) {
//                paymentInfo = getPaymentInfo();
//                purchaseBalance(paymentInfo);
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void onClick$btnExistingPaymentPurchase() {
//        try {
//            this.clearMessage();
//
//            if (lbxPaymentOptions.getSelectedItem() == null) {
//                this.showMessage("sp.error.payment.notSelected", true, null);
//            } else if (!cbxTerms.isChecked()) {
//                this.showMessage("sp.error.field.terms", true, null);
//            } else {
//                paymentInfo = (PaymentInfo) lbxPaymentOptions.getSelectedItem().getValue();
//                purchaseBalance(paymentInfo);
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//
//    }
//
//    public void onClick$btnNewPayment() {
//
//        vltExistingPayment.setVisible(false);
//        vltNewPayment.setVisible(true);
//        btnExistingPaymentPurchase.setVisible(false);
//        btnNewPaymentPurchase.setVisible(true);
//        btnNewPaymentCancel.setVisible(true);
//    }
//
//    public void onClick$btnNewPaymentCancel() {
//
//        vltExistingPayment.setVisible(true);
//        vltNewPayment.setVisible(false);
//        btnNewPaymentCancel.setVisible(false);
//        btnExistingPaymentPurchase.setVisible(true);
//        btnNewPaymentPurchase.setVisible(false);
//    }
//
//    public void onClick$btnInvoce() {
//        Sessions.getCurrent().setAttribute("transaction", successFulTransaction);
//        Executions.getCurrent().sendRedirect("invoiceView.zul", "_blank");
//
//    }
//
//    public void onClick$btnNewPurchase() {
//        Executions.getCurrent().sendRedirect(null);
//
//    }
//}
