""" leaving this here for reference, not going to use this code for now """
from flask import Flask

app = Flask(__name__)

# tells SQLAlchemy how to connect to our DB
# pword goes between root: and @ sign
db_uri = 'mysql+pymysql://root:@localhost/gdelt'

# the flask sqlalchemy module will pull this db URI string so that it
# knows how to connect
app.config['SQLALCHEMY_DATABASE_URI'] = db_uri
