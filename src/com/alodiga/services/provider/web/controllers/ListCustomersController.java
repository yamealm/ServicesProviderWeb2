package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ChangeStatusButton;
import com.alodiga.services.provider.web.components.ListcellAddDescendantButton;
import com.alodiga.services.provider.web.components.ListcellDetailsButton;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListCustomersController extends GenericAbstractListController<Customer> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtName;
    private Textbox txtLogin;
    private Textbox txtEmail;
    private Combobox cmbEnterprises;
    private UtilsEJB utilsEJB = null;
    private CustomerEJB customerEJB = null;
    private List<Customer> customers = null;
    private User currentUser;
    private Profile currentProfile;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_CUSTOMER);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_CUSTOMER);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_CUSTOMER_STATUS);
//            distributorMonitory = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_MONITORING_DISTRIBUTOR);
//            sendDistributorData = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.SEND_DISTRIBUTOR_DATA);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            adminPage = "adminCustomer.zul";
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
            checkPermissions();
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadEnterprises();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private Listcell initEnabledButton(final Boolean enabled, final Listitem listItem) {

        Listcell cell = new Listcell();
        cell.setValue("");
        final ChangeStatusButton button = new ChangeStatusButton(enabled);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.changeStatus"));
        button.setClass("open orange");
        button.addEventListener("onClick", new EventListener() {

            public void onEvent(Event event) throws Exception {
                changeStatus(button, listItem);
            }
        });

        button.setParent(cell);
        return cell;
    }

    

    public List<Customer> getFilteredList(String filter) {
        List<Customer> listAux = new ArrayList<Customer>();
        for (Iterator<Customer> i = customers.iterator(); i.hasNext();) {
            Customer tmp = i.next();
            String field = tmp.getFirstName();
            if (field.toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0) {
                listAux.add(tmp);
            }
        }
        return listAux;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect(adminPage);
    }

    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
        try {
            Customer customer = (Customer) listItem.getValue();
            button.changeImageStatus(customer.getEnabled());
            customer.setEnabled(!customer.getEnabled());
            listItem.setValue(customer);

            //request.setAuditData(AccessControl.getCurrentAudit());
            request.setParam(customer);
            customerEJB.saveCustomer(request);
            AccessControl.saveAction(Permission.CHANGE_CUSTOMER_STATUS, "changeStatus customer = " + customer.getId() + " to status = " + !customer.getEnabled());

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void forwardData(Customer customer) {
        try {

            //request.setAuditData(AccessControl.getCurrentAudit());
            request.setParam(customer);
            customerEJB.saveCustomer(request);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void loadList(List<Customer> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (Customer customer : list) {

                    item = new Listitem();
                    item.setValue(customer);
                    item.appendChild(new Listcell(customer.getFirstName()));
                    item.appendChild(new Listcell(customer.getLastName()));
                    item.appendChild(new Listcell(customer.getPhoneNumber()));
                    item.appendChild(new Listcell(customer.getEmail()));
                    item.appendChild(permissionChangeStatus ? initEnabledButton(customer.getEnabled(), item) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, customer,Permission.EDIT_CUSTOMER) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, customer,Permission.VIEW_CUSTOMER) : new Listcell());
//                    item.appendChild(sendDistributorData ? initForwardDataButton(distributor) : new Listcell());
//                    item.appendChild(permissionAdd ? new ListcellAddDescendantButton(adminPage, customer,Permission.ADD_DISTRIBUTOR) : new Listcell());
//                    item.appendChild(distributorMonitory ? new ListcellDetailsButton("viewMonitoringDistributor.zul", distributor, Permission.DISTRIBUTION_MONITORY) : new Listcell());
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
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

    public void getData() {
        customers = new ArrayList<Customer>();
        try {
            request.setFirst(0);
            request.setLimit(null);
//            request.setAuditData(AccessControl.getCurrentAudit());
            customers = customerEJB.getCustomers(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.distributor.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        txtName.setText("");
        txtLogin.setText("");
        txtEmail.setText("");
        lbxRecords.getItems().clear();
    }

    public void onClick$btnSearch() throws InterruptedException {
        try {
            String email = !txtEmail.getText().isEmpty() ? txtEmail.getText() : null;
            String login = !txtLogin.getText().isEmpty() ? txtLogin.getText() : null;
            String fullName = !txtName.getText().isEmpty() ? txtName.getText() : null;
            Long enterpriseId = ((Enterprise) cmbEnterprises.getSelectedItem().getValue()).getId();
            loadList(customerEJB.searchCustomers(enterpriseId, login, fullName, email));
        } catch (Exception ex) {
            showError(ex);
        }
    }

   
    private void loadEnterprises() {
        List<Enterprise> enterprises = null;
        try {
            cmbEnterprises.getItems().clear();
            enterprises = utilsEJB.getEnterprises();
            for (Enterprise e : enterprises) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbEnterprises);

            }
            cmbEnterprises.setSelectedIndex(0);
        } catch (Exception ex) {
            showError(ex);
        }

    }


}
