package util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TxtManager {

    public static int[] cargarNumeros() throws Exception {
        String ruta = "numeros.txt";
        return Files.readAllLines(Paths.get(ruta))
                .stream()
                .filter(linea -> !linea.isBlank())
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}