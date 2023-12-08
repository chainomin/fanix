/*     */ package controlador.docente;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import modelo.docente.MarcacionDocenteBD;
/*     */ import modelo.docente.MarcacionDocenteMD;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.usuario.UsuarioBD;
/*     */ import utils.Mensajes;
/*     */ import utils.Recursos;
/*     */ import utils.RelojDigital;
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
/*     */ public class MarcacionDocente
/*     */ {
/*     */   private vista.docente.MarcacionDocente vista;
/*     */   private RelojDigital reloj;
/*     */   private UsuarioBD usuario;
/*     */   private Mensajes mensajes;
/*     */   
/*     */   public MarcacionDocente(vista.docente.MarcacionDocente vista) {
/*  44 */     this.vista = vista;
/*  45 */     vista.setVisible(true);
/*  46 */     this.reloj = new RelojDigital(vista);
/*  47 */     this.reloj.iniciar();
/*  48 */     this.mensajes = new Mensajes(vista);
/*  49 */     iniciaControl();
/*     */   }
/*     */   
/*     */   public void iniciaControl() {
/*  53 */     this.vista.getClockFace1().setRomano(false);
/*  54 */     this.vista.getBntMarcar1().addActionListener(l -> registraMarcarcion());
/*     */     
/*  56 */     this.vista.getUsuario().addKeyListener(new KeyListener()
/*     */         {
/*     */           public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void keyPressed(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void keyReleased(KeyEvent e) {
/*  69 */             validateTextLength();
/*     */           }
/*     */           
/*     */           private void validateTextLength() {
/*  73 */             String text = MarcacionDocente.this.vista.getUsuario().getText();
/*  74 */             if (text.length() > 9) {
/*  75 */               MarcacionDocente.this.vista.getUsuario().setText(text.substring(0, 10));
/*     */             }
/*  77 */             if (text.length() == 10) {
/*  78 */               MarcacionDocente.this.cargaDatosDocentes();
/*     */             }
/*     */           }
/*     */         });
/*     */     
/*  83 */     this.vista.getPassx().addKeyListener(new KeyListener()
/*     */         {
/*     */           public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void keyPressed(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void keyReleased(KeyEvent e) {
/*  94 */             if (e.getKeyCode() == 10) {
/*  95 */               MarcacionDocente.this.registraMarcarcion();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void cargaDatosDocentes() {
/* 102 */     if (this.vista.getUsuario().getText().length() == 10) {
/* 103 */       String usuarioMC = this.vista.getUsuario().getText();
/* 104 */       DocenteMD docente = DocenteBD.single().buscarDocente(usuarioMC);
/*     */       
/* 106 */       if (docente != null) {
/* 107 */         this.vista.getLblDocente().setText(docente.getNombreCompleto());
/* 108 */         this.vista.getPassx().requestFocus();
/*     */         
/* 110 */         if (docente.getFoto() != null) {
/* 111 */           Image icono = docente.getFoto().getScaledInstance(this.vista.getLbFoto().getWidth(), this.vista
/* 112 */               .getLbFoto().getHeight(), 4);
/* 113 */           this.vista.getLbFoto().setIcon(new ImageIcon(icono));
/* 114 */           this.vista.getLbFoto().updateUI();
/*     */         } else {
/*     */           
/* 117 */           this.vista.getLbFoto().setIcon((Icon)null);
/* 118 */           this.vista.getLbFoto().updateUI();
/*     */         } 
/*     */       } else {
/*     */         
/* 122 */         this.vista.getUsuario().setText("");
/*     */         
/* 124 */         this.vista.getTxtMensaje().setForeground(Color.red);
/* 125 */         (new Thread(() -> {
/*     */               Recursos.voz("audio3.wav", this.vista, "USUARIO NO ENCONTRADO");
/*     */               Recursos.error("Atención", "USUARIO NO ENCONTRADO");
/* 128 */             })).start();
/* 129 */         limpiar();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void registraMarcarcion() {
/* 137 */     String usuarioMC = this.vista.getUsuario().getText();
/* 138 */     String passwordMC = this.vista.getPassx().getText();
/* 139 */     String entrad_salida = "ENTRADA";
/* 140 */     this.usuario = new UsuarioBD();
/* 141 */     this.usuario.setUsername(usuarioMC);
/* 142 */     this.usuario.setPassword(passwordMC);
/* 143 */     this.usuario = this.usuario.selectWhereUsernamePassword();
/* 144 */     if (this.usuario != null) {
/* 145 */       DocenteMD docente = DocenteBD.single().buscarDocente(usuarioMC);
/* 146 */       MarcacionDocenteMD marcacion = new MarcacionDocenteMD();
/* 147 */       marcacion.setDocente_id(docente.getIdDocente());
/* 148 */       marcacion.setFecha_hora(this.vista.getTxtReloj().getText());
/* 149 */       marcacion.setEntrada_salida(entrad_salida);
/* 150 */       MarcacionDocenteMD registro = MarcacionDocenteBD.single().obtenerUltimaMarcacion(docente);
/* 151 */       if (registro.getFecha_hora() != null) {
/* 152 */         if (registro.getEntrada_salida().equals("ENTRADA")) {
/* 153 */           marcacion.setEntrada_salida("SALIDA");
/*     */         }
/* 155 */         if (MarcacionDocenteBD.single().validarTiempoMarcacion(marcacion, registro)) {
/* 156 */           registrar(marcacion);
/*     */         } else {
/*     */           
/* 159 */           this.vista.getTxtMensaje().setForeground(Color.red);
/* 160 */           (new Thread(() -> {
/*     */                 Recursos.warning("Atención", "EL USUARIO YA SE ENCUENTRA REGISTRADO");
/*     */                 Recursos.voz("audio2.wav", this.vista, "EL USUARIO YA SE ENCUENTRA REGISTRADO");
/* 163 */               })).start();
/* 164 */           limpiar();
/*     */         } 
/*     */       } else {
/* 167 */         registrar(marcacion);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 172 */       this.vista.getTxtMensaje().setForeground(Color.red);
/* 173 */       (new Thread(() -> {
/*     */             Recursos.voz("audio3.wav", this.vista, "USUARIO O CONTRASEÑA INCORRECTA");
/*     */             Recursos.error("Atención", "USUARIO O CONTRASEÑA INCORRECTA");
/* 176 */           })).start();
/* 177 */       limpiar();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void registrar(MarcacionDocenteMD marcacion) {
/* 183 */     MarcacionDocenteBD.single().guardar(marcacion);
/* 184 */     this.vista.getLblHora().setText(this.vista.getTxtReloj().getText());
/*     */     
/* 186 */     this.vista.getTxtMensaje().setForeground(Color.green);
/* 187 */     (new Thread(() -> {
/*     */           Recursos.voz("audio1.wav", this.vista, "ACCESO CORRECTO");
/*     */           Recursos.success("Atención", "ACCESO CORRECTO");
/* 190 */         })).start();
/* 191 */     limpiar();
/*     */   }
/*     */   
/*     */   public void limpiar() {
/* 195 */     this.vista.getLblHora().setText("");
/* 196 */     this.vista.getUsuario().setText("");
/* 197 */     this.vista.getLblDocente().setText("");
/* 198 */     this.vista.getPassx().setText("");
/* 199 */     this.vista.getLbFoto().setIcon((Icon)null);
/* 200 */     this.vista.getLbFoto().updateUI();
/* 201 */     this.vista.getUsuario().requestFocus();
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\MarcacionDocente.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */