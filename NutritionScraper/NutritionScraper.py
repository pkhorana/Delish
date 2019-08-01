from pyvirtualdisplay import Display
from bs4 import BeautifulSoup
from selenium import webdriver
import time
import json

soup = ""

def get_serving():
    global soup
    
    serving = soup.select('span[itemprop="servingSize"]')
    for n in serving:
        return n.text.split()[0]

def get_calories():
    global soup
    
    calories = soup.select('div > span[itemprop="calories"]')[0]
    return calories.string.strip()

def get_nutrition_facts():
    global soup
    
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
    global soup
    
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

def scrape(item):
    global soup
    
    #display = Display(visible=0, size=(800, 600))
    #display.start()
    
    browser = webdriver.Firefox()  # Optional argument, if not specified will search path.
    browser.get('https://www.nutritionix.com/food/' + item);

    time.sleep(1)

    html = browser.page_source
    
    browser.quit()
    #display.stop()
    
    soup = BeautifulSoup(html, 'html.parser')
    browser.quit()

    json_dictionary = {}
    json_dictionary["serving_size"] = get_serving();
    json_dictionary["calories"] = get_calories();
    json_dictionary["nutrition"] = get_nutrition_facts()
    json_dictionary["activity"]= get_workout()

    return json.dumps(json_dictionary)


if __name__ == '__main__':
    print(scrape("chicken"))
