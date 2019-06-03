from socket import *
import xml.etree.ElementTree as ET
import _thread

#create and set up sockets and connections
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
	
		_thread.start_new_thread(process,(connectionSocket,))
	
	serverSocket.close() 

#run server
def process(connectionSocket) :	
	
	# Receives the request message from the client
	message = connectionSocket.recv(1024).decode()
	if len(message) > 1:
		try:
			# Extract the path of the requested object from the message
			print(message)
			#update cache.xml once recieved requests from network
			update_cache_xml(message)
			filename = message.split()[1]
			f = open(filename[1:],"rb")	
			outputdata = f.read()
			
			connectionSocket.send(("HTTP/1.1 200 OK\r\n\r\n" ).encode())	
			connectionSocket.send(outputdata)	
			connectionSocket.close()
			
		except IOError:
			# Send HTTP response message for file not found
			connectionSocket.send("HTTP/1.1 404 Not Found\r\n\r\n".encode())
			connectionSocket.send("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n".encode())
			connectionSocket.close()
			
#handle message from other user's client and update cache.xml file	
def update_cache_xml(message):
	tree = ET.parse("cache.xml")
	root = tree.getroot()
	ipaddress = "http://localhost:" + message.split("\r\n\r\n")[-1].split("=")[0] 
	user = root.findall("user")
	for u in user:
		if u.find("IPaddress").text == ipaddress:
			cacheDate = u.find("cacheDate")
			cacheDate.text = message.split("\r\n\r\n")[-1].split("=")[1].replace("%2C",",").replace("+"," ").replace("%3A",":") 
			break
		
	tree.write("cache.xml",xml_declaration = True, encoding='UTF-8')

#run server at port numbr 8080	
server(8080)
