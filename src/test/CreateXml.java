package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
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

public class CreateXml {

	static List<Path> myFiles = new ArrayList<Path>();
	static HashMap<Integer, String> myIndex = new HashMap<Integer, String>();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String workingDate = args[0];
		String labelType = args[1];
		
		buildMyIndex();
		
		String inDir = "C:\\GP\\binnys\\"+ workingDate + "\\FromBinnys\\" + labelType; 
		String tmpInDir = inDir + "\\tmp";

        mkDir(tmpInDir);
        
        String outDir = "C:\\GP\\binnys\\"+ workingDate + "\\FromBinnys\\" + labelType; 
		String tmpOutDir = outDir + "\\output";
		
		
		mkDir(tmpOutDir);
		
        //Get a list of all PDF files in the 
		List<String> myFiles2 = labelTypeFilesList(inDir);
		
		for (int j=0; j < myFiles2.size(); j++) {
			// System.out.println("myFiles2: " + myFiles2.get(j));
			SplitFiles(inDir + "\\" + myFiles2.get(j));
		}

		for (int k=0; k < myFiles2.size(); k++) {
			myFiles.clear();
			buildXmlFile(inDir + "\\tmp\\" + myFiles2.get(k));
		}

System.out.println("---------");
		for (int k=0; k < myFiles2.size(); k++) {
			genPdf(inDir + "\\tmp\\" + myFiles2.get(k), inDir, labelType);
		}

		
	}

	private static void mkDir(String inDir) {
        File tmpInDir = new File(inDir);

        if (!tmpInDir.exists()) {
            if (tmpInDir.mkdir()) {
                System.out.println(inDir + " - Directory is created!");
            } else {
                System.out.println(inDir + " - Failed to create directory!");
            }
        } else {
        	System.out.println(inDir + " - already exists");
        }
		
		
	}
	
	private static String getFileNoPdfExt (String inFile) {
        String fileNoExt = inFile.substring(inFile.lastIndexOf("\\")+1,inFile.lastIndexOf(".pdf"));
        return fileNoExt;
	}
	
	private static void genPdf (String fileName, String inDir, String labelType) {
		
		try {
	        String fileNoExt = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.lastIndexOf(".pdf"));
			//System.out.println("fileName: " + fileName);
			String foFile = fileName.substring(0, fileName.lastIndexOf(".pdf")) + "\\fo\\" + fileNoExt + ".fo";
			String xmlFile = fileName.substring(0, fileName.lastIndexOf(".pdf")) + "\\labels.xml";
			String outputPdf = inDir + "\\output\\" + fileNoExt + ".pdf";
//			System.out.println("foFile: " + foFile);
//	        System.out.println("fileNoExt: " + fileNoExt);
//	        System.out.println("xmlFile: " + xmlFile);
//	        System.out.println("outputPdf: " + outputPdf);
				
			String myCmd = "C:/GP/bin/gen-binnys-fo.bat 0726 2017 " + foFile + " " +xmlFile +  " " + outputPdf + " " + labelType+ " 2>&1";
//			System.out.println("XEP cmd: " + myCmd);
			Process p = Runtime.getRuntime().exec("cmd /c "+ myCmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}

		} catch (IOException e1) {
		} catch (InterruptedException e2) {
		}

		System.out.println("\nDone generating all PDFs for " + labelType);

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

				Element groupElement = doc.createElement("group");

				String fullFile = myFiles3.get(i).toString();
				String fileOnly = fullFile.substring(fullFile.lastIndexOf("\\") + 1);
				String fileNoExt = fullFile.substring(fullFile.lastIndexOf("\\")+1,fullFile.indexOf("."));
				String dirName = inFile.substring(0, inFile.lastIndexOf("\\")) + "\\" + fileNoExt;
				
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
					fileNoExt = fullFile.substring(fullFile.lastIndexOf("\\")+1,fullFile.indexOf("."));
					// dirName = fullFile.substring(0, fullFile.lastIndexOf("\\"));
					// dirName = inFile.substring(0, inFile.lastIndexOf("\\"));
					dirName = inFile.substring(0, inFile.lastIndexOf("\\")) + "\\" + fileNoExt;
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
			//System.out.println("output: " + xmlFile);
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static void SplitFiles (String inFile) throws IOException, InterruptedException {
		
        String fileOnly = inFile.substring(inFile.lastIndexOf("\\")+1);
        String fileNoExt = inFile.substring(inFile.lastIndexOf("\\")+1,inFile.lastIndexOf(".pdf"));
        String dirName  = inFile.substring(0, inFile.lastIndexOf("\\"));
        String newDirName = dirName + "\\tmp\\" + fileNoExt;
        String foName = newDirName + "\\fo";
        
        mkDir(newDirName);
        mkDir(foName);
        
        
		// Loading an existing PDF document
		File file = new File(inFile);
		PDDocument document;
		document = new PDDocument();
		
		document.close();
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
			pd.save(newDirName +"\\" + fileNoExt + "." + myIndex.get(i) + ".pdf");
			pd.close();
			i++;;
		}
		//System.out.println(dirName + " - split complete");
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
            		//System.out.println(file.getName());
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
	
	private static void buildMyIndex () {

		myIndex.put(1, "A");
		myIndex.put(2, "B");
		myIndex.put(3, "C");
		myIndex.put(4, "D");
		myIndex.put(5, "E");
		myIndex.put(6, "F");
		myIndex.put(7, "G");
		myIndex.put(8, "H");
		myIndex.put(9, "I");
		myIndex.put(10, "J");
		myIndex.put(11, "K");
		myIndex.put(12, "L");
		myIndex.put(13, "M");
		myIndex.put(14, "N");
		myIndex.put(15, "O");
		myIndex.put(16, "P");
		myIndex.put(17, "Q");
		myIndex.put(18, "R");
		myIndex.put(19, "S");
		myIndex.put(20, "T");
		myIndex.put(21, "U");
		myIndex.put(22, "V");
		myIndex.put(23, "W");
		myIndex.put(24, "X");
		myIndex.put(25, "Y");
		myIndex.put(26, "Z");
		myIndex.put(27, "AA");
		myIndex.put(28, "AB");
		myIndex.put(29, "AC");
		myIndex.put(30, "AD");
		myIndex.put(31, "AE");
		myIndex.put(32, "AF");
		myIndex.put(33, "AG");
		myIndex.put(34, "AH");
		myIndex.put(35, "AI");
		myIndex.put(36, "AJ");
		myIndex.put(37, "AK");
		myIndex.put(38, "AL");
		myIndex.put(39, "AM");
		myIndex.put(40, "AN");
		myIndex.put(41, "AO");
		myIndex.put(42, "AP");
		myIndex.put(43, "AQ");
		myIndex.put(44, "AR");
		myIndex.put(45, "AS");
		myIndex.put(46, "AT");
		myIndex.put(47, "AU");
		myIndex.put(48, "AV");
		myIndex.put(49, "AW");
		myIndex.put(50, "AX");
		myIndex.put(51, "AY");
		myIndex.put(52, "AZ");
		myIndex.put(53, "BA");
		myIndex.put(54, "BB");
		myIndex.put(55, "BC");
		myIndex.put(56, "BD");
		myIndex.put(57, "BE");
		myIndex.put(58, "BF");
		myIndex.put(59, "BG");
		myIndex.put(60, "BH");
		myIndex.put(61, "BI");
		myIndex.put(62, "BJ");
		myIndex.put(63, "BK");
		myIndex.put(64, "BL");
		myIndex.put(65, "BM");
		myIndex.put(66, "BN");
		myIndex.put(67, "BO");
		myIndex.put(68, "BP");
		myIndex.put(69, "BQ");
		myIndex.put(70, "BR");
		myIndex.put(71, "BS");
		myIndex.put(72, "BT");
		myIndex.put(73, "BU");
		myIndex.put(74, "BV");
		myIndex.put(75, "BW");
		myIndex.put(76, "BX");
		myIndex.put(77, "BY");
		myIndex.put(78, "BZ");
		myIndex.put(79, "CA");
		myIndex.put(80, "CB");
		myIndex.put(81, "CC");
		myIndex.put(82, "CD");
		myIndex.put(83, "CE");
		myIndex.put(84, "CF");
		myIndex.put(85, "CG");
		myIndex.put(86, "CH");
		myIndex.put(87, "CI");
		myIndex.put(88, "CJ");
		myIndex.put(89, "CK");
		myIndex.put(90, "CL");
		myIndex.put(91, "CM");
		myIndex.put(92, "CN");
		myIndex.put(93, "CO");
		myIndex.put(94, "CP");
		myIndex.put(95, "CQ");
		myIndex.put(96, "CR");
		myIndex.put(97, "CS");
		myIndex.put(98, "CT");
		myIndex.put(99, "CU");
		myIndex.put(100, "CV");
		myIndex.put(101, "CW");
		myIndex.put(102, "CX");
		myIndex.put(103, "CY");
		myIndex.put(104, "CZ");
		myIndex.put(105, "DA");
		myIndex.put(106, "DB");
		myIndex.put(107, "DC");
		myIndex.put(108, "DD");
		myIndex.put(109, "DE");
		myIndex.put(110, "DF");
		myIndex.put(111, "DG");
		myIndex.put(112, "DH");
		myIndex.put(113, "DI");
		myIndex.put(114, "DJ");
		myIndex.put(115, "DK");
		myIndex.put(116, "DL");
		myIndex.put(117, "DM");
		myIndex.put(118, "DN");
		myIndex.put(119, "DO");
		myIndex.put(120, "DP");
		myIndex.put(121, "DQ");
		myIndex.put(122, "DR");
		myIndex.put(123, "DS");
		myIndex.put(124, "DT");
		myIndex.put(125, "DU");
		myIndex.put(126, "DV");
		myIndex.put(127, "DW");
		myIndex.put(128, "DX");
		myIndex.put(129, "DY");
		myIndex.put(130, "DZ");
		myIndex.put(131, "EA");
		myIndex.put(132, "EB");
		myIndex.put(133, "EC");
		myIndex.put(134, "ED");
		myIndex.put(135, "EE");
		myIndex.put(136, "EF");
		myIndex.put(137, "EG");
		myIndex.put(138, "EH");
		myIndex.put(139, "EI");
		myIndex.put(140, "EJ");
		myIndex.put(141, "EK");
		myIndex.put(142, "EL");
		myIndex.put(143, "EM");
		myIndex.put(144, "EN");
		myIndex.put(145, "EO");
		myIndex.put(146, "EP");
		myIndex.put(147, "EQ");
		myIndex.put(148, "ER");
		myIndex.put(149, "ES");
		myIndex.put(150, "ET");
		myIndex.put(151, "EU");
		myIndex.put(152, "EV");
		myIndex.put(153, "EW");
		myIndex.put(154, "EX");
		myIndex.put(155, "EY");
		myIndex.put(156, "EZ");
	}

}
