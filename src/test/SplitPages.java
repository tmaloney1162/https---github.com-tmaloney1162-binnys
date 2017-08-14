package test;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

public class SplitPages {

//	private static String workingDate = "20170726";
//	private static String labelType = "SS";
//	private static String inputDir = "C:/GP/binnys/" + workingDate + "/FromBinnys/" + labelType;
//	private static String xmlFile = inputDir + "/labels.xml";

	public static void main(String[] args) throws IOException {
		String workingDate = args[0];
		String labelType = args[1];
		String inputDir = "C:/GP/binnys/" + workingDate + "/FromBinnys/" + labelType;
		//String xmlFile = inputDir + "/labels.xml";
		
		String inFile = "C:\\GP\\binnys\\20170726\\FromBinnys\\HT8UP\\HT8up-1-555750-7.pdf";
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
		PDDocument document = PDDocument.load(file);

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
			pd.save(newDirName +"\\" + fileNoExt + ".A" + i++ + ".pdf");
		}
		System.out.println("Multiple PDF’s created");
		document.close();
	}
}