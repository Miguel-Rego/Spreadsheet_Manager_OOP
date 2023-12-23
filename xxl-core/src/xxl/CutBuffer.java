package xxl;

import java.io.Serializable;

public class CutBuffer implements Serializable {
    private Cell[][] clipboard;

    public CutBuffer(int rows, int cols){
        clipboard = new Cell[rows + 1][cols + 1];
    }

    public void setClipboard(Cell[][] clipboard) {
        this.clipboard = clipboard;
    }

    public Cell[][] getClipboard() {
        return clipboard;
    }

    public int get_rows_size() {
        if (clipboard != null) {
            return clipboard.length - 1; // Returns the number of rows
        } else {
            return 0; // Handle the case when clipboard is null
        }
    }

    public int get_columns_size() {
        if (clipboard != null && clipboard.length > 0) {
            return clipboard[0].length - 1; // Returns the number of columns in the first row (assuming all rows have the same number of columns)
        } else {
            return 0; // Handle the case when clipboard is null or empty
        }
    }
    public String showCutBuffer() {
        int cont = 0;
        StringBuilder result = new StringBuilder();
        for (int row = 1; row <= get_rows_size(); row++) {
            for (int column = 1; column <= get_columns_size(); column++) {
                Cell cell = clipboard[row][column];
                if (cell != null) {
                    Content content = cell.getContent();
                    if (content != null) {
                        result.append(ShowCells(row, column, content));
                    } else {
                        result.append(ShowCellsNull(row, column));
                    }
                    cont += 1;
                    // Append a newline character if not the last cell
                    if ((column > 1 && cont == get_columns_size()) || (row > 1 && cont == get_rows_size())) {
                        break;
                    }
                    else{
                            result.append("\n");

                    }
                }
                else break;
            }
        }
        return result.toString();
    }
    public static String ShowCells(int row, int column, Content content){
        return row + ";" + column + "|" + content.asString();
    }
    public static String ShowCellsNull(int row, int column){
        return row + ";" + column + "|";
    }

}

