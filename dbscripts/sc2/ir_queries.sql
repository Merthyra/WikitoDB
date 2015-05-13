
## timestamp declaration @ the beginning!
declare timest TIMESTAMP;
set timest = '2015-01-02 10:35:12';
############################################################################################################################
###### OKAPI BM 25 Retrieval applied to scenario 2
WITH
## filter valid documents and calculate avg(len) and N - take always the last revision of the pageid.... that means page cannot be deleted instead new empty document is added if doc should be deleted
qdocs AS (SELECT * FROM  (SELECT *, row_number() OVER (PARTITION BY pageid order by added desc) as rnk FROM docs1 WHERE added <= timest)as sub where rnk =1),

## valid terms containing one of the search strings and contain querie df and tf values
qterms AS (SELECT terms.tid, terms.did, tdic.df, terms.tf, tdic.term FROM (SELECT tid, term, df FROM wiki2.dict WHERE dict.term IN ('computer', 'computers' , 'usa') 
AND added <= timest AND (removed IS NULL OR removed > timest)) AS tdic 
JOIN wiki2.terms ON terms.tid = tdic.tid 
JOIN qdocs ON qdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM qdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from qdocs),

## document term scores
subscores AS (
SELECT qdocs.docid, qdocs."len", qterms.tid, qterms.df, qterms.tf, (SELECT tnr FROM doc_nr) AS n, (SELECT anr FROM len_avg) as av,
(log(((SELECT tnr FROM doc_nr) - qterms.df + 0.5)/(qterms.df + 0.5)) * qterms.tf * (1.2 + 1) / 
(qterms.tf + 1.2 * (1 - 0.75 + 0.75 * ((qdocs."len")/((SELECT anr FROM len_avg)))))) AS subscore
FROM qterms 
JOIN qdocs ON qterms.did=qdocs.docid)

## suming up document scores
SELECT subscores.docid, sum(subscores.subscore) AS rnk 
FROM subscores GROUP BY subscores.docid ORDER BY rnk desc LIMIT 50;
#SELECT * from subscores ORDER BY subscores.docid LIMIT 20;
 

 ############################################################################################################################
 ##obtaion df and tf values for specific document and term
 
WITH
## filter valid documents and calculate avg(len) and N
qdocs AS (SELECT * FROM wiki2.DOCS WHERE added <= timest AND (removed IS NULL OR removed > timest))

 ## valid terms containing one of the search strings and contain querie df and tf values
SELECT terms.tid, terms.did, tdic.df, terms.tf, tdic.term FROM 
(SELECT tid, term, df FROM wiki2.dict WHERE dict.term IN ('usa') AND added <= timest AND (removed IS NULL OR removed > timest)) AS tdic 
JOIN wiki2.terms ON terms.tid = tdic.tid 
JOIN qdocs ON qdocs.docid = terms.did 
where terms.did = 1633 AND terms.tid = 3653;