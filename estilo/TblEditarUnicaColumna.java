/*    */ package controlador.estilo;
/*    */ 
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TblEditarUnicaColumna
/*    */   extends DefaultTableModel
/*    */ {
/*    */   private final int colEditar;
/*    */   private final String[] titulos;
/*    */   private final Object[][] datos;
/*    */   
/*    */   public TblEditarUnicaColumna(String[] titulos, String[][] datos, int colEditar) {
/* 17 */     this.titulos = titulos;
/* 18 */     this.datos = (Object[][])datos;
/* 19 */     this.colEditar = colEditar;
/* 20 */     setDataVector((Object[][])datos, (Object[])titulos);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCellEditable(int row, int col) {
/* 25 */     return (col == this.colEditar);
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\estilo\TblEditarUnicaColumna.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */