import javafx.util.Pair;
import java.util.*;

public class ParseOutput {
    private Parser parser;
    private Grammar grammar;
    private Stack<Pair<String,Set<Item>>> workingStack; //alpha
    private Stack<String> inputStack; //beta
    private Stack<Item> outputStack; //pi

    public ParseOutput(Parser parser, Grammar grammar) {
        this.parser = parser;
        this.grammar = grammar;
        workingStack = new Stack<>();
        inputStack = new Stack<>();
        outputStack = new Stack<>();
    }

    private ParseTree getOutputTree(){
        ParseTree tree = new ParseTree();
        return tree;
    }
    public ParseTree parse(String sequence){
        System.out.println("=====================================================");
        System.out.println("Parsing "+sequence);
        //initialize the stacks
        Set<Item> startState = parser.getStartState();
        workingStack.push(new Pair<>("$",startState));
        List<String> inputs = Arrays.asList(sequence.split(" "));
        Collections.reverse(inputs);
        inputStack.push("$");
        inputs.forEach(input->inputStack.push(input));

        Set<Item> currentState = startState;
        boolean end = false;
        while(!end) {
            String action = parser.getActionOfState(currentState);
            System.out.println(currentState +" "+ action);

            if(action.equals("shift")){
                String a = inputStack.pop();
                currentState = parser.getGoTo(currentState,a);
                workingStack.push(new Pair<>(a,currentState));
                System.out.println(a+" "+currentState);
                System.out.println("=====================================================");
            }else{
                if(action.contains("reduce")){
                    String[] split = action.split(",");
                    int posInProductions = Integer.parseInt(split[1]);
                    int posInRules = Integer.parseInt(split[2]);
                    Item production = grammar.getProduction(posInProductions, posInRules);
                    System.out.println(production);
                    for(int i=0;i<production.getRhs().size();i++)
                        workingStack.pop();
                    currentState = parser.getGoTo(workingStack.peek().getValue(),production.getLhs());
                    workingStack.push(new Pair<>(production.getLhs(), currentState));
                    outputStack.push(production);
                    System.out.println("=====================================================");
                }else{
                    if(action.equals("accept")){
                        System.out.println("Success!");
                        end = true;
                    }else{
                        if(action.equals("Error")){
                            System.out.println("Error");
                            end = true;
                        }
                    }
                }
            }
        }
        return getOutputTree();
    }
}
