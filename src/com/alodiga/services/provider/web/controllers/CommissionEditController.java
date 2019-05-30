//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.AccountProduct;
//import com.alodiga.services.provider.commons.models.TopUpCalculation;
//import com.alodiga.services.provider.commons.models.Product;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.sql.Timestamp;
//import java.util.List;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Doublebox;
//import org.zkoss.zul.Label;
//
//public class CommissionEditController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private UserEJB userEJB;
//    private Label lblLogin;
//    private Label lblEmail;
//    private Label lblAccount;
//    private Label lblDatePin;
//    private Label lblDateTopUp;
//    private Label lblResponsiblePin;
//    private Label lblResponsibleTopUp;
//    private Doublebox dbxPercentPin;
//    private Doublebox dbxPercentTopUp;
//    private Account accountParam;
//    private Button btnSave;
//    private List<AccountProduct> accountProducts = null;
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
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
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
//    private void loadFields(List<AccountProduct> accountProducts) {
//        try {
//            if (accountProducts == null) {
//                lblAccount.setValue(accountParam.getFullName());
//                lblResponsiblePin.setValue("");
//                lblResponsibleTopUp.setValue("");
//                lblLogin.setValue(accountParam.getLogin());
//                lblEmail.setValue(accountParam.getEmail());
//                lblDatePin.setValue("");
//                lblDateTopUp.setValue("");
//                dbxPercentPin.setValue(0);
//                dbxPercentTopUp.setValue(0);
//            } else {
//                lblAccount.setValue(accountParam.getFullName());
//                lblLogin.setValue(accountParam.getLogin());
//                lblEmail.setValue(accountParam.getEmail());
//
//
//                for(AccountProduct accountProduct:accountProducts){
//
//                    //lblResponsible.setValue(topUpCalculation.getUser().getFirstName() + " " + topUpCalculation.getUser().getLastName());
//                    if(accountProduct.getProduct().getId().equals(Product.ELECTRONIC_PIN_ID)){
//                        dbxPercentPin.setValue(accountProduct.getCommission());
//                        lblResponsiblePin.setValue(accountProduct.getUser().getFirstName() + " " + accountProduct.getUser().getLastName());
//                        lblDatePin.setValue(GeneralUtils.date2String(accountProduct.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//                    }
//                    else if(accountProduct.getProduct().getId().equals(Product.TOP_UP_PRODUCT_ID)){
//                        dbxPercentTopUp.setValue(accountProduct.getCommission());
//                        lblResponsibleTopUp.setValue(accountProduct.getUser().getFirstName() + " " + accountProduct.getUser().getLastName());
//                        lblDateTopUp.setValue(GeneralUtils.date2String(accountProduct.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//
//                    }
//                
//                }
//            }
//
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void blockFields() {
//        dbxPercentPin.setDisabled(true);
//        dbxPercentTopUp.setDisabled(true);
//        btnSave.setVisible(false);
//    }
//
//    public boolean validateFields() {
//        if (dbxPercentPin.getValue() == null) {
//            dbxPercentPin.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dbxPercentPin.getValue() < 0) {
//            dbxPercentPin.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else if (dbxPercentPin.getValue() > 100) {
//            dbxPercentPin.setFocus(true);
//            this.showMessage("sp.product.commission.percent.error", true, null);
//        } else if (dbxPercentTopUp.getValue() == null) {
//            dbxPercentTopUp.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dbxPercentTopUp.getValue() < 0) {
//            dbxPercentTopUp.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else if (dbxPercentTopUp.getValue() > 100) {
//            dbxPercentTopUp.setFocus(true);
//            this.showMessage("sp.product.commission.percent.error", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    private void save(Account account) {
//        try {
//            if (accountProducts != null) {//MODIFICAR UN TopUpCalculation EXISTENTE
//
//                    for(AccountProduct accountProduct : accountProducts){
//                        
//                        AccountProduct ap = new AccountProduct();
//                        ap.setAccount(account);
//                        ap.setResidualCommission(0f);
//                        
//                        if(dbxPercentPin.getValue().floatValue() != accountProduct.getCommission() && accountProduct.getProduct().getId().equals(Product.ELECTRONIC_PIN_ID))
//                        {
//                            accountProduct.setEndingDate(new Timestamp(new java.util.Date().getTime()));
//                            request.setParam(accountProduct);
//                            accountProduct = userEJB.saveAccountProduct(request);
//                            ap.setUser(AccessControl.loadCurrentUser());
//                            ap.setProduct(accountProduct.getProduct());
//                            ap.setCommission(dbxPercentPin.getValue().floatValue());
//                            ap.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//                            request.setParam(ap);
//                            ap = userEJB.saveAccountProduct(request);
//                        }
//                        else if(dbxPercentTopUp.getValue().floatValue() != accountProduct.getCommission() && accountProduct.getProduct().getId().equals(Product.TOP_UP_PRODUCT_ID))
//                        {
//                            accountProduct.setEndingDate(new Timestamp(new java.util.Date().getTime()));
//                            request.setParam(accountProduct);
//                            accountProduct = userEJB.saveAccountProduct(request);
//                            ap.setUser(AccessControl.loadCurrentUser());
//                            ap.setProduct(accountProduct.getProduct());
//                            ap.setCommission(dbxPercentTopUp.getValue().floatValue());
//                            ap.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//                            request.setParam(ap);
//                            ap = userEJB.saveAccountProduct(request);
//                        }
//
//                    }
//                this.showMessage("sp.common.save.success", false, null);
//            }else{
//                AccountProduct ap = new AccountProduct();
//                ap.setResidualCommission(0f);
//                ap.setAccount(account);
//                ap.setUser(AccessControl.loadCurrentUser());
//                Product product = new Product();
//                product.setId(Product.ELECTRONIC_PIN_ID);
//                ap.setProduct(product);
//                ap.setCommission(dbxPercentPin.getValue().floatValue());
//                ap.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//                request.setParam(ap);
//                ap = userEJB.saveAccountProduct(request);
//                ap = new AccountProduct();
//                ap.setResidualCommission(0f);
//                ap.setAccount(account);
//                ap.setUser(AccessControl.loadCurrentUser());
//                ap.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//                product = new Product();
//                product.setId(Product.TOP_UP_PRODUCT_ID);
//                ap.setProduct(product);
//                ap.setCommission(dbxPercentTopUp.getValue().floatValue());
//                request.setParam(ap);
//                ap = userEJB.saveAccountProduct(request);
//                this.showMessage("sp.common.save.success", false, null);
//            }
//            /*
//            accountProducts = new TopUpCalculation();
//            accountProducts.setAccount(account);
//            accountProducts.setUser(AccessControl.loadCurrentUser());
//            accountProducts.setPercent(dbxPercent.getValue().floatValue());
//            accountProducts.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
//            accountProducts = topUpEJB.saveTopUpCalculation(topUpCalculation);
//            lblDate.setValue(GeneralUtils.date2String(topUpCalculation.getBeginningDate(), GeneralUtils.FORMAT_DATE_USA));
//            lblResponsible.setValue(topUpCalculation.getUser().getFirstName() + " " + topUpCalculation.getUser().getLastName());
//            this.showMessage("sp.common.save.success", false, null);
//             */
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
//                accountProducts = userEJB.loadAccountProductCalculationByAccountId(accountParam.getId());
//            } catch (Exception ex) {
//            }
//            switch (eventType) {
//                case WebConstants.EVENT_EDIT:
//                    loadFields(accountProducts);
//                    break;
//                case WebConstants.EVENT_VIEW:
//                    loadFields(accountProducts);
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
