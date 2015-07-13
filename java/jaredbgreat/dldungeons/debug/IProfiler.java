package jaredbgreat.dldungeons.debug;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

public interface IProfiler {
	
	public abstract void startTask(String name);
	public abstract void endTask(String name);
	public abstract void infoOut(String info);

}
