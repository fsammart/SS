import numpy as np


def calculate_sm(points, m, b):
    n = len(points)
    sy = calculate_sy(points, m, b)
    x = list(points.keys())

    return sy * np.sqrt(n / calculate_denominator(x, n))


def calculate_sb(points, m, b):
    n = len(points)
    sy = calculate_sy(points, m, b)
    x = list(points.keys())

    return sy * np.sqrt(np.sum(np.power(x, 2)) / calculate_denominator(x, n))


def calculate_denominator(x, n):
    return n * np.sum(np.power(x, 2)) - (np.sum(x) ** 2)


def calculate_sy(points, m, b):
    n = len(points)
    y_peek = [m * x + b for x in points.keys()]
    delta = []
    for index, y in enumerate(points.values()):
        delta.append((y - y_peek[index]) ** 2)

    return np.sqrt(np.sum(delta) / (n - 2))

