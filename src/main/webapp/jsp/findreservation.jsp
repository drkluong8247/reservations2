<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>Reserve at your restaurant</title>
	<link rel="stylesheet" type="text/css" href="../css/styles.css">
</head>
<body>
	<script src="../js/findReservations.js"></script>

	<a href="/">Click here to make a reservation</a>
	<h3>Retrieve A Reservation</h3>
    <p>Enter your reservation ID to retrieve it: <input type="text" id="idNumber" name="idNumber"> </p>
	<button onclick="findReservation();">Retrieve your Reservation</button>
	<br>
	<div>
		<p id="errorText" class="helpText"></p>
		<p id="selectedReservation"></p>
	</div>
</body>
</html>
