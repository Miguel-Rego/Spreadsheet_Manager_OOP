package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import xxl.Calculator;
import java.io.FileNotFoundException;
import java.io.IOException;
import xxl.exceptions.MissingFileAssociationException;
// FIXME import classes

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
    protected final void execute() {

        try {
            try {
                _receiver.save();
            } catch (MissingFileAssociationException e1) {
                try {
                    _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
                    // _receiver.getSpreadsheet().set_changed_false();
                } catch (MissingFileAssociationException e2) {
                    _receiver.getSpreadsheet().set_changed();
                }
            }
        } catch (IOException e) {
            _receiver.getSpreadsheet().set_changed();
        }
    }
}
