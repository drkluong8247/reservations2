function initialize()
{
    retrieveRestaurants();
}

function retrieveRestaurants()
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        fillRestaurants(this);
        }
    };
    xhttp.open("GET", "restaurants.xml", true);
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
        var rating = "Rating: " + rList[i].getElementsByTagName("rating")[0].childNodes[0].nodeValue + "<br>";
        var foodtype = "Food type: " + rList[i].getElementsByTagName("foodtype")[0].childNodes[0].nodeValue + "<br>";

        table += "<tr class=restaurantSelect><td class=restaurantSelect>" +
        name + rating + foodtype +
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


function updateTime()
{
    var hour = getHour();
    var minute = getMinute();
    var AMorPM = getMorningNight();

    var str = "";
    str += hour + " : ";
    str += minute + " ";
    str += AMorPM;
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
