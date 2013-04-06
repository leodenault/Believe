package musicGame.GUI;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SpriteSheet;

/**
 * Utility class for drawing graphics.
 */
public final class GraphicsUtils {

	/**
	 * Creates an image with the specified colours as transparent.
	 * 
	 * @param image			The image to input.
	 * @param alphaColours	The colours to change to transparent.
	 * @return				An image with the specified colours set as transparent.
	 */
	public static Image makeImageWithAlpha(Image image, int[] alphaColours) {
		
		if (image == null) {
			throw new RuntimeException("An image must be supplied.");
		}
		else if (alphaColours == null || alphaColours.length == 0) {
			return image.copy();
		}
		
		ImageBuffer buffer = new ImageBuffer(image.getWidth(), image.getHeight());
		boolean set;
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				set = false;
				
				Color currentColor = image.getColor(j, i);
				int byteColor = (currentColor.getAlpha() << 24) | (currentColor.getRed() << 16) | (currentColor.getGreen() << 8) | currentColor.getBlue();
				
				for (int k = 0; k < alphaColours.length; k++) {
					if (byteColor == alphaColours[k]) {
						buffer.setRGBA(j, i, currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0);
						set = true;
						break;
					}
				}
				
				if (!set) {
					buffer.setRGBA(j, i, currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
				}
				
			}
		}
		
		return buffer.getImage();
	}
	
	public static Image[] makeImagesWithAlpha(Image[] images, int[] alphaColours) {
		Image[] newImages = new Image[images.length];
		for (int i = 0; i < images.length; i++) {
			newImages[i] = makeImageWithAlpha(images[i], alphaColours);
		}
		return newImages;
	}
	
	public static Animation makeAnimationWithAlpha(Image image, int[] alphaColours, int fw, int fh, int duration) {
		return new Animation(new SpriteSheet(makeImageWithAlpha(image, alphaColours), fw, fh), duration);
	}
	
	public static Animation[] makeAnimationsWithAlpha(Image images[], int[] alphaColours, int fw, int fh, int duration) {
		Animation[] newAnimations = new Animation[images.length];
		for (int i = 0; i < images.length; i++) {
			newAnimations[i] = makeAnimationWithAlpha(images[i], alphaColours, fw, fh, duration);
		}
		return newAnimations;
	}
}
