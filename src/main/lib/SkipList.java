 package main.lib;

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

import java.util.LinkedHashSet;
import java.util.List;

public class SkipList<T> {
    // 0: top
    // 1: bottom
    // 2: left
    // 3: right
    private MyLinkedList[] list;

    public SkipList(){
        this.list = new MyLinkedList[4];
        for (int i = 0; i < 4; i++) {
            this.list[i] = new MyLinkedList<T>(list, i);
        }
    }

    public SkipList(String level, LinkedHashSet<T> array) {
        this();
        int index = findIndex(level);
                if (index == -1) throw new IllegalArgumentException();
        for (T el : array) {
            this.list[index].add(el);
        }
    }


    public int findIndex(String level){
        int index = -1;
        switch(level){
            case "beginner":
            index = 0;
            break;
            case "intermediate":
            index = 1;
            break;
            case "advanced":
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
    public void add(String level, T data){
        this.list[this.findIndex(level)].add(data);
    }

    public MyLinkedList getItem(String level) {
        return this.list[this.findIndex(level)];
    }

}
