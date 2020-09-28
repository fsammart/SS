import numpy as np
import matplotlib.pyplot as plt


def plot_error(result):
    c_array = list(result.keys())
    error_array = list(result.values())
    plt.figure(1)
    plt.xticks(np.arange(min(c_array), max(c_array), 0.01))
    plt.plot(c_array, error_array)
    plt.xlabel("c")
    plt.ylabel("E(c)")
    plt.show()
    plt.savefig('E_c.png', bbox_inches='tight')


def calculate_error(points):
    x = list(points.keys())
    y = list(points.values())
    result = {}

    for c in np.arange(-0.05, 0.05, 0.000001):
        error = 0
        for index, y_item in enumerate(y):
            error += (y_item - c*x[index]) ** 2
        result[c] = error

    min_c = min(result, key=result.get)
    print("c value is ", min_c)
    return result, min_c

