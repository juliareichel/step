/** Creates a map and adds it to the page. */
function createMap() {
  var map = new google.maps.Map(document.getElementById('map'), {
    center: {lat:40.5500 , lng: 14.2333},
    zoom: 16,
    mapTypeId: 'satellite'
  });
}
