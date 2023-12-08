/*     */ package controlador.alumno;
/*     */ 
/*     */ import java.time.LocalTime;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.curso.SesionClaseMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import vista.curso.PnlHorarioClase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PnlHorarioAlmnCTR
/*     */ {
/*  17 */   private final String[] hm = new String[] { "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00" };
/*     */   
/*  19 */   private final String[] hmc = new String[] { "<html>07:00<br>08:00</html>", "<html>08:00<br>09:00</html>", "<html>09:00<br>10:00</html>", "<html>10:00<br>11:00</html>", "<html>11:00<br>12:00</html>", "<html>12:00<br>13:00</html>", "---------------------------", "<html>14:00<br>15:00</html>", "<html>15:00<br>16:00</html>", "<html>16:00<br>17:00</html>", "<html>17:00<br>18:00</html>", "<html>18:00<br>19:00</html>", "<html>19:00<br>20:00</html>", "<html>20:00<br>21:00</html>", "<html>21:00<br>22:00</html>" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultTableModel mdTbl;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ArrayList<SesionClaseMD> sesiones;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PnlHorarioClase pnl;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PnlHorarioAlmnCTR(ArrayList<SesionClaseMD> sesion, PnlHorarioClase pnl) {
/*  40 */     this.sesiones = sesion;
/*  41 */     this.pnl = pnl;
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  45 */     iniciaTbl();
/*     */     
/*  47 */     llenarTbl(this.sesiones);
/*     */   }
/*     */   
/*     */   private void iniciaTbl() {
/*  51 */     String[][] datos = new String[0][];
/*  52 */     String[] t = { "H", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
/*     */     
/*  54 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, t);
/*  55 */     formatoTbl(this.pnl.getTblHorario());
/*  56 */     llenarHoras(this.hmc);
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatoTbl(JTable tbl) {
/*  61 */     tbl.setModel(this.mdTbl);
/*     */     
/*  63 */     TblEstilo.formatoTblHCurso(tbl);
/*     */   }
/*     */   
/*     */   private void llenarHoras(String[] horas) {
/*  67 */     for (String h : horas) {
/*  68 */       Object[] v = { h };
/*  69 */       this.mdTbl.addRow(v);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarTbl(ArrayList<SesionClaseMD> sesiones) {
/*  74 */     if (sesiones != null) {
/*  75 */       sesiones.forEach(s -> buscarClm(s));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void buscarClm(SesionClaseMD s) {
/*  82 */     int posI = -1, posF = -1;
/*  83 */     int dia = s.getDia();
/*     */     int i;
/*  85 */     for (i = 0; i < this.hm.length; i++) {
/*  86 */       if (this.hm[i].equals(tranformar(s.getHoraIni()))) {
/*  87 */         posI = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  92 */     for (i = 0; i < this.hm.length; i++) {
/*  93 */       if (this.hm[i].equals(tranformar(s.getHoraFin()))) {
/*  94 */         posF = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  99 */     if (posI >= 0 && posF >= 0) {
/* 100 */       for (i = posI; i < posF; i++) {
/* 101 */         this.mdTbl.setValueAt("<html> <center>" + s.getId() + "" + s.getCurso().getId() + " | " + s
/* 102 */             .getCurso().getCapacidad() + "<br>" + s
/* 103 */             .getCurso().getMateria().getNombre() + "</center></html>", i, dia);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private String tranformar(LocalTime hora) {
/* 110 */     return hora.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\PnlHorarioAlmnCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */