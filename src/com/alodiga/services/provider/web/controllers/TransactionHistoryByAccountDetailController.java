//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.exceptions.NegativeBalanceException;
//import com.alodiga.services.provider.commons.exceptions.TransactionCanceledException;
//import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
//import com.alodiga.services.provider.commons.managers.ContentManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionStatus;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.services.models.WSConstants;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Groupbox;
//import org.zkoss.zul.Label;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.EjbUtils;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.Utils;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.zk.ui.event.EventQueues;
//
//public class TransactionHistoryByAccountDetailController extends GenericAbstractController implements GenericSPController {
//
//    static final long serialVersionUID = -9145887024839938515L;
//    private TransactionEJB transactionEJB;
//    private Transaction currenTransaction;
//    private Button btnProcessRefund;
//    private Groupbox gbxInformationAditional;
//    private Label lblTransactionTypeValue;
//    private Label lblTransactionNumberValue;
//    private Label lblTransactionStatusValue;
//    private Label lblTaxTotalAmountValue;
//    private Label lblTransactionAmountValue;
//    private Label lblTransactionInformation;
//    private Label lblTotalAlopintsValue;
//    private Label lblTotalpromotionalValue;
//    private Label lblTotalRealValue;
//    private Label lblTransactionDateValue;
//    private Account currentAccount;
//    private String currencySymbol = "";
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
//            currenTransaction = (Transaction) Sessions.getCurrent().getAttribute(WebConstants.SESSION_TRANSACTION);
//            currentAccount = AccessControl.loadCurrentAccount();
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            currencySymbol = currentAccount.getEnterprise().getCurrency().getSymbol();
//        } catch (Exception ex) {
//
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public void clearFields() {
//    }
//
//    public void loadData() {
//        boolean isBillPay = false;
//        printLabelsTransaction();
//            boolean isTopUp = false;
//            if (currenTransaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)) {
//
//                if (currenTransaction.getCreationDate().before(EjbUtils.getBeginningDate(new Date()))) {
//                    gbxInformationAditional.setVisible(true);
//                    lblTransactionInformation.setValue(Labels.getLabel("sp.error.purchage.date"));
//                    btnProcessRefund.setVisible(false);
//                }
//
//            } else if (currenTransaction.getTransactionStatus().toString().equals(TransactionStatus.CANCELED)) {
//                gbxInformationAditional.setVisible(true);
//                lblTransactionInformation.setValue(Labels.getLabel("sp.transactionStatus.canceled.message"));
//                btnProcessRefund.setVisible(false);
//                return;
//            } else if (!currenTransaction.getAccount().getId().equals(currentAccount.getId())) {
//                btnProcessRefund.setVisible(false);
//                return;
//            } else if (isTopUp) {
//                gbxInformationAditional.setVisible(true);
//                lblTransactionInformation.setValue(Labels.getLabel("sp.common.alodiga.cannot.cancel"));
//                btnProcessRefund.setVisible(false);
//                return;
//            } else if (isBillPay) {
//                gbxInformationAditional.setVisible(true);
//                lblTransactionInformation.setValue(Labels.getLabel("sp.common.alodiga.bill.payment.cannot.cancel"));
//                btnProcessRefund.setVisible(false);
//                return;
//            } else if (!currenTransaction.getTransactionStatus().equals(Transaction.STATUS_CANCELED)) {
//                gbxInformationAditional.setVisible(false);
//                btnProcessRefund.setVisible(true);
//            } else {
//                gbxInformationAditional.setVisible(true);
//                btnProcessRefund.setVisible(false);
//                lblTransactionInformation.setValue(Labels.getLabel("sp.error.transactionCanceled.void.promotion"));
//              }
//    }
//
//    private void printLabelsTransaction() {
//
//        lblTransactionNumberValue.setValue(currenTransaction.getId().toString());
//        lblTransactionStatusValue.setValue(Utils.getTransactionStatusName(currenTransaction.getTransactionStatus().getName()));
//        lblTransactionTypeValue.setValue(Utils.getTransactionTypeName(currenTransaction.getTransactionType().getId()));
//        lblTotalpromotionalValue.setValue(GeneralUtils.getCorrectAmount(currencySymbol, currenTransaction.getPromotionAmount(), 2));
//        lblTaxTotalAmountValue.setValue(GeneralUtils.getCorrectAmount(currencySymbol, currenTransaction.getTotalTax(), 2));
//        lblTransactionAmountValue.setValue(GeneralUtils.getCorrectAmount(currencySymbol, currenTransaction.getTotalAmount(), 2));
//        lblTransactionDateValue.setValue(currenTransaction.getCreationDate().toString());
//    }
//
//
//    private void cancelTransaction(Transaction transaction) {
//        try {
//            boolean success = false;
//            TransactionStatus transactionStatus = ContentManager.getInstance().getTransactionStatusById(TransactionStatus.CANCELED);
//            transaction.setTransactionStatus(transactionStatus);
//            //        transaction.setTransactionStatus(Transaction.STATUS_CANCELED);
//            Map params = new HashMap();
//            params.put(WSConstants.PARAM_TRANSACTION_DATA, transaction);
//            params.put(WSConstants.PARAM_LANGUAGE_ID, 1l);
//            EJBRequest request = new EJBRequest();
//            request.setParams(params);
//            try {
//                EJBRequest _request = new EJBRequest();
//                _request.setParam(transaction.getId());
//                Transaction _transaction = transactionEJB.loadTransaction(_request);
//                if (!_transaction.getTransactionStatus().getId().equals(TransactionStatus.PROCESSED)) {
//                    throw new TransactionCanceledException("");
//                }
//               else if (transaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)) {
//                    if (currenTransaction.getCreationDate().after(EjbUtils.getBeginningDate(new Date()))) {
//                        transactionEJB.cancelPurchaseBalanceAccount(request); //3
//                        showBalance();
//                    } else {
//                        this.showMessage("sp.error.purchage.date", true, null);
//                        return;
//                    }
//                }
//                success = true;
//            } catch (TransactionCanceledException ex) {
//                ex.printStackTrace();
//                this.showMessage("sp.error.already.transactionCanceled", true, ex);
//                return;
//            }catch (NegativeBalanceException ex) {
//                ex.printStackTrace();
//                this.showMessage("sp.error.transactionCanceled.negativeBalance", true, ex);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                this.showMessage("sp.error.general", true, ex);
//            }
//            if (success == false) {
//                this.showMessage("sp.error.general", true, null);
//                return;
//            }
//            this.showMessage("sp.transaction.confirm.remove.successful", false, null);
//        } catch (Exception ex) {
//            Logger.getLogger(TransactionHistoryByAccountDetailController.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
//    public void onClick$btnProcessRefund() {
//        cancelTransaction(currenTransaction);
//        btnProcessRefund.setVisible(false);
//    }
//
//    public void updateTotalLabels(Transaction transaction) {
//    }
//
//    private void showBalance() {
//        EventQueues.lookup("loadSummaryData", EventQueues.APPLICATION, true).publish(new Event(""));
//    }
//}