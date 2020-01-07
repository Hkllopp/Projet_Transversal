--Pour créer la BDD
CREATE TABLE Capteurs(idCapteur integer, coordXCapteur integer, coordYCapteur integer, activation boolean,CONSTRAINT pk_capteurs PRIMARY KEY(idCapteur));
CREATE TABLE Alertes(idAlerte integer, idCapteur integer, coordXFeu integer, coordYFeu integer, intensiteFeu integer,CONSTRAINT pk_Alertes PRIMARY KEY(idAlerte), CONSTRAINT fk_Alertes_Capteurs FOREIGN KEY (idCapteur) REFERENCES Capteurs(idCapteur));
CREATE TABLE Casernes(idCaserne integer, nomCaserne VARCHAR(25), coordCaserneX float8, coordCaserneY float8, CONSTRAINT pk_caserne PRIMARY KEY(idCaserne));
CREATE TABLE Camions(idCamion VARCHAR(5), idCaserne integer, coordonneeActuelXCamion float8, coordonneeActuelYCamion float8, statut VARCHAR(25), CONSTRAINT pk_camions PRIMARY KEY(idCamion), CONSTRAINT fk_camions_casernes FOREIGN KEY(idCaserne) REFERENCES Casernes(idCaserne));
CREATE TABLE Interventions(idIntervention integer, idAlertes integer, idCamion VARCHAR(5), CONSTRAINT pk_interventions PRIMARY KEY(idIntervention), CONSTRAINT fk_interventions_alertes FOREIGN KEY(idAlertes) REFERENCES Alertes(idAlerte), CONSTRAINT fk_interventions_camions FOREIGN KEY(idCamion) REFERENCES Camions(idCamion));

--Equivalent
CREATE TABLE Capteurs(idCapteur integer, coordXCapteur integer, coordYCapteur integer, activation boolean,CONSTRAINT pk_capteurs PRIMARY KEY(idCapteur)); CREATE TABLE Alertes(idAlerte integer, idCapteur integer, coordXFeu integer, coordYFeu integer, intensiteFeu integer,CONSTRAINT pk_Alertes PRIMARY KEY(idAlerte), CONSTRAINT fk_Alertes_Capteurs FOREIGN KEY(idCapteur) REFERENCES Capteurs(idCapteur)); CREATE TABLE Casernes(idCaserne integer, nomCaserne VARCHAR(25), coordCaserneX float8, coordCaserneY float8, CONSTRAINT pk_caserne PRIMARY KEY(idCaserne)); CREATE TABLE Camions(idCamion VARCHAR(5), idCaserne integer, coordonneeActuelXCamion float8, coordonneeActuelYCamion float8, statut VARCHAR(25), CONSTRAINT pk_camions PRIMARY KEY(idCamion), CONSTRAINT fk_camions_casernes FOREIGN KEY(idCaserne) REFERENCES Casernes(idCaserne)); CREATE TABLE Interventions(idIntervention integer, idAlertes integer, idCamion VARCHAR(5), CONSTRAINT pk_interventions PRIMARY KEY(idIntervention), CONSTRAINT fk_interventions_alertes FOREIGN KEY(idAlertes) REFERENCES Alertes(idAlerte), CONSTRAINT fk_interventions_camions FOREIGN KEY(idCamion) REFERENCES Camions(idCamion)); 

--Pour supprimer la BDD
DROP TABLE Capteurs;
DROP TABLE Alertes;
DROP TABLE Casernes;
DROP TABLE Camions;
DROP TABLE Interventions;

--Equivalent
DROP TABLE Capteurs CASCADE; DROP TABLE Alertes CASCADE; DROP TABLE Casernes CASCADE; DROP TABLE Camions CASCADE; DROP TABLE Interventions CASCADE;

--On créé alors une caserne puis plusieurs camions qui lui sont reliés
INSERT INTO Casernes VALUES (0,'Première Caserne',1.0,2.0);
INSERT INTO Camions VALUES ('CAM1',0,1.0,2.0,'ALLER');
INSERT INTO Camions VALUES ('CAM2',0,5.0,3.0,'ALLER');
INSERT INTO Camions VALUES ('CAM3',0,8.0,2.0,'ALLER');
INSERT INTO Camions VALUES ('CAM4',0,1.0,0.0,'ALLER');
INSERT INTO Camions VALUES ('CAM5',0,6.0,7.0,'ALLER');
INSERT INTO Camions VALUES ('CAM6',0,5.0,9.0,'ALLER');

INSERT INTO Capteurs VALUES(1, 1, 0, false);
INSERT INTO Capteurs VALUES(2, 1, 1, false);
INSERT INTO Capteurs VALUES(3, 1, 2, false);
INSERT INTO Capteurs VALUES(4, 1, 3, false);
INSERT INTO Capteurs VALUES(5, 1, 4, false);
INSERT INTO Capteurs VALUES(6, 1, 5, false);
INSERT INTO Capteurs VALUES(7, 1, 6, false);
INSERT INTO Capteurs VALUES(8, 1, 7, false);
INSERT INTO Capteurs VALUES(9, 1, 8, false);
INSERT INTO Capteurs VALUES(10, 1, 9, false);

INSERT INTO Alertes VALUES(0,1,0,0,1);--Feu témoin qui est ID 0, appartient au capteur 0 et est en 0,0

--Equivalent
INSERT INTO Casernes VALUES(0,'Première Caserne',1.0,2.0); INSERT INTO Camions VALUES ('CAM1',0,1.0,2.0,'ALLER'); INSERT INTO Camions VALUES ('CAM2',0,5.0,3.0,'ALLER'); INSERT INTO Camions VALUES ('CAM3',0,8.0,2.0,'ALLER'); INSERT INTO Camions VALUES ('CAM4',0,1.0,0.0,'ALLER'); INSERT INTO Camions VALUES ('CAM5',0,6.0,7.0,'ALLER'); INSERT INTO Camions VALUES ('CAM6',0,5.0,9.0,'ALLER'); INSERT INTO Capteurs VALUES(1, 1, 0, false); INSERT INTO Capteurs VALUES(2, 1, 1, false); INSERT INTO Capteurs VALUES(3, 1, 2, false); INSERT INTO Capteurs VALUES(4, 1, 3, false); INSERT INTO Capteurs VALUES(5, 1, 4, false); INSERT INTO Capteurs VALUES(6, 1, 5, false); INSERT INTO Capteurs VALUES(7, 1, 6, false); INSERT INTO Capteurs VALUES(8, 1, 7, false); INSERT INTO Capteurs VALUES(9, 1, 8, false); INSERT INTO Capteurs VALUES(10, 1, 9, false); INSERT INTO Alertes VALUES(0,1,0,0,1);