package zju.group1.forum.tool;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ImageProcess {
    public BufferedImage ensureOpaque(BufferedImage bi) {
        if (bi.getTransparency() == BufferedImage.OPAQUE) {
            return bi;
        }
        int w = bi.getWidth();
        int h = bi.getHeight();
        int[] pixels = new int[w * h];
        bi.getRGB(0, 0, w, h, pixels, 0, w);
        BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bi2.setRGB(0, 0, w, h, pixels, 0, w);
        return bi2;
    }
}
