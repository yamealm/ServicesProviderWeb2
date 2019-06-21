package com.alodiga.services.provider.web.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;

public class ListcellRemoveButton extends Listcell {

    public ListcellRemoveButton() {
    	RemoveButton editButton = new RemoveButton();
        editButton.setParent(this);
    }

    public ListcellRemoveButton(String destinationView, Object obj, Long permissionId) {
    	RemoveButton button = new RemoveButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.remove"));
        button.setClass("open orange");
        button.setParent(this);
    }

}
