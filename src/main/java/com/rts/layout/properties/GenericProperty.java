/**
 *
 */
package com.rts.layout.properties;

import com.rts.property.Property;

/**
 * @author p.mankala
 *
 */
public class GenericProperty<T> extends Property<T> {
    private final String                propKey;
    private T                           propValue;
    private final GenericValueParser<T> parser;

    public GenericProperty(GenericValueParser<T> parser, String key) {
        propKey = key;
        this.parser = parser;
    }

    @Override
    public String getPropertyName() {
        return propKey;
    }

    @Override
    public T getPropertyValue() {
        return propValue;
    }

    @Override
    public T setValueFromRawString(String rawValue) {
        return propValue = parser.parse(rawValue);
    }
}
