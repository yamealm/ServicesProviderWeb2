package com.alodiga.services.provider.web.controllers;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menubar;

import com.alodiga.services.provider.commons.ejbs.UserEJB;
import com.alodiga.services.provider.commons.models.Account;
import com.alodiga.services.provider.commons.utils.EJBServiceLocator;
import com.alodiga.services.provider.commons.utils.EjbConstants;
import com.alodiga.services.provider.web.utils.AccessControl;

public class AccountHeaderMenuController extends GenericForwardComposer {

    private static final long serialVersionUID = -9145887024839938515L;
    Label lblCurrentBalance;
    Label lblCurrentBalanceValue;
    Label lblCurrentLimit;
    Label lblCurrentBalanceLimit;
    Label lblCurrentBalanceValue2;
    Label lblCurrentBalanceValueAvalaible;
    Label lblAccountName;
    Label lblAccountType;
    Label lblInfo;
    Menubar menuOptions;
    Button btnRSBuyBalance;
//    TransactionEJB transactionEJB;
    UserEJB userEJB;
    Account currentAccount;
    String currencySymbol = "";
    private Hlayout balance;
    private Hlayout balanceCreditLimit;
    private Hlayout balance2;
    private Hlayout balanceAvalaible;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initialize();
        loadSummaryData();
    }

    public void initialize() {
        try {
            userEJB = (UserEJB) EJBServiceLocator.getInstance().get(EjbConstants.USER_EJB);
//            transactionEJB = (TransactionEJB) EJBServiceLocator.getInstance().get(EjbConstants.TRANSACTION_EJB);
            currentAccount = AccessControl.loadCurrentAccount();
            startListener();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(AccountHeaderMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startListener() {
        EventQueue que = EventQueues.lookup("loadSummaryData", EventQueues.APPLICATION, true);
        que.subscribe(new EventListener() {

            public void onEvent(Event evt) {
                try {
                    loadSummaryData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void loadSummaryData() {
        try {
            currencySymbol = currentAccount.getEnterprise().getCurrency().getSymbol();
            lblAccountName.setValue(currentAccount.getFullName());
           // lblAccountType.setValue(currentAccount.getCurrentProfile().getProfileDataByLanguageId(AccessControl.getLanguage()).getAlias());
            if (currentAccount.getIsPrePaid()) {
                lblAccountType.setValue(Labels.getLabel("sp.common.type.isprepaid"));
                balance.setVisible(true);
                Float currentBalance = 0f;// transactionEJB.getCurrentBalanceByAccount(currentAccount.getId());
                Sessions.getCurrent().setAttribute("currentBalance", currentBalance);
                lblCurrentBalanceValue.setValue(new DecimalFormat("#.##").format(currentBalance) + " " + currencySymbol);
            } else {
                lblAccountType.setValue(Labels.getLabel("sp.common.type.postpaid"));
                balanceCreditLimit.setVisible(true);
                lblCurrentBalanceLimit.setValue(currentAccount.getCrediLimit().toString());
                balance2.setVisible(true);
                lblCurrentBalanceValue2.setValue(currentAccount.getBalance().toString());
                balanceAvalaible.setVisible(true);
                Float limit = Float.valueOf(currentAccount.getCrediLimit().toString());
                Float balanc = Float.valueOf(currentAccount.getBalance().toString());
                Float avalaible = (limit) + (balanc);
                lblCurrentBalanceValueAvalaible.setValue(avalaible.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
