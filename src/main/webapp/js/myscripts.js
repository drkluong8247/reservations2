var restaurantSelected = false;

var openHour = 0;
var openMinute = 0;
var closeHour = 0;
var closeMinute = 0;

function initialize()
{
    retrieveRestaurants();
    initializeDate();
    initializeTime();
}

function retrieveRestaurants()
{
    makeRequest("/retrieve", fillRestaurants);
}

function initializeTime()
{
    updateTime();
}

function initializeDate()
{
    var today = new Date();

    var hours = today.getHours();
    if(hours >= 19)
    {
        today = new Date(today.valueOf() + 86400000);
    }

    var month = today.getMonth();
    var day = today.getDate();
    var year = today.getFullYear();

    var mons = document.getElementById("month").options;
    for(var i = 0; i < mons.length; i++)
    {
        if(mons[i].value == month+1)
        {
            mons.selectedIndex = i;
            break;
        }
    }

    document.getElementById("day").value = day;

    var years = document.getElementById("year").options;
    for(var i = 0; i < years.length; i++)
    {
        if(years[i].value == year)
        {
            years.selectedIndex = i;
            break;
        }
    }
}


function makeRequest(url, func)
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            func(this);
        }
    }

    xhttp.open("GET", url, true);
    xhttp.send();
}


function fillRestaurants(xml)
{
    var xmlData = xml.responseXML;
    var rList = xmlData.getElementsByTagName("restaurant");
    var table = "";
    for (i = 0; i < rList.length; i++)
    {
        var name = "Restaurant Name: " + rList[i].getElementsByTagName("name")[0].childNodes[0].nodeValue + "<br>";
        var address = "Restaurant Address: " + rList[i].getElementsByTagName("address")[0].childNodes[0].nodeValue + "<br>";
        var rating = "Rating: " + rList[i].getElementsByTagName("rating")[0].childNodes[0].nodeValue + "<br>";
        var pricing = "Price Range: " + rList[i].getElementsByTagName("pricerange")[0].childNodes[0].nodeValue + "<br>";
        var foodtype = "Food type: " + rList[i].getElementsByTagName("foodtype")[0].childNodes[0].nodeValue + "<br>";

        var openHours = rList[i].getElementsByTagName("opentime")[0].childNodes[0].nodeValue;
        var closeHours = rList[i].getElementsByTagName("closetime")[0].childNodes[0].nodeValue;

        var openFrom = "Open From: " + openHours + "  -  " + closeHours + " ";

        var id = rList[i].getElementsByTagName("restaurantid")[0].childNodes[0].nodeValue;
        var selectButton = "<button onclick=selectRestaurant(" + id + ")> Select this Restaurant</button>";

        table += "<tr onclick=selectRestaurant(" + id + ")><td>" +
        name + address + rating + foodtype + pricing + openFrom + selectButton +
        "</td></tr>";
    }
    document.getElementById("RestaurantList").innerHTML = table;
}


function selectRestaurant(i)
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            updateSelectedRestaurant(this);
        }
    }

    xhttp.open("POST", "/select", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("id=" + i);
}

function updateSelectedRestaurant(xml)
{
    var xmlData = xml.responseXML;
    var rList = xmlData.getElementsByTagName("restaurant");
    var result = "";
    for (i = 0; i < rList.length; i++)
    {
        var name = "Name: " + rList[i].getElementsByTagName("name")[0].childNodes[0].nodeValue + "<br>";
        var address = "Address: " + rList[i].getElementsByTagName("address")[0].childNodes[0].nodeValue + "<br>";
        var rating = "Rating: " + rList[i].getElementsByTagName("rating")[0].childNodes[0].nodeValue + "<br>";
        var pricing = "Price Range: " + rList[i].getElementsByTagName("pricerange")[0].childNodes[0].nodeValue + "<br>";
        var foodtype = "Food type: " + rList[i].getElementsByTagName("foodtype")[0].childNodes[0].nodeValue + "<br>";

        var openHours = rList[i].getElementsByTagName("opentime")[0].childNodes[0].nodeValue;
        var closeHours = rList[i].getElementsByTagName("closetime")[0].childNodes[0].nodeValue;

        var openFrom = "Open From: " + openHours + "  -  " + closeHours + "<br>";

        result = name + address + rating + foodtype + pricing + openFrom;

        var id = "<input type=\"hidden\" id=\"sRestaurantId\" name=\"sRestaurantId\" value=\""
            + rList[i].getElementsByTagName("restaurantid")[0].childNodes[0].nodeValue + "\">";
        result += id;

        // Updates some javascript variables for time validation.
        openHour = rList[i].getElementsByTagName("openhour")[0].childNodes[0].nodeValue;
        openMinute = rList[i].getElementsByTagName("openminute")[0].childNodes[0].nodeValue;
        closeHour = rList[i].getElementsByTagName("closehour")[0].childNodes[0].nodeValue;
        closeMinute = rList[i].getElementsByTagName("closeminute")[0].childNodes[0].nodeValue;
    }
    document.getElementById("selectedRestaurant").innerHTML = result;
    restaurantSelected = true;
}


function updateTime()
{
    var hour = getHour();
    var minute = getMinute();
    var AMorPM = getMorningNight();

    var str = "  ";
    str += hour + " : ";
    str += minute + " ";
    str += AMorPM;

    if(hour == "12" && minute == "00")
    {
        if(AMorPM == "AM") {
            str += " (midnight)";
        }
        else {
            str += " (noon)";
        }
    }
    document.getElementById("timeDisplay").innerHTML = str;

    checkTime();
}

function getHour()
{
    var hour = document.getElementById("hour");
    return hour.value;
}

function getMinute()
{
    var minuteName = "minutes";
    var i;
    for(i = 0; i <= 45; i+=15)
    {
        var minuteId = minuteName + i;
        var minutes = document.getElementById(minuteId);
        if(minutes.checked)
        {
            if(i == 0)
            {
                return "00";
            }
            return i;
        }
    }

    return "00";
}

function getMorningNight()
{
    var am = document.getElementById("AM");
    if(am.checked)
    {
        return "AM";
    }

    return "PM";
}


// Returns true if the form inputs are valid.
function validateForm()
{
    if(!restaurantSelected)
    {
        document.getElementById("selectedRestaurant").innerHTML =
        "<p class=\"helpText\">" + "Pick a restaurant from the results above." + "</p>";
        window.scrollTo(0, 200);
        return false;
    }

    if(!checkDate())
    {
        return false;
    }

    if(!checkTime())
    {
        return false;
    }
    return true;
}

// Returns true if the date is valid & is after the current date (or is today's date).
function checkDate()
{
    var today = new Date();

    // Checks that the selected day is actually valid first.
    if(dateInvalid())
    {
        return false;
    }

    // Checks the selected year
    var year = today.getFullYear();
    var temp = document.getElementById("year");
    var selectedYear = temp.options[temp.selectedIndex].value;
    if(selectedYear < year)
    {
        suggestDate("The selected year is in the past.");
        document.getElementById("year").focus();
        return false;
    }
    else if(selectedYear == year)
    {
        // Checks the selected month
        var month = today.getMonth() + 1;
        temp = document.getElementById("month");
        var selectedMonth = temp.options[temp.selectedIndex].value;
        if (selectedMonth < month)
        {
            suggestDate("The selected month is in the past. Please change to a later month.");
            document.getElementById("month").focus();
            return false;
        }
        else if(selectedMonth == month)
        {
            // Checks the selected day
            var day = today.getDate();
            var selectedDay = document.getElementById("day").value;
            if(selectedDay < day)
            {
                suggestDate("The selected day is in the past. Please change to a later day.");
                document.getElementById("day").focus();
                return false;
            }
        }
    }

    document.getElementById("dateHelp").innerHTML = "";
    return true;
}

