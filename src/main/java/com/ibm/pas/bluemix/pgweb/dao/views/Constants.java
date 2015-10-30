package com.ibm.pas.bluemix.pgweb.dao.views;

public interface Constants
{
        public static final String USER_VIEWS =
                        "  SELECT n.nspname as \"Schema\", " +
                        "    c.relname as \"Name\", " +
                        "    pg_catalog.pg_get_userbyid(c.relowner) as \"Owner\" " +
                        "  FROM pg_catalog.pg_class c " +
                        "       LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                        "  WHERE c.relkind IN ('v','') " +
                        "        AND n.nspname = ? " +
                                "  and c.relname like ? "+
                        "        AND n.nspname <> 'pg_catalog' " +
                        "        AND n.nspname <> 'information_schema' " +
                        "        AND n.nspname !~ '^pg_toast' " +
                        "    AND pg_catalog.pg_table_is_visible(c.oid) " +
                        "  ORDER BY 1,2 ";

            public static final String USER_VIEWS_COUNT =
                    "select object_type, count(*) " +
                            "from ( " +
                            "  SELECT n.nspname as \"Schema\", " +
                            "    c.relname as \"Name\", " +
                            "    'View'::text as object_type, " +
                            "    pg_catalog.pg_get_userbyid(c.relowner) as \"Owner\" " +
                            "  FROM pg_catalog.pg_class c " +
                            "       LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace " +
                            "  WHERE c.relkind IN ('v','') " +
                            "        AND n.nspname = ? " +
                            "        AND n.nspname <> 'pg_catalog' " +
                            "        AND n.nspname <> 'information_schema' " +
                            "        AND n.nspname !~ '^pg_toast' " +
                            "    AND pg_catalog.pg_table_is_visible(c.oid) " +
                            "  ORDER BY 1,2) a " +
                            "group by object_type ";

        public static final String USER_VIEW_DEF =
                "select definition " +
                        "from pg_views " +
                        "where schemaname = ? " +
                        "and   viewname = ? ";

        public static String DROP_VIEW = "drop view %s.%s";

        public static String DROP_VIEW_PUBLIC = "drop view %s";
}
