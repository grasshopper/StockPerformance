/**
 * 
 */

	function accountSelectionChange(object) {
		console.log("Starting accountSelectionChange");
		console.log(object.options[object.selectedIndex].value);
		console.log(object.options[object.selectedIndex].text);
		
		$.ajax({
			type : 'POST',
			url : 'viewAccountStocks.html', 
			data : 'accountId=' + object.options[object.selectedIndex].value,
			dataType: 'html',				
			success: function(msg) { 
                // alert( "Data retrieved: " + msg );
                
				document.getElementById("myDiv").innerHTML = msg;
            },
            error: function (jqxhr, status, errorThrown) {
                alert("Failure, Unable to recieve content")
                alert(jqxhr.responseText);
            }
		});
	}

	
	function stockSelectionChange(object) {
		console.log("Starting  stockSelectionChange");
		var atLeastOneIsChecked = $('input[name="stocksChosen"]:checked').length > 0;
		console.log("Is there at least one checked stock? " + atLeastOneIsChecked);
		
		var checkValues = $('input[name=stocksChosen]:checked').map(function() {
		    return $(this).attr('id');
		}).get();
		
		console.log("checkValues: " + checkValues);
		
		// a second way of getting the checked stocks
		/*
		var boxes = $('input[name="stocksChosen"]:checked');
		console.log("Number of checked stock: " + $(boxes).size());
		$(boxes).each(function(){
			console.log("Stock is checked: " + this.id);
		});
		
		console.log("Checked boxes: " + boxes);
		*/
		
		alert("Making an Ajax call for URL viewStocksMovingAverage.html");
		// make the ajax call to display the graph for the selected stocks		
		$.ajax({
			type : 'POST',
			url : 'viewStocksMovingAverage.html', 
			data : "stockIds=" + checkValues,
			dataType: 'html',				
			success: function(msg) { 
				document.getElementById("stockGraph").innerHTML = msg;
				
				// drawSampleChart();
            },
            error: function (jqxhr, status, errorThrown) {
                alert("Failure, unable to recieve content")
                alert(jqxhr.responseText);
            }
		});
	}
	
	/*
	 * Handle user selection from the Moving Average Days drop-down listbox 
	 */
	function movingDaySelectionChange(movingAvgDaysObj) {
		console.log("Starting movingDaySelectionChange");
		console.log("Moving Average Days value: " + movingAvgDaysObj.options[movingAvgDaysObj.selectedIndex].value);
		console.log("Moving Average Days text: " + movingAvgDaysObj.options[movingAvgDaysObj.selectedIndex].text);
		
		// make the ajax call to display the graph for the selected stocks		
		$.ajax({
			type : 'POST',
			url : 'updateStocksMovingAverage.html', 
			data : 'movingAvgDaysId=' + movingAvgDaysObj.options[movingAvgDaysObj.selectedIndex].value,
			dataType: 'html',				
			success: function(msg) { 
				document.getElementById("stockGraph").innerHTML = msg;
				
				// drawSampleChart();
            },
            error: function (jqxhr, status, errorThrown) {
                alert("Failure, Unable to recieve content")
                alert(jqxhr.responseText);
            }
		});
	}


	function myFunction( test ) {
		alert( test );
		console.log("Starting myFunction with param value: " + test);
	}
    
	
	function drawSampleChart() {
		console.log("Starting drawSampleChart");
	    $('#container').highcharts({
	        chart: {
	            type: 'bar'
	        },
	        title: {
	            text: 'Fruit Consumption'
	        },
	        xAxis: {
	            categories: ['Apples', 'Bananas', 'Oranges']
	        },
	        yAxis: {
	            title: {
	                text: 'Fruit eaten'
	            }
	        },
	        series: [{
	            name: 'Jane',
	            data: [1, 0, 4]
	        }, {
	            name: 'John',
	            data: [5, 7, 3]
	        }]
	    });
	}
	

	
	function WorkedaccountSelectionChange(object) {
		console.log(object.optsions[object.selectedIndex].value);
		console.log(object.options[object.selectedIndex].text);
		
		
		// configure the XMLHttpRequest
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		
		// setup the handling of the return value from the server call
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
			}
		}
		
		xmlhttp.open("GET", "viewAccountStocks.html", true);
		xmlhttp.send();
	}
	
	function OldAccountSelectionChange(object) {
		console.log(object.options[object.selectedIndex].value);
		console.log(object.options[object.selectedIndex].text);

		// configure the XMLHttpRequest
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		// setup the handling of the return value from the server call
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
			}
		}
		
		// make the server call
//		xmlhttp.open("POST", "viewAccountStocks.html", true);
//		xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
//		xmlhttp.send("fname=Henry&lname=Ford");
		
		xmlhttp.open("GET", "viewAccountStocks.html", true);
		xmlhttp.send();
	}

