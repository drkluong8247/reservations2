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
			<td width="50%">
				<table id="RestaurantList" class="restaurantSelect">
				</table>
			</td>
			<td width="50%">
			</td>
		</tr>
		<tr width="50%">
		</tr>
	</table>

	<form>
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

		<p>Time of Reservation: <br>
			<input id="time" type="number" min="1" max="12"> : <input id="minutes" type="number" min="0" max="59">
		</p>

		<input type="submit" value="Submit" Name="submit">
	</form>
</body>
</html>
