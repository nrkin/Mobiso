package com.nrk.mobiso;

public class Answer {
	public long id;
	public String contents;
	public int score;
	public String ownerName;
	
	public Answer(long id, String contents, int score, String ownerName){
		this.id = id;
		this.contents = contents;
		this.score = score;
		this.ownerName = ownerName;
	}
}
