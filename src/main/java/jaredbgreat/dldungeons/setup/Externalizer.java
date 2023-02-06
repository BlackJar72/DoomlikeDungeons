package jaredbgreat.dldungeons.setup;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import jaredbgreat.dldungeons.util.debug.Logging;


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
	private static final   String BASE_LOCATION = "/jaredbgreat/dldungeons/res/themes/";
	private static         String outDirectory;
	
	private static final String[] THEMES 
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
	
	private static final String[] BLOCKS 
			= new String[]{"mixedcobble.json",
				           "mixedbricks1.json",
				           "mixedbricks2.json",
				           "mixedbricks3.json",
				           "crumbling_walls1.json",
				           "crumbling_walls2.json"};
	
	public Externalizer(String outDir) {
		outDirectory = outDir;
	}
	
	
	/**
	 * Exports a theme file by reading it from the jar and 
	 * writing it to the hard drive, but only if a file with 
	 * that path does not already exist on the drive.
	 * 
	 * @param name
	 */
	public void copyOut(String name) {
		copyOut(name, name);
	}
	
	
	/**
	 * Exports a theme file by reading it from the jar and 
	 * writing it to the hard drive, but only if a file with 
	 * that path does not already exist on the drive.
	 * 
	 * @param name
	 */
	public void copyOut(String name, String outloc) {
		try {
			outFile = new File(outDirectory + outloc);
			if(outFile.exists()) return;
			instream = new BufferedReader(new InputStreamReader(getClass()
					.getResourceAsStream(BASE_LOCATION + name)));
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
	
	
	/**
	 * Exports a theme file by reading it from the jar and 
	 * writing it to the hard drive, over-writing any file 
	 * with the same path.
	 * 
	 * @param name
	 */
	public boolean forceOut(String name) {
		return forceOut(name, name);
	}
	
	
	/**
	 * Exports a theme file by reading it from the jar and 
	 * writing it to the hard drive, over-writing any file 
	 * with the same path.
	 * 
	 * @param name
	 */
	public boolean forceOut(String name, String outloc) {
		boolean result = true;
		try {
			outFile = new File(outDirectory + outloc);
			if(outFile.exists()) {
				if(outFile.canWrite() && !outFile.isDirectory()) {
					outFile.delete();
				} else {
					System.err.println("[DLDUNGEONS] Warning, could not delete " + outFile);
					result = false;
				}
			}
			instream = new BufferedReader(new InputStreamReader(getClass()
					.getResourceAsStream(BASE_LOCATION + name)));
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
	
	
	public void makeThemesDir() {
		File outDir = new File(outDirectory);
		if(!outDir.exists()) {
			outDir.mkdirs();
		} 		
		if(!outDir.exists()) {
			Logging.logInfo("Warning: Could not create " + outDir + ".");
		} else if (!outDir.isDirectory()) {
			Logging.logInfo("Warning: " + outDir 
					+ " is not a directory (folder); no themes loaded.");
		} //else ThemeReader.setThemesDir(outDir);
	}
	
	
	/**
	 * Iterate through the default themes and export them to their own files.
	 */
	public void makeThemes() {
		for(String name : THEMES) {
			Logging.logInfo("[DLDUNGEONS] Installing file " + outDirectory + name);
			copyOut(name);
		}
	}
	
	
	/**
	 * Iterate through the default block families and export them to their own files.
	 */
	public void makeBlocks() {
		for(String name : BLOCKS) {
			Logging.logInfo("[DLDUNGEONS] Installing file " + outDirectory + name);
			copyOut(name);
		}
	}
	
	
	/**
	 * Iterate through the default themes and export them to their own files, 
	 * over-writing any files with the same path.
	 */
	public void forceThemes() {
		for(String name : THEMES) {
			System.out.println("[DLDUNGEONS] Installing file " + outDirectory + name);
			forceOut(name);
		}
	}
	
	
	/**
	 * Iterate through the default themes and export them to their own files, 
	 * over-writing any files with the same path.
	 */
	public void forceBlocks() {
		for(String name : BLOCKS) {
			System.out.println("[DLDUNGEONS] Installing file " + outDirectory + name);
			forceOut(name);
		}
	}
	
	
	/**
	 * Export the chest.cfg file if it doesn't currently exist.
	 */
	public void makeChestCfg() {
		System.out.println("[DLDUNGEONS] Installing files "	+ outDirectory + "chests.cfg");
		copyOut("nbt.cfg");
		copyOut("chests.cfg");
		copyOut("oceanic_chests.cfg", "SpecialChests" + File.separator + "oceanic_chests.cfg");
	}
	
	
	/**
	 * Export the chest.cfg file, over-writing it if it already exists.
	 */
	public void forceChestCfg() {
		System.out.println("[DLDUNGEONS] Installing files "	+ outDirectory + "chests.cfg");
		forceOut("chests.cfg");
		forceOut("SpecialChests" + File.separator + "oceanic_chests.cfg");
	}
	
	
	/**
	 * Export the nbt.cfg file if it doesn't currently exist.
	 */
	public void makeNBTCfg() {
		System.out.println("[DLDUNGEONS] Installing files " + outDirectory + "nbt.cfg and " );
		copyOut("nbt.cfg");
	}
	
	
	/**
	 * Export the nbt.cfg file, over-writing it if it already exists.
	 */
	public void forceNBTCfg() {
		System.out.println("[DLDUNGEONS] Installing files "  + outDirectory + "nbt.cfg and " );
		forceOut("nbt.cfg");
	}
	

}
