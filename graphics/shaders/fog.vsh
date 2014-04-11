#version 120

uniform mat4 MVP;
uniform float max_z;
uniform float curr_z;

attribute vec3 position;
attribute vec2 texture;
attribute vec3 normal;

varying vec3 fog_color;
varying float fade;

varying vec2 uv;
varying vec3 light;

void main(){
	const vec3 lvec = normalize(vec3(-3., 1., 2.));
	const vec3 lcol = vec3(.7, .7, .7);
	const vec3 amb = vec3(.5, .5, .5);
	const vec3 fog = vec3(.9, .9, 1.);
	const float DEPTH = 10.;

	gl_Position = MVP * vec4(position, 1.);

	fog_color = ((position.z+DEPTH)/max_z)*fog;
	fade = max((position.z+DEPTH-curr_z)/DEPTH, 0.);

	uv = texture;
	light = lcol*max(dot(normal, lvec), 0.) + amb;
}
