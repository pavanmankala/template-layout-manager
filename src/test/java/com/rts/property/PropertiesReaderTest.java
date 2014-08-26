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
        PropertyReader reader = new PropertyReader(new TemplateLayoutPropertyFactory());
        Iterable<Property<?>> keys = null;

        try {
            keys = reader.read(PropertiesReaderTest.class.getResourceAsStream("/swing-layout.layout"));
        } catch (Throwable e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        for (Property<?> key : keys) {
            System.out.println(key);
        }
    }
}
