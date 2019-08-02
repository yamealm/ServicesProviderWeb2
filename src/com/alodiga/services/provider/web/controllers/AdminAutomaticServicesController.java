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

import com.alodiga.services.provider.commons.ejbs. AutomaticProcessTimerEJB;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;

public class AdminAutomaticServicesController extends GenericAbstractController {

    private Label lblInfo;

    private Label lblUpdateDate;
    private Label lblUpdateInterval;
    private AutomaticProcessTimerEJB automaticProcessTimerEJB = null;
    private Button btnPPNSubmitFile;
            
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

            showExecutionDates();

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

  

    /*
      Method in charge of eliminating the file of the temporary directory and 
      inserts a new one obtained from the event UploadEvent
      @params event     Type: org.zkoss.zk.ui.event.UploadEvent
      @authors Jose Nunez
     */
    public void onUpload$btnPPNSubmitFile(org.zkoss.zk.ui.event.UploadEvent event) throws Throwable {
        org.zkoss.util.media.Media media = event.getMedia();
        
        if (media != null) {
            if (validateFormatFile(media)) {
                File csv = new File("/tmp/" + media.getName());
                File csvTemp = csv;
                csvTemp.delete();
                btnPPNSubmitFile.setDisabled(true);
                if (media.isBinary()) {
                    Files.copy(csv, media.getStreamData());
                } else {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
                    Files.copy(writer, media.getReaderData());
                }
            }
        } else {
            lblInfo.setValue("Error");
        }

//       File fileTmp = new File("/tmp/pricelist_open_range_alodigaor.csv");
//       if (validateFormatFile(media)) {
//           if (media != null) {
//               fileTmp.delete();
//               try {
//                   File csv = new File("/tmp/" + media.getName());
//                   Files.copy(csv, (InputStream) new ByteArrayInputStream(media.getStringData().getBytes()));
//               } catch (Exception ex) {
//                   ex.printStackTrace();
//                   System.out.println(ex.getMessage());
//               }
//               } else {
//                   showMessage("Error", true, null);
//               }
//           }
    }

    /*
      Method valid format file of the Media
      @params media     Type: org.zkoss.util.media.Media
      @return boolean   type: boolean
      @authors Jose Nunez
     */
    private boolean validateFormatFile(org.zkoss.util.media.Media media) throws InterruptedException {
        if (!(media.getName().equalsIgnoreCase("pricelist_open_range_alodigaor.csv"))) {
            Messagebox.show(Labels.getLabel("sp.error.fileupload.invalid.file"), "Advertencia", 0, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }

}
