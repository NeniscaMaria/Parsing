import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private List<Set<State>> states;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        collectionCanonical();
        System.out.println("The states are: ");
        states.forEach(System.out::println);
    }

    //what a state contains
    private Set<State> closure(Set<State> state){
        
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
        states = new ArrayList<>();
        states.add(closure(firstState));
        int noStates;
        do{
            noStates = states.size();
            for(int i=0; i<states.size(); i++){
                Set<State> s = states.get(i);
                for(State a : s){
                    List<String> rhs = a.getRhs();
                    int indexOfDot = rhs.indexOf(".");
                    if(indexOfDot != rhs.size()-1) {
                        String x = rhs.get(indexOfDot + 1);
                        Set<State> j = gotoLR(s, x);
                        if(!states.contains(j)) {
                            states.add(j);
                        }
                    }
                }
            }
        }while(noStates != states.size());
    }

}
