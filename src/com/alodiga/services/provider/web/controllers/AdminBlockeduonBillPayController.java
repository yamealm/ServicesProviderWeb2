//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.Invoice;
//import com.alodiga.services.provider.commons.models.Language;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.TinType;
//import com.alodiga.services.provider.commons.models.Transaction;
//import com.alodiga.services.provider.commons.models.TransactionType;
//import com.alodiga.services.provider.commons.utils.Constants;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.components.ListcellDetailsButton;
//import com.alodiga.services.provider.web.components.ListcellPaymentButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
//import com.alodiga.services.provider.web.utils.Utils;
//
//import java.util.Date;
//import java.util.List;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.zul.Datebox;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//import org.zkoss.zul.Row;
//
//public class AdminBlockeduonBillPayController extends GenericAbstractAdminController implements GenericSPController{
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxReport;
//    private Combobox cmbTransactionType;
//    private Combobox cmbAccount;
//    private Datebox dtbBeginningDate;
//    private Datebox dtbEndingDate;
//    //private Account account ;
//     private Textbox txtLogin;
//    private TransactionEJB transactionEJB;
//    private UtilsEJB utilsEJB;
//    private UserEJB userEJB = null;
//    Boolean isStoreAll = false;
//    List<Transaction> transactions ;
//    private Label lblInfo;
//    private Row rowPassword;
//    private Combobox cmbEnterprises;
//    private Combobox cmbTinTypes;
//    private Listbox lbxRecords;
//    private Combobox cmbLanguages;
//    private Combobox cmbPeriods;
//    private Combobox cmbType;
//    private Textbox txtIdentifier;
//    private Label lblPassword;
//    private Textbox txtEmail;
//    private Textbox txtFullName;
//    private Textbox txtBusinessName;
//    private Label lblLogin;
//    private Textbox txtCountryCode;
//    private String login;
//    private Long enterpriseId;
//    Account account = new Account();
//
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
//         
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//
//
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
// 
//
//    public void onClick$btnSearch() {
//        try {
//            Account account2 = new Account();
//            login = !txtLogin.getText().isEmpty() ? txtLogin.getText() : null;
//           account2 = userEJB.searchAccountsByLogin(login);
//            loadFields(account2);
//         
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            showError(ex);
//        }
//        
//       
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
//
//
//    public void loadList(List<Invoice> list) {
//        String adminPage="listTransactionForInvoice.zul";
//         String adminPage2 ="listInvoices.zul";
//        try {
//
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//
//                for (Invoice invoice : list) {
//
//                   item = new Listitem();
//                    item.setValue(invoice);
//
//                    item.appendChild(new Listcell(String.valueOf(invoice.getNumberInvoice())));
//                    item.appendChild(new Listcell(String.valueOf(invoice.getTotalToPay())));
//                    item.appendChild(new Listcell(String.valueOf(invoice.getEmissionDate())));
//                    item.appendChild(new Listcell(invoice.getInvoiceStatus().getName()));
//
//                    item.appendChild(new Listcell());
//                    permissionEdit = true;
//                    permissionRead = true;
//
//                     item.appendChild(permissionEdit ? new ListcellDetailsButton(adminPage, invoice,Permission.READ_INVOICE) : new Listcell());
//                     if(invoice.getInvoiceStatus().getName().equals("POR PAGAR")){
//                     item.appendChild(permissionRead ? new ListcellPaymentButton(adminPage2, invoice,Permission.READ_INVOICE) : new Listcell());
//                     }
//
//
//
//                    item.setParent(lbxRecords);
//                }
//            } else {
//
//                item = new Listitem();
//                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.setParent(lbxRecords);
//            }
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//
//    private void loadFields(Account account)  {
//
//            if (account != null) {
//            
//            try {
//                cmbEnterprises.setDisabled(true);
//                cmbTinTypes.setDisabled(true);
//                txtIdentifier.setReadonly(true);
//                lblPassword.setValue(account.getPassword());
//                rowPassword.setVisible(true);
//                loadEnterprises(account.getEnterprise());
//                loadLanguages(account.getLanguage());
//                loadTinTypes(account.getTinType());
//                //loadPeriods(account.getPreiod());
//                txtEmail.setText(account.getEmail());
//                txtFullName.setText(account.getFullName());
//                txtBusinessName.setText(account.getBusinessName());
//                lblLogin.setValue(account.getLogin());
//                txtIdentifier.setText(account.getIdentifier());
//                Map params = new HashMap();
//                params.put(QueryConstants.PARAM_ACCOUNT_ID, account.getId());
//                request.setParams(params);
//                loadList(utilsEJB.loadInvoicesbyId(request));
//            } catch (NullParameterException ex) {
//                Logger.getLogger(AdminBlockeduonBillPayController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (EmptyListException ex) {
//                Logger.getLogger(AdminBlockeduonBillPayController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (GeneralException ex) {
//                Logger.getLogger(AdminBlockeduonBillPayController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//           
//
//        } else {//ADDING
//            loadEnterprises(null);
//            loadLanguages(null);
//            loadTinTypes(null);
//            loadCountryCode();
//        }
//    }
//
//    private void loadEnterprises(Enterprise enterprise) {
//        List<Enterprise> enterprises = null;
//        try {
//            cmbEnterprises.getItems().clear();
//            enterprises = utilsEJB.getEnterprises();
//            for (Enterprise e : enterprises) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(e.getName());
//                cmbItem.setValue(e);
//                cmbItem.setParent(cmbEnterprises);
//                if (enterprise != null && e.getId().equals(enterprise.getId())) {
//                    cmbEnterprises.setSelectedItem(cmbItem);
//                }
//            }
//            if (enterprise == null) {
//                cmbEnterprises.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//
//    private void loadTinTypes(TinType tinType) {
//        List<TinType> tinTypes = null;
//        Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
//        try {
//            cmbTinTypes.getItems().clear();
//
//            tinTypes = utilsEJB.getTinTypesByEnterprise(enterprise.getId());
//            for (TinType tt : tinTypes) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(tt.getName());
//                cmbItem.setDescription(tt.getPrefix());
//                cmbItem.setValue(tt);
//                cmbItem.setParent(cmbTinTypes);
//                if (tinType != null && tt.getId().equals(tinType.getId())) {
//                    cmbTinTypes.setSelectedItem(cmbItem);
//                }
//            }
//            if (tinType == null) {
//                cmbTinTypes.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadLanguages(Language language) {
//        List<Language> languages = null;
//if (languages!=null){
//        try {
//            cmbLanguages.getItems().clear();
//
//            languages = utilsEJB.getLanguages();
//            for (Language l : languages) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(l.getDescription());
//                cmbItem.setDescription(l.getIso());
//                cmbItem.setValue(l);
//                cmbItem.setParent(cmbLanguages);
//                if (language != null && l.getId().equals(language.getId())) {
//                    cmbLanguages.setSelectedItem(cmbItem);
//                }
//            }
//            if (language == null) {
//                cmbLanguages.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//        }else{
//        System.out.println(" no se encontro lenguage");
//
//        }
//    }
//
//    public void loadCountryCode() {
//        if (cmbEnterprises.getSelectedItem() != null) {
//            Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
//            if (enterprise.getId().equals(Enterprise.ALODIGA_USA)) {
//                txtCountryCode.setReadonly(true);
//                txtCountryCode.setText("" + Constants.USA_CODE);
//            } else {
//                txtCountryCode.setReadonly(false);
//                txtCountryCode.setRawValue(null);
//            }
//            loadTinTypes(null);
//        }
//    }
//
//
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
//
//    public void onClick$btnAdd() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void blockFields() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void loadData() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//  
//
//
//
//   
//
//   
//
//}
