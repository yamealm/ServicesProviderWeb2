package com.alodiga.services.provider.web.controllers;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AddProductController extends GenericAbstractAdminController {

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

    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private Button btnAddDenomination;
    private Button btnSave;
    private Window winAddProductView;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
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
               loadEnterprises(null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        
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
    	    if (txtBachNumber.getText().isEmpty()) {
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
           }if (!GeneralUtils.isNumeric(txtAmount.getText())) {
           	txtAmount.setFocus(true);
               this.showMessage("sp.error.field.number", true, null);
           } else {
            return true;
        }
        return false;

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
    
    private void save(Product _product) {
		Product product = new Product();
		try {

			if (_product != null)
				product.setId(_product.getId());
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
			Enterprise e = (Enterprise) cmbEnterprise.getSelectedItem().getValue();
			product.setEnterprise(e);
			request.setParam(product);
			request.setAuditData(null);
			blockFields();
			product = productEJB.saveProduct(request);
//			productParam = product;
//			eventType = WebConstants.EVENT_EDIT;
			this.showMessage("sp.common.save.success", false, null);
		} catch (Exception ex) {
			showError(ex);
		}
	}
    
    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    save(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    save(productParam);
                    break;
                default:
                    break;
            }
        }
    }

	public void loadData() {
		switch (eventType) {
		case WebConstants.EVENT_EDIT:
			break;
		case WebConstants.EVENT_VIEW:
			break;
		case WebConstants.EVENT_ADD:
			break;
		default:
			break;
		}

	}
	
	 public void onClick$btnBack() {
		 winAddProductView.detach();
	   	 Window window = (Window)Executions.createComponents("catProducts.zul", null, null);
	        try {
				window.doModal();
			} catch (SuspendNotAllowedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
}
