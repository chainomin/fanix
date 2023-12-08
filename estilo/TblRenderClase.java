/*    */ package controlador.estilo;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableCellRenderer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblRenderClase
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private final int clm;
/*    */   private JLabel lbl;
/*    */   
/*    */   public TblRenderClase(int clm) {
/* 24 */     this.clm = clm;
/*    */   }
/*    */   
/*    */   private void iniciaLbl() {
/* 28 */     this.lbl.setVisible(true);
/* 29 */     this.lbl.setOpaque(false);
/* 30 */     this.lbl.setHorizontalAlignment(0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
/* 36 */     super.getTableCellRendererComponent(table, value, selected, focused, row, column);
/*    */     
/* 38 */     this.lbl = new JLabel();
/* 39 */     iniciaLbl();
/*    */     
/* 41 */     if (value != null) {
/* 42 */       this.lbl.setText(value.toString());
/*    */     }
/*    */     
/* 45 */     if (focused && 
/* 46 */       this.clm == column) {
/* 47 */       this.lbl.setOpaque(true);
/* 48 */       this.lbl.setBackground(new Color(153, 153, 153));
/*    */     } 
/*    */ 
/*    */     
/* 52 */     return this.lbl;
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\TblRenderClase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */