public class Node {
    private String val;
    private Node child;
    private Node next;

    public Node(String P) {
        val = P;
        child = null;
        next = null;
    }

    public String getVal() {
        return val;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "val='" + val + '\'' +
                ", child=" + child +
                ", next=" + next +
                '}';
    }
}