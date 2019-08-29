package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.ControlType;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.EnterCalibration;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminAddMetrologicalControlController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbBraund;
    private Combobox cmbModel;
    private Combobox cmbEnterCalibration;
    private Textbox txtDesignation;
    private Textbox txtInstrument;
    private Textbox txtType;
    private Textbox txtSerilNumber;
    private Textbox txtRank;
    private Textbox txtUbication;
    private Textbox txtScale;
    private Textbox txtObservation;
    private Datebox dtxCalibration;
    private Datebox dtxExpiration;
    private Datebox dtxCreation;
 
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private MetrologicalControl metrologicalControlParam;
    private List<Braund> braunds;
    private List<Model> models;
    private List<EnterCalibration> enterCalibrations;

    private User user;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        metrologicalControlParam = (Sessions.getCurrent().getAttribute("object") != null) ? (MetrologicalControl) Sessions.getCurrent().getAttribute("object") : null;
        user = AccessControl.loadCurrentUser();
        initialize();
        initView(eventType, "sp.crud.product");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.product");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            dtxExpiration.setValue(new Timestamp(new Date().getTime()));
            dtxCalibration.setValue(new Timestamp(new Date().getTime()));
            dtxCreation.setValue(new Timestamp(new Date().getTime()));
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
    	txtSerilNumber.setRawValue(null);
    	txtScale.setRawValue(null);
    	txtRank.setRawValue(null);
    	txtUbication.setRawValue(null);
    	txtObservation.setRawValue(null);
    	txtType.setRawValue(null);
	 }

    public void blockFields() {


    }

    public Boolean validateEmpty() {
    	if (txtDesignation.getText().isEmpty()) {
    		txtDesignation.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } 
    	if (txtInstrument.getText().isEmpty()) {
    		txtInstrument.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } 
    	if (txtRank.getText().isEmpty()) {
    		txtRank.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtSerilNumber.getText().isEmpty()) {
        	txtSerilNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (cmbBraund.getText().isEmpty()) {
        	cmbBraund.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (cmbModel.getText().isEmpty()) {
        	cmbModel.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (cmbEnterCalibration.getText().isEmpty()) {
        	cmbEnterCalibration.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (txtUbication.getText().isEmpty()) {
        	txtUbication.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null); 
        }
        
        
        else {
            return true;
        }
        return false;
    }


    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                	saveMetreologicalControl(null);
                    break;
                case WebConstants.EVENT_EDIT:
                	saveMetreologicalControl(metrologicalControlParam);
                    break;
                default:
                    break;
            }
        }
    }

    

    public void onClick$btnBack() {
    	 Executions.sendRedirect("./listMetrologicalControl.zul");
    }
    
    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_DELETE:
                loadFields(metrologicalControlParam);
                loadBraunds(metrologicalControlParam.getBraund());
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(metrologicalControlParam);
                loadBraunds(metrologicalControlParam.getBraund());
//                loadControlType(metrologicalControlParam.getControlType());
                loadEnterCalibration(metrologicalControlParam.getEnterCalibration());
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
            	loadBraunds(null);
//                loadControlType(null);
                loadEnterCalibration(null);
                break;
            default:
                break;
        }
    }

    
    public void loadFields(MetrologicalControl metrologicalControlParam) {
    	txtDesignation.setText(metrologicalControlParam.getDesignation());
    	txtInstrument.setText(metrologicalControlParam.getInstrument());
    	txtSerilNumber.setText(metrologicalControlParam.getSerie());
    	txtRank.setText(metrologicalControlParam.getRango());
    	
    	txtSerilNumber.setText(metrologicalControlParam.getSerie());
    	dtxCreation.setValue(metrologicalControlParam.getCreationDate());
    	//buscar el ultimo
    	MetrologicalControlHistory history = null;
		try {
			history = transactionEJB.loadLastMetrologicalControlHistoryByMetrologicalControlId(metrologicalControlParam.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	dtxCalibration.setValue(history.getCalibrationDate());
    	dtxExpiration.setValue(history.getExpirationDate());
    	
    	txtUbication.setText(metrologicalControlParam.getUbication());
    	txtScale.setText(metrologicalControlParam.getScale());
    	txtObservation.setText(history.getObservation()); ///buscar el ultimo
    	txtType.setText(metrologicalControlParam.getControlType());
    	
		
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
    
    private void saveMetreologicalControl(MetrologicalControl _metrologicalControl) {
    	MetrologicalControl metrologicalControl = new MetrologicalControl();
        try {

            if (_metrologicalControl != null) 
            	metrologicalControl.setId(_metrologicalControl.getId());
            Braund braund = (Braund) cmbBraund.getSelectedItem().getValue();
            metrologicalControl.setBraund(braund);
            Model model = (Model) cmbModel.getSelectedItem().getValue();
            metrologicalControl.setModel(model);
            EnterCalibration enterCalibration = (EnterCalibration) cmbEnterCalibration.getSelectedItem().getValue();
            metrologicalControl.setEnterCalibration(enterCalibration);
            metrologicalControl.setDesignation(txtDesignation.getText());
            metrologicalControl.setInstrument(txtInstrument.getText());
            metrologicalControl.setControlType(txtType.getText());
            metrologicalControl.setSerie(txtSerilNumber.getText());
            metrologicalControl.setRango(txtRank.getText());
            metrologicalControl.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            metrologicalControl.setScale(txtScale.getText());
            metrologicalControl.setUbication(txtUbication.getText());
            metrologicalControl.setEnabled(true);

            MetrologicalControlHistory metrologicalControlHistory = new MetrologicalControlHistory();
            metrologicalControlHistory.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            metrologicalControlHistory.setCalibrationDate(new Timestamp(dtxCalibration.getValue().getTime()));
            metrologicalControlHistory.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
            metrologicalControl = transactionEJB.saveMetrologicalControl(metrologicalControl,metrologicalControlHistory);

            this.showMessage("sp.common.save.success", false, null);
        } catch (NullParameterException ex) {
        	showMessage("sp.error.field.number", true, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
  
}