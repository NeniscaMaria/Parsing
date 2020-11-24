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
}
