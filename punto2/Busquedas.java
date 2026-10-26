package punto2;

// Puente publico hacia las busquedas de Algoritmo (que son paquete-privadas),
// para poder usarlas desde punto2.pruebas sin modificar Algoritmo.java.
// Todas devuelven: indice (>= 0), NO_ENCONTRADO, o SIN_INDICE si la busqueda
// solo dice "encontrado" (ternaria).
public class Busquedas {

    public static final int NO_ENCONTRADO = -1;
    public static final int SIN_INDICE = -2;

    public static int lineal(int[] arr, int x) {
        return Algoritmo.LinearSearch.search(arr, x);
    }

    public static int binaria(int[] arr, int x) {
        return Algoritmo.BinarySearch.binarySearch(arr, x);
    }

    public static int ternaria(int[] arr, int x) {
        return Algoritmo.TernarySearch.ternarySearch(arr, x) ? SIN_INDICE : NO_ENCONTRADO;
    }

    public static int saltos(int[] arr, int x) {
        return Algoritmo.JumpSearch.jumpSearch(arr, x);
    }

    public static int interpolacion(int[] arr, int x) {
        return Algoritmo.InterpolationSearch.interpolationSearch(arr, 0, arr.length - 1, x);
    }

    public static int exponencial(int[] arr, int x) {
        return Algoritmo.ExponentialSearch.exponentialSearch(arr, arr.length, x);
    }
}
