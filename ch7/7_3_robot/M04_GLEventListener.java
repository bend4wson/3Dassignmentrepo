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
    lampLight1.dispose(gl);
    light2.dispose(gl);
    light3.dispose(gl);
    light4.dispose(gl);
    ttWall.dispose(gl);
    ttWindow.dispose(gl);
    ttFloor.dispose(gl);
    ttBackground.dispose(gl);
    ttCeiling.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
    cube3.dispose(gl);
    cube4.dispose(gl);
    cube5.dispose(gl);
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
  private Light lampLight1, light2, light3, light4;
  private Model cube, cube2, cube3, cube4, cube5, cube6, lightCube1, lightCube2, sphere, jointSphere, tableLeg, ttWall, ttFloor, ttWindow, ttCeiling, ttBackground;
  //private int frame = 1;

  private SGNode branchRoot, lamp1Root, lamp2Root;
  private TransformNode translateEgg, rotateEggZ, rotateEggY;
  private TransformNode jointTransform, lamp1MoveTranslate, rotateLamp, lArmTransform, rotateLowerZ, rotateLowerY, rotateUpper, uArmTranslate, rotateHead, rotateLampLight1, lampLight1Transform, lampLight1Translate; //, uArmTranslate;//, lArm;//Transform;
  private TransformNode lamp2MoveTranslate, rotateLamp2, lArm2Transform, rotateLowerZ2, rotateLowerY2, rotateUpper2, uArm2Translate, rotateHead2;

  private static final boolean DISPLAY_SHADERS = false;
  private Shader shader;
  //Necessary? \/ \/ \/ 


  // private float lArmAngleZ = 30;
  // private float lArmAngleY = 0;
  // private float headAngleZ = 0;
  private float xPosition = 0;
  private float upperAngle = -60;
  private float upperAngle2 = 60;
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
    Shader shaderStatic = new Shader(gl, "vs_tt_05_static.txt", "fs_tt_05.txt");
    Shader shaderWindow = new Shader(gl, "vs_tt_05_static.txt", "fs_tt_05_window.txt");

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
    int[] textureId14 = TextureLibrary.loadTexture(gl, "textures/glass.jpg");

    //lampLight1.setCamera(camera);
    lampLight1 = new Light(gl);
    lampLight1.setCamera(camera);
    light2 = new Light(gl);
    light2.setCamera(camera);
    light3 = new Light(gl);
    light3.setCamera(camera);
    light4 = new Light(gl);
    light4.setCamera(camera);

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
    //lightShader = new Shader(gl, "vs_light_01.txt", "fs_light_01.txt");
    //lampLight1 = new Model(gl, camera, light, lightShader, material, modelMatrix, mesh, textureId4, textureId4);


    Material material = new Material(new Vec3(1f, 1f, 1f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    ttFloor = new Model(gl, camera, lampLight1, shaderStatic, material, modelMatrix, mesh, textureId12);
    ttWindow = new Model(gl, camera, lampLight1, shaderWindow, material, modelMatrix, mesh, textureId14);
    ttWall = new Model(gl, camera, lampLight1, shaderStatic, material, modelMatrix, mesh, textureId11);
    ttBackground = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId9);
    ttCeiling = new Model(gl, camera, light3, shaderStatic, material, modelMatrix, mesh, textureId13);

    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4, 4, 4), Mat4Transform.translate(0, 0.5f, 0));
    cube = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId4, textureId4);
    cube2 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube3 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube4 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube5 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);
    cube6 = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId3, textureId3);

    tableLeg = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId13, textureId13);

    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId1, textureId2);
    jointSphere = new Model(gl, camera, lampLight1, shader, material, modelMatrix, mesh, textureId0, textureId0);

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
    System.out.println("-------------------=====================");
    lamp1Root.update();
    System.out.println("22222222222222222222222222222222222222222");
    lamp2Root = createLamp2(m);
    System.out.println("33333333333333333333333333333");
    lamp2Root.update();
    System.out.println("////////////////////////////");

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

        float lowerYAngle = 0;
        float lowerZAngle = 30;

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
        lamp1MoveTranslate = new TransformNode("lamp1MoveTranslate",Mat4Transform.translate(-5f,0,0));

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        NameNode base = new NameNode("base");
            // m = new Mat4(1);
            // m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
            // TransformNode lArmTranslate = new TransformNode("lArmTranslate", m);

            
            //m = new Mat4(1);
            Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
            //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
            m = Mat4.multiply(m, Mat4Transform.translate(0f,(float)(baseHeight/2),0f));
            TransformNode baseTransform = new TransformNode("baseTransform", m);
            ModelNode baseShape = new ModelNode("baseShape(Cube)", cube5);
            //TransformNode baseRotate = new TransformNode("base rotate", m);

        //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

            rotateLamp = new TransformNode("rotateLamp", Mat4Transform.rotateAroundY(0));

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        NameNode lArm = new NameNode("lArm"); 
            TransformNode lArmPosTransform = new TransformNode("lArmPosTransform", Mat4Transform.translate(0, 0, 0));

            //m = new Mat4(1);
            m = Mat4Transform.rotateAroundZ(lowerZAngle);
            rotateLowerZ = new TransformNode("rotateLowerZ", m);
            //rotateLowerZ = new TransformNode("rotateLowerZ", m);

            //m = new Mat4(1);
            m = Mat4Transform.rotateAroundY(lowerYAngle);
            rotateLowerY = new TransformNode("rotateLowerY", m);
            //rotateLowerY = new TransformNode("rotateLowerY", m);


            //m = new Mat4(1);
            m = Mat4Transform.scale(lArmScale,lArmHeight,lArmScale);
            m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));

            //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
            TransformNode lArmTransform = new TransformNode("lArmTransform", m);
            ModelNode lArmShape = new ModelNode("lArmShape(Cube)", cube2);

            TransformNode translateTopLArm = new TransformNode("translateTopLArm", Mat4Transform.translate(0, (baseHeight+lArmHeight), 0));

        //Translate to origin, scale, etc.
        //When scaling something, always refer it to f.
        //Rotate should be in spearate node (if it affects nodes underneath)

        //Define a translate with respect to the origin of the node underneath it 
        //(i.e:(0,0,0) is the node underneath), so you just have to translate by the 
        //length of the current object you're defining.

        //rotateUpper = new TransformNode("Rotate Upper", Mat4Transform.translate(xPosition,0,0));

        System.out.println("||||||||||||||||||||||||||||||||||||");

        NameNode joint = new NameNode("joint");
            //Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
            //leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));

            //m = new Mat4(1);
            m = Mat4Transform.scale(jointScale,jointScale,jointScale);
            // float height = (baseHeight+lArmHeight+(float)(jointScale/2));
            m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));            // -9, 2.85f, 0));
            TransformNode jointTransform = new TransformNode("jointTransform", m);
            ModelNode jointShape = new ModelNode("jointShape", jointSphere);
            

        NameNode uArm = new NameNode("uArm"); 
            rotateUpper = new TransformNode("rotateUpper (" + this.upperAngle + ")", Mat4Transform.rotateAroundZ(this.upperAngle)); //Mat4Transform.rotateAroundZ(-30));
            //rotateUpper = new TransformNode("rotateUpper (" + this.upperAngle + ")", Mat4Transform.rotateAroundZ(this.upperAngle));


            // m = new Mat4(1);
            // //float rotationUpperXOffset = (uArmHeight * (float)Math.sin(this.upperAngle));
            // //System.out.println("@@@ " + rotationUpperXOffset + " @@@ " + (float)Math.sin(this.upperAngle));
            // m = Mat4.multiply(m, Mat4Transform.translate(1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));//(float)uArmHeight/2, 0f));//(float)(lArmHeight+(uArmHeight/2)+(jointScale/2)), 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));//0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f));         //(lArmHeight+(uArmHeight/2)), 0f));
            // uArmTranslate = new TransformNode("uArmTranslate", m);

        //m = new Mat4(1);
            //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(uArmHeight/2), 0f)); //-7f, -0.15f, 0f));
            m = Mat4Transform.scale(uArmScale,uArmHeight,uArmScale);
            m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f)); //1.5f, (float)(lArmHeight+(uArmHeight/2)-0.5f), 0f)); //0f, 0.5f, 0f));
            //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
            TransformNode uArmTransform = new TransformNode("uArmTransform", m);
            ModelNode uArmShape = new ModelNode("uArmShape", cube2);

            TransformNode translateTopUArm = new TransformNode("translateTopUArm", Mat4Transform.translate(0f, (float)(uArmHeight), 0f));
        
        NameNode head = new NameNode("head");
        //   m = new Mat4(1);
        // // Mat4 m = Mat4Transform.scale(headDepth,headHeight,headWidth);
        //   //headHeight = baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f)+2.5f;
        //   m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)((uArmHeight/2)), 0f)); //-3.5f,(baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f))+2.5f,0));
        //   TransformNode translateHead = new TransformNode("translateHead", m);

            // m = new Mat4(1);
            // m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(headLightAngle)); //(baseHeight+lArmHeight+(jointScale/2)-1)
            
            
            
            rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));
            //rotateHead = new TransformNode("rotateHead", Mat4Transform.rotateAroundZ(headLightAngle));

            //m = new Mat4(1);
            m = Mat4Transform.scale(headDepth,headHeight,headWidth);
            m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, 0f));
            TransformNode headTransform = new TransformNode("headTransform", m);
            ModelNode headShape = new ModelNode("headShape", cube4);
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

        System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}}}}}}}}}}");

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
        System.out.println("END OF SCENE GRAPH");
        //lamp1Root.update();
        // System.out.println("***");
        return lamp1Root;






    // lArmTranslate.addChild(rotateLowerY);
    //   rotateLowerY.addChild(rotateLowerZ);
    //     rotateLowerZ.addChild(lArm); //Thus should rotate the lower arm, the lower arm doesn't need a rotate itself?
    //       lArm.addChild(lArmTransform);
    //         lArmTransform.addChild(lArmShape);
    //       //lArm.addChild(jointRotate);
    //       lArm.addChild(joint);
    //         joint.addChild(jointTransform);
    //           jointTransform.addChild(jointShape);
    //         joint.addChild(uArmTranslate);
    //           uArmTranslate.addChild(rotateUpper);
    //             rotateUpper.addChild(uArm);
    //               uArm.addChild(uArmTransform);
    //                 uArmTransform.addChild(uArmShape);
    //               uArm.addChild(translateHead);
    //                 translateHead.addChild(rotateHead);
    //                   rotateHead.addChild(head);
    //                     head.addChild(headTransform);
    //                       headTransform.addChild(headShape);
    //                     // head.addChild(lampLight1Translate);
    //                     //   lampLight1Translate.addChild(rotateLampLight1);
    //                     //     rotateLampLight1.addChild(lampLight1);
    //                     //       lampLight1.addChild(lampLight1Transform);
    //                     //         lampLight1Transform.addChild(lampLight1Shape);

      

    // lamp1Root.addChild(lamp1MoveTranslate);
    //       //Do I need lamp1MoveTranslate here?
    //       lamp1MoveTranslate.addChild(rotateLamp);
    //         rotateLamp.addChild(base);
    //           base.addChild(baseTransform);
    //             baseTransform.addChild(baseShape);
    //           base.addChild(lArmTranslate);
    //             lArmTranslate.addChild(rotateLowerY);
    //               rotateLowerY.addChild(rotateLowerZ);
    //                 rotateLowerZ.addChild(lArm); //Thus should rotate the lower arm, the lower arm doesn't need a rotate itself?
    //                   lArm.addChild(lArmTransform);
    //                     lArmTransform.addChild(lArmShape);
    //                   //lArm.addChild(jointRotate);
    //                   lArm.addChild(joint);
    //                     joint.addChild(jointTransform);
    //                       jointTransform.addChild(jointShape);
    //                     joint.addChild(uArmTranslate);
    //                       uArmTranslate.addChild(rotateUpper);
    //                         rotateUpper.addChild(uArm);
    //                           uArm.addChild(uArmTransform);
    //                             uArmTransform.addChild(uArmShape);
    //                           uArm.addChild(translateHead);
    //                             translateHead.addChild(rotateHead);
    //                               rotateHead.addChild(head);
    //                                 head.addChild(headTransform);
    //                                   headTransform.addChild(headShape);


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
    //lamp1Root.print(0, false);
    

  }


  //For the second lamp (CREATE NEW CLASS FOR IT):

  private SGNode createLamp2(Mat4 m) {
    float base2Height = 0.35f;
    float base2Scale = 1.5f;
    float joint2Scale = 0.75f;
    float head2Height = 0.5f;
    float head2Width = 0.75f;
    float head2Depth = 1.5f;
    float uArm2Height = 2.5f;
    float uArm2Scale = 0.35f;
    float lArm2Height = 2.5f;
    float lArm2Scale = 0.35f;

    lamp2Root = new NameNode("lamp-2-branch-structure");
    //translateLamp = new TransformNode("lamp 1 transform",Mat4Transform.translate(xPosition,0,0));
    lArm2Transform = new TransformNode("lArm 2 transform",Mat4Transform.translate(-xPosition,0,0));
    // rotateUpper = new TransformNode("Rotate Upper Lamp1",Mat4Transform.translate(xPosition,0,0));
    // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.translate(xPosition,0,0));
    // rotateHead = new TransformNode("Rotate Head", Mat4Transform.translate(xPosition, 0, 0));


    rotateHead2 = new TransformNode("Rotate Head 2",Mat4Transform.translate(-xPosition,0,0));
    
    
    //branchRoot = new NameNode("sphere-branch-structure");

                                                                                                //Prev (xPosition, 0, 0)
    rotateLamp2 = new TransformNode("lamp 2 translate",Mat4Transform.translate(-xPosition,0,0));
    lamp2MoveTranslate = new TransformNode("lamp 2 move translate",Mat4Transform.translate(5f,0,0));


    NameNode base2 = new NameNode("base2");
        m = new Mat4(1);

        //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-30));
        //TransformNode rotateLowerZ = new TransformNode("lower lamp1 rotate", m);
        rotateLowerZ2 = new TransformNode("lower lamp2 rotate Z", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(-0));
        rotateLowerY2 = new TransformNode("lower lamp2 rotate Y", m);
        // rotateLowerZ = new TransformNode("Rotate Lower Lamp1Z",Mat4Transform.rotateAroundZ(0));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(base2Scale,base2Height,base2Scale));
        //Mat4 m = Mat4Transform.scale(baseScale,baseHeight,baseScale);
        m = Mat4.multiply(m, Mat4Transform.translate(-0f,(float)(base2Height/2),0f));
        TransformNode base2Transform = new TransformNode("base 2 transform", m);
          ModelNode base2Shape = new ModelNode("Cube(base)", cube5);
        //TransformNode baseRotate = new TransformNode("base rotate", m);

    //rotateLowerZ = new TransformNode("Rotate Lower",Mat4Transform.translate(xPosition,0,0));

    NameNode lArm2 = new NameNode("lower arm"); 

        m = new Mat4(1);
        float height2 = (base2Height+(lArm2Height/2.0f))-0.5f;
        //float lArmFloorLen = (float)Math.sqrt((height1*height1) - (lArmHeight*lArmHeight));
        m = Mat4.multiply(m, Mat4Transform.translate(-0f, height2, 0f)); //-4.9f-1.2f,height1,0));//(lArmFloorLen/2),height1,this.lArmTranslateZ));
        m = Mat4.multiply(m, Mat4Transform.scale(lArm2Scale,lArm2Height,lArm2Scale));

        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));    (baseHeight+(lArmHeight/2.0f))
        TransformNode lArm2Transform = new TransformNode("lower arm 2 transform", m);
          ModelNode lArm2Shape = new ModelNode("cube(base)", cube2);

//Translate to origin, scale, etc.
//When scaling something, always refer it to f.
//Rotate should be in spearate node (if it affects nodes underneath)

//Define a translate with respect to the origin of the node underneath it 
//(i.e:(0,0,0) is the node underneath), so you just have to translate by the 
//length of the current object you're defining.

    //rotateUpper = new TransformNode("Rotate Upper", Mat4Transform.translate(xPosition,0,0));

    NameNode joint2 = new NameNode("joint");
        //Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
        //leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(this.upperAngle2));
        //System.out.println("!!!!");
        //m = Mat4.multiply(m, Mat4Transform.rotateAroundY(0));
        rotateUpper2 = new TransformNode("upper arm 2 rotate", m); //Mat4Transform.rotateAroundZ(-30));
        //m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-30));

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(joint2Scale,joint2Scale,joint2Scale));
        height2 = (base2Height+lArm2Height+(float)(joint2Scale/2));
        m = Mat4.multiply(m, Mat4Transform.translate(-0f, height2, 0f)); // -9, 2.85f, 0));
        TransformNode joint2Transform = new TransformNode("joint 2 transform", m);
          //TransformNode jointScale = new TransformNode("joint transform", m); 
            ModelNode joint2Shape = new ModelNode("Sphere(joint)", jointSphere);
        

    NameNode uArm2 = new NameNode("upper arm 2"); 
        m = new Mat4(1);
        float rotationUpperXOffset2 = -(uArm2Height * (float)Math.sin(this.upperAngle));
        //System.out.println("@@@ " + rotationUpperXOffset + " @@@ " + (float)Math.sin(this.upperAngle));
        m = Mat4.multiply(m, Mat4Transform.translate(-1.5f, (float)(lArm2Height+(uArm2Height/2)-0.5f), 0f));//(lArmHeight+(uArmHeight/2)), 0f));
        uArm2Translate = new TransformNode("upper arm 2 translate", m);

        m = new Mat4(1);
        //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(uArmHeight/2), 0f)); //-7f, -0.15f, 0f));
        m = Mat4.multiply(m, Mat4Transform.scale(uArm2Scale,uArm2Height,uArm2Scale));
        //m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        //rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
        TransformNode uArm2Transform = new TransformNode("upper arm 2 transform", m);
          ModelNode uArm2Shape = new ModelNode("cube(base)", cube2);
      
    NameNode head2 = new NameNode("head 2");
        m = new Mat4(1);
      // Mat4 m = Mat4Transform.scale(headDepth,headHeight,headWidth);
        //headHeight = baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f)+2.5f;
        m = Mat4.multiply(m, Mat4Transform.translate(-0f, (float)((uArm2Height/2)), 0f)); //-3.5f,(baseHeight+lArmHeight+jointScale+uArmHeight+(headHeight/2.0f))+2.5f,0));
        TransformNode translateHead2 = new TransformNode("head translate 2", m);

        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-20)); //(baseHeight+lArmHeight+(jointScale/2)-1)
        rotateHead2 = new TransformNode("head 2 rotate", m);
        //m = new Mat4(1);

        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.scale(head2Depth,head2Height,head2Width));
        //m = Mat4.multiply(m, Mat4Transform.translate(0f, (float)(headHeight/2), 0f)); 
        TransformNode head2Transform = new TransformNode("head 2 transform", m);
          ModelNode head2Shape = new ModelNode("Cube(head)", cube2);

    lamp2Root.addChild(lamp2MoveTranslate);
      //Do I need lamp1MoveTranslate here?
      lamp2MoveTranslate.addChild(rotateLamp2);
        rotateLamp2.addChild(base2);
          base2.addChild(base2Transform);
          base2Transform.addChild(base2Shape);
          base2.addChild(rotateLowerY2);
            rotateLowerY2.addChild(rotateLowerZ2);
              rotateLowerZ2.addChild(lArm2); //Thus should rotate the lower arm, the lower arm doesn't need a rotate itself?
                lArm2.addChild(lArm2Transform);
                lArm2Transform.addChild(lArm2Shape);
                //lArm.addChild(jointRotate);
                lArm2.addChild(joint2);
                  joint2.addChild(joint2Transform);
                    joint2Transform.addChild(joint2Shape);
                  joint2.addChild(uArm2Translate);
                    uArm2Translate.addChild(rotateUpper2);
                      rotateUpper2.addChild(uArm2);
                        uArm2.addChild(uArm2Transform);
                          uArm2Transform.addChild(uArm2Shape);
                        uArm2.addChild(translateHead2);
                          translateHead2.addChild(rotateHead2);
                            rotateHead2.addChild(head2);
                              head2.addChild(head2Transform);
                                head2Transform.addChild(head2Shape);
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


    lampLight1.setPosition(getLightPosition(1));  // changing light position each frame
    lampLight1.render(gl);

    light2.setPosition(getLightPosition(2));  // changing light position each frame
    light2.render(gl);

    light3.setPosition(getLightPosition(3));  // changing light position each frame
    light3.render(gl);

    light4.setPosition(getLightPosition(4));  // changing light position each frame
    light4.render(gl);

    ttFloor.setModelMatrix(getMforTT1());       // change transform
    ttFloor.render(gl);

    ttCeiling.setModelMatrix(getMforTT2());
    ttCeiling.render(gl);

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
    cube6.setModelMatrix(getMforEggStand());     // change transform
    cube6.render(gl);

    ttWindow.setModelMatrix(getMforTT6());       // change transform
    ttWindow.render(gl);


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
    
    // System.out.println("*");
    lamp1Root.draw(gl);
    System.out.println("*");
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
  private Mat4 getMforTT1() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTT2() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,size,0f), modelMatrix);
    //modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(180), modelMatrix);
    return modelMatrix;
  }

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
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-1.0f,size*0.5f,-size*2.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforTT6() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,size*0.5f,-size*0.5f), modelMatrix);
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
    rotateLamp.setTransform(Mat4Transform.rotateAroundY(0));
    rotateLamp.update();

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
    lamp1MoveTranslate.update();

    // uArmTranslate.setTransform(Mat4Transform.translate(1.5f, 3.25f, 0f));
    // uArmTranslate.update();

    // rotateLampLight1.setTransform(Mat4Transform.rotateAroundZ(20));
    // rotateLampLight1.update();

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

    //lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
  }

  public void lamp1Position2() {
    rotateLamp.setTransform(Mat4Transform.rotateAroundY(0));
    // rotateLamp.update();

    lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));
    // lamp1MoveTranslate.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateLowerZ.update();

    // uArmTranslate.setTransform(Mat4Transform.translate(-0.3f, (float)(3.5f), 0f));
    // uArmTranslate.update();

    rotateUpper.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateUpper.update();


    // rotateHead.setTransform(Mat4Transform.rotateAroundZ(10));
    // rotateHead.update();

    rotateLowerY.setTransform(Mat4Transform.rotateAroundY(90));
    rotateLowerY.update();

    // lamp1MoveTranslate.setTransform(Mat4Transform.translate(-5f, 0f, 0f));

    rotateLampLight1.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateLampLight1.update();

    rotateHead.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateHead.update();

    // rotateUpper.setTransform(Mat4Transform.rotateAroundZ(50));
    // lamp1Root.update();

  }

  public void lamp1Position3() {

    uArmTranslate.setTransform(Mat4Transform.translate(1.5f, 3.25f, 0f));
    uArmTranslate.update();

    // rotateLampLight1.setTransform(Mat4Transform.rotateAroundZ(20));
    // rotateLampLight1.update();

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
    rotateLamp2.setTransform(Mat4Transform.rotateAroundY(-0));
    rotateLamp2.update();

    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, 0f));
    lamp2MoveTranslate.update();

    uArm2Translate.setTransform(Mat4Transform.translate(-1.5f, 3.25f, 0f));
    uArm2Translate.update();

    rotateHead2.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateHead2.update();

    rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(this.upperAngle2));
    //rotateUpper.setTransform(Mat4Transform.translate(0, 0, 0.5f));
    rotateUpper2.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerY2.setTransform(Mat4Transform.rotateAroundY(-0));
    rotateLowerY2.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ2.setTransform(Mat4Transform.rotateAroundZ(-30));
    rotateLowerZ2.update();

    jointTransform.setTransform(Mat4Transform.rotateAroundY(30));
    jointTransform.setTransform(Mat4Transform.translate(5f, 0f, 0f));
    jointTransform.update();


    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, 0f));
    lamp2MoveTranslate.update();

    // lamp1Root.update();
  }

  public void lamp2Position2() {
    rotateLamp2.setTransform(Mat4Transform.rotateAroundY(-0));
    rotateLamp2.update();

    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, 0f));
    lamp2MoveTranslate.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ2.setTransform(Mat4Transform.rotateAroundZ(20));
    rotateLowerZ2.update();

    uArm2Translate.setTransform(Mat4Transform.translate(0.3f, (float)(3.5f), 0f));
    uArm2Translate.update();

    rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateUpper2.update();


    // rotateHead.setTransform(Mat4Transform.rotateAroundZ(10));
    // rotateHead.update();

    rotateLowerY2.setTransform(Mat4Transform.rotateAroundY(-90));
    rotateLowerY2.update();

    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, 0f));

    rotateHead2.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateHead2.update();
  }

  public void lamp2Position3() {
    uArm2Translate.setTransform(Mat4Transform.translate(-1.5f, 3.25f, 0f));
    uArm2Translate.update();

    rotateHead2.setTransform(Mat4Transform.rotateAroundZ(-20));
    rotateHead2.update();

    rotateUpper2.setTransform(Mat4Transform.rotateAroundZ(this.upperAngle2));
    //rotateUpper.setTransform(Mat4Transform.translate(0, 0, 0.5f));
    rotateUpper2.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerY2.setTransform(Mat4Transform.rotateAroundY(-0));
    rotateLowerY2.update();

    //rotateHead.setTransform(Mat4Transform.rotateAroundZ(30));
    rotateLowerZ2.setTransform(Mat4Transform.rotateAroundZ(-30));
    rotateLowerZ2.update();

    rotateLamp2.setTransform(Mat4Transform.rotateAroundY(45));
    rotateLamp2.update();

    lamp2MoveTranslate.setTransform(Mat4Transform.translate(5f, 0f, -3f));
    lamp2MoveTranslate.update();
    
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