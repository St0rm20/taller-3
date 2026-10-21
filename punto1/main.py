import os
import sys
import time

# Agrega la raiz del proyecto (taller-3) para poder importar util
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

import Algoritmo
from util import csv, txtmanager

# Dual pivot y merge sort son recursivos; se sube el limite por seguridad
sys.setrecursionlimit(20_000)

TAMANOS = [1_000_000, 100_000, 10_000, 1_000]
REPETICIONES = 5

# Insertion sort es O(n^2) y en Python 1.000.000 tardaria horas por corrida,
# asi que por defecto se omite ese tamano. Agrega 1_000_000 si de verdad lo quieres.
TAMANOS_INSERTION = [10_000, 1_000]


# --------------------- Metodos auxiliares ---------------------------

def esta_ordenado(arreglo):
    for i in range(1, len(arreglo)):
        if arreglo[i] < arreglo[i - 1]:
            print("------------------ Error ----------------------------")
            print("El arreglo no esta ordenado")
            return False
    return True


# Mide el tiempo (ns) que tarda un algoritmo en ordenar el arreglo recibido
def ejecutar(algoritmo, arr):
    tiempo_inicial = time.perf_counter_ns()
    algoritmo(arr)
    tiempo_final = time.perf_counter_ns()
    print("Tiempo:", tiempo_final - tiempo_inicial)
    return tiempo_final - tiempo_inicial


# Corre un algoritmo para todos los tamanos, REPETICIONES veces cada uno
def probar(nombre, arr, algoritmo, tamanos=TAMANOS):
    print("Algoritmo", nombre)

    for n in tamanos:
        print("Longitud", n)
        for _ in range(REPETICIONES):
            arr_copy = arr[:n]
            res = ejecutar(algoritmo, arr_copy)
            if esta_ordenado(arr_copy):
                print("ordenado")
                csv.registrar_tiempo(nombre, "ordenamiento", n, res)


# --------------------- Un metodo por algoritmo ---------------------------

def insertion_sort(arr):
    probar("Insertion sort", arr, lambda a: Algoritmo.insertionSort(a),
           TAMANOS_INSERTION)


def dual_pivot_quicksort(arr):
    probar("Dual pivot quicksort", arr,
           lambda a: Algoritmo.dualPivotQuickSort(a, 0, len(a) - 1))


def merge_sort(arr):
    probar("Merge sort", arr,
           lambda a: Algoritmo.mergeSort(a, 0, len(a) - 1))


def heap_sort(arr):
    probar("Heap sort", arr, lambda a: Algoritmo.heapSort(a))


def radix_sort(arr):
    probar("Radix sort", arr, lambda a: Algoritmo.radixSort(a))


def bucket_sort(arr):
    probar("Bucket sort", arr, lambda a: Algoritmo.bucket_sort(a))


# --------------------- Main ---------------------------

def main():
    arr = txtmanager.cargar_numeros()
    print("Longitud del arreglo extraido:", len(arr))

    insertion_sort(arr)
    dual_pivot_quicksort(arr)
    merge_sort(arr)
    heap_sort(arr)
    radix_sort(arr)
    bucket_sort(arr)


if __name__ == "__main__":
    main()