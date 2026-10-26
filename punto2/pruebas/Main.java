package punto2.pruebas;

import punto2.Busquedas;
import util.TxtManager;

import java.util.Arrays;

// Pruebas del punto 2 (busqueda):
//   A) tiempo total de buscar 1.000 claves (500 presentes, 500 ausentes) y promedio por busqueda
//   B) tiempo total de buscar 1.000 veces una clave al inicio, al centro y al final
// Uso (desde la raiz): java punto2.pruebas.Main [claves|posicion]   (sin argumento hace ambas)
public class Main {

    private static final int[] TAMANOS = { 1_000_000, 100_000, 10_000, 1_000 };
    private static final int REPETICIONES = 5;
    private static final int BUSQUEDAS_POSICION = 1_000;

    // Calentamiento de la JVM (no se mide ni se guarda): varias pasadas de 1.000 busquedas
    // sobre un arreglo chico, rotando por TODOS los algoritmos para que todos lleguen
    // a la medicion con el mismo estado del JIT.
    private static final int CALENTAMIENTO_PASADAS = 100;
    private static final int CALENTAMIENTO_TAMANO = 5_000;

    private static boolean hacerClaves = true;
    private static boolean hacerPosicion = true;

    // Acumula los resultados de las busquedas: evita que el JIT las elimine
    private static int sumidero;

    // Una busqueda devuelve indice, NO_ENCONTRADO o SIN_INDICE (ver punto2.Busquedas)
    interface Busqueda {
        int buscar(int[] arr, int x);
    }

    private static final Busqueda LINEAL = Busquedas::lineal;
    private static final Busqueda BINARIA = Busquedas::binaria;
    private static final Busqueda TERNARIA = Busquedas::ternaria;
    private static final Busqueda SALTOS = Busquedas::saltos;
    private static final Busqueda INTERPOLACION = Busquedas::interpolacion;
    private static final Busqueda EXPONENCIAL = Busquedas::exponencial;

    // --------------------- Metodos auxiliares ---------------------------

    // Verifica el resultado: ausente -> NO_ENCONTRADO; presente -> indice valido (o SIN_INDICE)
    private static boolean esCorrecto(int[] arr, int resultado, int x) {
        boolean presente = Arrays.binarySearch(arr, x) >= 0;
        if (!presente)
            return resultado == Busquedas.NO_ENCONTRADO;
        return resultado == Busquedas.SIN_INDICE || (resultado >= 0 && arr[resultado] == x);
    }

    // Mide (ns) buscar todas las claves: un solo par de lecturas del reloj
    private static long medirClaves(Busqueda busqueda, int[] arr, int[] claves) {
        int suma = 0;
        long tiempoInicial = System.nanoTime();
        for (int x : claves) {
            suma += busqueda.buscar(arr, x);
        }
        long tiempoFinal = System.nanoTime();
        sumidero = suma;
        return tiempoFinal - tiempoInicial;
    }

    // Mide (ns) buscar la misma clave "veces" veces seguidas
    private static long medirRepetida(Busqueda busqueda, int[] arr, int x, int veces) {
        int suma = 0;
        long tiempoInicial = System.nanoTime();
        for (int i = 0; i < veces; i++) {
            suma += busqueda.buscar(arr, x);
        }
        long tiempoFinal = System.nanoTime();
        sumidero = suma;
        return tiempoFinal - tiempoInicial;
    }

    // Fuera del tiempo medido: repite cada busqueda una vez, la valida y comprueba
    // que la suma coincida con la de la corrida medida
    private static boolean validarClaves(Busqueda busqueda, int[] arr, int[] claves, int sumaMedida) {
        int suma = 0;
        for (int x : claves) {
            int resultado = busqueda.buscar(arr, x);
            if (!esCorrecto(arr, resultado, x)) {
                System.out.println("------------------ Error ----------------------------");
                System.out.println("Resultado incorrecto para la clave " + x + ": " + resultado);
                return false;
            }
            suma += resultado;
        }
        if (suma != sumaMedida) {
            System.out.println("------------------ Error ----------------------------");
            System.out.println("La corrida medida no coincide con la validacion");
            return false;
        }
        return true;
    }

    private static boolean validarRepetida(Busqueda busqueda, int[] arr, int x, int veces, int sumaMedida) {
        int resultado = busqueda.buscar(arr, x);
        if (!esCorrecto(arr, resultado, x) || sumaMedida != resultado * veces) {
            System.out.println("------------------ Error ----------------------------");
            System.out.println("Resultado incorrecto para la clave " + x + ": " + resultado);
            return false;
        }
        return true;
    }

