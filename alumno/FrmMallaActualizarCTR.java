/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JTextField;
/*     */ import modelo.alumno.MallaAlumnoBD;
/*     */ import modelo.alumno.MallaAlumnoMD;
/*     */ import modelo.validaciones.TxtVNota;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.FrmMallaActualizar;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrmMallaActualizarCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final FrmMallaActualizar frmMalla;
/*     */   private final MallaAlumnoMD malla;
/*     */   private final MallaAlumnoBD bd;
/*     */   private final VtnMallaAlumnoCTR ctrMalla;
/*  30 */   private final int[] numMatriculas = new int[] { 1, 2, 3 };
/*  31 */   private int posMatricula = 0;
/*     */   
/*     */   private int numMatricula;
/*  34 */   private final String[] estados = new String[] { "Matriculado", "Cursado", "Reprobado", "Pendiente", "Anulado" };
/*     */   
/*     */   private int posEstado;
/*     */   
/*     */   private double nota1;
/*     */   
/*     */   private double nota2;
/*     */   private double nota3;
/*     */   private String notaAux;
/*     */   
/*     */   public FrmMallaActualizarCTR(VtnPrincipalCTR ctrPrin, MallaAlumnoMD malla, MallaAlumnoBD bd, VtnMallaAlumnoCTR ctrMalla) {
/*  45 */     super(ctrPrin);
/*  46 */     this.malla = malla;
/*  47 */     this.ctrMalla = ctrMalla;
/*  48 */     this.bd = bd;
/*  49 */     this.frmMalla = new FrmMallaActualizar((Frame)ctrPrin.getVtnPrin(), false);
/*  50 */     this.frmMalla.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*  51 */     this.frmMalla.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  58 */     this.frmMalla.getTxtNota().setEnabled(false);
/*  59 */     llenarComboNumMatriculas();
/*  60 */     llenarCmb();
/*     */     
/*  62 */     cargarDatos();
/*  63 */     eventoActualizar(this.frmMalla.getTxtNota());
/*  64 */     iniciarAtajosTeclado();
/*     */     
/*  66 */     this.frmMalla.getTxtNota().addKeyListener((KeyListener)new TxtVNota(this.frmMalla.getTxtNota()));
/*     */     
/*  68 */     this.frmMalla.getCmbNumMatricula().addActionListener(e -> clickCmbNumMatricula());
/*  69 */     this.frmMalla.getCmbEstado().addActionListener(e -> clickEstados());
/*  70 */     this.frmMalla.getBtnGuardar().addActionListener(e -> guardar());
/*     */ 
/*     */     
/*  73 */     this.ctrPrin.eventoJDCerrar((JDialog)this.frmMalla);
/*  74 */     mostrarVtnMalla((JDialog)this.frmMalla);
/*     */   }
/*     */ 
/*     */   
/*     */   private void iniciarAtajosTeclado() {
/*  79 */     this.frmMalla.getTxtNota().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyPressed(KeyEvent e) {
/*  82 */             if (e.getKeyCode() == 10) {
/*  83 */               FrmMallaActualizarCTR.this.guardar();
/*     */             }
/*     */           }
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
/*  96 */     if (this.nota3 > 0.0D) {
/*  97 */       this.numMatricula = 3;
/*  98 */     } else if (this.nota1 > 0.0D && this.nota2 > 0.0D) {
/*  99 */       this.numMatricula = 2;
/* 100 */     } else if (this.nota1 > 0.0D) {
/* 101 */       this.numMatricula = 1;
/*     */     } else {
/* 103 */       this.numMatricula = 0;
/*     */     } 
/*     */     
/* 106 */     boolean guardar = true;
/*     */     
/* 108 */     if (!Validar.esNota(this.nota1 + "") && !Validar.esNota(this.nota2 + "") && !Validar.esNota(this.nota3 + "")) {
/* 109 */       guardar = false;
/*     */     }
/* 111 */     if (guardar && 
/* 112 */       this.bd.actualizarNota(this.malla.getId(), this.nota1, this.nota2, this.nota3, this.numMatricula, this.frmMalla.getLblEstado().getText())) {
/* 113 */       this.ctrPrin.getVtnPrin().setEnabled(true);
/* 114 */       this.ctrMalla.actualizarVtn(this.malla);
/* 115 */       this.frmMalla.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickEstados() {
/* 124 */     this.posEstado = this.frmMalla.getCmbEstado().getSelectedIndex();
/* 125 */     if (this.posEstado > 0) {
/* 126 */       this.frmMalla.getLblEstado().setText(((String)this.frmMalla.getCmbEstado().getItemAt(this.posEstado)).charAt(0) + "");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmb() {
/* 134 */     this.frmMalla.getCmbEstado().removeAllItems();
/* 135 */     this.frmMalla.getCmbEstado().addItem("Seleccione");
/* 136 */     for (String e : this.estados) {
/* 137 */       this.frmMalla.getCmbEstado().addItem(e);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbNumMatricula() {
/* 146 */     this.posMatricula = this.frmMalla.getCmbNumMatricula().getSelectedIndex();
/* 147 */     if (this.posMatricula > 0) {
/* 148 */       this.frmMalla.getTxtNota().setEnabled(true);
/*     */     } else {
/* 150 */       this.frmMalla.getTxtNota().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void eventoActualizar(final JTextField txt) {
/* 160 */     txt.addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 163 */             FrmMallaActualizarCTR.this.notaAux = txt.getText().trim();
/* 164 */             if (FrmMallaActualizarCTR.this.notaAux.length() == 0) {
/* 165 */               FrmMallaActualizarCTR.this.notaAux = "0.0";
/*     */             }
/* 167 */             switch (FrmMallaActualizarCTR.this.posMatricula) {
/*     */               case 1:
/* 169 */                 FrmMallaActualizarCTR.this.frmMalla.getLblNota1().setText(FrmMallaActualizarCTR.this.notaAux);
/*     */                 break;
/*     */               case 2:
/* 172 */                 FrmMallaActualizarCTR.this.frmMalla.getLblNota2().setText(FrmMallaActualizarCTR.this.notaAux);
/*     */                 break;
/*     */               case 3:
/* 175 */                 FrmMallaActualizarCTR.this.frmMalla.getLblNota3().setText(FrmMallaActualizarCTR.this.notaAux);
/*     */                 break;
/*     */             } 
/*     */ 
/*     */             
/* 180 */             FrmMallaActualizarCTR.this.estado(FrmMallaActualizarCTR.this.frmMalla.getLblNota1().getText(), FrmMallaActualizarCTR.this
/* 181 */                 .frmMalla.getLblNota2().getText(), FrmMallaActualizarCTR.this
/* 182 */                 .frmMalla.getLblNota3().getText());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void estado(String n1, String n2, String n3) {
/* 195 */     if (Validar.esNota(n1) && Validar.esNota(n2) && Validar.esNota(n3)) {
/* 196 */       this.nota1 = Double.parseDouble(n1);
/* 197 */       this.nota2 = Double.parseDouble(n2);
/* 198 */       this.nota3 = Double.parseDouble(n3);
/* 199 */       if (this.nota1 >= 70.0D || this.nota2 >= 70.0D || this.nota3 >= 70.0D) {
/* 200 */         this.frmMalla.getLblEstado().setText("C");
/* 201 */         this.numMatricula = 3;
/*     */       } else {
/* 203 */         this.frmMalla.getLblEstado().setText("R");
/*     */       } 
/* 205 */       if (this.nota1 == 0.0D && this.nota2 == 0.0D && this.nota3 == 0.0D) {
/* 206 */         this.frmMalla.getLblEstado().setText("P");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarDatos() {
/* 217 */     this.nota1 = this.malla.getNota1();
/* 218 */     this.nota2 = this.malla.getNota2();
/* 219 */     this.nota3 = this.malla.getNota3();
/* 220 */     this.numMatricula = this.malla.getMallaNumMatricula();
/*     */     
/* 222 */     this.frmMalla.getLblNota1().setText(this.malla.getNota1() + "");
/* 223 */     this.frmMalla.getLblNota2().setText(this.malla.getNota2() + "");
/* 224 */     this.frmMalla.getLblNota3().setText(this.malla.getNota3() + "");
/* 225 */     this.frmMalla.getLblMateria().setText(this.malla.getMateria().getNombre());
/* 226 */     this.frmMalla.getLblNombre().setText(this.malla.getAlumnoCarrera().getAlumno().getNombreCompleto());
/* 227 */     this.frmMalla.getLblEstado().setText(this.malla.getEstado());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarComboNumMatriculas() {
/* 235 */     this.frmMalla.getCmbNumMatricula().removeAllItems();
/* 236 */     this.frmMalla.getCmbNumMatricula().addItem("--");
/* 237 */     for (int n : this.numMatriculas) {
/* 238 */       this.frmMalla.getCmbNumMatricula().addItem(n + "");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mostrarVtnMalla(JDialog vtn) {
/* 247 */     vtn.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/* 250 */             FrmMallaActualizarCTR.this.ctrMalla.actualizarVtn(FrmMallaActualizarCTR.this.malla);
/* 251 */             FrmMallaActualizarCTR.this.frmMalla.dispose();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\FrmMallaActualizarCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */