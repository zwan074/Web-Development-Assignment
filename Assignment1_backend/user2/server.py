
from socket import *
import _thread
import server_help_functions as sh	
	
			
def process(connectionSocket,serverPort) :	
	# Receives the request message from the client
	message = connectionSocket.recv(1024).decode()
	if len(message) > 1:
		try:
			# Extract the path of the requested object from the message
			#print(message)
			
			contentType = ""
			cache = ""
			outputdata = ""
			last_modified = ""
			
			filename = message.split()[1]
			print(filename)
			
			sh.add_status(filename,message)
			sh.add_like(filename)

					
			f = open(filename[1:],"rb")	
			outputdata = f.read()	
			
			if filename.endswith("html"):
				contentType = "text/html"

				if "update" in filename:
					outputdata += sh.display_status_in_update_html().encode()
					outputdata += sh.display_friendlist_in_update_html ().encode()
					last_modified = sh.cache_for_updatePage ()
					
				if "friend" in filename:
					friendStatus = sh.display_friendStatus_in_friend_html ("http://localhost:" + str(serverPort))
					outputdata += friendStatus[0].encode()
					last_modified = friendStatus[1]
				
			cache = "Cache-Control: private, max-age=600 \r\n" + "Last-Modified:" + last_modified + "\r\n" 
			
			if filename.endswith(('png', 'jpg')):
				contentType = "image/"+filename.split('.')[-1]	
					
			connectionSocket.send(("HTTP/1.1 200 OK Content-Type:"+ contentType + "\r\n" + cache + "\r\n" ).encode())
			#print(("HTTP/1.1 200 OK Content-Type:"+ contentType + "\r\n" + cache + "\r\n" ))
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

server(8081)
