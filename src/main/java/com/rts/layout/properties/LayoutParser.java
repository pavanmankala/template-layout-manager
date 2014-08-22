/**
 *
 */
package com.rts.layout.properties;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author p.mankala
 *
 */
public class LayoutParser implements GenericValueParser<Layout> {
    @Override
    public Layout parse(String rawString) throws ParseException {
        int columns = -1, width = 0, lastAreaEnd = -1, currRow = 0, currCol = 0;
        List<char[]> layoutRows = new ArrayList<char[]>(30);
        Matcher lineMatcher = LINE_PATTERN.matcher(rawString);

        while (lineMatcher.find()) {
            String layoutLine = lineMatcher.group(1);
            char[] charArr = layoutLine.toCharArray();

            if (columns == -1) {
                columns = charArr.length;
            } else if (columns != charArr.length) {
                throw new ParseException("Error parsing layout", layoutRows.size() + 1);
            }

            layoutRows.add(charArr);
        }

        LayoutArea[][] totalArea = new LayoutArea[layoutRows.size()][columns];

        LayoutArea currArea = null;

        char[] lastRow = layoutRows.get(layoutRows.size() - 1);

        for (int i = 0; i < lastRow.length; i++) {
            if (lastRow[i] == '%') {
                lastRow[i] = '-';
            }
        }

        for (int row = 0; row < layoutRows.size(); row++) {
            char[] currLayoutRow = layoutRows.get(row);
            boolean useNewArea = false;

            if (currLayoutRow[columns - 1] == '%') {
                currLayoutRow[columns - 1] = '|';
            }

            for (int col = 0; col < currLayoutRow.length; col++) {
                if (currLayoutRow[col] == '%' && currLayoutRow[col + 1] == '-') {
                    totalArea[row][col] = currArea = new LayoutArea();
                    useNewArea = true;
                    continue;
                } else if (currLayoutRow[col] == '%' && currLayoutRow[col + 1] != '-') {
                    useNewArea = false;
                }

                currArea = ((row == 0 || useNewArea) ? currArea : totalArea[row - 1][col]);
                totalArea[row][col] = currArea;
            }
            System.out.println(Arrays.toString(totalArea[row]));;
        }

        return null;
    }
}
