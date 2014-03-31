#version 120

uniform sampler2D tex;

uniform float fade;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);
	gl_FragColor = vec4(color.r*light.r*fade,
						color.g*light.g*fade,
						color.b*light.b*fade,
						color.a);
}

