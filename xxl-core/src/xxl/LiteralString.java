package xxl;

public class LiteralString extends Literal {
    private String _value;

    public LiteralString(String value) {
        this._value = value;
    }

    public String toString() {
        return  _value;
    }

    public String asString() {
        return _value;
    }

    public int asInt() {
        return 0;
    }

    public Literal value() {
        return this;
    }
}
