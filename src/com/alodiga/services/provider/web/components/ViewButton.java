package com.alodiga.services.provider.web.components;

import com.alodiga.services.provider.web.utils.WebConstants;
import org.zkoss.zul.Button;

public class ViewButton extends Button{
    public ViewButton(){
        this.setImage("/images/icon-invoice.png");
    }
    public ViewButton(String view, Object obj,Long permissionId){
        this.setImage("/images/icon-invoice.png");
        this.addEventListener("onClick", new ShowAdminViewListener(WebConstants.EVENT_VIEW, view, obj,permissionId));
        
    }
}
