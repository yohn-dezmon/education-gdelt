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
    # since FreqUsed is defined as FreqUsed(db.Model) it has a .query attribute
    hellow = "hello world"

    sns.set(style='whitegrid')

    df = pd.read_csv('/media/sf_sharedwithVM/MySQL/keyword_count.csv',
                            sep=',',
                            names=['Keyword','Count']
                            )
    df_sorted = df.sort_values(by=['Count'], ascending=False).head(10)
    df_sorted2 = df.sort_values(by=['Count'], ascending=False).tail(11)

    # default = 6.4 (width?), 4.8 (height)
    ax = sns.barplot(x='Keyword',y='Count', data=df_sorted, palette='spring')
    ax.set_xticklabels(ax.get_xticklabels(), rotation=20, ha="right", fontsize=10)
    plt.xlabel('Keyword', fontsize=12)
    # ok I think the ordering of tight_layout() -> subplots_adjust is important!

    y = df_sorted['Count']
    plt.yticks(np.arange(0, 60000, 5000))
    plt.ylabel('Count', fontsize=12)
    plt.tight_layout()
    plt.subplots_adjust(top=0.9)
    plt.title('Educational Buzzwords in US Media Top 10')
    # figsize=(4.0,3.8)
    plt.figure(1)
    url = 'static/buzzwords-top10.png'
    # f = BytesIO()
    plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/buzzwords-top10.png')

    plt.figure(2)
    ax = sns.barplot(x='Keyword',y='Count', data=df_sorted2, palette='spring')
    ax.set_xticklabels(ax.get_xticklabels(), rotation=20, ha="right", fontsize=10)
    plt.xlabel('Keyword', fontsize=12)
    plt.ylabel('Count', fontsize=12)
    plt.tight_layout()
    plt.subplots_adjust(top=0.9)
    plt.title('Educational Buzzwords in US Media (11-21)')
    url2 = 'static/buzzwords-11-21.png'
    plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/buzzwords-11-21.png')

    return render_template('index.html', url=url, url2=url2)

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
    pd.set_option('display.max_colwidth', -1)

    df = pd.read_csv('/media/sf_sharedwithVM/MySQL/CA_curri_URL.csv',
                        sep=',',
                        header=0,
                        error_bad_lines=False,
                        warn_bad_lines=True,
                        )
    path_col = [urlparse(row).path for row in df['SOURCEURL']]
    df['URL Path'] = path_col

    # def make_clickable(val):
    #     return '<a href="{}">{}</a>'.format(val,val)
    #
    # df.style.format({'SOURCEURL':  make_clickable})
    # this needs IPython maybe? classes='table table-striped table-hover'
    html_table = df.to_html(index=False)
    titles = ['URL','URL Path']
    # pd.set_option('display.max_colwidth', 50)




    return render_template('curriculum.html', table=html_table, titles=titles)

@app.route('/essa', methods=['GET'])
def essa_page():

    return render_template('essa.html')

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
    maine_html_table = graph.assessment_tables("main_nummen_assessment")
    student_html_table = graph.assessment_tables("student_nummen_assessment")
    US_html_table = graph.assessment_tables("US_nummen_assessment")
    FL_html_table = graph.assessment_tables("FL_nummen_assessment")

    return render_template('assessment.html', maine_html_table=maine_html_table,
    student_html_table=student_html_table, US_html_table=US_html_table,
    FL_html_table=FL_html_table, assessment_avgtone=assessment_avgtone,
     assessment_count=assessment_count)

app.run()
