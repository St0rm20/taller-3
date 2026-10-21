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


if __name__ == "__main__":
    generar_numeros()