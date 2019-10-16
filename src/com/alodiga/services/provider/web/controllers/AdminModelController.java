package com.alodiga.services.provider.web.controllers;


import java.sql.Timestamp;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.Event;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Model;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminModelController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Combobox cmbBraund;
    private UtilsEJB utilsEJB = null;
    private Model modelParam;
    private List<Braund> braunds;
    private Button btnSave;
    private User user;
    private AuditoryEJB auditoryEJB;
    private String ipAddress;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        modelParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Model) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        user = AccessControl.loadCurrentUser();
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
          	ipAddress = Executions.getCurrent().getRemoteAddr();
        	auditoryEJB = (AuditoryEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUDITORY_EJB);
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
               Braund braund = (Braund) cmbBraund.getSelectedItem().getValue();
               model.setBraund(braund);
               model = utilsEJB.saveModel(model);   
               this.showMessage("sp.common.save.success", false, null);
               AccessControl.saveAction(Permission.ADD_MODEL, "Ingreso el modelo= " +model.getName());
               saveAudit(_model, model);
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

    public void saveAudit(Model rolOld ,Model rolNew){
        EJBRequest request1 = new EJBRequest();
        EJBRequest request2 = new EJBRequest();            
        String result = "";
         String oldValue ="";
        request1.setParam(rolOld);
        request2.setParam(rolNew);

        try {
            result = auditoryEJB.getNaturalFieldModel(request1, request2);
        } catch (Exception ex) {
           
        }

        if(!result.isEmpty()||!"".equals(result)){
            String descrip = rolOld.getName();
            
            String status = rolOld.getBraund().getName();
           
            oldValue = "Name:"+descrip+"|Braund:"+status;
            
			try {
				EJBRequest ejbRequest = new EJBRequest();
				ejbRequest.setParam(eventType);
				Event ev = auditoryEJB.loadEvent(ejbRequest);
				Audit audit = new Audit();
				EJBRequest auditRequest = new EJBRequest();
				audit.setUser(user);
				audit.setEvent(ev);
				Permission permission = PermissionManager.getInstance().getPermissionById(2L);
				audit.setPermission(permission);
				audit.setCreationDate(new Timestamp((new java.util.Date().getTime())));
				audit.setTableName("Model");
				audit.setRemoteIp(ipAddress);
				audit.setOriginalValues(oldValue);
				audit.setNewValues(result);
				audit.setResponsibleType("usuario");
				auditRequest.setParam(audit);
				audit = auditoryEJB.saveAudit(auditRequest);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
        }
    }
}
