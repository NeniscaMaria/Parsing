import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private Set<State> states;
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
    private Set<State> closure(State state){
        return new HashSet<>();
    }

    //how to move from a state to another
    private Set<State> gotoLR(State s, String x){
        return new HashSet<>();
    }

    //construct set of states
    private void collectionCanonical(){
        states = new HashSet<>();
        Production firstProduction = grammar.getProductions().get(0);
        System.out.println(firstProduction);
        State firstState = new State(firstProduction.getStart(), firstProduction.getRules().get(0));
        System.out.println(firstState);
        Set<State> s0 = closure(firstState);
        //states.addAll(s0);
        int noStates;
        do{
            noStates = states.size();
            for(State s : states){
                List<String> concat = new ArrayList<>();
                concat.addAll(grammar.getNonTerminals());
                concat.addAll(grammar.getTerminals());
                System.out.println(concat);
                for(String x : concat){
                    Set<State> nextStates = gotoLR(s,x);
                    if(!nextStates.isEmpty() && !nextStates.equals(states)){
                        states.addAll(nextStates);
                    }
                }
            }
        }while(noStates != states.size());
    }

}
