<?php
$dsn = "mysql:host=http://127.0.0.1:5433/;dbname=Simulation";
$user = "postgres";
$passwd = "superuser";

$pdo = new PDO($dsn, $user, $passwd);

$stm = $pdo->query("SELECT * FROM 'Location'");

$result = $stm->fetchAll();

echo($result);

?>