<%@ page language="java" import="java.sql.*, java.io.*, java.util.Date, java.util.*,javax.servlet.*, java.text.SimpleDateFormat, java.util.Calendar " %>
<%  Class.forName("oracle.jdbc.driver.OracleDriver"); %>
 <%
     Connection connection=DriverManager.getConnection ("jdbc:oracle:thin:@RAC1.dinu.com:1521:orcl2","cog","cog123");
     Statement statement12 = connection.createStatement();
          ResultSet resultset12 = 
            statement12.executeQuery("select * from(select INSTANCE_NAME,PCPU from ORA_CPU_STATUS order by PCPU desc) where rownum<=10");


List<String> list = new ArrayList<String>();
List<String> list2 = new ArrayList<String>();
while(resultset12.next())
{
    String val = resultset12.getString(1);
    list.add(val);
    String val2 = resultset12.getString(2);
    list2.add(val2);

}

String csv = list2.toString();
String csvWithQuote = list.toString().replace("[", "['").replace("]", "']").replace(", ", "','");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Highcharts Example</title>
<script type="text/javascript" src="/DBdashboard/1.js"></script>
<script type="text/javascript" src="/DBdashboard/2a.js"></script>
<script type="text/javascript" src="/DBdashboard/3a.js"></script>
<script type="text/javascript" src="/DBdashboard/highcharts-more.js"></script>
<script type="text/javascript" src="/DBdashboard/json2.js"></script>
<script>
$(function () {
    var dincpu=<%=csv%>;
    var dinpcat = <%=csvWithQuote%>;
    var input = dincpu,
        data = [],
        categories =dinpcat;
        $.each(input, function(index, value){
            var color;
            if (value > 80) color = 'red';
            else if (value > 60) color = 'Orange';
            else color = 'green';
            data.push({y:value, color: color, url:'https://www.google.com'});
        });

    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'COL',
            type: 'column'
        },
        title: {
            text: 'Current Top 10 CPU Consumers',
            style: {fontSize: '10px'}
        },
        xAxis: {
            categories: categories,
            labels: {
                rotation: -35,
                align: 'center'
            }
        },
        yAxis: {
            title: {
                text: 'Percentage',
                style: {fontSize: '11px'}
            }
        },
        exporting: { enabled: false },
        legend: {
            enabled: false,
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.x +'</b>' +'- Oracle User Process CPU Consumed :'+'<b>'+ this.y +' % ' +'</b>' ;
            }
        },
        plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function() {
                            location.href = this.options.url;
                        }
                    }
                }
            }
        },
        series: [{
            name: 'CPU Consumed',
            pointWidth: 28,
            data: data

        }]
    });
});
</script>
</head>
<body>
<div id="COL" style="min-width: 100px; height: 300px; margin: 0 auto"></div>
</body>
</html>