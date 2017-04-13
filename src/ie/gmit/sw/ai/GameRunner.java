package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import ie.gmit.sw.ai.fuzzylogic;

import javax.swing.*;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private Node[][] maze;
	private GameView view;
	private int currentRow;
	private int currentCol;
	private int currentRowGoal;
	private int currentColGoal;
	private double Weaponstrength;
	private double playerStrenght=100;
	private double spiderStrenght=25;
	fuzzylogic fuzz = new fuzzylogic();
	Node goal;
	Maze m = null;
	private Node player;
	private static final int IMAGE_COUNT = 15;
	
	
	public GameRunner() throws Exception{
    	m = new Maze(MAZE_DIMENSION);
		maze = m.getMaze();
		view = new GameView(maze);
		Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	

    	

    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
        placePlayer();
    	endNode();
	}
	
	private void placePlayer(){
		m.setPlayer();
		player = m.getPlayer();
		currentRow = player.getRow();
		currentCol = player.getCol();
    	maze[currentRow][currentCol].setNodeType(NodeType.PlayerNode);
    	updateView();
    		
	}
	private void endNode(){   	
		m.setGoal();
		goal = m.getGoal();
		currentRowGoal = goal.getRow();
		currentColGoal = goal.getCol();
    	maze[currentRowGoal][currentColGoal].setNodeType(NodeType.GoalNode);
    	System.out.println(goal);
	}
	
	
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
		System.out.println("weapon Strength"+Weaponstrength);
	}
	

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;        	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore
	
private boolean isValidMove(int row, int col) {
		

		if (row <= maze.length - 1 && col <= maze[row].length - 1 && maze[row][col].getNodeType() == NodeType.WalkableNode){
			maze[row][col].setNodeType(NodeType.PlayerNode);
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);

			return true;
		}
		else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BombNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength+=3;
			return false;
		} else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.HydrogenBombNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength+=5;
			return false;
		}  
		else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BlackSpider){
			maze[row][col].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,spiderStrenght);
			return false;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.SwordNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength++;
			return false;
		}  else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.GoalNode){
			System.exit(0);
			return true;
		}else {
			return false; 
		}
	}
	private Sprite[] getSprites() throws Exception{	
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", "resources/hedge.png");
		sprites[1] = new Sprite("Sword", "resources/sword.png");
		sprites[2] = new Sprite("Help", "resources/help.png");
		sprites[3] = new Sprite("Bomb", "resources/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", "resources/h_bomb.png");
		sprites[5] = new Sprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		sprites[6] = new Sprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		sprites[14] = new Sprite("End Game", "resources/EndGame.png");
		return sprites;
	}
	
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}