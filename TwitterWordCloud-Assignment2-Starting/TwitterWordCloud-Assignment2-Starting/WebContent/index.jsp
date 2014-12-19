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
	<link href="css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
    <script src="js/bootstrap.min.js"></script>
	<div class="container">
	  	<div class="dropdown">
		  <button class="btn btn-default dropdown-toggle" type="button" id="" data-toggle="dropdown" aria-expanded="true">
		    Select Topic
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownTopic">
		    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Finance</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">News</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Sports</a></li>
		    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Weather</a></li>
		  </ul>
		</div>
	</div>
	<!--<div id="wordCloud" style="float:left;border:solid 1px black"></div>-->
	<script type="text/javascript">	 	
		  	var fill = d3.scale.category20();
		  	var filters = ["Obama","microsoft","India","Mumbai","Columbia","Ebola","NYC","boy","girl","love","hate","suarez","weather"].map(function(d) {
		      	return {text: d, size: 5, count:0};
		  	});
		  	var updates = 0;
		
		 	function updateCloud(filters){
			  	//console.log(filters);
			  	d3.layout.cloud().size([900, 600])
		      					 .words(filters)
		      					.padding(5)
		      					.rotate(function() { return ~~(Math.random() * 2) * 90; })
		      					.font("Impact")
		      					.fontSize(function(d) { return d.size; })
		      					.on("end", draw)
		      					.start();
		  	}
		  
		  	function draw(filters) {
			  	var svg;
			  	$("#map-canvas").empty();
			  	//$("#wordCloud").empty();
			  	if (d3.select("#map-canvas").selectAll("svg")[0][0] == undefined){
		         	var svg = d3.select("#map-canvas").append("svg")
		                  		.attr("width", 900)
		                  		.attr("height", 600)
		                  		.append("g")
		                  		.attr("transform", "translate(400, 300)");        
		     	}
			  	else{
		    		//console.log("already found");
		    	  	var svg =  d3.select("#map-canvas").selectAll("svg")
		            	  		.attr("width", 900)
		              	 		.attr("height", 600)
		              			.select("g")
		              			.attr("transform", "translate(400,300)");
		      	}
			  
			    svg.selectAll("text")
			        	.data(filters)
			      		.enter().append("text")
			        		.style("font-size", function(d) { return d.size + "px"; })
			        		.style("font-family", "Impact")
			        		.style("fill", function(d, i) { return fill(i); })
			        		.attr("text-anchor", "middle")
			        		.attr("transform", function(d) {return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";})
			        	.text(function(d) { return d.text; });
			}
		  
		  	function updateWords(){
		  		document.getElementById('map-canvas').innerHTML = "Loading Please wait...";
		  		clearGauges();
			    $.ajax({
			    	url: "Tweet",
			    	type: "Post",
			    	dataType: 'json',
			    	data: {filterByKeyword:"All"},
			    	success: function(data) {
			     		var totalCount = 0;
			     		for(var i=0;i<Object.keys(data).length;i++){
			      			for(var j = 0; j<filters.length; j++){
			       				if(filters[j].text.toLowerCase() == data[i].keyword.toLowerCase()){
			        				totalCount++;
			        				filters[j].count++;
			       				}
			      			}
			     		}
			     		for(var k = 0; k<filters.length; k++){
         					//var relativeCount = filters[k].count / totalCount;
         					//filters[k].size = (relativeCount * 40) + (10 * filters[k].count);
         					filters[k].size = ((filters[k].count / totalCount) * 300) + 30;
         					console.log(filters[k].text.toLowerCase() + ":" + filters[k].count + ":" + filters[k].size);
        				}
			     		updateCloud(filters);
			    	}
			   });
			    
			}
	</script>
</body>
</html>