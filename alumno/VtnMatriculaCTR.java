/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.MatriculaBD;
/*     */ import modelo.alumno.MatriculaMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.alumno.VtnMatricula;
/*     */ 
/*     */ public class VtnMatriculaCTR
/*     */   extends DVtnCTR {
/*     */   private final VtnMatricula vtnMatri;
/*  31 */   private final MatriculaBD MTBD = MatriculaBD.single();
/*     */   
/*     */   private ArrayList<MatriculaMD> matriculas;
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*  36 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */ 
/*     */ 
/*     */   
/*     */   private int posPrd;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnMatriculaCTR(VtnPrincipalCTR ctrPrin, VtnMatricula vtnMatri) {
/*  46 */     super(ctrPrin);
/*  47 */     this.vtnMatri = vtnMatri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  55 */     String[] t = { "Periodo", "Cedula", "Alumno", "Fecha" };
/*  56 */     this.mdTbl = iniciarTbl(this.vtnMatri.getTblMatricula(), t);
/*     */     
/*  58 */     TblEstilo.columnaMedida(this.vtnMatri.getTblMatricula(), 1, 100);
/*     */     
/*  60 */     llenarCmbPrd();
/*  61 */     cargarMatriculas();
/*     */     
/*  63 */     iniciarAcciones();
/*  64 */     formatoBuscador(this.vtnMatri.getTxtBuscar(), this.vtnMatri.getBtnBuscar());
/*  65 */     iniciarBuscador();
/*  66 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnMatri);
/*  67 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean validarFecha() {
/*  78 */     LocalDate fi = this.PLBD.buscarFechaInicioPrd(((MatriculaMD)this.matriculas.get(this.posFila)).getPeriodo().getID());
/*  79 */     LocalDate fa = LocalDate.now();
/*  80 */     return fa.isBefore(fi.plusMonths(1L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickAnular() {
/*  87 */     this.posFila = this.vtnMatri.getTblMatricula().getSelectedRow();
/*  88 */     if (this.posFila >= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       if (validarFecha()) {
/*  95 */         JDAnularMatriculaCTR ctr = new JDAnularMatriculaCTR(this.ctrPrin, this.matriculas.get(this.posFila));
/*  96 */         ctr.iniciar();
/*     */       } else {
/*  98 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Ya pasaron mas de 30 dias ya no se puede anular la matricula.");
/*     */       } 
/*     */     } else {
/* 101 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickEditar() {
/* 110 */     this.posFila = this.vtnMatri.getTblMatricula().getSelectedRow();
/* 111 */     if (this.posFila >= 0) {
/* 112 */       JDEditarMatriculaCTR ctr = new JDEditarMatriculaCTR(this.ctrPrin, this.matriculas.get(this.posFila));
/* 113 */       ctr.iniciar();
/*     */     } else {
/*     */       
/* 116 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarBuscador() {
/* 124 */     this.vtnMatri.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e)
/*     */           {
/* 128 */             if (e.getKeyCode() == 10) {
/* 129 */               VtnMatriculaCTR.this.buscar(VtnMatriculaCTR.this.vtnMatri.getTxtBuscar().getText().trim());
/* 130 */             } else if (VtnMatriculaCTR.this.vtnMatri.getTxtBuscar().getText().length() == 0) {
/* 131 */               VtnMatriculaCTR.this.cargarMatriculas();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String aguja) {
/* 143 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 144 */       this.matriculas = this.MTBD.buscarMatriculas(aguja);
/* 145 */       llenarTbl(this.matriculas);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarAcciones() {
/* 153 */     this.vtnMatri.getCmbPeriodos().addActionListener(e -> clickPrd());
/* 154 */     this.vtnMatri.getBtnImprimirFicha().addActionListener(e -> clickImprimirFicha());
/* 155 */     this.vtnMatri.getBtnHistoria().addActionListener(e -> llamaReporteMatriculaPeriodo());
/* 156 */     this.vtnMatri.getBtnIngresar().addActionListener(e -> abrirFrm());
/* 157 */     this.vtnMatri.getBtnEditar().addActionListener(e -> clickEditar());
/* 158 */     this.vtnMatri.getBtnAnular().addActionListener(e -> clickAnular());
/* 159 */     this.vtnMatri.getBtnCartaCompromiso().addActionListener(e -> clickCartaCompromiso());
/* 160 */     this.vtnMatri.getBtnNumMatricula().addActionListener(e -> clickNumMatricula());
/* 161 */     this.vtnMatri.getBtnReporteTipoMatricula().addActionListener(e -> abrirJDReporteTipoMatricula());
/*     */   }
/*     */   
/*     */   private void abrirJDReporteTipoMatricula() {
/* 165 */     JDReporteTipoMatriculaCTR ctr = new JDReporteTipoMatriculaCTR(this.ctrPrin, this.periodos);
/* 166 */     ctr.iniciar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarMatriculas() {
/* 173 */     this.matriculas = this.MTBD.cargarMatriculas();
/* 174 */     llenarTbl(this.matriculas);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickPrd() {
/* 182 */     this.posPrd = this.vtnMatri.getCmbPeriodos().getSelectedIndex();
/* 183 */     if (this.posPrd > 0) {
/* 184 */       this.matriculas = this.MTBD.cargarMatriculasPorPrd(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 185 */       llenarTbl(this.matriculas);
/*     */     } else {
/* 187 */       cargarMatriculas();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbPrd() {
/* 195 */     this.periodos = this.PLBD.cargarPrdParaCmbVtn();
/* 196 */     this.vtnMatri.getCmbPeriodos().removeAllItems();
/* 197 */     if (this.periodos != null) {
/* 198 */       this.vtnMatri.getCmbPeriodos().addItem("Seleccione");
/* 199 */       this.periodos.forEach(p -> this.vtnMatri.getCmbPeriodos().addItem(p.getNombre()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTbl(ArrayList<MatriculaMD> matriculas) {
/* 211 */     this.mdTbl.setRowCount(0);
/* 212 */     if (matriculas != null) {
/* 213 */       matriculas.forEach(m -> {
/*     */             Object[] v = { m.getPeriodo().getNombre(), m.getAlumno().getIdentificacion(), m.getAlumno().getNombreCompleto(), m.getSoloFecha(), m.getSoloHora() };
/*     */ 
/*     */             
/*     */             this.mdTbl.addRow(v);
/*     */           });
/*     */       
/* 220 */       this.vtnMatri.getLblNumResultados().setText(matriculas.size() + " Resultados obtenidos.");
/*     */     } else {
/* 222 */       this.vtnMatri.getLblNumResultados().setText("0 Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abrirFrm() {
/* 230 */     this.ctrPrin.abrirFrmMatricula();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickImprimirFicha() {
/* 238 */     this.posFila = this.vtnMatri.getTblMatricula().getSelectedRow();
/* 239 */     if (this.posFila >= 0) {
/* 240 */       int s = JOptionPane.showOptionDialog((Component)this.vtnMatri, "Reporte de matricula\n¿Elegir el tipo de reporte?", "Ficha matricula", 1, 1, null, new Object[] { "Con Foto", "Sin Foto", "Cancelar" }, "Con Foto");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 248 */       switch (s) {
/*     */         case 0:
/* 250 */           llamaReporteMatricula();
/*     */           break;
/*     */         case 1:
/* 253 */           llamaReporteMatriculaSinFoto();
/*     */           break;
/*     */       } 
/*     */     } else {
/* 257 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una persona antes.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCartaCompromiso() {
/* 265 */     this.posFila = this.vtnMatri.getTblMatricula().getSelectedRow();
/* 266 */     if (this.posFila >= 0) {
/* 267 */       int s = JOptionPane.showOptionDialog((Component)this.vtnMatri, "Reporte de matricula\n¿Elegir el tipo de carta compromiso?", "Cartas compromiso", 1, 1, null, new Object[] { "Acta", "Segunda Matricula", "Tercera Matricula", "Cancelar" }, "Acta");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 275 */       switch (s) {
/*     */         case 0:
/* 277 */           llamaReporteActa();
/*     */           break;
/*     */         case 1:
/* 280 */           llamaReporteCartaNumMatricula(2, "SEGUNDA MATRÍCULA");
/*     */           break;
/*     */         case 2:
/* 283 */           llamaReporteCartaNumMatricula(3, "TERCERA MATRÍCULA");
/*     */           break;
/*     */       } 
/*     */     } else {
/* 287 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una persona antes.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickNumMatricula() {
/* 292 */     this.posPrd = this.vtnMatri.getCmbPeriodos().getSelectedIndex();
/* 293 */     if (this.posPrd > 0) {
/* 294 */       int s = JOptionPane.showOptionDialog((Component)this.vtnMatri, "Reporte de  numero de matricula\nElija el numero de matricula", "Numero de matricula", 1, 1, null, new Object[] { "Primera Matricula", "Segunda Matricula", "Tercera Matricula", "Completo", "Cancelar" }, "Completo");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 302 */       switch (s) {
/*     */         case 0:
/* 304 */           llamarReporteNumMatricula(1);
/*     */           break;
/*     */         case 1:
/* 307 */           llamarReporteNumMatricula(2);
/*     */           break;
/*     */         case 2:
/* 310 */           llamarReporteNumMatricula(3);
/*     */           break;
/*     */         case 3:
/* 313 */           llamarReporteMatriculados(this.posPrd);
/*     */           break;
/*     */       } 
/*     */     } else {
/* 317 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar un periodo lectivo.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private String selecCurso() {
/* 322 */     ArrayList<String> cursos = this.MTBD.cursosMatriculado(((MatriculaMD)this.matriculas.get(this.posFila)).getAlumno().getId_Alumno(), ((MatriculaMD)this.matriculas
/* 323 */         .get(this.posFila)).getPeriodo().getID());
/* 324 */     Object np = JOptionPane.showInputDialog(null, "Cursos en los que se matriculo: ", "Matricula", 3, null, cursos
/*     */ 
/*     */         
/* 327 */         .toArray(), "Seleccione");
/*     */     
/* 329 */     if (np == null) {
/* 330 */       JOptionPane.showMessageDialog(null, "Debe seleccionar un curso");
/* 331 */       selecCurso();
/* 332 */       return null;
/*     */     } 
/* 334 */     return np.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llamaReporteMatricula() {
/*     */     try {
/* 343 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repImpresionMatricula.jasper"));
/* 344 */       Map<Object, Object> parametro = new HashMap<>();
/* 345 */       parametro.put("cedula", ((MatriculaMD)this.matriculas.get(this.posFila)).getAlumno().getIdentificacion());
/* 346 */       parametro.put("idPeriodo", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getPeriodo().getID()));
/* 347 */       parametro.put("usuario", CONS.USUARIO.getUsername());
/* 348 */       CON.mostrarReporte(jr, parametro, "Reporte de Matricula");
/* 349 */     } catch (JRException ex) {
/* 350 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llamaReporteMatriculaSinFoto() {
/*     */     try {
/* 359 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repMatriculaSinFoto.jasper"));
/* 360 */       Map<Object, Object> parametro = new HashMap<>();
/* 361 */       parametro.put("cedula", ((MatriculaMD)this.matriculas.get(this.posFila)).getAlumno().getIdentificacion());
/* 362 */       parametro.put("idPeriodo", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getPeriodo().getID()));
/* 363 */       parametro.put("usuario", CONS.USUARIO.getUsername());
/* 364 */       CON.mostrarReporte(jr, parametro, "Reporte de Matricula | Sin foto");
/* 365 */     } catch (JRException ex) {
/* 366 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llamaReporteMatriculaPeriodo() {
/* 375 */     int posCombo = this.vtnMatri.getCmbPeriodos().getSelectedIndex();
/* 376 */     if (posCombo > 0) {
/*     */       try {
/* 378 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repMatriculadosPeriodo.jasper"));
/* 379 */         Map<Object, Object> parametro = new HashMap<>();
/* 380 */         parametro.put("periodo", Integer.valueOf(((PeriodoLectivoMD)this.periodos.get(posCombo - 1)).getID()));
/* 381 */         parametro.put("periodo_titulo", ((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getNombre());
/* 382 */         CON.mostrarReporte(jr, parametro, "Reporte Historial de Matrícula por Periodo");
/* 383 */       } catch (JRException ex) {
/* 384 */         JOptionPane.showMessageDialog(null, "Error: " + ex);
/*     */       } 
/*     */     } else {
/* 387 */       JOptionPane.showMessageDialog(null, "Seleccione un periodo lectivo, del combo.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llamaReporteActa() {
/* 395 */     String curso = selecCurso();
/* 396 */     if (curso != null) {
/*     */       try {
/* 398 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/cartaCompromiso.jasper"));
/* 399 */         Map<Object, Object> parametro = new HashMap<>();
/* 400 */         parametro.put("idAlumno", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getAlumno().getId_Alumno()));
/* 401 */         parametro.put("idPeriodo", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getPeriodo().getID()));
/* 402 */         parametro.put("curso", curso);
/* 403 */         CON.mostrarReporte(jr, parametro, "Reporte de Matricula");
/* 404 */       } catch (JRException ex) {
/* 405 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } else {
/* 408 */       JOptionPane.showMessageDialog(null, "No selecciono un curso.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llamaReporteCartaNumMatricula(int numMatricula, String matricula) {
/* 413 */     String curso = selecCurso();
/* 414 */     if (curso != null) {
/*     */       try {
/* 416 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/cartaNumMatricula.jasper"));
/* 417 */         Map<Object, Object> parametro = new HashMap<>();
/* 418 */         parametro.put("idAlumno", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getAlumno().getId_Alumno()));
/* 419 */         parametro.put("idPeriodo", Integer.valueOf(((MatriculaMD)this.matriculas.get(this.posFila)).getPeriodo().getID()));
/* 420 */         parametro.put("curso", curso);
/* 421 */         parametro.put("numMatricula", Integer.valueOf(numMatricula));
/* 422 */         parametro.put("matricula", matricula);
/* 423 */         System.out.println("Parametros: " + parametro);
/* 424 */         CON.mostrarReporte(jr, parametro, "Reporte de Matricula");
/* 425 */       } catch (JRException ex) {
/* 426 */         JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
/*     */       } 
/*     */     } else {
/* 429 */       JOptionPane.showMessageDialog(null, "No selecciono un curso.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 434 */     this.vtnMatri.getBtnIngresar().getAccessibleContext().setAccessibleName("Matricula-Ingresar");
/* 435 */     this.vtnMatri.getBtnAnular().getAccessibleContext().setAccessibleName("Matricula-Anular");
/* 436 */     this.vtnMatri.getBtnEditar().getAccessibleContext().setAccessibleName("Matricula-Editar");
/* 437 */     this.vtnMatri.getBtnCartaCompromiso().getAccessibleContext().setAccessibleName("Matricula-Reporte-Carta");
/* 438 */     this.vtnMatri.getBtnHistoria().getAccessibleContext().setAccessibleName("Matricula-Reporte-Historial");
/* 439 */     this.vtnMatri.getBtnImprimirFicha().getAccessibleContext().setAccessibleName("Matricula-Imprimir");
/*     */     
/* 441 */     CONS.activarBtns(new JComponent[] { this.vtnMatri.getBtnIngresar(), this.vtnMatri.getBtnAnular(), this.vtnMatri
/* 442 */           .getBtnEditar(), this.vtnMatri.getBtnCartaCompromiso(), this.vtnMatri
/* 443 */           .getBtnHistoria(), this.vtnMatri.getBtnImprimirFicha() });
/*     */   }
/*     */   
/*     */   private void llamarReporteNumMatricula(int numMatricula) {
/* 447 */     this.posPrd = this.vtnMatri.getCmbPeriodos().getSelectedIndex();
/* 448 */     if (this.posPrd > 0) {
/*     */       try {
/* 450 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repNumMatriculaPeriodo.jasper"));
/* 451 */         Map<Object, Object> parametro = new HashMap<>();
/* 452 */         parametro.put("matricula", Integer.valueOf(numMatricula));
/* 453 */         parametro.put("periodo", Integer.valueOf(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID()));
/* 454 */         System.out.println("Parametros: " + parametro);
/* 455 */         CON.mostrarReporte(jr, parametro, "Reporte de Matricula");
/* 456 */       } catch (JRException ex) {
/* 457 */         JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
/*     */       } 
/*     */     } else {
/* 460 */       JOptionPane.showMessageDialog(null, "Debe seleccionar un periodo lectivo.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llamarReporteMatriculados(int posPrd) {
/*     */     try {
/* 466 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repMatriculasPeriodo.jasper"));
/* 467 */       Map<Object, Object> parametro = new HashMap<>();
/* 468 */       parametro.put("periodo", Integer.valueOf(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()));
/* 469 */       parametro.put("periodo_titulo", ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getNombre());
/* 470 */       System.out.println("Parametros: " + parametro.toString());
/* 471 */       CON.mostrarReporte(jr, parametro, "Reporte de Matricula");
/* 472 */     } catch (JRException ex) {
/* 473 */       JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnMatriculaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */