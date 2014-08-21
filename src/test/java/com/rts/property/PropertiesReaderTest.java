/**
 *
 */
package com.rts.property;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author p.mankala
 *
 */
public class PropertiesReaderTest {
    @Test
    public void testLineReader() {
        PropertiesReader reader = new PropertiesReader(new MyRegistry());
        Iterable<PropertyKey<?>> keys = null;

        try {
            keys =
                    reader.read(PropertiesReaderTest.class
                            .getResourceAsStream("/swing-layout.layout"));
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        for (PropertyKey<?> key : keys) {
            System.out.println(key);
        }
    }

    enum Property implements PropertyKeyGenerator {
        fill(new PropertyKeyGenerator() {
            @Override
            public PropertyKey<?> generate(String key) {
                return new FloatTemplatePropertyKey(key);
            }
        }), string(new PropertyKeyGenerator() {
            @Override
            public PropertyKey<?> generate(String key) {
                return new StringTemplatePropertyKey(key);
            }
        });
        private final PropertyKeyGenerator generator;

        private Property(PropertyKeyGenerator generator) {
            this.generator = generator;
        }

        @Override
        public PropertyKey<?> generate(String key) {
            return generator.generate(key);
        }
    }

    static interface PropertyKeyGenerator {
        PropertyKey<?> generate(String key);
    }
    static class FloatTemplatePropertyKey extends PropertyKey<Float> {
        private final String key;
        private float        value;

        public FloatTemplatePropertyKey(String key) {
            this.key = key;
        }

        @Override
        public String getPropertyName() {
            return key;
        }

        @Override
        public Float getPropertyValue() {
            return value;
        }

        @Override
        public Float setValueFromRawString(String rawValue) {
            return value = Float.parseFloat(rawValue);
        }
    }
    static class StringTemplatePropertyKey extends PropertyKey<String> {
        private final String key;
        private String       value;

        public StringTemplatePropertyKey(String key) {
            this.key = key;
        }

        @Override
        public String getPropertyName() {
            return key;
        }

        @Override
        public String getPropertyValue() {
            return value;
        }

        @Override
        public String setValueFromRawString(String rawValue) {
            return value = rawValue;
        }
    }

    static class MyRegistry implements PropertyFactory {
        @Override
        public PropertyKey<?> createTemplateProperty(String rawPropertyName) {
            if (rawPropertyName.endsWith("fill")) {
                return Property.fill.generate(rawPropertyName);
            } else {
                return Property.string.generate(rawPropertyName);
            }
        }
    }
}
