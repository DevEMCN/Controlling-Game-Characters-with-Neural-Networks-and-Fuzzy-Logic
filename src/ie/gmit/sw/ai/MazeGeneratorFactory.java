package ie.gmit.sw.ai;

import ie.gmit.sw.ai.MazeGenerator.*;
public class MazeGeneratorFactory {
	private static MazeGeneratorFactory mgf = new MazeGeneratorFactory();
	
	private MazeGeneratorFactory(){		
	}
	
	public static MazeGeneratorFactory getInstance(){
		return mgf;
	}
	
	public MazeGenerator getMazeGenerator(MazeGenerator.GeneratorAlgorithm algorithm, int rows, int cols){
		if (algorithm == GeneratorAlgorithm.HuntAndKill){
			return new HuntAndKillMazeGenerator(rows, cols);			
		}
		return null;	
	}	
}