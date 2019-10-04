package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.TransactionEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Category;
import com.alodiga.services.provider.commons.models.MetrologicalControlHistory;
import com.alodiga.services.provider.commons.models.Product;
import com.alodiga.services.provider.commons.models.ProductSerie;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.PDFUtil;
import com.alodiga.services.provider.web.utils.Utils;


public class MunitoringController extends GenericAbstractListController<ProductSerie> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxReport;
    private Listbox lbxReport2;
    private Listbox lbxReport3;
    private Listbox lbxReport4;
    private Listbox lbxReport5;
    private Combobox cmbProvider;
    private Button btnExportPdf;
    private Datebox dtbBeginningDate;
    private Button btnExportPdf2;
    private Button btnDownload2;
    private Button btnDownload23;
    private Button btnExportPdf23;
    private Button btnDownload4;
    private Button btnExportPdf4;
    private Button btnDownload5;
    private Button btnExportPdf5;
     
     
    

    private Datebox dtbEndingDate;
    private ProductEJB productEJB = null;
    private TransactionEJB transactionEJB = null;
    private Intbox intDay;
    private Intbox intCuradoStock4;
    Boolean isStoreAll = false;
    List<ProductSerie> productSeries = null;
    List<MetrologicalControlHistory> controlHistories = null;
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
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
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
			loadList2(null);
		}
    	
    }
    
    public void onClick$tabControl() {
    	try {
    		
    		controlHistories = productEJB.getMetrologicalControlDefeated(5);
			intDay.setValue(5);
			loadList5(controlHistories);
			
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			loadList5(null);
		}
    	
    }
 
    
	public void onClick$tabMinStock() {
		try {
			EJBRequest request2 = new EJBRequest();
			List<Product> products = new ArrayList<Product>();
			products = productEJB.getProducts(request2);
			List<Product> productsReturn = new ArrayList<Product>();
			for (Product p : products) {
				int quantity = transactionEJB.loadQuantityByProductId(p.getId(), Category.STOCK);
				if (quantity < p.getStockMin()) {
					p.setStockMax(quantity);
					productsReturn.add(p);
				}
			}
			loadList3(productsReturn);
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			e.printStackTrace();
		}
	}
	
	
    public void onClick$tabCure() {
    	try {
			productSeries = productEJB.getProductDefeatedCure(5);
			intCuradoStock4.setValue(5);
			loadList4(productSeries);
		} catch (GeneralException e) {
			e.printStackTrace();
		} catch (NullParameterException e) {
			e.printStackTrace();
		} catch (EmptyListException e) {
			loadList4(null);
		}
    	
    }
	
    

    public void onClick$btnSearch4() {
        try {
        	productSeries = productEJB.getProductDefeatedCure(intCuradoStock4.getValue());
        	loadList4(productSeries);
        } catch (GeneralException ex) {
            showError(ex);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            loadList4(null);
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
        	loadList2(null);
        }
    }

//    private void loadProvider() {
//        try {
//        	cmbProvider.getItems().clear();
//            List<Provider> providers = utilsEJB.getProvider();
//            Comboitem item = new Comboitem();
//            item.setLabel(Labels.getLabel("sp.common.all"));
//            item.setParent(cmbProvider);
//            cmbProvider.setSelectedItem(item);
//            for (int i = 0; i < providers.size(); i++) {
//                item = new Comboitem();
//                item.setValue(providers.get(i));
//                item.setLabel(providers.get(i).getName());
//                item.setParent(cmbProvider);
//            }
//        } catch (Exception ex) {
//            this.showError(ex);
//        }

//    }

    
    


    
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
                  
                    
                    item.appendChild(new Listcell(productSerie.getProduct().getPartNumber()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getSerie()!=null?productSerie.getSerie():""));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    item.appendChild(new Listcell(productSerie.getCondition().getName()));
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
                  
                    
                    item.appendChild(new Listcell(productSerie.getProduct().getPartNumber()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getSerie()!=null?productSerie.getSerie():""));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    item.appendChild(new Listcell(productSerie.getCondition().getName()));
              
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
    
    public void loadList4(List<ProductSerie> list) {
        try {
        	lbxReport4.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (ProductSerie productSerie : list) {
                	
                	try {
                		productSerie.getCure().toString();
					} catch (NullPointerException e) {
					    System.out.println("entro en el nullpointer");
						continue;
					}
                	
                    item = new Listitem();
                    item.setValue(productSerie);
                    item.appendChild(new Listcell(productSerie.getProduct().getPartNumber()));
                    item.appendChild(new Listcell(productSerie.getProduct().getDescription()));
                    item.appendChild(new Listcell(productSerie.getSerie()!=null?productSerie.getSerie():""));
                    item.appendChild(new Listcell(productSerie.getProvider().getName()));
                    item.appendChild(new Listcell(productSerie.getCondition().getName()));
              
                    Listcell listCellEnding = new Listcell(productSerie.getCure().toString());
                    
                    if(productSerie.getCure().getTime()< new Date().getTime()) {
                    	 listCellEnding.setStyle("color:red");
                    }
                    item.appendChild(listCellEnding);
                    item.appendChild(new Listcell(productSerie.getAmount().toString()));
                    item.appendChild(new Listcell(String.valueOf(productSerie.getQuantity())));
                    //item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
                    item.setParent(lbxReport4);
                }
                btnDownload4.setVisible(true);
                btnExportPdf4.setVisible(true);
            } else {
                btnDownload4.setVisible(false);
                btnExportPdf4.setVisible(false);
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
                item.setParent(lbxReport4);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    
    
    public void loadList3(List<Product> list) {
        try {
        	lbxReport3.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (Product product : list) {
                	
                    item = new Listitem();
                    item.setValue(product);
                    item.appendChild(new Listcell(product.getPartNumber()));
                    item.appendChild(new Listcell(product.getDescription()));
                    item.appendChild(new Listcell(product.getActNpNsn()));
                    item.appendChild(new Listcell(product.getUbicationBox()));
                    item.appendChild(new Listcell(String.valueOf(product.getStockMin())));
                    item.appendChild(new Listcell(String.valueOf(product.getStockMax())));
                    //item.appendChild(new ListcellViewButton(adminPage, transaction, Permission.VIEW_TRANSACTION));
                    item.setParent(lbxReport3);
                }
                btnDownload23.setVisible(true);
                btnExportPdf23.setVisible(true);
            } else {
                btnDownload23.setVisible(false);
                btnExportPdf23.setVisible(false);
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
                item.setParent(lbxReport3);
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void loadList5(List<MetrologicalControlHistory> list) {
        try {
        	lbxReport5.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
                for (MetrologicalControlHistory controlHistory : list) {
                	
                	try {
                		controlHistory.getExpirationDate().toString();
					} catch (NullPointerException e) {
					    System.out.println("entro en el nullpointer");
						continue;
					}
                	
                    item = new Listitem();
                    item.setValue(controlHistory);
                  
                    
                    item.appendChild(new Listcell(controlHistory.getMetrologicalControl().getDesignation()));
                    item.appendChild(new Listcell(controlHistory.getMetrologicalControl().getInstrument()));
                    item.appendChild(new Listcell(controlHistory.getMetrologicalControl().getBraund().getName()+ "/"+
                    		controlHistory.getMetrologicalControl().getModel().getName()));
                    item.appendChild(new Listcell(controlHistory.getMetrologicalControl().getSerie()));
                    item.appendChild(new Listcell(controlHistory.getCalibrationDate().toString()));
                    Listcell listCellEnding = new Listcell(controlHistory.getExpirationDate().toString());
                    
                    if(controlHistory.getExpirationDate().getTime()< new Date().getTime()) {
                    	 listCellEnding.setStyle("color:red");
                    }
                    item.appendChild(listCellEnding);
                    item.setParent(lbxReport5);
                }
                btnDownload5.setVisible(true);
                btnExportPdf5.setVisible(true);
            } else {
                btnDownload5.setVisible(false);
                btnExportPdf5.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell());
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxReport5);
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
    
    
    public void onClick$btnDownload23() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport3, Labels.getLabel("sp.report.title"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnExportPdf23() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxReport3,0);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    
    public void onClick$btnDownload4() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport4, Labels.getLabel("sp.report.title"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnExportPdf4() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.stock"))+".pdf", Labels.getLabel("sp.crud.product.list.reporte"), lbxReport4,0);
        } catch (Exception ex) {
            showError(ex);
        }
    }
    
    public void onClick$btnDownload5() throws InterruptedException {
        try {
            Utils.exportExcel(lbxReport5, Labels.getLabel("sp.crud.metrological.control.list.reporte"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnExportPdf5() throws InterruptedException {
        try {
        	PDFUtil.exportPdf((Labels.getLabel("sp.common.meteorological"))+".pdf", Labels.getLabel("sp.crud.metrological.control.list.reporte"), lbxReport5,0);
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
