package org.mxhero.engine.plugin.threadlight.pagination.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.mxhero.engine.plugin.threadlight.pagination.common.PageInfo;
import org.springframework.jdbc.core.RowMapper;

public class JdbcPageInfo extends PageInfo<Map, String> {
	  public static final String JDBC_PAGE_INFO_SQL = "JDBC_PAGE_INFO_SQL";
      public static final String JDBC_PAGE_INFO_EXAMPLE_MODEL = "JDBC_PAGE_INFO_EXAMPLE_MODEL";
      public static final String JDBC_PAGE_INFO_ROW_MAPPER = "JDBC_PAGE_INFO_ROW_MAPPER";

      public JdbcPageInfo() {
              super();
              super.expression = new HashMap();
      }

      public String getSql() {
              return (String) this.getExpression().get(JDBC_PAGE_INFO_SQL);
      }

      public void putSql(String sql) {
              this.getExpression().put(JDBC_PAGE_INFO_SQL, sql);
      }

      public Map<String, ?> getExampleModel() {
              return (Map) this.getExpression().get(JDBC_PAGE_INFO_EXAMPLE_MODEL);
      }

      public void putExampleModel(Map<String, ?> obj) {
              this.getExpression().put(JDBC_PAGE_INFO_EXAMPLE_MODEL, obj);
      }

      @Override
      public void addOrderByAsc(String orderBy) {
              super.getOrderByList().add(orderBy + " asc");
      }

      @Override
      public void addOrderByDesc(String orderBy) {
              super.getOrderByList().add(orderBy + " desc");
      }

      public RowMapper getRowMapper() {
              return (RowMapper) this.getExpression().get(JDBC_PAGE_INFO_ROW_MAPPER);
      }

      public void putRowMapper(RowMapper rm) {
              this.getExpression().put(JDBC_PAGE_INFO_ROW_MAPPER, rm);
      }
}
