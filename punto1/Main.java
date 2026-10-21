package punto1;

import util.TxtManager;

public class Main {

    // --------------------- Ejecutar algoritmos ---------------------------

    public static void ejecutarAlgoritmos(int[] arr) {
        long tiempoInicial = System.nanoTime();
        Algoritmo.InsertionSort.insertionSort(arr);
        long tiempoFinal = System.nanoTime();
        System.out.println("Tiempo: " + (tiempoFinal - tiempoInicial));
    }

    // --------------------- Crear archivo con numeros ---------------------------

    public static void main(String[] args) throws Exception {

        int[] arr = TxtManager.cargarNumeros();
        ejecutarAlgoritmos(arr);
        ejecutarAlgoritmos(arr);
        System.out.println("Hola");

    }
}
