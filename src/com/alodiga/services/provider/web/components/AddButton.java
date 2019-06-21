package com.alodiga.services.provider.web.components;

import com.alodiga.services.provider.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class AddButton extends Button{
    public AddButton(){
        this.setImage("/images/icon-add.png");
    }
    public AddButton(String view, Object obj, Long permissionId){
        this.setImage("/images/icon-add.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_ADD, view, obj,permissionId));
        
    }
}
