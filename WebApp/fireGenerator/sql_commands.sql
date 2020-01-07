--Pour créer la BDD
CREATE TABLE Capteurs(idCapteur integer, coordXCapteur integer, coordYCapteur integer, activation boolean,CONSTRAINT pk_capteurs PRIMARY KEY(idCapteur));
CREATE TABLE Alertes(idAlerte integer, idCapteur integer, coordXFeu integer, coordYFeu integer, intensiteFeu integer,CONSTRAINT pk_Alertes PRIMARY KEY(idAlerte), CONSTRAINT fk_Alertes_Capteurs FOREIGN KEY (idCapteur) REFERENCES Capteurs(idCapteur));
--Equivalent
CREATE TABLE Capteurs(idCapteur integer, coordXCapteur integer, coordYCapteur integer, activation boolean,CONSTRAINT pk_capteurs PRIMARY KEY(idCapteur)); CREATE TABLE Alertes(idAlerte integer, idCapteur integer, coordXFeu integer, coordYFeu integer, intensiteFeu integer,CONSTRAINT pk_Alertes PRIMARY KEY(idAlerte), CONSTRAINT fk_Alertes_Capteurs FOREIGN KEY(idCapteur) REFERENCES Capteurs(idCapteur)); 

--Pour supprimer la BDD
DROP TABLE Capteurs;
DROP TABLE Alertes;
DROP TABLE Casernes;
DROP TABLE Camions;
DROP TABLE Interventions;

--Equivalent
DROP TABLE Capteurs CASCADE; DROP TABLE Alertes CASCADE; DROP TABLE Casernes CASCADE; DROP TABLE Camions CASCADE; DROP TABLE Interventions CASCADE;

--On créé alors 10 capeteurs et les feux qui y sont reliés
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

INSERT INTO Alertes VALUES(1,1,5,2,3);
INSERT INTO Alertes VALUES(2,2,3,6,7);
INSERT INTO Alertes VALUES(3,3,1,1,8);
INSERT INTO Alertes VALUES(4,4,6,6,8);
INSERT INTO Alertes VALUES(5,5,2,4,3);
INSERT INTO Alertes VALUES(6,6,2,1,9);
INSERT INTO Alertes VALUES(7,7,5,4,3);
INSERT INTO Alertes VALUES(8,8,7,6,7);
INSERT INTO Alertes VALUES(9,9,5,9,3);
INSERT INTO Alertes VALUES(10,10,7,2,3);

--Equivalent
INSERT INTO Capteurs VALUES(1, 1, 0, false); INSERT INTO Capteurs VALUES(2, 1, 1, false); INSERT INTO Capteurs VALUES(3, 1, 2, false); INSERT INTO Capteurs VALUES(4, 1, 3, false); INSERT INTO Capteurs VALUES(5, 1, 4, false); INSERT INTO Capteurs VALUES(6, 1, 5, false); INSERT INTO Capteurs VALUES(7, 1, 6, false); INSERT INTO Capteurs VALUES(8, 1, 7, false); INSERT INTO Capteurs VALUES(9, 1, 8, false); INSERT INTO Capteurs VALUES(10, 1, 9, false); INSERT INTO Alertes VALUES(1,1,5,2,3); INSERT INTO Alertes VALUES(2,2,3,6,7); INSERT INTO Alertes VALUES(3,3,1,1,8); INSERT INTO Alertes VALUES(4,4,6,6,8); INSERT INTO Alertes VALUES(5,5,2,4,3); INSERT INTO Alertes VALUES(6,6,2,1,9); INSERT INTO Alertes VALUES(7,7,5,4,3); INSERT INTO Alertes VALUES(8,8,7,6,7); INSERT INTO Alertes VALUES(9,9,5,9,3); INSERT INTO Alertes VALUES(10,10,7,2,3);