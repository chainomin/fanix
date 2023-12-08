/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.Egresado;
/*     */ import modelo.alumno.EgresadoBD;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import vista.alumno.VtnAlumnoEgresados;
/*     */ 
/*     */ public abstract class AVtnAlumnoEgresadoCTR
/*     */   extends DCTR
/*     */ {
/*     */   protected final VtnAlumnoEgresados vtn;
/*     */   protected List<Egresado> egresados;
/*     */   protected List<Egresado> todosEgresados;
/*     */   protected DefaultTableModel mdTbl;
/*  27 */   protected final EgresadoBD EBD = EgresadoBD.single();
/*  28 */   private final CarreraBD CBD = CarreraBD.single();
/*  29 */   private final PeriodoLectivoBD PBD = PeriodoLectivoBD.single();
/*     */   
/*     */   private ArrayList<CarreraMD> todasCarreras;
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> todosPeriodos;
/*     */   protected ArrayList<CarreraMD> carreras;
/*     */   protected ArrayList<PeriodoLectivoMD> periodos;
/*     */   private final JDEgresarAlumnoCTR ECTR;
/*     */   private IAlumnoEgresadoVTNCTR VTNCTR;
/*     */   
/*     */   public AVtnAlumnoEgresadoCTR(VtnPrincipalCTR ctrPrin) {
/*  40 */     super(ctrPrin);
/*  41 */     this.vtn = new VtnAlumnoEgresados();
/*  42 */     this.ECTR = new JDEgresarAlumnoCTR(ctrPrin);
/*     */   }
/*     */   
/*     */   protected void iniciarAcciones() {
/*  46 */     this.vtn.getBtnEliminar().addActionListener(e -> clickEliminar());
/*  47 */     this.vtn.getBtnEditar().addActionListener(e -> clickEditar());
/*  48 */     this.vtn.getBtnNotasAlumno().setVisible(false);
/*  49 */     this.vtn.getBtnNotasPeriodo().setVisible(false);
/*     */   }
/*     */   
/*     */   private void clickEliminar() {
/*  53 */     int posFila = this.vtn.getTblEgresados().getSelectedRow();
/*  54 */     if (posFila >= 0) {
/*  55 */       int r = JOptionPane.showConfirmDialog((Component)this.vtn, "Esta seguro de eliminar el egreso de: \n" + ((Egresado)this.egresados
/*     */ 
/*     */           
/*  58 */           .get(posFila)).getAlmnCarrera()
/*  59 */           .getAlumno().getNombreCompleto());
/*     */       
/*  61 */       if (r == 0) {
/*  62 */         if (this.EBD.eliminar(((Egresado)this.egresados.get(posFila)).getId())) {
/*  63 */           JOptionPane.showMessageDialog((Component)this.vtn, "Eliminamos correctamente.");
/*     */ 
/*     */ 
/*     */           
/*  67 */           this.mdTbl.removeRow(posFila);
/*     */         } else {
/*  69 */           JOptionPane.showMessageDialog((Component)this.vtn, "No se elimino el registro.");
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  77 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickEditar() {
/*  82 */     int posFila = this.vtn.getTblEgresados().getSelectedRow();
/*  83 */     if (posFila >= 0) {
/*  84 */       this.ECTR.editar(this.egresados.get(posFila));
/*     */     } else {
/*  86 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void iniciarVtn(String[] titulo, IAlumnoEgresadoVTNCTR VTNCTR) {
/*  91 */     this.VTNCTR = VTNCTR;
/*  92 */     this.mdTbl = TblEstilo.modelTblSinEditar(titulo);
/*  93 */     this.vtn.getTblEgresados().setModel(this.mdTbl);
/*  94 */     cargarCmb();
/*  95 */     this.vtn.getCmbCarrera().addActionListener(e -> clickCarreras());
/*  96 */     this.vtn.getCmbPeriodo().addActionListener(e -> clickPeriodo());
/*     */   }
/*     */   
/*     */   private void cargarCmb() {
/* 100 */     this.todasCarreras = this.CBD.cargarCarrerasCmb();
/* 101 */     this.todosPeriodos = this.PBD.cargarPrdParaCmbVtn();
/* 102 */     this.carreras = this.todasCarreras;
/* 103 */     this.periodos = this.todosPeriodos;
/* 104 */     llenarCmbCarreras(this.carreras);
/* 105 */     llenarCmbPeriodos(this.periodos);
/*     */   }
/*     */   
/*     */   private void clickPeriodo() {
/* 109 */     int posPeriodo = this.vtn.getCmbPeriodo().getSelectedIndex();
/* 110 */     if (posPeriodo > 0 && this.vtnCargada) {
/* 111 */       this.egresados = new ArrayList<>();
/* 112 */       this.todosEgresados.forEach(e -> {
/*     */             if (e.getPeriodo().getNombre().equals(this.vtn.getCmbPeriodo().getSelectedItem().toString())) {
/*     */               this.egresados.add(e);
/*     */             }
/*     */           });
/*     */       
/* 118 */       this.VTNCTR.llenarTbl(this.egresados);
/*     */     } else {
/* 120 */       clickCarreras();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickCarreras() {
/* 125 */     int posCarrera = this.vtn.getCmbCarrera().getSelectedIndex();
/* 126 */     if (posCarrera > 0 && this.vtnCargada) {
/* 127 */       this.periodos = new ArrayList<>();
/* 128 */       this.todosPeriodos.forEach(p -> {
/*     */             if (p.getCarrera().getId() == ((CarreraMD)this.carreras.get(posCarrera - 1)).getId()) {
/*     */               this.periodos.add(p);
/*     */             }
/*     */           });
/* 133 */       llenarCmbPeriodos(this.periodos);
/* 134 */       this.egresados = new ArrayList<>();
/* 135 */       this.todosEgresados.forEach(e -> {
/*     */             if (e.getAlmnCarrera().getCarrera().getCodigo().equals(this.vtn.getCmbCarrera().getSelectedItem().toString())) {
/*     */               this.egresados.add(e);
/*     */             }
/*     */           });
/*     */       
/* 141 */       this.VTNCTR.llenarTbl(this.egresados);
/*     */     } else {
/* 143 */       this.periodos = this.todosPeriodos;
/* 144 */       llenarCmbPeriodos(this.periodos);
/* 145 */       this.egresados = this.todosEgresados;
/* 146 */       this.VTNCTR.llenarTbl(this.egresados);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarCmbCarreras(ArrayList<CarreraMD> carreras) {
/* 151 */     this.vtn.getCmbCarrera().removeAllItems();
/* 152 */     this.vtn.getCmbCarrera().addItem("Seleccione");
/* 153 */     carreras.forEach(c -> this.vtn.getCmbCarrera().addItem(c.getCodigo()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbPeriodos(ArrayList<PeriodoLectivoMD> periodos) {
/* 159 */     this.vtn.getCmbPeriodo().removeAllItems();
/* 160 */     this.vtn.getCmbPeriodo().addItem("Seleccione");
/* 161 */     periodos.forEach(p -> this.vtn.getCmbPeriodo().addItem(p.getNombre()));
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\AVtnAlumnoEgresadoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */