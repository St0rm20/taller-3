package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;

public class Csv {

    // Si es false se asume que solo son pruebas y no se guarda nada
    public static boolean guardar = true;

    static final String CARPETA = "tiempos/java";
    static final String ENCABEZADO = "algoritmo,tipo,longitud,tiempo";

    // Metodo de utilidad: recibe los 4 parametros y hace todo el proceso
    public static void registrarTiempo(String algoritmo, String tipo, int longitud, long tiempo) {
        if (!guardar)
            return;
        crearCarpeta();
        crearArchivo(tipo);
        guardarDatos(algoritmo, tipo, longitud, tiempo);
    }

    // Metodo de utilidad: devuelve el nombre normalizado del tipo
    // ("ordenamiento" o "busqueda")
    static String normalizarTipo(String tipo) {
        String t = Normalizer.normalize(tipo.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        if (!t.equals("ordenamiento") && !t.equals("busqueda")) {
            throw new IllegalArgumentException("Tipo de algoritmo no valido: " + tipo);
        }
        return t;
    }

    // Metodo de utilidad: ruta del CSV segun el tipo
    static Path rutaArchivo(String tipo) {
        return Paths.get(CARPETA, normalizarTipo(tipo) + ".csv");
    }

    // Crea la carpeta "tiempos" si no existe
    public static void crearCarpeta() {
        if (!guardar)
            return;
        Path carpeta = Paths.get(CARPETA);
        if (!Files.exists(carpeta)) {
            try {
                Files.createDirectories(carpeta);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear la carpeta " + CARPETA, e);
            }
        }
    }

    // Crea el CSV del tipo indicado (con encabezado) si no existe
    public static void crearArchivo(String tipo) {
        if (!guardar)
            return;
        Path archivo = rutaArchivo(tipo);
        if (!Files.exists(archivo)) {
            try {
                Files.write(archivo, (ENCABEZADO + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el archivo " + archivo, e);
            }
        }
    }

    // Agrega una fila al CSV del tipo indicado
    // Agrega una fila al CSV del tipo indicado
    public static void guardarDatos(String algoritmo, String tipo, int longitud, long tiempo) {
        if (!guardar)
            return;
        crearCarpeta();
        crearArchivo(tipo);
        Path archivo = rutaArchivo(tipo);
        String fila = algoritmo + "," + normalizarTipo(tipo) + "," + longitud + "," + tiempo
                + System.lineSeparator();
        try {
            Files.write(archivo, fila.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo escribir en " + archivo, e);
        }
    }
}