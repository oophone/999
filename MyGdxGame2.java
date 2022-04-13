package com.glitch.template.core;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.utils.*;
import io.github.srjohnathan.gdx.effekseer.core.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;
import io.github.srjohnathan.gdx.effekseer.core.*;
import com.glitch.template.core.TestShader.DoubleColorAttribute;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.glitch.template.core.ShipShader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g3d.particles.renderers.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
public class MyGdxGame2 extends ApplicationAdapter {
    public Environment environment,environment2;//可以包含点光源集合和线光源集合
    public PerspectiveCamera cam;//3D视角
    public FirstPersonCameraController camController;//视角控制器
	public Template hub;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public ModelBatch modelBatch,
	modelBatch2
	
	;
	private World world;
	private Body dogBody,dogBody2;
	private float reduce = 100;// 缩小100 倍易于观察到物理现象
    private boolean isJump;
	public Vector2 position,position2;
	b2de w1,w2;
	Vector2 linearVelocity;
    public boolean loading;
	private AnimationController controller;
    AIDETemplate cre;
	Model ship2;
	int fangxiang;
	private Box2DDebugRenderer debugRenderer;
	ParticleEffekseer effekseer,effekseer2;
	EffekseerManager manager,manager2;
	public Stage stage;
	// 按钮 弹起 状态的纹理
	private Texture upTexture;
	Renderable obj;
	// 按钮 按下 状态的纹理
	private Texture downTexture;
	ShapeRenderer rend;
	// 按钮
	public Button button,button2,button3;
	public Touchpad touchPad;
	TouchpadStyle style2;
	TextureRegionDrawable background;
	TextureRegionDrawable knobRegion;
    Matrix4 mrt4;
	Texture texture;
	float angle;
	private ShaderProgram shader;
    private Texture shaderTexture,tex;
    private SpriteBatch batch;
    public FrameBuffer fbo;

    private float shaderTime, aspectRatio;
    private int fboScale;
	ShaderTest s2;
    private FitViewport viewport;
    private Stage info;
    Vector2 iMouse666;
	// Map
    public static final int MAP_WIDTH = 3200;
    public static final int MAP_HEIGHT = 2000;
    public static float map_dx;
    public static float map_dy;

