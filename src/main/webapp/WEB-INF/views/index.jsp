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
	<div id="realtimeKeyword">
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
<script src="//d3js.org/d3.v2.min.js"></script>
<script>

var width = 960,
    height = 500

var svg = d3.select("body").append("svg")
    .attr("width", width)
    .attr("height", height);

var force = d3.layout.force()
    .gravity(0.05)
    .distance(100)
    .charge(-100)
    .size([width, height]);

d3.json("api/getJsonFile", function(error, json) {
  if (error) throw error;

  force
      .nodes(json.nodes)	
      .links(json.links)
      .start();

  var link = svg.selectAll(".link")
      .data(json.links)
    .enter().append("line")
      .attr("class", "link");

  var node = svg.selectAll(".node")
      .data(json.nodes)
    .enter().append("g")
      .attr("class", "node")
      .call(force.drag);

  node.append("image")
      .attr("xlink:href", "https://github.com/favicon.ico")
      .attr("x", -8)
      .attr("y", -8)
      .attr("width", 16)
      .attr("height", 16);

  node.append("text")
      .attr("dx", 12)
      .attr("dy", ".35em")
      .text(function(d) { return d.name });

  force.on("tick", function() {
    link.attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

    node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
  });
});

</script>
<style>

.link {
  stroke: #ccc;
}

.node text {
  pointer-events: none;
  font: 10px sans-serif;
}
</style>
</body>
</html>