package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zul.Datebox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.api.Intbox;

public class MunitoringController extends GenericAbstractListController<ProductSerie> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Listbox lbxReport2;
    private Combobox cmbProvider;
    private Button btnExportPdf;
    private Combobox cmbProduct;
    private Datebox dtbBeginningDate;
    private Button btnExportPdf2;
    private Button btnDownload2;
    

    private Datebox dtbEndingDate;
    private UtilsEJB utilsEJB = null;
    private ProductEJB productEJB = null;
    private Intbox intDay;
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
            
            getData();
            
            

            
            
            
            
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        lblInfo.setValue("");
        lblInfo.setVisible(false);
    }
    
    public void onClick$tabProxEnding() {
    	try {
    		
			productSeries = productEJB.getProductDefeated(5);
			intDay.setValue(5);
			loadList2(productSeries);
			
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			e.printStackTrace();
		}
    	
    }
 

    public void onClick$btnSearch() {
        try {
        	productSeries = productEJB.getProductDefeated(intDay.getValue());
        	loadList2(productSeries);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            loadList(null);
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
        } catch (Exception ex) {
            this.showError(ex);
        }

    }

    
    


    public void loadList(List<ProductSerie> list) {
        try {
            lbxReport.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (ProductSerie productSerie : list) {
                	
                	try {
                		productSerie.getExpirationDate().toString();
					} catch (NullPointerException e) {
					    System.out.println("entro en el nullpointer");
						continue;
					}
                	
                    item = new Listitem();
                    item.setValue(productSerie);
                  
                    
                    item.appendChild(new Listcell(productSerie.getId().toString()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
              
                    Listcell listCellEnding = new Listcell(productSerie.getExpirationDate().toString());
                    listCellEnding.setStyle("color:red");
                    
                    item.appendChild(listCellEnding);
                    item.appendChild(new Listcell(productSerie.getAmount().toString()));
                    item.appendChild(new Listcell(String.valueOf(productSerie.getQuantity())));
                    //item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
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
    
    
    public void loadList2(List<ProductSerie> list) {
        try {
        	lbxReport2.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (ProductSerie productSerie : list) {
                	
                	try {
                		productSerie.getExpirationDate().toString();
					} catch (NullPointerException e) {
					    System.out.println("entro en el nullpointer");
						continue;
					}
                	
                    item = new Listitem();
                    item.setValue(productSerie);
                  
                    
                    item.appendChild(new Listcell(productSerie.getId().toString()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
              
                    Listcell listCellEnding = new Listcell(productSerie.getExpirationDate().toString());
                    
                    if(productSerie.getExpirationDate().getTime()< new Date().getTime()) {
                    	 listCellEnding.setStyle("color:red");
                    }
                    item.appendChild(listCellEnding);
                    item.appendChild(new Listcell(productSerie.getAmount().toString()));
                    item.appendChild(new Listcell(String.valueOf(productSerie.getQuantity())));
                    //item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
                    item.setParent(lbxReport2);
                }
                btnDownload2.setVisible(true);
                btnExportPdf2.setVisible(true);
            } else {
                btnDownload2.setVisible(false);
                btnExportPdf2.setVisible(false);
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
                item.setParent(lbxReport2);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        cmbProvider.setSelectedIndex(0);
        Date date = new Date();
        dtbBeginningDate.setValue(date);
        dtbEndingDate.setValue(date);
        clearFields();
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport, Labels.getLabel("sp.report.title"));
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnExportPdf() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxReport,0);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    
    public void onClick$btnDownload2() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport2, Labels.getLabel("sp.report.title"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnExportPdf2() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxReport2,0);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    
    
    public void getData() {
    	try {
			productSeries = productEJB.getProductDefeated();
			loadList(productSeries);
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			e.printStackTrace();
		}
        
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

}