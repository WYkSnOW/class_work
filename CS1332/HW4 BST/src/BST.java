import java.util.*;

/**
 * Your implementation of a BST.
 *
 * @author Waiyuk Kwong
 * @version 1.0
 * @userid wkwong6
 * @GTID 903722134
 *
 * Collaborators: NA
 *
 * Resources: NA
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not add null into BST.");
        } else {
            size = 0;
            for (T current : data) {
                add(current);
            }
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Can not add null into BST.");
        }
        if (root == null) {
            root = new BSTNode<>(data);
            size++;
        } else {
            addHelper(data, root);
        }
    }

    /**
     * Helper method of add method
     *
     * @param data the data to add
     * @param currentNode the node we currently at
     */
    private void addHelper(T data, BSTNode<T> currentNode) {
        if (data.compareTo(currentNode.getData()) < 0) {
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(new BSTNode<>(data));
                size++;
            } else {
                addHelper(data, currentNode.getLeft());
            }
        } else if (data.compareTo(currentNode.getData()) > 0) {
            if (currentNode.getRight() == null) {
                currentNode.setRight(new BSTNode<>(data));
                size++;
            } else {
                addHelper(data, currentNode.getRight());
            }
        }
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not remove null from BST.");
        } else {
            BSTNode<T> removeNode = new BSTNode<>(null);
            root = removeHelper(data, root, removeNode);
            return removeNode.getData();
        }
    }

    /**
     * Helper method of remove method

     *
     * @param data the data to remove
     * @param currentNode the node we currently at
     * @param removeNode the node that we're going to remove
     * @return the parent node of removeNode
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private BSTNode<T> removeHelper(T data, BSTNode<T> currentNode, BSTNode<T> removeNode) {
        if (currentNode == null) {
            throw new java.util.NoSuchElementException("Can not remove a element that is not in the BST.");
        } else {
            if (data.compareTo(currentNode.getData()) < 0) {
                currentNode.setLeft(removeHelper(data, currentNode.getLeft(), removeNode));
            } else if (data.compareTo(currentNode.getData()) > 0) {
                currentNode.setRight(removeHelper(data, currentNode.getRight(), removeNode));
            } else {
                removeNode.setData(currentNode.getData());
                size--;
                if (currentNode.getLeft() == null) {
                    return currentNode.getRight();
                } else if (currentNode.getRight() == null) {
                    return currentNode.getLeft();
                } else {
                    BSTNode<T> sortNode = new BSTNode<>(null);
                    currentNode.setRight(successor(currentNode.getRight(), sortNode));
                    currentNode.setData(sortNode.getData());
                }
            }
        }
        return currentNode;
    }

    /**
     * helper method
     *
     * @param currentNode the node we currently at
     * @param sortNode the node that sort data of successor
     * @return the child of successor
     */
    private BSTNode<T> successor(BSTNode<T> currentNode, BSTNode<T> sortNode) {
        if (currentNode.getLeft() == null) {
            sortNode.setData(currentNode.getData());
            return currentNode.getRight();
        } else {
            currentNode.setLeft(successor(currentNode.getLeft(), sortNode));
            return currentNode;
        }
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not search for null.");
        } else {
            BSTNode<T> sortNode = new BSTNode<>(null);
            getHelper(data, root, sortNode);
            return sortNode.getData();
        }
    }

    /**
     * helper method for get method
     *
     * @param data the data to search for
     * @param currentNode the node we currently at
     * @param sortNode the node that sort data
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private void getHelper(T data, BSTNode<T> currentNode, BSTNode<T> sortNode) {
        if (currentNode == null) {
            throw new java.util.NoSuchElementException("Can not search for data that is not in the BST.");
        } else {
            if (data.compareTo(currentNode.getData()) < 0) {
                getHelper(data, currentNode.getLeft(), sortNode);
            } else if (data.compareTo(currentNode.getData()) > 0) {
                getHelper(data, currentNode.getRight(), sortNode);
            } else {
                sortNode.setData(currentNode.getData());
            }
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Can not search for null.");
        } else {
            return containsHelper(data, root);
        }
    }

    /**
     * helper method for contains method
     *
     * @param data the data to search for
     * @param currentNode the node we currently at
     * @return true if the parameter is contained within the tree, false otherwise
     */
    private boolean containsHelper(T data, BSTNode<T> currentNode) {
        if (currentNode == null) {
            return false;
        } else if (data.compareTo(currentNode.getData()) < 0) {
            return containsHelper(data, currentNode.getLeft());
        } else if (data.compareTo(currentNode.getData()) > 0) {
            return containsHelper(data, currentNode.getRight());
        } else {
            return true;
        }
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> preList = new ArrayList<>();
        preorderHelper(preList, root);
        return preList;
    }

    /**
     * Helper method for preorder method
     *
     * @param preList the pre-order traversal of the tree
     * @param currentNode the node we currently at
     */
    private void preorderHelper(List<T> preList, BSTNode<T> currentNode) {
        if (currentNode != null) {
            preList.add(currentNode.getData());
            preorderHelper(preList, currentNode.getLeft());
            preorderHelper(preList, currentNode.getRight());
        }
    }

    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> inList = new ArrayList<>();
        inorderHelper(inList, root);
        return inList;
    }

    /**
     * Helper method for inorder method
     *
     * @param inList the in-order traversal of the tree
     * @param currentNode the node we currently at
     */
    private void inorderHelper(List<T> inList, BSTNode<T> currentNode) {
        if (currentNode != null) {
            inorderHelper(inList, currentNode.getLeft());
            inList.add(currentNode.getData());
            inorderHelper(inList, currentNode.getRight());
        }
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> postList = new ArrayList<>();
        postorderHelper(postList, root);
        return postList;
    }

    /**
     * Helper method for postorder method
     *
     * @param postList the post-order traversal of the tree
     * @param currentNode the node we currently at
     */
    private void postorderHelper(List<T> postList, BSTNode<T> currentNode) {
        if (currentNode != null) {
            postorderHelper(postList, currentNode.getLeft ());
            postorderHelper(postList, currentNode.getRight());
            postList.add(currentNode.getData());
        }
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> levelList = new ArrayList<>();
        Queue<BSTNode<T>> q = new LinkedList<>();
        q.add(root);
        while (!(q.isEmpty())) {
            BSTNode<T> currentNode = q.poll();
            levelList.add(currentNode.getData());
            if (currentNode.getLeft() != null) {
                q.add(currentNode.getLeft());
            }
            if (currentNode.getRight() != null) {
                q.add(currentNode.getRight());
            }
        }
        return levelList;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    /**
     * Helper method for height method
     *
     * @param currentNode the BST node that we curr ently at
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    private int heightHelper(BSTNode<T> currentNode) {
        if (currentNode == null) {
            return -1;
        }
        return 1 + Math.max(heightHelper(currentNode.getLeft()), heightHelper(currentNode.getRight()));
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Finds and retrieves the k-largest elements from the BST in sorted order,
     * least to greatest.
     *
     * This must be done recursively.
     *
     * In most cases, this method will not need to traverse the entire tree to
     * function properly, so you should only traverse the branches of the tree
     * necessary to get the data and only do so once. Failure to do so will
     * result in an efficiency penalty.
     *
     * EXAMPLE: Given the BST below composed of Integers:
     *
     *                50
     *              /    \
     *            25      75
     *           /  \
     *          12   37
     *         /  \    \
     *        10  15    40
     *           /
     *          13
     *
     * kLargest(5) should return the list [25, 37, 40, 50, 75].
     * kLargest(3) should return the list [40, 50, 75].
     *
     * Should have a running time of O(log(n) + k) for a balanced tree and a
     * worst case of O(n + k), with n being the number of data in the BST
     *
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     * @throws java.lang.IllegalArgumentException if k < 0 or k > size
     */
    public List<T> kLargest(int k) {
        if (k < 0 || k > size) {
            throw new java.lang.IllegalArgumentException("k must be in the range of 0 to size.");
        }
        List<T> kList = new ArrayList(k);
        return kLargestHelper(kList, root, k);
    }

    /**
     * Helper method for kLargest method
     *
     * @param kList list that retrieves the k-largest elements from the BST in sorted order
     * @param currentNode the node we currently at
     * @param k the number of largest elements to return
     * @return sorted list consisting of the k largest elements
     */
    private List<T> kLargestHelper(List<T> kList, BSTNode<T> currentNode, int k) {
        if (currentNode == null) {
            return kList;
        }
        kLargestHelper(kList, currentNode.getRight(), k);
        if (kList.size() == k) {
            return kList;
        }
        kList.add(0, currentNode.getData());
        kLargestHelper(kList, currentNode.getLeft(), k);
        return kList;
    }


    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
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
