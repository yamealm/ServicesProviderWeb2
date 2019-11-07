package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Transaction;
import com.alodiga.services.provider.commons.models.TransactionType;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminEgressStockController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Combobox cmbEnterprise;
    private Combobox cmbCategory;
    private Combobox cmbCustomer;

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
    private Textbox txtWorkOrder;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intStock;
    private Datebox dtxExit;
    private Listbox lbxRecords;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private CustomerEJB customerEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
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
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            dtxExit.setValue(new Timestamp(new Date().getTime()));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
		cbxExpiration.setChecked(false);
		cbxCure.setChecked(false);
		cbxSerialVarius.setChecked(false);
//		intQuantity.setRawValue(null);
		txtBachNumber.setRawValue(null);
		txtUbicationFolder.setRawValue(null);
		txtUbicationBox.setRawValue(null);
		txtWorkOrder.setRawValue(null);
		txtactNpNsn.setRawValue(null);
		txtDescription.setRawValue(null);
		txtPartNumber.setRawValue(null);
    	intStockMax.setRawValue(null);
    	intStockMin.setRawValue(null);
    	txtInvoice.setRawValue(null);
    	txtObservation.setRawValue(null);
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
//    	intQuantity.setReadonly(true);
    }

    public Boolean validateEmpty() {
    	if (txtUbicationFolder.getText().isEmpty()) {
        	txtUbicationFolder.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtUbicationBox.getText().isEmpty()) {
        	txtUbicationBox.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtDescription.getText().isEmpty()) {
        	txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null); 
        } else if (intStockMax.getText().isEmpty()) {
        	intStockMax.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (intStockMin.getText().isEmpty()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (intStockMin.getValue()>intStockMax.getValue()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.common.stock.min.error", true, null);
        }else if (!GeneralUtils.isNumeric(txtAmount.getText())) {
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
                    saveProduct(null);
                    break;
                default:
                    break;
            }
    }

    

    public void onClick$btnBack() {
    	 Executions.sendRedirect("./listStock.zul");
    }
    
    public void loadData() {
    	Category category = new Category();
    	category.setId(Category.STOCK);
        switch (eventType) {
            case WebConstants.EVENT_DELETE:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                loadCategory(category);
                loadCustomer(null);
                blockFields();
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                blockFields();
                break;
            case WebConstants.EVENT_ADD:
            	loadFields(productParam);
                loadEnterprises(productParam.getEnterprise());
                loadCategory(category);
                loadCustomer(null);
                blockFields();
                break;
            default:
                break;
        }
    }

    
    public void loadFields(Product product) {
    	
    	intStockMax.setValue(product.getStockMax());
    	intStockMin.setValue(product.getStockMin());
    	txtAmount.setText(String.valueOf(product.getAmount()));
    	if (product.getBatchNumber()!=null && !product.getBatchNumber().equals(""))
			txtBachNumber.setText(product.getBatchNumber());
		txtUbicationFolder.setText(product.getUbicationFolder());
		txtUbicationBox.setText(product.getUbicationBox());
		if (product.getActNpNsn()!=null && !product.getActNpNsn().equals(""))
			txtactNpNsn.setText(product.getActNpNsn());
		txtDescription.setText(product.getDescription());
		txtPartNumber.setText(product.getPartNumber());
		txtPartNumber.setReadonly(true);
		try {
    		int  quantity = transactionEJB.loadQuantityByProductId(product.getId(), Category.STOCK);
    		intStock.setValue(quantity);
    	} catch (Exception ex) {
    		intStock.setValue(0);
        }
		try {
    		List<ProductSerie>  productSeries = transactionEJB.searchProductSerieByProductId(product.getId(), Category.STOCK);
    		loadList(productSeries);
    	} catch (Exception ex) {
    		
        }
		
    }
    
    private Listcell addIntbox( final int quantity, final Listitem listItem) {

        Listcell cell = new Listcell();
        Intbox textbox = new Intbox();
        textbox.setWidth("100px");
        cell.appendChild(textbox);
        textbox.setTooltiptext(Labels.getLabel("sp.common.quantity"));
        textbox.addEventListener("onChange", new EventListener() {

            public void onEvent(Event event) throws Exception {
            	if(textbox.getValue()> quantity) {
            		textbox.setFocus(true);
            		textbox.setText("");
            		textbox.setErrorMessage(Labels.getLabel("sp.error.value.quantity"));
            	}
//            	else
//            		intQuantity.setValue(textbox.getValue());
            }
        });

        textbox.setParent(cell);
        return cell;
    }
    
    public void loadList(List<ProductSerie> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (ProductSerie productSerie : list) {
                    item = new Listitem();
                    item.setValue(productSerie);
                    item.appendChild(new Listcell(productSerie.getSerie()));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    item.appendChild(new Listcell(productSerie.getCondition().getName()));
                    String dateExp = null;
					if (productSerie.getExpirationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						dateExp = df.format(productSerie.getExpirationDate().getTime());
					}
                    item.appendChild(new Listcell(dateExp));
                    item.appendChild(new Listcell(String.valueOf(productSerie.getQuantity())));
                    item.appendChild(addIntbox(productSerie.getQuantity(),item));
                    
                    
                    item.setParent(lbxRecords);
                }
            } else {
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

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
        } catch (EmptyListException ex) {
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
            Category category =(Category) cmbCategory.getSelectedItem().getValue();
            transaction.setCategory(category);
            Customer customer = null;
            if (cmbCustomer.getSelectedItem()!=null)
            	customer = (Customer) cmbCustomer.getSelectedItem().getValue();
            transaction.setCustomer(customer);
            transaction.setUser(user);
            transaction.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            TransactionType transactionType = transactionEJB.loadTransactionTypebyId(TransactionType.EXIT);
            transaction.setTransactionType(transactionType);
            transaction.setAmount(Float.valueOf(txtAmount.getText()));
            transaction.setOrderWord(txtWorkOrder.getText());
            productParam.setAmount(Float.valueOf(txtAmount.getText()));
            List<ProductSerie> productSeries = new ArrayList<ProductSerie>();
            int totalQuantity = 0;
            List<Listitem> listitems = lbxRecords.getItems();
    		for (Listitem lml: listitems){
    			Listcell cell = (Listcell) lml.getChildren().get(5);
    			Intbox intVal = (Intbox)(cell.getChildren().get(0));
				if (intVal.getValue() != null) {
					int quantity = intVal.getValue();
					totalQuantity = totalQuantity + quantity;
					ProductSerie productSerie = (ProductSerie) lml.getValue();
					ProductSerie productSerie2 =(ProductSerie)productSerie.clone();
					productSerie.setEndingDate(new Timestamp(dtxExit.getValue().getTime()));
					int oldQuantity = productSerie.getQuantity();
					productSerie.setQuantity(quantity);
					transaction.setCondition(productSerie.getCondition());
					transaction.setProvider(productSerie.getProvider());
					productSerie.setEndingTransactionId(transaction);
					productSerie.setOrderWord(txtWorkOrder.getText());
					productSeries.add(productSerie);
					if ((oldQuantity - quantity) > 0) {
						productSerie2.setId(null);
						productSerie2.setQuantity(oldQuantity - quantity);
						productSerie2.setBeginTransactionId(transaction);
						productSeries.add(productSerie2);
						productSerie.setEndingTransactionId(transaction);
					}
				}
			}
    		if (totalQuantity>0) {
    			transaction.setQuantity(totalQuantity);
    			transaction = transactionEJB.saveEgressStock(transaction,productSeries);
    			AccessControl.saveAction(Permission.REMOVE_STOCK, "Extraer producto de stock = " + productParam.getPartNumber() + " la cantidad de:" + totalQuantity);
    			this.showMessage(Labels.getLabel("sp.common.save.success"), false, null);
    			btnSave.setVisible(false);
    		}else
    			 showError(Labels.getLabel("sp.error.validate.transaction"));
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
   
    
} 
