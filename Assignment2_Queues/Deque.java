/*----------------------------------------------------------------
 *  Author:        Kevin Zhou
 *  Written:       12/18/2016
 *  Last updated:  12/18/2016
 *
 *  Compilation:   javac Deque.java
 *  Execution:     java 
 *  
 *  
 *
 *
 *----------------------------------------------------------------*/



import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private int n; // nodes in linkedlist
    private Node current;
    
    // container for object 
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    
    // construct an empty deque
    public Deque() {
        current = null;
        n = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return current == null;
    }
    
    // return the number of items on the deque
    public int size() {
        return n;
    }  
    
    // add the item to the front
    public void addFirst(Item item) {
        if(item==null) throw new java.lang.NullPointerException();
        Node oldfirst = current;
        current = new Node();
        current.item = item;
        current.next = oldfirst;
        if (current.next != null) {
          oldfirst.previous = current;
        }
        n++;
    }  
    
    // add the item to the end
    public void addLast(Item item) {
        if(item==null) throw new java.lang.NullPointerException();
        if (current == null) {
            current = new Node();
            current.item = item;
        } else {
            while (current.next != null) current = current.next;
            Node oldfirst = current;
            current = new Node();
            current.item = item;
            current.previous = oldfirst;
            oldfirst.next = current;
            while (current.previous != null) current = current.previous;
        }    
        n++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (current == null) throw new NoSuchElementException();
        Item temp = current.item;
        if (current.next != null) {
            current = current.next;
            current.previous = null;
        }
        else current = null;
        n--;
        return temp;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (current == null) throw new NoSuchElementException();
        else if (current.next == null){ 
            Item temp = current.item;
            current = null;
            n--;
            return temp;
        }
        else {
            while (current.next != null) current = current.next;
            Item temp = current.item;
            current = current.previous;   // the last element
            current.next = null;
            while (current.previous != null) current = current.previous;
            n--;
            return temp;
        }
        
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
      return new DequeIterator();
    } 
    
    private class DequeIterator implements Iterator<Item> {
        private Node now = current;
        public boolean hasNext()  { return now != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }
    
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = now.item;
            now = now.next; 
            return item;
        }
    }
    
}