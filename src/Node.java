import java.util.LinkedList;

public class Node {
	private LinkedList<Domino> player = new LinkedList<Domino>();
	private LinkedList<Domino> computer = new LinkedList<Domino>();

	private Board board;

	private int side;

	public Node(LinkedList<Domino> player, LinkedList<Domino> computer, Board board){
		this.player = player;
		this.computer = computer;
		this.board = board;
		side = 0;
	}

	public LinkedList<Domino> getPlayer() {
		return player;
	}

	public void setPlayer(LinkedList<Domino> jugador) {
		this.player = jugador;
	}

	public LinkedList<Domino> getComputer() {
		return computer;
	}

	public void setComputer(LinkedList<Domino> ordinador) {
		this.computer = ordinador;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board fila) {
		this.board = fila;
	}

	public String toString(){
		return board.toString();
	}

	public Node clone(){
		return new Node(new LinkedList<Domino>(getPlayer()), new LinkedList<Domino>(getComputer()), getBoard().clone());
	}

	public boolean isFinal(){
		if(this.player.size()==0 || this.computer.size() == 0 || 
				!(this.board.canIputAny(this.player) || this.board.canIputAny(this.computer))){
			return true;
		}else{
			return false;
		}
	}

	public int getResult(){
		int i = 0;

		if(this.player.size() == 0){
			i = -1;		//-INFINITY
		}else if(this.computer.size() == 0){
			i = 1000;		//INFINITY
		}else{
			int computerPoints = 0;
			int playerPoints = 0;
			for(Domino fo : computer){
				computerPoints += fo.getPoints();
			}
			for(Domino fj : player){
				playerPoints += fj.getPoints();
			}

			if(playerPoints > computerPoints){
				i = 1000;
			}else if(playerPoints < computerPoints){
				i = -1;
			}
		}
		return i;
	}

	public LinkedList<Node> getChildren(int level){
		LinkedList<Node> children = new LinkedList<Node>();

		boolean cantPlay = true;
		if(!isFinal()){
			if(level%2==1){	//computer turn
				for(Domino d : this.computer){
					if(board.canIput(d)){
						Board brd = board.clone();

						LinkedList<Domino> newComputer = new LinkedList<Domino>(computer);
						if(		(board.getLeft() == board.getRight()) || 
								((board.getLeft() == d.getSide1() && board.getRight() == d.getSide2()) ||
										(board.getLeft() == d.getSide2() && board.getRight() == d.getSide1())))
						{
							Board brd2 = brd.clone();
							brd.play(d, 1);
							brd2.play(d, 2);
							newComputer.remove(d);
							Node n = new Node(new LinkedList<Domino>(player), newComputer, brd2);
							n.setSide(2);
							children.add(n);
						}else{
							brd.play(d, 0);
							newComputer.remove(d);
						}
						Node n = new Node(new LinkedList<Domino>(player), newComputer, brd);
						n.setSide(1);
						children.add(n);
						cantPlay = false;
					}
				}
				if(cantPlay) children.add(this.clone());
			}else{		//player turn
				for(Domino d : this.player){
					if(board.canIput(d)){
						Board brd = board.clone();
						LinkedList<Domino> newPlayer = new LinkedList<Domino>(player);
						if(		(board.getLeft() == board.getRight()) || 
								((board.getLeft() == d.getSide1() && board.getRight() == d.getSide2()) ||
										(board.getLeft() == d.getSide2() && board.getRight() == d.getSide1())))
						{
							Board brd2 = brd.clone();
							brd.play(d, 1);
							brd2.play(d, 2);
							newPlayer.remove(d);
							children.add(new Node(newPlayer,new LinkedList<Domino>(computer), brd2));
						}else{
							brd.play(d,0);
							newPlayer.remove(d);
						}
						children.add(new Node(newPlayer,new LinkedList<Domino>(computer), brd));
						cantPlay = false;
					}
				}
				if(cantPlay) children.add(this.clone());
			}
		}
		return children;

	}

	public int getSide() {
		return side;
	}

	public void setSide(int costat) {
		this.side = costat;
	}
}
