package com.alodiga.services.provider.web.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.alodiga.services.provider.commons.ejbs.AccessControlEJB;
import com.alodiga.services.provider.commons.ejbs.AuditoryEJB;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.managers.PermissionManager;
import com.alodiga.services.provider.commons.models.Audit;
import com.alodiga.services.provider.commons.models.Event;
import com.alodiga.services.provider.commons.models.Language;
import com.alodiga.services.provider.commons.models.Permission;
import com.alodiga.services.provider.commons.models.PermissionHasProfile;
import com.alodiga.services.provider.commons.models.Profile;
import com.alodiga.services.provider.commons.models.ProfileData;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import com.alodiga.services.provider.web.utils.WebConstants;

public class AdminProfileController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Textbox txtAlias;
    private Textbox txtAliasSpanish;
    private Textbox txtAliasEnglish;
    private Textbox txtDescriptionSpanish;
    private Textbox txtDescriptionEnglish;
    private Checkbox cbxEnabled;
    private Listbox lbxPermissions;
    private AccessControlEJB accessEjb = null;
    private AuditoryEJB auditoryEJB;
    private Profile profileParam;
    Profile parentProfile = null;
    private Button btnSave;
    private User user;
    private String ipAddress;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        profileParam = (Sessions.getCurrent().getAttribute("object") != null) ? (Profile) Sessions.getCurrent().getAttribute("object") : null;
        user = AccessControl.loadCurrentUser();
        initialize();
        initView(eventType, "sp.crud.profile");
    }

    @Override
    public void initView(int eventType, String adminView) {
        super.initView(eventType, "sp.crud.profile");
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
        	ipAddress = Executions.getCurrent().getRemoteAddr();
            accessEjb = (AccessControlEJB) EJBServiceLocator.getInstance().get(EjbConstants.ACCESS_CONTROL_EJB);
            auditoryEJB = (AuditoryEJB) EJBServiceLocator.getInstance().get(EjbConstants.AUDITORY_EJB);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
        txtAlias.setRawValue(null);
        txtAliasSpanish.setRawValue(null);
        txtAliasEnglish.setRawValue(null);
        txtDescriptionSpanish.setRawValue(null);
        txtDescriptionEnglish.setRawValue(null);
        cbxEnabled.setChecked(true);
    }

    private void loadFields(Profile profile) {
        txtAlias.setText(profile.getName());
        txtAliasEnglish.setText(profile.getProfileData().get(0).getAlias());
        txtAliasSpanish.setText(profile.getProfileData().get(1).getAlias());
        txtDescriptionEnglish.setText(profile.getProfileData().get(0).getDescription());
        txtDescriptionSpanish.setText(profile.getProfileData().get(1).getDescription());
        cbxEnabled.setChecked(profile.getEnabled());
    }

    public void blockFields() {
        txtAlias.setReadonly(true);
        txtAliasSpanish.setReadonly(true);
        txtAliasEnglish.setReadonly(true);
        txtDescriptionSpanish.setReadonly(true);
        txtDescriptionEnglish.setReadonly(true);
        cbxEnabled.setDisabled(true);
        lbxPermissions.setCheckmark(false);
        btnSave.setVisible(false);
    }

    public Boolean validateEmpty() {
        if (txtAlias.getText().isEmpty()) {
            txtAlias.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAliasEnglish.getText().isEmpty()) {
            txtAliasEnglish.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (txtAliasSpanish.getText().isEmpty()) {
            txtAliasSpanish.setFocus(true);
            this.showMessage("sp.error.field.cannotNull", true, null);
        } else if (lbxPermissions.getSelectedCount() == 0) {
            this.showMessage("sp.error.permission.notSelected", true, null);
        } else {
            return true;
        }
        return false;

    }

    private void loadPermissionsList(Boolean isAdd) {

        List<Permission> permissions = new ArrayList<Permission>();
        try {
            request.setFirst(0);
            request.setFirst(null);
            permissions = accessEjb.getPermissions(request);
            lbxPermissions.getItems().clear();
            if (permissions != null && !permissions.isEmpty()) {
                for (Permission permission : permissions) {
                    if (permission.getEnabled()) {
                        Listitem item = new Listitem();
                        if (!isAdd) {
                            List<PermissionHasProfile> permissionsProfile = profileParam.getPermissionHasProfiles();
                            for (int y = 0; y < permissionsProfile.size(); y++) {
                                Permission p = permissionsProfile.get(y).getPermission();
                                if (p.getId().equals(permission.getId())) {
                                    item.setSelected(true);
                                }
                            }
                        }
                        item.setValue(permission);
                        item.appendChild(new Listcell());
                        item.appendChild(new Listcell(permission.getPermissionDataByLanguageId(languageId) != null ? permission.getPermissionDataByLanguageId(languageId).getAlias() : permission.getName()));
                        item.appendChild(new Listcell(permission.getAction()));
                        item.appendChild(new Listcell(permission.getEntity()));
                        item.setParent(lbxPermissions);
                    }

                }
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void saveProfile(Profile _profile) {
        Profile rolOld = _profile;
        Profile rolNew = null;
        Profile profile = new Profile();
        try {
            profile.setName(txtAlias.getText());
            profile.setEnabled(cbxEnabled.isChecked());

            List<PermissionHasProfile> permissionHasProfiles = new ArrayList<PermissionHasProfile>();
            Set items = lbxPermissions.getSelectedItems();
            List al = new ArrayList(items);
            for (Iterator it = al.iterator(); it.hasNext();) {
                PermissionHasProfile permissionHasProfile = new PermissionHasProfile();
                Listitem li = (Listitem) it.next();
                Permission permission = (Permission) li.getValue();
                permissionHasProfile.setPermission(permission);
                permissionHasProfile.setProfile(profile);
                permissionHasProfiles.add(permissionHasProfile);
            }
            profile.setPermissionHasProfiles(permissionHasProfiles);
            if (_profile != null) {
                profile.setId(_profile.getId());
                List<ProfileData> profileDatas = profileParam.getProfileData();
                if (profileDatas != null) {
                    profileDatas.get(0).setAlias(txtAliasEnglish.getText());
                    profileDatas.get(0).setDescription(txtDescriptionEnglish.getText());
                    profileDatas.get(1).setAlias(txtAliasSpanish.getText());
                    profileDatas.get(1).setDescription(txtDescriptionSpanish.getText());
                    profile.setProfileData(profileDatas);
                }
                accessEjb.deletePermissionHasProfile(_profile.getId());
            } else {
                List<ProfileData> pds = new ArrayList<ProfileData>();
                ProfileData profileDataEnglish = new ProfileData();
                ProfileData profileDataSpanish = new ProfileData();
                Language languageEnglish = new Language();
                Language languageSpanish = new Language();
                languageEnglish.setId(Language.ENGLISH);
                languageSpanish.setId(Language.SPANISH);
                profileDataEnglish.setLanguage(languageEnglish);
                profileDataEnglish.setAlias(txtAliasEnglish.getText());
                profileDataEnglish.setDescription(txtDescriptionEnglish.getText());
                profileDataEnglish.setProfile(profile);
                profileDataSpanish.setLanguage(languageSpanish);
                profileDataSpanish.setAlias(txtAliasSpanish.getText());
                profileDataSpanish.setDescription(txtDescriptionSpanish.getText());
                profileDataSpanish.setProfile(profile);
                pds.add(profileDataEnglish);
                pds.add(profileDataSpanish);
                profile.setProfileData(pds);
            }
            request.setParam(profile);
            profile = accessEjb.saveProfile(request);
            rolNew = profile;
            profileParam = profile;
            eventType = WebConstants.EVENT_EDIT;
            this.showMessage("sp.common.save.success", false, null);
            saveAudit(rolOld, rolNew);
            try {
                PermissionManager.refresh();
            } catch (Exception ex) {
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void onClick$btnSave() {
        if (validateEmpty()) {
            switch (eventType) {
                case WebConstants.EVENT_ADD:
                    saveProfile(null);
                    break;
                case WebConstants.EVENT_EDIT:
                    saveProfile(profileParam);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadData() {
        switch (eventType) {
            case WebConstants.EVENT_EDIT:
                loadFields(profileParam);
                loadPermissionsList(false);
                break;
            case WebConstants.EVENT_VIEW:
                loadFields(profileParam);
                loadPermissionsList(false);
                break;
            case WebConstants.EVENT_ADD:
                loadPermissionsList(true);
                break;
            default:
                break;
        }
    }
    
    public void saveAudit(Profile rolOld ,Profile rolNew){
        EJBRequest request1 = new EJBRequest();
        EJBRequest request2 = new EJBRequest();            
        String result = "";
         String oldValue ="";
        request1.setParam(rolOld);
        request2.setParam(rolNew);

        try {
            result = auditoryEJB.getNaturalFieldProfile(request1, request2);
        } catch (Exception ex) {
           
        }

        if(!result.isEmpty()||!"".equals(result)){
            String descrip = rolOld.getName();
            
            String status = String.valueOf(!rolOld.getEnabled());
           
            oldValue = "Name:"+descrip+"|Status:"+status;
            
            Audit audit = new Audit();
            EJBRequest auditRequest = new EJBRequest();
            audit.setUser(user);
            Event ev = new Event();
            ev.setId(2l);
            audit.setEvent(ev);
            try {                
            Permission permission = PermissionManager.getInstance().getPermissionById(2L);
            audit.setPermission(permission);
            audit.setCreationDate(new Timestamp((new java.util.Date().getTime())));           
            audit.setTableName("Document Type");
            audit.setRemoteIp(ipAddress);
            audit.setOriginalValues(oldValue);
            audit.setNewValues(result);
            audit.setResponsibleType("usuario");
            auditRequest.setParam(audit);
               audit = auditoryEJB.saveAudit(auditRequest);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
    }
}
