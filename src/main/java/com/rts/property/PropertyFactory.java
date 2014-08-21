/**
 *
 */
package com.rts.property;

/**
 * @author p.mankala
 *
 */
public interface PropertyFactory {
    PropertyKey<?> createTemplateProperty(String rawPropertyName);
}
