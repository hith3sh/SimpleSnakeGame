import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
	static final int SCREEN_WIDTH=500;
	static final int SCREEN_HEIGHT=500;
	static final int UNIT_SIZE=25;
	static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY=75;
	
	final int x[]=new int[GAME_UNITS];
	final int y[]=new int[GAME_UNITS];
	int bodyParts=5;
	int applesEaten;
	int appleX;
	int appleY;
	char direction='R';
	boolean running=false;
	boolean snakeHasEaten=true;
	Timer timer;
	Random random;

	GamePanel(){
		random=new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running=true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	
	}
	public void draw(Graphics g) {
		if (running) {
			//drawing the grid lines
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			//setting the apples
			g.setColor(Color.RED);
			g.fillOval(appleX,appleY, UNIT_SIZE,UNIT_SIZE);
			
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					//coloring the head of the snake 
					g.setColor(Color.green);
					g.fillRect(x[i], y[i],UNIT_SIZE, UNIT_SIZE);
				}
				else {
					if (snakeHasEaten) {
						g.setColor(new Color(233,240,180));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}else {
						g.setColor(new Color(45,180,0));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
					
				}
			}
			g.setColor(Color.green);
			g.setFont(new Font("Ink free",Font.BOLD,20));
			FontMetrics fmetrics=getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten,(SCREEN_WIDTH - fmetrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize() );
			//resetting snake has eaten
			snakeHasEaten=false;
		}
		else {
			gameOver(g);
		}
		
		
	}
	public void newApple() {
		appleX=random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY=random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
		
		
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]=y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0]=y[0] +UNIT_SIZE;
			break;
		case 'L':
			x[0]=x[0] -UNIT_SIZE;
			break;
		case 'R':
			x[0]=x[0] +UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if (x[0]== appleX &&  y[0]==appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
			snakeHasEaten=true;
		}
	}
	public void checkCollisions() {
		//this checks if the head of snake collides with the body
		for(int i=bodyParts;i>0;i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				running=false;
			}
		}
		//this checks if snake's head touches left,right,top,bottom border
		if ( x[0]<0 || x[0]>SCREEN_WIDTH || y[0]<0 || y[0]>SCREEN_HEIGHT) {
			running=false;
		}
		if (!running) {
			timer.stop();
		}
		
	}
	public void gameOver(Graphics g) {
		//displays score after game over
		g.setColor(Color.green);
		g.setFont(new Font("Ink free",Font.BOLD,20));
		FontMetrics fmetrics1=getFontMetrics(g.getFont());
		g.drawString("Score is: "+applesEaten,(SCREEN_WIDTH - fmetrics1.stringWidth("Score is: "+applesEaten))/2,6*SCREEN_HEIGHT/10  );
		//Display game over message
		g.setColor(Color.red);
		g.setFont(new Font("Ink free",Font.BOLD,40));
		FontMetrics fmetrics2 = getFontMetrics(g.getFont());
		g.drawString("Snake died :(",(SCREEN_WIDTH-fmetrics2.stringWidth("Snake died XD"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction !='R') {
					direction='L';
				}break;
			case KeyEvent.VK_RIGHT:
				if (direction !='L') {
					direction ='R';
				}break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction ='U';
				}break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction ='D';
				}break;
			}
		}
	}
}
