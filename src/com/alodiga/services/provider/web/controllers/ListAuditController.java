package com.alodiga.services.provider.web.controllers;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;
import org.zkoss.zul.Datebox;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;

public class ListAuditController extends GenericAbstractAdminController {

    public static final Logger LOG = Logger.getLogger(ListAuditController.class);
    private Combobox cmbUser;
    private boolean f1 = true;
    private Datebox datefrom;
    private Datebox dateuntil;
    private Listbox listAudits;     
    private HashMap<String, Object> params;
    private EJBRequest request;
    String login="";
    public Div divInfo;
    Long statusSelected=null;
    private AccessControlEJB accessControlEJB = null;
    private AuditoryEJB auditoryEJB = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void initialize() {
        try {            
            InitialContext ic;
            ic = new InitialContext();
            auditoryEJB = (AuditoryEJB) ic.lookup("java:global/P2PEAR/P2PEJB/ejb/p2p/AuditoryEJB!com.alodiga.p2p.common.ejb.AuditoryEJB");
            accessControlEJB = (AccessControlEJB) ic.lookup("java:global/P2PEAR/P2PEJB/ejb/p2p/AccessControlEJB!com.alodiga.p2p.common.ejb.AccessControlEJB");
            loadUsers();             
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
       
    public void loadUsers() {
        List<User> users = new ArrayList<User>();  
        try {
            params = new HashMap<String, Object>();
            params.put(QueryConstants.PARAM_FILTER, false);
//            params.put(EjbConstants.PARAM_USER, cmbUser.getValue());
//            params.put(EjbConstants.PARAM_STATUS_Id, cmbStatus.getSelectedItem()!=null?cmbStatus.getSelectedItem().getValue():null);
            request = new EJBRequest();
            request.setParams(params);
            users = accessControlEJB.getUsersWithParams(request);
        } catch (GeneralException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!users.isEmpty()) {
            cmbUser.getItems().clear();
            Comboitem cmbItem = new Comboitem();
            cmbItem.setLabel(Labels.getLabel("ac.common.field.select"));
            cmbItem.setValue(null);
            cmbItem.setParent(cmbUser);
    
            for (User cc : users) {             
                cmbItem = new Comboitem();
                cmbItem.setLabel(cc.getLogin());
                cmbItem.setValue(cc.getId());
                cmbItem.setParent(cmbUser);
            }
            cmbUser.setSelectedIndex(0);
        }
    }
    
    public void loadDataAudit(boolean filter) {
        
        List<Audit> audits = new ArrayList<Audit>();
        try {
        Date startDate = datefrom.getValue();
        Date endDate = dateuntil.getValue();        
        params = new HashMap<String, Object>();
        params.put(QueryConstants.PARAM_FILTER, filter);
        params.put(QueryConstants.PARAM_USER_ID, cmbUser.getSelectedItem()!=null?cmbUser.getSelectedItem().getValue():null);                               
        params.put(QueryConstants.PARAM_BEGINNING_DATE, startDate);
        params.put(QueryConstants.PARAM_ENDING_DATE, endDate);
            
            if (dateuntil.getValue().getTime() >= datefrom.getValue().getTime()) {
                request = new EJBRequest();
                request.setParams(params);
                audits = auditoryEJB.searchAudit(request);
            }
        } catch (NullParameterException ex) {
            ex.printStackTrace();
        } catch (EmptyListException ex) {
        } catch (GeneralException ex) {
        }
            listAudits.getItems().clear();
            Listitem item = null;
            if (audits != null && !audits.isEmpty()) {
                for (Audit au : audits) {

                item = new Listitem();
                item.setValue(au);
                item.appendChild(new Listcell(au.getUser().getLogin())); 
                item.appendChild(new Listcell(au.getUser().getFirstName()+ " "+au.getUser().getLastName())); 
                item.appendChild(new Listcell(au.getPermission().getName().toString()));
                  item.appendChild(new Listcell(au.getOriginalValues()));
                item.appendChild(new Listcell(au.getNewValues()));
                item.appendChild(new Listcell(au.getCreationDate().toString()));
                item.setParent(listAudits);
                }
            } else {
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell()); 
                item.appendChild(new Listcell()); 
                item.setParent(listAudits);
            }
                       
    }

    public void onClick$btnSearch(){                          
            loadDataAudit(f1);    
    }


    public void showError() {
        try {
            Window window = (Window) Executions.createComponents("genralError.zul", null, null);
            window.doModal();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }         
    
}
