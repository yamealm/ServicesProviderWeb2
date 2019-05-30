//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
//import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
//import com.alodiga.services.provider.commons.models.Account;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Textbox;
//import java.util.ArrayList;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.services.models.Invoice;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import org.zkoss.zul.Textbox;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import org.zkoss.zul.Combobox;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.web.utils.Utils;
//import org.zkoss.zul.Datebox;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//
//
//
///**
// *
// * @author kbermudez
// */
//public class DetailTransactionsController extends GenericAbstractController implements GenericSPController {
//
//    private Textbox txtInvoiceNumbr;
//    private Textbox txtAmount;
//    private Textbox txtTax;
//    private Textbox txtTotalToPay;
//    private Account currentAccount;
//    private Datebox dtbBeginningDate;
//    private UserEJB userEJB;
//    private UtilsEJB utilsEJB;
//    private Combobox cmbNumberInvoice;
//    private Transaction transaction;
//    private TransactionEJB transactionEJB;
//    private Invoice invoice;
//    List<Transaction> transactions;
//    private Listbox lbxTransactions;
//    private List<com.alodiga.services.provider.commons.models.Invoice> numberInvoices;
//    Map params = new HashMap<String, Object>();
//    private String  numb;
//    
//   
//
// @Override
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
////            servicesEJB = (ServicesEJB) EJBServiceLocator.getInstance().get(EjbConstants.SERVICES_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
////            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
//            currentAccount = AccessControl.loadCurrentAccount();
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//       
//            params.put(QueryConstants.PARAM_ACCOUNT_ID, currentAccount.getId());
//            request.setParams(params);
//            numberInvoices = utilsEJB.loadInvoicesbyId(request);
//            numb = numberInvoices.get(0).getNumberInvoice().toString();
//            System.out.println(numb);
//            transactions = transactionEJB.loadTransactionByInvoice(numb);
//             loadTransaction(transactions);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void clearFields() {
//    }
//
//    public void loadData() {
//       loadInvoice();
//       loadTransaction(transactions);
//
//       Map params = new HashMap<String, Object>();
//        params.put(QueryConstants.PARAM_ACCOUNT_ID, currentAccount.getId());
//        request.setParams(params);
//
//    }
//
//    @Override
//    public void loadPermission(AbstractSPEntity clazz) throws Exception {
//    }
//
//    private void loadTransaction(List<Transaction> transactions) {
//        lbxTransactions.getItems().clear();
//        Listitem item = null;
////        item.appendChild(new Listcell(transaction.getId().toString()));
//        for (Transaction transaction : transactions) {
//             String description;
//             String value = transaction.getDescription();
//            item = new Listitem();
//            item.appendChild(new Listcell(transaction.getId().toString()));
//            item.appendChild(new Listcell(Utils.getTransactionTypeName(transaction.getTransactionType().getId())));
//            item.appendChild(new Listcell(Utils.getTransactionStatusName(transaction.getTransactionStatus().getName())));
//            item.appendChild(new Listcell(transaction.getTotalAmount().toString()));
//            item.appendChild(new Listcell(transaction.getCreationDate().toString()));
//            //seleccion de descripciones
//            if(transaction.getTransactionType().getId().equals(TransactionType.PIN_PURCHASE)){
//                description = getPhoneNumberByPin_Purchase(value);
//                item.appendChild(new Listcell(description));
//
//            }else if (transaction.getTransactionType().getId().equals(TransactionType.PURCHASE_BALANCE)){
//                item.appendChild(new Listcell(transaction.getAccount().getPhoneNumber().toString()));
//
//            }else if (transaction.getTransactionType().getId().equals(TransactionType.PIN_RECHARGE)){
//                description = getPhoneNumberByPin_Rechase(value);
//                item.appendChild(new Listcell(description));
//
//            }else if(transaction.getTransactionType().getId().equals(TransactionType.PIN_PURCHASE) && transaction.getTransactionType().getId().equals(transaction.STATUS_CANCELED)){
//                description = getPhoneByError(value);
//                item.appendChild(new Listcell(description));
//
//            }else if(transaction.getTransactionType().getId().equals(TransactionType.PIN_RECHARGE) && transaction.getTransactionType().getId().equals(transaction.STATUS_CANCELED)){
//                description = getPhoneByError(value);
//                item.appendChild(new Listcell(description));
//
//            }else if(transaction.getTransactionType().getId().equals(TransactionType.TOP_UP_PURCHASE)){
//                description = getPhoneNumberByTOP_UP_PURCHASE(value);
//                item.appendChild(new Listcell(description));
//            }
//     //       item.appendChild(new Listcell(transaction.getDescription().toString()));
//            item.setParent(lbxTransactions);
//        }
//        System.out.println("Fin del metodo");
//        System.out.println("Fin del metodo");
//    }
//
//
//    private void loadInvoice() {
//        Map params = new HashMap<String, Object>();
//        params.put(QueryConstants.PARAM_ACCOUNT_ID, currentAccount.getId());
//        request.setParams(params);
//        try {
//         if(!currentAccount.getIsPrePaid()){                
//            if (numberInvoices != null) {
//                txtInvoiceNumbr.setValue(numberInvoices.get(0).getNumberInvoice().toString());
//                txtAmount.setValue(numberInvoices.get(0).getTotal().toString());
//                txtTax.setValue(numberInvoices.get(0).getTaxTotal().toString());
//                txtTotalToPay.setValue(numberInvoices.get(0).getTotalToPay().toString());
//                dtbBeginningDate.setValue(numberInvoices.get(0).getEmissionDate());
//            }
//         }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }     
//    }
//
//
//    //Metodo que captura el numwero cuando la descripcion en PIN_PURCHASE
//    private String getPhoneNumberByPin_Purchase(String value){
//        String value_=  (value.split("ani")[1]).substring(1,(value.split("ani")[1]).length()-1);
//        return value_;
//    }
//
//    //Metodo que captura el numwero cuando la descripcion en PIN_RECHARGE
//    private String getPhoneNumberByPin_Rechase(String value){
//        String value_=  (value.split("serial")[1]).substring(1,(value.split("serial")[1]).length()-1);
//        return value_;
//    }
//
//     //Metodo que captura el numwero cuando la descripcion en TOP_UP_PURCHASE
//    private String getPhoneNumberByTOP_UP_PURCHASE(String value){
//         String value_=  (value.split("PhoneNumber:")[1]).substring(1,(value.split("PhoneNumber:")[1]).length());
//        return value_;
//    }
//
//
//   //Metodo que valida si la cadena es numerica
//    private static  boolean isNumeric(String value){
//        try {
//            Integer.parseInt(value);
//        } catch (NumberFormatException e) {
//            return false;
//        }
//        return true;
//    }
//
//    private String getPhoneByError(String value){
//        String number="";
//        for(int i=0;i<value.length();i++){
//            try {
//                Integer.parseInt(String.valueOf(value.charAt(i)));
//                if(i>0 && (isNumeric(String.valueOf(value.charAt(i-1))))){
//
//                    number+=String.valueOf(value.charAt(i));
//                }
//            } catch (NumberFormatException e) {
//
//            }
//        }
//        number=value.split(number)[0].substring(value.split(number)[0].length()-1)+number;
//        System.out.println(number);
//        return number;
//    }
//
//
//
//}
//   
//
