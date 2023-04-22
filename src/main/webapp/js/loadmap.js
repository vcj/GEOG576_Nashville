var map;
var place;
var autocomplete;
var infowindow = new google.maps.InfoWindow();

function initialization() {
    console.log("loadmap.js calling show all reports")
    showAllReports();
    console.log("loadmap.js calling initautocomplete after show all reports")
    initAutocomplete();
}

function showAllReports() {
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: { "tab_id": "1"},
        success: function(reports) {
            console.log("loadmap.js map initialization, print reports" + reports)
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("An AJAX error occured: " + status + "\nError: " + error);
        }
    });
}

function mapInitialization(reports) {
    var mapOptions = {
        mapTypeId : google.maps.MapTypeId.ROADMAP, // Set the type of Map
    };
    console.log("loadmap.js in mapinitialization, printing reports " + reports)
    // Render the map within the empty div
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    var bounds = new google.maps.LatLngBounds ();

    $.each(reports, function(i, e) {
        var long = Number(e['longitude']);
        var lat = Number(e['latitude']);
        // to do:
        // check if longitude and/or latitude is null
        // if long = null
        if (isNaN(long)) {
            // do not calculate lat/long
            console.log("longitude is null")
            // else
            // calculate lat/long and print that long is populated
        } else {
            var latlng = new google.maps.LatLng(lat, long);
            console.log("longitude is populated")

            console.log("loadmap.js, in each loop, printing long " + long + " and lat " + lat)
            bounds.extend(latlng);


            // Create the infoWindow content
            var contentStr = '<h4>Report Details</h4><hr>';
            contentStr += '<p><b>' + 'Disaster' + ':</b>&nbsp' + e['disaster'] + '</p>';
            contentStr += '<p><b>' + 'Report Type' + ':</b>&nbsp' + e['report_type'] +
                '</p>';
            if (e['report_type'] == 'request' || e['report_type'] == 'donation') {
                contentStr += '<p><b>' + 'Resource Type' + ':</b>&nbsp' +
                    e['resource_type'] + '</p>';
            }
            else if (e['report_type'] == 'damage') {
                contentStr += '<p><b>' + 'Damage Type' + ':</b>&nbsp' + e['damage_type']
                    + '</p>';
            }
            // Q1 Add Reporter (Firstname Lastname) to the info window
            contentStr += '<p><b>' + 'Reporter' + ':</b>&nbsp' + e['first_name'] + " " + e['last_name']
                + '</p>';
            console.log("loadmap.js in mapinitialization content string: " + contentStr + " e timestamp: "+ e['time_stamp'])
            contentStr += '<p><b>' + 'Timestamp' + ':</b>&nbsp' + e['time_stamp'].substring(0,19) + '</p>';
            google.maps.event.trigger(map, 'resize');
            if ('message' in e){
                contentStr += '<p><b>' + 'Message' + ':</b>&nbsp' + e['message'] + '</p>';
            }

            // Q2 Create the marker
            // icon images in img folder, match to correct report type using switch statement instead of multiple 'if/else if' lines

            switch (e['report_type']) {
                // donation case, use the donation svg
                case 'donation':
                    var url = "img/donation.svg";
                    break;

                case 'damage':
                    var url = "img/damage.svg";
                    break;

                case 'request':
                    var url = "img/request.svg";
                    break;
            }

            var icons = {
                url: url,
                scaledSize: new google.maps.Size(150, 150), // scaled size
                origin: new google.maps.Point(0,0), // origin
                anchor: new google.maps.Point(65,65) // anchor - this is not perfect but keeps the markers mostly in the right spot when zooming in/out
            };
            var marker = new google.maps.Marker({ // Set the marker
                position : latlng, // Position marker to coordinates
                map : map, // assign the market to our map variable
                customInfo: contentStr,
                animation: google.maps.Animation.DROP, // a drop animation, because why not?
                // if I can't make this code work I can at least make it look fun.
                icon: icons,
            });
            // Add a Click Listener to the marker
            google.maps.event.addListener(marker, 'click', function() {
                // use 'customInfo' to customize infoWindow
                infowindow.setContent(marker['customInfo']);
                infowindow.open(map, marker); // Open InfoWindow
            });


        };


    });

    map.fitBounds (bounds);

}
function initAutocomplete() {
    // Create the autocomplete object
    autocomplete = new google.maps.places.Autocomplete(document
        .getElementById('autocomplete'));

    // When the user selects an address from the dropdown, zoom to the place selected!!! It works!!!
    autocomplete.addListener('place_changed', onPlaceChanged);
}
// Q3 zoom to place in Address field
function onPlaceChanged() {
    place = autocomplete.getPlace();
    if (!place.geometry || !place.geometry.location) {
        // Popup error alert if the user enters a place not in the autocomplete dropdown or the Place Details request failed.
        window.alert("No details available for input: '" + place.name + "'");
        return;
    }

    // If the place has a geometry, then present it on a map.
    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(17);
    }
};


//Execute our 'initialization' function once the page has loaded.
google.maps.event.addDomListener(window, 'load', initialization);