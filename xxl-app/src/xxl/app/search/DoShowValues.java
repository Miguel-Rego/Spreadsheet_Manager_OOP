package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Cell;
import xxl.Content;
import xxl.LiteralString;
import xxl.LiteralInteger;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

import java.util.ArrayList;
import java.util.List;
// FIXME import classes

/**
 * Command for searching content values.
 */
class DoShowValues extends Command<Spreadsheet> {

    DoShowValues(Spreadsheet receiver) {
        super(Label.SEARCH_VALUES, receiver);
        addStringField("value", Prompt.searchValue());
    }

    protected final void execute() {
        String searchValue = stringField("value");
        List<Cell> matchingCells = new ArrayList<>();

        for (int i = 1; i <= _receiver.get_rows(); i++) {
            for (int j = 1; j <= _receiver.get_columns(); j++) {
                Cell cell = _receiver.getCell(i, j);
                Content content = cell.getContent();
                if (content != null) {
                    LiteralInteger intValue = content.value() instanceof LiteralInteger
                            ? (LiteralInteger) content.value()
                            : null;
                    LiteralString strValue = content.value() instanceof LiteralString
                            ? (LiteralString) content.value()
                            : null;
                    if ((intValue != null && !searchValue.startsWith("'") && intValue.asInt() == Integer.parseInt(searchValue))
                            || (strValue != null && strValue.asString().equals(searchValue))) {
                        matchingCells.add(cell);
                    }
                }
            }
        }

        // Display matching cells
        for (Cell cell : matchingCells) {
            _display.popup(Spreadsheet.ShowCells(cell.getRow(), cell.getColumn(), cell.getContent()));
        }
    }
}

