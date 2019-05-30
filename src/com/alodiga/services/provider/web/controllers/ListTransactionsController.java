//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.models.Account;
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
//import java.util.HashMap;
//import java.util.Map;
//import org.zkoss.zul.Datebox;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//
//public class ListTransactionsController extends GenericAbstractListController<Transaction> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxReport;
//    private Combobox cmbTransactionType;
//    private Combobox cmbAccount;
//    private Datebox dtbBeginningDate;
//    private Datebox dtbEndingDate;
//    private TransactionEJB transactionEJB = null;
//    private UserEJB userEJB = null;
//    Boolean isStoreAll = false;
//    List<Transaction> transactions = null;
//    private List<Account> accounts = null;
//    private Label lblInfo;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    public void startListener() {
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
//            getData();
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void clearFields() {
//        lblInfo.setValue("");
//        lblInfo.setVisible(false);
//    }
//
//    public void onClick$btnSearch() {
//        try {
//            clearFields();
//            clearMessage();
//            EJBRequest _request = new EJBRequest();
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put(QueryConstants.PARAM_BEGINNING_DATE, dtbBeginningDate.getValue());
//            params.put(QueryConstants.PARAM_ENDING_DATE, dtbEndingDate.getValue());
//            if (dtbEndingDate.getValue().getTime() > dtbBeginningDate.getValue().getTime()) {
//                if (cmbAccount.getSelectedItem() != null && cmbAccount.getSelectedIndex() != 0) {
//                    params.put(QueryConstants.PARAM_ACCOUNT_ID, ((Account) cmbAccount.getSelectedItem().getValue()).getId());
//                }
//                if (cmbTransactionType.getSelectedItem() != null && cmbTransactionType.getSelectedIndex() != 0) {
//                    params.put(QueryConstants.PARAM_TRANSACTION_TYPE_ID, ((TransactionType) cmbTransactionType.getSelectedItem().getValue()).getId());
//                }
//                _request.setParams(params);
//                _request.setParam(true);
//                transactions = transactionEJB.searchTransaction(_request);
//                loadList(transactions);
//            } else  {
//                this.showMessage("sp.error.date.invalid", true, null);
//            }
//        } catch (GeneralException ex) {
//            showError(ex);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//            loadList(null);
//        }
//    }
//
//    private void loadTransactionTypes() {
//
//        try {
//            cmbTransactionType.getItems().clear();
//            List<TransactionType> transactionTypes = transactionEJB.getTransactionTypes();
//            Comboitem item = new Comboitem();
//            item.setLabel(Labels.getLabel("sp.common.all"));
//            item.setParent(cmbTransactionType);
//            cmbTransactionType.setSelectedItem(item);
//            for (int i = 0; i < transactionTypes.size(); i++) {
//                item = new Comboitem();
//                item.setValue(transactionTypes.get(i));
//                item.setLabel(Utils.getTransactionTypeName(transactionTypes.get(i).getId()));
//                item.setParent(cmbTransactionType);
//            }
//        } catch (Exception ex) {
//            this.showError(ex);
//        }
//
//    }
//
//    private void loadAccount() {
//
//        try {
//            cmbAccount.getItems().clear();
//            accounts = new ArrayList<Account>();
//            request.setFirst(0);
//            request.setLimit(null);
//            accounts = userEJB.getAccounts(request);
//            Comboitem item = new Comboitem();
//            item.setLabel(Labels.getLabel("sp.common.all"));
//            item.setParent(cmbAccount);
//            cmbAccount.setSelectedItem(item);
//            for (int i = 0; i < accounts.size(); i++) {
//                item = new Comboitem();
//                item.setValue(accounts.get(i));
//                item.setLabel(accounts.get(i).getFullName());
//                item.setParent(cmbAccount);
//            }
//        } catch (Exception ex) {
//            this.showError(ex);
//        }
//
//    }
//
//    public void loadList(List<Transaction> list) {
//        try {
//            lbxReport.getItems().clear();
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//                for (Transaction transaction : list) {
//                    item = new Listitem();
//                    item.setValue(transaction);
//                    item.appendChild(new Listcell(transaction.getId().toString()));
////                    item.appendChild(new Listcell(transaction.getAccount().getLogin()));
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
//    public void onClick$btnClear() throws InterruptedException {
//        cmbTransactionType.setSelectedIndex(0);
//        cmbAccount.setSelectedIndex(0);
//        Date date = new Date();
//        dtbBeginningDate.setValue(date);
//        dtbEndingDate.setValue(date);
//        clearFields();
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
//    public void getData() {
//        dtbBeginningDate.setFormat("yyyy/MM/dd");
//        dtbBeginningDate.setValue(new Timestamp(new java.util.Date().getTime()));
//        dtbEndingDate.setFormat("yyyy/MM/dd");
//        dtbEndingDate.setValue(new Timestamp(new java.util.Date().getTime()));
//        loadAccount();
//        loadTransactionTypes();
//    }
//
//    public void onClick$btnAdd() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void blockFields() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public List<Transaction> getFilteredList(String filter) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
