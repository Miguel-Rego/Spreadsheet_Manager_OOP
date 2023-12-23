package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.Spreadsheet;
import xxl.exceptions.MissingFileAssociationException;

import java.io.IOException;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
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
        Form Novo = new Form("Novo");
        Novo.addIntegerField("lines", Prompt.lines());
        Novo.addIntegerField("columns", Prompt.columns());
        Novo.parse();
        Spreadsheet spreadsheet = _receiver.createnewSpreadsheet(Novo.integerField("lines"), Novo.integerField("columns"));
    }

}
