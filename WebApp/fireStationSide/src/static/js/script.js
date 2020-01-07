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
                var str = data;
                var i = 0;
                for (coord of str)
                {
                        console.log("Le tuple n°"+i+" est "+coord);
                        console.log("Le chiffre n°1 est "+coord[0]);
                        console.log("Le chiffre n°2 est "+coord[1]);
                        console.log("Le chiffre n°3 est "+coord[2]);
                        i++
                        var realCoord1 = coord[0]*0.00888+45.71;//Méthode pour convertir la latitude proportionnellement: coords de départ [0:9] --> [45.71:45.78]
                        var stringCoord1 = "Latitude: "+ realCoord1 ;
                        console.log(stringCoord1);
                        var realCoord2 = coord[1]*0.01555+4.8;//Méthode pour convertir la longitude proportionnellement: coords de départ [0:9] --> [4.8:4.93995]
                        var stringCoord2 = "Longitude: " +realCoord2;
                        console.log(stringCoord2);  
                        var stringIntens = "Intensité: " + coord[2];
                        console.log(stringIntens);
                        addLocation(realCoord1,realCoord2,"Feu n° "+(i),"<br>"+stringCoord1+"</br>"+"<br>"+stringCoord2+"</br>"+"<br>"+stringIntens+"</br>",fireMarkerLayerGroup,fireIcon);

                }
        })
        }
        if (e.options[e.selectedIndex].value == 'truck' || e.options[e.selectedIndex].value == 'both')
        {
                truckMarkerLayerGroup = L.layerGroup().addTo(mymap);
                console.log("Camions séléctionnés");
                //Ici, ajouter une fonction qui récupère les coordonées des camions dans la BDD et les charge sur la MAP
                $.get("/loadTruckLocation", function(data) {//Permet de récupérer les points dans la BDD en JSON
                        console.log("Raw data : ");
                        console.log(data);
                        var str = data;
                        var i = 0;
                        for (coord of str)
                        {
                                console.log("Le tuple n°"+i+" est "+coord);
                                console.log("L'id du camion est "+coord[0]);
                                console.log("La coordX du camion est "+coord[1]);
                                console.log("La coordY du camions est "+coord[2]);
                                i++

                                var stringId = "Id: " + coord[0];
                                console.log(stringId);

                                var realCoord1 = coord[1]*0.00888+45.71;//Méthode pour convertir la latitude proportionnellement: coords de départ [0:9] --> [45.71:45.78]
                                var stringCoord1 = "Latitude: "+ realCoord1 ;
                                console.log(stringCoord1);

                                var realCoord2 = coord[2]*0.01555+4.8;//Méthode pour convertir la longitude proportionnellement: coords de départ [0:9] --> [4.8:4.93995]
                                var stringCoord2 = "Longitude: " +realCoord2;
                                console.log(stringCoord2);  

                                addLocation(realCoord1,realCoord2,"Camion n° "+(i)+" , id "+coord[0],"<br>"+stringCoord1+"</br>"+"<br>"+stringCoord2+"</br>",truckMarkerLayerGroup,truckIcon);

                        }
                })

        }        
}

//Ajoute un marqueur à un point défini avec un popup, une icon personnalisé et accroché à une couche spécifique
function addLocation(pointA, pointB, boldText, simpleText,layerGroup,markerIcon)
{
        var marqueur = L.marker([pointA,pointB],{icon : markerIcon}).addTo(layerGroup);
        if (boldText == undefined)boldText = "";
        if (simpleText == undefined)simpleText = "";
        var message = '<b>'+boldText+'</b><br>'+simpleText+'</br>';
        marqueur.bindPopup(message);
}

function resetLocations()//Permet de supprimer tous les marqueurs d'une couche
{
        var e = document.getElementById("markersLayerSelection");
        if (e.options[e.selectedIndex].value == 'fire' || e.options[e.selectedIndex].value == 'both')
        {
                console.log("Suppression des feux demandée");
                fireMarkerLayerGroup.clearLayers();
        }
        if (e.options[e.selectedIndex].value == 'truck' || e.options[e.selectedIndex].value == 'both')
        {
                console.log("Suppression des camions demandée");
                truckMarkerLayerGroup.clearLayers();
        }
        
}