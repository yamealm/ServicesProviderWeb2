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
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class AdminProductController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Checkbox cbxEnabled;
    private Textbox txtAmount;
    private Textbox txtRealAmount;
    private Textbox txtInitialAmount;
    private Textbox txtBachNumber;
    private Textbox txtUbicationFolder;
    private Textbox txtUbicationBox;
    private Textbox txtactNpNsn;
    private Textbox txtDescription;
    private Textbox txtPartNumber;
    private Intbox intStockMax;
    private Intbox intStockMin;

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
		cbxEnabled.setChecked(false);
		txtRealAmount.setRawValue(null);
		txtInitialAmount.setRawValue(null);
		txtBachNumber.setRawValue(null);
		txtUbicationFolder.setRawValue(null);
		txtUbicationBox.setRawValue(null);
		txtactNpNsn.setRawValue(null);
		txtDescription.setRawValue(null);
		txtPartNumber.setRawValue(null);
    	intStockMax.setRawValue(null);
    	intStockMin.setRawValue(null);
    }

    public void blockFields() {

    	cbxEnabled.setChecked(false);
		txtRealAmount.setReadonly(true);
		txtInitialAmount.setReadonly(true);
		txtBachNumber.setReadonly(true);
		txtUbicationFolder.setReadonly(true);
		txtUbicationBox.setReadonly(true);
		txtactNpNsn.setReadonly(true);
		txtDescription.setReadonly(true);
		txtPartNumber.setReadonly(true);
    	intStockMax.setReadonly(true);
    	intStockMin.setReadonly(true);
        cbxEnabled.setDisabled(true);
        cmbEnterprise.setDisabled(true);
        btnAddDenomination.setVisible(false);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtRealAmount.getText().isEmpty()) {
        	txtRealAmount.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }  if (txtInitialAmount.getText().isEmpty()) {
        	txtInitialAmount.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtBachNumber.getText().isEmpty()) {
        	txtBachNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtUbicationFolder.getText().isEmpty()) {
        	txtUbicationFolder.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtUbicationBox.getText().isEmpty()) {
        	txtUbicationBox.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtactNpNsn.getText().isEmpty()) {
        	txtactNpNsn.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtDescription.getText().isEmpty()) {
        	txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null); 
        }if (intStockMax.getText().isEmpty()) {
        	intStockMax.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (intStockMin.getText().isEmpty()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (intStockMin.getValue()>intStockMax.getValue()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.common.stock.min.error", true, null);
        }if (GeneralUtils.isNumeric(txtAmount.getText())) {
        	txtAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }if (GeneralUtils.isNumeric(txtRealAmount.getText())) {
        	txtAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }if (GeneralUtils.isNumeric(txtInitialAmount.getText())) {
        	txtInitialAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }
        
        
        else {
            return true;
        }
        return false;
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

    

    public void onClick$btnBack() {
    	 Executions.sendRedirect("./listProducts.zul");
    }
    
    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
                loadEnterprises(null);
                break;
            default:
                break;
        }
    }

    
    public void loadFields(Product product) {
    	
    	intStockMax.setValue(product.getStockMax());
    	intStockMin.setValue(product.getStockMin());
    	txtAmount.setText(String.valueOf(product.getAmount()));
    	txtRealAmount.setText(String.valueOf(product.getRealAmount()));
		txtInitialAmount.setText(String.valueOf(product.getInictialAmount()));
		txtBachNumber.setText(product.getBatchNumber());
		txtUbicationFolder.setText(product.getUbicationFolder());
		txtUbicationBox.setText(product.getUbicationBox());
		txtactNpNsn.setText(product.getActNpNsn());
		txtDescription.setText(product.getDescription());
		txtPartNumber.setText(product.getPartNumber());
		cbxEnabled.setChecked(product.getEnabled());
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
        } catch (Exception ex) {
            showError(ex);
        }
    }



    private void saveProduct(Product _product) {
        Product product = new Product();
        try {
            product.setDescription(txtDescription.getText());
            product.setAmount(Float.valueOf(txtAmount.getText()));
            product.setActNpNsn(txtactNpNsn.getText());
            product.setBatchNumber(txtBachNumber.getText());
            product.setEnabled(cbxEnabled.isChecked());
            product.setInictialAmount(Float.valueOf(txtInitialAmount.getText()));
            product.setPartNumber(txtPartNumber.getText());
            product.setRealAmount(Float.valueOf(txtRealAmount.getText()));
            product.setUbicationBox(txtUbicationBox.getText());
            product.setUbicationFolder(txtUbicationFolder.getText());
            product.setStockMax(intStockMin.getValue());
            product.setStockMin(intStockMin.getValue());
            Enterprise e = new Enterprise();
            e.setId(((Enterprise) cmbEnterprise.getSelectedItem().getValue()).getId());
            product.setEnterprise(e);
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