/*     */ package controlador.carrera;
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
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.carrera.FrmCarrera;
/*     */ import vista.carrera.VtnCarrera;
/*     */ 
/*     */ public class VtnCarreraCTR extends DVtnCTR {
/*     */   private final VtnCarrera vtnCarrera;
/*  34 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */   
/*  36 */   private final CarreraBD CRBD = CarreraBD.single();
/*     */   private ArrayList<CarreraMD> carreras;
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*     */   
/*     */   public VtnCarreraCTR(VtnCarrera vtnCarrera, VtnPrincipalCTR ctrPrin) {
/*  41 */     super(ctrPrin);
/*  42 */     this.vtnCarrera = vtnCarrera;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  49 */     this.vtnCarrera.getBtnReporteAlumnoCarrera().setEnabled(false);
/*  50 */     String[] titutlo = { "id", "Codigo", "Nombre", "Fecha Inicio", "Modalidad", "Semanas", "Coordinador" };
/*  51 */     String[][] datos = new String[0][];
/*  52 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titutlo);
/*  53 */     this.vtnCarrera.getTblMaterias().setModel(this.mdTbl);
/*  54 */     TblEstilo.formatoTbl(this.vtnCarrera.getTblMaterias());
/*  55 */     TblEstilo.ocualtarID(this.vtnCarrera.getTblMaterias());
/*  56 */     TblEstilo.columnaMedida(this.vtnCarrera.getTblMaterias(), 1, 50);
/*  57 */     TblEstilo.columnaMedida(this.vtnCarrera.getTblMaterias(), 3, 90);
/*  58 */     TblEstilo.columnaMedida(this.vtnCarrera.getTblMaterias(), 4, 90);
/*  59 */     TblEstilo.columnaMedida(this.vtnCarrera.getTblMaterias(), 5, 80);
/*     */     
/*  61 */     cargarCarreras();
/*     */     
/*  63 */     this.vtnCarrera.getBtnIngresar().addActionListener(e -> abrirFrmCarrera());
/*  64 */     this.vtnCarrera.getBtnEditar().addActionListener(e -> editarCarrera());
/*  65 */     this.vtnCarrera.getBtnEliminar().addActionListener(e -> eliminarCarrera());
/*  66 */     this.vtnCarrera.getTblMaterias().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  69 */             VtnCarreraCTR.this.validarBotonesReportes();
/*     */           }
/*     */         });
/*  72 */     this.vtnCarrera.getBtnReporteAlumnoCarrera().addActionListener(e -> llamaReporteAlumnoCarrera());
/*  73 */     this.vtnCarrera.getBtnReporteDocente().addActionListener(e -> botonDocentes());
/*  74 */     this.vtnCarrera.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  77 */             String b = VtnCarreraCTR.this.vtnCarrera.getTxtBuscar().getText().trim();
/*  78 */             if (e.getKeyCode() == 10) {
/*  79 */               VtnCarreraCTR.this.buscar(b);
/*  80 */             } else if (b.length() == 0) {
/*  81 */               VtnCarreraCTR.this.cargarCarreras();
/*     */             } 
/*     */           }
/*     */         });
/*  85 */     this.vtnCarrera.getBtnBuscar().addActionListener(e -> buscar(this.vtnCarrera.getTxtBuscar().getText().trim()));
/*  86 */     this.vtnCarrera.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnCarrera.getTxtBuscar(), this.vtnCarrera
/*  87 */           .getBtnBuscar()));
/*     */     
/*  89 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnCarrera);
/*  90 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String b) {
/*  99 */     if (Validar.esLetrasYNumeros(b)) {
/* 100 */       this.carreras = this.CRBD.buscarCarrera(b);
/* 101 */       llenarTbl(this.carreras);
/*     */     } else {
/* 103 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "No debe ingresar caracteres especiales.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void editarCarrera() {
/* 112 */     int fila = this.vtnCarrera.getTblMaterias().getSelectedRow();
/* 113 */     if (fila >= 0) {
/* 114 */       FrmCarrera frmCarrera = new FrmCarrera();
/* 115 */       this.ctrPrin.eventoInternal((JInternalFrame)frmCarrera);
/* 116 */       FrmCarreraCTR ctrFrmCarrera = new FrmCarreraCTR(frmCarrera, this.ctrPrin);
/* 117 */       ctrFrmCarrera.iniciar();
/* 118 */       ctrFrmCarrera.editar(this.carreras.get(fila));
/* 119 */       this.ctrPrin.cerradoJIF();
/* 120 */       this.vtnCarrera.dispose();
/*     */     } else {
/* 122 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una carrera primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void eliminarCarrera() {
/* 130 */     int fila = this.vtnCarrera.getTblMaterias().getSelectedRow();
/* 131 */     if (fila >= 0) {
/* 132 */       int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Seguro que quiere eliminar \n" + this.vtnCarrera
/* 133 */           .getTblMaterias().getValueAt(fila, 2).toString() + "\nNo se podran recuperar los datos despues.");
/*     */       
/* 135 */       if (r == 0) {
/* 136 */         this.CRBD.eliminarCarrera(((CarreraMD)this.carreras.get(fila)).getId());
/* 137 */         cargarCarreras();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void abrirFrmCarrera() {
/* 146 */     this.ctrPrin.abrirFrmCarrera();
/* 147 */     this.vtnCarrera.dispose();
/* 148 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cargarCarreras() {
/* 155 */     this.carreras = this.CRBD.cargarCarreras();
/* 156 */     llenarTbl(this.carreras);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void llenarTbl(ArrayList<CarreraMD> carreras) {
/* 165 */     this.mdTbl.setRowCount(0);
/* 166 */     if (carreras != null) {
/* 167 */       carreras.forEach(c -> {
/*     */             if (c.getCoordinador().getPrimerNombre() == null) {
/*     */               Object[] valoresSD = { Integer.valueOf(c.getId()), c.getCodigo(), c.getNombre(), c.getFechaInicio(), c.getModalidad(), Integer.valueOf(c.getNumSemanas()), "SIN COORDINADOR " };
/*     */ 
/*     */               
/*     */               this.mdTbl.addRow(valoresSD);
/*     */             } else {
/*     */               Object[] valoresCD = { Integer.valueOf(c.getId()), c.getCodigo(), c.getNombre(), c.getFechaInicio(), c.getModalidad(), Integer.valueOf(c.getNumSemanas()), c.getCoordinador().getPrimerApellido() + " " + c.getCoordinador().getSegundoApellido() + " " + c.getCoordinador().getPrimerNombre() + " " + c.getCoordinador().getSegundoNombre() };
/*     */ 
/*     */               
/*     */               this.mdTbl.addRow(valoresCD);
/*     */             } 
/*     */           });
/*     */ 
/*     */       
/* 182 */       this.vtnCarrera.getLblResultados().setText(carreras.size() + " Resutados obtendidos.");
/*     */     } else {
/* 184 */       this.vtnCarrera.getLblResultados().setText("0 Resutados obtendidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void llamaReporteAlumnoCarrera() {
/* 193 */     this.posFila = this.vtnCarrera.getTblMaterias().getSelectedRow();
/*     */     
/* 195 */     String path = "/vista/reportes/repAlumnosCarrera.jasper";
/* 196 */     if (this.posFila >= 0) {
/*     */       try {
/* 198 */         Map<Object, Object> parametro = new HashMap<>();
/* 199 */         parametro.put("alumnoCarrera", Integer.valueOf(((CarreraMD)this.carreras.get(this.posFila)).getId()));
/* 200 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 201 */         CON.mostrarReporte(jr, parametro, "Reporte de alumnos por Carrera");
/* 202 */       } catch (JRException ex) {
/* 203 */         JOptionPane.showMessageDialog(null, "Error: " + ex);
/*     */       } 
/*     */     } else {
/* 206 */       JOptionPane.showMessageDialog((Component)this.vtnCarrera, "Seleccione una carrera.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void botonDocentes() {
/* 215 */     int s = JOptionPane.showOptionDialog((Component)this.vtnCarrera, "Reporte de Docentes por periodo LEctivo\n¿Elegir el tipo de Reporte?", "REPORTE DOCENTES", 1, 1, null, new Object[] { "Elegir Periodo", "Cancelar" }, "Cancelar");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     switch (s) {
/*     */       case 0:
/* 224 */         seleccionarPeriodo();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void seleccionarPeriodo() {
/* 235 */     this.periodos = this.PLBD.cargarPeriodos();
/* 236 */     ArrayList<String> nmPrd = new ArrayList<>();
/* 237 */     nmPrd.add("Seleccione");
/* 238 */     this.periodos.forEach(p -> nmPrd.add(p.getNombre()));
/*     */ 
/*     */     
/* 241 */     Object np = JOptionPane.showInputDialog((Component)this.ctrPrin.getVtnPrin(), "Lista de periodos lectivos", "Periodos lectivos", 3, null, nmPrd
/*     */ 
/*     */         
/* 244 */         .toArray(), "Seleccione");
/*     */     
/* 246 */     if (np == null) {
/* 247 */       botonDocentes();
/*     */     }
/* 249 */     else if (np.equals("Seleccione")) {
/*     */       
/* 251 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar un periodo lectivo.");
/* 252 */       seleccionarPeriodo();
/*     */     }
/*     */     else {
/*     */       
/* 256 */       String path = "/vista/reportes/repDocentesPrdLectivo.jasper";
/*     */       try {
/* 258 */         Map<Object, Object> parametro = new HashMap<>();
/*     */         
/* 260 */         parametro.put("idPeriodo", np);
/* 261 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 262 */         CON.mostrarReporte(jr, parametro, "Reporte de Materias del Docente por Periodos Lectivos");
/*     */       }
/* 264 */       catch (JRException ex) {
/* 265 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 271 */     this.vtnCarrera.getBtnEliminar().getAccessibleContext().setAccessibleName("Carreras-Eliminar");
/* 272 */     this.vtnCarrera.getBtnEditar().getAccessibleContext().setAccessibleName("Carreras-Editar");
/* 273 */     this.vtnCarrera.getBtnIngresar().getAccessibleContext().setAccessibleName("Carreras-Ingresar");
/* 274 */     this.vtnCarrera.getBtnReporteAlumnoCarrera().getAccessibleContext().setAccessibleName("Carreras-Reporte-Alumno");
/* 275 */     this.vtnCarrera.getBtnReporteDocente().getAccessibleContext().setAccessibleName("Carreras-Reporte-Docente");
/*     */     
/* 277 */     CONS.activarBtns(new JComponent[] { this.vtnCarrera.getBtnEliminar(), this.vtnCarrera.getBtnEditar(), this.vtnCarrera.getBtnIngresar(), this.vtnCarrera
/* 278 */           .getBtnReporteAlumnoCarrera(), this.vtnCarrera.getBtnReporteDocente() });
/*     */   }
/*     */   
/*     */   public void validarBotonesReportes() {
/* 282 */     int selecTabl = this.vtnCarrera.getTblMaterias().getSelectedRow();
/* 283 */     if (selecTabl >= 0) {
/* 284 */       this.vtnCarrera.getBtnReporteAlumnoCarrera().setEnabled(true);
/*     */     } else {
/* 286 */       this.vtnCarrera.getBtnReporteAlumnoCarrera().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\carrera\VtnCarreraCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */