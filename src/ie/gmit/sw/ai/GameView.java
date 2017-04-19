package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;
import ie.gmit.sw.ai.Node.Direction;
public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private Sprite[] sprites;
	private int enemy_state = 5;
	private Node player;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	ExecutorService es = Executors.newCachedThreadPool();
	public GameView(Node[][] maze, Node player) throws Exception{
		this.maze = maze;
		this.player=player;
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze.length - 1) - cellpadding){
			currentCol = (maze.length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
           int counter = 0;   
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size; 
        		NodeType nodeType = maze[row][col].getNodeType(); 
        		
        		

        		if (zoomOut){
        			switch (nodeType) {
    				case PlayerNode:
    					g2.setColor(Color.BLACK);
						g2.fillRect(x1, y1, size, size);
						g2.drawString("You're Here", x1, y1);
    					break;
    				case BlackSpider:
    					g2.setColor(Color.DARK_GRAY);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case BlueSpider:
    					g2.setColor(Color.BLUE);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case BrownSpider:
    					g2.setColor(Color.CYAN);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case GreenSpider:
    					g2.setColor(Color.GREEN);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case GreySpider:
    					g2.setColor(Color.GRAY);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case OrangeSpider:
    					g2.setColor(Color.ORANGE);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case RedSpider:
    					g2.setColor(Color.RED);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case YellowSpider:
    					g2.setColor(Color.YELLOW);
						g2.fillRect(x1, y1, size, size);
    					break;
    				case GoalNode:
    					g2.setColor(Color.BLACK);
						g2.fillRect(x1, y1, size, size);
						g2.drawString("EXIT", x1, y1);
    					break;
    				default:
    					break;
    				}
        		}else{
        			nodeType = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
        			
        		}
        		
        		
        		switch (nodeType) {
				case WallNode:
					imageIndex = 0;
					break;
				case SwordNode:
					imageIndex = 1;
					break;
				case BombNode:
					imageIndex = 3;
					break;
				case HydrogenBombNode:
					imageIndex = 4;
					break;
				case PlayerNode:
					imageIndex = 5;
					break;
				case BlackSpider:
					imageIndex = 6;
					
					break;
				case BlueSpider:
					imageIndex = 7;
					break;
				case BrownSpider:
					imageIndex = 8;
					break;
				case GreenSpider:
					imageIndex = 9;
					break;
				case GreySpider:
					imageIndex = 10;
					break;
				case OrangeSpider:
					imageIndex =11;
					break;
				case RedSpider:
					imageIndex = 12;
					break;
				case YellowSpider:
					imageIndex = 13;
					break;
				case GoalNode:
					imageIndex = 14;
					break;
				default:
					imageIndex = -1;
					break;
				}
        		if (imageIndex >= 0) {
					g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);

				} else {
					if (maze[row][col].isVisited() && !maze[row][col].isGoalNode()) {
						g2.setColor(maze[row][col].getColor());
						g2.fillRect(x1, y1, size, size);
					}
					else{
						g2.setColor(Color.LIGHT_GRAY);
						g2.fillRect(x1, y1, size, size);
					}
				}
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		this.repaint();
	}
	
	public void setSprites(Sprite[] sprites){
		this.sprites = sprites;
	}
}