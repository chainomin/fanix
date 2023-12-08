/*     */ package controlador.carrera;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.validaciones.CmbValidar;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.TxtVLetras;
/*     */ import modelo.validaciones.TxtVNumeros;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.carrera.FrmCarrera;
/*     */ 
/*     */ public class FrmCarreraCTR
/*     */   extends DCTR {
/*     */   private final FrmCarrera frmCarrera;
/*     */   private boolean editar = false;
/*  33 */   private int idCarrera = 0;
/*     */   
/*  35 */   private final DocenteBD DBD = DocenteBD.single();
/*     */   
/*     */   private ArrayList<DocenteMD> docentes;
/*     */   
/*  39 */   private final String[] MODALIDADES = new String[] { "PRESENCIAL", "SEMIPRESENCIAL", "DISTANCIA", "DUAL", "ESPECIAL" };
/*     */   private DefaultTableModel mdTbl;
/*     */   private final Calendar fechaIni;
/*     */   
/*     */   public FrmCarreraCTR(FrmCarrera frmCarrera, VtnPrincipalCTR ctrPrin)
/*     */   {
/*  45 */     super(ctrPrin);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     this.fechaIni = Calendar.getInstance(); this.frmCarrera = frmCarrera; } public void iniciar() { ocultarErrores(); cargarCmbModalidades(); validaciones(); String[] titulo = { "Cédula", "Nombre" }; String[][] datos = new String[0][]; this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo); this.frmCarrera.getTblDocentes().setModel(this.mdTbl); TblEstilo.formatoTbl(this.frmCarrera.getTblDocentes()); this.frmCarrera.getTxtBuscar().addKeyListener(new KeyAdapter() { public void keyReleased(KeyEvent e) { String a = FrmCarreraCTR.this.frmCarrera.getTxtBuscar().getText().trim(); if (e.getKeyCode() == 10)
/*     */               FrmCarreraCTR.this.buscarDocentes(a);  } }
/*     */       ); this.frmCarrera.getBtnBuscar().addActionListener(e -> buscarDocentes(this.frmCarrera.getTxtBuscar().getText().trim())); this.frmCarrera.getBtnGuardar().addActionListener(e -> guardarYSalir()); this.frmCarrera.getBtnGuardarContinuar().addActionListener(e -> guardarYContinuar()); this.ctrPrin.agregarVtn((JInternalFrame)this.frmCarrera); }
/*     */   private void validaciones() { this.frmCarrera.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.frmCarrera.getTxtBuscar())); this.frmCarrera.getCmbModalidad().addActionListener((ActionListener)new CmbValidar(this.frmCarrera.getCmbModalidad())); this.frmCarrera.getTxtNombre().addKeyListener((KeyListener)new TxtVLetras(this.frmCarrera.getTxtNombre(), this.frmCarrera.getLblErrorNombre())); this.frmCarrera.getTxtCodigo().addKeyListener((KeyListener)new TxtVLetras(this.frmCarrera.getTxtCodigo(), this.frmCarrera.getLblErrorCodigo())); this.frmCarrera.getTxtSemanas().addKeyListener((KeyListener)new TxtVNumeros(this.frmCarrera.getTxtSemanas())); }
/*     */   private void ocultarErrores() { this.frmCarrera.getLblErrorCodigo().setVisible(false); this.frmCarrera.getLblErrorNombre().setVisible(false); }
/*     */   private void guardarYSalir() { if (guardar()) {
/*     */       this.frmCarrera.dispose();
/*     */       this.ctrPrin.cerradoJIF();
/*     */     }  }
/* 228 */   public void editar(CarreraMD carrera) { this.frmCarrera.getTxtNombre().setText(carrera.getNombre());
/* 229 */     this.frmCarrera.getTxtCodigo().setText(carrera.getCodigo());
/* 230 */     this.fechaIni.set(carrera.getFechaInicio().getYear(), carrera.getFechaInicio().getMonthValue() - 1, carrera
/* 231 */         .getFechaInicio().getDayOfMonth());
/* 232 */     this.frmCarrera.getJdFechaInicio().setCalendar(this.fechaIni);
/*     */     
/* 234 */     this.frmCarrera.getCmbModalidad().setSelectedItem(carrera.getModalidad());
/* 235 */     this.frmCarrera.getTxtSemanas().setText(carrera.getNumSemanas() + "");
/*     */     
/* 237 */     if (carrera.getCoordinador().getIdentificacion() != null) {
/* 238 */       this.frmCarrera.getTxtBuscar().setText(carrera.getCoordinador().getIdentificacion());
/* 239 */       buscarDocentes(carrera.getCoordinador().getIdentificacion());
/* 240 */       this.frmCarrera.getTblDocentes().selectAll();
/* 241 */       this.frmCarrera.getTxtBuscar().setEnabled(false);
/* 242 */       this.frmCarrera.getBtnBuscar().setEnabled(false);
/* 243 */       this.frmCarrera.getTblDocentes().setEnabled(false);
/*     */     } 
/*     */     
/* 246 */     this.editar = true;
/* 247 */     this.idCarrera = carrera.getId(); }
/*     */ 
/*     */   
/*     */   private void guardarYContinuar() {
/*     */     if (guardar())
/*     */       borrarCampos(); 
/*     */   }
/* 254 */   private void borrarCampos() { this.frmCarrera.getTxtNombre().setText("");
/* 255 */     this.frmCarrera.getTxtCodigo().setText("");
/* 256 */     this.frmCarrera.getJdFechaInicio().setCalendar(null);
/* 257 */     this.frmCarrera.getCmbModalidad().setSelectedItem("Seleccione");
/* 258 */     this.frmCarrera.getTxtBuscar().setText("");
/* 259 */     this.mdTbl.setRowCount(0); }
/*     */   private boolean guardar() { boolean guardar = true; SimpleDateFormat formFecha = new SimpleDateFormat("dd/MM/yyyy"); Date fecha = this.frmCarrera.getJdFechaInicio().getDate(); String fechaS = formFecha.format(fecha); String[] fec = fechaS.split("/"); String dia = fec[0], mes = fec[1], anio = fec[2]; String nombre = this.frmCarrera.getTxtNombre().getText(); String codigo = this.frmCarrera.getTxtCodigo().getText(); String semanas = this.frmCarrera.getTxtSemanas().getText(); String modalidad = this.frmCarrera.getCmbModalidad().getSelectedItem().toString(); int posCoord = this.frmCarrera.getTblDocentes().getSelectedRow(); LocalDate fechaInicio = LocalDate.now(); if (Validar.esNumeros(dia) && Validar.esNumeros(mes) && Validar.esNumeros(anio)) { try { fechaInicio = LocalDate.of(Integer.parseInt(anio), Integer.parseInt(mes), Integer.parseInt(dia)); } catch (NumberFormatException e) { System.out.println("No es fecha. " + e.getMessage()); guardar = false; }  } else { guardar = false; }  if (!Validar.esNumeros(semanas))
/*     */       guardar = false;  if (posCoord < 0)
/*     */       guardar = false;  if (guardar) { CarreraBD CRBD = CarreraBD.single(); CarreraMD car = new CarreraMD(); car.setCodigo(codigo); car.setFechaInicio(fechaInicio); car.setModalidad(modalidad); car.setNombre(nombre); car.setCoordinador(this.docentes.get(posCoord)); car.setNumSemanas(Integer.parseInt(semanas)); if (this.editar) { car.setId(this.idCarrera); guardar = CRBD.editarCarrera(car); if (guardar) { this.editar = false; return true; }  } else { guardar = CRBD.guardarCarrera(car); }  }  return guardar; }
/*     */   private void buscarDocentes(String aguja) { if (Validar.esLetrasYNumeros(aguja)) { this.docentes = this.DBD.buscar(aguja); llenarTblDocentes(this.docentes); }
/*     */      }
/*     */   private void llenarTblDocentes(ArrayList<DocenteMD> docentes) { this.mdTbl.setRowCount(0); if (docentes != null)
/*     */       docentes.forEach(d -> { Object[] valores = { d.getIdentificacion(), d.getPrimerApellido() + " " + d.getSegundoApellido() + " " + d.getPrimerNombre() + "  " + d.getSegundoNombre() }; this.mdTbl.addRow(valores);
/* 267 */           });  } private void cargarCmbModalidades() { this.frmCarrera.getCmbModalidad().removeAllItems();
/* 268 */     this.frmCarrera.getCmbModalidad().addItem("Seleccione");
/* 269 */     for (String m : this.MODALIDADES)
/* 270 */       this.frmCarrera.getCmbModalidad().addItem(m);  }
/*     */ 
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\carrera\FrmCarreraCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */