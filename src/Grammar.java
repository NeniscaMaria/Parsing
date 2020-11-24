import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {
    private List<String> nonTerminals;
    private Set<String> terminals;
    private List<Production> productions;
    private String filename;

    public Grammar(String filename) {
        nonTerminals = new LinkedList<>();
        terminals = new HashSet<>();
        productions = new ArrayList<>();
        this.filename = filename;
        getGrammarFromFile();
    }

    private void getGrammarFromFile() {
        try {
            int i = 0;
            for (String line : Files.readAllLines(Paths.get(this.filename))) {
                if (i < 2){
                    String[] tokens = line.split(" ");
                    for (String token : tokens) {
                        if (i == 0)
                            terminals.add(token);
                        if (i == 1)
                            nonTerminals.add(token);
                    }
                }

                if (i >= 2) {
                    String[] tokens = line.split(" - ");
                    List<List<String>> rules = new ArrayList<>();

                    for ( String rule: tokens[1].split(" \\| "))
                        rules.add(Arrays.asList(rule.split(" ")));
                    productions.add(new Production(tokens[0], rules));
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Set<Production> getProductionsContainingNonterminal(String nonterminal) {
        Set<Production> productionsForNonterminal = new HashSet<>();
        for (Production production : productions) {
            for (List<String> rule : production.getRules())
                if (rule.indexOf(nonterminal) != -1)
                    productionsForNonterminal.add(production);
        }
        return productionsForNonterminal;
    }

    public List<Production> getProductionsForNonterminal(String nonterminal) {
        List<Production> productionsForNonterminal = new LinkedList<>();
        for (Production production : productions) {
            if (production.getStart().equals(nonterminal)) {
                productionsForNonterminal.add(production);
            }
        }
        return productionsForNonterminal;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public String toString() {
        return "G =( " + nonTerminals.toString() + ", " + terminals.toString() + ", " +
                productions.toString() + ") ";

    }
}