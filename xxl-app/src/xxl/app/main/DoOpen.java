package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.ImportFileException;
import xxl.Calculator;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

import java.io.IOException;
// FIXME import classes

/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
        addStringField("file", Prompt.openFile());
    }

    @Override
    protected final void execute() throws CommandException {
        if(_receiver.getSpreadsheet() != null && _receiver.getSpreadsheet().is_changed()){
            if(Form.confirm(Prompt.saveBeforeExit())){
                try {
                    try {
                        _receiver.save();
                    } catch (MissingFileAssociationException e1) {
                        try {
                            _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
                        } catch (MissingFileAssociationException e2) {
                        }
                    }
                } catch (IOException e) {
                }
            }
        }
        try {
             _receiver.importFile(stringField("file"));
        } catch ( ImportFileException e) {
            throw new FileOpenFailedException(e);
        }

    }
}


