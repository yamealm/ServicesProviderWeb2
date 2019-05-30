package com.alodiga.services.provider.web.components;

import com.alodiga.services.provider.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class EditButton extends Button {

    public EditButton() {
        this.setImage("/images/icon-edit.png");
    }

    public EditButton(String view, Object obj, Long permissionId) {
        this.setImage("/images/icon-edit.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_EDIT, view, obj,permissionId));
    }

}
