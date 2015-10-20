package com.ibm.pas.bluemix.pgweb.dao.tables;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.main.BluemixPGWebException;

import java.util.List;

public interface TableDAO
{
    public List<Table> retrieveTableList(String schema, String search, String type, String userKey) throws BluemixPGWebException;

    public Result simpletableCommand (String schemaName, String tableName, String type, String userKey) throws BluemixPGWebException;

    public javax.servlet.jsp.jstl.sql.Result getTableStructure (String schema, String tableName, String userKey) throws BluemixPGWebException;
}
