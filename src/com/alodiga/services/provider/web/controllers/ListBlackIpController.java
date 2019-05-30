package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.exceptions.RegisterNotFoundException;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Enterprise;
import com.alodiga.services.provider.commons.models.IpAddress;
import com.alodiga.services.provider.commons.models.IpBlackList;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.Utils;
import com.alodiga.services.provider.web.components.DeleteButton;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;


public class ListBlackIpController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Listbox lbxBlacks;
    private UtilsEJB utilsEJB = null;
    private List<IpBlackList> ipBlackList = null;
    private User currentUser;
    private Profile currentProfile;
    private Button btnAdd;
    private Hlayout addIp;
    private Textbox txtIp;
    private Textbox txtDescription;
    private IpBlackList ipBlack;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            currentUser = AccessControl.loadCurrentUser();
            currentProfile = currentUser.getCurrentProfile(Enterprise.ALODIGA_USA);
            checkPermissions();
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            loadPermission(new IpBlackList());
            startListener();
            loadData();            
        } catch (Exception ex) {
            showError(ex);
        }
    }

     @Override
    public void checkPermissions() {
        try {
            btnAdd.setVisible(PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.ADD_IP));
            permissionChangeStatus = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.CHANGE_PROVIDER_STATUS);
        } catch (Exception ex) {
            showError(ex);
        }

    }

    public void startListener() {
    }
  
    public List<IpBlackList> getFilteredList(String filter) {
        List<IpBlackList> ipBlackListaux = new ArrayList<IpBlackList>();
        for (Iterator<IpBlackList> i = ipBlackList.iterator(); i.hasNext();) {
            IpBlackList tmp = i.next();
            String field = tmp.getIpAddress().toLowerCase();
            if (field.indexOf(filter.trim().toLowerCase()) >= 0) {
                ipBlackListaux.add(tmp);
            }
        }
        return ipBlackListaux;
    }
 

    private void deleteIp(DeleteButton btnDelete, Listitem listItem) {
        try {
            IpBlackList ipBlackList = (IpBlackList) listItem.getValue();
            utilsEJB.deleteIpBlackList(ipBlackList.getIpAddress());
            AccessControl.saveAction(Permission.DELETE_IP, "delete ip address = " + ipBlackList);
            loadData();
        } catch (NullParameterException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
            ex.printStackTrace();
        } 

        
    }

    public void onClick$btnAdd() throws InterruptedException {
        System.out.println("Entro al metodo add");
        IpBlackList ipBlack = new IpBlackList();
        try {
            utilsEJB.loadBlackList(txtIp.getText());
            this.showMessage("sp.crud.ipBlackList.exit", true, null);;

        } catch (RegisterNotFoundException ex) {
            try {
                System.out.println("captura la ip a guardar");
                ipBlack.setIpAddress(txtIp.getText());
                ipBlack.setDate(new Timestamp(new java.util.Date().getTime()));
                utilsEJB.saveIpBlackList(ipBlack);
                txtIp.setText(" ");
                loadData();
            } catch (NullParameterException ex1) {
                ex1.printStackTrace();
            } catch (GeneralException ex1) {
                ex1.printStackTrace();
            }
        } catch (NullParameterException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
            ex.printStackTrace();
        }

    }

    public void onClick$btnDownload() throws InterruptedException {
        try {
            Utils.exportExcel(lbxBlacks, Labels.getLabel("sp.crud.ipBlackList.list"));
        } catch (Exception ex) {
            showError(ex);
        }
    }
 

     private Listcell initDeleteButton(final Long ipBlackList, final Listitem listItem) {
        Listcell cell = new Listcell();
       try {
           cell.setValue("");
           final DeleteButton button = new DeleteButton(listItem);
           button.setTooltiptext(Labels.getLabel("sp.common.actions.delete"));
           button.setClass("open orange");
           button.addEventListener("onClick", new EventListener() {

               @Override
               public void onEvent(Event event) throws Exception {
                   System.out.println("entro al evento/***************");
                   deleteIp(button, listItem);
               }
           });

           button.setParent(cell);
       } catch (Exception ex) {
           showError(ex);
       }
        return cell;
    }

    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void loadData() {
       try {
           List<IpBlackList> list = null;
            permissionDelete = PermissionManager.getInstance().hasPermisssion(currentProfile.getId(), Permission.DELETE_IP);
                try {
                    lbxBlacks.getItems().clear();
                    Listitem item = null;
                    list = utilsEJB.getBlackList();
                    if (list != null && !list.isEmpty()) {
                        addIp.setVisible(true);
                        for (IpBlackList ipBlackList : list) {
                            item = new Listitem();
                            item.setValue(ipBlackList);
                            item.appendChild(new Listcell(ipBlackList.getIpAddress()));
                            item.appendChild(new Listcell(ipBlackList.getDate().toString()));
                             item.appendChild(permissionDelete ? initDeleteButton(ipBlackList.getId(), item) : new Listcell());
                            item.setParent(lbxBlacks);
                        }
                    } else {
                        item = new Listitem();
                        item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.setParent(lbxBlacks);
                    }
                }catch (EmptyListException ex) {
                        Listitem item = new Listitem();
                        item.appendChild(new Listcell(Labels.getLabel("sp.error.empty.list")));
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell());
                        item.setParent(lbxBlacks);
                } catch (Exception ex) {

                }

        } catch (Exception ex) {
            Logger.getLogger(ChangeIpAdressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



