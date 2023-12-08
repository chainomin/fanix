/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.Egresado;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import utils.ToExcel;
/*     */ 
/*     */ public class VtnAlumnoEgresadosCTR
/*     */   extends AVtnAlumnoEgresadoCTR implements IAlumnoEgresadoVTNCTR {
/*  18 */   private static final String[] TITULO = new String[] { "Carrera", "Periodo", "Cédula", "Alumno", "Fecha egreso", "Graduado" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnAlumnoEgresadosCTR(VtnPrincipalCTR ctrPrin) {
/*  25 */     super(ctrPrin);
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  29 */     iniciarVtn(TITULO, this);
/*  30 */     cargarDatos();
/*  31 */     iniciarBuscador();
/*  32 */     iniciarAcciones();
/*  33 */     iniciarClicks();
/*  34 */     this.vtn.setTitle("Alumnos Egresados");
/*  35 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtn);
/*  36 */     this.vtn.getBtnNotasAlumno().setVisible(true);
/*  37 */     this.vtn.getBtnNotasPeriodo().setVisible(true);
/*  38 */     this.vtnCargada = true;
/*     */   }
/*     */   
/*     */   private void iniciarClicks() {
/*  42 */     this.vtn.getBtnRepPeriodo().addActionListener(e -> clickReportePorPeriodo());
/*  43 */     this.vtn.getBtnNotasAlumno().addActionListener(e -> clickReporteNotasAlumno());
/*  44 */     this.vtn.getBtnNotasPeriodo().addActionListener(e -> clickReporteNotasPeriodo());
/*     */   }
/*     */   
/*     */   private void cargarDatos() {
/*  48 */     this.todosEgresados = this.EBD.getAllEgresados();
/*  49 */     this.egresados = this.todosEgresados;
/*  50 */     llenarTbl(this.egresados);
/*     */   }
/*     */   
/*     */   private void iniciarBuscador() {
/*  54 */     this.vtn.getBtnBuscar().addActionListener(e -> buscar(this.vtn.getTxtBuscar().getText().trim()));
/*     */ 
/*     */     
/*  57 */     this.vtn.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  60 */             VtnAlumnoEgresadosCTR.this.buscar(VtnAlumnoEgresadosCTR.this.vtn.getTxtBuscar().getText().trim());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void buscar(String aguja) {
/*  66 */     this.egresados = new ArrayList<>();
/*  67 */     this.todosEgresados.forEach(e -> {
/*     */           if (e.getAlmnCarrera().getAlumno().getNombreCompleto().toLowerCase().contains(aguja.toLowerCase())) {
/*     */             this.egresados.add(e);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  74 */     llenarTbl(this.egresados);
/*     */   }
/*     */ 
/*     */   
/*     */   public void llenarTbl(List<Egresado> egresados) {
/*  79 */     this.mdTbl.setRowCount(0);
/*  80 */     if (egresados != null) {
/*  81 */       egresados.forEach(r -> {
/*     */             Object[] valores = { r.getAlmnCarrera().getCarrera().getCodigo(), r.getPeriodo().getNombre(), r.getAlmnCarrera().getAlumno().getIdentificacion(), r.getAlmnCarrera().getAlumno().getPrimerApellido() + " " + r.getAlmnCarrera().getAlumno().getSegundoApellido() + " " + r.getAlmnCarrera().getAlumno().getPrimerNombre() + " " + r.getAlmnCarrera().getAlumno().getSegundoNombre(), r.getFechaEgreso().toString(), r.isGraduado() ? "Si" : "No" };
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
/*  95 */       this.vtn.getLblResultados().setText(egresados.size() + " Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickReportePorPeriodo() {
/* 100 */     int posPeriodo = this.vtn.getCmbPeriodo().getSelectedIndex();
/* 101 */     if (posPeriodo > 0) {
/*     */ 
/*     */       
/* 104 */       String nombre = this.vtn.getCmbPeriodo().getSelectedItem().toString().replace(" ", "").replace("/", "-") + "  Egresados";
/*     */       
/* 106 */       List<List<String>> alumnos = this.EBD.getReportesEgresadosExcel(((PeriodoLectivoMD)this.periodos
/* 107 */           .get(posPeriodo - 1)).getID() + "");
/*     */       
/* 109 */       List<String> cols = new ArrayList<>();
/* 110 */       cols.add("CÓDIGO DEL IST");
/* 111 */       cols.add("NOMBRE DEL INSTITUTO");
/* 112 */       cols.add("PROVINCIA");
/* 113 */       cols.add("CÓDIGO DE LA CARRERA");
/* 114 */       cols.add("CARRERA");
/* 115 */       cols.add("MODALIDAD DE ESTUDIOS");
/* 116 */       cols.add("TIPO DE IDENTIFICACIÓN");
/* 117 */       cols.add("NRO. DE IDENTIFICACIÓN");
/* 118 */       cols.add("APELLIDOS Y NOMBRES");
/* 119 */       cols.add("NACIONALIDAD");
/* 120 */       cols.add("TRABAJO DE TITULACIÓN FINALIZADO S/N");
/* 121 */       ToExcel excel = new ToExcel();
/* 122 */       excel.exportarExcel(cols, alumnos, nombre);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 128 */       JOptionPane.showMessageDialog((Component)this.vtn, "No selecciono un periodo lectivo para el reporte.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickReporteNotasPeriodo() {
/* 133 */     int posPeriodo = this.vtn.getCmbPeriodo().getSelectedIndex();
/* 134 */     if (posPeriodo > 0) {
/* 135 */       int s = JOptionPane.showOptionDialog((Component)this.vtn, "Reporte de notas por periodo\n¿Elegir el tipo de carrera?", "Notas Finales", 1, 1, null, new Object[] { "Tradicional", "Dual", "Cancelar" }, "Tradicional");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       switch (s) {
/*     */         case 0:
/* 145 */           reporteNotasPeriodo(posPeriodo);
/*     */           break;
/*     */         case 1:
/* 148 */           reporteNotasPeriodoDual(posPeriodo);
/*     */           break;
/*     */       } 
/*     */     } else {
/* 152 */       JOptionPane.showMessageDialog((Component)this.vtn, "No selecciono un periodo lectivo para el reporte.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickReporteNotasAlumno() {
/* 157 */     int posEgresado = this.vtn.getTblEgresados().getSelectedRow();
/* 158 */     if (posEgresado >= 0) {
/* 159 */       int s = JOptionPane.showOptionDialog((Component)this.vtn, "Reporte de notas por alumno\n¿Elegir el tipo de carrera?", "Notas Finales", 1, 1, null, new Object[] { "Tradicional", "Dual", "Cancelar" }, "Tradicional");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       switch (s) {
/*     */         case 0:
/* 169 */           reporteNotasAlumno(posEgresado);
/*     */           break;
/*     */         case 1:
/* 172 */           reporteNotasAlumnoDual(posEgresado);
/*     */           break;
/*     */       } 
/*     */     } else {
/* 176 */       JOptionPane.showMessageDialog((Component)this.vtn, "No selecciono un alumno.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reporteNotasPeriodoDual(int posPeriodo) {
/* 186 */     String nombre = this.vtn.getCmbPeriodo().getSelectedItem().toString().replace(" ", "").replace("/", "-") + " Notas Egresados" + ((PeriodoLectivoMD)this.periodos.get(posPeriodo - 1)).getID();
/*     */     
/* 188 */     List<List<String>> alumnos = this.EBD.getNotasPromedioPorPeriodoDual(((PeriodoLectivoMD)this.periodos
/* 189 */         .get(posPeriodo - 1)).getID());
/*     */     
/* 191 */     List<String> cols = new ArrayList<>();
/* 192 */     cols.add("IDENTIFICACIÓN");
/* 193 */     cols.add("PRIMER NOMBRE");
/* 194 */     cols.add("SEGUNDO NOMBRE");
/* 195 */     cols.add("PRIMER APELLIDO");
/* 196 */     cols.add("SEGUNDO APELLIDO");
/*     */     
/* 198 */     cols.add("FASE TEORICA");
/* 199 */     cols.add("PTI");
/* 200 */     cols.add("FASE PRACTICA");
/* 201 */     ToExcel excel = new ToExcel();
/* 202 */     excel.exportarExcel(cols, alumnos, nombre);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reporteNotasPeriodo(int posPeriodo) {
/* 215 */     String nombre = this.vtn.getCmbPeriodo().getSelectedItem().toString().replace(" ", "").replace("/", "-") + " Notas Egresados" + ((PeriodoLectivoMD)this.periodos.get(posPeriodo - 1)).getID();
/*     */     
/* 217 */     List<List<String>> alumnos = this.EBD.getNotasPromedioPorPeriodo(((PeriodoLectivoMD)this.periodos
/* 218 */         .get(posPeriodo - 1)).getID());
/*     */     
/* 220 */     List<String> cols = new ArrayList<>();
/* 221 */     cols.add("IDENTIFICACIÓN");
/* 222 */     cols.add("PRIMER NOMBRE");
/* 223 */     cols.add("SEGUNDO NOMBRE");
/* 224 */     cols.add("PRIMER APELLIDO");
/* 225 */     cols.add("SEGUNDO APELLIDO");
/*     */     
/* 227 */     cols.add("PROMEDIO FINAL");
/* 228 */     ToExcel excel = new ToExcel();
/* 229 */     excel.exportarExcel(cols, alumnos, nombre);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reporteNotasAlumnoDual(int posEgresado) {
/* 239 */     String nombre = this.vtn.getCmbPeriodo().getSelectedItem().toString().replace(" ", "").replace("/", "-") + " Notas Alumno Egresados" + ((Egresado)this.egresados.get(posEgresado)).getId();
/*     */     
/* 241 */     List<List<String>> alumnos = this.EBD.getNotasPromedioPorEstudianteDual(((Egresado)this.egresados
/* 242 */         .get(posEgresado)).getId());
/*     */     
/* 244 */     List<String> cols = new ArrayList<>();
/* 245 */     cols.add("IDENTIFICACIÓN");
/* 246 */     cols.add("PRIMER NOMBRE");
/* 247 */     cols.add("SEGUNDO NOMBRE");
/* 248 */     cols.add("PRIMER APELLIDO");
/* 249 */     cols.add("SEGUNDO APELLIDO");
/*     */     
/* 251 */     cols.add("CICLO");
/* 252 */     cols.add("# MATERIAS");
/* 253 */     cols.add("FASE TEORICA");
/* 254 */     cols.add("FASE PRACTICA");
/* 255 */     cols.add("PTI");
/* 256 */     ToExcel excel = new ToExcel();
/* 257 */     excel.exportarExcel(cols, alumnos, nombre);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reporteNotasAlumno(int posEgresado) {
/* 268 */     String nombre = this.vtn.getCmbPeriodo().getSelectedItem().toString().replace(" ", "").replace("/", "-") + " Notas Alumno Egresados" + ((Egresado)this.egresados.get(posEgresado)).getId();
/*     */     
/* 270 */     List<List<String>> alumnos = this.EBD.getNotasPromedioPorEstudiante(((Egresado)this.egresados
/* 271 */         .get(posEgresado)).getId());
/*     */     
/* 273 */     List<String> cols = new ArrayList<>();
/* 274 */     cols.add("IDENTIFICACIÓN");
/* 275 */     cols.add("PRIMER NOMBRE");
/* 276 */     cols.add("SEGUNDO NOMBRE");
/* 277 */     cols.add("PRIMER APELLIDO");
/* 278 */     cols.add("SEGUNDO APELLIDO");
/*     */     
/* 280 */     cols.add("CICLO");
/* 281 */     cols.add("# MATERIAS");
/* 282 */     cols.add("PROMEDIO FINAL");
/* 283 */     ToExcel excel = new ToExcel();
/* 284 */     excel.exportarExcel(cols, alumnos, nombre);
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoEgresadosCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */