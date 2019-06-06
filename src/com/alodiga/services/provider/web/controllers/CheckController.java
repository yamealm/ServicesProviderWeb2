package com.alodiga.services.provider.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.ejbs.BannerEJB;
import com.alodiga.services.provider.commons.ejbs.BillingsEJB;
import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.PreferencesEJB;
import com.alodiga.services.provider.commons.ejbs.ReportEJB;
import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
import com.alodiga.services.provider.commons.managers.ContentManager;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.managers.PreferenceManager;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.generic.controllers.GenericSPController;

public class CheckController extends GenericAbstractController implements GenericSPController {

    private static final long serialVersionUID = -9145887024839938515L;
    Listbox lbxEjbsFounded;
    Listbox lbxEjbsNotFounded;
    List<String> goodEjbs = new ArrayList<String>();
    List<String> badEjbs = new ArrayList<String>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        checkEjbs();
        loadList();
    }

    private void checkEjbs() {
        try {
            AccessControlEJB accessControlEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
            AuditoryEJB auditoryEJB = (AuditoryEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUDITORY_EJB);
            PreferencesEJB preferencesEJB = (PreferencesEJB) EJBServiceLocator.getInstance().get(EjbConstants.PREFERENCES_EJB);
            ReportEJB reportEJB = (ReportEJB) EJBServiceLocator.getInstance().get(EjbConstants.REPORT_EJB);
            UserEJB userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            UtilsEJB utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            CustomerEJB customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
            BillingsEJB billingsEJB = (BillingsEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLINGS_EJB);
            BannerEJB bannerEJB = (BannerEJB) EJBServiceLocator.getInstance().get(EjbConstants.BANNER_EJB);
                     
            
            if (accessControlEJB != null) {
                goodEjbs.add("accessControlEJB");
            } else {
                badEjbs.add("accessControlEJB");
            }
            if (auditoryEJB != null) {
                goodEjbs.add("auditoryEJB");
            } else {
                badEjbs.add("auditoryEJB");
            }

            if (preferencesEJB != null) {
                goodEjbs.add("preferencesEJB");
            } else {
                badEjbs.add("preferencesEJB");
            }
            if (reportEJB != null) {
                goodEjbs.add("reportEJB");
            } else {
                badEjbs.add("reportEJB");
            }
            if (userEJB != null) {
                goodEjbs.add("userEJB");
            } else {
                badEjbs.add("userEJB");
            }
            if (utilsEJB != null) {
                goodEjbs.add("utilsEJB");
            } else {
                badEjbs.add("utilsEJB");
            }
            if (billingsEJB != null) {
                goodEjbs.add("billingsEJB");
            } else {
                badEjbs.add("billingsEJB");
            }
             if (bannerEJB != null) {
                goodEjbs.add("bannerEJB");
            } else {
                badEjbs.add("bannerEJB");
            }

            if (customerEJB != null) {
                goodEjbs.add("customerEJB");
            } else {
                badEjbs.add("customerEJB");
            }
                    
        } catch (Exception ex) {
            ex.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        }
    }

    private void loadList() {
        for (String ejb : goodEjbs) {
            Listitem item = new Listitem();
            item.appendChild(new Listcell(ejb));
            item.setParent(lbxEjbsFounded);
        }
        for (String ejb : badEjbs) {
            Listitem item = new Listitem();
            item.appendChild(new Listcell(ejb));
            item.setParent(lbxEjbsNotFounded);
        }
    }

     public void onClick$btnReloadCache() {
        try {
            ContentManager.refresh();
            PreferenceManager.refresh();
            PermissionManager.refresh();
            this.showMessage("sp.common.reload.cache.success", false, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.showMessage("sp.error.general", true, ex);
        }
    }

    @Override
    public void loadPermission(AbstractSPEntity clazz) throws GeneralException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
