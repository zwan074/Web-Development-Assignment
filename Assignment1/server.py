
from socket import *
import _thread
from datetime import datetime, timedelta
import xml.etree.ElementTree as ET

#user post status	
def add_status(filename,message):
	if filename.split("/")[-1] == 'new-status':
		userID = filename.split("/")[1]
		statusContent = message.split("\n")[15] #get status message from txt box
		timestamp =  datetime.utcnow()- timedelta(seconds=30)  
		timestamp = timestamp.strftime('%a, %d %b %Y %H:%M:%S GMT') 
		print(userID,statusContent,timestamp)
		update_status_xml(timestamp, statusContent,userID)
	
def update_status_xml(timestamp, statusContent,userID):
	tree = ET.parse(userID + "/status.xml")
	root = tree.getroot()
	parser = ET.XMLParser()
	parser.feed ("<status>")
	parser.feed ("<lastModified>" + timestamp + "</lastModified>")
	parser.feed ("<timestamp>" + timestamp + "</timestamp>")
	parser.feed ("<content>" + statusContent + "</content>")
	parser.feed ("<likes></likes>")
	parser.feed ("</status>")
	element = parser.close()
	root.append(element)
	tree.write(userID + "/status.xml")

#user like friend status
def add_like(filename):
	if len(filename.split("/")) > 3:
		if filename.split("/")[2] == 'like':
			userID = filename.split("/")[1]
			friendID = filename.split("/")[3]
			StatusNumber = filename.split("/")[-1]
			print(userID,friendID,StatusNumber) # user like friend's x status
			update_status_like_xml(userID,friendID,StatusNumber)

def update_status_like_xml(userID,friendID,StatusNumber):
	tree = ET.parse(friendID + "/status.xml") # userID + status.xml
	root = tree.getroot()
	status = root.findall(".//status")
	likes = status[int(StatusNumber)].find("likes")
	lastModified = status[int(StatusNumber)].find("lastModified")
	
	parser = ET.XMLParser()
	parser.feed ("<IPaddress>" + userID + "</IPaddress>" )
	IPaddressElement = parser.close()
	
	lastModifiedTime =  datetime.utcnow() - timedelta(seconds=30)  
	lastModifiedTime = lastModifiedTime.strftime('%a, %d %b %Y %H:%M:%S GMT') 
		
	parser = ET.XMLParser()
	parser.feed ("<lastModified>" + lastModifiedTime + "</lastModified>")
	lastModifiedElement = parser.close()
	
	
	likes.append(IPaddressElement)
	lastModified.text = lastModifiedElement.text 
	tree.write( friendID + "/status.xml")

def cache_for_updatePage (filename) :
	userID = filename.split("/")[1]
	tree = ET.parse(userID + "/status.xml")
	root = tree.getroot()
	lastModifiedElements = root.findall(".//lastModified")
	last_modified = ""
	if (len (lastModifiedElements) > 0 ):
		last_modified = "Mon, 01 Jan 1900 00:00:00 GMT"
		for e in root.iter("lastModified"):
			if (e.text > last_modified):
				last_modified = e.text
		last_modified = "Last-Modified: " + last_modified # find last updated like / status
	
	return last_modified
	
	
		
def process(connectionSocket) :	
	# Receives the request message from the client
	message = connectionSocket.recv(1024).decode()
	if len(message) > 1:
		try:
			# Extract the path of the requested object from the message
			#print(message)
			filename = message.split()[1]
			print(filename)
			add_status(filename,message)
			add_like(filename)
			f = open(filename[1:],"rb")	
			outputdata = f.read()
			contentType = ""
			cacheControl = "";
			
			if filename.endswith("html"):
				contentType = "text/html"
			if filename.endswith(('png', 'jpg')):
				contentType = "image/"+filename.split('.')[-1]
			if filename.endswith("xml"):
				last_modified = cache_for_updatePage (filename)
				cacheControl = "cache-control: private, must-revalidate\r\n" + last_modified + "\r\n\r\n"
				
			connectionSocket.send(("HTTP/1.1 200 OK Content-Type:"+ contentType + "\r\n" + cacheControl + "\r\n" ).encode())
			connectionSocket.send(outputdata)
			connectionSocket.close()
		except IOError:
			# Send HTTP response message for file not found
			connectionSocket.send("HTTP/1.1 404 Not Found\r\n\r\n".encode())
			connectionSocket.send("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n".encode())
			connectionSocket.close()


serverSocket = socket(AF_INET, SOCK_STREAM)

serverPort = 8080
serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
serverSocket.bind(("", serverPort))

serverSocket.listen(5)
print('The server is running')	
# Server should be up and running and listening to the incoming connections

while True:
	
	# Set up a new connection from the client
	connectionSocket, addr = serverSocket.accept()
	#Clients timeout after 60 seconds of inactivity and must reconnect.
	connectionSocket.settimeout(60)
	# start new thread to handle incoming request
	_thread.start_new_thread(process,(connectionSocket,))

serverSocket.close()  



