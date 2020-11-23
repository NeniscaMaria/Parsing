import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private Set<Set<State>> states;
    //parsing steps
    //1.define item
    //LR(0) item: [A -> a.B]
    //2.construct set of states
    //closure, goto and colcan
    //3.construct table


    public Parser(Grammar grammar) {
        this.grammar = grammar;
        //generation of states is done only once at the beginning
        collectionCanonical();
    }

    //what a state contains
    private Set<State> closure(Set<State> state){
        return new HashSet<>();
    }

    //how to move from a state to another
    private Set<State> gotoLR(Set<State> s, String x){
        Set<State> nextStates = new HashSet<>();
        if(!s.isEmpty()){
            for(State state : s){
                if(state.getRhs().contains(x)) {
                    List<String> rhs = new ArrayList<>(state.getRhs());
                    int indexDot = rhs.indexOf(".");
                    if(indexDot + 1 == rhs.indexOf(x)) {
                        rhs.remove(indexDot);
                        rhs.add(indexDot + 1, ".");
                        nextStates.add(new State(state.getLhs(), rhs));
                    }
                }
            }
            return closure(nextStates);
        }
        return nextStates;
    }

    private Set<State> getFirstState(){
        Production firstProduction = grammar.getProductions().get(0);
        List<String> rhs = new ArrayList<>();
        rhs.add(".");
        rhs.addAll(firstProduction.getRules().get(0));
        State firstState = new State(firstProduction.getStart(), rhs);
        Set<State> ss = new HashSet<>();
        ss.add(firstState);
        return ss;
    }

    //construct set of states
    private void collectionCanonical(){
        Set<State> firstState = getFirstState();
        states = new HashSet<>();
        states.add(closure(firstState));
        int noStates;
        do{
            noStates = states.size();
            for(Set<State> s : states){
                for(State a : s){
                    List<String> rhs = a.getRhs();
                    int indexOfDot = rhs.indexOf(".");
                    if(indexOfDot != rhs.size()) {
                        String x = rhs.get(indexOfDot + 1);
                        Set<State> j = gotoLR(s, x);
                        states.add(j);
                    }
                }
            }
        }while(noStates != states.size());
    }

}
