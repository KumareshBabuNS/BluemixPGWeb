package com.ibm.pas.bluemix.pgweb.dao;

import com.ibm.pas.bluemix.pgweb.dao.tables.TableDAO;
import com.ibm.pas.bluemix.pgweb.dao.tables.TableDAOImpl;
import com.ibm.pas.bluemix.pgweb.dao.views.ViewDAO;
import com.ibm.pas.bluemix.pgweb.dao.views.ViewDAOImpl;

public class PGWebDAOFactory
{
    public static TableDAO getTableDAO()
    {
        return new TableDAOImpl();
    }

    public static ViewDAO getViewDAO()
    {
        return new ViewDAOImpl();
    }
}
