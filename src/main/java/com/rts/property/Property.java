package com.rts.property;

import java.text.ParseException;
import java.util.regex.Pattern;


public abstract class Property<T> {
    public static final Pattern LINE_PATTERN = Pattern.compile("^([^\\#\\n\\r][^\\n\\r]*)$", Pattern.MULTILINE);

    public abstract String getPropertyName();

    public abstract T getPropertyValue();

    public abstract T setValueFromRawString(String rawValue) throws ParseException;

    public int hashCode() {
        return getPropertyName().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            try {
                Property<?> other = Property.class.cast(obj);
                return this.getPropertyName().equals(other.getPropertyName());
            } catch (ClassCastException e) {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return getPropertyName() + " : " + getPropertyValue();
    }
}
