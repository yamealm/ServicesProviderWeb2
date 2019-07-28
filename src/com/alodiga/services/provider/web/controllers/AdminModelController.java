package com.alodiga.services.provider.web.controllers;


import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminModelController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Combobox cmbBraund;
    private UtilsEJB utilsEJB = null;
    private Model modelParam;
    private List<Braund> braunds;
    private Button btnSave;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        modelParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Model) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.model");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.model");
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

    private void loadFields(Model model) {
        try {
            txtName.setText(model.getName());
            loadBraund(model.getBraund());
         } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadBraund(Braund braund) {
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
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void blockFields() {
        txtName.setReadonly(true);
        cmbBraund.setReadonly(true);
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

  

    private void saveModel(Model _model) {
    	   try {
           	Model model = new Model();;
               if (_model != null) {
               	model.setId(_model.getId());
               }
               model.setName(txtName.getText()); 
               Braund braund = new Braund();
               braund.setId(((Braund) cmbBraund.getSelectedItem().getValue()).getId());
               model.setBraund(braund);
               model = utilsEJB.saveModel(model);   
               this.showMessage("sp.common.save.success", false, null);
           } catch (Exception ex) {
              showError(ex);
           }

    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveModel(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveModel(modelParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(modelParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(modelParam);
                break;
            case WebConstants.EVENT_ADD:
            	loadBraund(null);
                break;
            default:
                break;
        }
    }


    public void onClick$btnBack() {
    	Executions.sendRedirect("./listModels.zul");
    }

}
