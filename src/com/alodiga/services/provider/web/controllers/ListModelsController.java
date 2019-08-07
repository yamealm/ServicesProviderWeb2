package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.CountryTranslation;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListModelsController extends GenericAbstractListController<Model> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private UtilsEJB utilsEJB = null;
    private List<Model> models = null;
    private User currentUser;
    private Profile currentProfile;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        
    }

    public void startListener() {
    }

    @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_MODEL));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_MODEL);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_MODEL);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.TURBINES);
            checkPermissions();
            adminPage = "adminModel.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            getData();
            loadList(models);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public List<Model> getFilteredList(String filter) {
    	 List<Model> auxList = new ArrayList<Model>();
         for (Iterator<Model> i = models.iterator(); i.hasNext();) {
        	 Model tmp = i.next();
             String field = tmp.getName().toLowerCase();
             if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                 auxList.add(tmp);
             }
         }
         return auxList;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Executions.getCurrent().sendRedirect(adminPage);

    }

    public void onClick$btnDelete() {
    }

    public void loadList(List<Model> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
                for (Model model : list) {
                    item = new Listitem();
                    item.setValue(model);
                    item.appendChild(new Listcell(model.getId().toString()));
                    item.appendChild(new Listcell(model.getBraund().getName()));
                    item.appendChild(new Listcell(model.getName()));
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, model, Permission.EDIT_MODEL) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, model, Permission.VIEW_MODEL) : new Listcell());
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
        models = new ArrayList<Model>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            models = utilsEJB.getModels();
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
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.model.list"));
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
