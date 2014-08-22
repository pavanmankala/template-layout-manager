/**
 *
 */
package com.rts.layout.properties;

/**
 * @author p.mankala
 *
 */
public interface GenericValueParser<T> {
    T parse(String rawString);
}
