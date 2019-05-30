//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.MaxAmountBalanceException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.BalanceHistory;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Textbox;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import org.zkoss.zul.Decimalbox;
//import org.zkoss.zul.Div;
//import org.zkoss.zul.Grid;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Rows;
//import org.zkoss.zul.Vlayout;
//
//
//    public class BalanceAdjusmentViewController extends GenericAbstractAdminController {
//    //public class BalanceAdjusmentViewController extends GenericAbstractController implements GenericSPController {
//
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Textbox txtLogin;
//    private Textbox txtReason;
//    private Textbox txtReasonLimit;
//    private Label lblAccountValue;
//    private Label lblDateValue2;
//    private Label lblAccountValue2;
//    private Label lblDateValue;
//    private Label lblCurrentBalanceValue;
//    private Label lblCurrentLimitValue;
//    private Decimalbox dbxNewBalance;
//    private Decimalbox dbxNewLimit;
//    private UserEJB userEJB = null;
//    private TransactionEJB transactionEJB = null;
//    private BalanceHistory balanceHistory = null;
//    private Account account = null;
//    private Vlayout vltBalanceHistory;
//    private Vlayout vltBalanceLimitHistory;
//    private User currentUser = null;
//    private Account currentAccount = null;
//    private Div saveButton;
//    private Grid grdBalanceHistory;
//    private Grid grdBalanceLimitHistory;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//        transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//
//        try {
//            currentUser = AccessControl.loadCurrentUser();
//            currentAccount = AccessControl.loadCurrentAccount();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public boolean validateEmpty() {
//        boolean valid = false;
//
//
//        if (txtLogin.getText().isEmpty()) {
//            txtLogin.setFocus(true);
////            lblInfo.setValue(Labels.getLabel("sp.error.field.cannotNull"));
//            this.showMessage("sp.error.field.cannotNull", false, null);
//        } else {
//            valid = true;
//        }
//        return valid;
//    }
//
//    public void onClick$btnFind() {
//        divInfo.setVisible(false);
//        if (validateEmpty()) {
//            try {
//                account = userEJB.loadAccountByLogin(txtLogin.getText());
//                if (account.getIsPrePaid()) {
//                    balanceHistory = userEJB.loadLastBalanceHistoryByAccountId(account.getId());
//                    lblAccountValue.setValue(account.getFullName());
//                    lblDateValue.setValue(balanceHistory.getDate().toString());
//                    lblCurrentBalanceValue.setValue(balanceHistory.getCurrentAmount().toString());
//                    dbxNewBalance.setValue(BigDecimal.valueOf(balanceHistory.getCurrentAmount()));
//                    vltBalanceHistory.setVisible(true);
//                    vltBalanceLimitHistory.setVisible(false);
//                    saveButton.setVisible(true);
//                } else {
//                    lblAccountValue2.setValue(account.getFullName());
//                    lblDateValue2.setValue(account.getCreationDate().toString());
//                    lblCurrentLimitValue.setValue(account.getCrediLimit().toString());
//                    dbxNewLimit.setValue(BigDecimal.valueOf(account.getCrediLimit()));
//                    vltBalanceLimitHistory.setVisible(true);
//                    vltBalanceHistory.setVisible(false);
//                    saveButton.setVisible(true);
//
//                }
//            } catch (RegisterNotFoundException ex) {
//                ex.printStackTrace();
//                 vltBalanceLimitHistory.setVisible(false);
//                 vltBalanceHistory.setVisible(false);
//                 saveButton.setVisible(false);
//                 this.showMessage("sp.error.empty.list", false, ex);
////                lblInfo.setValue(Labels.getLabel("sp.error.empty.list"));
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                this.showMessage("sp.error.general", false, ex);
////                lblInfo.setValue(Labels.getLabel("sp.error.general"));
//            }
//        }
//    }
//
//    public void onClick$btnAdjustBalance() {
//        divInfo.setVisible(true);
//
//        float amount = dbxNewBalance.getValue().floatValue();
//        float amountLimit = dbxNewLimit.getValue().floatValue();
//        if (amount >= 0) {
//             if (account.getIsPrePaid()) {
//                if (amount != balanceHistory.getCurrentAmount()) {
//                    try {
//
//                        BalanceHistory newBalanceHistory = new BalanceHistory();
//                        newBalanceHistory.setAccount(account);
//                        newBalanceHistory.setOldAmount(balanceHistory.getCurrentAmount());
//                        newBalanceHistory.setCurrentAmount(amount);
//                        newBalanceHistory.setDate(new Timestamp(new java.util.Date().getTime()));
//                        newBalanceHistory.setAdjusmentInfo("Responsable: " + currentUser.getFirstName() + " " + currentUser.getLastName() + " Reason : " + txtReason.getText());
//                        transactionEJB.validateBalance(newBalanceHistory, 0, 0, false);
//                        EJBRequest request = new EJBRequest();
//                        request.setParam(newBalanceHistory);
//                        balanceHistory = userEJB.saveBalanceHistory(request);
//                        this.showMessage("sp.common.balanceAdjusment.successfully", false, null);
//
//                    }
//                    catch (MaxAmountBalanceException ex) {
//                        this.showMessage("sp.error.amountBalance", false, ex);
////                        lblInfo.setValue(Labels.getLabel("sp.error.amountBalance") + ex);
//
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        this.showMessage("sp.error.general", false, ex);
//                    }
//                }
//            }else {
//                 if (amountLimit != account.getCrediLimit()) {
//                    try {
//                        account.setCrediLimit(amountLimit);
//                        EJBRequest request = new EJBRequest();
//                        request.setParam(account);
//                        account = userEJB.saveAccount(request);
//                        this.showMessage("sp.common.balanceAdjusment.successfully", false, null);
//                    } catch (GeneralException ex) {
//                        ex.printStackTrace();
//                    } catch (NullParameterException ex) {
//                        ex.printStackTrace();
//                    }
//                 }
//            }
//        } else {
////            lblInfo.setValue(Labels.getLabel("sp.error.balanceAdjustment.zero"));
////            this.showMessage("sp.error.balanceAdjustment.zero", false, ex);
//        }
//    }
//
//    @Override
//    public void loadPermission(AbstractSPEntity clazz) throws GeneralException {
//        //throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void loadData() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void blockFields() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
