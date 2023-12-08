/*    */ package controlador.docente;
/*    */ 
/*    */ import controlador.principal.DCTR;
/*    */ import controlador.principal.VtnPrincipalCTR;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.KeyListener;
/*    */ import java.util.ArrayList;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.JOptionPane;
/*    */ import modelo.docente.RolPeriodoBD;
/*    */ import modelo.docente.RolPeriodoMD;
/*    */ import modelo.periodolectivo.PeriodoLectivoBD;
/*    */ import modelo.periodolectivo.PeriodoLectivoMD;
/*    */ import modelo.validaciones.TxtVLetras;
/*    */ import modelo.validaciones.Validar;
/*    */ import vista.docente.FrmRolesPeriodos;
/*    */ 
/*    */ public class FrmRolPeriodoCTR
/*    */   extends DCTR
/*    */ {
/*    */   private final FrmRolesPeriodos frmRolPer;
/* 22 */   private final RolPeriodoBD RPLBD = RolPeriodoBD.single();
/*    */   private ArrayList<PeriodoLectivoMD> periodos;
/* 24 */   private final PeriodoLectivoBD PRBD = PeriodoLectivoBD.single();
/*    */   private boolean editar = false;
/*    */   private int idRolPrd;
/*    */   
/*    */   public FrmRolPeriodoCTR(FrmRolesPeriodos frmRolPer, VtnPrincipalCTR ctrPrin) {
/* 29 */     super(ctrPrin);
/* 30 */     this.frmRolPer = frmRolPer;
/*    */   }
/*    */   
/*    */   public void iniciar() {
/* 34 */     cargarCmbPrdLectivo();
/* 35 */     this.frmRolPer.getLbl_error_roles().setVisible(false);
/* 36 */     iniciarValidaciones();
/* 37 */     this.frmRolPer.getBtnGuardar().addActionListener(e -> insertarRolesPeriodos());
/*    */     
/* 39 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmRolPer);
/*    */   }
/*    */   
/*    */   private void cargarCmbPrdLectivo() {
/* 43 */     this.periodos = this.PRBD.cargarPrdParaCmbFrm();
/* 44 */     if (this.periodos != null) {
/* 45 */       this.frmRolPer.getCmbPeriodoLectivo().removeAllItems();
/* 46 */       this.frmRolPer.getCmbPeriodoLectivo().addItem("Seleccione");
/* 47 */       this.periodos.forEach(p -> this.frmRolPer.getCmbPeriodoLectivo().addItem(p.getNombre()));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void insertarRolesPeriodos() {
/* 54 */     int posFila = this.frmRolPer.getCmbPeriodoLectivo().getSelectedIndex();
/* 55 */     boolean guardar = true;
/* 56 */     if (posFila == 0) {
/* 57 */       guardar = false;
/*    */     }
/* 59 */     if (!Validar.esLetras(this.frmRolPer.getTxtNombreRol().getText().trim())) {
/* 60 */       guardar = false;
/*    */     }
/*    */     
/* 63 */     if (guardar) {
/* 64 */       RolPeriodoMD rpl = new RolPeriodoMD();
/* 65 */       rpl.setPeriodo(this.periodos.get(posFila - 1));
/* 66 */       rpl.setNombre_rol(this.frmRolPer.getTxtNombreRol().getText());
/* 67 */       if (this.editar) {
/* 68 */         rpl.setId_rol(this.idRolPrd);
/* 69 */         this.RPLBD.editarRolPeriodo(rpl);
/* 70 */         JOptionPane.showMessageDialog(null, "Datos editados correctamente");
/*    */       }
/* 72 */       else if (this.RPLBD.InsertarRol(rpl)) {
/* 73 */         JOptionPane.showMessageDialog(null, "Datos grabados correctamente");
/*    */       } else {
/* 75 */         JOptionPane.showMessageDialog(null, "Error en grabar los datos");
/*    */       } 
/*    */       
/* 78 */       this.frmRolPer.dispose();
/* 79 */       this.ctrPrin.abrirVtnRolesPeriodos();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void iniciarValidaciones() {
/* 84 */     this.frmRolPer.getTxtNombreRol().addKeyListener((KeyListener)new TxtVLetras(this.frmRolPer
/* 85 */           .getTxtNombreRol(), this.frmRolPer.getLbl_error_roles()));
/*    */   }
/*    */   
/*    */   public void editarRolesPeriodos(RolPeriodoMD rp) {
/* 89 */     this.idRolPrd = rp.getId_rol();
/* 90 */     this.editar = true;
/* 91 */     this.frmRolPer.getTxtNombreRol().setText(rp.getNombre_rol());
/* 92 */     this.frmRolPer.getCmbPeriodoLectivo().setSelectedItem(rp.getPeriodo().getNombre());
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\FrmRolPeriodoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */