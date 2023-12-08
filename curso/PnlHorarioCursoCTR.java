/*     */ package controlador.curso;
/*     */ 
/*     */ import java.time.LocalTime;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.curso.SesionClaseBD;
/*     */ import modelo.curso.SesionClaseMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import vista.curso.PnlHorarioClase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PnlHorarioCursoCTR
/*     */ {
/*     */   private final PnlHorarioClase pnl;
/*     */   private final CursoMD curso;
/*  24 */   private final SesionClaseBD SCBD = SesionClaseBD.single();
/*     */   private ArrayList<SesionClaseMD> sesionLunes;
/*     */   private ArrayList<SesionClaseMD> sesionMartes;
/*     */   private ArrayList<SesionClaseMD> sesionMiercoles;
/*  28 */   private final String[][] datos = new String[0][]; private ArrayList<SesionClaseMD> sesionJueves; private ArrayList<SesionClaseMD> sesionViernes; private ArrayList<SesionClaseMD> sesionSabado; private DefaultTableModel mdTbl;
/*  29 */   private final String[] t = new String[] { "H", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
/*  30 */   private final String[] tn = new String[] { "H", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
/*  31 */   private final String[] hm = new String[] { "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00" };
/*     */   
/*  33 */   private final String[] hmc = new String[] { "<html>07:00<br>08:00</html>", "<html>08:00<br>09:00</html>", "<html>09:00<br>10:00</html>", "<html>10:00<br>11:00</html>", "<html>11:00<br>12:00</html>", "<html>12:00<br>13:00</html>", "<html>13:00<br>14:00</html>", "<html>14:00<br>15:00</html>", "<html>15:00<br>16:00</html>", "<html>16:00<br>17:00</html>", "<html>17:00<br>18:00</html>", "<html>18:00<br>19:00</html>", "<html>19:00<br>20:00</html>", "<html>20:00<br>21:00</html>", "<html>21:00<br>22:00</html>" };
/*     */   
/*     */   private String[] hSelec;
/*     */   
/*     */   private String[] jSelec;
/*     */   
/*     */   private String[] tSelec;
/*     */   
/*     */   private int posI;
/*     */   
/*     */   private int posF;
/*     */   
/*     */   private int posFila;
/*     */   
/*     */   private int posColum;
/*     */   
/*     */   private String horaString;
/*     */   private String minutoString;
/*     */   
/*     */   public PnlHorarioCursoCTR(PnlHorarioClase pnl, CursoMD curso) {
/*  53 */     this.pnl = pnl;
/*  54 */     this.curso = curso;
/*     */   }
/*     */   
/*     */   public PnlHorarioCursoCTR(PnlHorarioClase pnl, String nomCurso, int idPrd) {
/*  58 */     this.pnl = pnl;
/*  59 */     this.curso = new CursoMD();
/*  60 */     this.curso.setNombre(nomCurso);
/*     */     
/*  62 */     PeriodoLectivoMD p = new PeriodoLectivoMD();
/*  63 */     p.setID(idPrd);
/*  64 */     this.curso.setPeriodo(p);
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  68 */     iniciaTbl();
/*  69 */     eliminarFilasSinDatos();
/*  70 */     eliminarColumnasSinDatos();
/*     */   }
/*     */   
/*     */   private void iniciaTbl() {
/*  74 */     switch (this.curso.getNombre().charAt(0)) {
/*     */       case 'M':
/*  76 */         this.mdTbl = TblEstilo.modelTblSinEditar(this.datos, this.t);
/*  77 */         formatoTbl(this.pnl.getTblHorario());
/*  78 */         llenarHoras(this.hmc);
/*  79 */         this.hSelec = this.hm;
/*  80 */         this.jSelec = this.t;
/*  81 */         llenarLunesSabado();
/*     */         return;
/*     */       case 'V':
/*  84 */         this.mdTbl = TblEstilo.modelTblSinEditar(this.datos, this.t);
/*  85 */         formatoTbl(this.pnl.getTblHorario());
/*  86 */         llenarHoras(this.hmc);
/*  87 */         this.hSelec = this.hm;
/*  88 */         this.jSelec = this.t;
/*  89 */         llenarLunesSabado();
/*     */         return;
/*     */       case 'N':
/*  92 */         this.mdTbl = TblEstilo.modelTblSinEditar(this.datos, this.tn);
/*  93 */         formatoTbl(this.pnl.getTblHorario());
/*  94 */         llenarHoras(this.hmc);
/*  95 */         this.hSelec = this.hm;
/*  96 */         this.jSelec = this.tn;
/*  97 */         llenarLunesSabado();
/*     */         return;
/*     */     } 
/* 100 */     this.mdTbl = TblEstilo.modelTblSinEditar(this.datos, this.tn);
/* 101 */     formatoTbl(this.pnl.getTblHorario());
/* 102 */     llenarHoras(this.hmc);
/* 103 */     this.hSelec = this.hm;
/* 104 */     this.jSelec = this.tn;
/* 105 */     llenarLunesSabado();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarLunesViernes() {
/* 111 */     actualizarLunes();
/* 112 */     actualizarMartes();
/* 113 */     actualizarMiercoles();
/* 114 */     actuatizarJueves();
/* 115 */     actualizarViernes();
/*     */   }
/*     */   
/*     */   private void llenarLunesSabado() {
/* 119 */     llenarLunesViernes();
/* 120 */     actualizarSabado();
/*     */   }
/*     */   
/*     */   private void formatoTbl(JTable tbl) {
/* 124 */     tbl.setModel(this.mdTbl);
/*     */     
/* 126 */     TblEstilo.formatoTblHCurso(tbl);
/*     */   }
/*     */   
/*     */   private void llenarHoras(String[] horas) {
/* 130 */     for (String h : horas) {
/* 131 */       Object[] v = { h };
/* 132 */       this.mdTbl.addRow(v);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarDia(ArrayList<SesionClaseMD> sesiones, int dia) {
/* 137 */     if (sesiones != null) {
/* 138 */       sesiones.forEach(s -> buscarClm(s, dia));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void buscarClm(SesionClaseMD s, int dia) {
/*     */     int i;
/* 145 */     for (i = 0; i < this.hSelec.length; i++) {
/* 146 */       if (this.hSelec[i].equals(tranformar(s.getHoraIni()))) {
/* 147 */         this.posI = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 152 */     for (i = 0; i < this.hSelec.length; i++) {
/* 153 */       if (this.hSelec[i].equals(tranformar(s.getHoraFin()))) {
/* 154 */         this.posF = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 159 */     for (i = this.posI; i < this.posF; i++) {
/* 160 */       this.mdTbl.setValueAt("<html> <center>" + s.getId() + "" + s.getCurso().getId() + "<br>" + s
/* 161 */           .getCurso().getMateria().getNombre() + "<br>" + s
/* 162 */           .getCurso().getDocente().getNombreCorto() + "</center></html>", i, dia);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String tranformar(LocalTime hora) {
/* 171 */     if (hora.getHour() < 10) {
/* 172 */       this.horaString = "0" + hora.getHour();
/*     */     } else {
/* 174 */       this.horaString = "" + hora.getHour();
/*     */     } 
/*     */     
/* 177 */     if (hora.getMinute() < 10) {
/* 178 */       this.minutoString = "0" + hora.getMinute();
/*     */     } else {
/* 180 */       this.minutoString = "" + hora.getMinute();
/*     */     } 
/* 182 */     return this.horaString + ":" + this.minutoString;
/*     */   }
/*     */ 
/*     */   
/*     */   public void actualizarLunes() {
/* 187 */     this.sesionLunes = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 1, this.curso
/* 188 */         .getPeriodo().getID());
/* 189 */     llenarDia(this.sesionLunes, 1);
/*     */   }
/*     */   
/*     */   public void actualizarMartes() {
/* 193 */     this.sesionMartes = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 2, this.curso
/* 194 */         .getPeriodo().getID());
/* 195 */     llenarDia(this.sesionMartes, 2);
/*     */   }
/*     */   
/*     */   public void actualizarMiercoles() {
/* 199 */     this.sesionMiercoles = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 3, this.curso
/* 200 */         .getPeriodo().getID());
/* 201 */     llenarDia(this.sesionMiercoles, 3);
/*     */   }
/*     */   
/*     */   public void actuatizarJueves() {
/* 205 */     this.sesionJueves = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 4, this.curso
/* 206 */         .getPeriodo().getID());
/* 207 */     llenarDia(this.sesionJueves, 4);
/*     */   }
/*     */   
/*     */   public void actualizarViernes() {
/* 211 */     this.sesionViernes = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 5, this.curso
/* 212 */         .getPeriodo().getID());
/* 213 */     llenarDia(this.sesionViernes, 5);
/*     */   }
/*     */   
/*     */   public void actualizarSabado() {
/* 217 */     this.sesionSabado = this.SCBD.cargarHorarioCursoPorDia(this.curso.getNombre(), 6, this.curso
/* 218 */         .getPeriodo().getID());
/* 219 */     llenarDia(this.sesionSabado, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   private void eliminarFilasSinDatos() {
/* 224 */     for (int i = 0; i < this.mdTbl.getRowCount(); i++) {
/* 225 */       boolean borrar = true;
/* 226 */       for (int j = 1; j < this.mdTbl.getColumnCount(); j++) {
/* 227 */         if (this.mdTbl.getValueAt(i, j) != null) {
/* 228 */           borrar = false;
/*     */         }
/*     */       } 
/* 231 */       if (borrar) {
/* 232 */         this.mdTbl.removeRow(i);
/* 233 */         eliminarFilasSinDatos();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void eliminarColumnasSinDatos() {
/* 240 */     for (int i = 1; i < this.pnl.getTblHorario().getColumnCount(); i++) {
/* 241 */       boolean borrar = true;
/* 242 */       for (int j = 0; j < this.mdTbl.getRowCount(); j++) {
/* 243 */         if (this.mdTbl.getValueAt(j, i) != null) {
/* 244 */           borrar = false;
/*     */         }
/*     */       } 
/* 247 */       if (borrar) {
/* 248 */         TableColumnModel tcm = this.pnl.getTblHorario().getColumnModel();
/* 249 */         TableColumn cb = tcm.getColumn(i);
/* 250 */         this.pnl.getTblHorario().removeColumn(cb);
/* 251 */         eliminarColumnasSinDatos();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\curso\PnlHorarioCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */