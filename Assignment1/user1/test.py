
import xml.etree.ElementTree as ET


def update_status_xml(statusContent,timestamp):
    tree = ET.parse('status.xml')
    root = tree.getroot()
    parser = ET.XMLParser()
    parser.feed ("<status>")
    parser.feed ("<timestamp>" +  " Wed Apr  3 19:38:43 2019" + "</timestamp>")
    parser.feed ("<content>" + "post your status" + "</content>")
    parser.feed ("<likes>a</likes></status>")
    element = parser.close()
    root.append(element)
    tree.write('status.xml')


    

def cache_for_updatePage (filename) :
    tree = ET.parse("status.xml")
    root = tree.getroot()
    lastModifiedElements = root.findall(".//lastModified")
    last_modified = ""
    if (len (lastModifiedElements) > 0 ) :
        last_modified = "Mon, 01 Jan 1900 00:00:00 GMT"
        for e in root.iter("lastModified"):
            if (e.text > last_modified):
                last_modified = e.text
        last_modified = "Last-Modified: " + last_modified # find last updated like / status
        print(last_modified)
        
    return last_modified
  
cache_for_updatePage ("")    
