package com.kpisoft.kpi.utility;

public class ScaleRange<T extends Comparable>
{
    private T from;
    private T to;
    
    public ScaleRange(final T start, final T end) {
        this.from = null;
        this.to = null;
        this.from = start;
        this.to = end;
    }
    
    public boolean contains(final T value, final boolean isMinHigher) {
        if (isMinHigher) {
            if (this.from != null && this.to != null) {
                return (this.from.compareTo(this.to) == 0 && this.from.compareTo(value) == 0) || (this.from.compareTo(value) <= 0 && this.to.compareTo(value) > 0);
            }
            if (this.from != null) {
                return this.from.compareTo(value) <= 0;
            }
            return this.to != null && this.to.compareTo(value) > 0;
        }
        else {
            if (this.from != null && this.to != null) {
                return (this.from.compareTo(this.to) == 0 && this.from.compareTo(value) == 0) || (this.from.compareTo(value) <= 0 && this.to.compareTo(value) > 0);
            }
            if (this.from != null) {
                return this.from.compareTo(value) <= 0;
            }
            return this.to != null && this.to.compareTo(value) > 0;
        }
    }
}
