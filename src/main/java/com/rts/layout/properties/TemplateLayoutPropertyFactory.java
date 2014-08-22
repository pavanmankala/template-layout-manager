/**
 *
 */
package com.rts.layout.properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rts.property.PropertyFactory;
import com.rts.property.PropertyKey;

/**
 * @author p.mankala
 *
 */
public class TemplateLayoutPropertyFactory implements PropertyFactory {
    private final Pattern propertyPattern = Pattern.compile("^((.*)\\-(.*))$");

    public PropertyKey<?> createTemplateProperty(String rawPropertyName) {
        Matcher propMatcher = propertyPattern.matcher(rawPropertyName);
        if (propMatcher.matches()) {
            try {
                String propCategory = propMatcher.group(3);
                PropertyCategory category = PropertyCategory.valueOf(propCategory);

                return category.createPropertyKey(rawPropertyName);
            } catch (Exception e) {
                return PropertyCategory.Default.createPropertyKey(rawPropertyName);
            }
        } else {
            throw new RuntimeException("Illegal propName: " + rawPropertyName);
        }
    }
}
