package com.alodiga.services.provider.web.generic.controllers;

import com.alodiga.services.provider.commons.genericEJB.EJBRequest;

import java.util.ArrayList;
import java.util.List;

public interface GenericSPController {

    public EJBRequest request = new EJBRequest();


    public void initialize();

    //public void loadPermission(AbstractDistributionEntity clazz) throws Exception;

}
