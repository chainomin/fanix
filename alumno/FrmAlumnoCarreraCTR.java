/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoCarreraBD;
/*     */ import modelo.alumno.AlumnoCarreraMD;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.persona.AlumnoBD;
/*     */ import modelo.persona.AlumnoMD;
/*     */ import modelo.validaciones.TxtVBuscador;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.FrmAlumnoCarrera;
/*     */ 
/*     */ public class FrmAlumnoCarreraCTR
/*     */   extends DCTR {
/*     */   private final FrmAlumnoCarrera frmAlmCarrera;
/*  30 */   private final AlumnoCarreraBD ACBD = AlumnoCarreraBD.single();
/*     */   
/*     */   private boolean matriculado = false;
/*     */   
/*     */   private String carrera;
/*     */   
/*     */   private DefaultTableModel mdTbl;
/*     */   
/*  38 */   private final AlumnoBD ABD = AlumnoBD.single();
/*     */   
/*     */   private ArrayList<AlumnoMD> alumnos;
/*  41 */   private final CarreraBD CBD = CarreraBD.single();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList<CarreraMD> carreras;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FrmAlumnoCarreraCTR(FrmAlumnoCarrera frmAlmCarrera, VtnPrincipalCTR ctrPrin) {
/*  52 */     super(ctrPrin);
/*  53 */     this.frmAlmCarrera = frmAlmCarrera;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  61 */     ocultarErrores();
/*  62 */     cargarCmbCarreras();
/*     */     
/*  64 */     String[] titulo = { "Cédula", "Nombre" };
/*  65 */     String[][] datos = new String[0][];
/*     */     
/*  67 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  68 */     this.frmAlmCarrera.getTblAlumnos().setModel(this.mdTbl);
/*  69 */     TblEstilo.formatoTbl(this.frmAlmCarrera.getTblAlumnos());
/*     */     
/*  71 */     this.frmAlmCarrera.getBtnGuardar().addActionListener(e -> guardar());
/*  72 */     this.frmAlmCarrera.getBtnBuscar().addActionListener(e -> buscarAlmns(this.frmAlmCarrera.getTxtBuscar().getText().trim()));
/*     */     
/*  74 */     this.frmAlmCarrera.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  77 */             String aguja = FrmAlumnoCarreraCTR.this.frmAlmCarrera.getTxtBuscar().getText().trim();
/*  78 */             if (e.getKeyCode() == 10) {
/*  79 */               FrmAlumnoCarreraCTR.this.buscarAlmns(aguja);
/*     */             }
/*     */           }
/*     */         });
/*  83 */     this.frmAlmCarrera.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.frmAlmCarrera.getTxtBuscar(), this.frmAlmCarrera
/*  84 */           .getBtnBuscar()));
/*  85 */     TblEstilo.columnaMedida(this.frmAlmCarrera.getTblAlumnos(), 0, 100);
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.ctrPrin.estadoCargaFrmFin("Alumno por carrera");
/*     */     
/*  91 */     this.frmAlmCarrera.getTblAlumnos().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  94 */             FrmAlumnoCarreraCTR.this.clickAlumno();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  99 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmAlmCarrera);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ocultarErrores() {
/* 107 */     this.frmAlmCarrera.getLblError().setVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void guardar() {
/* 115 */     if (!this.matriculado) {
/* 116 */       int posAlm = this.frmAlmCarrera.getTblAlumnos().getSelectedRow();
/* 117 */       int posCar = this.frmAlmCarrera.getCmbCarreras().getSelectedIndex();
/*     */       
/* 119 */       boolean guardar = !buscarSiEstaMatriculado(posAlm, posCar);
/*     */       
/* 121 */       if (posAlm < 0 || posCar < 1) {
/* 122 */         guardar = false;
/* 123 */         this.frmAlmCarrera.getLblError().setVisible(true);
/*     */       } else {
/* 125 */         this.frmAlmCarrera.getLblError().setVisible(false);
/*     */       } 
/*     */       
/* 128 */       if (guardar) {
/*     */         
/* 130 */         int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Se matricula a: \n" + ((AlumnoMD)this.alumnos
/* 131 */             .get(posAlm)).getNombreCompleto() + "\n En: \n" + ((CarreraMD)this.carreras
/* 132 */             .get(posCar - 1)).getNombre());
/* 133 */         if (r == 0) {
/* 134 */           AlumnoCarreraMD ac = new AlumnoCarreraMD();
/* 135 */           ac.setAlumno(this.alumnos.get(posAlm));
/* 136 */           ac.setCarrera(this.carreras.get(posCar - 1));
/* 137 */           if (this.ACBD.guardar(ac)) {
/* 138 */             this.frmAlmCarrera.dispose();
/* 139 */             this.ctrPrin.cerradoJIF();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 144 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Ya esta matriculado.\n " + this.carrera + "No se puede inscribir en otra carrera.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean buscarSiEstaMatriculado(int posAlmn, int posCar) {
/* 150 */     this.carrera = this.ACBD.estaMatriculadoEn(((AlumnoMD)this.alumnos
/* 151 */         .get(posAlmn)).getId_Alumno(), ((CarreraMD)this.carreras
/* 152 */         .get(posCar - 1)).getId());
/*     */     
/* 154 */     if (this.carrera.length() > 0) {
/* 155 */       JOptionPane.showMessageDialog((Component)this.ctrPrin
/* 156 */           .getVtnPrin(), ((AlumnoMD)this.alumnos
/* 157 */           .get(posAlmn)).getNombreCompleto() + "\nSe encuentra matriculado en: \n" + this.carrera);
/*     */ 
/*     */       
/* 160 */       return true;
/*     */     } 
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickAlumno() {
/* 170 */     int posAlmn = this.frmAlmCarrera.getTblAlumnos().getSelectedRow();
/* 171 */     int posCar = this.frmAlmCarrera.getCmbCarreras().getSelectedIndex();
/* 172 */     if (posAlmn >= 0 && posCar > 0) {
/* 173 */       this.matriculado = buscarSiEstaMatriculado(posAlmn, posCar);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buscarAlmns(String aguja) {
/* 184 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 185 */       this.frmAlmCarrera.getCmbCarreras().setSelectedIndex(0);
/* 186 */       this.alumnos = this.ABD.buscarAlumnos(aguja);
/* 187 */       this.mdTbl.setRowCount(0);
/* 188 */       if (this.alumnos != null) {
/* 189 */         this.alumnos.forEach(a -> {
/*     */               Object[] valores = { a.getIdentificacion(), a.getPrimerApellido() + " " + a.getSegundoApellido() + " " + a.getPrimerNombre() + " " + a.getSegundoNombre() };
/*     */               this.mdTbl.addRow(valores);
/*     */             });
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
/*     */ 
/*     */   
/*     */   private void cargarCmbCarreras() {
/* 206 */     this.carreras = this.CBD.cargarCarreras();
/* 207 */     if (this.carreras != null) {
/* 208 */       this.frmAlmCarrera.getCmbCarreras().removeAllItems();
/* 209 */       this.frmAlmCarrera.getCmbCarreras().addItem("Seleccione");
/* 210 */       this.carreras.forEach(c -> this.frmAlmCarrera.getCmbCarreras().addItem(c.getNombre()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\FrmAlumnoCarreraCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */