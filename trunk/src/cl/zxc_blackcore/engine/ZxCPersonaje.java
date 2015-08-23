package cl.zxc_blackcore.engine;

public class ZxCPersonaje {
	ZxCAnimaciones caminar;
	
	public ZxCPersonaje() {
		caminar = new ZxCAnimaciones(
				"werewolfRigging_000001.obj", 1);
	}

	public void draw(float[] mvpMatrix) {
		caminar.draw(mvpMatrix);
	}
}
