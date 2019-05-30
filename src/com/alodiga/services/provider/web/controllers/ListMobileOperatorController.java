//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.TopUpProductEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.exceptions.NullParameterException;
//import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
//import com.alodiga.services.provider.commons.managers.PermissionManager;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.MobileOperator;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.components.ListcellEditButton;
//import com.alodiga.services.provider.web.components.ListcellViewButton;
//import com.alodiga.services.provider.web.generic.controllers.GenericAbstractListController;
//import com.alodiga.services.provider.web.utils.AccessControl;
//import com.alodiga.services.provider.web.utils.Utils;
//import com.alodiga.services.provider.web.utils.WebConstants;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import org.zkoss.util.resource.Labels;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//public class ListMobileOperatorController extends GenericAbstractListController<MobileOperator> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxRecords;
//    private Textbox txtAlias;
//    private TopUpProductEJB topUpEJB = null;
//    private List<MobileOperator> mobileOperators = null;
//    private User currentUser;
//    private Profile currentProfile;
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        initialize();
//    }
//
//    @Override
//    public void checkPermissions() {
//        try {
//            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_MOBILE_OPERATOR));
//            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_MOBILE_OPERATOR);
//            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_MOBILE_OPERATOR);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    public void startListener() {
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            currentUser = AccessControl.loadCurrentUser();
//            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
//            checkPermissions();
//            adminPage = "adminMobileOperator.zul";
//            topUpEJB = (TopUpProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.TOP_UP_EJB);
//            getData();
//            loadList(mobileOperators);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public List<MobileOperator> getFilteredList(String filter) {
//        List<MobileOperator> mobileOperatoraux = new ArrayList<MobileOperator>();
//        for (Iterator<MobileOperator> i = mobileOperators.iterator(); i.hasNext();) {
//            MobileOperator tmp = i.next();
//
//            if (tmp.getName().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0
//                    || (tmp.getAlternativeName1()!=null && tmp.getAlternativeName1().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getAlternativeName2()!=null && tmp.getAlternativeName2().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getAlternativeName3()!=null && tmp.getAlternativeName3().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getCountry().getName()!=null && tmp.getCountry().getName().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getCountry().getAlternativeName1()!=null && tmp.getCountry().getAlternativeName1().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getCountry().getAlternativeName2()!=null && tmp.getCountry().getAlternativeName2().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)
//                    || (tmp.getCountry().getAlternativeName3()!=null && tmp.getCountry().getAlternativeName3().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)) {
//                mobileOperatoraux.add(tmp);
//            }
//        }
//        return mobileOperatoraux;
//    }
//
//    public void onClick$btnAdd() throws InterruptedException {
//        Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
//        Executions.getCurrent().sendRedirect(adminPage);
//
//    }
//
//    public void onClick$btnDelete() {
//    }
//
//    public void loadList(List<MobileOperator> list) {
//        try {
//            lbxRecords.getItems().clear();
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
////                btnDownload.setVisible(true);
//                for (MobileOperator mobileOperator : list) {
//                    item = new Listitem();
//                    item.setValue(mobileOperator);
//                    item.appendChild(new Listcell(mobileOperator.getName()));
//                    item.appendChild(new Listcell(mobileOperator.getAlternativeName1()));
//                    item.appendChild(new Listcell(mobileOperator.getAlternativeName2()));
//                    item.appendChild(new Listcell(mobileOperator.getAlternativeName3()));
//                    item.appendChild(new Listcell(mobileOperator.getCountry().getName()));
//                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, mobileOperator,Permission.EDIT_MOBILE_OPERATOR) : new Listcell());
//                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, mobileOperator,Permission.EDIT_MOBILE_OPERATOR) : new Listcell());
//                    item.setParent(lbxRecords);
//                }
//            } else {
//                btnDownload.setVisible(false);
//                item = new Listitem();
//                item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.appendChild(new Listcell());
//                item.setParent(lbxRecords);
//            }
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void getData() {
//        mobileOperators = new ArrayList<MobileOperator>();
//        try {
//            request.setFirst(0);
//            request.setLimit(null);
//            mobileOperators = topUpEJB.getMobileOperators(request);
//        } catch (RegisterNotFoundException ex) {
//            showError(ex);
//        } catch (NullParameterException ex) {
//            showError(ex);
//        } catch (EmptyListException ex) {
//            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        try {
//            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.mobileOperator.list"));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnClear() throws InterruptedException {
//        txtAlias.setText("");
//    }
//
//    public void onClick$btnSearch() throws InterruptedException {
//        try {
//            loadList(getFilteredList(txtAlias.getText()));
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//}
