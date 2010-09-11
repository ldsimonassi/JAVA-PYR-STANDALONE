package model;

public class ScoreUnit extends GameElement{
	/**
	 * Estructuras de datos
	 */
	
	private Player winner;
	private Wire up;
	private Wire down;
	private Wire left;
	private Wire right;
	
	
	/**
	 * Crear una unidad de puntaje (En la pantalla es un cuadrado)
	 * @param gameCore
	 */
	public ScoreUnit(GameCore gameCore) {
		super(gameCore);
	}

	public Wire getUp() {
		return up;
	}

	public void setUp(Wire up) {
		this.up = up;
		up.addRelatedScoreUnit(this);
	}

	public Wire getDown() {
		return down;
	}

	public void setDown(Wire down) {
		this.down = down;
		down.addRelatedScoreUnit(this);
	}

	public Wire getLeft() {
		return left;
	}

	public void setLeft(Wire left) {
		this.left = left;
		left.addRelatedScoreUnit(this);
	}

	public Wire getRight() {
		return right;
	}

	public void setRight(Wire right) {
		this.right = right;
		right.addRelatedScoreUnit(this);
	}

	public Player getWinner() {
		return winner;
	}
	
	public boolean isCompleted(){
		return up.player    != null && 
			   down.player  != null && 
			   right.player != null && 
			   left.player  != null;
	}

	public boolean isCompleteWith(Wire wire) {
		return (up==wire    || up.player!=null   ) && 
			   (down==wire  || down.player!=null ) && 
			   (right==wire || right.player!=null) && 
			   (left==wire  || left.player!=null );
	}

	public boolean setWinner(Player p) {
		if(winner!=null) 
			return false;
		winner= p;
		System.out.println("****** El jugador["+p+"] ganó un punto :-) **********");
		return true;
	}
	
	@Override
	public String toString() {
		String s= "Up   :"+up+"\n"+
				  "Left :"+left+"\n"+
				  "Right:"+right+"\n"+
				  "Down :"+down+"\n";
		return s;
	}
}
