/**
 *
 */
package com.rts.layout.properties;

/**
 * @author p.mankala
 *
 */
public class LayoutArea {
    static char t = 'a' - 1;
    char di;
    /**
     *
     */
    public LayoutArea() {
        di = ++t;
    }

    @Override
    public String toString() {
        return String.valueOf(di);
    }
}
