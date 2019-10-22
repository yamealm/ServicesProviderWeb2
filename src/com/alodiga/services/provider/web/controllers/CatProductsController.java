package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.EditButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class CatProductsController extends GenericAbstractListController<Product> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private ProductEJB productEJB = null;
    private List<Product> products = null;
    private User currentUser;
    private Profile currentProfile;
    private Window winProductsView;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_PRODUCT));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_PRODUCT);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_PRODUCT);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_PRODUCT_STATUS);
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
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            checkPermissions();
            getData();
            loadList(products);
        } catch (Exception ex) {
            showError(ex);
        }
    }

       public List<Product> getFilteredList(String filter) {
        List<Product> auxList = new ArrayList<Product>();
        for (Iterator<Product> i = products.iterator(); i.hasNext();) {
            Product tmp = i.next();
            String field = tmp.getPartNumber().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                auxList.add(tmp);
            }
        }
        return auxList;
    }

    public void onClick$btnAdd() throws InterruptedException {
    	winProductsView.detach();
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Window window = (Window)Executions.createComponents("addProduct.zul", null, null);
        Sessions.getCurrent().setAttribute("page1","catProducts.zul");
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

    public void loadList(List<Product> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (Product product : list) {
                	if (product.getEnabled()) {
	                    item = new Listitem();
	                    item.setValue(product);
	                    item.appendChild(new Listcell(product.getPartNumber()));
	                    item.appendChild(new Listcell(product.getDescription()));
	                    item.appendChild(new Listcell(String.valueOf(product.getAmount())));
	                    item.appendChild(permissionEdit ? initEnabledButton(product.getEnabled(), item) : new Listcell());
	                    item.setParent(lbxRecords);
                	}
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

    public void getData() {
        products = new ArrayList<Product>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            request.setAuditData(null);
            products = productEJB.getProducts(request);
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
			Product product = (Product) lbxRecords.getSelectedItem().getValue();
			Sessions.getCurrent().setAttribute("object", product);
			String page = (String) Sessions.getCurrent().getAttribute("page");
			Executions.sendRedirect("./" + page);
		} else {
			try {
				Messagebox.show(Labels.getLabel("sp.error.products.notSelected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
   }
    
    public void onClick$btnCancel() {
    	winProductsView.detach();
   }

	@Override
	public void onClick$btnSearch() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
    
	 private Listcell initEnabledButton(final Boolean enabled, final Listitem listItem) {
	        Listcell cell = new Listcell();
	        cell.setValue("");
	        final EditButton button = new EditButton();
	        button.setTooltiptext(Labels.getLabel("sp.common.actions.edit"));
	        button.setClass("open orange");
	        button.addEventListener("onClick", new EventListener() {
	            public void onEvent(Event event) throws Exception {
	                changeStatus(button, listItem);
	            }
	        });

	        button.setParent(cell);
	        return cell;
	    }
	 
	 private void changeStatus(EditButton button, Listitem listItem) {
	        try {
	            Product product = (Product) listItem.getValue();
	            AccessControl.saveAction(Permission.EDIT_PRODUCT, "edit product = " + product.getPartNumber());
	        	Sessions.getCurrent().setAttribute("object",product);
	        	Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_EDIT);
	        	winProductsView.detach();
	            Window window = (Window)Executions.createComponents("addProduct.zul", null, null);
	            Sessions.getCurrent().setAttribute("page1","catProducts.zul");
	            try {
	    			window.doModal();
	    		} catch (SuspendNotAllowedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        } catch (Exception ex) {
	            showError(ex);
	        }
	    }
}
