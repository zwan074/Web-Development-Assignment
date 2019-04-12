"use strict"

function hasliked(likes,userIP,IPaddress) // Submit button clicked
  {

	for (let i = 0; i < IPaddress.length; i++) {
    	if (IPaddress[i].childNodes[0].nodeValue == userIP ) return true;
    }
	
	return false;
	
  }

function submitForm(FormElement)
{
  var xhttp = new XMLHttpRequest();
  xhttp.open (FormElement.method, FormElement.action, true);
  xhttp.send (new FormData (FormElement));
  return false;
}

function fetch_data_from_server() {
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	displayFriendList(this);
	    	displayStatus(this);

	    }
	};
	xhttp.open("GET", "status.xml", true);
	xhttp.send();
	
	
}

function displayFriendList(xml) {
    var txt , xmlDoc, friendlist,ipInfriendlist;
    xmlDoc = xml.responseXML;
    txt = "<table style=\"width:7%; border: 1px solid black; border-collapse: collapse;\">";
    txt += "<tr><th>Friends List</th></tr>";
    friendlist = xmlDoc.getElementsByTagName("friendlist");
    
    if ( friendlist[0] != null  ) {
    	ipInfriendlist = friendlist[0].getElementsByTagName("IPaddress"); 
    	if ( ipInfriendlist[0] != null  ){
        	
        	for (let i = 0; i < ipInfriendlist.length; i++) {
        		txt += "<tr><td>"
                txt += ipInfriendlist[i].childNodes[0].nodeValue + " </td></tr>";
            }
        }
    }
    
    txt += "</table>";
     
    
    document.getElementById("friendsList").innerHTML = txt; 
}

function displayStatus(xml) {
    var txt , xmlDoc, status, likes, IPaddress,timestamp,content,likes;
    xmlDoc = xml.responseXML;
    txt = "<table style=\"width:20%; border: 1px solid black; border-collapse: collapse;\">";
    txt += "<tr><th>User Status</th></tr>";
    
    status = xmlDoc.getElementsByTagName("status");
    
    for (let i = 0; i < status.length; i++) {
    	if (status[i].childNodes[0] != null){
    		timestamp = status[i].getElementsByTagName("timestamp"); 
    		content = status[i].getElementsByTagName("content"); 
	    	likes = status[i].getElementsByTagName("likes"); 
	    	txt += "<table style=\"width:20%; border: 1px solid black; border-collapse: collapse;\">";
	    	if ( content[0].childNodes[0] != null && timestamp[0].childNodes[0] != null ){
	    		txt += "<tr><td>Content:</td><td>" + content[0].childNodes[0].nodeValue + "</td></tr>";
	        	txt += "<tr><td>Post Time: </td><td>" + timestamp[0].childNodes[0].nodeValue + "</td></tr>";
	        	txt += "<tr><th>List of friends LIKE:</th></tr>";
	    	}
	        
	        
	        if (likes[0] != null){
	        	IPaddress = likes[0].getElementsByTagName("IPaddress"); 
	        	for (let j = 0; j < IPaddress.length; j++) {
		        	txt += "<tr><td>" + IPaddress[j].childNodes[0].nodeValue + "</td><td>";
		        }
	        }
	        txt += "</table>";
	        
    	}
    	
	    	
    }
    txt += "</table>";
    document.getElementById("status").innerHTML = txt; 
}



function fetch_data_from_server_to_display_friendStatus() {
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	var xmlDoc = this.responseXML;
	    	var userIP = xmlDoc.getElementsByTagName("data")[0].getAttribute("ipAddress");
	
	    	display_all_friendStatus(this,userIP);
	    	

	    }
	}
	
	xhttp.open("GET", "status.xml", true);
	xhttp.send();
	
	
}

function display_all_friendStatus(xml,userIP) {
	
	
	var xmlDoc, friend, friendlist,ipInfriendlist;
    xmlDoc = xml.responseXML;
    friendlist = xmlDoc.getElementsByTagName("friendlist");
    ipInfriendlist = friendlist[0].getElementsByTagName("IPaddress"); 
    
    for (let i = 0; i < ipInfriendlist.length; i++) {
    	
    	friend = ipInfriendlist[i].childNodes[0].nodeValue;
    	display_friendStatus(userIP,friend);
    	
    }	
	
}


