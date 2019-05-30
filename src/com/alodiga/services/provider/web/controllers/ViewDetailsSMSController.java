package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.models.SMS;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
import com.alodiga.services.provider.web.generic.controllers.GenericSPController;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;

public class ViewDetailsSMSController extends GenericAbstractController implements GenericSPController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblDestination;
    private Label lblContent;
    private Label lblProvider;
    private SMS smsParam = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        smsParam = (Sessions.getCurrent().getAttribute("object") != null) ? (SMS) Sessions.getCurrent().getAttribute("object") : null;
        initialize();

    }

    @Override
    public void initialize() {
        try {
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadData() {

        try {
            lblProvider.setValue(smsParam.getIntegratorName());
            lblContent.setValue(smsParam.getContent());
            lblDestination.setValue(smsParam.getDestination());
        } catch (Exception ex) {

            this.showError(ex);
        }
    }

    public void blockFields() {
    }

}
