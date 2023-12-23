package xxl;

import xxl.exceptions.UnrecognizedEntryException;

public class Edit {
    public static void InsertContent(Spreadsheet spreadsheet, String[] stringfields) throws UnrecognizedEntryException {
        new Parser(spreadsheet, stringfields);
    }
}
