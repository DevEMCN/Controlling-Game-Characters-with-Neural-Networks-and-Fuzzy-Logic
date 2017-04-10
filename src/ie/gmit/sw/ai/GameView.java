package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;
public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private Sprite[] sprites;
	private int enemy_state = 5;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	
	public GameView(Node[][] maze) throws Exception{
		this.maze = maze;
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
              
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		NodeType nodeType = maze[row][col].getNodeType();
       		
        		if (zoomOut){
        			if (nodeType==NodeType.PlayerNode){
	        			if (row == currentRow && col == currentCol){
	        				g2.setColor(Color.YELLOW);
	        			}else{
	        		//		g2.setColor(reds[(int) (Math.random() * 3)]);
	        			}
        				g2.fillRect(x1, y1, size, size);
        			}
        		}else{
        			nodeType = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
        		}
        		
        		
  
        		if (nodeType == NodeType.WallNode) { // x = wallnode
					imageIndex = 0;
				} else if (nodeType == NodeType.SwordNode) {
					imageIndex = 1;
				} else if (nodeType == NodeType.BombNode) {
					imageIndex = 3;
				} else if (nodeType == NodeType.HydrogenBombNode) {

					imageIndex = 4;
				} else if (nodeType == NodeType.PlayerNode) {
					imageIndex = 5;
				}else {
					imageIndex = -1;
				}
        		if (imageIndex >= 0) {
					g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);

				} else {

					g2.setColor(Color.LIGHT_GRAY);
					g2.fillRect(x1, y1, size, size);
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