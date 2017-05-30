package com.canopus.dac.query.executor;

import com.canopus.dac.query.impl.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import com.canopus.dac.query.vo.*;

public class JdbcTemplateExecutor implements QueryExecutor
{
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @Override
    public void execute(final QueryMetadata query, final Map<String, Object> params) {
        final String queryStr = query.getQuery();
        if (query.getExecType() == QueryExecutionType.SQL_QUERY || query.getExecType() == QueryExecutionType.PROCEDURE) {
            switch (query.getQueryType()) {
                case DDL:
                case INSERT:
                case UPDATE:
                case DELETE: {
                    this.jdbcTemplate.update(queryStr, (Map)params);
                    break;
                }
            }
            this.jdbcTemplate.query(queryStr, (Map)params, (RowMapper)new NullMapper());
        }
        throw new UnsupportedOperationException("Unsupported query type");
    }
    
    @Override
    public int executeForInt(final QueryMetadata query, final Map<String, Object> params) {
        final String queryStr = query.getQuery();
        if (query.getExecType() != QueryExecutionType.SQL_QUERY && query.getExecType() != QueryExecutionType.PROCEDURE) {
            throw new UnsupportedOperationException("Unsupported query type");
        }
        switch (query.getQueryType()) {
            case DDL:
            case INSERT:
            case UPDATE:
            case DELETE: {
                return this.jdbcTemplate.update(queryStr, (Map)params);
            }
            default: {
                return (int)this.jdbcTemplate.queryForObject(queryStr, (Map)params, (Class)Integer.class);
            }
        }
    }
    
    @Override
    public Table executeForTable(final QueryMetadata query, final Map<String, Object> params) {
        final String queryStr = query.getQuery();
        if (query.getExecType() != QueryExecutionType.SQL_QUERY && query.getExecType() != QueryExecutionType.PROCEDURE) {
            throw new UnsupportedOperationException("Unsupported query type");
        }
        switch (query.getQueryType()) {
            case DDL:
            case INSERT:
            case UPDATE:
            case DELETE: {
                final int i = this.jdbcTemplate.update(queryStr, (Map)params);
                final Table table = new Table();
                final List<String> cols = new ArrayList<String>();
                cols.add("count");
                table.setColumns(cols);
                final Table.Row row = new Table.Row(table);
                row.set("count", Integer.toString(i));
                table.addRow(row);
                return table;
            }
            default: {
                final List<Map<String, Object>> data = (List<Map<String, Object>>)this.jdbcTemplate.queryForList(queryStr, (Map)params);
                final Table table = new Table();
                if (!data.isEmpty()) {
                    final Map<String, Object> row2 = data.get(0);
                    final List<String> cols2 = new ArrayList<String>();
                    cols2.addAll(row2.keySet());
                    table.setColumns(cols2);
                    for (final Map<String, Object> datarow : data) {
                        final Table.Row row3 = new Table.Row(table);
                        row3.setAll(datarow);
                        table.addRow(row3);
                    }
                }
                return table;
            }
        }
    }
    
    @Override
    public Object executeForObject(final QueryMetadata query, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<Object> executeForList(final QueryMetadata query, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setDataSource(final DataSource ds) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }
    
    private static class NullMapper implements RowMapper
    {
        public String mapRow(final ResultSet rs, final int i) throws SQLException {
            return "";
        }
    }
}
