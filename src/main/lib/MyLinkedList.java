package main.lib;

public class MyLinkedList<T>
{
    //Class variables for the Linked List
    private Node<T> head;
    public int numNodes = 0;
    private MyLinkedList[] list;
    public int listIndex;

    public MyLinkedList(MyLinkedList[] list, int listIndex)
    {
        this.list = list;
        listIndex = listIndex;
        head = new Node<T>(null, ++numNodes, this.list, listIndex);
    }

    public void add(T data)
    {
        Node<T> temp = head;
        if (head == null || head.data == null) {
            this.head = new Node<T>(data, ++numNodes, list, listIndex);
            return;
        }
        while(temp.next != null)
        {
            temp = temp.next;
        }

        temp.next = new Node<T>(data, numNodes, list, listIndex);
        numNodes++;
    }

    public int find(Node<T> n)
    {
        Node<T> t = head;
        int index = 0;
        while(t != n)
        {
            index++;
            t = t.next;
        }
        if (t == null) return -1;
        return index;
    }

    public Node<T> find(int index) {
        if (index < 0 || index > getSize()) throw new IndexOutOfBoundsException();
        Node<T> temp=head;
        for(int i=0; i<index && temp.next != null; i++) {
            temp = temp.next;
        }
        return temp;
    }

    public void printList()
    {
        Node<T> temp = head;
        while(temp != null)
        {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    public int getSize()
    {
        return numNodes;
    }

    public String toString() {
        String toDisplay = "";
        Node<T> temp = head;
        for (int i = 0; i < getSize(); i++) {
            System.out.println(head);
            temp = find(i);
        }
        return toDisplay;
    }

}