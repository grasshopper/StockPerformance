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

select * from ticker_type;

select *
from   ticker;

select count(*)
from   ticker;


select *
from   ticker_type;


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



commit;

rollback;







