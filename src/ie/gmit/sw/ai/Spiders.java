package ie.gmit.sw.ai;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.DepthLimitedDFSTraversator;

public class Spiders implements Runnable {
	private Node[][] maze;
	private int row;
	private int col;
	private Node GoalNode;
	private NodeType nodeType;
	public Spiders(Node[][] maze, Node GoalNode, NodeType nodeType) throws InterruptedException {
		this.maze = maze;
		this.GoalNode = GoalNode;
		this.nodeType= nodeType;
		setSpiders();
	}
	public void updatePath(int x, int y) throws InterruptedException {
		if (maze[this.row][this.col].getNodeType() != NodeType.PlayerNode) {
			maze[this.row][this.col].setNodeType(NodeType.WalkableNode);
			maze[x][y].setNodeType(nodeType);
			this.setRow(x);
			this.setCol(y);
		}
	}
	
	private void setSpiders() {
		int offSet = (int) ((int) maze.length * 0.20);
		Random ran = new Random();
		int row = ran.nextInt(this.maze.length - offSet) + offSet;
		int col = ran.nextInt(this.maze[0].length - offSet) + offSet;
		this.row = row;
		this.col = col;
		maze[row][col].setNodeType(nodeType);
	}

	
	

		public void setRow(int row) {
			this.row = row;
		}
		
		public void setCol(int col) {
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
		private void traverse() throws InterruptedException {
			//DepthLimitedDFSTraversator tr = new DepthLimitedDFSTraversator(200,this,nodeType);
			RecursiveDFSTraversator tr = new RecursiveDFSTraversator(200, this, nodeType);
			tr.traverse(maze, maze[this.getRow()][this.getCol()]);
		}
	public void run() {
		// TODO Auto-generated method stub
		try {
			traverse();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
