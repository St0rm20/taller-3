package punto2;

import util.Csv;
import util.TxtManager;

import java.util.Arrays;

public class Main {

    private static final int[] TAMANOS = { 1_000_000, 100_000, 10_000, 1_000 };
    private static final int REPETICIONES = 5;

    // Calentamiento de la JVM (no se mide ni se guarda).
    // Una busqueda es muy corta, asi que se necesitan muchas llamadas para que el JIT compile.
    private static final int CALENTAMIENTO_REPETICIONES = 20_000;
    private static final int CALENTAMIENTO_TAMANO = 50_000;

    // Resultado de la ultima busqueda: sirve para validar y evita que el JIT elimine la llamada
    private static boolean ultimoResultado;

    // Una busqueda recibe el arreglo ordenado y el elemento, y dice si lo encontro bien
    interface Busqueda {
        boolean buscar(int[] arr, int x);
    }

    // --------------------- Metodos auxiliares ---------------------------

    // Para los algoritmos que devuelven indice: verifica que apunte al elemento buscado
    private static boolean esCorrecto(int[] arr, int indice, int x) {
        return indice >= 0 && arr[indice] == x;
    }

    // Mide el tiempo (ns) que tarda una busqueda
    public static long ejecutar(Busqueda busqueda, int[] arr, int x) {
        long tiempoInicial = System.nanoTime();
        boolean resultado = busqueda.buscar(arr, x);
        long tiempoFinal = System.nanoTime();
        ultimoResultado = resultado;
        System.out.println("Tiempo: " + (tiempoFinal - tiempoInicial));
        return tiempoFinal - tiempoInicial;
    }

    // Ejecuta la busqueda muchas veces sin medir para que el JIT la compile
    private static void calentar(Busqueda busqueda, int[] arr) {
        System.out.println("Calentando...");
        int[] ordenado = Arrays.copyOf(arr, Math.min(CALENTAMIENTO_TAMANO, arr.length));
        Arrays.sort(ordenado);
        int x = ordenado[ordenado.length - 1];
        for (int i = 0; i < CALENTAMIENTO_REPETICIONES; i++) {
            ultimoResultado = busqueda.buscar(ordenado, x);
        }
    }

    // Corre un algoritmo para todos los tamaños, REPETICIONES veces cada uno
    private static void probar(String nombre, int[] arr, Busqueda busqueda) {
        System.out.println("Algoritmo " + nombre);

        calentar(busqueda, arr);

        for (int n : TAMANOS) {
            System.out.println("Longitud " + n);

            // La busqueda no modifica el arreglo: se prepara una sola vez por tamaño.
            // Se ordena porque binaria, ternaria, jump, interpolation y exponential lo requieren.
            int[] ordenado = Arrays.copyOf(arr, n);
            Arrays.sort(ordenado);

            // Se busca el mayor elemento (peor caso para la lineal y para las demas)
            int x = ordenado[n - 1];

            for (int i = 0; i < REPETICIONES; i++) {
                long res = ejecutar(busqueda, ordenado, x);
                if (ultimoResultado) {
                    System.out.println("encontrado");
                    Csv.registrarTiempo(nombre, "busqueda", n, res);
                } else {
                    System.out.println("------------------ Error ----------------------------");
                    System.out.println("No se encontro el elemento");
                }
            }
        }
    }

    // --------------------- Un metodo por algoritmo ---------------------------

    public static void linearSearch(int[] arr) {
        probar("Linear search", arr,
                (a, x) -> esCorrecto(a, Algoritmo.LinearSearch.search(a, x), x));
    }

    public static void binarySearch(int[] arr) {
        probar("Binary search", arr,
                (a, x) -> esCorrecto(a, Algoritmo.BinarySearch.binarySearch(a, x), x));
    }

    public static void ternarySearch(int[] arr) {
        probar("Ternary search", arr,
                (a, x) -> Algoritmo.TernarySearch.ternarySearch(a, x));
    }

    public static void jumpSearch(int[] arr) {
        probar("Jump search", arr,
                (a, x) -> esCorrecto(a, Algoritmo.JumpSearch.jumpSearch(a, x), x));
    }

    public static void interpolationSearch(int[] arr) {
        probar("Interpolation search", arr,
                (a, x) -> esCorrecto(a, Algoritmo.InterpolationSearch.interpolationSearch(a, 0, a.length - 1, x), x));
    }

    public static void exponentialSearch(int[] arr) {
        probar("Exponential search", arr,
                (a, x) -> esCorrecto(a, Algoritmo.ExponentialSearch.exponentialSearch(a, a.length, x), x));
    }

    // --------------------- Main ---------------------------

    public static void main(String[] args) throws Exception {

        int[] arr = TxtManager.cargarNumeros("ascendente.txt");
        System.out.println("Longitud del arreglo extraido: " + arr.length);

//        linearSearch(arr);
//        binarySearch(arr);
//        ternarySearch(arr);
//        jumpSearch(arr);
        interpolationSearch(arr);
        exponentialSearch(arr);
    }
}