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

package com.badlogic.gdx.backends.android;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceViewCupcake;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;

/**
 * An implementation of the {@link Application} interface for Android. Create an {@link Activity}
 * that derives from this class. In the {@link Activity.onCreate()} method call the {@link initialize()}
 * method specifying the configuration for the GLSurfaceView.
 *
 * @author mzechner
 */
public class AndroidApplication extends Activity implements Application {
    
	static 
    {
		Version.loadLibrary();
    }

    /**
     * the android graphics instance *
     */
    private AndroidGraphics graphics;

    /**
     * the input instance *
     */
    private AndroidInput input;

    /**
     * the audio instance *
     */
    private AndroidAudio audio;

    /**
     * the resources instance *
     */
    private AndroidFiles resources;

    /**
     * the DestroyListener *
     */
    private ApplicationListener listener;
    
    /**
     * This method has to be called in the {@link Activity.onCreate()}
     * method. It sets up all the things necessary to get input, render
     * via OpenGL and so on. If useGL20IfAvailable is set the
     * AndroidApplication will try to create an OpenGL ES 2.0 context
     * which can then be used via {@link AndroidApplication.getGraphics().getGL20()}. The
     * {@link GL10} and {@link GL11} interfaces should not be used when
     * OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was
     * successful use the {@link AndroidApplication.getGraphics().isGL20Available()}
     * method. Sleep time in touch event handler is 0, so no sleeping is performed.
     *
     * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
     */
    public void initialize(boolean useGL2IfAvailable) {
        initialize(useGL2IfAvailable, 0);
    }

    /**
     * This method has to be called in the {@link Activity.onCreate()}
     * method. It sets up all the things necessary to get input, render
     * via OpenGL and so on. If useGL20IfAvailable is set the
     * AndroidApplication will try to create an OpenGL ES 2.0 context
     * which can then be used via {@link AndroidApplication.getGraphics().getGL20()}. The
     * {@link GL10} and {@link GL11} interfaces should not be used when
     * OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was
     * successful use the {@link AndroidApplication.getGraphics().isGL20Available()}
     * method. sleepTime specifies the number of milliseconds to sleep in the touch
     * event handler. This may be used on <= 1.6 Android devices. Note that it will not
     * solve the CPU usage problem of the event handler of the Android system. Things will
     * still slow down.
     *
     * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
     * @param sleepTime         specifies the number of milliseconds to sleep in the touch event handler
     */
    public void initialize(boolean useGL2IfAvailable, int sleepTime ) 
    {    	    	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        graphics = new AndroidGraphics(this, useGL2IfAvailable);
        setContentView( graphics.getView() );
        input = new AndroidInput(this, graphics.view, sleepTime);
        graphics.setInput(input);
        audio = new AndroidAudio(this);
        resources = new AndroidFiles(this.getAssets());   
        
        Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
    }
    
    /**
     * This method has to be called in the {@link Activity.onCreate()}
     * method. It sets up all the things necessary to get input, render
     * via OpenGL and so on. If useGL20IfAvailable is set the
     * AndroidApplication will try to create an OpenGL ES 2.0 context
     * which can then be used via {@link AndroidApplication.getGraphics().getGL20()}. The
     * {@link GL10} and {@link GL11} interfaces should not be used when
     * OpenGL ES 2.0 is enabled. To query whether enabling OpenGL ES 2.0 was
     * successful use the {@link AndroidApplication.getGraphics().isGL20Available()}
     * method. sleepTime specifies the number of milliseconds to sleep in the touch
     * event handler. This may be used on <= 1.6 Android devices. Note that it will not
     * solve the CPU usage problem of the event handler of the Android system. Things will
     * still slow down.
     * 
     * Note: you have to add the returned view to your layout!
     *
     * @param useGL2IfAvailable whether to use OpenGL ES 2.0 if its available.
     * @param sleepTime         specifies the number of milliseconds to sleep in the touch event handler
     * @return the GLSurfaceView of the application
     */
    public View initializeForView( boolean useGL2IfAvailable, int sleepTime )
    {
        graphics = new AndroidGraphics(this, useGL2IfAvailable );
        input = new AndroidInput(this, graphics.view, sleepTime);
        graphics.setInput(input);
        audio = new AndroidAudio(this);
        resources = new AndroidFiles(this.getAssets());   
        
        Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		
		return graphics.getView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing())
        {
            graphics.disposeRenderListener();
            graphics.clearManagedCaches();
        }

        if ( graphics != null && graphics.view != null)
        {
        	if( graphics.view instanceof GLSurfaceViewCupcake )
        		((GLSurfaceViewCupcake)graphics.view).onPause();
        	if( graphics.view instanceof android.opengl.GLSurfaceView )
        		((android.opengl.GLSurfaceView)graphics.view).onPause();
        }

        if( audio != null )
        	audio.pause();

        if (listener != null)
            listener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
        
        if (listener != null)
            listener.resume();

        if ( graphics != null && graphics.view != null)
        {
        	if( graphics.view instanceof GLSurfaceViewCupcake )
        		((GLSurfaceViewCupcake)graphics.view).onResume();
        	if( graphics.view instanceof android.opengl.GLSurfaceView )
        		((android.opengl.GLSurfaceView)graphics.view).onResume();
        }

        if( audio != null )
        	audio.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listener != null)
            listener.destroy();
        
        audio.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Audio getAudio() {
        return audio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Files getFiles() {
        return resources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Input getInput() {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationListener(ApplicationListener listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String tag, String message) {
        Log.d(tag, message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationType getType() {
        return ApplicationType.Android;
	}
}