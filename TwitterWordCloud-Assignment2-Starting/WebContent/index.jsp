<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">   
	<title>Twitter Sentiment Analysis</title>
	<script src="javascript/jquery-1.9.1.js"></script>
	<script src="javascript/d3.js"></script>
	<script src="javascript/d3.layout.clouds.js"></script>
	<script src="javascript/bootstrap.min.js"></script>	
	<script src="javascript/highcharts-custom.js"></script>	
	<link href="css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
    <div class="well">
    	<div class="page-header">
		  <h1 align="center"> Twitter Sentiment Analysis <small> Politics </small></h1>
		  
		  <div class="btn-group pull-right" role="group" aria-label="...">
		  	  <button type="button" class="btn btn-default btn-lg" onClick="showWordCloud()">View Trend</button>
			  <button type="button" class="btn btn-default btn-lg" onClick="showTimeLine()">View TimeLine</button>
		  </div>
		</div>			  	
	</div>
	<div id="progressBar" class="progress" style="width:25%;display:none" align="center">
	  <div class="progress-bar progress-bar-striped active" role="progressbar" style="width:100%">
	    Loading...
	  </div>
	</div>
	<div id="graph1" class="panel panel-default" style="display:none">
	  <div class="panel-body container">
	    <div class="row">
	  		<div id="wordCloud" class="col-md-6" ></div>
	  		<div id="barGraph" class="col-md-6"></div>
	  	</div>
	  </div>
	</div>
	<div id="graph2" class="panel panel-default" style="display:none">
	  <div class="panel-body container">
	    <div id="timeGraph"></div>
	  </div>
	</div>
	<script type="text/javascript">
		var tweets = [];
		var posTimes = [
	                    [Date.UTC(2014,  04, 2), 2 ],
	                    [Date.UTC(2014,  04, 3), 5 ],
	                    [Date.UTC(2014,  04, 4), 20],
	                    [Date.UTC(2014,  05,  5), 3 ]
	                ];
		var negTimes = [
	                    [Date.UTC(2014,  04, 18), 1 ],
	                    [Date.UTC(2014,  04, 19), 5 ],
	                    [Date.UTC(2014,  04, 20), 10],
	                    [Date.UTC(2014,  05, 21), 12]
	                ];
		var positive = 0;
		var negative = 0;
		var updateBarGraph = function(){
		$(function () {
			    //Sentiment Count
			    document.getElementById('graph1').style.display = "block";
			    document.getElementById('graph2').style.display = "none";
			    $('#barGraph').highcharts({
			        chart: {
			            type: 'column'
			        },
			        title: {
			            text: 'Sentiment Analysis'
			        },
			        xAxis: {
			            categories: ['Positive', 'Negative']
			        },
			        credits: {
			            enabled: false
			        },
			        series: [{
			            name: 'Sentiment Score',
			            data: [positive, negative]
			        }]
			    });		    	
			});
		}
		var updateTimeGraph = function(){
			$(function () {
				//Timeseries
				document.getElementById('graph1').style.display = "none";
				document.getElementById('progressBar').style.display = "none";
			    document.getElementById('graph2').style.display = "block";
			    	$('#timeGraph').highcharts({
			            chart: {
			                type: 'spline'
			            },
			            title: {
			                text: 'Timeline Sentiment Analysis '
			            },
			            subtitle: {
			                text: 'Period:April 21 to April 30 2014'
			            },
			            xAxis: {
			                type: 'datetime',
			                dateTimeLabelFormats: { // don't display the dummy year
			                    month: '%e. %b',
			                    year: '%b'
			                },
			                title: {
			                    text: 'Date'
			                }
			            },
			            yAxis: {
			                title: {
			                    text: 'Sentiment Count'
			                },
			                min: 0
			            },
			            tooltip: {
			                headerFormat: '<b>{series.name}</b><br>',
			                pointFormat: '{point.x:%e. %b}: {point.y:.2f} m'
			            },

			            series: [{
			                name: 'Positive Sentiment',
			                	data: posTimes
			            }, {
			                name: 'Negative Sentiments',
			                data: negTimes
			            }]
			        });
		    	});
			}
			var showWordCloud = function(){
				if(tweets.length>0){
					updateCloud(tweets);
					updateBarGraph();
			    }
			    else {
			    	document.getElementById('progressBar').style.display = "block";			    	
			        setTimeout(computeWC, 1000); // check again in a second
			    }
			}	
			var showTimeLine = function(){
				  //computeTL();
				  document.getElementById('progressBar').style.display = "block";
				  updateTimeGraph();		    			        
			}
			function computeTL() {
				$.ajax({
				    	url: "Tweet",
				    	type: "Post",
				    	dataType: 'json',
				    	success: function(data) {
				    		times=[];
				    		for(var i=0;i<Object.keys(data).length;i++){
				    			var timeline= {date:Date.UTC(2014 ,data[i].getUTCMonth(), data[i].getUTCDate), count:data[i].value};
								if(data[i].value<0)
								{
									posTimes.push(timeline);
								}
								else
								{
									negTimes.push(timeline);
								}
								console.log(times);
							}
				    	}
				   }); 
			}
			function computeWC() {
				$.ajax({
				    	url: "Tweet",
				    	type: "Post",
				    	dataType: 'json',
				    	success: function(data) {
				    		tweets=[];
				    		for(var i=0;i<Object.keys(data).length;i++){
				    			var tweet= {text:data[i].key, color:data[i].color, count:data[i].value, size:(Math.abs(data[i].value)+50)};
								tweets.push(tweet); 
								if(data[i].value<0)
								{
									negative = negative + Math.abs(data[i].value);
								}
								else
								{
									positive = positive + data[i].value;
								}
								console.log(tweets);
							}
				    	}
				   }); 						
			}
			
			var fill = d3.scale.category20();
		  	
		  	function updateCloud(tweets){
		 		d3.layout.cloud().size([500, 500])
		      					 .words(tweets)
		      					 .padding(5)
		      					 .rotate(function() { return ~~(Math.random() * 2) * 90; })
		      					 .font("Impact")
		      					 .fontSize(function(d) { return d.size; })
		      					 .on("end", draw)
		      					 .start();

			 	
		  	}
		  
		  	function draw(tweets) {
			  	var svg;
			  	document.getElementById('graph1').style.display = "block";
			  	document.getElementById('progressBar').style.display = "none";
			  	$("#wordCloud").empty();
			  	if (d3.select("#wordCloud").selectAll("svg")[0][0] == undefined){
		         	var svg = d3.select("#wordCloud").append("svg")
		                  		.attr("width", 500)
		                  		.attr("height", 500)
		                  		.append("g")
		                  		.attr("transform", "translate(300, 300)");        
		     	}
			  	else{
		    		var svg =  d3.select("#wordCloud").selectAll("svg")
		            	  		.attr("width", 500)
		              	 		.attr("height", 500)
		              			.select("g")
		              			.attr("transform", "translate(300,300)");
		      	}
			  
			    svg.selectAll("text")
			        	.data(tweets)
			      		.enter().append("text")
			        		.style("font-size", function(d) { return d.size + "px"; })
			        		.style("font-family", "Impact")
			        		.style("fill", function(d, i) { return d.color; })
			        		.attr("text-anchor", "middle")
			        		.attr("transform", function(d) {return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";})
			        	.text(function(d) { return d.text; });
			}		  
	</script>
</body>
</html>