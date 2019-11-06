package com.alodiga.services.provider.web.controllers;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;

public class DetailsMetrologicalControlController extends GenericAbstractListController<MetrologicalControlHistory> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Button btnExportPdf;
    private ProductEJB productEJB = null;
    List<MetrologicalControlHistory> metrologicalControls = null;
    private Label lblInfo;
    private MetrologicalControl controlParam;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        controlParam = (Sessions.getCurrent().getAttribute("object") != null) ? (MetrologicalControl) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
    }

    public void startListener() {
        
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            getData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        lblInfo.setValue("");
        lblInfo.setVisible(false);
    }

    public void getData() {
        try {
            clearFields();
            clearMessage();
            EJBRequest _request = new EJBRequest();
            Map<String, Object> params = new HashMap<String, Object>();
			if (controlParam != null ) {
				params.put(QueryConstants.PARAM_CONTROL, controlParam.getId());
			_request.setParams(params);
			_request.setParam(true);
			metrologicalControls = productEJB.loadMetrologicalControlHistory(_request);
			 loadList(metrologicalControls);
			}
			 AccessControl.saveAction(Permission.REPORT_METEOROLOGICAL_CONTROL, "Se busco el detalle de Control Metrologico");
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            loadList(null);
        }
    }

    public void loadList(List<MetrologicalControlHistory> list) {
        try {
            lbxReport.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (MetrologicalControlHistory control : list) {
              
                    item = new Listitem();
                    item.setValue(control);
                    item.appendChild(new Listcell(control.getMetrologicalControl().getDesignation()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getInstrument()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getBraund().getName()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getModel().getName()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getSerie()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getRango()));
                    String date = null;
					if (control.getCalibrationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(control.getCalibrationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    if (control.getExpirationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(control.getExpirationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getUbication()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getScale()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getControlType()));
                    item.setParent(lbxReport);
                }
                btnDownload.setVisible(true);
                btnExportPdf.setVisible(true);
            } else {
                btnDownload.setVisible(false);
                btnExportPdf.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell());
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxReport);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }


    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport, Labels.getLabel("sp.crud.metrological.control.list.reporte"));
            AccessControl.saveAction(Permission.REPORT_METEOROLOGICAL_CONTROL, "Se descargo detalle de Control Metrologico formato excel");
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.meteorological"))+".pdf", Labels.getLabel("sp.crud.metrological.control.list.reporte"), lbxReport,0);
        	AccessControl.saveAction(Permission.REPORT_METEOROLOGICAL_CONTROL, "Se descargo detalle de Control Metrologico stock formato pdf");
        } catch (Exception ex) {
            showError(ex);
        }
    }


    public void onClick$btnAdd() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public List<MetrologicalControlHistory> getFilteredList(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick$btnClear() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick$btnSearch() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

}
