
public class Domino {

	private int side1;
	private int side2;

	public Domino(int side1, int side2){

		this.side1 = side1;
		this.side2 = side2;

	}

	public int getSide1() {
		return side1;
	}

	public void setSide1(int costat1) {
		this.side1 = costat1;
	}

	public int getSide2() {
		return side2;
	}

	public void setSide2(int costat2) {
		this.side2 = costat2;
	}

	public int getPoints(){
		return this.side1 + this.side2;
	}

	public String toString(){
		return "["+side1+"·"+side2+"]";
	}

	@Override
	public boolean equals(Object o){

		return ((this.getSide1() == ((Domino) o).getSide2()) && (this.getSide2() == ((Domino) o).getSide1()) || 
				(this.getSide1() == ((Domino) o).getSide1()) && (this.getSide2() == ((Domino) o).getSide2()));
	}

	public Domino clone(){
		return new Domino(getSide1(), getSide2());
	}

}
