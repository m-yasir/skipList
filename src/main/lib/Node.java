package main.lib;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node<T> {
    public Node next;
    public Node prev;
    T data;
    int ind;
    int listIndex;
    //Declare class variables
    private Node top;
    private Node bottom;
    private MyLinkedList[] ll;

    public Node(T dat, int index, MyLinkedList[] ll, int listIndex) {
        data = dat;
        ind = index;
        listIndex = listIndex;
        ll = ll;
    }

    public T getData() {
        return data;
    }

    Node top() {
        return ll[listIndex - 1].find(ind);
    }

    Node bottom() {
        return ll[listIndex + 1].find(ind);
    }

    Node left() {
        return this.prev;
    }

    Node right() {
        return this.next;
    }

    public String toString() {
        return this.next + " -> ";
    }
}
