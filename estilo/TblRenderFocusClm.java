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
/*    */ public class TblRenderFocusClm
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private final int clm;
/*    */   private JLabel lbl;
/*    */   
/*    */   public TblRenderFocusClm(int clm) {
/* 19 */     this.clm = clm;
/*    */   }
/*    */   
/*    */   private void iniciaLbl() {
/* 23 */     this.lbl.setVisible(true);
/* 24 */     this.lbl.setOpaque(false);
/* 25 */     this.lbl.setHorizontalAlignment(0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
/* 31 */     super.getTableCellRendererComponent(table, value, selected, focused, row, column);
/*    */     
/* 33 */     this.lbl = new JLabel();
/* 34 */     iniciaLbl();
/*    */     
/* 36 */     if (value != null && 
/* 37 */       value.toString().contains("%")) {
/* 38 */       this.lbl.setText(value.toString().split("%")[1]);
/*    */     }
/*    */ 
/*    */     
/* 42 */     if (focused && 
/* 43 */       this.clm == column) {
/* 44 */       this.lbl.setOpaque(true);
/* 45 */       this.lbl.setBackground(new Color(153, 153, 153));
/*    */     } 
/*    */ 
/*    */     
/* 49 */     return this.lbl;
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\TblRenderFocusClm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */