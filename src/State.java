import java.util.List;
import java.util.Objects;

public class State {
    //left hand side of prod
    private String lhs;
    //right hand side of prod
    private List<String> rhs;

    public State(String lhs, List<String> rhs) {
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
        return "State{" +
                "lhs='" + lhs + '\'' +
                ", rhs=" + rhs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(lhs, state.lhs) &&
                Objects.equals(rhs, state.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
