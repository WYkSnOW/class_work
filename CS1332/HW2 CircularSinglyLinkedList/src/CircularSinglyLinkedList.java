/**
 * Your implementation of a CircularSinglyLinkedList without a tail pointer.
 *
 * @author Waiyuk Kwong
 * @version 1.0
 * @userid YOUR USER ID HERE wkwong6
 * @GTID YOUR GT ID HERE 903722134
 *
 * Collaborators: NA
 *
 * Resources: NA
 */
public class CircularSinglyLinkedList<T> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private CircularSinglyLinkedListNode<T> head;
    private int size;

    /*
     * Do not add a constructor.
     */

    /**
     * Adds the data to the specified index.
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new data
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Cannot insert data to a index that < 0 or > size.");
        } else if (data == null) {
            throw new IllegalArgumentException("Cannot insert null into Circularly Linked Lists.");
        } else if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else {
            CircularSinglyLinkedListNode<T> newNode = new CircularSinglyLinkedListNode<T>(data);
            CircularSinglyLinkedListNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            newNode.setNext(curr.getNext());
            curr.setNext(newNode);
            size++;
        }
    }

    /**
     * Adds the data to the front of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null into Circularly Linked Lists.");
        } else {
            CircularSinglyLinkedListNode<T> newNode = new CircularSinglyLinkedListNode<T>(null);
            if (size == 0) {
                head = newNode;
                head.setData(data);
                head.setNext(head);
            } else {
                newNode.setData(head.getData());
                newNode.setNext(head.getNext());
                head.setNext(newNode);
                head.setData(data);
            }
            size++;
        }
    }

    /**
     * Adds the data to the back of the list.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null into Circularly Linked Lists.");
        } else {
            CircularSinglyLinkedListNode<T> newNode = new CircularSinglyLinkedListNode<T>(null);
            if (size == 0) {
                head = newNode;
                head.setData(data);
                head.setNext(head);
            } else {
                newNode.setData(head.getData());
                newNode.setNext(head.getNext());
                head.setNext(newNode);
                head.setData(data);
                head = (head.getNext());
            }
            size++;
        }
    }

    /**
     * Removes and returns the data at the specified index.
     *
     * Must be O(1) for index 0 and O(n) for all other cases.
     *
     * @param index the index of the data to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Cannot insert data to a index that < 0 or >= size.");
        } else {
            if (index == 0) {
                removeFromFront();
            }
            CircularSinglyLinkedListNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            T removed = curr.getNext().getData();
            curr.setNext(curr.getNext().getNext());
            size--;
            return removed;
        }
    }

    /**
     * Removes and returns the first data of the list.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Can not remove element from a empty Circularly Linked Lists.");
        } else {
            T removed = head.getData();
            if (size == 1) {
                head = null;
            } else {
                head.setData(head.getNext().getData());
                head.setNext(head.getNext().getNext());
            }
            size--;
            return removed;
        }

    }

    /**
     * Removes and returns the last data of the list.
     *
     * Must be O(n).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Can not remove element from a empty Circularly Linked Lists.");
        } else {
            if (size == 1) {
                T removed = head.getData();
                head = null;
                size--;
                return removed;
            } else {
                CircularSinglyLinkedListNode<T> curr = head;
                for (int i = 0; i < size - 2; i++) {
                    curr = curr.getNext();
                }
                T removed = curr.getNext().getData();
                curr.setNext(head);
                size--;
                return removed;
            }
        }
    }

    /**
     * Returns the data at the specified index.
     *
     * Should be O(1) for index 0 and O(n) for all other cases.
     *
     * @param index the index of the data to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Cannot insert data to a index that < 0 or >= size.");
        } else {
            if (size == 1) {
                return head.getData();
            }
            CircularSinglyLinkedListNode<T> curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.getNext();
            }
            return curr.getData();
        }
    }

    /**
     * Returns whether or not the list is empty.
     *
     * Must be O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * Must be O(n).
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null into Circularly Linked Lists.");
        } else {
            CircularSinglyLinkedListNode<T> curr = head;
            CircularSinglyLinkedListNode<T> nodeBeforeRemoved = null;
            for (int i = 0; i < size; i++) {
                if (curr.getNext().getData().equals(data)) {
                    nodeBeforeRemoved = curr;
                }
                curr = curr.getNext();
            }

            if (nodeBeforeRemoved.getNext() == null) {
                throw new java.util.NoSuchElementException("Input data is not found in the Circularly Linked Lists");
            } else if (nodeBeforeRemoved.getNext() == head) {
                return removeFromFront();
            } else {
                T temp = nodeBeforeRemoved.getNext().getData();
                nodeBeforeRemoved.setNext(nodeBeforeRemoved.getNext().getNext());
                size--;
                return temp;
            }
        }
    }

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return the array of length size holding all of the data (not the
     * nodes) in the list in the same order
     */
    public T[] toArray() {
        T[] arr = (T[]) (new Object[size]);
        if (size != 0) {
            CircularSinglyLinkedListNode<T> curr = head;
            for (int i = 0; i < size; i++) {
                arr[i] = curr.getData();
                curr = curr.getNext();
            }
        }
        return arr;
    }

    /**
     * Returns the head node of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the list
     */
    public CircularSinglyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the size of the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }
}
