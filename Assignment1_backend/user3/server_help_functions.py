
from datetime import datetime
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
        last_modified = datetime.strptime(lastModifiedElements[0].text,'%a, %d %b %Y %H:%M:%S GMT') 
        for e in lastModifiedElements:
            if (datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT') > last_modified):
                last_modified = datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT')
    
    return last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT') 
    

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
    last_modified = datetime.strptime('Mon, 1 Jan 1900 00:00:00 GMT','%a, %d %b %Y %H:%M:%S GMT') 
    
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
        
        lastModifiedElements = root.findall(".//lastModified")
        if (len (lastModifiedElements) > 0 ):
            for e in lastModifiedElements:
                if (datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT') > last_modified):
                    last_modified = datetime.strptime(e.text,'%a, %d %b %Y %H:%M:%S GMT')
        
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
    return "<script>document.getElementById(\"friendStatus\").innerHTML = \"" + friendStatus_section + "\"</script></body></html>" , last_modified.strftime('%a, %d %b %Y %H:%M:%S GMT')

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
