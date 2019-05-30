//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.models.Country;
//import com.alodiga.services.provider.commons.models.CountryTranslation;
//import com.alodiga.services.provider.commons.models.Language;
//import com.alodiga.services.provider.commons.models.MobileOperator;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.util.ArrayList;
//import java.util.List;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Checkbox;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Textbox;
//
//public class AdminMobileOperatorController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Textbox txtName;
//    private Textbox txtAlternativeName1;
//    private Textbox txtAlternativeName2;
//    private Textbox txtAlternativeName3;
//    private Combobox cmbCountries;
//    private Checkbox cbxEnabled;
//    private TopUpProductEJB topUpProductEJB = null;
//    private UtilsEJB utilsEJB = null;
//    private Language Language;
//    private MobileOperator mobileOperatorParam;
//    private Button btnSave;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        mobileOperatorParam = (Sessions.getCurrent().getAttribute("object") != null) ? (MobileOperator) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.mobileOperator");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, "sp.crud.mobileOperator");
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//
//            topUpProductEJB = (TopUpProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.TOP_UP_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void clearFields() {
//        txtName.setRawValue(null);
//        txtAlternativeName1.setRawValue(null);
//        txtAlternativeName2.setRawValue(null);
//        txtAlternativeName3.setRawValue(null);
//
//    }
//
//    private void loadFields(MobileOperator mobileOperator) {
//        try {
//            txtName.setText(mobileOperator.getName());
//            txtAlternativeName1.setText(mobileOperator.getAlternativeName1());
//            txtAlternativeName2.setText(mobileOperator.getAlternativeName2());
//            txtAlternativeName3.setText(mobileOperator.getAlternativeName3());
//            cbxEnabled.setChecked(mobileOperator.getEnabled());
//         } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void blockFields() {
//        txtName.setReadonly(true);
//        txtAlternativeName1.setReadonly(true);
//        txtAlternativeName2.setReadonly(true);
//        txtAlternativeName3.setReadonly(true);
//        btnSave.setVisible(false);
//    }
//
//    public Boolean validateEmpty() {
//        if (txtName.getText().isEmpty()) {
//            txtName.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        }  else if (cmbCountries.getSelectedIndex() == -1) {
//            cmbCountries.setFocus(true);
//            this.showMessage("sp.error.field.required", true, null);
//        } else {
//            return true;
//        }
//        return false;
//
//    }
//
//    private void loadCountries(Country country) {
//        try {
//            List<Country> countries = utilsEJB.getCountries(request);
//
//            for (int i = 0; i < countries.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(countries.get(i));
//                item.setLabel(countries.get(i).getName());
//                item.setParent(cmbCountries);
//                if (country != null && countries.get(i).getId().equals(country.getId())) {
//                    cmbCountries.setSelectedItem(item);
//                }
//            }
//        } catch (Exception ex) {
//             showError(ex);
//        }
//    }
//
//    private void saveMobileOperator(MobileOperator _mobileOperator) {
//        try {
//            MobileOperator mobileOperator = null;
//            List<CountryTranslation> countryTranslations = null;
//
//            if (_mobileOperator != null) {
//                mobileOperator = _mobileOperator;
//            } else {//New country
//                mobileOperator = new MobileOperator();
//            }
//            mobileOperator.setName(txtName.getText());
//            mobileOperator.setAlternativeName1(txtAlternativeName1.getText());
//            mobileOperator.setAlternativeName2(txtAlternativeName2.getText());
//            mobileOperator.setAlternativeName3(txtAlternativeName3.getText());
//            mobileOperator.setEnabled(cbxEnabled.isChecked());
//            mobileOperator.setCountry((Country)cmbCountries.getSelectedItem().getValue());
//            request.setParam(mobileOperator);
//            mobileOperator = topUpProductEJB.saveMobileOperator(request);
//            mobileOperatorParam = mobileOperator;
//            this.showMessage("sp.common.save.success", false, null);
//        } catch (Exception ex) {
//           showError(ex);
//        }
//
//    }
//
//    public void onClick$btnSave() {
//        if (validateEmpty()) {
//            switch (eventType) {
//                case WebConstants.EVENT_ADD:
//                    saveMobileOperator(null);
//                    break;
//                case WebConstants.EVENT_EDIT:
//                    saveMobileOperator(mobileOperatorParam);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    public void loadData() {
//        switch (eventType) {
//            case WebConstants.EVENT_ADD:
//                loadCountries(null);
//                break;
//            case WebConstants.EVENT_EDIT:
//                loadFields(mobileOperatorParam);
//                loadCountries(mobileOperatorParam.getCountry());
//                break;
//            case WebConstants.EVENT_VIEW:
//                loadFields(mobileOperatorParam);
//                loadCountries(mobileOperatorParam.getCountry());
//                blockFields();
//                btnSave.setVisible(false);
//                break;
//            default:
//                break;
//        }
//    }
//
//
//  
//}
