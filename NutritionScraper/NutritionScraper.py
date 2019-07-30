from bs4 import BeautifulSoup
from selenium import webdriver
import time
import json

def save_to_file(json_dictionary):
    # index = 0
    # while path.exists("demoRecipe" + str(index) + ".txt"):
    #     index = index + 1

    f = open("nutrition.txt", "w")
    f.write(json.dumps(json_dictionary))
    f.close()

def get_serving():
    serving = soup.select('span[itemprop="servingSize"]')
    for n in serving:
        return n.text.split()[0]

def get_calories():
    calories = soup.select('div > span[itemprop="calories"]')[0]
    return calories.string.strip()

def get_nutrition_facts():
    nutrition_type = []
    nutrition_amt = []
    dic = {}
    nutrition = soup.select('div[class="line"] > strong')
    for n in nutrition:
        nutrition_type.append((n.string))

    nutrition2 = soup.select('div[class="line"] > span')
    for n in nutrition2:
        nutrition_amt.append(n.text.split()[0])
    for i in range(len(nutrition_type)):
        dic[nutrition_type[i]] = nutrition_amt[i]
    return dic


def get_workout():
    work_activity = []
    work_time = []
    dic = {}
    val = True

    workout = soup.select("div td")
    for work in workout:
        if work.string:
            if val:
                work_activity.append(work.string)
            else:
                work_time.append(work.string)
            val = not val

    for i in range(len(work_activity)):
        dic[work_activity[i]] = work_time[i]
    return dic

def scrape():
    browser = webdriver.Chrome()  # Optional argument, if not specified will search path.
    browser.get('https://www.nutritionix.com/food/milk');

    time.sleep(1)

    html = browser.page_source
    soup = BeautifulSoup(html, 'html.parser')

    json_dictionary = {}
    json_dictionary["serving_size"] = get_serving();
    json_dictionary["calories"] = get_calories();
    json_dictionary["nutrition"] = get_nutrition_facts()
    json_dictionary["activity"]= get_workout()

    save_to_file(json_dictionary)

    browser.quit()

