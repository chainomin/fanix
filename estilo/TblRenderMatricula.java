/*    */ package controlador.estilo;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableCellRenderer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblRenderMatricula
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private final int clm;
/*    */   
/*    */   public TblRenderMatricula(int clm) {
/* 17 */     this.clm = clm;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
/* 23 */     super.getTableCellRendererComponent(table, value, selected, focused, row, column);
/* 24 */     setBackground(Color.white);
/* 25 */     setForeground(Color.BLACK);
/* 26 */     if (this.clm == column && 
/* 27 */       value != null) {
/* 28 */       if (value.toString().charAt(0) == 'C') {
/* 29 */         setBackground(Color.red);
/* 30 */       } else if (value.toString().charAt(0) == '0') {
/* 31 */         setBackground(Color.YELLOW);
/*    */       } 
/*    */     }
/*    */     
/* 35 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\TblRenderMatricula.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */