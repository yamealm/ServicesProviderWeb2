//package com.alodiga.services.provider.web.controllers;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.managers.PermissionManager;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.components.ListcellEditButton;
//import com.alodiga.services.provider.web.components.ListcellViewButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.Utils;
//
//public class TopUpCommissionAccountsController extends GenericAbstractListController<Account> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxRecords;
//    private Textbox txtName;
//    private Textbox txtLogin;
//    private Textbox txtEmail;
//    private Combobox cmbLevels;
//    private Combobox cmbEnterprises;
//    private UtilsEJB utilsEJB = null;
//    private UserEJB userEJB = null;
//    private TopUpProductEJB topUpEJB = null;
//    private List<Account> accounts = null;
//    private User currentUser;
//    private Profile currentProfile;
//    private boolean editTopUpPercent = false;
//    private boolean viewTopUpPercent = false;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    @Override
//    public void checkPermissions() {
//        try {
//            editTopUpPercent = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.TOP_UP_CALCULATION_EDIT);
//            viewTopUpPercent = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.TOP_UP_CALCULATION_VIEW);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            adminPage = "topUpCommissionEdit.zul";
//            currentUser = AccessControl.loadCurrentUser();
//            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
//            checkPermissions();
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            topUpEJB = (TopUpProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.TOP_UP_EJB);
//            loadEnterprises();
//        } catch (Exception ex) {
//            showError(ex);
//        }
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
//    @Override
//    public void onClick$btnAdd() throws InterruptedException {
//
//    }
//
//    public void loadList(List<Account> list) {
//        try {
//            lbxRecords.getItems().clear();
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
//                for (Account account : list) {
//
//                    item = new Listitem();
//                    item.setValue(account);
//                    item.appendChild(new Listcell(account.getFullName()));
//                    item.appendChild(new Listcell(account.getLogin()));
//                    item.appendChild(new Listcell(account.getEmail()));
//                    item.appendChild(new Listcell(topUpEJB.getTopUpCalculationByAccountId(account.getId()) + " %"));
//                    item.appendChild(viewTopUpPercent ? new ListcellViewButton(adminPage, account,Permission.TOP_UP_CALCULATION_VIEW) : new Listcell());
//                    item.appendChild(editTopUpPercent ? new ListcellEditButton(adminPage, account,Permission.TOP_UP_CALCULATION_EDIT) : new Listcell());
//                    item.setParent(lbxRecords);
//                }
//            } else {
//                btnDownload.setVisible(false);
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
//    public void getData() {
//        accounts = new ArrayList<Account>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            accounts = userEJB.getAccounts(request);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        try {
//            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.topUpCommission.list"));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnClear() throws InterruptedException {
//        txtName.setText("");
//        txtLogin.setText("");
//        txtEmail.setText("");
//        cmbLevels.setSelectedIndex(0);
//        lbxRecords.getItems().clear();
//    }
//
//    public void onClick$btnSearch() throws InterruptedException {
//        try {
//            String email = !txtEmail.getText().isEmpty() ? txtEmail.getText() : null;
//            String login = !txtLogin.getText().isEmpty() ? txtLogin.getText() : null;
//            String fullName = !txtName.getText().isEmpty() ? txtName.getText() : null;
//            Long enterpriseId = ((Enterprise) cmbEnterprises.getSelectedItem().getValue()).getId();
//            loadList(userEJB.searchAccounts(enterpriseId, login, fullName, email,"1"));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    
//    private void loadEnterprises() {
//        List<Enterprise> enterprises = null;
//        try {
//            cmbEnterprises.getItems().clear();
//            enterprises = utilsEJB.getEnterprises();
//            for (Enterprise e : enterprises) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(e.getName());
//                cmbItem.setValue(e);
//                cmbItem.setParent(cmbEnterprises);
//
//            }
//            cmbEnterprises.setSelectedIndex(0);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    @Override
//    public void startListener() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//
//}
