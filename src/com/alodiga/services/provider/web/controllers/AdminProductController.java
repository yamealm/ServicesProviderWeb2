package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Currency;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductData;
import com.alodiga.services.provider.commons.models.ProductDenomination;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class AdminProductController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbIntegrationType;
    private Combobox cmbCurrency;
    private Textbox txtName;
    private Checkbox cbxTaxInclude;
    private Checkbox cbxEnabled;
    private Checkbox cbxFree;
    private Textbox txtReferenceCode;
    private Textbox txtURLRates;
    private Textbox txtAccessNumbers;
    private Textbox txtAliasES;
    private Textbox txtDescriptionES;
    private Textbox txtAliasEN;
    private Textbox txtDescriptionEN;
    private Decimalbox tbxDenomination;
    private Listbox lbxDenominations;
    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private Button btnAddDenomination;
    private Button btnSave;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Product) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.product");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.product");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {

            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        txtName.setRawValue(null);
        cbxTaxInclude.setChecked(false);
        cbxEnabled.setChecked(false);
        txtReferenceCode.setRawValue(null);
        txtURLRates.setRawValue(null);
        txtAccessNumbers.setRawValue(null);
        txtAliasES.setRawValue(null);
        txtDescriptionES.setRawValue(null);
        txtAliasEN.setRawValue(null);
        txtDescriptionEN.setRawValue(null);
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtReferenceCode.setReadonly(true);
        txtURLRates.setReadonly(true);
        txtAccessNumbers.setReadonly(true);
        cbxTaxInclude.setDisabled(true);
        cbxEnabled.setDisabled(true);
        cbxFree.setDisabled(true);
        txtAliasES.setReadonly(true);
        txtDescriptionES.setReadonly(true);
        txtAliasEN.setReadonly(true);
        txtDescriptionEN.setReadonly(true);
        tbxDenomination.setReadonly(true);
        cmbCurrency.setDisabled(true);
        cmbEnterprise.setDisabled(true);
        cmbCategory.setDisabled(true);
        btnAddDenomination.setVisible(false);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;
    }

    public void onChange$cmbEnterprise() {
        Enterprise enterprise = (Enterprise) cmbEnterprise.getSelectedItem().getValue();
        loadCurrencies(enterprise);

    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveProduct(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProduct(productParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(productParam);
                loadCategories(productParam.getCategory());
                loadEnterprises(productParam.getEnterprise());
                loadDenominations();
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(productParam);
                loadCategories(productParam.getCategory());
                loadEnterprises(productParam.getEnterprise());
                loadDenominations();
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                loadCategories(null);
                loadEnterprises(null);
                break;
            default:
                break;
        }
    }

    public void loadFields(Product product) {
        txtName.setText(product.getName());
        txtReferenceCode.setText(product.getReferenceCode());
        txtURLRates.setText(product.getRatesUrl());
        txtAccessNumbers.setText(product.getAccessNumberUrl());
        cbxTaxInclude.setChecked(product.getTaxInclude());
        cbxEnabled.setChecked(product.getEnabled());
        cbxFree.setChecked(product.getIsFree());

        List<ProductData> productDatas = product.getProductData();
        for (ProductData productData : productDatas) {
            if (productData.getLanguage().getId().equals(Language.SPANISH)) {
                txtAliasES.setText(productData.getAlias());
                txtDescriptionES.setText(productData.getDescription());
            } else {
                txtAliasEN.setText(productData.getAlias());
                txtDescriptionEN.setText(productData.getDescription());
            }
        }

    }

    public void onClick$btnAddDenomination() throws InterruptedException {

        if (cmbCurrency.getSelectedIndex() == -1) {
            this.showMessage("sp.error.currencyNotSelected", true, null);

            return;
        }
        if (tbxDenomination.getText() == null || tbxDenomination.getText().equals("")) {
            return;
        } else {
            String _amount = tbxDenomination.getText();
            List items = (List) lbxDenominations.getItems();
            for (int i = 0; i < items.size(); i++) {
                ProductDenomination denomination = new ProductDenomination();
                Float amount = Float.parseFloat(_amount);
                Listitem item = (Listitem) items.get(i);
                if (item.getChildren() != null && item.getChildren().get(0) != null) {
                    item.getChildren().get(1);
                    denomination = (ProductDenomination) ((Listcell) item.getChildren().get(0)).getValue();

                    if (amount.equals(denomination.getAmount())) {
                        return;
                    }
                }
            }

            Listitem listItem = new Listitem();
            Listcell cell = new Listcell(_amount);
            ProductDenomination denomination = new ProductDenomination();
            denomination.setAmount(tbxDenomination.getValue().floatValue());
            denomination.setCurrency(((Currency) cmbCurrency.getSelectedItem().getValue()));
            cell.setValue(denomination);
            listItem.appendChild(cell);
            listItem.appendChild(new Listcell(((Currency) cmbCurrency.getSelectedItem().getValue()).getSymbol()));
            listItem.appendChild(initDeleteButton(listItem));
            listItem.setParent(lbxDenominations);
            tbxDenomination.setText("");
        }
    }

    private void loadCurrencies(Enterprise enterprise) {

        try {
            cmbCurrency.getItems().clear();
            Currency c = enterprise.getCurrency();
            System.out.println("Cuur" + c.getName());
            Comboitem cmbItem = new Comboitem();
            cmbItem.setLabel(c.getName());
            cmbItem.setDescription(c.getSymbol());
            cmbItem.setValue(c);
            cmbItem.setParent(cmbCurrency);
            cmbCurrency.setSelectedItem(cmbItem);
        } catch (Exception ex) {
            showError(ex);

        }

    }

    private void loadEnterprises(Enterprise enterprise) {
        try {
            cmbEnterprise.getItems().clear();
            enterprises = utilsEJB.getEnterprises();
            for (Enterprise e : enterprises) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbEnterprise);
                if (enterprise != null && enterprise.getId().equals(e.getId())) {
                    cmbEnterprise.setSelectedItem(cmbItem);
                } else {
                    cmbEnterprise.setSelectedIndex(0);
                }
            }
            onChange$cmbEnterprise();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadCategories(Category category) {
        try {
            cmbCategory.getItems().clear();
            categories = productEJB.getCategories(request);
            for (Category c : categories) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(c.getName());
                cmbItem.setValue(c);
                cmbItem.setParent(cmbCategory);
                if (category != null && category.getId().equals(c.getId())) {
                    cmbCategory.setSelectedItem(cmbItem);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadDenominations() {

        List<ProductDenomination> denominations = null;
        if (productParam != null) {
            denominations = productParam.getProductDenominations();
        }

        if (denominations != null || denominations.size() > 0) {
            for (ProductDenomination denomination : denominations) {
                Listitem listItem = new Listitem();
                Listcell cell = new Listcell(denomination.getAmount().toString());
                cell.setValue(denomination);
                listItem.appendChild(cell);
                listItem.appendChild(new Listcell(denomination.getCurrency().getSymbol()));
                listItem.appendChild(eventType!=null && !eventType.equals(WebConstants.EVENT_VIEW)?initDeleteButton(listItem):new Listcell());
                listItem.setParent(lbxDenominations);
            }
        }
    }

    private void saveProduct(Product _product) {
        Product product = new Product();
        try {
            product.setName(txtName.getText());
            product.setReferenceCode(txtReferenceCode.getText());
            product.setAccessNumberUrl(txtAccessNumbers.getText());
            product.setRatesUrl(txtURLRates.getText());
            product.setEnabled(cbxEnabled.isChecked());
            product.setIsFree(cbxFree.isChecked());
            product.setTaxInclude(cbxTaxInclude.isChecked());
            Enterprise e = new Enterprise();
            e.setId(((Enterprise) cmbEnterprise.getSelectedItem().getValue()).getId());
            product.setEnterprise(e);
            product.setCategory((Category) cmbCategory.getSelectedItem().getValue());

            if (_product != null) {
                product.setId(_product.getId());
                List<ProductData> productDatas = productParam.getProductData();
                for (ProductData productData : productDatas) {
                    productData.setAlias(productData.getLanguage().getId().equals(Language.SPANISH) ? txtAliasES.getText() : txtAliasEN.getText());
                    productData.setDescription(productData.getLanguage().getId().equals(Language.SPANISH) ? txtDescriptionES.getText() : txtDescriptionEN.getText());

                }
            } else {
                //ESPAÑOL
                List<ProductData> productDatas = new ArrayList<ProductData>();
                ProductData productData1 = new ProductData();
                productData1.setAlias(txtAliasES.getText());
                productData1.setDescription(txtDescriptionES.getText());
                request.setParam(Language.SPANISH);
                productData1.setLanguage(utilsEJB.loadLanguage(request));
                productData1.setProduct(product);
                productDatas.add(productData1);
                //INGLÉS
                ProductData productData2 = new ProductData();
                productData2 = new ProductData();
                productData2.setAlias(txtAliasEN.getText());
                productData2.setDescription(txtDescriptionEN.getText());
                request.setParam(Language.ENGLISH);
                productData2.setLanguage(utilsEJB.loadLanguage(request));
                productData2.setProduct(product);
                productDatas.add(productData2);
                product.setProductData(productDatas);
            }
            // Denominaciones: se debe eliminar las anteriores
            List<ProductDenomination> productDenominations = new ArrayList<ProductDenomination>();
            List items = (List) lbxDenominations.getItems();
            for (int i = 0; i < items.size(); i++) {
                ProductDenomination denomination = new ProductDenomination();

                Listitem item = (Listitem) items.get(i);
                if (item.getChildren() != null && item.getChildren().get(0) != null) {
                    denomination = (ProductDenomination) ((Listcell) item.getChildren().get(0)).getValue();
                    denomination.setId(null);
                    denomination.setProduct(product);
                    productDenominations.add(denomination);
                }
            }
            if (eventType == WebConstants.EVENT_EDIT) {
                request.setParam(productParam.getId());
                request.setAuditData(null);
                productEJB.deleteProductDenomination(request);
            }

            product.setProductDenominations(productDenominations);
            request.setParam(product);
            request.setAuditData(null);
            product = productEJB.saveProduct(request);
            productParam = product;
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private Listcell initDeleteButton(final Listitem listItem) {
        Listcell cell = new Listcell();
        final Button button = new Button();
        button.setClass("open gray");
        button.setImage("/images/icon-remove.png");
        button.addEventListener("onClick", new EventListener() {

            @Override
            public void onEvent(Event arg0) throws Exception {
                listItem.getParent().removeChild(listItem);
            }
        });

        button.setParent(cell);
        return cell;
    }
}
