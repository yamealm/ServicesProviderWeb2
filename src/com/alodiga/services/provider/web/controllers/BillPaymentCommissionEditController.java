//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.BillPaymentEJB;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.BillPaymentCalculation;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.sql.Timestamp;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Doublebox;
//import org.zkoss.zul.Label;
//
//public class BillPaymentCommissionEditController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private BillPaymentEJB billPaymentEJB;
//    private Label lblLogin;
//    private Label lblEmail;
//    private Label lblAccount;
//    private Label lblDate;
//    private Label lblResponsible;
//    private Doublebox dbxFee;
//    private Account accountParam;
//    private BillPaymentCalculation billPaymentCalculation = null;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.billPaymentCommission");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, adminView);
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            billPaymentEJB = (BillPaymentEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLPAYMENT_EJB);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//        loadData();
//    }
//
//    @Override
//    public void clearFields() {
//    }
//
//    private void loadFields(BillPaymentCalculation billPaymentCalculation) {
//        try {
//            if (billPaymentCalculation == null) {
//                lblAccount.setValue(accountParam.getFullName());
//                lblResponsible.setValue("");
//                lblLogin.setValue(accountParam.getLogin());
//                lblEmail.setValue(accountParam.getEmail());
//                lblDate.setValue("");
//                dbxFee.setValue(0f);
//            } else {
//                lblAccount.setValue(billPaymentCalculation.getAccount().getFullName());
//                lblLogin.setValue(billPaymentCalculation.getAccount().getLogin());
//                lblEmail.setValue(billPaymentCalculation.getAccount().getEmail());
//                lblDate.setValue(GeneralUtils.date2String(billPaymentCalculation.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//                lblResponsible.setValue(billPaymentCalculation.getUser().getFirstName() + " " + billPaymentCalculation.getUser().getLastName());
//                dbxFee.setValue(billPaymentCalculation.getFee());
//            }
//
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void blockFields() {
//        dbxFee.setDisabled(true);
//    }
//
//    public Boolean validateEmpty() {
//        if (dbxFee.getValue() == null) {
//            dbxFee.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dbxFee.getValue() < 0) {
//            dbxFee.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else {
//            return true;
//        }
//        return false;
//
//    }
//
//    private void save(Account account) {
//        try {
//            if (billPaymentCalculation != null) {//MODIFICAR UN BillPaymentCalculation EXISTENTE
//                billPaymentCalculation.setFee(dbxFee.getValue().floatValue());
//                billPaymentCalculation.setEndingDate(new Timestamp(new java.util.Date().getTime()));
//                billPaymentCalculation.setUser(AccessControl.loadCurrentUser());
//                billPaymentCalculation = billPaymentEJB.saveBillPaymentCalculation(billPaymentCalculation);
//
//            }
//            billPaymentCalculation = new BillPaymentCalculation();
//            billPaymentCalculation.setAccount(account);
//            billPaymentCalculation.setUser(AccessControl.loadCurrentUser());
//            billPaymentCalculation.setFee(dbxFee.getValue().floatValue());
//            billPaymentCalculation.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//            billPaymentCalculation = billPaymentEJB.saveBillPaymentCalculation(billPaymentCalculation);
//            lblDate.setValue(GeneralUtils.date2String(billPaymentCalculation.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//                lblResponsible.setValue(billPaymentCalculation.getUser().getFirstName() + " " + billPaymentCalculation.getUser().getLastName());
//            this.showMessage("sp.common.save.success", false, null);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnSave() {
//        if (validateEmpty()) {
//            switch (eventType) {
//                case WebConstants.EVENT_ADD:
//                    save(null);
//                    break;
//                case WebConstants.EVENT_EDIT:
//                    save(accountParam);
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    public void loadData() {
//        try {
//            try {
//                billPaymentCalculation = billPaymentEJB.loadBillPaymentCalculationByAccountId(accountParam.getId());
//            } catch (RegisterNotFoundException ex) {
//            }
//            switch (eventType) {
//                case WebConstants.EVENT_EDIT:
//                    loadFields(billPaymentCalculation);
//                    break;
//                case WebConstants.EVENT_VIEW:
//                    loadFields(billPaymentCalculation);
//                    blockFields();
//                    break;
//                default:
//                    break;
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//}
