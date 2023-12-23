package xxl;

import java.util.ArrayList;
import java.util.List;
import xxl.Parser;
import xxl.exceptions.UnrecognizedEntryException;

public class Range {
    private List<Integer> listRows = new ArrayList<>();
    private List<Integer> listCols = new ArrayList<>();

    public Range(String rangeDescription, int rows, int columns){
        String[] parts = rangeDescription.split("[:;]");
        int maxRows = rows;
        int maxColumns = columns;
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[1]);
        int num3 = Integer.parseInt(parts[2]);
        int num4 = Integer.parseInt(parts[3]);

        if (num1 < 0 || num1 > maxRows || num3 < 0 || num3 > maxRows ||
                num2 < 0 || num2 > maxColumns || num4 < 0 || num4 > maxColumns) {
            listRows = null;
            listCols = null;
        }
            if ((num1 == num3 && num2 == num4) || (num1 == num3 && num2 != num4) || (num1 != num3 && num2 == num4)) {
                if (num1 == num3) {
                    listRows.add(num1);
                    for (int i = Math.min(num2, num4); i <= Math.max(num2, num4); i++) {
                        listCols.add(i);
                    }
                } else {
                    listCols.add(num2);
                    for (int i = Math.min(num1, num3); i <= Math.max(num1, num3); i++) {
                        listRows.add(i);
                    }
                }


            } else {
                listRows = null;
                listCols = null;
            }

    }
    public void InsertRangeContent(String[] spreadLine, Spreadsheet spreadsheet) throws UnrecognizedEntryException {
        new Parser(spreadsheet, spreadLine);
    }

    public List<Integer> getListRows() {
        return listRows;
    }

    public List<Integer> getListCols() {
        return listCols;
    }
}
