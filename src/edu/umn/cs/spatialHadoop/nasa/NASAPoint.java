package edu.umn.cs.spatialHadoop.nasa;

import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.Text;

import edu.umn.cs.spatialHadoop.core.Point;
import edu.umn.cs.spatialHadoop.core.Rectangle;
import edu.umn.cs.spatialHadoop.io.TextSerializerHelper;

public class NASAPoint extends Point implements NASAShape {
  
  private static final byte[] Separator = {','};
  
  /**Value stored at this point*/
  public int value;
  
  /** date of the given point*/
  public int date;
  
  public NASAPoint() {
//	  date=0;
  }
  public NASAPoint(int date) {
	  this.date=date;
  }
//  public NASAPoint(double x, double y, int value) {
//    super(x, y);
//    this.value = value;
//  }
  
  public NASAPoint(double x, double y, int value, int date) {
	    super(x, y);
	    this.value = value;
	    this.date = date;
  }

  public int getValue() {
    return value;
  }
  
  public void setValue(int value) {
    this.value = value;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    super.write(out);
    out.writeInt(value);
    out.writeInt(date);
  }
  
  @Override
  public void readFields(DataInput in) throws IOException {
    super.readFields(in);
    this.value = in.readInt();
    this.date = in.readInt();
  }
  
  @Override
  public Text toText(Text text) {
    super.toText(text);
    text.append(Separator, 0, Separator.length);
    TextSerializerHelper.serializeInt(value, text, ',');
    
    TextSerializerHelper.serializeInt(date, text, '\0');

    return text;
  }
  
  @Override
  public void fromText(Text text) {
    super.fromText(text);
    byte[] bytes = text.getBytes();
    text.set(bytes, 1, text.getLength() - 1);
    value = TextSerializerHelper.consumeInt(text, ',');
    date = TextSerializerHelper.consumeInt(text, '\0');
  }
  
  @Override
  public String toString() {
    return super.toString() + " - "+value;
  }
  
  /**Valid range of values. Used for drawing.*/
  public static float minValue, maxValue;
  
  protected static Color color1, color2;
  protected static float hue1, hue2;
  
  public static enum GradientType {GT_HUE, GT_COLOR};
  public static GradientType gradientType;

  public static void setColor1(Color color) {
    color1 = color;
    float[] hsbvals = new float[3];
    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
    hue1 = hsbvals[0];
  }
  
  public static void setColor2(Color color) {
    color2 = color;
    float[] hsbvals = new float[3];
    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
    hue2 = hsbvals[0];
  }
  
  @Override
  public void draw(Graphics g, Rectangle fileMBR, int imageWidth,
      int imageHeight, double scale) {
    g.setColor(NASARectangle.calculateColor(this.value));
    super.draw(g, fileMBR, imageWidth, imageHeight, scale);
  }
}
