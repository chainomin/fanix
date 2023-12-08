/*    */ package controlador.alumno;
/*    */ 
/*    */ import controlador.principal.DCTR;
/*    */ import controlador.principal.VtnPrincipalCTR;
/*    */ import java.awt.Component;
/*    */ import java.awt.Frame;
/*    */ import java.util.ArrayList;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ import modelo.alumno.AlumnoCursoMD;
/*    */ import modelo.curso.CursoBD;
/*    */ import modelo.curso.CursoMD;
/*    */ import modelo.estilo.TblEstilo;
/*    */ import vista.alumno.JDMateriasCurso;
/*    */ 
/*    */ public class JDMateriasCursoCTR
/*    */   extends DCTR
/*    */ {
/*    */   private final JDMateriasCurso jdMat;
/* 20 */   private final CursoBD CRBD = CursoBD.single();
/*    */ 
/*    */   
/*    */   private final AlumnoCursoMD almCurso;
/*    */ 
/*    */   
/*    */   private ArrayList<CursoMD> cursos;
/*    */ 
/*    */   
/*    */   private DefaultTableModel mdTbl;
/*    */ 
/*    */ 
/*    */   
/*    */   public JDMateriasCursoCTR(AlumnoCursoMD almCurso, VtnPrincipalCTR ctrPrin) {
/* 34 */     super(ctrPrin);
/* 35 */     this.almCurso = almCurso;
/* 36 */     this.jdMat = new JDMateriasCurso((Frame)ctrPrin.getVtnPrin(), false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void iniciar() {
/* 44 */     this.jdMat.setLocationRelativeTo((Component)this.ctrPrin.getVtnPrin());
/* 45 */     this.jdMat.setVisible(true);
/*    */     
/* 47 */     String[] titulo = { "Materia", "Docente" };
/* 48 */     String[][] datos = new String[0][];
/* 49 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/* 50 */     this.jdMat.getTblMaterias().setModel(this.mdTbl);
/* 51 */     TblEstilo.formatoTblConColor(this.jdMat.getTblMaterias());
/*    */     
/* 53 */     buscar();
/* 54 */     this.ctrPrin.eventoJDCerrar((JDialog)this.jdMat);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void buscar() {
/* 61 */     this.cursos = this.CRBD.buscarCursosPorAlumno(this.almCurso.getAlumno().getIdentificacion(), this.almCurso
/* 62 */         .getCurso().getNombre());
/* 63 */     llenarTbl(this.cursos);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void llenarTbl(ArrayList<CursoMD> cursos) {
/* 72 */     this.mdTbl.setRowCount(0);
/* 73 */     if (!cursos.isEmpty())
/* 74 */       cursos.forEach(c -> {
/*    */             Object[] v = { c.getMateria().getNombre(), c.getDocente().getPrimerNombre() + " " + c.getDocente().getPrimerApellido() };
/*    */             this.mdTbl.addRow(v);
/*    */           }); 
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDMateriasCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */