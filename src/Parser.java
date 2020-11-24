import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private List<Set<Item>> states;
    //the state in pos integer goes to set<state> with string
    private List<Pair<Pair<String,Integer>, Set<Item>>> mapping;
    //<pos state in states, term/nonterm> --> <pos next state in states, action name>
    private Map<Pair<Integer,String>,Pair<Integer,String>> action;
    //pos state in states --> <term/nonterm, pos next state in states>
    private Map<Integer,List<Goto>> goTo;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        mapping = new ArrayList<>();
        action = new HashMap<>();
        goTo = new HashMap<>();
        collectionCanonical();
        System.out.println("The states are: ");
        mapping.forEach(System.out::println);
        formTable();
         for(int i=0;i<states.size();i++){
            System.out.println(i+"--"+states.get(i));
        }
        System.out.println("action=========");
        System.out.println(action);
        System.out.println("goto==========");
        goTo.forEach((k,v)-> System.out.println(k+"---"+v));
    }

    //what a state contains
    private Set<Item> closure(Set<Item> state){
        /* Allocate space for the result. */
        List<Item> setOfStates = new ArrayList<>(state);
        boolean changed = true;
        while(changed){ // fixed-point approach to compute closure
            int oldSize = setOfStates.size();
            for(int i=0;i<setOfStates.size();i++) {
                Item s = setOfStates.get(i);
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
                            Item newState=new Item(currentProduction.getStart(),newRhs);
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
    private Set<Item> gotoLR(Set<Item> s, String x){
        Set<Item> nextStates = new HashSet<>();
        if(!s.isEmpty()){
            for(Item state : s){
                if(state.getRhs().contains(x)) {
                    List<String> rhs = new ArrayList<>(state.getRhs());
                    int indexDot = rhs.indexOf(".");
                    if(indexDot + 1 == rhs.indexOf(x)) {
                        rhs.remove(indexDot);
                        rhs.add(indexDot + 1, ".");
                        nextStates.add(new Item(state.getLhs(), rhs));
                    }
                }
            }
            return closure(nextStates);
        }
        return nextStates;
    }

    private Set<Item> getFirstState(){
        Production firstProduction = grammar.getProductions().get(0);
        List<String> rhs = new ArrayList<>();
        rhs.add(".");
        rhs.addAll(firstProduction.getRules().get(0));
        Item firstState = new Item(firstProduction.getStart(), rhs);
        Set<Item> ss = new HashSet<>();
        ss.add(firstState);
        return ss;
    }

    //construct set of states
    private void collectionCanonical(){
        Set<Item> firstState = getFirstState();
        states = new ArrayList<>();
        states.add(closure(firstState));
        mapping.add(new Pair<>(new Pair<>("-",0),closure(firstState)));
        int noStates;
        do{
            noStates = states.size();
            for(int i=0; i<states.size(); i++){
                Set<Item> s = states.get(i);
                for(Item a : s){
                    List<String> rhs = a.getRhs();
                    int indexOfDot = rhs.indexOf(".");
                    if(indexOfDot != rhs.size()-1) {
                        String x = rhs.get(indexOfDot + 1);
                        Set<Item> j = gotoLR(s, x);
                        Pair p = new Pair<>(new Pair<>(x,i),j);
                        if(!states.contains(j)) {
                            states.add(j);
                        }
                        if(!mapping.contains(p)) {
                            mapping.add(p);
                            Goto g = new Goto(x,states.indexOf(j));
                            if (goTo.containsKey(i)) {
                                goTo.get(i).add(g);
                            }else {
                                List<Goto> list = new ArrayList<>();
                                list.add(g);
                                goTo.put(i, list);
                            }
                        }

                    }
                }
            }
        }while(noStates != states.size());
    }

    private void formTable(){
        Production firstProduction = grammar.getProductions().get(0);
        List<String> rhs = new ArrayList<>(firstProduction.getRules().get(0));
        rhs.add(".");
        Item acceptanceState = new Item(firstProduction.getStart(), rhs);

        for(int i=0;i<states.size();i++) {
            Set<Item> state = states.get(i);
            AtomicReference<Item> stateWithFinalDot = new AtomicReference<>();
            state.forEach(s->{
                if(s.getRhs().get(s.getRhs().size() - 1).equals("."))
                    stateWithFinalDot.set(s);
            });
            if(stateWithFinalDot.get()!= null && stateWithFinalDot.get()!=acceptanceState) {
                for (String nonterminal : grammar.getNonTerminals()) {
                    String actionS = "";
                    int next = -1;
                    action.put(new Pair<>(i, nonterminal), new Pair<>(next, "reduce"));
                }
            }
            for(int j=0; j<mapping.size();j++){
                int positionPrevState = mapping.get(j).getKey().getValue();
                if(positionPrevState == i) {
                    String t = mapping.get(j).getKey().getKey();
                    int positionNextState = states.indexOf(mapping.get(j).getValue());
                    Set<Item> nextState = mapping.get(i).getValue();
                    if (nextState.contains(acceptanceState)) {
                        //accept
                        action.put(new Pair<>(i, t), new Pair<>(positionNextState, "accept"));
                        System.out.println("accept");
                    }
                    if (grammar.getTerminals().contains(t)) {
                        //shift to position next state
                        action.put(new Pair<>(i, t), new Pair<>(positionNextState, "shift"));
                    }

                }

            }
        }
    }

}