import java.util.List;

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
}
