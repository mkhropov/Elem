#version 120

uniform sampler2D tex;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);
	gl_FragColor = vec4(color.r*light.r,
						color.g*light.g,
						color.b*light.b,
						color.a);
}

