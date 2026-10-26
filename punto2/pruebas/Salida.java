package punto2.pruebas;

import util.Csv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

// Escribe los CSV de las pruebas de busqueda (sin tocar util.Csv).
// Respeta la bandera Csv.guardar: si es false no se escribe nada.
public class Salida {

    static final String CARPETA = "tiempos/java";
    static final String ARCHIVO_CLAVES = "busqueda_claves.csv";
    static final String ARCHIVO_POSICION = "busqueda_posicion.csv";
    static final String ENCABEZADO_CLAVES = "algoritmo,longitud,repeticion,tiempo_total_ns,tiempo_promedio_ns";
    static final String ENCABEZADO_POSICION = "algoritmo,longitud,posicion,repeticion,tiempo_total_ns,tiempo_promedio_ns";

    public static void registrarClaves(String algoritmo, int longitud, int repeticion,
            long total, double promedio) {
        String fila = String.format(Locale.ROOT, "%s,%d,%d,%d,%.3f",
                algoritmo, longitud, repeticion, total, promedio);
        agregar(ARCHIVO_CLAVES, ENCABEZADO_CLAVES, fila);
    }

    public static void registrarPosicion(String algoritmo, int longitud, String posicion,
            int repeticion, long total, double promedio) {
        String fila = String.format(Locale.ROOT, "%s,%d,%s,%d,%d,%.3f",
                algoritmo, longitud, posicion, repeticion, total, promedio);
        agregar(ARCHIVO_POSICION, ENCABEZADO_POSICION, fila);
    }

    // Agrega una fila al archivo; crea la carpeta y el encabezado si hacen falta
    private static void agregar(String nombreArchivo, String encabezado, String fila) {
        if (!Csv.guardar)
            return;
        Path archivo = Paths.get(CARPETA, nombreArchivo);
        try {
            Files.createDirectories(archivo.getParent());
            if (!Files.exists(archivo)) {
                Files.write(archivo, (encabezado + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            }
            Files.write(archivo, (fila + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir en " + archivo, e);
        }
    }
}
