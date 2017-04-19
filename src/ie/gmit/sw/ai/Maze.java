package ie.gmit.sw.ai;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;
public class Maze {
	private int dimension = 100;
	private Node[][] maze = new Node[dimension][dimension];
	private Node player;
	private Node goal;
	private int col, row ;
	ExecutorService ex = Executors.newCachedThreadPool();
	

	public Maze(int dimension) throws InterruptedException{
		maze = new Node[dimension][dimension];
		init();
		buildMaze();
		setGoal();
		buildPath();
		
		
		int featureNumber = 20;
		addFeature(NodeType.SwordNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.HelpNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.BombNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.HydrogenBombNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.BlackSpider, NodeType.WallNode, featureNumber); //6 is a Black Spider, 0 is a hedge
		addFeature(NodeType.BlueSpider, NodeType.WallNode, featureNumber); //7 is a Blue Spider, 0 is a hedge
		addFeature(NodeType.BrownSpider, NodeType.WallNode, featureNumber); //8 is a Brown Spider, 0 is a hedge
		addFeature(NodeType.GreenSpider, NodeType.WallNode, featureNumber); //9 is a Green Spider, 0 is a hedge
		addFeature(NodeType.GreySpider, NodeType.WallNode, featureNumber); //: is a Grey Spider, 0 is a hedge
		addFeature(NodeType.OrangeSpider, NodeType.WallNode, featureNumber); //; is a Orange Spider, 0 is a hedge
		addFeature(NodeType.RedSpider, NodeType.WallNode, featureNumber); //< is a Red Spider, 0 is a hedge
		addFeature(NodeType.YellowSpider, NodeType.WallNode, featureNumber); //= is a Yellow Spider, 0 is a hedge
		
		
	}
	private void buildPath() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				if (col < maze[row].length - 1) {
					if (maze[row][col + 1].getNodeType() == NodeType.WalkableNode) {
						maze[row][col].addPath(Node.Direction.West);
					}
				}
				if (col > 0) {
					if (maze[row][col - 1].getNodeType() == NodeType.WalkableNode) {
						maze[row][col].addPath(Node.Direction.East);
					}
				}
				if (row < maze.length - 1) {
					if (maze[row + 1][col].getNodeType() == NodeType.WalkableNode) {
						maze[row][col].addPath(Node.Direction.North);
					}
				}
				if (row > 0) {
					if (maze[row - 1][col].getNodeType() == NodeType.WalkableNode) {
						maze[row][col].addPath(Node.Direction.South);
					}
				}
			}
		}

	}
	
	private void init(){
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				maze[row][col] = new Node(row, col);
				maze[row][col].setNodeType(NodeType.WallNode);
			}
		}
	}
	
	private void addFeature(NodeType feature, NodeType replace, int number) throws InterruptedException{
		int counter = 0;
		while (counter < number){
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			if (maze[row][col].nodeType == replace){
				maze[row][col].nodeType = feature;
				if(feature==NodeType.BlackSpider || feature==NodeType.BlueSpider || feature==NodeType.BrownSpider
						|| feature==NodeType.OrangeSpider || feature==NodeType.GreySpider || feature==NodeType.GreenSpider
						|| feature==NodeType.RedSpider || feature==NodeType.YellowSpider){
				ex.execute(new Spiders(maze, player,feature));
				}
				counter++;
			}
		}
	}
	
	private void buildMaze(){ 
		for (int row = 1; row < maze.length - 1; row++){
			for (int col = 1; col < maze[row].length - 1; col++){
				int num = (int) (Math.random() * 10);
				if (num > 5 && col + 1 < maze[row].length - 1){
					maze[row][col + 1].nodeType = NodeType.WalkableNode;
					continue;
				}else{
					maze[row + 1][col].nodeType = NodeType.WalkableNode;
				}
			}
		}		
	}
	
	public Node[][] getMaze(){
		return this.maze;
	}
	
	public int size(){
		return this.maze.length;
	}
	public Node getPlayer() {
		return player;
	}
	
	
	public void setPlayer() {
		int currentRow = (int) ((dimension-20) * Math.random());
		int currentCol = (int) ((dimension-20) * Math.random());
		
		while(maze[currentRow][currentCol].getNodeType()!=NodeType.WalkableNode){
			 currentRow = (int) ((dimension-20) * Math.random());
			 currentCol = (int) ((dimension-20) * Math.random());
		}
		maze[currentRow][currentCol].setNodeType(NodeType.PlayerNode);
		player = maze[currentRow][currentCol];
	}
	public void setGoal() {
		Random ran = new Random();
		for (int i = 0; i < 1; i++) {
			int currentRow = ran.nextInt(dimension-10) + 10;
			int currentCol = ran.nextInt(dimension-10) + 10;
			while(maze[currentRow][currentCol].getNodeType()!=NodeType.WalkableNode || 
					maze[currentRow][currentCol].getNodeType()==NodeType.PlayerNode){
				 currentRow = ran.nextInt(dimension-10) + 10;
				 currentCol = ran.nextInt(dimension-10) + 10;
			}
			this.getMaze()[currentRow][currentCol].setNodeType(NodeType.GoalNode);
			goal = this.getMaze()[currentRow][currentCol];
		}
	}

	public Node getGoal() {
		return goal;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1) sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}