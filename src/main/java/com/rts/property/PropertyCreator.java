/**
 *
 */
package com.rts.property;

import com.rts.property.Property;

/**
 * @author p.mankala
 *
 */
public interface PropertyCreator {
    Property<?> createProperty(String rawPropName);
}
