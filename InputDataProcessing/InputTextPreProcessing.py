from __future__ import division

import sys
import nltk
import re

from sklearn.metrics import precision_recall_fscore_support
from nltk.corpus import stopwords

#Remove stop words
##input_text = open('InputFile.txt', 'r')
##input_text_preprocessed = open('InputFilePreProcessed.txt', 'w')
##
##pattern = re.compile("[@a-zA-Z][a-zA-Z][a-zA-Z0-9]*") 
##     
##for line in input_text:
##    empty_line = 1
##    for word in pattern.findall(line):
##        empty_line = 0
##        input_text_preprocessed.write(word+' ')
##    if empty_line == 0:
##        input_text_preprocessed.write('\n')
##
##input_text_preprocessed.close()
##input_text.close()
##
##input_text_preprocessed_2 = open('InputFilePreProcessed_2.txt', 'w')
##input_text_2= open('InputFilePreProcessed.txt', 'r')
##        
##     
##for line in input_text_2:
##    line_modified = ' '.join([word for word in line.split() if word not in (stopwords.words('english'))])
##    input_text_preprocessed_2.write(line_modified + '\n')
##
##input_text_preprocessed_2.close()
##
##input_text_2.close()




input_text_modified= open('InputFilePreProcessed_2.txt', 'r')

line_count = 0
for line in input_text_modified:
    line_count = line_count + 1

print line_count
input_text_modified.close()



date = 20140421
tweets_per_date = line_count/10
current_tweet_per_date = 0

input_text_modified= open('InputFilePreProcessed_2.txt', 'r')

#Generate input file with date
input_file_date = open('InputFileDate.txt', 'w')
for line in input_text_modified:
    current_tweet_per_date = current_tweet_per_date + 1
    if (current_tweet_per_date > tweets_per_date):
        date = date + 1
        current_tweet_per_date = 0
    input_file_date.write(line + ' ' + str(date) + '\n')
    

input_text_modified.close()

#input_text_preprocessed.close()

input_file_date.close()



