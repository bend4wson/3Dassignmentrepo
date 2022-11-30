import gmaths.*;
//import Math;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class M04_GLEventListener implements GLEventListener {
      
  public M04_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,12f,18f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    // gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    // gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light1.dispose(gl);
    light2.dispose(gl);
    ttWall.dispose(gl);
    ttWindow.dispose(gl);
    ttFloor.dispose(gl);
    ttBackground.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
    cube3.dispose(gl);
    cube4.dispose(gl);
    tableLeg.dispose(gl);
    // tableLeg.dispose(gl);
    // tableLeg.dispose(gl);
    // tableLeg.dispose(gl);
    //robot.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
   
  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
   
  // public void incXPosition() {
  //   robot.incXPosition();
  // }
   
  // public void decXPosition() {
  //   robot.decXPosition();
  // }
  
  // public void loweredArms() {
  //   stopAnimation();
  //   robot.loweredArms();
  // }
   
  // public void raisedArms() {
  //   stopAnimation();
  //   robot.raisedArms();
  // }
  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model ttBackground;
  private Light light1, light2;
  private Model cube, cube2, cube3, cube4, sphere, jointSphere, tableLeg, ttWall, ttFloor, ttWindow;
  //private int frame = 1;

  private SGNode branchRoot, lamp1Root, lamp2Root;
  private TransformNode translateEgg, rotateEggZ, rotateEggY, lamp1MoveTranslate, rotateLamp, translateLamp2, lArmTransform, rotateLowerZ, rotateLowerY, rotateUpper, uArmTranslate, rotateHead; //, uArmTranslate;//, lArm;//Transform;

  private static final boolean DISPLAY_SHADERS = false;
  private Shader shader;
  //Necessary? \/ \/ \/ 


  // private float lArmAngleZ = 30;
  // private float lArmAngleY = 0;
  // private float headAngleZ = 0;
  private float xPosition = 0;
  private float upperAngle = -60;
  // private float lArmTranslateZ = 0;
  // private float lArmTranslateX = -4.9f;  

  // private float uArmAngleY = 0;
  // private float uArmAngleZ = -30;
  // private float uArmTranslateZ = 0;
  // private float uArmTranslateX = -7f;  
  // private float uArmTranslateY = -0.15f;


  // private float jointTranslateX = -9f;
  // private float jointTranslateY = 2.85f;
  // private float jointTranslateZ = 0;
  private float rotateAllAngleStart = 10, rotateAllAngle = rotateAllAngleStart;
  private float rotateYAngleStart = 45, rotateYAngle = rotateYAngleStart;
  //private float rotateUpperAngleStart = -60, rotateUpperAngle = rotateUpperAngleStart;
  //private SGNode robotRoot;
  
  //private Robot robot;

  // private int[] textureId9 = new int[1];
  // private int[] vertexArrayId = new int[1];
  // private int[] indices = {         // Note that we start from 0!
  //     0, 1, 2
  // }; 

  public void initialise(GL3 gl) {
    createRandomNumbers();

    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");

    //int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    //int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/ear0xuu2.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud2.jpg");
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/blizzardBackground.jpg");
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/mar0kuu2_specular.jpg");

    light1 = new Light(gl);
    light1.setCamera(camera);
    light2 = new Light(gl);
    light2.setCamera(camera);

//----------------------//----------------------//----------------------//----------------------//----------------------
    // double elapsedTime = getSeconds() - startTime;
    // shader.use(gl);
    // double t = elapsedTime*0.1;
    // float offsetX = (float)(t - Math.floor(t));
    // float offsetY = 0.0f;
    // shader.setFloat(gl, "offset", offsetX, offsetY);
    
    // shader.setInt(gl, "first_texture", 0);
    // shader.setInt(gl, "second_texture", 1);

    // gl.glActiveTexture(GL.GL_TEXTURE0);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1);
    // gl.glActiveTexture(GL.GL_TEXTURE1);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2);
  
    // gl.glBindVertexArray(vertexArrayId[0]);
    // gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    // gl.glBindVertexArray(0);
    
    // gl.glActiveTexture(GL.GL_TEXTURE0);
//----------------------//----------------------//----------------------//----------------------//----------------------

    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    ttFloor = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId3);
    ttWindow = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId8);
    ttWall = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId3);
    ttBackground = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId9);


    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4, 4, 4), Mat4Transform.translate(0, 0.5f, 0));
    cube = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId4, textureId4);
    cube2 = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube3 = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube4 = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId0, textureId0);

    tableLeg = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId3, textureId4);

    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId1, textureId2);
    jointSphere = new Model(gl, camera, light1, shader, material, modelMatrix, mesh, textureId0, textureId0);

    branchRoot = new NameNode("sphere-branch-structure");
    translateEgg = new TransformNode("translate("+xPosition+",0,0)", Mat4Transform.translate(xPosition,3,0));
    rotateEggZ = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));
    //model = Mat4.multiply(model, Mat4Transform.rotateAroundY(yAngle));
    rotateEggY = new TransformNode("rotateAroundY("+rotateYAngle+")", Mat4Transform.rotateAroundY(rotateYAngle));
    NameNode branch = new NameNode("lower branch");
    Mat4 m = Mat4Transform.scale(1.5f,2.6f,1.5f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode makeBranch = new TransformNode("(0.9f,2.6f,0.9f); translate(2.5,7.0,2.5)", m);
    ModelNode cube0Node = new ModelNode("Sphere(0)", sphere); 

    branchRoot.addChild(translateEgg);
      translateEgg.addChild(rotateEggZ);
        rotateEggZ.addChild(branch);
      translateEgg.addChild(rotateEggY);
        rotateEggY.addChild(branch);
          branch.addChild(makeBranch);
      makeBranch.addChild(cube0Node);
              //translateEgg.addChild(rotateEggY);
    branchRoot.update();

    // lamp1Root = createLamp1(m, -30);
    // lamp1Root.update();
    // lamp2Root = createLamp2(m);
    // lamp2Root.update();
  

    lamp1Root = createLamp1(m); //, -30, (int)this.lArmAngleZ, 0);
    lamp1Root.update();
    lamp2Root = createLamp2(m);
    lamp2Root.update();

  //For the first lamp (CREATE NEW CLASS FOR IT):
  }

  //private SGNode createLamp1(Mat4 m, int uArmAngle, int lArmAngle, int headAngle) {
  private SGNode createLamp1(Mat4 m) {//, int lArmAngleZ, lArmAngleY, uArmAngleZ, headAngleZ)
    float baseHeight = 0.35f;
    float baseScale = 1.5f;
    float jointScale = 0.75f;
    float headHeight = 0.5f;
    float headWidth = 0.75f;
    float headDepth = 1.5f;
    float uArmHeight = 2.5f;
    float uArmScale = 0.35f;
    float lArmHeight = 2.5f;
    float lArmScale = 0.35f;

    lamp1Root = new NameNode("lamp-branch-structure");
    //translateLamp = new TransformNode("lamp 1 transform",Mat4Transform.translate(xPosition,0,0));
    lArmTransform = new TransformNode("lArm 1 transform",Mat4Transform.translate(xPosition,0,0));
    // rotateUpper = new TransformNode("Rotate Upper Lamp1",Mat4Transform.translate(xPosition,0,0));
    // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.translate(xPosition,0,0));
    // rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));


    rotateHead = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));
    
    
    //branchRoot = new NameNode("sphere-branch-structure");

                                                                                                //Prev (xPosition, 0, 0)
    rotateLamp = new TransformNode("lamp 1 translate",Mat4Transform.translate(xPosition,0,0));
    lamp1MoveTranslate = new TransformNode("lamp 1 move translate",Mat4Transform.translate(-5f,0,0));


    NameNode base = new NameNode("base");
        m = new Mat4(1);

        //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(30));
        //TransformNode rotateLowerZ = new TransformNode("lower lamp1 rotate", m);
        rotateLowerZ = new TransformNode("lower lamp1 rotate Z", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        rotateLowerY = new TransformNode("lower lamp1 rotate Y", m);
        // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.rotateAroundZ(0));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(baseScale,baseHeight,baseScale));
        //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
        m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
        TransformNode baseTransform = new TransformNode("base transform", m);
          ModelNode baseShape = new ModelNode("Cube(base)", cube);
        //TransformNode baseRotate = new TransformNode("base rotate", m);

    //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

    NameNode lArm = new NameNode("lower arm"); 

        m = new Mat4(1);
        float height = (baseHeight+(lArmHeight/2.0f))-0.5f;
        //float lArmFloorLen = (float)Math.sqrt((height1*height1) - (lArmHeight*lArmHeight));
        m = Mat4.multiply(m, Mat4Transform.translate(0f, height, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));
        m = Mat4.multiply(m, Mat4Transform.scale(lArmScale,lArmHeight,lArmScale));

        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
        TransformNode lArmTransform = new TransformNode("lower arm transform", m);
          ModelNode lArmShape = new ModelNode("cube(base)", cube3);

//Translate to origin, scale, etc.
//When scaling something, always refer it to f.
//Rotate should be in spearate node (if it affects nodes underneath)

//Define a translate with respect to the origin of the node underneath it 
//(i.e:(0,0,0) is the node underneath), so you just have to translate by the 
//length of the current object you're defining.

    //rotateUpper = new TransformNode("Rotate Upper", Mat4Transform.translate(xPosition,0,0));

    NameNode joint = new NameNode("joint");
        //Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
        //leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(this.upperAngle));
        //System.out.println("!!!!");
        //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        rotateUpper = new TransformNode("upper arm rotate", m); //Mat4Transform.rotateAroundZ(-30));
        //m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-30));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(jointScale,jointScale,jointScale));
        height = (baseHeight+lArmHeight+(float)(jointScale/2));
        m = Mat4.multiply(m, Mat4Transform.translate(0f, height, 0f)); // -9, 2.85f, 0));
        TransformNode jointTransform = new TransformNode("joint transform", m);
          //TransformNode jointScale = new TransformNode("joint transform", m); 
            ModelNode jointShape = new ModelNode("Sphere(joint)", jointSphere);
        

    NameNode uArm = new NameNode("upper arm"); 
        m = new Mat4(1);
        float rotationUpperXOffset = (uArmHeight * (float)Math.sin(this.upperAngle));
        //System.out.println("@@@ " + rotationUpperXOffset + " @@@ " + (float)Math.sin(this.upperAngle));
        m = Mat4.multiply(m, Mat4Transform.translate(1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));//(lArmHeight+(uArmHeight/2)), 0f));
        uArmTranslate = new TransformNode("upper arm translate", m);

        m = new Mat4(1);
        //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(uArmHeight/2), 0f)); //-7f, -0.15f, 0f));
        m = Mat4.multiply(m, Mat4Transform.scale(uArmScale,uArmHeight,uArmScale));
        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        TransformNode uArmTransform = new TransformNode("upper arm transform", m);
          ModelNode uArmShape = new ModelNode("cube(base)", cube2);
      
    NameNode head = new NameNode("head");
        m = new Mat4(1);
      // Mat4 m = Mat4Transform.scale(headDepth,headHeight,headWidth);
        //headHeight = baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f)+2.5f;
        m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)((uArmHeight/2)), 0f)); //-3.5f,(baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f))+2.5f,0));
        TransformNode translateHead = new TransformNode("head translate", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(20)); //(baseHeight+lArmHeight+(jointScale/2)-1)
        rotateHead = new TransformNode("head rotate", m);
        //m = new Mat4(1);

        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.scale(headDepth,headHeight,headWidth));
        //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(headHeight/2), 0f)); 
        TransformNode headTransform = new TransformNode("head transform", m);
          ModelNode headShape = new ModelNode("Cube(head)", cube4);



    lamp1Root.addChild(lamp1MoveTranslate);
      //Do I need lamp1MoveTranslate here?
      lamp1MoveTranslate.addChild(rotateLamp);
        rotateLamp.addChild(base);
          base.addChild(baseTransform);
          baseTransform.addChild(baseShape);
          base.addChild(rotateLowerY);
            rotateLowerY.addChild(rotateLowerZ);
              rotateLowerZ.addChild(lArm); //Thus should rotate the lower arm, the lower arm doesn't need a rotate itself?
                lArm.addChild(lArmTransform);
                lArmTransform.addChild(lArmShape);
                //lArm.addChild(jointRotate);
                lArm.addChild(joint);
                  joint.addChild(jointTransform);
                    jointTransform.addChild(jointShape);
                  joint.addChild(uArmTranslate);
                    uArmTranslate.addChild(rotateUpper);
                      rotateUpper.addChild(uArm);
                        uArm.addChild(uArmTransform);
                          uArmTransform.addChild(uArmShape);
                        uArm.addChild(translateHead);
                          translateHead.addChild(rotateHead);
                            rotateHead.addChild(head);
                              head.addChild(headTransform);
                                headTransform.addChild(headShape);


        // base.addChild(lArm);
        //   lArm.addChild(lArmTransform);
        //   lArmTransform.addChild(lArmShape);
        //   lArm.addChild(joint);
        //     joint.addChild(jointTransform);
        //     jointTransform.addChild(jointShape);
        //     joint.addChild(uArm);
        //       uArm.addChild(uArmTransform);
        //       uArmTransform.addChild(uArmShape);
        //       uArm.addChild(head);
        //         head.addChild(headTransform);
        //         headTransform.addChild(headShape);
                // head.addChild(hLight1);
                //   hLight1Transform.addChild(hLight1Shape);

    return lamp1Root;
    

  }


  //For the second lamp (CREATE NEW CLASS FOR IT):

  private SGNode createLamp2(Mat4 m) {
    // \/\/ REPEATED for the second lamp
    float baseHeight = 0.35f;
    float baseScale = 1.5f;
    float jointScale = 0.75f;
    float headHeight = 0.5f;
    float headWidth = 0.75f;
    float headDepth = 1.5f;
    float uArmHeight = 2.5f;
    float uArmScale = 0.35f;
    float lArmHeight = 2.5f;
    float lArmScale = 0.35f;
    // --------------------------------

    lamp2Root = new NameNode("lamp-branch-structure");
    translateLamp2 = new TransformNode("lamp 2 transform",Mat4Transform.translate(-xPosition,0,0));
    
    
    //branchRoot = new NameNode("sphere-branch-structure");

    TransformNode lamp2Translate = new TransformNode("lamp 2 transform",Mat4Transform.translate(-xPosition,0,0));


    NameNode base2 = new NameNode("base2");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(baseScale,baseHeight,baseScale));
        m = Mat4.multiply(m, Mat4Transform.translate(3.8f,0.5f,-0));
        //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
        TransformNode base2Transform = new TransformNode("base2 transform", m);
          ModelNode base2Shape = new ModelNode("Cube(base)", cube);

    NameNode uArm2 = new NameNode("upper arm 2"); 
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(30));
        m = Mat4.multiply(m, Mat4Transform.translate(7f, (baseHeight+(uArmHeight/2)+jointScale)-2.5f,-0));
        m = Mat4.multiply(m, Mat4Transform.scale(uArmScale,uArmHeight,uArmScale));
        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        TransformNode uArm2Transform = new TransformNode("upper arm 2 transform", m);
          ModelNode uArm2Shape = new ModelNode("cube(base)", cube2);

    NameNode joint2 = new NameNode("joint2");
        //Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
        //leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(jointScale,jointScale,jointScale));
        m = Mat4.multiply(m, Mat4Transform.translate(9,(baseHeight+lArmHeight),0));
      TransformNode joint2Transform = new TransformNode("joint 2 transform", m);
        //TransformNode jointScale = new TransformNode("joint transform", m); 
          ModelNode joint2Shape = new ModelNode("Sphere(joint)", jointSphere);

    NameNode lArm2 = new NameNode("lower arm 2"); 
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-30)); //(baseHeight+lArmHeight+(jointScale/2)-1)
        // m = Mat4.multiply(m, Mat4Transform.translate(0.5f,(baseHeight+(lArmHeight/2.0f)),0));
        // m = Mat4.multiply(m, Mat4Transform.scale(lArmScale,lArmHeight,lArmScale));
        m = Mat4.multiply(m, Mat4Transform.translate(4.9f,(baseHeight+(lArmHeight/2.0f)+2.25f),-0));
        m = Mat4.multiply(m, Mat4Transform.scale(lArmScale,lArmHeight,lArmScale));
        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
        TransformNode lArm2Transform = new TransformNode("lower arm 2 transform", m);
          ModelNode lArm2Shape = new ModelNode("cube(base)", cube3);

      
    NameNode head2 = new NameNode("head2");
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.scale(headDepth,headHeight,headWidth));
        m = Mat4.multiply(m, Mat4Transform.translate(3.5f,(baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f))+2.5f,-0));
      // Mat4 m = Mat4Transform.scale(headDepth,headHeight,headWidth);
        TransformNode head2Transform = new TransformNode("head 2 transform", m);
          ModelNode head2Shape = new ModelNode("Cube(head)", cube4);

    // NameNode hLight2 = new NameNode("head light 2");
    // //light1.setPosition(getLightPosition());  // changing light position each frame
    //     m = new Mat4(1);
    //     m = Mat4.multiply(m, Mat4Transform.translate(4.5f, 4.4f, -0));
    //     TransformNode hLight2Transform = new TransformNode("head light 2 transform", m);
    //       LightNode hLight2Shape = new LightNode("Light(light2)", light2);

    lamp2Root.addChild(translateLamp2);
      translateLamp2.addChild(base2);
        base2.addChild(base2Transform);
          base2Transform.addChild(base2Shape);
        base2.addChild(lArm2);
          lArm2.addChild(lArm2Transform);
            lArm2Transform.addChild(lArm2Shape);
          lArm2.addChild(joint2);
            joint2.addChild(joint2Transform);
              joint2Transform.addChild(joint2Shape);
            joint2.addChild(uArm2);
              uArm2.addChild(uArm2Transform);
                uArm2Transform.addChild(uArm2Shape);
              uArm2.addChild(head2);
                head2.addChild(head2Transform);
                  head2Transform.addChild(head2Shape);
                // head2.addChild(hLight2);
                //   hLight2Transform.addChild(hLight2Shape);
    return lamp2Root;
  }

  

 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);


    // //-------- Animated BG ---------
    // double elapsedTime1 = getSeconds() - startTime;
    // shader.use(gl);
    
    // double t = elapsedTime1*0.1;
    // float offsetX = (float)(t - Math.floor(t));
    // float offsetY = 0.0f;
    // shader.setFloat(gl, "offset", offsetX, offsetY);
    
    // shader.setInt(gl, "first_texture", 0);
    // shader.setInt(gl, "second_texture", 1);

    // gl.glActiveTexture(GL.GL_TEXTURE8);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId8[0]);
    // gl.glActiveTexture(GL.GL_TEXTURE9);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId9[0]);

    // gl.glBindVertexArray(vertexArrayId[0]);
    // gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    // gl.glBindVertexArray(0);

    // gl.glActiveTexture(GL.GL_TEXTURE8);
    // //--------NOTE - HAVEN'T CHANGED VS OR FS YET---------


    Mat4 m = Mat4Transform.scale(1.5f,2.6f,1.5f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    // vector = new Vec3 (-4.5f, 4.4f, 0);
    // vector2 = new Vec3 (4.5f, 4.4f, 0);

    //Used for creating an overlayed animated background
    // double elapsedTime = getSeconds() - startTime;
    // shader.use(gl);
    // double t = elapsedTime*0.1;
    // float offsetX = (float)(t - Math.floor(t));
    // float offsetY = 0.0f;
    // shader.setFloat(gl, "offset", offsetX, offsetY);


    light1.setPosition(getLightPosition(1));  // changing light position each frame
    light1.render(gl);

    light2.setPosition(getLightPosition(2));  // changing light position each frame
    light2.render(gl);

    ttFloor.setModelMatrix(getMforTT1());       // change transform
    ttFloor.render(gl);
    // ttWindow.setModelMatrix(getMforTT2());       // change transform
    // ttWindow.render(gl);
    ttWall.setModelMatrix(getMforTT3());       // change transform
    ttWall.render(gl);
    ttWall.setModelMatrix(getMforTT4());       // change transform
    ttWall.render(gl);
    ttBackground.setModelMatrix(getMforTT5());       // change transform
    ttBackground.render(gl, true); //, True);

    cube.setModelMatrix(getMforTableTop());     // change transform
    cube.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg1());     // change transform
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg2());     // change transform
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg3());     // change transform
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg4());     // change transform
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforEggStand());     // change transform
    tableLeg.render(gl);


    if (animation) {
      double elapsedTime = getSeconds()-startTime;
    }

    //robot.render(gl);
    updateBranches();
    branchRoot.draw(gl);
    //lArm.setModelMatrix(getMforTableLeg3());

    //int lArmAngle = 0;
    // if (this.frame < 50) {
    //       lArmAngle = 30;
    //       this.frame+=1;
    //     } else if (this.frame < 100) {
    //       lArmAngle = -30;
    //       this.frame+=1;
    //     } else {
    //       this.frame = 0;
    //     }
    

    lamp1Root.draw(gl);
    //rotateUpper.draw(gl);
    lamp2Root.draw(gl);
    //System.out.println("***");


//----------------//----------------//----------------//----------------//----------------
    // double elapsedTime = getSeconds() - startTime;
  
    // //shader.use(gl);

    // double t = elapsedTime*0.1;
    // float offsetX = (float)(t - Math.floor(t));
    // float offsetY = 0.0f;
    // shader.setFloat(gl, "offset", offsetX, offsetY);
    
    // shader.setInt(gl, "first_texture", 0);
    // shader.setInt(gl, "second_texture", 1);

    // gl.glActiveTexture(GL.GL_TEXTURE0);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    // gl.glActiveTexture(GL.GL_TEXTURE1);
    // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
  
    // gl.glBindVertexArray(vertexArrayId[0]);
    // gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    // gl.glBindVertexArray(0);
    
    // gl.glActiveTexture(GL.GL_TEXTURE0);
  }

  private void updateBranches() {
    //currentTime = getSeconds();
    double elapsedTime = getSeconds()-startTime; //currentTime; //startTime;
    //float currentTimer = 0.0f;
    //float totalTimer = 5.0f;
    // if ((elapsedTime > 2) && (elapsedTime < 4)) {
    //   rotateAllAngle = rotateAllAngleStart*(float)(Math.sin(8*elapsedTime)); //Math.round((rotateAllAngleStart*(float)Math.sin(elapsedTime))/2);
    // } else if (elapsedTime >= 4 ) {
    //   rotateAllAngle = 0;

    // }
    if (Math.sin(elapsedTime*2) > 0.6) {
      //double elapsedTime2 = getSeconds()-startTime;
      rotateAllAngle = rotateAllAngleStart*(float)(Math.sin(10*elapsedTime)); //Math.round((rotateAllAngleStart*(float)Math.sin(elapsedTime))/2);
    } else {
      rotateAllAngle = 0;
    }
    //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));

    //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
    //rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle));

    if (Math.sin(elapsedTime*5) > 0) {
      translateEgg.setTransform(Mat4Transform.translate(xPosition,(3.0f+(float)Math.sin(5*elapsedTime)),0));
      rotateYAngle = rotateAllAngleStart*(float)(Math.sin(10*elapsedTime));
      
    } else {
      translateEgg.setTransform(Mat4Transform.translate(xPosition, 3, 0));
      rotateYAngle = 0;
    }
    
    //rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle));
    //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
    rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle*2));

    // rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle));

    //translateEgg = new TransformNode("translate("+xPosition+",0,0)", Mat4Transform.translate(xPosition,3,0));
    //rotateEgg = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));

  //translateEgg = new TransformNode("translate("+xPosition+",0,0)", Mat4Transform.translate(xPosition,3,0));
    //translateEgg.setTransform()
    //rotateUpperAngle = rotateUpperAngleStart*(float)Math.sin(elapsedTime*0.7f);
    
    //rotateUpper.setTransform(Mat4Transform.rotateAroundZ(rotateUpperAngle));
    branchRoot.update(); // IMPORTANT – the scene graph has changed
  }

  /*cube.setModelMatrix(getMforCube());     // change transform
    cube.render(gl);
    tt.setModelMatrix(getMforTT1());       // change transform
    tt.render(gl);
    tt.setModelMatrix(getMforTT2());       // change transform
    tt.render(gl);
    tt.setModelMatrix(getMforTT3());       // change transform
    tt.render(gl);
    tt.setModelMatrix(getMforTT4());       // change transform
    tt.render(gl);*/
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition(int opt) {
    // double elapsedTime = getSeconds()-startTime;
    // float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    // float y = 2.7f;
    // float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));

    if (opt == 1) {
      return new Vec3 (-4.5f, 4.4f, 0);
    } else { //if (opt == 2) {
      return new Vec3 (4.5f, 4.4f, 0);
    } 
    // else  (opt == 3) {
    //   return new Vec3 (-4.5f, 4.4f, 0);
    // }


    //return new Vec3 (-4.5f, 4.4f, 0);
    //return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }



  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTT1() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }
  
  // // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  // private Mat4 getMforTT2() {
  //   float size = 16f;
  //   Mat4 modelMatrix = new Mat4(1);
  //   modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
  //   modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
  //   modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
  //   return modelMatrix;
  // }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTT3() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTT4() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTT5() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size*4, 5f, size*3), modelMatrix); //3f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-1.0f,size*0.7f,-size*1.5f), modelMatrix);
    return modelMatrix;
  }
  

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTableTop() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f, 5f, 0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(5f,0.5f,5f), modelMatrix);
    return modelMatrix;
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTableLeg1() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-4.5f, 0.5f, -4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }
  
  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTableLeg2() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(4.5f, 0.5f, 4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTableLeg3() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(4.5f, 0.5f, -4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforTableLeg4() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-4.5f, 0.5f, 4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getMforEggStand() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f, 6f, 0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2.5f,0.5f,2.5f), modelMatrix);
    return modelMatrix;
  }

  public void lamp1Position1() {  
  // this.uArmAngleY = 0;
  // this.uArmAngleZ = -30;
  // this.uArmTranslateX = -7;
  // this.uArmTranslateY = -0.15f;
  // this.uArmTranslateZ = 0;

  // this.headAngleZ = 0;

  // //this.xPosition = 0;
  // this.lArmAngleZ = 30;
  // this.lArmAngleY = 0;
  // this.lArmTranslateZ = 0;
  // this.lArmTranslateX = -4.9f;

  // this.jointTranslateX = -9f;
  // this.jointTranslateY = 2.85f;
  // this.jointTranslateZ = 0;
  //  m = new Mat4(1);
  //   m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(this.upperAngle));
  //       //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
  //       TransformNode rotateUpper = new TransformNode("upper arm rotate", m); //Mat4Transform.rotateAroundZ(-30));
  // this.upperAngle = 0;

  //m = new Mat4(1);
        // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(this.upperAngle));
        // //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        // TransformNode rotateUpper = new TransformNode("upper arm rotate", m); //Mat4Transform.rotateAroundZ(-30));

  //System.out.println("&&&&&");

  rotateLamp.setTransform(Mat4Transform.rotateAroundY(0));
  rotateLamp.update();

  lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
  lamp1MoveTranslate.update();

  uArmTranslate.setTransform(Mat4Transform.translate(1.5f, 3.25f, 0f));
  uArmTranslate.update();

  rotateHead.setTransform(Mat4Transform.rotateAroundZ(20));
  rotateHead.update();

  rotateUpper.setTransform(Mat4Transform.rotateAroundZ(this.upperAngle));
  //rotateUpper.setTransform(Mat4Transform.translate(0, 0, 0.5f));
  rotateUpper.update();

  //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
  rotateLowerY.setTransform(Mat4Transform.rotateAroundY(0));
  rotateLowerY.update();

  //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
  rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(30));
  rotateLowerZ.update();

  lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));

  // System.out.println("Here");
  //uArm.update();
  //head.update();
  //rotateHead.update();

  //translateLamp.setTransform(Mat4Transform.rotateAroundY(0));
  // lArmRotate.setTransform(Mat4Transform.rotateAroundZ(0));
  //translateLamp.update();
  // lArmRotate.update();
  }

  public void lamp1Position2() {
    rotateLamp.setTransform(Mat4Transform.rotateAroundY(0));
    rotateLamp.update();

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
    lamp1MoveTranslate.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateLowerZ.update();

    //rotateUpper.update();

    // //uArmHeight = 2.5f
    // //rotateUpper.setTransform(Mat4Transform.translate(0f, (float)(2.5f*Math.sin(30)), -1f));
    // m = new Mat4(1);
    // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(30));
    uArmTranslate.setTransform(Mat4Transform.translate(-0.3f, (float)(3.5f), 0f));
    uArmTranslate.update();

    rotateUpper.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateUpper.update();


    // rotateHead.setTransform(Mat4Transform.rotateAroundZ(10));
    // rotateHead.update();

    rotateLowerY.setTransform(Mat4Transform.rotateAroundY(90));
    rotateLowerY.update();

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));

    // rotateUpper.setTransform(Mat4Transform.rotateAroundZ(-30));
    // //rotateUpper.setTransform(Mat4Transform.translate(0, 0, 0.5f));
    // rotateUpper.update();

    // //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    // rotateLowerY.setTransform(Mat4Transform.rotateAroundY(90));
    // rotateLowerY.update();


    // this.uArmAngleY = -90;
    // // this.uArmAngleZ = -30;
    // this.uArmTranslateX = -6.6f;
    // this.uArmTranslateY = -0.17f;
    // this.uArmTranslateZ = -0.6f;


    // this.lArmAngleY = -90;
    // this.lArmAngleZ = 40;
    // this.lArmTranslateZ = -0.6f;
    // this.lArmTranslateX = -4.5f;

    // this.headAngleZ = 0;

    // this.jointTranslateX = -7.6f;
    // this.jointTranslateY = 2.85f;
    // this.jointTranslateZ = -2.1f;
    //this.lArmTranslateZ = 0;
    //lArmAngleZ, lArmAngleY, uArmAngleZ, headZ, 
    //this.lArmTranslateZ -= 1;
    //this.lArmTranslateX -= 3;

    //translateLamp.addChild(base);
    
    //System.out.println("*");

    rotateHead.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateHead.update();

    // translateLamp.setTransform(Mat4Transform.rotateAroundY(-90));
    // // lArmRotate.setTransform(Mat4Transform.rotateAroundZ(-90));
    // translateLamp.update();
    // // lArmRotate.update();

  

  }

  public void lamp1Position3() {
    // // translateLamp.setTransform(Mat4Transform.rotateAroundY(90));
    // // // lArmRotate.setTransform(Mat4Transform.rotateAroundZ(90));
    // // translateLamp.update();
    // // // lArmRotate.update();

    // //--------New transform--------
    // rotateHead.setTransform(Mat4Transform.rotateAroundZ(60));
    // rotateHead.update();


    // //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    // rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(50));
    // rotateLowerZ.update();

    // // //rotateUpper.update();

    // // // //uArmHeight = 2.5f
    // // // //rotateUpper.setTransform(Mat4Transform.translate(0f, (float)(2.5f*Math.sin(30)), -1f));
    // // // m = new Mat4(1);
    // // // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(30));
    // // uArmTranslate.setTransform(Mat4Transform.translate(-0.3f, (float)(2.5f), -3f));
    // // uArmTranslate.update();

    // // rotateUpper.setTransform(Mat4Transform.rotateAroundZ(-40));
    // // rotateUpper.update();


    // rotateHead.setTransform(Mat4Transform.rotateAroundZ(70));
    // rotateHead.update();

    // rotateLowerY.setTransform(Mat4Transform.rotateAroundY(65));
    // rotateLowerY.update();

    uArmTranslate.setTransform(Mat4Transform.translate(1.5f, 3.25f, 0f));
    uArmTranslate.update();

    rotateHead.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateHead.update();

    rotateUpper.setTransform(Mat4Transform.rotateAroundZ(this.upperAngle));
    //rotateUpper.setTransform(Mat4Transform.translate(0, 0, 0.5f));
    rotateUpper.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerY.setTransform(Mat4Transform.rotateAroundY(0));
    rotateLowerY.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ.update();

    rotateLamp.setTransform(Mat4Transform.rotateAroundY(-45));
    rotateLamp.update();

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, -3f));
    lamp1MoveTranslate.update();
  }

  public void lamp2Position1() {
    translateLamp2.setTransform(Mat4Transform.rotateAroundY(0));
    //lArm2Transform.setTransform(Mat4Transform.rotateAroundZ(0));
    translateLamp2.update();
    //lArm2Transform.update();
  }

  public void lamp2Position2() {
    translateLamp2.setTransform(Mat4Transform.rotateAroundY(45));
    //lArm2Transform.setTransform(Mat4Transform.rotateAroundZ(0));
    translateLamp2.update();
    //lArm2Transform.update();
  }

  public void lamp2Position3() {
    translateLamp2.setTransform(Mat4Transform.rotateAroundY(-45));
    //lArm2Transform.setTransform(Mat4Transform.rotateAroundZ(0));
    translateLamp2.update();
    //lArm2Transform.update();
    
  }

  // private Mat4 getMforLampArmLower() {
  //   Mat4 modelMatrix = new Mat4(1);
  //   modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(45), modelMatrix);
  //   return modelMatrix;
  // }


  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}