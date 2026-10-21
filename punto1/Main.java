package punto1;

import util.TxtManager;
import util.Csv;

import java.util.Arrays;
import java.util.function.Consumer;

public class Main {

    private static final int[] TAMANOS = { 1_000_000, 100_000, 10_000, 1_000 };
    private static final int REPETICIONES = 5;

    // Calentamiento de la JVM (no se mide ni se guarda)
    private static final int CALENTAMIENTO_REPETICIONES = 10;
    private static final int CALENTAMIENTO_TAMANO = 50_000;

    // --------------------- Metodos auxiliares ---------------------------

    public static boolean estaOrdenado(int[] arreglo) {
        for (int i = 1; i < arreglo.length; i++) {
            if (arreglo[i] < arreglo[i - 1]) {
                System.out.println("------------------ Error ----------------------------");
                System.out.println("El arreglo no está ordenado");
                return false;
            }
        }
        return true;
    }

    // Mide el tiempo (ns) que tarda un algoritmo en ordenar el arreglo recibido
    public static long ejecutar(Consumer<int[]> algoritmo, int[] arr) {
        long tiempoInicial = System.nanoTime();
        algoritmo.accept(arr);
        long tiempoFinal = System.nanoTime();
        System.out.println("Tiempo: " + (tiempoFinal - tiempoInicial));
        return tiempoFinal - tiempoInicial;
    }

    // Ejecuta el algoritmo varias veces sin medir para que el JIT lo compile
    private static void calentar(Consumer<int[]> algoritmo, int[] arr) {
        System.out.println("Calentando...");
        int n = Math.min(CALENTAMIENTO_TAMANO, arr.length);
        for (int i = 0; i < CALENTAMIENTO_REPETICIONES; i++) {
            algoritmo.accept(Arrays.copyOf(arr, n));
        }
    }

    // Corre un algoritmo para todos los tamaños, REPETICIONES veces cada uno
    private static void probar(String nombre, int[] arr, Consumer<int[]> algoritmo) {
        System.out.println("Algoritmo " + nombre);

        calentar(algoritmo, arr);

        for (int n : TAMANOS) {
            System.out.println("Longitud " + n);
            for (int i = 0; i < REPETICIONES; i++) {
                int[] arrCopy = Arrays.copyOf(arr, n);
                long res = ejecutar(algoritmo, arrCopy);
                if (estaOrdenado(arrCopy)) {
                    System.out.println("ordenado");
                    Csv.guardarDatos(nombre, "ordenamiento", n, res);
                }
            }
        }
    }

    // --------------------- Un metodo por algoritmo ---------------------------

    public static void insertionSort(int[] arr) {
        probar("Insertion sort", arr, a -> Algoritmo.InsertionSort.insertionSort(a));
    }

    public static void dualPivotQuickSort(int[] arr) {
        probar("Dual pivot quicksort", arr,
                a -> Algoritmo.DualPivotQuicksort.dualPivotQuickSort(a, 0, a.length - 1));
    }

    public static void mergeSort(int[] arr) {
        probar("Merge sort", arr,
                a -> Algoritmo.MergeSort.mergeSort(a, 0, a.length - 1));
    }

    public static void heapSort(int[] arr) {
        probar("Heap sort", arr, a -> Algoritmo.HeapSort.heapSort(a));
    }

    public static void radixSort(int[] arr) {
        probar("Radix sort", arr, a -> Algoritmo.Radix.radixsort(a, a.length));
    }

    public static void bucketSort(int[] arr) {
        probar("Bucket sort", arr, a -> Algoritmo.BucketSort.bucketSort(a));
    }

    // --------------------- Main ---------------------------

    public static void main(String[] args) throws Exception {

        int[] arr = TxtManager.cargarNumeros();
        System.out.println("Longitud del arreglo extraido: " + arr.length);

        insertionSort(arr);
        dualPivotQuickSort(arr);
        mergeSort(arr);
        heapSort(arr);
        radixSort(arr);
        bucketSort(arr);
    }
}