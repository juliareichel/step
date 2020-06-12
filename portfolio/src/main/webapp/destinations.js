const curLocation = {lat:40.5500 , lng: 14.2333};

/** Creates a map and adds it to the page. */
function createMap() {
  const map = new google.maps.Map(document.getElementById('map'), {
    center: curLocation,
    zoom: 10,
    mapTypeId: 'terrain'
  });

  const marker = new google.maps.Marker({position: curLocation, map: map});

/** Taken from Google Maps online API*/
  const searchInput = document.getElementById('search');
  const searchBox = new google.maps.places.SearchBox(searchInput);
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(searchInput);

  map.addListener('bounds_changed', function() {
    searchBox.setBounds(map.getBounds());
  });

  var markers = [];
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener('places_changed', function() {
    const places = searchBox.getPlaces();
      if (places.length == 0) {
        return;
      }

    // Clear out the old markers.
    markers.forEach(function(marker) {
      marker.setMap(null);
    });

    markers = [];

    // For each place, get the icon, name and location.
    const bounds = new google.maps.LatLngBounds();
    places.forEach(function(place) {
      if (!place.geometry) {
        return;
      }
      const icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      markers.push(new google.maps.Marker({
        map: map,
        icon: icon,
        title: place.name,
        position: place.geometry.location
      }));

      if (place.geometry.viewport) {
      // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
    });
    map.fitBounds(bounds);
  });
}
