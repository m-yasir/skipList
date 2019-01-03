package main.lib;

import java.util.List;
import java.util.Set;

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

    public SkipList(String level, Set<T> array) {
        this();
        int index = findIndex(level);
                if (index == -1) throw new IllegalArgumentException();
        for (T el : array) {
            this.list[index].add(el);
        }
    }

    public SkipList(String level, List<T> array) {
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

    public void add(String level, Set<T> array) {
        int index = this.findIndex(level);
        for (T el : array) this.list[index].add(el);
    }

    public MyLinkedList<T> getItem(String level) {
        return this.list[this.findIndex(level)];
    }

}
