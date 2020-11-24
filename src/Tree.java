public class Tree {
    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Node addChild(String parent, String child) {
        //returns the added node
        Node node = findNode(root, parent);
        Node toAdd = new Node(child);
        if (node != null) {
            if (node.getChild() == null) {
                node.setChild(toAdd);
            } else {
                Node next = findNextFree(node.getChild());
                next.setNext(toAdd);
            }
            return toAdd;
        }
        return null;
    }

    public Node addChildToNode(Node parent, String child) {
        //returns the added node
        Node nodeToAdd = new Node(child);
        if (parent != null) {
            if (parent.getChild() == null) {
                parent.setChild(nodeToAdd);
            } else {
                Node next = findNextFree(parent.getChild());
                next.setNext(nodeToAdd);
            }
            return nodeToAdd;
        }
        return null;
    }

    private Node findNextFree(Node node) {
        if (node.getNext() == null)
            return node;
        else return findNextFree(node.getNext());
    }

    public Node findNode(Node current, String value) {
        if (current == null) return null;
        if (current.getVal().equals(value))
            return current;
        else {
            Node candidate = findNode(current.getChild(), value);
            if (candidate == null)
                return findNode(current.getNext(), value);
            else
                return candidate;
        }
    }

    public Node findNodeWithNode(Node current, Node node) {
        if (current == null) return null;
        if (current == node)
            return current;
        else {
            Node candidate = findNodeWithNode(current.getChild(), node);
            if (candidate == null)
                return findNodeWithNode(current.getNext(), node);
            else
                return candidate;
        }
    }

    public Node findParent(Node current, String node) {
        if (current == null)
            return null;
        if (current.getChild().getVal().equals(node))
            return current;
        if (searchSiblings(current.getChild(), node))
            return current;
        else {
            return findParent(current.getChild(), node);
        }
    }

    public String getLeaves(Node current) {
        if (current == null)
            return "";
        if (current.getChild() == null && current.getNext() != null)
            return current.getVal() + getLeaves(current.getNext());
        else if (current.getChild() == null)
            return current.getVal();
        return getLeaves(current.getChild()) + getLeaves(current.getNext());
    }

    private boolean searchSiblings(Node current, String value) {
        if (current == null) return false;
        if (current.getVal().equals(value))
            return true;
        else return searchSiblings(current.getNext(), value);
    }

    public Node getRoot() {
        return root;
    }

    public Node getKthChild(Node root, String P, int childNo) {
        if (root == null)
            return null;
        if (root.getVal().equals(P)) {
            Node t = root.getChild();
            int i = 1;
            while (t != null && i < childNo) {
                t = t.getNext();
                i++;
            }

            return (t);
        }
        getKthChild(root.getChild(), P, childNo);
        getKthChild(root.getNext(), P, childNo);
        return null;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                '}';
    }
}
