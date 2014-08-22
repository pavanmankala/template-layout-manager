/**
 *
 */
package com.rts.layout.properties;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rts.layout.properties.enums.Anchor;
import com.rts.layout.properties.enums.Size;
import com.rts.property.PropertyCreator;
import com.rts.property.PropertyKey;

/**
 * @author p.mankala
 *
 */
public enum PropertyCategory implements PropertyCreator {
    Anchor(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<Anchor>(new EnumParser<Anchor>(Anchor.class), rawPropName);
        }
    }), Layout(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return null;
        }
    }), Area(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<Dimension>(new GenericValueParser<Dimension>() {
                private final Pattern dimPattern =
                                                         Pattern.compile("\\s*(\\d+)\\s*[xX]\\s*(\\d+)\\s*");

                @Override
                public Dimension parse(String rawString) {
                    Matcher dimMatcher = dimPattern.matcher(rawString);

                    if (dimMatcher.find()) {
                        return new Dimension(Integer.parseInt(dimMatcher.group(1)), Integer
                                .parseInt(dimMatcher.group(2)));
                    }

                    return null;
                }
            }, rawPropName);
        }
    }), Mapping(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<Map<String, String>>(
                    new GenericValueParser<Map<String, String>>() {
                        private final Pattern mapPattern =
                                                                 Pattern.compile("\\s*(.*?)\\s*\\-\\>\\s*(.*)\\s*");

                        @Override
                        public Map<String, String> parse(String rawString) {
                            Matcher lineMatcher = PropertyCategory.linePattern.matcher(rawString);
                            Map<String, String> returnMap = new HashMap<String, String>();

                            while (lineMatcher.find()) {
                                Matcher mapMatcher = mapPattern.matcher(lineMatcher.group(1));
                                if (mapMatcher.matches()) {
                                    returnMap.put(mapMatcher.group(1), mapMatcher.group(2));
                                } else {
                                    throw new RuntimeException(
                                            "Illegal syntax for mapping property:" + rawString);
                                }
                            }

                            return returnMap;
                        }
                    }, rawPropName);
        }
    }), Size(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<Size>(new EnumParser<Size>(Size.class), rawPropName);
        }
    }), Fill(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<Float>(new GenericValueParser<Float>() {
                @Override
                public Float parse(String rawString) {
                    return Float.parseFloat(rawString);
                }
            }, rawPropName);
        }
    }), Default(new PropertyCreator() {
        @Override
        public PropertyKey<?> createPropertyKey(String rawPropName) {
            return new GenericPropertyKey<String>(new GenericValueParser<String>() {
                @Override
                public String parse(String rawString) {
                    return rawString;
                }
            }, rawPropName);
        }
    }), ;

    private static final Pattern  linePattern = Pattern.compile("^([^\\#\\n\\r][^\\n\\r]*)$");
    private final PropertyCreator rootCreator;

    private PropertyCategory(PropertyCreator creator) {
        rootCreator = creator;
    }

    @Override
    public PropertyKey<?> createPropertyKey(String rawPropName) {
        return rootCreator.createPropertyKey(rawPropName);
    }
}
