package audio;

public enum AudioType {

	INVINCIBLE("invincible"),
	WOOD_HIT_01("explosion01"), 
	BG00("bg00"), 
	CANNON_SHOOT("cannon_shoot"),
	BOSS("boss");
	
	private String name;
	
	AudioType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
