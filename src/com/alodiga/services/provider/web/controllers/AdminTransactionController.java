//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.models.Language;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.Utils;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Div;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Row;
//
//public class AdminTransactionController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Label lblName;
//    private Label lblNumber;
//    private Label lblLogin;
//    private Label cmbTransactionType;
//    private Label dbxAmount;
//    private Label dbxTaxAmount;
//    private Label dbxPromotionAmount;
//    private Label dtbCreationDate;
//    private Label lblStatus;
//    private Label lblDescription;
//    private Label lblAditional;
//    private Label lblExternalId;
//    private Label lblReferenceCode;
//    private Label lblRechargeId;
//    private Label lblPinSerial;
//    private Label lblId;
//    private Label lblNameTopup;
//    private Label lblDescriptionTopup;
//    private Label lblmobileOperator;
//    private Label lblProvider;
//    private Label lblDenominations;
//    private Label lblCommissions;
//    private Label lblCreationDate;
//    private Label lblReferenceCodeTopup;
//    private Label lblStatusTopup;
//    private Label lblIdBillpayment;
//    private Label lblNameBillpayment;
//    private Label lblDescriptionBillpayment;
//    private Label lblReferenceCodeBillpayment;
//    private Label lblProviderBillpayment;
//    private Label lblCountry;
//    private Label lblexchangeRate;
//    private Label lblMinAmount;
//    private Label lblMaxAmount;
//    private Label lblcurrencyCode;
//    private Label lblStatusBillpayment;
//    private Row rowrechargeid;
//    private Row rowpinserial;
//    private Div divTopup;
//    private Div divBillpayment;
//    private TopUpProductEJB topUpProductEJB = null;
//    private TransactionEJB transactionEJB = null;
//    private UtilsEJB utilsEJB = null;
//    private Language Language;
//    private Transaction transactionParam;
//    private Button btnSave;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        transactionParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Transaction) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.transaction");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, "sp.crud.transaction");
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            topUpProductEJB = (TopUpProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.TOP_UP_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            loadData();
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadFields(Transaction transaction) {
//        try {
//            lblNumber.setValue(transaction.getId().toString());
//            lblLogin.setValue(transaction.getAccount().getLogin());
//            lblName.setValue(transaction.getAccount().getFullName());
//            cmbTransactionType.setValue(Utils.getTransactionTypeName(transaction.getTransactionType().getId()));
//            dtbCreationDate.setValue(transaction.getCreationDate().toString());
//            dbxAmount.setValue(transaction.getTotalAmount().toString());
//            dbxTaxAmount.setValue(transaction.getTotalTax().toString());
//            dbxPromotionAmount.setValue(transaction.getPromotionAmount().toString());
//            lblStatus.setValue(Utils.getTransactionStatusName(transaction.getTransactionStatus().getName()));
//            lblDescription.setValue(transaction.getDescription().toString());
//            lblAditional.setValue(transaction.getAdditional().toString());
//            lblExternalId.setValue(transaction.getExternalID().toString());
//            lblReferenceCode.setValue(transaction.getReferenceProviderCode().toString());
//            if (transaction.getRecharge() != null) {
//                rowrechargeid.setVisible(true);
//                lblRechargeId.setValue(transaction.getRecharge().getId().toString());
//            }
//            if (transaction.getPinSerial() != null) {
//                rowpinserial.setVisible(true);
//                lblPinSerial.setValue(transaction.getPinSerial().toString());
//            }
//            if (transaction.getTopUpProduct() != null) {
//                divTopup.setVisible(true);
//                lblId.setValue(transaction.getTopUpProduct().getId().toString());
//                lblNameTopup.setValue(transaction.getTopUpProduct().getName().toString());
//                lblDescriptionTopup.setValue(transaction.getTopUpProduct().getDescription().toString());
//                lblmobileOperator.setValue(transaction.getTopUpProduct().getMobileOperator().getName().toString());
//                lblProvider.setValue(transaction.getTopUpProduct().getProvider().getName().toString());
//                lblDenominations.setValue(transaction.getTopUpProduct().getProductDenomination().getAmount().toString());
//                lblCommissions.setValue(transaction.getTopUpProduct().getCommissionPercent().toString());
//                lblCreationDate.setValue(transaction.getTopUpProduct().getCreationDate().toString());
//                lblReferenceCodeTopup.setValue(transaction.getTopUpProduct().getReferenceCode().toString());
//                lblStatusTopup.setValue(Utils.getStatus(transaction.getTopUpProduct().getEnabled()));
//            }
//            if (transaction.getBillPaymentProduct() != null) {
//                divBillpayment.setVisible(true);
//                lblIdBillpayment.setValue(transaction.getBillPaymentProduct().getId().toString());
//                lblNameBillpayment.setValue(transaction.getBillPaymentProduct().getName().toString());
//                lblDescriptionBillpayment.setValue(transaction.getBillPaymentProduct().getDescription().toString());
//                lblReferenceCodeBillpayment.setValue(transaction.getBillPaymentProduct().getReferenceCode().toString());
//                lblProviderBillpayment.setValue(transaction.getBillPaymentProduct().getProvider().getName().toString());
//                lblCountry.setValue(transaction.getBillPaymentProduct().getCountry().getName().toString());
//                lblexchangeRate.setValue(transaction.getBillPaymentProduct().getExchangeRate().toString());
//                lblMinAmount.setValue(transaction.getBillPaymentProduct().getMinAmount().toString());
//                lblMaxAmount.setValue(transaction.getBillPaymentProduct().getMaxAmount().toString());
//                lblcurrencyCode.setValue(transaction.getBillPaymentProduct().getCurrencyCode().toString());
//                lblStatusBillpayment.setValue(Utils.getStatus(transaction.getBillPaymentProduct().getEnabled()));
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//     }
//
//    @Override
//    public void loadData() {
//        switch (eventType) {
//            case WebConstants.EVENT_ADD:
////                loadCountries(null);
//                break;
//            case WebConstants.EVENT_VIEW:
//                loadFields(transactionParam);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void blockFields() {
////        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
