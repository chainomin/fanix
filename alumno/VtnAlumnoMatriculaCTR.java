/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JInternalFrame;
/*     */ import modelo.alumno.AlumnoMatriculaBD;
/*     */ import modelo.alumno.AlumnoMatriculaMD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.VtnAlumnoMatricula;
/*     */ 
/*     */ 
/*     */ public class VtnAlumnoMatriculaCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final VtnAlumnoMatricula vtnMatri;
/*  22 */   private final AlumnoMatriculaBD almMatri = AlumnoMatriculaBD.single();
/*     */   
/*     */   private ArrayList<AlumnoMatriculaMD> almnMatricula;
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*  26 */   private final PeriodoLectivoBD prd = PeriodoLectivoBD.single();
/*     */   private int posPrd;
/*     */   
/*     */   public VtnAlumnoMatriculaCTR(VtnPrincipalCTR ctrPrin, VtnAlumnoMatricula vtnMatri) {
/*  30 */     super(ctrPrin);
/*  31 */     this.vtnMatri = vtnMatri;
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  35 */     String[] t = { "Periodo", "Cedula", "Nombres", "Apellidos", "Correo", "Celular", "Telefono", "Carrera", "Cursos" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  43 */     this.mdTbl = iniciarTbl(this.vtnMatri.getTblMatricula(), t);
/*     */     
/*  45 */     llenarCmbPrd();
/*  46 */     formatoBuscador(this.vtnMatri.getTxtBuscar(), this.vtnMatri.getBtnBuscar());
/*  47 */     iniciarBuscador();
/*     */     
/*  49 */     this.vtnMatri.getCmbPeriodos().addActionListener(e -> clickPrd());
/*  50 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnMatri);
/*     */   }
/*     */   
/*     */   private void iniciarBuscador() {
/*  54 */     this.vtnMatri.getBtnBuscar().addActionListener(e -> buscar(this.vtnMatri.getTxtBuscar().getText().trim()));
/*     */     
/*  56 */     this.vtnMatri.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  59 */             if (e.getKeyCode() == 10) {
/*  60 */               VtnAlumnoMatriculaCTR.this.buscar(VtnAlumnoMatriculaCTR.this.vtnMatri.getTxtBuscar().getText().trim());
/*  61 */             } else if (VtnAlumnoMatriculaCTR.this.vtnMatri.getTxtBuscar().getText().length() == 0) {
/*  62 */               VtnAlumnoMatriculaCTR.this.cargarAlumnosMatriculas();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void buscar(String aguja) {
/*  69 */     if (Validar.esLetrasYNumeros(aguja)) {
/*  70 */       this.almnMatricula = this.almMatri.buscarPor(aguja);
/*  71 */       llenarTbl(this.almnMatricula);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cargarAlumnosMatriculas() {
/*  76 */     (new Thread(() -> {
/*     */           this.almnMatricula = this.almMatri.getTodos();
/*     */           llenarTbl(this.almnMatricula);
/*  79 */         })).start();
/*     */   }
/*     */   
/*     */   private void llenarTbl(ArrayList<AlumnoMatriculaMD> alumnosMatriculas) {
/*  83 */     this.mdTbl.setRowCount(0);
/*  84 */     if (alumnosMatriculas != null) {
/*  85 */       alumnosMatriculas.forEach(am -> {
/*     */             Object[] v = { am.getPeriodo().getNombre(), am.getAlumno().getIdentificacion(), am.getAlumno().getSoloNombres(), am.getAlumno().getSoloApellidos(), am.getAlumno().getCorreo(), am.getAlumno().getCelular(), am.getAlumno().getTelefono(), am.getPeriodo().getCarrera().getCodigo(), am.getCursos() };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             this.mdTbl.addRow(v);
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  99 */       this.vtnMatri.getLblNumResultados().setText(alumnosMatriculas.size() + " Resultados obtenidos.");
/*     */     } else {
/* 101 */       this.vtnMatri.getLblNumResultados().setText("0 Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickPrd() {
/* 106 */     this.posPrd = this.vtnMatri.getCmbPeriodos().getSelectedIndex();
/* 107 */     if (this.posPrd > 0) {
/* 108 */       this.almnMatricula = this.almMatri.getPorPeriodo(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 109 */       llenarTbl(this.almnMatricula);
/*     */     } else {
/* 111 */       cargarAlumnosMatriculas();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbPrd() {
/* 119 */     this.periodos = this.prd.cargarPrdParaCmbVtn();
/* 120 */     this.vtnMatri.getCmbPeriodos().removeAllItems();
/* 121 */     if (this.periodos != null) {
/* 122 */       this.vtnMatri.getCmbPeriodos().addItem("Seleccione");
/* 123 */       this.periodos.forEach(p -> this.vtnMatri.getCmbPeriodos().addItem(p.getNombre()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoMatriculaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */