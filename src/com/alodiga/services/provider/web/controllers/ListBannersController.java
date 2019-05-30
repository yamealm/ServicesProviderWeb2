package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.BannerEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.ContentManager;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Banner;
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

public class ListBannersController extends GenericAbstractListController<Banner> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private BannerEJB bannerEJB = null;
    private List<Banner> banners = null;
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
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_BANNER));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_BANNER);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_BANNER);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_BANNER_STATUS);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("updateBanners", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {

            public void onEvent(Event evt) {
                getData();
                loadList(banners);
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
            adminPage = "adminBanner.zul";
            bannerEJB = (BannerEJB) EJBServiceLocator.getInstance().get(EjbConstants.BANNER_EJB);
            loadPermission(new Banner());
            getData();
            loadList(banners);
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

    public List<Banner> getFilteredList(String filter) {
        List<Banner> bannersaux = new ArrayList<Banner>();
        for (Iterator<Banner> i = banners.iterator(); i.hasNext();) {
            Banner tmp = i.next();
            String field = tmp.getName();
            if (field.toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0) {
                bannersaux.add(tmp);
            }
        }
        return bannersaux;
    }

    public void onClick$btnAdd() throws InterruptedException {
        AccessControl.saveAction(Permission.ADD_BANNER, adminPage);
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Executions.getCurrent().sendRedirect(adminPage);

    }

    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
        try {
            
            Banner banner = (Banner) listItem.getValue();
            AccessControl.saveAction(Permission.CHANGE_BANNER_STATUS, "changeStatus banner = "+banner.getId() +" to status = "+ !banner.isEnabled());
            button.changeImageStatus(banner.isEnabled());
            banner.setEnabled(!banner.isEnabled());
            listItem.setValue(banner);
            request.setParam(banner);
            bannerEJB.saveBanner(request);
            ContentManager contentManager = ContentManager.getInstance();
            contentManager.refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            showError(ex);
        }
    }

    public void loadList(List<Banner> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
                for (Banner banner : list) {
                    item = new Listitem();
                    item.setId(banner.getId().toString());
                    item.setValue(banner);
                    item.appendChild(new Listcell());
                    item.appendChild(new Listcell(banner.getName()));
                    item.appendChild(new Listcell(Utils.getBannerTypeName(banner.getBannerType().getId())));
                    item.appendChild(new Listcell(banner.getOrderList().toString()));
                    item.appendChild(permissionChangeStatus ? initEnabledButton(banner.isEnabled(), item) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, banner,Permission.EDIT_BANNER) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, banner,Permission.VIEW_BANNER) : new Listcell());
                    item.setParent(lbxRecords);
                }
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        banners = new ArrayList<Banner>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            banners = bannerEJB.getBanners(request);
        } catch (RegisterNotFoundException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
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

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.banner.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }
}
