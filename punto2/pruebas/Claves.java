package punto2.pruebas;

import util.TxtManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Conjunto de 1.000 claves por tamaño: 500 presentes y 500 ausentes.
// Se genera una sola vez y se guarda en punto2/pruebas/claves_<n>.txt;
// si el archivo ya existe se carga (Java y Python comparten el mismo archivo).
public class Claves {

    static final String CARPETA = "punto2/pruebas";
    static final long SEMILLA = 42;
    static final int PRESENTES = 500;
    static final int AUSENTES_MENORES = 50;
    static final int AUSENTES_MAYORES = 50;
    static final int AUSENTES_EN_RANGO = 400;
    static final int TOTAL = PRESENTES + AUSENTES_MENORES + AUSENTES_MAYORES + AUSENTES_EN_RANGO;

    // arr: arreglo ordenado de tamaño n (los primeros n elementos del ascendente)
    public static int[] cargarOGenerar(int[] arr) throws Exception {
        Path ruta = Paths.get(CARPETA, "claves_" + arr.length + ".txt");
        if (Files.exists(ruta)) {
            int[] claves = TxtManager.cargarNumeros(ruta.toString());
            if (claves.length == TOTAL)
                return claves;
            System.out.println("El archivo " + ruta + " no tiene " + TOTAL + " claves; se regenera");
        }
        int[] claves = generar(arr);
        guardar(ruta, claves);
        System.out.println("Claves generadas en " + ruta);
        return claves;
    }

    static int[] generar(int[] arr) {
        int n = arr.length;
        Random azar = new Random(SEMILLA + n);
        List<Integer> claves = new ArrayList<>();

        // Presentes: valores distintos elegidos al azar del arreglo
        Set<Integer> presentes = new LinkedHashSet<>();
        while (presentes.size() < PRESENTES)
            presentes.add(arr[azar.nextInt(n)]);
        claves.addAll(presentes);

        // Ausentes: algunos menores que el minimo, algunos mayores que el maximo
        // y el resto dentro del rango pero que no pertenezcan al arreglo
        int min = arr[0], max = arr[n - 1];
        Set<Integer> ausentes = new LinkedHashSet<>();
        while (ausentes.size() < AUSENTES_MENORES)
            ausentes.add(1 + azar.nextInt(min - 1));
        int tope = ausentes.size() + AUSENTES_MAYORES;
        while (ausentes.size() < tope)
            ausentes.add((int) Math.min(Integer.MAX_VALUE, (long) max + 1 + azar.nextInt(1_000_000)));
        tope = ausentes.size() + AUSENTES_EN_RANGO;
        while (ausentes.size() < tope) {
            int x = min + azar.nextInt(max - min + 1);
            if (Arrays.binarySearch(arr, x) < 0)
                ausentes.add(x);
        }
        claves.addAll(ausentes);

        // Se mezclan para que presentes y ausentes queden intercalados
        Collections.shuffle(claves, azar);
        return claves.stream().mapToInt(Integer::intValue).toArray();
    }

    static void guardar(Path ruta, int[] claves) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (int c : claves)
            lineas.add(String.valueOf(c));
        Files.write(ruta, lineas);
    }
}
