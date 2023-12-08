/*     */ package controlador.curso;
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.time.LocalTime;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.curso.SesionClaseBD;
/*     */ import modelo.curso.SesionClaseMD;
/*     */ import modelo.validaciones.TxtVHora;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.curso.JDHorario;
/*     */ import vista.curso.PnlHorarioClase;
/*     */ 
/*     */ public class JDHorarioCTR extends DVtnCTR {
/*     */   private final JDHorario jd;
/*     */   private final CursoMD curso;
/*  25 */   private final SesionClaseBD SCBD = SesionClaseBD.single();
/*     */   
/*     */   private int idSesion;
/*     */   
/*     */   private int posFil;
/*     */   private int posColum;
/*     */   private int dia;
/*     */   private int horaC;
/*     */   private int horaT;
/*     */   private int minutoC;
/*     */   private int minutoT;
/*     */   private String jornada;
/*     */   private String horaIni;
/*     */   
/*     */   public JDHorarioCTR(VtnPrincipalCTR ctrPrin, CursoMD curso) {
/*  40 */     super(ctrPrin);
/*  41 */     this.curso = curso;
/*  42 */     this.jd = new JDHorario((Frame)ctrPrin.getVtnPrin(), false);
/*  43 */     this.jd.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*     */   }
/*     */   private String horaFin; private String[] hs; private boolean guardar; private boolean editar; private PnlHorarioClase pnl; private PnlHorarioClase pnlCurso; private PnlHorarioClaseCTR ctrHClase; private PnlHorarioCursoCTR ctrHCurso; private LocalTime inicio;
/*     */   private LocalTime fin;
/*     */   private SesionClaseMD sesion;
/*     */   
/*     */   public void iniciar() {
/*  50 */     cargarDatos();
/*  51 */     horarioClase();
/*  52 */     iniciarValidaciones();
/*  53 */     this.ctrPrin.eventoJDCerrar((JDialog)this.jd);
/*     */     
/*  55 */     llenarCmbDias();
/*     */     
/*  57 */     this.jd.setVisible(true);
/*  58 */     this.jd.getBtnCancelar().setVisible(false);
/*  59 */     this.jd.getBtnCancelar().addActionListener(e -> clickCancelar());
/*     */     
/*  61 */     this.jd.getBtnGuardar().addActionListener(e -> guardar());
/*  62 */     clickTbl();
/*  63 */     horarioCurso();
/*  64 */     this.jd.getTbpHorario().addChangeListener(e -> clickTbp());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickTbp() {
/*  72 */     if (this.jd.getTbpHorario().getSelectedIndex() == 1)
/*     */     {
/*  74 */       this.ctrHCurso.iniciar();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCancelar() {
/*  82 */     this.editar = false;
/*  83 */     this.idSesion = 0;
/*  84 */     limpiarFrm();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbDias() {
/*  91 */     this.jd.getCmbDia().removeAllItems();
/*  92 */     this.jd.getCmbDia().addItem("Seleccione");
/*  93 */     String[] t = { "H", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
/*  94 */     for (int i = 1; i < t.length; i++) {
/*  95 */       this.jd.getCmbDia().addItem(t[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarValidaciones() {
/* 103 */     this.jd.getTxtHoraInicio().addKeyListener((KeyListener)new TxtVHora(this.jd.getTxtHoraInicio()));
/* 104 */     this.jd.getTxtHoraFin().addKeyListener((KeyListener)new TxtVHora(this.jd.getTxtHoraFin()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void guardar() {
/* 111 */     this.guardar = true;
/* 112 */     this.dia = this.jd.getCmbDia().getSelectedIndex();
/* 113 */     this.horaIni = this.jd.getTxtHoraInicio().getText().trim();
/* 114 */     this.horaFin = this.jd.getTxtHoraFin().getText().trim();
/*     */     
/* 116 */     if (this.dia == 0) {
/* 117 */       this.guardar = false;
/* 118 */       this.jd.getLblError().setText("Debe seleccionar un dia.");
/*     */     } else {
/* 120 */       this.jd.getLblError().setText("");
/*     */     } 
/*     */     
/* 123 */     if (!Validar.esHora(this.horaIni) || !Validar.esHora(this.horaFin)) {
/* 124 */       this.guardar = false;
/* 125 */       this.jd.getLblError().setText("<html>Debe ingresar horas validas <br>08:00 - 20:00</html>");
/*     */     } else {
/* 127 */       this.jd.getLblError().setText("");
/*     */       
/* 129 */       this.hs = this.horaIni.split(":");
/* 130 */       this.horaC = Integer.parseInt(this.hs[0]);
/* 131 */       this.minutoC = Integer.parseInt(this.hs[1]);
/* 132 */       this.hs = this.horaFin.split(":");
/* 133 */       this.horaT = Integer.parseInt(this.hs[0]);
/* 134 */       this.minutoT = Integer.parseInt(this.hs[1]);
/*     */       
/* 136 */       if (this.horaC > 22 || this.horaC < 7 || this.horaT > 22 || this.horaC < 7) {
/* 137 */         this.guardar = false;
/* 138 */         this.jd.getLblError().setText("<html>Esta fuera de rango recuerde el formato: <br>08:00 - 20:00</html>");
/*     */       } else {
/* 140 */         this.jd.getLblError().setText("");
/* 141 */         if (this.horaC > this.horaT) {
/* 142 */           this.jd.getLblError().setText("<html>La clase debe empezar antes <br>" + this.horaIni + " - " + this.horaFin + "</html>");
/*     */         } else {
/* 144 */           this.jd.getLblError().setText("");
/* 145 */           todosLosCamposLlenos();
/*     */         } 
/*     */       } 
/*     */       
/* 149 */       if (this.horaT - this.horaC > 1 && this.editar) {
/* 150 */         this.jd.getLblError().setText("<html>Debe ingresar solamente una hora: <br>08:00 - 09:00</html>");
/* 151 */         this.guardar = false;
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     if (this.guardar) {
/*     */       
/* 157 */       this.inicio = LocalTime.of(this.horaC, this.minutoC);
/* 158 */       this.fin = LocalTime.of(this.horaT, this.minutoT);
/* 159 */       SesionClaseMD s = this.SCBD.existeSesion(this.curso.getId(), this.dia, this.inicio, this.fin);
/* 160 */       if (s.getCurso() != null) {
/* 161 */         JOptionPane.showMessageDialog((Component)this.pnlCurso, "Ya ingreso este horario.");
/* 162 */         this.guardar = false;
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     if (!this.guardar || this.minutoC > 0 || this.minutoT > 0);
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (this.guardar) {
/* 171 */       String nsql = "";
/* 172 */       SesionClaseMD sc = new SesionClaseMD();
/* 173 */       if (this.horaT > this.horaC && this.minutoC == 0 && this.minutoT == 0) {
/* 174 */         for (int i = this.horaC; i < this.horaT; i++) {
/* 175 */           this.inicio = LocalTime.of(i, this.minutoC);
/* 176 */           this.fin = LocalTime.of(i + 1, this.minutoT);
/* 177 */           sc.setCurso(this.curso);
/* 178 */           sc.setDia(this.dia);
/* 179 */           sc.setHoraIni(this.inicio);
/* 180 */           sc.setHoraFin(this.fin);
/* 181 */           nsql = nsql + this.SCBD.obtenerInsert(sc) + "\n";
/*     */         } 
/*     */       } else {
/* 184 */         this.inicio = LocalTime.of(this.horaC, this.minutoC);
/* 185 */         this.fin = LocalTime.of(this.horaT, this.minutoT);
/* 186 */         sc.setCurso(this.curso);
/* 187 */         sc.setDia(this.dia);
/* 188 */         sc.setHoraIni(this.inicio);
/* 189 */         sc.setHoraFin(this.fin);
/* 190 */         nsql = nsql + this.SCBD.obtenerInsert(sc) + "\n";
/*     */       } 
/*     */       
/* 193 */       if (this.editar) {
/* 194 */         sc.setId(this.idSesion);
/* 195 */         this.SCBD.editar(sc);
/* 196 */         this.idSesion = 0;
/* 197 */         this.editar = false;
/* 198 */         this.jd.getBtnCancelar().setVisible(false);
/*     */       } else {
/* 200 */         this.SCBD.ingresarHorarios(nsql);
/*     */       } 
/*     */ 
/*     */       
/* 204 */       this.ctrHClase.actualizar(this.dia);
/* 205 */       limpiarFrm();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void todosLosCamposLlenos() {
/* 213 */     if (this.horaIni.equals("") || this.horaFin.equals("") || this.dia == 0) {
/* 214 */       this.guardar = false;
/* 215 */       this.jd.getLblError().setText("<html>Todos los campos son obligatorios <br> Formato hora: 08:00 - 20:00 </html>");
/*     */     } else {
/* 217 */       this.jd.getLblError().setText("");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void horarioClase() {
/* 225 */     this.pnl = new PnlHorarioClase();
/* 226 */     this.ctrHClase = new PnlHorarioClaseCTR(this.pnl, this.curso, this.SCBD);
/* 227 */     this.ctrHClase.iniciar();
/* 228 */     this.jd.getTbpHorario().addTab("Horario Clase", (Component)this.pnl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void horarioCurso() {
/* 235 */     this.pnlCurso = new PnlHorarioClase();
/* 236 */     this.ctrHCurso = new PnlHorarioCursoCTR(this.pnlCurso, this.curso);
/* 237 */     this.jd.getTbpHorario().addTab("Horario Curso", (Component)this.pnlCurso);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarDatos() {
/* 245 */     this.jd.setTitle("Horario - " + this.curso.getMateria().getNombre() + " - " + this.curso.getNombre());
/* 246 */     this.jd.getLblPrd().setText(this.curso.getPeriodo().getNombre());
/* 247 */     this.jd.getLblMateria().setText(this.curso.getMateria().getNombre());
/* 248 */     switch (this.curso.getNombre().charAt(0)) {
/*     */       case 'M':
/* 250 */         this.jornada = "MATUTINA";
/*     */         break;
/*     */       case 'V':
/* 253 */         this.jornada = "VESPERTINA";
/*     */         break;
/*     */       case 'N':
/* 256 */         this.jornada = "NOCTURNA";
/*     */         break;
/*     */       default:
/* 259 */         this.jornada = "NA";
/*     */         break;
/*     */     } 
/* 262 */     this.jd.getLblJornada().setText(this.jornada);
/* 263 */     this.jd.getLblDocente().setText(this.curso.getDocente().getNombreCorto());
/* 264 */     this.jd.getLblCurso().setText(this.curso.getNombre());
/* 265 */     this.jd.getLblCapacidad().setText(this.curso.getCapacidad() + "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickTbl() {
/* 274 */     this.pnl.getTblHorario().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 277 */             JDHorarioCTR.this.posFil = JDHorarioCTR.this.pnl.getTblHorario().getSelectedRow();
/* 278 */             JDHorarioCTR.this.posColum = JDHorarioCTR.this.pnl.getTblHorario().getSelectedColumn();
/*     */ 
/*     */ 
/*     */             
/* 282 */             if (JDHorarioCTR.this.posColum == 3 && 
/* 283 */               JDHorarioCTR.this.pnl.getTblHorario().getValueAt(JDHorarioCTR.this.posFil, JDHorarioCTR.this.posColum) != null) {
/* 284 */               JDHorarioCTR.this.idSesion = Integer.parseInt(JDHorarioCTR.this
/* 285 */                   .pnl.getTblHorario().getValueAt(JDHorarioCTR.this.posFil, JDHorarioCTR.this.posColum).toString().split("%")[0]);
/* 286 */               JDHorarioCTR.this.sesion = JDHorarioCTR.this.SCBD.buscarSesion(JDHorarioCTR.this.idSesion);
/* 287 */               int r = JOptionPane.showOptionDialog((Component)JDHorarioCTR.this.ctrPrin.getVtnPrin(), "Selecciono: " + JDHorarioCTR.this.idSesion + " " + 
/* 288 */                   (String)JDHorarioCTR.this.jd.getCmbDia().getItemAt(JDHorarioCTR.this.sesion.getDia()) + "\nHora inicio: " + JDHorarioCTR.this.sesion.getHoraIni() + "\nHora fin: " + JDHorarioCTR.this
/* 289 */                   .sesion.getHoraFin(), "Sesion Clase", 1, 1, null, new Object[] { "Editar", "Eliminar", "Cancelar" }, "Cancelar");
/*     */ 
/*     */               
/* 292 */               switch (r) {
/*     */                 case 0:
/* 294 */                   JDHorarioCTR.this.jd.getCmbDia().setSelectedIndex(JDHorarioCTR.this.sesion.getDia());
/* 295 */                   JDHorarioCTR.this.jd.getTxtHoraInicio().setText(JDHorarioCTR.this.sesion.getHoraIni().toString());
/* 296 */                   JDHorarioCTR.this.jd.getTxtHoraFin().setText(JDHorarioCTR.this.sesion.getHoraFin().toString());
/* 297 */                   JDHorarioCTR.this.editar = true;
/* 298 */                   JDHorarioCTR.this.jd.getBtnCancelar().setVisible(true);
/*     */                   return;
/*     */                 case 1:
/* 301 */                   JDHorarioCTR.this.idSesion = JDHorarioCTR.this.sesion.getId();
/* 302 */                   JDHorarioCTR.this.SCBD.eliminar(JDHorarioCTR.this.idSesion);
/* 303 */                   JDHorarioCTR.this.pnl.getTblHorario().setValueAt((Object)null, JDHorarioCTR.this.posFil, JDHorarioCTR.this.posColum);
/* 304 */                   JDHorarioCTR.this.ctrHClase.actualizar(JDHorarioCTR.this.sesion.getDia());
/*     */                   return;
/*     */               } 
/* 307 */               System.out.println("Desidio cancelar");
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
/*     */   private void limpiarFrm() {
/* 320 */     this.jd.getCmbDia().setSelectedIndex(0);
/* 321 */     this.jd.getTxtHoraFin().setText("");
/* 322 */     this.jd.getTxtHoraInicio().setText("");
/* 323 */     this.jd.getLblError().setText("");
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\curso\JDHorarioCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */