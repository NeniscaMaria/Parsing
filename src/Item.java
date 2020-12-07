import java.util.List;
import java.util.Objects;

public class Item {
    //left hand side of prod
    private String lhs;
    //right hand side of prod
    private List<String> rhs;

    public Item(String lhs, List<String> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLhs() {
        return lhs;
    }

    public List<String> getRhs() {
        return rhs;
    }

    @Override
    public String toString() {
        return "Item{" +
                "lhs='" + lhs + '\'' +
                ", rhs=" + rhs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item state = (Item) o;
        return Objects.equals(lhs, state.lhs) &&
                Objects.equals(rhs, state.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
