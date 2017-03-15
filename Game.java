import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;



public class Game implements MouseListener,KeyListener, Runnable {
	
	//protected Game game;
	
	protected Rectangle bird;
	protected Random rand;
	protected int difficulity = 6;
	protected boolean game_over = false;
	protected ArrayList<Rectangle> tubes;
	protected float score = 0;
	protected Graphics g;
	private Thread thread;
	private Canvas canvas;
	private BufferStrategy bs;
	private float yMotion = 0, tick;
	protected JFrame jff;
	

	public Game(){
		
		jff = new JFrame("Flapy Bird! Good Luck! ");
		rand = new Random();
		bird = new Rectangle(300,150,25,25);
		tubes = new ArrayList<Rectangle>();
		
		jff.addMouseListener(this);
		jff.addKeyListener(this);
		jff.setSize(800, 600);
		jff.setResizable(false);
		jff.setLocationRelativeTo(null);
		jff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jff.setVisible(false);
		
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(800,600));
		canvas.setMaximumSize(new Dimension(800,600));
		canvas.addMouseListener(this);
		canvas.addKeyListener(this);
		canvas.setMinimumSize(new Dimension(800,600));
		jff.add(canvas);
		jff.pack();
		
		
		
		
	}
	
	private void addTube(){
		int space = 350;
		int width = 100;
		int height = 50 + rand.nextInt(250);
		tubes.add(new Rectangle(800 + width + tubes.size()*250, 600 - 150 - height, width,height));
		tubes.add(new Rectangle(800 + width + (tubes.size()-1)*250, 0, width, 600 - height - space));
	}
	
	private void paint_Tube(Graphics g, Rectangle tube){
		g.setColor(Color.gray);
		g.fillRect(tube.x, tube.y, tube.width, tube.height);
	}
	
	private void init(){
		addTube();
		score += 0.5;
		//background
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 800, 600);
		//grass
		g.setColor(Color.GREEN);
		g.fillRect(0, 450, 800, 150);
		//bird
		g.setColor(Color.RED);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		//tube
		for(Rectangle rec : tubes){
			paint_Tube(g, rec);
		}
		//score
		g.setColor(Color.black);
		g.setFont(new Font("Arial",1, 25));
		g.drawString("Score: " + score/10, 50, 550);
		
	}
	
	
	
	public synchronized void start() {
		if(game_over){
			return;
		}
		game_over = false;
		thread = new Thread(this);
		thread.start();
		
		
	}
	
	public synchronized void stop() {
		if(!game_over){
			return;
		}
			
		try {
			
			game_over = true;
			
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	public JFrame getJFrame(){
		return jff;
	}
	public int getDifficulity(){
		return difficulity;
	}
	public void setDifficulity(int i){
		difficulity = i;
	}
	
	
	
	@Override
	public void run() {
		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
			
		while(!game_over){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;
			if(delta >= 1){
				tick();
				render();
				delta --;
			}	
		}
		stop();
	}

	private void render(){
		bs = this.getCanvas().getBufferStrategy();
		if(bs == null){
			this.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//g.clearRect(0, 0, 800, 600);
		init();
		action();
		
		bs.show();
		g.dispose();
	}


	private void tick() {
		// TODO Auto-generated method stub
		
	}

	
	public void action() {
		tick++;
		int speed = 5;
		for(int i = 0; i < tubes.size();i++){
			Rectangle tube = tubes.get(i);
			tube.x -= speed;
		}
		if(tick % 3 == 0 && yMotion < 15){
			yMotion += 1;
		}
		
		for(int i = 0; i < tubes.size(); i++){
			Rectangle tube = tubes.get(i);
			if(tube.x + tube.width < 0){
				tubes.remove(tube);
				if(tube.y == 0){
					addTube();
				}
			}
		}
		
		for(Rectangle tube : tubes){
			if(tube.intersects(bird)){
				game_over = true;
			}
		}
		if(bird.y >= 450 || bird.y <= 0){
			game_over = true;
		}
		bird.y += yMotion;
		
		if(game_over){			
			g.drawString("Game over! Your score is " + score/10, 200, 300);
			g.drawString("You can pree key R to restart the game! " , 200, 250);
		}
		
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}


	private void jump() {
		if(game_over){
			bird = new Rectangle(300 - 50,150 - 50, 25,25);
			tubes.clear();
			yMotion = 0;
			score = 0;
		}else{
			if(yMotion > 0){
				yMotion = 0;
			}
			yMotion -= difficulity;
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R){
			jff.dispose();
			new Menu();
		}
		if(e.getKeyCode() == KeyEvent.VK_UP){
			jump();
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			jump();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	
	
	
}
