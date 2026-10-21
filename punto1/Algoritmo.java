package punto1;

import java.util.*;

public class Algoritmo {

    // Fuente: https://www.geeksforgeeks.org/dsa/insertion-sort-algorithm/

    static class InsertionSort {
        static void insertionSort(int[] arr) {
            int n = arr.length;
            for (int i = 1; i < n; ++i) {
                int key = arr[i];
                int j = i - 1;

                // Move elements of arr[0..i-1] that are greater than key ahead
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
        }
    }

    static void printArray(int[] arr) {
        for (int x : arr) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    // Fuente: https://www.geeksforgeeks.org/dsa/dual-pivot-quicksort/
    // dual pivot QuickSort
    static class DualPivotQuicksort {

        static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        static void dualPivotQuickSort(int[] arr,
                int low, int high) {
            if (low < high) {

                // piv[] stores left pivot and right pivot.
                // piv[0] means left pivot and
                // piv[1] means right pivot
                int[] piv;
                piv = partition(arr, low, high);

                dualPivotQuickSort(arr, low, piv[0] - 1);
                dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
                dualPivotQuickSort(arr, piv[1] + 1, high);
            }
        }

        static int[] partition(int[] arr, int low, int high) {
            if (arr[low] > arr[high])
                swap(arr, low, high);

            // p is the left pivot, and q
            // is the right pivot.
            int j = low + 1;
            int g = high - 1, k = low + 1,
                    p = arr[low], q = arr[high];

            while (k <= g) {

                // If elements are less than the left pivot
                if (arr[k] < p) {
                    swap(arr, k, j);
                    j++;
                }

                // If elements are greater than or equal
                // to the right pivot
                else if (arr[k] >= q) {
                    while (arr[g] > q && k < g)
                        g--;

                    swap(arr, k, g);
                    g--;

                    if (arr[k] < p) {
                        swap(arr, k, j);
                        j++;
                    }
                }
                k++;
            }
            j--;
            g++;

            // Bring pivots to their appropriate positions.
            swap(arr, low, j);
            swap(arr, high, g);

            // Returning the indices of the pivots
            // because we cannot return two elements
            // from a function, we do that using an array.
            return new int[] { j, g };
        }

    }

    // This code is contributed by Gourish Sadhu

    // Fuente: https://www.geeksforgeeks.org/dsa/merge-sort/

    static class MergeSort {

        // Merges two subarrays of arr[].
        // First subarray is arr[l..m]
        // Second subarray is arr[m+1..r]
        static void merge(int arr[], int l, int m, int r) {

            // Find sizes of two subarrays to be merged
            int n1 = m - l + 1;
            int n2 = r - m;

            // Create temp arrays
            int L[] = new int[n1];
            int R[] = new int[n2];

            // Copy data to temp arrays
            for (int i = 0; i < n1; ++i)
                L[i] = arr[l + i];
            for (int j = 0; j < n2; ++j)
                R[j] = arr[m + 1 + j];

            // Merge the temp arrays

            // Initial indices of first and second subarrays
            int i = 0, j = 0;

            // Initial index of merged subarray array
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }

            // Copy remaining elements of L[] if any
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }

            // Copy remaining elements of R[] if any
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }

        // Main function that sorts arr[l..r] using
        // merge()
        static void mergeSort(int arr[], int l, int r) {

            if (l < r) {

                // Find the middle point
                int m = l + (r - l) / 2;

                // Sort first and second halves
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);

                // Merge the sorted halves
                merge(arr, l, m, r);
            }
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/heap-sort/

    static class HeapSort {

        // To heapify a subtree rooted with node i
        static void heapify(int[] arr, int n, int i) {

            // Initialize largest as root
            int largest = i;

            // left index = 2*i + 1
            int l = 2 * i + 1;

            // right index = 2*i + 2
            int r = 2 * i + 2;

            // If left child is larger than root
            if (l < n && arr[l] > arr[largest])
                largest = l;

            // If right child is larger than largest so far
            if (r < n && arr[r] > arr[largest])
                largest = r;

            // If largest is not root
            if (largest != i) {
                int temp = arr[i];
                arr[i] = arr[largest];
                arr[largest] = temp;

                // Recursively heapify the affected sub-tree
                heapify(arr, n, largest);
            }
        }

        // Main function to do heap sort
        static void heapSort(int[] arr) {
            int n = arr.length;

            // Build heap (rearrange vector)
            for (int i = n / 2 - 1; i >= 0; i--)
                heapify(arr, n, i);

            // One by one extract an element from heap
            for (int i = n - 1; i > 0; i--) {

                // Move current root to end
                int temp = arr[0];
                arr[0] = arr[i];
                arr[i] = temp;

                // Call max heapify on the reduced heap
                heapify(arr, i, 0);
            }
        }

        public static void main(String[] args) {
            int[] arr = { 9, 4, 3, 8, 10, 2, 5 };

            heapSort(arr);

            for (int i = 0; i < arr.length; ++i)
                System.out.print(arr[i] + " ");
        }
    }

    // Fuente: https://www.geeksforgeeks.org/dsa/radix-sort/

    static class Radix {

        // A utility function to get maximum value in arr[]
        static int getMax(int arr[], int n) {
            int mx = arr[0];
            for (int i = 1; i < n; i++)
                if (arr[i] > mx)
                    mx = arr[i];
            return mx;
        }

        // A function to do counting sort of arr[] according to
        // the digit represented by exp.
        static void countSort(int arr[], int n, int exp) {
            int output[] = new int[n]; // output array
            int i;
            int count[] = new int[10];
            Arrays.fill(count, 0);

            // Store count of occurrences in count[]
            for (i = 0; i < n; i++)
                count[(arr[i] / exp) % 10]++;

            // Change count[i] so that count[i] now contains
            // actual position of this digit in output[]
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            // Build the output array
            for (i = n - 1; i >= 0; i--) {
                output[count[(arr[i] / exp) % 10] - 1] = arr[i];
                count[(arr[i] / exp) % 10]--;
            }

            // Copy the output array to arr[], so that arr[] now
            // contains sorted numbers according to current
            // digit
            for (i = 0; i < n; i++)
                arr[i] = output[i];
        }

        // The main function to that sorts arr[] of
        // size n using Radix Sort
        static void radixsort(int arr[], int n) {
            // Find the maximum number to know number of digits
            int m = getMax(arr, n);

            // Do counting sort for every digit. Note that
            // instead of passing digit number, exp is passed.
            // exp is 10^i where i is current digit number
            for (int exp = 1; m / exp > 0; exp *= 10)
                countSort(arr, n, exp);
        }

        // A utility function to print an array
        static void print(int arr[], int n) {
            for (int i = 0; i < n; i++)
                System.out.print(arr[i] + " ");
        }

        // Main driver method
        public static void main(String[] args) {
            int arr[] = { 170, 45, 75, 90, 802, 24, 2, 66 };
            int n = arr.length;

            // Function Call
            radixsort(arr, n);
            print(arr, n);
        }
    }

    // Fuente: https://www.geeksforgeeks.org/dsa/bucket-sort-2/

    static class BucketSort {
        // Insertion sort function to sort individual buckets
        public static void insertionSort(List<Integer> bucket) {
            for (int i = 1; i < bucket.size(); ++i) {
                int key = bucket.get(i);
                int j = i - 1;
                while (j >= 0 && bucket.get(j) > key) {
                    bucket.set(j + 1, bucket.get(j));
                    j--;
                }
                bucket.set(j + 1, key);
            }
        }

        // Function to sort arr[] of size n using bucket sort
        public static void bucketSort(int[] arr) {
            int n = arr.length;

            int min = arr[0], max = arr[0];
            for (int i = 1; i < n; i++) {
                if (arr[i] < min)
                    min = arr[i];
                if (arr[i] > max)
                    max = arr[i];
            }
            long range = (long) max - min + 1;

            // 1) Create n empty buckets
            List<Integer>[] buckets = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                buckets[i] = new ArrayList<>();
            }

            // 2) Put array elements in different buckets
            for (int i = 0; i < n; i++) {
                int bi = (int) (((long) (arr[i] - min) * n) / range);
                buckets[bi].add(arr[i]);
            }

            // 3) Sort individual buckets using insertion sort
            for (int i = 0; i < n; i++) {
                insertionSort(buckets[i]);
            }

            // 4) Concatenate all buckets into arr[]
            int index = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < buckets[i].size(); j++) {
                    arr[index++] = buckets[i].get(j);
                }
            }
        }
    }
}