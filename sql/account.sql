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


INSERT INTO account
(ID, NAME, PARENT_ID)
values
(10, 'Angela Level 3 401K', null);



select * 
from   account;

