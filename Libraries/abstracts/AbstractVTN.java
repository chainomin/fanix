/*    */ package controlador.Libraries.abstracts;
/*    */ 
/*    */ import controlador.Libraries.Effects;
/*    */ import controlador.principal.VtnPrincipalCTR;
/*    */ import java.awt.Component;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ import modelo.persona.PersonaMD;
/*    */ import modelo.usuario.UsuarioMD;
/*    */ import utils.CONS;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractVTN<V extends JInternalFrame, M>
/*    */ {
/*    */   protected V vista;
/*    */   protected final VtnPrincipalCTR desktop;
/*    */   protected JTable table;
/*    */   protected M modelo;
/*    */   protected DefaultTableModel tableM;
/*    */   protected List<M> lista;
/*    */   protected boolean showTableMessage = true;
/* 31 */   protected final UsuarioMD user = (UsuarioMD)CONS.USUARIO;
/* 32 */   protected final PersonaMD personaCONS = CONS.USUARIO.getPersona();
/*    */   
/*    */   public AbstractVTN(VtnPrincipalCTR desktop) {
/* 35 */     this.desktop = desktop;
/*    */   }
/*    */   
/*    */   public void setTable(JTable table) {
/* 39 */     this.table = table;
/* 40 */     this.tableM = (DefaultTableModel)this.table.getModel();
/*    */   }
/*    */ 
/*    */   
/*    */   public V getVista() {
/* 45 */     return this.vista;
/*    */   }
/*    */   
/*    */   public void setVista(V vista) {
/* 49 */     this.vista = vista;
/*    */   }
/*    */   
/*    */   public M getModelo() {
/* 53 */     return this.modelo;
/*    */   }
/*    */   
/*    */   public void setModelo(M modelo) {
/* 57 */     this.modelo = modelo;
/*    */   }
/*    */   
/*    */   public List<M> getLista() {
/* 61 */     return this.lista;
/*    */   }
/*    */   
/*    */   public void setLista(List<M> lista) {
/* 65 */     this.lista = lista;
/*    */   }
/*    */   
/*    */   public DefaultTableModel getTableM() {
/* 69 */     return this.tableM;
/*    */   }
/*    */   
/*    */   public Integer getSelectedRow() {
/* 73 */     int row = this.table.getSelectedRow();
/* 74 */     if (row == -1) {
/* 75 */       if (this.showTableMessage) {
/* 76 */         JOptionPane.showMessageDialog((Component)this.vista, "DEBE SELECCIONA UN REGISTRO DE LA TABLA PRIMERO", "Aviso", 0);
/*    */       }
/* 78 */       return Integer.valueOf(row);
/*    */     } 
/* 80 */     return Integer.valueOf(row);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void Init() {
/* 87 */     Effects.addInDesktopPane((JInternalFrame)this.vista, this.desktop.getVtnPrin().getDpnlPrincipal());
/*    */   }
/*    */   
/*    */   protected void cargarTabla(Consumer<M> cargador) {
/* 91 */     this.tableM.setRowCount(0);
/* 92 */     if (this.lista != null)
/* 93 */       this.lista.forEach(cargador); 
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\abstracts\AbstractVTN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */