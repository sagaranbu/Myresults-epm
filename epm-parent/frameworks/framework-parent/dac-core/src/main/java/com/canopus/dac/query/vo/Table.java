package com.canopus.dac.query.vo;

import com.canopus.mw.dto.*;
import java.util.*;

public class Table extends BaseValueObject
{
    private List<String> columns;
    private Map<String, Integer> colIndex;
    private List<Row> rows;
    
    public Table() {
        this.columns = new ArrayList<String>();
        this.colIndex = new HashMap<String, Integer>();
        this.rows = new ArrayList<Row>();
    }
    
    public List<String> getColumns() {
        return Collections.unmodifiableList((List<? extends String>)this.columns);
    }
    
    public void setColumns(final List<String> columns) {
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Columns can't be null/empty");
        }
        if (!this.rows.isEmpty()) {
            throw new IllegalStateException("Can't modify table structure after rows are added");
        }
        this.columns = columns;
        this.colIndex = new HashMap<String, Integer>();
        for (int i = 0; i < columns.size(); ++i) {
            this.colIndex.put(columns.get(i), new Integer(i));
        }
    }
    
    public List<Row> getRows() {
        return this.rows;
    }
    
    public void addRow(final Row row) {
        if (row == null) {
            throw new NullPointerException("Row is null");
        }
        int idx = this.rows.size();
        row.setRowNum(idx++);
        this.rows.add(row);
    }
    
    public static class Row
    {
        private int rowNum;
        private Table parent;
        private List<String> data;
        
        public Row(final Table table) {
            this.parent = table;
            this.data = new ArrayList<String>();
        }
        
        public void set(final String column, final String val) {
            this.data.set(this.getIndex(column), val);
        }
        
        public void setAll(final Map<String, Object> data) {
            for (final String key : data.keySet()) {
                final Object val = data.get(key);
                if (val != null) {
                    this.set(key, val.toString());
                }
            }
        }
        
        public String get(final String column) {
            return this.data.get(this.getIndex(column));
        }
        
        public Map<String, String> asMap() {
            final Map<String, String> map = new HashMap<String, String>();
            for (final String column : this.parent.columns) {
                map.put(column, this.get(column));
            }
            return map;
        }
        
        private int getIndex(final String column) {
            final Integer idx = this.parent.colIndex.get(column);
            if (idx == null) {
                throw new IllegalArgumentException("Invalid column " + column);
            }
            return idx;
        }
        
        public int getRowNum() {
            return this.rowNum;
        }
        
        public void setRowNum(final int rowNum) {
            this.rowNum = rowNum;
        }
    }
}
