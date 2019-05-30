package com.alodiga.services.provider.web.controllers;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.exceptions.EmptyListException;
import com.alodiga.services.provider.commons.exceptions.GeneralException;
import com.alodiga.services.provider.commons.exceptions.NullParameterException;
import com.alodiga.services.provider.commons.genericEJB.EJBRequest;
import com.alodiga.services.provider.commons.models.User;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.commons.utils.QueryConstants;
import com.alodiga.services.provider.web.generic.controllers.GenericAbstractAdminController;
import com.alodiga.services.provider.web.utils.AccessControl;
import java.util.List;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.zul.Datebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;

public class ClosureProcessController extends GenericAbstractAdminController {

    private static final long serialVersionUID = -9145887024839938515L;
    private Datebox dtbBeginningDate;
    private UserEJB userEJB = null;
    Boolean isStoreAll = false;
    private User currentUser;


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
    }

    public void startListener() {
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
            currentUser = AccessControl.loadCurrentUser();
            loadData();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    public void loadData() {
        dtbBeginningDate.setFormat("yyyy/MM/dd");
        dtbBeginningDate.setValue(new Timestamp(new java.util.Date().getTime()));
    }

    public void onClick$btnProcess() {
        Date date = dtbBeginningDate.getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		date = cal.getTime();
		Map<String, Object> paramsValidate = new HashMap<String, Object>();
		paramsValidate.put(QueryConstants.PARAM_BEGINNING_DATE, date);
		request.setParams(paramsValidate);
		EJBRequest request = new EJBRequest();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(QueryConstants.PARAM_BEGINNING_DATE, dtbBeginningDate.getValue());
		request.setParams(params); 
    }

    public void blockFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clearFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
