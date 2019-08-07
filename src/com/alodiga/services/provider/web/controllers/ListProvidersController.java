package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ChangeStatusButton;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListProvidersController extends GenericAbstractListController<Provider> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private ProductEJB productEJB = null;
    private List<Provider> providers = null;
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
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_PROVIDER));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_PROVIDER);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_PROVIDER);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_PROVIDER_STATUS);
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
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.TURBINES);
            checkPermissions();
            adminPage = "adminProvider.zul";
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            loadPermission(new Provider());
            startListener();
            getData();
            loadList(providers);
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

    public List<Provider> getFilteredList(String filter) {
        List<Provider> providersaux = new ArrayList<Provider>();
        for (Iterator<Provider> i = providers.iterator(); i.hasNext();) {
            Provider tmp = i.next();
            String field = tmp.getName().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                providersaux.add(tmp);
            }
        }
        return providersaux;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect(adminPage);
    }

    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
        try {
            Provider provider = (Provider) listItem.getValue();
            button.changeImageStatus(provider.getEnabled());
            provider.setEnabled(!provider.getEnabled());
            listItem.setValue(provider);
            request.setAuditData(AccessControl.getCurrentAudit());
            request.setParam(provider);
            productEJB.saveProvider(request);
            AccessControl.saveAction(Permission.CHANGE_PROVIDER_STATUS, "changeStatus provider = " + provider.getId() + " to status = " + !provider.getEnabled());

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void loadList(List<Provider> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (Provider provider : list) {

                    item = new Listitem();
                    item.setValue(provider);
                    item.appendChild(new Listcell(provider.getName()));
                    item.appendChild(new Listcell(provider.getAddress()));
                    item.appendChild(new Listcell(provider.getEnabled() ? Labels.getLabel("sp.common.yes") : Labels.getLabel("sp.common.no")));
                    item.appendChild(permissionChangeStatus ? initEnabledButton(provider.getEnabled(), item) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, provider,Permission.EDIT_PROVIDER) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, provider,Permission.VIEW_PROVIDER) : new Listcell());
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
        providers = new ArrayList<Provider>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            request.setAuditData(null);
            providers = productEJB.getProviders(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.provider.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        txtAlias.setText("");
    }

    public void onClick$btnSearch() throws InterruptedException {
        try {
            loadList(getFilteredList(txtAlias.getText()));
        } catch (Exception ex) {
            showError(ex);
        }
    }
}
