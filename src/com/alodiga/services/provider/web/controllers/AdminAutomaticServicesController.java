package com.alodiga.services.provider.web.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.zkoss.io.Files;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import com.alodiga.services.provider.commons.ejbs.AutomaticProcessControlTimerEJB;
import com.alodiga.services.provider.commons.ejbs. AutomaticProcessTimerEJB;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;

public class AdminAutomaticServicesController extends GenericAbstractController {

    private Label lblInfo;
    private Label lblTopUpUpdateDate;
    private Label lblTopUpUpdateInterval;
    private Label lblUpdateDate;
    private Label lblUpdateInterval;
    private AutomaticProcessTimerEJB automaticProcessTimerEJB = null;
    private AutomaticProcessControlTimerEJB automaticProcessControlTimerEJB = null;
            
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            automaticProcessTimerEJB = (AutomaticProcessTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUTOMATIC_PROCESS_TIMER_EJB);
            automaticProcessControlTimerEJB = (AutomaticProcessControlTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUTOMATIC_PROCESS_CONTROL_TIMER_EJB);

            showExecutionDates();
            showTTExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    private void showExecutionDates() {
        Date date1 = automaticProcessTimerEJB.getNextExecutionDate();
        lblUpdateDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
        Long dailyInterval = automaticProcessTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
        lblUpdateInterval.setValue(dailyInterval.toString());

    }
	
    public void onClick$btnRestart() {
        try {
        	automaticProcessTimerEJB.restart();
            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
            showExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    public void onClick$btnStop() {
        try {
        	automaticProcessTimerEJB.stop();
            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
            showExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    public void onClick$btnTimeout() {
        try {
        	automaticProcessTimerEJB.forceTimeout();
            String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
            lblInfo.setValue(response);
            showExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    public void onClick$btnNextExecution() {
        try {
            showExecutionDates();
        } catch (Exception ex) {
            ex.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }
  
    private void showTTExecutionDates() {
    	Date date1 = automaticProcessControlTimerEJB.getNextExecutionDate();
    	lblTopUpUpdateDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
    	Long dailyInterval = automaticProcessControlTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
    	lblTopUpUpdateInterval.setValue(dailyInterval.toString());
    	
    }
    
	public void onClick$btnTTRestart() {
		try {
			automaticProcessControlTimerEJB.restart();
			String response = Labels.getLabel("sp.automatic.commission.success");
			lblInfo.setValue(response);
			showTTExecutionDates();
		} catch (Exception ex) {
			ex.printStackTrace();
			lblInfo.setValue(Labels.getLabel("sp.error.general"));
		}
	}

	public void onClick$btnTTStop() {
		try {
			automaticProcessControlTimerEJB.stop();
			lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
			showTTExecutionDates();
		} catch (Exception ex) {
			ex.printStackTrace();
			lblInfo.setValue(Labels.getLabel("sp.error.general"));
		}
	}   
	
	public void onClick$btnTTTimeout() {
		try {

			automaticProcessControlTimerEJB.forceTimeout();
			String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
			lblInfo.setValue(response);
			showTTExecutionDates();
		} catch (Exception ex) {
			ex.printStackTrace();
			lblInfo.setValue(Labels.getLabel("sp.error.general"));
		}
	}
	
	public void onClick$btnTTNextExecution() {
		try {
			showTTExecutionDates();
		} catch (Exception ex) {
			ex.printStackTrace();
			lblInfo.setValue(Labels.getLabel("sp.error.general"));

		}
	}
		
}
