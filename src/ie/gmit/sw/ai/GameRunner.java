package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.lang.management.MemoryType;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.fuzzylogic;
import javax.swing.*;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private Node[][] maze;
	private GameView view;
	private int currentRow;
	private int currentCol;
	private double Weaponstrength;
	private double playerStrenght=100;
	fuzzylogic fuzz = new fuzzylogic();
	Node goal;
	Maze m = null;
	private Node player;
	private static final int IMAGE_COUNT = 15;
	Traversator playerPath;
	JPanel panel;
	JLabel lblPHealth,lblWHealth;
	NumberFormat formatter = new DecimalFormat("#0.00");     

	
	public GameRunner() throws Exception{
    	m = new Maze(MAZE_DIMENSION);
		maze = m.getMaze();
		view = new GameView(maze,player);
		Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	view.setBounds(0, 0, 800, 800);

    	

    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(null);
		f.getContentPane().add(view);
		f.setSize(1000, 1000);
        f.setLocation(100,100);
        f.setVisible(true);
        placePlayer();
        Scores(f);
        endNode();
    	validatePath();
	}
	private void Scores(JFrame f) {
		panel = new JPanel();
		panel.setForeground(Color.GRAY);
		panel.setBounds(650, 50, 491, 638);
		f.getContentPane().add(panel);
		panel.setLayout(null);
		lblPHealth = new JLabel("Players Health: "+ Double.toString(playerStrenght));
		lblPHealth.setBounds(160, 10, 491, 14);
		lblWHealth = new JLabel("Weapon Strenght: "+ Double.toString(Weaponstrength));
		lblWHealth.setBounds(160, 50, 491, 14);
		panel.add(lblPHealth);
		panel.add(lblWHealth);
		
		panel.repaint();
	
	}
	private void placePlayer(){
		m.setPlayer();
		player = m.getPlayer();
		currentRow = player.getRow();
		currentCol = player.getCol();
    	maze[currentRow][currentCol].setNodeType(NodeType.PlayerNode);
    	updateView();
    		
	}
	private void constantPathUpdate() {
		goal = m.getGoal();
		AStarTraversator update = new AStarTraversator(goal);
		update.traverse(maze, maze[currentRow][currentCol]);

	}
	private void endNode(){   	
		this.goal = m.getGoal();
		this.goal.setGoalNode(true);
	}
	
	private void validatePath() throws InterruptedException {
		int checker = (int) TraversatorStats.depth;
		if (checker == 0) {
			goal.setNodeType(NodeType.WalkableNode);
		}
		while (checker < 20) {
			m.setGoal();
			goal.setNodeType(NodeType.WalkableNode);
			goal = m.getGoal();
			System.out.println("recalculating the path...");
			Traversator playerPath = new AStarTraversator(goal);
			playerPath.traverse(maze, maze[player.getRow()][player.getCol()]);
			System.out.println("finished calculating...");
			checker = (int) TraversatorStats.depth;
			if (checker > 20) {
				break;
			}
		}
	}
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
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
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BombNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength+=3;
			lblWHealth.setText("Weapon Strenght: "+formatter.format(Weaponstrength));
			return false;
		} else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.HelpNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			fuzz.NewPlayerHealth+=25;
			lblPHealth.setText("Player Strenght: "+formatter.format(fuzz.NewPlayerHealth));
			return false;
		} else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.HydrogenBombNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength+=5;
			lblWHealth.setText("Weapon Strenght: "+formatter.format(Weaponstrength));
			return false;
		}  
		else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BlackSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);	
			fuzz.fight(Weaponstrength,playerStrenght,30);
			getScore();
			constantPathUpdate();
			return true;
		}
		else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BlueSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,27);
			constantPathUpdate();
			getScore();
			return true;
		}
		else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.BrownSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,25);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.GreenSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,22);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.GreySpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,18);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.OrangeSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,15);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.RedSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,12);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.YellowSpider){
			maze[currentRow][currentCol].setNodeType(NodeType.WalkableNode);
			fuzz.fight(Weaponstrength,playerStrenght,8);
			getScore();
			constantPathUpdate();
			return true;
		}else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.SwordNode){
			maze[row][col].setNodeType(NodeType.WallNode);
			Weaponstrength++;
			lblWHealth.setText("Weapon Strenght: "+formatter.format(Weaponstrength));			
			constantPathUpdate();
			return false;
		}  else if (row <= maze.length - 1 && col <= maze[row].length - 1 && (maze[row][col].getNodeType() == NodeType.WalkableNode)|| maze[row][col].getNodeType() == NodeType.GoalNode){
			System.exit(0);
			return true;
		}else {
			return false; 
		}
	}
	private void getScore(){
		if(Weaponstrength>0){
		Weaponstrength=Weaponstrength-fuzz.weaponDamage;
		lblPHealth.setText("Player Strenght: "+formatter.format(fuzz.NewPlayerHealth));
		lblWHealth.setText("Weapon Strenght: "+formatter.format(Weaponstrength));	
		}
		else{
			Weaponstrength=0;
			lblPHealth.setText("Player Strenght: "+formatter.format(fuzz.NewPlayerHealth));
			lblWHealth.setText("Weapon Strenght: "+formatter.format(Weaponstrength));	
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