/*     */ package controlador.asistenciaAlumnos;
/*     */ 
/*     */ import controlador.Libraries.Middlewares;
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.asistencia.AsistenciaMD;
/*     */ import modelo.asistencia.FechasClase;
/*     */ import modelo.asistencia.GenerarFechas;
/*     */ import modelo.asistencia.NEWAsistenciaBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import utils.CONS;
/*     */ import utils.M;
/*     */ import vista.asistenciaAlumnos.NEWFrmAsistencia;
/*     */ 
/*     */ public class NEWFrmAsistenciaCTR
/*     */   extends DCTR
/*     */ {
/*  33 */   private final NEWFrmAsistencia VTN = new NEWFrmAsistencia();
/*     */   
/*  35 */   private final NEWAsistenciaBD ABD = NEWAsistenciaBD.single();
/*     */   
/*     */   private List<PeriodoLectivoMD> pls;
/*     */   
/*     */   private List<CursoMD> cs;
/*     */   
/*     */   private List<FechasClase> fechas;
/*     */   private List<FechasClase> fechasSelec;
/*     */   private List<AsistenciaMD> as;
/*     */   private DefaultTableModel mdTbl;
/*  45 */   private int maxFaltas = 0;
/*     */   
/*     */   public NEWFrmAsistenciaCTR(VtnPrincipalCTR ctrPrin) {
/*  48 */     super(ctrPrin);
/*     */   }
/*     */   private int numAlum;
/*     */   public void iniciar() {
/*  52 */     iniciarCMBPeriodo();
/*  53 */     iniciarTablas();
/*     */     
/*  55 */     iniciarBuscarCmbFechas();
/*  56 */     this.ctrPrin.agregarVtn((JInternalFrame)this.VTN);
/*  57 */     iniciarAcciones();
/*  58 */     this.vtnCargada = true;
/*     */   }
/*     */   
/*     */   private void iniciarTablas() {
/*  62 */     String[] titulo = { "id", "#", "Alumno", "Faltas" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.mdTbl = iniciarTblConEditar(this.VTN
/*  69 */         .getTblAlumnos(), titulo, 3);
/*     */ 
/*     */ 
/*     */     
/*  73 */     TblEstilo.ocualtarID(this.VTN.getTblAlumnos());
/*  74 */     TblEstilo.columnaMedida(this.VTN.getTblAlumnos(), 1, 30);
/*  75 */     TblEstilo.columnaMedida(this.VTN.getTblAlumnos(), 3, 50);
/*  76 */     this.VTN.getTblAlumnos().setRowHeight(25);
/*  77 */     this.VTN.getTblAlumnos().setModel(this.mdTbl);
/*     */   }
/*     */   
/*     */   private void iniciarAcciones() {
/*  81 */     this.VTN.getCmbMateria().addActionListener(e -> clickCmbCurso());
/*     */ 
/*     */     
/*  84 */     this.VTN.getCmbPeriodo().addActionListener(e -> clickCmbPeriodo());
/*  85 */     this.VTN.getBtnCargarLista().addActionListener(e -> cargarLista());
/*  86 */     this.VTN.getBtnGuardar().addActionListener(e -> guardar());
/*  87 */     this.VTN.getBtnImprimir().addActionListener(e -> {
/*     */           int posPrd = this.VTN.getCmbPeriodo().getSelectedIndex();
/*     */           
/*     */           int posMateria = this.VTN.getCmbMateria().getSelectedIndex();
/*     */           
/*     */           int posFecha = this.VTN.getCmbFechas().getSelectedIndex();
/*     */           
/*     */           if (posPrd > 0 && posMateria > 0) {
/*     */             imprimirReporte();
/*     */           } else {
/*     */             M.errorMsg("Debe selecionar un periodo, una materia y la fecha para poder imprimir un reporte.");
/*     */           } 
/*     */         });
/* 100 */     iniciarAccionesTbl();
/*     */   }
/*     */   
/*     */   private void iniciarAccionesTbl() {
/* 104 */     this.mdTbl.addTableModelListener(new TableModelListener()
/*     */         {
/*     */           boolean active = false;
/*     */           
/*     */           public void tableChanged(TableModelEvent e) {
/* 109 */             if (!this.active && e.getType() == 0) {
/* 110 */               this.active = true;
/* 111 */               NEWFrmAsistenciaCTR.this.actualizarFalta();
/* 112 */               this.active = false;
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void actualizarFalta() {
/* 119 */     int colum = this.VTN.getTblAlumnos().getSelectedColumn();
/* 120 */     int row = this.VTN.getTblAlumnos().getSelectedRow();
/* 121 */     String estado = this.VTN.getTblAlumnos().getValueAt(row, colum).toString();
/*     */     
/* 123 */     if (estado.matches("[0-9]")) {
/* 124 */       int faltas = Integer.parseInt(estado);
/* 125 */       if (faltas > this.maxFaltas) {
/* 126 */         JOptionPane.showMessageDialog((Component)this.VTN, "Hoy solo tenemos " + this.maxFaltas + " horas de clase.");
/* 127 */         this.VTN.getTblAlumnos().setValueAt(Integer.valueOf(this.maxFaltas), row, colum);
/*     */       } else {
/* 129 */         this.VTN.getTblAlumnos().setValueAt(Integer.valueOf(faltas), row, colum);
/*     */       } 
/*     */     } else {
/* 132 */       this.VTN.getTblAlumnos().setValueAt(Integer.valueOf(0), row, colum);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void iniciarCMBPeriodo() {
/* 137 */     this.pls = this.ABD.getPeriodosDocente(CONS.USUARIO
/* 138 */         .getPersona().getIdentificacion());
/*     */     
/* 140 */     this.VTN.getCmbPeriodo().removeAllItems();
/* 141 */     this.VTN.getCmbPeriodo().addItem("Seleccione");
/* 142 */     this.pls.forEach(p -> this.VTN.getCmbPeriodo().addItem(p.getNombre()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbPeriodo() {
/* 148 */     int posPrd = this.VTN.getCmbPeriodo().getSelectedIndex();
/* 149 */     if (posPrd > 0) {
/* 150 */       this.cs = this.ABD.getCursosPeriodoDocente(((PeriodoLectivoMD)this.pls
/* 151 */           .get(posPrd - 1)).getID(), CONS.USUARIO
/* 152 */           .getPersona().getIdentificacion());
/*     */       
/* 154 */       this.VTN.getCmbMateria().removeAllItems();
/* 155 */       this.VTN.getCmbMateria().addItem("Seleccione");
/* 156 */       this.cs.forEach(c -> this.VTN.getCmbMateria().addItem(c.getNombre() + " | " + c.getMateria().getNombre()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 162 */       this.vtnCargada = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickCmbCurso() {
/* 167 */     int posCurso = this.VTN.getCmbMateria().getSelectedIndex();
/* 168 */     if (posCurso > 0) {
/* 169 */       GenerarFechas gf = new GenerarFechas();
/* 170 */       this.fechas = gf.getFechasClaseCurso(((CursoMD)this.cs
/* 171 */           .get(posCurso - 1)).getId());
/*     */       
/* 173 */       this.fechasSelec = this.fechas;
/* 174 */       this.VTN.getCmbFechas().removeAllItems();
/* 175 */       this.VTN.getCmbFechas().addItem("");
/* 176 */       LocalDate ld = LocalDate.now();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 183 */       String fechaActual = ((ld.getDayOfMonth() > 9) ? (String)Integer.valueOf(ld.getDayOfMonth()) : ("0" + ld.getDayOfMonth())) + "/" + ((ld.getMonthValue() > 9) ? (String)Integer.valueOf(ld.getMonthValue()) : ("0" + ld.getMonthValue())) + "/" + ld.getYear();
/* 184 */       this.fechas.forEach(f -> {
/*     */             this.VTN.getCmbFechas().addItem(f.getFecha());
/*     */             if (f.getFecha().equals(fechaActual)) {
/*     */               this.VTN.getCmbFechas().setSelectedItem(fechaActual);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarBuscarCmbFechas() {
/* 196 */     listenerCmbBuscador(this.VTN.getCmbFechas(), buscarFun());
/*     */   }
/*     */   
/*     */   private Function<String, Void> buscarFun() {
/* 200 */     return t -> {
/*     */         buscarCmbFechas(t);
/*     */         return null;
/*     */       };
/*     */   }
/*     */   
/*     */   private void buscarCmbFechas(String aguja) {
/* 207 */     this.VTN.getCmbFechas().removeAllItems();
/* 208 */     this.VTN.getCmbFechas().addItem(aguja);
/* 209 */     this.fechasSelec = new ArrayList<>();
/* 210 */     this.fechas.forEach(f -> {
/*     */           if (f.getFecha().contains(aguja)) {
/*     */             this.VTN.getCmbFechas().addItem(f.getFecha());
/*     */             this.fechasSelec.add(f);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void cargarLista() {
/* 220 */     String fecha = this.VTN.getCmbFechas().getSelectedItem().toString();
/* 221 */     int posCurso = this.VTN.getCmbMateria().getSelectedIndex();
/* 222 */     int posFecha = this.VTN.getCmbFechas().getSelectedIndex();
/* 223 */     if (!fecha.equals("") && posCurso > 0 && posFecha > 0) {
/* 224 */       this.as = this.ABD.getAlumnosCursoFicha(((CursoMD)this.cs
/* 225 */           .get(posCurso - 1)).getId(), fecha);
/*     */ 
/*     */       
/* 228 */       this.maxFaltas = ((FechasClase)this.fechasSelec.get(posFecha - 1)).getHoras();
/* 229 */       this.VTN.getLblInfo().setText(this.maxFaltas + " numero de horas clase.");
/* 230 */       if (this.as.size() > 0) {
/* 231 */         llenarTbl(this.as);
/*     */       } else {
/* 233 */         this.ABD.iniciarAsistenciaCursoFecha(((CursoMD)this.cs
/* 234 */             .get(posCurso - 1)).getId(), fecha);
/*     */ 
/*     */         
/* 237 */         cargarLista();
/*     */       } 
/*     */     } else {
/* 240 */       JOptionPane.showMessageDialog((Component)this.VTN, "Debe seleccionar un curso y la fecha.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTbl(List<AsistenciaMD> as) {
/* 251 */     this.mdTbl.setRowCount(0);
/* 252 */     this.numAlum = 0;
/* 253 */     as.forEach(a -> {
/*     */           this.numAlum++;
/*     */           Object[] r = { Integer.valueOf(a.getId()), Integer.valueOf(this.numAlum), a.getAlumnoCurso().getAlumno().getApellidosNombres(), Integer.valueOf(a.getNumeroFaltas()) };
/*     */           this.mdTbl.addRow(r);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void guardar() {
/* 267 */     String sql = "";
/* 268 */     for (int i = 0; i < this.VTN.getTblAlumnos().getRowCount(); i++) {
/* 269 */       sql = sql + this.ABD.getSqlActualizar(
/* 270 */           Integer.parseInt(this.VTN.getTblAlumnos().getValueAt(i, 0).toString()), 
/* 271 */           Integer.parseInt(this.VTN.getTblAlumnos().getValueAt(i, 3).toString()));
/*     */     }
/*     */     
/* 274 */     if (this.ABD.actualizarFaltas(sql)) {
/* 275 */       JOptionPane.showMessageDialog((Component)this.VTN, "Guardamos correctamente las faltas.");
/*     */     } else {
/* 277 */       M.errorMsg("Error al guardar las faltas.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void imprimirReporte() {
/* 282 */     int posFecha, r = JOptionPane.showOptionDialog((Component)this.VTN, "Reporte individual\n¿Elegir el tipo de Reporte?", "REPORTE UBE", 1, 1, null, new Object[] { "Reporte Asistencia", "Reporte Asistencia UBE", "Reporte Asistencia por Día" }, "Cancelar");
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 297 */     switch (r) {
/*     */       case 0:
/* 299 */         generarReporteAsistencia();
/*     */         break;
/*     */       case 1:
/* 302 */         generarReporteAsistenciaUBE();
/*     */         break;
/*     */       case 2:
/* 305 */         posFecha = this.VTN.getCmbFechas().getSelectedIndex();
/* 306 */         if (posFecha > 0) {
/* 307 */           generarReporteAsistenciaPorDia(); break;
/*     */         } 
/* 309 */         M.errorMsg("Debe seleccionar una fecha para su reporte por dia.");
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generarReporteAsistencia() {
/* 320 */     int posCurso = this.VTN.getCmbMateria().getSelectedIndex();
/*     */     
/* 322 */     String nombrePeriodo = this.VTN.getCmbPeriodo().getSelectedItem().toString().trim();
/* 323 */     String path = "/vista/asistenciaAlumnos/reporteAsistencia/reporteAsistencia.jasper";
/*     */     
/* 325 */     Map<Object, Object> parametros = new HashMap<>();
/* 326 */     parametros.put("id_curso", Integer.valueOf(((CursoMD)this.cs.get(posCurso - 1)).getId()));
/* 327 */     parametros.put("prd_lectivo_nombre", String.valueOf(nombrePeriodo));
/*     */     
/* 329 */     Middlewares.generarReporte(getClass().getResource(path), "Reporte Asistencia", parametros);
/*     */   }
/*     */   
/*     */   private void generarReporteAsistenciaUBE() {
/* 333 */     int posCurso = this.VTN.getCmbMateria().getSelectedIndex();
/* 334 */     String nombrePeriodo = this.VTN.getCmbPeriodo().getSelectedItem().toString().trim();
/* 335 */     String path = "/vista/asistenciaAlumnos/reporteAsistencia/reporteAsistenciaUBE.jasper";
/*     */     
/* 337 */     Map<Object, Object> parametros = new HashMap<>();
/*     */     
/* 339 */     parametros.put("id_curso", Integer.valueOf(((CursoMD)this.cs.get(posCurso - 1)).getId()));
/* 340 */     parametros.put("prd_lectivo_nombre", String.valueOf(nombrePeriodo));
/*     */     
/* 342 */     Middlewares.generarReporte(getClass().getResource(path), "Reporte Asistencia UBE", parametros);
/*     */   }
/*     */   
/*     */   private void generarReporteAsistenciaPorDia() {
/* 346 */     int posCurso = this.VTN.getCmbMateria().getSelectedIndex();
/* 347 */     String nombrePeriodo = this.VTN.getCmbPeriodo().getSelectedItem().toString().trim();
/*     */     
/* 349 */     String path = "/vista/asistenciaAlumnos/reporteAsistencia/reporteAsistenciaPorDia.jasper";
/*     */     
/* 351 */     Map<Object, Object> parametros = new HashMap<>();
/* 352 */     parametros.put("id_curso", Integer.valueOf(((CursoMD)this.cs.get(posCurso - 1)).getId()));
/* 353 */     parametros.put("prd_lectivo_nombre", String.valueOf(nombrePeriodo));
/* 354 */     parametros.put("fecha_asistencia", this.VTN.getCmbFechas().getSelectedItem().toString());
/*     */     
/* 356 */     System.out.println(parametros);
/* 357 */     Middlewares.generarReporte(getClass().getResource(path), "Reporte Asistencia por Día", parametros);
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\asistenciaAlumnos\NEWFrmAsistenciaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */