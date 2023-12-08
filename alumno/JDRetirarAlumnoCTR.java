/*     */ package controlador.alumno;
/*     */ 
/*     */ import com.toedter.calendar.JDateChooser;
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.time.LocalDate;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.AlumnoCarreraMD;
/*     */ import modelo.alumno.Retirado;
/*     */ import modelo.alumno.RetiradoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.periodolectivo.RetiradoPeriodoLectivoBD;
/*     */ import vista.alumno.JDRetirarAlumno;
/*     */ 
/*     */ public class JDRetirarAlumnoCTR
/*     */   extends DCTR {
/*     */   private AlumnoCarreraMD ac;
/*     */   private final JDRetirarAlumno FRM;
/*     */   private Retirado r;
/*  27 */   private final RetiradoBD RBD = RetiradoBD.single();
/*     */   private boolean editar = false;
/*  29 */   private final RetiradoPeriodoLectivoBD RPBD = RetiradoPeriodoLectivoBD.single();
/*     */   
/*     */   private List<PeriodoLectivoMD> ps;
/*     */ 
/*     */   
/*     */   public JDRetirarAlumnoCTR(VtnPrincipalCTR ctrPrin) {
/*  35 */     super(ctrPrin);
/*  36 */     this.FRM = new JDRetirarAlumno((Frame)ctrPrin.getVtnPrin(), false);
/*     */   }
/*     */   
/*     */   public void ingresar(AlumnoCarreraMD ac) {
/*  40 */     this.ac = ac;
/*  41 */     this.r = new Retirado();
/*  42 */     this.r.setAlmnCarrera(ac);
/*  43 */     iniciarVtn();
/*  44 */     this.FRM.getJdcFechaRetiro().setDate(
/*  45 */         getDateFromLocalDate(LocalDate.now()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void editar(Retirado r) {
/*  50 */     this.editar = true;
/*  51 */     this.ac = null;
/*  52 */     this.r = r;
/*  53 */     iniciarVtn();
/*  54 */     this.FRM.getJdcFechaRetiro().setDate(
/*  55 */         getDateFromLocalDate(r.getFechaRetiro()));
/*     */     
/*  57 */     this.FRM.getCmbPeriodo().setSelectedIndex(
/*  58 */         getPosPeriodoById(r.getPeriodo().getID()));
/*     */     
/*  60 */     this.FRM.getTxtMotivo().setText(r.getMotivo());
/*  61 */     this.editar = true;
/*     */   }
/*     */   
/*     */   public void informacion(Retirado r) {
/*  65 */     this.ac = null;
/*  66 */     this.r = r;
/*  67 */     iniciarVtn();
/*  68 */     this.FRM.getJdcFechaRetiro().setDate(
/*  69 */         getDateFromLocalDate(r.getFechaRetiro()));
/*     */     
/*  71 */     this.FRM.getTxtMotivo().setText(r
/*  72 */         .getMotivo());
/*     */     
/*  74 */     this.FRM.getCmbPeriodo().setSelectedIndex(
/*  75 */         getPosPeriodoById(r.getPeriodo().getID()));
/*     */     
/*  77 */     this.FRM.getBtnGuardar().setVisible(false);
/*     */   }
/*     */   
/*     */   private int getPosPeriodoById(int idPeriodo) {
/*  81 */     int pos = 0;
/*  82 */     for (int i = 0; i < this.ps.size(); i++) {
/*  83 */       if (idPeriodo == ((PeriodoLectivoMD)this.ps.get(i)).getID()) {
/*  84 */         pos = i + 1;
/*     */         break;
/*     */       } 
/*     */     } 
/*  88 */     return pos;
/*     */   }
/*     */   
/*     */   private void iniciarVtn() {
/*  92 */     cargarCmbPeriodo();
/*  93 */     this.FRM.getJdcFechaRetiro().setDateFormatString("dd/MM/yyyy");
/*  94 */     this.FRM.getBtnGuardar().addActionListener(e -> guardar());
/*  95 */     abrirJD((JDialog)this.FRM);
/*     */   }
/*     */   
/*     */   private Date getDateFromLocalDate(LocalDate fecha) {
/*  99 */     return Date.from(fecha
/* 100 */         .atStartOfDay().atZone(ZoneId.systemDefault())
/* 101 */         .toInstant());
/*     */   }
/*     */ 
/*     */   
/*     */   private void guardar() {
/* 106 */     int posPrd = this.FRM.getCmbPeriodo().getSelectedIndex();
/* 107 */     LocalDate fecha = getFechaJDC(this.FRM.getJdcFechaRetiro());
/* 108 */     if (posPrd > 0 && fecha != null) {
/* 109 */       this.r.setPeriodo(this.ps.get(posPrd - 1));
/* 110 */       this.r.setMotivo(this.FRM.getTxtMotivo().getText());
/* 111 */       this.r.setFechaRetiro(fecha);
/* 112 */       if (this.editar) {
/* 113 */         this.editar = false;
/* 114 */         if (this.RBD.editar(this.r) > 0) {
/* 115 */           JOptionPane.showMessageDialog((Component)this.FRM, "Editamos correctamente.");
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 121 */       else if (this.RBD.guardar(this.r) > 0) {
/* 122 */         JOptionPane.showMessageDialog((Component)this.FRM, "Guardamos correctamente.");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       this.FRM.setVisible(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cargarCmbPeriodo() {
/* 133 */     if (this.ac != null) {
/* 134 */       this.ps = this.RPBD.getForAlmnCarrera(this.ac.getId());
/*     */     } else {
/* 136 */       this.ps = this.RPBD.getForAlmnCarrera(this.r.getAlmnCarrera().getId());
/*     */     } 
/* 138 */     this.FRM.getCmbPeriodo().removeAllItems();
/* 139 */     this.FRM.getCmbPeriodo().addItem("Seleccione");
/* 140 */     this.ps.forEach(p -> this.FRM.getCmbPeriodo().addItem(p.getNombre()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LocalDate getFechaJDC(JDateChooser jdc) {
/* 146 */     LocalDate fecha = null;
/* 147 */     if (jdc.getDate() != null) {
/* 148 */       fecha = jdc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
/*     */     }
/* 150 */     return fecha;
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDRetirarAlumnoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */