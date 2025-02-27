package bearmaps.utils.pq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    private int size;

    private HashMap otherContents;
    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        // contents.add(null);
        otherContents = new HashMap<E, Integer>();
    }


    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
        otherContents.put(element, index);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        int neededIndex = 2 * index;
        if (getElement(neededIndex) == null) {
            return contents.size() + 1;
        }
        return neededIndex;

    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        int neededIndex = (2 * index) + 1;
        if (getElement(neededIndex) == null) {
            return contents.size() + 1;
        }
        return neededIndex;
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        int neededIndex = index / 2;
        return neededIndex;
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        if (element1 == null) {
            return index2;
        }
        if (element2 == null) {
            return index1;
        }
        if (element1.compareTo(element2) < 0) {
            return index1;
        }
        return index2;
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        return getElement(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        E element = getElement(index);
        if (index == 1) {
            return;
        }
        E parent = getElement(getParentOf(index));
        if (element.compareTo(parent) >= 0 || element == findMin()) {
            return;
        } else {
            swap(index, getParentOf(index));
            bubbleUp(getParentOf(index));

        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        E current = getElement(index);
        int minIndex = min(getLeftOf(index), getRightOf(index));
        E min = getElement(minIndex);

        if ((getElement(getRightOf(index)) == null && getElement(getLeftOf(index)) == null)) {
            return;
        } else if (current.compareTo(min) <= 0) {
            return;
        } else {
            swap(index, minIndex);
            bubbleDown(minIndex);
        }
    }


    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return size;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if (contains(element)) {
            throw new IllegalArgumentException();
        }
        size++;
        int neededIndex = size;
        setElement(neededIndex, element);
        bubbleUp(neededIndex);

    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        E saved = findMin();
        swap(1, size);
        contents.remove(size);
        otherContents.remove(saved);
        bubbleDown(1);
        size--;
        return saved;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        int needed = 0;
        if (!contains(element)) {
            throw new NoSuchElementException();
        }
        needed = (int) otherContents.get(element);
        setElement(needed, element);
        bubbleUp(needed);
        bubbleDown(needed);

    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        return otherContents.containsKey(element);

    }
}
