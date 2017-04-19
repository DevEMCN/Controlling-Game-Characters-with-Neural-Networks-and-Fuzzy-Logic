package ie.gmit.sw.ai.sprites;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.Node;

public class Spider extends Node {
	
	//private Object lock = null;
	private Node[][] maze = null;
	private int row;
	private int col;
	//private Random rand = new Random();
	//private long speed = 1000;
	private ExecutorService executor = Executors.newFixedThreadPool(1);
	//private volatile int moveNum = 0;
	
	private void setCol(int col){
		this.col = col;
	}
	private void setRow(int row){
		this.row = row;
	}
	public void updatePath(int x, int y) throws InterruptedException {
		if (maze[this.row][this.col].getTag() != 5) {
			maze[this.row][this.col].setTag(-1);
			maze[x][y].setTag(6);
			this.setRow(x);
			this.setCol(y);
		}
	}
	
	public Spider(int row, int col, int feature) {
		super(row, col, feature);
	}

}
