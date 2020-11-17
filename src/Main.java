import java.util.Scanner;

public class Main {

    private static void treeCheck(){
        Tree tree = new Tree(new Node("A"));
        tree.addChild("A","B");
        tree.addChild("A","E");
        tree.addChild("B","C");
        tree.addChild("B","G");
        tree.addChild("C","D");
        tree.addChild("C","F");
        System.out.println(tree);
        System.out.println(tree.findParent(tree.getRoot(),"E").getVal());
        System.out.println(tree.getKthChild(tree.getRoot(),"D",1));
        System.out.println(tree.getLeaves(tree.getRoot()));
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
        Grammar grammar = new Grammar();
        boolean finsihed = false;
        Scanner console = new Scanner(System.in);
        while(!finsihed){
            showMenu();
            System.out.println(">>");
            int choice = console.nextInt();
            switch (choice){
                case 0:
                    finsihed=true;
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
