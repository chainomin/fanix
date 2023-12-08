/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.docente.DocenteMateriaBD;
/*     */ import modelo.docente.DocenteMateriaMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.validaciones.CmbValidar;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.docente.FrmDocenteMateria;
/*     */ 
/*     */ public class FrmDocenteMateriaCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final FrmDocenteMateria frmDM;
/*  32 */   private final DocenteMateriaBD DMBD = DocenteMateriaBD.single();
/*     */   private DocenteMateriaMD docenMat;
/*  34 */   private final DocenteBD DBD = DocenteBD.single();
/*  35 */   private final CarreraBD CRBD = CarreraBD.single();
/*  36 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */   
/*     */   private ArrayList<DocenteMD> docentes;
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */   private ArrayList<MateriaMD> materias;
/*     */   private ArrayList<Integer> ciclos;
/*     */   DefaultTableModel mdTbl;
/*     */   
/*     */   public FrmDocenteMateriaCTR(FrmDocenteMateria frmDM, VtnPrincipalCTR ctrPrin) {
/*  46 */     super(ctrPrin);
/*  47 */     this.frmDM = frmDM;
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  51 */     this.frmDM.getLblError().setText("");
/*     */     
/*  53 */     String[] titulo = { "Cédula", "Docente" };
/*  54 */     String[][] datos = new String[0][];
/*  55 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  56 */     this.frmDM.getTblDocentes().setModel(this.mdTbl);
/*  57 */     TblEstilo.formatoTbl(this.frmDM.getTblDocentes());
/*     */     
/*  59 */     TblEstilo.columnaMedida(this.frmDM.getTblDocentes(), 0, 100);
/*     */     
/*  61 */     estadoCmbCicloYMateria(false);
/*  62 */     llenarCmbCarrera();
/*     */     
/*  64 */     this.frmDM.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  67 */             String a = FrmDocenteMateriaCTR.this.frmDM.getTxtBuscar().getText().trim();
/*  68 */             if (e.getKeyCode() == 10) {
/*  69 */               FrmDocenteMateriaCTR.this.buscarDocente(a);
/*     */             }
/*     */           }
/*     */         });
/*     */     
/*  74 */     this.frmDM.getCmbCarrera().addActionListener(e -> clickCarreras());
/*  75 */     this.frmDM.getCmbCiclo().addActionListener(e -> clickCiclo());
/*     */     
/*  77 */     this.frmDM.getBtnBuscar().addActionListener(e -> buscarDocente(this.frmDM.getTxtBuscar().getText().trim()));
/*  78 */     this.frmDM.getBtnGuardar().addActionListener(e -> guardarYSalir());
/*  79 */     iniciarValidaciones();
/*     */     
/*  81 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmDM);
/*     */   }
/*     */   
/*     */   private void iniciarValidaciones() {
/*  85 */     this.frmDM.getCmbCarrera().addActionListener((ActionListener)new CmbValidar(this.frmDM.getCmbCarrera()));
/*  86 */     this.frmDM.getCmbCiclo().addActionListener((ActionListener)new CmbValidar(this.frmDM.getCmbCiclo()));
/*  87 */     this.frmDM.getCmbMateria().addActionListener((ActionListener)new CmbValidar(this.frmDM.getCmbMateria()));
/*  88 */     this.frmDM.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.frmDM.getTxtBuscar()));
/*     */   }
/*     */   
/*     */   private void guardarYSalir() {
/*  92 */     if (guardar()) {
/*  93 */       this.frmDM.dispose();
/*  94 */       this.ctrPrin.cerradoJIF();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean guardar() {
/* 100 */     boolean guardar = true;
/* 101 */     int posMat = this.frmDM.getCmbMateria().getSelectedIndex();
/* 102 */     int posDoc = this.frmDM.getTblDocentes().getSelectedRow();
/* 103 */     if (posMat < 1) {
/* 104 */       guardar = false;
/*     */     }
/* 106 */     if (posDoc < 0) {
/* 107 */       guardar = false;
/*     */     }
/*     */     
/* 110 */     if (guardar) {
/* 111 */       this.docenMat = this.DMBD.existeDocenteMateria(((DocenteMD)this.docentes.get(posDoc)).getIdDocente(), ((MateriaMD)this.materias
/* 112 */           .get(posMat - 1)).getId());
/* 113 */       if (this.docenMat != null) {
/* 114 */         this.frmDM.getLblError().setText("Ya se asigno esta materia al docente.");
/* 115 */         guardar = false;
/* 116 */         if (!this.docenMat.isActivo()) {
/* 117 */           int r = JOptionPane.showConfirmDialog(null, "Ya se asigno esta materia pero se\nencuentra eliminada, desea activala.");
/*     */           
/* 119 */           if (r == 0) {
/* 120 */             this.DMBD.activar(this.docenMat.getId());
/* 121 */             this.frmDM.dispose();
/* 122 */             this.ctrPrin.cerradoJIF();
/* 123 */             this.ctrPrin.abrirVtnDocenteMateria();
/*     */           } 
/*     */         } 
/*     */       } else {
/* 127 */         this.frmDM.getLblError().setText("");
/*     */       } 
/*     */     } 
/* 130 */     if (guardar) {
/* 131 */       DocenteMateriaMD dcm = new DocenteMateriaMD();
/* 132 */       dcm.setDocente(this.docentes.get(posDoc));
/* 133 */       dcm.setMateria(this.materias.get(posMat - 1));
/* 134 */       guardar = this.DMBD.guardar(dcm);
/*     */     } 
/* 136 */     return guardar;
/*     */   }
/*     */ 
/*     */   
/*     */   private void buscarDocente(String aguja) {
/* 141 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 142 */       this.docentes = this.DBD.buscar(aguja);
/* 143 */       llenarTblDocentes(this.docentes);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void estadoCmbCicloYMateria(boolean estado) {
/* 149 */     this.frmDM.getCmbCiclo().setEnabled(estado);
/* 150 */     this.frmDM.getCmbCiclo().removeAllItems();
/* 151 */     this.frmDM.getCmbMateria().setEnabled(estado);
/* 152 */     this.frmDM.getCmbMateria().removeAllItems();
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickCarreras() {
/* 157 */     int posCar = this.frmDM.getCmbCarrera().getSelectedIndex();
/* 158 */     if (posCar > 0) {
/* 159 */       estadoCmbCicloYMateria(true);
/* 160 */       int idCar = ((CarreraMD)this.carreras.get(posCar - 1)).getId();
/* 161 */       this.ciclos = this.MTBD.cargarCiclosCarrera(idCar);
/* 162 */       llenarCmbCiclo(this.ciclos);
/*     */     } else {
/* 164 */       estadoCmbCicloYMateria(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void clickCiclo() {
/* 170 */     int posCar = this.frmDM.getCmbCarrera().getSelectedIndex();
/* 171 */     int posCic = this.frmDM.getCmbCiclo().getSelectedIndex();
/* 172 */     if (posCar > 0 && posCic > 0) {
/* 173 */       this.materias = this.MTBD.cargarMateriaPorCarreraCiclo(((CarreraMD)this.carreras.get(posCar - 1)).getId(), posCic);
/*     */       
/* 175 */       llenarCmbMaterias(this.materias);
/*     */     } else {
/* 177 */       clickCarreras();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarCmbCarrera() {
/* 182 */     this.frmDM.getCmbCarrera().removeAllItems();
/* 183 */     this.carreras = this.CRBD.cargarCarreras();
/* 184 */     if (this.carreras != null) {
/* 185 */       this.frmDM.getCmbCarrera().addItem("Seleccione");
/* 186 */       this.carreras.forEach(c -> this.frmDM.getCmbCarrera().addItem(c.getCodigo()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbMaterias(ArrayList<MateriaMD> materias) {
/* 193 */     this.frmDM.getCmbMateria().removeAllItems();
/* 194 */     if (materias != null) {
/* 195 */       this.frmDM.getCmbMateria().addItem("Todos");
/* 196 */       materias.forEach(m -> this.frmDM.getCmbMateria().addItem(m.getNombre()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbCiclo(ArrayList<Integer> ciclos) {
/* 203 */     this.frmDM.getCmbCiclo().removeAllItems();
/* 204 */     if (ciclos != null) {
/* 205 */       this.frmDM.getCmbCiclo().addItem("Todos");
/* 206 */       ciclos.forEach(c -> this.frmDM.getCmbCiclo().addItem(c + ""));
/*     */ 
/*     */       
/* 209 */       this.frmDM.getCmbCiclo().setSelectedIndex(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarTblDocentes(ArrayList<DocenteMD> docentes) {
/* 214 */     this.mdTbl.setRowCount(0);
/* 215 */     if (docentes != null)
/* 216 */       docentes.forEach(d -> {
/*     */             Object[] valores = { d.getIdentificacion(), d.getNombreCompleto() };
/*     */             this.mdTbl.addRow(valores);
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\FrmDocenteMateriaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */