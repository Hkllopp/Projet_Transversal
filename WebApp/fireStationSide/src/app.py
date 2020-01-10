# coding:utf8


from flask import Flask, jsonify, abort, make_response, render_template, request
import psycopg2,re
from influxdb import InfluxDBClient


"""
/!\ Avant de coder dans l'app, il faut rentrer dans l'environnement virtuel.
Sur Windows :./venv/Scripts/activate
Sur Linux :. venv/bin/activate
On lance l'app python avec : uwsgi --http 127.0.0.1:8000 --wsgi-file ./src/app.py --callable app_dispatch
clear && uwsgi --socket 127.0.0.1:3031 --plugin python --wsgi-file ./src/app.py --callable app_dispatch --stats 127.0.0.1:9191

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

Pour créer l'exeutable à partir du fichier spec :
pyinstaller --onefile -F app.spec
"""

app = Flask(__name__)

@app.route('/test', methods=['GET'])
def testConnection():
    return "Connexion de test réussie"

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
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "Caserne")
    cursor = connection.cursor()
    cursor.execute('select coordXFeu,coordYFeu,intensiteFeu from Alertes')
    fire_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return fire_records

# Permet de récupérer les points de camions et de les redonner au js
@app.route('/loadTruckLocation')
def get_truck_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "Caserne")
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
# curl -i http://192.168.0.118:5000/fireService
@app.route('/fireService', methods=["POST"])
def query_example():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "Caserne")
    print("Connecté à la BDD")
    cursor = connection.cursor()


    sql_request_list = []

    #On récupère les données du POST
    print("Detection d'une requête")
    request.get_data()
    list = re.findall('\((.*?)\)',str(request.data))#Liste des valeurs envoyées par la requête POST
    #Les données sont sous la forme :
    #data = "b\"b'(0,0,0)(0,1,0)(0,2,0)(0,3,0)(0,4,0)(0,5,0)(0,6,0)(0,7,0)(0,8,0)(0,9,0)(1,0,0)(1,1,0)(1,2,0)(1,3,0)(1,4,0)(1,5,0)(1,6,0)(1,7,0)(1,8,0)(1,9,0)(2,0,0)(2,1,0)(2,2,0)(2,3,0)(2,4,0)(2,5,0)(2,6,0)(2,7,0)(2,8,0)(2,9,0)(3,0,0)(3,1,0)(3,2,0)(3,3,0)(3,4,0)(3,5,0)(3,6,0)(3,7,0)(3,8,0)(3,9,0)(4,0,0)(4,1,0)(4,2,0)(4,3,0)(4,4,0)(4,5,0)(4,6,0)(4,7,0)(4,8,0)(4,9,0)(5,0,0)(5,1,0)(5,2,0)(5,3,0)(5,4,0)(5,5,0)(5,6,0)(5,7,0)(5,8,0)(5,9,0)\n'"

    fire_attributes = []
    numero = 1
    #Pour chaque feu envoyé, on vérifie s'il est déjà dans la BDD
    for i in list:
        coordX = int(i[0])
        coordY = int(i[2])
        intensite = int(i[4])
        fire_attributes.append([coordX,coordY,intensite])
        print("coord n°"+str(numero)+":coordX=" + str(coordX) +":coordY=" + str(coordY) +":intensité=" + str(intensite))

    commande  = 'INSERT INTO Alertes(coordXFeu,coordYFeu,intensiteFeu,idAlerte) VALUES '
    next_id = 1
    if fire_attributes:
        for i in fire_attributes:
            value = '(' + str(i[0]) + ',' + str(i[1]) + ','+ str(i[2]) + ',' + str(next_id) + ')'
            commande = commande + value + ","
            next_id+=1
        commande = commande[:-1] + ";"
        sql_request_list.append(commande)

    cursor.execute('DELETE FROM Alertes WHERE 1=1;')
    connection.commit()

    print("Les requêtes executées sont :")
    for i in sql_request_list:
        print(str(i))
    for i in sql_request_list:
        cursor.execute(i)

    connection.commit()
    cursor.close()
    connection.close()    
    return "done"

#Redirige la page par défaut à index.html
@app.route('/', defaults={'path': ''})
@app.route('/<path:path>')
def catch_all(path):
    return app.send_static_file("index.html")

if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=False,threaded=True) 