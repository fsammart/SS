# -*- coding: utf-8 -*-
"""SS-coloring.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1bo17lvSzFUgbMfJU1xsoOYVOiztmblLX
"""

import os
import sys
import altair as alt
import pandas as pd

from altair_saver import save

# neighbour file
file = sys.argv[1]


res = pd.read_csv(file,skiprows=[0])

import altair as alt


chart = alt.Chart(res).mark_circle().encode(
    x='ParticleX',
    y='ParticleY',
    color=alt.Color('ParticleType', type='nominal'),
).properties(
    width=600,
    height=600
)

save(chart, 'chart.png')
