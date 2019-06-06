//package com.alodiga.services.provider.web.controllers;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Combobox;
//import org.zkoss.zul.Comboitem;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Row;
//import org.zkoss.zul.Textbox;
//
//import com.alodiga.services.provider.commons.ejbs.UserEJB;
//import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.models.Account;
//import com.alodiga.services.provider.commons.models.City;
//import com.alodiga.services.provider.commons.models.County;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.Invoice;
//import com.alodiga.services.provider.commons.models.Language;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.State;
//import com.alodiga.services.provider.commons.models.TinType;
//import com.alodiga.services.provider.commons.utils.Constants;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.commons.utils.GeneralUtils;
//import com.alodiga.services.provider.commons.utils.QueryConstants;
//import com.alodiga.services.provider.web.components.ListcellDetailsButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
//import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
//import com.alodiga.services.provider.web.utils.WebConstants;
//
//public class InvoiceAdminController extends GenericAbstractAdminController implements GenericSPController {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//
//private Combobox cmbEnterprises;
//private Listbox lbxRecords;
//private Combobox cmbLanguages;
//private Combobox cmbPeriods;
//private Combobox cmbType;
//private Row rowPassword;
//private Label lblPassword;
//private Combobox cmbCountries;
//private Combobox cmbStates;
//private Combobox cmbCounties;
//private Combobox cmbCities;
//private Combobox cmbTinTypes;
//private Label lblLogin;
//private Textbox txtFullName;
//private Textbox txtBusinessName;
//private Textbox txtIdentifier;
//private Textbox txtnunDepTrans;
//private Textbox txtCountryCode;
//private Textbox txtRegionCode;
//private Textbox txtPhoneNumber;
//private Textbox txtEmail;
//private Account accountParam;
//private Button btnSave;
//private Button btnGeneratePassword;
//private UserEJB userEJB;
//private UtilsEJB utilsEJB;
//private String newPassword = null;
//    Map params = null;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        accountParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Account) Sessions.getCurrent().getAttribute("object") : null;
//        initialize();
//        loadEnterprises(accountParam.getEnterprise());
//        loadTinTypes(accountParam.getTinType());
//        loadLanguages(accountParam.getLanguage());
//        //loadPeriods(accountParam.getPreiod());
//        loadFields(accountParam);
//
//
//
//        //initView(eventType, "sp.crud.account");
//    }
//
//    @Override
//    public void initView(int eventType, String adminView) {
//        super.initView(eventType, "sp.crud.account");
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//
//            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadEnterprises(Enterprise enterprise) {
//        List<Enterprise> enterprises = null;
//        try {
//            cmbEnterprises.getItems().clear();
//            enterprises = utilsEJB.getEnterprises();
//            for (Enterprise e : enterprises) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(e.getName());
//                cmbItem.setValue(e);
//                cmbItem.setParent(cmbEnterprises);
//                if (enterprise != null && e.getId().equals(enterprise.getId())) {
//                    cmbEnterprises.setSelectedItem(cmbItem);
//                }
//            }
//            if (enterprise == null) {
//                cmbEnterprises.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadTinTypes(TinType tinType) {
//        List<TinType> tinTypes = null;
//        Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
//        try {
//            cmbTinTypes.getItems().clear();
//
//            tinTypes = utilsEJB.getTinTypesByEnterprise(enterprise.getId());
//            for (TinType tt : tinTypes) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(tt.getName());
//                cmbItem.setDescription(tt.getPrefix());
//                cmbItem.setValue(tt);
//                cmbItem.setParent(cmbTinTypes);
//                if (tinType != null && tt.getId().equals(tinType.getId())) {
//                    cmbTinTypes.setSelectedItem(cmbItem);
//                }
//            }
//            if (tinType == null) {
//                cmbTinTypes.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadLanguages(Language language) {
//        List<Language> languages = null;
//if (languages!=null){
//        try {
//            cmbLanguages.getItems().clear();
//
//            languages = utilsEJB.getLanguages();
//            for (Language l : languages) {
//                Comboitem cmbItem = new Comboitem();
//                cmbItem.setLabel(l.getDescription());
//                cmbItem.setDescription(l.getIso());
//                cmbItem.setValue(l);
//                cmbItem.setParent(cmbLanguages);
//                if (language != null && l.getId().equals(language.getId())) {
//                    cmbLanguages.setSelectedItem(cmbItem);
//                }
//            }
//            if (language == null) {
//                cmbLanguages.setSelectedIndex(0);
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//        }else{
//        System.out.println(" no se encontro lenguage");
//
//        }
//    }
//
//
//
//
//
//    @Override
//    public void clearFields() {
//    }
//
//    private void loadFields(Account account)  {
//            if (account != null) {//UPDATE
//            
//            try {
//                //UPDATE
//                //UPDATE
//                cmbEnterprises.setDisabled(true);
//                cmbTinTypes.setDisabled(true);
//                txtIdentifier.setReadonly(true);
//                lblPassword.setValue(account.getPassword());
//                rowPassword.setVisible(true);
//                loadEnterprises(account.getEnterprise());
//                loadLanguages(account.getLanguage());
//                loadTinTypes(account.getTinType());
//                //loadPeriods(account.getPreiod());
//                txtEmail.setText(account.getEmail());
//                txtFullName.setText(account.getFullName());
//                txtBusinessName.setText(account.getBusinessName());
//                lblLogin.setValue(account.getLogin());
//                txtIdentifier.setText(account.getIdentifier());
//                txtEmail.setText(account.getEmail());
//                Map params = new HashMap();
//                params.put(QueryConstants.PARAM_ACCOUNT_ID, account.getId());
//                request.setParams(params);
//                //loadList(utilsEJB.loadInvoicesbyId(request));
//                loadList(utilsEJB.loadInvoicesbyIds(account.getId()));
//            } catch (GeneralException ex) {
//                Logger.getLogger(InvoiceAdminController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            
//        } else {//ADDING
//            loadEnterprises(null);
//            loadLanguages(null);
//            loadTinTypes(null);
//            loadCountryCode();
//        }
//    }
//
//    
//
//   private String  getDateBell(Integer dis) {
//      CalendarT teste = new CalendarT();
//      if(dis!=0){
//      Calendar cal = teste.addDays(null, dis);
//       Date now = new Date(cal.getTimeInMillis());
//       SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//       String fecha = format.format(now);
//       return fecha;
//
//      }else{
//
//      Calendar cal = teste.addDays(null, 0);
//        Date now = new Date(cal.getTimeInMillis());
//        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//       String fecha = format.format(now);
//       return fecha;
//      }
//
//   }
//
//
//
//
//
//    public void loadData() {
//        switch (eventType) {
//            case WebConstants.EVENT_EDIT:
//
//            loadFields(accountParam);
//
//                break;
//            case WebConstants.EVENT_VIEW:
//
//            loadFields(accountParam);
//
//                break;
//            case WebConstants.EVENT_ADD:
//
//            loadFields(null);
//
//        
//                break;
//            default:
//                break;
//        }
//    }
//
//
//
//
//    private void loadCounties(State state, County county) {
//
//        try {
//            cmbCounties.getItems().clear();
//            params = new HashMap();
//            params.put(QueryConstants.PARAM_STATE_ID, state.getId());
//            request.setParams(params);
//            List<County> counties = utilsEJB.getCountiesByState(request);
//            for (int i = 0; i < counties.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(counties.get(i));
//                item.setLabel(counties.get(i).getName());
//                item.setParent(cmbCounties);
//                if (counties.get(i).getId().equals(county.getId())) {
//                    cmbCounties.setSelectedItem(item);
//                }
//            }
//            loadCities(state, county, accountParam.getAddress().getCity());
//        } catch (EmptyListException ex) {
//            cmbCounties.setText(Labels.getLabel("sp.common.noApply"));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private void loadCities(State state, County county, City city) {
//        try {
//            cmbCities.getItems().clear();
//            List<City> cities = null;
//            params = new HashMap();
//            if (county != null) {
//                params.put(QueryConstants.PARAM_COUNTY_ID, county.getId());
//                request.setParams(params);
//                cities = utilsEJB.getCitiesByCounty(request);
//            } else if (state != null) {
//                params.put(QueryConstants.PARAM_STATE_ID, state.getId());
//                request.setParams(params);
//                cities = utilsEJB.getCitiesByState(request);
//            }
//            for (int i = 0; i < cities.size(); i++) {
//                Comboitem item = new Comboitem();
//                item.setValue(cities.get(i));
//                item.setLabel(cities.get(i).getName());
//                item.setParent(cmbCities);
//                if (cities.get(i).getId().equals(city.getId())) {
//                    cmbCities.setSelectedItem(item);
//                }
//            }
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnGeneratePassword() {
//        try {
//            newPassword = GeneralUtils.encryptMD5(GeneralUtils.getRamdomPassword(8));
//            lblPassword.setValue(newPassword);
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    
//
//    public void loadCountryCode() {
//        if (cmbEnterprises.getSelectedItem() != null) {
//            Enterprise enterprise = (Enterprise) cmbEnterprises.getSelectedItem().getValue();
//            if (enterprise.getId().equals(Enterprise.ALODIGA_USA)) {
//                txtCountryCode.setReadonly(true);
//                txtCountryCode.setText("" + Constants.USA_CODE);
//            } else {
//                txtCountryCode.setReadonly(false);
//                txtCountryCode.setRawValue(null);
//            }
//            loadTinTypes(null);
//        }
//    }
//
//
//
//
//    public void loadList(List<Invoice> list) {
//        String adminPage="listTransactionForInvoice.zul";
//         String adminPage2 ="listInvoices.zul";
//        try {
//            
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//
//                for (Invoice invoice : list) {
//
//                   item = new Listitem();
//                    item.setValue(invoice);
//                    
//                    item.appendChild(new Listcell(String.valueOf(invoice.getNumberInvoice())));
//                    item.appendChild(new Listcell(String.valueOf(invoice.getTotalToPay())));
//                    item.appendChild(new Listcell(String.valueOf(invoice.getEmissionDate())));
//                    item.appendChild(new Listcell(invoice.getInvoiceStatus().getName()));
//
//                    item.appendChild(new Listcell());
//                    permissionEdit = true;
//                    permissionRead = true;
//
//                     item.appendChild(permissionEdit ? new ListcellDetailsButton(adminPage, invoice,Permission.READ_INVOICE) : new Listcell());
//                     if(invoice.getInvoiceStatus().getName().equals("POR PAGAR")){
////                     item.appendChild(permissionRead ? new ListcellPaymentButton(adminPage2, invoice,Permission.READ_INVOICE) : new Listcell());
//                     }
//
//
//
//                    item.setParent(lbxRecords);
//                }
//            } else {
//
//                item = new Listitem();
//                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.setParent(lbxRecords);
//            }
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//
//    public void onClick$btnReferenceScript1() {
//
//        Executions.getCurrent().sendRedirect("/docs/script_referencia1.txt", "_blank");
//    }
//
//    public void onClick$btnReferenceScript2() {
//        Executions.getCurrent().sendRedirect("/docs/script_referencia2.txt", "_blank");
//    }
//
//    public void blockFields() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//
//
//   
//}
