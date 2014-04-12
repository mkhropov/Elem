#version 120

uniform mat4 M;
uniform mat4 VP;

attribute vec3 position;
attribute vec2 texture;
attribute vec3 normal;

varying vec2 uv;
varying vec3 light;

void main(){
	const vec3 lvec = normalize(vec3(-3., 1., 2.));
	const vec3 lcol = vec3(.7, .7, .7);
	const vec3 amb = vec3(0.8, 0.8, 0.8);

	gl_Position = VP * M * vec4(position, 1.);

	uv = texture;
	light = lcol*max(dot(normal, lvec), 0.) + amb;
}
