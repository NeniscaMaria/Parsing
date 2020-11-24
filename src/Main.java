import org.xml.sax.helpers.ParserAdapter;

import java.util.Scanner;

public class Main {

    private static void treeCheck(){
        ParseTree parseTree = new ParseTree();
        int posRoot = parseTree.addChild("S",-1); //root
        int pos1 = parseTree.addChild("a",posRoot);
        int pos2 = parseTree.addSibling("S",pos1);
        int pos3 = parseTree.addSibling("b",pos2);
        int pos4 = parseTree.addSibling("S",pos3);
        int pos5 = parseTree.addChild("a",pos2);
        int pos6 = parseTree.addSibling("S",pos5);
        int pos7 = parseTree.addChild("c",pos4);
        int pos8 = parseTree.addChild("c",pos6);
        System.out.println(parseTree);
        System.out.println(parseTree.getLeaves(parseTree.getRoot()));
}

    private static void showMenu(){
        System.out.println("Choose one of the following:");
        System.out.println("1.Show set of nonterminals");
        System.out.println("2.Show set of terminals");
        System.out.println("3.Show set of productions");
        System.out.println("4.Show production for a nonterminal");
        System.out.println("0.Exit");
    }
    public static void main(String[] args) {
        treeCheck();
        Grammar grammar = new Grammar("g3.txt");
        Parser parser = new Parser(grammar);
        ParserOutput parserOutput = new ParserOutput(parser);
        boolean finished = false;
        Scanner console = new Scanner(System.in);
        while(!finished){
            showMenu();
            System.out.println(">>");
            int choice = console.nextInt();
            switch (choice){
                case 0:
                    finished=true;
                    break;
                case 1:
                    System.out.println(grammar.getNonTerminals());
                    break;
                case 2:
                    System.out.println(grammar.getTerminals());
                    break;
                case 3:
                    System.out.println(grammar.getProductions());
                    break;
                case 4:
                    System.out.println("Nonterminal: ");
                    console.nextLine();
                    String nonterminal = console.nextLine();
                    System.out.println(grammar.getProductionsContainingNonterminal(nonterminal));
                    break;
                default:
                    System.out.println("Wrong command");
            }
        }
    }
}
