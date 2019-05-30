package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.ejbs.UtilsEJB;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.GeneralUtils;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.CategoryModel;
import org.zkoss.zul.Chart;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleCategoryModel;

public class HomeController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Label lblLogin;
    private Textbox txtOldPassword;
    private Textbox txtNewPassword1;
    private Textbox txtNewPassword2;
    private Textbox txtName;
    private Textbox txtLastName;
    private Textbox txtEmail;
    private Textbox txtPhoneNumber;
    private Checkbox cbxEnabled;
    private Checkbox cbxReceiveTopNotification;
    private Listbox lbxRoyalties;
    private Listbox lbxTransactions;
    private Listbox lbxpromotions;
    private UtilsEJB utilsEJB = null;
    private UserEJB userEJB = null;
    private Label lblProfile;
    private Chart chtGoal;
    private Chart chtSales;
    private Label lblTotalPinAmount;
    private Label lblTotalPinCommission;
    private Label lblTotalTopUpAmount;
    private Label lblTotalTopUpCommission;
    private String currencySymbol="";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        initView(eventType, "crud.user");
    }

    @Override
    public void initialize() {
        try {
            super.initialize();
            utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            loadCharts();
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void clearFields() {
    }

    public void blockFields() {
    }

    public void onClick$btnCancel() {
        clearFields();
    }

    private void loadCharts() {

        try {
            Date date = new Date();
            CategoryModel salesCategoryModel = new SimpleCategoryModel();
            chtGoal.setModel(salesCategoryModel);
            Float totalTransactions = 1F;
            Float totalCommissions = 1F;
//            Float totalTransactions = 1000F;
//            Float totalCommissions = 100F;

            salesCategoryModel = new SimpleCategoryModel();
            salesCategoryModel.setValue(Labels.getLabel("sp.home.total.pinOperations"), "", totalTransactions);
            salesCategoryModel.setValue(Labels.getLabel("sp.home.commissions.pinOperations"), "", totalCommissions);
            lblTotalPinAmount.setValue(GeneralUtils.getCorrectAmount(currencySymbol, totalTransactions, 2));
            lblTotalPinCommission.setValue(GeneralUtils.getCorrectAmount(currencySymbol, totalCommissions, 2));
            totalTransactions =1F;
            totalCommissions = 1F;
//            totalTransactions = 4000F;
//            totalCommissions = 300F;
            salesCategoryModel.setValue(Labels.getLabel("sp.home.total.topUps"), "", totalTransactions);
            salesCategoryModel.setValue(Labels.getLabel("sp.home.commission.topUps"), "", totalCommissions);
            lblTotalTopUpAmount.setValue(GeneralUtils.getCorrectAmount(currencySymbol, totalTransactions, 2));
            lblTotalTopUpCommission.setValue(GeneralUtils.getCorrectAmount(currencySymbol, totalCommissions, 2));
            chtSales.setModel(salesCategoryModel);

        } catch (Exception ex) {
            showError(ex);
        }
    }

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

}
