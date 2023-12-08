/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.alumno.AlumnoCursoBD;
/*     */ import modelo.alumno.AlumnoCursoMD;
/*     */ import modelo.alumno.AlumnoCursoRetiradoBD;
/*     */ import modelo.alumno.AlumnoCursoRetiradoMD;
/*     */ import modelo.alumno.MatriculaMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaRequisitoBD;
/*     */ import modelo.materia.MateriaRequisitoMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.JDAnularMatricula;
/*     */ 
/*     */ public class JDAnularMatriculaCTR
/*     */   extends DVtnCTR {
/*     */   private final JDAnularMatricula jd;
/*     */   private final MatriculaMD matricula;
/*  26 */   private final AlumnoCursoBD ALCBD = AlumnoCursoBD.single();
/*     */   private ArrayList<AlumnoCursoMD> almnsCurso;
/*  28 */   private final AlumnoCursoRetiradoBD ALCRBD = AlumnoCursoRetiradoBD.single(); private ArrayList<AlumnoCursoMD> almnsCursoAnular;
/*     */   private ArrayList<MateriaRequisitoMD> corequisitos;
/*  30 */   private final MateriaRequisitoBD MRBD = MateriaRequisitoBD.single();
/*  31 */   private String materiaAnular = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDAnularMatriculaCTR(VtnPrincipalCTR ctrPrin, MatriculaMD matricula) {
/*  41 */     super(ctrPrin);
/*  42 */     this.matricula = matricula;
/*  43 */     this.jd = new JDAnularMatricula((Frame)ctrPrin.getVtnPrin(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  50 */     inicarInformacion();
/*  51 */     iniciarTbls();
/*  52 */     inicarAcciones();
/*     */     
/*  54 */     iniciarJD();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inicarAcciones() {
/*  62 */     this.jd.getBtnAnular().addActionListener(e -> clickAnular());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void anunarMatricula(ArrayList<AlumnoCursoMD> almnsCursoAnular) {
/*  72 */     String observacion = JOptionPane.showInputDialog("Ingrese la razon de porque \n" + this.matricula
/*     */         
/*  74 */         .getAlumno().getNombreCompleto() + " anula la matricula de: \n" + this.materiaAnular);
/*     */     
/*  76 */     if (observacion != null) {
/*  77 */       if (Validar.esLetras(observacion)) {
/*     */         
/*  79 */         almnsCursoAnular.forEach(ac -> {
/*     */               AlumnoCursoRetiradoMD acr = new AlumnoCursoRetiradoMD();
/*     */               
/*     */               acr.setAlumnoCurso(ac);
/*     */               
/*     */               acr.setObservacion(observacion);
/*     */               this.ALCRBD.guardar(acr);
/*     */             });
/*  87 */         llenarTbl();
/*     */       } else {
/*  89 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Unicamente puede ingresar letras.");
/*  90 */         anunarMatricula(almnsCursoAnular);
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
/*     */   private void clickAnular() {
/* 103 */     this.posFila = this.jd.getTblCursos().getSelectedRow();
/* 104 */     this.almnsCursoAnular = new ArrayList<>();
/* 105 */     this.materiaAnular = "";
/* 106 */     if (this.posFila >= 0) {
/* 107 */       this.materiaAnular += ((AlumnoCursoMD)this.almnsCurso.get(this.posFila)).getCurso().getMateria().getNombre() + "\n";
/* 108 */       this.corequisitos = this.MRBD.buscarDeQueEsCorequisito(((AlumnoCursoMD)this.almnsCurso.get(this.posFila)).getCurso().getMateria().getId());
/* 109 */       if (this.corequisitos.size() > 0) {
/* 110 */         this.materiaAnular += "Con sus corequisitos: \n";
/*     */       }
/*     */       
/* 113 */       this.corequisitos.forEach(c -> this.materiaAnular += c.getMateria().getNombre() + "\n");
/*     */ 
/*     */ 
/*     */       
/* 117 */       int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Se anulara la matricula de: \n" + this.materiaAnular);
/* 118 */       if (r == 0) {
/* 119 */         this.almnsCurso.forEach(ac -> {
/*     */               for (int i = 0; i < this.corequisitos.size(); i++) {
/*     */                 if (ac.getCurso().getMateria().getId() == ((MateriaRequisitoMD)this.corequisitos.get(i)).getMateria().getId()) {
/*     */                   this.almnsCursoAnular.add(ac);
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             });
/* 127 */         this.almnsCursoAnular.add(this.almnsCurso.get(this.posFila));
/* 128 */         anunarMatricula(this.almnsCursoAnular);
/*     */       } 
/*     */     } else {
/* 131 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Seleccione una fila primero.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarTbls() {
/* 139 */     String[] t = { "Materia", "Curso" };
/* 140 */     String[][] datos = new String[0][];
/* 141 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, t);
/* 142 */     this.jd.getTblCursos().setModel(this.mdTbl);
/* 143 */     TblEstilo.formatoTbl(this.jd.getTblCursos());
/* 144 */     TblEstilo.columnaMedida(this.jd.getTblCursos(), 1, 50);
/* 145 */     llenarTbl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTbl() {
/* 152 */     this.mdTbl.setRowCount(0);
/* 153 */     this.almnsCurso = this.ALCBD.buscarCursosAlmPeriodo(this.matricula.getAlumno().getId_Alumno(), this.matricula
/* 154 */         .getPeriodo().getID());
/* 155 */     this.almnsCurso.forEach(ac -> {
/*     */           Object[] v = { ac.getCurso().getMateria().getNombre(), ac.getCurso().getNombre() };
/*     */           this.mdTbl.addRow(v);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inicarInformacion() {
/* 166 */     this.jd.getLblAlumno().setText(this.matricula.getAlumno().getNombreCompleto());
/* 167 */     this.jd.getLblPeriodo().setText(this.matricula.getPeriodo().getNombre());
/* 168 */     this.jd.getLblFecha().setText(this.matricula.getSoloFecha());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarJD() {
/* 175 */     this.jd.setVisible(true);
/* 176 */     this.jd.setLocationRelativeTo((Component)this.ctrPrin.getVtnPrin());
/* 177 */     this.jd.setTitle("Anular matricula");
/* 178 */     this.ctrPrin.eventoJDCerrar((JDialog)this.jd);
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDAnularMatriculaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */