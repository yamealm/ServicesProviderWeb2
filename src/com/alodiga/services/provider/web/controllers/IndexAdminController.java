package com.alodiga.services.provider.web.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.exceptions.DisabledUserException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.utils.AccessControl;

public class IndexAdminController extends GenericAbstractController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtLogin;
    private Textbox txtRecoverLogin;
    private Textbox txtPassword;
    private Groupbox gbxLogin;
    private Groupbox gbxRecoverPass;
    //private Label lblInfo;
    private UserEJB userEJB = null;
    private String adminHome ="home-admin.zul";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }


    @Override
    public void initialize() {
        super.initialize();
        try {
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        }
        txtLogin.setFocus(true);
    }

    public void clearFields() {
        txtLogin.setRawValue(null);
        txtPassword.setRawValue(null);
    }


    public Boolean validateEmpty() {
        if (txtLogin.getText().isEmpty()) {
            txtLogin.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPassword.getText().isEmpty()) {
            txtPassword.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    public boolean validateRecoverLogin() {
        
        if (txtRecoverLogin.getText().isEmpty()) {
            this.showMessage("sp.error.field.cannotNull", true, null);
            txtRecoverLogin.setFocus(true);
        }else{
            return true;
        }
        return false;
    }

     public boolean validate() {
        if (validateEmpty()) {
            try {
                AccessControl.validateUser(txtLogin.getText(), txtPassword.getText());
                return true;
            } catch (DisabledUserException ex) {
                this.showMessage("sp.error.disableAccount", true, null);
            } catch (RegisterNotFoundException ex) {
                this.showMessage("sp.error.invalid.login", true, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.showMessage("sp.error.general", true, null);
            }
        }
        return false;
    }

  public void onClick$btnLogin() throws InterruptedException {
      this.clearMessage();
        if (validate()) {
            Executions.sendRedirect(adminHome);
        }
    }

    public void onOK$txtLogin() {
        this.clearMessage();
        if (validate()) {
             Executions.sendRedirect(adminHome);
        }
    }

    public void onOK$txtPassword() {
        this.clearMessage();
        if (validate()) {
             Executions.sendRedirect(adminHome);
        }
    }

    public void onClick$btnRecoverPassword() throws InterruptedException {
        this.clearMessage();
        gbxLogin.setVisible(false);
        gbxRecoverPass.setVisible(true);
    }

    public void onClick$btnCancelRecoverPassword() throws InterruptedException {
        this.clearMessage();
        gbxLogin.setVisible(true);
        gbxRecoverPass.setVisible(false);
    }
    public void onClick$btnRecoverPass() throws InterruptedException {
        this.clearMessage();
        if (validateRecoverLogin()) {
            try {
                User user = null;
                Map params = new HashMap<String, Object>();
                params.put(QueryConstants.PARAM_LOGIN, txtRecoverLogin.getText());
                request.setParams(params);
                user = userEJB.loadUserByLogin(request);
                AccessControl.generateNewPassword(user, false);
                this.showMessage("sp.common.recoveryPassword.success", false, null);
            } catch (RegisterNotFoundException e) {
                this.showMessage("sp.common.recoveryPassword.notFound", true, null);
            } catch (Exception e) {
                e.printStackTrace();
                this.showMessage("sp.error.general", true, null);
            }
        }
    }
    
    public void onClick$mniEnglish() {

        if (!Locales.getCurrent().getLanguage().equals("en")) {
            Locale _newLocal = new Locale("en", "US", "en");
            session.setAttribute(Attributes.PREFERRED_LOCALE, _newLocal);
            Executions.sendRedirect(null);
        }
    }

    public void onClick$mniSpanish() {
        if (!Locales.getCurrent().getLanguage().equals("es")) {
            Locale _newLocal = new Locale("es", "ES", "es");
            session.setAttribute(Attributes.PREFERRED_LOCALE, _newLocal);
            Executions.sendRedirect(null);
        }
    }
}