// Updates the help string for the date
function suggestDate(helpString)
{
    document.getElementById("dateHelp").innerHTML = helpString;
}

// Returns true if the date is invalid (basically just checks the day.)
function dateInvalid()
{
    var temp = document.getElementById("year");
    var year = temp.options[temp.selectedIndex].value;
    temp = document.getElementById("month");
    var month = temp.options[temp.selectedIndex].value;
    var day = document.getElementById("day").value;

    var days = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    if(isLeapYear(year))
    {
        days[2] = 29;
    }

    if(isNaN(day))
    {
        var help = "The day in the date should be numeric (1-" + days[month] + ").";
        suggestDate(help);
        document.getElementById("day").focus();
        return true;
    }

    if(day < 0 || day > days[month])
    {
        var help = "The day in the date should be from 1 to " + days[month] + ".";
        suggestDate(help);
        document.getElementById("day").focus();
        return true;
    }

    return false;
}

function isLeapYear(year)
{
    if(year % 4 == 0)
    {
        if(year % 100 == 0)
        {
            if(year % 400 == 0)
            {
                return true;
            }
            return false;
        }
        return true;
    }

    return false;
}

// Returns true if the time is valid
function checkTime()
{
    // Checks if time is outside the restaurant hours
    if(isTimeOutsideWorkingHours())
    {
        suggestTime("The time selected is outside the hours when the restaurant is open.");
        return false;
    }

    // Checks if time is outside the restaurant hours
    if(isTimeInPast())
    {
        suggestTime("This time is in the past. Either adjust the date or move the time forward.");
        return false;
    }

    document.getElementById("timeHelp").innerHTML = "";
    return true;
}

// Updates the help string for the time
function suggestTime(helpString)
{
    document.getElementById("timeHelp").innerHTML = helpString;
}

function isTimeOutsideWorkingHours()
{
    var openTime = parseInt(openHour * 60) + parseInt(openMinute);
    var closeTime = parseInt(closeHour * 60) + parseInt(closeMinute);
    if(openTime == closeTime) {
        return false;
    }

    var selectedTime = getSelectedTimeVal();
    if(openTime < closeTime) {
        return ((selectedTime >= closeTime) || (selectedTime < openTime));
    }
    else {
        return !((selectedTime > closeTime) || (selectedTime <= openTime));
    }
}

function isTimeInPast()
{
    var selectedTime = getSelectedTimeVal();

    var today = new Date();

    // Checks the selected year
    var year = today.getFullYear();
    var temp = document.getElementById("year");
    var selectedYear = temp.options[temp.selectedIndex].value;
    if(selectedYear < year)
    {
        return true;
    }
    else if(selectedYear == year)
    {
        // Checks the selected month
        var month = today.getMonth() + 1;
        temp = document.getElementById("month");
        var selectedMonth = temp.options[temp.selectedIndex].value;
        if (selectedMonth < month)
        {
            return true;
        }
        else if(selectedMonth == month)
        {
            // Checks the selected day
            var day = today.getDate();
            var selectedDay = document.getElementById("day").value;
            if(selectedDay < day)
            {
                return true;
            }
            else if(selectedDay == day)
            {
                var hoursVal = (today.getHours() * 60);
                var time = hoursVal.valueOf() + today.getMinutes();
                if (selectedTime < time) {
                    return true;
                }
            }
        }
    }

    return false;
}

// Helper function to return a comparison value for current time,
// as number of minutes past midnight.
function getSelectedTimeVal()
{
    var selectedHour = getHour();
    var selectedMinute = getMinute();
    if(selectedMinute == "00")
    {
        selectedMinute = 0;
    }
    var amPm = getMorningNight();
    if (selectedHour == 12)
    {
        if(amPm == "AM")
        {
            selectedHour = 0;
        }
    }
    else if (amPm == "PM")
    {
        selectedHour = parseInt(selectedHour) + parseInt(12);
    }

    var selectedTime = (selectedHour * 60) + selectedMinute;
    return selectedTime;
}
