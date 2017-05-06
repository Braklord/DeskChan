package info.deskchan.gui_javafx;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

class SingleImageSkin implements Skin {
	
	private final Path path;
	private final Image image;
	private final Path propertiesPath;
	private final Properties properties = new Properties();
	
	SingleImageSkin(Path path) {
		this.path = path;
		InputStream stream = null;
		try {
			stream = Files.newInputStream(path);
		} catch (IOException e) {
			Main.getInstance().getPluginProxy().log(e);
		}
		image = (stream != null) ? new Image(stream) : null;
		propertiesPath = Main.getInstance().getPluginProxy().getDataDirPath().resolve(
				"skin_" + getName() + ".properties"
		);
		try {
			properties.load(Files.newBufferedReader(propertiesPath));
		} catch (Throwable e) {
			try {
				properties.load(Files.newBufferedReader(
						path.resolveSibling(path.getFileName().toString() + ".properties")
				));
			} catch (Throwable e2) {
				// Do nothing
			}
		}
	}
	
	@Override
	public String getName() {
		return path.getFileName().toString();
	}
	
	@Override
	public Image getImage(String name) {
		return image;
	}
	
	@Override
	public Point2D getPreferredBalloonPosition(String imageName) {
		try {
			String value = properties.getProperty("balloon_offset", null);
			if (value == null) {
				return null;
			}
			String[] coords = value.split(";");
			return new Point2D(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
		} catch (Throwable e) {
			return null;
		}
	}
	
	@Override
	public void overridePreferredBalloonPosition(String imageName, Point2D position) {
		try {
			String key = "balloon_offset";
			String value = String.valueOf(position.getX()) + ";" +
					String.valueOf(position.getY());
			String oldValue = properties.getProperty(key);
			if ((oldValue != null) && oldValue.equals(value)) {
				return;
			}
			properties.setProperty(key, value);
			properties.store(Files.newBufferedWriter(propertiesPath), "Skin properties");
		} catch (Throwable e) {
			Main.log(e);
		}
	}
	
	@Override
	public String toString() {
		Path cur = path;
		StringBuilder name = new StringBuilder(cur.getFileName().toString().replace(".png", ""));
		cur = cur.getParent();
		while (!cur.equals(Skin.getSkinsPath())) {
			name.insert(0, cur.getFileName().toString() + " ");
			cur = cur.getParent();
		}
		return name + " [SINGLE IMAGE]";
	}
	
	static class Loader implements SkinLoader {
		
		@Override
		public boolean matchByPath(Path path) {
			return Files.isReadable(path) && path.getFileName().toString().endsWith(".png");
		}
		
		@Override
		public Skin loadByPath(Path path) {
			return new SingleImageSkin(path);
		}
		
	}
	
}
