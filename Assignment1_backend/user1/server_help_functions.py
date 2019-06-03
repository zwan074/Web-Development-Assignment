
from datetime import datetime
import xml.etree.ElementTree as ET
import pycurl
from io import BytesIO
from urllib.parse import urlencode
import base64

#handle user post a new status    
def add_status(filename,message,serverPort):
    
    if filename.split("/")[-1] == 'new-status' :
        #parse and get status message from txt box
        statusContent = message.split("name=\"description\"\r\n\r\n")[1].split("------")[0]
        statusContent = statusContent.replace("\r\n","*br*")
        
        #parse and set up timestamp and lastModified data 
        timestamp =  datetime.now()
        lastmodified = datetime.now()
        timestamp = timestamp.strftime('%a, %d %b %Y %H:%M:%S GMT') 
        lastmodified = lastmodified.strftime('%a, %d %b %Y %H:%M:%S GMT') 
        
        #Then update status.xml file
        update_status_xml(timestamp,lastmodified, statusContent,serverPort)
        
#update status.xml file for a new status   
def update_status_xml(timestamp, lastmodified, statusContent,serverPort):
    tree = ET.parse("status.xml")
    root = tree.getroot()
    parser = ET.XMLParser()
    #add new status data in status.xml file
    parser.feed ("<status>")
    parser.feed ("<lastModified>" + lastmodified + "</lastModified>")
    parser.feed ("<timestamp>" + timestamp + "</timestamp>")
    parser.feed ("<content>" + statusContent + "</content>")
    parser.feed ("<likes></likes>")
    parser.feed ("</status>")
    element = parser.close()
    #put it on the top to show the latest user status
    root.insert(1,element)
    tree.write("status.xml",xml_declaration = True, encoding='UTF-8')
    
    #notify cache server to update this user's last modifed data and time 
    c = pycurl.Curl()
    c.setopt(c.URL, "http://localhost:8080")
    post_data = {str(serverPort): cache_for_updatePage ()}
    postfields = urlencode(post_data)
    c.setopt(c.POSTFIELDS, postfields)
    try:
        c.perform()
    except pycurl.error:
        c.close()
        return
    c.close()
      
#handle a user like friend status at user server
def post_like(filename,serverPort):    
    if len(filename.split("/")) > 5 :
        #print(filename.split("/"))
        if filename.split("/")[1] == "like":
            #set up user IP address and the status has been liked by a user. 
            friendIP = filename.split("/")[-2].split(":")[-1]
            StatusNumber = filename.split("/")[-1]                        
            
            #post user like friend's nth status to friend's server
            c = pycurl.Curl()
            c.setopt(c.URL, "http://localhost:" + friendIP )
            post_data = {str(serverPort): "like" + StatusNumber}
            postfields = urlencode(post_data)
            c.setopt(c.POSTFIELDS, postfields)
            try:
                c.perform()
            except pycurl.error:
                c.close()
                return
            c.close()
            
#add like at friend's server            
def add_like(message,serverPort):    
    if "=like" in message:
        filename = message.split("\r\n")[-1]
        #set up user IP address and the status has been liked by a user. 
        friendIP = "http://localhost:" + filename.split("=like")[0]
        StatusNumber = filename.split("=like")[1]                      
        #print(friendIP,StatusNumber) # user like friend's x status
        #update status.xml file
        update_status_like_xml(friendIP,StatusNumber,serverPort)
            
#update status.xml file for a new liked status  
def update_status_like_xml(friendIP,StatusNumber,serverPort):
    tree = ET.parse("status.xml") 
    root = tree.getroot()
    
    #find the ip addresses in <likes> tag
    status = root.findall(".//status")
    ip_in_likes = status[int(StatusNumber)].find("likes")
    
    # check if user IP in friend's IP addresses list , make sure no double one status could only be liked once    
    if not hasliked(ip_in_likes,friendIP):
        
        # update status.xml file , that friend¡¯s HTTP server appends the user¡¯s IP address into the status post.
        lastModified = status[int(StatusNumber)].find("lastModified")
        
        parser = ET.XMLParser()
        parser.feed ("<IPaddress>" + friendIP + "</IPaddress>" )
        IPaddressElement = parser.close()
        
        lastModifiedTime =  datetime.now() 
        lastModifiedTime = lastModifiedTime.strftime('%a, %d %b %Y %H:%M:%S GMT')  
        parser = ET.XMLParser()
        parser.feed ("<lastModified>" + lastModifiedTime + "</lastModified>")
        lastModifiedElement = parser.close()
        
        ip_in_likes.append(IPaddressElement)
        lastModified.text = lastModifiedElement.text 
        
        tree.write("status.xml",xml_declaration = True, encoding='UTF-8')
        
        #notify cache server to update this user's last modifed data and time 
        c = pycurl.Curl()
        c.setopt(c.URL, "http://localhost:8080")
        post_data = { str(serverPort): cache_for_updatePage ()}
        postfields = urlencode(post_data)
        c.setopt(c.POSTFIELDS, postfields)
        try:
            c.perform()
        except pycurl.error:
            c.close()
            return
        c.close()

# check if user IP in friend's IP addresses list , make sure no double one status could only be liked once       
def hasliked(ip_in_likes,userIP):
    for ip in ip_in_likes:
        if ip.text == userIP: 
            return True
    return False   

#compute the last modifed date and time for update.html page, could also used for user profile pictures        
def cache_for_updatePage () :
    tree = ET.parse("status.xml")
    root = tree.getroot()
    lastModifiedElements = root.findall(".//lastModified")
    last_modified = ""
    
    if (len (lastModifiedElements) > 0 ):
        last_modified = datetime.strptime(lastModifiedElements[0].text,'%a, %d %b %Y %H:%M:%S GMT') 
        for e in lastModifiedElements:
            if (datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT') > last_modified):
                last_modified = datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT')
    
        return last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT') 
        
    return ""

#compute the last modifed date and time for friend.html page    
def cache_for_friendPage () :
    last_modified = datetime.strptime('Mon, 1 Jan 1900 00:00:00 GMT','%a, %d %b %Y %H:%M:%S GMT')
    
    #request cache.xml file from cache server
    response_buffer = BytesIO()
    c = pycurl.Curl() 
    c.setopt(c.URL,"http://localhost:8080/cache.xml")
    c.setopt(c.WRITEFUNCTION, response_buffer.write)
    try:
        c.perform()
    except pycurl.error:
        return ""
    c.close()
    
    response_value = response_buffer.getvalue().decode('UTF-8')
    
    #find all users data in cache.xml file
    root = ET.fromstring(response_value)
    users = root.findall("user")
    
    #find all friend's IP addresses
    tree = ET.parse("status.xml")
    root = tree.getroot()
    friendsIP = root.find("friendlist")
    
    #compare users data in cache.xml and friend's IP addresses in status.xml to find the 
    #last modified date and time for friend.html page
    for u in users:
        for f in friendsIP:
            if f.text == u.find("IPaddress").text:
                cacheDate = u.find("cacheDate")
                if (datetime.strptime(cacheDate.text,'%a, %d %b %Y %H:%M:%S GMT') > last_modified):
                    last_modified = datetime.strptime(cacheDate.text,'%a, %d %b %Y %H:%M:%S GMT')

    return last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT')           


#display status in update.html page    
def display_status_in_update_html () :
    tree = ET.parse("status.xml")
    root = tree.getroot()
    status = root.findall("status")
    
    #generate html information below
    status_section = "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
    status_section +="<tr><th>User Status</th></tr>"
    for s in status:
        content = s.find("content")
        timestamp = s.find("timestamp")
        status_section += "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
        #transfer *br* to <br>, since xml file will treat <br> as a node.
        status_section += "<tr><td>Content:</td><td>" + content.text.replace("*br*","<br>") + "</td></tr>" 
        status_section += "<tr><td>Post Time: </td><td>" + timestamp.text + "</td></tr>"
        status_section += "<tr><th>List of friends LIKE:</th></tr>"
        likes = s.find("likes")
        for l in likes:
            status_section += "<tr><td>" + l.text + "</td><td>"
        status_section += "</table>"    
    status_section += "</table>"    
    return "<script>document.getElementById(\"status\").innerHTML = \"" + status_section + "\"</script>"

#display friend's ip addresses in update.html page
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


#dipplay friend status post in friend.html page
def display_friendStatus_in_friend_html (userIP) :
    tree = ET.parse("status.xml")
    root = tree.getroot()
    
    #get all friend's IP addresses.
    friendsIP = root.find("friendlist")
    friendStatus_section = ""
    last_modified = datetime.strptime('Mon, 1 Jan 1900 00:00:00 GMT','%a, %d %b %Y %H:%M:%S GMT') 
    
    for f in friendsIP:
        
        #get friend's status.xml data
        response_buffer = BytesIO()
        curl = pycurl.Curl()
        curl.setopt(curl.URL,f.text + "/status.xml")
        curl.setopt(curl.WRITEFUNCTION, response_buffer.write)
        #send Origin header 
        curl.setopt(pycurl.HTTPHEADER, ["Origin: " + userIP])
        #handle excemption when friend's server is down.
        try:
            curl.perform()
        except pycurl.error:
            continue
        
        curl.close()
        response_value = response_buffer.getvalue().decode('UTF-8')
        root = ET.fromstring(response_value)
        
        #compute last modified data and time at the same time
        lastModifiedElements = root.findall(".//lastModified")
        if (len (lastModifiedElements) > 0 ):
            for e in lastModifiedElements:
                if (datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT') > last_modified):
                    last_modified = datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT')
        #get ip addresses in friend list
        ip_in_friendlist = root.find("friendlist")
        
        #get user profile image from friend server
        response_buffer = BytesIO()
        curl = pycurl.Curl()
        curl.setopt(curl.URL,f.text + "/userprofile.PNG")
        curl.setopt(curl.WRITEFUNCTION, response_buffer.write)
        #send Origin header 
        curl.setopt(pycurl.HTTPHEADER, ["Origin: " + userIP])
        #handle excemption when friend's server is down.
        try:
            curl.perform()
        except pycurl.error:
            continue
        
        curl.close()
        #encode image byte value to base64 string and decode to UTF-8
        userprofileImage = base64.b64encode( response_buffer.getvalue() ).decode('UTF-8')
        
        
        #Guard the friend¡¯s HTTP server will only grant access to the requested object 
        #iff the user (HTTP server) requesting the data is in it¡¯s own friend list.    
        if ( hasUserIP ( ip_in_friendlist, userIP  )):
            #generate html information below
            friendStatus = root.findall("status")
            friendStatus_section += "<table style=\\\"width:20%; border: 1px solid black; border-collapse: collapse;\\\">"
            friendStatus_section += "<tr><th>"
            friendStatus_section += "<img src=\\\"data:image/png;base64,"  + userprofileImage +   "\\\" alt=\\\"" + f.text + " profile picture\\\" width=\\\"200\\\" height=\\\"20\\\" \/>" + "<br>"
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
                    friendStatus_section += "<tr><td><form name=\\\"like\\\" action=\\\"like\/" +  f.text + "\/" + str(i) +  "\\\" method=\\\"post\\\" enctype=\\\"multipart\/form-data\\\" onsubmit=\\\"return submitForm(this);\\\">"
                    friendStatus_section += "<button type=\\\"submit\\\" onclick=\\\"this.disabled = true;\\\" >LIKE<\/button><\/form></td></tr>"
                    friendStatus_section += "<tr><td>Number of friends LIKE: </td><td>" + str (len(ip_in_likes)) + "</td></tr>"
                i += 1
                friendStatus_section += "</table>"
            friendStatus_section += "</table>"
        else:
            friendStatus_section += ""
        friendStatus_section += "<br>"    
    #return two values, the second one is the last modified date and time computed at same time in this function    
    return "<script>document.getElementById(\"friendStatus\").innerHTML = \"" + friendStatus_section + "\"</script></body></html>" , last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT')

  
#Guard the friend¡¯s HTTP server will only grant access to the requested object 
#iff the user (HTTP server) requesting the data is in it¡¯s own friend list.    
def hasUserIP (ip_in_friendlist, userIP):
    for ip in ip_in_friendlist:
        if ip.text == userIP: 
            return True
    return False

#compute Access-Control-Allow-Origin response header.
def Access_Control_Allow_Origin(message):
    origin = ""
    if "Origin: " in message:
        origin = message.split("Origin: ")[-1].split("\r\n")[0]
    elif "Referer: " in message:
        origin = message.split("Referer: ")[-1].split("/friend.html")[0]
        
    tree = ET.parse("status.xml")
    root = tree.getroot()
    allowed_ip = ""
    friendsIP = root.find("friendlist")
    for ip in friendsIP:
        if ( ip.text == origin  ):
            allowed_ip = ip.text 
            return "Access-Control-Allow-Origin:" + allowed_ip + "\r\n"
    return ""