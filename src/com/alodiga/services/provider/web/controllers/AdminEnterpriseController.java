package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.Currency;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminEnterpriseController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtURL;
    private Textbox txtAddress;
    private Textbox txtInvoiceAddress;
    private Textbox txtEmail;
    private Textbox txtInfoEmail;
    private Textbox txtATCNumber;
    private Checkbox cbxEnabled;
    private Combobox cmbCurrencies;
    private Combobox cmbCountries;
    private UtilsEJB utilsEJB = null;
    private Enterprise enterpriseParam;
    private Button btnSave;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        enterpriseParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Enterprise) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.enterprise");
        
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.enterprise");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
        txtURL.setRawValue(null);
        txtAddress.setRawValue(null);
        txtInvoiceAddress.setRawValue(null);
        txtEmail.setRawValue(null);
        txtInfoEmail.setRawValue(null);
        txtATCNumber.setRawValue(null);
    
        cbxEnabled.setChecked(true);
    }

    private void loadFields(Enterprise enterprise) {
        txtName.setText(enterprise.getName());
        txtURL.setText(enterprise.getUrl());
        txtAddress.setText(enterprise.getAddress());
        txtInvoiceAddress.setText(enterprise.getInvoiceAddress());
        txtEmail.setText(enterprise.getEmail());
        txtInfoEmail.setText(enterprise.getInfoEmail());
        txtATCNumber.setText(enterprise.getAtcNumber());
        cbxEnabled.setChecked(enterprise.getEnabled());
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtURL.setReadonly(true);
        txtAddress.setReadonly(true);
        txtInvoiceAddress.setReadonly(true);
        txtEmail.setReadonly(true);
        txtInfoEmail.setReadonly(true);
        txtATCNumber.setReadonly(true);
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
        } else if (txtAddress.getText().isEmpty()) {
            txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtInvoiceAddress.getText().isEmpty()) {
            txtInvoiceAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtInfoEmail.getText().isEmpty()) {
            txtInfoEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtATCNumber.getText().isEmpty()) {
            txtATCNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }  else {
            return true;
        }
        return false;

    }

    private void saveEnterprise(Enterprise _enterprise) {
        Enterprise enterprise = new Enterprise();
        try {
            enterprise.setName(txtName.getText());
            enterprise.setAddress(txtAddress.getText());
            enterprise.setAtcNumber(txtATCNumber.getText());
            enterprise.setCountry((Country)cmbCountries.getSelectedItem().getValue());
            enterprise.setCurrency((Currency)cmbCurrencies.getSelectedItem().getValue());
            enterprise.setEmail(txtEmail.getText());
            enterprise.setInfoEmail(txtInfoEmail.getText());
            enterprise.setInvoiceAddress(txtInvoiceAddress.getText());
            enterprise.setUrl(txtURL.getText());
            enterprise.setEnabled(cbxEnabled.isChecked());
           
            if (_enterprise != null) {
                enterprise.setId(_enterprise.getId());
            }
            request.setParam(enterprise);
            enterprise = utilsEJB.saveEnterprise(request);
            enterpriseParam = enterprise;
            this.showMessage("sp.common.save.success", false, null);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveEnterprise(enterpriseParam);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveEnterprise(enterpriseParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(enterpriseParam);
                loadCurrencies(false);
                loadCountries(false);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(enterpriseParam);
                loadCurrencies(false);
                loadCountries(false);
                break;
            case WebConstants.EVENT_ADD:
                loadCurrencies(true);
                loadCountries(true);
                break;
            default:
                break;
        }
    }

    private void loadCurrencies(boolean isAdd) {
        List<Currency> currencies = new ArrayList<Currency>();
        try {
            currencies = utilsEJB.getCurrencies();
            for (Currency c : currencies) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(c.getName());
                cmbItem.setDescription(c.getSymbol());
                cmbItem.setValue(c);
                cmbItem.setParent(cmbCurrencies);
                if (!isAdd) {
                    if (c.getId().equals(enterpriseParam.getCurrency().getId())) {
                        cmbCurrencies.setSelectedItem(cmbItem);
                    }
                }
            }

        } catch (Exception ex) {
            showError(ex);
        }

    }

    private void loadCountries(boolean isAdd) {
        List<Country> countries = new ArrayList<Country>();
        try {
            request.setFirst(0);
            request.setFirst(null);
            countries = utilsEJB.getCountries(request);
            for (Country c : countries) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(c.getName());
                cmbItem.setValue(c);
                cmbItem.setParent(cmbCountries);
                if (!isAdd) {
                    if (c.getId().equals(enterpriseParam.getCountry().getId())) {
                        cmbCountries.setSelectedItem(cmbItem);
                    }
                }
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

   
   

}
