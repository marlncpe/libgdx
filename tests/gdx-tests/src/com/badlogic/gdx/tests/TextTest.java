/*******************************************************************************
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.RenderListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Font;
import com.badlogic.gdx.graphics.Font.FontStyle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.SpriteBatch;

public class TextTest implements RenderListener
{
	SpriteBatch spriteBatch;
	Font font;				


	@Override
	public void surfaceCreated( ) 
	{
		if( font == null )
		{		
			spriteBatch = new SpriteBatch( );
			font = Gdx.graphics.newFont( "Arial", 32, FontStyle.Italic );
		}
	}
	
	@Override
	public void render( ) 
	{
		Gdx.graphics.getGL10().glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		Gdx.graphics.getGL10().glClearColor( 0.7f, 0.7f, 0.7f, 1 );
		Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT );						
		
		spriteBatch.begin();
		spriteBatch.draw( font.getTexture(), 0, 100, font.getTexture().getWidth(), font.getTexture().getHeight(), 0, 0, font.getTexture().getWidth(), font.getTexture().getHeight(), Color.WHITE, false, true );
		spriteBatch.drawText( font, "this is a test", 100, 100, Color.RED );
		spriteBatch.end();
	}

	@Override
	public void dispose( ) 
	{	
		
	}
	
	@Override
	public void surfaceChanged( int width, int height) 
	{	
		
	}
}