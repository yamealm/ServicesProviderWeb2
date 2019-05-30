package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.models.Address;
import com.alodiga.services.provider.commons.models.City;
import com.alodiga.services.provider.commons.models.CivilState;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.County;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Gender;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.Encoder;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

public class AdminCustomerController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprises;
    private Combobox cmbCountries;
    private Combobox cmbStates;
    private Combobox cmbCounties;
    private Combobox cmbCities;
    private Combobox cmbGenders;
    private Label lblLogin;
    private Textbox txtPassword;
    private Textbox txtFirstName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Textbox txtCountryCode;
    private Textbox txtRegionCode;
    private Textbox txtPhoneNumber;
    private Datebox dtbBirthDate;
    private Combobox cmbCivilStates;
    private Textbox txtFacebookAccount;
    private Textbox txtTwitterAccount;
    private Textbox txtStateName;
    private Textbox txtCountyName;
    private Textbox txtCityName;
    private Textbox txtAddress;
    private Textbox txtZipCode;
    private Checkbox cbxEnabled;
    private Row rowPassword;
    private Customer customerParam;
    private Button btnSave;
    private CustomerEJB customerEJB;
    private UtilsEJB utilsEJB;
    private ProductEJB productEJB;
    private boolean editingPassword = false;
    Map params = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Customer) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.customer");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.customer");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {

            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadEnterprises(Enterprise enterprise) {
        List<Enterprise> enterprises = null;
        try {
            cmbEnterprises.getItems().clear();
            enterprises = utilsEJB.getEnterprises();
            for (Enterprise e : enterprises) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbEnterprises);
                if (enterprise != null && e.getId().equals(enterprise.getId())) {
                    cmbEnterprises.setSelectedItem(cmbItem);
                }
            }
            if (enterprise == null) {
                cmbEnterprises.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadCivilStates(String civilStates) {
        try {
            cmbCivilStates.getItems().clear();
            for (CivilState c : CivilState.values()) {
                Comboitem item = new Comboitem();
                item.setValue(c);
                item.setLabel(Labels.getLabel(c.getPropKey()));
                item.setParent(cmbCivilStates);
                cmbCivilStates.setSelectedIndex(0);
                try {
                    if (customerParam.getCivilState() != null && c.equals(CivilState.valueOf(customerParam.getCivilState().toUpperCase()))) {
                        cmbCivilStates.setSelectedItem(item);
                    }
                } catch (IllegalArgumentException ex) {
                    //
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

     private void loadGenders(String gender) {
        try {
            cmbGenders.getItems().clear();
            for (Gender g : Gender.values()) {
                Comboitem item = new Comboitem();
                item.setValue(g);
                item.setLabel(Labels.getLabel(g.getPropKey()));
                item.setParent(cmbGenders);
                cmbGenders.setSelectedIndex(0);
                try {
                    if (gender.equals(Gender.valueOf(customerParam.getGender().toUpperCase()))) {
                        cmbGenders.setSelectedItem(item);
                    }
                } catch (IllegalArgumentException ex) {
                    //
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        
    }

    private void loadFields(Customer customer) {
        if (customer != null) {//UPDATE
            cmbEnterprises.setDisabled(true);
            rowPassword.setVisible(true);
            txtPassword.setReadonly(true);
            txtPassword.setText("*****");
            cbxEnabled.setChecked(customer.getEnabled());
            loadEnterprises(customer.getEnterprise());
            loadGenders(customer.getGender());
            loadCivilStates(customer.getCivilState());
            txtFirstName.setText(customer.getFirstName());
            txtLastName.setText(customer.getLastName());
            lblLogin.setValue(customer.getLogin());
            txtEmail.setText(customer.getEmail());
            dtbBirthDate.setValue(customer.getBirthDate());
            txtFacebookAccount.setText(customer.getFacebookAccount());
            txtTwitterAccount.setText(customer.getTwitterAccount());
            txtAddress.setText(customer.getAddress().getAddress());
            txtZipCode.setText(customer.getAddress().getZipCode());
            loadCountries(customer.getAddress().getCountry());
            loadPhoneNumber(customer.getPhoneNumber());
        } 
    }

    public void blockFields() {
        cmbEnterprises.setDisabled(true);
        rowPassword.setVisible(false);
        cbxEnabled.setDisabled(true);
        txtFirstName.setReadonly(true);
        txtLastName.setReadonly(true);
        txtEmail.setReadonly(true);
        txtFacebookAccount.setReadonly(true);
        txtTwitterAccount.setReadonly(true);
        dtbBirthDate.setReadonly(true);
        txtAddress.setReadonly(true);
        txtZipCode.setReadonly(true);
        cmbCountries.setDisabled(true);
        cmbStates.setDisabled(true);
        cmbCounties.setDisabled(true);
        cmbCities.setDisabled(true);
        txtStateName.setReadonly(true);
        txtCityName.setReadonly(true);
        txtCountyName.setReadonly(true);
        txtCountryCode.setReadonly(true);
        txtRegionCode.setReadonly(true);
        txtPhoneNumber.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (cmbEnterprises.getSelectedItem() == null) {
            cmbEnterprises.setFocus(true);
            this.showMessage("sp.error.enteprise.notSelected", true, null);
        } else if (txtFirstName.getText().isEmpty()) {
            txtFirstName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtLastName.getText().isEmpty()) {
            txtLastName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (editingPassword && txtPassword.getText().isEmpty()) {
            txtPassword.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (editingPassword && !GeneralUtils.isNumeric(txtPassword.getText())) {
            txtPassword.setFocus(true);
            this.showMessage("sp.error.invalid.valueNumber", true, null);
        } else if (dtbBirthDate.getText().isEmpty()) {
            dtbBirthDate.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbCivilStates.getSelectedItem() == null) {
            cmbCivilStates.setFocus(true);
            this.showMessage("sp.error.tinType.notSelected.single", true, null);
        }  else if (txtEmail.getText().isEmpty()) {
            txtEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtCountryCode.getText().isEmpty()) {
            txtCountryCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtRegionCode.getText().isEmpty()) {
            txtRegionCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPhoneNumber.getText().isEmpty()) {
            txtPhoneNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }  else if (txtCountryCode.isReadonly() && !Utils.validatePhoneNumberUSA(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText())) {
            this.showMessage("sp.error.invalidPhoneNumber", true, null);
        }  else if (!cmbStates.isVisible() && txtStateName.getText().isEmpty()) {
            txtStateName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (!cmbCities.isVisible() && txtCityName.getText().isEmpty()) {
            txtCityName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAddress.getText().isEmpty()) {
            txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtZipCode.getText().isEmpty()) {
            txtZipCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    private void save(Customer _customer) {
        try {
            String password = "";
            Customer customer = new Customer();
            if (_customer != null) {
               customer.setId(customerParam.getId());
               customer.setLogin(_customer.getLogin());
            }
            customer.setEnterprise((Enterprise) cmbEnterprises.getSelectedItem().getValue());
            customer.setAddress(getAddress());
            customer.setPassword(Encoder.MD5(txtPassword.getText()));
            customer.setFirstName(txtFirstName.getText());
            customer.setLastName(txtLastName.getText());
            customer.setEmail(txtEmail.getText());
            customer.setPhoneNumber(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText());
            customer.setGender(((Gender) cmbGenders.getSelectedItem().getValue()).toString());
            customer.setCreationDate(new Timestamp(new Date().getTime()));
            customer.setBirthDate(new Timestamp(dtbBirthDate.getValue().getTime()));
            customer.setCivilState(((CivilState) cmbCivilStates.getSelectedItem().getValue()).toString());
            customer.setFacebookAccount(txtFacebookAccount.getText());
            customer.setTwitterAccount(txtTwitterAccount.getText());
            customer.setEnabled(cbxEnabled.isChecked());
            request.setParam(customer);
            customerParam = customerEJB.saveCustomer(request);
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

   
    private Address getAddress() {

        Address address = new Address();
        Country country = (Country) cmbCountries.getSelectedItem().getValue();
        address.setCountry(country);
        if (cmbStates.isVisible()) {// Country have states
            address.setState((State) cmbStates.getSelectedItem().getValue());
        } else {
            address.setStateName(txtStateName.getText());
        }
        if (cmbCounties.getSelectedIndex() != -1) {
            address.setCounty((County) cmbCounties.getSelectedItem().getValue());
        }
        if (cmbCities.isVisible()) {// State have cities
            address.setCity((City) cmbCities.getSelectedItem().getValue());
        } else {
            address.setCityName(txtCityName.getText());
        }
        address.setAddress(txtAddress.getText());
        address.setZipCode(txtZipCode.getText());
        return address;
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    save(null);
                    break;
                case WebConstants.EVENT_ADD_DESCENDANT:
                    save(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    save(customerParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(customerParam);

                break;
            case WebConstants.EVENT_VIEW:
                loadFields(customerParam);

                break;
            default:
                break;
        }
    }

    private void loadCountries(Country country) {
        try {
            List<Country> countries = new ArrayList<Country>();
            request.setParam(Country.USA);
            countries.add(utilsEJB.loadCountry(request));

            // countries = utilsEJB.getCountries(request);
            for (int i = 0; i < countries.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(countries.get(i));
                item.setLabel(countries.get(i).getName());
                item.setParent(cmbCountries);
                if (countries.get(i).getId().equals(country.getId())) {
                    cmbCountries.setSelectedItem(item);
                }
            }
            loadStates(country, customerParam.getAddress().getState());
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    private void loadStates(Country country, State state) {

        if (state == null) {
            cmbStates.setVisible(false);
            txtStateName.setVisible(true);
            txtStateName.setText(customerParam.getAddress().getStateName());
            cmbCities.setVisible(false);
            txtCityName.setVisible(true);
            txtCityName.setText(customerParam.getAddress().getCityName());
        } else {
            try {
                cmbCounties.getItems().clear();
                cmbStates.getItems().clear();
                params = new HashMap();
                params.put(QueryConstants.PARAM_COUNTRY_ID, country.getId());
                List<State> states = null;
                request.setParams(params);
                states = utilsEJB.getStateByCountry(request);
                for (int i = 0; i < states.size(); i++) {
                    Comboitem item = new Comboitem();
                    item.setValue(states.get(i));
                    item.setLabel(states.get(i).getName());
                    item.setParent(cmbStates);
                    if (states.get(i).getId().equals(state.getId())) {
                        cmbStates.setSelectedItem(item);
                    }
                }
                if (customerParam.getAddress().getCounty() != null) {
                    loadCounties(state, customerParam.getAddress().getCounty());
                } else if (customerParam.getAddress().getCity() != null) {
                    loadCities(state, null, customerParam.getAddress().getCity());
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }
    }

    private void loadCounties(State state, County county) {

        try {
            cmbCounties.getItems().clear();
            params = new HashMap();
            params.put(QueryConstants.PARAM_STATE_ID, state.getId());
            request.setParams(params);
            List<County> counties = utilsEJB.getCountiesByState(request);
            for (int i = 0; i < counties.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(counties.get(i));
                item.setLabel(counties.get(i).getName());
                item.setParent(cmbCounties);
                if (counties.get(i).getId().equals(county.getId())) {
                    cmbCounties.setSelectedItem(item);
                }
            }
            loadCities(state, county, customerParam.getAddress().getCity());
        } catch (EmptyListException ex) {
            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCities(State state, County county, City city) {
        try {
            cmbCities.getItems().clear();
            List<City> cities = null;
            params = new HashMap();
            request.setParams(params);
            if (county != null) {
                params.put(QueryConstants.PARAM_COUNTY_ID, county.getId());
                cities = utilsEJB.getCitiesByCounty(request);
            } else if (state != null) {
                params.put(QueryConstants.PARAM_STATE_ID, state.getId());
                cities = utilsEJB.getCitiesByState(request);
            }
            for (int i = 0; i < cities.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(cities.get(i));
                item.setLabel(cities.get(i).getName());
                item.setParent(cmbCities);
                if (cities.get(i).getId().equals(city.getId())) {
                    cmbCities.setSelectedItem(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onClick$btnEditPassword() {
        txtPassword.setReadonly(false);
        txtPassword.setRawValue(null);
        txtPassword.setFocus(true);
        editingPassword = true;
    }

    private void loadPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() >= 11) {
            txtCountryCode.setText(phoneNumber.substring(0, 1));
            txtRegionCode.setText(phoneNumber.substring(1, 4));
            txtPhoneNumber.setText(phoneNumber.substring(4, 11));
        }
    }

}
