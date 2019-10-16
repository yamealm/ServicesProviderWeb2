package com.alodiga.services.provider.web.controllers;


import java.sql.Timestamp;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.Braund;
import com.alodiga.services.provider.commons.models.Event;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.MetrologicalControl;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminMarkController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private UtilsEJB utilsEJB = null;
    private Braund braundParam;
    private Button btnSave;
    private User user;
    private AuditoryEJB auditoryEJB;
    private String ipAddress;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        braundParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Braund) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        user = AccessControl.loadCurrentUser();
        initView(eventType, "sp.crud.braund");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.braund");
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

    private void loadFields(Braund braund) {
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

  

    private void saveCountry(Braund _braund) {
        try {
        	Braund braund = new Braund();;
            if (_braund != null) {
            	braund.setId(_braund.getId());
            }
            braund.setName(txtName.getText());            
            braund = utilsEJB.saveBraund(braund);   
            this.showMessage("sp.common.save.success", false, null);
            AccessControl.saveAction(Permission.ADD_MARK, "Ingreso la marca= " +braund.getName());
//            saveAudit(_braund, braund);
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
                    saveCountry(braundParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(braundParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(braundParam);
                break;
            case WebConstants.EVENT_ADD:
                break;
            default:
                break;
        }
    }
    

    public void onClick$btnBack() {
    	Executions.sendRedirect("./listMarkes.zul");
    }
    
    public void saveAudit(Braund controlOld ,Braund controlNew){
        EJBRequest request1 = new EJBRequest();
        EJBRequest request2 = new EJBRequest();            
        String result = "";
         String oldValue ="";
        request1.setParam(controlOld);
        request2.setParam(controlNew);

        try {
            result = auditoryEJB.getNaturalFieldBraund(request1, request2);
        } catch (Exception ex) {
           
        }

        if(!result.isEmpty()||!"".equals(result)){
            String descrip = controlOld.getName();
            
            oldValue = "Name:"+descrip;
            
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
				audit.setTableName("Braund");
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
