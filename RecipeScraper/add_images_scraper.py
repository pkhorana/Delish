import RecipeScraper
from os import path
import json

if __name__ == "__main__":
    f = open("input.txt", "r")
    line = f.readline()
    count = 0
    recipe_list = []
    tag = ""
    
    while line:
        if line[0] == '+':
            tag = line[1:-1].strip()
            line = f.readline()
        else:
            recipe_list.append(RecipeScraper.retry_protocol_scraper(line, tag, 0, 100))
            line = f.readline()
            count = count + 1
            print count
            
            
            
    recipes_image = {}
    for rec in recipe_list:
        recipes_image[rec["name"]] = rec

    index = 0

    while path.exists("Recipes/demoRecipe" + str(index) + ".txt"):
        f = open("Recipes/demoRecipe" + str(index) + ".txt", "r")
        json_text = f.read()
        f.close()
        
        json_dictionary = json.loads(json_text)
        json_dictionary["image_URL"] = recipes_image[json_dictionary["name"]]["image_URL"]

        f = open("Recipes/demoRecipe" + str(index) + ".txt", "w")
        f.write(json.dumps(json_dictionary))
        f.close()

        index = index + 1

