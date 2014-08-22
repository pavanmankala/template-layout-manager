/**
 *
 */
package com.rts.layout.properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rts.property.PropertyFactory;
import com.rts.property.Property;

/**
 * @author p.mankala
 *
 */
public class TemplateLayoutPropertyFactory implements PropertyFactory {
    private final Pattern propertyPattern = Pattern.compile("^((.*)\\-(.*))$");

    public Property<?> createTemplateProperty(String rawPropertyName) {
        Matcher propMatcher = propertyPattern.matcher(rawPropertyName);
        if (propMatcher.matches()) {
            try {
                String propCategory = propMatcher.group(3);
                PropertyCategory category = PropertyCategory.valueOf(propCategory);

                return category.createProperty(rawPropertyName);
            } catch (Exception e) {
                return PropertyCategory.Default.createProperty(rawPropertyName);
            }
        } else {
            throw new RuntimeException("Illegal propName: " + rawPropertyName);
        }
    }
}
