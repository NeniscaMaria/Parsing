import javafx.util.Pair;

import java.util.*;

public class ParseOutput {
    private Parser parser;
    private Grammar grammar;
    private Stack<Pair<String, Set<Item>>> workingStack; //alpha
    private Stack<String> inputStack; //beta
    private Stack<Item> outputStack; //pi

    public ParseOutput(Parser parser, Grammar grammar) {
        this.parser = parser;
        this.grammar = grammar;
        workingStack = new Stack<>();
        inputStack = new Stack<>();
        outputStack = new Stack<>();
    }

    private ParseTree getOutputTree() {
        ParseTree tree = new ParseTree();
        int lastPosition;
        int fatherPosition = -1;
        while (!outputStack.empty()) {
            Item production = outputStack.pop();
            System.out.println(production);
            String lhs = production.getLhs();
            List<String> rhs = production.getRhs();
            if (tree.getSize() != 0) {
                lastPosition = tree.addChild(rhs.get(0), fatherPosition);
            } else {
                lastPosition = tree.addChild(lhs, -1);
                lastPosition = tree.addChild(rhs.get(0), lastPosition);
            }

            for (int i = 1; i < rhs.size(); i++) {
                lastPosition = tree.addSibling(rhs.get(i), lastPosition);
                //get the next father position by seeing if this term is the first nonterminal in the rhs
                if (grammar.getNonTerminals().contains(rhs.get(i))) {
                    int frequency = Collections.frequency(rhs, rhs.get(i));
                    if (frequency == 1)
                        fatherPosition = lastPosition;
                    else if (rhs.indexOf(rhs.get(i)) == i)
                        fatherPosition = lastPosition;

                }
            }

        }
        return tree;
    }

    public ParseTree parse(String sequence) {
        System.out.println("=====================================================");
        System.out.println("Parsing " + sequence);
        //initialize the stacks
        Set<Item> startState = parser.getStartState();
        workingStack.push(new Pair<>("$", startState));
        List<String> inputs = Arrays.asList(sequence.split(" "));
        Collections.reverse(inputs);
        inputStack.push("$");
        inputs.forEach(input -> inputStack.push(input));
        String previousInput = "";
        Set<Item> currentState = startState;
        boolean end = false;
        while (!end) {
            String action = parser.getActionOfState(currentState);
            System.out.println(currentState + " " + action);

            if (action.equals("shift")) {
                String a = inputStack.pop();
                if(a.contains("$")){
                    System.out.println("Error: could not resolve "+previousInput+" at position "+(sequence.split(" ").length-inputStack.size()));
                    return null;
                }
                currentState = parser.getGoTo(currentState, a);
                if(currentState == null){
                    System.out.println("Error: could not resolve "+a+" at position "+(sequence.split(" ").length-inputStack.size()));
                    return null;
                }
                workingStack.push(new Pair<>(a, currentState));
                previousInput = a;
                System.out.println(a + " " + currentState);
                System.out.println("=====================================================");
            } else {
                if (action.contains("reduce")) {
                    String[] split = action.split(",");
                    int posInProductions = Integer.parseInt(split[1]);
                    int posInRules = Integer.parseInt(split[2]);
                    Item production = grammar.getProduction(posInProductions, posInRules);
                    System.out.println(production);
                    for (int i = 0; i < production.getRhs().size(); i++)
                        workingStack.pop();
                    currentState = parser.getGoTo(workingStack.peek().getValue(), production.getLhs());
                    workingStack.push(new Pair<>(production.getLhs(), currentState));
                    outputStack.push(production);
                    System.out.println("=====================================================");
                } else {
                    if (action.equals("accept")) {
                        if(inputStack.peek().equals("$")) {
                            System.out.println("Success!");
                            end = true;
                        }else{
                            System.out.println("Error: could not resolve "+inputStack.pop()+" at position "+(sequence.split(" ").length-inputStack.size()));
                            return null;
                        }
                    } else {
                        if (action.equals("Error")) {
                            System.out.println("Error");
                            return null;
                        }
                    }
                }
            }
        }
        return getOutputTree();
    }
}
