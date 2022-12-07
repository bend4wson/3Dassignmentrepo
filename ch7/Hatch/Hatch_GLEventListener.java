/* I declare that this code is my own work */
/* Author Ben Dawson bcdawson1@sheffield.ac.uk */

import gmaths.*;
//import Math;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import java.util.ArrayList;

public class Hatch_GLEventListener implements GLEventListener {
      
  public Hatch_GLEventListener(Camera camera) {
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

  //Used in order to clean up memory
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();

    //------------------CODE FOR MULTIPLE LIGHT SOURCES--------------------------
    // lampLight1.dispose(gl);
    // lampLight2.dispose(gl);
    // overheadLight1.dispose(gl);
    // overheadLight2.dispose(gl);
    //-------------------------------------------------------------------------------------
    //-------------CODE FOR SINGLE LIGHT SOURCE--------------------------------------------
    light.dispose(gl);
    //-------------------------------------------------------------------------------------


    ttWall.dispose(gl);
    ttWindow.dispose(gl);
    ttLowWall.dispose(gl);
    ttFloor.dispose(gl);
    ttBackground.dispose(gl);
    ttCeiling.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
    cube3.dispose(gl);
    cube4.dispose(gl);
    cube6.dispose(gl);
    tableLeg.dispose(gl);
  }
  

  //Defining methods to handle the scene:

  private Camera camera;
  private Mat4 perspective;
  private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere1, jointSphere2, eyeSphere, tableLeg, ttWall, ttLowWall, ttFloor, ttWindow, ttCeiling, ttBackground; //ttLowWall;

  private SGNode branchRoot, lamp1Root, lamp2Root;
  private TransformNode translateEgg, rotateEggZ, rotateEggY;
  private TransformNode jointTransform, lamp1MoveTranslate, rotateLamp, lArmTransform, rotateLowerZ, rotateLowerY, rotateUpper, uArmTranslate, rotateHead, rotateLampLight1, lampLight1Transform, lampLight1Translate; //, uArmTranslate;//, lArm;//Transform;
  private TransformNode lamp2MoveTranslate, rotateLamp2, lArm2Transform, rotateLowerZ2, rotateLowerY2, rotateUpper2, uArm2Translate, rotateHead2;

  //---------------------------CODE FOR MULTIPLE LIGHT SOURCES------------------------------
  // private PointLight overheadLight1, overheadLight2;
  // private SpotLight lampLight1, lampLight2;
  //--------------------------------------------------------------------------------------------
  //---------------------------CODE FOR SINGLE LIGHT SOURCE---------------------------------------
  private Light light;
  //--------------------------------------------------------------------------------------------

  private static final boolean DISPLAY_SHADERS = false;
  private Shader shader;
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

  private float rotateAllAngleStart = 10, rotateAllAngle = rotateAllAngleStart;
  private float rotateYAngleStart = 45, rotateYAngle = rotateYAngleStart;


