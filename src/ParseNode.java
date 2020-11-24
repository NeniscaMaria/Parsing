import java.util.Objects;

public class ParseNode {
    private String value;
    private int father;
    private int sibling;

    public ParseNode(String value, int father, int sibling) {
        this.value = value;
        this.father = father;
        this.sibling = sibling;
    }

    public String getValue() {
        return value;
    }

    public int getFather() {
        return father;
    }

    public int getSibling() {
        return sibling;
    }

    @Override
    public String toString() {
        return "   |  " + value + "  |   " + father + "   |  " + sibling;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParseNode parseNode = (ParseNode) o;
        return father == parseNode.father &&
                sibling == parseNode.sibling &&
                Objects.equals(value, parseNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, father, sibling);
    }
}