function display_friendStatus(userIP,friend) {
	
	
	var friendxhttp = new XMLHttpRequest();
	var ifModifiedSince;
	
	friendxhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	    	var xmlDoc, status, likes, IPaddress,timestamp,content,likes, friendsection ;
	    	var txt ="";
	    	xmlDoc = this.responseXML;
	    	txt = "<table style=\"width:20%; border: 1px solid black; border-collapse: collapse;\">";
    	    txt += "<tr><th>";
	    	txt += "<img src=\"" + friend + "\/userprofile.PNG\" alt=\"" + friend + " profile picture\" width=\"200\" height=\"20\" \/>" + "<br>";
	    	txt += friend ;
	    	txt += "</th></tr>";
	    	status = xmlDoc.getElementsByTagName("status");
	    	
	    	for (let i = 0; i < status.length; i++) {
	        	if (status[i].childNodes[0] != null){
	        		timestamp = status[i].getElementsByTagName("timestamp"); 
	        		content = status[i].getElementsByTagName("content"); 
	    	    	likes = status[i].getElementsByTagName("likes"); 
	    	    	txt += "<table style=\"width:20%; border: 1px solid black; border-collapse: collapse;\">";
	    	    	if ( content[0].childNodes[0] != null && timestamp[0].childNodes[0] != null ){
	    	    		txt += "<tr><td>Content: </td><td>" + content[0].childNodes[0].nodeValue + "</td></tr>";
		    	        txt += "<tr><td>Post Time: </td><td>" + timestamp[0].childNodes[0].nodeValue + "</td></tr>";
	    	    	}
	    	    	
	    	        if (likes[0] != null){
	    	        	IPaddress = likes[0].getElementsByTagName("IPaddress"); 
	    	        	if (hasliked(likes,userIP,IPaddress) ){
	    	        		txt += "<tr><td><button type=\"submit\" disabled>LIKE<\/button><\/form></td></tr>"
		    	    	    txt += "<tr><td>Number of friends LIKE:</td><td> " + IPaddress.length + "</td></tr>";
	    	        	}
	    	        	
	    	        	else {
	    	        		//txt += "<tr><td><form action=\"like\/" + friend +"\/" + i + "\" method=\"post\" enctype=\"multipart\/form-data\" onsubmit=\"return submitForm(this);\">"
	    	        		txt += "<tr><td><form action=\"" + friend + "\/" + userIP + "\/like\/" + i +  "\" method=\"post\" enctype=\"multipart\/form-data\" onsubmit=\"return submitForm(this);\">"
	    	    	        
	    	        		txt += "<button type=\"submit\" onclick=\"this.disabled = true\" >LIKE<\/button><\/form></td></tr>";
	    	    	        txt += "<tr><td>Number of friends LIKE: </td><td>" + IPaddress.length + "</td></tr>";
	    	        	}
	    	        	
	    	        	
	    	        }
	    	        

	    	        
	    	        else {
	    	       
	    	        	txt += "<tr><td><form action=\"" + friend + "\/" + userIP + "\/like\/" + i +  "\" method=\"post\" enctype=\"multipart\/form-data\" onsubmit=\"return submitForm(this);\">"    	    	        
	    	        	txt += "<button type=\"submit\" onclick=\"this.disabled = true\" >LIKE<\/button><\/form></td></tr>";
	    	        	//txt += "<tr><td>Number of friends LIKE: 0 </td></tr>";
	    	        	txt += "<tr><td>Number of friends LIKE: </td><td>" + IPaddress.length + "</td></tr>";
	    	        }
	    	        
	    	        txt += "</table>";
	    	        
	    	        
	    	         
	        	}
	        	
	        }
	    	txt += "</table><br>"
	    	
	    	document.getElementById("friendStatus").innerHTML += txt;
	    	
	    }
	    
    	
	}
	
    friendxhttp.open("GET",  friend + "/status.xml", true);
    friendxhttp.send();
    
}	
	
	

