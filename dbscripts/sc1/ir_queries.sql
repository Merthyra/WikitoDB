WITH
## filter valid documents and calculate avg(len) and N
validdocs AS (SELECT * FROM DOCS WHERE added <= '2019-01-14 10:46:14' AND (removed IS NULL OR removed > '2019-01-14 10:46:14')),

## valid terms containing one of the search strings
qterms AS (SELECT terms.tid, terms.did, tdic.term FROM (SELECT tid, term FROM dict WHERE dict.term IN ('computer', 'computers' , 'usa')) AS tdic JOIN terms ON terms.tid = tdic.tid JOIN validdocs ON validdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM validdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from validdocs),

## frequency of terms in documents (tf) = term frequency
term_tf AS (SELECT tid, did, COUNT(*) AS tf FROM qterms GROUP BY tid, did),

## number of documents containing search term (df)
term_df AS (SELECT tid, count(tid) AS df from term_tf GROUP BY tid),

## document term scores
subscores AS (
SELECT validdocs.docid, validdocs."len", term_tf.tid, term_df.df, term_tf.tf, (SELECT tnr FROM doc_nr) AS n, (SELECT anr FROM len_avg) as av,
(log(((SELECT tnr FROM doc_nr) - term_df.df + 0.5)/(term_df.df + 0.5)) * term_tf.tf * (1.2 + 1) / (term_tf.tf + 1.2 * (1 - 0.75 + 0.75 * ((validdocs."len")/((SELECT anr FROM len_avg)))))) AS subscore
FROM term_tf 
JOIN validdocs ON term_tf.did=validdocs.docid
JOIN term_df ON term_df.tid = term_tf.tid)

## suming up document scores
SELECT subscores.docid, sum(subscores.subscore) AS rnk 
FROM subscores GROUP BY subscores.docid ORDER BY rnk desc LIMIT 50;
#SELECT * from subscores ORDER BY subscores.docid LIMIT 20;