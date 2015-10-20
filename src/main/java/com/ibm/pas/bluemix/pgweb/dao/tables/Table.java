package com.ibm.pas.bluemix.pgweb.dao.tables;

public class Table
{
    public String schemaName;
    public String tableName;
    public String owner;

    public Table()
    {
    }

    public Table(String schemaName, String tableName, String owner)
    {
        this.owner = owner;
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Table{" +
                "schemaName='" + schemaName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
