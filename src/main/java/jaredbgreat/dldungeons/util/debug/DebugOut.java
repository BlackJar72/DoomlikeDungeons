package jaredbgreat.dldungeons.util.debug;

public final class DebugOut {

	public static void bigSysout(String in) {
		System.out.println();
		System.out.println("\n**********************************\n\n"
						 + in 
						 + "\n\n**********************************\n");
		System.out.println();
	}

	public static void bigSysout(Object in) {
		System.out.println();
		System.out.println("\n**********************************\n\n"
						 + in 
						 + "\n\n**********************************\n");
		System.out.println();
	}
}
