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

    def buzzwords_graph(self, name_of_file):
        sns.set(style='whitegrid')

        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/keyword_count.csv',
                                sep=',',
                                names=['Keyword','Count']
                                )
        if name_of_file == "top10":
            df_sorted = df.sort_values(by=['Count'], ascending=False).head(10)


            # default = 6.4 (width?), 4.8 (height)
            plt.figure()
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

            url = 'static/buzzwords-top10.png'
            plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/buzzwords-top10.png')

        if name_of_file == "11-21":
            df_sorted2 = df.sort_values(by=['Count'], ascending=False).tail(11)
            plt.figure()
            ax = sns.barplot(x='Keyword',y='Count', data=df_sorted2, palette='spring')
            ax.set_xticklabels(ax.get_xticklabels(), rotation=20, ha="right", fontsize=10)
            plt.xlabel('Keyword', fontsize=12)
            plt.ylabel('Count', fontsize=12)
            plt.tight_layout()
            plt.subplots_adjust(top=0.9)
            plt.title('Educational Buzzwords in US Media (11-21)')
            url2 = 'static/buzzwords-11-21.png'
            plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/buzzwords-11-21.png')
            return url2

        return url

    def assessment_count(self):
        sns.set(style='whitegrid')

        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/count_assesment_US.csv',
                                sep=','
                                )
        df_sorted = df.sort_values(by=['Count'], ascending=False).head(15)


        # default = 6.4 (width?), 4.8 (height)
        plt.figure()
        ax = sns.barplot(x='Actor',y='Count', data=df_sorted, palette='spring')

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

    def table_generator(self, name_of_file, topic):
        # This allows the full URL to be printed within the table cell.
        pd.set_option('display.max_colwidth', -1)

        if topic == "assessment":
            mydateparser = lambda x: pd.datetime.strptime(x, "%Y%m%d")
            df = pd.read_csv('/media/sf_sharedwithVM/MySQL/'+name_of_file+'.csv',
                                sep=',',
                                header=0,
                                error_bad_lines=False,
                                warn_bad_lines=True,
                                parse_dates=['Date'],
                                date_parser=mydateparser
                                )
        elif topic == "curriculum":
            df = pd.read_csv('/media/sf_sharedwithVM/MySQL/'+name_of_file+'.csv',
                                sep=',',
                                header=0,
                                error_bad_lines=False,
                                warn_bad_lines=True,
                                )

        df_subset = df[['SOURCEURL']].apply(lambda x: '<a href="'+x+'">'+x+'</a>')

        if name_of_file == "FL_nummen_assessment":
            # Escape = False prevents characters like '<' from being escaped by pandas
            return df_subset.to_html(index=False, classes=["assessment","FL"], escape=False)

        if topic == "curriculum":
            return df_subset.to_html(index=False, classes="curriculum", escape=False)

        return df_subset.to_html(index=False, classes="assessment", escape=False)

    def curri_org_table(self):
        """ Keeping this for reference """
        pd.set_option('display.max_colwidth', -1)

        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/CA_curri_URL.csv',
                            sep=',',
                            header=0,
                            error_bad_lines=False,
                            warn_bad_lines=True,
                            )
        path_col = [urlparse(row).path for row in df['SOURCEURL']]
        df['URL Path'] = path_col


        html_table = df.to_html(index=False)
        titles = ['URL','URL Path']

    def curri_distplot(self, name_of_file, state):
        sns.set(style='whitegrid')

        df = pd.read_csv('/media/sf_sharedwithVM/MySQL/'+name_of_file+'.csv',
                                sep=','
                                )
        df = df['AvgTone']
        # this needs to come before the sns.plot method to avoid
        # re-using the same canvas
        plt.figure(figsize=(7.4,4.0))
        # default = 6.4 (width?), 4.8 (height)
        ax = sns.distplot(df, color='#53F464')


        plt.xlabel('Average Tone', fontsize=12)
        # ok I think the ordering of tight_layout() -> subplots_adjust is important!
        # plt.yticks(np.arange(0, 60000, 5000))
        plt.ylabel('Count', fontsize=12)
        plt.tight_layout()
        plt.subplots_adjust(top=0.9)
        if state == "Texas":
            plt.title(state+'\' Average Tone Distribution', fontsize=14)
            url = 'static/'+name_of_file+'.png'
            plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/'+name_of_file+'.png')
            
        plt.title(state+'\'s Average Tone Distribution', fontsize=14)
        url = 'static/'+name_of_file+'.png'
        plt.savefig('/media/sf_sharedwithVM/gdelt-education/gdelt-edu-web/static/'+name_of_file+'.png')

        return url
