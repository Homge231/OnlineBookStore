package Coursework;

/*=============================
   LinkedQueueADT (Queue ADT)
===============================*/
public interface AbstractLinkedQueue<E> {
    void offer(E element);
    E poll();
    E peek();
    int size();
    boolean isEmpty();
}