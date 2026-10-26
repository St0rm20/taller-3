package util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TxtManager {

    public static int[] cargarNumeros(String ruta) throws Exception {
        return Files.readAllLines(Paths.get(ruta))
                .stream()
                .filter(linea -> !linea.isBlank())
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}