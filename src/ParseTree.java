import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private List<ParseNode> table;

    public ParseTree() {
        table = new ArrayList<>();
    }

    public int getSize() {
        return table.size();
    }

    public int addChild(String child, int parent) {
        //returns the position on which the child was put
        ParseNode nodeToAdd = new ParseNode(child, parent, -1);
        table.add(nodeToAdd);
        return table.size() - 1;
    }

    public int addSibling(String sibling, int position) {
        //returns the position on which the sibling was put
        ParseNode node = table.get(position);
        if (node.getSibling() != -1) {
            node = table.get(node.getSibling());
        }
        ParseNode nodeToAdd = new ParseNode(sibling, node.getFather(), position);
        table.add(nodeToAdd);
        return table.size() - 1;
    }

    public boolean hasChildren(ParseNode node) {
        int position = table.indexOf(node);
        boolean foundChildren = false;
        for (int i = 0; i < table.size() && !foundChildren; i++) {
            if (table.get(i).getFather() == position)
                foundChildren = true;
        }
        return foundChildren;
    }

    public ParseNode getRoot() {
        return table.get(0);
    }

    public List<ParseNode> getChildren(ParseNode node) {
        int position = table.indexOf(node);
        List<ParseNode> children = new ArrayList<>();
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i).getFather() == position)
                children.add(table.get(i));
        }
        return children;
    }

    public List<String> getLeaves(ParseNode node) {
        List<String> leaves = new ArrayList<>();
        if (!hasChildren(node))
            leaves.add(node.getValue());
        else {
            List<ParseNode> children = getChildren(node);
            for (ParseNode n : children)
                leaves.addAll(getLeaves(n));
        }
        return leaves;
    }

    public void writeToFile(String filename){

    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("==========ParseTree=========\n");
        s.append("pos | val | father | sibling\n");
        for (int i = 0; i < table.size(); i++) {
            s.append(i).append(table.get(i).toString()).append("\n");
            s.append("----------------------------\n");
        }

        return s.toString();
    }
}
