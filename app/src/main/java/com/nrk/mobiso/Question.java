package com.nrk.mobiso;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{
	public String ownerName;
	public int score;
	public String title;
	public long qId;
	public String contents;
	
	public Question(long qId, String title, int score, String ownerName, String contents){
		this.qId = qId;
		this.ownerName = ownerName;
		this.score = score;
		this.title = title;
		this.contents = contents;
	}
	
	public Question(Parcel p){
		this.ownerName = p.readString();
		this.score = p.readInt();
		this.title = p.readString();
		this.qId = p.readLong();
		this.contents = p.readString();
	}
	
	public static final Parcelable.Creator<Question> CREATOR
		= new Parcelable.Creator<Question>() {
			public Question createFromParcel(Parcel in) {
				return new Question(in);
			}
			public Question[] newArray(int size) {
				return new Question[size];
			}
		};


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ownerName);
		dest.writeInt(score);
		dest.writeString(title);
		dest.writeLong(qId);
		dest.writeString(contents);
	}
}
