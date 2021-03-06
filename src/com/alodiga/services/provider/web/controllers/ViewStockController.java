package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.Transaction;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class ViewStockController extends GenericAbstractAdminController {

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
    private Textbox txtInvoice;
    private Textbox txtObservation;
    private Textbox txtSerial;
    private Intbox intStockMax;
    private Intbox intStockMin;
    private Intbox intStock;
    private Intbox intQuantity;
    private Datebox dtxExpiration;
    private Datebox dtxCure;
    private Datebox dtxCreation;
    private Textbox txtForm;
    private byte[] form =	null;
    private String extForm = null;
    private String nameForm = null;
    private boolean uploaded = false;

    private ProductEJB productEJB = null;
    private UtilsEJB utilsEJB = null;
    private TransactionEJB transactionEJB = null;
    private ProductSerie productSerieParam;
    private List<Enterprise> enterprises;
    private List<Category> categories;
    private List<Provider> providers;
    private List<Condicion> conditions;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        productSerieParam = (Sessions.getCurrent().getAttribute("object") != null) ? (ProductSerie) Sessions.getCurrent().getAttribute("object") : null;
        eventType = WebConstants.EVENT_EDIT;
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
            dtxCreation.setValue(new Timestamp(new Date().getTime()));
            dtxExpiration.setValue(new Timestamp(new Date().getTime()));
            dtxCure.setValue(new Timestamp(new Date().getTime()));
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
    	if (cmbProvider.getText().isEmpty()) {
        	cmbProvider.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (dtxCreation.getText().isEmpty()) {
        	dtxCreation.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }else if (txtUbicationFolder.getText().isEmpty()) {
        	txtUbicationFolder.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtUbicationBox.getText().isEmpty()) {
        	txtUbicationBox.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if(txtDescription.getText().isEmpty()) {
        	txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null); 
        } else if (intStockMax.getText().isEmpty()) {
        	intStockMax.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (intStockMin.getText().isEmpty()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPartNumber.getText().isEmpty()) {
        	txtPartNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (intStockMin.getValue()>intStockMax.getValue()) {
        	intStockMin.setFocus(true);
            this.showMessage("sp.common.stock.min.error", true, null);
        } else if (!GeneralUtils.isNumeric(txtAmount.getText())) {
        	txtAmount.setFocus(true);
            this.showMessage("sp.error.field.number", true, null);
        } else if (intQuantity.getText().isEmpty()) {
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
    	Executions.sendRedirect("./listAddStock.zul");
    }
    
    public void onClick$viewDetail() {
    	Sessions.getCurrent().setAttribute("object",productSerieParam.getProduct());
    	Executions.sendRedirect("./listAddStock.zul");
    }
    
    public void loadData() {
    	Category category = new Category();
    	category.setId(Category.STOCK);
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
    	if (productSerie.getProduct().getBatchNumber()!=null && !productSerie.getProduct().getBatchNumber().equals(""))
    		txtBachNumber.setText(productSerie.getProduct().getBatchNumber());
		txtUbicationFolder.setText(productSerie.getProduct().getUbicationFolder());
		txtUbicationBox.setText(productSerie.getProduct().getUbicationBox());
		if (productSerie.getProduct().getActNpNsn()!=null && !productSerie.getProduct().getActNpNsn().equals(""))
			txtactNpNsn.setText(productSerie.getProduct().getActNpNsn());
		txtDescription.setText(productSerie.getProduct().getDescription());
		txtInvoice.setText(productSerie.getBeginTransactionId().getInvoice() );
		txtPartNumber.setText(productSerie.getProduct().getPartNumber());
		if (productSerie.getBeginTransactionId().getForm()!=null){
			
			form = productSerie.getBeginTransactionId().getForm();

			txtForm.setText(productSerie.getBeginTransactionId().getNameForm());
			txtForm.setReadonly(true);
		    extForm = productSerie.getBeginTransactionId().getExtensionForm();
		    nameForm = productSerie.getBeginTransactionId().getNameForm();
		}
		try {
    		int  quantity = transactionEJB.loadQuantityByProductId(productSerie.getProduct().getId(),productSerie.getCategory().getId());
    		intStock.setValue(quantity);
    	} catch (Exception ex) {
    		intStock.setValue(0);
        }
		intQuantity.setValue(productSerie.getQuantity());
		dtxCreation.setValue(productSerie.getCreationDate());
		if (productSerie.getExpirationDate()!=null) {
			cbxExpiration.setChecked(true);
			dtxExpiration.setValue(productSerie.getExpirationDate());
		}else {
			cbxExpiration.setChecked(false);
			dtxExpiration.setVisible(false);
		}if (productSerie.getCure()!=null) {
			cbxCure.setChecked(true);
			dtxCure.setValue(productSerie.getCure());
		}else{
			cbxCure.setChecked(false);
			dtxCure.setVisible(false);
		}
		txtObservation.setText(productSerie.getObservation());
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
                } else {
                	cmbProvider.setSelectedIndex(0);
                }
            }
        } catch (EmptyListException ex) {
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
			Condicion condition = (Condicion) cmbCondition.getSelectedItem().getValue();
			transaction.setCondition(condition);
			Provider provider = (Provider) cmbProvider.getSelectedItem().getValue();
			transaction.setProvider(provider);
			transaction.setObservation(txtObservation.getText());
			transaction.setInvoice(txtInvoice.getText());
			
			if (uploaded) {
	           	transaction.setForm(form);
	           	transaction.setExtForm(extForm);
	           	transaction.setNameForm(nameForm);
	        }else if(!uploaded && form==null){
	        	transaction.setForm(null);
	           	transaction.setExtForm(null);
	           	transaction.setNameForm(null);
	        }
			
			productSerie.setProvider(provider);
			productSerie.setAmount(Float.valueOf(txtAmount.getText()));
			productSerie.setQuantity(intQuantity.getValue());
			productSerie.setCondition(condition);
			productSerie.setSerie(txtSerial.getText());
			productSerie.setObservation(txtObservation.getText());
			productSerie.setCreationDate(new Timestamp(dtxCreation.getValue().getTime()));
			if (cbxExpiration.isChecked())
				productSerie.setExpirationDate(new Timestamp(dtxExpiration.getValue().getTime()));
			if (cbxCure.isChecked())
				productSerie.setCure(new Timestamp(dtxCure.getValue().getTime()));
    		transaction = transactionEJB.modificarStock(transaction, productSerie);
    		AccessControl.saveAction(Permission.EDIT_STOCK, "Se edito producto en stock = " + productSerie.getProduct().getPartNumber() + " la cantidad de:" + intQuantity.getValue()+" numero de serie"+productSerie.getSerie()!=null?productSerie.getSerie():"");
    		this.showMessage(Labels.getLabel("sp.common.save.success"), false, null);
    		
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
    
    public void onClick$btnDownload() throws InterruptedException {
        try {
            if (form!=null){
    			Filedownload.save(form, extForm, nameForm);
            }
        } catch (Exception ex) {
            showError(ex);
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
    
    
} 
