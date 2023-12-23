package xxl;

public abstract class Function extends Content {
    protected String _name;

    public Function(String name) {
        this._name = name;
    }

    public String toString() {
        return "Function{" +
                 _name + '\'' +
                '}';
    }

    public abstract Literal compute();

    public Literal value() {
        return compute();
    }
}
