package xxl;

import java.io.Serializable;

public class Cell implements Serializable {
    private int _row;
    private int _column;
    private Content content;

    public Cell(int row, int column) {
        this._row = row;
        this._column = column;
        this.content = null; // Initialize content to null
    }

    public int getRow() {
        return _row;
    }

    public void setRow(int row) {
        this._row = row;
    }

    public int getColumn() {
        return _column;
    }

    public void setColumn(int column) {
        this._column = column;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content c) {
        this.content = c;
    }

    public Literal value() {
        if (Literal.class.isAssignableFrom(content.getClass())) {
            return ((Literal) content).value();
        } else {
            return null; // Return null for non-literal content
        }
    }

    @Override
    public String toString() {
        return _row +";" +
                _column + "|" +
                content;
    }
}
