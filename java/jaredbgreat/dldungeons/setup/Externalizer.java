package jaredbgreat.dldungeons.setup;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * The purpose of this class is to read internal resource, located inside the jar
 * by URI and write them to external files.  More to the point, this if for 
 * auto-installing themes, and thus focuses on text processing methods.
 * 
 * (Note that themes will still be installed as external files so as to allow 
 * players to easily edit them without needing to edit the jar.)
 * 
 * @author JaredBGreat (Jared Blackburn)
 */
public class Externalizer {
	private BufferedReader instream;
	private BufferedWriter outstream;
	private File           outFile;
	private static final   String baseLocation = "/jaredbgreat/dldungeons/res/themes/";
	private static         String outDirectory;
	
	private static final String[] themes 
			= new String[]{"common.cfg",
				           "continentalShelf.cfg",
				           "dank.cfg",
				           "desert.cfg",
				           "frozen.cfg",
				           "jungle.cfg",
				           "mesa.cfg",
				           "nether.cfg",
				           "oceanic.cfg",
				           "template.cfg",
				           "urban.cfg",
				           "villagelike.cfg",
				           "volcanic.cfg"};
	
	public Externalizer(String outDir) {
		outDirectory = outDir;
	}
	
	public void copyOut(String name) {
		try {
			outFile = new File(outDirectory + name);
			if(outFile.exists()) return;
			instream = new BufferedReader(new InputStreamReader(getClass()
					.getResourceAsStream(baseLocation + name)));
			outstream = new BufferedWriter(new FileWriter(outFile));
			String line;
			if((instream != null) && (outstream != null)) 
				while((line = instream.readLine()) != null) {
					outstream.write(line + System.lineSeparator());
				} else {
					System.err.println("[DLDUNGEONS] Error! Failed to write theme file " + outFile);
				}
			if(instream  != null) instream.close();
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			System.err.println("[DLDUNGEONS] Error! Failed to write theme file " + outFile);
			//e.printStackTrace();
		} 
	}
	
	public boolean forceOut(String name) {
		boolean result = true;
		try {
			outFile = new File(outDirectory + name);
			if(outFile.exists()) {
				if(outFile.canWrite() && !outFile.isDirectory()) {
					outFile.delete();
				} else {
					System.err.println("[DLDUNGEONS] Warning, could not delete " + outFile);
					result = false;
				}
			}
			instream = new BufferedReader(new InputStreamReader(getClass()
					.getResourceAsStream(baseLocation + name)));
			outstream = new BufferedWriter(new FileWriter(outFile));
			String line;
			if((instream != null) && (outstream != null)) 
				while((line = instream.readLine()) != null) {
					outstream.write(line + System.lineSeparator());
				} else {
					System.err.println("[DLDUNGEONS] Error! Failed to write theme file " + outFile);
					result = false;
				}
			if(instream  != null) instream.close();
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			System.err.println("[DLDUNGEONS] Error! Failed to write theme file " + outFile);
			result = false;
			e.printStackTrace();
		} 
		return result;
	}
	
	
	public void makeThemes() {
		for(String name : themes) {
			System.out.println("[DLDUNGEONS] Installing file " + outDirectory + name);
			copyOut(name);
		}
	}
	
	
	public void forceThemes() {
		for(String name : themes) {
			System.out.println("[DLDUNGEONS] Installing file " + outDirectory + name);
			forceOut(name);
		}
	}
	
	
	public void makeChestCfg() {
		System.out.println("[DLDUNGEONS] Installing file " + outDirectory + "chests.cfg");
		copyOut("chests.cfg");
	}
	
	
	public void forceChestCfg() {
		System.out.println("[DLDUNGEONS] Installing file " + outDirectory + "chests.cfg");
		forceOut("chests.cfg");
	}
	

}
