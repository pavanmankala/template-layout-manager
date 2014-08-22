/**
 *
 */
package com.rts.property;

/**
 * @author p.mankala
 *
 */
public interface PropertyFactory {
    Property<?> createTemplateProperty(String rawPropertyName);
}
