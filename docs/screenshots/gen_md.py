import os
for x in os.listdir('.'):
    y = x.rpartition('.')[0]
    print(f'![{y}](docs/screenshots/{x}?raw=true "{y}" =250x)')
