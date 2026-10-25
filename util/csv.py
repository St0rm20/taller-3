import os
import unicodedata

# Si es False se asume que solo son pruebas y no se guarda nada
guardar = False

CARPETA = os.path.join("tiempos", "python")
ENCABEZADO = "algoritmo,tipo,longitud,tiempo"


# Metodo de utilidad: recibe los 4 parametros y hace todo el proceso
def registrar_tiempo(algoritmo, tipo, longitud, tiempo):
    if not guardar:
        return
    crear_carpeta()
    crear_archivo(tipo)
    guardar_datos(algoritmo, tipo, longitud, tiempo)


# Metodo de utilidad: devuelve el nombre normalizado del tipo
# ("ordenamiento" o "busqueda")
def normalizar_tipo(tipo):
    t = unicodedata.normalize("NFD", tipo.strip().lower())
    t = "".join(c for c in t if unicodedata.category(c) != "Mn")
    if t not in ("ordenamiento", "busqueda"):
        raise ValueError("Tipo de algoritmo no valido: " + tipo)
    return t


# Metodo de utilidad: ruta del CSV segun el tipo
def ruta_archivo(tipo):
    return os.path.join(CARPETA, normalizar_tipo(tipo) + ".csv")


# Crea la carpeta "tiempos" si no existe
def crear_carpeta():
    if not guardar:
        return
    if not os.path.isdir(CARPETA):
        os.makedirs(CARPETA)


# Crea el CSV del tipo indicado (con encabezado) si no existe
def crear_archivo(tipo):
    if not guardar:
        return
    archivo = ruta_archivo(tipo)
    if not os.path.isfile(archivo):
        with open(archivo, "w", encoding="utf-8") as f:
            f.write(ENCABEZADO + "\n")


# Agrega una fila al CSV del tipo indicado
def guardar_datos(algoritmo, tipo, longitud, tiempo):
    if not guardar:
        return
    archivo = ruta_archivo(tipo)
    with open(archivo, "a", encoding="utf-8") as f:
        f.write(f"{algoritmo},{normalizar_tipo(tipo)},{longitud},{tiempo}\n")


# Lee el CSV del tipo indicado y devuelve una lista de diccionarios
def leer_datos(tipo):
    if not guardar:
        return []
    archivo = ruta_archivo(tipo)
    if not os.path.isfile(archivo):
        return []
    datos = []
    with open(archivo, "r", encoding="utf-8") as f:
        next(f, None)
        for linea in f:
            linea = linea.strip()
            if not linea:
                continue
            algoritmo, t, longitud, tiempo = linea.split(",")
            datos.append({
                "algoritmo": algoritmo,
                "tipo": t,
                "longitud": int(longitud),
                "tiempo": int(tiempo),
            })
    return datos