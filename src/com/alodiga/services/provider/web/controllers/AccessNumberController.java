package com.alodiga.services.provider.web.controllers;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.ServicesEJB;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
import com.alodiga.services.provider.commons.utils.AccountData;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.ServiceConstans;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.generic.controllers.GenericSPController;

public class AccessNumberController extends GenericAbstractController implements GenericSPController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtPhoneNumber;
    private Label lblAccessNumber;
    private Textbox txtContent;
    private ServicesEJB servicesEJB = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();

    }

    @Override
    public void initialize() {
        try {
            servicesEJB = (ServicesEJB) EJBServiceLocator.getInstance().get(EjbConstants.SERVICES_EJB);
            txtPhoneNumber.setFocus(true);
        } catch (Exception ex) {
            showError(ex);
        }
    }

   

    public boolean validateEmpty() {
        boolean valid = false;
        if (txtPhoneNumber.getText().isEmpty()) {
            txtPhoneNumber.setFocus(true);
            this.showMessage("error.field.cannotNull", true, null);
        }  else {
            valid = true;
        }
        return valid;
    }

    public void onClick$btnSearch() {
        if (validateEmpty()) {
            try {
                AccountData accountData = new AccountData();
                accountData.setLogin(ServiceConstans.USER_SP);
                accountData.setPassword(ServiceConstans.PASSWORD_SP);
//                List<String> accessBumbers = servicesEJB.getAccessNumberByPhoneNumber(accountData, txtPhoneNumber.getText());
//                lblAccessNumber.setValue(accessBumbers.get(0)!=null?accessBumbers.get(0):"");
                this.showMessage(Labels.getLabel("sp.automatic.promotion.success"), false, null);
            } catch (Exception ex) {
                this.showMessage(ex.getMessage(), true, ex);
            }
        }
    }

    public void onClick$btnCancel() {
        txtPhoneNumber.setText("");
    }

    @Override
    public void loadPermission(AbstractSPEntity clazz) throws GeneralException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
