/**
 *
 */
package com.rts.layout.properties;

/**
 * @author p.mankala
 *
 */
public class LayoutArea {
    private String areaCode;

    @Override
    public String toString() {
        if (areaCode == null) {
            return " ";
        } else {
            return String.valueOf(areaCode);
        }
    }

    public String setAreaCode(String s) {
        return areaCode = s;
    }
}
