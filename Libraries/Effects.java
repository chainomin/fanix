/*    */ package controlador.Libraries;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Container;
/*    */ import java.awt.Cursor;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.beans.PropertyVetoException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JDesktopPane;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.Timer;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Effects
/*    */ {
/*    */   private static final Cursor LOAD_CURSOR;
/*    */   private static final Cursor DEFAULT_CURSOR;
/* 27 */   private static Logger LOGGER = LoggerFactory.getLogger(Effects.class);
/*    */   
/*    */   static {
/* 30 */     LOAD_CURSOR = new Cursor(3);
/* 31 */     DEFAULT_CURSOR = new Cursor(0);
/*    */   }
/*    */   
/*    */   public static void addInDesktopPane(JInternalFrame component, JDesktopPane desktop) {
/*    */     try {
/* 36 */       centerFrame(component, desktop);
/* 37 */       desktop.add(component);
/* 38 */       component.setSelected(true);
/* 39 */       component.setVisible(true);
/* 40 */     } catch (PropertyVetoException ex) {
/* 41 */       Logger.getLogger(Middlewares.class.getName()).log(Level.SEVERE, (String)null, ex);
/*    */     } 
/*    */     
/* 44 */     System.out.println("-------THREADS-------->" + Thread.activeCount());
/*    */   }
/*    */   
/*    */   public static synchronized void eliminarThread(Thread thread) {
/* 48 */     thread = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void centerFrame(JInternalFrame view, JDesktopPane desktop) {
/* 53 */     int deskWidth = desktop.getWidth() / 2 - view.getWidth() / 2;
/* 54 */     int deskHeight = desktop.getHeight() / 2 - view.getHeight() / 2;
/*    */     
/* 56 */     if (desktop.getHeight() < 500) {
/* 57 */       deskHeight = 0;
/*    */     }
/* 59 */     view.setLocation(deskWidth, deskHeight);
/*    */   }
/*    */   
/*    */   public static void setTextInLabel(JLabel component, String message, Color color, int time) {
/* 63 */     if (color != null) {
/* 64 */       component.setForeground(color);
/*    */     }
/* 66 */     setText(component, message, time);
/*    */   }
/*    */   
/*    */   public static void setText(JLabel component, String text, int time) {
/* 70 */     component.setText(text);
/* 71 */     Timer task = new Timer(time * 1000, e -> component.setText(""));
/*    */ 
/*    */     
/* 74 */     task.setRepeats(false);
/* 75 */     task.start();
/*    */   }
/*    */   
/*    */   public static void setLoadCursor(Container view) {
/* 79 */     view.setCursor(LOAD_CURSOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void setDefaultCursor(Container view) {
/* 84 */     view.setCursor(DEFAULT_CURSOR);
/*    */   }
/*    */   
/*    */   public static void btnHover(JButton btnIngresar, final JLabel lblBtnHover, final Color enterColor, final Color exitColor) {
/* 88 */     btnIngresar.addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 91 */             lblBtnHover.setBackground(enterColor);
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 96 */             lblBtnHover.setBackground(exitColor);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\Effects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */