#version 120

uniform sampler2D tex;

varying vec3 fog_color;
varying float fade;

varying vec3 light;
varying vec2 uv;
uniform vec2 start;
uniform vec2 size;

void main(){
        vec2 uv2 = vec2(uv.x*size.x+start.x, uv.y*size.y+start.y);
	vec4 color = texture2D(tex, uv2);
	gl_FragColor = vec4(color.rgb*light*fade+(1-fade)*fog_color, 1);
}

