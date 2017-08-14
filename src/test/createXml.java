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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class createXml {

	//private static String inputDir = "C:\\GP\\binnys\\20170726\\FromBinnys\\HT8UP\\HT8up-1-555750-7";

	//private static String xmlFile = inputDir + "/labels.xml";

	static List<Path> myFiles = new ArrayList<Path>();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String workingDate = args[0];
		String labelType = args[1];
		
		String inDir = "C:\\GP\\binnys\\"+ workingDate + "\\FromBinnys\\" + labelType; 
		
        //Get a list of all PDF files in the 
		List<String> myFiles2 = labelTypeFilesList(inDir);
		
		for (int j=0; j < myFiles2.size(); j++) {
			// System.out.println("myFiles2: " + myFiles2.get(j));
			SplitFiles(inDir + "\\" + myFiles2.get(j));
		}

		for (int k=0; k < myFiles2.size(); k++) {
			myFiles.clear();
			buildXmlFile(inDir + "\\" + myFiles2.get(k));
		}

		
	}

	private static void buildXmlFile (String inFile) {
		String inFileDir = inFile.substring(0, inFile.lastIndexOf(".pdf"));
		String xmlFile = inFileDir + "\\labels.xml";
		
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder icBuilder;
		try {
			FileVisitor<Path> fileProcessor = new ProcessFile();
//			Files.walkFileTree(Paths.get(inFileDir), fileProcessor);

			List<String> myFiles3 = labelTypeFilesList(inFileDir);
			

			icBuilder = icFactory.newDocumentBuilder();

			Document doc = icBuilder.newDocument();
			Element mainRootElement = doc.createElement("binnys");
			icBuilder = icFactory.newDocumentBuilder();
			doc.appendChild(mainRootElement);
			Attr fileCount = doc.createAttribute("fileCount");
			fileCount.setValue(String.valueOf(myFiles3.size()));
			mainRootElement.setAttributeNode(fileCount);

			// System.out.println("\nXML DOM Created Successfully..");

			for (int i = 0; i < myFiles3.size(); i++) {
				// System.out.println("i: " + i + "mod: " + i % 2);
				// if ((i % 2) ==0 ) {
				Element groupElement = doc.createElement("group");

				String fullFile = myFiles3.get(i).toString();
				String fileOnly = fullFile.substring(fullFile.lastIndexOf("\\") + 1);
//				String dirName = fullFile.substring(0, fullFile.lastIndexOf("\\"));
				String dirName = inFile.substring(0, inFile.lastIndexOf("\\"));
				// System.out.println(fileOnly);
				// System.out.println(fullFile);
				Element fileElement = doc.createElement("file");
				Attr directory = doc.createAttribute("directory");
				directory.setValue(dirName);
				fileElement.setAttributeNode(directory);
				fileElement.appendChild(doc.createTextNode(fileOnly));
			//	System.out.println(fileOnly);
				groupElement.appendChild(fileElement);
				i++;

				if (i < myFiles3.size()) {
					fullFile = myFiles3.get(i).toString();
					fileOnly = fullFile.substring(fullFile.lastIndexOf("\\") + 1);
					// dirName = fullFile.substring(0, fullFile.lastIndexOf("\\"));
					dirName = inFile.substring(0, inFile.lastIndexOf("\\"));
					// System.out.println(fileOnly);
					// System.out.println(fullFile);
					fileElement = doc.createElement("file");
					directory = doc.createAttribute("directory");
					directory.setValue(dirName);
					fileElement.setAttributeNode(directory);
				//	System.out.println(fileOnly);

					fileElement.appendChild(doc.createTextNode(fileOnly));
					groupElement.appendChild(fileElement);
				}

				mainRootElement.appendChild(groupElement);


			}

			// output DOM XML to console
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
//			StreamResult console = new StreamResult(System.out);
//			transformer.transform(source, console);
		
			StreamResult result = new StreamResult(new File(xmlFile));
			//System.out.println("input:  " + inputDir);
			System.out.println("output: " + xmlFile);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static void SplitFiles (String inFile) throws IOException, InterruptedException {
		
        String fileOnly = inFile.substring(inFile.lastIndexOf("\\")+1);
        String fileNoExt = inFile.substring(inFile.lastIndexOf("\\")+1,inFile.lastIndexOf(".pdf"));
        String dirName  = inFile.substring(0, inFile.lastIndexOf("\\"));
        System.out.println(fileOnly);
        System.out.println(dirName);
        System.out.println(fileNoExt);
        
        String newDirName = dirName + "\\" + fileNoExt;
        File newDir = new File(newDirName);
        if (!newDir.exists()) {
            if (newDir.mkdir()) {
                System.out.println(newDirName + " - Directory is created!");
            } else {
                System.out.println(newDirName + " - Failed to create directory!");
            }
        } else {
        	System.out.println(newDirName + " - already exists");
        }
		// Loading an existing PDF document
//		File file = new File("C:/GP/binnys/20170726/FromBinnys/HT8UP/HT8up-1-555750-7.pdf");
		File file = new File(inFile);
		PDDocument document;
		document = new PDDocument();
		
		document.close();
		//PDDocument document = PDDocument.load(file);
		document = PDDocument.load(file);
		
		
		// Instantiating Splitter class
		Splitter splitter = new Splitter();

		// splitting the pages of a PDF document
		List<PDDocument> Pages = splitter.split(document);

		
		// Creating an iterator
		Iterator<PDDocument> iterator = Pages.listIterator();

		// Saving each page as an individual document
		int i = 1;
		while (iterator.hasNext()) {
			PDDocument pd = iterator.next();
//			pd.save("C:/GP/binnys/20170726/FromBinnys/HT8UP/HT8up-1-555750-7.pdf-" + i++ + ".pdf");
			pd.save(newDirName +"\\" + fileNoExt + "." + i++ + ".pdf");
			pd.close();
		}
		System.out.println(dirName + " - split complete");
		document.close();	
		
	}
	
	private static List<String> labelTypeFilesList(String inputDir) {
		List<String> files = new ArrayList<String>();
        File myDir = new File(inputDir);		
        File[] fList = myDir.listFiles();
        for (File file : fList){
            if (file.isFile()){
            	if (file.getName().endsWith(".pdf")) {
            		files.add(file.getName());
            		System.out.println(file.getName());
            	}
            }
        }
        return files;
	}
	
	
	private static final class ProcessFile extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {

			if (aFile.getFileName().toString().endsWith("pdf")) {
				myFiles.add(aFile);
				System.out.println("Processing file:" + aFile);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
			// System.out.println("Processing directory:" + aDir);

			return FileVisitResult.CONTINUE;
		}
	}

}
