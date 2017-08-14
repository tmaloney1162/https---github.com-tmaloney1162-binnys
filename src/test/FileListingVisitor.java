package test;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/** Recursive listing with SimpleFileVisitor in JDK 7. */
public final class FileListingVisitor {
    public  final static String PROLOG = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>";
    public  final static String OPEN_START = "<";
    public  final static String OPEN_END = "</";
    public  final static String CLOSE = ">";
    public  final static String NEWLINE = "\n";
    public  final static String INDENT = "\t";	
  
    private static String workingDate = "20170726";
    private static String fromBinnysDir = "FromBinnys";
    private static String whichRun = "FromBinnys";
    private static String inputDir = "C:/GP/binnys/"+workingDate+"/"+fromBinnysDir;  
  
    
    public static StringBuffer output = new StringBuffer();
    public static String header, rowname, rootname, footerField1, footerField2, footerCopyrightYear;

    
  public static void main(String... aArgs) throws IOException{
    //String ROOT = "C:\\test";
    String ROOT = inputDir;
    
    output.append(header);
    output.append(NEWLINE);
    output.append(OPEN_START + rootname + CLOSE);
    output.append(NEWLINE);
    
    FileVisitor<Path> fileProcessor = new ProcessFile();
    Files.walkFileTree(Paths.get(ROOT), fileProcessor);
  }

  private static final class ProcessFile extends SimpleFileVisitor<Path> {
    @Override public FileVisitResult visitFile(
      Path aFile, BasicFileAttributes aAttrs
    ) throws IOException {
      System.out.println("Processing file:" + aFile);
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