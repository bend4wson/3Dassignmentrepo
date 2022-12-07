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

    //blend
    gl.glEnable(GL.GL_BLEND);
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
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

    //------------------COMMENTED LIGHTING--------------------------
    lampLight1.dispose(gl);
    lampLight2.dispose(gl);
    overheadLight1.dispose(gl);
    overheadLight2.dispose(gl);
    //--------------------------------------------------------------
    // light.dispose(gl);


    // light3.dispose(gl);
    // light4.dispose(gl);
    ttWall.dispose(gl);
    ttWindow.dispose(gl);
    // ttLowWall.dispose(gl);
    ttFloor.dispose(gl);
    ttBackground.dispose(gl);
    ttCeiling.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
    cube3.dispose(gl);
    cube4.dispose(gl);
    // cube5.dispose(gl);
    cube6.dispose(gl);
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
  //light1/light2 should be part of scene graph for lamps
  private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere1, jointSphere2, eyeSphere, tableLeg, ttWall, ttLowWall, ttFloor, ttWindow, ttCeiling, ttBackground; //ttLowWall;
  //private int frame = 1;

  private SGNode branchRoot, lamp1Root, lamp2Root;
  private TransformNode translateEgg, rotateEggZ, rotateEggY;
  private TransformNode jointTransform, lamp1MoveTranslate, rotateLamp, lArmTransform, rotateLowerZ, rotateLowerY, rotateUpper, uArmTranslate, rotateHead, rotateLampLight1, lampLight1Transform, lampLight1Translate; //, uArmTranslate;//, lArm;//Transform;
  private TransformNode lamp2MoveTranslate, rotateLamp2, lArm2Transform, rotateLowerZ2, rotateLowerY2, rotateUpper2, uArm2Translate, rotateHead2;

  // private Light lampLight1, light2, light3, light4;

  //---------------------------COMMENTED LIGHTING------------------------------
  private PointLight overheadLight1, overheadLight2;
  private SpotLight lampLight1, lampLight2;
  //-------------------------------------------------------------------------
  private Light light;

  private static final boolean DISPLAY_SHADERS = false;
  private Shader shader;
  //Necessary? \/ \/ \/ 


  // private float lArmAngleZ = 30;
  // private float lArmAngleY = 0;
  // private float headAngleZ = 0;
  private float xPosition = 0;

  private float targetLampRot1 = 0f;
  private float currentLampRot1 = 0f;
  private float targetLowerZ1 = 30f; 
  private float currentLowerZ1 = 30f;
  private float targetLowerY1 = 0f; 
  private float currentLowerY1 = 0f;
  private float targetUpper1 = -60f; 
  private float currentUpper1 = -60f;
  private float targetHead1 = 20f;
  private float currentHead1 = 20f;
  
  private float targetLampRot2 = 0f;
  private float currentLampRot2 = 0f;
  private float targetLowerZ2 = -30f;
  private float currentLowerZ2 = -30f;
  private float targetLowerY2 = 0f;
  private float currentLowerY2 = 0f;
  private float targetUpper2 = 60f;
  private float currentUpper2 = 60f;
  private float targetHead2 = -20f;
  private float currentHead2 = -20f;


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
    Shader shader = new Shader(gl, "vertexShaders/vs_tt_05.txt", "fragmentShaders/fs_tt_05.txt");
    //COMMENTED LIGHTING \/\/
    Shader shaderStatic = new Shader(gl, "vertexShaders/vs_tt_05_static.txt", "fragmentShaders/fs_tt_05.txt"); //"fragmentShaders/fs_multiple_casters.txt"); 
    Shader shaderWindow = new Shader(gl, "vertexShaders/vs_tt_05_static.txt", "fragmentShaders/fs_tt_05_window.txt");

    //int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/woodCeiling3.jpg");
    //int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/ear0xuu2.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud2.jpg");
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/medievalTown.jpg");
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/mar0kuu2_specular.jpg");
    int[] textureId11 = TextureLibrary.loadTexture(gl, "textures/woodWallColours.jpg");
    int[] textureId12 = TextureLibrary.loadTexture(gl, "textures/woodFloor2.jpg");
    int[] textureId13 = TextureLibrary.loadTexture(gl, "textures/woodCeiling3.jpg");
    int[] textureId14 = TextureLibrary.loadTexture(gl, "textures/stainedGlassNew.jpg");
    int[] textureId15 = TextureLibrary.loadTexture(gl, "textures/redEye.jpg");
    int[] textureId16 = TextureLibrary.loadTexture(gl, "textures/slug.jpg");
    int[] textureId17 = TextureLibrary.loadTexture(gl, "textures/dragonScale3.jpg");
    int[] textureId18 = TextureLibrary.loadTexture(gl, "textures/dragonTail.jpg");
    int[] textureId19 = TextureLibrary.loadTexture(gl, "textures/egg.jpg");
    int[] textureId20 = TextureLibrary.loadTexture(gl, "textures/eggSpecular.jpg");


    // light = new Light(gl);
    // light.setCamera(camera);

    //-----------------------COMMENTED LIGHTING----------------------
    //lampLight1.setCamera(camera);
    lampLight1 = new SpotLight(gl, camera);
    // lampLight1.setCamera(camera);
    lampLight2 = new SpotLight(gl, camera);
    // light2.setCamera(camera);
    overheadLight1 = new PointLight(gl, camera);
    // light3.setCamera(camera);
    overheadLight2 = new PointLight(gl, camera);
    // light4.setCamera(camera);
    //--------------------------------------------------------------


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
    //lightShader = new Shader(gl, "vertexShaders/vs_light_01.txt", "fragmentShaders/fs_light_01.txt");
    //lampLight1 = new Model(gl, camera, light, lightShader, material, modelMatrix, mesh, textureId4, textureId4);




    //--------------------------COMMENTED LIGHTING--------------------------------
    List<Light> lightArray = new ArrayList<Light>();
    lightArray.add(lampLight1);
    lightArray.add(lampLight2);
    lightArray.add(overheadLight1);
    lightArray.add(overheadLight2);
    // ArrayList<Light> lightArray = {lampLight1, lampLight2, overheadLight1, overheadLight2};
    //----------------------------------------------------------------------------
    //NOTE - Also took out lightArray everywhere in the Models below and replaced with Light \/\/

    Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    ttFloor = new Model(gl, camera, lightArray, shaderStatic, material, modelMatrix, mesh, textureId12);
    ttWindow = new Model(gl, camera, lightArray, shaderWindow, material, modelMatrix, mesh, textureId14);
    ttWall = new Model(gl, camera, lightArray, shaderStatic, material, modelMatrix, mesh, textureId11);
    ttLowWall = new Model(gl, camera, lightArray, shaderStatic, material, modelMatrix, mesh, textureId11);
    // ttLowWall = new Model(gl, camera, lampLight1, shaderStatic, material, modelMatrix, mesh, textureId11);
    ttBackground = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId9);
    ttCeiling = new Model(gl, camera, lightArray, shaderStatic, material, modelMatrix, mesh, textureId13);

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4, 4, 4), Mat4Transform.translate(0, 0.5f, 0));
    cube = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId4, textureId4);
    cube2 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId16, textureId16);
    cube3 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId17, textureId17);
    cube4 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId18, textureId18);
    // cube4 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId16, textureId16);
    // cube5 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId16, textureId16);
    cube6 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId3, textureId3);

    tableLeg = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId13, textureId13);

    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId19, textureId20);
    jointSphere1 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId16, textureId16);
    jointSphere2 = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId18, textureId18);
    eyeSphere = new Model(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId15, textureId15);

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

    // lamp1Root = (m, -30);
    // lamp1Root.update();
    // lamp2Root = createLamp2(m);
    // lamp2Root.update();
  

    lamp1Root = createLamp1();//m); //, -30, (int)this.lArmAngleZ, 0);
    lamp1Root.update();
    lamp2Root = createLamp2();
    lamp2Root.update();

  //For the first lamp (CREATE NEW CLASS FOR IT):
  }

  //private SGNode createLamp1(Mat4 m, int uArmAngle, int lArmAngle, int headAngle) {
  //private SGNode createLamp1(Mat4 m) {//, int lArmAngleZ, lArmAngleY, uArmAngleZ, headAngleZ)
  private SGNode createLamp1() {//, int lArmAngleZ, lArmAngleY, uArmAngleZ, headAngleZ)
      //System.out.println("***");
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
      float lampLight1Scale = 0.25f;
      float headLightAngle = 20f;

      float lowerYAngle = 0f;
      float lowerZAngle = 30f;
      float upperAngle = -60f;
      float headAngle = 20f;
      float eyeScale = 0.3f;
      Vec3 lampMoveTranslate = new Vec3(-5f, 0f, 0f);

      SGNode lamp1Root = new NameNode("lamp-branch-structure");
      //translateLamp = new TransformNode("lamp 1 transform",Mat4Transform.translate(xPosition,0,0));
      lArmTransform = new TransformNode("lArm 1 transform",Mat4Transform.translate(xPosition,0,0));
      // rotateUpper = new TransformNode("Rotate Upper Lamp1",Mat4Transform.translate(xPosition,0,0));
      // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.translate(xPosition,0,0));
      rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));


      // rotateHead = new TransformNode("rotateHead",Mat4Transform.translate(xPosition,0,0));
      
      
      //branchRoot = new NameNode("sphere-branch-structure");

                                                                              //Prev (xPosition, 0, 0)
      rotateLamp = new TransformNode("rotateLamp",Mat4Transform.translate(xPosition,0,0));
      lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(lampMoveTranslate));

      NameNode base = new NameNode("base");
          // m = new Mat4(1);
          // m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          // TransformNode lArmTranslate = new TransformNode("lArmTranslate", m);

          
          //m = new Mat4(1);
          Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          TransformNode baseTransform = new TransformNode("baseTransform", m);
          ModelNode baseShape = new ModelNode("baseShape(Cube)", cube2);
          //TransformNode baseRotate = new TransformNode("base rotate", m);

      //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

          rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(this.currentLampRot1));


      NameNode lArm = new NameNode("lArm"); 
          TransformNode lArmPosTransform = new TransformNode("lArmPosTransform", Mat4Transform.translate(0, 0, 0));

          //m = new Mat4(1);
          m = Mat4Transform.rotateAroundZ(this.currentLowerZ1);
          rotateLowerZ = new TransformNode("rotateLowerZ", m);
          //rotateLowerZ = new TransformNode("rotateLowerZ", m);

          //m = new Mat4(1);
          m = Mat4Transform.rotateAroundY(this.currentLowerY1);
          rotateLowerY = new TransformNode("rotateLowerY", m);
          //rotateLowerY = new TransformNode("rotateLowerY", m);


          //m = new Mat4(1);
          m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));

          //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
          TransformNode lArmTransform = new TransformNode("lArmTransform", m);
          ModelNode lArmShape = new ModelNode("lArmShape(Cube)", cube2);

          TransformNode translateTopLArm = new TransformNode("translateTopLArm", Mat4Transform.translate(0f, (float)(baseHeight+lArmHeight), 0f));


      NameNode joint = new NameNode("joint");
          m = Mat4Transform.scale(jointScale,jointScale,jointScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));        
          TransformNode jointTransform = new TransformNode("jointTransform", m);
          ModelNode jointShape = new ModelNode("jointShape", jointSphere1);
          

      NameNode uArm = new NameNode("uArm"); 
          rotateUpper = new TransformNode("rotateUpper (" + this.currentUpper1 + ")", Mat4Transform.rotateAroundZ(this.currentUpper1)); //Mat4Transform.rotateAroundZ(-30));
          m = Mat4Transform.scale(uArmScale,uArmHeight,uArmScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f)); //0f, 0.5f, 0f));
          //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
          TransformNode uArmTransform = new TransformNode("uArmTransform", m);
          ModelNode uArmShape = new ModelNode("uArmShape", cube2);

          TransformNode translateTopUArm = new TransformNode("translateTopUArm", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
      
      NameNode head = new NameNode("head");            
          rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(this.currentHead1));
          //rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));

          //m = new Mat4(1);
          m = Mat4Transform.scale(headDepth,headHeight,headWidth);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
          TransformNode headTransform = new TransformNode("headTransform", m);
          ModelNode headShape = new ModelNode("headShape", cube2);

          TransformNode translateTopHead = new TransformNode("translateTopHead", Mat4Transform.translate(0f, (float)(headHeight), 0f));

      NameNode eye1 = new NameNode("eye1");
          m = Mat4Transform.scale(eyeScale,eyeScale,eyeScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, -0.3f, 0.6f));        
          TransformNode eye1Transform = new TransformNode("eye1Transform", m);
            ModelNode eye1Shape = new ModelNode("eye1Shape", eyeSphere);

      NameNode eye2 = new NameNode("eye2");
          m = Mat4Transform.scale(eyeScale,eyeScale,eyeScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, -0.3f, -0.6f));        
          TransformNode eye2Transform = new TransformNode("eye2Transform", m);
            ModelNode eye2Shape = new ModelNode("eye2Shape", eyeSphere);


      lamp1Root.addChild(lamp1MoveTranslate);
        //Do I need lamp1MoveTranslate here?
        lamp1MoveTranslate.addChild(base); //rotateLamp);
            //rotateLamp.addChild(base);
            base.addChild(baseTransform);
                baseTransform.addChild(baseShape);
            base.addChild(rotateLamp); //lArmTranslate);
                rotateLamp.addChild(lArm);
                    lArm.addChild(lArmPosTransform);
                    // \/ Translates to origin for the rotation
                    lArmPosTransform.addChild(rotateLowerY); //translateTopBase);
                    //translateTopBase.addChild(rotateLowerY);
                        rotateLowerY.addChild(rotateLowerZ);
                        rotateLowerZ.addChild(lArmTransform);
                            lArmTransform.addChild(lArmShape);
                        rotateLowerZ.addChild(translateTopLArm);
                            translateTopLArm.addChild(joint);
                            joint.addChild(jointTransform);
                                jointTransform.addChild(jointShape);
                            joint.addChild(uArm);//translateMiddleJoint);
                                //translateMiddleJoint.addChild(uArm);
                                uArm.addChild(rotateUpper);
                                    rotateUpper.addChild(uArmTransform);
                                    uArmTransform.addChild(uArmShape);
                                    rotateUpper.addChild(translateTopUArm);
                                    translateTopUArm.addChild(head);
                                        head.addChild(rotateHead);
                                        rotateHead.addChild(headTransform);
                                            headTransform.addChild(headShape);
                                            rotateHead.addChild(translateTopHead);
                                              translateTopHead.addChild(eye1);
                                                eye1.addChild(eye1Transform);
                                                  eye1Transform.addChild(eye1Shape);
                                              translateTopHead.addChild(eye2);
                                                eye2.addChild(eye2Transform);
                                                  eye2Transform.addChild(eye2Shape);
                                          
      return lamp1Root;

}


//For the second lamp (CREATE NEW CLASS FOR IT):

  private SGNode createLamp2() {
//System.out.println("***");
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
      float lampLight1Scale = 0.25f;
      float headLightAngle = 20f;
      float tailHeight = 1f;
      float tailScale = 0.4f;

      float lowerYAngle = 0f;
      float lowerZAngle = 30f;
      float upperAngle = -60f;
      float headAngle = 20f;
      Vec3 lampMoveTranslate = new Vec3(5f, 0f, 0f);

      SGNode lamp2Root = new NameNode("lamp2-branch-structure");
      //translateLamp = new TransformNode("lamp 1 transform",Mat4Transform.translate(xPosition,0,0));
      lArm2Transform = new TransformNode("lArm 2 transform",Mat4Transform.translate(-xPosition,0,0));
      // rotateUpper = new TransformNode("Rotate Upper Lamp1",Mat4Transform.translate(xPosition,0,0));
      // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.translate(xPosition,0,0));
      rotateHead2 = new TransformNode("Rotate Head 2", Mat4Transform.translate(-xPosition, 0, 0));


      // rotateHead = new TransformNode("rotateHead",Mat4Transform.translate(xPosition,0,0));
      
      
      //branchRoot = new NameNode("sphere-branch-structure");

                                                                              //Prev (xPosition, 0, 0)
      lamp2MoveTranslate = new TransformNode("lamp2MoveTranslate",Mat4Transform.translate(lampMoveTranslate));

      NameNode base2 = new NameNode("base2");
          // m = new Mat4(1);
          // m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          // TransformNode lArmTranslate = new TransformNode("lArmTranslate", m);

          Mat4 m = new Mat4(1);
          //m = Mat4Transform.translate(-xPosition,0,0);
          m = Mat4.multiply(m, Mat4Transform.rotateAroundY(this.currentLampRot2));
          rotateLamp2 = new TransformNode("rotateLamp2",m); //Mat4Transform.translate(-xPosition,0,0));

          
          // m = new Mat4(1);
          m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          TransformNode base2Transform = new TransformNode("baseTransform2", m);
          ModelNode base2Shape = new ModelNode("baseShape2(Cube)", cube4);
          //TransformNode baseRotate = new TransformNode("base rotate", m);

      //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

          // rotateLamp2 = new TransformNode("rotateLamp2", Mat4Transform.rotateAroundY(0));


      NameNode lArm2 = new NameNode("lArm2"); 
          TransformNode lArm2PosTransform = new TransformNode("lArm2PosTransform", Mat4Transform.translate(0, 0, 0));

          //m = new Mat4(1);
          m = Mat4Transform.rotateAroundZ(this.targetLowerZ2);
          rotateLowerZ2 = new TransformNode("rotateLowerZ2", m);
          //rotateLowerZ = new TransformNode("rotateLowerZ", m);

          //m = new Mat4(1);
          m = Mat4Transform.rotateAroundY(this.targetLowerY2);
          rotateLowerY2 = new TransformNode("rotateLowerY2", m);
          //rotateLowerY = new TransformNode("rotateLowerY", m);


          //m = new Mat4(1);
          m = Mat4Transform.scale((float)(lArmScale*1.3),lArmHeight,(float)(lArmScale*1.3));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));

          //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
          TransformNode lArm2Transform = new TransformNode("lArm2Transform", m);
          ModelNode lArm2Shape = new ModelNode("lArm2Shape(Cube)", cube3);

          TransformNode translateTopLArm2 = new TransformNode("translateTopLArm2", Mat4Transform.translate(0, (baseHeight+lArmHeight), 0));


      NameNode joint2 = new NameNode("joint2");
          m = Mat4Transform.scale((float)(jointScale*1.3),(float)(jointScale*1.3),(float)(jointScale*1.4));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));        
          TransformNode joint2Transform = new TransformNode("joint2Transform", m);
          ModelNode joint2Shape = new ModelNode("joint2Shape", jointSphere2);

      NameNode tail = new NameNode("tail");
          m = Mat4Transform.rotateAroundZ(60);
          m = Mat4.multiply(m, Mat4Transform.scale((float)(tailScale*0.4),(float)(tailHeight*2),(float)(tailScale*0.8)));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)-(tailHeight*0.3), 0f));
          TransformNode tailTransform = new TransformNode("head2Transform", m);
          ModelNode tailShape = new ModelNode("head2Shape", cube4);          

      NameNode uArm2 = new NameNode("uArm2"); 
          rotateUpper2 = new TransformNode("rotateUpper2 (" + upperAngle + ")", Mat4Transform.rotateAroundZ(upperAngle)); //Mat4Transform.rotateAroundZ(-30));
          m = Mat4Transform.scale((float)(uArmScale*1.3),uArmHeight,(float)(uArmScale*1.3));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f)); //0f, 0.5f, 0f));
          //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
          TransformNode uArm2Transform = new TransformNode("uArm2Transform", m);
          ModelNode uArm2Shape = new ModelNode("uArm2Shape", cube3);

          TransformNode translateTopUArm2 = new TransformNode("translateTopUArm2", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
      
      NameNode head2 = new NameNode("head2");            
          rotateHead2 = new TransformNode("rotateHead2", Mat4Transform.rotateAroundZ(this.targetHead2));
          //rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));

          //m = new Mat4(1);
          m = Mat4Transform.scale((float)(headDepth*1.2),(float)(headHeight*1.2),(float)(headWidth*1.4));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
          TransformNode head2Transform = new TransformNode("head2Transform", m);
          ModelNode head2Shape = new ModelNode("head2Shape", cube4);
      // NameNode lampLight1 = new NameNode("lamp light 1");
      //     m = new Mat4(1);
      //     m = Mat4.multiply(m, Mat4Transform.translate((float)(headDepth/1.9f), 0f, 0f));
      //     TransformNode translateLampLight1 = new TransformNode("lamp light 1 translate", m);

      //     m = new Mat4(1);
      //     m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(headLightAngle));
      //     rotateLampLight1 = new TransformNode("lamp light 1 rotate", m);

      //     m = new Mat4(1);
      //     m = Mat4.multiply(m, Mat4Transform.scale(lampLight1Scale, lampLight1Scale, lampLight1Scale));
      //     TransformNode lampLight1Transform = new TransformNode("lamp light 1 transform", m);
      //       ModelNode lampLight1Shape = new ModelNode("Cube(light)", lampLight1);

      lamp2Root.addChild(lamp2MoveTranslate);
        //Do I need lamp1MoveTranslate here?
        lamp2MoveTranslate.addChild(base2); //rotateLamp);
            //rotateLamp.addChild(base);
            base2.addChild(base2Transform);
                base2Transform.addChild(base2Shape);
            base2.addChild(rotateLamp2); //lArmTranslate);
                rotateLamp2.addChild(lArm2);
                    lArm2.addChild(lArm2PosTransform);
                    // \/ Translates to origin for the rotation
                    lArm2PosTransform.addChild(rotateLowerY2); //translateTopBase);
                    //translateTopBase.addChild(rotateLowerY);
                        rotateLowerY2.addChild(rotateLowerZ2);
                        rotateLowerZ2.addChild(lArm2Transform);
                            lArm2Transform.addChild(lArm2Shape);
                        rotateLowerZ2.addChild(translateTopLArm2);
                            translateTopLArm2.addChild(joint2);
                            joint2.addChild(joint2Transform);
                                joint2Transform.addChild(joint2Shape);
                            joint2.addChild(tail);
                                tail.addChild(tailTransform);
                                  tailTransform.addChild(tailShape);
                            joint2.addChild(uArm2);//translateMiddleJoint);
                                //translateMiddleJoint.addChild(uArm);
                                uArm2.addChild(rotateUpper2);
                                    rotateUpper2.addChild(uArm2Transform);
                                    uArm2Transform.addChild(uArm2Shape);
                                    rotateUpper2.addChild(translateTopUArm2);
                                    translateTopUArm2.addChild(head2);
                                        head2.addChild(rotateHead2);
                                        rotateHead2.addChild(head2Transform);
                                            head2Transform.addChild(head2Shape);

      lamp2Root.update();
      lamp2Root.print(0, false);
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


//-------------COMMENTED LIGHTING-----------------------------------------------
    lampLight1.render(gl, Mat4Transform.translate(getLightPosition(1))); //m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);

    // lampLight2.setPosition(getLightPosition(1));  // changing light position each frame
    lampLight2.render(gl, Mat4Transform.translate(getLightPosition(2)));

    // overheadLight1.setPosition(getLightPosition(2));
    overheadLight1.render(gl, Mat4Transform.translate(getLightPosition(3)));
    // overheadLight2.setPosition(getLightPosition(3));
    overheadLight2.render(gl, Mat4Transform.translate(getLightPosition(4)));
//------------------------------------------------------------------------------
    // light.setPosition(getLightPosition(3));
    // light.render(gl);



    // light2.setPosition(getLightPosition(2));  // changing light position each frame
    // light2.render(gl);

    // light3.setPosition(getLightPosition(3));  // changing light position each frame
    // light3.render(gl);

    // light4.setPosition(getLightPosition(4));  // changing light position each frame
    // light4.render(gl);

    ttFloor.setModelMatrix(getModelMatFloor());       // change transform
    ttFloor.render(gl);

    ttCeiling.setModelMatrix(getModelMatCeiling());
    ttCeiling.render(gl);

    ttWall.setModelMatrix(getModelMatWall1());       // change transform
    ttWall.render(gl);
    ttWall.setModelMatrix(getModelMatWall2());       // change transform
    ttWall.render(gl);
    ttLowWall.setModelMatrix(getModelMatLowWall());
    ttLowWall.render(gl);
    ttBackground.setModelMatrix(getModelMatBackground());       // change transform
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
    cube6.setModelMatrix(getMforEggStand());     // change transform
    cube6.render(gl);

    // ttLowWall.setModelMatrix(getModelMatLowWall());
    // ttLowWall.render(gl);

    ttWindow.setModelMatrix(getModelMatWindow());       // change transform
    ttWindow.render(gl);


    animateLamps();
    // System.out.println(this.currentLowerZ1);
    

    //robot.render(gl);
    updateBranches();
    branchRoot.draw(gl);
    
    // System.out.println("*");
    lamp1Root.draw(gl);
    // System.out.println("*");
    //rotateUpper.draw(gl);
    lamp2Root.draw(gl);
  }

  private void animateLamps() {
    // System.out.println("** " + this.targetLowerZ1 + " ** " + this.currentLowerZ1);
    if (this.targetLampRot1 > this.currentLampRot1) { this.currentLampRot1 += 1; } 
    else if (this.targetLampRot1 < this.currentLampRot1) { this.currentLampRot1 -= 1;}
    if (this.targetLowerZ1 > this.currentLowerZ1) { this.currentLowerZ1 += 1; } 
    else if (this.targetLowerZ1 < this.currentLowerZ1) { this.currentLowerZ1 -= 1;}
    if (this.targetLowerY1 > this.currentLowerY1) { this.currentLowerY1 += 1; } 
    else if (this.targetLowerY1 < this.currentLowerY1) { this.currentLowerY1 -= 1;}
    if (this.targetUpper1 > this.currentUpper1) { this.currentUpper1 += 1; } 
    else if (this.targetUpper1 < this.currentUpper1) { this.currentUpper1 -= 1;}
    if (this.targetHead1 > this.currentHead1) { this.currentHead1 += 1; } 
    else if (this.targetHead1 < this.currentHead1) { this.currentHead1 -= 1;}
    if (this.targetLampRot2 > this.currentLampRot2) { this.currentLampRot2 += 1; } 
    else if (this.targetLampRot2 < this.currentLampRot2) { this.currentLampRot2 -= 1;}
    if (this.targetLowerZ2 > this.currentLowerZ2) { this.currentLowerZ2 += 1; } 
    else if (this.targetLowerZ2 < this.currentLowerZ2) { this.currentLowerZ2 -= 1;}
    if (this.targetLowerY2 > this.currentLowerY2) { this.currentLowerY2 += 1; } 
    else if (this.targetLowerY2 < this.currentLowerY2) { this.currentLowerY2 -= 1;}
    if (this.targetUpper2 > this.currentUpper2) { this.currentUpper2 += 1; } 
    else if (this.targetUpper2 < this.currentUpper2) { this.currentUpper2 -= 1;}
    if (this.targetHead2 > this.currentHead2) { this.currentHead2 += 1; } 
    else if (this.targetHead2 < this.currentHead2) { this.currentHead2 -= 1;}


    //Update Values (needs to be done in render)
    rotateLamp.setTransform(Mat4Transform.rotateAroundY(this.currentLampRot1));
    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(this.currentHead1));
    rotateUpper.setTransform(Mat4Transform.rotateAroundZ(this.currentUpper1));
    rotateLowerY.setTransform(Mat4Transform.rotateAroundY(this.currentLowerY1));
    rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(this.currentLowerZ1));

     //Update Values (needs to be done in render)
    rotateLamp2.setTransform(Mat4Transform.rotateAroundY(this.currentLampRot2));
    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, 0f));
    rotateHead2.setTransform(Mat4Transform.rotateAroundZ(this.currentHead2));
    rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(this.currentUpper2));
    rotateLowerY2.setTransform(Mat4Transform.rotateAroundY(this.currentLowerY2));
    rotateLowerZ2.setTransform(Mat4Transform.rotateAroundZ(this.currentLowerZ2));

    lamp1Root.update();
    lamp2Root.update();
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
    rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle*2));
    branchRoot.update(); // IMPORTANT – the scene graph has changed
  }


  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition(int opt) {
    // double elapsedTime = getSeconds()-startTime;
    // float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    // float y = 2.7f;
    // float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));

    if (opt == 1) {
      return new Vec3 (-4f, 4.5f, 0);
    } else if (opt == 2) { //if (opt == 2) {
      return new Vec3 (4f, 4.5f, 0);
    } else if (opt == 3) {
      return new Vec3 (-2f, 15.5f, 0f);
    } else {
      return new Vec3 (2f, 15.5f, 0f);
    }
  }



  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getModelMatFloor() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getModelMatCeiling() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,size,0f), modelMatrix);
    //modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(180), modelMatrix);
    return modelMatrix;
  }

  // As the transforms do not change over time for this object, they could be stored once rather than continually being calculated
  private Mat4 getModelMatWall1() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getModelMatWall2() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getModelMatBackground() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size*4, 5f, size*3), modelMatrix); //3f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-1.0f,size*0.8f,-size*2.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getModelMatWindow() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,size*0.5f,-size*0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getModelMatLowWall() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size*0.2f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,size*0.035f,-size*0.4f), modelMatrix);
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
    this.targetLampRot1 = 0;
    this.targetHead1 = 20;
    this.targetUpper1 = -60;
    this.targetLowerY1 = 0;
    this.targetLowerZ1 = 30;

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
  }

  public void lamp1Position2() {
    lamp1Position1();
    this.targetUpper1 = -50;
    this.targetLowerY1 = -90;
    this.targetHead1 = -10;
    this.targetLowerZ1 = 50;

  }

  public void lamp1Position3() {
    lamp1Position1();
    this.targetHead1 = 30;
    this.targetLowerY1 = -70;
  }

  public void lamp2Position1() {
    this.targetLampRot2 = 0;
    this.targetHead2 = -20;
    this.targetUpper2 = 60;
    this.targetLowerY2 = 0;
    this.targetLowerZ2 = -30;
  }

  public void lamp2Position2() {
    lamp2Position1();
    this.targetUpper2 = 80;
    this.targetLowerZ2 = -15;
    this.targetHead2 = -60;
    
  }

  public void lamp2Position3() {
    lamp2Position1();
    this.targetLowerZ2 = -20;
    this.targetLowerY2 = -90;
    this.targetHead2 = -30;
  }


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