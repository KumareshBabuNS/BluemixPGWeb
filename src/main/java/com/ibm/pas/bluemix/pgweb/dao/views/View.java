package com.ibm.pas.bluemix.pgweb.dao.views;

public class View
{
    public String schemaName;
    public String viewName;
    public String owner;

    public View()
    {
    }

    public View(String schemaName, String viewName, String owner) {
        this.schemaName = schemaName;
        this.viewName = viewName;
        this.owner = owner;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "View{" +
                "schemaName='" + schemaName + '\'' +
                ", viewName='" + viewName + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
