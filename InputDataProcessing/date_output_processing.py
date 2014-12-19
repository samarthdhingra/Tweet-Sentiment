import sys
import re
import csv

date_dict = {}

output_file = open('sentiments_date_output.txt', 'w')

csvreader = csv.reader(open('Date_output.txt', 'r'), delimiter='\t', quoting=csv.QUOTE_NONE)

for row in csvreader:
    word_date = row[0].split(':#:')
    #print word_date
    date = word_date[1]
    #print date
    word = word_date[0]
    #print word

    word_count = int(row[1])
    #print word_count

    r = date_dict.get( date, 'None' )
    if r == 'None':
        counts = []
        if (word_count > 0):
            counts.append(word_count)
            counts.append(0)
        else:
            counts.append(0)
            counts.append(word_count)
        #print counts
        date_dict[date] = counts
    else:
        if (word_count > 0):
            (date_dict[date])[0] = (date_dict[date])[0] + word_count
        else:
            (date_dict[date])[1] = (date_dict[date])[1] + word_count
         
for date in date_dict:
    output_file.write(date + '\t' + str((date_dict[date])[0]) + '\t' + str((date_dict[date])[1]) + '\n') 

output_file.close()
