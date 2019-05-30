package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.models.City;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.County;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import java.util.List;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;

public class AddressController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtStateName;
    private Textbox txtCityName;
    private Combobox cmbCountries;
    private Combobox cmbStates;
    private Combobox cmbCounties;
    private Combobox cmbCities;
    private UtilsEJB utilsEJB;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();

    }


    public void onChange$cmbCountries() {
        cmbStates.setVisible(true);
        txtStateName.setVisible(false);
        txtStateName.setConstraint("");
        cmbCities.setVisible(true);
        txtCityName.setVisible(false);
        txtCityName.setConstraint("");

        Country country = (Country) cmbCountries.getSelectedItem().getValue();
        loadStates(country.getId());

    }

    public void onChange$cmbStates() {
        State state = (State) cmbStates.getSelectedItem().getValue();
        if (state.getId().equals(State.FLORIDA)) {
            loadCountiesByState(state.getId());
        } else {
            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
            cmbCounties.getItems().clear();
            loadCities(null, state.getId());
        }
    }

    public void onChange$cmbCounties() {
        County county = (County) cmbCounties.getSelectedItem().getValue();
        loadCities(county.getId(), null);
    }

    private void loadCountries() {
        try {
            List<Country> countries = new ArrayList<Country>();
            request.setParam(Country.USA);
            //countries.add(utilsEJB.loadCountry(request));
            countries = utilsEJB.getCountries(request);
            for (int i = 0; i < countries.size(); i++) {

                if (countries.get(i).getId().equals(Country.USA)) {
                    Comboitem item = new Comboitem();
                    item.setValue(countries.get(i));
                    item.setLabel(countries.get(i).getName());
                    item.setParent(cmbCountries);
                    cmbCountries.setSelectedItem(item);
                }
            }
            loadStates(Country.USA);
            //} catch (EmptyListException ex) {
            //    Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadStates(Long countryId) {
        try {
            cmbCounties.getItems().clear();
            cmbStates.getItems().clear();
            Map params = new HashMap();
            params.put(QueryConstants.PARAM_COUNTRY_ID, countryId);
            List<State> states = null;
            request.setParams(params);
            states = utilsEJB.getStateByCountry(request);
            for (int i = 0; i < states.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(states.get(i));
                item.setLabel(states.get(i).getName());
                item.setParent(cmbStates);
            }
            cmbStates.setSelectedIndex(0);
            State state = (State) cmbStates.getSelectedItem().getValue();
            if (state.getId().equals(State.FLORIDA)) {
                loadCountiesByState(state.getId());
            } else {
                cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
                loadCities(null, state.getId());
            }

        } catch (EmptyListException ex) {
            cmbStates.setVisible(false);
            txtStateName.setVisible(true);
            txtStateName.setConstraint("no empty: " + Labels.getLabel("sp.error.field.required"));
            cmbCities.setVisible(false);
            txtCityName.setVisible(true);
            txtCityName.setConstraint("no empty: " + Labels.getLabel("sp.error.field.required"));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCountiesByState(Long stateId) {
        try {
            cmbCounties.getItems().clear();
            Map params = new HashMap();
            params.put(QueryConstants.PARAM_STATE_ID, stateId);
            request.setParams(params);
            List<County> counties = utilsEJB.getCountiesByState(request);
            for (int i = 0; i < counties.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(counties.get(i));
                item.setLabel(counties.get(i).getName());
                item.setParent(cmbCounties);
            }
            cmbCounties.setSelectedIndex(0);
            County county = (County) cmbCounties.getSelectedItem().getValue();
            loadCities(county.getId(), null);
        } catch (EmptyListException ex) {
            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
            State state = (State) cmbStates.getSelectedItem().getValue();
            loadCities(null, state.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCities(Long countyId, Long stateId) {
        try {
            cmbCities.getItems().clear();
            List<City> cities = null;
            Map params = new HashMap();
            request.setParams(params);
            if (countyId != null) {
                params.put(QueryConstants.PARAM_COUNTY_ID, countyId);
                cities = utilsEJB.getCitiesByCounty(request);
            } else if (stateId != null) {
                params.put(QueryConstants.PARAM_STATE_ID, stateId);
                cities = utilsEJB.getCitiesByState(request);
            }
            for (int i = 0; i < cities.size(); i++) {
                Comboitem item = new Comboitem();
                item.setValue(cities.get(i));
                item.setLabel(cities.get(i).getName());
                item.setParent(cmbCities);
            }
            cmbCities.setSelectedIndex(0);
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadCountries();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearFields() {
    }

    public void loadData() {
    }

    public void blockFields() {
    }
}
