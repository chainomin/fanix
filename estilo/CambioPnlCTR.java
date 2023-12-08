/*    */ package controlador.estilo;
/*    */ 
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CambioPnlCTR
/*    */ {
/*    */   public static void cambioPnl(JPanel pnlPadre, JPanel pnlHijo) {
/* 13 */     pnlPadre.removeAll();
/* 14 */     pnlPadre.repaint();
/* 15 */     pnlPadre.revalidate();
/*    */     
/* 17 */     pnlPadre.add(pnlHijo);
/* 18 */     pnlPadre.repaint();
/* 19 */     pnlPadre.revalidate();
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\CambioPnlCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */