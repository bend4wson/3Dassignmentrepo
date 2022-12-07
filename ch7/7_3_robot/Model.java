import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

public class Model {
  
  private Mesh mesh;
  private int[] textureId1; 
  private int[] textureId2; 
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private Light[] lightArray;
  private double startTime;
  
  public Model(GL3 gl, Camera camera, Light[] lightArray, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.lightArray = lightArray;
    //this.light = light;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  public Model(GL3 gl, Camera camera, Light[] lightArray, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1) {
    this(gl, camera, lightArray, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, Light[] lightArray, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, lightArray, shader, material, modelMatrix, mesh, null, null);
  }

  // public Model(GL3 gl, Camera camera, Light[] lightArray, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
  //   this(gl, camera, lightArray, shader, material, modelMatrix, mesh, null, null);
  // }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  // public void setLight(Light light) {
  //   this.light = light;
  // }

  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }

//   public void render(GL3 gl, Mat4 modelMatrix) {
//     Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
//     shader.use(gl);
//     shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
//     shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
//     shader.setVec3(gl, "viewPos", camera.getPosition());

//     shader.setVec3(gl, "light.position", light.getPosition());
//     shader.setVec3(gl, "light.ambient", light.getMaterial().getAmbient());
//     shader.setVec3(gl, "light.diffuse", light.getMaterial().getDiffuse());
//     shader.setVec3(gl, "light.specular", light.getMaterial().getSpecular());

//     shader.setVec3(gl, "material.ambient", material.getAmbient());
//     shader.setVec3(gl, "material.diffuse", material.getDiffuse());
//     shader.setVec3(gl, "material.specular", material.getSpecular());
//     shader.setFloat(gl, "material.shininess", material.getShininess());  
// //---------
//     if (textureId1!=null) {
//       shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
//       gl.glActiveTexture(GL.GL_TEXTURE0);
//       gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
//     }
//     if (textureId2!=null) {
//       shader.setInt(gl, "second_texture", 1);
//       gl.glActiveTexture(GL.GL_TEXTURE1);
//       gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
//     }
//     mesh.render(gl);
//   } 
//   //--------



//------------------
public void render(GL3 gl, Mat4 modelMatrix, Boolean moving) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());

    shader.setVec3(gl, "viewPos", camera.getPosition());

    if (moving) {
      calculateOffset(gl);
      shader.setVec3(gl, "material.specular", new Vec3(0f, 0f, 0f));
    } 

    //------NEW---------
    System.out.println("Amount of lights: " + this.lightArray.length); //.size());
    for (int i = 0; i < this.lightArray.length; i++) { //.size(); i++) {

      Light light = this.lightArray.get(i);

      boolean lightIsSpotlight = light instanceof SpotLight;

      String target;

      if (lightIsSpotlight) {
        target = String.format("spotLights[%d].", i-2);
      } else {
        target = String.format("pointLights[%d].", i);
      }
      
      System.out.println("Light position: " + light.getPosition());
      shader.setVec3(gl, "position: " + target, light.getPosition());
      Material m = new Material(new Vec3(), new Vec3(), new Vec3(), 0);
      if (light.isOn()) {
        m = light.getMaterial();
      }


      shader.setVec3(gl, target + "ambient", m.getAmbient());
      shader.setVec3(gl, target + "diffuse", m.getDiffuse());
      shader.setVec3(gl, target + "specular", m.getSpecular());

      shader.setFloat(gl, target + "constant", light.getAttenuationConstant());
      shader.setFloat(gl, target + "linear", light.getAttenuationLinear());
      shader.setFloat(gl, target + "quadratic", light.getAttenuationQuadratic());

      if (lightIsSpotlight) {
        SpotLight sLight = (SpotLight) light;
        shader.setVec3(gl, target + "direction: ", sLight.getDirection());
        shader.setFloat(gl, target + "cutOff: ", sLight.getCutOff());
        shader.setFloat(gl, target + "outerCutOff: ", sLight.getOuterCutOff());
      }

    }

    shader.setFloat(gl, "material.shininess", material.getShininess());
    if (textureId1 != null) {
      shader.setInt(gl, "material.diffuse", 0); // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
      // textureId1.bind(gl); // uses JOGL Texture class
    }
    if (textureId2 != null) {
      shader.setInt(gl, "material.specular", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
      // textureId2.bind(gl); // uses JOGL Texture class
    }
    mesh.render(gl);
  }


  public void render(GL3 gl, boolean moving) {
    render(gl, modelMatrix, moving); 
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    render(gl, modelMatrix, false);
  }

//------------------



  // // public void dispose(GL3 gl) {
  // //   mesh.dispose(gl);
  // //   if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
  // //   if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  // // }

  // public void render(GL3 gl, Mat4 modelMatrix, Boolean moving) {
  //   Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
  //   shader.use(gl);
  //   shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
  //   shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
  //   shader.setVec3(gl, "viewPos", camera.getPosition());

  //   if (moving) {
  //     calculateOffset(gl);
  //     shader.setVec3(gl, "material.specular", new Vec3(0f, 0f, 0f));
  //   }

  //   //System.out.println("******!!!!!");

  //   shader.setVec3(gl, "light.position", light.getPosition());
  //   shader.setVec3(gl, "light.ambient", light.getMaterial().getAmbient());
  //   shader.setVec3(gl, "light.diffuse", light.getMaterial().getDiffuse());
  //   shader.setVec3(gl, "light.specular", light.getMaterial().getSpecular());

  //   shader.setVec3(gl, "material.ambient", material.getAmbient());
  //   shader.setVec3(gl, "material.diffuse", material.getDiffuse());
  //   shader.setVec3(gl, "material.specular", material.getSpecular());
  //   shader.setFloat(gl, "material.shininess", material.getShininess());  

  //   if (textureId1!=null) {
  //     shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
  //     gl.glActiveTexture(GL.GL_TEXTURE0);
  //     gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
  //   }
  //   if (textureId2!=null) {
  //     shader.setInt(gl, "second_texture", 1);
  //     gl.glActiveTexture(GL.GL_TEXTURE1);
  //     gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
  //   }
  //   mesh.render(gl);
  // } 





  private void calculateOffset(GL3 gl) {
    double elapsedTime = getSeconds() - startTime;
    double time = elapsedTime * 0.025;
    float offsetX = (float) (time - Math.floor(time));
    float offsetY = 0.0f;
    shader.setFloat(gl, "offset", offsetX, offsetY);
  }
  
  private double getSeconds() {
    return System.currentTimeMillis() / 1000.0;
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
    if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  }
  
}