package com.alodiga.services.provider.web.controllers;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.EnterCalibration;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminCalibrationEntityController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private UtilsEJB utilsEJB = null;
    private EnterCalibration calibrationParam;
    private Button btnSave;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        calibrationParam = (Sessions.getCurrent().getAttribute("object") != null) ? (EnterCalibration) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.enter");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.enter");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
      
    }

    private void loadFields(EnterCalibration braund) {
        try {
            txtName.setText(braund.getName());
           
         } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        txtName.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

  

    private void saveCountry(EnterCalibration _calibration) {
        try {
        	EnterCalibration calibration = new EnterCalibration();;
            if (_calibration != null) {
            	calibration.setId(_calibration.getId());
            }
            calibration.setName(txtName.getText());            
            calibration = utilsEJB.saveEnterCalibration(calibration);   
            this.showMessage("sp.common.save.success", false, null);
            AccessControl.saveAction(Permission.ADD_CALIBRATION, "Ingreso el ente de calibracion= " + calibration.getName());
        } catch (Exception ex) {
           showError(ex);
        }

    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveCountry(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveCountry(calibrationParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(calibrationParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(calibrationParam);
                break;
            case WebConstants.EVENT_ADD:
                break;
            default:
                break;
        }
    }
    

    public void onClick$btnBack() {
    	Executions.sendRedirect("./listCalibrationEntities.zul");
    }
    
    
}
