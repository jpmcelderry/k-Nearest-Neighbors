
public class Node {

	public Node(int index, double distance) {
		this.index = index;
		this.distance = distance;
	}
	
	private double distance;
	private int index;
	private Node next;
	private Node prev;
	
	//Setters and Getters
	public void setNext(Node next) {
		this.next = next;
	}
	
	public Node getNext() {
		return this.next;
	}
	
	public void setPrev(Node prev) {
		this.prev = prev;
	}
	
	public Node getPrev() {
		return this.prev;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public double getDistance() {
		return this.distance;
	}
}
