package com.m2leal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class BasicFrame implements GLEventListener, KeyListener  {

	private GLU glu = new GLU();
	private float[] eye = {0f,5f,50f}, focus = {0f,4.9f,49f}, up = {0f,1f,0f};
	
	private boolean moveForward = false;
	private boolean rotateRight = false;
	private boolean moveBack = false;
	private boolean rotateLeft = false;
	
	public void display(GLAutoDrawable drawable) {
		
		if(rotateLeft)
			eye = this.rotateAroudEye(0.05f, eye, focus);
		if(moveForward)
			moveForward(eye, focus);
		if(rotateRight)
			eye = this.rotateAroudEye(-0.05f, eye, focus);
		if(moveBack)
			moveBack(eye, focus);
		
		
		
		final GL2 gl = drawable.getGL().getGL2();
		// Clear The Screen And The Depth Buffer
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// Especifica sistema de coordenadas de projeção
		gl.glMatrixMode(GL2.GL_PROJECTION);
		// Inicializa sistema de coordenadas de projeção
		gl.glLoadIdentity();

		// Especifica a projeção perspectiva fovy, aspect, zNear, zFar
		glu.gluPerspective(45,2,1.0,500);

		// Especifica sistema de coordenadas do modelo
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// Inicializa sistema de coordenadas do modelo
		gl.glLoadIdentity();

		// Especifica posição do observador e do alvo -  eyeX,eyeY,eyeZ,   centerX,centerY,centerZ,    upX,upY,upZ
		glu.gluLookAt(eye[0],eye[1],eye[2], focus[0],focus[1],focus[2], up[0],up[1],up[2]);
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glColor3f(1.0f, 1.0f, 1.0f);

		//Draw the ground
		gl.glBegin(GL2.GL_LINES);
		for (int i = -50; i <= 50; i++) {
			gl.glVertex3i(i, 0,  50);
			gl.glVertex3i(i, 0, -50);
			
			gl.glVertex3i( 50, 0, i);
			gl.glVertex3i(-50, 0, i);
		}
		gl.glEnd();
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height <= 0)
			height = 1;
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public static void main(String[] args) {
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);

		// The canvas
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		BasicFrame basicFrame = new BasicFrame();
		glcanvas.addGLEventListener(basicFrame);
		glcanvas.setSize(1024, 768);
		glcanvas.addKeyListener(basicFrame);
		glcanvas.requestFocus();
		
		final JFrame frame = new JFrame("3d  Triangle (solid)");
		frame.getContentPane().add(glcanvas);
		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);
		animator.start();
	}

	
	
	
	
// -=-=-=-=-=-=-=-=-=-= Key Listener -=-=-=-=-=-=-=-=-=-=
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Key Typed: "+ e.getKeyCode());
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		
		if(e.getKeyCode() == KeyEvent.VK_UP)
			moveForward = true;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			rotateRight = true;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			moveBack = true;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			rotateLeft = true;
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP)
			moveForward = false;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			rotateRight = false;
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
			moveBack = false;
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			rotateLeft = false;
	}

// -=-=-=-=-=-=-=-=-=-= Aux -=-=-=-=-=-=-=-=-=-=
	public float[] rotateAroudEye(float angle, float[] eye, float[] focus) {
		float x = eye[0]-focus[0];
		float y = eye[1];
		float z = eye[2]-focus[2];

		x =  (x * (float)Math.cos(angle)) + (z * (float)Math.sin(angle));
		z = -(x * (float)Math.sin(angle)) + (z * (float)Math.cos(angle));
		
		x += focus[0];
		z += focus[2];
		
		return new float[] {x, y, z};
	}
	
	public float[] rotateAroudFocus(float angle, float[] eye, float[] focus) {
		float x = focus[0]-eye[0];
		float y = focus[1];
		float z = focus[2]-eye[2];
		
		x =  (x * (float)Math.cos(angle)) + (z * (float)Math.sin(angle));
		z = -(x * (float)Math.sin(angle)) + (z * (float)Math.cos(angle));
		
		x += eye[0];
		z += eye[2];
		
		return new float[] {x, y, z};
	}
	
	public void moveForward(float[] eye, float[] center) {
		float x = eye[0]-center[0];
		float z = eye[2]-center[2];
		
		eye[0] -= x;
		eye[2] -= z;
		
		center[0] -= x;
		center[2] -= z;
	}
	
	public void moveBack(float[] eye, float[] center) {
		float x = eye[0]-center[0];
		float z = eye[2]-center[2];
		
		eye[0] += x;
		eye[2] += z;
		
		center[0] += x;
		center[2] += z;
	}
}
