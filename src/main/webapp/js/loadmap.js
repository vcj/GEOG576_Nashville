var map;
var place;
var autocomplete;
var infowindow = new google.maps.InfoWindow();

function initialization() {
    showAllReports();
    initAutocomplete();
}

function showAllReports() {
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: { "tab_id": "1"},
        success: function(reports) {
            //console.log("loadmap.js map initialization, print reports" + JSON.stringify(reports));
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("An AJAX error occured: " + status + "\nError: " + error);
        }
    });
}

function mapInitialization(reports) {


    var mapOptions = {
        mapId : "2ef0a683384d2e45", // Set the type of Map
    };



    // Render the map within the empty div
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    // Configure the click listener.
    map.addListener("click", (mapsMouseEvent) => {
        // Close the current InfoWindow.
        // Create a new InfoWindow.
        function setLat(){
            var t = JSON.parse(JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2));
            var valueLat = t['lat'];
            document.getElementById("lat").value=valueLat;
            var valueLon = t['lng'];
            document.getElementById("lon").value=valueLon;
        }
        //console.log(JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2));

        setLat();
    });


    var bounds = new google.maps.LatLngBounds (
    );

    //create the icon constructors
    var icons = {
        historical_marker: {
            url: 'img/historical-brown.png',
            scaledSize: new google.maps.Size(30, 23)
        },
        art_in_public_places: {
            url: 'img/art-pink.png',
            scaledSize: new google.maps.Size(45, 45)
        }
    };

    $.each(reports, function(i, e) {
        var long = Number(e['longitude']);
        var lat = Number(e['latitude']);
        var latlng = new google.maps.LatLng(lat, long);

        bounds.extend(latlng);

        // Create the infoWindow content
        var contentStr = '<h4>Site Details</h4><hr>';
        //pulled this out while I was setting up the map
        contentStr += '<p><b>' + 'Site Name' + ':</b>&nbsp' + e['title'] + '</p>';
        contentStr += '<p><b>' + 'Description' + ':</b>&nbsp' + e['description'] +
            '</p>';
        contentStr += '<p><b>' + 'Address' + ':</b>&nbsp' + e['location'] +
            '</p>';
        contentStr += '<p><b>' + 'Type' + ':</b>&nbsp' + e['site_type'] +
            '</p>';
        contentStr += '<p><b>' + 'Average Review' + ':</b>&nbsp' + e['average_review'] + ':</b>&nbsp out of ' + e['total_reviews'] +
            ' reviews.</p>';
        contentStr += '<p><b>' + 'Most Recent Comment' + ':</b>&nbsp'+ e['most_recent_comment'] +
            '</p>';

        // Create the marker
        var marker = new google.maps.Marker({ // Set the marker
            position : latlng, // Position marker to coordinates
            map : map, // assign the market to our map variable
            icon: icons[e['site_type']], //add custom icons based on site type
            customInfo: contentStr,
        });

        var siteName =  e['title'];

        // Add a Click Listener to the marker
        google.maps.event.addListener(marker, 'click', function() {
            // use 'customInfo' to customize infoWindow
            infowindow.setContent(marker['customInfo']);
            infowindow.open(map, marker); // Open InfoWindow

            function setSite(){
                document.getElementById("sN").value=siteName;
            }
            //console.log(JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2));

            setSite();
        });

    });
    console.log(bounds.Ua.hi);
    if (bounds.Ua.hi != -1) {
        map.fitBounds (bounds);
    } else {
        var latlng = new google.maps.LatLng(36.11502267719292, -86.83868336541748);
        bounds.extend(latlng);
        var latlng2 = new google.maps.LatLng(36.219236356179856, -86.70581745965576);
        bounds.extend(latlng2);
        map.fitBounds (bounds);

    }

}



function initAutocomplete() {
    // Create the autocomplete object
    autocomplete = new google.maps.places.Autocomplete(document
        .getElementById('autocomplete'));

    // When the user selects an address from the dropdown, show the place selected
    autocomplete.addListener('place_changed', onPlaceChanged);
}
//updated onPlaceChanged for question 3
function onPlaceChanged() {
    //defined marker
    const marker = new google.maps.Marker({
        map,
        anchorPoint: new google.maps.Point(0, -29),
    });

    place = autocomplete.getPlace();
    //used code from teh autocomplete example to move to that location and add a marker
    if (!place.geometry || !place.geometry.location) {
        // User entered the name of a Place that was not suggested and
        // pressed the Enter key, or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
    }

    // If the place has a geometry, then present it on a map.
    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(10);
    }

    marker.setPosition(place.geometry.location);
    marker.setVisible(true);
}

//Execute our 'initialization' function once the page has loaded.
google.maps.event.addDomListener(window, 'load', initialization);