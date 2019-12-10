from flask import Flask, jsonify, abort, make_response, render_template
import psycopg2

"""
Le but de cette application est de :
    - Récupérer les données de feu de l'UART via un script python et les insérer dans la BDD
    - Afficher les objets feu et camions de pompier inscrits dans la BDD
    - Récupérer les données pour en faire des graphiques


En optionnel :
    - Afficher la trajectoire des camions grâce à Leaflet Routing Machine :
    https://www.liedman.net/leaflet-routing-machine/#about
    - Ne plus représenter les camions comme des marqueurs sur la map mais bien à des checkpoints sur la trajectoire
"""

app = Flask(__name__)


#Permet d'aller sur la page de visualisation des feux
#127.0.0.1:5000/todo/api/v1.0/showfire
@app.route('/todo/api/v1.0/showfire', methods=['GET'])
def showfire():
    print("Map chargée")
    return render_template("index.html")

#Permet de récupérer les points de feux et de les redonner au js
@app.route('/loadFireLocation')
def get_fire_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5433",database = "Caserne")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
    cursor = connection.cursor()
    cursor.execute('select "localisation" from "feux"')
    fire_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return fire_records

#Permet de récupérer les points de camions et de les redonner au js
@app.route('/loadTruckLocation')
def get_truck_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5433",database = "Caserne")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
    print("Connecté à la BDD")
    cursor = connection.cursor()
    cursor.execute('select "position_actuel" from "camions"')
    truck_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return truck_records

if __name__ == '__main__':
    app.run(debug=True)