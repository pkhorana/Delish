import requests
from bs4 import BeautifulSoup
import sys
import json
from os import path
import time
from timeit import default_timer as timer

ingredients_body = ""
ingredients_index = 0


def get_cook_time(soup):
    prep_time_parent = soup.find_all("ul", class_ = "prepTime")[0]
        
    cook_time_object = prep_time_parent.find_all("time", attrs = {"itemprop" : "cookTime"})
    if cook_time_object:
        return cook_time_object[0]["datetime"][2:]
    else:
        return ""


def get_ready_time(soup):
    prep_time_parent = soup.find_all("ul", class_ = "prepTime")[0]
    
    prep_time_object = prep_time_parent.find_all("time", attrs = {"itemprop" : "prepTime"})
    if prep_time_object:
        return prep_time_object[0]["datetime"][2:]
    else:
        return ""


def get_total_time(soup):
    prep_time_parent = soup.find_all("ul", class_ = "prepTime")[0]
    
    prep_time_object = prep_time_parent.find_all("time", attrs = {"itemprop" : "prepTime"})
    if prep_time_object:
        return prep_time_object[0]["datetime"][2:]
    else:
        return ""


def get_direction(soup):
    directions_list = []
    
    directionsObj = soup.find_all("ol", class_ = "list-numbers recipe-directions__list")[0].find_all("li")
    
    for dirItem in directionsObj:
        directions_list.append(dirItem.find("span").string.encode('utf-8').strip())

    return directions_list


def nutritional_content(soup):
    nurtitional_content = {}
    
    nutrition_parent_object = soup.find_all("div", class_ = "nutrition-summary-facts")
    if not nutrition_parent_object:
        return nurtitional_content
    nutrition_parent = nutrition_parent_object[0].find_all("span")
        
    for tag in nutrition_parent:
        if tag.has_attr('itemprop'):
            if len(tag.contents) == 2:
                nurtitional_content[tag["itemprop"].split()[0].encode('utf-8')] = {"amount" : tag.contents[0].string.split()[0].encode('utf-8'), "unit" : tag.contents[1]["aria-label"].split()[0].encode('utf-8') }
            elif len(tag.contents) == 1:
                nurtitional_content[(tag["itemprop"].split()[0]).encode('utf-8')] = {"amount" : (tag.contents[0].string.split()[0]).encode('utf-8'), "unit" : (tag.contents[0].string.split()[1][:-1]).encode('utf-8') }

    return nurtitional_content


def get_ingredients(soup):
    global ingredients_body
    global ingredients_index
    
    ingredients_list = []
    
    try:
        listIndex = 1
        
        while True:
            itemIndex = 0
            itemObj = soup.find(id = 'lst_ingredients_' + str(listIndex)).find_all('li')
            while itemIndex < len(itemObj):
                new_ing = itemObj[itemIndex].find('span').string.encode('utf-8')
                if new_ing == "Add all ingredients to list":
                    break
                if ':' not in new_ing:
                    ingredients_list.append(ingredients_index)
                    ingredients_body = ingredients_body + new_ing + "\n"
                    ingredients_index = ingredients_index + 1
                            
                itemIndex = itemIndex + 1

                
                
            listIndex = listIndex + 1

    except AttributeError:
        pass

    return ingredients_list


def is_rating_tag(tag):
    return tag.has_attr('data-ratingstars') and tag.has_attr('class') and "rating-stars" in tag['class']


def get_basic_info(soup):
    name = soup.find(id = "recipe-main-content", attrs = {"itemprop" : "name"}).string
    rating = soup.find(is_rating_tag)['data-ratingstars']

    return name, rating

def get_image_URL(soup):
    return soup.find("img", class_ = "rec-photo")['src']

def recipe_scraper(url, tags):
    target_URL = url
    
    req = requests.get(target_URL)
    site_HTML = req.text
    
                       #urllib2.Request(target_URL)
                       #response = urllib2.urlopen(req)
                       #site_HTML = response.read()
    
    soup = BeautifulSoup(site_HTML, 'html.parser')

    return recipe_scraper2(soup, url, tags)

def recipe_scraper2(soup, url, tags):
    
    json_dictionary = {}
    json_dictionary["name"], json_dictionary["rating"] = get_basic_info(soup)
    json_dictionary["cook_time"] = get_cook_time(soup)
    json_dictionary["prep_time"] = get_ready_time(soup)
    json_dictionary["ready_in"] = get_total_time(soup)
    json_dictionary["directions"] = get_direction(soup)
    json_dictionary["nurtitional_content"] = nutritional_content(soup)
    json_dictionary["tags"] = tags
    json_dictionary["ingredients"] = get_ingredients(soup)
    json_dictionary["image_URL"] = get_image_URL(soup)

    return json_dictionary
    
    
def save_to_file(json_dictionary):
    index = 0
    while path.exists("Recipes/demoRecipe" + str(index) + ".txt"):
        index = index + 1

    f = open("Recipes/demoRecipe" + str(index) + ".txt", "w")
    f.write(json.dumps(json_dictionary))
    f.close()


def match_responses(response_list, recipe_list):
    for recipe in recipe_list:
        for i in range(len(recipe["ingredients"])):
            recipe["ingredients"][i] = response_list[recipe["ingredients"][i]]


def flush_batch():
    
    global ingredients_body
    global ingredients_index
    
    f = open("CookaloID.txt", "r")
    CookaloID = f.readline().strip()
    f.close()
    
    CookaloID = ""
    
    response = requests.post("https://akia-ai-powered-recipe-parsing-v1.p.rapidapi.com/recipe-mashape",
                            headers={
                            "X-RapidAPI-Host": "akia-ai-powered-recipe-parsing-v1.p.rapidapi.com",
                            "X-RapidAPI-Key": CookaloID,
                            "Content-Type": "text/plain",
                            "Accept": "text/plain"
                            },
                            params=(ingredients_body)
                            )
    f = open("ResponseBody.txt", "w")
    print response.text
    f.write(json.dumps(response.text))
    f.close()
    
    response_list = json.loads(json.dumps(response.text))
    match_responses(response_list, recipe_list)
    
    for recipe in recipe_list:
        save_to_file(recipe)
        
    ingredients_body = ""
    ingredients_index = 0

    print("batch done")


def retry_protocol_scraper(url, tag, count, max_count):
    try:
        return recipe_scraper(url, tag)
    except AttributeError or ValueError:
        if count < max_count:
            print(str(count + 1))
            time.sleep(5)
            return retry_protocol_scraper(url, tag, count + 1, max_count)
        else:
            raise ValueError("Could not resolve error")

def retry_protocol_scraper2(soup, url, tag, count, max_count):
    try:
        return recipe_scraper2(soup, url, tag)
    except AttributeError or ValueError:
        if count < max_count:
            print(str(count + 1))
            time.sleep(5)
            return retry_protocol_scraper(url, tag, count + 1, max_count)
        else:
            raise ValueError("Could not resolve error")

"""
if __name__ == "__main__":
    start = timer()

    count = 0
    
    recipe_list = []
    tag = []
    
    address_base = "https://www.allrecipes.com/recipe/"
    
    index = 15000
    
    while index < 1000000:
        address = address_base + str(index) + "/"
        
        req = urllib2.Request(address)
        try:
            response = urllib2.urlopen(req)
        except urllib2.HTTPError:
            print index
            index = index + 1
            continue
        site_HTML = response.read()
        soup = BeautifulSoup(site_HTML, 'html.parser')

        if len(soup.find_all("section", class_ = "error-page")) == 0:
            try:
                rec = retry_protocol_scraper2(soup, address, tag, 0, 5)
                recipe_list.append(rec)
                count = count + 1
                print str(count) + "   " + str(index)
            except ValueError:
                pass

        index = index + 1
        
        if count == 2500:
            break
        


    f = open("out_json.txt", "w")
    for rec in recipe_list:
        f.write(json.dumps(rec))
    f.close()
    
    f = open("out_body.txt", "w")
    f.write(ingredients_body)
    f.close()
    
    flush_batch()

    end = timer()
    print(end - start)


"""
if __name__ == "__main__":
    batch_size = 10
    count = 0
    f = open("input.txt", "r")
    line = f.readline()
    
    recipe_list = []
    tag = ""
    
    while line:
        if line[0] == '+':
            tag = line[1:-1].strip()
            line = f.readline()
        else:
            recipe_list.append(retry_protocol_scraper(line, tag, 0, 100))
            line = f.readline()
            count = count + 1
            print count

            if count % batch_size == 0:
                flush_batch()
        

    f.close()
    flush_batch()

    

