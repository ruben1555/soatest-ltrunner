package com.parasoft.soavirt.loadtestrunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import com.parasoft.api.Application;

public class Executor {
	
	public Executor(String basePath) {
		
		try {			
			ProcessBuilder builder = new ProcessBuilder(Paths.get("").toAbsolutePath().toFile() + File.separator + "lt.exe", "-cmd", "-run", "loadtest.script");

			builder.directory(new File(basePath));
			builder.redirectErrorStream(true);
			
			Process process = builder.start();		
			InputStream in = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			while (process.isAlive()) {
				
				Thread.sleep(100);
				
				String line = null;
				while((line = br.readLine()) != null)
					Application.showMessage("Parasoft Load Test -- " + line);
			}
		}
		catch (Exception e) {
			Application.showMessage("Error with execution: " + e);
		}
	}

}
