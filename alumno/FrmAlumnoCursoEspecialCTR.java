/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoCursoBD;
/*     */ import modelo.alumno.MatriculaBD;
/*     */ import modelo.alumno.MatriculaMD;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.persona.AlumnoBD;
/*     */ import modelo.persona.AlumnoMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.FrmAlumnoCursoEspecial;
/*     */ 
/*     */ public class FrmAlumnoCursoEspecialCTR extends DCTR {
/*     */   private DefaultTableModel mdTblMP;
/*     */   private DefaultTableModel mdTblMS;
/*     */   private DefaultTableModel mdTblAlm;
/*     */   private List<PeriodoLectivoMD> pls;
/*     */   private ArrayList<AlumnoMD> als;
/*     */   private ArrayList<String> ncs;
/*     */   private ArrayList<CursoMD> tcs;
/*     */   private ArrayList<CursoMD> csp;
/*     */   private ArrayList<CursoMD> css;
/*  37 */   private final AlumnoBD ALBD = AlumnoBD.single();
/*  38 */   private final CursoBD CRBD = CursoBD.single();
/*  39 */   private final AlumnoCursoBD ACBD = AlumnoCursoBD.single();
/*  40 */   private final MatriculaBD MTBD = MatriculaBD.single();
/*     */   
/*  42 */   private final FrmAlumnoCursoEspecial FRM = new FrmAlumnoCursoEspecial();
/*     */   
/*  44 */   private String materiasMatricula = "";
/*  45 */   private int numMateria = 0;
/*     */   
/*     */   public FrmAlumnoCursoEspecialCTR(VtnPrincipalCTR ctrPrin) {
/*  48 */     super(ctrPrin);
/*  49 */     this.css = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  53 */     iniciarCmbPeriodo();
/*  54 */     iniciarTbls();
/*  55 */     iniciarBuscador();
/*  56 */     iniciarCmbCurso();
/*  57 */     iniciarAcciones();
/*     */     
/*  59 */     this.ctrPrin.agregarVtn((JInternalFrame)this.FRM);
/*  60 */     this.vtnCargada = true;
/*     */   }
/*     */   
/*     */   private void iniciarAcciones() {
/*  64 */     this.FRM.getBtnPasar1().addActionListener(e -> pasarUno());
/*  65 */     this.FRM.getBtnPasarTodos().addActionListener(e -> pasarTodos());
/*  66 */     this.FRM.getBtnRegresar1().addActionListener(e -> regresarUno());
/*  67 */     this.FRM.getBtnRegresarTodos().addActionListener(e -> regresarTodos());
/*  68 */     this.FRM.getBtnGuardar().addActionListener(e -> guardar());
/*     */   }
/*     */   
/*     */   private void iniciarCmbPeriodo() {
/*  72 */     this.FRM.getCmbPrdLectivo().removeAllItems();
/*  73 */     this.FRM.getCmbPrdLectivo().addItem("Seleccione");
/*  74 */     PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*  75 */     this.pls = PLBD.cargarPeriodoEspecial();
/*  76 */     this.pls.forEach(e -> this.FRM.getCmbPrdLectivo().addItem(e.getNombre()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarCmbCurso() {
/*  82 */     this.FRM.getCmbCurso().addActionListener(e -> {
/*     */           if (this.vtnCargada) {
/*     */             cargarMaterias();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void cargarMaterias() {
/*  90 */     int posCurso = this.FRM.getCmbCurso().getSelectedIndex();
/*  91 */     if (posCurso > 0) {
/*  92 */       String nombre = this.FRM.getCmbCurso().getSelectedItem().toString();
/*  93 */       this.csp = new ArrayList<>();
/*  94 */       this.tcs.forEach(c -> {
/*     */             if (c.getNombre().equals(nombre)) {
/*     */               this.csp.add(c);
/*     */             }
/*     */           });
/*     */       
/* 100 */       this.mdTblMP.setRowCount(0);
/* 101 */       this.csp.forEach(c -> {
/*     */             Object[] r = { c.getMateria().getNombre(), "0.0" };
/*     */             this.mdTblMP.addRow(r);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private Function<String, Void> buscarAlumno() {
/* 109 */     return t -> {
/*     */         if (Validar.esLetrasYNumeros(t)) {
/*     */           buscarAlumno(t);
/*     */         }
/*     */         return null;
/*     */       };
/*     */   }
/*     */   
/*     */   private void buscarAlumno(String aguja) {
/* 118 */     this.als = this.ALBD.buscarAlumnos(aguja);
/* 119 */     this.mdTblAlm.setRowCount(0);
/* 120 */     this.als.forEach(a -> {
/*     */           Object[] r = { a.getIdentificacion(), a.getApellidosNombres() };
/*     */           this.mdTblAlm.addRow(r);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarBuscador() {
/* 130 */     listenerTxtBuscar(this.FRM
/* 131 */         .getTxtBuscar(), this.FRM
/* 132 */         .getBtnBuscar(), 
/* 133 */         buscarAlumno());
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickAlumno() {
/* 138 */     int posAlmn = this.FRM.getTblAlumnos().getSelectedRow();
/* 139 */     int posPrd = this.FRM.getCmbPrdLectivo().getSelectedIndex();
/* 140 */     if (posAlmn >= 0 && posPrd > 0) {
/* 141 */       this.ncs = this.CRBD.cargarNombreCursosPorPeriodo(((PeriodoLectivoMD)this.pls
/* 142 */           .get(posPrd - 1)).getID(), 0, 10);
/*     */ 
/*     */ 
/*     */       
/* 146 */       this.vtnCargada = false;
/* 147 */       this.FRM.getCmbCurso().removeAllItems();
/* 148 */       this.FRM.getCmbCurso().addItem("Seleccione");
/* 149 */       this.ncs.forEach(c -> this.FRM.getCmbCurso().addItem(c));
/*     */ 
/*     */       
/* 152 */       this.vtnCargada = true;
/*     */       
/* 154 */       this.tcs = this.CRBD.buscarCursosPorPeriodoAlumno(((PeriodoLectivoMD)this.pls
/* 155 */           .get(posPrd - 1)).getID(), ((AlumnoMD)this.als
/* 156 */           .get(posAlmn)).getId_Alumno());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void iniciarTbls() {
/* 162 */     String[] TM = { "Materia" };
/* 163 */     String[] TAL = { "Cedula", "Nombre" };
/* 164 */     this.mdTblMP = iniciarTbl(this.FRM.getTblMateriasPen(), TM);
/* 165 */     this.mdTblMS = iniciarTbl(this.FRM.getTblMateriasSelec(), TM);
/* 166 */     this.mdTblAlm = iniciarTbl(this.FRM.getTblAlumnos(), TAL);
/*     */     
/* 168 */     this.FRM.getTblAlumnos().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 171 */             FrmAlumnoCursoEspecialCTR.this.clickAlumno();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void regresarTodos() {
/* 177 */     this.css = new ArrayList<>();
/* 178 */     this.mdTblMS.setRowCount(0);
/*     */   }
/*     */   
/*     */   private void regresarUno() {
/* 182 */     int pos = this.FRM.getTblMateriasSelec().getSelectedRow();
/* 183 */     if (pos >= 0) {
/* 184 */       this.css.remove(pos);
/* 185 */       this.mdTblMS.removeRow(pos);
/*     */     } else {
/* 187 */       JOptionPane.showMessageDialog((Component)this.FRM, "Debe seleccionar una materia.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pasarUno() {
/* 195 */     int pos = this.FRM.getTblMateriasPen().getSelectedRow();
/* 196 */     if (pos >= 0) {
/* 197 */       this.css.add(this.csp.get(pos));
/* 198 */       this.csp.remove(pos);
/* 199 */       this.mdTblMP.removeRow(pos);
/* 200 */       llenarTblMS(this.css);
/*     */     } else {
/* 202 */       JOptionPane.showMessageDialog((Component)this.FRM, "Debe seleccionar una materia.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pasarTodos() {
/* 210 */     if (!this.csp.isEmpty()) {
/* 211 */       this.mdTblMP.setRowCount(0);
/* 212 */       this.css = this.csp;
/* 213 */       llenarTblMS(this.css);
/*     */     } else {
/* 215 */       JOptionPane.showMessageDialog((Component)this.FRM, "No hay materias que pasar.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTblMS(ArrayList<CursoMD> cs) {
/* 223 */     this.mdTblMS.setRowCount(0);
/* 224 */     cs.forEach(c -> {
/*     */           Object[] r = { c.getMateria().getNombre() };
/*     */           this.mdTblMS.addRow(r);
/*     */         });
/*     */   }
/*     */   
/*     */   private void guardar() {
/* 231 */     boolean guardar = !this.css.isEmpty();
/* 232 */     int posAlm = this.FRM.getTblAlumnos().getSelectedRow();
/* 233 */     int posPrd = this.FRM.getCmbPrdLectivo().getSelectedIndex();
/*     */     
/* 235 */     if (posAlm < 0 && posPrd < 1) {
/* 236 */       guardar = false;
/*     */     }
/*     */     
/* 239 */     if (guardar) {
/*     */       
/* 241 */       this.ACBD.borrarMatricula();
/* 242 */       this.materiasMatricula = "";
/* 243 */       this.numMateria = 1;
/* 244 */       this.css.forEach(c -> {
/*     */             this.ACBD.agregarMatricula(((AlumnoMD)this.als.get(posAlm)).getId_Alumno(), c.getId(), 1);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             this.materiasMatricula += this.numMateria + ":   Curso: " + c.getNombre() + "  Matricula: " + c.getNumMatricula() + "  Materia: " + c.getMateria().getNombre() + "    \n";
/*     */ 
/*     */ 
/*     */             
/*     */             this.numMateria++;
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 259 */       int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Se matricula a: \n" + ((AlumnoMD)this.als
/* 260 */           .get(posAlm)).getNombreCorto() + "\nPeriodo: \n" + ((PeriodoLectivoMD)this.pls
/* 261 */           .get(posPrd - 1)).getNombre() + "\nEn las siguientes materias: \n" + this.materiasMatricula);
/*     */ 
/*     */       
/* 264 */       if (r == 0) {
/*     */         
/* 266 */         MatriculaMD m = this.MTBD.buscarMatriculaAlmnPrd(((AlumnoMD)this.als
/* 267 */             .get(posAlm)).getId_Alumno(), ((PeriodoLectivoMD)this.pls
/* 268 */             .get(posPrd - 1)).getID());
/*     */ 
/*     */         
/* 271 */         if (m == null) {
/* 272 */           MatriculaMD matricula = new MatriculaMD();
/* 273 */           matricula.setAlumno(this.als.get(posAlm));
/* 274 */           matricula.setPeriodo(this.pls.get(posPrd - 1));
/* 275 */           matricula.setTipo("ORDINARIA");
/* 276 */           this.MTBD.ingresar(matricula);
/*     */         } 
/*     */         
/* 279 */         if (this.ACBD.guardarAlmnCurso()) {
/* 280 */           JOptionPane.showMessageDialog((Component)this.FRM, "Guadamos correctamente.");
/* 281 */           recetearFrm();
/*     */         } else {
/* 283 */           JOptionPane.showMessageDialog((Component)this.FRM, "No pudimos guardar.");
/*     */         } 
/*     */       } 
/*     */     } else {
/* 287 */       JOptionPane.showMessageDialog((Component)this.FRM, "El formulario contiene errores.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recetearFrm() {
/* 292 */     this.css = new ArrayList<>();
/* 293 */     this.csp = this.css;
/* 294 */     this.FRM.getTxtBuscar().setText("");
/* 295 */     this.mdTblAlm.setRowCount(0);
/* 296 */     this.mdTblMP.setRowCount(0);
/* 297 */     this.mdTblMS.setRowCount(0);
/* 298 */     this.FRM.getCmbCurso().removeAllItems();
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\FrmAlumnoCursoEspecialCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */