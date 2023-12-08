/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.AlumnoCursoRetiradoBD;
/*     */ import modelo.alumno.AlumnoCursoRetiradoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.alumno.VtnMatriculasAnuladas;
/*     */ 
/*     */ public class VtnMatriculasAnuladasCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final VtnMatriculasAnuladas vtnAR;
/*  31 */   private final AlumnoCursoRetiradoBD ACRBD = AlumnoCursoRetiradoBD.single();
/*     */   private ArrayList<AlumnoCursoRetiradoMD> almsCursosRetirados;
/*  33 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */ 
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*     */ 
/*     */   
/*     */   private int posPrd;
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnMatriculasAnuladasCTR(VtnPrincipalCTR ctrPrin, VtnMatriculasAnuladas vtnAR) {
/*  44 */     super(ctrPrin);
/*  45 */     this.vtnAR = vtnAR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  52 */     iniciarDependencias();
/*  53 */     iniciarTblACR();
/*  54 */     iniciarBuscador();
/*  55 */     iniciarAcciones();
/*  56 */     formatoTbl();
/*  57 */     cargarAnulados();
/*  58 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnAR);
/*  59 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickEliminar() {
/*  67 */     this.posFila = this.vtnAR.getTblAlumnosClase().getSelectedRow();
/*  68 */     if (this.posFila >= 0) {
/*  69 */       int r = JOptionPane.showConfirmDialog((Component)this.vtnAR, "Se eliminara la anulacion de \n" + ((AlumnoCursoRetiradoMD)this.almsCursosRetirados
/*  70 */           .get(this.posFila)).getAlumnoCurso().getCurso().getMateria().getNombre() + "\n¿Seguro que quiere continuar?");
/*     */       
/*  72 */       if (r == 0 && 
/*  73 */         this.ACRBD.eliminar(((AlumnoCursoRetiradoMD)this.almsCursosRetirados.get(this.posFila)).getId())) {
/*  74 */         cargarAnulados();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarDependencias() {
/*  85 */     this.periodos = this.PLBD.cargarPrdParaCmbFrm();
/*  86 */     llenarCmbPrd(this.periodos);
/*  87 */     this.vtnAR.getLblResultados().setText("0 Resultados obtenidos.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatoTbl() {
/*  94 */     TblEstilo.formatoTbl(this.vtnAR.getTblAlumnosClase());
/*  95 */     this.vtnAR.getTblAlumnosClase().setModel(this.mdTbl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarAcciones() {
/* 102 */     this.vtnAR.getCmbPrdLectivos().addActionListener(e -> clickPrd());
/* 103 */     this.vtnAR.getCbxEliminados().addActionListener(e -> clickCbxEliminados());
/* 104 */     this.vtnAR.getBtnEliminar().addActionListener(e -> clickEliminar());
/* 105 */     this.vtnAR.getBtnBuscar().addActionListener(e -> buscar());
/*     */     
/* 107 */     this.vtnAR.getBtnImprimir().addActionListener(l -> llamaReporteAnulaciones());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCbxEliminados() {
/* 115 */     this.vtnAR.getTxtBuscar().setText("");
/* 116 */     if (this.vtnAR.getCbxEliminados().isSelected()) {
/* 117 */       this.vtnAR.getBtnEliminar().setEnabled(false);
/* 118 */       cargarEliminados();
/*     */     } else {
/* 120 */       this.vtnAR.getBtnEliminar().setEnabled(true);
/* 121 */       cargarAnulados();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarEliminados() {
/* 130 */     this.almsCursosRetirados = this.ACRBD.cargarRetiradosEliminados();
/* 131 */     llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarAnulados() {
/* 138 */     this.almsCursosRetirados = this.ACRBD.cargarRetirados();
/* 139 */     llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarBuscador() {
/* 146 */     this.vtnAR.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 149 */             if (e.getKeyCode() == 10) {
/* 150 */               VtnMatriculasAnuladasCTR.this.buscar();
/* 151 */             } else if (VtnMatriculasAnuladasCTR.this.vtnAR.getTxtBuscar().getText().length() == 0) {
/* 152 */               VtnMatriculasAnuladasCTR.this.mdTbl.setRowCount(0);
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
/*     */   
/*     */   private void buscar() {
/* 165 */     String b = this.vtnAR.getTxtBuscar().getText().trim();
/* 166 */     if (Validar.esLetrasYNumeros(b)) {
/* 167 */       if (this.vtnAR.getCbxEliminados().isSelected()) {
/* 168 */         buscarACRE(b);
/*     */       } else {
/* 170 */         buscarACR(b);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscarACR(String b) {
/* 181 */     if (Validar.esLetrasYNumeros(b)) {
/* 182 */       this.almsCursosRetirados = this.ACRBD.buscarRetirados(b);
/* 183 */       llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscarACRE(String b) {
/* 191 */     if (Validar.esLetrasYNumeros(b)) {
/* 192 */       this.almsCursosRetirados = this.ACRBD.buscarRetiradosEliminados(b);
/* 193 */       llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickPrd() {
/* 201 */     this.posPrd = this.vtnAR.getCmbPrdLectivos().getSelectedIndex();
/* 202 */     if (this.posPrd > 0) {
/* 203 */       if (this.vtnAR.getCbxEliminados().isSelected()) {
/* 204 */         this.almsCursosRetirados = this.ACRBD.cargarRetiradosPorPrdEliminados(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 205 */         llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */       } else {
/* 207 */         this.almsCursosRetirados = this.ACRBD.cargarRetiradosPorPrd(((PeriodoLectivoMD)this.periodos.get(this.posPrd - 1)).getID());
/* 208 */         llenarTblAlmRetirado(this.almsCursosRetirados);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarTblACR() {
/* 217 */     String[] t = { "Periodo", "Alumno", "Materia", "Fecha", "Observacion" };
/* 218 */     String[][] d = new String[0][];
/* 219 */     this.mdTbl = TblEstilo.modelTblSinEditar(d, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbACR() {
/* 226 */     this.periodos = this.PLBD.cargarPrdParaCmbVtn();
/* 227 */     llenarCmbPrd(this.periodos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTblAlmRetirado(ArrayList<AlumnoCursoRetiradoMD> almns) {
/* 236 */     this.vtnAR.getTblAlumnosClase().setModel(this.mdTbl);
/* 237 */     this.mdTbl.setRowCount(0);
/* 238 */     if (almns != null) {
/* 239 */       almns.forEach(a -> {
/*     */             Object[] v = { a.getAlumnoCurso().getCurso().getPeriodo().getNombre(), a.getAlumnoCurso().getAlumno().getNombreCorto(), a.getAlumnoCurso().getCurso().getMateria().getNombre(), a.getFecha(), a.getObservacion() };
/*     */ 
/*     */             
/*     */             this.mdTbl.addRow(v);
/*     */           });
/*     */       
/* 246 */       this.vtnAR.getLblResultados().setText(almns.size() + " Resultados obtendios.");
/*     */     } else {
/* 248 */       this.mdTbl.setRowCount(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbPrd(ArrayList<PeriodoLectivoMD> periodos) {
/* 258 */     this.vtnAR.getCmbPrdLectivos().removeAllItems();
/* 259 */     this.vtnAR.getCmbPrdLectivos().addItem("Seleccione");
/* 260 */     if (periodos != null) {
/* 261 */       periodos.forEach(p -> this.vtnAR.getCmbPrdLectivos().addItem(p.getNombre()));
/*     */ 
/*     */       
/* 264 */       this.vtnAR.getCmbPrdLectivos().setSelectedIndex(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 269 */     this.vtnAR.getBtnEliminar().getAccessibleContext().setAccessibleName("Matriculas-Anuladas-Eliminar");
/* 270 */     this.vtnAR.getCbxEliminados().getAccessibleContext().setAccessibleName("Matriculas-Anuladas-Ver Eliminados");
/*     */     
/* 272 */     CONS.activarBtns(new JComponent[] { this.vtnAR.getBtnEliminar(), this.vtnAR.getCbxEliminados() });
/*     */   }
/*     */   
/*     */   public void llamaReporteAnulaciones() {
/* 276 */     int filas = this.vtnAR.getTblAlumnosClase().getRowCount();
/* 277 */     if (filas > 0) {
/* 278 */       this.posFila = this.vtnAR.getCmbPrdLectivos().getSelectedIndex();
/* 279 */       if (this.posFila > 0) {
/*     */         try {
/* 281 */           JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repAlumnosAnuladosCarrera.jasper"));
/* 282 */           Map<Object, Object> parametro = new HashMap<>();
/* 283 */           parametro.put("PeriodoLectivo", Integer.valueOf(((PeriodoLectivoMD)this.periodos.get(this.posFila - 1)).getID()));
/* 284 */           CON.mostrarReporte(jr, parametro, "Reporte de Anulaciones");
/* 285 */         } catch (JRException ex) {
/* 286 */           JOptionPane.showMessageDialog(null, "error" + ex);
/*     */         } 
/*     */       } else {
/* 289 */         JOptionPane.showMessageDialog(null, "Selecione un periodo lectivo.");
/*     */       } 
/*     */     } else {
/* 292 */       JOptionPane.showMessageDialog(null, "No se encuentran materias anuladas en este período.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnMatriculasAnuladasCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */