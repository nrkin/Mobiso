package com.nrk.mobiso;

import org.xml.sax.XMLReader;

import android.text.Editable;
import android.text.Html.TagHandler;
import android.util.Log;

public class TagParser implements TagHandler{
	private boolean preStart = false;
	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		Log.i("TAG_HANDLER", "opening = [ " + opening +" ], tag = [ " + tag + " ], output = [ " + output + " ]");
	}

}
