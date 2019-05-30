//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.ProductEJB;
//import com.alodiga.services.provider.commons.ejbs.ServicesEJB;
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Pin;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionStatus;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.services.models.WSConstants;
//import com.alodiga.services.provider.commons.utils.AccountData;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
//import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import com.alodiga.services.provider.web.utils.Utils;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Textbox;
//import java.util.ArrayList;
//import java.util.Date;
//import org.zkoss.zk.ui.event.EventQueues;
//import org.zkoss.zul.Datebox;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Button;
//
//public class TransactionHistoryByAccountViewController extends GenericAbstractController implements GenericSPController {
//
//    static final long serialVersionUID = -9145887024839938515L;
//    private UserEJB userEJB;
//    private TransactionEJB transactionEJB;
//    private ServicesEJB servicesEJB;
//    private ProductEJB productEJB;
//    private Combobox cmbStatus;
//    private Combobox cmbAccounts;
//    private Combobox cmbTransactionTypes;
//    private Datebox dtbBeginningDate;
//    private Datebox dtbEndingDate;
//    private Listbox lbxTransactions;
//    private Textbox txtCustomerFilter;
//    private Account currentAccount;
//    private Label lblTotalAmount;
//    private Button btnExport;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//        loadData();
//    }
//
//    @Override
//    public void initialize() {
//        try {
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            servicesEJB = (ServicesEJB) EJBServiceLocator.getInstance().get(EjbConstants.SERVICES_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
//            currentAccount = AccessControl.loadCurrentAccount();
//            dtbBeginningDate.setValue(new Timestamp(new Date().getTime()));
//            dtbEndingDate.setValue(new Timestamp(new Date().getTime()));
//            dtbBeginningDate.setReadonly(true);
//            dtbEndingDate.setReadonly(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void clearFields() {
//    }
//
//    public void loadData() {
//        loadStatus();
//        loadTransactionTypes();
//        loadAccount();
//    }
//
//
//    private void loadStatus() {
//        cmbStatus.setText(Labels.getLabel("sp.transaction.status"));
//        Comboitem cbi = new Comboitem();
//        cbi.setLabel(Utils.getTransactionStatusName(Transaction.STATUS_CANCELED));
//        cbi.setValue(2L);
//        cbi.setParent(cmbStatus);
//
//        cbi = new Comboitem();
//        cbi.setLabel(Utils.getTransactionStatusName(Transaction.STATUS_FAILED));
//        cbi.setValue(3L);
//        cbi.setParent(cmbStatus);
//
//        cbi = new Comboitem();
//        cbi.setLabel(Utils.getTransactionStatusName(Transaction.STATUS_PROCESSED));
//        cbi.setValue(1L);
//        cbi.setParent(cmbStatus);
//
//        cbi = new Comboitem();
//        cbi.setLabel(Utils.getTransactionStatusName(Transaction.STATUS_REJECTED_BY_PAYMENT));
//        cbi.setValue(4L);
//        cbi.setParent(cmbStatus);
//        cmbStatus.setSelectedIndex(0);
//    }
//
//    private void loadTransactionTypes() {
//        List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            transactionTypes = transactionEJB.getTransactionTypes(request);
//            for (TransactionType transactionType : transactionTypes) {
////                if (!transactionType.getId().equals(TransactionType.PIN_PURCHASE) && !transactionType.getId().equals(TransactionType.PIN_RECHARGE) && !transactionType.getId().equals(TransactionType.TOP_UP_PURCHASE) && !transactionType.getId().equals(TransactionType.BILL_PAYMENT_PURCHASE) && !transactionType.getId().equals(TransactionType.PURCHASE_BALANCE)) {
//                    Comboitem item = new Comboitem();
//                    System.out.println("transactionTypeId"+transactionType.getId());
//                    item.setValue(transactionType);
//                    item.setLabel(Utils.getTransactionTypeName(transactionType.getId()));
//                    item.setParent(cmbTransactionTypes);
//           //     }
//            }
//            cmbTransactionTypes.setSelectedIndex(0);
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private void loadAccount() {
//        try {
//            Comboitem item = new Comboitem();
//            item.setValue(currentAccount);
//            item.setLabel(currentAccount.getFullName());
//            item.setDescription(currentAccount.getCurrentProfile().getName());
//            item.setParent(cmbAccounts);
//            cmbAccounts.setSelectedIndex(0);
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    @Override
//    public void loadPermission(AbstractSPEntity clazz) throws Exception {
//    }
//
//    public void onClick$btnSearch() {
//        btnExport.setVisible(false);
//        this.clearMessage();
//        if (dtbBeginningDate.getValue().getTime() > dtbEndingDate.getValue().getTime()) {
//            this.showMessage("sp.error.dateSelectInvalid.Invalid", true, null);
//        } else if (!cmbAccounts.getItems().isEmpty()) {//CABLE ENORME
////            int limit = 100;
//            List<Transaction> transactionsAux = new ArrayList<Transaction>();
//            Map<String, Object> params = new HashMap<String, Object>();
//            TransactionType transactionType = null;
//            try {
//                try {
//                    transactionType = (TransactionType) cmbTransactionTypes.getSelectedItem().getValue();
//                } catch (ClassCastException e) {
//                    e.printStackTrace();
//                }
//                Account account = (Account) cmbAccounts.getSelectedItem().getValue();
//                params.put(QueryConstants.PARAM_ACCOUNT, account.getId());
//                if (!txtCustomerFilter.getText().isEmpty()) {
//                    params.put(QueryConstants.PARAM_CUSTOMER_LOGIN, txtCustomerFilter.getText());
//                }
//                if (cmbStatus.getSelectedIndex() != 0) {
//                    params.put(QueryConstants.PARAM_STATUS, cmbStatus.getSelectedItem().getValue());
//                }
//                if (dtbBeginningDate.getValue() != null) {
//                    params.put(QueryConstants.PARAM_BEGINNING_DATE, dtbBeginningDate.getValue());
//                }
//
//                if (dtbEndingDate.getValue() != null) {
//                    params.put(QueryConstants.PARAM_ENDING_DATE, dtbEndingDate.getValue());
//                }
//                if (transactionType != null) {
//                    params.put(QueryConstants.PARAM_TRANSACTION_TYPE_ID, transactionType.getId());
//                }
//                if (QueryConstants.PARAM_ACCOUNT_ID != null) {
//                    params.put(QueryConstants.PARAM_ACCOUNT_ID, currentAccount.getId());
//                }
//
//                request.setParams(params);
//                this.clearFields();
//                lbxTransactions.getItems().clear();
//                transactionsAux = transactionEJB.searchTransaction(request);
//                loadDataList(transactionsAux);
//                btnExport.setVisible(true);
//            } catch (EmptyListException ex) {
//                this.showMessage("sp.error.empty.list", true, ex);
//                loadDataList(new ArrayList<Transaction>());
//            } catch (Exception ex) {
//                this.showMessage("sp.error.general", true, ex);
//            }
//        }
//    }
//
//    public void loadDataList(List<Transaction> list) {
//        try {
//
//            lbxTransactions.getItems().clear();
//            Float totalAmount = 0F;
//            Float totalTaxes = 0F;
//            Float totalPromotions = 0F;
//            for (int i = 0; i < list.size(); i++) {
//                Transaction transaction = list.get(i);
//                if (transaction.getTransactionStatus().getId().equals(TransactionStatus.PROCESSED)) {
//                    totalAmount += transaction.getTotalAmount();
//                }
//                Listitem item = createTransactionListItem(transaction);
//                item.setParent(lbxTransactions);
//            }
//            lblTotalAmount.setValue(totalAmount.toString());
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void onClick$btnExport() {
//        try {
//            Utils.exportExcel(lbxTransactions, "report_transactions");
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private Listcell initDeleteButton(final Listitem listItem, boolean disabled) {
//
//        Listcell cell = new Listcell();
//        final Button button = new Button();
//        button.setVisible(!disabled);
//        button.setLabel(Labels.getLabel("sp.common.details"));
//        button.setClass("open gray");
//        button.addEventListener("onClick", new EventListener() {
//
//            public void onEvent(Event event) throws Exception {
//                Transaction transaction = (Transaction) listItem.getValue();
//                execution.sendRedirect("transactionHistoryByAccountDetail.zul");
//                Sessions.getCurrent().setAttribute(WebConstants.SESSION_TRANSACTION, transaction);
//            }
//        });
//        button.setParent(cell);
//        return cell;
//    }
//
//    private void cancelTransaction(Listitem listItem) {
//        boolean success = false;
//        Transaction transaction = (Transaction) listItem.getValue();
////        transaction.setTransactionStatus(Transaction.STATUS_CANCELED);
//        Map params = new HashMap();
//        params.put(WSConstants.PARAM_TRANSACTION_DATA, transaction);
//        params.put(WSConstants.PARAM_LANGUAGE_ID, 1l);
//        EJBRequest request = new EJBRequest();
//        request.setParams(params);
//        try {
//            if (transaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)) {
//                if (validateVoidDate(transaction.getCreationDate())) {
//                    AccountData accountData = new AccountData();
//                    accountData.setLogin(currentAccount.getLogin());
//                    accountData.setPassword(currentAccount.getPassword());
//                    servicesEJB.cancelPayment(accountData, transaction);
//                    showBalance();
//                } else {
//                    this.showMessage("sp.error.purchage.date", true, null);
//                    return;
//                }
//            }
//            success = true;
//        }  catch (Exception ex) {
//            ex.printStackTrace();
//            this.showMessage("sp.error.general", true, ex);
//        }
//
//        if (success == false) {
//            this.showMessage("sp.error.general", true, null);
//            return;
//        }
//        Listbox parent = (Listbox) listItem.getParent();
//        parent.removeChild(listItem);
//
//        this.showMessage("sp.transaction.confirm.remove.successful", false, null);
//
//    }
//
//    private Listitem createTransactionListItem(Transaction transaction) {
//
//        //System.out.println("transaction " + transaction.getId());
//        Listitem item = new Listitem();
//        item.setValue(transaction);
//        item.appendChild(new Listcell(transaction.getId().toString()));
//        Transaction tItem = transaction;
//        boolean isTopUp = false;
//        if (tItem.getTopUpProduct() != null) {
//                isTopUp = true;
//        }
//        if (!isTopUp) {
//            try {
//                Pin pin = null;
//                if (transaction.getTransactionType().getId().equals(TransactionType.PIN_PURCHASE)) {
//                    pin = productEJB.loadPinByTransactionItemId(tItem.getId());
//                } else if (transaction.getTransactionType().getId().equals(TransactionType.PIN_RECHARGE)) {
//                    pin = productEJB.loadPinBySerial(transaction.getPinSerial().toString());
//                }
//                if (transaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)) {
//                    item.appendChild(new Listcell(transaction.getAccount().getPhoneNumber()));
//                } else {
//                    item.appendChild(new Listcell(pin.getPinFrees().get(0).getAni()));
//                }
//
//            } catch (Exception ex) {
//                item.appendChild(new Listcell());
//            }
//        } else if (isTopUp) {
//            item.appendChild(new Listcell(tItem.getTopUpDestination()!=null ? tItem.getTopUpDestination() :""));
//        } else {
//            item.appendChild(new Listcell());
//
//        }
//        item.appendChild(new Listcell(Utils.getTransactionTypeName(transaction.getTransactionType().getId())));
//        item.appendChild(new Listcell(Utils.getTransactionStatusName(transaction.getTransactionStatus().getName())));
//        item.appendChild(new Listcell(transaction.getTotalAmount() + currentAccount.getEnterprise().getCurrency().getSymbol()));
////        item.appendChild(new Listcell(transaction.getTotalAlopointsUsed().toString()));
//        item.appendChild(new Listcell(transaction.getCreationDate().toString()));
//        item.appendChild(initDeleteButton(item, false));
//        return item;
//    }
//
//    private boolean validateVoidDate(Timestamp transactionDate) {
//        Calendar dayAfter = Calendar.getInstance();
//        dayAfter.setTimeInMillis(transactionDate.getTime());
//        dayAfter.add(Calendar.DAY_OF_MONTH, 1);
//        dayAfter.set(Calendar.HOUR, 0);
//        dayAfter.set(Calendar.MINUTE, 0);
//        dayAfter.set(Calendar.SECOND, 0);
//        dayAfter.set(Calendar.MILLISECOND, 0);
//        dayAfter.set(Calendar.AM_PM, Calendar.AM);
//        Calendar transactionDay = Calendar.getInstance();
//        transactionDay.setTimeInMillis(transactionDate.getTime());
//        if (transactionDay.compareTo(dayAfter) > 0) {
//            return false;
//        }
//        return true;
//    }
//
//    private void showBalance() {
//        EventQueues.lookup("loadSummaryData", EventQueues.APPLICATION, true).publish(new Event(""));
//    }
//  }
