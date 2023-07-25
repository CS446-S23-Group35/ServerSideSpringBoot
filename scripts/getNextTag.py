from sys import argv
from json import JSONDecoder

arg = "".join(argv[1:])

max = 0

decoder = JSONDecoder()
while arg:
    obj, idx = decoder.raw_decode(arg)
    arg = arg[idx:]
    tag = obj["Tag"]
    if tag != "latest" and tag != "<none>":
        if int(tag) > max:
            max = int(tag)
print(max + 1)