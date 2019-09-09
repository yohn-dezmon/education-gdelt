from flask import Flask, abort, redirect, request, render_template
# from app import app
# # set up models in models folder (table structure)
# from models.freqused import FreqUsed
# from db import db
# python regex
# import re
import pymysql
import mpld3
import seaborn as sns
from urllib.parse import urlparse
import pandas as pd
import numpy as np
from io import BytesIO
import matplotlib.pyplot as plt
from graphs import Graph
import pdb
# from IPython.display import HTML

app = Flask(__name__)

class Database:
    def __init__(self):
        host = "127.0.0.1"
        user= "root"
        password = ""
        db = "gdelt"

        self.con = pymysql.connect(host=host, user=user, password=password, db=db,
                                cursorclass=pymysql.cursors.DictCursor)
        self.cur = self.con.cursor()

    def list_actors(self):
        self.cur.execute("SELECT Actor2Name from freqused LIMIT 50")
        result = self.cur.fetchall()

        return result

# instantiate Graph() class


@app.route('/', methods=['GET'])
def index_get():



    return render_template('index.html')

@app.route('/querydb', methods=['GET'])
def query_db_page():

    def db_query():
        db = Database()
        actors = db.list_actors()

        return actors

    res = db_query()


    return render_template('dbquery.html', query_result=res)

@app.route('/curriculum', methods=['GET'])
def curri_page():

    graph = Graph()


    CA_distplot = graph.curri_distplot("ca_avgtone_curri_topstates", "California")
    TX_distplot = graph.curri_distplot("tx_avgtone_curri_topstates", "Texas")
    MA_distplot = graph.curri_distplot("ma_avgtone_curri_topstates", "Massachusetts")
    NY_distplot = graph.curri_distplot("ny_avgtone_curri_topstates", "New York")

    CA_html_table = graph.table_generator("ca_curri_topstates", "curriculum")
    TX_html_table = graph.table_generator("tx_curri_topstates", "curriculum")
    MA_html_table = graph.table_generator("ma_curri_topstates", "curriculum")
    NY_html_table = graph.table_generator("ny_curri_topstates", "curriculum")

    return render_template('curriculum.html', CA_distplot=CA_distplot,
                            TX_distplot=TX_distplot,
                            MA_distplot=MA_distplot,
                            NY_distplot=NY_distplot,
                            CA_html_table=CA_html_table,
                            TX_html_table=TX_html_table,
                            MA_html_table=MA_html_table,
                            NY_html_table=NY_html_table
                            )

@app.route('/charter-schools', methods=['GET'])
def charter_schools_page():

    graph = Graph()

    # This line can be used to generate the avgtone/nummention line plot again if needed.
    # I left it commented out because there are a lot of data points and it takes a long time
    # to load.
    # lineplot_url = graph.charter_lineplot()
    top10_us_url = graph.buzzwords_graph("keyword_count")

    # I used the highest number of mentions from 2014-2015 to create this table
    top_20_20142015 = graph.table_generator("charter_schools_20142015", "charter-school")

    # I used the lowest number of mentions from 2014-2015 to create this table
    top_20_20152016 = graph.table_generator("charter_schools_20152016", "charter-school")

    # I used the lowest number of mentions from 2014-2015 to create this table
    top_20_2017pres = graph.table_generator("charter_schools_2017pres", "charter-school")

    return render_template('charter-schools.html',
                            top10_us_url=top10_us_url,
                            # lineplot_url=lineplot_url,
                            top_20_20142015=top_20_20142015,
                            top_20_20152016=top_20_20152016,
                            top_20_2017pres=top_20_2017pres
                            )

@app.route('/essa', methods=['GET'])
def essa_page():

    return render_template('essa.html')

@app.route('/GDELT-details', methods=['GET'])
def details_page():

    return render_template('GDELT-details.html')

@app.route('/mastery', methods=['GET'])
def mastery_page():

    return render_template('mastery.html')

@app.route('/contact', methods=['GET'])
def contact_page():

    return render_template('contact.html')


@app.route('/assessment', methods=['GET'])
def assessment_page():
    graph = Graph()
    # dtype={'AvgAvgTone': 'float'}
    assessment_count = graph.assessment_count()
    assessment_avgtone = graph.assessment_avgtone()

    # pdb.set_trace()
    maine_html_table = graph.table_generator("main_nummen_assessment", "assessment")
    student_html_table = graph.table_generator("student_nummen_assessment", "assessment")
    US_html_table = graph.table_generator("US_nummen_assessment", "assessment")
    FL_html_table = graph.table_generator("FL_nummen_assessment", "assessment")

    return render_template('assessment.html', maine_html_table=maine_html_table,
    student_html_table=student_html_table, US_html_table=US_html_table,
    FL_html_table=FL_html_table, assessment_avgtone=assessment_avgtone,
     assessment_count=assessment_count)

app.run()
