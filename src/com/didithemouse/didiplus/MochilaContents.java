package com.didithemouse.didiplus;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

import com.didithemouse.didiplus.etapas.ChinatownActivity;
import com.didithemouse.didiplus.etapas.ConeyActivity;
import com.didithemouse.didiplus.etapas.EmpireStateActivity;
import com.didithemouse.didiplus.etapas.InicioActivity;

public class MochilaContents {

	private DropPanelWrapper dropPanel;
	private ArrayList<ViewWrapper> items;
	private String[] texts;
	private String[] textsOriginal;
	
	private boolean created;
	public boolean hasEdited;
	public boolean hasFinished;
	
	private int kidNumber = 0;
	private String kidName = "";
	private String dirName = "" ;
	//private String RCSdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/My SugarSync Folders/My SugarSync/RC-Write/";
	private String RCSdir = Environment.getExternalStorageDirectory().getAbsolutePath() +"/TestWrite/";
	
	//Deshabilita log, guardado, etc. para debug.
	public static final boolean SAVING  = true;
	public static final boolean LOGGING = false;
	public static final boolean SKIP_OBJECTS = true;
	public static final boolean SKIP_MAP     = true;
	
	public static int numStages = 4;
	private int visitedPlaces = 0;
	public int getVisitedPlaces()
	{
		visitedPlaces = 0;
		if(EmpireStateActivity.visitedFlag) visitedPlaces++;
		if(ConeyActivity.visitedFlag ) visitedPlaces++;
		if(InicioActivity.visitedFlag) visitedPlaces++;
		if(ChinatownActivity.visitedFlag ) visitedPlaces++;
		return visitedPlaces;
	}
	
	
	private static MochilaContents INSTANCE = new MochilaContents();
	private MochilaContents() {

	}
	public static MochilaContents getInstance() {
        return INSTANCE;
    }
/*	public DropPanelWrapper getPanel(int i) {
		return dropPanelsWrappers.get(i);
	}*/
	public DropPanelWrapper getDropPanel() {
		if (dropPanel == null) dropPanel = new DropPanelWrapper();
		return dropPanel;
	}
	
	public void setDropPanel(DropPanelWrapper _dropPanel) {
		dropPanel = _dropPanel;
	}

	
	public void addItem(ExtendedImageView v) {
		int vHeight = v.getHeight();
		int vWidth = v.getWidth();
		float vSize = Math.max(v.getHeight(), v.getWidth());
		
		float scaleFactor = CreateActivity.objectSize*1.0f/vSize; 
		v.setScaleFactor(scaleFactor);

		ViewWrapper vw = new ViewWrapper(0, 0, vWidth/2, vHeight/2, v, v.getEtapa());
		items.add(vw);
		vw.destroyView();
	}
	
	public void addItem(ViewWrapper v) {
		items.add(v);
	}
	
	public ArrayList<ViewWrapper> getItems()
	{
		return items;
	}
	
	public boolean isCreated() { return created;	}
	public void setCreated(boolean created) {	this.created = created; }
	
	public void setText(int index, String text)
	{
		if(texts == null) texts = new String[] {"","",""};
		texts[index%3] = text;
	}
	public String getText(int index)
	{
		if(texts == null) texts = new String[] {"","",""};
		return texts[index%3];
	}
	public String getTextOriginal(int index)
	{
		if(textsOriginal == null) textsOriginal = new String[] {"","",""};
		return textsOriginal[index%3];
	}
	public void setTextOriginal(int index, String text)
	{
		if(textsOriginal == null) textsOriginal = new String[] {"","",""};
		textsOriginal[index%3] = text;
	}
	public String[] getTexts(){ return texts;}
	public String[] getTextsOriginal(){ return textsOriginal;}
	public void cloneTexts(){
			for(int i =0; i<3; i++)
			textsOriginal[i]=texts[i];

	}
	
	public void cleanPanels()
	{
		for(ViewWrapper wx: items)
			wx.destroyView();
		dropPanel.cleanPanel(true);
	}
	
	public void setKid(int _kidNumber,String _kidName) { 
		kidNumber = _kidNumber;
		kidName = _kidName != null? _kidName: "";
		
		dirName = RCSdir +"/"+kidNumber+"/";
        (new File (dirName)).mkdirs();
	}
	
	public int getKidNumber(){ return kidNumber; }
	public String getKidName(){ return kidName; }
	public String getDirectory() { return dirName; }
	
	private final String logDirname =  RCSdir + "/log/";
	
	public String getLogDirname()
	{
		File f = new File(logDirname);
		if (!f.exists()) f.mkdirs();
		return logDirname;
	}
	
	
	public boolean kidExists(int num)
	{
		String dirnameX = RCSdir +"/"+num+"/" ;
        return (new File(dirnameX)).exists();
        	
	}
	
	public void restart()
	{
		Saver.clear();
		if (dropPanel != null)
			dropPanel.killPanel(true);
		if (items != null)
		for(ViewWrapper wx: items)
			wx.destroyView();
		items = new ArrayList<ViewWrapper>();
		dropPanel = new DropPanelWrapper();
		texts = new String[] {"","",""};
		textsOriginal= new String[] {"","",""};
		created = false;
		hasEdited=false;
		hasFinished=false;
		
		visitedPlaces = 1;
				
		InicioActivity.visitedFlag=false;
		EmpireStateActivity.visitedFlag = false;
		ConeyActivity.visitedFlag = false;
		ChinatownActivity.visitedFlag = false;

	}
	
}
