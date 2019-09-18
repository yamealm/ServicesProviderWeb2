package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Event;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

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

    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private Button btnAddDenomination;
    private Button btnSave;
    private User user;
    private AuditoryEJB auditoryEJB;
    private String ipAddress;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Product) Sessions.getCurrent().getAttribute("object") : null;
        user = AccessControl.loadCurrentUser();
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
        	ipAddress = Executions.getCurrent().getRemoteAddr();
        	auditoryEJB = (AuditoryEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUDITORY_EJB);
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
        }if (!GeneralUtils.isNumeric(txtAmount.getText())) {
        	txtAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }if (!GeneralUtils.isNumeric(txtRealAmount.getText())) {
        	txtAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }if (!GeneralUtils.isNumeric(txtInitialAmount.getText())) {
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
            product = productEJB.saveProduct(request);
            productParam = product;
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
            saveAudit(_product,product);
        } catch (Exception ex) {
            showError(ex);
        }
    }


    public void saveAudit(Product fCustomerOld ,Product fCustomerNew){
        EJBRequest request1 = new EJBRequest();
        EJBRequest request2 = new EJBRequest();            
        String result = "";
         String oldValue ="";
        request1.setParam(fCustomerOld);
        request2.setParam(fCustomerNew);

        try {
            result = auditoryEJB.getNaturalFieldProduct(request1, request2);
        } catch (Exception ex) {
            
        }

        if(!result.isEmpty()||!"".equals(result)){
            String name = fCustomerOld.getDescription();
            String actNpNsn = fCustomerOld.getActNpNsn();
            String batchNumber = fCustomerOld.getBatchNumber();
            String box = fCustomerOld.getUbicationBox();
            String folder = fCustomerOld.getUbicationFolder();
            String partNumber = fCustomerOld.getPartNumber();
            String amount = String.valueOf(fCustomerOld.getAmount());
            oldValue = "Description:"+name+"|ActNpNsn:"+actNpNsn+"|BatchNumber:"+batchNumber
                    +"|UbicationBox:"+box+"|UbicationFolder:"+folder+"|PartNumber:"+partNumber+"|Amount:"+amount;
            
            try {
				EJBRequest ejbRequest = new EJBRequest();
				ejbRequest.setParam(eventType);
				Event ev = auditoryEJB.loadEvent(ejbRequest);
				Audit audit = new Audit();
				EJBRequest auditRequest = new EJBRequest();
				audit.setUser(user);
				audit.setEvent(ev);
				Permission permission = PermissionManager.getInstance().getPermissionById(2L);
				audit.setPermission(permission);
				audit.setCreationDate(new Timestamp((new java.util.Date().getTime())));
				audit.setTableName("Product");
				audit.setRemoteIp(ipAddress);
				audit.setOriginalValues(oldValue);
				audit.setNewValues(result);
				audit.setResponsibleType("usuario");
				auditRequest.setParam(audit);
				audit = auditoryEJB.saveAudit(auditRequest);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
        }
    }
}