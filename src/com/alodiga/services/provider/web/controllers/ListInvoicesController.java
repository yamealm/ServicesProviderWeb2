//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.BillingsEJB;
//import com.alodiga.services.provider.commons.ejbs.ProductEJB;
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.managers.ContentManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Invoice;
//import com.alodiga.services.provider.commons.models.InvoiceStatus;
//import com.alodiga.services.provider.commons.models.Payment;
//import com.alodiga.services.provider.commons.models.PaymentType;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Product;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.components.ChangeStatusButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//public class ListInvoicesController extends GenericAbstractListController<Product> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxRecords;
//    private Textbox txtAlias;
//    private ProductEJB productEJB;
//    private List<Product> products ;
//    private User currentUser;
//    private Profile currentProfile;
//    private Textbox txtIdentifier;
//    private UserEJB userEJB ;
//    private Invoice invoiceParam ;
////    private Invoice invoice = null;
//    private Textbox txtNumInv;
//    private Textbox txtEnterprises;
//    private Textbox txtFullName;
//    private Textbox txtBusinessName;
//    private Textbox txtNumDoc;
//    private Textbox txtPayment;
//    private Combobox cmbPaymentTypes;
//    private UtilsEJB utilsEJB;
//    private BillingsEJB billingsEJB;
//    private TransactionEJB transactionEJB;
//    private Timestamp timeStampDate;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//
//        invoiceParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Invoice) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//    }
//
//    @Override
//    public void checkPermissions() {
//        try {
//            
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    public void startListener() {
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//        try {
//            adminPage = "listInvoices.zul";
//            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            transactionEJB=(TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            billingsEJB = (BillingsEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLINGS_EJB);
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//        //loadFields(invoiceParam);
//       loadPayments(null);
//    }
//
//    private void loadFields(Invoice invoice) {
//        if (invoice != null) {
//
//             if(invoice.getInvoiceStatus().getName().equals("POR PAGAR")){
//
//            txtNumInv.setText(String.valueOf(invoice.getId()));
//            txtEnterprises.setText(invoice.getAccount().getEnterprise().getName());
//            txtFullName.setText(invoice.getAccount().getFullName());
//            txtBusinessName.setText(invoice.getAccount().getBusinessName());
//            txtPayment.setText(String.valueOf(invoice.getTotalToPay()));
//            }else{
//                this.showMessage("sp.common.page.cancel.invoice", true, null);
//            }
//        } else {
//
//       
//           
//
//        }
//    }
//
//        private void loadPayments(Payment payment) {
//        try {
//
//            List<PaymentType> paymentTypes = utilsEJB.getPaymentTypes();
//            for (int i = 0; i < paymentTypes.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(paymentTypes.get(i));
//                item.setLabel(paymentTypes.get(i).getName());
//                item.setParent(cmbPaymentTypes);
//                if (payment != null && paymentTypes.get(i).getId().equals(payment.getId())) {
//                    cmbPaymentTypes.setSelectedItem(item);
//                }
//            }
//        } catch (Exception ex) {
//             showError(ex);
//             ex.printStackTrace();
//             this.showMessage("sp.error.paymentInfo", true, null);
//
//
//        }
//    }
//
//    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
//        try {
//            Product product = (Product) listItem.getValue();
//            button.changeImageStatus(product.getEnabled());
//            product.setEnabled(!product.getEnabled());
//            listItem.setValue(product);
//            request.setParam(product);
//            productEJB.saveProduct(request);
//            AccessControl.saveAction(Permission.CHANGE_PRODUCT_STATUS, "changeStatus product = " + product.getId() + " to status = " + !product.getEnabled());
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void getData() {
//        products = new ArrayList<Product>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            request.setAuditData(null);
//            products = productEJB.getProducts(request);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnClear() throws InterruptedException {
//        txtNumInv.setText("");
//        txtNumDoc.setText("");
//        txtPayment.setText("");
//        txtEnterprises.setText("");
//        txtFullName.setText("");
//        txtBusinessName.setText("");
//        txtPayment.setText("");
//
//
//
//    }
//
//    public void onClick$btnSave() throws GeneralException, ParseException {
//          Date date = new Date();
//           java.sql.Timestamp timeStampDate = new  Timestamp(date.getTime());
//            
//                
//        if (validateEmpty()) {
//
//
//            Account account = null;
//            try {
//                EJBRequest request = new EJBRequest();
//                Float totalP = 0F;
//                account = invoiceParam.getAccount();
//                Float AccountBalance = account.getBalance();
//                Float totalPayment=  Float.parseFloat(txtPayment.getValue());
//
//                totalP=AccountBalance - totalPayment;
//                account.setBalance(totalP);
//                request.setParam(account);
//                account = userEJB.saveAccount(request);
//
//                Payment payment = new Payment();
//                payment.setInvoice(invoiceParam);
//                payment.setPaymentType((PaymentType) cmbPaymentTypes.getSelectedItem().getValue());
//                payment.setNumDeposit(txtNumDoc.getValue());
//                payment.setAmount(Float.valueOf(txtPayment.getValue()));
//                payment.setCreationDate(timeStampDate);
//                payment.setStatus(true);
//                payment.setAccount(account);
//                transactionEJB.savePayment(payment);
//                EJBRequest request3 = new EJBRequest();
//                InvoiceStatus invoiceStatus = new InvoiceStatus();
//                invoiceStatus = ContentManager.getInstance().getInvoiceStatusById(InvoiceStatus.PAGADA);
//                invoiceParam.setInvoiceStatus(invoiceStatus);
//
//                request3.setParam(invoiceParam);
//                invoiceParam = userEJB.saveInvoice(request3);
//
//
//              if(account.getEnabled() == false){
//                   billingsEJB.analyzeActivationAccounts(account);
//               }
//
//                
//
//            } catch (NullParameterException ex) {
//                ex.printStackTrace();
//                showError(ex);
//                this.showMessage("sp.error.field.error.save.NPE", true, null);
//                Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//                
//            } catch (GeneralException ex) {
//                ex.printStackTrace();
//                showError(ex);
//                this.showMessage("sp.error.field.error.save.GE", true, null);
//                Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//                
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                showError(ex);
//                this.showMessage("sp.error.field.cannotNull", true, null);
//                Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//
//            }
//
//        }
//            Executions.getCurrent().sendRedirect(adminPage);
//    }
//
//
//
//
//
//    public Boolean validateEmpty() {
//        if (txtFullName.getText().isEmpty()) {
//            txtFullName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtBusinessName.getText().isEmpty()) {
//            txtBusinessName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtNumInv.getText().isEmpty()) {
//            txtNumInv.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtPayment.getText().isEmpty()) {
//            txtPayment.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtNumDoc.getText().isEmpty()) {
//            txtNumDoc.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtEnterprises.getText().isEmpty()) {
//            txtNumDoc.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    public void onClick$btnSearch() throws InterruptedException {
//        //initialize();
//        if (txtNumInv.getText().isEmpty()) {
//            txtNumInv.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else {
//            try {
//                Long nInvoice = 0L;
//                String numIvoice = !txtNumInv.getText().isEmpty() ? txtNumInv.getText() : null;
//                nInvoice = Long.valueOf(numIvoice);
//                try {
//                    //invoiceParam = userEJB.getInvoiceByID(nInvoice);
//                    invoiceParam = transactionEJB.getInvoiceById(nInvoice);
//                    loadFields(invoiceParam);
//                } catch (EmptyListException ex) {
//                    showError(ex);
//                    this.showMessage("sp.error.field.error.save.ELE", true, null);
//                    Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NullParameterException ex) {
//                    showError(ex);
//                    this.showMessage("sp.error.field.error.save.NPE", true, null);
//                    Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (RegisterNotFoundException ex) {
//                    showError(ex);
//                    this.showMessage("sp.error.field.error.save.NFE", true, null);
//                    Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//            } catch (GeneralException ex) {
//                 showError(ex);
//                 this.showMessage("sp.error.field.error.save.NFE", true, null);
//                Logger.getLogger(ListInvoicesController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//
//        }
//
//    }
//
//    public void loadList(List<Product> list) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public List<Product> getFilteredList(String filter) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnAdd() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
