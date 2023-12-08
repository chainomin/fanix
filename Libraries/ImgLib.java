/*     */ package controlador.Libraries;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import org.postgresql.util.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImgLib
/*     */ {
/*     */   public static ImageIcon JFCToImageIcon(JFileChooser findFile) {
/*  29 */     ImageIcon image = null;
/*     */ 
/*     */     
/*     */     try {
/*  33 */       URI uri = findFile.getSelectedFile().toURI();
/*     */       
/*  35 */       try (FileInputStream input = new FileInputStream(new File(uri)))
/*     */       {
/*  37 */         BufferedImage bffImage = ImageIO.read(input);
/*     */         
/*  39 */         if (bffImage == null)
/*     */         {
/*  41 */           return null;
/*     */         }
/*     */         
/*  44 */         image = new ImageIcon(bffImage);
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  49 */     catch (FileNotFoundException ex) {
/*  50 */       System.out.println(ex.getMessage());
/*  51 */     } catch (IOException ex) {
/*  52 */       System.out.println(ex.getMessage());
/*     */     } 
/*     */     
/*  55 */     return image;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileInputStream getImgFileFromJFC(JFileChooser jFileChooser) {
/*     */     try {
/*  62 */       return new FileInputStream(new File(jFileChooser.getSelectedFile().toPath().toUri()));
/*  63 */     } catch (FileNotFoundException ex) {
/*  64 */       Logger.getLogger(ImgLib.class.getName()).log(Level.SEVERE, (String)null, ex);
/*     */       
/*  66 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setImageInLabel(Image image, JLabel label) {
/*  71 */     if (image != null && label.getHeight() != 0 && label.getWidth() != 0) {
/*  72 */       label.setSize(label.getWidth(), label.getHeight());
/*     */       
/*  74 */       Icon icon = new ImageIcon(image.getScaledInstance(label.getWidth(), label.getHeight(), 1));
/*     */       
/*  76 */       label.setIcon(icon);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getStringPathFromJFC(JFileChooser JfileChooser) {
/*  81 */     return JfileChooser.getSelectedFile().toPath().toString();
/*     */   }
/*     */   
/*     */   private static BufferedImage toBufferedImage(Image image) {
/*  85 */     if (image instanceof BufferedImage) {
/*  86 */       return (BufferedImage)image;
/*     */     }
/*  88 */     BufferedImage buffImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
/*  89 */     Graphics2D g2D = buffImage.createGraphics();
/*  90 */     g2D.drawImage(image, 0, 0, (ImageObserver)null);
/*  91 */     g2D.dispose();
/*  92 */     return buffImage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String setImageInBase64(Image image) {
/*  98 */     if (image != null) {
/*     */       
/* 100 */       String base = null;
/*     */       
/* 102 */       ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
/*     */       
/*     */       try {
/* 105 */         BufferedImage img = toBufferedImage(image);
/*     */         
/* 107 */         ImageIO.write(img, "PNG", byteAOS);
/*     */         
/* 109 */         base = Base64.encodeBytes(byteAOS.toByteArray());
/*     */       }
/* 111 */       catch (IOException e) {
/* 112 */         System.out.println(e.getMessage());
/*     */       } 
/*     */       
/* 115 */       return base;
/*     */     } 
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Image byteToImage(byte[] byteFoto) {
/* 124 */     if (byteFoto != null) {
/* 125 */       return (new ImageIcon(byteFoto)).getImage();
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\ImgLib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */