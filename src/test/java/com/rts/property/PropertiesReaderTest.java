package com.rts.property;

import org.junit.Assert;
import org.junit.Test;

import com.rts.layout.properties.TemplateLayoutPropertyFactory;

/**
 * @author p.mankala
 *
 */
public class PropertiesReaderTest {
    @Test
    public void testLineReader() {
        PropertiesReader reader = new PropertiesReader(new TemplateLayoutPropertyFactory());
        Iterable<PropertyKey<?>> keys = null;

        try {
            keys = reader.read(PropertiesReaderTest.class
                            .getResourceAsStream("/swing-layout.layout"));
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        for (PropertyKey<?> key : keys) {
            System.out.println(key);
        }
    }
}
