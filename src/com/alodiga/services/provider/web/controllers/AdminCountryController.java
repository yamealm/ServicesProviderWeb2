package com.alodiga.services.provider.web.controllers;


import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.CountryTranslation;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

public class AdminCountryController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtName;
    private Textbox txtShortName;
    private Textbox txtCode;
    private Textbox txtAlternativeName1;
    private Textbox txtAlternativeName2;
    private Textbox txtAlternativeName3;
    private Textbox txtSpanishAlias;
    private Textbox txtEnglishAlias;
    private UtilsEJB utilsEJB = null;
    private Language Language;
    private Country countryParam;
    private Button btnSave;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        countryParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Country) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.country");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.country");
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
        txtShortName.setRawValue(null);
        txtCode.setRawValue(null);
        txtAlternativeName1.setRawValue(null);
        txtAlternativeName2.setRawValue(null);
        txtAlternativeName3.setRawValue(null);

    }

    private void loadFields(Country country) {
        try {
            txtName.setText(country.getName());
            txtShortName.setText(country.getShortName());
            txtCode.setText(country.getCode());
            txtAlternativeName1.setText(country.getAlternativeName1());
            txtAlternativeName2.setText(country.getAlternativeName2());
            txtAlternativeName3.setText(country.getAlternativeName3());
            List<CountryTranslation> countryTranslations = utilsEJB.getCountryTranslationByCountryId(country.getId());
            for (CountryTranslation countryTranslation : countryTranslations) {
                if (countryTranslation.getLanguage().getId().equals(Language.SPANISH)) {
                    txtSpanishAlias.setText(countryTranslation.getAlias());
                } else {
                    txtEnglishAlias.setText(countryTranslation.getAlias());
                }

            }
         } catch (Exception ex) {
            showError(ex);
        }
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtShortName.setReadonly(true);
        txtCode.setReadonly(true);
        txtAlternativeName1.setReadonly(true);
        txtAlternativeName2.setReadonly(true);
        txtSpanishAlias.setReadonly(true);
        txtEnglishAlias.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtShortName.getText().isEmpty()) {
            txtShortName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtCode.getText().isEmpty()) {
            txtCode.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtSpanishAlias.getText().isEmpty()) {
            txtSpanishAlias.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtEnglishAlias.getText().isEmpty()) {
            txtEnglishAlias.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else {
            return true;
        }
        return false;

    }

    public void onClick$btnCodes() {
        Executions.getCurrent().sendRedirect("/docs/T-SP-E.164D-2009-PDF-S.pdf", "_blank");
    }

    public void onClick$btnShortNames() {
        Executions.getCurrent().sendRedirect("/docs/countries-abbreviation.pdf", "_blank");
    }

    private void saveCountry(Country _country) {
        try {
            Country country = null;
            List<CountryTranslation> countryTranslations = null;

            if (_country != null) {
                country = _country;
                countryTranslations = utilsEJB.getCountryTranslationByCountryId(_country.getId());
            } else {//New country
                country = new Country();
            }
            country.setName(txtName.getText());
            country.setCode(txtCode.getText());
            country.setShortName(txtShortName.getText());
            country.setAlternativeName1(txtAlternativeName1.getText());
            country.setAlternativeName2(txtAlternativeName2.getText());
            country = utilsEJB.saveCountry(country);
            processCountryTranslation(countryTranslations, country);
            countryParam = country;
            this.showMessage("sp.common.save.success", false, null);
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
                    saveCountry(countryParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(countryParam);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(countryParam);
                break;
            case WebConstants.EVENT_ADD:
                break;
            default:
                break;
        }
    }


    private void processCountryTranslation(List<CountryTranslation> countryTranslations, Country country) throws Exception {

        List<CountryTranslation> newCountryTranslations = null;
        if (countryTranslations != null) {
            for (CountryTranslation countryTranslation : countryTranslations) {
                countryTranslation.setAlias(countryTranslation.getLanguage().getId().equals(Language.SPANISH) ? txtSpanishAlias.getText() : txtEnglishAlias.getText());
                utilsEJB.saveCountryTranslation(countryTranslation);
            }
            //return countryTranslations;

        } else {
            newCountryTranslations = new ArrayList<CountryTranslation>();
            //Spanish
            CountryTranslation countryTranslation = new CountryTranslation();
            countryTranslation.setAlias(txtSpanishAlias.getText());
            countryTranslation.setCountry(country);
            Language language = new Language();
            language.setId(Language.SPANISH);
            countryTranslation.setLanguage(language);
            newCountryTranslations.add(countryTranslation);
            utilsEJB.saveCountryTranslation(countryTranslation);
            //English
            countryTranslation = new CountryTranslation();
            countryTranslation.setAlias(txtEnglishAlias.getText());
            countryTranslation.setCountry(country);
            language = new Language();
            language.setId(Language.ENGLISH);
            countryTranslation.setLanguage(language);
            newCountryTranslations.add(countryTranslation);
            utilsEJB.saveCountryTranslation(countryTranslation);
            //return newCountryTranslations;
        }

    }
}
