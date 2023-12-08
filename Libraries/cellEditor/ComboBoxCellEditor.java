/*    */ package controlador.Libraries.cellEditor;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Font;
/*    */ import java.awt.event.ItemEvent;
/*    */ import java.util.EventObject;
/*    */ import java.util.List;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.event.CellEditorListener;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.table.TableCellEditor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComboBoxCellEditor
/*    */   extends JComboBox
/*    */   implements TableCellEditor
/*    */ {
/* 24 */   private CellEditorListener cellEditorListener = null;
/*    */   
/*    */   private Object oldValue;
/*    */   
/*    */   private final boolean editar;
/*    */   private final List<String> items;
/*    */   
/*    */   public ComboBoxCellEditor(boolean editar, List<String> items) {
/* 32 */     this.editar = editar;
/* 33 */     this.items = items;
/*    */     
/* 35 */     items.forEach(obj -> addItem((E)obj));
/*    */ 
/*    */     
/* 38 */     addItemListener(e -> stopCellEditing());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row, int column) {
/* 47 */     setSelectedItem(obj.toString());
/*    */     
/* 49 */     setFont(new Font("Arial", 0, 12));
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getCellEditorValue() {
/* 56 */     return getSelectedItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCellEditable(EventObject e) {
/* 61 */     return this.editar;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldSelectCell(EventObject e) {
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean stopCellEditing() {
/*    */     try {
/* 72 */       this.cellEditorListener.editingStopped(new ChangeEvent(this));
/* 73 */     } catch (NullPointerException|ArrayIndexOutOfBoundsException nullPointerException) {}
/*    */     
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelCellEditing() {
/* 80 */     this.cellEditorListener.editingCanceled(new ChangeEvent(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCellEditorListener(CellEditorListener celleditorlistener) {
/* 85 */     this.cellEditorListener = celleditorlistener;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeCellEditorListener(CellEditorListener celleditorlistener) {
/* 90 */     if (this.cellEditorListener == this.cellEditorListener);
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\cellEditor\ComboBoxCellEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */