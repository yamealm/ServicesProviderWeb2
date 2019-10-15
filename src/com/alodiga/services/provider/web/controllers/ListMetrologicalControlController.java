package com.alodiga.services.provider.web.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ChangeStatusButton;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;

public class ListMetrologicalControlController extends GenericAbstractListController<MetrologicalControl> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private TransactionEJB transactionEJB = null;
    private List<MetrologicalControl> metrologicalControls = null;
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
            permissionAdd = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_METEOROLOGICAL_CONTROL);
            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.REMOVE_METEOROLOGICAL_CONTROL);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_METEOROLOGICAL_CONTROL);
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_METEOROLOGICAL_CONTROL);
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
            adminPage = "viewMetrologicalControl.zul";
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            loadPermission(new Provider());
            startListener();
            getData();
            loadList(metrologicalControls);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public List<MetrologicalControl> getFilteredList(String filter) {
        List<MetrologicalControl> auxList = new ArrayList<MetrologicalControl>();
		if (metrologicalControls != null) {
			for (Iterator<MetrologicalControl> i = metrologicalControls.iterator(); i.hasNext();) {
				MetrologicalControl tmp = i.next();
				String field = tmp.getInstrument().toLowerCase();
				if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
					auxList.add(tmp);
				}
			}
		}
        return auxList;
    }


    public void loadList(List<MetrologicalControl> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                btnDownload.setVisible(true);
                for (MetrologicalControl metrologicalControl : list) {
                
                    item = new Listitem();
                    item.setValue(metrologicalControl);
                    item.appendChild(new Listcell(metrologicalControl.getDesignation()));
                    item.appendChild(new Listcell(metrologicalControl.getInstrument()));
                    item.appendChild(new Listcell(metrologicalControl.getBraund().getName()));
                    item.appendChild(new Listcell(metrologicalControl.getModel().getName()));
                    item.appendChild(new Listcell(metrologicalControl.getSerie()));
                    item.appendChild(new Listcell(metrologicalControl.getRango()));
                    MetrologicalControlHistory history = transactionEJB.loadLastMetrologicalControlHistoryByMetrologicalControlId(metrologicalControl.getId());
                    String date = null;
                    if (history != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(history.getCalibrationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    date = null;
                    if (history != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(history.getExpirationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    item.appendChild(new Listcell(metrologicalControl.getUbication()));
                    item.appendChild(new Listcell(metrologicalControl.getScale()));
                    item.appendChild(new Listcell(metrologicalControl.getControlType()));
                    item.appendChild(permissionDelete ? initEnabledButton(metrologicalControl.isEnabled(), item) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, metrologicalControl,Permission.EDIT_METEOROLOGICAL_CONTROL) : new Listcell());
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item = new Listitem();
                item = new Listitem();
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }
            AccessControl.saveAction(Permission.METEOROLOGICAL_CONTROL, "Se busco listado de Control Metrologico");
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
    
    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
        try {
            MetrologicalControl metrologicalControl = (MetrologicalControl) listItem.getValue();
            button.changeImageStatus(metrologicalControl.isEnabled());
            metrologicalControl.setEnabled(!metrologicalControl.isEnabled());
            listItem.setValue(metrologicalControl);
            request.setParam(metrologicalControl);
            transactionEJB.saveMetrologicalControl(metrologicalControl);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
    	metrologicalControls = new ArrayList<MetrologicalControl>();
        try {
        	EJBRequest request = new EJBRequest();
        	Map<String, Object> params = new HashMap<String, Object>();
        	request.setParams(params);
            request.setParam(true);
            metrologicalControls = transactionEJB.searchMetrologicalControl(request);
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
            AccessControl.saveAction(Permission.METEOROLOGICAL_CONTROL, "Se busco listado de Control Metrologico");
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.meteorological"))+".pdf", Labels.getLabel("sp.crud.metrological.control.list.reporte"), lbxRecords,3);
        	AccessControl.saveAction(Permission.METEOROLOGICAL_CONTROL, "Se descargo listado de Control Metrologico formato pdf");
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.product.list.meteorological"));
            AccessControl.saveAction(Permission.METEOROLOGICAL_CONTROL, "Se descargo listado de Control Metrologico formato excel");
        } catch (Exception ex) {
            showError(ex);
        }
    }

	@Override
	public void onClick$btnAdd() throws InterruptedException {
		Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
		Sessions.getCurrent().setAttribute("object", null);
		Executions.sendRedirect("./adminAddMetrorologicalControl.zul");
	}
    
}
