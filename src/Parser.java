import javafx.util.Pair;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
        /* Allocate space for the result. */
        List<State> setOfStates = new ArrayList<>(state);
        boolean changed = true;
        while(changed){ // fixed-point approach to compute closure
            int oldSize = setOfStates.size();
            for(int i=0;i<setOfStates.size();i++) {
                State s = setOfStates.get(i);
                List<String> rhs = new ArrayList<>(s.getRhs());
                int indexDot = rhs.indexOf(".");

                if (indexDot != rhs.size() - 1) {
                    String b =rhs.get(indexDot+1);
                    List<Production> productions=grammar.getProductionsForNonterminal(b);

                    for (int j = 0; j < productions.size(); j++) {
                        Production currentProduction = productions.get(j);
                        for(List<String> rule : currentProduction.getRules()){
                            List<String> newRhs = new ArrayList<>();
                            newRhs.add(".");
                            newRhs.addAll(rule);
                            State newState=new State(currentProduction.getStart(),newRhs);
                            if(!setOfStates.contains(newState))
                                setOfStates.add(newState);
                        }
                    }
                }
            }
            changed = (setOfStates.size() != oldSize);
        }
        return Set.copyOf(setOfStates);
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

    private boolean checkLR0(Set<State> s){
        Set<String> last = new HashSet<>();
        AtomicBoolean result = new AtomicBoolean(true);
        s.forEach(ss->{
            if(ss.getRhs().indexOf(".") == ss.getRhs().size())
                if(!last.add(ss.getRhs().get(ss.getRhs().size()-1)))
                    result.set(false);
        });
        return result.get();
    }

    //construct set of states
    private boolean collectionCanonical(){
        Set<State> firstState = getFirstState();
        states = new HashSet<>();
        states.add(closure(firstState));
        mapping.add(new Pair<>(new Pair<>("-",0),closure(firstState)));
        int noStates;
        boolean isLR0 = true;
        do{
            noStates = states.size();
            for(Set<State> s : states){
                isLR0 = checkLR0(s);
                for(State a : s){
                    List<String> rhs = a.getRhs();
                    int indexOfDot = rhs.indexOf(".");
                    if(indexOfDot != rhs.size()) {
                        String x = rhs.get(indexOfDot + 1);
                        Set<State> j = gotoLR(s, x);
                        if(!states.contains(j)) {
                            states.add(j);
                        }
                    }
                }
            }
        }while(noStates != states.size() && isLR0);
        return isLR0;
    }

}
