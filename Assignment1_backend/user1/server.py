
from socket import *
import _thread
import server_help_functions as sh	
from datetime import datetime

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
	
		_thread.start_new_thread(process,(connectionSocket,serverPort,))
	
	serverSocket.close() 
	
#run server
def process(connectionSocket,serverPort) :	
	
	# Receives the request message from the client
	message = connectionSocket.recv(1024).decode()
	if len(message) > 1:
		try:
			# Extract the path of the requested object from the message
			print(message)
			filename = message.split()[1]
			#print(filename)
			last_modified = "" 
			sh.add_status(filename,message,serverPort) #handle current user's status update request
			sh.post_like(filename,serverPort) #post like to friend's server
			sh.add_like(message,serverPort) #handle current user's status liked by a friend 
			
			# check 'If-Modified-Since' request header from browser		
			if "If-Modified-Since:" in message: 
				
				# parse and set up 'If-Modified-Since' date time from 'If-Modified-Since' request header
				If_Modified_Since_header = datetime.strptime (message.split("If-Modified-Since: ")[-1].split("\r\n")[0],'%a, %d %b %Y %H:%M:%S GMT')
				
				if filename.endswith("html"):
					if "update" in filename: 
						last_modified = sh.cache_for_updatePage () # parse 'last-Modified' response header in update.html
					if "friend" in filename:	
						last_modified = sh.cache_for_friendPage () # parse 'last-Modified' response header in friend.html
						
				if filename.endswith(('PNG', 'jpg','png')):
					last_modified = sh.cache_for_updatePage () # parse 'last-Modified' response header in user profile picture
				
				if 	last_modified == "": #handle a fresh update.html and friend.html page,run server with out cache
					run(filename,message,connectionSocket,serverPort)
				else: 	
					last_modified_header = datetime.strptime(last_modified,'%a, %d %b %Y %H:%M:%S GMT') # set up 'last-Modified' response header
					
					if  last_modified_header <= If_Modified_Since_header: # check if 'If-Modified-Since' date is later than 'last-Modified' date
						connectionSocket.send(("HTTP/1.1 304 Not Modified\r\n\r\n" ).encode()) # then response 304 Not Modified for cache purpose
					else:	
						run(filename,message,connectionSocket,serverPort) # else run server with out cache
			
			# if browser first time send request , just run server with out cache
			else:	
				run(filename,message,connectionSocket,serverPort)
				
			connectionSocket.close()
			
		except IOError:
			# Send HTTP response message for file not found
			connectionSocket.send("HTTP/1.1 404 Not Found\r\n\r\n".encode())
			connectionSocket.send("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n".encode())
			connectionSocket.close()

#run server with out cache			
def run(filename,message,connectionSocket,serverPort):
	
	#initialise response headers 
	contentType = ""
	cache = ""
	outputdata = ""
	
	#authenticate request from differen IP address, iff user's friend could send request to this server
	#then set up a Access-Control-Allow-Origin Header
	AccessControlAllowOriginHeader = sh.Access_Control_Allow_Origin(message)
	
	
	f = open(filename[1:],"rb")	
	outputdata = f.read()	
	
	if filename.endswith("html"):
		contentType = "Content-Type: text/html\r\n"
		if "update" in filename:
			outputdata += sh.display_status_in_update_html().encode()      # render user's status in update.html
			outputdata += sh.display_friendlist_in_update_html ().encode() # render user's friend list in update.html
			last_modified = sh.cache_for_updatePage () #set up Last-Modified response header from user's status.xml 
			
		if "friend" in filename:
			# compute each friend's status of this user
			friendStatus = sh.display_friendStatus_in_friend_html ("http://localhost:" + str(serverPort)) 
			outputdata += friendStatus[0].encode() # render user's each friends status in friend.html
			last_modified = friendStatus[1] # set up Last-Modified response header from user's friends status.xml
		
		#set up Last-Modified and Cache-Control response header	for update.html and friend.html
		cache =  "Last-Modified:" + last_modified + "\r\n" + "Cache-Control:public, max-age=31536000\r\n"  

	if filename.endswith(('PNG', 'jpg','png')):
		contentType = "Content-Type: image/"+filename.split('.')[-1] +"\r\n"	
		last_modified = sh.cache_for_updatePage () #set up Last-Modified response header from user's status.xml
		#set up Last-Modified and Cache-Control response header for user's profile picture
		cache =  "Last-Modified:" + last_modified + "\r\n" + "Cache-Control: public, max-age=31536000\r\n" 
		
	content_length = "Content-Length:" + str (len(outputdata)) + "\r\n"		
	connectionSocket.send(("HTTP/1.1 200 OK\r\n"+ contentType + content_length + cache + AccessControlAllowOriginHeader  + "\r\n" ).encode())		
	connectionSocket.send(outputdata)

#run server now
server(8081)
