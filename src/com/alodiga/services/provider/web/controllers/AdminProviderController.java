package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.Event;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminProviderController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtAddress;
    private Checkbox cbxEnabled;
    private ProductEJB productEJB = null;
    private Provider providerParam;
    private Button btnSave;
    private User user;
    private AuditoryEJB auditoryEJB;
    private String ipAddress;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        providerParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Provider) Sessions.getCurrent().getAttribute("object") : null;
        user = AccessControl.loadCurrentUser();
        initialize();
        initView(eventType, "sp.crud.provider");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.provider");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
        	ipAddress = Executions.getCurrent().getRemoteAddr();
        	auditoryEJB = (AuditoryEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUDITORY_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
        txtAddress.setRawValue(null);
        cbxEnabled.setChecked(true);
    }

    private void loadFields(Provider provider) {
        txtName.setText(provider.getName());
        txtAddress.setText(provider.getAddress());
        cbxEnabled.setChecked(provider.getEnabled());
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtAddress.setReadonly(true);
        cbxEnabled.setDisabled(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAddress.getText().isEmpty()) {
        	txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    private void saveProvider(Provider _provider) {
        Provider provider = new Provider();
        try {
            if(_provider != null && _provider.getId() != null){
            provider.setId(_provider.getId());
            }
            provider.setName(txtName.getText());
            provider.setAddress(txtAddress.getText());
            provider.setEnabled(cbxEnabled.isChecked());
            request.setParam(provider);
            providerParam = productEJB.saveProvider(request);
            providerParam = provider;
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
            saveAudit(_provider, provider);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveProvider(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProvider(providerParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(providerParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(providerParam);
                break;
            case WebConstants.EVENT_ADD:
                break;
            default:
                break;
        }
    }
    
    public void saveAudit(Provider fCustomerOld ,Provider fCustomerNew){
        EJBRequest request1 = new EJBRequest();
        EJBRequest request2 = new EJBRequest();            
        String result = "";
         String oldValue ="";
        request1.setParam(fCustomerOld);
        request2.setParam(fCustomerNew);

        try {
            result = auditoryEJB.getNaturalFieldProvider(request1, request2);
        } catch (Exception ex) {
            
        }

        if(!result.isEmpty()||!"".equals(result)){
            String name = fCustomerOld.getName();
            String address = fCustomerOld.getAddress();
            
            oldValue = "Name:"+name+"|Address:"+address;
            
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
				audit.setTableName("Provider");
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
