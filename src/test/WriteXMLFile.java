package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXMLFile {

	private static List<String> myFiles = new ArrayList<String>();
    private static String workingDate = "20170726";
    private static String fromBinnysDir = "FromBinnys";
    private static String whichRun = "FromBinnys";
    private static String inputDir = "C:/GP/binnys/"+workingDate+"/"+fromBinnysDir;  
    
    
	public static void main(String argv[]) throws IOException {

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("company");
		doc.appendChild(rootElement);

		// staff elements
		Element staff = doc.createElement("Staff");
		rootElement.appendChild(staff);

		// set attribute to staff element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		staff.setAttributeNode(attr);

		// shorten way
		// staff.setAttribute("id", "1");

		// firstname elements
		Element firstname = doc.createElement("firstname");
		firstname.appendChild(doc.createTextNode("yong"));
		staff.appendChild(firstname);

		// lastname elements
		Element lastname = doc.createElement("lastname");
		lastname.appendChild(doc.createTextNode("mook kim"));
		staff.appendChild(lastname);

		// nickname elements
		Element nickname = doc.createElement("nickname");
		nickname.appendChild(doc.createTextNode("mkyong"));
		staff.appendChild(nickname);

		// salary elements
		Element salary = doc.createElement("salary");
		salary.appendChild(doc.createTextNode("100000"));
		staff.appendChild(salary);
		
		
		
		
		
	    //String ROOT = "C:\\test";
	    String ROOT = inputDir;
	    
	    FileVisitor<Path> fileProcessor = new ProcessFile();
	    Files.walkFileTree(Paths.get(ROOT), fileProcessor);

	    for (String element : myFiles) {
	        //System.out.println(element);
	    	Element fileXml = doc.createElement("file");
	    	fileXml.appendChild(doc.createTextNode(element));
	    	staff.appendChild(fileXml);
	    }
	    
	    

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		//StreamResult result = new StreamResult(new File("C:\\file.xml"));

		// Output to console for testing
		 StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

		
		


Map<List<String>,List<String>> map = new HashMap<List<String>,List<String>>();
List keys = new ArrayList();
keys.add("one");keys.add("one");keys.add("two");keys.add("three");
List values = new ArrayList();
values.add("First");values.add("First12");values.add("second");values.add("second");
map.put(keys, values);
System.out.println(map.keySet().toString());
System.out.println(map.values().toString());
System.out.println("map:"+map.toString());

		

	    
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
	
	
    private static final class ProcessFile extends SimpleFileVisitor<Path> {
        @Override public FileVisitResult visitFile(
          Path aFile, BasicFileAttributes aAttrs
        ) throws IOException {
          System.out.println("Processing file:" + aFile);
          myFiles.add(aFile.toString());
          return FileVisitResult.CONTINUE;
        }
        
        @Override  public FileVisitResult preVisitDirectory(
          Path aDir, BasicFileAttributes aAttrs
        ) throws IOException {
          System.out.println("Processing directory:" + aDir);
          
          
          return FileVisitResult.CONTINUE;
        }
      }   
	
}