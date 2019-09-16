import os
for x in os.listdir('.'):
    y = x.rpartition('.')[0]
    print(f'<img src="docs/screenshots/{x}" alt="{y}" width="200"/>')
