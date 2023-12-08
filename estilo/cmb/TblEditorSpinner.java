/*    */ package controlador.estilo.cmb;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.util.EventObject;
/*    */ import javax.swing.AbstractCellEditor;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.SpinnerModel;
/*    */ import javax.swing.event.CellEditorListener;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.table.TableCellEditor;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblEditorSpinner
/*    */   extends AbstractCellEditor
/*    */   implements TableCellEditor
/*    */ {
/*    */   private final JSpinner spn;
/* 20 */   private CellEditorListener cellEditorListener = null;
/*    */   
/*    */   public TblEditorSpinner(JSpinner spn) {
/* 23 */     this.spn = spn;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCellEditorValue() {
/* 28 */     SpinnerModel sm = this.spn.getModel();
/* 29 */     return sm;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 34 */     table.setValueAt(value, row, column);
/* 35 */     return this.spn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCellEditable(EventObject e) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldSelectCell(EventObject e) {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stopCellEditing() {
/*    */     try {
/* 51 */       this.cellEditorListener.editingStopped(new ChangeEvent(this));
/* 52 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelCellEditing() {
/* 59 */     this.cellEditorListener.editingCanceled(new ChangeEvent(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCellEditorListener(CellEditorListener celleditorlistener) {
/* 64 */     this.cellEditorListener = celleditorlistener;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeCellEditorListener(CellEditorListener celleditorlistener) {
/* 69 */     if (this.cellEditorListener == this.cellEditorListener)
/* 70 */       this.cellEditorListener = null; 
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\cmb\TblEditorSpinner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */