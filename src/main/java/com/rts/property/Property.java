package com.rts.property;


public abstract class Property<T> {
    public abstract String getPropertyName();

    public abstract T getPropertyValue();

    public abstract T setValueFromRawString(String rawValue);

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
