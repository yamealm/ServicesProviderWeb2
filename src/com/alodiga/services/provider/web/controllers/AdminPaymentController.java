//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.managers.ContentManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Invoice;
//import com.alodiga.services.provider.commons.models.InvoiceStatus;
//import com.alodiga.services.provider.commons.models.Payment;
//import com.alodiga.services.provider.commons.models.PaymentType;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.EjbUtils;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Datebox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Row;
//import org.zkoss.zul.Textbox;
//
//public class AdminPaymentController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private UtilsEJB utilsEJB = null;
//    private UserEJB userEJB = null;
//    private TransactionEJB transactionEJB = null;
//    private Account currentAccount;
//    private Combobox cmbPaymentType;
//    private Textbox txtTransactionNumber;
//    private Textbox txtAmount;
//    private Datebox dtbcreationDate;
//    private Combobox cmbNumberInvoice;
//    private Payment paymentParam;
//    private Row NumberInvoice;
//    private Invoice invoice;
//    Map params = null;
// 
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//
//        super.doAfterCompose(comp);
//        paymentParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Payment) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.product");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, "sp.crud.product");
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            currentAccount = AccessControl.loadCurrentAccount();
//           
//            getData();
//            loadNumberInvoices();
//            loadData();
//            } catch  (Exception ex) {
//            showError(ex);
//        }
//    }
//
//
//    private void loadPayments(Payment payment) {
//        try {
//            List<PaymentType> paymentTypes = utilsEJB.getPaymentTypes();
//            for (int i = 0; i < paymentTypes.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(paymentTypes.get(i));
//                item.setLabel(paymentTypes.get(i).getName());
//                item.setParent(cmbPaymentType);
//                if (payment != null && paymentTypes.get(i).getId().equals(payment.getId())) {
//                    cmbPaymentType.setSelectedItem(item);
//                }
//            }
//        } catch (Exception ex) {
//             showError(ex);
//        }
//    }
//
//    private void loadNumberInvoices() {
//        Map params = new HashMap<String, Object>();
//        params.put(QueryConstants.PARAM_ACCOUNT_ID, currentAccount.getId());
//        params.put(QueryConstants.PARAM_STATUS, InvoiceStatus.POR_PAGAR);
//        request.setParams(params);
//        try {
//         if(!currentAccount.getIsPrePaid()){
//             List<Invoice> numberInvoices = utilsEJB.loadInvoicesbyId(request);
////             if (numberInvoices != null && !numberInvoices.isEmpty()) {
//                for  (int i = 0; i < numberInvoices.size(); i++) {
//                  Comboitem item = new Comboitem();
//                  item.setValue(numberInvoices.get(i));
//                  item.setLabel(numberInvoices.get(i).getNumberInvoice());
//                  item.setParent(cmbNumberInvoice);
//            }
//          }
//
//        } catch (Exception ex) {
//             this.showMessage("sp.transaction.empty.invoice", true, null);;
////        }
//       }
//    }
//
//    public void onChange$cmbNumberInvoice() {
//        Invoice invoice = (Invoice) cmbNumberInvoice.getSelectedItem().getValue();
//        loadAmount(invoice);
//    }
//
//    public void loadAmount(Invoice invoice) {
//        try {
//            List<Invoice> numberInvoices = utilsEJB.loadInvoicesbyId(request);
//            if (numberInvoices.size() > 0) {
//                txtAmount.setValue(numberInvoices.get(0).getTotalToPay().toString());
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void clearFields() {
//
//    }
//
//    public void blockFields() {
//
//    }
//
//    public Boolean validateEmpty() {
//        if (cmbPaymentType.getSelectedItem() == null) {
//            cmbPaymentType.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtTransactionNumber.getText().isEmpty()) {
//            txtTransactionNumber.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtAmount.getValue().isEmpty()) {
//            txtAmount.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dtbcreationDate.getValue().compareTo(dtbcreationDate.getValue()) > 0) {
//            dtbcreationDate.setFocus(true);
//            this.showMessage("sp.error.date.invalid", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    public void getData() {
//        loadPayments(paymentParam);
//         if (currentAccount.getIsPrePaid()) {
//                NumberInvoice.setVisible(false);
//            } else {
//                NumberInvoice.setVisible(true);
//            }
//    }
//
//
//    public void onClick$btnSave() {
//        if (validateEmpty()) {
//             savePayment(null);
//        }
//    }
//
//     @Override
//    public void loadData() {
//        switch (eventType) {
//            case WebConstants.EVENT_ADD:
////                loadCountries(null);
//                break;
//            case WebConstants.EVENT_VIEW:
//                loadPayments(paymentParam);
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void savePayment(Payment _payment) {
//            Payment payment = null;
//              try {
//            EJBRequest request =  new EJBRequest();
//            if (_payment != null) {
//                payment = _payment;
//            } else {
//                payment = new Payment();
//            }
//            payment.setPaymentType((PaymentType)cmbPaymentType.getSelectedItem().getValue());
//            payment.setNumDeposit(txtTransactionNumber.getText());
//            payment.setAmount(Float.valueOf(txtAmount.getValue()));
//            payment.setCreationDate(new Timestamp(EjbUtils.getBeginningDate(dtbcreationDate.getValue()).getTime()));
//            payment.setStatus(true);
//            payment.setInvoice(cmbNumberInvoice.getSelectedItem()!=null?(Invoice)cmbNumberInvoice.getSelectedItem().getValue():null);
//            payment.setAccount(currentAccount);
//            request.setParam(payment);
//            payment = transactionEJB.savePayment(payment);
//            if(!currentAccount.getIsPrePaid()){
//            invoice = transactionEJB.getInvoiceById(((Invoice)cmbNumberInvoice.getSelectedItem().getValue()).getId());
//            InvoiceStatus invoiceStatus = ContentManager.getInstance().getInvoiceStatusById(InvoiceStatus.PAGADA);
//            invoice.setInvoiceStatus(invoiceStatus);
//            request.setParam(invoice);
//            userEJB.saveInvoice(request);
//            }
//            this.showMessage("sp.common.save.success", false, null);
//        } catch (Exception ex) {
//           showError(ex);
//        }
//    }
//
//    private Listcell initDeleteButton(final Listitem listItem) {
//        Listcell cell = new Listcell();
//        final Button button = new Button();
//        button.setClass("open gray");
//        button.setImage("/images/icon-remove.png");
//        button.addEventListener("onClick", new EventListener() {
//
//            @Override
//            public void onEvent(Event arg0) throws Exception {
//                listItem.getParent().removeChild(listItem);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
//
//
//}
