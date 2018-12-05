function findReservation()
{
    i = document.getElementById("idNumber").value;

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            updateSelectedReservation(this);
        }
    }

    xhttp.open("POST", "/find", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("id=" + i);
}

function updateSelectedReservation(xml)
{
    var xmlData = xml.responseXML;

    var status = xmlData.getElementsByTagName("result");
    if(status.length <= 0)
    {
        document.getElementById("selectedReservation").innerHTML = "Sorry, we couldn't find a reservation with that ID.";
        return;
    }
    else if(status[0].childNodes[0].nodeValue != "true")
    {
        document.getElementById("selectedReservation").innerHTML = "Sorry, we couldn't find a reservation with that ID.";
        return;
    }

    var rList = xmlData.getElementsByTagName("reservation");
    var result = "";
    for (i = 0; i < rList.length; i++)
    {
        var name = "Reservation ID:  " + rList[i].getElementsByTagName("resid")[0].childNodes[0].nodeValue + "<br><br>";
        var time = "Reservation Time:  " + rList[i].getElementsByTagName("time")[0].childNodes[0].nodeValue + "<br><br>";
        var address = "Reservation Date:  " + rList[i].getElementsByTagName("date")[0].childNodes[0].nodeValue + "<br><br>";
        var numPeople = "Number of People:  " + rList[i].getElementsByTagName("numpeople")[0].childNodes[0].nodeValue + "<br><br>";
        var foodtype = "Person Name:  " + rList[i].getElementsByTagName("personname")[0].childNodes[0].nodeValue + "<br><br><br>";

        var restaurantName = "Restaurant:  " + rList[i].getElementsByTagName("restaurantname")[0].childNodes[0].nodeValue + "<br><br>";
        var restaurantAddress = "Restaurant Address: " + rList[i].getElementsByTagName("address")[0].childNodes[0].nodeValue + "<br><br>";

        result = "<hr>" + name + time + address + numPeople + foodtype + restaurantName + restaurantAddress;

        // Add some features
        var cancelId = rList[i].getElementsByTagName("resid")[0].childNodes[0].nodeValue;
        var cancel = "<br><br><br><button id=\"" + cancelId + "\" onclick=\"cancelReservation(" + cancelId + ");\"> Cancel this reservation </button>";
        var print = "<br><button onclick=\"window.print();\"> Print this reservation </button>";
        result += print + cancel;
        result += "<hr>"
    }
    document.getElementById("selectedReservation").innerHTML = result;
}

function cancelReservation(i)
{
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            checkDeletedReservation(this, i);
        }
    }

    xhttp.open("POST", "/delete", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("id=" + i);
    document.getElementById(i).disabled = true;
}

function checkDeletedReservation(xml, i)
{
    var xmlData = xml.responseXML;

    var status = xmlData.getElementsByTagName("result");
    if(status.length <= 0)
    {
        document.getElementById(i).disabled = false;
        document.getElementById("errorText").innerHTML =
            "Sorry, we couldn't process your request right now. Please try again or come back later.";
        return;
    }
    else if(status[0].childNodes[0].nodeValue != "true")
    {
        document.getElementById(i).disabled = false;
        document.getElementById("errorText").innerHTML =
            "Sorry, we couldn't process your request right now. Please try again or come back later.";
        return;
    }

    document.getElementById("errorText").innerHTML = "";
    document.getElementById("selectedReservation").innerHTML = "We successfully deleted your reservation!";
}
