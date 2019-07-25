import urllib2
from bs4 import BeautifulSoup
import sys
import json
from os import path

def code_scraper(url, tags):
    target_URL = url

    req = urllib2.Request(target_URL)
    response = urllib2.urlopen(req)
    site_HTML = response.read()

    soup = BeautifulSoup(site_HTML, 'html.parser')

    name = soup.find(id = "recipe-main-content", attrs = {"itemprop" : "name"}).string

    def is_rating_tag(tag):
        return tag.has_attr('data-ratingstars') and tag.has_attr('class') and "rating-stars" in tag['class']

    rating = soup.find(is_rating_tag)['data-ratingstars']

    ingredients_list = []

    try:
        listIndex = 1
        
        while True:
            itemIndex = 0
            itemObj = soup.find(id = 'lst_ingredients_' + str(listIndex)).find_all('li')
            while itemIndex < len(itemObj):
                new_ing = itemObj[itemIndex].find('span').string
                if new_ing == "Add all ingredients to list":
                    break
                ingredients_list.append(new_ing)
                itemIndex = itemIndex + 1
            listIndex = listIndex + 1
    except AttributeError:
        pass

    cook_time = ""
    prep_time = ""
    ready_in = ""


    try:
        prep_time_parent = soup.find_all("ul", class_ = "prepTime")[0]

        try:
            cook_time = prep_time_parent.find_all("time", attrs = {"itemprop" : "cookTime"})[0]["datetime"][2:]
        except IndexError:
            pass
        try:
            prep_time = prep_time_parent.find_all("time", attrs = {"itemprop" : "prepTime"})[0]["datetime"][2:]
        except IndexError:
            pass
        try:
            ready_in = prep_time_parent.find_all("time", attrs = {"itemprop" : "totalTime"})[0]["datetime"][2:]
        except IndexError:
            pass
    except IndexError:
        pass

    directions_list = []

    try:
        directionsObj = soup.find_all("ol", class_ = "list-numbers recipe-directions__list")[0].find_all("li")

        for dirItem in directionsObj:
            directions_list.append(dirItem.find("span").string.strip())

    except IndexError:
        pass


    nurtitional_content = {}

    try:
        nutrition_parent = soup.find_all("div", class_ = "nutrition-summary-facts")[0].find_all("span")

        for tag in nutrition_parent:
            if tag.has_attr('itemprop'):
                if len(tag.contents) == 2:
                    nurtitional_content[str(tag["itemprop"].split()[0])] = {"amount" : str(tag.contents[0].string.split()[0]), "unit" : str(tag.contents[1]["aria-label"].split()[0]) }
                elif len(tag.contents) == 1:
                    nurtitional_content[str(tag["itemprop"].split()[0])] = {"amount" : str(tag.contents[0].string.split()[0]), "unit" : str(tag.contents[0].string.split()[1][:-1]) }
    except IndexError:
        pass


    json_dictionary = {}
    json_dictionary["name"] = name
    json_dictionary["rating"] = rating
    json_dictionary["ingredients"] = ingredients_list
    json_dictionary["directions"] = directions_list
    json_dictionary["prep_time"] = prep_time
    json_dictionary["cook_time"] = cook_time
    json_dictionary["ready_in"] = ready_in
    json_dictionary["nurtitional_content"] = nurtitional_content


    index = 0
    while path.exists("demoRecipe" + str(index) + ".txt"):
        index = index + 1

    f = open("demoRecipe" + str(index) + ".txt", "w")
    f.write(json.dumps(json_dictionary))
    f.close()

if __name__ == "__main__":
    count = 0
    f = open("input.txt", "r")
    line = f.readline()
    while line:
        try:
            code_scraper(line, [sys.argv[1]])
        except AttributeError:
            print str(count) + " --- Attribute Error"
        except ValueError:
            print str(count) + " --- Value Error"
        finally:
            line = f.readline()
            count = count + 1

    f.close()

