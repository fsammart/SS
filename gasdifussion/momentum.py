from collections import OrderedDict, defaultdict
import matplotlib.pyplot as plt
from linear_regression import *
from plot_error import *

particles_position = defaultdict(lambda: defaultdict(tuple))

particles_to_watch = []

ID_INDEX = 0
X_INDEX = 1
Y_INDEX = 2

epsilon = 0.001
first_position = defaultdict(tuple)

with open('results/dynamicStep/momentum3') as file:

    lines = file.readlines()
    time = 0

    for line in lines[1:100]:
        values = line.split("\t")
        if len(values) == 1:
            continue

        if values[0] == "-1":
            continue
        if len(values) != 6:
            continue
        x = float(values[X_INDEX])
        y = float(values[Y_INDEX])
        id = int(values[ID_INDEX])

        if (x - 0.12) ** 2 + (y - 0.045) ** 2 <= 0.02 ** 2:
            particles_to_watch.append(id)

    for line in lines:
        values = line.split("\t")
        if len(line.split("\t")) == 1:
            time = float(line)
            continue

        if values[0] == "-1":
            continue

        id = int(values[ID_INDEX])

        if id in particles_to_watch:
            x = float(values[X_INDEX])
            y = float(values[Y_INDEX])

            if first_position[id]:
                particles_position[int(time)][id] = ((x - first_position[id][0]) ** 2, (y - first_position[id][1]) ** 2)
            else:
                particles_position[int(time)][id] = (0, 0)
                first_position[id] = (x, y)

particles_deviations = {}
particles_averages = {}
print(len(particles_to_watch))
for time, positions in particles_position.items():
    sum = []

    for position in positions.values():
        sum.append(position[0] + position[1])

    if len(sum) > int(0.85* len(particles_to_watch)):
        print(len(sum))
        particles_averages[time - 18] = np.average(sum)
        particles_deviations[time - 18] = np.std(sum)


ordered_dict = OrderedDict(sorted(particles_averages.items()))
ordered_deviations = OrderedDict(sorted(particles_deviations.items()))

x = list(ordered_dict.keys())
y = list(ordered_dict.values())

errors, min_c = calculate_error(ordered_dict)

plt.figure(0)
plt.errorbar(x, y, fmt=".", yerr=ordered_deviations.values())
plt.plot(x, np.dot(min_c, x), 'r')
plt.ylim(0, 0.0011)
plt.xticks(np.arange(min(x), max(x) + 1, 1))
plt.xlabel("Tiempo [s]")
plt.ylabel("DCM")
plt.show()

plot_error(errors)
# plt.show()
plt.savefig('DCM_time.png', bbox_inches='tight')

# slope, intercept, r_value, p_value, std_err = stats.linregress(x, y)
# print("m", slope)
# print("b", intercept)
# print("Sm", calculate_sm(ordered_dict, slope, intercept))
# print("Sb", calculate_sb(ordered_dict, slope, intercept))