package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
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
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.EnterCalibration;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class ViewMetrologicalControlController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbBraund;
    private Combobox cmbModel;
    private Combobox cmbEnterCalibration;
    private Combobox cmbCategory;
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
    private List<Braund> braunds;
    private List<Model> models;
    private List<EnterCalibration> enterCalibrations;
    private List<Category> categories;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private MetrologicalControl controlParam;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        controlParam = (Sessions.getCurrent().getAttribute("object") != null) ? (MetrologicalControl) Sessions.getCurrent().getAttribute("object") : null;
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
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {

    }

    public void blockFields() {

    }

    public Boolean validateEmpty() {
    	if (txtDesignation.getText().isEmpty()) {
    		txtDesignation.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtInstrument.getText().isEmpty()) {
    		txtInstrument.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtRank.getText().isEmpty()) {
    		txtRank.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtSerilNumber.getText().isEmpty()) {
        	txtSerilNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbBraund.getText().isEmpty()) {
        	cmbBraund.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbModel.getText().isEmpty()) {
        	cmbModel.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbEnterCalibration.getText().isEmpty()) {
        	cmbEnterCalibration.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (txtUbication.getText().isEmpty()) {
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
                case WebConstants.EVENT_EDIT:
                	if (validateEmpty())
                		saveMetreologicalControl(controlParam);
                    break;
                default:
                    break;
            }
    	  }
    }

    

    public void onClick$btnBack() {
    	Sessions.getCurrent().setAttribute("object",controlParam);
    	Executions.sendRedirect("./listMetrologicalControl.zul");
    }
    
    public void onClick$viewDetail() {
    	Sessions.getCurrent().setAttribute("object",controlParam);
    	Executions.sendRedirect("./listMetrologicalControl.zul");
    }
    
    public void loadData() {
    	Category category = new Category();
    	category.setId(Category.METEOROLOGICAL_CONTROL);
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(controlParam);
                loadBraunds(controlParam.getBraund());
                loadEnterCalibration(controlParam.getEnterCalibration());
                blockFields();
                blockFields();
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
			loadCategory(history.getCategory());
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
    
    private void loadCategory(Category category) {
        try {
    	cmbCategory.getItems().clear();
    	categories = transactionEJB.getCategories();
		for (Category e : categories) {
			if (e.getId().equals(Category.QUARANTINE) || e.getId().equals(Category.METEOROLOGICAL_CONTROL)) {
				Comboitem cmbItem = new Comboitem();
				cmbItem.setLabel(e.getName());
				cmbItem.setValue(e);
				cmbItem.setParent(cmbCategory);
				if (category != null && category.getId().equals(e.getId())) {
					cmbCategory.setSelectedItem(cmbItem);
				}
			}
		}
    } catch (Exception ex) {
        showError(ex);
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

    private void saveMetreologicalControl(MetrologicalControl _metrologicalControl) {
    	MetrologicalControl metrologicalControl = new MetrologicalControl();
        try {

            if (_metrologicalControl != null) 
            	metrologicalControl.setId(_metrologicalControl.getId());
            Braund braund = (Braund) cmbBraund.getSelectedItem().getValue();
            metrologicalControl.setBraund(braund);
            Model model = (Model) cmbModel.getSelectedItem().getValue();
            metrologicalControl.setModel(model);
            Category category =(Category) cmbCategory.getSelectedItem().getValue();
            EnterCalibration enterCalibration = (EnterCalibration) cmbEnterCalibration.getSelectedItem().getValue();
            metrologicalControl.setEnterCalibration(enterCalibration);
            metrologicalControl.setDesignation(txtDesignation.getText());
            metrologicalControl.setInstrument(txtInstrument.getText());
            metrologicalControl.setControlType(txtType.getText());
            metrologicalControl.setSerie(txtSerilNumber.getText());
            metrologicalControl.setRango(txtRank.getText());
            metrologicalControl.setCreationDate(new Timestamp(dtxCreation.getValue().getTime()));
            metrologicalControl.setScale(txtScale.getText());
            metrologicalControl.setUbication(txtUbication.getText());
            if(category.getId().equals(Category.METEOROLOGICAL_CONTROL))
            	metrologicalControl.setEnabled(true);
            else
            	metrologicalControl.setEnabled(false);

            MetrologicalControlHistory metrologicalControlHistory = new MetrologicalControlHistory();
            metrologicalControlHistory.setCategory(category);
            metrologicalControlHistory.setCreationDate(new Timestamp(dtxCreation.getValue().getTime()));
            metrologicalControlHistory.setCalibrationDate(new Timestamp(dtxCalibration.getValue().getTime()));
            metrologicalControlHistory.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
            metrologicalControl = transactionEJB.saveMetrologicalControl(metrologicalControl,metrologicalControlHistory);
            AccessControl.saveAction(Permission.EDIT_METEOROLOGICAL_CONTROL, "Se edito producto en control metrologico ID Instrumento: " + metrologicalControl.getInstrument()!=null?metrologicalControl.getInstrument():"");
            this.showMessage("sp.common.save.success", false, null);
        } catch (NullParameterException ex) {
        	showMessage("sp.error.field.number", true, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   
    
} 
