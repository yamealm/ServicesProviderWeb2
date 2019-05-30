package com.alodiga.services.provider.web.controllers;

import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.ProductEJB;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.genericEJB.AbstractSPEntity;
import com.alodiga.services.provider.commons.models.Provider;
import com.alodiga.services.provider.commons.models.SMS;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.generic.controllers.GenericSPController;

public class SMSTestingController extends GenericAbstractController implements GenericSPController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtSender;
    private Textbox txtDestination;
    private Textbox txtContent;
    private Combobox cmbProviders;
    private ProductEJB productEJB = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();

    }

    @Override
    public void initialize() {
        try {
            productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            txtSender.setFocus(true);
            loadProviders();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadProviders() {

        try {
            cmbProviders.getItems().clear();
            Comboitem item = new Comboitem();
            item.setValue("NORMAL");
            item.setLabel("Flujo normal");
            item.setParent(cmbProviders);
            List<Provider> providers = productEJB.getSMSProviders(request);
            for (Provider provider : providers) {
                item = new Comboitem();
                item.setValue(provider);
                item.setLabel(provider.getName());
                item.setParent(cmbProviders);
            }
        } catch (Exception ex) {

            this.showError(ex);
        }
        cmbProviders.setSelectedIndex(0);
    }

    public boolean validateEmpty() {
        boolean valid = false;
        if (txtSender.getText().isEmpty()) {
            txtSender.setFocus(true);
            this.showMessage("error.field.cannotNull", true, null);
        } else if (txtDestination.getText().isEmpty()) {
            txtDestination.setFocus(true);
            this.showMessage("error.field.cannotNull", true, null);
        } else if (txtContent.getText().isEmpty()) {
            txtContent.setFocus(true);
            this.showMessage("error.field.cannotNull", true, null);
        } else {
            valid = true;
        }
        return valid;
    }

    public void onClick$btnSend() {
        if (validateEmpty()) {
            try {
                SMS sms = new SMS();
                sms.setSender(txtSender.getText());
                sms.setDestination(txtDestination.getText());
                sms.setContent(txtContent.getText());
                Provider provider = (Provider)cmbProviders.getSelectedItem().getValue();
                int pId = Integer.parseInt(provider.getId().toString());
//
//                if (pId == Provider.MLAT) {
//                    new SMSSender().sendMLatSMS(sms);
//
//                } else if (pId == Provider.TELINTEL) {
//                    new SMSSender().sendTelintelSMS(sms);
//                    
//                } else if (pId == Provider.TWILIO) {
//                    new SMSSender().sendTwilioSMS(sms);
//
//                } else {
//                    new SMSSender().sendSMS(sms);
//                    
//                    
//                }
                this.showMessage(Labels.getLabel("sp.common.SMS.sent"), false, null);
//            } catch (SMSFailureException ex) {
//                this.showMessage(ex.getMessage(), true, ex);
            } catch (Exception ex) {
                this.showMessage(ex.getMessage(), true, ex);
            }
        }
    }

    public void onClick$btnCancel() {
        loadProviders();
        txtContent.setText("");
        txtDestination.setText("");
        txtSender.setText("");
    }

    @Override
    public void loadPermission(AbstractSPEntity clazz) throws GeneralException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
