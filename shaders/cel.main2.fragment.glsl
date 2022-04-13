#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif
uniform samplerCube u_environmentCubemap;





#if defined(specularTextureFlag) || defined(specularColorFlag)
#define specularFlag
#endif

#ifdef normalFlag
varying vec3 v_normal;
#endif //normalFlag

#if defined(colorFlag)
varying vec4 v_color;
#endif

#ifdef blendedFlag
varying float v_opacity;
#ifdef alphaTestFlag
varying float v_alphaTest;
#endif //alphaTestFlag
#endif //blendedFlag

#if defined(diffuseTextureFlag) || defined(specularTextureFlag)
#define textureFlag
#endif

#ifdef diffuseTextureFlag
varying MED vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
varying MED vec2 v_specularUV;
#endif

#ifdef diffuseColorFlag
uniform vec4 u_diffuseColor;
#endif

#ifdef diffuseTextureFlag
uniform sampler2D u_diffuseTexture;
#endif

#ifdef specularColorFlag
uniform vec4 u_specularColor;
#endif

#ifdef specularTextureFlag
uniform sampler2D u_specularTexture;
#endif

#ifdef normalTextureFlag
uniform sampler2D u_normalTexture;
#endif

#ifdef lightingFlag
varying vec3 v_lightDiffuse;

#if	defined(ambientLightFlag) || defined(ambientCubemapFlag) || defined(sphericalHarmonicsFlag)
#define ambientFlag
#endif //ambientFlag

#ifdef specularFlag
varying vec3 v_lightSpecular;
#endif //specularFlag

#if defined(ambientFlag) && defined(separateAmbientFlag)
varying vec3 v_ambientLight;
#endif //separateAmbientFlag

#endif //lightingFlag

#ifdef fogFlag
uniform vec4 u_fogColor;
varying float v_fog;
#endif // fogFlag

void main() {
	#if defined(normalFlag)
		vec3 normal = v_normal;
	#endif // normalFlag

	#if defined(diffuseTextureFlag) && defined(diffuseColorFlag) && defined(colorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor * v_color;
	#elif defined(diffuseTextureFlag) && defined(diffuseColorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor;
	#elif defined(diffuseTextureFlag) && defined(colorFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV)* v_color;
	#elif defined(diffuseTextureFlag)
		vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV);
	#elif defined(diffuseColorFlag) && defined(colorFlag)
		vec4 diffuse = u_diffuseColor * v_color;
	#elif defined(diffuseColorFlag)
		vec4 diffuse = u_diffuseColor;
	#elif defined(colorFlag)
		vec4 diffuse = v_color;
	#else
		vec4 diffuse = vec4(1.0);
	#endif

//	float diffIntensity = max(diffuse.r, max(diffuse.g, diffuse.b));
//	float diffFactor;
//	if (diffIntensity > 0.8)
//		diffFactor = 1.0;
//	else if (diffIntensity > 0.5)
//		diffFactor = 0.8;
//	else if (diffIntensity > 0.25)
//		diffFactor = 0.3;
//	else
//		diffFactor = 0.1;

//	diffuse *= diffFactor;

	#if (!defined(lightingFlag))
		gl_FragColor.rgb = diffuse.rgb;
	#elif (!defined(specularFlag))
		#if defined(ambientFlag) && defined(separateAmbientFlag)
			gl_FragColor.rgb = (diffuse.rgb * (v_ambientLight + v_lightDiffuse));
		#else
			gl_FragColor.rgb = (diffuse.rgb * v_lightDiffuse);
		#endif
	#else
		#if defined(specularTextureFlag) && defined(specularColorFlag)
			vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * u_specularColor.rgb * v_lightSpecular;
		#elif defined(specularTextureFlag)
			vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * v_lightSpecular;
		#elif defined(specularColorFlag)
			vec3 specular =v_lightSpecular *u_specularColor.rgb;
		
		#else
			vec3 specular = v_lightSpecular;

		#endif

		float specIntensity = max(specular.r, max(specular.g, specular.b));
		float specFactor;

		if (specIntensity > 0.6)
			specFactor = 1.0;
		else if (specIntensity > 0.3)
			specFactor = 0.5;
		else
			specFactor = 0.1;

		specular *= specFactor;
		
		
float Air = 1.0;
float Glass = 1.51714;


float Eta = Air / Glass;
 

float R0 = ((Air - Glass) * (Air - Glass)) / ((Air + Glass) * (Air + Glass));
		
float fresnel = R0 + (1.0 - R0) * pow((1.0 - dot(-v_lightSpecular, v_normal)), 5.0);
		
vec3 v_refract = refract(v_lightSpecular, v_normal,Eta);
vec3 refractcol=textureCube(u_environmentCubemap, normalize(v_refract)).rgb;
vec3 v_reflect = reflect(v_lightSpecular, v_normal);
vec3 reflectcol=textureCube(u_environmentCubemap, normalize(v_reflect)).rgb;


		
vec3 glasscol=vec3(1,1,1)*mix(refractcol,reflectcol,fresnel);
		
#if defined(ambientFlag) && defined(separateAmbientFlag)
			gl_FragColor.rgb = /*(diffuse.rgb * (v_lightDiffuse + v_ambientLight)) + specular+*/glasscol;
		#else
			gl_FragColor.rgb =/* (diffuse.rgb * v_lightDiffuse) + specular+*/glasscol;
		#endif
	#endif //lightingFlag

	#ifdef fogFlag
		gl_FragColor.rgb = mix(gl_FragColor.rgb, u_fogColor.rgb, v_fog);
	#endif // end fogFlag

	#ifdef blendedFlag
		gl_FragColor.a = diffuse.a * v_opacity;
		#ifdef alphaTestFlag
			if (gl_FragColor.a <= v_alphaTest)
				discard;
		#endif
	#else
		gl_FragColor.a = 1.0;
	#endif

	float intensity = max(gl_FragColor.r, max(gl_FragColor.g, gl_FragColor.b));
	float factor;
	if (intensity > 0.8)
		factor = 1.0;
	else if (intensity > 0.5)
		factor = 0.8;
	else if (intensity > 0.25)
		factor = 0.3;
	else
		factor = 0.1;

	gl_FragColor.rgb *= factor;
}