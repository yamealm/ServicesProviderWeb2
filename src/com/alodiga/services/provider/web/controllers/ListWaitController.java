package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ListcellAddButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;

public class ListWaitController extends GenericAbstractListController<Product> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private TransactionEJB transactionEJB = null;
    private List<Product> products = null;
    private User currentUser;
    private Profile currentProfile;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Sessions.getCurrent().removeAttribute("customer");
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            permissionAdd = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_STOCK);
            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.REMOVE_STOCK);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_STOCK);
//            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_STOCK);
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
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            loadPermission(new Provider());
            startListener();
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
            String field = tmp.getDescription().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                auxList.add(tmp);
            }
        }
        return auxList;
    }


    public void loadList(List<Product> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            int stock = 0;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (Product product : list) {
                	try {
                		int  currentQuantity = transactionEJB.loadQuantityByProductId(product.getId(),Category.WAIT);
                		stock = currentQuantity;
                	} catch (Exception ex) {
                		stock = 0;
                    }
                    item = new Listitem();
                    item.setValue(product);
                    item.appendChild(new Listcell(product.getPartNumber()));
                    item.appendChild(new Listcell(product.getDescription()));
                    item.appendChild(new Listcell(product.getUbicationBox()));
                    item.appendChild(new Listcell(product.getUbicationFolder()));
                    item.appendChild(new Listcell(String.valueOf(product.getAmount())));
                    item.appendChild(new Listcell(String.valueOf(stock)));
                    item.appendChild(permissionAdd ? new ListcellAddButton("adminAddWait.zul", product,Permission.ADD_WAIT) : new Listcell());
//                    item.appendChild(permissionDelete && stock>0? new ListcellRemoveButton("adminEgressWait.zul", product,Permission.REMOVE_WAIT) : new Listcell());
//                    item.appendChild(permissionRead  && stock>0? new ListcellViewButton("listAddWait.zul", product,Permission.VIEW_WAIT) : new Listcell());
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
        products = new ArrayList<Product>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            request.setAuditData(null);
            products = transactionEJB.listProducts();
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
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.product"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxRecords,3);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.product.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

	@Override
	public void onClick$btnAdd() throws InterruptedException {
		  Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
	      Sessions.getCurrent().removeAttribute("object");
	      Executions.getCurrent().sendRedirect("adminAddWait.zul");
	}
    
}
