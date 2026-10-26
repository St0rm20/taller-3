import random


def generar_numeros(cantidad=1_000_000, ruta_archivo="numeros.txt", digitos=8):
    min_val = 10 ** (digitos - 1)
    max_val = (10**digitos) - 1

    with open(ruta_archivo, "w", encoding="utf-8") as f:
        f.writelines(
            f"{random.randint(min_val, max_val)}\n" for _ in range(cantidad)
        )

    print("Listo")


def cargar_numeros(ruta_archivo="numeros.txt"):
    with open(ruta_archivo, "r", encoding="utf-8") as f:
        return [int(linea) for linea in f if linea.strip()]


def guardar_numeros(numeros, ruta_archivo):
    with open(ruta_archivo, "w", encoding="utf-8") as f:
        f.writelines(f"{n}\n" for n in numeros)


def obtener_ascendente(ruta_archivo="numeros.txt"):
    return sorted(cargar_numeros(ruta_archivo))


def obtener_descendente(ruta_archivo="numeros.txt"):
    return sorted(cargar_numeros(ruta_archivo), reverse=True)


def generar_ordenados(
    ruta_archivo="numeros.txt",
    ruta_asc="numeros_asc.txt",
    ruta_desc="numeros_desc.txt",
):
    """Lee numeros.txt y guarda sus versiones ascendente y descendente."""
    asc = obtener_ascendente(ruta_archivo)
    guardar_numeros(asc, ruta_asc)
    guardar_numeros(reversed(asc), ruta_desc)

    print("Listo")


if __name__ == "__main__":
    generar_numeros()
    generar_ordenados()