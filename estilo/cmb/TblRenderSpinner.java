/*    */ package controlador.estilo.cmb;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableCellRenderer;
/*    */ import modelo.validaciones.Validar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblRenderSpinner
/*    */   extends DefaultTableCellRenderer
/*    */ {
/*    */   private final JSpinner spn;
/*    */   
/*    */   public TblRenderSpinner(JSpinner spn) {
/* 18 */     this.spn = spn;
/* 19 */     iniciarEventos();
/*    */   }
/*    */   
/*    */   private void iniciarEventos() {
/* 23 */     this.spn.setVisible(true);
/* 24 */     this.spn.setOpaque(false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
/* 30 */     super.getTableCellRendererComponent(table, value, selected, focused, row, column);
/*    */     
/* 32 */     if (value != null && 
/* 33 */       Validar.esNumeros(value.toString())) {
/* 34 */       this.spn.setValue(Integer.valueOf(Integer.parseInt(value.toString())));
/* 35 */       this.spn.getValue();
/*    */     } 
/*    */     
/* 38 */     return this.spn;
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\cmb\TblRenderSpinner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */