package com.ibm.pas.bluemix.pgweb.dao;

import com.ibm.pas.bluemix.pgweb.dao.tables.TableDAO;
import com.ibm.pas.bluemix.pgweb.dao.tables.TableDAOImpl;

public class PGWebDAOFactory
{
    public static TableDAO getTableDAO()
    {
        return new TableDAOImpl();
    }
}
