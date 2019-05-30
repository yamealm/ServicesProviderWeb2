//package com.alodiga.services.provider.web.controllers;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.util.Date;
//
//import org.zkoss.io.Files;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zul.Button;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.Messagebox;
//
//import com.alodiga.services.provider.commons.ejbs.BillPaymentTimerEJB;
//import com.alodiga.services.provider.commons.ejbs.BillingTimerEJB;
//import com.alodiga.services.provider.commons.ejbs.PrepayNationTopUpUpdateTimerEJB;
//import com.alodiga.services.provider.commons.ejbs.TransferToTimerEJB;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractController;
//
//public class AdminAutomaticServicesController extends GenericAbstractController {
//
//    private Label lblInfo;
//    private Label lblTopUpUpdateDate;
//    private Label lblTopUpUpdateInterval;
//    private Label lblTopUpPrepayNationUpdateDate;
//    private Label lblTopUpPrepayNationUpdateInterval;
//    private Label lblBillPaymentUpdateDate;
//    private Label lblBillingInvoiceUpdate;
//    private Label lblBillPaymentUpdateInterval;
//    private Label lblBillPaymentBillingInterval;
//    private PrepayNationTopUpUpdateTimerEJB prepayNationTimerEJB = null;
//    private TransferToTimerEJB topUpUpdateTimerEJB = null;
//    private BillPaymentTimerEJB billPaymentTimerEJB = null;
//    private BillingTimerEJB billingTimerEJB = null;
//    private Button btnPPNSubmitFile;
//            
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    @Override
//    public void initialize() {
//        try {
//            super.initialize();
//            prepayNationTimerEJB = (PrepayNationTopUpUpdateTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.PREPAY_NATION_TOP_UP_UPDATE_TIMER_EJB);
//            topUpUpdateTimerEJB = (TransferToTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSFER_TO_TIMER_EJB);
//            billPaymentTimerEJB = (BillPaymentTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILL_PAYMENT_UPDATE_TIMER_EJB);
//            billingTimerEJB = (BillingTimerEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLING_TIMER_EJB);
//
//            showPPNExecutionDates();
//            showTTExecutionDates();
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    private void showPPNExecutionDates() {
//        Date date1 = prepayNationTimerEJB.getNextExecutionDate();
//        lblTopUpPrepayNationUpdateDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
//        Long dailyInterval = prepayNationTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
//        lblTopUpPrepayNationUpdateInterval.setValue(dailyInterval.toString());
//
//    }
//
//    private void showTTExecutionDates() {
//        Date date1 = topUpUpdateTimerEJB.getNextExecutionDate();
//        lblTopUpUpdateDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
//        Long dailyInterval = topUpUpdateTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
//        lblTopUpUpdateInterval.setValue(dailyInterval.toString());
//
//    }
//
//    private void showPPNBillPaymentExecutionDates() {
//        Date date1 = billPaymentTimerEJB.getNextExecutionDate();
//        lblBillPaymentUpdateDate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
//        Long dailyInterval = billPaymentTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
//        lblBillPaymentUpdateInterval.setValue(dailyInterval.toString());
//    }
//
//    private void showPPNBillingIvoiceExecutionDates() {
//        Date date1 = billingTimerEJB.getNextExecutionDate();
//        lblBillingInvoiceUpdate.setValue(date1 != null ? date1.toString() : Labels.getLabel("sp.automatic.commission.noDate"));
//        Long dailyInterval = billingTimerEJB.getTimeoutInterval() / 86400000;// 86400000 Milisegundos en un dia
//        lblBillPaymentBillingInterval.setValue(dailyInterval.toString());
//    }
//
//    public void onClick$btnPPNRestart() {
//        try {
//            prepayNationTimerEJB.restart();
//            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
//            showPPNExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnPPNStop() {
//        try {
//            prepayNationTimerEJB.stop();
//            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
//            showPPNExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnPPNTimeout() {
//        try {
//            prepayNationTimerEJB.forceTimeout();
//            String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
//            lblInfo.setValue(response);
//            showPPNExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnPPNNextExecution() {
//        try {
//            showPPNExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnTTRestart() {
//        try {
//
//            topUpUpdateTimerEJB.restart();
//            String response = Labels.getLabel("sp.automatic.commission.success");
//            lblInfo.setValue(response);
//            showTTExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnTTStop() {
//        try {
//            topUpUpdateTimerEJB.stop();
//            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
//            showTTExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnTTTimeout() {
//        try {
//
//            topUpUpdateTimerEJB.forceTimeout();
//            String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
//            lblInfo.setValue(response);
//            showTTExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnTTNextExecution() {
//        try {
//            showTTExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//
//        }
//    }
//
//    public void onClick$btnPNBPRestart() {
//
//        try {
//            billPaymentTimerEJB.restart();
//            String response = Labels.getLabel("sp.automatic.commission.success");
//            lblInfo.setValue(response);
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnBIRestart() {
//
//        try {
//            billingTimerEJB.restart();
//            String response = Labels.getLabel("sp.automatic.commission.success");
//            lblInfo.setValue(response);
//            showPPNBillingIvoiceExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnPNBPStop() {
//
//        try {
//            billPaymentTimerEJB.stop();
//            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnBIPStop() {
//
//        try {
//            billingTimerEJB.stop();
//            lblInfo.setValue(Labels.getLabel("sp.automatic.commission.success"));
//            showPPNBillingIvoiceExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//    }
//
//    public void onClick$btnPNBPTimeout() {
//        try {
//            billPaymentTimerEJB.forceTimeout();
//            String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
//            lblInfo.setValue(response);
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//
//    }
//
//    public void onClick$btnBITimeout() {
//        try {
//            billingTimerEJB.forceTimeout();
//            String response = Labels.getLabel("sp.automatic.commission.timeoutMessage");
//            lblInfo.setValue(response);
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//        }
//
//    }
//
//    public void onClick$btnPNBPNextExecution() {
//        try {
//            showPPNBillPaymentExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//
//        }
//    }
//
//    public void onClick$btnBINextExecution() {
//        try {
//            showPPNBillingIvoiceExecutionDates();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            lblInfo.setValue(Labels.getLabel("sp.error.general"));
//
//        }
//    }
//
//    /*
//      Method in charge of eliminating the file of the temporary directory and 
//      inserts a new one obtained from the event UploadEvent
//      @params event     Type: org.zkoss.zk.ui.event.UploadEvent
//      @authors Jose Nunez
//     */
//    public void onUpload$btnPPNSubmitFile(org.zkoss.zk.ui.event.UploadEvent event) throws Throwable {
//        org.zkoss.util.media.Media media = event.getMedia();
//        
//        if (media != null) {
//            if (validateFormatFile(media)) {
//                File csv = new File("/tmp/" + media.getName());
//                File csvTemp = csv;
//                csvTemp.delete();
//                btnPPNSubmitFile.setDisabled(true);
//                if (media.isBinary()) {
//                    Files.copy(csv, media.getStreamData());
//                } else {
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
//                    Files.copy(writer, media.getReaderData());
//                }
//            }
//        } else {
//            lblInfo.setValue("Error");
//        }
//
////       File fileTmp = new File("/tmp/pricelist_open_range_alodigaor.csv");
////       if (validateFormatFile(media)) {
////           if (media != null) {
////               fileTmp.delete();
////               try {
////                   File csv = new File("/tmp/" + media.getName());
////                   Files.copy(csv, (InputStream) new ByteArrayInputStream(media.getStringData().getBytes()));
////               } catch (Exception ex) {
////                   ex.printStackTrace();
////                   System.out.println(ex.getMessage());
////               }
////               } else {
////                   showMessage("Error", true, null);
////               }
////           }
//    }
//
//    /*
//      Method valid format file of the Media
//      @params media     Type: org.zkoss.util.media.Media
//      @return boolean   type: boolean
//      @authors Jose Nunez
//     */
//    private boolean validateFormatFile(org.zkoss.util.media.Media media) throws InterruptedException {
//        if (!(media.getName().equalsIgnoreCase("pricelist_open_range_alodigaor.csv"))) {
//            Messagebox.show(Labels.getLabel("sp.error.fileupload.invalid.file"), "Advertencia", 0, Messagebox.EXCLAMATION);
//            return false;
//        }
//        
//        return true;
//    }
//
//}
