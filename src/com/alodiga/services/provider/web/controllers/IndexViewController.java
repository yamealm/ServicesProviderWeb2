package com.alodiga.services.provider.web.controllers;

import java.util.Calendar;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.DisabledAccountException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.utils.AccessControl;

public class IndexViewController extends GenericAbstractController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtLogin;
    private Textbox txtPassword;
    private Label lblInfo;
    private Label lblInfo02;
    private String urlRedirect = null;
    private Vlayout vl01;
    private Vlayout vl02;
    private UserEJB userEJB = null;
    private UtilsEJB utilsEJB = null;
    private Textbox txtLoginRecover;
    private Enterprise enterprise = null;
//    private Label lblUpdatedVersion;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        request.setFirst(0);
        initialize();
    }

    public void initialize() {
        try {
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);

            Calendar today = Calendar.getInstance();
            Calendar endingDate = Calendar.getInstance();
            endingDate.set(Calendar.MONTH, Calendar.AUGUST);
            endingDate.set(Calendar.DAY_OF_MONTH, 1);
            endingDate.set(Calendar.YEAR, 2013);
//            lblUpdatedVersion.setVisible(today.before(endingDate));

        } catch (Exception e) {
            e.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    public void onClick$btnLogin() throws InterruptedException {
        pageRedirection();
    }

    public void onClick$lblRecoverReturn() throws InterruptedException {
        vl01.setVisible(true);
        vl02.setVisible(false);
    }

    public void onClick$btnLoginRecover() throws InterruptedException {
        if (validateEmptyRecover()) {
            try {
//                Account account = null;
////                account = userEJB.loadAccountByLogin(txtLoginRecover.getText());
//                AccessControl.generateNewPassword(null, account, false);
                lblInfo02.setValue(Labels.getLabel("sp.common.recoveryPassword.success"));
//            } catch (RegisterNotFoundException ex) {
//                lblInfo02.setValue(Labels.getLabel("sp.common.recoveryPassword.notFound"));
            } catch (Exception ex) {
                lblInfo02.setValue(Labels.getLabel("sp.error.general"));
            }
        }
    }

    public Boolean validateEmptyRecover() {
        Boolean valid = true;
        if (txtLoginRecover.getText().isEmpty()) {
            valid = false;
            lblInfo02.setValue(Labels.getLabel("sp.error.field.cannotNull"));
            txtLoginRecover.setFocus(true);
        }
        return valid;
    }

    public void onOK$txtLogin() {
        pageRedirection();
    }

    public void onOK$txtPassword() {
        pageRedirection();
    }

    public void onClick$lblRecoverPassword() throws InterruptedException {
        vl01.setVisible(false);
        vl02.setVisible(true);
    }

    public void onOK$btnRecover() {
        //TODO:
    }

    public boolean validate() {
        lblInfo.setValue("");
        boolean valid = false;
        if (validateEmpty()) {
            try {
//                valid = AccessControl.validateAccount(txtLogin.getText(), txtPassword.getText());
                urlRedirect = "loggedAccountView.zul";
//            } catch (DisabledAccountException ex) {
//                ex.printStackTrace();
//                lblInfo.setValue(Labels.getLabel("sp.error.disableAccount"));
//            } catch (RegisterNotFoundException ex) {
//                ex.printStackTrace();
//                lblInfo.setValue(Labels.getLabel("sp.error.invalid.login"));
            } catch (Exception ex) {
                ex.printStackTrace();
                lblInfo.setValue(Labels.getLabel("sp.error.general"));
            }
        }
        return valid;
    }

    public boolean validateEmpty() {
        boolean valid = false;
        if (txtLogin.getText().isEmpty()) {
            lblInfo.setValue(Labels.getLabel("sp.error.field.cannotNull"));
            txtLogin.setFocus(true);
        } else if (txtPassword.getText().isEmpty()) {
            lblInfo.setValue(Labels.getLabel("sp.error.field.cannotNull"));
            txtPassword.setFocus(true);
        } else {
            valid = true;
        }
        return valid;
    }

    private void pageRedirection() {
        if (validate()) {
            Executions.sendRedirect(urlRedirect);
        }
    }

    public void onClick$btnAccessNumbers() {
        Executions.getCurrent().sendRedirect("/docs/access-numbers.pdf", "_blank");
    }

    public void onClick$btnRates() {
        Executions.getCurrent().sendRedirect("/docs/new_rates.pdf", "_blank");
    }

    public void onClick$btnTerms() {
        Executions.getCurrent().sendRedirect("/docs/terms.pdf", "_blank");
    }
}
