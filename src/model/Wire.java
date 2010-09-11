package model;

import java.util.LinkedList;

public class Wire extends GameElement {
	LinkedList<ScoreUnit> myScoreUnits= new LinkedList<ScoreUnit>();

	public Wire(int wireId, GameCore core){
		super(core);
		this.wireId= wireId;
	}
	
	public void addRelatedScoreUnit(ScoreUnit unit){
		myScoreUnits.add(unit);
	}
	
	int wireId;
	Player player= null;
	
	public boolean play() {
		boolean playerWins= false;
		boolean playerWinsAny= false;
		if(player!=null)
			return false;
		Player p= getGameCore().getCurrentPlayer();

		for (ScoreUnit unit : myScoreUnits) {
			playerWins= !unit.isCompleted() && unit.isCompleteWith(this);
			if(playerWins){
				if(!unit.setWinner(p)) throw new Error("Inconsistencia!: La ScoreUnit ya tenia ganador.");
				playerWinsAny= true;
			}
		}	
		this.player= p;
		if(!playerWinsAny)
			getGameCore().nextPlayer();
		getGameCore().fireGameStatusChanged();
		return true;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public String toString() {
		return wireId+"["+player+"]["+myScoreUnits.size()+"]";
	}
}