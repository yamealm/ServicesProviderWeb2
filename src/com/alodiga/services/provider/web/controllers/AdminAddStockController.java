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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condition;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductExpiration;
import com.alodiga.services.provider.commons.models.ProductSerie;
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

public class AdminAddStockController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbProvider;
    private Combobox cmbCondition;
    private Combobox cmbCustomer;
    private Checkbox cbxSerial;
    private Checkbox cbxExpiration;
    private Checkbox cbxCure;
    private Textbox txtAmount;
    private Textbox txtBachNumber;
    private Textbox txtUbicationFolder;
    private Textbox txtUbicationBox;
    private Textbox txtactNpNsn;
    private Textbox txtDescription;
    private Textbox txtPartNumber;
    private Textbox txtSerial;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intQuantity;
    private Datebox dtxExpiration;
    private Datebox dtxCure;

    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private CustomerEJB customerEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private List<Provider> providers;
    private List<Condition> conditions;
    private List<Customer> customers;
    private User user;
    private Button btnSave;
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

            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
		cbxSerial.setChecked(false);
		cbxExpiration.setChecked(false);
		cbxCure.setChecked(false);
		intQuantity.setRawValue(null);
		txtBachNumber.setRawValue(null);
		txtUbicationFolder.setRawValue(null);
		txtUbicationBox.setRawValue(null);
		txtSerial.setRawValue(null);
		txtactNpNsn.setRawValue(null);
		txtDescription.setRawValue(null);
		txtPartNumber.setRawValue(null);
    	intStockMax.setRawValue(null);
    	intStockMin.setRawValue(null);
    	txtInvoice.setRawValue(null);
    	txtObservation.setRawValue(null);
    }

    public void blockFields() {

    	cbxSerial.setChecked(false);
    	cbxExpiration.setChecked(false);
    	cbxCure.setChecked(false);
    	intQuantity.setReadonly(true);
		txtBachNumber.setReadonly(true);
		txtUbicationFolder.setReadonly(true);
		txtUbicationBox.setReadonly(true);
		txtactNpNsn.setReadonly(true);
		txtDescription.setReadonly(true);
		txtPartNumber.setReadonly(true);
    	intStockMax.setReadonly(true);
    	txtSerial.setReadonly(true);
    	intStockMin.setReadonly(true);
    	txtInvoice.setReadonly(true);
    	txtObservation.setReadonly(true);
    	cbxSerial.setDisabled(true);
        cmbEnterprise.setDisabled(true);
        cmbCategory.setDisabled(true);
        cmbCondition.setDisabled(true);
        cmbCustomer.setDisabled(true);
        cmbProvider.setDisabled(true);
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
        }if (!GeneralUtils.isNumeric(intQuantity.getText())) {
        	intQuantity.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        }if (intQuantity.getText().isEmpty()) {
        	intQuantity.setFocus(true);
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
                case WebConstants.EVENT_ADD:
                    saveProduct(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProduct(null);
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
            case WebConstants.EVENT_DELETE:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
            	loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                loadCategory(null);
                loadCondition(null);
                loadProvider(null);
                loadCustomer(null);
                break;
            default:
                break;
        }
    }

    
    public void loadFields(Product product) {
    	
    	intStockMax.setValue(product.getStockMax());
    	intStockMin.setValue(product.getStockMin());
    	txtAmount.setText(String.valueOf(product.getAmount()));
		txtBachNumber.setText(product.getBatchNumber());
		txtUbicationFolder.setText(product.getUbicationFolder());
		txtUbicationBox.setText(product.getUbicationBox());
		txtactNpNsn.setText(product.getActNpNsn());
		txtDescription.setText(product.getDescription());
		txtPartNumber.setText(product.getPartNumber());
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
                } else {
                	cmbCategory.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private void loadCondition(Condition condition) {
        try {
        	cmbCondition.getItems().clear();
        	conditions = transactionEJB.getConditions();
            for (Condition e : conditions) {
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

    private void loadCustomer(Customer customer) {
        try {
        	cmbCustomer.getItems().clear();
        	EJBRequest request = new EJBRequest();
        	customers = customerEJB.getCustomers(request);
            for (Customer e : customers) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(e.getFirstName() + " " + e.getLastName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbCustomer);
                if (customer != null && customer.getId().equals(e.getId())) {
                	cmbCustomer.setSelectedItem(cmbItem);
                } else {
                	cmbCustomer.setSelectedIndex(0);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void saveProduct(Transaction _transaction) {
        Transaction transaction = new Transaction();
        try {

            if (_transaction != null) 
            	transaction.setId(_transaction.getId());
            transaction.setProduct(productParam);
            Category category = new Category();
            category.setId(((Category) cmbCategory.getSelectedItem().getValue()).getId());
            transaction.setCategory(category);
            Condition condition = new Condition();
            condition.setId(((Condition) cmbCondition.getSelectedItem().getValue()).getId());
            transaction.setCondition(condition);
            Customer customer = new Customer();
            customer.setId(((Customer) cmbCustomer.getSelectedItem().getValue()).getId());
            transaction.setCustomer(customer);
            Provider provider = new Provider();
            provider.setId(((Provider) cmbCondition.getSelectedItem().getValue()).getId());
            transaction.setProvider(provider);
            transaction.setUser(user);
            transaction.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            transaction.setQuantity(intQuantity.getValue());
            TransactionType transactionType = new TransactionType();
            transactionType.setId(TransactionType.ADD);
            transaction.setTransactionType(transactionType);
            transaction.setSerial(cbxSerial.isChecked());
            transaction.setInvoice(txtInvoice.getText());
            productParam.setAmount(Float.valueOf(txtAmount.getText()));
            if (cbxSerial.isChecked()){
	            ProductSerie productSerie  = new ProductSerie(); 
	            productSerie.setBeginTransactionId(transaction);
	            productSerie.setSerie(txtSerial.getText());
	            if (cbxExpiration.isChecked())
	            	productSerie.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
	            if (cbxCure.isChecked())
	            	productSerie.setCure(new Timestamp(dtxCure.getValue().getTime()));
	            productSerie.setProduct(productParam);
	            productSerie.setProvider(provider);
	            productSerie.setAmount(Float.valueOf(txtAmount.getText()));
            }else if (cbxExpiration.isChecked()){
            	ProductExpiration productExpiration = new ProductExpiration();
            	productExpiration.setProduct(productParam);
            	productExpiration.setAmount(Float.valueOf(txtAmount.getText()));
            	productExpiration.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
            	productExpiration.setBeginTransactionId(transaction);
            	productExpiration.setProvider(provider);
            	productExpiration.setQuantity(intQuantity.getValue());
            }
            transaction = transactionEJB.saveTransactionStock(transaction);
//            productParam = product;
//            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

//    private Listcell initDeleteButton(final Listitem listItem) {
//        Listcell cell = new Listcell();
//        final Button button = new Button();
//        button.setClass("open gray");
//        button.setImage("/images/icon-remove.png");
//        button.addEventListener("onClick", new EventListener() {
//
//            @Override
//            public void onEvent(Event arg0) throws Exception {
//                listItem.getParent().removeChild(listItem);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
}