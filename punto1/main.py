#Fuente: https://www.geeksforgeeks.org/dsa/insertion-sort-algorithm/

def insertionSort(arr):
    n = len(arr)
    for i in range(1, n):
        key = arr[i]
        j = i - 1

        # Move elements of arr[0..i-1] that are greater than key ahead
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1

        arr[j + 1] = key

#Fuente: https://www.geeksforgeeks.org/dsa/dual-pivot-quicksort/

# Python3 program to implement 
# dual pivot QuickSort
def dualPivotQuickSort(arr, low, high):
    
    if low < high:
        
        # lp means left pivot and rp 
        # means right pivot
        lp, rp = partition(arr, low, high)
        
        dualPivotQuickSort(arr, low, lp - 1)
        dualPivotQuickSort(arr, lp + 1, rp - 1)
        dualPivotQuickSort(arr, rp + 1, high)
        
def partition(arr, low, high):
    
    if arr[low] > arr[high]:
        arr[low], arr[high] = arr[high], arr[low]
        
    # p is the left pivot, and q is the right pivot.
    j = k = low + 1
    g, p, q = high - 1, arr[low], arr[high]
    
    while k <= g:
        
        # If elements are less than the left pivot
        if arr[k] < p:
            arr[k], arr[j] = arr[j], arr[k]
            j += 1
            
        # If elements are greater than or equal 
        # to the right pivot
        elif arr[k] >= q:
            while arr[g] > q and k < g:
                g -= 1
                
            arr[k], arr[g] = arr[g], arr[k]
            g -= 1
            
            if arr[k] < p:
                arr[k], arr[j] = arr[j], arr[k]
                j += 1
                
        k += 1
        
    j -= 1
    g += 1
    
    # Bring pivots to their appropriate positions.
    arr[low], arr[j] = arr[j], arr[low]
    arr[high], arr[g] = arr[g], arr[high]
    
    # Returning the indices of the pivots
    return j, g

# This code is contributed by Gourish Sadhu


#fuente: https://www.geeksforgeeks.org/dsa/merge-sort/

def merge(arr, left, mid, right):
    n1 = mid - left + 1
    n2 = right - mid

    # Create temp arrays
    L = [0] * n1
    R = [0] * n2

    # Copy data to temp arrays L[] and R[]
    for i in range(n1):
        L[i] = arr[left + i]
    for j in range(n2):
        R[j] = arr[mid + 1 + j]
        
    i = 0  
    j = 0  
    k = left  

    # Merge the temp arrays back
    # into arr[left..right]
    while i < n1 and j < n2:
        if L[i] <= R[j]:
            arr[k] = L[i]
            i += 1
        else:
            arr[k] = R[j]
            j += 1
        k += 1

    # Copy the remaining elements of L[],
    # if there are any
    while i < n1:
        arr[k] = L[i]
        i += 1
        k += 1

    # Copy the remaining elements of R[], 
    # if there are any
    while j < n2:
        arr[k] = R[j]
        j += 1
        k += 1

def mergeSort(arr, left, right):
    if left < right:
        mid = (left + right) // 2

        mergeSort(arr, left, mid)
        mergeSort(arr, mid + 1, right)
        merge(arr, left, mid, right)


#Fuente: https://www.geeksforgeeks.org/dsa/heap-sort/


# To heapify a subtree rooted with node i
def heapify(arr, n, i):

    # Initialize largest as root
    largest = i

    # left index = 2*i + 1
    l = 2 * i + 1

    # right index = 2*i + 2
    r = 2 * i + 2

    # If left child is larger than root
    if l < n and arr[l] > arr[largest]:
        largest = l

    # If right child is larger than largest so far
    if r < n and arr[r] > arr[largest]:
        largest = r

    # If largest is not root
    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]

        # Recursively heapify the affected sub-tree
        heapify(arr, n, largest)

# Main function to do heap sort
def heapSort(arr):
    n = len(arr)

    # Build heap (rearrange vector)
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)

    # One by one extract an element from heap
    for i in range(n - 1, 0, -1):

        # Move current root to end
        arr[0], arr[i] = arr[i], arr[0]

        # Call max heapify on the reduced heap
        heapify(arr, i, 0)

if __name__ == "__main__":
    arr = [9, 4, 3, 8, 10, 2, 5]

    heapSort(arr)

    for i in range(len(arr)):
        print(arr[i], end=" ")


#Fuente: https://www.geeksforgeeks.org/dsa/radix-sort/


# Python program for implementation of Radix Sort
# A function to do counting sort of arr[] according to
# the digit represented by exp.


def countingSort(arr, exp1):

    n = len(arr)

    # The output array elements that will have sorted arr
    output = [0] * (n)

    # initialize count array as 0
    count = [0] * (10)

    # Store count of occurrences in count[]
    for i in range(0, n):
        index = arr[i] // exp1
        count[index % 10] += 1

    # Change count[i] so that count[i] now contains actual
    # position of this digit in output array
    for i in range(1, 10):
        count[i] += count[i - 1]

    # Build the output array
    i = n - 1
    while i >= 0:
        index = arr[i] // exp1
        output[count[index % 10] - 1] = arr[i]
        count[index % 10] -= 1
        i -= 1

    # Copying the output array to arr[],
    # so that arr now contains sorted numbers
    i = 0
    for i in range(0, len(arr)):
        arr[i] = output[i]

# Method to do Radix Sort


def radixSort(arr):

    # Find the maximum number to know number of digits
    max1 = max(arr)

    # Do counting sort for every digit. Note that instead
    # of passing digit number, exp is passed. exp is 10^i
    # where i is current digit number
    exp = 1
    while max1 / exp >= 1:
        countingSort(arr, exp)
        exp *= 10


#Fuente: https://www.geeksforgeeks.org/dsa/bucket-sort-2/

def insertion_sort(bucket):
    for i in range(1, len(bucket)):
        key = bucket[i]
        j = i - 1
        while j >= 0 and bucket[j] > key:
            bucket[j + 1] = bucket[j]
            j -= 1
        bucket[j + 1] = key
 
def bucket_sort(arr):
    n = len(arr)
    buckets = [[] for _ in range(n)]
 
    mn = min(arr)
    rng = max(arr) - mn + 1
 
    # Put array elements in different buckets
    for num in arr:
        bi = (num - mn) * n // rng
        buckets[bi].append(num)
 
    # Sort individual buckets using insertion sort
    for bucket in buckets:
        insertion_sort(bucket)
 
    # Concatenate all buckets into arr[]
    index = 0
    for bucket in buckets:
        for num in bucket:
            arr[index] = num
            index += 1
 