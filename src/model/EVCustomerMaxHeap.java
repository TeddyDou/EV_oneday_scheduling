package model;

import java.util.HashMap;

public class EVCustomerMaxHeap {

    private EVCustomer[] Heap;
    private int size;
    private int maxsize;

    private static final int FRONT = 1;

    public EVCustomerMaxHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new EVCustomer[this.maxsize + 1];
        Heap[0] = new EVCustomer();
    }

    private int parent(int pos) {
        return pos / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    private boolean isLeaf(int pos) {
        if ((pos >= (size / 2) && pos <= size) || size == 0) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos) {
        EVCustomer tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

//    private void maxHeapify(int pos) {
//        if (!isLeaf(pos)) {
//            if (Heap[pos].getBarWindowRatio() < Heap[leftChild(pos)].getBarWindowRatio() ||
//                    Heap[pos].getBarWindowRatio() < Heap[rightChild(pos)].getBarWindowRatio()) {
//                if (Heap[leftChild(pos)].getBarWindowRatio() > Heap[rightChild(pos)].getBarWindowRatio()) {
//                    swap(pos, leftChild(pos));
//                    maxHeapify(leftChild(pos));
//                } else {
//                    swap(pos, rightChild(pos));
//                    maxHeapify(rightChild(pos));
//                }
//            }
//        }
//    }
//
//    public void insert(EVCustomer element) {
//        Heap[++size] = element;
//        int current = size;
//
//        while (Heap[current].getBarWindowRatio() > Heap[parent(current)].getBarWindowRatio()) {
//            swap(current, parent(current));
//            current = parent(current);
//        }
//    }

    private void maxHeapify(int pos) {
        if (!isLeaf(pos)) {
            if (Heap[pos].getScore() < Heap[leftChild(pos)].getScore() ||
                    Heap[pos].getScore() < Heap[rightChild(pos)].getScore()) {
                if (Heap[leftChild(pos)].getScore() > Heap[rightChild(pos)].getScore()) {
                    swap(pos, leftChild(pos));
                    maxHeapify(leftChild(pos));
                } else {
                    swap(pos, rightChild(pos));
                    maxHeapify(rightChild(pos));
                }
            }
        }
    }

    public void insert(EVCustomer element) {
        Heap[++size] = element;
        int current = size;

        while (Heap[current].getScore() > Heap[parent(current)].getScore()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public void print() {
        for (int i = 1; i <= size / 2; i++) {
            System.out.print(
                    " PARENT : " + (Heap[i] == null ? null : Heap[i].getEvID()) + " LEFT CHILD : " + (Heap[2 * i] ==
            null ? null : Heap[2 * i].getEvID()) + " " +
                            "RIGHT CHILD :" +
                            (Heap[2 * i + 1] == null ? null : Heap[2 * i + 1].getEvID()));
            System.out.println();
        }
    }

    // initilize max heap
    public void maxHeap() {
        for (int pos = (size / 2); pos >= 1; pos--) {
            maxHeapify(pos);
        }
    }

    public EVCustomer remove() {
        EVCustomer popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        maxHeapify(FRONT);
        return popped;
    }

    public boolean hasNext(){
        if (size ==0 )
            return false;
        return true;
    }

    public void demo() {
        System.out.println("The Max Heap is ");

        this.maxHeap();

        this.print();
        while (this.hasNext()) {
            System.out.println(this.hasNext());
            System.out.println("The max val is " + this.remove());
        }
    }
}
