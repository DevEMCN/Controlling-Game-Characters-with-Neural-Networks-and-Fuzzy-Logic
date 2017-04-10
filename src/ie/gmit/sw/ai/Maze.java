package ie.gmit.sw.ai;
import ie.gmit.sw.ai.Node;


public class Maze {
	private int dimension = 100;
	private Node[][] maze = new Node[dimension][dimension];
	private Node player;
	
	public Maze(int dimension){
		maze = new Node[dimension][dimension];
		init();
		buildMaze();
		
		int featureNumber = (int)((dimension * dimension) * 0.01);
		addFeature(NodeType.SwordNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.HelpNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.BombNode, NodeType.WallNode, featureNumber);
		addFeature(NodeType.HydrogenBombNode, NodeType.WallNode, featureNumber);
		

		

		featureNumber = (int)((dimension * dimension) * 0.01);
	//	addFeature('\u0036', '0', featureNumber); //6 is a Black Spider, 0 is a hedge
	//	addFeature('\u0037', '0', featureNumber); //7 is a Blue Spider, 0 is a hedge
	//	addFeature('\u0038', '0', featureNumber); //8 is a Brown Spider, 0 is a hedge
	//	addFeature('\u0039', '0', featureNumber); //9 is a Green Spider, 0 is a hedge
	//	addFeature('\u003A', '0', featureNumber); //: is a Grey Spider, 0 is a hedge
	//	addFeature('\u003B', '0', featureNumber); //; is a Orange Spider, 0 is a hedge
	//	addFeature('\u003C', '0', featureNumber); //< is a Red Spider, 0 is a hedge
	//	addFeature('\u003D', '0', featureNumber); //= is a Yellow Spider, 0 is a hedge
		
		
	}
	
	private void init(){
		for (int row = 0; row < maze.length; row++){
			for (int col = 0; col < maze[row].length; col++){
				maze[row][col] = new Node(row, col);
				maze[row][col].setNodeType(NodeType.WallNode);
			}
		}
	}
	
	private void addFeature(NodeType feature, NodeType replace, int number){
		int counter = 0;
		while (counter < number){
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			if (maze[row][col].nodeType == replace){
				maze[row][col].nodeType = feature;
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