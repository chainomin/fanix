/*     */ package controlador.login;
/*     */ 
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.List;
/*     */ import javax.swing.JFrame;
/*     */ import modelo.ConnDBPool;
/*     */ import modelo.usuario.RolBD;
/*     */ import modelo.usuario.UsuarioBD;
/*     */ import utils.CONBD;
/*     */ import utils.CONS;
/*     */ import vista.usuario.VtnSelectRol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VtnSelectRolCTR
/*     */   extends CONBD
/*     */ {
/*     */   private final VtnSelectRol vista;
/*     */   private RolBD modelo;
/*     */   private final UsuarioBD usuario;
/*     */   private List<RolBD> rolesDelUsuario;
/*     */   
/*     */   public VtnSelectRolCTR() {
/*  32 */     this.vista = new VtnSelectRol();
/*  33 */     this.modelo = new RolBD();
/*  34 */     this.usuario = CONS.USUARIO;
/*  35 */     this.vista.setIconImage(CONS.getImage());
/*  36 */     registroIngreso((JFrame)this.vista);
/*     */   }
/*     */   
/*     */   private void registroIngreso(JFrame vtn) {
/*  40 */     vtn.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowOpened(WindowEvent e) {
/*  43 */             VtnSelectRolCTR.this.inicioSesion();
/*     */           }
/*     */ 
/*     */           
/*     */           public void windowClosing(WindowEvent e) {
/*  48 */             VtnSelectRolCTR.this.cierreSesion();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inicioSesion() {
/*  57 */     String nsql = "INSERT INTO public.\"HistorialUsuarios\"(\n\tusu_username, historial_fecha, historial_tipo_accion, historial_nombre_tabla, historial_pk_tabla)\n\tVALUES ('" + this.usuario.getUsername() + "', now(), 'INICIO SESION', 'SISTEMA', 0);";
/*  58 */     CON.executeNoSQL(nsql);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cierreSesion() {
/*  64 */     String nsql = " INSERT INTO public.\"HistorialUsuarios\"(\n  \tusu_username, historial_fecha, historial_tipo_accion, historial_nombre_tabla, historial_pk_tabla)\n  \tVALUES ('" + this.usuario.getUsername() + "', now(), 'CIERRE SESION', 'SISTEMA', 0);";
/*  65 */     CON.executeNoSQL(nsql);
/*  66 */     ConnDBPool pool = ConnDBPool.single();
/*  67 */     pool.closePool();
/*     */   }
/*     */ 
/*     */   
/*     */   public void Init() {
/*  72 */     this.rolesDelUsuario = this.modelo.SelectWhereUSUARIOusername(this.usuario.getUsername());
/*  73 */     this.vista.getLblUsuario().setText(this.usuario.getUsername());
/*     */     
/*  75 */     rellenarCombo();
/*  76 */     InitEventos();
/*     */     
/*  78 */     this.vista.setVisible(true);
/*  79 */     this.vista.setLocationRelativeTo(null);
/*     */   }
/*     */   
/*     */   private void InitEventos() {
/*  83 */     this.vista.getBtnSeleccionar().addActionListener(e -> ingresar());
/*  84 */     this.vista.getBtnCancelar().addActionListener(e -> {
/*     */           cierreSesion();
/*     */           System.exit(0);
/*     */         });
/*  88 */     this.vista.getCmbRoles().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  91 */             if (e.getKeyCode() == 10) {
/*  92 */               VtnSelectRolCTR.this.ingresar();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void rellenarCombo() {
/* 100 */     this.vista.getCmbRoles().removeAllItems();
/* 101 */     this.rolesDelUsuario.forEach(obj -> this.vista.getCmbRoles().addItem(obj.getNombre()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setModelo() {
/* 109 */     this
/*     */ 
/*     */ 
/*     */       
/* 113 */       .modelo = this.rolesDelUsuario.stream().filter(item -> item.getNombre().equals(this.vista.getCmbRoles().getSelectedItem().toString())).findFirst().orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void ingresar() {
/* 119 */     setModelo();
/* 120 */     CONS.setRol(this.modelo);
/* 121 */     CONS.refreshPermisos();
/* 122 */     VtnPrincipalCTR vtn = new VtnPrincipalCTR(this);
/* 123 */     vtn.iniciar();
/* 124 */     this.vista.dispose();
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\login\VtnSelectRolCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */