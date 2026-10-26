import bisect
import itertools
import os
import sys
import time

# Agrega la raiz del proyecto (para importar util) y punto2 (para importar Algoritmo)
RAIZ = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
sys.path.insert(0, RAIZ)
sys.path.insert(0, os.path.join(RAIZ, "punto2"))

import Algoritmo
import claves as modulo_claves
import salida
from util import txtmanager

# Interpolation search es recursiva; se sube el limite por seguridad
sys.setrecursionlimit(20_000)

# Pruebas del punto 2 (busqueda):
#   A) tiempo total de buscar 1.000 claves (500 presentes, 500 ausentes) y promedio por busqueda
#   B) tiempo total de buscar 1.000 veces una clave al inicio, al centro y al final
# Uso (desde la raiz): python punto2/pruebas/main.py [claves|posicion]  (sin argumento hace ambas)

TAMANOS = [1_000_000, 100_000, 10_000, 1_000]
REPETICIONES = 5
BUSQUEDAS_POSICION = 1_000

hacer_claves = True
hacer_posicion = True


# --------------------- Metodos auxiliares ---------------------------

# Dice si el resultado de la busqueda es correcto.
# Ternary devuelve True/False; los demas devuelven un indice (o -1).
# Ojo: jump search puede devolver el indice como float, por eso el int().
def es_correcto(arr, resultado, x):
    i = bisect.bisect_left(arr, x)
    presente = i < len(arr) and arr[i] == x
    if isinstance(resultado, bool):
        return resultado == presente
    if not presente:
        return resultado == -1
    return resultado >= 0 and arr[int(resultado)] == x


# Mide (ns) buscar todas las claves: un solo par de lecturas del reloj
def medir_claves(busqueda, arr, claves):
    tiempo_inicial = time.perf_counter_ns()
    for x in claves:
        busqueda(arr, x)
    tiempo_final = time.perf_counter_ns()
    return tiempo_final - tiempo_inicial


# Mide (ns) buscar la misma clave "veces" veces seguidas
def medir_repetida(busqueda, arr, x, veces):
    repeticiones = itertools.repeat(None, veces)
    tiempo_inicial = time.perf_counter_ns()
    for _ in repeticiones:
        busqueda(arr, x)
    tiempo_final = time.perf_counter_ns()
    return tiempo_final - tiempo_inicial


# Fuera del tiempo medido: repite cada busqueda una vez y la valida
def validar(busqueda, arr, claves):
    for x in claves:
        resultado = busqueda(arr, x)
        if not es_correcto(arr, resultado, x):
            print("------------------ Error ----------------------------")
            print("Resultado incorrecto para la clave", x, ":", resultado)
            return False
    return True


# --------------------- Pruebas ---------------------------

# Prueba A: conjunto de 1.000 claves
def prueba_claves(nombre, arr, busqueda):
    n = len(arr)
    claves = modulo_claves.cargar_o_generar(arr)
    print("Prueba claves (", len(claves), "claves )")

    for rep in range(1, REPETICIONES + 1):
        total = medir_claves(busqueda, arr, claves)
        if validar(busqueda, arr, claves):
            promedio = total / len(claves)
            print("Total:", total, " Promedio:", promedio)
            salida.registrar_claves(nombre, n, rep, total, promedio)


# Prueba B: la misma clave 1.000 veces, al inicio, al centro y al final
def prueba_posicion(nombre, arr, busqueda):
    n = len(arr)
    posiciones = [("inicio", 0), ("centro", n // 2), ("final", n - 1)]

    for etiqueta, indice in posiciones:
        x = arr[indice]
        print("Prueba posicion", etiqueta, "(clave", x, ")")

        for rep in range(1, REPETICIONES + 1):
            total = medir_repetida(busqueda, arr, x, BUSQUEDAS_POSICION)
            if validar(busqueda, arr, [x]):
                promedio = total / BUSQUEDAS_POSICION
                print("Total:", total, " Promedio:", promedio)
                salida.registrar_posicion(nombre, n, etiqueta, rep, total, promedio)


# Corre un algoritmo para todos los tamanos
def probar(nombre, arr, busqueda):
    print("Algoritmo", nombre)

    for n in TAMANOS:
        print("Longitud", n)
        # Primeros n elementos del ascendente: sigue ordenado
        parcial = arr[:n]
        if hacer_claves:
            prueba_claves(nombre, parcial, busqueda)
        if hacer_posicion:
            prueba_posicion(nombre, parcial, busqueda)


# --------------------- Un metodo por algoritmo ---------------------------

def linear_search(arr):
    probar("Linear search", arr, Algoritmo.search)


def binary_search(arr):
    probar("Binary search", arr, Algoritmo.binarySearch)


def ternary_search(arr):
    probar("Ternary search", arr, Algoritmo.ternarySearch)


def jump_search(arr):
    probar("Jump search", arr,
           lambda a, x: Algoritmo.jumpSearch(a, x, len(a)))


def interpolation_search(arr):
    probar("Interpolation search", arr,
           lambda a, x: Algoritmo.interpolationSearch(a, 0, len(a) - 1, x))


def exponential_search(arr):
    probar("Exponential search", arr, Algoritmo.exponential_search)


# --------------------- Main ---------------------------

def main():
    global hacer_claves, hacer_posicion
    if len(sys.argv) > 1:
        if sys.argv[1] == "claves":
            hacer_posicion = False
        elif sys.argv[1] == "posicion":
            hacer_claves = False
        else:
            sys.exit("Argumento no valido: " + sys.argv[1] + " (usa claves|posicion)")

    arr = txtmanager.cargar_numeros("numeros_asc.txt")
    print("Longitud del arreglo extraido:", len(arr))

    linear_search(arr)
    binary_search(arr)
    ternary_search(arr)
    jump_search(arr)
    interpolation_search(arr)
    exponential_search(arr)


if __name__ == "__main__":
    main()
