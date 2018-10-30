var restaurants = [
    "McDonalds",
    "Cheesecake Factory",
    "Olive Garden",
    "Iron Age",
    "Pizza Hut",
    "Golden Corral",
    "A nice restaurant"
];

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
    //for (i = 0; i < restaurants.length; i++)
    //{
    //    str += "<tr><td class=restaurantSelect>" + restaurants[i] + "</td></tr>";
    //}
    //document.getElementById("RestaurantList").innerHTML = str;
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

        table += "<tr><td class=restaurantSelect>" +
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
