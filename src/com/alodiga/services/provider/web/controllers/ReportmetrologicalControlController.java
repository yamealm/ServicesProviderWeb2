package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.EnterCalibration;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.TransactionType;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zul.Datebox;
import org.apache.log4j.lf5.viewer.categoryexplorer.CategoryAbstractCellEditor;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ReportmetrologicalControlController extends GenericAbstractListController<MetrologicalControlHistory> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Combobox cmbBraund;
    private Combobox cmbModel;
    private Combobox cmbEnterCalibration;
    private Button btnExportPdf;
    private Datebox dtbBeginningDate;
    private Datebox dtbEndingDate;
    private Textbox txtSerial;
    private UtilsEJB utilsEJB = null;
    private CustomerEJB customerEJB = null;
    private ProductEJB productEJB = null;
    private TransactionEJB transactionEJB = null;
    private List<Braund> braunds;
    private List<Model> models;
    private List<EnterCalibration> enterCalibrations;
    List<MetrologicalControlHistory> metrologicalControls = null;
    private Label lblInfo;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void startListener() {
        
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            adminPage = "adminTransaction.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            getData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        lblInfo.setValue("");
        lblInfo.setVisible(false);
    }

    public void onClick$btnSearch() {
        try {
            clearFields();
            clearMessage();
            EJBRequest _request = new EJBRequest();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(QueryConstants.PARAM_BEGINNING_DATE, dtbBeginningDate.getValue());
            params.put(QueryConstants.PARAM_ENDING_DATE, dtbEndingDate.getValue());
            params.put(QueryConstants.PARAM_CATEGORY_ID, Category.WAIT);
            if (dtbEndingDate.getValue().getTime() >= dtbBeginningDate.getValue().getTime()) {
              
                if (cmbBraund.getSelectedItem() != null && cmbBraund.getSelectedIndex() != 0) {
                    params.put(QueryConstants.PARAM_BRAUND_ID, ((Provider) cmbBraund.getSelectedItem().getValue()).getId());
                }
                if (cmbModel.getSelectedItem() != null && cmbModel.getSelectedIndex() != 0) {
                  params.put(QueryConstants.PARAM_MODEL_ID, ((Product) cmbModel.getSelectedItem().getValue()).getId());
                }
                if (cmbEnterCalibration.getSelectedItem() != null && cmbEnterCalibration.getSelectedIndex() != 0) {
                    params.put(QueryConstants.PARAM_ENTER_CALIBRATION_ID, ((Customer) cmbEnterCalibration.getSelectedItem().getValue()).getId());
                }
               
                if (txtSerial.getText() != null && txtSerial.getText() !="") {
                    params.put(QueryConstants.PARAM_SERIAL, txtSerial.getText());
                }
                _request.setParams(params);
                _request.setParam(true);
                metrologicalControls = productEJB.searchMetrologicalControl(_request);
                loadList(metrologicalControls);
            } else  {
                this.showMessage("sp.error.date.invalid", true, null);
            }
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            loadList(null);
        }
    }

    private void loadBraunds(Braund braund) {
        try {
            cmbBraund.getItems().clear();
            braunds = utilsEJB.getBraunds();
            for (Braund e : braunds) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbBraund);
                if (braund != null && braund.getId().equals(e.getId())) {
                	cmbBraund.setSelectedItem(cmbItem);
                } else {
                	cmbBraund.setSelectedIndex(0);
                	braund = (Braund) cmbBraund.getSelectedItem().getValue();
                }
            }
            loadModel(braund);
        } catch (EmptyListException ex) {
            
        }catch (Exception ex) {
            showError(ex);
        }
    }

       
    private void loadModel(Braund braund) {


            try {
            	cmbModel.getItems().clear();
                models = utilsEJB.getModelsByBraund(braund.getId());
                for (int i = 0; i < models.size(); i++) {
                    Comboitem item = new Comboitem();
                    item.setValue(models.get(i));
                    item.setLabel(models.get(i).getName());
                    item.setParent(cmbModel);
                }
                cmbModel.setSelectedIndex(0);
            } catch (EmptyListException ex) {
               
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        
    }

    private void loadEnterCalibration(EnterCalibration enterCalibration) {
        try {
        	cmbEnterCalibration.getItems().clear();
        	enterCalibrations = utilsEJB.getEnterCalibrations();
            for (EnterCalibration e : enterCalibrations) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbEnterCalibration);
                if (enterCalibration != null && enterCalibration.getId().equals(e.getId())) {
                	cmbEnterCalibration.setSelectedItem(cmbItem);
                } else {
                	cmbEnterCalibration.setSelectedIndex(0);
                }
            }
        }catch (EmptyListException ex) {
            
        } catch (Exception ex) {
            showError(ex);
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
                    item.appendChild(new Listcell(control.getMetrologicalControl().getUbication()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getScale()));
                    item.appendChild(new Listcell(control.getMetrologicalControl().getControlType()));
                    //item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
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
                item.setParent(lbxReport);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        cmbBraund.setSelectedIndex(0);
        cmbModel.setSelectedIndex(0);
        cmbEnterCalibration.setSelectedIndex(0);
        Date date = new Date();
        dtbBeginningDate.setValue(date);
        dtbEndingDate.setValue(date);
        clearFields();
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport, Labels.getLabel("sp.report.title"));
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxReport,0);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        dtbBeginningDate.setFormat("yyyy/MM/dd");
        dtbBeginningDate.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbEndingDate.setFormat("yyyy/MM/dd");
        dtbEndingDate.setValue(new Timestamp(new java.util.Date().getTime()));
        loadBraunds(null);
        loadEnterCalibration(null);
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

}
