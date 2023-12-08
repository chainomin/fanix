/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.docente.RolPeriodoBD;
/*     */ import modelo.docente.RolPeriodoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import utils.CONS;
/*     */ import vista.docente.FrmRolesPeriodos;
/*     */ import vista.docente.VtnRolesPeriodos;
/*     */ 
/*     */ public class VtnRolPeriodosCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final VtnRolesPeriodos vtnRolPe;
/*  23 */   private final RolPeriodoBD rolPer = RolPeriodoBD.single();
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*     */   private ArrayList<RolPeriodoMD> roles;
/*     */   
/*     */   public VtnRolPeriodosCTR(VtnRolesPeriodos vtnRolPe, VtnPrincipalCTR ctrPrin) {
/*  28 */     super(ctrPrin);
/*  29 */     this.vtnRolPe = vtnRolPe;
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  33 */     String[] titulo = { "Periodos Lectivos", "Roles" };
/*  34 */     String[][] datos = new String[0][];
/*     */ 
/*     */     
/*  37 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*     */     
/*  39 */     this.vtnRolPe.getTblAlumno().setModel(this.mdTbl);
/*     */     
/*  41 */     TblEstilo.formatoTbl(this.vtnRolPe.getTblAlumno());
/*  42 */     this.vtnRolPe.getTblAlumno().setModel(this.mdTbl);
/*     */     
/*  44 */     this.vtnRolPe.getBtnIngresar().addActionListener(e -> abrirFRM());
/*  45 */     this.vtnRolPe.getBtnEditar().addActionListener(e -> abrirFrmEditar());
/*  46 */     this.vtnRolPe.getBtnEliminar().addActionListener(e -> eliminarRolPeriodo());
/*  47 */     llenarTabla();
/*     */     
/*  49 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnRolPe);
/*  50 */     InitPermisos();
/*     */   }
/*     */   
/*     */   private void abrirFRM() {
/*  54 */     this.ctrPrin.abrirFrmRolesPeriodos();
/*  55 */     this.vtnRolPe.dispose();
/*  56 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */   
/*     */   public void abrirFrmEditar() {
/*  60 */     this.posFila = this.vtnRolPe.getTblAlumno().getSelectedRow();
/*  61 */     if (this.posFila >= 0) {
/*     */       
/*  63 */       FrmRolesPeriodos frm = new FrmRolesPeriodos();
/*  64 */       this.ctrPrin.eventoInternal((JInternalFrame)frm);
/*  65 */       FrmRolPeriodoCTR ctr = new FrmRolPeriodoCTR(frm, this.ctrPrin);
/*  66 */       ctr.iniciar();
/*  67 */       ctr.editarRolesPeriodos(this.roles.get(this.posFila));
/*  68 */       this.vtnRolPe.dispose();
/*  69 */       this.ctrPrin.cerradoJIF();
/*     */     } else {
/*  71 */       JOptionPane.showMessageDialog(null, "Seleccione una fila para editar");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void eliminarRolPeriodo() {
/*  76 */     this.posFila = this.vtnRolPe.getTblAlumno().getSelectedRow();
/*  77 */     if (this.posFila >= 0) {
/*  78 */       this.rolPer.eliminarRolPeriodo(((RolPeriodoMD)this.roles.get(this.posFila)).getId_rol());
/*  79 */       JOptionPane.showMessageDialog(null, "Datos eliminados correctamente");
/*  80 */       llenarTabla();
/*     */     } else {
/*  82 */       JOptionPane.showMessageDialog(null, "Seleccione una fila para eliminar");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void llenarTabla() {
/*  87 */     this.mdTbl = (DefaultTableModel)this.vtnRolPe.getTblAlumno().getModel();
/*  88 */     for (int i = this.vtnRolPe.getTblAlumno().getRowCount() - 1; i >= 0; i--) {
/*  89 */       this.mdTbl.removeRow(i);
/*     */     }
/*     */     
/*  92 */     this.roles = this.rolPer.llenarTabla();
/*  93 */     int columnas = this.mdTbl.getColumnCount();
/*  94 */     for (int j = 0; j < this.roles.size(); j++) {
/*  95 */       this.mdTbl.addRow(new Object[columnas]);
/*  96 */       this.vtnRolPe.getTblAlumno().setValueAt(String.valueOf(((RolPeriodoMD)this.roles.get(j)).getPeriodo().getNombre()), j, 0);
/*  97 */       this.vtnRolPe.getTblAlumno().setValueAt(String.valueOf(((RolPeriodoMD)this.roles.get(j)).getNombre_rol()), j, 1);
/*     */     } 
/*     */     
/* 100 */     this.vtnRolPe.getLblResultados().setText(String.valueOf(this.roles.size()) + " Resultados obtenidos.");
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 104 */     this.vtnRolPe.getBtnIngresar().getAccessibleContext().setAccessibleName("Roles-Periodo-Ingresar");
/* 105 */     this.vtnRolPe.getBtnEliminar().getAccessibleContext().setAccessibleName("Roles-Periodo-Eliminar ");
/* 106 */     this.vtnRolPe.getBtnEditar().getAccessibleContext().setAccessibleName("Roles-Periodo-Editar");
/*     */     
/* 108 */     CONS.activarBtns(new JComponent[] { this.vtnRolPe.getBtnIngresar(), this.vtnRolPe.getBtnEditar(), this.vtnRolPe.getBtnEliminar() });
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\VtnRolPeriodosCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */