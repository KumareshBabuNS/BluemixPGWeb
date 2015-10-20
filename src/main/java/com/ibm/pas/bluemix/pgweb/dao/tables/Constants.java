package com.ibm.pas.bluemix.pgweb.dao.tables;

public interface Constants
{
        public static final String USER_TABLES =
            "  SELECT n.nspname as \"Schema\", " +
                    "    c.relname as \"Name\", " +
                    "    pg_catalog.pg_get_userbyid(c.relowner) as \"Owner\" " +
                    "  FROM pg_catalog.pg_class c " +
                    "       LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                    "  WHERE c.relkind IN ('r','') " +
                    "        AND n.nspname <> 'pg_catalog' " +
                    "        AND n.nspname <> 'information_schema' " +
                    "        AND n.nspname !~ '^pg_toast' " +
                    "    AND pg_catalog.pg_table_is_visible(c.oid) " +
                    "  and n.nspname = ? " +
                    "  and c.relname like ? "+
                    "  ORDER BY 1,2 ";


        public static final String USER_TABLES_COUNT =
            "select object_type, count(*) " +
                    "from ( " +
                    "  SELECT n.nspname as \"Schema\", " +
                    "    c.relname as \"Name\", " +
                    "    'Table'::text as object_type, " +
                    "    pg_catalog.pg_get_userbyid(c.relowner) as \"Owner\" " +
                    "  FROM pg_catalog.pg_class c " +
                    "       LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                    "  WHERE c.relkind IN ('r','') " +
                    "        AND n.nspname = ? " +
                    "        AND n.nspname <> 'pg_catalog' " +
                    "        AND n.nspname <> 'information_schema' " +
                    "        AND n.nspname !~ '^pg_toast' " +
                    "    AND pg_catalog.pg_table_is_visible(c.oid) " +
                    "  ORDER BY 1,2) a " +
                    "group by object_type ";

        public static String DROP_TABLE = "DROP TABLE %s.%s";

        public static String TRUNCATE_TABLE = "TRUNCATE TABLE %s.%s";

        public static String DROP_TABLE_PUBLIC = "DROP TABLE %s";

        public static String TRUNCATE_TABLE_PUBLIC = "TRUNCATE TABLE %s";

        public static final String USER_TAB_COLUMNS =
                "SELECT " +
                    "    f.attname AS column_name, " +
                    "    pg_catalog.format_type(f.atttypid,f.atttypmod) AS column_type, " +
                    "    f.attnotnull AS allow_null " +
                    "FROM pg_attribute f " +
                    "    JOIN pg_class c ON c.oid = f.attrelid " +
                    "    LEFT JOIN pg_attrdef d ON d.adrelid = c.oid AND d.adnum = f.attnum " +
                    "    LEFT JOIN pg_namespace n ON n.oid = c.relnamespace " +
                    "    LEFT JOIN pg_constraint p ON p.conrelid = c.oid AND f.attnum = ANY (p.conkey) " +
                    "    LEFT JOIN pg_class AS g ON p.confrelid = g.oid " +
                    "WHERE c.relkind = 'r'::char " +
                    "    AND n.nspname = ? " +
                    "    AND c.relname = ? " +
                    "    AND f.attnum > 0 " +
                    "ORDER BY f.attnum";

}
