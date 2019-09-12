from app import app
from db import db
from models import freqused
import routes
import time
from datetime import datetime

""" leaving this here for reference, not going to use this code for now """

with app.app_context():
    # creates the tables defined in models
    db.create_all()

app.run()
