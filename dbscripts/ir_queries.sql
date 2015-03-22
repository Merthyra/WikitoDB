WITH
## filter valid documents and calculate avg(len) and N
validdocs AS (SELECT * FROM DOCS WHERE added <= '2019-01-14 10:46:14' AND (removed IS NULL OR removed > '2019-01-14 10:46:14')),

## valid terms containing one of the search strings
qterms AS (SELECT terms.tid, docs.docid, tdic.term FROM (SELECT tid, term FROM dict WHERE dict.term IN ('usa', 'as', 'car')) AS tdic JOIN terms ON terms.tid = tdic.tid JOIN validdocs ON validdocs.docid = terms.did ),

## average document length (avg(len))
len_avg AS (SELECT avg(len) AS anr FROM validdocs),

## total number of documents (N)
doc_nr AS (SELECT count(*) AS tnr from validdocs),

## frequency of terms in documents (tf) = term frequency
term_tf AS (SELECT term, docid, COUNT(*) AS tf FROM qterms GROUP BY term, docid),

## number of documents containing search term (df)
term_df AS (SELECT term, count(term) AS df from term_tf GROUP BY term),

## document term scores
subscores AS (
SELECT validdocs.docid, validdocs."len", term_tf.term, term_df.df, term_tf.tf, (SELECT tnr FROM doc_nr) AS n, (SELECT anr FROM len_avg) as av,
(log(((SELECT tnr FROM doc_nr) - term_df.df + 0.5)/(term_df.df + 0.5)) * term_tf.tf * (1.2 + 1) / (term_tf.tf + 1.2 * (1 - 0.75 + 0.75 * ((validdocs."len")/((SELECT anr FROM len_avg)))))) AS subscore
FROM term_tf 
JOIN validdocs ON term_tf.docid=validdocs.docid
JOIN term_df ON term_df.term = term_tf.term)

## suming up document scores
SELECT subscores.docid, sum(subscores.subscore) AS rnk 
FROM subscores GROUP BY subscores.docid ORDER BY rnk desc;
#SELECT * from subscores ORDER BY subscores.docid;