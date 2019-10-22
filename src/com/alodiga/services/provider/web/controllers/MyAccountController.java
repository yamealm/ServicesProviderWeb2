package com.alodiga.services.provider.web.controllers;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.models.UserHasProfileHasEnterprise;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.Encoder;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class MyAccountController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblLogin;
    private Textbox txtOldPassword;
    private Textbox txtNewPassword1;
    private Textbox txtNewPassword2;
    private Textbox txtName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Textbox txtPhoneNumber;
//    private Checkbox cbxEnabled;
//    private Checkbox cbxReceiveTopNotification;
    private Listbox lbxEnterprises;
    private Label lblProfile;
    private UserEJB userEJB = null;
    private User userParam;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        initView(eventType, "crud.user");
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            userParam = AccessControl.loadCurrentUser();
            loadFields(userParam);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
    }

    private void loadFields(User user) {
        try {
            lblLogin.setValue(user.getLogin());
            lblProfile.setValue(user.getUserHasProfileHasEnterprises().get(0).getProfile().getProfileDataByLanguageId(languageId).getAlias());
            txtName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());
            txtEmail.setText(user.getEmail());
            txtPhoneNumber.setText(user.getPhoneNumber());
//            cbxEnabled.setChecked(user.getEnabled());
//            cbxReceiveTopNotification.setChecked(user.getReceiveTopUpNotification());
            lbxEnterprises.getItems().clear();
            loadEnterpriseList(user.getUserHasProfileHasEnterprises());
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadEnterpriseList(List<UserHasProfileHasEnterprise> userHasProfileHasEnterprises) {
        lbxEnterprises.setCheckmark(false);
        for (int i = 0; i < userHasProfileHasEnterprises.size(); i++) {
            if (userHasProfileHasEnterprises.get(i).getEndingDate() == null) {
                Enterprise enterprise = userHasProfileHasEnterprises.get(i).getEnterprise();
                Listitem item = new Listitem();
                item.setValue(enterprise);
                item.appendChild(new Listcell(enterprise.getName()));
                item.setParent(lbxEnterprises);
            }
        }
    }

    public void blockFields() {
    }

    public boolean validateEmpty() {

        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtLastName.getText().isEmpty()) {
            txtLastName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPhoneNumber.getText().isEmpty()) {
            txtPhoneNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            return true;
        }
        return false;

    }

    private void saveUser(User _user) {
        try {
            if (_user != null) {
                User user = _user;
                if (txtOldPassword.getText().isEmpty()) {
                    user.setFirstName(txtName.getText());
                    user.setLastName(txtLastName.getText());
                    user.setEmail(txtEmail.getText());
                    user.setPhoneNumber(txtPhoneNumber.getText());
                    user.setEnabled(true);
                    user.setReceiveTopUpNotification(true);
                    request.setParam(user);
                    user = userEJB.saveUser(request);
                    userParam = user;
                    Sessions.getCurrent().setAttribute(WebConstants.SESSION_USER, user);
                    this.showMessage("sp.common.save.update", false, null);
                } else if (user.getPassword().equals(Encoder.MD5(txtOldPassword.getText()))) {
                    if (txtNewPassword1.getText().equals(txtNewPassword2.getText())) {
                        user.setPassword(Encoder.MD5(txtNewPassword1.getText()));
                        user.setFirstName(txtName.getText());
                        user.setLastName(txtLastName.getText());
                        user.setEmail(txtEmail.getText());
                        user.setPhoneNumber(txtPhoneNumber.getText());
                        user.setEnabled(true);
                        user.setReceiveTopUpNotification(true);
                        request.setParam(user);
                        user = userEJB.saveUser(request);
                        userParam = user;
                        Sessions.getCurrent().setAttribute(WebConstants.SESSION_USER, user);
                        this.showMessage("sp.common.save.update", false, null);
                    } else {

                        this.showMessage("sp.error.confirmationPasswordNotEqual", true, null);
                    }
                } else {
                    this.showMessage("sp.error.wrongPassword", true, null);
                }
            } else {
                this.showMessage("sp.error.general", true, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        }
    }

    public void onClick$btnCancel() {
        clearFields();
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            saveUser(userParam);
        }
    }

    public void loadData() {
        loadFields(userParam);
    }
}
