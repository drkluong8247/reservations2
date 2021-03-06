<%@ page import = "java.util.*" %>

<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>Reserve at your restaurant</title>
	<link rel="stylesheet" type="text/css" href="../css/styles.css">
</head>
<body onload="initialize()">
	<script src="../js/myscripts.js"></script>

	<a href="/jsp/findreservation.jsp">Click here to retrieve a reservation</a>

	<h3>Make A Reservation</h3>

	<h1>Step 1: Select your restaurant</h1>

	<h2>Results: </h2>
	<div class="restaurantSearchResults">
		<table id="RestaurantList" class="restaurantSelect">
		</table>
	</div>

	<hr>

	<h1>Step 2: Fill in Information</h1>

	<form action="/submit" method="post" onsubmit="return validateForm();" accept-charset="UTF-8">

		<h2>Selected Restaurant: </h2>
		<div id="selectedRestaurant" class="selectedRestaurant">
			 Click a restaurant in the results above to select it.
		</div>

		<div class="datePicker">
			<h2>Date of Reservation</h2>
			<table>
				<tr>
					<th style="width: 150px;">Month</th>
					<th style="width: 150px;">Day</th>
					<th style="width: 150px;">Year</th>
				</tr>
				<tr>
					<td>
						<select id="month" name="month" oninput="checkDate();">
							<option value="1">January</option>
							<option value="2">Feburary</option>
							<option value="3">March</option>
							<option value="4">April</option>
							<option value="5">May</option>
							<option value="6">June</option>
							<option value="7">July</option>
							<option value="8">August</option>
							<option value="9">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
						</select>
					</td>
					<td>
						<input id="day" name="day" type="number" min="1" max="31" value="1" oninput="checkDate();">
					</td>
					<td>
						<select id="year" name="year" oninput="checkDate();">
							<%
								GregorianCalendar cal = new GregorianCalendar();
								int year = cal.get(Calendar.YEAR);
								for(int i = 0; i < 10; i++)
								{
									int optYear = year + i;
									out.println("<option value=\"" + optYear + "\">" + optYear + " </option>");
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<p id="dateHelp" class="helpText"></p>
					</td>
				</tr>
			</table>
		</div>

		<br>

		<div class="timeSelect">
		<h2>  Time of Reservation </h2>
		<p id="timeDisplay"></p>
		<table>
			<tr>
				<th>Hours</th>
				<th>Minutes</th>
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
						<option value="7" selected>7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select>
				</td>
				<td>
					<label for="minutes0">00</label>
					<input id="minutes0" type="radio" name="minute" value="00" onchange="updateTime();" checked><br>
					<label for="minutes15">15</label>
					<input id="minutes15" type="radio" name="minute" value="15" onchange="updateTime();"><br>
					<label for="minutes30">30</label>
					<input id="minutes30" type="radio" name="minute" value="30" onchange="updateTime();"><br>
					<label for="minutes45">45</label>
					<input id="minutes45" type="radio" name="minute" value="45" onchange="updateTime();"><br>
				</td>
				<td>
					<label for="AM">AM</label>
					<input id="AM" type="radio" name="AmPm" value="AM" onchange="updateTime();"><br>
					<label for="PM">PM</label>
					<input id="PM" type="radio" name="AmPm" value="PM" onchange="updateTime();" checked><br>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<p id="timeHelp" class="helpText"></p>
				</td>
			</tr>
		</table>
		</div>

		<br>
		<p> Number of People: <input id="numPeople" name="numPeople" type="number" min="1" max="20" value="2"></p>
		<p> Name: <input id="personName" name="personName" type="text" required></p>
		<br>

		<input type="submit" value="Submit" Name="submit">
	</form>
</body>
</html>
