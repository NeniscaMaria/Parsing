import javafx.util.Pair;

import java.util.*;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private List<Set<State>> states;
    //the state in pos integer goes to set<state> with string
    private List<Pair<Pair<String,Integer>, Set<State>>> mapping;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        mapping = new ArrayList<>();
        collectionCanonical();
        System.out.println("The states are: ");
        //states.forEach(System.out::println);
        mapping.forEach(System.out::println);
    }

    //what a state contains
    private Set<State> closure(Set<State> state){
        List<State> result = new ArrayList<>(state);
        int size;
        do{
            size = result.size();
            for(int i=0; i<result.size(); i++){
                State s = result.get(i);
                int indexDot = s.getRhs().indexOf(".");
                if(indexDot != s.getRhs().size()-1) {
                    String b = s.getRhs().get(indexDot + 1);
                    List<Production> productions = grammar.getProductionsForNonterminal(b);
                    for(Production p : productions){
                        for(List<String> rule : p.getRules()) {
                            List<String> rhs = new ArrayList<>();
                            rhs.add(".");
                            rhs.addAll(rule);
                            State newState = new State(p.getStart(), rhs);
                            if(!result.contains(newState))
                                result.add(newState);
                        }
                    }
                }
            }
        }while(size != result.size());
        return Set.copyOf(result);
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
        mapping.add(new Pair<>(new Pair<>("-",0),closure(firstState)));
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
                        Pair p = new Pair<>(new Pair<>(x,i),j);
                        if(!mapping.contains(p))
                            mapping.add(p);
                        if(!states.contains(j)) {
                            states.add(j);
                        }
                    }
                }
            }
        }while(noStates != states.size());
    }

}
