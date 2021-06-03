#ifdef GL_ES 
precision mediump float;
#endif

// sampler for texture number 0 (ship texture)
uniform sampler2D u_texture;
uniform samplerCube sCubemapTexture;
// texture coordnate of texture 0 (set in vertex shader)
varying vec2 v_texCoord0;
 const float Eta = 0.15; // Water
 
varying vec3 incident;
uniform float iTime;
varying vec3 v_Position;
varying vec4 v_Color;
varying vec3 v_Normal;
void main() {
     vec3 u_LightPos=vec3(-50.0, -3.0, -50.0);
     // compute and normalize light vector
    float distance = length(u_LightPos - v_Position);
    vec3 lightVector = normalize(u_LightPos - v_Position);

    // compute dot product between normal vector and light vector
    // consider only values 0.1 or higher so dark fragments are not totally black
    float diffuse = max(dot(v_Normal, lightVector), 0.1);
    vec3 r=reflect(v_Normal, lightVector);
    vec3 v=lightVector;
    float S = pow( max(dot(r,v),0.1), 2.0);


    vec3 v_refraction=refract(incident, v_Normal,Eta);
    vec3  v_reflection = reflect(incident, v_Normal);
    float fresnel = Eta + (1.0 - Eta) * pow(max(0.0, 1.0 - dot(-incident,v_Normal)), 5.0);
	
    
    // make more distant points darker
    diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));
    S = S * (1.0 / (1.0 + (0.25 * distance * distance)));
    
    
			
    vec4 diff = v_Color * diffuse;
    vec4 spec = v_Color * S;
     vec4 refractionColor = textureCube(sCubemapTexture, normalize(v_refraction))
     +texture2D(u_texture,v_texCoord0);
    
    vec4 reflectionColor = textureCube(sCubemapTexture, normalize(v_reflection))
    +texture2D(u_texture,v_texCoord0);
    
	vec4 col=mix(refractionColor,reflectionColor,fresnel);
    // bind texture "pixel" to one fragment using texture2D function
    col.a=.5;
    gl_FragColor =col+spec+diff;
    
}
