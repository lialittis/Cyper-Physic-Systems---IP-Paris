import numpy as np
import matplotlib.pyplot as plt


plt.figure(1) # 生成第一个图，且当前要处理的图为fig.1

x = np.arange(1, 11, 1)


y1 = np.array([0.254433,0.113838,0.223971,0.357934,0.130038,0.525401,0.292656,0.494793,0.285851,0.244689])
y2 = np.array([0.096471, 0.243770, 0.065051, 0.058581, 0.312519, 0.044341, 0.054919, 0.058273, 0.048011, 0.056001])
y3 = np.array([0.053252, 0.054721, 0.060486, 0.040744, 0.057799, 0.054689, 0.038617, 0.058594, 0.032372, 0.040173])
y4 = np.array([0.051830, 0.052003, 0.051989, 0.059729, 0.054592, 0.058228, 0.059056, 0.060191, 0.043629, 0.059680])

plt.plot(x, y1, linestyle="-", linewidth=2, label = '30%')

plt.plot(x, y2, color="b", linestyle="-", linewidth=2, label = '50%')

plt.plot(x, y3, color="g", linestyle="-", linewidth=2, label = '70%')

plt.plot(x, y4, color="r",  linestyle="-", linewidth=2, label = '80%')

plt.xlabel("iterate times")
plt.ylabel("mean pint corresp. error")

plt.legend(loc='upper right')

plt.show()
