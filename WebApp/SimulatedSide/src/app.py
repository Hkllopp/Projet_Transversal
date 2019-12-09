from flask import Flask, jsonify, abort, make_response, render_template
import psycopg2


app = Flask(__name__)

#Permet de récupérer les données de la table caractfeux en JSON
#curl -i http://127.0.0.1:5000/todo/api/v1.0/getfire
#127.0.0.1:5000/todo/api/v1.0/getfire
@app.route('/todo/api/v1.0/getfire', methods=['GET'])
def dbConnect():
    #Se connecter à la BDD
    print("Début de la connexion")
    #username : testuser ; password : testuser
    connection = psycopg2.connect(user = "postgres",
                                password = "superuser",
                                host = "localhost",
                                port = "5433",#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
                                database = "Simulation")
    print("Connexion réussie")

    cursor = connection.cursor()

    postgreSQL_select_Query = "select * from caractfeux"

    cursor.execute(postgreSQL_select_Query)
    print("Selecting rows from caractfeux table using cursor.fetchall")
    fire_records = jsonify(cursor.fetchall())
    
    #print("Print each row and it's columns values")
    #for row in fire_records:

    cursor.close()
    connection.close()
    print("PostgreSQL connection is closed")
    
    return fire_records

#Permet d'aller sur la page de visualisation des feux
@app.route('/todo/api/v1.0/showfire', methods=['GET'])
def showfire():
    return render_template("index.html")

#Permet de récupérer les points et de les redonner au js
@app.route('/loadFireLocation')
def get_python_data():
    connection = psycopg2.connect(user = "postgres",password = "superuser",host ="localhost",port = "5433",database = "Simulation")#/!\ C'est ici que ca coince, le port est bien 5433,pas 5432
    cursor = connection.cursor()
    cursor.execute('select * from "Location"')
    fire_records = jsonify(cursor.fetchall())
    cursor.close()
    connection.close()
    return fire_records



@app.after_request
def add_header(r):
    r.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    r.headers["Pragma"] = "no-cache"
    r.headers["Expires"] = "0"
    r.headers['Cache-Control'] = 'public, max-age=0'
    return r


if __name__ == '__main__':
    app.run(debug=True)