from db import db
import datetime
import re
""" leaving this here for reference, not going to use this code for now """

class FreqUsed(db.Model):
    # idk if I can set this as PK since it isn't in the DB
    GLOBALEVENTID = db.Column(db.Integer, primary_key=True)
    Year = db.Column(db.Integer, nullable=True)
    Date = db.Column(db.BigInteger, nullable=True)
    DateAdded = db.Column(db.BigInteger, nullable=True)
    Actor1Code = db.Column(db.String(21), nullable=True)
    Actor1Name = db.Column(db.String(120), nullable=True)
    Actor2Code = db.Column(db.String(21), nullable=True)
    Actor2Name = db.Column(db.String(120), nullable=True)
    IsRootEvent = db.Column(db.Integer, nullable=True)
    QuadClass = db.Column(db.Integer, nullable=True)
    NumMentions = db.Column(db.Integer, nullable=True)
    AvgTone = db.Column(db.Float, nullable=True)
    ActionGeo_FullName = db.Column(db.String(500), nullable=True)
    ActionGeo_CountryCode = db.Column(db.String(8), nullable=True)
    State = db.Column(db.String(10), nullable=True)
    SOURCEURL = db.Column(db.String(240), nullable=True)

""" GLOBALEVENTID, Year, Date, DateAdded, Actor1Code, Actor1Name, Actor2Code, Actor2Name,
IsRootEvent, substr(EventCode) as EventCode,  QuadClass, NumMentions, AvgTone,
ActionGeo_FullName, ActionGeo_CountryCode, substr2(ActionGeo_ADM1Code) as State,
SOURCEURL from gdeltedu ORDER BY DateAdded"""

# Still need to add lessfreqused table




# (Actor2Code,StringType)
# (Actor2Name,StringType)
# (IsRootEvent,IntegerType)
# (EventCode,StringType)
# (EventBaseCode,StringType)
# (EventRootCode,StringType)
# (QuadClass,IntegerType)
# (NumMentions,IntegerType)
# (AvgTone,DoubleType)
# (Actor1Geo_Type,IntegerType)
# (Actor1Geo_FullName,StringType)
# (ActionGeo_Type,IntegerType)
# (ActionGeo_FullName,StringType)
# (ActionGeo_CountryCode,StringType)
# (ActionGeo_ADM1Code,StringType)
# (ActionGeo_Lat,DoubleType)
# (ActionGeo_Long,DoubleType)
# (SOURCEURL,StringType)
# (Actor1KnownGroupCode,StringType)
# (Actor1Religion1Code,StringType)
# (Actor1Type1Code,StringType)
# (Actor2Type2Code,StringType)
# (Actor1Type3Code,StringType)
