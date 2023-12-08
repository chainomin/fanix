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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblRenderNumMatricula
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private final int clm;
/*    */   
/*    */   public TblRenderNumMatricula(int clm) {
/* 22 */     this.clm = clm;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
/* 28 */     super.getTableCellRendererComponent(table, value, selected, focused, row, column);
/* 29 */     if (this.clm == column && 
/* 30 */       value != null)
/* 31 */     { switch (value.toString().charAt(0))
/*    */       { case '1':
/* 33 */           setForeground(Color.WHITE);
/* 34 */           setBackground(new Color(69, 183, 126));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 51 */           return this;case '2': setForeground(Color.WHITE); setBackground(new Color(1, 100, 106)); return this;case '3': setForeground(Color.WHITE); setBackground(new Color(234, 69, 20)); return this; }  setBackground(Color.white); setForeground(Color.BLACK); }  return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\TblRenderNumMatricula.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */