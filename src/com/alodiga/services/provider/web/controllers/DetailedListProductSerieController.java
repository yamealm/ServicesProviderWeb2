package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.Condicion;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.TransactionType;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;

public class DetailedListProductSerieController extends GenericAbstractListController<ProductSerie> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Combobox cmbProvider;
    private Combobox cmbCustomer;
    private Combobox cmbCondition;
    private Combobox cmbTransactionType;
    private Button btnExportPdf;
    private Combobox cmbProduct;
    private Datebox dtbBeginningDate;
    private Datebox dtbEndingDate;
    private Datebox dtbBeginningDateExit;
    private Datebox dtbEndingDateExit;
    private Textbox txtWorkOrder;
    private Textbox txtPartNumber;
    private UtilsEJB utilsEJB = null;
    private CustomerEJB customerEJB = null;
    private ProductEJB productEJB = null;
    private TransactionEJB transactionEJB = null;
    Boolean isStoreAll = false;
    List<ProductSerie> productSeries = null;
    private Label lblInfo;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void startListener() {
        
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            adminPage = "adminTransaction.zul";
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            getData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        lblInfo.setValue("");
        lblInfo.setVisible(false);
    }

    public void onClick$btnSearch() {
        try {
            clearFields();
            clearMessage();
            EJBRequest _request = new EJBRequest();
            Map<String, Object> params = new HashMap<String, Object>();
        	if (dtbEndingDate.getValue() != null && dtbBeginningDate.getValue() != null) {
				params.put(QueryConstants.PARAM_BEGINNING_DATE, dtbBeginningDate.getValue());
				params.put(QueryConstants.PARAM_ENDING_DATE, dtbEndingDate.getValue());
				if (dtbEndingDate.getValue().getTime() >= dtbBeginningDate.getValue().getTime()) {

				} else {
					this.showMessage("sp.error.date.invalid", true, null);
				}
			}
        	if (dtbEndingDateExit.getValue() != null && dtbBeginningDateExit.getValue() != null) {
 				params.put(QueryConstants.PARAM_BEGINNING_DATE_EXIT, dtbBeginningDateExit.getValue());
 				params.put(QueryConstants.PARAM_ENDING_DATE_EXIT, dtbEndingDateExit.getValue());
 				if (dtbEndingDateExit.getValue().getTime() >= dtbBeginningDateExit.getValue().getTime()) {

 				} else {
 					this.showMessage("sp.error.date.invalid", true, null);
 				}
 			}
            params.put(QueryConstants.PARAM_CATEGORY_ID, Category.STOCK);
              
			if (cmbProvider.getSelectedItem() != null && cmbProvider.getSelectedIndex() != 0) {
				params.put(QueryConstants.PARAM_PROVIDER_ID,((Provider) cmbProvider.getSelectedItem().getValue()).getId());
			}
			if (cmbProduct.getSelectedItem() != null && cmbProduct.getSelectedIndex() != 0) {
				params.put(QueryConstants.PARAM_PRODUCT_ID,	((Product) cmbProduct.getSelectedItem().getValue()).getId());
			}
			if (cmbCustomer.getSelectedItem() != null && cmbCustomer.getSelectedIndex() != 0) {
				params.put(QueryConstants.PARAM_CUSTOMER_ID,((Customer) cmbCustomer.getSelectedItem().getValue()).getId());
			}
			if (cmbCustomer.getSelectedItem() != null && cmbCustomer.getSelectedIndex() != 0) {
				params.put(QueryConstants.PARAM_CUSTOMER_ID,((Customer) cmbCustomer.getSelectedItem().getValue()).getId());
			}
			if (cmbCondition.getSelectedItem() != null && cmbCondition.getSelectedIndex() != 0) {
				params.put(QueryConstants.PARAM_CONDITION_ID,((Condicion) cmbCondition.getSelectedItem().getValue()).getId());
			}
			if (cmbTransactionType.getSelectedItem() != null) {
				params.put(QueryConstants.PARAM_TRANSACTION_TYPE_ID,((TransactionType) cmbTransactionType.getSelectedItem().getValue()).getId());
			}
			if (txtWorkOrder.getText() != null && !txtWorkOrder.getText().equals("") && !txtWorkOrder.getText().isEmpty()) {
				params.put(QueryConstants.PARAM_WORK_ORDER, txtWorkOrder.getText());
			}
			if (txtPartNumber.getText() != null && !txtPartNumber.getText().equals("") && !txtPartNumber.getText().isEmpty()) {
				params.put(QueryConstants.PARAM_PART_NUMBER, txtPartNumber.getText());
			}
			_request.setParams(params);
			_request.setParam(true);
			TransactionType ttype = (TransactionType) cmbTransactionType.getSelectedItem().getValue();
			productSeries = productEJB.searchProductSerie(_request);
			loadList(productSeries,ttype.getId().equals(TransactionType.ENTRY));
			AccessControl.saveAction(Permission.STOCK, "Se busco listado de productos en stock");
		} catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            loadList(null,false);
        }
    }

    private void loadProvider() {
        try {
        	cmbProvider.getItems().clear();
            List<Provider> providers = utilsEJB.getProvider();
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbProvider);
            cmbProvider.setSelectedItem(item);
            for (int i = 0; i < providers.size(); i++) {
                item = new Comboitem();
                item.setValue(providers.get(i));
                item.setLabel(providers.get(i).getName());
                item.setParent(cmbProvider);
            }
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            this.showError(ex);
        }

    }
    
    private void loadCustomer() {
        try {
        	cmbCustomer.getItems().clear();
            List<Customer> customers = customerEJB.getCustomers(request);
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbCustomer);
            cmbCustomer.setSelectedItem(item);
            for (int i = 0; i < customers.size(); i++) {
                item = new Comboitem();
                item.setValue(customers.get(i));
                item.setLabel(customers.get(i).getFirstName());
                item.setParent(cmbCustomer);
            }
        }catch (EmptyListException ex) {
        }  catch (Exception ex) {
            this.showError(ex);
        }

    }
    
    private void loadCondition() {
        try {
        	cmbCondition.getItems().clear();
            List<Condicion> condicions = transactionEJB.getConditions();
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbCondition);
            cmbCondition.setSelectedItem(item);
            for (int i = 0; i < condicions.size(); i++) {
                item = new Comboitem();
                item.setValue(condicions.get(i));
                item.setLabel(condicions.get(i).getName());
                item.setParent(cmbCondition);
            }
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            this.showError(ex);
        }

    }
    
    private void loadTransactionType() {
        try {
        	cmbTransactionType.getItems().clear();
            List<TransactionType> transactionTypes = transactionEJB.getTransactionTypes();
            for (int i = 0; i < transactionTypes.size(); i++) {
            	Comboitem item = new Comboitem();
                item.setValue(transactionTypes.get(i));
                if (transactionTypes.get(i).getId().equals(TransactionType.ENTRY)) {
                	item.setLabel(Labels.getLabel("sp.common.entry"));
                }else {
                	item.setLabel(Labels.getLabel("sp.common.exit"));
                }
                item.setParent(cmbTransactionType);
                cmbTransactionType.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            this.showError(ex);
        }

    }
    
    private void loadProduct() {
        try {
        	cmbProduct.getItems().clear();
        	EJBRequest request2 = new EJBRequest();
            List<Product> products = productEJB.getProducts(request2);
            Comboitem item = new Comboitem();
            item.setLabel(Labels.getLabel("sp.common.all"));
            item.setParent(cmbProduct);
            cmbProduct.setSelectedItem(item);
            for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getCategory().getId().equals(Category.STOCK)) {
					item = new Comboitem();
					item.setValue(products.get(i));
					item.setLabel(products.get(i).getDescription());
					item.setParent(cmbProduct);
				}
            }
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            this.showError(ex);
        }

    }


    public void loadList(List<ProductSerie> list, boolean entry) {
        try {
            lbxReport.getItems().clear();
            
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (ProductSerie productSerie : list) {
                    item = new Listitem();
                    item.setHeight("10px");
                    item.setValue(productSerie);
                    item.appendChild(new Listcell(productSerie.getProduct().getPartNumber()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    int quantity = 0;
                    if (entry) {
                    	item.appendChild(new Listcell(Labels.getLabel("sp.common.entry")));
                    	quantity = productSerie.getQuantityInto();
                    }else {
                    	item.appendChild(new Listcell(Labels.getLabel("sp.common.exit")));
                    	quantity = productSerie.getQuantity();
                    }
                    item.appendChild(new Listcell(productSerie.getCondition().getName()));
                    item.appendChild(new Listcell(productSerie.getSerie()));
                    item.appendChild(new Listcell(!entry?productSerie.getOrderWord():null));
                    item.appendChild(new Listcell(productSerie.getAmount().toString()));
                    item.appendChild(new Listcell(String.valueOf(quantity)));
                    String date = null;
					if (productSerie.getCreationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getCreationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    date = null;
                    if (productSerie.getExpirationDate() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getExpirationDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    date = null;
                    if (productSerie.getCure() != null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getCure().getTime());
					}
                    item.appendChild(new Listcell(date));
                    date = null;
                    if (!entry && productSerie.getEndingDate()!=null) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						date = df.format(productSerie.getEndingDate().getTime());
					}
                    item.appendChild(new Listcell(date));
                    item.setParent(lbxReport);
                }
                btnDownload.setVisible(true);
                btnExportPdf.setVisible(true);
            } else {
                btnDownload.setVisible(false);
                btnExportPdf.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell());
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxReport);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        cmbProvider.setSelectedIndex(0);
        cmbCustomer.setSelectedIndex(0);
        cmbCondition.setSelectedIndex(0);
        cmbTransactionType.setSelectedIndex(0);
        Date date = new Date();
        dtbBeginningDate.setValue(date);
        dtbEndingDate.setValue(date);
        clearFields();
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport, Labels.getLabel("sp.crud.product.list.reporte.stock"));
            AccessControl.saveAction(Permission.STOCK, "Se descargo listado de productos en stock formato excel");
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte.stock"), lbxReport,0);
        	AccessControl.saveAction(Permission.STOCK, "Se descargo listado de productos en stock formato pdf");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        dtbBeginningDate.setFormat("yyyy/MM/dd");
        dtbBeginningDate.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbBeginningDate.setText("");
        dtbEndingDate.setFormat("yyyy/MM/dd");
        dtbEndingDate.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbEndingDate.setText("");
        dtbBeginningDateExit.setFormat("yyyy/MM/dd");
        dtbBeginningDateExit.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbBeginningDateExit.setText("");
        dtbEndingDateExit.setFormat("yyyy/MM/dd");
        dtbEndingDateExit.setValue(new Timestamp(new java.util.Date().getTime()));
        dtbEndingDateExit.setText("");
        //loadAccount();
        loadProvider();
        loadProduct();
        loadCustomer();
        loadCondition();
        loadTransactionType();
    }

    public void onClick$btnAdd() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public List<ProductSerie> getFilteredList(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadList(List<ProductSerie> list) {
		// TODO Auto-generated method stub
		
	}

}
