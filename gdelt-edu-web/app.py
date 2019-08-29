from flask import Flask, abort, redirect, request, render_template
# from app import app
# # set up models in models folder (table structure)
# from models.freqused import FreqUsed
# from db import db
# python regex
# import re
import pymysql

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

@app.route('/', methods=['GET'])
def index_get():
    # since FreqUsed is defined as FreqUsed(db.Model) it has a .query attribute
    hellow = "hello world"


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

    return render_template('curriculum.html')

@app.route('/essa', methods=['GET'])
def essa_page():
    #essa = Every Student Succeeds Act
    return render_template('essa.html')

app.run()
