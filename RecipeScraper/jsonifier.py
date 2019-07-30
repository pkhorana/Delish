import urllib2
from bs4 import BeautifulSoup
import sys
import json
from os import path
from ingredient_parser.en import parse
import unirest
import time
from timeit import default_timer as timer

out = {"Recipes" : {}}

for i in range(50):
    f = open("Recipes/demoRecipe" + str(i) + ".txt", "r")
    json_val = f.readline()
    f.close()

    json_loaded = json.loads(json_val)
    
    if json_loaded["tags"] in out["Recipes"]:
        out["Recipes"][json_loaded["tags"]].append(json_loaded)
    else:
        out["Recipes"][json_loaded["tags"]] = [json_loaded]
    





out = json.dumps(out)

f = open("Recipes/demoRecipeCompiles.json", "w")
f.write(out)
f.close()



