/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.infrastructure.pagination.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.mxhero.console.backend.infrastructure.pagination.common.PageInfo;
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
