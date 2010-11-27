/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.backends.lwjgl;

import java.io.File;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author mzechner
 * @author Nathan Sweet <misc@n4te.com>
 */
final class LwjglFileHandle extends FileHandle {
	LwjglFileHandle (String fileName, FileType type) {
		super(fileName, type);
	}

	LwjglFileHandle (File file, FileType type) {
		super(file, type);
	}

	public FileHandle child (String name) {
		return new LwjglFileHandle(new File(file, name), type);
	}

	public FileHandle parent () {
		File parent = file.getParentFile();
		if (parent == null) {
			switch (type) {
			case Classpath:
			case Absolute:
				parent = new File("/");
				break;
			case Internal:
				parent = new File("");
				break;
			case External:
				parent = new File(Gdx.files.getExternalStoragePath());
				break;
			}
		}
		return new LwjglFileHandle(parent, type);
	}
}
