//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.TopUpCalculation;
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
//public class TopUpCommissionEditController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private TopUpProductEJB topUpEJB;
//    private Label lblLogin;
//    private Label lblEmail;
//    private Label lblAccount;
//    private Label lblDate;
//    private Label lblResponsible;
//    private Doublebox dbxPercent;
//    private Account accountParam;
//    private TopUpCalculation topUpCalculation = null;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.topUpCommission");
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
//            topUpEJB = (TopUpProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.TOP_UP_EJB);
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
//    private void loadFields(TopUpCalculation topUpCalculation) {
//        try {
//            if (topUpCalculation == null) {
//                lblAccount.setValue(accountParam.getFullName());
//                lblResponsible.setValue("");
//                lblLogin.setValue(accountParam.getLogin());
//                lblEmail.setValue(accountParam.getEmail());
//                lblDate.setValue("");
//                dbxPercent.setValue(0f);
//            } else {
//                lblAccount.setValue(topUpCalculation.getAccount().getFullName());
//                lblLogin.setValue(topUpCalculation.getAccount().getLogin());
//                lblEmail.setValue(topUpCalculation.getAccount().getEmail());
//                lblDate.setValue(GeneralUtils.date2String(topUpCalculation.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//                lblResponsible.setValue(topUpCalculation.getUser().getFirstName() + " " + topUpCalculation.getUser().getLastName());
//                dbxPercent.setValue(topUpCalculation.getPercent());
//            }
//
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void blockFields() {
//        dbxPercent.setDisabled(true);
//    }
//
//    public boolean validateFields() {
//        if (dbxPercent.getValue() == null) {
//            dbxPercent.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dbxPercent.getValue() < 0) {
//            dbxPercent.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else if (dbxPercent.getValue() > 100) {
//            dbxPercent.setFocus(true);
//            this.showMessage("sp.product.commission.percent.error", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    private void save(Account account) {
//        try {
//            if (topUpCalculation != null) {//MODIFICAR UN TopUpCalculation EXISTENTE
//                topUpCalculation.setPercent(dbxPercent.getValue().floatValue());
//                topUpCalculation.setEndingDate(new Timestamp(new java.util.Date().getTime()));
//                topUpCalculation.setUser(AccessControl.loadCurrentUser());
//                topUpCalculation = topUpEJB.saveTopUpCalculation(topUpCalculation);
//
//            }
//            topUpCalculation = new TopUpCalculation();
//            topUpCalculation.setAccount(account);
//            topUpCalculation.setUser(AccessControl.loadCurrentUser());
//            topUpCalculation.setPercent(dbxPercent.getValue().floatValue());
//            topUpCalculation.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//            topUpCalculation = topUpEJB.saveTopUpCalculation(topUpCalculation);
//            lblDate.setValue(GeneralUtils.date2String(topUpCalculation.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//            lblResponsible.setValue(topUpCalculation.getUser().getFirstName() + " " + topUpCalculation.getUser().getLastName());
//            this.showMessage("sp.common.save.success", false, null);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnSave() {
//        if (validateFields()) {
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
//                topUpCalculation = topUpEJB.loadTopUpCalculationByAccountId(accountParam.getId());
//            } catch (RegisterNotFoundException ex) {
//            }
//            switch (eventType) {
//                case WebConstants.EVENT_EDIT:
//                    loadFields(topUpCalculation);
//                    break;
//                case WebConstants.EVENT_VIEW:
//                    loadFields(topUpCalculation);
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
