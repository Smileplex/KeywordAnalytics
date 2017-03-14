<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>

<title>Insert title here</title>
</head>
<body>
	<div id="realtimeKeyword" style="float: left;">
		<h5>네이버</h5>
		<ul data-agentId='1'>

		</ul>
		<br />
		<h5>다음</h5>
		<ul data-agentId='2'>

		</ul>
	</div>


	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script>
$(document).ready(function(){
	$.ajax({
		url: "api/getAllRealtimeKeywords", 
		success: function(result){
			 $container = $("#realtimeKeyword");
				for(var i=0; i<result.length; i++){
			        $container.find("ul[data-agentId='"+result[i].agentId+"']").append("<li>"+result[i].name+"</li>");
		        }
				
		}});
	$(document).on('click','#realtimeKeyword ul li',function(){
		//alert($(this).text() + " | " + $(this).parent().attr('data-agentId'));
		$.ajax({
			type:"POST",
			url: "api/getRelatedKeyword", 
			data: {name:$(this).text(), agentId:$(this).parent().attr('data-agentId')},
			success: function(result){console.log(result)}
		});
	});
});
</script>
	<style>
.link {
  stroke: #ccc;
}

.node text {
  pointer-events: none;
  font: 13px sans-serif;
  color:red;
}
</style>
	<svg width="1280" height="1024"></svg>
	<script src="https://d3js.org/d3.v4.min.js"></script>
	<script>

var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var color = d3.scaleOrdinal(d3.schemeCategory20);

var simulation = d3.forceSimulation()
    .force("link", d3.forceLink().id(function(d) { return d.id; }).distance(200))
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width / 2, height / 2));
   

d3.json("api/getJsonFile", function(error, graph) {
  if (error) throw error;

  /*
  var link = svg.append("g")
      .attr("class", "links")
    .selectAll("line")
    .data(graph.links)
    .enter().append("line")
      .attr("stroke-width", function(d) { return Math.sqrt(d.value); });
  */
  var link = svg.selectAll(".link")
  .data(graph.links)
  .enter().append("line")
  .attr("class", "link");

  /*
  var node = svg.append("g")
      .attr("class", "nodes")
    .selectAll("circle")
    .data(graph.nodes)
    .enter().append("circle")
      .attr("r", 5)
      .attr("fill", function(d) { return color(d.group); })
      .call(d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended));
  */

  var node = svg.selectAll(".node")
  .data(graph.nodes)
	.enter().append("g")
	  .attr("class", "node")
	  .call(d3.drag()
		  .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended));	  
	  
	
	node.append("image")
	  .attr("xlink:href", "https://production-assets.codepen.io/assets/favicon/favicon-8ea04875e70c4b0bb41da869e81236e54394d63638a1ef12fa558a4a835f1164.ico")
	  .attr("x", -8)
	  .attr("y", -8)
	  .attr("width", 24)
	  .attr("height", 24);
	
	node.append("text")
	  .attr("dx", 20)
	  .attr("dy", ".35em")
	  .text(function(d) { return d.id });

  simulation
      .nodes(graph.nodes)
      .on("tick", ticked);

  simulation.force("link")
      .links(graph.links);

  function ticked() {
    link
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
  }
});

function dragstarted(d) {
  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
  d.fx = d.x;
  d.fy = d.y;
}

function dragged(d) {
  d.fx = d3.event.x;
  d.fy = d3.event.y;
}

function dragended(d) {
  if (!d3.event.active) simulation.alphaTarget(0);
  d.fx = null;
  d.fy = null;
}

</script>
</body>
</html>