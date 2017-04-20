package ie.gmit.sw.ai;


import java.util.*;
public class AStarTraversator implements Traversator {
	private Node goal;
	public boolean foundGoal = false;
	public Node[][] maze;
	public Node startNode;
	public NodeType targetNode;
	
	public AStarTraversator(Node goal) {
		this.goal = goal;
		System.out.println(goal);
	}

	public void traverse(Node[][] maze, Node node) {
		reInit(maze);
	
		long time = System.currentTimeMillis();
		int visitCount = 0;

		PriorityQueue<Node> open = new PriorityQueue<Node>(20,
				(Node current, Node next) -> (current.getPathCost() + current.getHeuristic(goal))
						- (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Node> closed = new ArrayList<Node>();

		open.offer(node);
		node.setPathCost(0);
		while (!open.isEmpty()) {
			node = open.poll();
			closed.add(node);
			node.setVisited(true);
			visitCount++;

			if (node.getNodeType() == NodeType.GoalNode) {
				time = System.currentTimeMillis() - time; // Stop the clock
				foundGoal = true;
				System.out.println("got it");
				TraversatorStats.printStats(node, time, visitCount);
				break;
			}
			// Process adjacent nodes
			Node[] children = node.children(maze);
			for (int i = 0; i < children.length; i++) {
				Node child = children[i];
				int score = node.getPathCost() + 1 + child.getHeuristic(goal);
				int existing = child.getPathCost() + child.getHeuristic(goal);

				if ((open.contains(child) || closed.contains(child)) && existing < score) {
					continue;
				} else {
					open.remove(child);
					closed.remove(child);
					child.setParent(node);
					child.setPathCost(node.getPathCost() + 1);
					open.add(child);
				}
			}
		}

	}
	private void reInit(Node[][] maze) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j].setVisited(false);
				maze[i][j].setParent(null);
			}
		}
	}
}

