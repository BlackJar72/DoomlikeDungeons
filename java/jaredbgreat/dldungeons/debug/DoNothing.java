package jaredbgreat.dldungeons.debug;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

public class DoNothing implements IProfiler {

	@Override
	public void startTask(String name) {/*Do Nothing!*/}

	@Override
	public void endTask(String name) {/*Do Nothing!*/}

	@Override
	public void infoOut(String info) {/*Do Nothing!*/}

}
