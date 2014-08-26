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
        int columns = -1;
        List<char[]> layoutRowsList = new ArrayList<char[]>(30);
        Matcher lineMatcher = LINE_PATTERN.matcher(rawString);

        while (lineMatcher.find()) {
            String layoutLine = lineMatcher.group(1);
            char[] charArr = layoutLine.toCharArray();

            if (columns == -1) {
                columns = charArr.length;
            } else if (columns != charArr.length) {
                throw new ParseException("Error parsing layout", layoutRowsList.size() + 1);
            }

            layoutRowsList.add(charArr);
        }

        char[][] layoutRows = new char[layoutRowsList.size()][columns];

        for (int row = 0; row < layoutRowsList.size(); row++) {
            layoutRows[row] = layoutRowsList.get(row);
        }

        char[][] transpose = new char[columns][layoutRows.length];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < layoutRows.length; j++) {
                char c = layoutRows[j][i];

                switch (c) {
                    case '-':
                        c = '|';

                        break;

                    case '|':
                        c = '-';

                        break;
                }

                transpose[i][j] = c;
            }
        }

        LayoutArea[][] totalArea = new LayoutArea[layoutRows.length][columns];
        char[] lastRow = layoutRows[layoutRows.length - 1];

        for (int i = 0; i < lastRow.length; i++) {
            if (lastRow[i] == '%') {
                lastRow[i] = '-';
            }
        }

        // Row-wise
        for (int row = 0; row < layoutRows.length; row++) {
            LayoutArea currArea = null;
            char[] currLayoutRow = layoutRows[row];
            boolean useNewArea = false;

            if (currLayoutRow[columns - 1] == '%') {
                currLayoutRow[columns - 1] = '|';
            }

            for (int col = 0; col < currLayoutRow.length; col++) {
                if (currLayoutRow[col] == '%') {
                    switch (currLayoutRow[col + 1]) {
                        case '-':
                            totalArea[row][col] = currArea = new LayoutArea();
                            useNewArea = true;

                            continue;
                        case '#':
                            throw new ParseException("parsing failed", row);
                        default:
                            useNewArea = false;
                            break;
                    }
                }

                currArea = (((row == 0) || useNewArea) ? currArea : totalArea[row - 1][col]);
                totalArea[row][col] = currArea;
            }
        }

        // Column-wise
        lastRow = transpose[transpose.length - 1];

        for (int i = 0; i < lastRow.length; i++) {
            if (lastRow[i] == '%') {
                lastRow[i] = '-';
            }
        }

        for (int row = 1; row < transpose.length; row++) {
            char[] currLayoutRow = transpose[row];
            boolean useExisting = false;

            if (currLayoutRow[currLayoutRow.length - 1] == '%') {
                currLayoutRow[currLayoutRow.length - 1] = '|';
            }

            for (int col = 0; col < currLayoutRow.length; col++) {
                if (currLayoutRow[col] == '%') {
                    switch (currLayoutRow[col + 1]) {
                        case '-':
                            useExisting = true;

                            continue;
                        case '#':
                            throw new ParseException("parsing failed", col);
                        default:
                            useExisting = false;
                            break;
                    }
                }

                if (!useExisting) {
                    totalArea[col][row] = totalArea[col][row - 1];
                }
            }
        }

        // Trim layout area
        for (int i = 0; i < totalArea.length - 1; i++) {
            totalArea[i] = Arrays.copyOf(totalArea[i], columns - 1);
        }

        return null;
    }
}
