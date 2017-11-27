
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
	for key,value in sorted(map.items(),key=lambda k,v:(v,k),reverse=True):
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
	soup = BeautifulSoup(data,'lxml')

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

sum = 0
for k,v in id_article_map.items():
	for k2,v2 in v.items():
		sum +=1
print('number of articles with body tag' + str(sum))

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
		#new_body = "".join([ c.lower() if ord(c) <128 else " " for c in body ])
		#new_body = "".join([ c.lower() if c.isalnum() else " " for c in new_body ])
		# Split the text into tokens, using space as the delimiter.
		tokens = body.split()
		print(tokens)
		# Eliminate only digits, from the stop list
		# Obtain the stem of each token using Porter's stemming algorithm;
		stem_list = []
		for token in tokens:
			if (not token.isdigit()) & (not token in stop_list):
				stem = ps.stem(token)
				stem_list.append(stem)
				if stem in stem_map:
					stem_map[stem] += 1
				else:
					stem_map[stem] = 1
		article_vec_raw[id] = stem_list
classification_f.close()

# # Eliminate any tokens that occur less than 5 times.
# word_bag = select_from_map(stem_map,5)
# article_vec = {}
# for id,word_list in article_vec_raw.items():
# 	article_vec[id] = filter(lambda a: a in word_bag, word_list)
#
# #write clabel
# label_vec = list(word_bag)
# label_f = open('reuters21578.clabel','w')
# all_labels = '\n'.join(label_vec)
# label_f.write(all_labels)
# label_f.close()
#
#
# #<-------------------Vector Representations----------------->
# l1 = []
# l2 = []
# l3 = []
# freq_csv = open('freq.csv','w')
# sqrtfreq_csv = open('sqrtfreq.csv','w')
# log2freq_csv = open('log2freq.csv','w')
# for id, word_vec in article_vec.items():
# 	counts = Counter(word_vec)
# 	n=1
# 	for dimension in label_vec:
# 		id_dim_freq1 = ','.join([str(id),str(n),str(counts[dimension])]) + '\n'
# 		l1.append(id_dim_freq1)
#
# 		if counts[dimension]> 0:
# 			counts2 = 1 + counts[dimension]**(1.0/2)
# 			counts3 = 1 + math.log(counts[dimension],2)
# 		else:
# 			counts2 = 0
# 			counts3 = 0
#
# 		id_dim_freq2 = ','.join([str(id),str(n),str(counts2)]) + '\n'
# 		l2.append(id_dim_freq2)
#
# 		id_dim_freq3 = ','.join([str(id),str(n),str(counts3)]) + '\n'
# 		l3.append(id_dim_freq3)
#
# 		if counts2 >1:
# 			print(id_dim_freq2)
# 		n += 1
# freq_csv.write(''.join(l1))
# sqrtfreq_csv.write(''.join(l2))
# log2freq_csv.write(''.join(l3))
#
#
#
#
#
#
#
#
#
#
#
#
#
# end = time.clock()
# print(end - start)
#
# #For each trial, we want different set of objects (we'll get them randomly).
#
# #We want #trials to be 20. You'll have to use these 20 seeds: [1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39].
#
# #When you generate random numbers, you give the random-number-generator function a seed. (The sequence of random numbers generated by a particular seed is same). Each trial corresponds to selecting a different seed for the random-number-generator function.
