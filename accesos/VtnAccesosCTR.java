/*    */ package controlador.accesos;
/*    */ 
/*    */ import controlador.Libraries.Effects;
/*    */ import controlador.principal.VtnPrincipalCTR;
/*    */ import java.util.List;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ import modelo.accesos.AccesosBD;
/*    */ import vista.accesos.VtnAccesos;
/*    */ 
/*    */ public class VtnAccesosCTR
/*    */ {
/*    */   private final VtnPrincipalCTR desktop;
/*    */   private final VtnAccesos vista;
/*    */   private final AccesosBD modelo;
/*    */   private DefaultTableModel tabla;
/*    */   private List<AccesosBD> listaAccesos;
/*    */   
/*    */   public VtnAccesosCTR(VtnPrincipalCTR desktop) {
/* 20 */     this.desktop = desktop;
/* 21 */     this.vista = new VtnAccesos();
/* 22 */     this.modelo = new AccesosBD();
/*    */   }
/*    */   
/*    */   public void setListaAccesos(List<AccesosBD> listaAccesos) {
/* 26 */     this.listaAccesos = listaAccesos;
/*    */   }
/*    */ 
/*    */   
/*    */   public void Init() {
/* 31 */     Effects.addInDesktopPane((JInternalFrame)this.vista, this.desktop.getVtnPrin().getDpnlPrincipal());
/*    */     
/* 33 */     this.tabla = (DefaultTableModel)this.vista.getTblAccesosDeRol().getModel();
/*    */     
/* 35 */     this.listaAccesos = this.modelo.SelectAll();
/* 36 */     cargarTabla();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void cargarTabla() {
/* 43 */     this.listaAccesos.stream().forEach(obj -> this.tabla.addRow(new Object[] { Integer.valueOf(this.tabla.getDataVector().size() + 1), Integer.valueOf(obj.getIdAccesos()), obj.getNombre() }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     this.vista.getLblResultados().setText((this.tabla.getDataVector().size() + 1) + " Resultados");
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\accesos\VtnAccesosCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */