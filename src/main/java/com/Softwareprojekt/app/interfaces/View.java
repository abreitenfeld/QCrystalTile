package com.Softwareprojekt.app.interfaces;

public interface View {
	void show();
	void invalidateView();
	void invalidateViewOptions();
	
	Model getModel();
}
