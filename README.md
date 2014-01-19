Slovenian NER service
===============

This is a RESTful service for doing named entity extraction for Slovenian language. It is written in Java and it needs a Java servlet container to run (e.g. Jetty, Tomcat or other).

Usage
--------

Send a POST request to `/tag` URL with content of your text:

```
{ "text": "Mitja zaradi testa hodi v Ljubljano." }
```

The service will respond with tokenized text as a JSON Array. Tagged words will have `tag` property set.

```
[{"word":"Mitja", "tag":"PERSON"},{"word":"zaradi"},{"word":"testa"},{"word":"hodi"},{"word":"v"},{"word":"Ljubljano","tag":"LOCATION"},{"word":"."}]
```

Possible tags are: PERSON, LOCATION and OTHER for personal, geographic and other names. Words that do not have a tag will not have `tag` property present.

Background
-----------

The tagger uses [Stanford Named Entity Recognizer][1] library trained on [ssj500k][2] tagged set with following properties file:

```
# location of the training file
trainFile = ssj500kv1_1.xml.txt
# location where you would like to save (serialize) your
# classifier; adding .gz at the end automatically gzips the file,
# making it smaller, and faster to load
serializeTo = slovenian.ser.gz

# structure of your training file; this tells the classifier that
# the word is in column 0 and the correct answer is in column 1
map = word=0,answer=1

# This specifies the order of the CRF: order 1 means that features
# apply at most to a class pair of previous class and current class
# or current class and next class.
maxLeft=1

# these are the features we'd like to train with
# some are discussed below, the rest can be
# understood by looking at NERFeatureFactory
useClassFeature=true
useWord=true
# word character ngrams will be included up to length 6 as prefixes
# and suffixes only
useNGrams=true
noMidNGrams=true
maxNGramLeng=6
usePrev=true
useNext=true
useDisjunctive=true
useSequences=true
usePrevSequences=true
# the last 4 properties deal with word shape features
useTypeSeqs=true
useTypeSeqs2=true
useTypeySequences=true
wordShape=chris2useLC
```




 [1]: http://nlp.stanford.edu/software/CRF-NER.shtml
 [2]: http://www.slovenscina.eu/tehnologije/ucni-korpus

