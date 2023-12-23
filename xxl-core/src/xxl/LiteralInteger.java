package xxl;

public class LiteralInteger extends Literal {
    private int _value;

    public LiteralInteger(int value) {
        this._value = value;
    }

    public String toString() {
        return "LiteralInteger{" +
                "_value=" + _value +
                '}';
    }

    public String asString() {
        return String.valueOf(_value);
    }

    public int asInt() {
        return _value;
    }

    public Literal value() {
        return this;
    }
}
