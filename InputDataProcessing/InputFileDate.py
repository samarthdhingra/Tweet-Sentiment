from __future__ import division

import sys
import nltk
import re

from sklearn.metrics import precision_recall_fscore_support
from nltk.corpus import stopwords

input_file_date = open('InputFileWithDate.txt', 'w')

input_text_modified= open('InputFilePreProcessed_2.txt', 'r')

line_count = 0
for line in input_text_modified:
    line_count = line_count + 1

print line_count

input_text_modified.close()

input_text_modified= open('InputFilePreProcessed_2.txt', 'r')

date = 20140421
tweets_per_date = line_count/10
current_tweet_per_date = 0
     
for line in input_text_modified:
    #print 'Inside'
    current_tweet_per_date = current_tweet_per_date + 1
    if (current_tweet_per_date > tweets_per_date):
        date = date + 1
        current_tweet_per_date = 0
    input_file_date.write((line.strip('\n')).rstrip() + ' ' + str(date) + '\n')
    

input_text_modified.close()

#input_text_preprocessed.close()

input_file_date.close()
#Generate input file with date

