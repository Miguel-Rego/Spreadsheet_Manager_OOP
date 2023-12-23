package xxl;

public class Reference extends Content {
    private int _row;
    private int _column;
    private Spreadsheet _spreadsheet;

    public Reference(int row, int column, Spreadsheet spreadsheet) {
        this._row = row;
        this._column = column;
        this._spreadsheet = spreadsheet;
    }

    public int get_column() {
        return _column;
    }

    public int get_row() {
        return _row;
    }

    public String toString() {
        return "Reference{" +
                "_row=" + _row +
                ", _column=" + _column +
                '}';
    }
    public String asString() {
        if (value() == null){
            return "#VALUE" + "=" + _row + ";" + _column;
        }
        return value().asString() + "=" + _row + ";" + _column;
    }

    public Literal value() {
        Cell ReferenceCell = _spreadsheet.getCell(_row, _column);
        if(ReferenceCell.getContent() == null){
            return null;
        }
        Literal ReferencedContent = ReferenceCell.getContent().value();
        return ReferencedContent;
    }
}
