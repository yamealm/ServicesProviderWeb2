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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
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
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class CatCustomersController extends GenericAbstractListController<Customer> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private CustomerEJB customerEJB = null;
    private List<Customer> customers = null;
    private User currentUser;
    private Profile currentProfile;
    private Window winCustomersView;

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
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            getData();
            loadList(customers);
        } catch (Exception ex) {
            showError(ex);
        }
    }

       public List<Customer> getFilteredList(String filter) {
        List<Customer> auxList = new ArrayList<Customer>();
        for (Iterator<Customer> i = customers.iterator(); i.hasNext();) {
            Customer tmp = i.next();
            String field = tmp.getFirstName().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                auxList.add(tmp);
            }
        }
        return auxList;
    }

    public void onClick$btnAdd() throws InterruptedException {
    	winCustomersView.detach();
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Window window = (Window)Executions.createComponents("addCustomer.zul", null, null);
        Sessions.getCurrent().setAttribute("page1","catCustomers.zul");
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

    public void loadList(List<Customer> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (Customer customer : list) {
                    item = new Listitem();
                    item.setValue(customer);
                    item.appendChild(new Listcell(customer.getFirstName()));
                    item.appendChild(new Listcell(customer.getLastName()));
                    item.appendChild(new Listcell(customer.getDni()));
                    item.appendChild(new Listcell(customer.getPhoneNumber()));
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
        customers = new ArrayList<Customer>();
        try {
            request.setFirst(0);
            request.setLimit(null);
//            request.setAuditData(null);
            customers = customerEJB.getCustomers(request);
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
		if (lbxRecords.getSelectedItem() != null) {
			Customer customer = (Customer) lbxRecords.getSelectedItem().getValue();
			Sessions.getCurrent().setAttribute("customer", customer);
			String page = (String) Sessions.getCurrent().getAttribute("page");
			Executions.sendRedirect("./" + page);
		} else {
			try {
				Messagebox.show(Labels.getLabel("sp.error.customers.notSelected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
   }
    
    public void onClick$btnCancel() {
    	winCustomersView.detach();
   }

	@Override
	public void onClick$btnSearch() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
   
   
    
}
