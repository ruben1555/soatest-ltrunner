package com.parasoft.soavirt.loadtestrunner.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.parasoft.api.Application;

public class WorkingDirectory {

    private File base;
    private File dir;
    
    public WorkingDirectory(File basePath) {
    	
    	if(!basePath.exists())
    		if(!basePath.mkdir())
    			Application.showMessage("Failed to create the directory " + basePath.getPath());
    	
        this.base = basePath;
        this.dir = createWorkingDirectory(generateDirectoryName());
    }
    
    private File createWorkingDirectory(String name) {
    	
        File dir = new File(base + File.separator + name);

        if(!dir.exists())
        	if(!dir.mkdir())
                Application.showMessage("Failed to create the directory " + dir.getPath());
        
        return dir;
    }

    // Generate directory name based on current date and time
    private String generateDirectoryName() {
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    public File createFile(String fileName) {
    	
    	return new File(dir.getPath() + File.separator + fileName);
    }
    
    public File readFile(String fileName) {
    	
    	return new File(dir.getPath() + File.separator + fileName);
    }
    
    public void writeToFile(String fileName, String fileContents) {
    	
    	File newFile = createFile(fileName);
    	
    	try (FileWriter fileWriter = new FileWriter(newFile)) {
    		fileWriter.write(fileContents);
    	} catch (IOException e) {
    		Application.showMessage("An error occurred while adding the file '" + fileName + "': " + e.getMessage());
    	}
    }
    
    public String getPath() {
    	
    	return dir.getPath();
    }
}