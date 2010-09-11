package model;

import java.awt.Color;

public class Player implements Comparable<Player>{
	private String name;
	private Color color;

	public Player(String name, Color c){
		this.name= name;
		this.color= c;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Color getColor() {
		return color;
	}
	
	/**
	 * Métodos para determinar la igualdad de los jugadores.
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Player))
			return false;
		Player p2= (Player)obj;
		if(this.name==null){
			if(p2.name==null) 
				return true;
			else 
				return false;
		}
		return this.name.equals(p2.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public int compareTo(Player o) {
		return this.name.compareTo(o.name);
	}
}