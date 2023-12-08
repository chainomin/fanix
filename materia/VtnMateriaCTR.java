/*     */ package controlador.materia;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
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
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import utils.CONS;
/*     */ import vista.materia.FrmMaterias;
/*     */ import vista.materia.FrmRequisitos;
/*     */ import vista.materia.VtnMateria;
/*     */ 
/*     */ public class VtnMateriaCTR
/*     */   extends DVtnCTR {
/*     */   private final VtnMateria vtnMateria;
/*  33 */   private final MateriaBD MTBD = MateriaBD.single();
/*  34 */   private final CarreraBD CRBD = CarreraBD.single();
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<MateriaMD> materias;
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */ 
/*     */   
/*     */   private ArrayList<Integer> ciclos;
/*     */ 
/*     */ 
/*     */   
/*     */   public VtnMateriaCTR(VtnMateria vtnMateria, VtnPrincipalCTR ctrPrin) {
/*  50 */     super(ctrPrin);
/*  51 */     this.vtnMateria = vtnMateria;
/*  52 */     InitPermisosDocente();
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
/*     */   public void iniciar() {
/*  65 */     this.vtnMateria.getBtnPEA().addActionListener(l -> generaPEA());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.vtnMateria.getBtnRequisitos().addActionListener(e -> abrirFrmRequisito());
/*     */     
/*  72 */     String[] titulo = { "id", "Carrera", "Código", "Nombre", "Ciclo", "Docencia", "Prácticas", "Autónomas", "Presencial", "Total" };
/*  73 */     String[][] datos = new String[0][];
/*     */     
/*  75 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*     */     
/*  77 */     this.vtnMateria.getTblMateria().setModel(this.mdTbl);
/*     */     
/*  79 */     TblEstilo.ocualtarID(this.vtnMateria.getTblMateria());
/*     */     
/*  81 */     TblEstilo.formatoTbl(this.vtnMateria.getTblMateria());
/*     */     
/*  83 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 1, 70);
/*  84 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 2, 70);
/*  85 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 4, 40);
/*  86 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 5, 70);
/*  87 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 6, 70);
/*  88 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 7, 70);
/*  89 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 8, 70);
/*  90 */     TblEstilo.columnaMedida(this.vtnMateria.getTblMateria(), 9, 40);
/*     */     
/*  92 */     this.vtnMateria.getCmbCiclo().removeAllItems();
/*  93 */     this.vtnMateria.getCmbCiclo().addItem("Todos");
/*  94 */     this.vtnMateria.getLblError().setVisible(false);
/*     */     
/*  96 */     cargarCmbCarreras();
/*  97 */     this.materias = this.MTBD.cargarMaterias();
/*  98 */     cargarTblMaterias();
/*  99 */     this.vtnMateria.getCmbCarreras().addActionListener(e -> filtrarPorCarrera());
/* 100 */     this.vtnMateria.getCmbCiclo().addActionListener(e -> filtrarPorCarreraPorCiclo());
/* 101 */     this.vtnMateria.getBtnIngresarMateria().addActionListener(e -> ingresarMaterias());
/* 102 */     this.vtnMateria.getBtnEditarMateria().addActionListener(e -> editarMaterias());
/*     */     
/* 104 */     this.vtnMateria.getBtnReporteMaterias().addActionListener(e -> llamaReporteMaterias());
/*     */     
/* 106 */     this.vtnMateria.getBtnBuscar().addActionListener(e -> buscarMaterias(this.vtnMateria.getTxtBuscar().getText().trim()));
/* 107 */     this.vtnMateria.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 110 */             String b = VtnMateriaCTR.this.vtnMateria.getTxtBuscar().getText().trim();
/*     */             
/* 112 */             if (e.getKeyCode() == 10) {
/* 113 */               VtnMateriaCTR.this.buscarMaterias(b);
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 118 */     this.vtnMateria.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnMateria.getTxtBuscar(), this.vtnMateria
/* 119 */           .getBtnBuscar()));
/* 120 */     this.vtnMateria.getBtnInfo().addActionListener(e -> infoMateria());
/*     */     
/* 122 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnMateria);
/* 123 */     this.vtnMateria.getBtnEliminarMateria().setVisible(false);
/* 124 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generaPEA() {
/* 135 */     this.posFila = this.vtnMateria.getTblMateria().getSelectedRow();
/* 136 */     if (this.posFila >= 0) {
/* 137 */       this.vtnMateria.getLblError().setVisible(false);
/*     */ 
/*     */       
/* 140 */       String path = "/vista/materia/reportes/repPEAMateria9_24.jasper";
/*     */       try {
/* 142 */         Map<Object, Object> parametro = new HashMap<>();
/* 143 */         parametro.put("consulta", this.MTBD.getSqlm(Integer.parseInt(this.vtnMateria.getTblMateria().getValueAt(this.posFila, 0).toString())));
/* 144 */         JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 145 */         CON.mostrarReporte(jr, parametro, "PEA");
/* 146 */       } catch (JRException ex) {
/* 147 */         System.out.println(ex.getMessage() + ex.getMessageKey());
/* 148 */         JOptionPane.showMessageDialog(null, "error" + ex);
/*     */       } 
/*     */       
/* 151 */       cargarTblMaterias();
/*     */     } else {
/* 153 */       this.vtnMateria.getLblError().setVisible(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void infoMateria() {
/* 172 */     int pos = this.vtnMateria.getTblMateria().getSelectedRow();
/* 173 */     if (pos >= 0) {
/* 174 */       MateriaMD mt = this.MTBD.buscarMateriaInfo(((MateriaMD)this.materias.get(pos)).getId());
/* 175 */       JDMateriaInfoCTR info = new JDMateriaInfoCTR(mt, this.ctrPrin);
/* 176 */       info.iniciar();
/*     */     } else {
/* 178 */       JOptionPane.showMessageDialog(null, "Seleccione una materia");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buscar() {
/* 186 */     String buscar = this.vtnMateria.getTxtBuscar().getText().trim();
/* 187 */     if (buscar.length() > 2) {
/* 188 */       buscarMaterias(buscar);
/*     */     }
/*     */   }
/*     */   
/*     */   public void buscarMaterias(String b) {
/* 193 */     if (Validar.esLetrasYNumeros(b)) {
/* 194 */       this.materias = this.MTBD.cargarMaterias(b);
/* 195 */       cargarTblMaterias();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void InitPermisosDocente() {
/* 201 */     if (CONS.ROL.getNombre().equalsIgnoreCase("Docente")) {
/* 202 */       this.vtnMateria.getBtnEliminarMateria().setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void cargarCmbCarreras() {
/* 207 */     this.carreras = this.CRBD.cargarCarrerasCmb();
/*     */     
/* 209 */     this.vtnMateria.getCmbCarreras().removeAllItems();
/* 210 */     this.vtnMateria.getCmbCarreras().addItem("Seleccione una carrera");
/* 211 */     this.carreras.forEach(car -> this.vtnMateria.getCmbCarreras().addItem(car.getCodigo()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void filtrarPorCarrera() {
/* 217 */     int pos = this.vtnMateria.getCmbCarreras().getSelectedIndex();
/*     */     
/* 219 */     if (pos > 0) {
/* 220 */       this.materias = this.MTBD.cargarMateriaPorCarrera(((CarreraMD)this.carreras.get(pos - 1)).getId());
/*     */       
/* 222 */       this.ciclos = this.MTBD.cargarCiclosCarrera(((CarreraMD)this.carreras.get(pos - 1)).getId());
/* 223 */       this.vtnMateria.getCmbCiclo().removeAllItems();
/* 224 */       this.vtnMateria.getCmbCiclo().addItem("Todos");
/* 225 */       this.ciclos.forEach(c -> this.vtnMateria.getCmbCiclo().addItem(c + ""));
/*     */ 
/*     */       
/* 228 */       this.vtnMateria.getCmbCiclo().setSelectedIndex(0);
/*     */     } else {
/* 230 */       this.materias = this.MTBD.cargarMaterias();
/*     */       
/* 232 */       this.vtnMateria.getCmbCiclo().removeAllItems();
/*     */     } 
/*     */     
/* 235 */     cargarTblMaterias();
/*     */   }
/*     */   
/*     */   private void cargarTblMaterias() {
/* 239 */     this.mdTbl.setRowCount(0);
/* 240 */     this.vtnMateria.getLblResultados().setText(this.materias.size() + " Resultados obtenidos.");
/* 241 */     if (!this.materias.isEmpty()) {
/* 242 */       this.materias.forEach(mt -> {
/*     */             Object[] valores = { Integer.valueOf(mt.getId()), obtenerCodigoCarrera(mt.getCarrera().getId()), mt.getCodigo(), mt.getNombre(), Integer.valueOf(mt.getCiclo()), Integer.valueOf(mt.getHorasDocencia()), Integer.valueOf(mt.getHorasPracticas()), Integer.valueOf(mt.getHorasAutoEstudio()), Integer.valueOf(mt.getHorasPresenciales()), Integer.valueOf(mt.getTotalHoras()) };
/*     */             this.mdTbl.addRow(valores);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void filtrarPorCarreraPorCiclo() {
/* 255 */     int ciclo = this.vtnMateria.getCmbCiclo().getSelectedIndex();
/* 256 */     int posCar = this.vtnMateria.getCmbCarreras().getSelectedIndex();
/* 257 */     if (ciclo > 0) {
/* 258 */       this.materias = this.MTBD.cargarMateriaPorCarreraCiclo(((CarreraMD)this.carreras
/* 259 */           .get(posCar - 1)).getId(), ciclo);
/* 260 */       cargarTblMaterias();
/*     */     } else {
/* 262 */       filtrarPorCarrera();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void llamaReporteMaterias() {
/* 267 */     this.MTBD.buscarMateria(this.posFila);
/*     */     
/* 269 */     String path = "/vista/reportes/repMaterias.jasper";
/*     */     try {
/* 271 */       Map<Object, Object> parametro = new HashMap<>();
/* 272 */       parametro.put("consulta", this.MTBD.getSql());
/* 273 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 274 */       CON.mostrarReporte(jr, parametro, "Reporte de Materias por Carrera");
/* 275 */     } catch (JRException ex) {
/* 276 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 281 */     this.vtnMateria.getBtnIngresarMateria().getAccessibleContext().setAccessibleName("Materias-Ingresar");
/* 282 */     this.vtnMateria.getBtnRequisitos().getAccessibleContext().setAccessibleName("Materias-Requisitos");
/* 283 */     this.vtnMateria.getBtnInfo().getAccessibleContext().setAccessibleName("Materias-Informacion");
/* 284 */     this.vtnMateria.getBtnEditarMateria().getAccessibleContext().setAccessibleName("Materias-Editar");
/* 285 */     this.vtnMateria.getBtnEliminarMateria().getAccessibleContext().setAccessibleName("Materias-Eliminar");
/* 286 */     this.vtnMateria.getBtnReporteMaterias().getAccessibleContext().setAccessibleName("Materias-Reporte");
/*     */     
/* 288 */     CONS.activarBtns(new JComponent[] { this.vtnMateria.getBtnIngresarMateria(), this.vtnMateria.getBtnRequisitos(), this.vtnMateria
/* 289 */           .getBtnInfo(), this.vtnMateria.getBtnEditarMateria(), this.vtnMateria
/* 290 */           .getBtnEliminarMateria(), this.vtnMateria.getBtnReporteMaterias() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void abrirFrmRequisito() {
/* 295 */     this.posFila = this.vtnMateria.getTblMateria().getSelectedRow();
/* 296 */     if (this.posFila >= 0) {
/*     */       
/* 298 */       FrmRequisitos frmreq = new FrmRequisitos();
/* 299 */       VtnRequisitosCTR vtnreq = new VtnRequisitosCTR(this.ctrPrin, frmreq, this.materias.get(this.posFila));
/* 300 */       vtnreq.iniciar();
/*     */     } else {
/* 302 */       JOptionPane.showMessageDialog(null, "Seleccione una materia");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void ingresarMaterias() {
/* 308 */     this.ctrPrin.abrirFrmMateria();
/* 309 */     this.vtnMateria.dispose();
/* 310 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */   
/*     */   private void editarMaterias() {
/* 314 */     this.posFila = this.vtnMateria.getTblMateria().getSelectedRow();
/* 315 */     if (this.posFila >= 0) {
/* 316 */       this.vtnMateria.getLblError().setVisible(false);
/* 317 */       FrmMaterias frmMateria = new FrmMaterias();
/* 318 */       FrmMateriasCTR ctrFrm = new FrmMateriasCTR(frmMateria, this.ctrPrin, this);
/* 319 */       ctrFrm.iniciar();
/*     */       
/* 321 */       MateriaMD matEditar = this.MTBD.buscarMateria(Integer.parseInt(this.vtnMateria.getTblMateria().getValueAt(this.posFila, 0).toString()));
/* 322 */       ctrFrm.editarMaterias(matEditar);
/* 323 */       cargarTblMaterias();
/*     */     } else {
/* 325 */       this.vtnMateria.getLblError().setVisible(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void eliminarMaterias() {
/* 331 */     this.posFila = this.vtnMateria.getTblMateria().getSelectedRow();
/* 332 */     if (this.posFila >= 0) {
/*     */       
/* 334 */       System.out.println(Integer.valueOf(this.vtnMateria.getTblMateria().getValueAt(this.posFila, 0).toString()));
/* 335 */       MateriaMD mate = this.MTBD.buscarMateria(Integer.valueOf(this.vtnMateria.getTblMateria().getValueAt(this.posFila, 0).toString()).intValue());
/* 336 */       int dialog = 1;
/* 337 */       int result = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar a \n" + this.vtnMateria
/* 338 */           .getTblMateria().getValueAt(this.posFila, 2) + "?", " Eliminar Materia", dialog);
/* 339 */       if (result == 0) {
/* 340 */         if (this.MTBD.elminarMateria(mate.getId()) == true) {
/* 341 */           JOptionPane.showMessageDialog(null, "Datos Eliminados Satisfactoriamente");
/*     */           
/* 343 */           cargarTblMaterias();
/* 344 */           this.vtnMateria.getTxtBuscar().setText("");
/*     */         } else {
/* 346 */           JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMINAR A LA MATERIA");
/*     */         } 
/*     */       }
/*     */     } else {
/* 350 */       JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA PARA ELIMINAR A LA MATERIA");
/*     */     } 
/*     */   }
/*     */   
/*     */   private String obtenerCodigoCarrera(int idCarrera) {
/* 355 */     String cod = "";
/* 356 */     for (int i = 0; i < this.carreras.size(); i++) {
/* 357 */       if (((CarreraMD)this.carreras.get(i)).getId() == idCarrera) {
/* 358 */         cod = ((CarreraMD)this.carreras.get(i)).getCodigo();
/*     */         break;
/*     */       } 
/*     */     } 
/* 362 */     return cod;
/*     */   }
/*     */   
/*     */   public void actualizarVtn() {
/* 366 */     if (this.vtnMateria.getTxtBuscar().getText().length() > 0) {
/* 367 */       buscar();
/*     */     } else {
/* 369 */       filtrarPorCarreraPorCiclo();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\materia\VtnMateriaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */