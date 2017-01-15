#adapted query from ir prototyping
WITH 
qterms AS (SELECT tid, did FROM terms_lim WHERE tid in (%QUERY%)),
subscores AS (SELECT docs1.did,  
(log(((3034603) - qdict.df + 0.5)/(qdict.df + 0.5)) * term_tf.tf * (1.2 + 1) / (term_tf.tf + 1.2 * (1 - 0.75 + 0.75 * ((docs1."len")/(( 898.9168003194732)))))) AS subscore
FROM 
(select did, tid, count(*) as tf from qterms group by did, tid) as term_tf
join docs1
on docs1.did = term_tf.did
join dict_limited qdict
on qdict.tid = term_tf.tid)
SELECT did, score FROM 
(SELECT did, sum(subscore) AS score 
FROM subscores GROUP BY did) AS scores 
ORDER BY score DESC LIMIT 50; 
