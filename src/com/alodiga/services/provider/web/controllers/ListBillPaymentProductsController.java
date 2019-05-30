//package com.alodiga.services.provider.web.controllers;
//
//import com.alodiga.services.provider.commons.ejbs.BillPaymentEJB;
//import com.alodiga.services.provider.commons.exceptions.EmptyListException;
//import com.alodiga.services.provider.commons.exceptions.GeneralException;
//import com.alodiga.services.provider.commons.managers.PermissionManager;
//import com.alodiga.services.provider.commons.models.Enterprise;
//import com.alodiga.services.provider.commons.models.Permission;
//import com.alodiga.services.provider.commons.models.Profile;
//import com.alodiga.services.provider.commons.models.User;
//import com.alodiga.services.provider.commons.models.billPayment.BillPaymentProduct;
//import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
//import com.alodiga.services.provider.commons.utils.EjbConstants;
//import com.alodiga.services.provider.web.components.ChangeStatusButton;
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
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zk.ui.event.EventQueue;
//import org.zkoss.zk.ui.event.EventQueues;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Listcell;
//import org.zkoss.zul.Listitem;
//import org.zkoss.zul.Textbox;
//
//public class ListBillPaymentProductsController extends GenericAbstractListController<BillPaymentProduct> {
//
//    private static final long serialVersionUID = -9145887024839938515L;
//    private Listbox lbxRecords;
//    private Textbox txtAlias;
//    private BillPaymentEJB billPaymentEJB = null;
//    private List<BillPaymentProduct> billPaymentProducts = null;
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
//            permissionEdit = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.EDIT_BILL_PAYMENT);
//            permissionRead = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.VIEW_BILL_PAYMENT);
//            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_BILL_PAYMENT_STATUS);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//
//    }
//
//    public void startListener() {
//        EventQueue que = EventQueues.lookup("updateProfiles", EventQueues.APPLICATION, true);
//        que.subscribe(new EventListener() {
//
//            public void onEvent(Event evt) {
//                getData();
//                loadList(billPaymentProducts);
//            }
//        });
//    }
//
//    @Override
//    public void initialize() {
//        super.initialize();
//        try {
//            currentUser = AccessControl.loadCurrentUser();
//            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
//            checkPermissions();
//            adminPage = "adminBillPayment.zul";
//            billPaymentEJB = (BillPaymentEJB) EJBServiceLocator.getInstance().get(EjbConstants.BILLPAYMENT_EJB);
//            getData();
//            loadList(billPaymentProducts);
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    private Listcell initEnabledButton(final Boolean enabled, final Listitem listItem) {
//
//        Listcell cell = new Listcell();
//        cell.setValue("");
//        final ChangeStatusButton button = new ChangeStatusButton(enabled);
//        button.setTooltiptext(Labels.getLabel("sp.common.actions.changeStatus"));
//        button.setClass("open orange");
//        button.addEventListener("onClick", new EventListener() {
//
//            public void onEvent(Event event) throws Exception {
//                changeStatus(button, listItem);
//            }
//        });
//
//        button.setParent(cell);
//        return cell;
//    }
//
//    public List<BillPaymentProduct> getFilteredList(String filter) {
//        List<BillPaymentProduct> list = new ArrayList<BillPaymentProduct>();
//        for (Iterator<BillPaymentProduct> i = billPaymentProducts.iterator(); i.hasNext();) {
//            BillPaymentProduct tmp = i.next();
//
//            if (tmp.getName().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0
//                    || (tmp.getReferenceCode() != null && tmp.getReferenceCode().toLowerCase().indexOf(filter.trim().toLowerCase()) >= 0)) {
//                list.add(tmp);
//            }
//        }
//        return list;
//    }
//
//    private void changeStatus(ChangeStatusButton button, Listitem listItem) {
//        try {
//            BillPaymentProduct product = (BillPaymentProduct) listItem.getValue();
//            button.changeImageStatus(product.getEnabled());
//            product.setEnabled(!product.getEnabled());
//            listItem.setValue(product);
//            request.setParam(product);
//            billPaymentEJB.saveBillPaymentProduct(request);
//
//        } catch (Exception ex) {
//            showError(ex);
//        }
//    }
//
//    public void loadList(List<BillPaymentProduct> list) {
//        try {
//            lbxRecords.getItems().clear();
//            Listitem item = null;
//            if (list != null && !list.isEmpty()) {
//                btnDownload.setVisible(true);
//                for (BillPaymentProduct paymentProduct : list) {
//
//                    item = new Listitem();
//                    item.setValue(paymentProduct);
//                    item.appendChild(new Listcell(paymentProduct.getName()));
//                    item.appendChild(new Listcell(paymentProduct.getReferenceCode()));
//                    item.appendChild(new Listcell(paymentProduct.getProvider().getName()));
//                    item.appendChild(new Listcell(paymentProduct.getCountry().getName()));
//                    item.appendChild(new Listcell(paymentProduct.getProviderFee().toString()));
//                    item.appendChild(new Listcell(paymentProduct.getMinAmount().toString()));
//                    item.appendChild(new Listcell(paymentProduct.getMaxAmount().toString()));
//                    item.appendChild(permissionChangeStatus ? initEnabledButton(paymentProduct.getEnabled(), item) : new Listcell());
//                    item.appendChild(permissionEdit ? new ListcellEditButton(adminPage, paymentProduct,Permission.EDIT_BILL_PAYMENT) : new Listcell());
//                    item.appendChild(permissionRead ? new ListcellViewButton(adminPage, paymentProduct,Permission.VIEW_BILL_PAYMENT) : new Listcell());
//                    item.setParent(lbxRecords);
//
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
//        billPaymentProducts = new ArrayList<BillPaymentProduct>();
//        try {
//            billPaymentProducts = billPaymentEJB.getActiveBillPaymentProducts();
//        } catch (EmptyListException ex) {
//            //lblInfo.setValue(Labels.getLabel("error.empty.list"));
//        } catch (GeneralException ex) {
//            showError(ex);
//        }
//    }
//
//    public void onClick$btnDownload() throws InterruptedException {
//        try {
//            Utils.exportExcel(lbxRecords, Labels.getLabel("sp.crud.billPayment.list"));
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
//
//    public void onClick$btnAdd() throws InterruptedException {
//       Sessions.getCurrent().setAttribute("eventType", WebConstants.EVENT_ADD);
//       Executions.getCurrent().sendRedirect(adminPage);
//    }
//}
