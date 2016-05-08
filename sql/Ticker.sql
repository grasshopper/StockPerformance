select sysdate() from dual;

SELECT T.ID, 
	   T.SYMBOL, 
	   T.NAME, 
       T.EXCHANGE, 
       T.TRACK, 
       T.OWN, 
	   A.NAME AS ACCOUNT, 
       T.TICKER_TYPE_ID 
FROM   TICKER T 
       INNER JOIN ACCOUNT A 
	     ON A.ID = T.ACCOUNT_ID 
WHERE  TRACK = TRUE 
ORDER BY OWN DESC, ACCOUNT_ID;


desc ticker;


select * from ticker_type;


SELECT *
FROM   ticker t
WHERE  T.SYMBOL = 'MSFT';

SELECT T.ID, T.SYMBOL, T.NAME, T.EXCHANGE, T.TRACK, T.OWN, T.ACCOUNT_ID, T.TICKER_TYPE_ID 
FROM   TICKER T 
       INNER JOIN ACCOUNT A 
         ON A.ID = T.ACCOUNT_ID 
WHERE SYMBOL = 'VITPX';



DELETE FROM TICKER 
WHERE  SYMBOL = 'MSFT';


select *
from   ticker
where  symbol = 'MXISX'
order by symbol;

update ticker
set    name = 'Great-West S&P SmallCap 600 Index Init'
where  name = 'Maxim S&P SmallCap 600 Index Initial' 
	   and symbol = 'MXISX';

commit;


delete FROM ticker
where  symbol = 'RWIGX';

commit;


select count(*)
from   ticker;


select *
from   ticker_type;

select max(id) from ticker;


insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(165, 'NONE', 'Wells Fargo Stable Return Fund N', null,false, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(166, 'PRRIX', 'PIMCO Real Return Instl', null,true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(167, 'PTTRX', 'PIMCO Total Return Instl', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(168, 'VBTIX', 'Vanguard Total Bond Market Index I', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(169, 'TRRIX', 'T.Rowe Price Retirement Income', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(170, 'TRRAX', 'T.Rowe Price Retirement 2010', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(171, 'TRRGX', 'T.Rowe Price Retirement 2015', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(172, 'TRRBX', 'T.Rowe Price Retirement 2020', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(185, 'TRRHX', 'T.Rowe Price Retirement 2025', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(186, 'TRRCX', 'T.Rowe Price Retirement 2030', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(173, 'TRRJX', 'T.Rowe Price Retirement 2035', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(174, 'TRRDX', 'T.Rowe Price Retirement 2040', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(175, 'TRRKX', 'T.Rowe Price Retirement 2045', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(176, 'TRRMX', 'T.Rowe Price Retirement 2050', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(177, 'PAFDX', 'T.Rowe Price Equity Income Adv', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(178, 'VINIX', 'Vanguard Institutional Index I', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(179, 'FCNTX', 'Fidelity Contrafund', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(180, 'FDGRX', 'Fidelity Growth Company', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(181, 'PMEGX', 'T.Rowe Price Instl Mid-Cap Equity Gr', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(182, 'NVSOX', 'Wells Fargo Advantage Small Cap Opp Adm', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(183, 'RERGX', 'American Funds EuroPacific Gr R6', null, true, true, 10, 3, null);

insert into ticker
(id, symbol, name, exchange, track, own, account_id, ticker_type_id, graph_url)
values
(189, 'DFEVX', 'DFA Emerging Markets Value I', null, true, true, 10, 3, null);



insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(198, 'ODVYX', 'Oppenheimer Developing Markets Y', 
	 null, true, true, 10, 3, null);

insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(199, 'PRRIX', 'PIMCO Real Return Instl', 
	 null, true, true, 10, 3, null);
	
insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(200, 'TRRAX', 'T. Rowe Price Retirement 2010', 
	 null, true, true, 10, 3, null);
	
insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(201, 'TRRGX', 'T. Rowe Price Retirement 2015', 
	 null, true, true, 10, 3, null);
	
insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(202, 'TRRNX', 'T. Rowe Price Retirement 2055', 
	 null, true, true, 10, 3, null);


insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(203, 'VWOB', 'Vangaurd Emerging Markets Government Bond', 
	 null, true, true, 1, 3, null);
	 
insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(204, 'VTIP', 'Vangaurd Short-Term Inflation-Protected Securities', 
	 null, true, true, 1, 3, null);
	 
insert into ticker
	(id, symbol, name, 
	 exchange, track, own, account_id, ticker_type_id, graph_url)
values
	(205, 'BNDX', 'Vangaurd Total International Bond', 
	 null, true, true, 1, 3, null);	 



commit;

rollback;







-- check if all the Vangaurd ETF's exist
select * from ticker t where  t.symbol = 'VHT';
select * from ticker t where  t.symbol = 'VCR';
select * from ticker t where  t.symbol = 'VEA';
select * from ticker t where  t.symbol = 'VGK';
select * from ticker t where  t.symbol = 'VPL';
select * from ticker t where  t.symbol = 'VBK';
select * from ticker t where  t.symbol = 'VUG';
select * from ticker t where  t.symbol = 'MGK';
select * from ticker t where  t.symbol = 'VOT';
select * from ticker t where  t.symbol = 'VSS';
select * from ticker t where  t.symbol = 'VXUS';
select * from ticker t where  t.symbol = 'VGT';
select * from ticker t where  t.symbol = 'VEU';
select * from ticker t where  t.symbol = 'VDC';
select * from ticker t where  t.symbol = 'VXF';
select * from ticker t where  t.symbol = 'VFH';
select * from ticker t where  t.symbol = 'VNQI';
select * from ticker t where  t.symbol = 'VT';
select * from ticker t where  t.symbol = 'VO';
select * from ticker t where  t.symbol = 'VWOB';
select * from ticker t where  t.symbol = 'VTI';
select * from ticker t where  t.symbol = 'VB';
select * from ticker t where  t.symbol = 'VV';
select * from ticker t where  t.symbol = 'MGC';
select * from ticker t where  t.symbol = 'VOO';
select * from ticker t where  t.symbol = 'VGIT';
select * from ticker t where  t.symbol = 'BSV';
select * from ticker t where  t.symbol = 'VCSH';
select * from ticker t where  t.symbol = 'BIV';
select * from ticker t where  t.symbol = 'VMBS';
select * from ticker t where  t.symbol = 'VTIP';
select * from ticker t where  t.symbol = 'VGSH';
select * from ticker t where  t.symbol = 'VCIT';
select * from ticker t where  t.symbol = 'VOE';
select * from ticker t where  t.symbol = 'BNDX';
select * from ticker t where  t.symbol = 'BND';
select * from ticker t where  t.symbol = 'MGV';
select * from ticker t where  t.symbol = 'VTV';
select * from ticker t where  t.symbol = 'VBR';
select * from ticker t where  t.symbol = 'VOX';
select * from ticker t where  t.symbol = 'VYM';
select * from ticker t where  t.symbol = 'VIG';
select * from ticker t where  t.symbol = 'VGLT';
select * from ticker t where  t.symbol = 'VNQ';
select * from ticker t where  t.symbol = 'VWO';
select * from ticker t where  t.symbol = 'BLV';
select * from ticker t where  t.symbol = 'VCLT';
select * from ticker t where  t.symbol = 'VIS';
select * from ticker t where  t.symbol = 'EDV';
select * from ticker t where  t.symbol = 'VAW';
select * from ticker t where  t.symbol = 'VPU';
select * from ticker t where  t.symbol = 'VDE';


13:10:21	select * from ticker t where  t.symbol = 'VWOB' LIMIT 0, 1000	0 row(s) returned	0.000 sec / 0.000 sec
13:10:22	select * from ticker t where  t.symbol = 'VTIP' LIMIT 0, 1000	0 row(s) returned	0.000 sec / 0.000 sec
13:10:22	select * from ticker t where  t.symbol = 'BNDX' LIMIT 0, 1000	0 row(s) returned	0.000 sec / 0.000 sec


