## GDELT Data Pipeline to Analyze Media Coverage of Education

## Purpose

The purpose of this project is to analyze the media coverage of educational topics within   the United States. I utilized the [GDELT dataset](https://www.gdeltproject.org/data.html)  
to look for keywords like curriculum, assessment, and charter schools in order  
to characterize their portrayal within the United States' media.  

## Table of Contents
1. [Purpose](README.md#purpose)
2. [Data Ingestion](README.md#data-ingestion)



## Data Ingestion
Using Google Cloud Platform, I set up a project within Google Big Query and began  
exploring the GDELT dataset. I used the query below to filter the gdeltv2-events table  
for educational events on 7/14/2019:  

```
SELECT GLOBALEVENTID,
SQLDATE,
MonthYear,
Year,
FractionDate,
Actor1Code,
Actor1Name,
Actor1CountryCode,
Actor1KnownGroupCode,
Actor1Religion1Code,
Actor1Type1Code,
Actor2Type2Code,
Actor1Type3Code,
Actor2Code,
Actor2Name,
IsRootEvent,
EventCode,
EventBaseCode,
EventRootCode,
QuadClass,
NumMentions,
AvgTone,
Actor1Geo_Type,
Actor1Geo_FullName,
ActionGeo_Type,
ActionGeo_FullName,
ActionGeo_CountryCode,
ActionGeo_ADM1Code,
ActionGeo_Lat,
ActionGeo_Long,
DATEADDED,
SOURCEURL FROM `gdelt-bq.gdeltv2.events` WHERE Actor1Code LIKE "%EDU%" or Actor2Code LIKE "%EDU%"
```

The data was originally queried/filtered using Google Big Query, then exported to   
Google Cloud Storage where I downloaded individual .gz files. After extracting the .gz files to  
.csv files, I used the Java Spark code within this repository to aggregate  
the separate .csv files into one batch of parquet files which I then imported into Spark once  
more to normalize and order the data further, and to place into MySQL for storage and analysis.  

I am currently in the process of analyzing and visualizing the data using MySQL, Hive, and the  
matplotlib, pandas, and seaborn libraries in Python.
