import os
import sys
import time

# Agrega la raiz del proyecto (taller-3) para poder importar util
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

import Algoritmo
from util import csv, txtmanager

# Interpolation search es recursiva; se sube el limite por seguridad
sys.setrecursionlimit(20_000)

TAMANOS = [1_000_000, 100_000, 10_000, 1_000]
REPETICIONES = 5


# --------------------- Metodos auxiliares ---------------------------

# Dice si el resultado de la busqueda es correcto.
# Ternary devuelve True/False; los demas devuelven un indice (o -1).
# Ojo: jump search puede devolver el indice como float, por eso el int().
def es_correcto(arr, resultado, x):
    if isinstance(resultado, bool):
        return resultado
    return resultado >= 0 and arr[int(resultado)] == x


# Mide el tiempo (ns) que tarda una busqueda (solo la busqueda, sin validar)
def ejecutar(busqueda, arr, x):
    tiempo_inicial = time.perf_counter_ns()
    resultado = busqueda(arr, x)
    tiempo_final = time.perf_counter_ns()
    print("Tiempo:", tiempo_final - tiempo_inicial)
    return tiempo_final - tiempo_inicial, resultado


# Corre un algoritmo para todos los tamanos, REPETICIONES veces cada uno
def probar(nombre, arr, busqueda):
    print("Algoritmo", nombre)

    for n in TAMANOS:
        print("Longitud", n)

        # La busqueda no modifica el arreglo: se prepara una sola vez por tamano.
        # Se ordena porque binaria, ternaria, jump, interpolation y exponential lo requieren.
        ordenado = sorted(arr[:n])

        # Se busca el mayor elemento (peor caso para la lineal y para las demas)
        x = ordenado[-1]

        for _ in range(REPETICIONES):
            res, resultado = ejecutar(busqueda, ordenado, x)
            if es_correcto(ordenado, resultado, x):
                print("encontrado")
                csv.registrar_tiempo(nombre, "busqueda", n, res)
            else:
                print("------------------ Error ----------------------------")
                print("No se encontro el elemento")


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
    arr = txtmanager.cargar_numeros()
    print("Longitud del arreglo extraido:", len(arr))

    #linear_search(arr)
    #binary_search(arr)
    #ternary_search(arr)
    #jump_search(arr)
    interpolation_search(arr)
    exponential_search(arr)


if __name__ == "__main__":
    main()