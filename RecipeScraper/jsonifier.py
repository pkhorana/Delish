import json
from os import path


def unroll(ing):
    unr = []
    for key in ing:
        unr.append(ing[key])

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

for tag in out["Recipes"]:
    for rec in out["Recipes"][tag]:
        newIngs = {}
        for i in range(len(rec["ingredients"])):
            if "name" in rec["ingredients"][i]:
                newIngs[rec["ingredients"][i]["name"]] = rec["ingredients"][i]
            else:
                newIngs[rec["ingredients"][i]["input"]] = rec["ingredients"][i]

        rec["ingredients"][i] = newIngs

out = json.dumps(out)

f = open("Recipes/demoRecipeCompiles.json", "w")
f.write(out)
f.close()


