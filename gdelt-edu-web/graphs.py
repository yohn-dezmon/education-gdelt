import pymysql
import mpld3
import seaborn as sns
from urllib.parse import urlparse
import pandas as pd
import numpy as np
from io import BytesIO
import matplotlib.pyplot as plt
import pdb


class Graph(object):
    """ A class to house all of the code responsible for creating
    the graphs on the website, thus making app.py more readable. """

    def __init__(self):
        pass


    def assessment_count(self):
        sns.set(style='whitegrid')

        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/count_assesment_US.csv',
                                sep=','
                                )
        df_sorted = df.sort_values(by=['Count'], ascending=False).head(15)


        # default = 6.4 (width?), 4.8 (height)
        ax = sns.barplot(x='Actor',y='Count', data=df_sorted, palette='spring')
        plt.figure(1)
        ax.set_xticklabels(ax.get_xticklabels(), rotation=25, ha="right", fontsize=10)
        plt.xlabel('Actor', fontsize=12)
        # ok I think the ordering of tight_layout() -> subplots_adjust is important!

        y = df_sorted['Count']
        # plt.yticks(np.arange(0, 60000, 5000))
        plt.ylabel('Count', fontsize=12)
        plt.tight_layout()
        plt.subplots_adjust(top=0.9)
        plt.title('Most Common Actors - Assessments')
        # figsize=(4.0,3.8)

        url = 'static/count_assessment_US.png'
        # f = BytesIO()
        plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/count_assessment_US.png')
        return url

    def assessment_avgtone(self):
        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/avgtone_assesment_US.csv',
                            sep=',',
                            header=0
                            )
        # print(df.dtypes)
        df_sorted = df.sort_values(by=['Average Tone'], ascending=False)
        df_sorted = df_sorted[df.Actor != 'PROFESSOR']
        df_sorted = df_sorted[df.Actor != 'COLLEGE']
        df_sorted = df_sorted[df.Actor != 'UNIVERSITY']


        x = df_sorted['Actor']
        y = df_sorted['Average Tone']
        plt.figure(figsize=(6.4,4.8))
        ax = sns.barplot(x,y, data=df_sorted, palette='spring')
        ax.set_xticklabels(ax.get_xticklabels(), rotation=40, ha="right", fontsize=9)
        # ax.get_legend().set_visible(False)
        plt.tight_layout()
        plt.title('Average Tone of Actors for Articles Involving Assessments in US')
        plt.ylabel('Average Tone')
        plt.xlabel('US Actor')

        url = 'static/avgtone_assessment_US.png'
        plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/avgtone_assessment_US.png')
        return url

    def assessment_tables(self, name_of_file):
        # This allows the full URL to be printed within the table cell.
        pd.set_option('display.max_colwidth', -1)

        mydateparser = lambda x: pd.datetime.strptime(x, "%Y%m%d")
        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/'+name_of_file+'.csv',
                            sep=',',
                            header=0,
                            error_bad_lines=False,
                            warn_bad_lines=True,
                            parse_dates=['Date'],
                            date_parser=mydateparser
                            )

        # df_subset = df[['SOURCEURL']]

        df_subset = df[['SOURCEURL']].apply(lambda x: '<a href="'+x+'">'+x+'</a>')

        if name_of_file == "FL_nummen_assessment":
            return df_subset.to_html(index=False, classes=["assessment","FL"], escape=False)

        return df_subset.to_html(index=False, classes="assessment", escape=False)
