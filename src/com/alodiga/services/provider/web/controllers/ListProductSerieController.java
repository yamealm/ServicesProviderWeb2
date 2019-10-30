package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
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
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;

public class ListProductSerieController extends GenericAbstractListController<Product> {

	    private static final long serialVersionUID = -9145887024839938515L;
	    private Listbox lbxRecords;
	    private Textbox txtAlias;
	    private TransactionEJB transactionEJB = null;
	    private ProductEJB productEJB = null;
	    private List<Product> products = null;
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
	            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_STOCK);
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
	            adminPage = "adminAddStock.zul";
	            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
	            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
	            getData();
	            loadList(products);
	        } catch (Exception ex) {
	            showError(ex);
	        }
	    }



	    public List<Product> getFilteredList(String filter) {
	        List<Product> auxList = new ArrayList<Product>();
	        if (products != null) {
				for (Iterator<Product> i = products.iterator(); i.hasNext();) {
					Product tmp = i.next();
					String field = tmp.getPartNumber().toLowerCase();
					if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
						auxList.add(tmp);
					}
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
	                	if (product.getEnabled() && product.getCategory().getId().equals(Category.STOCK)) {
		                	try {
		                		int  currentQuantity = transactionEJB.loadQuantityByProductId(product.getId(), Category.STOCK);
		                		stock = currentQuantity;
		                	} catch (Exception ex) {
		                		stock = 0;
		                    }
		                    item = new Listitem();
		                    item.setValue(product);
		                    item.appendChild(new Listcell(product.getPartNumber()));
		                    item.appendChild(new Listcell(product.getDescription()));
		                    item.appendChild(new Listcell(product.getActNpNsn()));
		                    item.appendChild(new Listcell(product.getUbicationBox()));
		                    item.appendChild(new Listcell(product.getBatchNumber()));
		                    item.appendChild(new Listcell(product.getUbicationFolder()));
		                    item.appendChild(new Listcell(String.valueOf(product.getAmount())));
		                    item.appendChild(new Listcell(String.valueOf(product.getStockMin())));
		                    item.appendChild(new Listcell(String.valueOf(product.getStockMax())));
		                    item.appendChild(new Listcell(String.valueOf(stock)));
		                    item.appendChild(permissionRead ? new ListcellViewButton("detailStock.zul", product,Permission.VIEW_STOCK) : new Listcell());
		                    item.setParent(lbxRecords);
	                	}
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

	    public void onClick$btnSearch() throws InterruptedException {
	        try {
	            loadList(getFilteredList(txtAlias.getText()));
	            AccessControl.saveAction(Permission.STOCK, "Se busco listado de productos en stock");
	        } catch (Exception ex) {
	            showError(ex);
	        }
	    }
	    
	    public void onClick$btnExportPdf() throws InterruptedException {
	        try {
	        	PDFUtil.exportPdf((Labels.getLabel("sp.common.product"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte.stock"), lbxRecords,3);
	        	AccessControl.saveAction(Permission.STOCK, "Se descargo listado de productos en stock formato pdf");
	        } catch (Exception ex) {
	            showError(ex);
	        }
	    }
	    
	    public void onClick$btnDownload() throws InterruptedException {
	        try {
	            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.product.list.stock"));
	            AccessControl.saveAction(Permission.STOCK, "Se descargo listado de productos en stock formato excel");
	        } catch (Exception ex) {
	            showError(ex);
	        }
	    }

		@Override
		 public void onClick$btnAdd() throws InterruptedException {
	    }
}
