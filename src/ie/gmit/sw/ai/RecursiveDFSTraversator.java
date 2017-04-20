package ie.gmit.sw.ai;

import ie.gmit.sw.ai.*;
public class RecursiveDFSTraversator implements Traversator{
	private Node[][] maze;
	private boolean keepRunning = true;
	private long time = System.currentTimeMillis();
	private int visitCount = 0;
	private Spiders goalNode;
	private NodeType nodeType;
	public RecursiveDFSTraversator(int limit, Spiders goalNode, NodeType nodeType){
		this.goalNode=goalNode;
		this.nodeType=nodeType;
	}
	
	public void traverse(Node[][] maze, Node node) throws InterruptedException {
		setParentsNull(maze);
		this.maze = maze;
		dfs(node);
	}
	
	private void dfs(Node node) throws InterruptedException{
		if (!keepRunning) return;
		
		node.setVisited(true);	
		visitCount++;
		
		if (node.isGoalNode()){
	        time = System.currentTimeMillis() - time; //Stop the clock
	        TraversatorStats.printStats(node, time, visitCount);
	        keepRunning = false;
			return;
		}
		
		try { //Simulate processing each expanded node
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Node[] children = node.children(maze);
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null && !children[i].isVisited()){
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
				dfs(children[i]);
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