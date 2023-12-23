package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.CutBuffer;

import java.util.ArrayList;
import java.util.List;
// FIXME import classes

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
        // FIXME add fields
    }

    @Override
    protected final void execute() throws CommandException {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Form Copiar = new Form("Copiar");
        Copiar.addStringField("range", Prompt.address());
        Copiar.parse();
        String input = Copiar.stringField("range");
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
         CutBuffer cutbuffer = new CutBuffer(_receiver.get_rows(), _receiver.get_columns());
        _receiver.set_cutbuffer(cutbuffer);
        _receiver.copyCells(list1.get(0), list2.get(0), list1.get(list1.size() - 1), list2.get(list2.size() - 1));
    }

}
