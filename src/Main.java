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

    public static void main(String[] args) {
        treeCheck();
    }
}
