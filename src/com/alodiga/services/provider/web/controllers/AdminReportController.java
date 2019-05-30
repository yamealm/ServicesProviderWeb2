/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.ejbs.ReportEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.ParameterType;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Report;
import com.alodiga.services.provider.commons.models.ReportHasProfile;
import com.alodiga.services.provider.commons.models.ReportParameter;
import com.alodiga.services.provider.commons.models.ReportType;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.PermissionConstants;
import com.alodiga.services.provider.web.components.CustomListCell;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author desarrollog3
 */
public class AdminReportController extends GenericAbstractAdminController {
    private static final long serialVersionUID = -9145887024839938515L;
    private ReportEJB reportEJB = null;
    private AccessControlEJB accessControlEJB = null;
    private Report reportParam = null;
    private List<ParameterType> parameterTypes;
    private Window winReportAdmin;
    private Label lblInfo;
    private Textbox txtName;
    private Textbox txtDescription;
    private Textbox txtQuery;
    private Textbox txtURLWebService;
    private Listbox listbox;
    private Listbox lboxProfile;
    private Checkbox cbxEnabled;
    private Combobox cmbReportType;

     @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        reportParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Report) Sessions.getCurrent().getAttribute("object") : null;
        initialize();
        initView(eventType, "sp.crud.report");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            reportEJB = (ReportEJB) EJBServiceLocator.getInstance().get(EjbConstants.REPORT_EJB);
            accessControlEJB = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    @Override
     public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.report");
    }


    @Override
    public void clearFields() {
        txtName.setRawValue(null);
        txtDescription.setRawValue(null);
        txtQuery.setRawValue(null);
        txtURLWebService.setRawValue(null);
        cbxEnabled.setChecked(false);

        List<Listitem> items = (List<Listitem>) listbox.getItems();
        if (items != null && items.size() > 0) {
            for (Listitem item : items) {
                listbox.removeItemAt(item.getIndex());
            }
        }
         items = (List<Listitem>) lboxProfile.getItems();
        if (items != null && items.size() > 0) {
            for (Listitem item : items) {
                lboxProfile.removeItemAt(item.getIndex());
            }
        }

    }

    public void onClick$btnCancel() {
        clearFields();
    }

    public boolean validateEmpty() {
        boolean valid = false;
        if (txtName.getText().isEmpty()) {
            txtName.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtDescription.getText().isEmpty()) {
            txtDescription.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (cmbReportType.getSelectedItem() == null) {
            cmbReportType.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else {
            valid = true;
        }
        return valid;
    }

     public void loadData() {
        try {
            try {
                request.setFirst(null);
                request.setLimit(null);
                parameterTypes = reportEJB.getParameterType(request);
            } catch (EmptyListException e) {
                lblInfo.setValue(Labels.getLabel("sp.error.general"));
                e.printStackTrace();
            } catch (GeneralException e) {
                lblInfo.setValue(Labels.getLabel("sp.error.general"));
                e.printStackTrace();
            } catch (NullParameterException e) {
                lblInfo.setValue(Labels.getLabel("sp.error.general"));
                e.printStackTrace();
            }
            loadcmbReportType();

            request.setFirst(null);
            request.setLimit(null);
            List<Profile> profiles = accessControlEJB.getProfiles(request);
            if (eventType != WebConstants.EVENT_ADD) {
                txtName.setText(reportParam.getName());
                txtDescription.setText(reportParam.getDescription());
                txtQuery.setText(reportParam.getQuery());
                txtURLWebService.setText(reportParam.getWebServiceUrl());
                cbxEnabled.setChecked(reportParam.getEnabled());
                //Se cargan los parametros
                loadParameters(reportParam.getReportParameters());
            }
            loadProfiles(profiles);
        } catch (Exception e) {
            e.printStackTrace();
            lblInfo.setValue(Labels.getLabel("sp.error.general"));
        }
    }

    private void loadcmbReportType() {

        List<ReportType> reportTypes = new ArrayList<ReportType>();
        EJBRequest request = new EJBRequest();
        try {
            reportTypes = reportEJB.getReportTypes(request);
            for (ReportType rp : reportTypes) {
                Comboitem item = new Comboitem();
                item.setWidth("140px");
                item.setValue(rp);
                item.setLabel(rp.getName());
                item.setParent(cmbReportType);

                if (eventType.equals(WebConstants.EVENT_EDIT) || eventType.equals(WebConstants.EVENT_VIEW)) {
                    if (rp.getId().equals(reportParam.getReportType().getId())) {
                        cmbReportType.setSelectedItem(item);
                    }
                }
            }
            if (eventType.equals(WebConstants.EVENT_ADD)) {
                cmbReportType.setText(Labels.getLabel("sp.common.select"));
            }

        } catch (EmptyListException e) {
            e.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        } catch (GeneralException e) {
            e.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        } catch (NullParameterException e) {
            e.printStackTrace();
            this.showMessage("sp.error.general", true, null);
        }
    }

    private void loadParameters(List<ReportParameter> reportParameters) {
        for (ReportParameter reportParameter : reportParameters) {
            Listitem listItem = new Listitem();
            Listcell tmpCell = new Listcell();

            listItem.setValue(reportParameter);

            if (!eventType.equals(WebConstants.EVENT_VIEW)) {
                listItem.appendChild(initDeleteButton(listItem));
            } else {
                listItem.appendChild(tmpCell);
            }

            listItem.appendChild(new CustomListCell(reportParameter.getName(), "120px", CustomListCell.TYPE_TEXT, eventType == WebConstants.EVENT_VIEW));//name

            // parameters type
            Combobox cmbParameterType = new Combobox();
            cmbParameterType.setText(Labels.getLabel("sp.common.select"));
            cmbParameterType.setWidth("100px");
            cmbParameterType.setReadonly(true);
            cmbParameterType.setMold("rounded");
            for (ParameterType parameterType : parameterTypes) {
                Comboitem item = new Comboitem();
                item.setValue(parameterType);
                item.setLabel(parameterType.getName());
                item.setParent(cmbParameterType);
                if (eventType != WebConstants.EVENT_ADD) {
                    if (reportParameter.getParameterType().getId().equals(parameterType.getId())) {
                        cmbParameterType.setSelectedItem(item);
                    }
                }
            }
            tmpCell = new Listcell();
            cmbParameterType.setReadonly(eventType == WebConstants.EVENT_VIEW);
            cmbParameterType.setParent(tmpCell);
            listItem.appendChild(tmpCell);// parameter type

            listItem.appendChild(new CustomListCell(reportParameter.getDefaultValue(), "120px", CustomListCell.TYPE_TEXT, eventType == WebConstants.EVENT_VIEW));// value
            listItem.appendChild(new CustomListCell(reportParameter.getIndexOrder().toString(), "50px", CustomListCell.TYPE_INT, eventType == WebConstants.EVENT_VIEW));// index

            tmpCell = new Listcell();
            Checkbox chkRequired = new Checkbox();
            chkRequired.setChecked(reportParameter.getRequired());
            chkRequired.setDisabled(eventType == WebConstants.EVENT_VIEW);
            chkRequired.setParent(tmpCell);
            listItem.appendChild(tmpCell);// required

            listItem.setParent(listbox);
        }
    }

    private void loadProfiles(List<Profile> profiles) {
        List<ReportHasProfile> reportHasProfiles = new ArrayList<ReportHasProfile>();
        if (!eventType.equals(WebConstants.EVENT_ADD)) {
            reportHasProfiles = reportParam.getReportHasProfiles();
        }
        for (Profile profile : profiles) {
            Listitem listItem = new Listitem();
            listItem.setValue(profile);
            listItem.appendChild(new Listcell());
            listItem.appendChild(new Listcell(profile.getName()));//name
            if (!eventType.equals(PermissionConstants.EVENT_ADD)) {
                for (ReportHasProfile reportProfile : reportHasProfiles) {
                    if (reportProfile.getProfile().getId().equals(profile.getId())) {
                        listItem.setSelected(true);
                    }
                }
            }

            listItem.setParent(lboxProfile);
        }
    }

    public void blockFields() {
        txtName.setReadonly(true);
        txtDescription.setReadonly(true);
        txtQuery.setReadonly(true);
        txtURLWebService.setReadonly(true);
        cbxEnabled.setDisabled(true);
        cmbReportType.setDisabled(true);
        List<Listitem> list = listbox.getItems();
        for (Listitem item : list) {
            item.setDisabled(true);
        }
    }

    public void onClick$btnAddParam() {
        Listitem listItem = new Listitem();
        Listcell tmpCell = new Listcell();

        listItem.appendChild(initDeleteButton(listItem));// check

        listItem.appendChild(new CustomListCell("", "120px", CustomListCell.TYPE_TEXT, eventType == WebConstants.EVENT_VIEW));//name

        // parameters type
        Combobox cmbParameterType = new Combobox();
        cmbParameterType.setText(Labels.getLabel("sp.common.select"));
        cmbParameterType.setWidth("100px");
        cmbParameterType.setReadonly(true);
        cmbParameterType.setMold("rounded");
        for (ParameterType parameterType : parameterTypes) {
            Comboitem item = new Comboitem();
            item.setValue(parameterType);
            item.setLabel(parameterType.getName());
            item.setParent(cmbParameterType);
        }
        tmpCell = new Listcell();
        cmbParameterType.setReadonly(eventType == WebConstants.EVENT_VIEW);
        cmbParameterType.setParent(tmpCell);
        listItem.appendChild(tmpCell);// parameter type

        listItem.appendChild(new CustomListCell("", "120px", CustomListCell.TYPE_TEXT, eventType == WebConstants.EVENT_VIEW));// value
        listItem.appendChild(new CustomListCell(null, "50px", CustomListCell.TYPE_INT, eventType == WebConstants.EVENT_VIEW));// index

        tmpCell = new Listcell();
        Checkbox chkRequired = new Checkbox();
        chkRequired.setDisabled(eventType == WebConstants.EVENT_VIEW);
        chkRequired.setParent(tmpCell);
        listItem.appendChild(tmpCell);// required

        listItem.setParent(listbox);
    }

    private Listcell initDeleteButton(final Listitem listItem) {
        Listcell cell = new Listcell();
        final Button button = new Button();
        button.setImage("/images/icon-remove.png");
        button.setWidth("40px");
        button.addEventListener("onClick", new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                listItem.getParent().removeChild(listItem);
            }
        });

        button.setParent(cell);
        return cell;
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveData(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveData(reportParam.getId());
                    break;
            }
        }
    }

    private void saveData(Long id) {
        Report newReport = new Report();
        try {
            newReport.setId(id);
            newReport.setName(txtName.getText());
            newReport.setDescription(txtDescription.getText());
            newReport.setQuery(txtQuery.getText());
            newReport.setWebServiceUrl(txtURLWebService.getText());
            newReport.setEnabled(cbxEnabled.isChecked());
            ReportType rptNew = (ReportType) cmbReportType.getSelectedItem().getValue();
            newReport.setReportType(rptNew);
            // Parameters
            List<ReportParameter> reportParameters = getReportParameters(newReport);
            newReport.setReportParameters(reportParameters);

            // Perfiles
            List<ReportHasProfile> reportHasProfiles = new ArrayList<ReportHasProfile>();
            Set<Listitem> items = lboxProfile.getSelectedItems();

            Iterator<Listitem> iter = items.iterator();
            while (iter.hasNext()) {
                ReportHasProfile reportHasProfile = new ReportHasProfile();
                reportHasProfile.setProfile((Profile) iter.next().getValue());
                reportHasProfile.setReport(newReport);
                reportHasProfiles.add(reportHasProfile);
            }

            newReport.setReportHasProfiles(reportHasProfiles);

            if (eventType == WebConstants.EVENT_EDIT) {
                request.setParam(id);
                reportEJB.deleteReportParameter(request);

                request.setParam(id);
                reportEJB.deleteProfileReports(request);
            }

            request.setParam(newReport);
            reportParam = reportEJB.saveReport(request);
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
        } catch (NullParameterException e) {
            e.printStackTrace();
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    private List<ReportParameter> getReportParameters(Report report) {
        List<ReportParameter> list = new ArrayList<ReportParameter>();
        List<Listitem> items = listbox.getItems();

        for (Listitem item : items) {
            ReportParameter reportParameter = new ReportParameter();
            Listcell cell = new Listcell();
            InputElement txt = null;
            Checkbox check = null;
            Combobox cmb = new Combobox();

            List<Listcell> cells = item.getChildren();
            txt = (Textbox) cells.get(1).getChildren().get(0);// Name
            reportParameter.setName(txt.getText());
            cmb = (Combobox) cells.get(2).getChildren().get(0);// type
            reportParameter.setParameterType((ParameterType) cmb.getSelectedItem().getValue());
            txt = (Textbox) cells.get(3).getChildren().get(0);// default value
            reportParameter.setDefaultValue(txt.getText());
            txt = (Intbox) cells.get(4).getChildren().get(0);// index
            reportParameter.setIndexOrder(Integer.valueOf(txt.getText()));
            check = (Checkbox) cells.get(5).getChildren().get(0);// required
            reportParameter.setRequired(check.isChecked());
            reportParameter.setReport(report);


            list.add(reportParameter);
        }

        return list;
    }
}
