# sql testsampling strategy

insert into docsa select pageid, revid + 1000, current_timestamp(), removed, name, len + 20 from docsa UNIFORM SAMPLE 500;
insert into termsa (pageid, revid, tid, pos) select pageid, 2000, tid, pos from  termsa where pageid in (select pageid from docsa where revid = 2000);

#### transform schema sc1 to sc2
# first create schema for sc1
## fill additional column (tf) for terms table with sc1 tables


#1) generate SC2 FROM SC1

#a) DOCS TABLE generate REMOVED COLUMN AND INSERT IT TO TABLE 






select pageid, revid, name, added, len from (select *, row_number() OVER (partition by pageid order by pageid, added desc) as rnk from docsa where added < current_timestamp()) AS curr where curr.rnk = 2 sample 100;







CREATE TABLE dict2_hist_ordered (id bigint generated always as identity (START WITH 1 INCREMENT BY 1), tid bigint NOT NULL, timestamp timestamp not null, df int);
insert into dict2_hist_ordered (tid, timestamp, df) SELECT tid, timestamp, df from dict2_hist order by tid, timestamp desc;


select tid, timestamp, row_number() OVER (partition by tid order by tid, timestamp) as rnk from dict2_hist;