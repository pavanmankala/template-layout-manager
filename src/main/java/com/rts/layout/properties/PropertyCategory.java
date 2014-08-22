/**
 *
 */
package com.rts.layout.properties;

import java.awt.Dimension;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rts.layout.properties.enums.Anchor;
import com.rts.layout.properties.enums.Size;
import com.rts.property.PropertyCreator;
import com.rts.property.Property;

/**
 * @author p.mankala
 *
 */
public enum PropertyCategory implements PropertyCreator {
    Anchor(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Anchor>(new EnumParser<Anchor>(Anchor.class), rawPropName);
        }
    }), Layout(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Layout>(new LayoutParser(), rawPropName);
        }
    }), Area(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Dimension>(new GenericValueParser<Dimension>() {
                private final Pattern dimPattern = Pattern.compile("\\s*(\\d+)\\s*[xX]\\s*(\\d+)\\s*");

                @Override
                public Dimension parse(String rawString) throws ParseException {
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
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Map<String, String>>(
                    new GenericValueParser<Map<String, String>>() {
                        private final Pattern mapPattern = Pattern.compile("\\s*(.*?)\\s*\\-\\>\\s*(.*)\\s*");

                        @Override
                        public Map<String, String> parse(String rawString) throws ParseException {
                            Matcher lineMatcher = Property.LINE_PATTERN.matcher(rawString);
                            Map<String, String> returnMap = new HashMap<String, String>();

                            while (lineMatcher.find()) {
                                String mappingLine = lineMatcher.group(1);
                                Matcher mapMatcher = mapPattern.matcher(mappingLine);

                                if (mapMatcher.matches()) {
                                    returnMap.put(mapMatcher.group(1), mapMatcher.group(2));
                                } else {
                                    throw new RuntimeException(
                                            "Illegal syntax for mapping property:" + mappingLine);
                                }
                            }

                            return returnMap;
                        }
                    }, rawPropName);
        }
    }), Size(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Size>(new EnumParser<Size>(Size.class), rawPropName);
        }
    }), Fill(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<Float>(new GenericValueParser<Float>() {
                @Override
                public Float parse(String rawString) throws ParseException {
                    return Float.parseFloat(rawString);
                }
            }, rawPropName);
        }
    }), Default(new PropertyCreator() {
        @Override
        public Property<?> createProperty(String rawPropName) {
            return new GenericProperty<String>(new GenericValueParser<String>() {
                @Override
                public String parse(String rawString) throws ParseException {
                    return rawString;
                }
            }, rawPropName);
        }
    }), ;

    private final PropertyCreator rootCreator;

    private PropertyCategory(PropertyCreator creator) {
        rootCreator = creator;
    }

    @Override
    public Property<?> createProperty(String rawPropName) {
        return rootCreator.createProperty(rawPropName);
    }
}
