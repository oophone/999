// a_position is a default name for position attribute (set programatically as ShaderProgram.POSITION_ATTRIBUTE in java code)
attribute vec3 a_position;
// coordinates of texture 0 "pixel"
attribute vec2 a_texCoord0;

// projection view transformation matrix (set programatically in java code)
uniform mat4 u_projViewTrans;

// world transformation matrix (set programatically in java code)
uniform mat4 u_worldTrans;
// move matrix
uniform mat4 u_moveMatrix;

// texture coordinate passed to vertex shader (set in main())
varying vec2 v_texCoord0;
 
 uniform sampler2D normalTexture;
 
 // a_color is a default name for color attribute (set programatically as ShaderProgram.COLOR_ATTRIBUTE in java code)
attribute vec3 a_color;
// a_normal is a default name for normal vector attribute (set programatically as ShaderProgram.NORMAL_ATTRIBUTE in java code)
attribute vec3 a_normal;


// vertex position passed to fragment shader (set in main())
varying vec3 v_Position;
// vertex color passed to fragment shader (set in main())
varying vec4 v_Color;
// vertex color passed to fragment shader (set in main())
varying vec3 v_Normal;
const float PI = 3.1415926535897932384626433832795;

const float waveLength = 4.0;
const float waveAmplitude = 0.2;
uniform float iTime;
varying vec3 incident;
float generateOffset(float x, float z, float val1, float val2){
	float radiansX = ((mod(x+z*x*val1, waveLength)/waveLength) + iTime * mod(x * 0.8 + z, 1.5)) * 2.0 * PI;
	float radiansZ = ((mod(val2 * (z*x +x*z), waveLength)/waveLength) + iTime * 2.0 * mod(x , 2.0) ) * 2.0 * PI;
	return waveAmplitude * 0.5 * (sin(radiansZ) + cos(radiansX));
}

vec3 applyDistortion(vec3 vertex){
	float xDistortion = generateOffset(vertex.x, vertex.z, 0.2, 0.1);
	float yDistortion = generateOffset(vertex.x, vertex.z, 0.1, 0.3);
	float zDistortion = generateOffset(vertex.x, vertex.z, 0.15, 0.2);
	return vertex + vec3(xDistortion, yDistortion, zDistortion);
}



void main() {
    // pass texture coordinate to fragment shader
    v_texCoord0 = a_texCoord0;
    
    vec3 currentVertex = vec3(a_position);
	currentVertex = applyDistortion(currentVertex);
   
   
   
    vec4 vertex = u_worldTrans*vec4(currentVertex, 0.0);
	vec4 u_camera=u_projViewTrans*vec4(currentVertex, 1.0);
	incident = normalize(vec3(u_camera));

    
  // compute vertex position on screen (rotate it using rotation maxtrix)
    v_Position = -vec3(u_projViewTrans * vec4(currentVertex, 0.0));
    // compute normal position on screen (rotate it using rotation maxtrix)
    v_Normal = vec3(u_projViewTrans * vec4(a_normal, 0.0));
    // reassign color to v_Color so it can be passed to fragment shader
    v_Color = vec4(.4,.4,.4, .4);
   
  /*  vec3 textNormal = normalize(texture2D(normalTexture, a_texCoord0).rgb*2.0 - 1.0);

    vec3 worldNormal = vec3(mat3(u_projViewTrans[0].xyz, u_projViewTrans[1].xyz, u_projViewTrans[2].xyz) * (a_normal+textNormal));

     v_Normal = worldNormal;*/
	
    
    // compute vertex position on screen
    gl_Position = u_projViewTrans * u_worldTrans * u_moveMatrix * vec4(currentVertex, 1.0);
}