    //Window attributes
    public static int WINDOW_RATIO = 8;  //8
    public static int WINDOW_WIDTH = MAP_WIDTH/ WINDOW_RATIO;
    public static int WINDOW_HEIGHT = MAP_HEIGHT/ WINDOW_RATIO;
    public RenderContext renderContext;
    TextureAttribute textureAttribute1;
    public Renderable renderable;
	Material material,
	material3,
	material2;
	Cubemap cubemap;
	ShipShader ship999;
    @Override
    public void create () {
		
		modelBatch = new ModelBatch(new DefaultShaderProvider(
										Gdx.files.internal("shaders/vertex.glsl"),
										Gdx.files.internal("shaders/fragment.glsl")));
		
    
		modelBatch2= new ModelBatch(
			Gdx.files.classpath("com/badlogic/gdx/graphics/g3d/shaders/default.vertex.glsl").readString(), 
			Gdx.files.internal("shaders/cel.main.fragment.glsl").readString());
		
		

	    cubemap = new Cubemap(
			Gdx.files.internal("environment/environment_01_PX.png"),
			Gdx.files.internal("environment/environment_01_NX.png"),
			Gdx.files.internal("environment/environment_01_PY.png"),
			Gdx.files.internal("environment/environment_01_NY.png"),
			Gdx.files.internal("environment/environment_01_PZ.png"),
			Gdx.files.internal("environment/environment_01_NZ.png"), false);
		

		

		
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .7f, .7f, .7f, 1f));//环境光
		environment.add(new DirectionalLight().set(.7f, .7f, .8f, -.5f, .3f, -.5f));//直线光源
		environment.add(new DirectionalLight().set(.7f, .7f, .8f, .5f, -1f, .5f));//直线光源
		environment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, cubemap));
		
		
		environment2 = new Environment();
        environment2.set(new ColorAttribute(ColorAttribute.AmbientLight, .6f, .6f, .6f, 1f));//环境光
		
		environment2.add(new DirectionalLight().set(.7f, .7f, .7f, -.5f, -1f, -.5f));//直线光源

		
		
		
		
		rend = new ShapeRenderer();
        
		
		
		
        assets = new AssetManager();
		mrt4=new Matrix4();
		ship999=new ShipShader();
		ship999.init();
		
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//67可以理解成一个定值，视角宽度（67度）
        
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
		
		aspectRatio = (float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        WINDOW_HEIGHT = (int) ((int) WINDOW_WIDTH/aspectRatio);
        viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT);
        viewport.apply();

        info = new Stage(viewport);




		fboScale = 5;

        shaderTime = 0;
        batch = new SpriteBatch();
		
		
		
		
		
		hub();
		// 创建一个世界，里面的重力加速度为 10
		world = new World(new Vector2(0, 0), false);





		// 再添加一个动态物体，可以把他看成玩家
		BodyDef dogBodyDef = new BodyDef();
		dogBodyDef.type = BodyDef.BodyType.DynamicBody;
		dogBodyDef.position.x = 0;
		dogBodyDef.position.y = 0;
		dogBody = world.createBody(dogBodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(50 / 2 / reduce, 50 / 2 / reduce);

		// 给物体添加一些属性
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;// 形状
		fixtureDef.restitution = 0.2f; // 设置这个值后，物体掉落到地面就会弹起一点高度...
		dogBody.createFixture(fixtureDef).setUserData(this);//设置自定义数据可以从这个物体获取这个数据对象

		w1=new b2de(dogBody,30);
		dynamicBox.dispose();



		BodyDef dogBodyDef2 = new BodyDef();
		dogBodyDef2.type = BodyDef.BodyType.DynamicBody;
		dogBodyDef2.position.x = 20;
		dogBodyDef2.position.y = 20;
		dogBody2 = world.createBody(dogBodyDef2);
		PolygonShape dynamicBox2 = new PolygonShape();
		dynamicBox2.setAsBox(50 / 2 / reduce, 50 / 2 / reduce);

		// 给物体添加一些属性
		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = dynamicBox2;// 形状
		fixtureDef2.restitution = 0.2f; // 设置这个值后，物体掉落到地面就会弹起一点高度...
		dogBody2.createFixture(fixtureDef2).setUserData(this);//设置自定义数据可以从这个物体获取这个数据对象

		w2=new b2de(dogBody2,30);
		dynamicBox2.dispose();

		position2 = dogBody2.getPosition();
		position = dogBody.getPosition();
		//  camController = new CameraInputController(cam);
		// Gdx.input.setInputProcessor(camController);
		final Seek<Vector2> seekSB = new Seek<Vector2>(w2, w1);
		w2.setSteeringBehavior(seekSB);

		
		EffekseerGdx.init();

		manager = new EffekseerManager(cam, EffekseerCore.TypeOpenGL.OPEN_GLES2);
		effekseer = new ParticleEffekseer(manager);
		effekseer.setMagnification(1f);
		try {

			// true = InternalStorage
			// false = ExternalStorage

			effekseer.load("Tktk01/Tktk01_Fire3.efkefc", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        assets.load("111/convertedModel.g3dj", Model.class);
		assets.load("222/convertedModel.g3dj", Model.class);
		assets.load("ship/ship.obj", Model.class);
		
    }

    private void doneLoading() {
        
		
		
		// You use an AnimationController to um, control animations.  Each control is tied to the model instance
		//controller = new AnimationController(instances.get(0));  
		// Pick the current animation by name
	
				Model ship = assets.get("111/convertedModel.g3dj", Model.class);
				ModelInstance shipInstance = new ModelInstance(ship);
				Model ship2 = assets.get("222/convertedModel.g3dj", Model.class);
				ModelInstance shipInstance2 = new ModelInstance(ship2);
	Model ship3 = assets.get("ship/ship.obj", Model.class);
		ModelInstance shipInstance3 = new ModelInstance(ship3);
		
		
				instances.add(shipInstance);
				instances.add(shipInstance2);
		instances.add(shipInstance3);
		loading = false;
    }
	public static Renderable createRenderableFromMesh(Mesh mesh, Material material, Shader shader, Environment environment) {
		Renderable outRend=new Renderable();
		outRend.meshPart.mesh=mesh;
		outRend.meshPart.primitiveType=GL20.GL_TRIANGLES;
		if(material!=null) outRend.material=material;
		if(environment!=null) outRend.environment=environment;
		outRend.meshPart.offset=0;
		//strada.shader=elrShader;
		if(shader!=null) outRend.shader=shader;
		outRend.meshPart.size=mesh.getNumIndices();
		return outRend;
	}
    @Override
    public void render () {
		
        
		if( !assets.update() ){
            rend.setColor( Color.BLUE );
            rend.begin( ShapeType.Line );
            rend.rect( Gdx.graphics.getWidth()/4-1, (Gdx.graphics.getHeight()-20)/2-1, Gdx.graphics.getWidth()/2+2, 20+2 );
            rend.end();

            rend.setColor( Color.GREEN );
            rend.begin( ShapeType.Filled );
            rend.rect( Gdx.graphics.getWidth()/4, (Gdx.graphics.getHeight()-20)/2, Gdx.graphics.getWidth()/2*assets.getProgress(), 20  );
            rend.end();
			loading = true;
        }
        else{
			if (loading){

				doneLoading();
				batch.setProjectionMatrix(viewport.getCamera().combined);
				fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth() / fboScale, Gdx.graphics.getHeight() / fboScale, false);
				Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth() / fboScale, Gdx.graphics.getHeight() / fboScale, Pixmap.Format.RGBA8888);
				shaderTexture = new Texture(pixmap);
				pixmap.dispose();
				ShaderProgram.pedantic = false;
				shader = new ShaderProgram(batch.getShader().getVertexShaderSource(), Gdx.files.internal("shaders/water.frag").readString());

				if (!shader.isCompiled()) {
					System.out.println("Error compiling shader: " + shader.getLog());
				}
				
				Timer timer = new Timer();
				Timer.Task t=new Timer.Task(){
					@Override
					public void run(){
						
						zou(position2.x,-position2.y);
						
						instances.get(0).transform.setToTranslation(position.x,0,-position.y).rotate(Vector3.Y,angle-90);
						
						instances.get(2).transform.setToTranslation(position.x,1,-position.y).rotate(Vector3.Y,angle-90)/*.scale(.1f,.1f,.1f)*/;
						
						effekseer.transform.setToTranslation(instances.get(0).transform.getTranslation(new Vector3()) );
						
						
							
						material = instances.get(0).materials.get(6);
						material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),IntAttribute.createCullFace(GL20.GL_NONE),new FloatAttribute(FloatAttribute.AlphaTest, .1f)
									 ,new DepthTestAttribute(false));
									 
						material2 = instances.get(0).materials.get(0);
						material2.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),IntAttribute.createCullFace(GL20.GL_NONE),new FloatAttribute(FloatAttribute.AlphaTest, .5f)
									 ,new DepthTestAttribute(false));
						
						material3 = instances.get(2).materials.get(0);
						material3.set(material2);
									 
						
/*
						material = instances.get(0).materials.get(0);
						material.set(textureAttribute1);
						material2 = instances.get(0).materials.get(1);
						material2.set(textureAttribute1);*/
					}


				};

				timer.scheduleTask(t, 0, .001f);
                camController.update();

			}
            
			
			
			
			Gdx.gl.glClearColor( 1, 0, 0, 1 );
			// camController.update();
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			// You need to call update on the animation controller so it will advance the animation.  Pass in frame delta
			
			
			
			
			modelBatch2.begin(cam);
			modelBatch.begin(cam);
			modelBatch.render(instances.get(2),environment);
			
			modelBatch2.render(instances.get(1),environment);
			modelBatch.render(instances.get(0),environment);
			modelBatch2.end();
			
			modelBatch.end();


			
			viewport.apply();
			batch.setProjectionMatrix(viewport.getCamera().combined);
			fbowith();
			
			 
			
			
			batch.setShader(null);
			
			hubrender();
			world.step(1 / 60f, 6, 2);
			//controller.update(Gdx.graphics.getDeltaTime());

			if(!effekseer.isPlay())
			{effekseer.play();	}
			manager.draw(Gdx.graphics.getDeltaTime());

			position = dogBody.getPosition();
			position2 = dogBody2.getPosition();
			w2.update(Gdx.graphics.getDeltaTime());
			cam.position.set(position.x, 2.2f, -position.y-2.2f);
			cam.lookAt(position.x, 2, -position.y);

			linearVelocity = dogBody.getLinearVelocity();

			if(touchPad.isTouched()){//判断摇杆是否被按下
				if (touchPad.getKnobPercentX()<0&& linearVelocity.x <= 2) { // 现在最大速度为 2，不然会放飞自我
					// 施加冲动 让物体运行起来，可以看成我们推一下物体就往一边移动了
					dogBody.applyLinearImpulse(new Vector2(0.1f, 0), dogBody.getWorldCenter(), true);
					fangxiang=1;
				}
				if (touchPad.getKnobPercentX()>0&& linearVelocity.x >= -2) {
					dogBody.applyLinearImpulse(new Vector2(-0.1f, 0), dogBody.getWorldCenter(), true);
					fangxiang=2;
				}
				if (touchPad.getKnobPercentY()<0&& linearVelocity.y <= 2) {
					dogBody.applyLinearImpulse(new Vector2(0, 0.1f), dogBody.getWorldCenter(), true);
					fangxiang=3;
				}
				if (touchPad.getKnobPercentY()>0&& linearVelocity.y >=-2) {
					dogBody.applyLinearImpulse(new Vector2(0, -0.1f), dogBody.getWorldCenter(), true);
					fangxiang=4;
				}

				Vector2 v = new Vector2(touchPad.getKnobPercentX(), touchPad.getKnobPercentY());
				angle = v.angle();

				/*controller.setAnimation("run",1, new AnimationListener(){

				 @Override
				 public void onEnd(AnimationDesc animation) {
				 // this will be called when the current animation is done. 
				 // queue up another animation called "balloon". 
				 // Passing a negative to loop count loops forever.  1f for speed is normal speed.
				 //controller.queue("balloon",-1,1f,null,0f);
				 }

				 @Override
				 public void onLoop(AnimationDesc animation) {
				 // TODO Auto-generated method stub

				 }
				 >
				 });*/

			}
			else if(!touchPad.isTouched()){
				dogBody.setLinearVelocity(0, 0);
				/*controller.setAnimation("idle",1, new AnimationListener(){

				 @Override
				 public void onEnd(AnimationDesc animation) {
				 // this will be called when the current animation is done. 
				 // queue up another animation called "balloon". 
				 // Passing a negative to loop count loops forever.  1f for speed is normal speed.
				 //controller.queue("balloon",-1,1f,null,0f);
				 }

				 @Override
				 public void onLoop(AnimationDesc animation) {
				 // TODO Auto-generated method stub

				 }

				 });*/

			}
			
        }
   
		
		
    }
	
	public void fbowith(){
		fbo.begin();
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
		upTexture.bind(0);
		final float cursorX = (float)Gdx.input.getX() / (float)Gdx.graphics.getWidth();
		final float cursorY = 1f - ((float)Gdx.input.getY() / (float)Gdx.graphics.getHeight());
        shaderTime += Gdx.graphics.getDeltaTime();
        batch.setShader(shader);
        shader.begin();
		shader.setUniformf("u_cursor", cursorX, cursorY);
		shader.setUniformi("u_texture", 0);
        shader.setUniformf("u_time", shaderTime);
        shader.setUniformf("u_resolution", new Vector2(fbo.getWidth(),fbo.getHeight()));
		shader.end();
        batch.begin();
        batch.draw(shaderTexture,0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        batch.end();
        fbo.end();
	}
	
	public void zou(float a,float b){
		instances.get(1).transform.setToTranslation(a,0,b);
	}
	public void hub(){
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		camController = new FirstPersonCameraController(cam);
		
       
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(camController);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		
		
		
		// 将输入处理设置到舞台（必须设置, 否则点击按钮没效果）
     //   Gdx.input.setInputProcessor(stage);


        /*
		 * 第 1 步: 创建 弹起 和 按下 两种状态的纹理
		 */
		upTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
		downTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
		
		/*
		 * 第 2 步: 创建 ButtonStyle
		 */
		Button.ButtonStyle style = new Button.ButtonStyle();

		// 设置 style 的 弹起 和 按下 状态的纹理区域
		style.up = new TextureRegionDrawable(new TextureRegion(upTexture));
		style.down = new TextureRegionDrawable(new TextureRegion(downTexture));

		/*
		 * 第 3 步: 创建 Button
		 */
		button = new Button(style);
        button2= new Button(style);
		button3= new Button(style);
		// 设置按钮的位置
		button.setPosition(Gdx.graphics.getWidth()*9/10,Gdx.graphics.getHeight()*8/10);
		button.setWidth(80);
		button.setHeight(80);
		button2.setPosition(Gdx.graphics.getWidth()*9/10,Gdx.graphics.getHeight()*6/10);
		button2.setWidth(80);
		button2.setHeight(80);
		button3.setPosition(Gdx.graphics.getWidth()*9/10,Gdx.graphics.getHeight()*4/10);
		button3.setWidth(80);
		button3.setHeight(80);
		// 给按钮添加点击监听器




		texture = new Texture(Gdx.files.internal("badlogic.jpg"));


		background = new TextureRegionDrawable(new TextureRegion(texture, 0, 0,128,128));
		knobRegion = new TextureRegionDrawable(new TextureRegion(texture,128,0,128,128));

		style2 = new TouchpadStyle(background, knobRegion);

		touchPad = new Touchpad(30, style2);//初始化游戏摇杆。(摇杆触碰区域的半径大小,TouchPagStyle)
		touchPad.setBounds(50, 50, 150, 150);//设置摇杆的位置和大小



		/*
		 * 第 4 步: 添加 button 到舞台
		 */
        stage.addActor(touchPad);
		stage.addActor(button);
		stage.addActor(button2);
		stage.addActor(button3);


	}



	public void hubrender(){

		// 更新舞台逻辑
		stage.act();
		// 绘制舞台
		stage.draw();

	}
    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
		cubemap.dispose();
        super.dispose();
    }
}
