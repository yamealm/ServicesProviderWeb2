package com.alodiga.services.provider.web.controllers;


import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Account;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.SMS;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import org.zkoss.zul.Datebox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listgroup;
import org.zkoss.zul.Listitem;

public class ListSMSController extends GenericAbstractListController<SMS> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Combobox cmbAccounts;
    private Datebox dtbBeginningDate;
    private Datebox dtbendingDate;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    Boolean isStoreAll = false;
    List<SMS> list = null;
    private Label lblInfo;
    //private Account accountParam;
    private User currentUser;
    private Profile currentProfile;
    private Listgroup lt;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        
        super.doAfterCompose(comp);
        //accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_SMS);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("updateSMS", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {

            public void onEvent(Event evt) {
                loadList(list);
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
            checkPermissions();
            adminPage = "viewDetailsSMS.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            getData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        lblInfo.setValue("");
        lblInfo.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (cmbAccounts.getSelectedIndex() == -1) {
            cmbAccounts.setFocus(true);
            this.showMessage("sp.error.field.required", true, null);
        } else {
            return true;
        }
        return false;

    }

    public void onClick$btnSearch() {
        try {
            if (validateEmpty()) {
                clearFields();
                Account account = null;
                if(cmbAccounts.getSelectedItem()!=null){
                    account = (Account)cmbAccounts.getSelectedItem().getValue();
                }
                list = utilsEJB.searchSMS(dtbBeginningDate.getValue(),dtbendingDate.getValue(), account );
                loadList(list);
            }
        } catch (EmptyListException ex) {
            loadList(null);
        }catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadAccounts() {
        try {
            cmbAccounts.getItems().clear();
            request.setFirst(0);
            request.setLimit(null);
            List<Account> accounts = userEJB.getAccounts(request);
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbAccounts);
            cmbAccounts.setSelectedItem(item);
            for (Account a : accounts) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(a.getFullName());
                cmbItem.setValue(a);
                cmbItem.setParent(cmbAccounts);
//                if (accountParam != null && accountParam.getId().equals(a.getId())) {
//                    cmbAccounts.setSelectedItem(cmbItem);
//                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void loadList(List<SMS> list) {
        try {
            lbxReport.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (SMS sms : list) {
                    item = new Listitem();
                    item.setValue(sms);
                    item.appendChild(new Listcell(sms.getIntegratorName()));
                    item.appendChild(new Listcell(sms.getCreationDate().toString()));
                    item.appendChild(new Listcell(sms.getDestination()));
                    item.appendChild(new Listcell(sms.getStatus()));
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, sms,Permission.VIEW_SMS) : new Listcell());
                    item.setParent(lbxReport);
                }
                btnDownload.setVisible(true);
            } else {
                btnDownload.setVisible(false);
                lblInfo.setVisible(true);
                lblInfo.setValue(Labels.getLabel("sp.error.empty.list"));
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        cmbAccounts.setSelectedIndex(0);
        Date date = new Date();
        dtbBeginningDate.setValue(date);
        dtbendingDate.setValue(date);
        clearFields();
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport, Labels.getLabel("sp.crud.sms.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        dtbBeginningDate.setFormat("yyyy/MM/dd");
        dtbBeginningDate.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbendingDate.setFormat("yyyy/MM/dd");
        dtbendingDate.setValue(new Timestamp(new java.util.Date().getTime()));
        loadAccounts();
    }

    public void onClick$btnAdd() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SMS> getFilteredList(String filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
