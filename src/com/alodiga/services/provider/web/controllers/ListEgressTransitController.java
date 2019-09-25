package com.alodiga.services.provider.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
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

public class ListEgressTransitController extends GenericAbstractListController<Product> {

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
        	Sessions.getCurrent().removeAttribute("customer");
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
        for (Iterator<Product> i = products.iterator(); i.hasNext();) {
            Product tmp = i.next();
            String field = tmp.getDescription().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                auxList.add(tmp);
            }
        }
        return auxList;
    }


    public void loadData() {
    	Listitem item = null;
        try {
            lbxRecords.getItems().clear();
            List<ProductSerie> producSeries = transactionEJB.searchProductSerieByCategoryId(Category.TRANSIT);
            if (producSeries != null && !producSeries.isEmpty()) {
                btnDownload.setVisible(true);
                for (ProductSerie productSerie : producSeries) {
                    item = new Listitem();
                    item.setValue(productSerie);
                    item.appendChild(new Listcell(productSerie.getProduct().getPartNumber()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    item.appendChild(new Listcell(productSerie.getSerie()));
                    item.appendChild(new Listcell(String.valueOf(productSerie.getQuantity())));
                    item.appendChild(new Listcell(productSerie.getCategory().getName()));
                    item.appendChild(new Listcell(productSerie.getProduct().getUbicationBox()));
                    String date = null;
					if (productSerie.getExpirationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getExpirationDate().getTime());
					}
					item.appendChild(new Listcell(date));
					date = null;
                    if (productSerie.getCure() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getCure().getTime());
					}
                    item.appendChild(new Listcell(date));
                    item.appendChild(permissionEdit ? new ListcellEditButton("viewTransit.zul", productSerie,Permission.EDIT_TRANSIT) : new Listcell());
                    item.appendChild(permissionDelete  ? new ListcellRemoveButton ("adminEgressInitTransit.zul", productSerie,Permission.REMOVE_TRANSIT) : new Listcell());
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
    
	private void deleteProductSerie(Listitem listItem) {
		try {
			Messagebox.show(Labels.getLabel("sp.common.actions.delete.question"),
					Labels.getLabel("sp.common.actions.delete.confirm"), Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						@Override
						public void onEvent(final Event evt) throws InterruptedException {
							if ("onYes".equals(evt.getName())) {
								try {
									ProductSerie productSerie = (ProductSerie) listItem.getValue();
									transactionEJB.deleteStock(productSerie.getBeginTransactionId(), productSerie);
									AccessControl.saveAction(Permission.REMOVE_TRANSIT,
											"product = " + productSerie.getProduct().getId() + " and product serie = "
													+ productSerie.getSerie());
									loadData();
								} catch (Exception ex) {
									showError(ex);
								}
							}
						}
					});

		} catch (InterruptedException e) {
			e.printStackTrace();
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
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.product.list.transit"));
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
