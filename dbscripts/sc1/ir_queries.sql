#### final query ranking documents with bm25 retrieval model!

WITH
## filter valid documents and calculate avg(len) and N
qdocs AS (SELECT * FROM wiki1.docs WHERE added <= '2019-01-14 10:46:14' AND (removed IS NULL OR removed > '2019-01-14 10:46:14')),

## valid terms containing one of the search strings
qterms AS (SELECT terms.tid, terms.did, tdic.term FROM 
(SELECT tid, term FROM wiki1.dict WHERE dict.term IN ('computer', 'computers' , 'usa')) AS tdic 
JOIN wiki1.terms ON terms.tid = tdic.tid 
JOIN qdocs ON qdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM qdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from qdocs),

## frequency of terms in documents (tf) = term frequency
term_tf AS (SELECT tid, did, COUNT(*) AS tf FROM qterms GROUP BY tid, did),

## number of documents containing search term (df)
term_df AS (SELECT tid, count(tid) AS df from term_tf GROUP BY tid),

## document term scores
subscores AS (
SELECT qdocs.docid, qdocs."len", term_tf.tid, term_df.df, term_tf.tf, (SELECT tnr FROM doc_nr) AS n, (SELECT anr FROM len_avg) as av,
(log(((SELECT tnr FROM doc_nr) - term_df.df + 0.5)/(term_df.df + 0.5)) * term_tf.tf * (1.2 + 1) / (term_tf.tf + 1.2 * (1 - 0.75 + 0.75 * ((qdocs."len")/((SELECT anr FROM len_avg)))))) AS subscore
FROM term_tf 
JOIN qdocs ON term_tf.did=qdocs.docid
JOIN term_df ON term_df.tid = term_tf.tid)

## suming up document scores
SELECT subscores.docid, sum(subscores.subscore) AS rnk 
FROM subscores GROUP BY subscores.docid ORDER BY rnk desc LIMIT 50;
#SELECT * from subscores ORDER BY subscores.docid LIMIT 20;


########################################################################################################################################################
######### get document frequency for term 
DECLARE timest TIMESTAMP;
SET timest '2014-01-14 10:46:14'
########################################################################################################################################################
######### IR QUERY 
WITH 
## filter valid documents and calculate avg(len) and N
qdocs AS (SELECT * FROM wiki1.docs WHERE added <= timest AND (removed IS NULL OR removed > timest)),

## valid terms containing one of the search strings
qterms AS (SELECT terms.tid, terms.did, tdic.term FROM 
(SELECT tid, term FROM wiki1.dict WHERE dict.term IN (
#### specify term here ####>
'usa'
#### specify term here ####<
)) AS tdic 
JOIN wiki1.terms ON terms.tid = tdic.tid 
JOIN qdocs ON qdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM qdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from qdocs),

## frequency of terms in documents (tf) = term frequency
term_tf AS (SELECT tid, did, COUNT(*) AS tf FROM qterms GROUP BY tid, did)

## number of documents containing search term (df)
SELECT tid, count(tid) AS df from term_tf GROUP BY tid;

########################################################################################################################################################
########## get term frequency for term in specific document

WITH
## filter valid documents and calculate avg(len) and N
qdocs AS (SELECT * FROM wiki1.docs WHERE added <= '2019-01-14 10:46:14' AND (removed IS NULL OR removed > '2019-01-14 10:46:14')),

## valid terms containing one of the search strings
qterms AS (SELECT terms.tid, terms.did, tdic.term FROM 
(SELECT tid, term FROM wiki1.dict WHERE dict.term IN (
#### specify term here ####>
'usa'
#### specify term here ####<
)) AS tdic 
JOIN wiki1.terms ON terms.tid = tdic.tid 
JOIN qdocs ON qdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM qdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from qdocs)

## frequency of terms in documents (tf) = term frequency
SELECT tid, did, COUNT(*) AS tf FROM qterms 

####specify doc number here >
 where qterms.did = 1633
#### <

GROUP BY tid, did;