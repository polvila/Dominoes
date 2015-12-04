
public class ValueNode {

	private Node node;
	private float value;

	public ValueNode(float value, Node node){
		this.node = node;
		this.value = value;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
