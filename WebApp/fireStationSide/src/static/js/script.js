var mymap //La carte, qui est en variable globale
var fireMarkerLayerGroup //La couche de marqueurs de feux, appliquée à la carte, pour pouvoir supprimer tous les marqueurs d'un coup plus facilement
var truckMarkerLayerGroup //La couche de marqueurs de camions, appliquée à la carte, pour pouvoir supprimer tous les marqueurs d'un coup plus facilement

var fireIcon = L.icon({//On créé l'icon de feu
iconUrl: '../../../static/asset/FireIcon.png',//On charge l'icon de feu 

iconSize:     [38, 80], // size of the icon
iconAnchor:   [22, 80], // point of the icon which will correspond to marker's location 
popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
});

var truckIcon = L.icon({//On créé l'icon de camion
iconUrl: '../../../static/asset/TruckIcon.png',//On charge l'icon de camion 

iconSize:     [38, 20], // size of the icon
iconAnchor:   [22, 20], // point of the icon which will correspond to marker's location 
popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
});

function displayMap(){//Permet d'afficher la map
        mymap = L.map(document.getElementById('mapid')).setView([45.75, 4.9], 12.5);
        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
                attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
                maxZoom: 18,
                id: 'mapbox/satellite-v9',
                accessToken: 'pk.eyJ1IjoiYmFyYXVndXN0aW4iLCJhIjoiY2szc2ttdm50MDYxbzNtcDd3NHd4aG5wbCJ9.e8bn1OfmUOrv0fYAkwdXlw'
            }).addTo(mymap);

        var popup = L.popup();
        function onMapClick(e) {popup
                .setLatLng(e.latlng)
                .setContent("Vous avez cliqué aux coordonées : " + e.latlng.toString())
                .openOn(mymap);
        }
        mymap.on('click', onMapClick);
        return mymap
}

function locationLoad()//Permet de charger et d'afficher les points contenus dans la BDD
{
        var e = document.getElementById("markersLayerSelection");
        if (e.options[e.selectedIndex].value == 'fire' || e.options[e.selectedIndex].value == 'both')
        {
                console.log("Feux séléctionnés");
                fireMarkerLayerGroup = L.layerGroup().addTo(mymap);
                //Ici, ajouter une fonction qui récupère les coordonées des feux dans la BDD et les charge sur la MAP
                $.get("/loadFireLocation", function(data) {//Permet de récupérer les points dans la BDD en JSON
                        var str = data.toString().substring(1);
                        str = str.substring(0, str.length-1);
                        str = str.split("),(");
                        console.log("Feux :");
                        for (var i = 0; i < str.length; i++)
                        {
                                var coordinates = str[i].split(",");
                                console.log("Coordonnées n°"+(i+1));
                                var stringCoord1 = "Latitude: "+ coordinates[0];
                                var stringCoord2 = "Longitude: "+ coordinates[1];
                                console.log(stringCoord1);
                                console.log(stringCoord2);
                                addLocation(parseFloat(coordinates[0]),parseFloat(coordinates[1]),"Feu n° "+(i+1),"<br>"+stringCoord1+"</br>"+"<br>"+stringCoord2+"</br>",fireMarkerLayerGroup,fireIcon);
                        }
                })
        }
        if (e.options[e.selectedIndex].value == 'truck' || e.options[e.selectedIndex].value == 'both')
        {
                truckMarkerLayerGroup = L.layerGroup().addTo(mymap);
                console.log("Camions séléctionnés");
                //Ici, ajouter une fonction qui récupère les coordonées des camions dans la BDD et les charge sur la MAP
                $.get("/loadTruckLocation", function(data) {//Permet de récupérer les points dans la BDD en JSON
                        var str = data.toString().substring(1);
                        str = str.substring(0, str.length-1);
                        str = str.split("),(");
                        console.log("Camions :");
                        for (var i = 0; i < str.length; i++)
                        {
                                var coordinates = str[i].split(",");
                                console.log("Coordonnées n°"+(i+1));
                                var stringCoord1 = "Latitude: "+ coordinates[0];
                                var stringCoord2 = "Longitude: "+ coordinates[1];
                                console.log(stringCoord1);
                                console.log(stringCoord2);
                                addLocation(parseFloat(coordinates[0]),parseFloat(coordinates[1]),"Camion n° "+(i+1),"<br>"+stringCoord1+"</br>"+"<br>"+stringCoord2+"</br>",truckMarkerLayerGroup,truckIcon);
                        }
                })

        }        
}

//Ajoute un marqueur à un point défini avec un popup, une icon personnalisé et accroché à une couche spécifique
function addLocation(coordA, coordB, boldText, simpleText,layerGroup,markerIcon)
{
        var marqueur = L.marker([coordA,coordB],{icon : markerIcon}).addTo(layerGroup);
        if (boldText == undefined)boldText = "";
        if (simpleText == undefined)simpleText = "";
        var message = '<b>'+boldText+'</b><br>'+simpleText+'</br>';
        marqueur.bindPopup(message);
}

function resetLocations()//Permet de supprimer tous les marqueurs d'une couche
{
        truckMarkerLayerGroup.clearLayers();
        fireMarkerLayerGroup.clearLayers();
}