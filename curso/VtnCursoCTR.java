/*     */ package controlador.curso;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.AlumnoCursoMD;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.curso.FrmCurso;
/*     */ import vista.curso.VtnCurso;
/*     */ 
/*     */ public class VtnCursoCTR
/*     */   extends DVtnCTR {
/*     */   private final VtnCurso vtnCurso;
/*     */   private ArrayList<AlumnoCursoMD> almns;
/*  37 */   private final CursoBD CBD = CursoBD.single();
/*     */   
/*     */   private ArrayList<CursoMD> cursos;
/*  40 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*     */   
/*     */   private ArrayList<String> nombresC;
/*     */   private String b;
/*     */   
/*     */   public VtnCursoCTR(VtnCurso vtnCurso, VtnPrincipalCTR ctrPrin) {
/*  48 */     super(ctrPrin);
/*  49 */     this.vtnCurso = vtnCurso;
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  53 */     this.vtnCurso.getBtnListaAlumnos().setEnabled(false);
/*  54 */     this.vtnCurso.getBtnListaSilabos().setEnabled(false);
/*     */ 
/*     */ 
/*     */     
/*  58 */     String[] titulo = { "id", "Periodo", "Materia", "Cedula", "Docente", "Ciclo", "Curso", "Capacidad", "Matriculados" };
/*  59 */     String[][] datos = new String[0][];
/*  60 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  61 */     this.vtnCurso.getTblCurso().setModel(this.mdTbl);
/*     */     
/*  63 */     TblEstilo.formatoTbl(this.vtnCurso.getTblCurso());
/*  64 */     TblEstilo.ocualtarID(this.vtnCurso.getTblCurso());
/*     */     
/*  66 */     TblEstilo.columnaMedida(this.vtnCurso.getTblCurso(), 3, 100);
/*  67 */     TblEstilo.columnaMedida(this.vtnCurso.getTblCurso(), 5, 60);
/*  68 */     TblEstilo.columnaMedida(this.vtnCurso.getTblCurso(), 6, 60);
/*  69 */     TblEstilo.columnaMedida(this.vtnCurso.getTblCurso(), 7, 70);
/*  70 */     TblEstilo.columnaMedida(this.vtnCurso.getTblCurso(), 8, 70);
/*  71 */     cargarCmbPrdLectio();
/*  72 */     cargarNombreCursos();
/*  73 */     this.vtnCurso.getCmbPeriodoLectivo().setSelectedIndex(0);
/*     */     
/*  75 */     cargarCursosPorPeriodo();
/*  76 */     this.vtnCurso.getBtnIngresar().addActionListener(e -> abrirFrmCurso());
/*  77 */     this.vtnCurso.getBtnEditar().addActionListener(e -> editarCurso());
/*  78 */     this.vtnCurso.getBtnEliminar().addActionListener(e -> eliminarCurso());
/*  79 */     this.vtnCurso.getBtnHorario().addActionListener(e -> horario());
/*  80 */     this.vtnCurso.getCbxEliminados().addActionListener(e -> verCursosEliminados());
/*     */     
/*  82 */     this.vtnCurso.getCmbPeriodoLectivo().addActionListener(e -> cargarCursosPorPeriodo());
/*     */     
/*  84 */     this.vtnCurso.getCmbCurso().addActionListener(e -> cargarCursosPorNombre());
/*     */     
/*  86 */     this.vtnCurso.getBtnBuscar().addActionListener(e -> buscar(this.vtnCurso.getTxtBuscar().getText().trim()));
/*     */     
/*  88 */     this.vtnCurso.getBtnBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnCurso.getTxtBuscar(), this.vtnCurso
/*  89 */           .getBtnBuscar()));
/*  90 */     this.vtnCurso.getTblCurso().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  93 */             VtnCursoCTR.this.validarBotonesReportes();
/*     */           }
/*     */         });
/*  96 */     this.vtnCurso.getBtnListaAlumnos().addActionListener(e -> reporteListaAlumnos());
/*  97 */     this.vtnCurso.getBtnListaSilabos().addActionListener(e -> reporteListaSilabos());
/*  98 */     iniciarBuscador();
/*     */     
/* 100 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnCurso);
/*     */     
/* 102 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarBuscador() {
/* 109 */     this.vtnCurso.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 112 */             VtnCursoCTR.this.b = VtnCursoCTR.this.vtnCurso.getTxtBuscar().getText().trim();
/*     */             
/* 114 */             if (e.getKeyCode() == 10) {
/* 115 */               VtnCursoCTR.this.buscar(VtnCursoCTR.this.b);
/* 116 */             } else if (VtnCursoCTR.this.b.length() == 0) {
/* 117 */               VtnCursoCTR.this.cargarCursos();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void abrirFrmCurso() {
/* 128 */     FrmCurso frmCurso = new FrmCurso();
/* 129 */     this.ctrPrin.eventoInternal((JInternalFrame)frmCurso);
/* 130 */     FrmCursoCTR ctrFrmCurso = new FrmCursoCTR(frmCurso, this.ctrPrin, this);
/* 131 */     ctrFrmCurso.iniciar();
/* 132 */     this.vtnCurso.setVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void editarCurso() {
/* 139 */     this.posFila = this.vtnCurso.getTblCurso().getSelectedRow();
/* 140 */     if (this.posFila >= 0) {
/* 141 */       FrmCurso frmCurso = new FrmCurso();
/* 142 */       FrmCursoCTR ctrFrmCurso = new FrmCursoCTR(frmCurso, this.ctrPrin, this);
/* 143 */       ctrFrmCurso.iniciar();
/* 144 */       this.ctrPrin.eventoInternal((JInternalFrame)frmCurso);
/* 145 */       ctrFrmCurso.editar(this.cursos.get(this.posFila));
/* 146 */       this.vtnCurso.setVisible(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actualizarVtn() {
/* 154 */     int posPrd = this.vtnCurso.getCmbPeriodoLectivo().getSelectedIndex();
/* 155 */     int posCur = this.vtnCurso.getCmbCurso().getSelectedIndex();
/* 156 */     if (this.vtnCurso.getTxtBuscar().getText().length() > 0) {
/* 157 */       buscar(this.vtnCurso.getTxtBuscar().getText().trim());
/*     */     }
/* 159 */     else if (posPrd > 0 && posCur > 0) {
/* 160 */       cargarCursosPorNombre();
/* 161 */     } else if (posPrd > 0) {
/* 162 */       cargarCursosPorPeriodo();
/*     */     } else {
/* 164 */       cargarCursos();
/*     */     } 
/*     */     
/* 167 */     this.vtnCurso.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String b) {
/* 176 */     if (Validar.esLetrasYNumeros(b)) {
/* 177 */       this.cursos = this.CBD.buscarCursos(b);
/* 178 */       llenarTbl(this.cursos);
/*     */     } else {
/* 180 */       System.out.println("No ingrese caracteres especiales");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cargarCursos() {
/* 189 */     this.cursos = this.CBD.cargarCursos();
/* 190 */     llenarTbl(this.cursos);
/* 191 */     System.out.println("Se cargaron cursos");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cargarNombreCursos() {
/* 198 */     this.nombresC = this.CBD.cargarNombreCursos();
/* 199 */     cargarCmbCursos(this.nombresC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCursosPorPeriodo() {
/* 206 */     this.vtnCurso.getTxtBuscar().setText("");
/* 207 */     int posPrd = this.vtnCurso.getCmbPeriodoLectivo().getSelectedIndex();
/* 208 */     if (posPrd >= 0) {
/*     */       
/* 210 */       this.nombresC = this.CBD.cargarNombreCursosPorPeriodo(((PeriodoLectivoMD)this.periodos.get(posPrd)).getID());
/* 211 */       cargarCmbCursos(this.nombresC);
/*     */       
/* 213 */       this.cursos = this.CBD.cargarCursosPorPeriodo(((PeriodoLectivoMD)this.periodos.get(posPrd)).getID());
/* 214 */       llenarTbl(this.cursos);
/*     */     } else {
/* 216 */       cargarCursos();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCursosPorNombre() {
/* 224 */     this.vtnCurso.getTxtBuscar().setText("");
/* 225 */     int posNom = this.vtnCurso.getCmbCurso().getSelectedIndex();
/* 226 */     int posPrd = this.vtnCurso.getCmbPeriodoLectivo().getSelectedIndex();
/* 227 */     if (posNom == 0) {
/* 228 */       cargarCursosPorPeriodo();
/* 229 */     } else if (posNom > 0 && posPrd == 0) {
/* 230 */       this.cursos = this.CBD.cargarCursosPorNombre(this.vtnCurso.getCmbCurso().getSelectedItem().toString());
/* 231 */       llenarTbl(this.cursos);
/* 232 */     } else if (posNom > 0 && posPrd >= 0) {
/* 233 */       this.cursos = this.CBD.cargarCursosPorNombreYPrdLectivo(this.vtnCurso.getCmbCurso().getSelectedItem().toString(), ((PeriodoLectivoMD)this.periodos
/* 234 */           .get(posPrd)).getID());
/* 235 */       llenarTbl(this.cursos);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTbl(ArrayList<CursoMD> cursos) {
/* 245 */     this.mdTbl.setRowCount(0);
/* 246 */     if (cursos != null) {
/* 247 */       cursos.forEach(c -> {
/*     */             Object[] valores = { Integer.valueOf(c.getId()), c.getPeriodo().getNombre(), c.getMateria().getNombre(), c.getDocente().getIdentificacion(), c.getDocente().getPrimerNombre() + " " + c.getDocente().getPrimerApellido(), Integer.valueOf(c.getCiclo()), c.getNombre(), Integer.valueOf(c.getCapacidad()), Integer.valueOf(c.getNumMatriculados()) };
/*     */ 
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
/* 262 */       this.vtnCurso.getLblResultados().setText(cursos.size() + " Resultados obtenidos.");
/*     */     } else {
/* 264 */       this.vtnCurso.getLblResultados().setText("0 Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbPrdLectio() {
/* 272 */     this.periodos = this.PLBD.cargarPrdParaCmbVtn();
/* 273 */     this.vtnCurso.getCmbPeriodoLectivo().removeAllItems();
/* 274 */     if (this.periodos != null) {
/*     */       
/* 276 */       this.periodos.forEach(p -> this.vtnCurso.getCmbPeriodoLectivo().addItem(p.getNombre()));
/*     */ 
/*     */ 
/*     */       
/* 280 */       this.vtnCurso.getCmbPeriodoLectivo().addItem("Todos");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reporteListaAlumnos() {
/* 289 */     String path = "/vista/reportes/repListaAlumno.jasper";
/*     */     try {
/* 291 */       this.posFila = this.vtnCurso.getTblCurso().getSelectedRow();
/* 292 */       Map<Object, Object> parametro = new HashMap<>();
/* 293 */       parametro.put("curso", Integer.valueOf(((CursoMD)this.cursos.get(this.posFila)).getId()));
/* 294 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 295 */       CON.mostrarReporte(jr, parametro, "Lista de estudiantes");
/* 296 */     } catch (JRException ex) {
/* 297 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reporteListaSilabos() {
/* 303 */     String titleRepor = JOptionPane.showInputDialog("Escriba el título para su reporte");
/* 304 */     if (titleRepor.length() > 5) {
/*     */ 
/*     */ 
/*     */       
/* 308 */       String path = "/vista/reportes/repListaSocializacion_1.jasper";
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 313 */         this.posFila = this.vtnCurso.getTblCurso().getSelectedRow();
/* 314 */         Map<Object, Object> parametro = new HashMap<>();
/* 315 */         parametro.put("curso", Integer.valueOf(((CursoMD)this.cursos.get(this.posFila)).getId()));
/* 316 */         parametro.put("titulo", titleRepor);
/* 317 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/*     */         
/* 319 */         CON.mostrarReporte(jr, parametro, "Socialización Sílabos");
/* 320 */       } catch (JRException ex) {
/* 321 */         JOptionPane.showMessageDialog(null, "error " + ex.getMessage() + ex.getMessageKey());
/*     */       } 
/*     */     } else {
/*     */       
/* 325 */       JOptionPane.showMessageDialog(null, "Escriba primero un título");
/* 326 */       reporteListaSilabos();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbCursos(ArrayList<String> nombresC) {
/* 336 */     this.vtnCurso.getCmbCurso().removeAllItems();
/* 337 */     if (nombresC != null) {
/* 338 */       this.vtnCurso.getCmbCurso().addItem("Todos");
/* 339 */       nombresC.forEach(n -> this.vtnCurso.getCmbCurso().addItem(n));
/*     */ 
/*     */       
/* 342 */       this.vtnCurso.getCmbCurso().setSelectedIndex(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void eliminarCurso() {
/* 347 */     int posCur = this.vtnCurso.getTblCurso().getSelectedRow();
/*     */     
/* 349 */     if (posCur >= 0) {
/* 350 */       String nom = this.vtnCurso.getTblCurso().getValueAt(posCur, 5).toString();
/* 351 */       int num = this.CBD.numAlumnos(((CursoMD)this.cursos.get(posCur)).getId());
/* 352 */       int r = JOptionPane.showConfirmDialog((Component)this.vtnCurso, "Seguro quiere " + this.vtnCurso
/* 353 */           .getBtnEliminar().getText().toLowerCase() + " el curso " + nom + "\nSe " + this.vtnCurso
/* 354 */           .getBtnEliminar().getText().toLowerCase() + "an todos los alumnos de este curso: " + num);
/*     */       
/* 356 */       if (r == 0) {
/* 357 */         if (this.vtnCurso.getCbxEliminados().isSelected()) {
/* 358 */           this.CBD.activarCurso(((CursoMD)this.cursos.get(posCur)).getId());
/*     */         } else {
/* 360 */           this.CBD.eliminarCurso(((CursoMD)this.cursos.get(posCur)).getId());
/*     */         } 
/* 362 */         verCursosEliminados();
/*     */       } 
/*     */     } else {
/*     */       
/* 366 */       JOptionPane.showMessageDialog((Component)this.vtnCurso, "Debe seleccionar una final antes.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verCursosEliminados() {
/* 374 */     if (this.vtnCurso.getCbxEliminados().isSelected()) {
/* 375 */       this.cursos = this.CBD.cargarCursosEliminados();
/* 376 */       llenarTbl(this.cursos);
/* 377 */       this.vtnCurso.getBtnEliminar().setText("Activar");
/*     */     } else {
/* 379 */       this.cursos = this.CBD.cargarCursos();
/* 380 */       llenarTbl(this.cursos);
/* 381 */       this.vtnCurso.getBtnEliminar().setText("Eliminar");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 386 */     this.vtnCurso.getBtnHorario().getAccessibleContext().setAccessibleName("Cursos-Horarios");
/* 387 */     this.vtnCurso.getCbxEliminados().getAccessibleContext().setAccessibleName("Cursos-Ver Eliminados");
/* 388 */     this.vtnCurso.getBtnEliminar().getAccessibleContext().setAccessibleName("Cursos-Eliminar");
/* 389 */     this.vtnCurso.getBtnEditar().getAccessibleContext().setAccessibleName("Cursos-Editar");
/* 390 */     this.vtnCurso.getBtnIngresar().getAccessibleContext().setAccessibleName("Cursos-Ingresar");
/* 391 */     this.vtnCurso.getBtnListaSilabos().getAccessibleContext().setAccessibleName("Cursos-Reporte-Lista para silabos");
/* 392 */     this.vtnCurso.getBtnListaAlumnos().getAccessibleContext().setAccessibleName("Cursos-Reporte-Lista de alumnos");
/*     */     
/* 394 */     CONS.activarBtns(new JComponent[] { this.vtnCurso.getBtnHorario(), this.vtnCurso.getCbxEliminados(), this.vtnCurso
/* 395 */           .getBtnEliminar(), this.vtnCurso.getBtnEditar(), this.vtnCurso.getBtnIngresar(), this.vtnCurso
/* 396 */           .getBtnListaAlumnos(), this.vtnCurso.getBtnListaSilabos() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void validarBotonesReportes() {
/* 401 */     int selecTabl = this.vtnCurso.getTblCurso().getSelectedRow();
/* 402 */     if (selecTabl >= 0) {
/* 403 */       this.vtnCurso.getBtnListaAlumnos().setEnabled(true);
/* 404 */       this.vtnCurso.getBtnListaSilabos().setEnabled(true);
/*     */     } else {
/* 406 */       this.vtnCurso.getBtnListaAlumnos().setEnabled(false);
/* 407 */       this.vtnCurso.getBtnListaSilabos().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void horario() {
/* 415 */     this.posFila = this.vtnCurso.getTblCurso().getSelectedRow();
/* 416 */     if (this.posFila >= 0) {
/* 417 */       JDHorarioCTR ctr = new JDHorarioCTR(this.ctrPrin, this.cursos.get(this.posFila));
/* 418 */       ctr.iniciar();
/*     */     } else {
/* 420 */       JOptionPane.showMessageDialog((Component)this.vtnCurso, "Antes debe seleccionar un curso.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void boton() {}
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\curso\VtnCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */