/*    */ package controlador.curso;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import javax.swing.JTable;
/*    */ import javax.swing.table.DefaultTableModel;
/*    */ import modelo.curso.CursoMD;
/*    */ import modelo.curso.SesionClaseBD;
/*    */ import modelo.curso.SesionClaseMD;
/*    */ import modelo.estilo.TblEstilo;
/*    */ import utils.CONS;
/*    */ import vista.curso.PnlHorarioClase;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PnlHorarioClaseCTR
/*    */ {
/*    */   private final PnlHorarioClase pnl;
/*    */   private final CursoMD curso;
/*    */   private final SesionClaseBD bd;
/*    */   private ArrayList<SesionClaseMD> sesiones;
/*    */   private DefaultTableModel mdTbl;
/* 24 */   private final String[][] datos = new String[0][];
/* 25 */   private final String[] t = new String[] { "Dia", "Hora Inicia", "Hora Fin", "Clase" };
/*    */   
/* 27 */   private final String[] hm = new String[] { "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00" };
/*    */   private String[] hSelec;
/*    */   private String[] jSelec;
/*    */   private String[] tSelec;
/*    */   
/*    */   public PnlHorarioClaseCTR(PnlHorarioClase pnl, CursoMD curso, SesionClaseBD bd) {
/* 33 */     this.pnl = pnl;
/* 34 */     this.curso = curso;
/* 35 */     this.bd = bd;
/*    */   }
/*    */   
/*    */   public void iniciar() {
/* 39 */     iniciaTbl();
/*    */   }
/*    */   
/*    */   private void iniciaTbl() {
/* 43 */     this.mdTbl = TblEstilo.modelTblSinEditar(this.datos, this.t);
/* 44 */     formatoTbl(this.pnl.getTblHorario());
/* 45 */     this.hSelec = this.hm;
/* 46 */     this.jSelec = this.t;
/* 47 */     llenarHorarios();
/*    */   }
/*    */   
/*    */   private void llenarHorarios() {
/* 51 */     this.mdTbl.setRowCount(0);
/* 52 */     this.sesiones = this.bd.cargarHorarioCurso(this.curso);
/* 53 */     if (this.sesiones != null) {
/* 54 */       System.out.println("---------");
/* 55 */       this.sesiones.forEach(s -> {
/*    */             System.out.println("Dia: " + s.getDia() + "  Horas: " + s.getHoraIni() + "    " + s.getHoraFin());
/*    */ 
/*    */             
/*    */             Object[] v = { CONS.getDia(s.getDia()), s.getHoraIni(), s.getHoraFin(), s.getId() + "%Clase \nEditar | Eliminar" };
/*    */             
/*    */             this.mdTbl.addRow(v);
/*    */           });
/*    */       
/* 64 */       System.out.println("---------");
/*    */     } 
/*    */   }
/*    */   
/*    */   private void formatoTbl(JTable tbl) {
/* 69 */     tbl.setModel(this.mdTbl);
/*    */     
/* 71 */     TblEstilo.formatoTblFocus(tbl);
/*    */   }
/*    */   
/*    */   public String[] gethSelec() {
/* 75 */     return this.hSelec;
/*    */   }
/*    */   
/*    */   public String[] getjSelec() {
/* 79 */     return this.jSelec;
/*    */   }
/*    */   
/*    */   public String[] gettSelec() {
/* 83 */     return this.tSelec;
/*    */   }
/*    */   
/*    */   public void actualizar(int dia) {
/* 87 */     llenarHorarios();
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\curso\PnlHorarioClaseCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */