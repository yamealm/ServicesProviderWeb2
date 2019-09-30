package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
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

public class AdminEgressUnitTransitController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbCustomer;
    private Combobox cmbProvider;
    private Combobox cmbCondition;
    private Checkbox cbxSerialVarius;
    private Checkbox cbxExpiration;
    private Checkbox cbxCure;
    private Textbox txtAmount;
    private Textbox txtBachNumber;
    private Textbox txtUbicationFolder;
    private Textbox txtUbicationBox;
    private Textbox txtactNpNsn;
    private Textbox txtDescription;
    private Textbox txtPartNumber;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Textbox txtSerial;
    private Textbox txtWorkOrder;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intStock;
    private Intbox intQuantity;
    private Datebox dtxExit;
    private Datebox dtxExpiration;
    private Datebox dtxCure;
    private Datebox dtxCreation;
    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private CustomerEJB customerEJB = null;
    private ProductSerie productSerieParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private List<Customer> customers;
    private List<Provider> providers;
    private List<Condicion> conditions;
    private User user;
    private Customer customer = null;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productSerieParam = (Sessions.getCurrent().getAttribute("object") != null) ? (ProductSerie) Sessions.getCurrent().getAttribute("object") : null;
        productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
        utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
        transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
        customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
        user = AccessControl.loadCurrentUser();
        if (customer != null && productSerieParam !=null) {
			loadData();
            loadCustomer(customer);
		}else if (customer == null ) {
			initialize();
		}
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
            dtxExit.setValue(new Timestamp(new Date().getTime()));
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
		cbxExpiration.setChecked(false);
		cbxCure.setChecked(false);
		cbxSerialVarius.setChecked(false);
		txtSerial.setRawValue(null);
		txtBachNumber.setRawValue(null);
		txtUbicationFolder.setRawValue(null);
		txtUbicationBox.setRawValue(null);
		txtactNpNsn.setRawValue(null);
		txtDescription.setRawValue(null);
		txtPartNumber.setRawValue(null);
    	intStockMax.setRawValue(null);
    	intStockMin.setRawValue(null);
    	txtInvoice.setRawValue(null);
    	txtObservation.setRawValue(null);
    	intQuantity.setRawValue(null);
    	txtSerial.setRawValue(null);
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
    	dtxExpiration.setReadonly(true);
    	dtxCure.setReadonly(true);
    	dtxCreation.setReadonly(true);
    	dtxExpiration.setDisabled(true);
    	dtxCure.setDisabled(true);
    	dtxCreation.setDisabled(true);
    	txtSerial.setReadonly(true);
    	cmbCategory.setReadonly(true);
    	cmbCondition.setReadonly(true);
    	cmbProvider.setReadonly(true);
    	cmbCategory.setDisabled(true);
    	cmbCondition.setDisabled(true);
    	cmbProvider.setDisabled(true);
    	
    }

    public Boolean validateEmpty() {
    	if (txtUbicationFolder.getText().isEmpty()) {
        	txtUbicationFolder.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtUbicationBox.getText().isEmpty()) {
        	txtUbicationBox.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }if (txtDescription.getText().isEmpty()) {
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
        }
       
        
        
        else {
            return true;
        }
        return false;
    }


    public void onClick$btnSave() {
            switch (eventType) {
                case WebConstants.EVENT_DELETE:
                    saveProduct();
                    break;
                default:
                    break;
            }
    }

    

    public void onClick$btnBack() {
    	 Executions.sendRedirect("./listEgressTransit.zul");
    }
    
	public void loadData() {
		loadFields(productSerieParam);
		loadEnterprises(productSerieParam.getProduct().getEnterprise());
		loadCategory(productSerieParam.getCategory());
		loadProvider(productSerieParam.getProvider());
		loadCondition(productSerieParam.getCondition());
		loadCustomer(productSerieParam.getCustomer());
		blockFields();

	}

    
    public void loadFields(ProductSerie productSerie) {
    	
    	intStockMax.setValue(productSerie.getProduct().getStockMax());
    	intStockMin.setValue(productSerie.getProduct().getStockMin());
    	txtAmount.setText(String.valueOf(productSerie.getProduct().getAmount()));
    	if (productSerie.getProduct().getBatchNumber()!=null && !productSerie.getProduct().getBatchNumber().equals(""))
    		txtBachNumber.setText(productSerie.getProduct().getBatchNumber());
		txtUbicationFolder.setText(productSerie.getProduct().getUbicationFolder());
		txtUbicationBox.setText(productSerie.getProduct().getUbicationBox());
		if (productSerie.getProduct().getActNpNsn()!=null && !productSerie.getProduct().getActNpNsn().equals(""))
			txtactNpNsn.setText(productSerie.getProduct().getActNpNsn());
		txtDescription.setText(productSerie.getProduct().getDescription());
		txtPartNumber.setText(productSerie.getProduct().getPartNumber());
		txtSerial.setText(productSerie.getSerie());
		txtObservation.setText(productSerie.getObservation());
		
		if (productSerie.getExpirationDate()!=null) {
			dtxExpiration.setValue(productSerie.getExpirationDate());
		}
		if (productSerie.getCure()!=null) {
			dtxCure.setValue(productSerie.getCure());
		}
		dtxCreation.setValue(productSerie.getCreationDate());
		try {
    		int  quantity = transactionEJB.loadQuantityByProductId(productSerie.getProduct().getId(),productSerie.getCategory().getId());
    		intStock.setValue(quantity);
    	} catch (Exception ex) {
    		intStock.setValue(0);
        }
		intQuantity.setValue(productSerie.getQuantity());
		txtWorkOrder.setText(productSerie.getOrderWord());

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
                } else if(e.getId().equals(Category.TRANSIT)){
                	cmbCategory.setSelectedItem(cmbItem);
                }
            }
            cmbCategory.setReadonly(true);
            cmbCategory.setDisabled(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    


    private void loadCustomer(Customer customer) {
        try {
        	cmbCustomer.getItems().clear();
        	EJBRequest request = new EJBRequest();
        	customers = customerEJB.getCustomers(request);
        	 Comboitem cmbItem = new Comboitem();
             cmbItem.setLabel("Seleccione");
             cmbItem.setValue(null);
             cmbItem.setParent(cmbCustomer);
            for (Customer e : customers) {
                cmbItem = new Comboitem();
                cmbItem.setLabel(e.getFirstName() + " " + e.getLastName());
                cmbItem.setValue(e);
                cmbItem.setParent(cmbCustomer);
                if (customer != null && customer.getId().equals(e.getId())) {
                	cmbCustomer.setSelectedItem(cmbItem);
                }  
            }
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


    private void saveProduct() {
        Transaction transaction = new Transaction();
        try {

            transaction.setProduct(productSerieParam.getProduct());
            Category category = (Category) cmbCategory.getSelectedItem().getValue();
            transaction.setCategory(category);
			if (cmbCustomer.getSelectedItem() != null) {
				Customer customer = (Customer) cmbCustomer.getSelectedItem().getValue();
				transaction.setCustomer(customer);
			}else
				transaction.setCustomer(null);
            transaction.setUser(user);
            transaction.setCreationDate(new Timestamp(dtxExit.getValue().getTime()));
            TransactionType transactionType = transactionEJB.loadTransactionTypebyId(TransactionType.REMOVE);
            transaction.setTransactionType(transactionType);
            transaction.setAmount(Float.valueOf(txtAmount.getText()));
            productSerieParam.getProduct().setAmount(Float.valueOf(txtAmount.getText()));
            List<ProductSerie> productSeries = new ArrayList<ProductSerie>();
			productSerieParam.setEndingDate(new Timestamp(dtxExit.getValue().getTime()));
			transaction.setCondition(productSerieParam.getCondition());
			transaction.setProvider(productSerieParam.getProvider());
			transaction.setObservation(txtObservation.getText());
			transaction.setOrderWord(txtWorkOrder.getText());
			productSerieParam.setEndingTransactionId(transaction);
			productSerieParam.setOrderWord(txtWorkOrder.getText());
			productSerieParam.setObservation(txtObservation.getText());
			productSeries.add(productSerieParam);
    		transaction.setQuantity(intQuantity.getValue());
    		transaction = transactionEJB.saveEgressStock(transaction,productSeries);
    		this.showMessage(Labels.getLabel("sp.common.save.success"), false, null);
    		
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
	 public void onClick$btnSearch() {
    	 Window window = (Window)Executions.createComponents("catProducts.zul", null, null);
    	 Sessions.getCurrent().setAttribute("page","adminEgressInitTransit.zul");
         try {
			window.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	 public void onClick$btnSearchCustomer() {
    	 Window window = (Window)Executions.createComponents("catCustomers.zul", null, null);
    	 Sessions.getCurrent().setAttribute("page","adminEgressInitTransit.zul");
         try {
			window.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
   
    
} 
