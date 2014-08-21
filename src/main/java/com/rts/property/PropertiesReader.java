package com.rts.property;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesReader {
    private static final String   MULTI_LINE_VALUE_PATTERN = "\\s*(.*?)\\s*(\\\\)?\\s*$";
    /*@formatter:off*/
    private final Pattern linePattern           = Pattern.compile("^(.*)$", Pattern.MULTILINE),
                          propertyPattern       = Pattern.compile("^\\s*([\\w\\-]+)\\s*\\:" + MULTI_LINE_VALUE_PATTERN),
                          multiLineValuePattern = Pattern.compile("^" + MULTI_LINE_VALUE_PATTERN);
    /*@formatter:on*/
    private final PropertyFactory factory;

    public PropertiesReader(PropertyFactory factory) {
        this.factory = factory;
    }

    public Iterable<PropertyKey<?>> read(InputStream stream) throws IOException, ParseException {
        return read(new InputStreamReader(stream, Charset.forName("UTF-8")));
    }

    public Iterable<PropertyKey<?>> read(Reader reader) throws IOException, ParseException {
        char[] builder = new char[2048];
        int readInt;
        int pos = 0;
        int increment = 100;

        while ((readInt = reader.read()) != -1) {
            builder[pos++] = (char) readInt;

            if (pos == builder.length) {
                char[] newBuilder = new char[builder.length + increment];

                System.arraycopy(builder, 0, newBuilder, 0, pos);

                builder = newBuilder;
            }
        }

        return parseFile(new CharArraySequence(builder, 0, pos));
    }

    private Collection<PropertyKey<?>> parseFile(CharSequence sequence) throws ParseException {
        List<PropertyKey<?>> retList = new ArrayList<PropertyKey<?>>();
        Matcher lineMatcher = linePattern.matcher(sequence);
        int lineNo = 0;
        StringBuilder propValueBuilder = new StringBuilder();

        while (lineMatcher.find()) {
            String line = lineMatcher.group(1).trim();
            lineNo++;

            if (line.isEmpty()) {
                continue;
            }

            Matcher propMatcher = propertyPattern.matcher(line);
            PropertyKey<?> templatePropertyKey;

            if (propMatcher.find()) {
                boolean hasMultiLineValue = propMatcher.group(3) != null;
                String propKey = propMatcher.group(1);
                templatePropertyKey = factory.createTemplateProperty(propKey);

                propValueBuilder.append(propMatcher.group(2));

                while (hasMultiLineValue && lineMatcher.find()) {
                    Matcher lineContinueMatcher =
                            multiLineValuePattern.matcher(lineMatcher.group(1));

                    lineNo++;

                    if (lineContinueMatcher.matches()) {
                        hasMultiLineValue = lineContinueMatcher.group(2) != null;
                        propValueBuilder.append('\n').append(lineContinueMatcher.group(1));
                    } else {
                        hasMultiLineValue = false;
                    }

                }
            } else {
                throw new ParseException("Error in parsing property @ line: " + lineNo, lineNo);
            }

            templatePropertyKey.setValueFromRawString(propValueBuilder.toString());
            propValueBuilder.setLength(0);

            retList.add(templatePropertyKey);
        }

        return retList;
    }

    static class CharArraySequence implements CharSequence {
        private final char[] charArr;
        private final int    begin, length;

        public CharArraySequence(char[] value, int offset, int length) {
            this.charArr = value;
            this.begin = offset;
            this.length = length;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int index) {
            return charArr[begin + index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new CharArraySequence(charArr, start, end - start);
        }

        @Override
        public String toString() {
            return new String(charArr, begin, length);
        }
    }
}
