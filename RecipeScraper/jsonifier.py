import urllib2
from bs4 import BeautifulSoup
import sys
import json
from os import path
from ingredient_parser.en import parse
import unirest
import time
from timeit import default_timer as timer

out = {}
final = '{"Recipe" : {'

#'{ "Recipe" : ['
for i in range(50):
    f = open("Recipes/demoRecipe" + str(i) + ".txt", "r")
    json_val = f.readline()
    f.close()

    json_loaded = json.loads(json_val)
    
    if json_loaded["tags"] in out:
        out[json_loaded["tags"]] = out[json_loaded["tags"]] + json_val + ","
    else:
        out[json_loaded["tags"]] = '"' + json_loaded["tags"] + '" : ['

for tag in out:
    final = final + out[tag][:-1] + "],"

final = final[:-1] + "}}"



out = final

f = open("Recipes/demoRecipeCompiles.txt", "w")
f.write(out)
f.close()



