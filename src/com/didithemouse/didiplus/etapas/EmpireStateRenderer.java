package com.didithemouse.didiplus.etapas;

import com.didithemouse.didiplus.R;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;

public class EmpireStateRenderer extends EtapaRenderer implements GLSurfaceView.Renderer {
	
	public EmpireStateRenderer(Context context, float initX, float initY, EtapaSurfaceView sv, Handler handler)
	{
		super(context,initX,initY,sv,handler);
		
	}
	void createSquares(){	
		//Aqui tenemos 4 grupos de imagenes, que van asi en la pantalla
		//[story01] [s02] [s03] [s04] [obj0_0] [obj0_1]
		//[story05] [s06] [s07] [s08] [obj1_0] [obj1_1]
		//Y asi

		squaresDrawables[0][0] = R.drawable.empirestate_story_1;
		squaresDrawables[0][1] = R.drawable.empirestate_story_2;
		squaresDrawables[0][2] = R.drawable.empirestate_story_3;
		squaresDrawables[0][3] = R.drawable.empirestate_story_4;
		squaresDrawables[0][4] = R.drawable.empirestate_story_5;
		squaresDrawables[0][5] = R.drawable.empirestate_story_6;
		
		squaresDrawables[1][0] = R.drawable.empirestate_story_7;
		squaresDrawables[1][1] = R.drawable.empirestate_story_8;
		squaresDrawables[1][2] = R.drawable.empirestate_story_9;
		squaresDrawables[1][3] = R.drawable.empirestate_story_10;
		squaresDrawables[1][4] = R.drawable.empirestate_story_11;
		squaresDrawables[1][5] = R.drawable.empirestate_story_12;
		
		squaresDrawables[2][0] = R.drawable.empirestate_story_13;
		squaresDrawables[2][1] = R.drawable.empirestate_story_14;
		squaresDrawables[2][2] = R.drawable.empirestate_story_15;
		squaresDrawables[2][3] = R.drawable.empirestate_story_16;
		squaresDrawables[2][4] = R.drawable.empirestate_story_17;
		squaresDrawables[2][5] = R.drawable.empirestate_story_18;
		
		squaresDrawables[3][0] = R.drawable.empirestate_story_19;
		squaresDrawables[3][1] = R.drawable.empirestate_story_20;
		squaresDrawables[3][2] = R.drawable.empirestate_story_21;
		squaresDrawables[3][3] = R.drawable.empirestate_story_22;
		squaresDrawables[3][4] = R.drawable.empirestate_story_23;
		squaresDrawables[3][5] = R.drawable.empirestate_story_24;
		
		squaresDrawables[4][0] = R.drawable.empirestate_story_25;
		squaresDrawables[4][1] = R.drawable.empirestate_story_26;
		squaresDrawables[4][2] = R.drawable.empirestate_story_27;
		squaresDrawables[4][3] = R.drawable.empirestate_story_28;
		squaresDrawables[4][4] = R.drawable.empirestate_story_29;
		squaresDrawables[4][5] = R.drawable.empirestate_story_30;
		
		squaresDrawables[0][6] = R.drawable.empirestate_objects_1;
		squaresDrawables[0][7] = R.drawable.empirestate_objects_2;
		squaresDrawables[0][8] = R.drawable.empirestate_objects_3;
		
		squaresDrawables[1][6] = R.drawable.empirestate_objects_4;
		squaresDrawables[1][7] = R.drawable.empirestate_objects_5;
		squaresDrawables[1][8] = R.drawable.empirestate_objects_6;
		
		squaresDrawables[2][6] = R.drawable.empirestate_objects_7;
		squaresDrawables[2][7] = R.drawable.empirestate_objects_8;
		squaresDrawables[2][8] = R.drawable.empirestate_objects_9;
		
		squaresDrawables[3][6] = R.drawable.empirestate_objects_10;
		squaresDrawables[3][7] = R.drawable.empirestate_objects_11;
		squaresDrawables[3][8] = R.drawable.empirestate_objects_12;
		
		squaresDrawables[4][6] = R.drawable.empirestate_objects_13;
		squaresDrawables[4][7] = R.drawable.empirestate_objects_14;
		squaresDrawables[4][8] = R.drawable.empirestate_objects_15;
		
		squaresDrawables[5][6] = R.drawable.empirestate_objects_16;
		squaresDrawables[5][7] = R.drawable.empirestate_objects_17;
		squaresDrawables[5][8] = R.drawable.empirestate_objects_18;
        
	}  
}
