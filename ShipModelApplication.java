package agh.edu.pl.gmiejski.x006_model;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;

import java.util.function.BiFunction;
/**
 * In this tutorial we load a 3D model and move it around the screen.
 * <p>
 * We also bind textures to vertex and fragment shader
 */
public class ShipModelApplication extends InputAdapter implements ApplicationListener {
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ShipShader ship,ship2;
    public RenderContext renderContext;
    public Model model;
    public Environment environment;
    public Renderable renderable,shipreb;

    @Override
    public void create() {
        // first we create an environment. Here we set the light type and color
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // we set a perspective camera, its position, direction and depth of view
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 0f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = .1f;
        cam.far = 3000f; // object further than 300f distance will not be rendered
        cam.update();

        // we set camera controller for simple camera movement using mouse
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
		wb=new ModelBuilder();
		createWaterSurface(wb);
		
        // Next we load our model from a obj file. If you want to learn about .obj format search in the internet.
        // Generally .obj is a description of vertexes and together with .mtl file and .png we know where to render each part of the .png file
        ModelLoader modelLoader = new ObjLoader();
        model = modelLoader.loadModel(Gdx.files.internal("006_model/data/ship.obj"));

        // we take the first block part from the model. The structure depends on how the model has bean saved (how .obj and .mtl files are configured).
        NodePart blockPart = water.nodes.get(0).parts.get(0);

        // we create a new renderable - our new renderable in this case
        renderable = new Renderable();
        blockPart.setRenderable(renderable);
        renderable.environment = environment;
        renderable.worldTransform.idt();
       
		NodePart blockPart2 = model.nodes.get(0).parts.get(0);
		shipreb = new Renderable();
        blockPart2.setRenderable(shipreb);
        shipreb.environment = environment;
        shipreb.worldTransform.idt();
        // this time we're not using shader programs in our Main as it's been moved to ShipShader - we use render context instead.
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

		
		
		// create new ship shader for rendering
        ship = new ShipShader();
        // init for loading its shaders and texture
        ship.init();
    }
	ModelBuilder wb,pb;
	Model water,planetGround;
	private void createWaterSurface(ModelBuilder modelBuilder) {
        modelBuilder.begin();
        MeshPartBuilder waterBuilder = modelBuilder.part("waterSurface", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
        createXZPlane(waterBuilder, new Vector2(2, 2), new Vector2(100, 100), 50, 50,
			new BiFunction<Float, Float, Float>() {
				@Override
				public Float apply(Float x, Float z) {
					return -0.15f;
				}
			});
		//waterBuilder.box(100,100,100);
        water = modelBuilder.end();
        
    }
	
    private void createXZPlane(MeshPartBuilder meshPartBuilder, Vector2 start, Vector2 end, int xSubdivisions, int zSubdivisions, BiFunction<Float, Float, Float> heightFunction) {
        float xStart = start.x;
        float xMultiplier = (end.x - start.x) / xSubdivisions;
        float zStart = start.y;
        float zMultiplier = (end.y - start.y) / zSubdivisions;

        Vector3[] posTemp = new Vector3[4];
        for (int i = 0; i < posTemp.length; i++) {
            posTemp[i] = new Vector3();
        }

        MeshPartBuilder.VertexInfo[] vertexInfo = new MeshPartBuilder.VertexInfo[6];
        for (int i = 0; i < vertexInfo.length; i++) {
            vertexInfo[i] = new MeshPartBuilder.VertexInfo();
        }

        Vector3[] normalTemp = new Vector3[2];
        for (int i = 0; i < normalTemp.length; i++) {
            normalTemp[i] = new Vector3();
        }

        for (int x = 0; x < xSubdivisions; x++) {
            for (int z = 0; z < zSubdivisions; z++) {
                float x1 = xStart + xMultiplier * x;
                float x2 = xStart + xMultiplier * (x + 1);
                float z1 = zStart + zMultiplier * z;
                float z2 = zStart + zMultiplier * (z + 1);

                posTemp[0].set(x1, heightFunction.apply(x1, z1), z1);
                posTemp[1].set(x1, heightFunction.apply(x1, z2), z2);
                posTemp[2].set(x2, heightFunction.apply(x2, z2), z2);
                posTemp[3].set(x2, heightFunction.apply(x2, z1), z1);

                // Vertices 0, 1, 2 and 2, 3, 0
                normalTemp[0].set(posTemp[1]).sub(posTemp[0]).crs(
					posTemp[2].x - posTemp[1].x, posTemp[2].y - posTemp[1].y, posTemp[2].z - posTemp[1].z).nor();
                normalTemp[1].set(posTemp[3]).sub(posTemp[2]).crs(
					posTemp[0].x - posTemp[3].x, posTemp[0].y - posTemp[3].y, posTemp[0].z - posTemp[3].z).nor();

                vertexInfo[0].set(posTemp[0], normalTemp[0], null, null);
                vertexInfo[1].set(posTemp[1], normalTemp[0], null, null);
                vertexInfo[2].set(posTemp[2], normalTemp[0], null, null);
                vertexInfo[3].set(posTemp[2], normalTemp[1], null, null);
                vertexInfo[4].set(posTemp[3], normalTemp[1], null, null);
                vertexInfo[5].set(posTemp[0], normalTemp[1], null, null);

                meshPartBuilder.triangle(vertexInfo[0], vertexInfo[1], vertexInfo[2]);
                meshPartBuilder.triangle(vertexInfo[3], vertexInfo[4], vertexInfo[5]);
            }
        }
    }
    @Override
    public void render() {
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //now instead of shader program we first begin rendering context
        renderContext.begin();

        // then we begin rendering our ship shader program
        ship.begin(cam, renderContext);
		Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
        // we now match our shader program with a renerable loaded from the model .obj and render it using our ship shader
		ship.render(shipreb);
		ship.render(renderable);
        ship.end();
        renderContext.end();
    }

    @Override
    public void dispose() {
        // we shouldn't forget about disposing our model and ship shader
        ship.dispose();
        model.dispose();
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
