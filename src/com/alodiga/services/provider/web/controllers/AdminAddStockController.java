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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
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
//    private Combobox cmbCustomer;
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
    private Textbox txtSerial;
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Textbox txtForm;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intStock;
    private Intbox intQuantity;
    private Datebox dtxExpiration;
    private Datebox dtxCure;
    private Radio ra1;
    private Radio ra2;
    private Radio ra3;
    private Row rowSerial;
    private Row rowSerials;
    private Grid gridSerials;
    private Rows rows;
    private byte[] form =	null;
    private String extForm = null;
    private String nameForm = null;
    private boolean uploaded = false;
    private Button btnSave; 
//    private Product product;

    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private CustomerEJB customerEJB = null;
    private Product productParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private List<Provider> providers;
    private List<Condicion> conditions;
    private List<Customer> customers;
    private Provider provider;
    private User user;
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Product) Sessions.getCurrent().getAttribute("object") : null;
        provider  = (Sessions.getCurrent().getAttribute("provider") != null) ? (Provider) Sessions.getCurrent().getAttribute("provider") : null;
        productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
        utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
        transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
        customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
        user = AccessControl.loadCurrentUser();
        eventType =WebConstants.EVENT_ADD;
        if (provider != null && productParam !=null) {
			loadFields(productParam!=null?productParam:null);
            loadEnterprises(productParam!=null?productParam.getEnterprise():null);
            loadCondition(null);
            loadCategory(null);
            loadProvider(provider);
//            loadCustomer(null);
            blockFields();
		}else if (provider == null ) {
			initialize();
		}else
			loadProvider(provider);
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

            dtxExpiration.setValue(new Timestamp(new Date().getTime()));
            dtxCure.setValue(new Timestamp(new Date().getTime()));
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
		cbxExpiration.setChecked(false);
		cbxCure.setChecked(false);
		cbxSerialVarius.setChecked(false);
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

    	intStock.setReadonly(true);
		txtDescription.setReadonly(true);
		txtPartNumber.setReadonly(true);
    }

    public Boolean validateEmpty() {
    	 if (txtUbicationFolder.getText().isEmpty()) {
        	txtUbicationFolder.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } if (txtUbicationBox.getText().isEmpty()) {
        	txtUbicationBox.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }  if (txtDescription.getText().isEmpty()) {
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
    	 Sessions.getCurrent().removeAttribute("provider");
    	 Executions.sendRedirect("./listStock.zul");
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
            	loadFields(productParam!=null?productParam:null);
                loadEnterprises(productParam!=null?productParam.getEnterprise():null);
                loadCondition(null);
                loadCategory(null);
                loadProvider(null);
//                loadCustomer(null);
                blockFields();
                break;
            default:
                break;
        }
    }

    
    public void loadFields(Product product) {
		if (product != null) {
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
				int quantity = transactionEJB.loadQuantityByProductId(product.getId(), Category.STOCK);
				intStock.setValue(quantity);
			} catch (Exception ex) {
				intStock.setValue(0);
			}
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
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

//    private void loadCustomer(Customer customer) {
//        try {
//        	cmbCustomer.getItems().clear();
//        	EJBRequest request = new EJBRequest();
//        	customers = customerEJB.getCustomers(request);
//            for (Customer e : customers) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(e.getFirstName() + " " + e.getLastName());
//                cmbItem.setValue(e);
//                cmbItem.setParent(cmbCustomer);
//                if (customer != null && customer.getId().equals(e.getId())) {
//                	cmbCustomer.setSelectedItem(cmbItem);
//                } else {
//                	cmbCustomer.setSelectedIndex(0);
//                }
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }

    private void saveProduct(Transaction _transaction) {
        Transaction transaction = new Transaction();
        try {

            if (_transaction != null) 
            	transaction.setId(_transaction.getId());
            Category category =(Category) cmbCategory.getSelectedItem().getValue();
            transaction.setCategory(category);
            Condicion condition =(Condicion) cmbCondition.getSelectedItem().getValue();
            transaction.setCondition(condition);
            transaction.setCustomer(null);
            Provider provider = (Provider) cmbProvider.getSelectedItem().getValue();
            transaction.setProvider(provider);
            transaction.setUser(user);
            transaction.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            transaction.setQuantity(intQuantity.getValue());
            TransactionType transactionType = transactionEJB.loadTransactionTypebyId(TransactionType.ADD);
            transaction.setTransactionType(transactionType);
            transaction.setAmount(Float.valueOf(txtAmount.getText()));
            transaction.setInvoice(txtInvoice.getText());
            if (uploaded) {
            	transaction.setForm(form);
            	transaction.setExtForm(extForm);
            	transaction.setNameForm(nameForm);
            }
            productParam.setInictialAmount(productParam.getAmount());
            productParam.setAmount(Float.valueOf(txtAmount.getText()));
            productParam.setActNpNsn(txtactNpNsn.getText());
            productParam.setBatchNumber(txtBachNumber.getText());
            productParam.setRealAmount(Float.valueOf(txtAmount.getText()));
            productParam.setUbicationBox(txtUbicationBox.getText());
            productParam.setUbicationFolder(txtUbicationFolder.getText());
            productParam.setStockMin(intStockMin.getValue());
            productParam.setStockMax(intStockMax.getValue());
            transaction.setProduct(productParam);
			List<ProductSerie> productSeries = new ArrayList<ProductSerie>();
			if (ra2.isChecked()) {
				for (int i = 0; i < intQuantity.getValue(); i++) {
					ProductSerie productSerie = new ProductSerie();
					productSerie.setProduct(productParam);
					productSerie.setProvider(provider);
					productSerie.setBeginTransactionId(transaction);
					productSerie.setCreationDate(new Timestamp((new java.util.Date().getTime())));
					productSerie.setAmount(Float.valueOf(txtAmount.getText()));
					productSerie.setQuantity(1);
					productSerie.setQuantityInto(1);
					productSerie.setCondition(condition);
					productSerie.setCategory(category);
					Row row = (Row) gridSerials.getRows().getChildren().get(i);
					Textbox textbox = (Textbox) row.getChildren().get(0);
					if (textbox.getText().isEmpty())
						throw  new NullParameterException("Serial vacio");
					productSerie.setSerie(textbox.getText());
					if (cbxExpiration.isChecked())
						productSerie.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
					if (cbxCure.isChecked())
						productSerie.setCure(new Timestamp(dtxCure.getValue().getTime()));
					productSeries.add(productSerie);
				}
			} else {
				ProductSerie productSerie = new ProductSerie();
				productSerie.setProduct(productParam);
				productSerie.setProvider(provider);
				productSerie.setBeginTransactionId(transaction);
				productSerie.setCreationDate(new Timestamp((new java.util.Date().getTime())));
				productSerie.setAmount(Float.valueOf(txtAmount.getText()));
				productSerie.setQuantity(intQuantity.getValue());
				productSerie.setQuantityInto(intQuantity.getValue());
				productSerie.setCondition(condition);
				productSerie.setCategory(category);
				if (ra1.isChecked()) {
					if (txtSerial.getText().isEmpty())
						throw  new NullParameterException("Serial vacio");
					productSerie.setSerie(txtSerial.getText());
				}
				if (cbxExpiration.isChecked())
					productSerie.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
				if (cbxCure.isChecked())
					productSerie.setCure(new Timestamp(dtxCure.getValue().getTime()));
				productSeries.add(productSerie);
			}

            transaction = transactionEJB.saveTransactionStock(transaction,productSeries);
            AccessControl.saveAction(Permission.ADD_STOCK, "Agrego producto a stock = " + productParam.getPartNumber() + " la cantidad de:" + intQuantity.getValue());
            this.showMessage("sp.common.save.success", false, null);
            btnSave.setVisible(false);
        } catch (NullParameterException ex) {
        	showMessage("sp.error.field.number", true, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onCheck$cbxExpiration(){
    	if (cbxExpiration.isChecked())
    		dtxExpiration.setVisible(true);
    	else
    		dtxExpiration.setVisible(false);
    }
    
    public void onCheck$cbxCure(){
    	if (cbxCure.isChecked())
    		dtxCure.setVisible(true);
    	else
    		dtxCure.setVisible(false);
    }
    
    
    public void onCheck$radiogroup(){
    	if (intQuantity.getText().isEmpty()) {
    		ra1.setChecked(false);
    		ra2.setChecked(false);
    		intQuantity.setFocus(true);
    		 this.showMessage("sp.error.field.cannotNull", true, null);
		} else {
			if (ra1.isChecked()) {
				rowSerial.setVisible(true);
				rowSerials.setVisible(false);
			} else if (ra2.isChecked()) {
				rows.getChildren().clear();
				rows.setParent(gridSerials);
				for (int i = 0; i < intQuantity.getValue(); i++) {
					Row row = new Row();
					row.setHeight("40px");
					Textbox textbox = new Textbox();
					textbox.setParent(row);
					row.setParent(rows);
				}
				rowSerials.setVisible(true);
				rowSerial.setVisible(false);
			}else if (ra3.isChecked()) {
				rowSerials.setVisible(false);
				rowSerial.setVisible(false);
			}
		}
    }
    
	public void onChange$intQuantity() {
		if (ra1.isChecked()) {
			rowSerial.setVisible(true);
			rowSerials.setVisible(false);
		} else if (ra2.isChecked()) {
			rows.getChildren().clear();
			rows.setParent(gridSerials);
			for (int i = 0; i < intQuantity.getValue(); i++) {
				Row row = new Row();
				row.setHeight("40px");
				Textbox textbox = new Textbox();
				textbox.setParent(row);
				row.setParent(rows);
			}
			rowSerials.setVisible(true);
			rowSerial.setVisible(false);
		} else if (ra3.isChecked()) {
			rowSerials.setVisible(false);
			rowSerial.setVisible(false);
		}

	}
	
	 public void onUpload$btnPPNSubmitFile(org.zkoss.zk.ui.event.UploadEvent event) throws Throwable {
	    org.zkoss.util.media.Media media = event.getMedia();
	        
		if (media != null) {
			if (validateFormatFile(media)) {
				txtForm.setText(media.getName());
				media.getFormat();
				form = media.getByteData();
				extForm = media.getFormat();
				nameForm = 	media.getName();	
				uploaded = true;

			}
		}else {
			showError(Labels.getLabel("sp.error.fileupload.invalid.file"));
        }
	}
	 
	private boolean validateFormatFile(org.zkoss.util.media.Media media) throws InterruptedException {
		if (!media.getFormat().equals("png") && !media.getFormat().equals("jpg") && !media.getFormat().equals("jpeg")&& !media.getFormat().equals("pdf")&& !media.getFormat().equals("jpeg")
				&& !media.getFormat().equals("xlsx")&& !media.getFormat().equals("docx")&& !media.getFormat().equals("xls")&& !media.getFormat().equals("doc")) {
			Messagebox.show(Labels.getLabel("sp.error.fileupload.invalid.format"), "Advertencia", 0,Messagebox.EXCLAMATION);
			return false;
		}
		return true;
	}
	
	
    public void onClick$btnClear() {
   	 	txtForm.setText("");
   	 	form = null;
		uploaded = false;
    }
    
    public void onClick$btnSearch() {
   	 Window window = (Window)Executions.createComponents("catProducts.zul", null, null);
   	 Sessions.getCurrent().setAttribute("page","adminAddStock.zul");
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

	public void onClick$btnSearchProvider() {
		Window window = (Window) Executions.createComponents("catProviders.zul", null, null);
		Sessions.getCurrent().setAttribute("page", "adminAddStock.zul");
		try {
			window.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
    public void onClick$btnRemove() {
		 cmbProvider.setSelectedItem(null);
		 cmbProvider.setValue(null);
		 cmbProvider.setText("");
	 }
}