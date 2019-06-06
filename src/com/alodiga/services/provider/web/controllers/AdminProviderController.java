package com.alodiga.services.provider.web.controllers;

//import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;

public class AdminProviderController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtURL;
    private Checkbox cbxEnabled;
    private Checkbox cbxIsSMSProvider;
//    private ProductEJB productEJB = null;
    private Provider providerParam;
    private Button btnSave;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        providerParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Provider) Sessions.getCurrent().getAttribute("object") : null;
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
//        try {
//            
//            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
//        } catch (Exception ex) {
//            showError(ex);
//        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
        txtURL.setRawValue(null);
        cbxIsSMSProvider.setChecked(false);
        cbxEnabled.setChecked(true);
    }

    private void loadFields(Provider provider) {
        txtName.setText(provider.getName());
        txtURL.setText(provider.getUrl());
        cbxIsSMSProvider.setChecked(provider.isIsSMSProvider());
        cbxEnabled.setChecked(provider.getEnabled());
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtURL.setReadonly(true);
        cbxIsSMSProvider.setDisabled(true);
        cbxEnabled.setDisabled(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtURL.getText().isEmpty()) {
            txtURL.setFocus(true);
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
            provider.setUrl(txtURL.getText());
            provider.setIsSMSProvider(cbxIsSMSProvider.isChecked());
            provider.setEnabled(cbxEnabled.isChecked());
            request.setParam(provider);
//            providerParam = productEJB.saveProvider(request);
            providerParam = provider;
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
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
}
