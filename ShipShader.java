package agh.edu.pl.gmiejski.x006_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.graphics.Cubemap;
import static com.badlogic.gdx.Gdx.input;
import com.badlogic.gdx.math.*;

/**
 * Our Shader program is responsible for drawing itself based on input and shaders program.
 * Also holds the texture its rendering.
 * <p>
 * we implement Shader interface to be able to delegate required actions from main program to ShipShader.
 */
public class ShipShader implements Shader {
	Cubemap ref;
    private ShaderProgram program;
    private Texture texture,nomal;
    private float xPos, yPos;
    private Vector2 speed = new Vector2(0.1f, 0.1f);
	float iTime;
	public void loadReflection(String refl){
        ref=new Cubemap(Gdx.files.internal("cubemaps/" + refl + "_c00.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c01.bmp"),
						Gdx.files.internal("cubemaps/" + refl + "_c02.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c03.bmp"),
						Gdx.files.internal("cubemaps/" + refl + "_c04.bmp"), Gdx.files.internal("cubemaps/" + refl + "_c05.bmp"));
        ref.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        ref.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
    }
    @Override
    public void init() {
        // load shaders and compile them to ShaderProgram
        String vert = Gdx.files.internal("006_model/vertex.glsl").readString();
        String frag = Gdx.files.internal("006_model/fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        // load texture - which is our ship's .png file
        texture = new Texture("water.png");
		loadReflection("rnl_phong_m00");
		//nomal=new Texture("nomal.png");
    }

    @Override
    public void render(Renderable renderable) {
        // we bind our ship's texture to shader program
        bindTexture();
        program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
		program.setUniformf("iTime", iTime+=Gdx.graphics.getDeltaTime());
        // then we render our renderable's mesh using our shader program and renderables details
        renderable.meshPart.mesh.render(program,
                renderable.meshPart.primitiveType,
                renderable.meshPart.offset,
                renderable.meshPart.size);
    }


    @Override
    public void begin(Camera camera, RenderContext context) {
        // this method is called from between context.begin() and .end()
        // we begin to draw our shader program associated with our ship
        program.begin();

        // we set our projection matrix from camera passed from main app and the translation matrix - based on user input
        program.setUniformMatrix("u_projViewTrans", camera.combined);
        program.setUniformMatrix("u_moveMatrix", newTranslationMatrix().translate(-50+10*MathUtils.sinDeg(30*iTime),0,-50));

        // set depth and cull face for proper rendering
        context.setDepthTest(GL20.GL_LEQUAL);
        context.setCullFace(1);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return false;
    }

    @Override
    public void dispose() {
        program.dispose();
    }

    private void bindTexture() {
        // before rendering we need to bind a texture to fragment shader, in order to fill our model with it's proper parts
        // we set the active texture as the one with index 0
        Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE1);
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE2);
        // we bind our texture on 0 index
        texture.bind(0);
		ref.bind(1);
		//nomal.bind(2);
		//Gdx.gl.glCopyTexImage2D(0,0,100,100,0,GL20.GL_RGB,GL20.GL_UNSIGNED_BYTE,0);
		//Gdx.gl.glCopyTexSubImage2D(0,0,100,100,0,GL20.GL_RGB,GL20.GL_UNSIGNED_BYTE,100);
		
	
        // we pass it to our shader vertex program and later to fragment shader by name "u_texture"
        program.setUniformi("u_texture", 0);
		program.setUniformi("sCubemapTexture", 1);
		//program.setUniformi("normalTexture", 2);
		
    }

    private Matrix4 newTranslationMatrix() {
        if (input.isKeyPressed(Input.Keys.UP)) {
            yPos += speed.y;
        } else if (input.isKeyPressed(Input.Keys.DOWN)) {
            yPos -= speed.y;
        }
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            xPos += speed.x;
        } else if (input.isKeyPressed(Input.Keys.LEFT)) {
            xPos -= speed.x;
        }
        if (input.isKeyPressed(Input.Keys.SPACE)) {
            resetPosition();
        }

        return new Matrix4(new float[]{
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                xPos, yPos, 0f, 1f});
    }

    private void resetPosition() {
        xPos = 0f;
        yPos = 0f;
    }
}
