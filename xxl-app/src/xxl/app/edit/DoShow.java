package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
import xxl.*;

import java.util.ArrayList;
import java.util.List;
import xxl.app.edit.*;

// FIXME import classes

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {
    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
    }


    @Override
    protected final void execute() throws CommandException {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Form Visualizar = new Form("Visualizar");
        Visualizar.addStringField("range", Prompt.address());
        Visualizar.parse();
        String input = Visualizar.stringField("range");
        String[] parts = input.split("[:;]"); // Split the string by either ':' or ';'
        int maxRows = _receiver.get_rows();
        int maxColumns = _receiver.get_columns();


        if (parts.length == 4) {
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            int num3 = Integer.parseInt(parts[2]);
            int num4 = Integer.parseInt(parts[3]);

            if (num1 < 0 || num1 > maxRows || num3 < 0 || num3 > maxRows ||
                    num2 < 0 || num2 > maxColumns || num4 < 0 || num4 > maxColumns) {
                throw new InvalidCellRangeException(input);
            }


            if ((num1 == num3 && num2 == num4) || (num1 == num3 && num2 != num4) || (num1 != num3 && num2 == num4)) {
                if (num1 == num3) {
                    list1.add(num1);
                    for (int i = Math.min(num2, num4); i <= Math.max(num2, num4); i++) {
                        list2.add(i);
                    }
                } else {
                    list2.add(num2);
                    for (int i = Math.min(num1, num3); i <= Math.max(num1, num3); i++) {
                        list1.add(i);
                    }
                }

            } else {
                    throw new InvalidCellRangeException(input);
            }
        } else if (parts.length == 2) {
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            if (num1 < 0 || num1 > maxRows ||
                    num2 < 0 || num2 > maxColumns) {
                throw new InvalidCellRangeException(input);
            }
            list1.add(num1);
            list2.add(num2);
        }
        Cell[][] grid1 = _receiver.getGrid();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                Cell cell = grid1[list1.get(i)][list2.get(j)];
                int cellRow = cell.getRow();
                int cellColumn = cell.getColumn();
                Content cellContent = cell.getContent();
                if (cellContent == null){
                    _display.popup(Spreadsheet.ShowCellsNull(cellRow, cellColumn));
                }
                else{
                    _display.popup(Spreadsheet.ShowCells(cellRow, cellColumn, cellContent));
                }
            }
        }
    }

}
