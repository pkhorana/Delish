from flask import Flask, request, jsonify, render_template, send_from_directory, send_file
import os
from  NutritionScraper import scrape as NutritionScrape
import json


application = Flask(__name__)

def preprocess_item(item):
    return item

@application.route('/nutrition_facts', methods=['POST'])
def get_nutrition_facts():

    item_info = request.json;
    item_name = item_info["item_name"]
    out = NutritionScrape(preprocess_item(item_name))
    return jsonify(out)

if __name__ == '__main__':
    application.run(host='0.0.0.0',debug=True)
