package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.CountryTranslation;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListCountryController extends GenericAbstractListController<Country> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private UtilsEJB utilsEJB = null;
    private List<Country> countries = null;
    private User currentUser;
    private Profile currentProfile;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        
    }

    public void startListener() {
    }

    @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_COUNTRY));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_COUNTRY);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_COUNTRY);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
            checkPermissions();
            adminPage = "adminCountry.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            getData();
            loadList(countries);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public List<Country> getFilteredList(String filter) {
        List<Country> countriesaux = new ArrayList<Country>();
        Country country;
        try {
            if (filter != null && !filter.equals("")) {
                country = utilsEJB.searchCountry(filter);
                countriesaux.add(country);
            } else {
                return countries;
            }
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            showError(ex);
        }
        return countriesaux;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Executions.getCurrent().sendRedirect(adminPage);

    }

    public void onClick$btnDelete() {
    }

    public void loadList(List<Country> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
                for (Country country : list) {
                    List<CountryTranslation> countryTranslations = utilsEJB.getCountryTranslationByCountryId(country.getId());
                    item = new Listitem();
                    item.setValue(country);
                    item.appendChild(new Listcell(country.getName()));
                    item.appendChild(new Listcell(country.getCode()));
                    item.appendChild(new Listcell(country.getShortName()));
                    item.appendChild(new Listcell(getCountryTraslationAlias(countryTranslations, Language.SPANISH)));
                    item.appendChild(new Listcell(getCountryTraslationAlias(countryTranslations, Language.ENGLISH)));
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, country, Permission.EDIT_COUNTRY) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, country, Permission.VIEW_COUNTRY) : new Listcell());
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        countries = new ArrayList<Country>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            countries = utilsEJB.getCountries(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

    public String getCountryTraslationAlias(List<CountryTranslation> countryTranslations, Long languageId) {
        String alias = "";
        if (countryTranslations != null) {
            for (CountryTranslation countryTranslation : countryTranslations) {
                if (countryTranslation.getLanguage().getId().equals(languageId)) {
                    alias = countryTranslation.getAlias();
                }
            }
        }
        return alias;
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.bread.crumb.country.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        txtAlias.setText("");
    }

    public void onClick$btnSearch() throws InterruptedException {
        try {
            loadList(getFilteredList(txtAlias.getText()));
        } catch (Exception ex) {
            showError(ex);
        }
    }


}
