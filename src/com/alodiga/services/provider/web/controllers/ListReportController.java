package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.ReportEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.Report;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.components.ChangeStatusButton;
import com.alodiga.services.provider.web.components.ListcellEditButton;
import com.alodiga.services.provider.web.components.ListcellViewButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.utils.WebConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ListReportController extends GenericAbstractListController<Report> {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxRecords;
    private Textbox txtAlias;
    private ReportEJB reportEJB = null;
    private List<Report> reports = null;
    private User currentUser;
    private Profile currentProfile;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        startListener();
    }

    @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_REPORTS));
            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_REPORTS);
            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_REPORTS);
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_REPORTS_STATUS);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("updateReports", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {

            public void onEvent(Event evt) {
                getData();
                loadList(reports);
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.TURBINES);
            checkPermissions();
            adminPage = "adminReport.zul";
            reportEJB = (ReportEJB) EJBServiceLocator.getInstance().get(EjbConstants.REPORT_EJB);
            getData();
            loadList(reports);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public List<Report> getFilteredList(String filter) {
        List<Report> reportaux = new ArrayList<Report>();
        for (Iterator<Report> i = reports.iterator(); i.hasNext();) {
            Report tmp = i.next();
            String field = tmp.getName();
            if (field.toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0) {
                reportaux.add(tmp);
            }
        }
        return reportaux;
    }

    public void onClick$btnAdd() throws InterruptedException {
        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
        Executions.getCurrent().sendRedirect(adminPage);

    }

    public void onClick$btnDelete() {
    }

    public void loadList(List<Report> list) {
        try {
            lbxRecords.getItems().clear();
            Listitem item = null;
            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
                for (Report report : list) {
                    item = new Listitem();
                    item.setValue(report);
                    item.appendChild(new Listcell(report.getName()));
                    item.appendChild(permissionChangeStatus ? initEnabledButton(report.getEnabled(), item) : new Listcell());
                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, report,Permission.EDIT_REPORTS) : new Listcell());
                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, report,Permission.VIEW_REPORTS) : new Listcell());
                    item.setParent(lbxRecords);
                }
            } else {
                btnDownload.setVisible(false);
                item = new Listitem();
                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.appendChild(new Listcell());
                item.setParent(lbxRecords);
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void getData() {
        reports = new ArrayList<Report>();
        try {
            request.setFirst(0);
            request.setLimit(null);
            reports = reportEJB.getReport(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (EmptyListException ex) {
            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
        } catch (GeneralException ex) {
            showError(ex);
        }
    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.report.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnClear() throws InterruptedException {
        txtAlias.setText("");
    }

    public void onClick$btnSearch() throws InterruptedException {
        try {
            loadList(getFilteredList(txtAlias.getText()));
        } catch (Exception ex) {
            showError(ex);
        }
    }

//    public void loadDataList(List<Report> reports) {
//        lbxRecords.getItems().clear();
//        if (reports == null) {
//            return;
//        }
//        for (Report report : reports) {
//            Listitem item = new Listitem();
//
//            item.setId(report.getId().toString());
//            item.setValue(report);
//            item.appendChild(new Listcell(report.getName()));
//
//            item.appendChild(initEnabledButton(report.getEnabled(), item));
//
//            if (permissionRead) {
//                item.appendChild(new ListcellViewButton(adminView, report));
//            } else {
//                item.appendChild(new Listcell());
//            }
//
//            if (permissionEdit) {
//                item.appendChild(new ListcellEditButton(adminView, report));
//            } else {
//                item.appendChild(new Listcell());
//            }
//
//            item.setParent(lbxReports);
//        }
//    }
    private Component initEnabledButton(final boolean enabled, final Listitem item) {
        Listcell cell = new Listcell();
        final ChangeStatusButton button = new ChangeStatusButton(enabled);
        button.addEventListener("onClick", new EventListener() {

            public void onEvent(Event event) throws Exception {
                enableReport(button, item);
            }
        });

        button.setParent(cell);
        return cell;
    }

    private void enableReport(ChangeStatusButton button, Listitem item) {
        try {
            Report report = (Report) item.getValue();
            button.changeImageStatus(report.getEnabled());
            report.setEnabled(!report.getEnabled());
            item.setValue(report);

            request.setParam(report);
            reportEJB.enableProduct(request);
        } catch (NullParameterException ex) {
            showError(ex);
        } catch (RegisterNotFoundException e) {
            showError(e);
        } catch (GeneralException ex) {
            showError(ex);
        }
    }
}
