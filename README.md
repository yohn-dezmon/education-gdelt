### GDELT Data Pipeline to Analyze Media Coverage of Education

This is the Java Spark code that I am using to normalize and aggregate data from the GDELT Data Set.

The purpose of this project is to analyze the media coverage of educational topics the United States.    
I'll be attempting to characterize what different members of the educational community are    
saying about keywords like curriculum, assessment, and charter schools. I'll also be attempting to  
find trends regarding media coverage of educational topics, and how this coverage differs across   
States within the US as well as a comparison of US media coverage and international media coverage    
regarding education.  

The data was originally queried/filtered using Google Big Query, then exported to   
Google Cloud Storage where I downloaded individual .gz files. After extracting the .gz files to  
.csv files, I used the Java Spark code within this repository to aggregate  
the separate .csv files into one batch of parquet files which I then imported into Spark once  
more to normalize and order the data further, and to place into MySQL for storage and analysis.  

I am currently in the process of analyzing and visualizing the data using MySQL, Hive, and the  
matplotlib, pandas, and seaborn libraries in Python. 

