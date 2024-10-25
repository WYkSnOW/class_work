import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Your implementation of an AVL.
 *
 * @author Waiyuk Kwong
 * @version 1.0
 * @userid wkwong6
 * @GTID 903722134
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not add null data into the tree.");
        } else {
            for (T i : data) {
                if (i == null) {
                    throw new java.lang.IllegalArgumentException("Can not add null element into the tree.");
                } else {
                    add(i);
                }
            }
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not add a null into the tree.");
        } else {
            root = addHelper(data, root);
        }
    }

    /**
     *Helper of the add method
     * @param data the data to add
     * @param currentNode the AVLNode we currently at
     *
     * @return the root of new tree
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> currentNode) {
        if (currentNode == null) {
            size++;
            return new AVLNode<T>(data);
        }
        if (data.compareTo(currentNode.getData()) < 0) {
            currentNode.setLeft(addHelper(data, currentNode.getLeft()));
        } else if (data.compareTo(currentNode.getData()) > 0) {
            currentNode.setRight(addHelper(data, currentNode.getRight()));
        }
        updateHeight(currentNode);
        return reBalance(currentNode);
    }

    /**
     * Help to set new height and balance factor
     *
     * @param currentNode the AVLNode we currently at
     */
    private void updateHeight(AVLNode<T> currentNode) {
        int leftHeight = -1;
        int rightHeight = -1;
        if (currentNode.getLeft() != null) {
            leftHeight = currentNode.getLeft().getHeight();
        }
        if (currentNode.getRight() != null) {
            rightHeight = currentNode.getRight().getHeight();
        }
        currentNode.setHeight(Math.max(leftHeight, rightHeight) + 1);
        currentNode.setBalanceFactor(leftHeight - rightHeight);
    }

    /**
     * Do left rotation for the subtree
     *
     * @param a the current root of subtree
     * @return root of subtree after left rotation
     */
    private AVLNode<T> leftRotation(AVLNode<T> a) {
        AVLNode<T> b = a.getRight();
        a.setRight(b.getLeft());
        b.setLeft(a);
        updateHeight(a);
        updateHeight(b);
        return b;
    }

    /**
     * Do right rotation for the subtree
     *
     * @param c the current root of subtree
     * @return root of subtree after right rotation
     */
    private AVLNode<T> rightRotation(AVLNode<T> c) {
        AVLNode<T> b = c.getLeft();
        c.setLeft(b.getRight());
        b.setRight(c);
        updateHeight(c);
        updateHeight(b);
        return b;
    }

    /**
     * re-Blance the tree if needed
     *
     * @param currentNode the AVLNode that we currently at
     * @return root of tree that is balance
     */
    private AVLNode<T> reBalance(AVLNode<T> currentNode) {
        if (currentNode.getBalanceFactor() < -1) {
            if (currentNode.getRight().getBalanceFactor() > 0) {
                currentNode.setRight(rightRotation(currentNode.getRight()));
            }
            return leftRotation(currentNode);
        } else if (currentNode.getBalanceFactor() > 1) {
            if (currentNode.getLeft().getBalanceFactor() < 0) {
                currentNode.setLeft(leftRotation(currentNode.getLeft()));
            }
            return rightRotation(currentNode);
        }
        return currentNode;
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not remove a null.");
        }
        AVLNode<T> removed = new AVLNode<T>(data);
        root = removedHelperFindNode(root, removed);
        size--;
        return removed.getData();
    }

    /**
     * Helper for remove method
     *
     * @param currentNode the AVLNode we currently at
     * @param removed the node that being removed
     * @return tree after the node is removed
     */
    private AVLNode<T> removedHelperFindNode(AVLNode<T> currentNode, AVLNode<T> removed) {
        if (currentNode == null) {
            throw new java.util.NoSuchElementException("The data you wants to remove is not found.");
        }
        if (removed.getData().compareTo(currentNode.getData()) < 0) {
            currentNode.setLeft(removedHelperFindNode(currentNode.getLeft(), removed));
        } else if (removed.getData().compareTo(currentNode.getData()) > 0) {
            currentNode.setRight(removedHelperFindNode(currentNode.getRight(), removed));
        } else {
            removed.setData(currentNode.getData());
            return removedHelperRemoveNode(currentNode);
        }
        return currentNode;
    }

    /**
     * Helper that help removedHelperFindNode method to handle different cases: If removed node is
     * a leaf → replace it with null, If removed node has one child → replace it with its child,
     * If removed node have two children → replace it wit predecessor
     *
     * @param currentNode the AVLNode we currently at
     * @return node or null that will replace the removed node
     */
    private AVLNode<T> removedHelperRemoveNode(AVLNode<T> currentNode) {
        boolean nullLeft = currentNode.getLeft() == null;
        boolean nullRight = currentNode.getRight() == null;
        if (nullLeft && nullRight) {
            return null;
        } else if (nullLeft) {
            return currentNode.getRight();
        } else if (nullRight) {
            return currentNode.getLeft();
        } else {
            AVLNode<T> predecessor = currentNode.getLeft();
            while (predecessor.getRight() != null) {
                predecessor = predecessor.getRight();
            }
            currentNode.setData(predecessor.getData());
            currentNode.setLeft(removedHelperFindNode(currentNode.getLeft(), predecessor));
            updateHeight(currentNode);
            return reBalance(currentNode);
        }
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not search for a null");
        } else {
            return getHelper(data, root);
        }
    }

    /**
     * Helper for get method
     * @param data the data to search for in the tree
     * @param currentNode the AVLNode we currently at
     * @return the data in the tree equal to the parameter
     */
    private T getHelper(T data, AVLNode<T> currentNode) {
        if (currentNode == null) {
            throw new java.util.NoSuchElementException("Data is not in the tree");
        }
        if (data.compareTo(currentNode.getData()) > 0) {
            return getHelper(data, currentNode.getRight());
        } else if (data.compareTo(currentNode.getData()) < 0) {
            return getHelper(data, currentNode.getLeft());
        } else {
            return currentNode.getData();
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not search for a null");
        } else {
            T node = getHelper(data, root);
            return data.compareTo(node) == 0;
        }
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * This must be done recursively.
     *
     * Your list should not have duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     * traversal order
     */
    public List<T> deepestBranches() {
        List<T> deepList = new ArrayList<>();
        deepestBranchesHelper(root, deepList);
        return deepList;
    }

    /**
     * Helper function for deepestBranches method
     *
     * @param curr the AVL that currently at
     * @param deepList ArrayList that is sorting the data
     */
    private void deepestBranchesHelper(AVLNode<T> curr, List<T> deepList) {
        if (curr == null) {
            return;
        }
        deepList.add(curr.getData());
        if (curr.getLeft() != null) {
            int leftHeight = curr.getLeft().getHeight();
            if (curr.getHeight() - leftHeight <= 1) {
                deepestBranchesHelper(curr.getLeft(), deepList);
            }
        }
        if (curr.getRight() != null) {
            int rightHeight = curr.getRight().getHeight();
            if (curr.getHeight()- rightHeight <= 1) {
                deepestBranchesHelper(curr.getRight(), deepList);
            }
        }
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * This must be done recursively.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13]
     * sortedInBetween(3, 8) returns [4, 5, 6, 7]
     * sortedInBetween(8, 8) returns []
     *
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * @return a sorted list of data that is > data1 and < data2
     * @throws IllegalArgumentException if data1 or data2 are null
     * or if data1 > data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        if (data1 == null || data2 == null || data1.compareTo(data2) > 0) {
            throw new IllegalArgumentException("data1 and data2 can not be null, and data1 must be smaller than data2");
        }
        List<T> betweenList = new ArrayList<>();
        sortedInBetweenHelper(data1, data2, root, betweenList);
        return betweenList;
    }

    /**
     * Helper function for sortedInBetween method
     *
     * @param curr the AVL that currently at
     * @param betweenList ArrayList that is sorting the data
     */
    private void sortedInBetweenHelper(T data1, T data2, AVLNode<T> curr, List<T> betweenList) {
        if (curr == null) {
            return;
        }
        if (curr.getData().compareTo(data1) > 0) {
            sortedInBetweenHelper(data1, data2, curr.getLeft(), betweenList);
        }
        T currData = curr.getData();
        if (currData.compareTo(data1) > 0 && currData.compareTo(data2) < 0) {
            betweenList.add(currData);
        }
        if (curr.getData().compareTo(data2) < 0) {
            sortedInBetweenHelper(data1, data2, curr.getRight(), betweenList);
        }

    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
