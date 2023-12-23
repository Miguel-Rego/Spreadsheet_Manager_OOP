package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("value", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        String searchValue = stringField("value");
        List<Cell> matchingCells = new ArrayList<>();
        String functionName = "";
        for (int i = 1; i <= _receiver.get_rows(); i++) {
            for (int j = 1; j <= _receiver.get_columns(); j++) {
                Cell cell = _receiver.getCell(i, j);
                Content content = cell.getContent();
                if (content != null && content instanceof ComputeOperation) {
                    ComputeOperation function = (ComputeOperation) content;
                    switch (function.getOperator()) {
                        case "+":
                            functionName = "ADD";
                            break;
                        case "-":
                            functionName = "SUB";
                            break;
                        case "*":
                            functionName = "MUL";
                            break;
                        case "/":
                            functionName = "DIV";
                            break;
                    }
                    if (functionName.toLowerCase().contains(searchValue.toLowerCase())) {
                        matchingCells.add(cell);
                    }
                }
                if (content != null && content instanceof ComputeIntervalOperation) {
                    ComputeIntervalOperation function = (ComputeIntervalOperation) content;
                    functionName = function.getOperator();
                    if (functionName.toLowerCase().contains(searchValue.toLowerCase())) {
                        matchingCells.add(cell);
                    }
                }
            }
        }


        // Sort matching cells by function name, row, and column
        matchingCells.sort((cell1, cell2) -> {
            ComputeOperation func1 = (ComputeOperation) cell1.getContent();
            ComputeOperation func2 = (ComputeOperation) cell2.getContent();

            // Compare function names, case-insensitive
            int nameComparison = func1.getOperator().compareToIgnoreCase(func2.getOperator());
            if (nameComparison != 0) {
                return nameComparison;
            }

            // If function names are equal, compare by row and column
            int rowComparison = Integer.compare(cell1.getRow(), cell2.getRow());
            if (rowComparison != 0) {
                return rowComparison;
            }

            return Integer.compare(cell1.getColumn(), cell2.getColumn());
        });

        // Display matching cells
        for (Cell cell : matchingCells) {
            _display.popup(Spreadsheet.ShowCells(cell.getRow(), cell.getColumn(), cell.getContent()));
        }
    }
}
