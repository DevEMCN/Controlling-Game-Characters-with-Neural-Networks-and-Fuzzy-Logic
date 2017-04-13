package ie.gmit.sw.ai;

public interface MazeGenerator {
	public enum GeneratorAlgorithm {HuntAndKill};
	
	public   void setGoal();
	public   Node getGoal();
	public   void setPlayer();
	public   Node getPlayer();
	public   Node[][] getMaze();
	public   void generateMaze();
}