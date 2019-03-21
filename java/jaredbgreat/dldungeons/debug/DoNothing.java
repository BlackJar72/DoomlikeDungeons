package jaredbgreat.dldungeons.debug;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		

/**
 * The profiler for when profiling is off.
 * 
 * @author Jared Blackburn
 */
public class DoNothing implements IProfiler {

	@Override
	public void startTask(String name) {/*Do Nothing!*/}

	@Override
	public void endTask(String name) {/*Do Nothing!*/}

	@Override
	public void infoOut(String info) {/*Do Nothing!*/}

}
