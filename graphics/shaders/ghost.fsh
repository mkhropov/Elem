#version 120

uniform sampler2D tex;
uniform int enabled;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);
//	float i = (color.r+color.g+color.b)/6.;
	float i = .5;
	if (enabled == 1)
		gl_FragColor = vec4(.4, .4+i, .4, 0.6);
	else
		gl_FragColor = vec4(.4+i, .4, .4, 0.6);
}

