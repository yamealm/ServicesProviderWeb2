package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.alodiga.services.provider.commons.ejbs.CustomerEJB;
import com.alodiga.services.provider.commons.models.Customer;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AddCustomerController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtDNI;
    private Textbox txtFirstName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Textbox txtPhoneNumber;
    private Textbox txtAddress;
    private Customer customerParam;
    private Window winAddCustomerView;
    private Button btnSave;
    private CustomerEJB customerEJB;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
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

    public void clearFields() {
        
    }

//    private void loadFields(Customer customer) {
//        if (customer != null) {//UPDATE
//            cbxEnabled.setChecked(customer.getEnabled());
//            txtFirstName.setText(customer.getFirstName());
//            txtLastName.setText(customer.getLastName());
//            txtEmail.setText(customer.getEmail());
//            txtAddress.setText(customer.getAddress());
//            txtDNI.setText(customer.getDni());
//            txtPhoneNumber.setText(customer.getPhoneNumber());
//        } 
//    }

    public void blockFields() {
//        cbxEnabled.setDisabled(true);
        txtFirstName.setReadonly(true);
        txtLastName.setReadonly(true);
        txtEmail.setReadonly(true);
        txtDNI.setReadonly(true);
        txtAddress.setReadonly(true);
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
            customer.setCreationDate(new Timestamp((new java.util.Date().getTime())));
            customer.setEnabled(true);
            customer.setDni(txtDNI.getText());
            customer.setPhoneNumber(txtPhoneNumber.getText());
            request.setParam(customer);
            customerParam = customerEJB.saveCustomer(request);
            blockFields();
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
			break;
		case WebConstants.EVENT_VIEW:
			break;
		case WebConstants.EVENT_ADD:
			break;
		default:
			break;
		}

	}
	
	 public void onClick$btnBack() {
		 winAddCustomerView.detach();
	   	 Window window = (Window)Executions.createComponents("catCustomers.zul", null, null);
	        try {
				window.doModal();
			} catch (SuspendNotAllowedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
}
