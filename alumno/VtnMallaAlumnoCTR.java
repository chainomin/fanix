/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoCarreraBD;
/*     */ import modelo.alumno.AlumnoCarreraMD;
/*     */ import modelo.alumno.MallaAlumnoBD;
/*     */ import modelo.alumno.MallaAlumnoMD;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import utils.ToExcel;
/*     */ import vista.alumno.VtnMallaAlumno;
/*     */ 
/*     */ public class VtnMallaAlumnoCTR
/*     */   extends DVtnCTR {
/*     */   private final VtnMallaAlumno vtnMallaAlm;
/*  42 */   private final MallaAlumnoBD MABD = MallaAlumnoBD.single();
/*  43 */   private final String[] cmbEstado = new String[] { "Seleccione", "Cursado", "Matriculado", "Pendiente", "Reprobado", "Anulado/Retirado" };
/*     */   
/*  45 */   private ArrayList<MallaAlumnoMD> mallas = new ArrayList<>();
/*     */   
/*  47 */   private final AlumnoCarreraBD ACRBD = AlumnoCarreraBD.single();
/*     */   
/*     */   private ArrayList<AlumnoCarreraMD> alumnos;
/*  50 */   private final CarreraBD CRBD = CarreraBD.single();
/*  51 */   private ArrayList<CarreraMD> carreras = new ArrayList<>();
/*     */   
/*     */   private DefaultTableModel mdlTbl;
/*     */   
/*     */   private ArrayList<Integer> ciclos;
/*  56 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */ 
/*     */   
/*     */   private boolean cargados = false;
/*     */   
/*     */   private static double suma;
/*     */ 
/*     */   
/*     */   public VtnMallaAlumnoCTR(VtnMallaAlumno vtnMallaAlm, VtnPrincipalCTR ctrPrin) {
/*  65 */     super(ctrPrin);
/*  66 */     this.vtnMallaAlm = vtnMallaAlm;
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
/*     */   public void iniciar() {
/*  78 */     String[] titulo = { "id", "Alumno", "Materia", "Estado", "Ciclo", "Matrícula", "Nota 1", "Nota 2", "Nota 3" };
/*  79 */     String[][] datos = new String[0][];
/*  80 */     this.mdlTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  81 */     TblEstilo.formatoTbl(this.vtnMallaAlm.getTblMallaAlumno());
/*  82 */     this.vtnMallaAlm.getTblMallaAlumno().setModel(this.mdlTbl);
/*     */ 
/*     */     
/*  85 */     TblEstilo.columnaMedida(this.vtnMallaAlm.getTblMallaAlumno(), 4, 60);
/*  86 */     TblEstilo.columnaMedida(this.vtnMallaAlm.getTblMallaAlumno(), 5, 60);
/*  87 */     TblEstilo.columnaMedida(this.vtnMallaAlm.getTblMallaAlumno(), 6, 60);
/*  88 */     TblEstilo.columnaMedida(this.vtnMallaAlm.getTblMallaAlumno(), 7, 60);
/*  89 */     TblEstilo.columnaMedida(this.vtnMallaAlm.getTblMallaAlumno(), 8, 60);
/*     */     
/*  91 */     cargarCmbCarrera();
/*     */ 
/*     */     
/*  94 */     this.vtnMallaAlm.getCmbAlumnos().setEnabled(false);
/*  95 */     this.vtnMallaAlm.getCmbEstado().setEnabled(false);
/*     */     
/*  97 */     this.vtnMallaAlm.getCmbCarreras().addActionListener(e -> clickCombo());
/*  98 */     this.vtnMallaAlm.getCmbAlumnos().addActionListener(e -> clickCombo());
/*  99 */     this.vtnMallaAlm.getCmbEstado().addActionListener(e -> clickCombo());
/* 100 */     this.vtnMallaAlm.getCmbCiclo().addActionListener(e -> clickCombo());
/*     */     
/* 102 */     this.vtnMallaAlm.getBtnActualizarNota().addActionListener(e -> actualizarNotas());
/* 103 */     this.vtnMallaAlm.getBtnIngNota().addActionListener(e -> ingresarNota());
/* 104 */     this.vtnMallaAlm.getBtnReporteCarrera().addActionListener(e -> reportePorCarrera());
/*     */     
/* 106 */     this.vtnMallaAlm.getBtnBuscar().addActionListener(e -> buscarMalla(this.vtnMallaAlm.getTxtBuscar().getText().trim()));
/*     */ 
/*     */     
/* 109 */     this.vtnMallaAlm.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnMallaAlm.getTxtBuscar()));
/*     */     
/* 111 */     this.vtnMallaAlm.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 114 */             String b = VtnMallaAlumnoCTR.this.vtnMallaAlm.getTxtBuscar().getText().trim();
/* 115 */             if (e.getKeyCode() == 10) {
/* 116 */               VtnMallaAlumnoCTR.this.buscarMalla(b);
/* 117 */             } else if (b.length() == 0) {
/* 118 */               VtnMallaAlumnoCTR.this.mdlTbl.setRowCount(0);
/*     */             } 
/*     */           }
/*     */         });
/* 122 */     this.vtnMallaAlm.getBtnReporteMallaAlumno().addActionListener(e -> llamaReporteMallaALumno());
/* 123 */     this.vtnMallaAlm.getBtnExportarExcel().addActionListener(e -> clickReporteExcel());
/*     */     
/* 125 */     this.vtnMallaAlm.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnMallaAlm.getTxtBuscar(), this.vtnMallaAlm
/* 126 */           .getBtnBuscar()));
/*     */     
/* 128 */     this.vtnMallaAlm.getCmbAlumnos().setEditable(true);
/*     */ 
/*     */     
/* 131 */     this.vtnMallaAlm.getCmbAlumnos().getEditor().getEditorComponent().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e)
/*     */           {
/* 135 */             String a = VtnMallaAlumnoCTR.this.vtnMallaAlm.getCmbAlumnos().getEditor().getItem().toString().trim();
/* 136 */             if (e.getKeyCode() != 38 && e.getKeyCode() != 40 && e
/* 137 */               .getKeyCode() != 37 && e.getKeyCode() != 39 && e
/* 138 */               .getKeyCode() != 13 && e
/* 139 */               .getKeyCode() == 10) {
/* 140 */               VtnMallaAlumnoCTR.this.buscarAlumno(a);
/*     */             }
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 146 */     this.vtnMallaAlm.getTblMallaAlumno().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {}
/*     */         });
/*     */ 
/*     */     
/* 152 */     InitPermisosTester();
/*     */     
/* 154 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnMallaAlm);
/* 155 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void InitPermisosTester() {
/* 162 */     if (CONS.ROL.getNombre().equalsIgnoreCase("TESTER")) {
/* 163 */       this.vtnMallaAlm.getBtnIngNota().setEnabled(false);
/* 164 */       this.vtnMallaAlm.getBtnActualizarNota().setEnabled(false);
/* 165 */       this.vtnMallaAlm.getTxtBuscar().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actualizarVtn(MallaAlumnoMD m) {
/* 175 */     this.mallas = this.MABD.cargarMallasPorEstudiante(m.getAlumnoCarrera().getId());
/* 176 */     llenarTbl(this.mallas);
/* 177 */     this.vtnMallaAlm.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void actualizarNotas() {
/* 184 */     this.posFila = this.vtnMallaAlm.getTblMallaAlumno().getSelectedRow();
/* 185 */     if (this.posFila >= 0) {
/* 186 */       FrmMallaActualizarCTR ctrFrm = new FrmMallaActualizarCTR(this.ctrPrin, this.mallas.get(this.posFila), this.MABD, this);
/* 187 */       ctrFrm.iniciar();
/*     */       
/* 189 */       this.vtnMallaAlm.setVisible(false);
/*     */     } else {
/* 191 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleecionar una fila antes.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscarAlumno(String aguja) {
/* 202 */     int posCar = this.vtnMallaAlm.getCmbCarreras().getSelectedIndex();
/* 203 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 204 */       this.alumnos = this.ACRBD.buscarAlumnoCarrera(((CarreraMD)this.carreras.get(posCar - 1)).getId(), aguja);
/*     */       
/* 206 */       llenarCmbAlumno(this.alumnos);
/* 207 */       this.vtnMallaAlm.getCmbAlumnos().showPopup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscarMalla(String aguja) {
/* 218 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 219 */       this.mallas = this.MABD.buscarMallaAlumno(aguja);
/* 220 */       llenarTbl(this.mallas);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarPorAlumno() {
/* 228 */     int posAlm = this.vtnMallaAlm.getCmbAlumnos().getSelectedIndex();
/* 229 */     if (posAlm > 0) {
/* 230 */       this.mallas = this.MABD.cargarMallasPorEstudiante(((AlumnoCarreraMD)this.alumnos.get(posAlm - 1)).getId());
/*     */       
/* 232 */       this.vtnMallaAlm.getCmbEstado().setEnabled(true);
/*     */ 
/*     */       
/* 235 */       this.posFila = this.vtnMallaAlm.getCmbAlumnos().getSelectedIndex() - 1;
/* 236 */       if (this.posFila >= 0) {
/* 237 */         this.vtnMallaAlm.getBtnReporteMallaAlumno().setEnabled(true);
/*     */       }
/*     */       
/* 240 */       llenarTbl(this.mallas);
/*     */     } else {
/*     */       
/* 243 */       this.mdlTbl.setRowCount(0);
/* 244 */       this.vtnMallaAlm.getCmbEstado().removeAllItems();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ingresarNota() {
/* 252 */     int pos = this.vtnMallaAlm.getTblMallaAlumno().getSelectedRow();
/* 253 */     if (pos >= 0) {
/* 254 */       MallaAlumnoMD malla = this.mallas.get(pos);
/* 255 */       if (malla.getMallaNumMatricula() > 0) {
/* 256 */         if (malla.getEstado().equals("C")) {
/* 257 */           JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Ya cursó ésta materia, no se puede ingresar  una nota.");
/*     */         } else {
/* 259 */           String nota = JOptionPane.showInputDialog("Ingrese la nota de \n" + malla
/* 260 */               .getMateria().getNombre() + "\nNúmero de matrícula: " + malla
/* 261 */               .getMallaNumMatricula());
/* 262 */           if (Validar.esNota(nota)) {
/* 263 */             this.MABD.ingresarNota(malla.getId(), malla.getMallaNumMatricula(), Double.parseDouble(nota));
/*     */           } else {
/* 265 */             JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Ingrese una nota válida.");
/* 266 */             ingresarNota();
/*     */           } 
/*     */         } 
/*     */       } else {
/* 270 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "No se encuentra matrícula en esta \nmateria, no puede ingresar su nota.");
/*     */       } 
/*     */     } else {
/*     */       
/* 274 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una fila para poder ingresar una nota.");
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
/*     */ 
/*     */   
/*     */   private void clickCombo() {
/* 288 */     int posCar = this.vtnMallaAlm.getCmbCarreras().getSelectedIndex();
/* 289 */     int ciclo = this.vtnMallaAlm.getCmbCiclo().getSelectedIndex();
/* 290 */     int posEst = this.vtnMallaAlm.getCmbEstado().getSelectedIndex();
/* 291 */     int posAlm = this.vtnMallaAlm.getCmbAlumnos().getSelectedIndex();
/* 292 */     if (posAlm < 1) {
/* 293 */       if (posCar > 0 && ciclo > 0 && posEst > 0) {
/*     */         
/* 295 */         this.mallas = this.MABD.cargarMallaPorCarreraCicloEstado(((CarreraMD)this.carreras
/* 296 */             .get(posCar - 1)).getId(), ciclo, this.cmbEstado[posEst]);
/* 297 */         llenarTbl(this.mallas);
/* 298 */       } else if (posCar > 0 && ciclo > 0 && posEst == 0) {
/*     */         
/* 300 */         this.mallas = this.MABD.cargarMallaPorCarreraCiclo(((CarreraMD)this.carreras.get(posCar - 1)).getId(), ciclo);
/* 301 */         llenarTbl(this.mallas);
/* 302 */       } else if (posCar > 0 && posEst > 0 && ciclo == 0) {
/*     */         
/* 304 */         this.mallas = this.MABD.cargarMallaPorCarreraEstado(((CarreraMD)this.carreras.get(posCar - 1)).getId(), this.cmbEstado[posEst]);
/* 305 */         llenarTbl(this.mallas);
/* 306 */       } else if (posCar > 0) {
/*     */ 
/*     */ 
/*     */         
/* 310 */         this.vtnMallaAlm.getCmbAlumnos().setEnabled(true);
/* 311 */         this.vtnMallaAlm.getCmbEstado().setEnabled(true);
/* 312 */         if (!this.cargados) {
/* 313 */           this.cargados = true;
/* 314 */           cargarCmbEstado();
/* 315 */           this.ciclos = this.MTBD.cargarCiclosCarrera(((CarreraMD)this.carreras.get(posCar - 1)).getId());
/* 316 */           cargarCmbCiclos(this.ciclos);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 321 */         this.cargados = false;
/* 322 */         this.vtnMallaAlm.getCmbAlumnos().removeAllItems();
/* 323 */         this.vtnMallaAlm.getCmbAlumnos().setEnabled(false);
/* 324 */         this.vtnMallaAlm.getCmbEstado().setEnabled(false);
/* 325 */         this.vtnMallaAlm.getCmbCiclo().removeAllItems();
/* 326 */         this.vtnMallaAlm.getCmbEstado().removeAllItems();
/*     */       }
/*     */     
/* 329 */     } else if (posEst > 0 && ciclo > 0) {
/* 330 */       this.mallas = this.MABD.cargarMallaAlumnoPorEstadoCiclo(((AlumnoCarreraMD)this.alumnos
/* 331 */           .get(posAlm - 1)).getId(), ciclo, this.cmbEstado[posEst]);
/* 332 */       llenarTbl(this.mallas);
/* 333 */     } else if (posEst > 0 && ciclo == 0) {
/* 334 */       this.mallas = this.MABD.cargarMallaAlumnoPorEstado(((AlumnoCarreraMD)this.alumnos
/* 335 */           .get(posAlm - 1)).getId(), this.cmbEstado[posEst]);
/* 336 */       llenarTbl(this.mallas);
/* 337 */     } else if (ciclo > 0 && posEst == 0) {
/* 338 */       this.mallas = this.MABD.cargarMallaAlumnoPorCiclo(((AlumnoCarreraMD)this.alumnos
/* 339 */           .get(posAlm - 1)).getId(), ciclo);
/* 340 */       llenarTbl(this.mallas);
/*     */     } else {
/* 342 */       cargarPorAlumno();
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
/*     */   private void llenarTbl(ArrayList<MallaAlumnoMD> mallas) {
/* 354 */     suma = 0.0D;
/* 355 */     this.mdlTbl.setRowCount(0);
/* 356 */     if (mallas != null) {
/* 357 */       mallas.forEach(m -> {
/*     */             Object[] valores = { Integer.valueOf(m.getId()), m.getAlumnoCarrera().getAlumno().getPrimerNombre() + " " + m.getAlumnoCarrera().getAlumno().getSegundoNombre() + " " + m.getAlumnoCarrera().getAlumno().getPrimerApellido() + " " + m.getAlumnoCarrera().getAlumno().getSegundoApellido(), m.getMateria().getNombre(), m.getEstado(), Integer.valueOf(m.getMallaCiclo()), Integer.valueOf(m.getMallaNumMatricula()), Double.valueOf(m.getNota1()), Double.valueOf(m.getNota2()), Double.valueOf(m.getNota3()) };
/*     */             this.mdlTbl.addRow(valores);
/*     */             switch (m.getMallaNumMatricula()) {
/*     */               case 1:
/*     */                 suma += m.getNota1();
/*     */                 break;
/*     */ 
/*     */ 
/*     */               
/*     */               case 2:
/*     */                 suma += m.getNota2();
/*     */                 break;
/*     */ 
/*     */ 
/*     */               
/*     */               case 3:
/*     */                 suma += m.getNota3();
/*     */                 break;
/*     */             } 
/*     */ 
/*     */           
/*     */           });
/* 380 */       double promedio = suma / mallas.size();
/* 381 */       this.vtnMallaAlm.getTxtPromedio().setText(String.format("%.2f", new Object[] { Double.valueOf(promedio) }));
/* 382 */       this.vtnMallaAlm.getLblResultados().setText(mallas.size() + " Resultados obtenidos.");
/*     */     } else {
/* 384 */       this.vtnMallaAlm.getLblResultados().setText("0 Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbCarrera() {
/* 393 */     this.carreras = this.CRBD.cargarCarrerasCmb();
/* 394 */     this.vtnMallaAlm.getCmbCarreras().removeAllItems();
/* 395 */     if (this.carreras != null) {
/* 396 */       this.vtnMallaAlm.getCmbCarreras().addItem("Seleccione");
/* 397 */       if (!CONS.ROL.getNombre().equalsIgnoreCase("TESTER")) {
/* 398 */         this.carreras.forEach(c -> this.vtnMallaAlm.getCmbCarreras().addItem(c.getCodigo()));
/*     */       }
/*     */       else {
/*     */         
/* 402 */         this.vtnMallaAlm.getCmbCarreras().addItem("TAS");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbCiclos(ArrayList<Integer> ciclos) {
/* 414 */     this.vtnMallaAlm.getCmbCiclo().removeAllItems();
/* 415 */     this.vtnMallaAlm.getCmbCiclo().addItem("Todos");
/* 416 */     ciclos.forEach(c -> this.vtnMallaAlm.getCmbCiclo().addItem(c + ""));
/*     */ 
/*     */     
/* 419 */     this.vtnMallaAlm.getCmbCiclo().setSelectedIndex(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbAlumno(ArrayList<AlumnoCarreraMD> alumnos) {
/* 428 */     this.vtnMallaAlm.getCmbAlumnos().removeAllItems();
/* 429 */     if (alumnos != null) {
/* 430 */       this.vtnMallaAlm.getCmbAlumnos().addItem("");
/* 431 */       alumnos.forEach(a -> this.vtnMallaAlm.getCmbAlumnos().addItem(a.getAlumno().getPrimerApellido() + " " + a.getAlumno().getSegundoApellido() + " " + a.getAlumno().getPrimerNombre() + " " + a.getAlumno().getSegundoNombre()));
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
/*     */   
/*     */   private void cargarCmbEstado() {
/* 444 */     this.vtnMallaAlm.getCmbEstado().removeAllItems();
/* 445 */     for (String e : this.cmbEstado) {
/* 446 */       this.vtnMallaAlm.getCmbEstado().addItem(e);
/*     */     }
/* 448 */     this.vtnMallaAlm.getCmbEstado().setSelectedIndex(0);
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 452 */     this.vtnMallaAlm.getBtnActualizarNota().getAccessibleContext().setAccessibleName("Malla-Alumnos-Actualizar Nota");
/* 453 */     this.vtnMallaAlm.getBtnIngNota().getAccessibleContext().setAccessibleName("Malla-Alumnos-Ingresar Nota");
/* 454 */     this.vtnMallaAlm.getBtnReporteMallaAlumno().getAccessibleContext().setAccessibleName("Malla-Alumno-Reporte-Malla de Alumno");
/*     */     
/* 456 */     CONS.activarBtns(new JComponent[] { this.vtnMallaAlm.getBtnActualizarNota(), this.vtnMallaAlm.getBtnIngNota(), this.vtnMallaAlm
/* 457 */           .getBtnReporteMallaAlumno() });
/* 458 */     if (CONS.ROL.getNombre().equalsIgnoreCase("Coordinador") || CONS.ROL
/* 459 */       .getNombre().equalsIgnoreCase("Dev") || CONS.ROL
/* 460 */       .getNombre().equalsIgnoreCase("Secretaria") || CONS.ROL
/* 461 */       .getNombre().equalsIgnoreCase("Secretario") || CONS.ROL
/* 462 */       .getNombre().equalsIgnoreCase("Coordinada/o")) {
/* 463 */       this.vtnMallaAlm.getBtnExportarExcel().setEnabled(true);
/*     */     } else {
/* 465 */       this.vtnMallaAlm.getBtnExportarExcel().setEnabled(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reportePorCarrera() {
/* 470 */     int pos = this.vtnMallaAlm.getCmbCarreras().getSelectedIndex();
/* 471 */     if (pos > 0) {
/* 472 */       this.MABD.cargarMallaPorCarrera(((CarreraMD)this.carreras.get(pos - 1)).getId());
/* 473 */       this.vtnMallaAlm.setCursor(new Cursor(3));
/* 474 */       reporteGrande();
/*     */     } else {
/*     */       
/* 477 */       JOptionPane.showMessageDialog((Component)this.vtnMallaAlm, "Seleccione una carrera.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void llamaReporteMallaALumno() {
/* 486 */     int posCar = this.vtnMallaAlm.getCmbCarreras().getSelectedIndex();
/* 487 */     if (this.MABD.getSql().length() > 0 && posCar > 0) {
/* 488 */       if (this.vtnMallaAlm.getCmbAlumnos().getSelectedIndex() > 0 || this.mallas.size() < 50) {
/*     */         
/* 490 */         String path = "/vista/reportes/repMalaAlumno_1.jasper";
/*     */         
/*     */         try {
/* 493 */           Map<Object, Object> parametro = new HashMap<>();
/* 494 */           parametro.put("consulta", this.MABD.getSql());
/* 495 */           parametro.put("idCarreras", Integer.valueOf(((CarreraMD)this.carreras.get(posCar - 1)).getId()));
/* 496 */           JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 497 */           CON.mostrarReporte(jr, parametro, "Reporte de Malla de Alumno");
/* 498 */         } catch (JRException ex) {
/* 499 */           JOptionPane.showMessageDialog(null, "error" + ex);
/*     */         } 
/*     */       } else {
/* 502 */         reporteGrande();
/*     */       } 
/*     */     } else {
/* 505 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar la carrera y un alumno.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void reporteGrande() {
/* 511 */     String path = "/vista/reportes/repMallas.jasper";
/*     */     try {
/* 513 */       Map<Object, Object> parametro = new HashMap<>();
/* 514 */       parametro.put("consulta", this.MABD.getSql());
/* 515 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 516 */       CON.mostrarReporte(jr, parametro, "Reporte de Malla de Alumno");
/*     */     }
/* 518 */     catch (JRException ex) {
/* 519 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } finally {
/* 521 */       this.vtnMallaAlm.setCursor(new Cursor(0));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickReporteExcel() {
/* 526 */     if (this.mallas.size() > 0 || this.vtnMallaAlm.getCmbCarreras().getSelectedIndex() > 0) {
/*     */       
/* 528 */       int as = this.vtnMallaAlm.getCmbAlumnos().getSelectedIndex();
/* 529 */       int cs = this.vtnMallaAlm.getCmbCarreras().getSelectedIndex();
/* 530 */       if (as > 0) {
/*     */         
/* 532 */         String nombre = ((MallaAlumnoMD)this.mallas.get(0)).getAlumnoCarrera().getAlumno().getIdentificacion();
/* 533 */         reportePorAlumnoCarrera(((AlumnoCarreraMD)this.alumnos
/* 534 */             .get(as - 1)).getId(), nombre);
/*     */ 
/*     */       
/*     */       }
/* 538 */       else if (cs > 0) {
/* 539 */         String nombre = this.vtnMallaAlm.getCmbCarreras().getSelectedItem().toString();
/* 540 */         reportePorCarrera(((CarreraMD)this.carreras
/* 541 */             .get(cs - 1)).getId(), nombre);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 546 */         String nombre = ((MallaAlumnoMD)this.mallas.get(0)).getAlumnoCarrera().getAlumno().getIdentificacion();
/* 547 */         reportePorAlumnoCarrera(((MallaAlumnoMD)this.mallas
/* 548 */             .get(0)).getAlumnoCarrera().getId(), nombre);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 553 */       JOptionPane.showMessageDialog((Component)this.vtnMallaAlm, "Debe buscar un alumno o seleccionar una carrera para poder exportar el reporte.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportePorAlumnoCarrera(int idAlmCarrera, String nombre) {
/* 564 */     nombre = nombre + "-" + LocalDate.now().toString().replace(":", "|").replace(".", "");
/*     */     
/* 566 */     List<List<String>> lista = this.MABD.getPorAlumnoCarrera(idAlmCarrera);
/* 567 */     generaReporte(lista, nombre);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportePorCarrera(int idCarrera, String nombre) {
/* 573 */     nombre = nombre + "-" + LocalDate.now().toString().replace(":", "|").replace(".", "");
/* 574 */     List<List<String>> lista = this.MABD.getPorCarrera(idCarrera);
/* 575 */     generaReporte(lista, nombre);
/*     */   }
/*     */ 
/*     */   
/*     */   private void generaReporte(List<List<String>> lista, String nombre) {
/* 580 */     List<String> cols = new ArrayList<>();
/* 581 */     cols.add("Cedula/Identificacion");
/* 582 */     cols.add("Apellidos");
/* 583 */     cols.add("Nombres");
/* 584 */     cols.add("Materia");
/* 585 */     cols.add("Estado");
/* 586 */     cols.add("Ciclo");
/* 587 */     cols.add("Numero de matricula");
/* 588 */     cols.add("Nota 1");
/* 589 */     cols.add("Nota 2");
/* 590 */     cols.add("Nota 3");
/* 591 */     ToExcel excel = new ToExcel();
/* 592 */     excel.exportarExcel(cols, lista, nombre);
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnMallaAlumnoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */