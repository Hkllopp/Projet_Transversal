# coding:utf8


from flask import Flask, jsonify, abort, make_response, render_template, request
import psycopg2
from influxdb import InfluxDBClient



"""
/!\ Avant de coder dans l'app, il faut rentrer dans l'environnement virtuel.
Sur Windows :./venv/Scripts/activate
Sur Linux :. venv/bin/activate
On lance l'app python avec : uwsgi --http 127.0.0.1:8000 --wsgi-file ./src/app.py --callable app_dispatch
clear && uwsgi --socket 127.0.0.1:3031 --plugin python --wsgi-file ./src/app.py --callable app_dispatch --stats 127.0.0.1:9191
On lance la bdd avec psql -U postgres -h localhost

Le but de cette application est de :
    X   - Récupérer les données de feu de l'UART via un script python et les insérer dans la BDD
    V   - Afficher les objets feu et camions de pompier inscrits dans la BDD
    ~   - Récupérer les données et les insérer dans la base de donnée influxdb


En optionnel :
    - Afficher la trajectoire des camions grâce à Leaflet Routing Machine :https://www.liedman.net/leaflet-routing-machine/#about
    - Ne plus représenter les camions comme des marqueurs sur la map mais bien à des checkpoints sur la trajectoire


Les packages du venv:
    - Flask pour gérer les requêtes HTTP sans php
    - Psycopg2 pour se connecter à la base Postgresql
    - InfluxDB-Python is a client for interacting with InfluxDB https://influxdb-python.readthedocs.io/en/latest/include-readme.html#examples https://v2.docs.influxdata.com/v2.0/write-data/#user-interface
    - uwsgi qui permet à l'SS de se connecter avec l'API python https://github.com/prometheus/client_python#flask
"""

app = Flask(__name__)

# Permet d'aller sur la page de visualisation des feux
# curl -i http://127.0.0.1:5000/todo/api/v1.0/showfire
# 127.0.0.1:5000/todo/api/v1.0/showfire
@app.route('/todo/api/v1.0/showfire', methods=['GET'])
def showfire():
    print("Map chargée")
    return render_template("index.html")

#Permet de récupérer les points de feux et de les redonner au js
@app.route('/loadFireLocation')
def get_fire_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "postgres")
    cursor = connection.cursor()
    cursor.execute('select coordXFeu,coordYFeu,intensiteFeu from Alertes')
    fire_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return fire_records

# Permet de récupérer les points de camions et de les redonner au js
@app.route('/loadTruckLocation')
def get_truck_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "postgres")
    print("Connecté à la BDD")
    cursor = connection.cursor()
    cursor.execute('select idCamion,coordonneeActuelXCamion,coordonneeActuelYCamion from Camions')
    truck_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return truck_records

# Permet aux scrapers de l'influxDB de récupérer régulièrement les données
# 127.0.0.1:5000/todo/api/v1.0/promGetFire
"""
@app.route('/todo/api/v1.0/promGetFire')
def sendFireData():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5433",database = "Caserne")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
    cursor = connection.cursor()
    cursor.execute('select "localisation" from "feux"')
    data = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()

    client = InfluxDBClient('localhost', 8086, 'root', 'root', 'example')
    client.write_points(data)
    result = client.query('select value from cpu_load_short;')
    print("Result: {0}".format(result))
"""

# Permet d'insérer dans la BDD les feux reçus du RF-SUB1G
# curl -i http://127.0.0.1:5000/todo/api/v1.0/postFire?fireData=125237
@app.route('/fireService', methods=["POST"])
def query_example():
    fireData = request.values#Liste des clefs
    array = []
    for i in fireData:
        array.append([i,request.form[i]])
        #On a un tableau à 2 dimensions avec [[clef1,valeur1],[clef2,valeur2]]
    
    
    return str(array)
    

    """if (array):#Si le tableau n'est pas vide, on insert les données
        connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "postgres")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
        print("Connecté à la BDD")
        cursor = connection.cursor()
        insertRequest = "INSERT INTO fireGenerator(xcoord,ycoord,intensite) VALUES "
        for i in array:
            value = '("' + i[0] + '","' + i[1] + '","'+ i[2] + '")'
            insertRequest = insertRequest + value
            if i != array[len(array)-1]:#Si c'est la dernière chaine, on ne rajoute pas une virgule après les values
                insertRequest = insertRequest + ","
        cursor.execute(insertRequest)
        cursor.close()
        connection.close()
        return '''<h1>Les données {} ont bien été insérées !</h1>'''.format(insertRequest)

    return format(array)"""

#Redirige la page par défaut à index.html
@app.route('/', defaults={'path': ''})
@app.route('/<path:path>')
def catch_all(path):
    return app.send_static_file("index.html")

if __name__ == '__main__':
    app.run(debug=True)