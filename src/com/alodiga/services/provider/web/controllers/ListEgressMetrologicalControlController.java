package com.alodiga.services.provider.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.DeleteButton;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.components.ListcellRemoveButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;

public class ListEgressMetrologicalControlController extends GenericAbstractListController<Product> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private ProductEJB productEJB = null;
    private TransactionEJB transactionEJB = null;
    private List<Product> products = null;
    private User currentUser;
    private Profile currentProfile;
    private User user;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        user = AccessControl.loadCurrentUser();
        initialize();
    }

    @Override
    public void checkPermissions() {
        try {
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_STOCK);   
            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.REMOVE_STOCK);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_QUARANTINE);
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
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            loadData();
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
   
    public List<Product> getFilteredList(String filter) {
        List<Product> auxList = new ArrayList<Product>();
		if (products != null) {
			for (Iterator<Product> i = products.iterator(); i.hasNext();) {
				Product tmp = i.next();
				String field = tmp.getDescription().toLowerCase();
				if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
					auxList.add(tmp);
				}
			}
		}
        return auxList;
    }


    public void loadData() {
    	Listitem item = null;
        try {
            lbxRecords.getItems().clear();
            List<MetrologicalControl> controls = transactionEJB.searchMetrologicalControl();
            if (controls != null && !controls.isEmpty()) {
                btnDownload.setVisible(true);
                for (MetrologicalControl control : controls) {
                    item = new Listitem();
                    item.setValue(control);
                    item.appendChild(new Listcell(control.getDesignation()));
                    item.appendChild(new Listcell(control.getInstrument()));
                    item.appendChild(new Listcell(control.getBraund().getName()));
                    item.appendChild(new Listcell(control.getModel().getName()));
                    item.appendChild(new Listcell(control.getSerie()));
                    item.appendChild(new Listcell(control.getRango()));
                    MetrologicalControlHistory history = null;
                    String date = null;
                    String date2 = null;
            		try {
            			history = transactionEJB.loadLastMetrologicalControlHistoryByMetrologicalControlId(control.getId());
            			if (history.getCalibrationDate() != null) {
            				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            				date = df.format(history.getCalibrationDate().getTime());
            			}
            			if (history.getExpirationDate() != null) {
            				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            				date = df.format(history.getExpirationDate().getTime());
            			}
            		} catch (Exception e) {
            			e.printStackTrace();
            		} 
                    item.appendChild(new Listcell(date));
                    item.appendChild(new Listcell(date2));
                    item.appendChild(new Listcell(control.getUbication()));
                    item.appendChild(new Listcell(control.getScale()));
                    item.appendChild(new Listcell(control.getControlType()));
                    item.appendChild(permissionEdit ? new ListcellEditButton("viewMetrologicalControl.zul", control,Permission.EDIT_METEOROLOGICAL_CONTROL) : new Listcell());
                    item.appendChild(permissionDelete  ? new ListcellRemoveButton ("adminEgressInitMetrologicalControl.zul", control,Permission.REMOVE_METEOROLOGICAL_CONTROL) : new Listcell());
                    item.appendChild(permissionDelete ? initDeleteButton(item) : new Listcell());
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
        } catch (EmptyListException ex) {
        	btnDownload.setVisible(false);
            item = new Listitem();
            item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
            item.appendChild(new Listcell());
            item.appendChild(new Listcell());
            item.appendChild(new Listcell());
            item.setParent(lbxRecords);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private Listcell initDeleteButton(final Listitem listItem) {

        Listcell cell = new Listcell();
        cell.setValue("");
        final DeleteButton button = new DeleteButton();
        button.setTooltiptext(Labels.getLabel("sp.common.actions.delete"));
        button.setClass("open orange");
        button.addEventListener("onClick", new EventListener() {

            public void onEvent(Event event) throws Exception {
            	deleteProductSerie( listItem);
            }
        });

        button.setParent(cell);
        return cell;
    }
    
    private void deleteProductSerie( Listitem listItem) {
        try {
            ProductSerie productSerie = (ProductSerie) listItem.getValue();
            transactionEJB.deleteStock(productSerie.getBeginTransactionId(), productSerie);
            AccessControl.saveAction(Permission.REMOVE_METEOROLOGICAL_CONTROL, "product = " + productSerie.getProduct().getId() + " and product serie = " + productSerie.getSerie());
            loadData();
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

  


    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.product"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxRecords,3);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.product.list.meteorological"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

	@Override
	public void onClick$btnAdd() throws InterruptedException {
		
	}

	@Override
	public void loadList(List<Product> list) {
		// TODO Auto-generated method stub
		
	}
    
}
