package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.BillingsEJB;
import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.models.Currency;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public class AdminForceBillingController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtLogin;
    private Combobox cmbCurrencies;
    private UtilsEJB utilsEJB = null;
    private BillingsEJB billingsEJB = null;
    private Enterprise enterpriseParam;
    //private Button btnForces;

    private Button btnSearch;


    private Timestamp timeStampDate;
    //private Account account;
    private UserEJB userEJB = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
             billingsEJB = (BillingsEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLINGS_EJB);
    
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnForces()  {
        String login = txtLogin.getValue();
        try {
            billingsEJB.forceBilling(login);
        } catch (GeneralException ex) {
            ex.printStackTrace();
                showError(ex);
                this.showMessage("sp.error.field.error.save.GE", true, null);
            Logger.getLogger(AdminForceBillingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            ex.printStackTrace();
                showError(ex);
                this.showMessage("sp.error.field.error.save.ELE", true, null);
            Logger.getLogger(AdminForceBillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


      public void onClick$btnSearch() {
          System.out.println("paso");
    }





    @Override
    public void clearFields() {
        txtLogin.setRawValue(null);
    }

    public void blockFields() {;
        btnSearch.setVisible(true);
    }

    private void saveEnterprise(Enterprise _enterprise) {
//        
    }

    public void loadData() {
    }

    private void loadCurrencies(boolean isAdd) {
        List<Currency> currencies = new ArrayList<Currency>();
        try {
            currencies = utilsEJB.getCurrencies();
            for (Currency c : currencies) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(c.getName());
                cmbItem.setDescription(c.getSymbol());
                cmbItem.setValue(c);
                cmbItem.setParent(cmbCurrencies);
                if (!isAdd) {
                    if (c.getId().equals(enterpriseParam.getCurrency().getId())) {
                        cmbCurrencies.setSelectedItem(cmbItem);
                    }
                }
            }

        }   catch (NullParameterException ex) {
                ex.printStackTrace();
                showError(ex);
                this.showMessage("sp.error.field.error.save.NPE", true, null);


            } catch (GeneralException ex) {
                ex.printStackTrace();
                showError(ex);
                this.showMessage("sp.error.field.error.save.GE", true, null);


	   } catch (EmptyListException ex) {
                    showError(ex);
                    this.showMessage("sp.error.field.error.save.ELE", true, null);


            } catch (Exception ex) {
                ex.printStackTrace();
                showError(ex);
                this.showMessage("sp.error.field.cannotNull", true, null);

            }

    }
}
