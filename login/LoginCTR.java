/*     */ package controlador.login;
/*     */ 
/*     */ import controlador.Libraries.Effects;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import modelo.usuario.UsuarioBD;
/*     */ import utils.CONS;
/*     */ import vista.Login;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoginCTR
/*     */ {
/*     */   private final Login vista;
/*     */   private UsuarioBD modelo;
/*     */   private final boolean carga = true;
/*     */   
/*     */   public LoginCTR() {
/*  23 */     this.vista = new Login();
/*  24 */     this.vista.setIconImage(CONS.getImage());
/*     */   }
/*     */ 
/*     */   
/*     */   public void Init() {
/*  29 */     InitEventos();
/*  30 */     this.vista.setLocationRelativeTo(null);
/*  31 */     this.vista.setVisible(true);
/*     */   }
/*     */   
/*     */   private void InitEventos() {
/*  35 */     this.vista.getBtnIngresar().addActionListener(e -> login());
/*  36 */     Effects.btnHover(this.vista.getBtnIngresar(), this.vista.getLblBtnHover(), new Color(139, 195, 74), new Color(235, 192, 36));
/*  37 */     this.vista.getTxtPassword().addKeyListener(eventoText());
/*  38 */     this.vista.getTxtUsername().addKeyListener(eventoText());
/*     */     
/*  40 */     this.vista.getTxtUsername().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  43 */             String txt = LoginCTR.this.vista.getTxtUsername().getText().trim();
/*  44 */             if (txt.length() <= 2) {
/*  45 */               LoginCTR.this.ingresoVeloz(txt);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void activarForm(boolean estado) {
/*  53 */     this.vista.getTxtUsername().setEnabled(estado);
/*  54 */     this.vista.getTxtPassword().setEnabled(estado);
/*  55 */     this.vista.getBtnIngresar().setEnabled(estado);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void login() {
/*  62 */     if (!this.vista.getTxtUsername().getText().isEmpty() && !this.vista.getTxtPassword().getText().isEmpty()) {
/*     */       
/*  64 */       (new Thread(() -> {
/*     */             try {
/*     */               Effects.setLoadCursor((Container)this.vista);
/*     */               
/*     */               String USERNAME = this.vista.getTxtUsername().getText().trim();
/*     */               
/*     */               String PASSWORD = this.vista.getTxtPassword().getText().trim();
/*     */               
/*     */               activarForm(false);
/*     */               
/*     */               this.modelo = new UsuarioBD();
/*     */               
/*     */               this.modelo.setUsername(USERNAME);
/*     */               
/*     */               this.modelo.setPassword(PASSWORD);
/*     */               
/*     */               this.modelo = this.modelo.selectWhereUsernamePassword();
/*     */               
/*     */               if (this.modelo != null) {
/*     */                 this.vista.dispose();
/*     */                 CONS.setUsuario(this.modelo);
/*     */                 VtnSelectRolCTR vtn = new VtnSelectRolCTR();
/*     */                 vtn.Init();
/*     */               } else {
/*     */                 Effects.setTextInLabel(this.vista.getLblAvisos(), "Revise la Informacion Ingresada", CONS.ERROR_COLOR, 2);
/*     */                 Effects.setDefaultCursor((Container)this.vista);
/*     */               } 
/*  91 */             } catch (NullPointerException e) {
/*     */               Effects.setDefaultCursor((Container)this.vista);
/*     */               
/*     */               Effects.setTextInLabel(this.vista.getLblAvisos(), "Revise la Informacion Ingresada", CONS.ERROR_COLOR, 2);
/*     */             } finally {
/*     */               Effects.setDefaultCursor((Container)this.vista);
/*     */               
/*     */               activarForm(true);
/*     */             } 
/* 100 */           })).start();
/*     */     } else {
/* 102 */       Effects.setTextInLabel(this.vista.getLblAvisos(), "RELLENE BIEN LA INFORMACION!!", CONS.ERROR_COLOR, 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyAdapter eventoText() {
/* 109 */     return new KeyAdapter()
/*     */       {
/*     */         public void keyReleased(KeyEvent e) {
/* 112 */           if (e.getKeyCode() == 10) {
/* 113 */             LoginCTR.this.login();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void ingresoVeloz(String c) {
/* 120 */     if (c.length() > 1 && c.length() <= 2)
/* 121 */       if (c.equalsIgnoreCase("J.")) {
/* 122 */         this.vista.getTxtUsername().setText("JOHNNY");
/* 123 */       } else if (c.equalsIgnoreCase("M.")) {
/* 124 */         this.vista.getTxtUsername().setText("MrRainx");
/* 125 */       } else if (c.equalsIgnoreCase("H.")) {
/* 126 */         this.vista.getTxtUsername().setText("0103156675");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\login\LoginCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */