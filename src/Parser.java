import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    //lr(0)
    private Grammar grammar;
    private List<Set<Item>> states;
    //pos state in states --> action name
    private Map<Integer,String> action;
    //pos state in states --> goto
    private Map<Integer,Set<Goto>> goTo;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        action = new HashMap<>();
        goTo = new HashMap<>();
        collectionCanonical();
        System.out.println("The states are: ");
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
        int noStates;
        do{
            noStates = states.size();
            for(int i=0; i<states.size(); i++) {
                Set<Item> s = states.get(i);
                for (Item a : s) {
                    List<String> rhs = a.getRhs();
                    int indexOfDot = rhs.indexOf(".");
                    if (indexOfDot != rhs.size() - 1) {
                        String x = rhs.get(indexOfDot + 1);
                        Set<Item> j = gotoLR(s, x);
                        Pair p = new Pair<>(new Pair<>(x, i), j);
                        if (!states.contains(j)) {
                            states.add(j);
                        }
                        Goto g = new Goto(x, states.indexOf(j));
                        if (goTo.containsKey(i)) {
                            goTo.get(i).add(g);
                        } else {
                            Set<Goto> list = new HashSet<>();
                            list.add(g);
                            goTo.put(i, list);
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
            AtomicBoolean foundFinal = new AtomicBoolean(false);
            //find an item with dot on last position in the state
            AtomicReference<Item> stateWithFinalDot = new AtomicReference<>(new Item("",new ArrayList<>()));
            state.forEach(s->{
                if(s.getRhs().get(s.getRhs().size() - 1).equals(".")) {
                    if(stateWithFinalDot.get().getLhs().equals("")){
                        System.out.println("Reduce-reduce error.");
                    }
                    stateWithFinalDot.set(s);
                }
                System.out.println(s.toString()+ acceptanceState.toString() +s.equals(acceptanceState));
                foundFinal.set(s.equals(acceptanceState));
            });
            if(foundFinal.get()){
                action.put(i,"accept");
            }else {
                //check if it can be reduces
                if (stateWithFinalDot.get() != null && stateWithFinalDot.get() != acceptanceState) {
                    for (String nonterminal : grammar.getNonTerminals()) {
                        String actionS = "";
                        int next = -1;
                        action.put(i, "reduce");
                    }
                }
            }
            //check for shift
            for(Item item : state){
                if(item.getRhs().indexOf(".") != item.getRhs().size()-1){
                    if(action.get(i)!=null){
                        System.out.println("Reduce shift conflict.");
                        return;
                    }else {
                        action.put(i, "shift");
                    }
                }
            }

        }
    }

}