import os
from bs4 import BeautifulSoup
from nltk import PorterStemmer
import time
from collections import Counter
import math

start = time.clock()
path = "./reuters21578"
#retain .sgm file
all_files = [file for file in os.listdir(path)]
files = []
for file in all_files:
	if (file.endswith(".sgm")):
		files.append(file)

def top_from_map(map, k):
	i = 0
	list = []
	for key,value in sorted(map.items(),key=lambda (k,v):(v,k),reverse=True):
		if (i < k):
			list.append(key)
			i += 1
	return list

def select_from_map(map, k):
	bag = set()
	for key,value in map.iteritems():
		if (value >= k):
			bag.add(key)
	return bag

def readStopList(stop_file):
	l = []
	content = stop_file.readlines()
	lines = [x.strip('\n') for x in content]
	for line in lines:
		words = line.split()
		for word in words:
			l.append(word)
	return l

stop_file = open('reuters21578/stoplist.txt','r')
stop_list = readStopList(stop_file)
stop_file.close()
print('---------------------stop_list done----------------------')




#open each file
id_article_map = {}
topic_freq = {}
for file_name in files:
	file_path = "./reuters21578/" + file_name
	sgm = open(file_path, 'r')
	data= sgm.read()
	data = data.replace('BODY','CONTENT')
	soup = BeautifulSoup(data)

	print("read............" + file_name)
	for article in soup.find_all('reuters'):
		num_topics = len(article.topics.findAll('d'))
		if num_topics == 1:
			if (article.find('content')):
				#extract topic
				for topic in article.topics.findAll('d'):
					topic_s = topic.contents[0]
					if topic_s in topic_freq:
						topic_freq[topic_s] += 1
					else:
						topic_freq[topic_s] = 1

				#extract body
				temp_dict = {}
				body = article.find('content').contents
				if topic_s in id_article_map:
					id_article_map[topic_s][article.get('newid')] = body[0]
				else:
					temp_dict = {}
					temp_dict[article.get('newid')] = body[0]
					id_article_map[topic_s] = temp_dict
	#break

sum_article = 0
for k,v in id_article_map.items():
	for k2,v2 in v.items():
		sum_article +=1
print('number of articles with body tag' + str(sum_article))

#retain articles of top 20 topics
top_topics = top_from_map(topic_freq, 20)

for each in top_topics:
	print(each)

classification_f = open('reuters21578.class','w')
ps = PorterStemmer()
word_bag = set()
article_vec_raw = {}
stem_map = {}
for topic in top_topics:
	for id,body in id_article_map[topic].items():
		# write .class
		classification_f.write(id + ',' + topic + "\n")

		#process text within body
		# 1 Eliminate any non-ascii characters.
		# 2 Change the character case to lower-case.
		# 3 Replace any non alphanumeric characters with space.
		new_body = "".join([ c if ord(c) <128 else " " for c in body ])
		new_body = "".join([ c.lower() if c.isalnum() else " " for c in new_body ])
		tokens = new_body.split()
		stem_list = []
		for token in tokens:
			# Eliminate only digits, from the stop list
			if (not token.isdigit()) & (not token in stop_list):
				# Obtain the stem of each token
				stem = ps.stem(token)
				stem_list.append(stem)
				if stem in stem_map:
					stem_map[stem] += 1
				else:
					stem_map[stem] = 1
		article_vec_raw[id] = stem_list

classification_f.close()

# Eliminate any tokens that occur less than 5 times.
word_bag = select_from_map(stem_map,5)
article_vec = {}
for id,word_list in article_vec_raw.items():
	article_vec[id] = filter(lambda a: a in word_bag, word_list)
print('article_vec length --' + str(len(article_vec)))
#write clabel
label_vec = list(word_bag)
label_f = open('reuters21578.clabel','w')
all_labels = '\n'.join(label_vec)
label_f.write(all_labels)
label_f.close()


#<-------------------Vector Representations----------------->

freq_csv = open('freq.csv','w')
sqrtfreq_csv = open('sqrtfreq.csv','w')
log2freq_csv = open('log2freq.csv','w')
dimension_num = len(label_vec)
ctr = 0
for id, word_vec in article_vec.items():
    l1 = []
    l2 = []
    l3 = []
    counts = Counter(word_vec)
    for dimension in label_vec:
        counts1 = counts[dimension]
	if counts1> 0:
		counts2 = 1 + counts1**(1.0/2)
		counts3 = 1 + math.log(counts1,2)
	else:
		counts2 = 0
		counts3 = 0
        l1.append(counts1)
        l2.append(counts2)
        l3.append(counts3)
    sum1 = sum(l1)
    sum2 = sum(l2)
    sum3 = sum(l3)
    norm1 = [float(x)/sum1 for x in l1]
    norm2 = [float(x)/sum2 for x in l2]
    norm3 = [float(x)/sum3 for x in l3]
    n=0
    while n < dimension_num:
        if norm1[n] != 0:
            id_dim_freq1 = ','.join([str(id),str(n+1),str(norm1[n])]) + '\n'
            freq_csv.write(id_dim_freq1)
        if norm2[n] != 0:
            id_dim_freq2 = ','.join([str(id),str(n+1),str(norm2[n])]) + '\n'
            sqrtfreq_csv.write(id_dim_freq2)
        if norm3[n] != 0:
            id_dim_freq3 = ','.join([str(id),str(n+1),str(norm3[n])]) + '\n'
            log2freq_csv.write(id_dim_freq3)
	n += 1
    print(ctr)
    ctr+=1
freq_csv.close()
sqrtfreq_csv.close()
log2freq_csv.close()

end = time.clock()
print(end - start)
