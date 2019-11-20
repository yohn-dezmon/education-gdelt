## GDELT Data Pipeline to Analyze Media Coverage of Education

## Purpose

The purpose of this project is to analyze the media coverage of educational topics within the United States. I utilized the [GDELT dataset](https://www.gdeltproject.org/data.html) to look for keywords like curriculum, assessment, and charter schools in order to characterize their portrayal within the United States' media. Some of the questions I was interested in answering include: which states have the greatest media coverage of educational topics, how are laws regarding education received by the public, and which topics within education are covered most by US media sources.  


## Table of Contents
1. [Purpose](README.md#purpose)  
2. [Data Ingestion](README.md#data-ingestion)  
3. [Data Processing and Storage](README.md#data-processing-and-storage)  
4. [Data Analysis and Web UI](README.md#data-analysis-and-web-ui)  


## Data Ingestion  

I began the project by experimenting with the GDELT dataset in AWS and Google Cloud   Services, and ultimately decided to use Google Cloud Services. I set up a project within Google Big Query and began exploring the GDELT dataset. I chose BigQuery/GCS over Athena/S3 because I found the interface easier to use and I was  able to perform queries faster. Also the data I was processing was within Google's free tier whereas the queries I was running in Athena were not considered part of Amazon's free tier. I used the query below to filter the gdeltv2-events table for educational events on 7/14/2019:  

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

I created a table from the query above, then exported the table to Google Cloud Storage where I downloaded individual .gz files to my computer. I decided to manually download the data into multiple files because the services provided for batch and streaming data processing (Cloud Dataflow/GCE) were not included in the free tier offered by Google.

## Data Processing and Storage

After extracting the .gz files to .csv files, I used [a Spark job written in Java](https://github.com/yohn-dezmon/education-gdelt/blob/master/src/main/java/jdes/gdeltedu/DFIForSpark2.java) within this repository to aggregate the separate .csv files into one batch of parquet files which I then imported into [another Spark job](https://github.com/yohn-dezmon/education-gdelt/blob/master/src/main/java/jdes/gdeltedu/FullDataOutput.java) to normalize and order the data further, and to place first into MySQL and then Hive, using Scoop, for  storage and analysis. The data was separated into two tables within both MySQL and   Hive,  linked by the GLOBALEVENTID field, one table consisting of fields that I determined I would use frequently and another table consisting of fields which would help to enhance the data I frequently queried.  
Frequently Used Fields:  
```
Dataset<Row> gdeltFreqUsed = inputdf.sqlContext().sql("Select "
				+ " GLOBALEVENTID, Year, Date, DateAdded, " +
				"Actor1Code, Actor1Name, " +
				" Actor2Code, Actor2Name, " +
				"IsRootEvent, substr(EventCode) as EventCode,  QuadClass, NumMentions, " +
				"AvgTone, ActionGeo_FullName, " +
				"ActionGeo_CountryCode, substr2(ActionGeo_ADM1Code) as State,  " +
				"SOURCEURL from gdeltedu ORDER BY DateAdded");

```
Less Frequently Used Fields:  
```
Dataset<Row> gdeltLessFreqUsed = inputdf.sqlContext().sql("Select GLOBALEVENTID, FractionDate, "
				+ "Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, "
				+ "Actor1Type1Code, Actor1Type3Code,  Actor2Type2Code, "
				+ "EventBaseCode, EventRootCode, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, "
				+ "ActionGeo_Lat, ActionGeo_Long from gdeltedu ORDER BY FractionDate");
```
Unittests for these Spark jobs are included in the [test folder](https://github.com/yohn-dezmon/education-gdelt/tree/master/src/test/java/jdes/gdeltedu) of this repository.


## Data Analysis and Web UI

Both MySQL and Hive were used to run SQL/Hive-QL queries, at times simultaneously, to   obtain subsets of data to answer the questions I was curious about. I used the URL within the SOURCEURL field of the dataset to search for key words like 'assessment', 'charter   schools', and 'Every Student Succeeds Act' that are within the titles listed in the   pathname of the URL. Other fields from the GDELT Events Table that were frequently accessed by queries included AvgTone, NumMentions, and Actor1Geo_ADM1Code. Please see my explanation of how I used the GDELT dataset [here](http://yohndezmon.pythonanywhere.com/GDELT-details) and for more sinformation on what each field within the GDELT dataset represents please see [this document](http://data.gdeltproject.org/documentation/GDELT-Event_Codebook-V2.0.pdf).  

To extract meaning from the data, I used the matplotlib, seaborn, and pandas libraries to create graphs. The code to create the majority of the graphs displayed on the web interface for the project can be found [here](https://github.com/yohn-dezmon/education-gdelt/blob/master/gdelt-edu-web/graphs.py).

I decided to use Flask to create a web interface for the project, and the Python/HTML/CSS code for the Flask app can be found within the [gdelt-edu-web folder](https://github.com/yohn-dezmon/education-gdelt/tree/master/gdelt-edu-web). To host the website, I decided to use pythonanywhere as their platform was intuitive and they offered free hosting.  

The web interface for the project can be found [here](http://yohndezmon.pythonanywhere.com/). Thanks for reading, and please reach out to me if you have any questions:  

johndesmond631@gmail.com
