package cl.zxc_blackcore.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ZxCCargarOBJ {
	float v[] = new float[2502];
	float vn[] = new float[2670];
	float vt[] = new float[1376];
	short faces[] = new short[4992];
	int vCount = 0;
	int vnCount = 0;
	int vtCount = 0;
	int fCount = 0;

	public void objLoader(String fileName) {
		
		BufferedReader reader = null;
		String line = null;

		try {
			reader = new BufferedReader(new InputStreamReader(ZxCEngine.context
					.getAssets().open(fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {
				
				
				if (line.startsWith("f")) {// a polygonal face
					String[] tokens = line.split("[ ]+");
					int c = tokens.length;

					if (tokens[1].matches("[0-9]+/[0-9]+/[0-9]+")) {// f:
																	// v/vt/vn

						if (c == 4) {// 3 faces
							for (int i = 1; i < c; i++, fCount++) {
								Short s = Short
										.valueOf(tokens[i].split("/")[0]);
								s--;
								faces[fCount] = s;
							}
						}
					}
				}else if (line.startsWith("vn")) {
					String[] tokens = line.split("[ ]+");
					int c = tokens.length;
					for (int i = 1; i < c; i++, vnCount++) {
						vn[vnCount] = Float.valueOf(tokens[i]);
					}
				} else if (line.startsWith("vt")) {
					String[] tokens = line.split("[ ]+");
					int c = tokens.length;
					for (int i = 1; i < c; i++, vtCount++) {
						vt[vtCount] = Float.valueOf(tokens[i]);
					}
				} else if (line.startsWith("v")) {
					String[] tokens = line.split("[ ]+");
					int c = tokens.length;
					for (int i = 1; i < c; i++, vCount++) {
						v[vCount] = Float.valueOf(tokens[i]);
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
