import java.util.LinkedList;

// package main.test;

// class MyLinkedList<T> {
//     LinkedList ll;
//     Iterator itr;
//     MyLinkedList skipList;
//     int parentIndex;
//     MyLinkedList(MyLinkedList skipList, int parentIndex){
//         super();
//         ll = new LinkedList<T>();
//         itr = ll.iterator();
//         skipList = skipList;
//         parentIndex = parentIndex;
//     }
//     Iterator right(){
//         return this.itr.next();
//     }
//     Iterator left(){
//         return this.itr.prev();
//     }
//     Iterator top(){
//         int index = ll.indexOf(this.head);
//         return this.skipList[parentIndex-1].itr.next();
//         // return this.skipList[parentIndex-1].get(index);
//     }
//     Iterator bottom(){
//         int index = ll.indexOf(this.head);
//         return this.skipList[parentIndex+1].getItem(index);
//     }
    
// }

class Node<T>
{
    //Declare class variables
    // private Node top;
    // private Node bottom;
    public Node next;
    public Node prev;
    T data;
    int ind;
    int listIndex;
    MyLinkedList[] ll;
    
    public Node(T dat, int index, MyLinkedList[] ll, int listIndex)
    {
        data = dat;
        ind = index;
        listIndex = listIndex;
        ll = ll;
    }
    
    public T getData()
    {
        return data;
    }

    Node top(){
        return ll[listIndex-1].find(ind);
    }
    Node bottom(){
        return ll[listIndex+1].find(ind);
    }
    Node left(){
        return this.prev;
    }
    Node right(){
        return this.next;
    }
}

class MyLinkedList<T>
{
	//Class variables for the Linked List
	Node head;
    int numNodes = 0;
    MyLinkedList[] list;
    int listIndex;
	
	MyLinkedList(MyLinkedList[] list, int listIndex)
	{
        list = list;
        listIndex = listIndex;
		head = new Node(null, ++numNodes, this.list, listIndex);
	}
	
	// public void addAtHead(Object dat)
	// {
	// 	Node temp = head;
	// 	head = new Node(dat, numNodes, list);
	// 	head.next = temp;
	// 	numNodes++;
	// }
	
	public void add(Object dat)
	{
		Node temp = head;
		while(temp.next != null)
		{
			temp = temp.next;
		}
		
		temp.next = new Node(dat, numNodes, list, listIndex);
		numNodes++;
	}
	
	// public void addAtIndex(int index, Object dat)
	// {
	// 	Node temp = head;
	// 	Node holder;
	// 	for(int i=0; i < index-1 && temp.next != null; i++)
	// 	{
	// 		temp = temp.next;
	// 	}
	// 	holder = temp.next;
	// 	temp.next = new Node(dat);
	// 	temp.next.next = holder;
	// 	numNodes++;
	// }
	
	// public void deleteAtIndex(int index)
	// {
	// 	Node temp = head;
	// 	for(int i=0; i< index - 1 && temp.next != null; i++)
	// 	{
	// 		temp = temp.next;
	// 	}
	// 	temp.next = temp.next.next;
	// 	numNodes--;
	// }
	
	public int find(Node n)
	{
		Node t = head;
		int index = 0;
		while(t != n)
		{
			index++;
			t = t.next;
		}
		return index;
	}
	
	public Node find(int index)
	{
		Node temp=head;
		for(int i=0; i<index; i++)
		{
			temp = temp.next;
		}
		return temp;
	}
		
	public void printList()
	{
		Node temp = head;
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
	
	
}

public class SkipList<T> {
    // 0: top
    // 1: bottom
    // 2: left
    // 3: right
    private MyLinkedList[] list = new MyLinkedList[4];

    SkipList(String title){
        this.list[0] = new MyLinkedList<T>(list, 0);
        this.list[1] = new MyLinkedList<T>(list, 1);
        this.list[2] = new MyLinkedList<T>(list, 2);
        this.list[3] = new MyLinkedList<T>(list, 3);
    }


    int findIndex(String direction){
        int index = -1;
        switch(direction){
            case "beginners":
            index = 0;
            break;
            case "intermediate":
            index = 1;
            break;
            case "advance":
            index = 2;
            break;
            case "expert":
            index = 3;
            break;
            default:
            throw new Error("Invalid direction");

        }
        return index;
    }
    void add(String direction, T data){
        this.list[this.findIndex(direction)].add(data);
    }
    MyLinkedList getItem(String direction) {
        return this.list[this.findIndex(direction)];
    }

}
