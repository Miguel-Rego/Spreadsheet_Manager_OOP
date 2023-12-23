package xxl;

import java.io.Serializable;

public abstract class Content implements Serializable {


    // Common methods and properties for content
    public abstract String toString();

    // This method calculates and returns the value of the content as a Literal
    public abstract Literal value();

    public String asString() {
        return value().asString();
    }

    public int asInt() {
        return value().asInt();
    }
}
