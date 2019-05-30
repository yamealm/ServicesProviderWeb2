package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Account;
import com.alodiga.services.provider.commons.models.Address;
import com.alodiga.services.provider.commons.models.City;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.County;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.utils.Constants;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.utils.Utils;
import org.zkoss.zul.Window;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.Executions;
import java.util.List;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;

public class EditAccountViewController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private Window winEditAccountView;
    private Textbox txtName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Textbox txtCountryCode;
    private Textbox txtRegionCode;
    private Textbox txtPhoneNumber;
    private Textbox txtStateName;
    private Textbox txtCityName;
    private Textbox txtAddress;
    private Textbox txtZipCode;
    private Label lblEnterpriseValue;
    private Label lblIdentifierValue;
    private Label lblLoginValue;
    private Combobox cmbCountries;
    private Combobox cmbStates;
    private Combobox cmbCounties;
    private Combobox cmbCities;
    private Combobox cmbLanguages;
    private Account currentAccount;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        initView(eventType, "sp.crud.account");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.profile");
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            currentAccount = AccessControl.loadCurrentAccount();
            txtCountryCode.setText(String.valueOf(Constants.USA_CODE));
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);

            loadData();

        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    @Override
    public void loadData() {
        lblEnterpriseValue.setValue(currentAccount.getEnterprise().getName());
        lblIdentifierValue.setValue(currentAccount.getTinType().getPrefix() + "-" + currentAccount.getIdentifier());
        lblLoginValue.setValue(currentAccount.getLogin());
        txtName.setValue(currentAccount.getFullName());
        txtEmail.setValue(currentAccount.getEmail());
        loadPhoneNumber(currentAccount.getPhoneNumber());
        loadAddress(currentAccount.getAddress());
        txtAddress.setValue(currentAccount.getAddress().getAddress());
        txtZipCode.setValue(currentAccount.getAddress().getZipCode());
        loadLanguages();
    }

    public void loadAddress(Address address) {
        long countryId = -1;
        long stateId = -1;
        long countyId = -1;
        long cityId = -1;
        try {
            //		COUNTRIES
            countryId = address.getCountry().getId();
            loadCountries(countryId);
            //		STATES
            if (address.getState() == null) {
                txtStateName.setValue(address.getStateName());
                txtStateName.setVisible(true);
                cmbStates.setVisible(false);
            } else {
                stateId = address.getState().getId();
                loadStates(countryId, stateId);
                txtStateName.setVisible(false);
                cmbStates.setVisible(true);
            }
            //		COUNTIES
            if (address.getCounty() != null) {
                address.getCounty().getId();
                loadCounties(stateId, countyId);
            }
            //		CITIES
            if (address.getCity() == null) {
                txtCityName.setValue(address.getCityName());
                txtCityName.setVisible(true);
                cmbCities.setVisible(false);
            } else {
                cityId = address.getCity().getId();
                loadCities(stateId, countyId, cityId);
                txtStateName.setVisible(false);
                cmbStates.setVisible(true);
            }
            //		ADDRESS AND ZIPCODE
            txtAddress.setValue(address.getAddress());
            txtZipCode.setValue(address.getZipCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCountries(long countryId) throws EmptyListException, GeneralException, NullParameterException {
        try {
            cmbCountries.getItems().clear();
            EJBRequest countryRequest = new EJBRequest();
            List<Country> countries = new ArrayList<Country>();
            countryRequest.setParam(Country.USA);
            countries.add(utilsEJB.loadCountry(countryRequest));
            //countries = utilsEJB.getCountries(countryRequest);
            for (Country country : countries) {
                Comboitem item = new Comboitem();
                item.setValue(country);
                item.setLabel(country.getName());
                item.setParent(cmbCountries);
                if (country.getId().longValue() == countryId) {
                    cmbCountries.setSelectedItem(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadStates(long countryId, long stateId) throws GeneralException, NullParameterException {
        cmbStates.getItems().clear();
        txtStateName.setValue("");
        EJBRequest stateRequest = new EJBRequest();
        Map<String, Object> stateParams = new HashMap<String, Object>();
        stateParams.put(QueryConstants.PARAM_COUNTRY_ID, new Long(countryId));
        stateRequest.setParams(stateParams);
        List<State> states = new ArrayList<State>();
        try {
            states = utilsEJB.getStateByCountry(stateRequest);
        } catch (EmptyListException e) {
            txtStateName.setVisible(true);
            cmbStates.setVisible(false);
        }
        for (State state : states) {
            Comboitem item = new Comboitem();
            item.setValue(state);
            item.setLabel(state.getName());
            item.setParent(cmbStates);
            if (state.getId().longValue() == stateId) {
                cmbStates.setSelectedItem(item);
            }
        }

    }

    private void loadCounties(long stateId, long countyId) throws GeneralException, NullParameterException {
        cmbCounties.getItems().clear();
        EJBRequest countyRequest = new EJBRequest();
        Map<String, Object> countyParams = new HashMap<String, Object>();
        countyParams.put(QueryConstants.PARAM_STATE_ID, new Long(stateId));
        countyRequest.setParams(countyParams);
        List<County> counties = new ArrayList<County>();
        try {
            counties = utilsEJB.getCountiesByState(countyRequest);
        } catch (EmptyListException e) {
            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
        }
        for (County county : counties) {
            Comboitem item = new Comboitem();
            item.setValue(county);
            item.setLabel(county.getName());
            item.setParent(cmbCounties);
            if (county.getId().longValue() == countyId) {
                cmbCounties.setSelectedItem(item);
            }
        }

    }

    private void loadCities(long stateId, long countyId, long cityId) throws GeneralException, NullParameterException {
        cmbCities.getItems().clear();
        txtCityName.setValue("");
        EJBRequest cityRequest = new EJBRequest();
        Map<String, Object> cityParams = new HashMap<String, Object>();
        cityRequest.setParams(cityParams);
        List<City> cities = new ArrayList<City>();
        try {
            if (countyId > -1) {//By county
                cityParams.put(QueryConstants.PARAM_COUNTY_ID, new Long(countyId));
                cities = utilsEJB.getCitiesByCounty(cityRequest);
            } else {//By state
                cityParams.put(QueryConstants.PARAM_STATE_ID, new Long(stateId));
                cities = utilsEJB.getCitiesByState(cityRequest);
            }
        } catch (EmptyListException e) {
            try {//By state
                cityParams.put(QueryConstants.PARAM_STATE_ID, new Long(stateId));
                cities = utilsEJB.getCitiesByState(cityRequest);
            } catch (EmptyListException e2) {
                txtStateName.setVisible(true);
                cmbStates.setVisible(false);
            }
        }
        for (City city : cities) {
            Comboitem item = new Comboitem();
            item.setValue(city);
            item.setLabel(city.getName());
            item.setParent(cmbCities);
            if (city.getId().longValue() == cityId) {
                cmbCities.setSelectedItem(item);
            }
        }

    }

    private void loadLanguages() {
        try {
            List<Language> languages = new ArrayList<Language>();

            languages = utilsEJB.getLanguages();
            cmbLanguages.getItems().clear();
            for (Language language : languages) {
                Comboitem item = new Comboitem();
                item.setValue(language);
                item.setLabel(Utils.getLanguageName(language.getId()));
                item.setParent(cmbLanguages);
                if (currentAccount.getLanguage().getId().equals(language.getId())) {
                    cmbLanguages.setSelectedItem(item);
                }
            }
        } catch (EmptyListException ex) {
            this.showMessage("sp.error.empty.list", true, ex);
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    private Address getAddress() {
        Address address = currentAccount.getAddress();
        Country country = (Country) cmbCountries.getSelectedItem().getValue();
        address.setCountry(country);
        if (cmbStates.isVisible()) {//Country have states
            address.setState((State) cmbStates.getSelectedItem().getValue());
            address.setStateName("");
        } else {
            address.setStateName(txtStateName.getText());
            address.setState(null);
        }
        if (cmbCounties.getSelectedIndex() != -1) {
            address.setCounty((County) cmbCounties.getSelectedItem().getValue());
        } else {
            address.setCounty(null);
        }
        if (cmbCities.isVisible()) {//State have cities
            address.setCity((City) cmbCities.getSelectedItem().getValue());
            address.setCityName("");
        } else {
            address.setCityName(txtCityName.getText());
            address.setCity(null);
        }
        address.setAddress(txtAddress.getText());
        address.setZipCode(txtZipCode.getText());
        return address;
    }

    public boolean validateEmpty() {
        boolean valid = false;
        if (currentAccount.getPassword().isEmpty()) {
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
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
        } else if (cmbLanguages.getSelectedItem() == null) {
            cmbLanguages.setFocus(true);
            this.showMessage("sp.error.language.notSelected", true, null);
        } else if (!Utils.validatePhoneNumberUSA(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText())) {
            this.showMessage("sp.error.invalidPhoneNumber", true, null);
        } else if (!cmbStates.isVisible() && txtStateName.getText().isEmpty()) {
            txtStateName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (!cmbCities.isVisible() && txtCityName.getText().isEmpty()) {
            txtCityName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (!validateEmptyAddress()) {
        } else {
            valid = true;
        }
        return valid;
    }

    private boolean validateEmptyAddress() {
        boolean valid = false;
        if (txtAddress.getText().isEmpty()) {
            txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtZipCode.getText().isEmpty()) {
            txtZipCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtStateName.getText().isEmpty() && cmbStates.getSelectedIndex() < 0) {
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtCityName.getText().isEmpty() && cmbCities.getSelectedIndex() < 0) {
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            valid = true;
        }
        return valid;
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            try {
                currentAccount.setFullName(txtName.getText());
                currentAccount.setEmail(txtEmail.getText());
                currentAccount.setPhoneNumber(txtCountryCode.getText() + txtRegionCode.getText() + txtPhoneNumber.getText());
                currentAccount.setAddress(getAddress());
                Language language = (Language) cmbLanguages.getSelectedItem().getValue();
                currentAccount.setLanguage(language);
                request.setAuditData(AccessControl.getCurrentAudit());
                request.setParam(currentAccount);
                currentAccount = userEJB.saveAccount(request);
                this.showMessage("sp.common.save.success", false, null);
            } catch (Exception ex) {
                this.showMessage("sp.error.general", true, ex);
            }
        }
    }

    public void onClick$btnEditPassword() {
        try {
            String view = "/editAccountPasswordView.zul";
            Map<String, Object> paramsPass = new HashMap<String, Object>();
            paramsPass.put("object", currentAccount);
            final Window window = (Window) Executions.createComponents(view, null, paramsPass);
            window.doModal();
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    private void loadPhoneNumber(String number) {
        try {
            txtCountryCode.setText(number.substring(0, 1));
            txtRegionCode.setText(number.substring(1, 4));
            txtPhoneNumber.setText(number.substring(4, 11));
        } catch (Exception ex) {
            this.showMessage("sp.error.general", true, ex);
        }
    }

    @Override
    public void blockFields() {
        // TODO Auto-generated method stub
    }

    public void clearFields() {
    }
}
