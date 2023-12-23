package xxl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User implements Serializable {
    private String _name;
    private Set<Spreadsheet> spreadsheets;

    public User(String name) {
        _name = name;
        spreadsheets = new HashSet<>();
    }
    public String getName(){ return _name; }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User user = (User) obj;
        return Objects.equals(_name, user._name);
    }

    public int hashCode() {
        return Objects.hash(_name);
    }

    public void add(Spreadsheet sheet) {
        spreadsheets.add(sheet);
    }
}
