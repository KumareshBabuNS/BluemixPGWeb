package com.ibm.pas.bluemix.pgweb.dao.views;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.main.BluemixPGWebException;

import java.util.List;

public interface ViewDAO
{
    public List<View> retrieveViewList(String schema, String search, String userKey) throws BluemixPGWebException;

    public Result simpleviewCommand (String schemaName, String viewName, String type, String userKey) throws BluemixPGWebException;

    public String getViewDefinition(String schemaName, String viewName, String userKey) throws BluemixPGWebException;
}
