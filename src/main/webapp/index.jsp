<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>Reserve at your restaurant</title>
	<link rel="stylesheet" type="text/css" href="../css/styles.css">
</head>
<body onload="retrieveRestaurants()">
	<script src="../js/myscripts.js"></script>

	<h1>Select your restaurant</h1>

	<p>Restaurant: <input id="restaurantSearch" type="text" onchange="updateRestaurants()"></p>

	<input type="checkbox" id="foodItalian"> Italian
	<input type="checkbox" id="foodKorean"> Korean
	<input type="checkbox" id="foodAmerican"> American



	<p>Search Results: </p>
	<table>
		<tr>
			<td width="100%">
				<table id="RestaurantList" class="restaurantSelect">
				</table>
			</td>
		</tr>
	</table>

	<form action="/submit" method="post">
		<p>Number of People: <input id="numPeople" type="number" min="1" max="100" value="2" oninput="validateNumber(numPeople, this.Value)"></p>

		<p> Date:
			<input type="radio" name="Month" value="January"> January
			<input type="radio" name="Month" value="Feburary"> Feburary
			<input type="radio" name="Month" value="March"> March
			<input type="radio" name="Month" value="April"> April
			<input type="radio" name="Month" value="May"> May
			<input type="radio" name="Month" value="June"> June
			<input type="radio" name="Month" value="July"> July
			<input type="radio" name="Month" value="August"> August
			<input type="radio" name="Month" value="September"> September
			<input type="radio" name="Month" value="October"> October
			<input type="radio" name="Month" value="November"> November
			<input type="radio" name="Month" value="December"> December

			<br>
			<input id="day" type="number" min="1" max="31">

			<br>
			<input id="year" type="number">
		</p>

		<p>Time of Reservation: </p>
		<p id="temp"></p>
		<table>
			<tr>
				<th>Hours</th>
				<th>minutes</th>
				<th></th>
			</tr>
			<tr>
				<td>
					<select id="hour" name="hour" onchange="updateTime()">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
				</td>
				<td>
					<input id="minutes0" type="radio" name="minute" value="00" onchange="updateTime()"> 00 <br>
					<input id="minutes15" type="radio" name="minute" value="15" onchange="updateTime()"> 15 <br>
					<input id="minutes30" type="radio" name="minute" value="30" onchange="updateTime()"> 30 <br>
					<input id="minutes45" type="radio" name="minute" value="45" onchange="updateTime()"> 45 <br>
				</td>
				<td>
					<input id="AM" type="radio" name="AmPm" value="AM" onchange="updateTime()"> AM <br>
					<input id="PM" type="radio" name="AmPm" value="PM" onchange="updateTime()" checked> PM <br>
				</td>
			</tr>
		</table>

		<input type="submit" value="Submit" Name="submit">
	</form>
</body>
</html>
