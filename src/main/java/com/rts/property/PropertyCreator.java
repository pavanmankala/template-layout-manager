/**
 *
 */
package com.rts.property;

import com.rts.property.PropertyKey;

/**
 * @author p.mankala
 *
 */
public interface PropertyCreator {
    PropertyKey<?> createPropertyKey(String rawPropName);
}
