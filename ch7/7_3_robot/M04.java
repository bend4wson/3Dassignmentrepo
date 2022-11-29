import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

// import GridLayout;


// public class GridLayoutEg extends JPanel {
//     private static final int ROWS = 3;
//     private static final int COLS = 10;

//     public GridLayoutEg() {
//         setLayout(new GridLayout(ROWS, COLS)); // set JPanel's layout
//         for (int i = 0; i < ROWS; i++) {
//             for (int j = 0; j < COLS; j++) {
//                 String text = String.format("[%d, %d]", j + 1, i + 1);
//                 add(new JButton(text)); // add component w/o 2nd parameter
//             }
//         }
//     }

//     private static void createAndShowGui() {
//         GridLayoutEg mainPanel = new GridLayoutEg();
//         JFrame frame = new JFrame("GridLayoutEg");
//         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         frame.add(mainPanel);
//         frame.pack();
//         frame.setLocationByPlatform(true);
//         frame.setVisible(true);
//     }
// }

public class M04 extends JFrame implements ActionListener {
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private M04_GLEventListener glEventListener;
  private final FPSAnimator animator; 
  private Camera camera;

  public static void main(String[] args) {
    M04 b1 = new M04("M04");
    //SwingUtilities.invokeLater(() -> createAndShowGui());
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
  }

  public M04(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new M04_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);
    
    JMenuBar menuBar=new JMenuBar();
    this.setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
    menuBar.add(fileMenu);
    
    JPanel p = new JPanel();
      JButton b = new JButton("camera X");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("camera Z");
      b.addActionListener(this);
      p.add(b);
      // b = new JButton("start");
      // b.addActionListener(this);
      // p.add(b);
      // b = new JButton("stop");
      // b.addActionListener(this);
      // p.add(b);
      b = new JButton("l1 pos1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("l1 pos2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("l1 pos3");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("l2 pos1");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("l2 pos2");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("l2 pos3");
      b.addActionListener(this);
      p.add(b);
      b = new JButton("quit");
      b.addActionListener(this);
      p.add(b);
    this.add(p, BorderLayout.SOUTH);
  
    // JPanel p = new JPanel();
    //   JButton b = new JButton("lamp 2 position 1");
    //   b.addActionListener(this);
    //   p.add(b);
    //   b = new JButton("lamp 2 position 2");
    //   b.addActionListener(this);
    //   p.add(b);
    //   b = new JButton("lamp 2 position 3");
    //   b.addActionListener(this);
    //   p.add(b);
    // this.add(p, BorderLayout.SOUTH);

    
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equalsIgnoreCase("camera X")) {
      camera.setCamera(Camera.CameraType.X);
      canvas.requestFocusInWindow();
    }
    else if (e.getActionCommand().equalsIgnoreCase("camera Z")) {
      camera.setCamera(Camera.CameraType.Z);
      canvas.requestFocusInWindow();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l1 pos1")) {
      glEventListener.lamp1Position1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l1 pos2")) {
      glEventListener.lamp1Position2();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l1 pos3")) {
      glEventListener.lamp1Position3();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l2 pos1")) {
      glEventListener.lamp2Position1();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l2 pos2")) {
      glEventListener.lamp2Position2();
    }
    else if (e.getActionCommand().equalsIgnoreCase("l2 pos3")) {
      glEventListener.lamp2Position3();
    }
    // else if (e.getActionCommand().equalsIgnoreCase("increase X position")) {
    //   glEventListener.incXPosition();
    // }
    // else if (e.getActionCommand().equalsIgnoreCase("decrease X position")) {
    //   glEventListener.decXPosition();
    // }
    // else if (e.getActionCommand().equalsIgnoreCase("lowered arms")) {
    //   glEventListener.loweredArms();
    // }
    // else if (e.getActionCommand().equalsIgnoreCase("raised arms")) {
    //   glEventListener.raisedArms();
    // }
    else if(e.getActionCommand().equalsIgnoreCase("quit"))
      System.exit(0);
  }
  
}
 
class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }
}