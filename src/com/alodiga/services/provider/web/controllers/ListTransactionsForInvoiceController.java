//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Invoice;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.components.ListcellViewButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
//import com.alodiga.services.provider.web.utils.Utils;
//import java.util.Date;
//import java.util.List;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//public class ListTransactionsForInvoiceController extends GenericAbstractListController<Transaction> {
//  
//    
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxReport;
//    private Textbox txtName;
//    private TransactionEJB transactionEJB = null;
//    private UserEJB userEJB = null;
//    Boolean isStoreAll = false;
//    List<Transaction> transactions = null;
//    private List<Account> accounts = null;
//    private Label lblInfo;
//    private UtilsEJB utilsEJB;
//    private Invoice invoiceParam;
//    private Textbox txtNumInv;
//    private Textbox txtTotalPay;
//    private Textbox txtStatus;
//     
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        invoiceParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Invoice) Sessions.getCurrent().getAttribute("object") : null;
//        super.doAfterCompose(comp);
//        initialize();
//
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            adminPage = "adminTransaction.zul";
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            loadTransaction();
//            loadFields(invoiceParam);
//    
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void loadTransaction() {
//        try {
//            transactions = transactionEJB.loadTransactionByInvoice(String.valueOf(invoiceParam.getId()));
//            loadFields(invoiceParam);
//            loadList(transactions);
//
//        } catch (NullParameterException ex) {
//            Logger.getLogger(ListTransactionsForInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (EmptyListException ex) {
//            Logger.getLogger(ListTransactionsForInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (GeneralException ex) {
//            Logger.getLogger(ListTransactionsForInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    public void loadList(List<Transaction> list) {
//        try {
//            
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//                for (Transaction transaction : list) {
//                    item = new Listitem();
//                    item.setValue(transaction);
//                    item.appendChild(new Listcell(transaction.getId().toString()));
//                    item.appendChild(new Listcell(transaction.getAccount().getLogin()));
//                    item.appendChild(new Listcell(transaction.getAccount().getFullName()));
//                    item.appendChild(new Listcell(Utils.getTransactionTypeName(transaction.getTransactionType().getId())));
//                    item.appendChild(new Listcell(transaction.getCreationDate().toString()));
//                    item.appendChild(new Listcell(transaction.getTotalAmount().toString()));
//                    item.appendChild(new Listcell(transaction.getTotalTax().toString()));
//                    item.appendChild(new Listcell(transaction.getPromotionAmount().toString()));
//                    item.appendChild(new Listcell(Utils.getTransactionStatusName(transaction.getTransactionStatus().getName())));
//                    item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
//                    item.setParent(lbxReport);
//                }
//            } else {
//                btnDownload.setVisible(false);
//                item = new Listitem();
//                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.setParent(lbxReport);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        try {
//            Utils.exportExcel(lbxReport, Labels.getLabel("sp.report.title"));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
// private void loadFields(Invoice invoice)  {
//        if (invoice != null) {//UPDATE
//           
//                //UPDATE
//
//                txtNumInv.setText(String.valueOf(invoice.getNumberInvoice()));
//                txtTotalPay.setText(String.valueOf(invoice.getTotal()));
//                txtStatus.setText(invoice.getInvoiceStatus().getName());
//        
//        } else {
//
//       System.out.println("no se encuientra Invoice");
//
//        }
//    }
//
//    public void startListener() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public List<Transaction> getFilteredList(String filter) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void getData() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnAdd() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnClear() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnSearch() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//
//}
