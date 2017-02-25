/*----------------------------------------------------------------
 *  Author:        Kevin Zhou
 *  Written:       12/18/2016
 *  Last updated:  12/18/2016
 *
 *  Compilation:   javac RandomizedQueue.java
 *  Execution:     java 
 *  
 *  
 *
 *
 *----------------------------------------------------------------*/


import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         //  items
    private int n;            // number of elements on stack
    
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];    
        n = 0;
    }   
    
    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return n;
    }
    
    // enlarge the array by twice of its size
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (n == a.length) resize(2*n);
        a[n++] = item;
    }      
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int pos =  StdRandom.uniform(n);
        Item temp = a[pos];
        a[pos] = a[n-1];
        a[n-1] = null;
        n--;
        if ( n > 0 && n == a.length/4) resize(a.length/2);
        return temp;
    }                   
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int pos =  StdRandom.uniform(n);
        Item temp = a[pos];
        return temp;
    } 
     // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;

        public RandomizedQueueIterator() {
            i = n-1;
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i--];
        }
    }
     
}