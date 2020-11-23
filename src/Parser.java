import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private Set<Set<State>> states;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        //generation of states is done only once at the beginning
        if(!collectionCanonical())
            System.out.println("This grammar is not LR(0)");
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
                        states.add(j);
                    }
                }
            }
        }while(noStates != states.size() && isLR0);
        return isLR0;
    }

}
