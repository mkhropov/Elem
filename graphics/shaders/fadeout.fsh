#version 120

uniform sampler2D tex;

varying vec3 fog_color;
varying float fade;

varying vec3 light;
varying vec2 uv;

void main(){
	vec4 color = texture2D(tex, uv);
	gl_FragColor = vec4(color.rgb*light*fade+(1-fade)*fog_color, 1);
}

