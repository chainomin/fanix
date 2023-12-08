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
/*     */ import modelo.alumno.Egresado;
/*     */ import modelo.alumno.EgresadoBD;
/*     */ import modelo.alumno.MallaAlumnoBD;
/*     */ import modelo.alumno.MallaAlumnoMD;
/*     */ import modelo.alumno.MatriculaBD;
/*     */ import modelo.alumno.UtilEgresadoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import vista.alumno.JDEgresarAlumno;
/*     */ 
/*     */ public class JDEgresarAlumnoCTR
/*     */   extends DCTR {
/*     */   private final JDEgresarAlumno FRM;
/*  28 */   private final EgresadoBD EBD = EgresadoBD.single();
/*     */   
/*     */   private List<PeriodoLectivoMD> ps;
/*  31 */   private Egresado egresado = new Egresado();
/*  32 */   private int idAlmnCarrera = 0;
/*     */   
/*     */   private boolean editar = false;
/*     */   
/*  36 */   private final MallaAlumnoBD MABD = MallaAlumnoBD.single();
/*     */   private List<MallaAlumnoMD> mallaAlumno;
/*  38 */   private final MatriculaBD MTBD = MatriculaBD.single();
/*  39 */   private final UtilEgresadoBD UEBD = UtilEgresadoBD.single();
/*     */   
/*     */   public JDEgresarAlumnoCTR(VtnPrincipalCTR ctrPrin) {
/*  42 */     super(ctrPrin);
/*  43 */     this.FRM = new JDEgresarAlumno((Frame)ctrPrin.getVtnPrin(), false);
/*     */   }
/*     */   
/*     */   public void ingresar(int idAlmnCarrera) {
/*  47 */     this.idAlmnCarrera = idAlmnCarrera;
/*  48 */     AlumnoCarreraMD ac = new AlumnoCarreraMD();
/*  49 */     ac.setId(idAlmnCarrera);
/*  50 */     this.egresado.setAlmnCarrera(ac);
/*  51 */     iniciarVtn();
/*  52 */     String msg = "";
/*  53 */     this.mallaAlumno = this.UEBD.getMateriasNoCursadas(idAlmnCarrera);
/*  54 */     if (this.mallaAlumno.isEmpty()) {
/*  55 */       this.mallaAlumno = this.UEBD.getMateriasNoPagadas(idAlmnCarrera);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  61 */       msg = this.mallaAlumno.stream().map(ma -> "Ciclo: " + ma.getMateria().getCiclo() + "  # Matricula: " + ma.getMallaNumMatricula() + "  Materia: " + ma.getMateria().getNombre() + " \n").reduce(msg, String::concat);
/*     */       
/*  63 */       if (msg.length() > 0) {
/*  64 */         msg = "Matricula que tiene pendiente su pago:\n" + msg;
/*     */       }
/*     */       
/*  67 */       String matriculasPagar = this.MTBD.getMatriculasAPagar(idAlmnCarrera);
/*  68 */       if (matriculasPagar.length() > 0) {
/*  69 */         msg = msg + "\nMatriculas a pagar: \n" + matriculasPagar;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  77 */       msg = this.mallaAlumno.stream().map(ma -> "Ciclo: " + ma.getMateria().getCiclo() + "  # Matricula: " + ma.getMallaNumMatricula() + "  Materia: " + ma.getMateria().getNombre() + " \n").reduce(msg, String::concat);
/*  78 */       msg = "Materias que aun no cursa.\n" + msg;
/*  79 */       this.FRM.getBtnGuardar().setEnabled(false);
/*     */     } 
/*     */     
/*  82 */     if (msg.length() > 0) {
/*  83 */       JOptionPane.showMessageDialog((Component)this.FRM, msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void editar(Egresado e) {
/*  91 */     this.idAlmnCarrera = e.getAlmnCarrera().getId();
/*  92 */     this.egresado = e;
/*  93 */     iniciarVtn();
/*  94 */     if (e.getFechaEgreso() != null) {
/*  95 */       this.FRM.getJdcFechaEgreso().setDate(
/*  96 */           getDateFromLocalDate(e.getFechaEgreso()));
/*     */     }
/*     */ 
/*     */     
/* 100 */     this.FRM.getCbxTrabajoTitulacion().setSelected(e.isTrabajoTitulacion());
/* 101 */     if (e.isGraduado() && e.getFechaGraduacion() != null) {
/* 102 */       this.FRM.getCbxGraduado().setSelected(true);
/* 103 */       this.FRM.getJdcFechaGraduacion().setDate(
/* 104 */           getDateFromLocalDate(e.getFechaGraduacion()));
/*     */     } 
/*     */     
/* 107 */     this.editar = true;
/*     */   }
/*     */   
/*     */   private void iniciarVtn() {
/* 111 */     cargarCmbPeriodo();
/* 112 */     this.FRM.getJdcFechaEgreso().setDateFormatString("dd/MM/yyyy");
/* 113 */     this.FRM.getJdcFechaGraduacion().setDateFormatString("dd/MM/yyyy");
/* 114 */     this.FRM.getBtnGuardar().addActionListener(ev -> guardar());
/* 115 */     abrirJD((JDialog)this.FRM);
/*     */   }
/*     */   
/*     */   private void guardar() {
/* 119 */     if (valido()) {
/* 120 */       int idGenerado; String msg; int posPeriodo = this.FRM.getCmbPeriodo().getSelectedIndex();
/* 121 */       this.egresado.setPeriodo(this.ps.get(posPeriodo));
/* 122 */       this.egresado.setFechaEgreso(getFechaJDC(this.FRM.getJdcFechaEgreso()));
/* 123 */       this.egresado.setGraduado(this.FRM.getCbxGraduado().isSelected());
/* 124 */       this.egresado.setTrabajoTitulacion(this.FRM.getCbxTrabajoTitulacion().isSelected());
/* 125 */       this.egresado.setFechaGraduacion(getFechaJDC(this.FRM.getJdcFechaGraduacion()));
/*     */ 
/*     */       
/* 128 */       if (this.editar) {
/* 129 */         if (this.egresado.isGraduado()) {
/* 130 */           idGenerado = this.EBD.editarConGraduacion(this.egresado);
/*     */         } else {
/* 132 */           idGenerado = this.EBD.editarSinGraduacion(this.egresado);
/*     */         } 
/* 134 */         msg = "Editamos correctamente.";
/* 135 */         this.editar = false;
/*     */       } else {
/* 137 */         if (this.egresado.isGraduado()) {
/* 138 */           idGenerado = this.EBD.guardarConGraduacion(this.egresado);
/*     */         } else {
/* 140 */           idGenerado = this.EBD.guardarSinGraduacion(this.egresado);
/*     */         } 
/* 142 */         msg = "Guardamos correctamente.";
/*     */       } 
/*     */       
/* 145 */       if (idGenerado > 0) {
/* 146 */         JOptionPane.showMessageDialog((Component)this.FRM, msg);
/* 147 */         this.FRM.setVisible(false);
/*     */       } else {
/* 149 */         JOptionPane.showMessageDialog((Component)this.FRM, "No pudimos realizar la accion.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean valido() {
/* 155 */     boolean valido = true;
/* 156 */     String msg = "";
/* 157 */     if (this.FRM.getJdcFechaEgreso().getDate() == null) {
/* 158 */       msg = msg + "No indico en que fecha egreso el alumno.\n";
/* 159 */       valido = false;
/*     */     } 
/*     */     
/* 162 */     if (this.FRM.getCbxGraduado().isSelected() && 
/* 163 */       this.FRM.getJdcFechaGraduacion().getDate() == null) {
/* 164 */       msg = msg + "No indico en que fecha se graduo el alumno.\n";
/* 165 */       valido = false;
/*     */     } 
/*     */ 
/*     */     
/* 169 */     if (!valido) {
/* 170 */       JOptionPane.showMessageDialog((Component)this.FRM, msg, "Error en el formulario", 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     return valido;
/*     */   }
/*     */   
/*     */   private void cargarCmbPeriodo() {
/* 182 */     this.ps = this.EBD.getPeriodoByIdAlmnCarrera(this.idAlmnCarrera);
/* 183 */     this.FRM.getCmbPeriodo().removeAllItems();
/* 184 */     this.ps.forEach(p -> this.FRM.getCmbPeriodo().addItem(p.getNombre()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LocalDate getFechaJDC(JDateChooser jdc) {
/* 190 */     LocalDate fecha = null;
/* 191 */     if (jdc.getDate() != null) {
/* 192 */       fecha = jdc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
/*     */     }
/* 194 */     return fecha;
/*     */   }
/*     */   
/*     */   private Date getDateFromLocalDate(LocalDate fecha) {
/* 198 */     return Date.from(fecha
/* 199 */         .atStartOfDay().atZone(ZoneId.systemDefault())
/* 200 */         .toInstant());
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDEgresarAlumnoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */