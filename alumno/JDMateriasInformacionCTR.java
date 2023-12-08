/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoCarreraMD;
/*     */ import modelo.alumno.MallaAlumnoBD;
/*     */ import modelo.alumno.MallaAlumnoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import vista.alumno.JDMateriasInformacion;
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
/*     */ public class JDMateriasInformacionCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final AlumnoCarreraMD alumno;
/*     */   private final MallaAlumnoBD mallaAlm;
/*     */   private final JDMateriasInformacion jd;
/*     */   private final String estado;
/*     */   private ArrayList<MallaAlumnoMD> materiasAlmn;
/*     */   private DefaultTableModel mdTbl;
/*     */   
/*     */   public JDMateriasInformacionCTR(AlumnoCarreraMD alumno, MallaAlumnoBD mallaAlm, String estado, VtnPrincipalCTR ctrPrin) {
/*  40 */     super(ctrPrin);
/*  41 */     this.alumno = alumno;
/*  42 */     this.mallaAlm = mallaAlm;
/*  43 */     this.estado = estado;
/*  44 */     this.jd = new JDMateriasInformacion((Frame)ctrPrin.getVtnPrin(), false);
/*  45 */     this.jd.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*     */     
/*  47 */     this.jd.setVisible(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  55 */     String[] titulo = { "Materia" };
/*  56 */     String[][] datos = new String[0][];
/*  57 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  58 */     this.jd.getTblMaterias().setModel(this.mdTbl);
/*  59 */     TblEstilo.formatoTblConColor(this.jd.getTblMaterias());
/*     */     
/*  61 */     this.jd.getLblAlumno().setText(this.alumno.getAlumno().getPrimerNombre() + " " + this.alumno
/*  62 */         .getAlumno().getPrimerApellido());
/*     */     
/*  64 */     this.jd.getTblMaterias().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  67 */             JDMateriasInformacionCTR.this.clickTbl();
/*     */           }
/*     */         });
/*     */     
/*  71 */     this.ctrPrin.eventoJDCerrar((JDialog)this.jd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cargarTercerasMatriculas() {
/*  78 */     this.materiasAlmn = this.mallaAlm.cargarMallaAlumnoPorEstado(this.alumno.getId(), "R");
/*  79 */     ArrayList<MallaAlumnoMD> terceras = new ArrayList<>();
/*  80 */     this.materiasAlmn.forEach(m -> {
/*     */           if (m.getMallaNumMatricula() == 2) {
/*     */             terceras.add(m);
/*     */           }
/*     */         });
/*  85 */     this.materiasAlmn = terceras;
/*  86 */     llenarTbl(this.materiasAlmn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cargarMateriasEstado() {
/*  94 */     this.materiasAlmn = this.mallaAlm.cargarMallaAlumnoPorEstado(this.alumno.getId(), this.estado);
/*  95 */     llenarTbl(this.materiasAlmn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTbl(ArrayList<MallaAlumnoMD> materiasAlmn) {
/* 104 */     this.mdTbl.setRowCount(0);
/* 105 */     if (materiasAlmn != null) {
/* 106 */       materiasAlmn.forEach(m -> {
/*     */             Object[] valores = { m.getMateria().getNombre() };
/*     */             this.mdTbl.addRow(valores);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickTbl() {
/* 118 */     int pos = this.jd.getTblMaterias().getSelectedRow();
/* 119 */     if (pos >= 0) {
/* 120 */       this.jd.getLblCiclo().setText(((MallaAlumnoMD)this.materiasAlmn.get(pos)).getMallaCiclo() + "");
/* 121 */       this.jd.getLblNota1().setText(((MallaAlumnoMD)this.materiasAlmn.get(pos)).getNota1() + "");
/* 122 */       this.jd.getLblNota2().setText(((MallaAlumnoMD)this.materiasAlmn.get(pos)).getNota2() + "");
/* 123 */       this.jd.getLblNota3().setText(((MallaAlumnoMD)this.materiasAlmn.get(pos)).getNota3() + "");
/* 124 */       this.jd.getLblNumMatricula().setText(((MallaAlumnoMD)this.materiasAlmn.get(pos)).getMallaNumMatricula() + "");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDMateriasInformacionCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */