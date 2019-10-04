package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
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
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListProfilesController extends GenericAbstractListController<Profile> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private AccessControlEJB accessEJB = null;
    private List<Profile> profiles = null;
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
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_PROFILE));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_PROFILE);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_PROFILE);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_PROFILE_STATUS);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("updateProfiles", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {

            public void onEvent(Event evt) {
                getData();
                loadList(profiles);
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.TURBINES);
            checkPermissions();
            adminPage = "adminProfile.zul";
            accessEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
            //loadPermission(new Profile());
            //startListener();
            getData();
            loadList(profiles);
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

    public List<Profile> getFilteredList(String filter) {
        List<Profile> list = new ArrayList<Profile>();
        for (Iterator<Profile> i = profiles.iterator(); i.hasNext();) {
            Profile tmp = i.next();
            String field = tmp.getProfileDataByLanguageId(languageId) != null ? tmp.getProfileDataByLanguageId(languageId).getAlias() : tmp.getName();
            if (field.toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0) {
                list.add(tmp);
            }
        }
        return list;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Sessions.getCurrent().removeAttribute("object");
        Executions.getCurrent().sendRedirect(adminPage);
    }

    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
        try {
            Profile profile = (Profile) listItem.getValue();
            button.changeImageStatus(profile.getEnabled());
            profile.setEnabled(!profile.getEnabled());
            listItem.setValue(profile);
            //request.setAuditData(AccessControl.getCurrentAudit());
            request.setParam(profile);
            accessEJB.saveProfile(request);
            AccessControl.saveAction(Permission.CHANGE_PROFILE_STATUS, "changeStatus profile = " + profile.getId() + " to status = " + !profile.getEnabled());

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void loadList(List<Profile> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (Profile profile : list) {

                    item = new Listitem();
                    item.setValue(profile);
                    item.appendChild(new Listcell(profile.getProfileDataByLanguageId(languageId) != null ? profile.getProfileDataByLanguageId(languageId).getAlias() : profile.getName()));
                    item.appendChild(permissionChangeStatus ? initEnabledButton(profile.getEnabled(), item) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, profile,Permission.EDIT_PROFILE) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, profile,Permission.VIEW_PROFILE) : new Listcell());
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
        profiles = new ArrayList<Profile>();
        try {
            request.setFirst(0);
            request.setLimit(null);
//            request.setAuditData(AccessControl.getCurrentAudit());
            profiles = accessEJB.getProfiles(request);
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
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.bread.crumb.profile.list"));
            AccessControl.saveAction(Permission.LIST_PROFILES, "Se descargo listado de perfiles formato excel");
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
