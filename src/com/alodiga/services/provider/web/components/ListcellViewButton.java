package com.alodiga.services.provider.web.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Listcell;

public class ListcellViewButton extends Listcell {

    public ListcellViewButton() {
    }

    public ListcellViewButton(String destinationView, Object obj, Long permissionId) {
        ViewButton button = new ViewButton(destinationView, obj,permissionId);
        button.setTooltiptext(Labels.getLabel("sp.common.actions.view"));
        button.setClass("open orange");
        button.setParent(this);
    }

    public ListcellViewButton(String destinationView, Object obj, boolean isRedirect, Long permissionId) {
        ViewButton viewButton = new ViewButton(destinationView, obj,permissionId);
        viewButton.setParent(this);
    }
}
