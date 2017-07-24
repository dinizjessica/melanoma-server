package project.melanoma.segmentation;

import static marvin.MarvinPluginCollection.brightnessAndContrast;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import marvin.MarvinPluginCollection;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinBlob;
import marvin.image.MarvinBlobSegment;
import marvin.image.MarvinContour;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;
import marvin.math.Point;

public class SegmentDiagram {

    public SegmentDiagram(){
        MarvinImage originalImage = MarvinImageIO.loadImage("/Users/jessicadiniz/Documents/ufrpe/2017.1/seg_java/ISIC_0000019.jpg");
        MarvinImage image = originalImage.clone();
        
     // 2. Increase contrast
     		brightnessAndContrast(image, 0, 50);
//     		MarvinImage imageOut = image.clone();
//     	// 3. Thesholding


//    		thresholdingNeighborhood(image, imageOut, 1, 8, 1);
    		BufferedImage thresholdImage = thresholdImage(image.getBufferedImage(), 130);
    		MarvinImage imageOut = new MarvinImage(thresholdImage);
          MarvinImage binImage = MarvinColorModelConverter.rgbToBinary(imageOut, 250);
          MarvinPluginCollection.morphologicalErosion(binImage.clone(), binImage, MarvinMath.getTrueMatrix(5, 5));


//     		
//    		// 4. Separate cells that are grouped
//    		invertColors(imageOut);
//    		MarvinImage bin = MarvinColorModelConverter.rgbToBinary(imageOut, 50);
//    		morphologicalErosion(bin.clone(), bin, MarvinMath.getTrueMatrix(3, 3));
//    		morphologicalDilation(bin.clone(), bin, MarvinMath.getTrueMatrix(3, 3));
    		/*
    		// 5. Segment each cell
    		image = MarvinColorModelConverter.binaryToRgb(bin);
    		image.setAlphaByColor(0, 0xFFFFFFFF);
    		*/
    		MarvinBlobSegment[] segments = MarvinPluginCollection.floodfillSegmentationBlob(image);
    		
    		for(MarvinBlobSegment s:segments){
    			MarvinBlob blob = s.getBlob();
    			MarvinContour contour = blob.toContour();
    			
    			if(blob.getArea() > 50){
    				for(Point p:contour.getPoints()){
    					originalImage.setIntColor(s.getX()+p.x, s.getY()+p.y, 0xFF00FF00);
    				}
    			}
    		}
     		
//        scale(image.clone(), image, 200, 200);
//        MarvinImage binImage = MarvinColorModelConverter.rgbToBinary(image, 250);
//        morphologicalErosion(image.clone(), image, MarvinMath.getTrueMatrix(5, 5));
//        image = MarvinColorModelConverter.binaryToRgb(binImage);
//        MarvinSegment[] segments = floodfillSegmentation(image);
//
//        for(int i=1; i<segments.length; i++){
//            MarvinSegment seg = segments[i];
//            originalImage.drawRect(seg.x1, seg.y1, seg.width, seg.height, Color.red);
//            originalImage.drawRect(seg.x1+1, seg.y1+1, seg.width, seg.height, Color.red);
//        }
        MarvinImageIO.saveImage(originalImage, "diagram_segmented.jpg");
    }
    
    /**
     * Converts an image to a binary one based on given threshold
     * @param image the image to convert. Remains untouched.
     * @param threshold the threshold in [0,255]
     * @return a new BufferedImage instance of TYPE_BYTE_GRAY with only 0'S and 255's
     */
    public static BufferedImage thresholdImage(BufferedImage image, int threshold) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        result.getGraphics().drawImage(image, 0, 0, null);
        WritableRaster raster = result.getRaster();
        int[] pixels = new int[image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            raster.getPixels(0, y, image.getWidth(), 1, pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < threshold) pixels[i] = 0;
                else pixels[i] = 255;
            }
            raster.setPixels(0, y, image.getWidth(), 1, pixels);
        }
        return result;
    }

    public static void main(String[] args) {
        new SegmentDiagram();
    }
}