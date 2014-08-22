package com.rts.layout.properties;

import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * @author p.mankala
 *
 */
public class EnumParser<T extends Enum<T>> implements GenericValueParser<T> {
    private final static Pattern HIFUN_REPLACEMENT = Pattern.compile("\\-");

    private final Class<T> enumClass;


    public EnumParser(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(String rawString) throws ParseException {
        return Enum.valueOf(enumClass, HIFUN_REPLACEMENT.matcher(rawString).replaceAll("_"));
    }
}
