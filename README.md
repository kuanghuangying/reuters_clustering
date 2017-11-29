# reuters_clustering
1. Dataset: from the "Reuters-21578 Text Categorization Collection Data Set" that is available at the UCI Machine Learning Repository (link: https://archive.ics.uci.edu/ml/datasets/Reuters-21578+Text+Categorization+Collection)

2. K-means clustering implementation

3. Criterion functions:
(i) standard sum-of-squared-errors criterion function of k-means,  
(ii) the I2 criterion function (i.e., spherical k-means)
(iii) the E1 criterion function. 

4. Input and Output for kcluster.java
  Input:
  criterion-function: SSE, I2, or E1
  class-file: and the class labels of the objects. 
  #clusters: the number of clusters,
  #trials: the number of trials that it will perform (each trial will be seeded with a different randomly selected set of objects), 
  
  Output
  output file: (i, cluster#) comma-separated format, where i is the article ID and cluster# is a number between 0 and #clusters-1, which is the cluster in which article i belongs to.
  output a two dimensional matrix of dimensions 
  (# of clusters)
  (# of classes)
  entries: the number of objects of a particular class that belongs to a particular cluster. 
  print the value of the criterion function, entropy, purity for the best trial

5. Data-preprocessing steps:
  a. Selecting the Subset of the Dataset for Clustering
Process the various SGML files (.sgm) extension and select the articles that contain only a single topic. The topic is included within a pair of <TOPICS><D>topic</D></TOPICS> tags. Ignore the articles that do not contain any topic or have more than a single topic. A back of the envelope calculation shows that are about 9494 such articles. From these articles retain only the articles that correspond to topics that occur in the 20 most frequent topics. 

From the above set of articles, extract the NEWID number (in the <REUTERS> tag attribute), the single topic, and the text that is included within the <BODY>...</BODY> tags.  The body of the article will form the text that you will be using for clustering, the topic will act as a class label for evaluation, and the NEWID will be used as the ID of the article. 



  b. Obtaining the Sparse Representation

Derive a bag of words representation. In order to do that, you need to first clean up the text. To do that perform the following steps in that sequence:

    Eliminate any non-ascii characters.
    Change the character case to lower-case.
    Replace any non alphanumeric characters with space.
    Split the text into tokens, using space as the delimiter.
    Eliminate any tokens that contain only digits.
    Eliminate any tokens from the stop list that is provided (file stoplist.txt).
    Obtain the stem of each token using Porter's stemming algorithm; you can use any of the implementations provided here: https://tartarus.org/martin/PorterStemmer/.
    Eliminate any tokens that occur less than 5 times.


  c. Vector Representations

Collect all the tokens that remained after step 8 (above) across all articles and use them to represent each article as a vector in the distinct token space. For each document, derive three different representations by using the following approaches to assign a value to each of the document's vector dimension that corresponds to a token t that it contains:

    The value will be the actual number of times that t occurs in the document (frequency).
    The value will be 1+sqrt(frequency) for those terms that have a non-zero frequency.
    The value will be 1+log2(frequency) for those terms that have a non-zero frequency.
Normalize them so that they are of unit length. These unit-length vectors will be the input to the clustering algorithms.
