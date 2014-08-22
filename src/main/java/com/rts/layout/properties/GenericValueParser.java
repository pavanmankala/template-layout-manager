/**
 *
 */
package com.rts.layout.properties;

import java.text.ParseException;
import java.util.regex.Pattern;

import com.rts.property.Property;

/**
 * @author p.mankala
 *
 */
public interface GenericValueParser<T> {
    Pattern LINE_PATTERN = Property.LINE_PATTERN;

    T parse(String rawString) throws ParseException;
}
