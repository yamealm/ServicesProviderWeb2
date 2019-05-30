package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Account;
import com.alodiga.services.provider.commons.models.AccountHasIpAddress;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ChangeStatusButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.commons.models.IpAddress;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.web.components.DeleteButton;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Listcell;



import org.zkoss.zul.Listbox;



public class ChangeIpAdressController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAccount;
    private Textbox txtIp;
    private Textbox txtDescription;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private List<AccountHasIpAddress> accountHasIpAddress = null;
    private User currentUser;
    private Profile currentProfile;
    private Label lblInfo;
    private Account account;
    private IpAddress ipAddressParam;
    private Button btnAdd;
    private Hlayout addIp;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        account = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.account");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.account");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            checkPermissions();
        } catch (Exception ex) {
            showError(ex);
        }
    }

//        @Override
//    public void checkPermissions() {
//        try {
//            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.DELETE_IP);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }

    public void onClick$btnSearch() {
        loadData();
    }

       public void onClick$btnAdd() throws InterruptedException {
           boolean exist = false;
           List<AccountHasIpAddress> list = account.getAccountHasIpAddress();
        try {
            IpAddress ipAddress = utilsEJB.loadIpddress(txtIp.getText());
            for (AccountHasIpAddress ip : list) {
                if (ip.getIpAddress().getIp().equals(ipAddress.getIp()) && ip.getEndingDate()==null) {
                    exist = true;
                }
            }
            if (!exist) {
                AccountHasIpAddress accountHasIp = new AccountHasIpAddress();
                accountHasIp.setAccount(account);
                accountHasIp.setIpAddress(ipAddress);
                accountHasIp.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
                list.add(accountHasIp);
                account.setAccountHasIpAddress(list);
               }
        } catch (RegisterNotFoundException ex) {
            IpAddress ipAddress = new IpAddress();
            ipAddress.setIp(txtIp.getText());
            ipAddress.setDescription(txtDescription.getText());
            AccountHasIpAddress accountHasIp = new AccountHasIpAddress();
            accountHasIp.setAccount(account);
            accountHasIp.setIpAddress(ipAddress);
            accountHasIp.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
            list.add(accountHasIp);
            account.setAccountHasIpAddress(list); 
        } catch (NullParameterException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
            ex.printStackTrace();
        } finally {
            try {
                request.setParam(account);
                account = userEJB.saveAccount(request);
                loadData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

   private Listcell initDeleteButton(final Long ipAddressId, final Listitem listItem) {
        Listcell cell = new Listcell();
       try {
           cell.setValue("");
           final DeleteButton button = new DeleteButton(listItem);
           button.setTooltiptext(Labels.getLabel("sp.common.actions.changeStatus"));
           button.setClass("open orange");
           button.addEventListener("onClick", new EventListener() {

               @Override
               public void onEvent(Event event) throws Exception {
                   System.out.println("entro al evento/***************");
                   changeStatus(button, ipAddressId, listItem);
               }
           });

           button.setParent(cell);
       } catch (Exception ex) {
           showError(ex);
       }
        return cell;
    }

    private void changeStatus(DeleteButton button, Long ipAddressId , Listitem listItem) {
        try {
            List<AccountHasIpAddress> list = new ArrayList<AccountHasIpAddress>();
            for (AccountHasIpAddress address : account.getAccountHasIpAddress()) {
                if (address.getIpAddress().getId().equals(ipAddressId) && address.getEndingDate() == null) {
                    address.setEndingDate(new Timestamp(new java.util.Date().getTime()));
                }
                list.add(address);
            }
            account.setAccountHasIpAddress(list);
            request.setParam(account);
            userEJB.saveAccount(request);
            AccessControl.saveAction(Permission.DELETE_IP, "delete ip address = " + ipAddressId + " of account = " + account.getId());
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }


     public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.account.list"));
        } catch (Exception ex) {
            showError(ex);
        }

      }

    @Override
    public void loadData() {
        try {
            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.DELETE_IP);
            if (txtAccount.getText() != null) {
                try {
                    lbxRecords.getItems().clear();
                    Listitem item = null;
                    account = userEJB.loadAccountByLogin(txtAccount.getText());
                    if (!account.getAccountHasIpAddress().isEmpty()) {
                        addIp.setVisible(true);
                        for (AccountHasIpAddress ipAddress : account.getAccountHasIpAddress()) {
                            item = new Listitem();
                            item.setValue(ipAddress);
                            item.appendChild(new Listcell(ipAddress.getIpAddress().getIp()));
                            item.appendChild(new Listcell(ipAddress.getIpAddress().getDescription()));
                            item.appendChild(permissionDelete ? initDeleteButton(ipAddress.getIpAddress().getId(), item) : new Listcell());
                            item.setParent(lbxRecords);
                        }
                    } else {
                        item = new Listitem();
                        item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.setParent(lbxRecords);
                    }
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ChangeIpAdressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
