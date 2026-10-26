import os

from util import csv as csv_util

# Escribe los CSV de las pruebas de busqueda (sin tocar util/csv.py).
# Respeta la bandera csv.guardar: si es False no se escribe nada.

CARPETA = os.path.join("tiempos", "python")
ARCHIVO_CLAVES = "busqueda_claves.csv"
ARCHIVO_POSICION = "busqueda_posicion.csv"
ENCABEZADO_CLAVES = "algoritmo,longitud,repeticion,tiempo_total_ns,tiempo_promedio_ns"
ENCABEZADO_POSICION = "algoritmo,longitud,posicion,repeticion,tiempo_total_ns,tiempo_promedio_ns"


def registrar_claves(algoritmo, longitud, repeticion, total, promedio):
    fila = f"{algoritmo},{longitud},{repeticion},{total},{promedio:.3f}"
    agregar(ARCHIVO_CLAVES, ENCABEZADO_CLAVES, fila)


def registrar_posicion(algoritmo, longitud, posicion, repeticion, total, promedio):
    fila = f"{algoritmo},{longitud},{posicion},{repeticion},{total},{promedio:.3f}"
    agregar(ARCHIVO_POSICION, ENCABEZADO_POSICION, fila)


# Agrega una fila al archivo; crea la carpeta y el encabezado si hacen falta
def agregar(nombre_archivo, encabezado, fila):
    if not csv_util.guardar:
        return
    os.makedirs(CARPETA, exist_ok=True)
    archivo = os.path.join(CARPETA, nombre_archivo)
    if not os.path.isfile(archivo):
        with open(archivo, "w", encoding="utf-8") as f:
            f.write(encabezado + "\n")
    with open(archivo, "a", encoding="utf-8") as f:
        f.write(fila + "\n")
