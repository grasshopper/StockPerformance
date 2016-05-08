select sysdate() from dual;


ALTER TABLE stock_quote_history CHANGE COLUMN TICKER_SYMBOL_ID TICKER_ID int;

ALTER TABLE STOCK_QUOTE_HISTORY ADD UNIQUE INDEX (TICKER_ID, QUOTE_DATE);

ALTER TABLE STOCK_QUOTE_HISTORY DROP INDEX LAST_TRADE_FOR_SYMBOL;
ALTER TABLE STOCK_QUOTE_HISTORY DROP INDEX ID;
ALTER TABLE STOCK_QUOTE_HISTORY DROP INDEX TICKER_SYMBOL_NDX;
ALTER TABLE STOCK_QUOTE_HISTORY DROP INDEX TICKER_ID;
ALTER TABLE STOCK_QUOTE_HISTORY DROP INDEX QUOTE_BY_DAY_NDX;


describe STOCK_QUOTE_HISTORY;

show indexes 
from STOCK_QUOTE_HISTORY;


SELECT COUNT(*) FROM STOCK_QUOTE_HISTORY;


SELECT MAX(LAST_TRADE_AMOUNT) FROM STOCK_QUOTE_HISTORY;

select *
from   stock_quote_history;

desc stock_quote_history;


-- delete from stock_quote_history
-- where  id != 0;

-- commit;

-- how many rows of data do we have per stock
select sqh.TICKER_ID, count(*)
from   stock_quote_history sqh
group by sqh.TICKER_ID
order by count(*) asc, sqh.TICKER_ID asc;

select sqh.TICKER_ID, count(*)
from   stock_quote_history sqh
group by sqh.TICKER_ID
order by sqh.TICKER_ID asc;

select sqh.*
from   stock_quote_history sqh
where  sqh.ticker_id = 108;

INSERT INTO STOCK_QUOTE_HISTORY 
(TICKER_ID, QUOTE_DATE, LAST_TRADE_AMOUNT, LAST_TRADE_DATE_TIME, CHANGE_AMOUNT, OPEN_AMOUNT, DAY_HIGH_AMOUNT, DAY_LOW_AMOUNT, VOLUME, PREVIOUS_CLOSE, CHANGE_PERCENT, FIFTY_TWO_WEEK_RANGE, EARNING_PER_SHARE, PRICE_PER_EARNINGS, AVERAGE_DAILY_VOLUME) 
VALUES (106, '2015-07-21 00:00:00.0', '48.200001', '2015-07-21 00:00:00.0', null, '48.200001', '48.200001', '48.200001', '0', null, null, null, null, null, '0')





-- remove history from stocks if the stock does not have a full history
delete from stock_quote_history
where TICKER_ID in (191);

commit;


-- stock prices by symbol id
select sqh.*
from   stock_quote_history sqh
where TICKER_ID in (192)
order by sqh.quote_date desc;



select sqh.*
from   stock_quote_history sqh
       inner join ticker t
         on t.id = sqh.ticker_id
where  T.SYMBOL = 'MSFT';
  

-- clear historical prices from DB
truncate table stock_quote_history;

commit;







select *
from   STOCK_QUOTE_HISTORY sqh
where  sqh.TICKER_ID = 1
	   and sqh.QUOTE_DATE < '2010-12-17 00:00:00'
order by sqh.last_trade_date_time ;






-- find two missing dates in a row
SELECT sqh1.ticker_id, t.name, (sqh1.QUOTE_DATE) + INTERVAL 2 DAY AS MISSING_DATE
FROM   STOCK_QUOTE_HISTORY sqh1
       LEFT OUTER JOIN STOCK_QUOTE_HISTORY sqh2 
	     ON DATE(sqh1.QUOTE_DATE) = DATE(sqh2.QUOTE_DATE) - INTERVAL 2 DAY
            AND sqh2.TICKER_ID = sqh1.TICKER_ID
       LEFT OUTER JOIN TICKER t
         ON t.ID = sqh1.TICKER_ID
WHERE  sqh1.QUOTE_DATE BETWEEN '2010-01-01' AND current_date() 
       AND sqh2.QUOTE_DATE IS NULL
       AND sqh1.TICKER_ID is not null
       AND sqh1.TICKER_ID = 1
ORDER BY t.name ASC, MISSING_DATE ASC;


SELECT SQH.QUOTE_DATE, 
	   TIMESTAMPDIFF(DAY, SQH.QUOTE_DATE, 
       (SELECT SQH2.QUOTE_DATE
			FROM   stock_quote_history SQH2 
			WHERE  SQH2.QUOTE_DATE > SQH.QUOTE_DATE
				   AND SQH2.TICKER_ID = SQH.TICKER_ID
			ORDER BY SQH2.QUOTE_DATE ASC
			LIMIT 1)) - 1 AS DAYS_MISSING_DATA
FROM   stock_quote_history SQH
WHERE  SQH.TICKER_ID = 1
	   AND SQH.QUOTE_DATE + interval 1 DAY < (SELECT SQH1.QUOTE_DATE 
											  FROM   stock_quote_history SQH1 
											  WHERE  SQH1.QUOTE_DATE > SQH.QUOTE_DATE
													 AND SQH1.TICKER_ID = SQH.TICKER_ID
											  ORDER BY SQH1.QUOTE_DATE ASC
											  LIMIT 1)
HAVING DAYS_MISSING_DATA > 2
ORDER BY SQH.QUOTE_DATE;





SELECT SQH.QUOTE_DATE, SQH.QUOTE_DATE + interval 1 DAY AS NEXT_DAY,
	   TIMESTAMPDIFF(DAY, SQH.QUOTE_DATE, 
       (SELECT SQH2.QUOTE_DATE
			FROM   stock_quote_history SQH2 
			WHERE  SQH2.QUOTE_DATE > SQH.QUOTE_DATE
				   AND SQH2.TICKER_ID = SQH.TICKER_ID
			ORDER BY SQH2.QUOTE_DATE ASC
			LIMIT 1)) - 1 AS DAYS_MISSING_DATA
FROM   stock_quote_history SQH
WHERE  SQH.TICKER_ID = 1
	   AND SQH.QUOTE_DATE + interval 1 DAY < (SELECT SQH1.QUOTE_DATE 
											  FROM   stock_quote_history SQH1 
											  WHERE  SQH1.QUOTE_DATE > SQH.QUOTE_DATE
													 AND SQH1.TICKER_ID = SQH.TICKER_ID
											  ORDER BY SQH1.QUOTE_DATE ASC
											  LIMIT 1)
ORDER BY SQH.QUOTE_DATE;





SELECT SQH.QUOTE_DATE
FROM   stock_quote_history SQH
WHERE  SQH.TICKER_ID = 1
ORDER BY SQH.QUOTE_DATE;


SELECT SQH.QUOTE_DATE, SQH.QUOTE_DATE + interval 1 DAY AS NEXT_DAY,
	   (SELECT SQH1.QUOTE_DATE 
		FROM   stock_quote_history SQH1 
        WHERE  SQH1.QUOTE_DATE > SQH.QUOTE_DATE
        ORDER BY SQH1.QUOTE_DATE ASC
        LIMIT 1)
FROM   stock_quote_history SQH
WHERE  SQH.TICKER_ID = 1
ORDER BY SQH.QUOTE_DATE;




SELECT sqh1.ticker_id, t.name, (sqh1.QUOTE_DATE)  + INTERVAL 10 DAY AS MISSING_DATE
FROM   STOCK_QUOTE_HISTORY sqh1
       LEFT OUTER JOIN STOCK_QUOTE_HISTORY sqh2 
	     ON DATE(sqh1.QUOTE_DATE) = DATE(sqh2.QUOTE_DATE) - INTERVAL 10 DAY
            AND sqh2.TICKER_ID = sqh1.TICKER_ID
       LEFT OUTER JOIN TICKER t
         ON t.ID = sqh1.TICKER_ID
WHERE  sqh1.QUOTE_DATE BETWEEN '2010-01-01' AND current_date() 
       AND sqh2.QUOTE_DATE IS NULL
--       AND sqh1.TICKER_ID is not null
       AND sqh1.TICKER_ID = 1
ORDER BY t.name ASC, MISSING_DATE ASC;




SELECT sqh1.QUOTE_DATE
FROM   STOCK_QUOTE_HISTORY sqh1
WHERE  sqh1.QUOTE_DATE < current_date();
