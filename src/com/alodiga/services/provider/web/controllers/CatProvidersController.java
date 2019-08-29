package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
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
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class CatProvidersController extends GenericAbstractListController<Provider> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private UtilsEJB utilsEJB = null;
    private List<Provider> providers = null;
    private User currentUser;
    private Profile currentProfile;
    private Window winProvidersView;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_CUSTOMER));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_CUSTOMER);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_CUSTOMER);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_CUSTOMER_STATUS);
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
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            getData();
            loadList(providers);
        } catch (Exception ex) {
            showError(ex);
        }
    }

       public List<Provider> getFilteredList(String filter) {
        List<Provider> auxList = new ArrayList<Provider>();
        for (Iterator<Provider> i = providers.iterator(); i.hasNext();) {
        	Provider tmp = i.next();
            String field = tmp.getName().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                auxList.add(tmp);
            }
        }
        return auxList;
    }

    public void onClick$btnAdd() throws InterruptedException {
    	winProvidersView.detach();
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Window window = (Window)Executions.createComponents("addProvider.zul", null, null);
        Sessions.getCurrent().setAttribute("page1","catProviders.zul");
        try {
			window.doModal();
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void loadList(List<Provider> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (Provider provider : list) {
                    item = new Listitem();
                    item.setValue(provider);
                    item.appendChild(new Listcell(provider.getName()));
                    item.appendChild(new Listcell(provider.getAddress()));
                    item.setParent(lbxRecords);
                }
            } else {
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
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
            providers = utilsEJB.getProvider();
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

    public void onChange$txtAlias() throws InterruptedException {
        try {
            loadList(getFilteredList(txtAlias.getText()));
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
       
    public void onClick$btnDownload() throws InterruptedException {
        
    }
    
    public void onClick$btnSelect() {
    	Provider provider = (Provider) lbxRecords.getSelectedItem().getValue();
    	Sessions.getCurrent().setAttribute("provider",provider);
     	String page = (String) Sessions.getCurrent().getAttribute("page");
    	Executions.sendRedirect("./"+page);
   }
    
    public void onClick$btnCancel() {
    	winProvidersView.detach();
   }

	@Override
	public void onClick$btnSearch() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
   
   
    
}
