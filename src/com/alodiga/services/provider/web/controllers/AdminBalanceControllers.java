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
//import com.alodiga.services.provider.commons.managers.PermissionManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.Payment;
//import com.alodiga.services.provider.commons.models.PaymentType;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.components.ChangeStatusButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//public class AdminBalanceControllers extends GenericAbstractListController<Account> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//
//    private Textbox txtFullName;
//    private Textbox txtNumDoc;
//    private Textbox txtLogin;
//    private Textbox txtBusinessName;
//    private Textbox txtnunDepTrans;
//    private Textbox txtAmount;
//    private UtilsEJB utilsEJB = null;
//    private UserEJB userEJB = null;
//    private TransactionEJB transactionEJB = null;
//    private List<Account> accounts = null;
//    private User currentUser;
//    private Profile currentProfile;
//    private boolean sendDistributorData = false;
//    private Account accountParam;
//    private Combobox cmbPaymentTypes;
//    private Payment payment;
//    private Payment payment2;
//
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    @Override
//    public void checkPermissions() {
//        try {
//
//            permissionAdd = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_ACCOUNT);
//            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_ACCOUNT);
//            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_ACCOUNT);
//            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_ACCOUNT_STATUS);
//            sendDistributorData = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.SEND_ACCOUNT_DATA);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        
//        try {
//            adminPage = "adminBalanceRecharge.zul";
//            currentUser = AccessControl.loadCurrentUser();
//            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
//            checkPermissions();
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
//            
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//        loadPayments(null);
//    }
//
//    private Listcell initEnabledButton(final Boolean enabled, final Listitem listItem) {
//
//        Listcell cell = new Listcell();
//        cell.setValue("");
//        final ChangeStatusButton button = new ChangeStatusButton(enabled);
//        button.setTooltiptext(Labels.getLabel("sp.common.actions.changeStatus"));
//        button.setClass("open orange");
//        button.addEventListener("onClick", new EventListener() {
//
//            @Override
//            public void onEvent(Event event) throws Exception {
//                changeStatus(button, listItem);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
//
//    private Listcell initForwardDataButton(final Account account) {
//
//        Listcell cell = new Listcell();
//        cell.setValue("");
//        final Button button = new Button();
//        button.setImage("/images/icon_envelope.png");
//        button.setTooltiptext(Labels.getLabel("sp.common.actions.forward"));
//        button.setClass("open orange");
//        button.addEventListener("onClick", new EventListener() {
//
//            @Override
//            public void onEvent(Event event) throws Exception {
//                AccessControl.generateNewPassword(null, account, true);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
//
//    @Override
//    public List<Account> getFilteredList(String filter) {
//        List<Account> listAux = new ArrayList<Account>();
//        for (Iterator<Account> i = accounts.iterator(); i.hasNext();) {
//            Account tmp = i.next();
//            String field = tmp.getFullName();
//            if (field.toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0) {
//                listAux.add(tmp);
//            }
//        }
//        return listAux;
//    }
//
////    @Override
////    public void onClick$btnAdd() throws InterruptedException {
////        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
////        Sessions.getCurrent().removeAttribute("object");
////        Executions.getCurrent().sendRedirect(adminPage);
////    }
//
//    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
//        try {
//            Account account = (Account) listItem.getValue();
//            button.changeImageStatus(account.getEnabled());
//            account.setEnabled(!account.getEnabled());
//            listItem.setValue(account);
//            request.setParam(account);
//            userEJB.saveAccount(request);
//            AccessControl.saveAction(Permission.CHANGE_ACCOUNT_STATUS, "changeStatus account = " + account.getId() + " to status = " + !account.getEnabled());
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//    private void loadFields(Account account) {
//        if (account != null) {
//            txtFullName.setText(account.getFullName());
//            txtBusinessName.setText(account.getBusinessName());
//
//        } else {
//
//        }
//    }
//
//
//    public void getData() {
//        accounts = new ArrayList<Account>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            accounts = userEJB.getAccounts(request);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//            
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//    }
//
//    
//
//    public void onClick$btnClear() throws InterruptedException {
//    
//        txtLogin.setText("");
//        
//    }
//
//    public void onClick$btnSearch() throws InterruptedException {
//        try {
//            String login = !txtLogin.getText().isEmpty() ? txtLogin.getText() : null;
//            accountParam = userEJB.searchAccountsByLogin(login);
//              loadFields(accountParam);
//            }catch (RegisterNotFoundException ex) {
//                this.showMessage("No existe registro solicitado", true, null);
//                ex.printStackTrace();
//               // Logger.getLogger(AdminBalanceController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (NullParameterException ex) {
//                
//                Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (GeneralException ex) {
//                Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (Exception ex) {
//
//            showError(ex);
//        }
//
//    }
//
//
//
//public void onClick$btnSave() throws GeneralException {
//         Date date = new Date();
//         Account account = null;
//         Timestamp timeStampDate = new  Timestamp(date.getTime());
//        if (validateEmpty()) {
//                EJBRequest request = new EJBRequest();
//                Float totalP = 0F;
//                account = accountParam;
//                payment = new Payment();
//                payment.setInvoice(null);
//                payment.setPaymentType((PaymentType) cmbPaymentTypes.getSelectedItem().getValue());
//                payment.setNumDeposit(txtNumDoc.getValue());
//                payment.setAmount(Float.valueOf(txtAmount.getValue()));
//                payment.setCreationDate(timeStampDate);
//                payment.setStatus(true);
//                payment.setAccount(accountParam);
//                totalP = (payment.getAmount() + accountParam.getBalance());
//                accountParam.setBalance(totalP);
//                
//                if((accountParam.getIsPrePaid())){
//                try {
//                    request.setParam(accountParam);
//                    account = userEJB.saveAccount(request);
////                    payment2.setParam(payment);
//                    payment2=new Payment();
//                    payment2=transactionEJB.savePayment(payment);
//                    Executions.getCurrent().sendRedirect(adminPage);
//                } catch (NullParameterException ex) {
//                    
//                }
//                }else {
//                    System.out.println("LA CUENTA NO ES PREPAGO ");
//                }         
//        }
//    }
//
//
//public void loadPayments(Payment payment) {
//        try {
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
//        }
//    }
//
//
//
//
// private void save(Account account) {
//String  Amount = !txtAmount.getText().isEmpty() ? txtAmount.getText() : null;
//boolean isPrepaid = account.getIsPrePaid();
//Float Amounts = Float.valueOf(Amount);
//Float Amaunts1 =account.getBalance();
//Float totalAmount;
//
//     if (account != null) {
//
//         if (Amaunts1 == 0 && isPrepaid == true) {
//               
//                         account.setBalance(Amounts);
//                         request.setParam(account);
//                         
//                          this.showMessage("sp.recharge.summary.Preinfo", false, null);
//                try {
//                    account = userEJB.saveAccount(request);
//                } catch (GeneralException ex) {
//                    Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NullParameterException ex) {
//                    Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//              }
//         }else if(Amaunts1 != Amounts && isPrepaid==true){
//            totalAmount = Amaunts1+Amounts;
//             System.out.println(""+totalAmount);
//             
//                  account.setBalance(totalAmount);
//                         request.setParam(account);
//                    
//                          this.showMessage("sp.recharge.summary.Preinfo", false, null);
//                try {
//                    account = userEJB.saveAccount(request);
//                } catch (GeneralException ex) {
//                    Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NullParameterException ex) {
//                    Logger.getLogger(AdminBalanceControllers.class.getName()).log(Level.SEVERE, null, ex);
//              }
//
//
//
//
//         }
//
//      }
//
//  }
//
//
//
//
//
//
//
//      public Boolean validateEmpty() {
//        if (txtFullName.getText().isEmpty()) {
//            txtFullName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtBusinessName.getText().isEmpty()) {
//            txtBusinessName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else  if (txtNumDoc.getText().isEmpty()) {
//            txtNumDoc.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtAmount.getText().isEmpty()) {
//            txtAmount.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        }else if (txtLogin.getText().isEmpty()) {
//            txtLogin.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        }else{
//        return true;
//        }
//        return false;
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
//
//
//    @Override
//    public void startListener() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void loadList(List<Account> list) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void onClick$btnAdd() throws InterruptedException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//
//}
