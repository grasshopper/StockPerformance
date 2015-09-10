<?xml version="1.0" encoding="ISO-8859-1" ?>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	

	<div class="sectionHeading">
		Moving Average
	</div>

	<select id="selectMovingDayAverage" class="dropdownlist" name="selectedMovingDayAverage" onchange="movingDaySelectionChange(this)">
		<c:set var="selectedId" value="${MovingAverageOptions.selectedAverage.id}" />

		<option value="" selected id="${selectedId}" />
		<c:forEach var="average" items="${MovingAverageOptions.movingAverageOptions}">
			<c:set var="selected" value=""/>
			<c:if test="${(selectedId == average.id)}">
				<c:set var="selected" value="selected"/>
			</c:if>
			<option value="${average.id}" ${selected}>
				<c:out value="${average.displayValue}" />
			</option>
		</c:forEach>
	</select>

	
	<c:set var="MovingAveragesForAllSelectedSecurities" value="${StockPriceHistory}" />
	
	<br/>
	History From Date: <c:out value="${MovingAveragesForAllSelectedSecurities.historyFromDate}" />
	<br/>
	History To Date: <c:out value="${MovingAveragesForAllSelectedSecurities.historyToDate}" />
	<br/>
	Selected Moving Average: <c:out value="${MovingAveragesForAllSelectedSecurities.movingAverage}" />
	<br/>
	<br/>
	
	
	
	<br/>
	<br/>
	
	<TABLE id="stockpricehistory" summary="This table charts the historical stock prices for the selected stocks">
		<CAPTION>Historical Stock Prices</CAPTION>
		<THEAD> 
			<TR> 
				<TH id="stockid">
					Stock ID
				</TH>
				<TH id="stockname">
					Stock Name
				</TH>
				<TH class="prices">
					Prices
				</TH>
			</TR>
		</THEAD>
		<TBODY>
			<c:forEach var="history" items="${MovingAveragesForAllSelectedSecurities.stockPriceHistoryMap}">
				<TR>
					<TD>
						<c:out value="${history.key}" />
					</TD>
					<TD>
						<c:out value="${history.value.name}" />
					</TD>
					<TD>
					
<!-- 
	< % @ include file="/WEB-INF/views/displayStockGraph.jsp" % >


						<script type="text/javascript" src="resources/javascript/main.js" />
						<SCRIPT type="text/javascript">
							function myFunction( test ) {
								alert( test );
								console.log("Starting myFunction with param value: " + test);
							}
						</SCRIPT>
						<SCRIPT type="text/javascript">
							myFunction("hello");
						</SCRIPT>
 -->						


						<c:forEach var="average" items="${history.value.movingAverage}">
							<c:out value="${average.priceDate}" />:&nbsp;<c:out value="${average.price}" />&nbsp;
						</c:forEach>

					</TD>
				</TR>
			</c:forEach>
		</TBODY>			
	</TABLE>
	
<script>
    window.onload = function() {
        alert("hello");
    }
</script>

	
	<!-- did we get the json object -->
	<br/>
	StockPriceHistoryJson: <c:out value="${StockPriceHistoryJson}" />
	<br/>
	
	
	<br/>
	<P class="serverTime">The time on the server is ${serverTime}.</P>
