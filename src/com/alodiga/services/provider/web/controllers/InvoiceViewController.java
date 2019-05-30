//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Pin;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.utils.CreditCardUtils;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import java.text.ParseException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Row;
//import org.zkoss.zul.Vlayout;
//import org.zkoss.zul.Window;
//
//public class InvoiceViewController extends GenericAbstractController {
//
//    private static final long serialVersionUID = 1L;
//    private Transaction transaction;
//    private String currencySymbol = "";
//    private Account currentAccount;
//    private Vlayout vltPinOperations;
//    private Vlayout vltPurchaseBalance;
//    private Label lblTransactionNumber1;
//    private Label lblDate1;
//    private Label lblPromotionalAmount1;
//    private Label lblTax1;
//    private Label lblTransactionAmount1;
//    private Label lblProductValue1;
//    private Label lblProductValue2;
//    private Label lblPhoneNumberValue1;
//    private Label lblSerial1;
//    private Label lblSecret1;
//    private Label lblInvCCType;
//    private Label lblInvCCName;
//    private Label lblInvCCNumber;
//    private Row row1;
//    private Row row2;
//    private Row row3;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//        loadData();
//
//    }
//
//    @Override
//    public void initialize() {
//        try {
//            transaction = (Transaction) Sessions.getCurrent().getAttribute("transaction");
//            currentAccount = AccessControl.loadCurrentAccount();
//            currencySymbol = currentAccount.getEnterprise().getCurrency().getSymbol();
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private void loadData() {
//        try {
//            if (transaction != null) {
//                lblTransactionNumber1.setValue(transaction.getId().toString());
//                lblDate1.setValue(GeneralUtils.date2String(transaction.getCreationDate(), GeneralUtils.FORMAT_DATE_USA));
//                lblPromotionalAmount1.setValue(GeneralUtils.getCorrectAmount(currencySymbol, transaction.getPromotionAmount(), 2));
//                lblTax1.setValue(GeneralUtils.getCorrectAmount(currencySymbol, transaction.getTotalTax(), 2));
//                lblTransactionAmount1.setValue(GeneralUtils.getCorrectAmount(currencySymbol, transaction.getTotalAmount(), 2));
//
//                if (transaction.getTransactionType().getId().equals(TransactionType.PIN_PURCHASE) || transaction.getTransactionType().getId().equals(TransactionType.PIN_RECHARGE)) {
//                    vltPinOperations.setVisible(true);
//
//                    Pin pin = (Pin) Sessions.getCurrent().getAttribute("pin");
//                    lblProductValue1.setValue(transaction.getTransactionType().getId().equals(TransactionType.PIN_PURCHASE) ? Labels.getLabel("sp.transactionType.purchaseProduct") : Labels.getLabel("sp.transactionType.rechargeProduct"));
//                    lblSerial1.setValue(pin.getSerial());
//                    lblSecret1.setValue(pin.getSecret());
//                    if (Sessions.getCurrent().getAttribute("pinFree") != null) {
//                        lblPhoneNumberValue1.setValue(Sessions.getCurrent().getAttribute("pinFree").toString());
//                    }
//
//                } else if (transaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)) {
//                    vltPurchaseBalance.setVisible(true);
//                    row1.setVisible(true);
//                    row2.setVisible(true);
//                    row3.setVisible(true);
//                    lblProductValue2.setValue(Labels.getLabel("transactionType.purchaseBalance"));
//                    lblInvCCType.setValue(transaction.getPaymentInfo().getCreditcardType().getName());
//                    lblInvCCName.setValue(transaction.getPaymentInfo().getCreditCardName());
//                    lblInvCCNumber.setValue(CreditCardUtils.hideCreditCardNumber(transaction.getPaymentInfo().getCreditCardNumber()));
//
//                }
//
//            }
//        } catch (ParseException ex) {
//            Logger.getLogger(InvoiceViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void onClick$btnClose() {
//
//        Window win = (Window) this.self;
//        win.detach();
//    }
//}
