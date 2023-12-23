package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Cell;
import xxl.Edit;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

import java.util.ArrayList;
import java.util.List;
// FIXME import classes

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        // FIXME add fields
    }

    @Override
    protected final void execute() throws CommandException {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Form Inserir = new Form("Inserir");
        Inserir.addStringField("range", Prompt.address());
        Inserir.addStringField("content", Prompt.content());
        Inserir.parse();
        String inputRange = Inserir.stringField("range");
        String[] parts = inputRange.split("[:;]");
        int maxRows = _receiver.get_rows();
        int maxColumns = _receiver.get_columns();


        if (parts.length == 4) {
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            int num3 = Integer.parseInt(parts[2]);
            int num4 = Integer.parseInt(parts[3]);

            if (num1 < 0 || num1 > maxRows || num3 < 0 || num3 > maxRows ||
                    num2 < 0 || num2 > maxColumns || num4 < 0 || num4 > maxColumns) {
                throw new InvalidCellRangeException(inputRange);
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
                throw new InvalidCellRangeException(inputRange);
            }
        } else if (parts.length == 2) {
            int num1 = Integer.parseInt(parts[0]);
            int num2 = Integer.parseInt(parts[1]);
            if (num1 < 0 || num1 > maxRows ||
                    num2 < 0 || num2 > maxColumns) {
                throw new InvalidCellRangeException(inputRange);
            }
            list1.add(num1);
            list2.add(num2);
        }
        String inputContent = Inserir.stringField("content");
        Cell[][] grid1 = _receiver.getGrid();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                Cell cell = grid1[list1.get(i)][list2.get(j)];
                int cellRow = cell.getRow();
                int cellColumn = cell.getColumn();
                String line = cellRow + ";" + cellColumn + "|" + inputContent;
                String[] fields = line.split("\\|");
                try {
                    Edit.InsertContent(_receiver, fields);
                } catch (UnrecognizedEntryException e) {
                    throw new UnknownFunctionException(fields[2]);
                }


            }
        }
        _receiver.set_changed();
    }
}
