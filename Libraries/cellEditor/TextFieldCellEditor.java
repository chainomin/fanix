/*     */ package controlador.Libraries.cellEditor;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.EventObject;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.CellEditorListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ 
/*     */ 
/*     */ public class TextFieldCellEditor
/*     */   extends JTextField
/*     */   implements TableCellEditor
/*     */ {
/*  22 */   private CellEditorListener cellEditorListener = null;
/*     */   
/*     */   private Object oldValue;
/*     */   
/*     */   private final boolean editar;
/*     */   
/*     */   public TextFieldCellEditor(boolean editar) {
/*  29 */     this.editar = editar;
/*  30 */     setOpaque(true);
/*  31 */     setBorder((Border)null);
/*  32 */     setHorizontalAlignment(0);
/*  33 */     addFocusListener(new FocusAdapter()
/*     */         {
/*     */           public void focusGained(FocusEvent e) {
/*  36 */             TextFieldCellEditor.this.selectAll();
/*     */           }
/*     */         });
/*     */     
/*  40 */     addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  43 */             if (e.getKeyCode() == 10) {
/*  44 */               TextFieldCellEditor.this.stopCellEditing();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getTableCellEditorComponent(JTable table, Object obj, boolean isSelected, int row, int column) {
/*  54 */     setText(obj.toString());
/*     */     
/*  56 */     if (isSelected) {
/*  57 */       selectAll();
/*     */     }
/*     */     
/*  60 */     setFont(new Font("Arial", 0, 12));
/*  61 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getCellEditorValue() {
/*  67 */     return getText();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCellEditable(EventObject e) {
/*  72 */     return this.editar;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSelectCell(EventObject e) {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stopCellEditing() {
/*     */     try {
/*  83 */       this.cellEditorListener.editingStopped(new ChangeEvent(this));
/*  84 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*     */     
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelCellEditing() {
/*  91 */     this.cellEditorListener.editingCanceled(new ChangeEvent(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCellEditorListener(CellEditorListener celleditorlistener) {
/*  96 */     this.cellEditorListener = celleditorlistener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeCellEditorListener(CellEditorListener celleditorlistener) {
/* 101 */     if (this.cellEditorListener == this.cellEditorListener)
/* 102 */       this.cellEditorListener = null; 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\cellEditor\TextFieldCellEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */