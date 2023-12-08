/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.docente.DocenteMateriaBD;
/*     */ import modelo.docente.DocenteMateriaMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import utils.CONS;
/*     */ import vista.docente.VtnDocenteMateria;
/*     */ 
/*     */ public class VtnDocenteMateriaCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final VtnDocenteMateria vtnDm;
/*  29 */   private final DocenteMateriaBD DMBD = DocenteMateriaBD.single();
/*  30 */   private final CarreraBD CRBD = CarreraBD.single();
/*  31 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */   private ArrayList<MateriaMD> materias;
/*     */   private ArrayList<DocenteMateriaMD> dms;
/*     */   private ArrayList<Integer> ciclos;
/*     */   
/*     */   public VtnDocenteMateriaCTR(VtnDocenteMateria vtnDm, VtnPrincipalCTR ctrPrin) {
/*  39 */     super(ctrPrin);
/*  40 */     this.vtnDm = vtnDm;
/*     */   }
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  45 */     String[] titulo = { "Cédula", "Docente", "Materia" };
/*  46 */     String[][] datos = new String[0][];
/*  47 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  48 */     this.vtnDm.getTblDocentesMateria().setModel(this.mdTbl);
/*  49 */     TblEstilo.formatoTbl(this.vtnDm.getTblDocentesMateria());
/*  50 */     TblEstilo.columnaMedida(this.vtnDm.getTblDocentesMateria(), 0, 100);
/*     */     
/*  52 */     estadoCmbCicloYMateria(false);
/*  53 */     llenarCmbCarrera();
/*     */     
/*  55 */     this.vtnDm.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  58 */             String b = VtnDocenteMateriaCTR.this.vtnDm.getTxtBuscar().getText().trim();
/*  59 */             if (e.getKeyCode() == 10) {
/*  60 */               VtnDocenteMateriaCTR.this.buscar(b);
/*  61 */             } else if (b.length() == 0) {
/*  62 */               VtnDocenteMateriaCTR.this.cargarDocenteMaterias();
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/*  67 */     this.vtnDm.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.vtnDm.getTxtBuscar(), this.vtnDm
/*  68 */           .getBtnBuscar()));
/*     */     
/*  70 */     this.vtnDm.getCmbCarrera().addActionListener(e -> clickCarreras());
/*  71 */     this.vtnDm.getCmbCiclo().addActionListener(e -> clickCiclo());
/*  72 */     this.vtnDm.getCmbMateria().addActionListener(e -> clickMateria());
/*     */     
/*  74 */     this.vtnDm.getBtnBuscar().addActionListener(e -> buscar(this.vtnDm.getTxtBuscar().getText().trim()));
/*  75 */     this.vtnDm.getBtnIngresar().addActionListener(e -> ingresar());
/*  76 */     this.vtnDm.getBtnEliminar().addActionListener(e -> eliminar());
/*     */     
/*  78 */     cargarDocenteMaterias();
/*     */     
/*  80 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtnDm);
/*  81 */     InitPermisos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void eliminar() {
/*  90 */     int pos = this.vtnDm.getTblDocentesMateria().getSelectedRow();
/*  91 */     if (pos >= 0) {
/*  92 */       this.DMBD.eliminar(((DocenteMateriaMD)this.dms.get(pos)).getId());
/*  93 */       buscar(this.vtnDm.getTxtBuscar().getText().trim());
/*     */     } else {
/*  95 */       JOptionPane.showMessageDialog(null, "Debe seleccionar una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void buscar(String aguja) {
/* 101 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 102 */       this.dms = this.DMBD.buscar(aguja);
/* 103 */       llenarTblDocenteMateria(this.dms);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ingresar() {
/* 108 */     this.ctrPrin.abrirFrmDocenteMateria();
/* 109 */     this.vtnDm.dispose();
/* 110 */     this.ctrPrin.cerradoJIF();
/*     */   }
/*     */   
/*     */   private void cargarDocenteMaterias() {
/* 114 */     this.dms = this.DMBD.cargarDocenteMateria();
/* 115 */     llenarTblDocenteMateria(this.dms);
/*     */   }
/*     */ 
/*     */   
/*     */   public void estadoCmbCicloYMateria(boolean estado) {
/* 120 */     this.vtnDm.getCmbCiclo().setEnabled(estado);
/* 121 */     this.vtnDm.getCmbCiclo().removeAllItems();
/* 122 */     this.vtnDm.getCmbMateria().setEnabled(estado);
/* 123 */     this.vtnDm.getCmbMateria().removeAllItems();
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickCarreras() {
/* 128 */     int posCar = this.vtnDm.getCmbCarrera().getSelectedIndex();
/* 129 */     if (posCar > 0) {
/* 130 */       estadoCmbCicloYMateria(true);
/* 131 */       int idCar = ((CarreraMD)this.carreras.get(posCar - 1)).getId();
/*     */ 
/*     */       
/* 134 */       this.ciclos = this.MTBD.cargarCiclosCarrera(idCar);
/* 135 */       llenarCmbCiclo(this.ciclos);
/*     */       
/* 137 */       this.dms = this.DMBD.cargarDocenteMateriaPorCarrera(idCar);
/* 138 */       llenarTblDocenteMateria(this.dms);
/*     */     } else {
/* 140 */       estadoCmbCicloYMateria(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickCiclo() {
/* 146 */     int posCar = this.vtnDm.getCmbCarrera().getSelectedIndex();
/* 147 */     int posCic = this.vtnDm.getCmbCiclo().getSelectedIndex();
/* 148 */     if (posCar > 0 && posCic > 0) {
/* 149 */       this.materias = this.MTBD.cargarMateriaPorCarreraCiclo(((CarreraMD)this.carreras.get(posCar - 1)).getId(), posCic);
/*     */       
/* 151 */       llenarCmbMaterias(this.materias);
/* 152 */       this.dms = this.DMBD.cargarDocenteMateriaPorCarreraYCiclo(((CarreraMD)this.carreras.get(posCar - 1)).getId(), posCic);
/*     */       
/* 154 */       llenarTblDocenteMateria(this.dms);
/*     */     } else {
/* 156 */       clickCarreras();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickMateria() {
/* 162 */     int posMat = this.vtnDm.getCmbMateria().getSelectedIndex();
/* 163 */     if (posMat > 0) {
/* 164 */       this.dms = this.DMBD.cargarDocenteMateriaPorMateria(((MateriaMD)this.materias.get(posMat - 1)).getId());
/* 165 */       llenarTblDocenteMateria(this.dms);
/*     */     } else {
/* 167 */       clickCiclo();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarCmbCarrera() {
/* 172 */     this.vtnDm.getCmbCarrera().removeAllItems();
/* 173 */     this.carreras = this.CRBD.cargarCarrerasCmb();
/* 174 */     if (this.carreras != null) {
/* 175 */       this.vtnDm.getCmbCarrera().addItem("Todas");
/* 176 */       this.carreras.forEach(c -> this.vtnDm.getCmbCarrera().addItem(c.getCodigo()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbMaterias(ArrayList<MateriaMD> materias) {
/* 183 */     this.vtnDm.getCmbMateria().removeAllItems();
/* 184 */     if (materias != null) {
/* 185 */       this.vtnDm.getCmbMateria().addItem("Todos");
/* 186 */       materias.forEach(m -> this.vtnDm.getCmbMateria().addItem(m.getNombre()));
/*     */ 
/*     */       
/* 189 */       this.vtnDm.getCmbMateria().setSelectedIndex(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarCmbCiclo(ArrayList<Integer> ciclos) {
/* 194 */     this.vtnDm.getCmbCiclo().removeAllItems();
/* 195 */     if (ciclos != null) {
/* 196 */       this.vtnDm.getCmbCiclo().addItem("Todos");
/* 197 */       ciclos.forEach(c -> this.vtnDm.getCmbCiclo().addItem(c + ""));
/*     */ 
/*     */       
/* 200 */       this.vtnDm.getCmbCiclo().setSelectedIndex(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarTblDocenteMateria(ArrayList<DocenteMateriaMD> dms) {
/* 205 */     this.mdTbl.setRowCount(0);
/* 206 */     if (dms != null) {
/* 207 */       dms.forEach(o -> {
/*     */             Object[] valores = { o.getDocente().getIdentificacion(), o.getDocente().getPrimerNombre() + " " + o.getDocente().getPrimerApellido(), o.getMateria().getNombre() };
/*     */             
/*     */             this.mdTbl.addRow(valores);
/*     */           });
/*     */       
/* 213 */       this.vtnDm.getLblResultados().setText(dms.size() + " Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void InitPermisos() {
/* 218 */     this.vtnDm.getBtnIngresar().getAccessibleContext().setAccessibleName("Materia-Docentes-Ingresar");
/* 219 */     this.vtnDm.getBtnEliminar().getAccessibleContext().setAccessibleName("Materia-Docentes-Eliminar");
/*     */     
/* 221 */     CONS.activarBtns(new JComponent[] { this.vtnDm.getBtnIngresar(), this.vtnDm.getBtnEliminar() });
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\VtnDocenteMateriaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */