import java.util.Objects;

public class Goto {
    private String term;
    private Integer stateIndex;

    public Goto(String term, Integer stateIndex) {
        this.term = term;
        this.stateIndex = stateIndex;
    }

    @Override
    public String toString() {
        return "Goto{" +
                "term='" + term + '\'' +
                ", stateIndex=" + stateIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goto aGoto = (Goto) o;
        return Objects.equals(term, aGoto.term) &&
                Objects.equals(stateIndex, aGoto.stateIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, stateIndex);
    }
}
