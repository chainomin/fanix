/*    */ package controlador.Libraries.cellEditor;
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
/*    */ public class MiRenderer
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 18 */     if (((Integer)table.getValueAt(row, 3)).intValue() > 0) { setForeground(Color.red); } else { setForeground(Color.BLACK); }
/* 19 */      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\cellEditor\MiRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */