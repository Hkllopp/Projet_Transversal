# coding:utf8

from flask import Flask, jsonify, abort, make_response, render_template, Response
import psycopg2, json

"""
Pour demain : 
- Créer une seconde BDD pour bien séparer les deux
- Tester les post pour essayer d'insérer dans la BDD 

/!\ Avant de coder dans l'app, il faut rentrer dans l'environnement virtuel.
Sur Windows :./venv/Scripts/activate
Sur Linux :. venv/bin/activate
On lance l'app python avec : uwsgi --http 127.0.0.1:8000 --wsgi-file ./src/app.py --callable app_dispatch
clear && uwsgi --socket 127.0.0.1:3031 --plugin python --wsgi-file ./src/app.py --callable app_dispatch --stats 127.0.0.1:9191
On lance la bdd avec psql -U postgres -h localhost
Le mot de passe est "superuser"


Pour créer l'exeutable à partir du fichier spec :
pyinstaller --onefile -F app.spec
"""


app = Flask(__name__)

@app.route('/test', methods=['GET'])
def testConnection():
    return "Connexion de test réussie"

# Permet de récupérer les données de la table caractfeux en JSON
# curl -i http://127.0.0.1:5000/todo/api/v1.0/getfire
# 127.0.0.1:5000/todo/api/v1.0/getfire
@app.route('/todo/api/v1.0/getfire', methods=['GET'])
def dbConnect():
    #Se connecter à la BDD
    print("Début de la connexion")
    #username : testuser ; password : testuser
    connection = psycopg2.connect(user = "postgres",
                                password = "superuser",
                                host = "localhost",
                                port = "5432",#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
                                database = "postgres")
    print("Connexion réussie")

    cursor = connection.cursor()

    postgreSQL_select_Query = "select coordXFeu,coordYFeu,intensiteFeu from Alertes"

    cursor.execute(postgreSQL_select_Query)

    #Aucune idée de comment ca marche mais ca marche ! Merci internet <3
    r=[dict((cursor.description[i][0],value)\
        for i, value in enumerate(row)) for row in cursor.fetchall()]

    cursor.close()
    connection.close()
    print("PostgreSQL connection is closed")   


    json_records = json.dumps(r)
    return Response(json_records,mimetype="application/json")

#Permet d'aller sur la page de visualisation des feux
@app.route('/todo/api/v1.0/showfire', methods=['GET'])
def showfire():
    return render_template("index.html")

#Permet de récupérer les points de feux et de les redonner au js
@app.route('/loadFireLocation')
def get_python_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5432",database = "postgres")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
    cursor = connection.cursor()
    cursor.execute('select coordXFeu,coordYFeu,intensiteFeu from Alertes')
    fire_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return fire_records

#Redirige la page par défaut à index.html
@app.route('/', defaults={'path': ''})
@app.route('/<path:path>')
def catch_all(path):
    return app.send_static_file("index.html")


if __name__ == '__main__':
    app.run(host='0.0.0.0',debug=False,threaded=True,port=5001) 