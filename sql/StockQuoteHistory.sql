select sysdate() from dual;

select *
from   stock_quote_history;

-- delete from stock_quote_history
-- where  id != 0;

-- commit;

select sqh.TICKER_SYMBOL_ID, count(*)
from   stock_quote_history sqh
where  sqh.TICKER_SYMBOL_ID > 164
group by sqh.TICKER_SYMBOL_ID;