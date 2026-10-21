import re

# Ruta del txt con los numeros (ajustala a la que usa tu TxtManager de Java)
ARCHIVO = "numeros.txt"


# Lee el txt y devuelve una lista con todos los enteros que encuentre,
# sin importar si estan separados por saltos de linea, comas o espacios
def cargar_numeros(ruta=ARCHIVO):
    with open(ruta, "r", encoding="utf-8") as f:
        return [int(x) for x in re.findall(r"-?\d+", f.read())]