import sys
import re

pattern = "modi|usa|russia|healthcare|china|india|sonia|gandhi|rahul|elections|bill|hillary|clinton|obama|kerry|politics|democracy|direct|liberal|participation|illegal|devolution|trustee|mandate|manifesto|microcosm|proportional|tactical|electoral|hung|parliament|reform|partisanship|party|landslide|government|capitalist|paternalism|consensus|social|economics|welfare|sectional"

keywords = ['modi','usa','russia','healthcare','china','india','sonia','gandhi','rahul','elections','bill','hillary','clinton','obama','kerry','politics','democracy','direct','liberal']

regexp = (re.compile(pattern, flags=re.IGNORECASE))

input_text_modified= open('Input_Files\\InputFilePreProcessed.txt', 'r')

filtered_input_text= open('Input_Files\\FilteredInputText.txt', 'w')

for line in input_text_modified:
    result = regexp.search(line)
    if result:
        filtered_input_text.write(line)

filtered_input_text.close()
input_text_modified.close()
        

filtered_input_text = open('Input_Files\\FilteredInputText.txt', 'r')

keywords_count = open('Input_Files\\KeywordCounts.txt', 'w')

keywords_dict = {}

for line in filtered_input_text:
    for word in line.split():
        r = keywords_dict.get( word, 'None' )
        if word in keywords:
            if r == 'None':
                keywords_dict[word] = 1
            else:
                keywordsdsords_dict[word] = keywords_dict[word]+1


print keywords_dict
