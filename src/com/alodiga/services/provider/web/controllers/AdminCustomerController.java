package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.models.Address;
import com.alodiga.services.provider.commons.models.City;
import com.alodiga.services.provider.commons.models.CivilState;
import com.alodiga.services.provider.commons.models.Country;
import com.alodiga.services.provider.commons.models.County;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Gender;
import com.alodiga.services.provider.commons.models.State;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.Encoder;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

public class AdminCustomerController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtDNI;
    private Textbox txtFirstName;
    private Textbox txtLastName;
    private Textbox txtEmail;
//    private Textbox txtCountryCode;
//    private Textbox txtRegionCode;
    private Textbox txtPhoneNumber;
    private Textbox txtAddress;
    private Checkbox cbxEnabled;
    private Customer customerParam;
    private Button btnSave;
    private CustomerEJB customerEJB;
    Map params = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        customerParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Customer) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.customer");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.customer");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {

            customerEJB = (CustomerEJB) EJBServiceLocator.getInstance().get(EjbConstants.CUSTOMER_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

//   
    public void clearFields() {
        
    }

    private void loadFields(Customer customer) {
        if (customer != null) {//UPDATE
            cbxEnabled.setChecked(customer.getEnabled());
            txtFirstName.setText(customer.getFirstName());
            txtLastName.setText(customer.getLastName());
            txtEmail.setText(customer.getEmail());
            txtAddress.setText(customer.getAddress());
            txtDNI.setText(customer.getDni());
            txtPhoneNumber.setText(customer.getPhoneNumber());
        } 
    }

    public void blockFields() {
        cbxEnabled.setDisabled(true);
        txtFirstName.setReadonly(true);
        txtLastName.setReadonly(true);
        txtEmail.setReadonly(true);
        txtDNI.setReadonly(true);
        txtAddress.setReadonly(true);
//        txtCountryCode.setReadonly(true);
//        txtRegionCode.setReadonly(true);
        txtPhoneNumber.setReadonly(true);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
    	if (txtFirstName.getText().isEmpty()) {
            txtFirstName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtLastName.getText().isEmpty()) {
            txtLastName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtEmail.getText().isEmpty()) {
            txtEmail.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtCountryCode.getText().isEmpty()) {
//            txtCountryCode.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
//        } else if (txtRegionCode.getText().isEmpty()) {
//            txtRegionCode.setFocus(true);
//            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtPhoneNumber.getText().isEmpty()) {
            txtPhoneNumber.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }   else if (txtAddress.getText().isEmpty()) {
            txtAddress.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtDNI.getText().isEmpty()) {
        	txtDNI.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        }  else {
            return true;
        }
        return false;

    }

    private void save(Customer _customer) {
        try {
            Customer customer = new Customer();
            if (_customer != null) {
               customer.setId(customerParam.getId());
               customer.setCreationDate(customerParam.getCreationDate());
            }
            customer.setAddress(txtAddress.getText());
            customer.setFirstName(txtFirstName.getText());
            customer.setLastName(txtLastName.getText());
            customer.setEmail(txtEmail.getText());
            customer.setPhoneNumber(txtPhoneNumber.getText());
            customer.setEnabled(cbxEnabled.isChecked());
            customer.setDni(txtDNI.getText());
            customer.setPhoneNumber(txtPhoneNumber.getText());
            request.setParam(customer);
            customerParam = customerEJB.saveCustomer(request);
            this.showMessage("sp.common.save.success", false, null);
        } catch (Exception ex) {
            showError(ex);
        }
    }

   
    
    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    save(null);
                    break;
                case WebConstants.EVENT_ADD_DESCENDANT:
                    save(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    save(customerParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(customerParam);

                break;
            case WebConstants.EVENT_VIEW:
                loadFields(customerParam);

                break;
            default:
                break;
        }
    }

 
}