  public void initialise(GL3 gl) {
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vertexShaders/vs_tt_05.txt", "fragmentShaders/fs_tt_05.txt");
    //CODE FOR MULTIPLE LIGHT SOURCES \/\/
    // Shader shader = new Shader(gl, "vertexShaders/vs_tt_05.txt", "fragmentShaders/fs_multiple_casters.txt");
    Shader shaderStatic = new Shader(gl, "vertexShaders/vs_tt_05_static.txt", "fragmentShaders/fs_tt_05.txt");  
    // Shader shaderStatic = new Shader(gl, "vertexShaders/vs_tt_05_static.txt", "fragmentShaders/fs_multiple_casters.txt");
    Shader shaderWindow = new Shader(gl, "vertexShaders/vs_tt_05_static.txt", "fragmentShaders/fs_tt_05_window.txt");

    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/woodCeiling3.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/ear0xuu2.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/cloud2.jpg");
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/medievalTown.jpg");
    int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
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

//---------------------CODE FOR SINGLE LIGHT SOURCE---------------------------------------
    light = new Light(gl);
    light.setCamera(camera);
//-----------------------------------------------------------------------------------------


//-----------------------CODE FOR MULTIPLE LIGHT SOURCES----------------------
    // // LightNode lampLight1 = new SpotLight("lampLight1 cube(0)", Cube); 
    // // LightNode lampLight2 = new SpotLight("lampLight2 cube(0)", Cube); 
    // // LightNode overheadLight1 = new PointLight("lampLight3 cube(0)", Cube); 
    // // LightNode overheadLight2 = new PointLight("lampLight4 cube(0)", Cube); 
    // lampLight1 = new SpotLight(gl, camera);
    // lampLight2 = new SpotLight(gl, camera);
    // overheadLight1 = new PointLight(gl, camera);
    // overheadLight2 = new PointLight(gl, camera);
    //--------------------------------------------------------------
    //--------------------------CODE FOR MULTIPLE LIGHT SOURCES--------------------------------
    // ArrayList<Light> lightArray = new ArrayList<Light>();
    // lightArray.add(lampLight1);
    // lightArray.add(lampLight2);
    // lightArray.add(overheadLight1);
    // lightArray.add(overheadLight2);
    //----------------------------------------------------------------------------

    //NOTE - Also took out lightArray everywhere in the Models below and replaced with Light \/\/
    Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    ttFloor = new Model(gl, camera, light, shaderStatic, material, modelMatrix, mesh, textureId12);
    ttWindow = new Model(gl, camera, light, shaderWindow, material, modelMatrix, mesh, textureId14);
    ttWall = new Model(gl, camera, light, shaderStatic, material, modelMatrix, mesh, textureId11);
    ttLowWall = new Model(gl, camera, light, shaderStatic, material, modelMatrix, mesh, textureId11);
    ttBackground = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId9);
    ttCeiling = new Model(gl, camera, light, shaderStatic, material, modelMatrix, mesh, textureId13);

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_cube_04.txt");
    //MULTIPLE LIGHT SOURCES SHADER \/
    // shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_multiple_casters.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4, 4, 4), Mat4Transform.translate(0, 0.5f, 0));
    cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId4, textureId4);
    cube2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId16, textureId16);
    cube3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId17, textureId17);
    cube4 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId18, textureId18);
    cube6 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3, textureId10);

    tableLeg = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId13, textureId13);

    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_cube_04.txt");
    //MULTIPLE LIGHT SOURCES SHADER \/
    // shader = new Shader(gl, "vertexShaders/vs_cube_04.txt", "fragmentShaders/fs_multiple_casters.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId19, textureId20);
    jointSphere1 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId16, textureId16);
    jointSphere2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId18, textureId18);
    eyeSphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId15, textureId15);

    branchRoot = new NameNode("sphere-branch-structure");
    translateEgg = new TransformNode("translate("+xPosition+",0,0)", Mat4Transform.translate(xPosition,3,0));
    rotateEggZ = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));
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

  

    lamp1Root = createLamp1();
    lamp1Root.update();
    lamp2Root = createLamp2();
    lamp2Root.update();

  //First Lamp Creation (Separate class to be implemented later):
  }

  private SGNode createLamp1() {
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
      lArmTransform = new TransformNode("lArm 1 transform",Mat4Transform.translate(xPosition,0,0));
      rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));
                                                                             
      rotateLamp = new TransformNode("rotateLamp",Mat4Transform.translate(xPosition,0,0));
      lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(lampMoveTranslate));

      NameNode base = new NameNode("base");
          Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          TransformNode baseTransform = new TransformNode("baseTransform", m);
          ModelNode baseShape = new ModelNode("baseShape(Cube)", cube2);

          rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(this.currentLampRot1));


      NameNode lArm = new NameNode("lArm"); 
          TransformNode lArmPosTransform = new TransformNode("lArmPosTransform", Mat4Transform.translate(0, 0, 0));

          m = Mat4Transform.rotateAroundZ(this.currentLowerZ1);
          rotateLowerZ = new TransformNode("rotateLowerZ", m);

          m = Mat4Transform.rotateAroundY(this.currentLowerY1);
          rotateLowerY = new TransformNode("rotateLowerY", m);

          m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); 

          TransformNode lArmTransform = new TransformNode("lArmTransform", m);
          ModelNode lArmShape = new ModelNode("lArmShape(Cube)", cube2);

          TransformNode translateTopLArm = new TransformNode("translateTopLArm", Mat4Transform.translate(0f, (float)(baseHeight+lArmHeight), 0f));


      NameNode joint = new NameNode("joint");
          m = Mat4Transform.scale(jointScale,jointScale,jointScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));        
          TransformNode jointTransform = new TransformNode("jointTransform", m);
          ModelNode jointShape = new ModelNode("jointShape", jointSphere1);
          

      NameNode uArm = new NameNode("uArm"); 
          rotateUpper = new TransformNode("rotateUpper (" + this.currentUpper1 + ")", Mat4Transform.rotateAroundZ(this.currentUpper1)); 
          m = Mat4Transform.scale(uArmScale,uArmHeight,uArmScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f));
          TransformNode uArmTransform = new TransformNode("uArmTransform", m);
          ModelNode uArmShape = new ModelNode("uArmShape", cube2);

          TransformNode translateTopUArm = new TransformNode("translateTopUArm", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
      
      NameNode head = new NameNode("head");            
          rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(this.currentHead1));

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
        lamp1MoveTranslate.addChild(base); 
            base.addChild(baseTransform);
                baseTransform.addChild(baseShape);
            base.addChild(rotateLamp); 
                rotateLamp.addChild(lArm);
                    lArm.addChild(lArmPosTransform);
                      lArmPosTransform.addChild(rotateLowerY); 
                          rotateLowerY.addChild(rotateLowerZ);
                            rotateLowerZ.addChild(lArmTransform);
                                lArmTransform.addChild(lArmShape);
                            rotateLowerZ.addChild(translateTopLArm);
                                translateTopLArm.addChild(joint);
                                joint.addChild(jointTransform);
                                    jointTransform.addChild(jointShape);
                                joint.addChild(uArm);
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

  //For the second lamp (Separate Lamp class to be added later):
  private SGNode createLamp2() {
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

      lArm2Transform = new TransformNode("lArm 2 transform",Mat4Transform.translate(-xPosition,0,0));
      rotateHead2 = new TransformNode("Rotate Head 2", Mat4Transform.translate(-xPosition, 0, 0));
      lamp2MoveTranslate = new TransformNode("lamp2MoveTranslate",Mat4Transform.translate(lampMoveTranslate));

      NameNode base2 = new NameNode("base2");
          Mat4 m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.rotateAroundY(this.currentLampRot2));
          rotateLamp2 = new TransformNode("rotateLamp2",m); 

          m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
          m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
          TransformNode base2Transform = new TransformNode("baseTransform2", m);
          ModelNode base2Shape = new ModelNode("baseShape2(Cube)", cube4);

      NameNode lArm2 = new NameNode("lArm2"); 
          TransformNode lArm2PosTransform = new TransformNode("lArm2PosTransform", Mat4Transform.translate(0, 0, 0));

          m = Mat4Transform.rotateAroundZ(this.targetLowerZ2);
          rotateLowerZ2 = new TransformNode("rotateLowerZ2", m);

          m = Mat4Transform.rotateAroundY(this.targetLowerY2);
          rotateLowerY2 = new TransformNode("rotateLowerY2", m);

          m = Mat4Transform.scale((float)(lArmScale*1.3),lArmHeight,(float)(lArmScale*1.3));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); 

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
          rotateUpper2 = new TransformNode("rotateUpper2 (" + upperAngle + ")", Mat4Transform.rotateAroundZ(upperAngle)); 
          m = Mat4Transform.scale((float)(uArmScale*1.3),uArmHeight,(float)(uArmScale*1.3));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); 

          TransformNode uArm2Transform = new TransformNode("uArm2Transform", m);
          ModelNode uArm2Shape = new ModelNode("uArm2Shape", cube3);

          TransformNode translateTopUArm2 = new TransformNode("translateTopUArm2", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
      
      NameNode head2 = new NameNode("head2");            
          rotateHead2 = new TransformNode("rotateHead2", Mat4Transform.rotateAroundZ(this.targetHead2));

          m = Mat4Transform.scale((float)(headDepth*1.2),(float)(headHeight*1.2),(float)(headWidth*1.4));
          m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
          TransformNode head2Transform = new TransformNode("head2Transform", m);
          ModelNode head2Shape = new ModelNode("head2Shape", cube4);

      lamp2Root.addChild(lamp2MoveTranslate);
        lamp2MoveTranslate.addChild(base2);
            base2.addChild(base2Transform);
                base2Transform.addChild(base2Shape);
            base2.addChild(rotateLamp2); 
                rotateLamp2.addChild(lArm2);
                    lArm2.addChild(lArm2PosTransform);
                      lArm2PosTransform.addChild(rotateLowerY2);
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
                                joint2.addChild(uArm2);
                                    uArm2.addChild(rotateUpper2);
                                        rotateUpper2.addChild(uArm2Transform);
                                        uArm2Transform.addChild(uArm2Shape);
                                        rotateUpper2.addChild(translateTopUArm2);
                                        translateTopUArm2.addChild(head2);
                                            head2.addChild(rotateHead2);
                                            rotateHead2.addChild(head2Transform);
                                                head2Transform.addChild(head2Shape);

      lamp2Root.update();
      return lamp2Root;
  }

  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    Mat4 m = Mat4Transform.scale(1.5f,2.6f,1.5f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));


//-------------CODE FOR MULTIPLE LIGHT SOURCES-----------------------------------------------
    // lampLight1.render(gl, Mat4Transform.translate(getLightPosition(1))); //m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);

    // // lampLight2.setPosition(getLightPosition(1));  // changing light position each frame
    // lampLight2.render(gl, Mat4Transform.translate(getLightPosition(2)));

    // // overheadLight1.setPosition(getLightPosition(2));
    // overheadLight1.render(gl, Mat4Transform.translate(getLightPosition(3)));
    // // overheadLight2.setPosition(getLightPosition(3));
    // overheadLight2.render(gl, Mat4Transform.translate(getLightPosition(4)));
//------------------------------------------------------------------------------

//-------------CODE FOR SINGLE LIGHT SOURCE-----------------------------------------------
    light.setPosition(getLightPosition(5));
    light.render(gl);
//-----------------------------------------------------------------------------------------


    ttFloor.setModelMatrix(getModelMatFloor());       
    ttFloor.render(gl);

    ttCeiling.setModelMatrix(getModelMatCeiling());
    ttCeiling.render(gl);

    ttWall.setModelMatrix(getModelMatWall1());      
    ttWall.render(gl);
    ttWall.setModelMatrix(getModelMatWall2());     
    ttWall.render(gl);
    ttLowWall.setModelMatrix(getModelMatLowWall());
    ttLowWall.render(gl);
    ttBackground.setModelMatrix(getModelMatBackground());      
    ttBackground.render(gl, true); 

    cube.setModelMatrix(getMforTableTop());   
    cube.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg1());    
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg2());    
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg3());   
    tableLeg.render(gl);
    tableLeg.setModelMatrix(getMforTableLeg4());    
    tableLeg.render(gl);
    cube6.setModelMatrix(getMforEggStand());   
    cube6.render(gl);

    ttWindow.setModelMatrix(getModelMatWindow());      
    ttWindow.render(gl);


    animateLamps();
  
    updateBranches();
    branchRoot.draw(gl);
    
    lamp1Root.draw(gl);
    lamp2Root.draw(gl);
  }

  private void animateLamps() {
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
    double elapsedTime = getSeconds()-startTime; //currentTime; //startTime;
  
    if (Math.sin(elapsedTime*2) > 0.6) {
      rotateAllAngle = rotateAllAngleStart*(float)(Math.sin(10*elapsedTime)); //Math.round((rotateAllAngleStart*(float)Math.sin(elapsedTime))/2);
    } else {
      rotateAllAngle = 0;
    }

    if (Math.sin(elapsedTime*5) > 0) {
      translateEgg.setTransform(Mat4Transform.translate(xPosition,(3.0f+(float)Math.sin(5*elapsedTime)),0));
      rotateYAngle = rotateAllAngleStart*(float)(Math.sin(10*elapsedTime));
      
    } else {
      translateEgg.setTransform(Mat4Transform.translate(xPosition, 3, 0));
      rotateYAngle = 0;
    }
    rotateEggY.setTransform(Mat4Transform.rotateAroundY(rotateYAngle*2));
    branchRoot.update(); 
  }



  //Light positions 1-4 used for multiple light sources
  private Vec3 getLightPosition(int opt) {

    if (opt == 1) {
      return new Vec3 (-4f, 4.5f, 0);
    } else if (opt == 2) { //if (opt == 2) {
      return new Vec3 (4f, 4.5f, 0);
    } else if (opt == 3) {
      return new Vec3 (-2f, 15.5f, 0f);
    } else if (opt == 4) {
      return new Vec3 (2f, 15.5f, 0f);
    } else {
      return new Vec3 (6f, 15.5f, 4f);
    }
  }

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
    return modelMatrix;
  }

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
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size*4, 5f, size*3), modelMatrix); 
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
  
  private Mat4 getMforTableTop() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f, 5f, 0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(5f,0.5f,5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTableLeg1() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-4.5f, 0.5f, -4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }
  
  private Mat4 getMforTableLeg2() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(4.5f, 0.5f, 4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTableLeg3() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(4.5f, 0.5f, -4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTableLeg4() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-4.5f, 0.5f, 4.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f, 2.5f, 0.5f), modelMatrix);
    return modelMatrix;
  }

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
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
}