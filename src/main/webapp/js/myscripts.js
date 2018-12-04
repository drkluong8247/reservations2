var restaurantSelected = false;

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
    if(hours >= 21)
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
        var name = "Name: " + rList[i].getElementsByTagName("name")[0].childNodes[0].nodeValue + "<br>";
        var address = "Address: " + rList[i].getElementsByTagName("address")[0].childNodes[0].nodeValue + "<br>";
        var rating = "Rating: " + rList[i].getElementsByTagName("rating")[0].childNodes[0].nodeValue + "<br>";
        var foodtype = "Food type: " + rList[i].getElementsByTagName("foodtype")[0].childNodes[0].nodeValue + "<br>";

        var id = rList[i].getElementsByTagName("restaurantid")[0].childNodes[0].nodeValue;

        table += "<tr onclick=selectRestaurant(" + id + ")><td>" +
        name + address + rating + foodtype +
        "</td></tr>";
    }
    document.getElementById("RestaurantList").innerHTML = table;
}

function updateRestaurants()
{
    var searchStr = document.getElementById("restaurantSearch").value;
    searchStr = searchStr.toLowerCase();
    var str = "";
    var i;
    for (i = 0; i < restaurants.length; i++)
    {
        var entry = restaurants[i];
        entry = entry.toLowerCase();
        if(entry.includes(searchStr))
        {
            str += "<tr><td class=restaurantSelect>" + restaurants[i] + "</td></tr>";
        }
    }
    document.getElementById("RestaurantList").innerHTML = str;
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
        var foodtype = "Food type: " + rList[i].getElementsByTagName("foodtype")[0].childNodes[0].nodeValue + "<br>";

        result = name + address + rating + foodtype;

        var id = "<input type=\"hidden\" id=\"sRestaurantId\" name=\"sRestaurantId\" value=\""
            + rList[i].getElementsByTagName("restaurantid")[0].childNodes[0].nodeValue + "\">";
        result += id;
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

function validateForm()
{
    if(!restaurantSelected)
    {
        return false;
    }

    return true;
}
