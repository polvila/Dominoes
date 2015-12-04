import java.util.LinkedList;

public class Board {

	private int left;
	private int right;

	public Board(int left, int right){
		this.left = left;
		this.right = right;
	}

	public void play(Domino domino, int side){

		if(side == 0 || side == 1){
			if(domino.getSide1() == this.getLeft()){
				this.setLeft(domino.getSide2());
			}else if(domino.getSide2() == this.getLeft()){
				this.setLeft(domino.getSide1());
			}else if(domino.getSide1() == this.getRight()){
				this.setRight(domino.getSide2());
			}else if(domino.getSide2() == this.getRight()){
				this.setRight(domino.getSide1());
			}
		}else{
			if(domino.getSide1() == this.getRight()){
				this.setRight(domino.getSide2());
			}else if(domino.getSide2() == this.getRight()){
				this.setRight(domino.getSide1());
			}else if(domino.getSide1() == this.getLeft()){
				this.setLeft(domino.getSide2());
			}else if(domino.getSide2() == this.getLeft()){
				this.setLeft(domino.getSide1());
			}
		}


	}

	public boolean canIput(Domino fitxa){
		if(
				(fitxa.getSide1() != this.getLeft()) &&
				(fitxa.getSide1() != this.getRight()) &&
				(fitxa.getSide2() != this.getLeft()) &&
				(fitxa.getSide2() != this.getRight())	)
		{
			return false;
		}else{
			return true;
		}
	}

	public boolean canIputAny(LinkedList<Domino> dominoes){
		boolean bool = false;
		for(Domino f : dominoes){
			if(this.canIput(f)){
				bool = true;
			}
		}
		return bool;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int cap) {
		this.left = cap;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int cua) {
		this.right = cua;
	}

	public Board clone(){
		return new Board(getLeft(), getRight());
	}

	public String toString(){
		return "\t\t\t\t["+this.getLeft()+"-------"+this.getRight()+"]";
	}
}
