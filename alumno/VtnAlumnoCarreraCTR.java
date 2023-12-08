/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoCarreraBD;
/*     */ import modelo.alumno.AlumnoCarreraMD;
/*     */ import modelo.alumno.Retirado;
/*     */ import modelo.alumno.RetiradoBD;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import utils.CONS;
/*     */ import vista.alumno.VtnAlumnoCarrera;
/*     */ 
/*     */ public class VtnAlumnoCarreraCTR
/*     */   extends DCTR {
/*     */   private final VtnAlumnoCarrera vtnAlmCar;
/*  29 */   private final AlumnoCarreraBD ACRBD = AlumnoCarreraBD.single();
/*     */   
/*     */   private ArrayList<AlumnoCarreraMD> almnsCarr;
/*     */   private final JDRetirarAlumnoCTR RCTR;
/*     */   private final JDEgresarAlumnoCTR ECTR;
/*  34 */   private final RetiradoBD RBD = RetiradoBD.single();
/*     */   
/*     */   private List<Retirado> retirados;
/*     */   
/*     */   private DefaultTableModel mdTbl;
/*     */   
/*  40 */   private final CarreraBD CRBD = CarreraBD.single();
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnAlumnoCarreraCTR(VtnAlumnoCarrera vtnAlmCar, VtnPrincipalCTR ctrPrin) {
/*  50 */     super(ctrPrin);
/*  51 */     this.vtnAlmCar = vtnAlmCar;
/*  52 */     this.RCTR = new JDRetirarAlumnoCTR(ctrPrin);
/*  53 */     this.ECTR = new JDEgresarAlumnoCTR(ctrPrin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  61 */     cargarCmbCarreras();
/*     */     
/*  63 */     String[] titulo = { "Carrera", "Alumno", "Cédula", "Fecha inscripción" };
/*  64 */     String[][] datos = new String[0][];
/*  65 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*     */     
/*  67 */     TblEstilo.formatoTbl(this.vtnAlmCar.getTblAlmnCarrera());
/*  68 */     TblEstilo.columnaMedida(this.vtnAlmCar.getTblAlmnCarrera(), 0, 70);
/*  69 */     TblEstilo.columnaMedida(this.vtnAlmCar.getTblAlmnCarrera(), 2, 120);
/*  70 */     this.vtnAlmCar.getTblAlmnCarrera().setModel(this.mdTbl);
/*     */     
/*  72 */     cargarAlmnsCarrera();
/*     */     
/*  74 */     this.vtnAlmCar.getCmbCarrera().addActionListener(e -> clickCmbCarreras());
/*     */     
/*  76 */     this.vtnAlmCar.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  79 */             String b = VtnAlumnoCarreraCTR.this.vtnAlmCar.getTxtBuscar().getText().trim();
/*  80 */             if (e.getKeyCode() == 10) {
/*  81 */               VtnAlumnoCarreraCTR.this.buscar(b);
/*  82 */             } else if (b.length() == 0) {
/*  83 */               VtnAlumnoCarreraCTR.this.cargarAlmnsCarrera();
/*     */             } 
/*     */           }
/*     */         });
/*  87 */     this.vtnAlmCar.getBtnBuscar().addActionListener(e -> buscar(this.vtnAlmCar.getTxtBuscar().getText().trim()));
/*  88 */     this.vtnAlmCar.getBtnIngresar().addActionListener(e -> abrirFrmAlumnoCarrera());
/*  89 */     this.vtnAlmCar.getCbxEliminados().addActionListener(e -> verEliminados());
/*     */     
/*  91 */     this.vtnAlmCar.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnAlmCar.getTxtBuscar(), this.vtnAlmCar
/*  92 */           .getBtnBuscar()));
/*  93 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnAlmCar);
/*  94 */     this.vtnAlmCar.getBtnRetirar().addActionListener(e -> abrirFRMRetirado());
/*  95 */     this.vtnAlmCar.getBtnEgresar().addActionListener(e -> abrirFrmEgresado());
/*  96 */     InitPermisos();
/*     */   }
/*     */   
/*     */   private void abrirFRMRetirado() {
/* 100 */     int posFila = this.vtnAlmCar.getTblAlmnCarrera().getSelectedRow();
/* 101 */     if (posFila >= 0) {
/* 102 */       this.RCTR.ingresar(this.almnsCarr.get(posFila));
/*     */     } else {
/* 104 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void abrirFrmEgresado() {
/* 109 */     int posFila = this.vtnAlmCar.getTblAlmnCarrera().getSelectedRow();
/* 110 */     if (posFila >= 0) {
/* 111 */       this.ECTR.ingresar(((AlumnoCarreraMD)this.almnsCarr.get(posFila)).getId());
/*     */     } else {
/* 113 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abrirFrmAlumnoCarrera() {
/* 121 */     this.ctrPrin.abrirFrmInscripcion();
/* 122 */     this.vtnAlmCar.dispose();
/* 123 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String b) {
/* 133 */     if (Validar.esLetrasYNumeros(b)) {
/* 134 */       this.almnsCarr = this.ACRBD.buscar(b);
/* 135 */       llenarTblAlmnCarreras(this.almnsCarr);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarAlmnsCarrera() {
/* 144 */     int posCar = this.vtnAlmCar.getCmbCarrera().getSelectedIndex();
/* 145 */     if (posCar > 0) {
/* 146 */       this.almnsCarr = this.ACRBD.cargarAlumnoCarreraPorCarrera(((CarreraMD)this.carreras
/* 147 */           .get(posCar - 1)).getId());
/*     */     } else {
/*     */       
/* 150 */       this.almnsCarr = this.ACRBD.cargarAlumnoCarrera();
/*     */     } 
/* 152 */     llenarTblAlmnCarreras(this.almnsCarr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTblAlmnCarreras(ArrayList<AlumnoCarreraMD> almns) {
/* 161 */     this.mdTbl.setRowCount(0);
/* 162 */     if (almns != null) {
/* 163 */       almns.forEach(a -> {
/*     */             Object[] valores = { a.getCarrera().getCodigo(), a.getAlumno().getPrimerApellido() + " " + a.getAlumno().getSegundoApellido() + " " + a.getAlumno().getPrimerNombre() + " " + a.getAlumno().getSegundoNombre(), a.getAlumno().getIdentificacion(), a.getFechaRegistro().getDayOfMonth() + "/" + a.getFechaRegistro().getMonth() + "/" + a.getFechaRegistro().getYear() };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             this.mdTbl.addRow(valores);
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       this.vtnAlmCar.getLblResultados().setText(almns.size() + " Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbCarreras() {
/* 185 */     this.carreras = this.CRBD.cargarCarrerasCmb();
/* 186 */     if (this.carreras != null) {
/* 187 */       this.vtnAlmCar.getCmbCarrera().removeAllItems();
/* 188 */       this.vtnAlmCar.getCmbCarrera().addItem("Todos");
/* 189 */       this.carreras.forEach(c -> this.vtnAlmCar.getCmbCarrera().addItem(c.getCodigo()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbCarreras() {
/* 200 */     int posCar = this.vtnAlmCar.getCmbCarrera().getSelectedIndex();
/* 201 */     if (posCar > 0) {
/* 202 */       this.almnsCarr = this.ACRBD.cargarAlumnoCarreraPorCarrera(((CarreraMD)this.carreras.get(posCar - 1)).getId());
/* 203 */       llenarTblAlmnCarreras(this.almnsCarr);
/*     */     } else {
/* 205 */       cargarAlmnsCarrera();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verEliminados() {
/* 214 */     if (this.vtnAlmCar.getCbxEliminados().isSelected()) {
/* 215 */       this.almnsCarr = this.ACRBD.cargarAlumnoCarreraEliminados();
/* 216 */       llenarTblAlmnCarreras(this.almnsCarr);
/* 217 */       this.vtnAlmCar.getCmbCarrera().setEnabled(false);
/*     */     } else {
/* 219 */       this.almnsCarr = this.ACRBD.cargarAlumnoCarrera();
/* 220 */       llenarTblAlmnCarreras(this.almnsCarr);
/* 221 */       this.vtnAlmCar.getCmbCarrera().setEnabled(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void InitPermisos() {
/* 227 */     this.vtnAlmCar.getBtnIngresar().getAccessibleContext().setAccessibleName("Inscripcion-Ingresar");
/* 228 */     this.vtnAlmCar.getCbxEliminados().getAccessibleContext().setAccessibleName("Inscripcion-Ver Elimandos");
/* 229 */     CONS.activarBtns(new JComponent[] { this.vtnAlmCar.getBtnIngresar(), this.vtnAlmCar.getCbxEliminados() });
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoCarreraCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */