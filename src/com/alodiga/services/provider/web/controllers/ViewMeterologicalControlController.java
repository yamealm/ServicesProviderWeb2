package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductHistory;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.Transaction;
import com.alodiga.services.provider.commons.models.TransactionType;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class ViewMeterologicalControlController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbCondition;
    private Combobox cmbProvider;
    private Checkbox cbxExpiration;
    private Checkbox cbxCure;
    private Textbox txtAmount;
    private Textbox txtBachNumber;
    private Textbox txtUbicationFolder;
    private Textbox txtUbicationBox;
    private Textbox txtactNpNsn;
    private Textbox txtDescription;
    private Textbox txtPartNumber;
    private Textbox txtWorkOrder;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Textbox txtSerial;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intStock;
    private Intbox intQuantity;
    private Datebox dtxExpiration;
    private Datebox dtxCure;
    private Toolbarbutton viewDetail;


    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private ProductSerie productSerieParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private List<Provider> providers;
    private List<Condicion> conditions;
    private List<Customer> customers;
    private User currentUser;
    private Profile currentProfile;
    private Button btnSave;
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productSerieParam = (Sessions.getCurrent().getAttribute("object") != null) ? (ProductSerie) Sessions.getCurrent().getAttribute("object") : null;
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
        	currentUser = AccessControl.loadCurrentUser();
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {

    }

    public void blockFields() {

    	intStock.setReadonly(true);
		txtBachNumber.setReadonly(true);
		txtAmount.setReadonly(true);
		txtUbicationFolder.setReadonly(true);
		txtUbicationBox.setReadonly(true);
		txtactNpNsn.setReadonly(true);
		txtDescription.setReadonly(true);
		txtPartNumber.setReadonly(true);
    	intStockMax.setReadonly(true);
    	intStockMin.setReadonly(true);
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
        }if (intQuantity.getText().isEmpty()) {
        	intQuantity.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (txtSerial.getText().isEmpty()) {
        	txtSerial.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }
       
        
        
        else {
            return true;
        }
        return false;
    }


    public void onClick$btnSave() {
    	  if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_EDIT:
                    saveProductSerie(productSerieParam);
                    break;
                default:
                    break;
            }
    	  }
    }

    

    public void onClick$btnBack() {
    	Sessions.getCurrent().setAttribute("object",productSerieParam.getProduct());
    	Executions.sendRedirect("./listAddMeteorologicalControl.zul");
    }
    
    public void onClick$viewDetail() {
    	Sessions.getCurrent().setAttribute("object",productSerieParam.getProduct());
    	Executions.sendRedirect("./listAddMeteorologicalControl.zul");
    }
    
    public void loadData() {
    	Category category = new Category();
    	category.setId(Category.METEOROLOGICAL_CONTROL);
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(productSerieParam);
                loadEnterprises(productSerieParam.getProduct().getEnterprise());
                loadCategory(productSerieParam.getCategory());
                loadCondition(productSerieParam.getCondition());
                loadProvider(productSerieParam.getProvider());
                blockFields();
                break;

            default:
                break;
        }
    }

    
    public void loadFields(ProductSerie productSerie) {
    	
    	intStockMax.setValue(productSerie.getProduct().getStockMax());
    	intStockMin.setValue(productSerie.getProduct().getStockMin());
    	txtAmount.setText(String.valueOf(productSerie.getProduct().getAmount()));
		txtBachNumber.setText(productSerie.getProduct().getBatchNumber());
		txtUbicationFolder.setText(productSerie.getProduct().getUbicationFolder());
		txtUbicationBox.setText(productSerie.getProduct().getUbicationBox());
		txtactNpNsn.setText(productSerie.getProduct().getActNpNsn());
		txtDescription.setText(productSerie.getProduct().getDescription());
		txtInvoice.setText(productSerie.getBeginTransactionId().getInvoice() );
		txtPartNumber.setText(productSerie.getProduct().getPartNumber());
		try {
    		int  quantity = transactionEJB.loadQuantityByProductId(productSerie.getProduct().getId(),productSerie.getCategory().getId());
    		intStock.setValue(quantity);
    	} catch (Exception ex) {
    		intStock.setValue(0);
        }
		intQuantity.setValue(productSerie.getQuantity());
		if (productSerie.getExpirationDate()!=null) {
			cbxExpiration.setChecked(true);
			dtxExpiration.setValue(productSerie.getExpirationDate());
		}else
			cbxExpiration.setChecked(false);
		if (productSerie.getCure()!=null) {
			cbxCure.setChecked(true);
			dtxCure.setValue(productSerie.getCure());
		}else
			cbxCure.setChecked(true);
		txtObservation.setText(productSerie.getBeginTransactionId().getObservation());
		txtSerial.setText(productSerie.getSerie());
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

    private void loadCategory(Category category) {
        try {
        	cmbCategory.getItems().clear();
        	categories = transactionEJB.getCategories();
            for (Category e : categories) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbCategory);
                if (category != null && category.getId().equals(e.getId())) {
                	cmbCategory.setSelectedItem(cmbItem);
                } else if(e.getId().equals(Category.METEOROLOGICAL_CONTROL)){
                	cmbCategory.setSelectedItem(cmbItem);
                }
            }
            cmbCategory.setReadonly(true);
            cmbCategory.setDisabled(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private void loadCondition(Condicion condition) {
        try {
        	cmbCondition.getItems().clear();
        	conditions = transactionEJB.getConditions();
            for (Condicion e : conditions) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbCondition);
                if (condition != null && condition.getId().equals(e.getId())) {
                	cmbCondition.setSelectedItem(cmbItem);
                } else {
                	cmbCondition.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private void loadProvider(Provider provider) {
        try {
        	cmbProvider.getItems().clear();
        	EJBRequest request = new EJBRequest();
        	providers = productEJB.getProviders(request);
            for (Provider e : providers) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbProvider);
                if (provider != null && provider.getId().equals(e.getId())) {
                	cmbProvider.setSelectedItem(cmbItem);
                } else {
                	cmbProvider.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void saveProductSerie(ProductSerie productSerie) {
        Transaction transaction = productSerie.getBeginTransactionId();
		try {
			int quantity = productSerie.getBeginTransactionId().getQuantity();
			quantity = quantity - productSerie.getQuantity() + intQuantity.getValue();
			transaction.setQuantity(quantity);
			Condicion condition = new Condicion();
			condition.setId(((Condicion) cmbCondition.getSelectedItem().getValue()).getId());
			transaction.setCondition(condition);
			Provider provider = new Provider();
			provider.setId(((Provider) cmbProvider.getSelectedItem().getValue()).getId());
			transaction.setProvider(provider);
			transaction.setObservation(txtObservation.getText());
			transaction.setInvoice(txtInvoice.getText());
			productSerie.setProvider(provider);
			productSerie.setAmount(Float.valueOf(txtAmount.getText()));
			productSerie.setQuantity(intQuantity.getValue());
			productSerie.setCondition(condition);
			productSerie.setSerie(txtSerial.getText());
			if (cbxExpiration.isChecked())
				productSerie.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
			if (cbxCure.isChecked())
				productSerie.setCure(new Timestamp(dtxCure.getValue().getTime()));
    		transaction = transactionEJB.modificarStock(transaction, productSerie);
//            productParam = product;
//            eventType = WebConstants.EVENT_EDIT;
    			this.showMessage(Labels.getLabel("sp.common.save.success"), false, null);
    		
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   
    
} 
