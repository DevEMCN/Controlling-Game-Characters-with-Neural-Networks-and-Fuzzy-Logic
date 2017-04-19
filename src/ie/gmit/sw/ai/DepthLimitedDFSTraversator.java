package ie.gmit.sw.ai;
public class DepthLimitedDFSTraversator implements Traversator{
	private Node[][] maze;
	private int limit;
	private boolean keepRunning = true;
	private long time = System.currentTimeMillis();
	private int visitCount = 0;
	private Spiders goalNode;
	private NodeType nodeType;
	
	public DepthLimitedDFSTraversator(int limit, Spiders goalNode, NodeType nodeType){
		this.limit = limit;
		this.goalNode=goalNode;
		this.nodeType=nodeType;
	}
	
	public void traverse(Node[][] maze, Node node) throws InterruptedException {
		this.maze = maze;
		setParentsNull(maze);
		dfs(node, 1);
	}
	
	private void dfs(Node node, int depth) throws InterruptedException{
		if (!keepRunning || depth > limit) return;
		
		node.setVisited(true);	
		visitCount++;
		
		if (node.getNodeType() == NodeType.PlayerNode){
	        time = System.currentTimeMillis() - time; //Stop the clock
	        TraversatorStats.printStats(node, time, visitCount);
	        keepRunning = false;
			return;
		}
		
		Node[] children = node.children(maze);
		for (int i = 0; i < children.length; i++) {
if (children[i] != null && !children[i].isVisited()) {
				
				if (node.getNodeType() == NodeType.WalkableNode|| node.getNodeType() == NodeType.PlayerNode) {
					goalNode.updatePath(node.getRow(), node.getCol());
					maze[node.getRow()][node.getCol()].setNodeType(nodeType);
					Thread.sleep(3000);
				}else if (node.getRow() <= maze.length - 1 && node.getCol() <= maze[node.getRow()].length - 1
						&& (maze[node.getRow()][node.getCol()].getNodeType() == NodeType.PlayerNode)) {
					goalNode.updatePath(node.getRow() - 1, node.getCol() - 1);
					maze[node.getRow() - 1][node.getCol() - 1].setNodeType(nodeType);
				}
				children[i].setParent(node);
				dfs(children[i], depth + 1);
			}
		}
	}
	private void setParentsNull(Node[][] maze) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j].setVisited(false);
				maze[i][j].setParent(null);
			}
		}
	}
}