/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.Retirado;
/*     */ import modelo.alumno.RetiradoBD;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import vista.alumno.VtnAlumnoRetirados;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VtnAlumnoRetiradosCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final VtnAlumnoRetirados vtn;
/*  27 */   private static final String[] TITULO = new String[] { "Carrera", "Cédula", "Alumno", "Fecha retiro", "Motivo" };
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultTableModel mdTbl;
/*     */ 
/*     */   
/*  34 */   private final RetiradoBD RBD = RetiradoBD.single();
/*     */   private List<Retirado> todosRetirados;
/*     */   private List<Retirado> retiradosBuscado;
/*  37 */   private final CarreraBD carr = CarreraBD.single();
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */   
/*     */   private final JDRetirarAlumnoCTR RCTR;
/*     */ 
/*     */   
/*     */   public VtnAlumnoRetiradosCTR(VtnPrincipalCTR ctrPrin) {
/*  45 */     super(ctrPrin);
/*  46 */     this.vtn = new VtnAlumnoRetirados();
/*  47 */     this.RCTR = new JDRetirarAlumnoCTR(ctrPrin);
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  51 */     cargarCmbCarreras();
/*  52 */     iniciarVtn();
/*  53 */     cargarAlmnsRetirados();
/*  54 */     this.vtnCargada = true;
/*  55 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtn);
/*     */   }
/*     */   
/*     */   private void iniciarVtn() {
/*  59 */     this.mdTbl = TblEstilo.modelTblSinEditar(TITULO);
/*  60 */     this.vtn.getTblRetirados().setModel(this.mdTbl);
/*  61 */     this.vtn.getBtnEditar().addActionListener(e -> clickEditar());
/*  62 */     this.vtn.getBtnEliminar().addActionListener(e -> clickEliminar());
/*  63 */     this.vtn.getBtnRepRetirados().addActionListener(e -> clickReportePorPeriodo());
/*  64 */     this.vtn.getCmbCarrera().addActionListener(e -> clickPeriodo());
/*  65 */     this.vtn.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  68 */             VtnAlumnoRetiradosCTR.this.buscar(VtnAlumnoRetiradosCTR.this.vtn.getTxtBuscar().getText().trim());
/*     */           }
/*     */         });
/*  71 */     this.vtn.getBtnBuscar().addActionListener(e -> buscar(this.vtn.getTxtBuscar().getText().trim()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscar(String aguja) {
/*  77 */     this.retiradosBuscado = new ArrayList<>();
/*  78 */     this.todosRetirados.forEach(r -> {
/*     */           if (r.getAlmnCarrera().getAlumno().getNombreCompleto().toLowerCase().trim().contains(aguja) || r.getMotivo().toLowerCase().contains(aguja) || r.getPeriodo().getNombre().toLowerCase().contains(aguja)) {
/*     */             this.retiradosBuscado.add(r);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     llenarTblAlmnRetirado(this.retiradosBuscado);
/*     */   }
/*     */   
/*     */   private void clickPeriodo() {
/*  92 */     if (this.vtnCargada) {
/*  93 */       cargarAlmnsRetirados();
/*     */     }
/*     */   }
/*     */   
/*     */   private void clickEditar() {
/*  98 */     int posFila = this.vtn.getTblRetirados().getSelectedRow();
/*  99 */     if (posFila >= 0) {
/* 100 */       this.RCTR.editar(this.retiradosBuscado.get(posFila));
/*     */     } else {
/* 102 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickEliminar() {
/* 107 */     int posFila = this.vtn.getTblRetirados().getSelectedRow();
/* 108 */     if (posFila >= 0) {
/* 109 */       if (this.RBD.eliminar(((Retirado)this.retiradosBuscado.get(posFila)).getId())) {
/* 110 */         JOptionPane.showMessageDialog((Component)this.vtn, "Eliminamos correctamente.");
/* 111 */         this.mdTbl.removeRow(posFila);
/*     */       } 
/*     */     } else {
/* 114 */       errorNoSeleccionoFila();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cargarAlmnsRetirados() {
/* 119 */     int posCar = this.vtn.getCmbCarrera().getSelectedIndex();
/* 120 */     if (posCar > 0) {
/* 121 */       this.todosRetirados = this.RBD.getByCarrera(((CarreraMD)this.carreras
/* 122 */           .get(posCar - 1)).getId());
/*     */     } else {
/*     */       
/* 125 */       this.todosRetirados = this.RBD.getAllTbl();
/*     */     } 
/* 127 */     this.retiradosBuscado = this.todosRetirados;
/* 128 */     llenarTblAlmnRetirado(this.todosRetirados);
/*     */   }
/*     */   
/*     */   private void llenarTblAlmnRetirado(List<Retirado> retirados) {
/* 132 */     this.mdTbl.setRowCount(0);
/* 133 */     if (retirados != null) {
/* 134 */       retirados.forEach(r -> {
/*     */             Object[] valores = { r.getAlmnCarrera().getCarrera().getCodigo(), r.getAlmnCarrera().getAlumno().getIdentificacion(), r.getAlmnCarrera().getAlumno().getPrimerApellido() + " " + r.getAlmnCarrera().getAlumno().getSegundoApellido() + " " + r.getAlmnCarrera().getAlumno().getPrimerNombre() + " " + r.getAlmnCarrera().getAlumno().getSegundoNombre(), r.getFechaRetiro().toString(), r.getMotivo() };
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
/* 147 */       this.vtn.getLblResultados().setText(retirados.size() + " Resultados obtenidos.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cargarCmbCarreras() {
/* 152 */     this.carreras = this.carr.cargarCarrerasCmb();
/* 153 */     if (this.carreras != null) {
/* 154 */       this.vtn.getCmbCarrera().removeAllItems();
/* 155 */       this.vtn.getCmbCarrera().addItem("Todos");
/* 156 */       this.carreras.forEach(c -> this.vtn.getCmbCarrera().addItem(c.getCodigo()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickReportePorPeriodo() {}
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoRetiradosCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */