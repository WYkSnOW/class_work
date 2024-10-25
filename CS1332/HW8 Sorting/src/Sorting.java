import java.util.*;

/**
 * Your implementation of various sorting algorithms.
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
public class Sorting {

    /**
     * Implement selection sort.
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n^2)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new java.lang.IllegalArgumentException("Array can not be null.");
        }
        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator can not be null.");
        }
        for (int n = arr.length - 1; n > 0; n--) {
            int max = n;
            for (int i = 0; i < n; i++) {
                if (comparator.compare(arr[i], arr[max]) > 0 ) {
                    max = i;
                }
            }
            T temp = arr[max];
            arr[max] = arr[n];
            arr[n] = temp;
        }
    }

    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new java.lang.IllegalArgumentException("The array can not be null");
        }
        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator can not be null");
        }
        boolean swapsMade = true;
        int startInd = 0;
        int endInd = arr.length - 1;
        int swap = 0;
        while (startInd < endInd && swapsMade) {
            swapsMade = false;
            for (int i = startInd; i < endInd; i++) {
                if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapsMade = true;
                    swap = i;
                }
            }
            endInd = swap;
            if (swapsMade) {
                swapsMade = false;
                for (int i = endInd; i > startInd; i--) {
                    if (comparator.compare(arr[i - 1], arr[i]) > 0) {
                        T temp = arr[i];
                        arr[i] = arr[i - 1];
                        arr[i - 1] = temp;
                        swapsMade = true;
                        swap = i;
                    }
                }
                startInd = swap;
            }
        }
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new java.lang.IllegalArgumentException("The array can not be null");
        }
        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator can not be null");
        }
        if (arr.length > 1) {
            int length = arr.length;
            int midIndex = length / 2;
            T[] left = (T[]) new Object[midIndex];
            T[] right = (T[]) new Object[length - midIndex];
            for (int i = 0; i < left.length; i++) {
                left[i] = arr[i];
            }
            for (int i = 0; i < right.length; i++) {
                right[i] = arr[i + midIndex];
            }
            mergeSort(left, comparator);
            mergeSort(right, comparator);
            int i = 0;
            int j = 0;
            while (i < left.length && j < right.length) {
                if (comparator.compare(left[i], right[j]) <= 0) {
                    arr[i + j] = left[i];
                    i++;
                } else {
                    arr[i + j] = right[j];
                    j++;
                }
            }
            while (i < left.length) {
                arr[i + j] = left[i];
                i++;
            }
            while (j < right.length) {
                arr[i + j] = right[j];
                j++;
            }
        }
    }

    /**
     * Implement kth select.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * You may assume that the array doesn't contain any null elements.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param k          the index to retrieve data from + 1 (due to
     *                   0-indexing) if the array was sorted; the 'k' in "kth
     *                   select"; e.g. if k == 1, return the smallest element
     *                   in the array
     * @param arr        the array that should be modified after the method
     *                   is finished executing as needed
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @return the kth smallest element
     * @throws java.lang.IllegalArgumentException if the array or comparator
     *                                            or rand is null or k is not
     *                                            in the range of 1 to arr
     *                                            .length
     */
    public static <T> T kthSelect(int k, T[] arr, Comparator<T> comparator,
                                  Random rand) {
        if (arr == null || comparator == null || rand == null || k < 1 || k > arr.length) {
            throw new java.lang.IllegalArgumentException(".");
        }
        return kthSelectHelper(arr, k, 0, arr.length, comparator, rand);
    }

    /**
     * Helper for kthSelect method
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param k          the index to retrieve data from + 1 (due to
     *                   0-indexing) if the array was sorted; the 'k' in "kth
     *                   select"; e.g. if k == 1, return the smallest element
     *                   in the array
     * @param start      left bound
     * @param end        right bound
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @return the kth smallest element
     */
    private static <T> T kthSelectHelper(T[] arr, int k, int start, int end, Comparator<T> comparator, Random rand) {
        int pivotIdx = rand.nextInt(end - start) + start;
        T pivotVal = arr[pivotIdx];
        T temp = arr[start];
        arr[start] = pivotVal;
        arr[pivotIdx] = temp;
        int i = start + 1;
        int j = end - 1;
        while (i <= j) {
            while (i <= j && comparator.compare(arr[i], pivotVal) <= 0) {
                i++;
            }
            while (i <= j && comparator.compare(arr[j], pivotVal) >= 0) {
                j--;
            }
            if (i <= j) {
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        temp = arr[start];
        arr[start] = arr[j];
        arr[j] = temp;
        if (j == k - 1) {
            return arr[j];
        } else if (j > k - 1) {
            return kthSelectHelper(arr, k, start, j, comparator, rand);
        }
        return kthSelectHelper(arr, k, j + 1, end, comparator, rand);
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need. The number of iterations
     * can be determined using the number with the largest magnitude.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new java.lang.IllegalArgumentException("The Array can not be null") ;
        }
        if (arr.length > 1) {
            LinkedList<Integer>[] buckets = new LinkedList[19];
            for (int i = 0; i < 19; i++) {
                buckets[i] = new LinkedList<>();
            }
            int longest = arr[0];
            int longestDigit = 1;

            for (int i : arr) {
                if (Math.abs(i) > longest) {
                    longest = i;
                }
            }
            while (longest >= 10) {
                longestDigit++;
                longest = (int)longest / 10;
            }
            int div = 1;
            for (int k = 0; k < longestDigit; k++) {
                for (int j : arr) {
                    int currentDigit = (j / div) % 10;
                    buckets[currentDigit + 9].add(j);
                }
                int idx = 0;
                for (LinkedList<Integer> bucket : buckets) {
                    while (!(bucket.isEmpty())) {
                        arr[idx++] = bucket.remove();
                    }
                }
                div *= 10;
            }
        }
    }

    /**
     * Implement heap sort.
     *
     * It should be:
     * out-of-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Use java.util.PriorityQueue as the heap. Note that in this
     * PriorityQueue implementation, elements are removed from smallest
     * element to largest element.
     *
     * Initialize the PriorityQueue using its build heap constructor (look at
     * the different constructors of java.util.PriorityQueue).
     *
     * Return an int array with a capacity equal to the size of the list. The
     * returned array should have the elements in the list in sorted order.
     *
     * @param data the data to sort
     * @return the array with length equal to the size of the input list that
     * holds the elements from the list is sorted order
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public static int[] heapSort(List<Integer> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("The data can not be null");
        }
        PriorityQueue queue = new PriorityQueue(data);
        int[] sorted = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            sorted[i] = (int)queue.remove();
        }
        return sorted;
    }
}
