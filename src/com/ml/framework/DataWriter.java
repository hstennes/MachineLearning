package com.ml.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {

	public DataWriter() {
		
	}
	
	public void saveLtm(String path, Vector ltm) throws IOException {
		File file = new File(path);
		FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        
        for(int i = 0; i < ltm.getLength(); i++) {
        		bw.write(Float.toString(ltm.get(i)) + ",");
        }
        bw.flush();
        bw.close();
		System.out.println("Ltm saved");
		
	}
	
}
