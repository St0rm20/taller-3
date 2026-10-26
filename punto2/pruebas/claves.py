import bisect
import os
import random

from util import txtmanager

# Conjunto de 1.000 claves por tamano: 500 presentes y 500 ausentes.
# Se genera una sola vez y se guarda en punto2/pruebas/claves_<n>.txt;
# si el archivo ya existe se carga (Java y Python comparten el mismo archivo).

CARPETA = os.path.dirname(os.path.abspath(__file__))
SEMILLA = 42
PRESENTES = 500
AUSENTES_MENORES = 50
AUSENTES_MAYORES = 50
AUSENTES_EN_RANGO = 400
TOTAL = PRESENTES + AUSENTES_MENORES + AUSENTES_MAYORES + AUSENTES_EN_RANGO


# arr: arreglo ordenado de tamano n (los primeros n elementos del ascendente)
def cargar_o_generar(arr):
    ruta = os.path.join(CARPETA, f"claves_{len(arr)}.txt")
    if os.path.isfile(ruta):
        claves = txtmanager.cargar_numeros(ruta)
        if len(claves) == TOTAL:
            return claves
        print(f"El archivo {ruta} no tiene {TOTAL} claves; se regenera")
    claves = generar(arr)
    guardar(ruta, claves)
    print("Claves generadas en", ruta)
    return claves


def generar(arr):
    n = len(arr)
    azar = random.Random(SEMILLA + n)

    # Presentes: valores distintos elegidos al azar del arreglo
    presentes = {}
    while len(presentes) < PRESENTES:
        presentes[arr[azar.randrange(n)]] = None

    # Ausentes: algunos menores que el minimo, algunos mayores que el maximo
    # y el resto dentro del rango pero que no pertenezcan al arreglo
    minimo, maximo = arr[0], arr[-1]
    ausentes = {}
    while len(ausentes) < AUSENTES_MENORES:
        ausentes[azar.randint(1, minimo - 1)] = None
    tope = len(ausentes) + AUSENTES_MAYORES
    while len(ausentes) < tope:
        ausentes[maximo + 1 + azar.randrange(1_000_000)] = None
    tope = len(ausentes) + AUSENTES_EN_RANGO
    while len(ausentes) < tope:
        x = azar.randint(minimo, maximo)
        i = bisect.bisect_left(arr, x)
        if i == n or arr[i] != x:
            ausentes[x] = None

    # Se mezclan para que presentes y ausentes queden intercalados
    claves = list(presentes) + list(ausentes)
    azar.shuffle(claves)
    return claves


def guardar(ruta, claves):
    with open(ruta, "w", encoding="utf-8") as f:
        f.writelines(f"{c}\n" for c in claves)
