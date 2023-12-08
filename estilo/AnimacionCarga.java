/*    */ package controlador.estilo;
/*    */ 
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnimacionCarga
/*    */   extends Thread
/*    */ {
/*    */   private final JLabel lbl;
/* 14 */   private int pos = 0;
/*    */   
/*    */   private boolean cargando = true;
/*    */   
/* 18 */   ImageIcon[] estados = new ImageIcon[] { new ImageIcon(
/* 19 */         getClass().getResource("/vista/img/animacion/LogoPosFinal.png")), new ImageIcon(
/* 20 */         getClass().getResource("/vista/img/animacion/LogoPos1.png")), new ImageIcon(
/* 21 */         getClass().getResource("/vista/img/animacion/LogoPos2.png")), new ImageIcon(
/* 22 */         getClass().getResource("/vista/img/animacion/LogoPos3.png")) };
/*    */ 
/*    */   
/*    */   public AnimacionCarga(JLabel lbl) {
/* 26 */     this.lbl = lbl;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 32 */     while (this.cargando) {
/*    */       
/* 34 */       this.lbl.setIcon(this.estados[this.pos]);
/*    */       
/* 36 */       dormir(500);
/* 37 */       this.pos++;
/* 38 */       if (this.pos > this.estados.length - 1) {
/* 39 */         this.pos = 1;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void iniciar() {
/* 48 */     System.out.println("Se inicio la animacion");
/* 49 */     this.cargando = true;
/*    */   }
/*    */   
/*    */   public void detener() {
/* 53 */     System.out.println("Se detuvo la animacion");
/* 54 */     this.cargando = false;
/* 55 */     this.lbl.setIcon(this.estados[0]);
/*    */   }
/*    */   
/*    */   public void dormir(int seg) {
/*    */     try {
/* 60 */       Thread.sleep(seg);
/* 61 */     } catch (InterruptedException e) {
/* 62 */       System.out.println("No se realizo la animacion " + e.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\AnimacionCarga.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */