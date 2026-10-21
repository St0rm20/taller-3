package punto2;

public class Algoritmo {

    // Fuente: https://www.geeksforgeeks.org/dsa/linear-search/

    static class LinearSearch {

        // Function to search for an element in the array
        static int search(int[] arr, int x) {

            // Iterate over the array to find x
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == x) {
                    return i;
                }
            }

            return -1;
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/binary-search/

    static class BinarySearch {

        static int binarySearch(int arr[], int x) {
            int low = 0, high = arr.length - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;

                // Check if x is present at mid
                if (arr[mid] == x)
                    return mid;

                // If x greater, ignore left half
                if (arr[mid] < x)
                    low = mid + 1;

                // If x is smaller, ignore right half
                else
                    high = mid - 1;
            }

            // If we reach here, then element was
            // not present
            return -1;
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/ternary-search/

    static class TernarySearch {
        static boolean ternarySearch(int[] arr, int x) {
            int l = 0;
            int r = arr.length - 1;

            while (l <= r) {

                // Split array portion into 3 parts
                int mid1 = l + (r - l) / 3;
                int mid2 = r - (r - l) / 3;

                if (arr[mid1] == x || arr[mid2] == x) {
                    return true;
                }

                if (x < arr[mid1]) {
                    r = mid1 - 1;
                } else if (x > arr[mid2]) {
                    l = mid2 + 1;
                } else {
                    l = mid1 + 1;
                    r = mid2 - 1;
                }
            }

            return false;
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/jump-search/

    // Java program to implement Jump Search.
    public class JumpSearch {
        public static int jumpSearch(int[] arr, int x) {
            int n = arr.length;

            // Finding block size to be jumped
            int step = (int) Math.floor(Math.sqrt(n));

            // Finding the block where element is
            // present (if it is present)
            int prev = 0;
            for (int minStep = Math.min(step, n) - 1; arr[minStep] < x; minStep = Math.min(step, n) - 1) {
                prev = step;
                step += (int) Math.floor(Math.sqrt(n));
                if (prev >= n)
                    return -1;
            }

            // Doing a linear search for x in block
            // beginning with prev.
            while (arr[prev] < x) {
                prev++;

                // If we reached next block or end of
                // array, element is not present.
                if (prev == Math.min(step, n))
                    return -1;
            }

            // If element is found
            if (arr[prev] == x)
                return prev;

            return -1;
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/interpolation-search/

    // Java program to implement interpolation
    // search with recursion

    static class InterpolationSearch {

        // If x is present in arr[0..n-1], then returns
        // index of it, else returns -1.
        public static int interpolationSearch(int arr[], int lo,
                int hi, int x) {
            int pos;

            // Since array is sorted, an element
            // present in array must be in range
            // defined by corner
            if (lo <= hi && x >= arr[lo] && x <= arr[hi]) {

                // Probing the position with keeping
                // uniform distribution in mind.
                pos = lo
                        + (((hi - lo) / (arr[hi] - arr[lo]))
                                * (x - arr[lo]));

                // Condition of target found
                if (arr[pos] == x)
                    return pos;

                // If x is larger, x is in right sub array
                if (arr[pos] < x)
                    return interpolationSearch(arr, pos + 1, hi,
                            x);

                // If x is smaller, x is in left sub array
                if (arr[pos] > x)
                    return interpolationSearch(arr, lo, pos - 1,
                            x);
            }
            return -1;
        }

    }

    // Fuente: https://www.geeksforgeeks.org/dsa/exponential-search/

    static class ExponentialSearch {

        static int exponentialSearch(int arr[],
                int n, int x) {
            // If x is present at first location itself
            if (arr[0] == x)
                return 0;

            // Find range for binary search by
            // repeated doubling
            int i = 1;
            while (i < n && arr[i] <= x)
                i = i * 2;

            // Call binary search for the found range.
            return binarySearch(arr, i / 2,
                    Math.min(i, n - 1), x);
        }

        static int binarySearch(int arr[], int low, int high, int x) {
            while (low <= high) {
                int mid = low + (high - low) / 2;

                if (arr[mid] == x)
                    return mid;

                if (arr[mid] < x)
                    low = mid + 1;

                else
                    high = mid - 1;
            }

            return -1;
        }

    }
}
