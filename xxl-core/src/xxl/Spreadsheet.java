package xxl;

import java.util.*;
import java.io.Serial;
import java.io.Serializable;

import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet. Contains a two-dimensional array of cells.
 */
public class Spreadsheet implements Serializable {
        private int _rows;
        private int _columns;
        private Cell[][] grid;
        private CutBuffer _cutbuffer;
        private boolean _changed = true;


    public Spreadsheet(int rows, int columns){
        _rows = rows;
        _columns = columns;
        grid = new Cell[rows + 1][columns + 1];
        initializeCells(rows, columns);
    }
    @Serial
    private static final long serialVersionUID = 202308312359L;

    public int get_columns() {
        return _columns;
    }

    public int get_rows() {
        return _rows;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public CutBuffer get_cutbuffer() {
        return _cutbuffer;
    }
    public void set_cutbuffer(CutBuffer _cutbuffer) {
        this._cutbuffer = _cutbuffer;
    }

    public boolean is_changed() {
        return _changed;
    }

    public void set_changed(){
        _changed = true;
    }

    public void set_changed_false(){
        _changed = false;
    }

    public void initializeCells(int rows, int columns){
        for(int i = 1; i < rows + 1 ; i++){
            for(int j = 1; j < columns + 1 ; j++) {
                Cell cell = new Cell(i, j);
                grid[i][j] = cell;
            }
        }
    }
    public void cutCells(int startRow, int startColumn, int endRow, int endColumn) {
        int cont = 1;
        Cell[][] clipboard = new Cell[endRow + 1][endColumn + 1];
        // Copy cells from the original spreadsheet to the clipboard
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                if(startRow == endRow){
                    Cell originalCell = getCell(row, column);
                    Cell dupCell = new Cell(originalCell.getRow(), originalCell.getColumn());
                    dupCell.setContent(originalCell.getContent());
                    clipboard[1][cont] = dupCell;;
                }
                else{
                    Cell originalCell = getCell(row, column);
                    Cell dupCell = new Cell(originalCell.getRow(), originalCell.getColumn());
                    dupCell.setContent(originalCell.getContent());
                    clipboard[cont][1] = dupCell;
                }
                cont += 1;
                insertContents(row, column, null);
            }
        }
        _cutbuffer.setClipboard(clipboard);
        set_changed();
    }
    public void copyCells(int startRow, int startColumn, int endRow, int endColumn) {
        int cont = 1;
        Cell[][] clipboard = new Cell[endRow + 1][endColumn + 1];
        // Copy cells from the original spreadsheet to the clipboard
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                if(startRow == endRow){
                    Cell originalCell = getCell(row, column);
                    Cell dupCell = new Cell(originalCell.getRow(), originalCell.getColumn());
                    dupCell.setContent(originalCell.getContent());
                    clipboard[1][cont] = dupCell;
                }
                else{
                    Cell originalCell = getCell(row, column);
                    Cell dupCell = new Cell(originalCell.getRow(), originalCell.getColumn());
                    dupCell.setContent(originalCell.getContent());
                    clipboard[cont][1] = dupCell;
                }
                cont += 1;
            }
        }
        _cutbuffer.setClipboard(clipboard);
    }
    public void pasteCells(int startRow, int startColumn, int endRow, int endColumn) {
        int cont = 1;
        Cell[][] clipboard = _cutbuffer.getClipboard();
        if(startRow == endRow && startColumn == endColumn){
            if(_cutbuffer.get_rows_size()  > _cutbuffer.get_columns_size()){
                for(int res = 1; res < _cutbuffer.get_rows_size() && res < get_rows() + 2 - startRow; res++){
                    Cell clipboardCell = clipboard[res][1];
                    if (clipboardCell != null) {
                        Content content = clipboardCell.getContent();
                        // Insert the copied or cut content into the original cell
                        insertContents(startRow + res - 1, startColumn, content);
                    }
                    else{
                        insertContents(startRow + res - 1, startColumn, null);
                    }
                }
            }
            else{
                for(int res = 1; res < _cutbuffer.get_columns_size() + 1 && res < get_columns() + 2 - startColumn; res++){
                    Cell clipboardCell = clipboard[1][res];
                    if (clipboardCell != null) {
                        Content content = clipboardCell.getContent();
                        // Insert the copied or cut content into the original cell
                        insertContents(startRow , startColumn + res - 1, content);
                    }
                else{
                    insertContents(startRow , startColumn + res - 1, null);
                    }
                }
            }
            set_changed();
        }
        else{
            for (int row = startRow; row <= endRow; row++) {
                for (int column = startColumn; column <= endColumn; column++) {
                    if(startRow == endRow){
                        Cell clipboardCell = clipboard[1][cont];
                        if (clipboardCell != null) {
                            Content content = clipboardCell.getContent();
                            // Insert the copied or cut content into the original cell
                            insertContents(row, column, content);
                        }
                        else{
                            insertContents(row, column, null);
                        }
                    }
                    else{
                        Cell clipboardCell = clipboard[cont][1];
                        if (clipboardCell != null) {
                            Content content = clipboardCell.getContent();
                            // Insert the copied or cut content into the original cell
                            insertContents(row, column, content);
                        }
                       else{
                        insertContents(row, column, null);
                        }
                    }
                    cont += 1;
                }
            }
        }
        set_changed();
    }


    public static String ShowCells(int row, int column, Content content){
        return row + ";" + column + "|" + content.asString();
    }
    public static String ShowCellsNull(int row, int column){
        return row + ";" + column + "|";
    }

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }


        /**
         * Insert specified content in specified range.
         * @param row
         * @param column
         * @param contentSpecification
         */
    public void insertContents(int row, int column, Content contentSpecification) {
        grid[row][column].setContent(contentSpecification);
        set_changed();
    }
}