    // Ejecuta todas las busquedas sin medir para que el JIT las compile
    private static void calentarTodas(int[] arr) {
        System.out.println("Calentando...");
        int[] chico = Arrays.copyOf(arr, Math.min(CALENTAMIENTO_TAMANO, arr.length));
        int[] claves = new int[1_000];
        for (int i = 0; i < claves.length; i++) {
            // Mitad presentes, mitad (casi siempre) ausentes
            claves[i] = chico[(i * 5) % chico.length] + (i % 2);
        }
        Busqueda[] todas = { LINEAL, BINARIA, TERNARIA, SALTOS, INTERPOLACION, EXPONENCIAL };
        for (int pasada = 0; pasada < CALENTAMIENTO_PASADAS; pasada++) {
            for (Busqueda busqueda : todas) {
                medirClaves(busqueda, chico, claves);
            }
        }
    }

    // --------------------- Pruebas ---------------------------

    // Prueba A: conjunto de 1.000 claves
    private static void pruebaClaves(String nombre, int[] arr, Busqueda busqueda) throws Exception {
        int n = arr.length;
        int[] claves = Claves.cargarOGenerar(arr);
        System.out.println("Prueba claves (" + claves.length + " claves)");

        for (int rep = 1; rep <= REPETICIONES; rep++) {
            long total = medirClaves(busqueda, arr, claves);
            int suma = sumidero;
            if (validarClaves(busqueda, arr, claves, suma)) {
                double promedio = total / (double) claves.length;
                System.out.println("Total: " + total + "  Promedio: " + promedio);
                Salida.registrarClaves(nombre, n, rep, total, promedio);
            }
        }
    }

    // Prueba B: la misma clave 1.000 veces, al inicio, al centro y al final
    private static void pruebaPosicion(String nombre, int[] arr, Busqueda busqueda) {
        int n = arr.length;
        String[] posiciones = { "inicio", "centro", "final" };
        int[] indices = { 0, n / 2, n - 1 };

        for (int p = 0; p < posiciones.length; p++) {
            int x = arr[indices[p]];
            System.out.println("Prueba posicion " + posiciones[p] + " (clave " + x + ")");

            for (int rep = 1; rep <= REPETICIONES; rep++) {
                long total = medirRepetida(busqueda, arr, x, BUSQUEDAS_POSICION);
                int suma = sumidero;
                if (validarRepetida(busqueda, arr, x, BUSQUEDAS_POSICION, suma)) {
                    double promedio = total / (double) BUSQUEDAS_POSICION;
                    System.out.println("Total: " + total + "  Promedio: " + promedio);
                    Salida.registrarPosicion(nombre, n, posiciones[p], rep, total, promedio);
                }
            }
        }
    }

    // Corre un algoritmo para todos los tamaños
    private static void probar(String nombre, int[] arr, Busqueda busqueda) throws Exception {
        System.out.println("Algoritmo " + nombre);

        for (int n : TAMANOS) {
            System.out.println("Longitud " + n);
            // Primeros n elementos del ascendente: sigue ordenado
            int[] parcial = Arrays.copyOf(arr, n);
            if (hacerClaves)
                pruebaClaves(nombre, parcial, busqueda);
            if (hacerPosicion)
                pruebaPosicion(nombre, parcial, busqueda);
        }
    }

    // --------------------- Un metodo por algoritmo ---------------------------

    public static void linearSearch(int[] arr) throws Exception {
        probar("Linear search", arr, LINEAL);
    }

    public static void binarySearch(int[] arr) throws Exception {
        probar("Binary search", arr, BINARIA);
    }

    public static void ternarySearch(int[] arr) throws Exception {
        probar("Ternary search", arr, TERNARIA);
    }

    public static void jumpSearch(int[] arr) throws Exception {
        probar("Jump search", arr, SALTOS);
    }

    public static void interpolationSearch(int[] arr) throws Exception {
        probar("Interpolation search", arr, INTERPOLACION);
    }

    public static void exponentialSearch(int[] arr) throws Exception {
        probar("Exponential search", arr, EXPONENCIAL);
    }

    // --------------------- Main ---------------------------

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            if (args[0].equals("claves")) {
                hacerPosicion = false;
            } else if (args[0].equals("posicion")) {
                hacerClaves = false;
            } else {
                throw new IllegalArgumentException("Argumento no valido: " + args[0] + " (usa claves|posicion)");
            }
        }

        int[] arr = TxtManager.cargarNumeros("numeros_asc.txt");
        System.out.println("Longitud del arreglo extraido: " + arr.length);

        calentarTodas(arr);

        linearSearch(arr);
        binarySearch(arr);
        ternarySearch(arr);
        jumpSearch(arr);
        interpolationSearch(arr);
        exponentialSearch(arr);
    }
}
