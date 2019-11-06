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
import org.zkoss.zul.Button;
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
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.QuarantineStatus;
import com.alodiga.services.provider.commons.models.Transaction;
import com.alodiga.services.provider.commons.models.TransactionType;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;

public class AdminEgressUnitQuarantineController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbCustomer;
    private Combobox cmbProvider;
    private Combobox cmbCondition;
    private Combobox cmbStatus;
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
    private Textbox txtQuarantine;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Textbox txtSerial;
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
    private List<QuarantineStatus> quarantineStatus;
    private List<Provider> providers;
    private List<Condicion> conditions;
    private User user;
    private Customer customer = null;
    private Button btnSave;
    
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
		txtQuarantine.setRawValue(null);
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
    	cmbProvider.setDisabled(true);
    }

    public Boolean validateEmpty() {
    	if (cmbStatus.getText().isEmpty()) {
        	cmbStatus.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null); 
        } else {
            return true;
        }
        return false;
    }


	public void onClick$btnSave() {
		if (validateEmpty())
			saveProduct();
	}
   

    public void onClick$btnBack() {
    	 Executions.sendRedirect("./listEgressQuarantine.zul");
    }
    
	public void loadData() {
		loadFields(productSerieParam);
		loadEnterprises(productSerieParam.getProduct().getEnterprise());
		loadCategory(productSerieParam.getCategory());
		loadProvider(productSerieParam.getProvider());
		loadCondition(productSerieParam.getCondition());
		loadCustomer(productSerieParam.getCustomer());
		loadStatus();
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
		txtPartNumber.setReadonly(true);
		txtQuarantine.setText(productSerie.getQuarantineReason());
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
                } else if(e.getId().equals(Category.QUARANTINE)){
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
            if (!customers.isEmpty())
            	cmbCustomer.setSelectedIndex(1);
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    private void loadStatus() {
        try {
        	cmbStatus.getItems().clear();
        	quarantineStatus = utilsEJB.getQuaratineStatus();
        	 Comboitem cmbItem = new Comboitem();
             cmbItem.setLabel("Seleccione");
             cmbItem.setValue(null);
             cmbItem.setParent(cmbStatus);
            for (QuarantineStatus status : quarantineStatus) {
                cmbItem = new Comboitem();
                cmbItem.setLabel(status.getName());
                cmbItem.setValue(status);
                cmbItem.setParent(cmbStatus);
              
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
        } catch (EmptyListException ex) {
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
            Customer customer = null;
            if (cmbCustomer.getSelectedItem()!=null)
            	customer = (Customer) cmbCustomer.getSelectedItem().getValue();
            transaction.setCustomer(customer);
            transaction.setUser(user);
            transaction.setCreationDate(new Timestamp(dtxExit.getValue().getTime()));
            TransactionType transactionType = transactionEJB.loadTransactionTypebyId(TransactionType.EXIT);
            transaction.setTransactionType(transactionType);
            transaction.setAmount(Float.valueOf(txtAmount.getText()));
            productSerieParam.getProduct().setAmount(Float.valueOf(txtAmount.getText()));
            List<ProductSerie> productSeries = new ArrayList<ProductSerie>();
			productSerieParam.setEndingDate(new Timestamp(dtxExit.getValue().getTime()));
			transaction.setCondition(productSerieParam.getCondition());
			transaction.setProvider(productSerieParam.getProvider());
			transaction.setObservation(txtObservation.getText());
			transaction.setQuarantineReason(txtQuarantine.getText());
			productSerieParam.setQuarantineReason(txtQuarantine.getText());
			productSerieParam.setEndingTransactionId(transaction);
			productSerieParam.setObservation(txtObservation.getText());
			QuarantineStatus status = (QuarantineStatus) cmbStatus.getSelectedItem().getValue();
			productSerieParam.setQuarantineStatus(status);
			productSeries.add(productSerieParam);
    		transaction.setQuantity(intQuantity.getValue());
    		transaction = transactionEJB.saveEgressStock(transaction,productSeries);
    		this.showMessage(Labels.getLabel("sp.common.save.success"), false, null);
    		btnSave.setVisible(false);
    		AccessControl.saveAction(Permission.REMOVE_QUARANTINE, "Extraer producto de quarentena = " + productSerieParam.getProduct().getPartNumber() + " la cantidad de:" + intQuantity.getValue());
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
	 public void onClick$btnSearch() {
    	 Sessions.getCurrent().setAttribute("page","adminEgressInitQuarantine.zul");
    	 Category category =(Category) cmbCategory.getSelectedItem().getValue();
    	 Sessions.getCurrent().setAttribute("category",category);
    	 Window window = (Window)Executions.createComponents("catProducts.zul", null, null);
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
    	 Sessions.getCurrent().setAttribute("page","adminEgressInitQuarantine.zul");
         try {
			window.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	 
	 
	 public void onClick$btnRemove() {
		 cmbCustomer.setSelectedItem(null);
		 cmbCustomer.setValue(null);
		 cmbCustomer.setText("");
	}
   
    
} 
