package com.jiang.rename_tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件重命名小工具。
 * 
 * @author Yuming Jiang
 * @since 0.0.1-SNAPSHOT
 */
public class App {
	
    public static void main(String[] args) {
    	
    	final String inputDir = "./origin-files/";
    	final String outputDir = "./renamed-files/";
    	
    	if(!Paths.get(inputDir).toFile().exists()) {
    		throw new RuntimeException("Can not find the files that should be renamed");
    	}
    	
    	if(!Paths.get(outputDir).toFile().exists()) {
    		Paths.get(outputDir).toFile().mkdir();
    	}
    	
    	final int[] renamedFilesNum = new int[1];
    	renamedFilesNum[0] = 0;
    	
    	try {
			Files.walk(Paths.get(inputDir))
			.filter(path -> !path.toFile().getName().equals("origin-files"))
			.filter(path -> path.toFile().isDirectory())
			.forEach(dirPath -> {
				final int[] count = new int[1];
				count[0] = 1;
				try {
					Files.walk(dirPath)
					.filter(path -> !path.toFile().isDirectory())
					.forEach(filePath -> {
					File temp = filePath.toFile();
					String fileName = temp.getName();
					String[] fileNameArray = fileName.split("-");
					String pdfTitle = fileNameArray[1].trim().replaceAll("《", "").replaceAll("》", "");
					String pdfAuthor = fileNameArray[2].trim();
					String[] parentPathStringArray = temp.getParent().split("\\\\");
					String[] codeArray = parentPathStringArray[2].split(" ");
					String code;
					if('0'==codeArray[0].charAt(0)) {
						code = codeArray[0].substring(1) + "-" +count[0];
					}else {
						code = codeArray[0] + "-" +count[0];
					}
					count[0]++;
					renamedFilesNum[0]++;
					System.out.println("./renamed-files/" + code + " " + pdfTitle + " - " + pdfAuthor);
					temp.renameTo(new File("./renamed-files/" + code + " " + pdfTitle + " - " + pdfAuthor));
					});;
					dirPath.toFile().delete();
				}catch(IOException e) {
					e.printStackTrace();
				}
			});
		}catch(IOException e) {
			e.printStackTrace();
		}
    	
    	Paths.get(inputDir).toFile().deleteOnExit();
    	
    	System.out.println("There are " + renamedFilesNum[0] + " files renamed.");
        
    }
    
}
