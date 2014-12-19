//function initialize(latitude, longitude) {
function initialize() {
		//alert("called here");
       var mapOptions = {
         center: { lat: 40.8088, lng: -73.95},
         zoom: 4
       };
       var my_map = new google.maps.Map(document.getElementById('map-canvasLive'), mapOptions);
       var marker_point = new google.maps.LatLng(40.8088, -73.95);
	   var tooltip_string = '<div>Hello World</div>';
	   
	   var marker = new google.maps.Marker({
		  position:marker_point, 
		  map:my_map,
		  title:tooltip_string
		  }) ;
		  
	   
	   /*var info_window = new google.maps.InfoWindow({
		   <!--content:tooltip_string -->
	   <!--}) */;
	   
	   google.maps.event.addListener(marker, 'mouseover', function(){
	      /*info_window.open(my_map, marker); */
		  tooltip.show(this.descrip);
	   });
	   
	   google.maps.event.addListener(marker, 'mouseout', function(){
	      /* info_window.open(my_map, marker); */
		  tooltip.hide();
	   });
	   google.maps.event.addDomListener(window, 'load', initialize);
} 
     
function loadMarkers(data) {
	alert(data);
	alert("Reached here");
	alert(data[0].content);
	alert(data[0].latitude);
	alert(data[0].longitude);
	alert(data[0].content);
	alert(data[0].screenName);
	var mapOptions = {
	         center: { lat: 40.8088, lng: -73.95},
	         zoom: 5
	       };
	var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
    var info_window = new google.maps.InfoWindow();
    var markers = [];
    var size = 0, key;
	alert(size);
    for (key in data) {
        if (data.hasOwnProperty(key)) size++;
    }
    for(i = 0; i < size; i++) {
        var info_window = get_info_window();

        var marker_pos = new google.maps.LatLng(data[i].latitude, data[i].longitude);

        marker_num = get_markers_count();

        marker = new google.maps.Marker({
            position: marker_pos,
            map: map,
            title:data[i].content
        });

        markers.push(marker);
        marker.set('retailer', data[i].screenName);

        google.maps.event.addListener(marker, 'click', function() {
            var retailer = this.get('retailer');

            info_window.setContent(retailer['name']);
            info_window.open(map, this);
        });
    }
    //google.maps.event.addDomListener(window, 'load', initialize);
}  