
from socket import *
import _thread
from datetime import datetime, timedelta
import xml.etree.ElementTree as ET
import pycurl
import certifi
from io import BytesIO

#user post status	
def add_status(filename,message):
	if filename.split("/")[-1] == 'new-status':
		statusContent = message.split("\r\n\r\n")[-1].split("------")[0]#get status message from txt box
		statusContent = statusContent.replace("\r\n","*br*")
		timestamp =  datetime.now()
		lastmodified = datetime.now() #+ timedelta(seconds=600)
		timestamp = timestamp.strftime('%a, %d %b %Y %H:%M:%S GMT') 
		lastmodified = lastmodified.strftime('%a, %d %b %Y %H:%M:%S GMT') 
		print(statusContent,timestamp,lastmodified)
		update_status_xml(timestamp,lastmodified, statusContent)
	
def update_status_xml(timestamp, lastmodified, statusContent):
	tree = ET.parse("status.xml")
	root = tree.getroot()
	parser = ET.XMLParser()
	parser.feed ("<status>")
	parser.feed ("<lastModified>" + lastmodified + "</lastModified>")
	parser.feed ("<timestamp>" + timestamp + "</timestamp>")
	parser.feed ("<content>" + statusContent + "</content>")
	parser.feed ("<likes></likes>")
	parser.feed ("</status>")
	element = parser.close()
	root.append(element)
	tree.write("status.xml")

#user like friend status
def add_like(filename):
	if len(filename.split("/")) > 3:
		if filename.split("/")[-2] == "like":
			userID = "http://" + filename.split("/")[-3]
			StatusNumber = filename.split("/")[-1]
			print(userID,StatusNumber) # user like friend's x status
			update_status_like_xml(userID,StatusNumber)

def update_status_like_xml(userID,StatusNumber):
	tree = ET.parse("status.xml") 
	root = tree.getroot()
	status = root.findall(".//status")
	likes = status[int(StatusNumber)].find("likes")
	lastModified = status[int(StatusNumber)].find("lastModified")
	
	parser = ET.XMLParser()
	parser.feed ("<IPaddress>" + userID + "</IPaddress>" )
	IPaddressElement = parser.close()
	
	lastModifiedTime =  datetime.now() #+ timedelta(seconds=600) 
	lastModifiedTime = lastModifiedTime.strftime('%a, %d %b %Y %H:%M:%S GMT') 
		
	parser = ET.XMLParser()
	parser.feed ("<lastModified>" + lastModifiedTime + "</lastModified>")
	lastModifiedElement = parser.close()
	
	
	likes.append(IPaddressElement)
	lastModified.text = lastModifiedElement.text 
	
	tree.write("status.xml")
	return

def cache_for_updatePage () :
	tree = ET.parse("status.xml")
	root = tree.getroot()
	lastModifiedElements = root.findall(".//lastModified")
	last_modified = ""
	if (len (lastModifiedElements) > 0 ):
		last_modified = lastModifiedElements[0].text 
		for e in lastModifiedElements:
			if (e.text > last_modified):
				last_modified = e.text
		#last_modified = "Last-Modified: " + last_modified + "\r\n" # find last updated like / status
	
	return last_modified
	

def Access_Control_Allow_Origin(message):
	origin = message.split("\r\n")[-3]
	tree = ET.parse("status.xml")
	root = tree.getroot()
	allowed_ip = ""
	friendsIP = root.find("friendlist")
	for ip in friendsIP:
		if ( ip.text == origin  ):
			allowed_ip = ip.text 
	
	return "Access-Control-Allow-Origin:" + allowed_ip + "\r\n"



def display_status_in_update_html () :
	tree = ET.parse("status.xml")
	root = tree.getroot()
	status = root.findall("status")
	status_section = "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
	status_section +="<tr><th>User Status</th></tr>"
	for s in status:
		content = s.find("content")
		timestamp = s.find("timestamp")
		
		status_section += "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
		status_section += "<tr><td>Content:</td><td>" + content.text.replace("*br*","<br>") + "</td></tr>"
		status_section += "<tr><td>Post Time: </td><td>" + timestamp.text + "</td></tr>"
		status_section += "<tr><th>List of friends LIKE:</th></tr>"
		likes = s.find("likes")
		for l in likes:
			status_section += "<tr><td>" + l.text + "</td><td>"
		status_section += "</table>"	
	status_section += "</table>"	
	return "<script>document.getElementById(\"status\").innerHTML = \"" + status_section + "\"</script>"

def display_friendlist_in_update_html () :
	tree = ET.parse("status.xml")
	root = tree.getroot()
	friendsIP = root.find("friendlist")
	friendlist_section = "<table style=\\\" width:7%; border: 1px solid black; border-collapse: collapse; \\\">"
	friendlist_section += "<tr><th>Friends List</th></tr>"
	for ip in friendsIP:
		friendlist_section += "<tr><td>" + ip.text + " </td></tr>"
	friendlist_section += "</table>"
	return "<script>document.getElementById(\"friendsList\").innerHTML = \"" + friendlist_section + "\"</script></body></html>"



def display_friendStatus_in_friend_html (userIP) :
	tree = ET.parse("status.xml")
	root = tree.getroot()
	friendsIP = root.find("friendlist")
	friendStatus_section = ""
	for f in friendsIP:
		response_buffer = BytesIO()
		curl = pycurl.Curl()
		curl.setopt(pycurl.CAINFO, certifi.where())
		curl.setopt(curl.URL,f.text + "/status.xml")
		curl.setopt(pycurl.HTTPHEADER, [userIP] )
		print(f.text + "/status.xml")
		curl.setopt(curl.WRITEFUNCTION, response_buffer.write)
		try:
			curl.perform()
		except pycurl.error:
			continue
		
		curl.close()
		response_value = response_buffer.getvalue().decode('UTF-8')
		root = ET.fromstring(response_value)
		ip_in_friendlist = root.find("friendlist")
		if ( hasUserIP ( ip_in_friendlist, userIP  )):
			friendStatus = root.findall("status")
			friendStatus_section += "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
			friendStatus_section += "<tr><th>"
			friendStatus_section += "<img src=\\\"" + f.text + "\/userprofile.PNG\\\" alt=\\\"" + f.text + " profile picture\\\" width=\\\"200\\\" height=\\\"20\\\" \/>" + "<br>"
			friendStatus_section += f.text 
			friendStatus_section += "</th></tr>";
			
			i = 0
			for s in friendStatus:
				content = s.find("content")
				timestamp = s.find("timestamp")
				likes = s.find("likes")
				ip_in_likes = likes.findall("IPaddress")
				friendStatus_section += "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
				friendStatus_section += "<tr><td>Content:</td><td>" + content.text.replace("*br*","<br>") + "</td></tr>"
				friendStatus_section += "<tr><td>Post Time: </td><td>" + timestamp.text + "</td></tr>"
				if (hasliked(ip_in_likes,userIP) ):
					friendStatus_section += "<tr><td><button type=\\\"submit\\\" disabled>LIKE<\/button><\/form></td></tr>"
					friendStatus_section += "<tr><td>Number of friends LIKE:</td><td> " + str (len(ip_in_likes)) + "</td></tr>"
				else:
					friendStatus_section += "<tr><td><form name=\\\"like\\\" action=\\\"" + f.text + "\/" + userIP + "\/like\/" + str(i) +  "\\\" method=\\\"post\\\" enctype=\\\"multipart\/form-data\\\" onsubmit=\\\"return submitForm(this);\\\">"
					friendStatus_section += "<button type=\\\"submit\\\" onclick=\\\"this.disabled = true;\\\" >LIKE<\/button><\/form></td></tr>"
					friendStatus_section += "<tr><td>Number of friends LIKE: </td><td>" + str (len(ip_in_likes)) + "</td></tr>"
				i += 1
				friendStatus_section += "</table>"
			friendStatus_section += "</table>"
		else:
			friendStatus_section += ""
		friendStatus_section += "<br>"	
	return "<script>document.getElementById(\"friendStatus\").innerHTML = \"" + friendStatus_section + "\"</script></body></html>"

def hasliked(ip_in_likes,userIP):
	for ip in ip_in_likes:
		if ip.text == userIP: 
			return True
	return False	 
	
def hasUserIP (ip_in_friendlist, userIP):
	for ip in ip_in_friendlist:
		if ip.text == userIP: 
			return True
	return False
	
	
			
def process(connectionSocket,serverPort) :	
	# Receives the request message from the client
	message = connectionSocket.recv(1024).decode()
	if len(message) > 1:
		try:
			# Extract the path of the requested object from the message
			print(message)
			
			contentType = ""
			cache = ""
			outputdata = ""
			
			filename = message.split()[1]
			print(filename)
			
			add_status(filename,message)
			add_like(filename)

					
			f = open(filename[1:],"rb")	
			outputdata = f.read()	
			
			if filename.endswith("html"):
				contentType = "text/html"
				last_modified = cache_for_updatePage ()
				cache = "Cache-Control: private, max-age=600 \r\n" + "Last-Modified:" + last_modified + "\r\n" 
				if "update" in filename:
					outputdata += display_status_in_update_html().encode()
					outputdata += display_friendlist_in_update_html ().encode()
				if "friend" in filename:
					outputdata += display_friendStatus_in_friend_html ("http://localhost:" + str(serverPort)).encode()
				
				
			if filename.endswith(('png', 'jpg')):
				contentType = "image/"+filename.split('.')[-1]	
					
			connectionSocket.send(("HTTP/1.1 200 OK Content-Type:"+ contentType + "\r\n" + cache + "\r\n" ).encode())
			print(("HTTP/1.1 200 OK Content-Type:"+ contentType + "\r\n" + cache + "\r\n" ))
			connectionSocket.send(outputdata)
			connectionSocket.close()
			
		except IOError:
			# Send HTTP response message for file not found
			connectionSocket.send("HTTP/1.1 404 Not Found\r\n\r\n".encode())
			connectionSocket.send("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n".encode())
			connectionSocket.close()

def server(serverPort):

	serverSocket = socket(AF_INET, SOCK_STREAM)
	serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
	serverSocket.bind(("", serverPort))
	serverSocket.listen(5)
	
	
	print("The server is running Port Numebr: " + str(serverPort) )	
	# Server should be up and running and listening to the incoming connections
	
	while True:
		
		# Set up a new connection from the client
		connectionSocket, addr = serverSocket.accept()
		#Clients timeout after 60 seconds of inactivity and must reconnect.
		connectionSocket.settimeout(60)
		# start new thread to handle incoming request
	
		_thread.start_new_thread(process,(connectionSocket,serverPort,))
	
	serverSocket.close()  

server(8080)
