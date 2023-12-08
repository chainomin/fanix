/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.Libraries.Effects;
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.AlumnoCursoBD;
/*     */ import modelo.alumno.AlumnoCursoMD;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.alumno.VtnAlumnoCurso;
/*     */ 
/*     */ 
/*     */ public class VtnAlumnoCursoCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final VtnAlumnoCurso vtnAlmnCurso;
/*     */   private int posPrd;
/*     */   private int posCur;
/*     */   private int posCiclo;
/*     */   private ArrayList<AlumnoCursoMD> almns;
/*  42 */   private final AlumnoCursoBD ALCBR = AlumnoCursoBD.single();
/*     */   
/*  44 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*  47 */   private final CursoBD CBD = CursoBD.single();
/*     */   
/*     */   private ArrayList<String> cursos;
/*     */   private ArrayList<Integer> ciclos;
/*  51 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnAlumnoCursoCTR(VtnAlumnoCurso vtnAlmnCurso, VtnPrincipalCTR ctrPrin) {
/*  60 */     super(ctrPrin);
/*  61 */     this.vtnAlmnCurso = vtnAlmnCurso;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  68 */     String[] titulo = { "Cédula", "Alumno", "Curso" };
/*  69 */     String[][] datos = new String[0][];
/*  70 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  71 */     TblEstilo.formatoTbl(this.vtnAlmnCurso.getTblAlumnoCurso());
/*  72 */     this.vtnAlmnCurso.getTblAlumnoCurso().setModel(this.mdTbl);
/*     */     
/*  74 */     cargarAlumnosCurso();
/*     */     
/*  76 */     cargarCmbPrds();
/*     */     
/*  78 */     this.vtnAlmnCurso.getTxtbuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  81 */             String b = VtnAlumnoCursoCTR.this.vtnAlmnCurso.getTxtbuscar().getText().trim();
/*  82 */             if (e.getKeyCode() == 10) {
/*  83 */               VtnAlumnoCursoCTR.this.buscar(b);
/*  84 */             } else if (b.length() == 0) {
/*  85 */               VtnAlumnoCursoCTR.this.cargarAlumnosCurso();
/*     */             } 
/*     */           }
/*     */         });
/*  89 */     this.vtnAlmnCurso.getBtnbuscar().addActionListener(e -> buscar(this.vtnAlmnCurso.getTxtbuscar().getText().trim()));
/*     */     
/*  91 */     this.vtnAlmnCurso.getTxtbuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnAlmnCurso.getTxtbuscar(), this.vtnAlmnCurso
/*  92 */           .getBtnbuscar()));
/*     */     
/*  94 */     this.vtnAlmnCurso.getCmbPrdLectivos().addActionListener(e -> clickCmbPrd());
/*  95 */     this.vtnAlmnCurso.getCmbCursos().addActionListener(e -> clickCmbCurso());
/*  96 */     this.vtnAlmnCurso.getCmbCiclo().addActionListener(e -> clickCmbCiclo());
/*     */     
/*  98 */     this.vtnAlmnCurso.getBtnMaterias().addActionListener(e -> materiasCurso());
/*     */     
/* 100 */     this.vtnAlmnCurso.getBtnRepAlum().addActionListener(e -> validaComboReporte());
/* 101 */     this.vtnAlmnCurso.getBtnListaCiclo().addActionListener(e -> validaComboReporteCiclo());
/* 102 */     this.vtnAlmnCurso.getBtnListaPeriodo().addActionListener(e -> ListaAlumnosPeriodo());
/* 103 */     this.vtnAlmnCurso.getBtnRepUBE().addActionListener(e -> btnUBE(e));
/*     */     
/* 105 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnAlmnCurso);
/* 106 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String b) {
/* 116 */     if (Validar.esLetrasYNumeros(b)) {
/* 117 */       this.almns = this.ALCBR.buscarAlumnosCursosTbl(b);
/* 118 */       llenatTbl(this.almns);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void abrirFrmCurso() {
/* 126 */     this.ctrPrin.abrirFrmMatricula();
/* 127 */     this.vtnAlmnCurso.dispose();
/* 128 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarAlumnosCurso() {
/* 135 */     this.almns = this.ALCBR.cargarAlumnosCursosTbl();
/* 136 */     llenatTbl(this.almns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarTblPorPrd() {
/* 143 */     if (this.posPrd > 0) {
/* 144 */       this.almns = this.ALCBR.cargarAlumnosCursosPorPrdTbl(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 145 */       llenatTbl(this.almns);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarTblPorCiclo() {
/* 153 */     if (this.posCiclo > 0) {
/* 154 */       this.almns = this.ALCBR.cargarAlumnosCursosPorCicloTbl(((Integer)this.ciclos.get(this.posCiclo - 1)).intValue(), ((PeriodoLectivoMD)this.periodos
/* 155 */           .get(this.posPrd - 1)).getID());
/* 156 */       llenatTbl(this.almns);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarTblPorCurso() {
/* 164 */     if (this.posCur > 0) {
/* 165 */       this.almns = this.ALCBR.cargarAlumnosCursosPorCursoTbl(this.cursos.get(this.posCur - 1), ((PeriodoLectivoMD)this.periodos
/* 166 */           .get(this.posPrd - 1)).getID());
/* 167 */       llenatTbl(this.almns);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenatTbl(ArrayList<AlumnoCursoMD> almns) {
/* 177 */     this.mdTbl.setRowCount(0);
/* 178 */     if (almns != null) {
/* 179 */       almns.forEach(a -> {
/*     */             Object[] valores = { a.getAlumno().getIdentificacion(), a.getAlumno().getPrimerNombre() + " " + a.getAlumno().getPrimerApellido(), a.getCurso().getNombre() };
/*     */ 
/*     */             
/*     */             this.mdTbl.addRow(valores);
/*     */           });
/*     */       
/* 186 */       this.vtnAlmnCurso.getLblResultados().setText(almns.size() + " Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbPrds() {
/* 194 */     this.periodos = this.PLBD.cargarPrdParaCmbVtn();
/* 195 */     this.vtnAlmnCurso.getCmbPrdLectivos().removeAllItems();
/* 196 */     if (!this.periodos.isEmpty()) {
/* 197 */       this.vtnAlmnCurso.getCmbPrdLectivos().addItem("Todos");
/* 198 */       this.periodos.forEach(p -> this.vtnAlmnCurso.getCmbPrdLectivos().addItem(p.getNombre()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbCiclo() {
/* 208 */     this.ciclos = this.MTBD.cargarCiclosCarrera(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getCarrera().getId());
/* 209 */     if (this.ciclos != null) {
/* 210 */       this.vtnAlmnCurso.getCmbCiclo().removeAllItems();
/* 211 */       this.vtnAlmnCurso.getCmbCiclo().addItem("Todos");
/* 212 */       this.ciclos.forEach(c -> this.vtnAlmnCurso.getCmbCiclo().addItem(c + ""));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbCiclo() {
/* 222 */     this.posCiclo = this.vtnAlmnCurso.getCmbCiclo().getSelectedIndex();
/* 223 */     cargarTblPorCiclo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbPrd() {
/* 231 */     this.posPrd = this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedIndex();
/* 232 */     cargarCmbCiclo();
/* 233 */     cargarCursoPorPrd();
/* 234 */     cargarTblPorPrd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbCurso() {
/* 241 */     this.posCur = this.vtnAlmnCurso.getCmbCursos().getSelectedIndex();
/* 242 */     cargarTblPorCurso();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCursoPorPrd() {
/* 250 */     if (this.posPrd > 0) {
/* 251 */       this.cursos = this.CBD.cargarNombreCursosPorPeriodo(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 252 */       this.vtnAlmnCurso.getCmbCursos().removeAllItems();
/* 253 */       if (this.cursos != null) {
/* 254 */         this.vtnAlmnCurso.getCmbCursos().addItem("Todos");
/* 255 */         this.cursos.forEach(c -> this.vtnAlmnCurso.getCmbCursos().addItem(c));
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 260 */       this.vtnAlmnCurso.getCmbCursos().removeAllItems();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void materiasCurso() {
/* 269 */     this.posFila = this.vtnAlmnCurso.getTblAlumnoCurso().getSelectedRow();
/* 270 */     if (this.posFila >= 0) {
/* 271 */       JDMateriasCursoCTR ctrM = new JDMateriasCursoCTR(this.almns.get(this.posFila), this.ctrPrin);
/* 272 */       ctrM.iniciar();
/*     */     } else {
/* 274 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar un alumno primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reporteAlumno() {
/* 283 */     String path = "/vista/reportes/repAlumTodoCurso.jasper";
/*     */     try {
/* 285 */       Map<Object, Object> parametro = new HashMap<>();
/* 286 */       parametro.put("periodo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem());
/* 287 */       parametro.put("curso", this.vtnAlmnCurso.getCmbCursos().getSelectedItem());
/* 288 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 289 */       CON.mostrarReporte(jr, parametro, "Reporte de Malla de Alumno");
/* 290 */     } catch (JRException ex) {
/* 291 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validaComboReporte() {
/* 299 */     int pos1 = this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedIndex();
/* 300 */     int pos2 = this.vtnAlmnCurso.getCmbCursos().getSelectedIndex();
/* 301 */     if (pos1 <= 0 || pos2 <= 0) {
/* 302 */       JOptionPane.showMessageDialog(null, "Seleccione un periodo y curso");
/*     */     } else {
/* 304 */       reporteAlumno();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reporte() {
/* 313 */     String path = "/vista/reportes/repListaAlumCiclo.jasper";
/*     */     try {
/* 315 */       Map<Object, Object> parametro = new HashMap<>();
/* 316 */       parametro.put("periodo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem());
/* 317 */       parametro.put("ciclo", Integer.valueOf(Integer.parseInt(this.vtnAlmnCurso
/* 318 */               .getCmbCiclo().getSelectedItem().toString())));
/*     */       
/* 320 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 321 */       CON.mostrarReporte(jr, parametro, "Reporte Lista de Alumnos");
/* 322 */     } catch (JRException ex) {
/* 323 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void ListaAlumnosPeriodo() {
/* 329 */     String path = "/vista/reportes/repListaAlumPeriodo.jasper";
/* 330 */     int posCMB = this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedIndex();
/* 331 */     if (posCMB <= 0) {
/* 332 */       JOptionPane.showMessageDialog(null, "Seleccione un periodo");
/*     */     } else {
/*     */       try {
/* 335 */         Map<Object, Object> parametro = new HashMap<>();
/* 336 */         parametro.put("periodo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem());
/* 337 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 338 */         CON.mostrarReporte(jr, parametro, "Reporte Lista de Alumnos");
/* 339 */       } catch (JRException ex) {
/* 340 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validaComboReporteCiclo() {
/* 349 */     int pos1 = this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedIndex();
/* 350 */     int pos2 = this.vtnAlmnCurso.getCmbCiclo().getSelectedIndex();
/* 351 */     if (pos1 <= 0 || pos2 <= 0) {
/* 352 */       JOptionPane.showMessageDialog(null, "Seleccione un periodo y un ciclo");
/*     */     } else {
/* 354 */       reporte();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 359 */     this.vtnAlmnCurso.getBtnMaterias().getAccessibleContext().setAccessibleName("Lista-Alumnos-Materias");
/* 360 */     this.vtnAlmnCurso.getBtnRepUBE().getAccessibleContext().setAccessibleName("Lista-Alumnos-Reporte-UBE");
/* 361 */     this.vtnAlmnCurso.getBtnListaCiclo().getAccessibleContext().setAccessibleName("Lista-Alumnos-Reporte-Lista Ciclo");
/* 362 */     this.vtnAlmnCurso.getBtnRepAlum().getAccessibleContext().setAccessibleName("Lista-Alumnos-Reporte-Lista Curso");
/*     */     
/* 364 */     CONS.activarBtns(new JComponent[] { this.vtnAlmnCurso.getBtnMaterias(), this.vtnAlmnCurso.getBtnRepUBE(), this.vtnAlmnCurso
/* 365 */           .getBtnListaCiclo(), this.vtnAlmnCurso.getBtnRepAlum() });
/*     */   }
/*     */   
/*     */   private void btnUBE(ActionEvent e) {
/* 369 */     (new Thread(() -> {
/*     */           int r = JOptionPane.showOptionDialog((Component)this.vtnAlmnCurso, "Reporte individual\n¿Elegir el tipo de Reporte?", "REPORTE UBE", 1, 1, null, new Object[] { "menos de 70 hasta interciclo", "menos de 70 final de ciclo", "Nota final" }, "Cancelar");
/*     */           Effects.setLoadCursor((Container)this.vtnAlmnCurso);
/*     */           switch (r) {
/*     */             case 0:
/*     */               llamaReporteUBEmenos70hastainterciclo();
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 1:
/*     */               llamaReporteUBEmenos70despuesinterciclo();
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 2:
/*     */               llamaReporteUBEcompleto();
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*     */             Thread.sleep(500L);
/* 403 */           } catch (InterruptedException ex) {
/*     */             System.out.println(ex.getMessage());
/*     */           } 
/*     */           
/*     */           Effects.setDefaultCursor((Container)this.vtnAlmnCurso);
/* 408 */         })).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void llamaReporteUBEmenos70hastainterciclo() {
/* 413 */     String path = "/vista/reportes/repUBNotasAporte1.jasper";
/* 414 */     this.posFila = this.vtnAlmnCurso.getTblAlumnoCurso().getSelectedRow();
/* 415 */     if (this.posFila >= 0) {
/*     */       
/*     */       try {
/* 418 */         Map<Object, Object> parametro = new HashMap<>();
/* 419 */         parametro.put("cedulaalumno", ((AlumnoCursoMD)this.almns.get(this.posFila)).getAlumno().getIdentificacion());
/* 420 */         parametro.put("periodolectivo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem().toString());
/* 421 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 422 */         CON.mostrarReporte(jr, parametro, "Reporte UBE");
/*     */       }
/* 424 */       catch (JRException ex) {
/* 425 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } else {
/* 428 */       JOptionPane.showMessageDialog((Component)this.vtnAlmnCurso, "Seleecione una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void llamaReporteUBEmenos70despuesinterciclo() {
/* 435 */     String path = "/vista/reportes/repUBNotasAporte2.jasper";
/* 436 */     this.posFila = this.vtnAlmnCurso.getTblAlumnoCurso().getSelectedRow();
/* 437 */     if (this.posFila >= 0) {
/*     */       
/*     */       try {
/* 440 */         Map<Object, Object> parametro = new HashMap<>();
/* 441 */         parametro.put("cedulaalumno", ((AlumnoCursoMD)this.almns.get(this.posFila)).getAlumno().getIdentificacion());
/* 442 */         parametro.put("periodolectivo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem().toString());
/* 443 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 444 */         CON.mostrarReporte(jr, parametro, "Reporte UBE");
/*     */       }
/* 446 */       catch (JRException ex) {
/* 447 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } else {
/* 450 */       JOptionPane.showMessageDialog((Component)this.vtnAlmnCurso, "Seleecione una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void llamaReporteUBEcompleto() {
/* 457 */     String path = "/vista/reportes/repUBcompleto.jasper";
/* 458 */     this.posFila = this.vtnAlmnCurso.getTblAlumnoCurso().getSelectedRow();
/* 459 */     if (this.posFila >= 0) {
/*     */       
/*     */       try {
/* 462 */         Map<Object, Object> parametro = new HashMap<>();
/* 463 */         parametro.put("cedulaalumno", ((AlumnoCursoMD)this.almns.get(this.posFila)).getAlumno().getIdentificacion());
/* 464 */         parametro.put("periodolectivo", this.vtnAlmnCurso.getCmbPrdLectivos().getSelectedItem().toString());
/* 465 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 466 */         CON.mostrarReporte(jr, parametro, "Reporte UBE");
/*     */       }
/* 468 */       catch (JRException ex) {
/* 469 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } else {
/* 472 */       JOptionPane.showMessageDialog((Component)this.vtnAlmnCurso, "Seleecione una fila primero.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */