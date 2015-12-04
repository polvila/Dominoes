import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static final int MAX_LEVEL_M = 10;
	public static final int NIVELL_MAXIM_P = 18;

	public static void main(String[] args) {
		LinkedList<Domino> tiles = new LinkedList<Domino>();

		LinkedList<Domino> player = new LinkedList<Domino>();
		LinkedList<Domino> computer = new LinkedList<Domino>();

		Scanner sc = new Scanner(System.in);

		Board board = new Board(-1,-1);

		long startTime;
		long endTime;

		long time = 0;

		int times = 0;

		boolean playerTurn = false;
		boolean noFinal = true;
		boolean dontPlayed = true;
		boolean computerDontPlay = false;
		boolean playerDontPlay = false;

		for(int i=0; i<7; i++){
			for(int j=0; j<7; j++){
				Domino domino = new Domino(i,j);
				if(!tiles.contains(domino)){
					tiles.add(domino);
				}
			}
		}
		
		System.out.println("Which algorithm you want to use? \n1.Minimax	\n2.Alpha–beta pruning \nPut a valid value:");
		int a = sc.nextInt();

		System.out.println("Which heuristic you want to use (1,2,3)?");
		int h = sc.nextInt();

		Collections.shuffle(tiles);

		for(Domino f : tiles){
			if(playerTurn){
				player.add(f.clone());
				playerTurn = false;
			}else{
				computer.add(f.clone());
				playerTurn = true;
			}
		}

		System.out.print("Plyr: ");
		for(Domino d : player){
			System.out.print(d+" ");
		}
		System.out.println();
		System.out.print("Cptr: ");
		for(Domino d : computer){
			System.out.print(d+" ");
		}
		System.out.println();

		if(player.contains(new Domino(6,6))){
			player.remove(new Domino(6,6));
			playerTurn = false;
			System.out.println("Player put the tile [6·6]");
		}else{
			computer.remove(new Domino(6,6));
			playerTurn = true;
			System.out.println("Computer put the tile [6·6]");
		}
		board.setLeft(6);
		board.setRight(6);

		while(noFinal){

			System.out.println(board);

			System.out.print("Plyr: ");
			for(Domino d : player){
				System.out.print(d+" ");
			}
			System.out.println();
			System.out.print("Cptr: ");
			for(Domino d : computer){
				System.out.print(d+" ");
			}
			System.out.println();

			if(playerTurn){
				System.out.println("Player turn.");
				if(board.canIputAny(player)){
					do{
						System.out.println("Enter the value of the tile that you want to play: (Ex:[4·3] -> 43)");
						int numTile = sc.nextInt();
						Domino domino = new Domino(numTile/10,numTile%10);
						if(board.canIput(domino) && player.contains(domino)){
							int side = 0;
							if(		(board.getLeft() == board.getRight()) || 
									((board.getLeft() == domino.getSide1() && board.getRight() == domino.getSide2()) ||
											(board.getLeft() == domino.getSide2() && board.getRight() == domino.getSide1())))
							{
								System.out.println("Which side you want to put? (1 | 2)");
								side = sc.nextInt();
							}
							board.play(domino, side);
							player.remove(domino.clone());
							dontPlayed = false;
						}
					}while(dontPlayed);
					dontPlayed = true;
					playerDontPlay = false;
				}else{
					System.out.println("You can not put any tile, you lose the turn.");
					playerDontPlay = true;
				}
				playerTurn = false;
			}else{	//Computer turn
				System.out.println("Computer turn.");
				Node father = new Node(new LinkedList<Domino>(player), new LinkedList<Domino>(computer), board.clone());
				if(board.canIputAny(computer)){
					startTime = System.nanoTime();
					ValueNode vn;
					if(a == 1){
						vn = minimax(father,1,h);
					}else{
						vn = alfaBeta(father,1,h,-1.0f, 1000.0f);
					}
					endTime = System.nanoTime();
					time += (endTime - startTime);
					times++;
					Domino fitxa = getTile(father,vn.getNode());
					System.out.println("Computer has played "+fitxa+"");
					board.play(fitxa,vn.getNode().getSide());
					computer.remove(fitxa.clone());
					computerDontPlay = false;
				}else{
					System.out.println("The computer can not play.");
					computerDontPlay = true;
				}
				playerTurn = true;
			}

			if(player.size() == 0){
				noFinal = false;
				System.out.println("You Win!");
				System.out.println(time/times);
			}else if(computer.size() == 0){
				noFinal = false;
				System.out.println("You Lose!");
				System.out.println(time/times);
			}else if(playerDontPlay && computerDontPlay){
				noFinal = false;
				int computerPoints = 0;
				int playerPoints = 0;
				for(Domino cd : computer){
					computerPoints += cd.getPoints();
				}
				for(Domino pd : player){
					playerPoints += pd.getPoints();
				}

				if(playerPoints > computerPoints){
					System.out.println("You Lose!");
				}else if(playerPoints < computerPoints){
					System.out.println("You Win!");
				}else{
					System.out.println("Tie!");
				}
				System.out.println(time/times);
			}
		}
		sc.close();
	}

	public static ValueNode minimax(Node node, int level, int h){

		if(node.isFinal()) {
			return new ValueNode((float) node.getResult(), node.clone());
		}
		else if(level == MAX_LEVEL_M) {
			ValueNode vn = null;
			switch(h){
			case 1:
				vn = new ValueNode(h1(node), node.clone());
				break;
			case 2:
				vn = new ValueNode(h2(node), node.clone());
				break;
			case 3:
				vn = new ValueNode(h3(node), node.clone());
				break;
			default:
				vn = new ValueNode(h3(node), node.clone());
				break;
			}
			return vn;
		}
		else{
			LinkedList<Node> children = node.getChildren(level);
			Node nodeToReturn = children.get(0);
			float valueToReturn;
			if(level%2==1) valueToReturn = -1.0f;
			else valueToReturn = 1000.0f;
			for(Node child : children){
				ValueNode vn = minimax(child.clone(), level+1, h);
				if(level%2==1){
					if(vn.getValue() > valueToReturn){
						valueToReturn = vn.getValue();
						nodeToReturn = child.clone();
					}
				}else{
					if(vn.getValue() < valueToReturn){
						valueToReturn = vn.getValue();
						nodeToReturn = child.clone();
					}
				}
			}
			return new ValueNode(valueToReturn, nodeToReturn);
		}
	}

	public static ValueNode alfaBeta(Node node, int level, int h, float alfa, float beta){

		if(node.isFinal()) {
			return new ValueNode((float) node.getResult(), node.clone());
		}
		else if(level == NIVELL_MAXIM_P) {
			ValueNode vn = null;
			switch(h){
			case 1:
				vn = new ValueNode(h1(node), node.clone());
				break;
			case 2:
				vn = new ValueNode(h2(node), node.clone());
				break;
			case 3:
				vn = new ValueNode(h3(node), node.clone());
				break;
			default:
				vn = new ValueNode(h3(node), node.clone());
				break;
			}
			return vn;
		}
		else{
			LinkedList<Node> children = node.getChildren(level);
			Node nodeToReturn = children.get(0);
			for(Node child : children){
				if(alfa < beta){
					ValueNode vn = alfaBeta(child.clone(), level+1, h, alfa, beta);
					if(level%2==1){
						if(vn.getValue() > alfa){
							alfa = vn.getValue();
							nodeToReturn = child.clone();
						}
					}else{
						if(vn.getValue() < beta){
							beta = vn.getValue();
							nodeToReturn = child.clone();
						}
					}
				}else{
					break;
				}
			}
			if(level%2==1) return new ValueNode(alfa, nodeToReturn);
			else return new ValueNode(beta, nodeToReturn);
		}
	}

	public static Domino getTile(Node father, Node child){
		for(Domino fatherD : father.getComputer()){
			if(!child.getComputer().contains(fatherD)){
				return fatherD.clone();
			}
		}
		return null;
	}

	public static float h1(Node n){
		int[] numbers = new int[7];
		for(Domino cd : n.getComputer()){
			numbers[cd.getSide1()] += 1;
			numbers[cd.getSide2()] += 1;
		}
		int max = 0;
		for(int i=0; i<7; i++){
			if(numbers[i] > max){
				max = numbers[i];
			}
		}
		return (8.0f - max);
	}

	public static float h2(Node n){
		int[] numbers = new int[7];
		for(Domino pd : n.getPlayer()){
			numbers[pd.getSide1()] += 1;
			numbers[pd.getSide2()] += 1;
		}
		int minor = 8;
		for(int i=0; i<7; i++){
			if(numbers[i] == 0){
				numbers[i] = 9;
			}
			if(numbers[i] < minor){
				minor = numbers[i];
			}
		}
		return (8.0f - minor);
	}

	public static float h3(Node n){
		if(n.getComputer().size() > n.getPlayer().size()){
			return h2(n);
		}else{
			return h1(n);
		}
	}

}
