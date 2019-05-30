//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.ejbs.BillPaymentEJB;
//import com.alodiga.services.provider.commons.ejbs.ProductEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.models.Country;
//import com.alodiga.services.provider.commons.models.Currency;
//import com.alodiga.services.provider.commons.models.Provider;
//import com.alodiga.services.provider.commons.models.billPayment.BillPaymentProduct;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Checkbox;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Datebox;
//import org.zkoss.zul.Doublebox;
//import org.zkoss.zul.Intbox;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Row;
//import org.zkoss.zul.Textbox;
//
//public class AdminBillPaymentController extends GenericAbstractAdminController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Checkbox cbxEnabled;
//    private BillPaymentEJB billPaymentEJB = null;
//    private Textbox txtName;
//    private Textbox txtDescription;
//    private Textbox txtReferenceCode;
//    private Combobox cmbProvider;
//    private Datebox dtxDate;
//    private Combobox cmbCountry;
//    
//    private Doublebox dbxMinAmount;
//    private Doublebox dbxMaxAmount;
//    private Intbox intRequiredDigits;
//    private Doublebox dbxFee;
//    private Combobox cmbCurrency;
//
//    private BillPaymentProduct billPaymentParam;
//    private UtilsEJB utilsEJB;
//    private ProductEJB productEJB;
//    
//    private Button btnSave;
//
//    private Label lblMinAmount;
//    private Label lblMaxAmount;
//    private Row rowExRate;
//    private Row rowDate;
//    private Doublebox dbxExRate;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        billPaymentParam = (Sessions.getCurrent().getAttribute("object") != null) ? (BillPaymentProduct) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        initView(eventType, "sp.crud.billPayment");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, "sp.crud.billPayment");
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
//            billPaymentEJB = (BillPaymentEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLPAYMENT_EJB);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    @Override
//    public void clearFields() {
//    }
//
//    private void loadFields(BillPaymentProduct billPaymentProduct) {
//        try {
//            if(billPaymentProduct != null){
//            txtName.setValue(billPaymentProduct.getName());
//            txtDescription.setValue(billPaymentProduct.getDescription());
//            txtReferenceCode.setValue(billPaymentProduct.getReferenceCode());
//            dtxDate.setValue(billPaymentProduct.getCreationDate());
//            //dbxExchangeRate.setValue(billPaymentProduct.getExchangeRate());
//            dbxMinAmount.setValue(billPaymentProduct.getMinAmount());
//            dbxMaxAmount.setValue(billPaymentProduct.getMaxAmount());
//            intRequiredDigits.setValue(billPaymentProduct.getRequiredDigitsAccount());
//            dbxFee.setValue(billPaymentProduct.getProviderFee());
//            cbxEnabled.setChecked(billPaymentProduct.getEnabled());
//            dbxExRate.setValue(billPaymentProduct.getExchangeRate());
//            if(billPaymentProduct.getExchangeRate()!=1){
//                rowExRate.setVisible(true);
//            }
//            loadProviders(billPaymentProduct.getProvider());
//            loadCountries(billPaymentProduct.getCountry());
//            loadCurrency(billPaymentProduct.getCurrencyCode());}
//            else {
//                loadProviders(null);
//                loadCountries(null);
//                loadCurrency(null);
//                rowDate.setVisible(false);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadCountries(Country country) {
//        try {
//            List<Country> countries = new ArrayList<Country>();
//            countries = getDataCountry();
//            for (int i = 0; i < countries.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(countries.get(i));
//                item.setLabel(countries.get(i).getName());
//                item.setParent(cmbCountry);
//                if (country != null && countries.get(i).getId().equals(country.getId())) {
//                    cmbCountry.setSelectedItem(item);
//                }
//            }
//            if(country==null){
//                cmbCountry.setSelectedItem(cmbCountry.getItemAtIndex(0));
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private void loadProviders(Provider provider) {
//        try {
//            List<Provider> providers = new ArrayList<Provider>();
//            //request.setParam(Country.USA);
//            //providers.add(utilsEJB.loadCountry(request));
//            providers = getDataProvider();
//            for (int i = 0; i < providers.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(providers.get(i));
//                item.setLabel(providers.get(i).getName());
//                item.setParent(cmbProvider);
//                if (provider!=null && providers.get(i).getId().equals(provider.getId())) {
//                    cmbProvider.setSelectedItem(item);
//                }
//            }
//            if(provider==null){
//                cmbProvider.setSelectedItem(cmbProvider.getItemAtIndex(0));
//            }
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    private void loadCurrency(String currencyCode) {
//        try {
//            List<Currency> currencies = new ArrayList<Currency>();
//            //request.setParam(Country.USA);
//            //providers.add(utilsEJB.loadCountry(request));
//            currencies = getDataCurrency();
//            for (int i = 0; i < currencies.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(currencies.get(i));
//                item.setLabel(currencies.get(i).getName());
//                item.setParent(cmbCurrency);
//                if (currencyCode!=null && ((currencyCode.equals(currencies.get(i).getSymbol())) ||
//                        //Colocar los simbolos que difieren en las dos tablas
//                        (currencies.get(i).getSymbol().equals("$") && currencyCode.equals("USD")))) {
//                    cmbCurrency.setSelectedItem(item);
//                    lblMinAmount.setValue(currencyCode);
//                    lblMaxAmount.setValue(currencyCode);
//                    if(!currencyCode.equals("USD")){
//                        rowExRate.setVisible(true);
//                    }
//                }
//            }
//            
//            if(currencyCode.equals("USD")){
//                        rowExRate.setVisible(false);
//            }
//            
//
//        } catch (Exception ex) {
//            this.showMessage("sp.error.general", true, ex);
//        }
//    }
//
//    public List<Provider> getDataProvider() {
//        List<Provider> providers = new ArrayList<Provider>();
//        providers = new ArrayList<Provider>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            request.setAuditData(null);
//            providers = productEJB.getProviders(request);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//
//        return providers;
//    }
//
//    public List<Country> getDataCountry() {
//        List<Country> countries = new ArrayList<Country>();
//        countries = new ArrayList<Country>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            countries = utilsEJB.getCountries(request);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//
//        return countries;
//    }
//
//    public List<Currency> getDataCurrency() {
//        List<Currency> currencies = new ArrayList<Currency>();
//        //currencies = new ArrayList<Currency>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            currencies = utilsEJB.getCurrencies();
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//
//        return currencies;
//    }
//
//    public void blockFields() {
//        txtName.setReadonly(true);
//        txtDescription.setReadonly(true);
//        txtReferenceCode.setReadonly(true);
//        dtxDate.setDisabled(true);
//        //dbxExchangeRate.setReadonly(true);
//        dbxMinAmount.setReadonly(true);
//        dbxMaxAmount.setReadonly(true);
//        intRequiredDigits.setReadonly(true);
//        dbxFee.setReadonly(true);
//        cmbProvider.setDisabled(true);
//        cmbCountry.setDisabled(true);
//        cbxEnabled.setDisabled(true);
//        btnSave.setVisible(false);
//        cmbCurrency.setDisabled(true);
//        dbxExRate.setReadonly(true);
//    }
//
//    public boolean validateEmpty() {
//        if (intRequiredDigits.getValue() == null) {
//            intRequiredDigits.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (intRequiredDigits.getValue() < 0) {
//            intRequiredDigits.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else if (dbxFee.getValue() == null) {
//            dbxFee.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (dbxFee.getValue() < 0) {
//            dbxFee.setFocus(true);
//            this.showMessage("sp.error.balanceAdjustment.zero", true, null);
//        } else if (dbxExRate.getValue() == null) {
//            dbxExRate.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else {
//            return true;
//        }
//        return false;
//    }
//
//    private void saveBillPaymentProduct() {
//        try {
//            billPaymentParam.setName(txtName.getValue());
//            billPaymentParam.setDescription(txtDescription.getValue());
//            billPaymentParam.setCategory("Bill Pay");
//            billPaymentParam.setProvider((Provider)cmbProvider.getSelectedItem().getValue());
//            billPaymentParam.setReferenceCode(txtReferenceCode.getValue());
//                if(dtxDate.getValue()!=null){
//                    billPaymentParam.setCreationDate(new Timestamp(dtxDate.getValue().getTime()));
//                }else{
//                    billPaymentParam.setCreationDate(new Timestamp(new Date().getTime()));}
//            if(((Country)cmbCountry.getSelectedItem().getValue()).getShortName()==null){
//                billPaymentParam.setCountryCode(((Country)cmbCountry.getSelectedItem().getValue()).getName());
//            }else{
//                billPaymentParam.setCountryCode(((Country)cmbCountry.getSelectedItem().getValue()).getShortName());
//            }
//            billPaymentParam.setCountry(((Country)cmbCountry.getSelectedItem().getValue()));
//            billPaymentParam.setExchangeRate(dbxExRate.getValue().floatValue());
//            billPaymentParam.setProviderFee(dbxFee.getValue().floatValue());
//            billPaymentParam.setLocalPhoneNumberLength(10);
//            billPaymentParam.setMinAmount(dbxMinAmount.getValue().floatValue());
//            billPaymentParam.setMaxAmount(dbxMaxAmount.getValue().floatValue());
//            billPaymentParam.setRequiredDigitsAccount(intRequiredDigits.getValue());
//            billPaymentParam.setCurrencyCode(lblMinAmount.getValue());
//            billPaymentParam.setImagePath(null);
//            billPaymentParam.setEnabled(cbxEnabled.isChecked());
//            request.setParam(billPaymentParam);
//            billPaymentParam = billPaymentEJB.saveBillPaymentProduct(request);
//            this.showMessage("sp.common.save.success", false, null);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnSave() {
//        if (validateEmpty()) {
//            saveBillPaymentProduct();
//        }
//    }
//
//    public void onChange$cmbCurrency(){
//        //No pude agarrar el objeto dentro del value del item seleccionado del combobox
//        
//        if (cmbCurrency.getSelectedItem().getLabel().equals("DOLLAR")){
//            lblMinAmount.setValue("USD");
//            lblMaxAmount.setValue("USD");
//        } else if (cmbCurrency.getSelectedItem().getLabel().equals("BOLIVAR")){
//            lblMinAmount.setValue("Bs");
//            lblMaxAmount.setValue("Bs");
//        } else if (cmbCurrency.getSelectedItem().getLabel().equals("PESO MEXICANO")){
//            lblMinAmount.setValue("MXN");
//            lblMaxAmount.setValue("MXN");
//        } else {
//            lblMinAmount.setValue(null);
//        }
//
//        if(!cmbCurrency.getSelectedItem().getLabel().equals("DOLLAR")){
//            rowExRate.setVisible(true);
//            dbxExRate.setValue(null);
//        } else {
//            rowExRate.setVisible(false);
//            dbxExRate.setValue(1);
//        }
//
//    }
//
//    public void loadData() {
//        switch (eventType) {
//            case WebConstants.EVENT_EDIT:
//                loadFields(billPaymentParam);
//                break;
//            case WebConstants.EVENT_VIEW:
//                loadFields(billPaymentParam);
//                blockFields();
//                break;
//            case WebConstants.EVENT_ADD:
//                loadFields(null);
//                billPaymentParam = new BillPaymentProduct();
//                break;
//
//            default:
//                break;
//        }
//    }
//}
