/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.alumno.AlumnoMatriculaBD;
/*     */ import modelo.alumno.EgresadoBD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import utils.ToExcel;
/*     */ import vista.alumno.JDReporteExcel;
/*     */ 
/*     */ 
/*     */ public class JDReporteTipoMatriculaCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final List<PeriodoLectivoMD> periodos;
/*  26 */   private final DefaultTableModel mdTbl = TblEstilo.modelTblSinEditar(new String[] { "Periodo" });
/*     */   
/*     */   private final JDReporteExcel vtn;
/*     */   
/*  30 */   private final String[] TIPO_MATRICULAS = new String[] { "TODAS", "ORDINARIA", "EXTRAORDINARIA", "ESPECIAL" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private final AlumnoMatriculaBD AMBD = AlumnoMatriculaBD.single();
/*  37 */   private final EgresadoBD EBD = EgresadoBD.single();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDReporteTipoMatriculaCTR(VtnPrincipalCTR ctrPrin, List<PeriodoLectivoMD> periodos) {
/*  43 */     super(ctrPrin);
/*  44 */     this.periodos = periodos;
/*  45 */     this.vtn = new JDReporteExcel((Frame)ctrPrin.getVtnPrin(), false);
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  49 */     iniciarTbl();
/*  50 */     this.vtn.getBtnReporte().addActionListener(e -> seleccioneTipoMatricula());
/*  51 */     this.vtn.getBtnEgresados().addActionListener(e -> clickReporteEgresados());
/*  52 */     this.vtn.getBtnNumAlumnos().addActionListener(e -> clickReporteNumeroAlumnos());
/*  53 */     this.vtn.getBtnNumAlmnJornada().addActionListener(e -> clickReporteNumeroAlumnosJornada());
/*  54 */     this.vtn.setLocationRelativeTo((Component)this.ctrPrin.getVtnPrin());
/*  55 */     this.vtn.setVisible(true);
/*  56 */     this.ctrPrin.eventoJDCerrar((JDialog)this.vtn);
/*     */   }
/*     */   
/*     */   private void iniciarTbl() {
/*  60 */     this.vtn.getTblPeriodos().setModel(this.mdTbl);
/*  61 */     this.periodos.forEach(p -> this.mdTbl.addRow(new Object[] { p.getNombre() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seleccioneTipoMatricula() {
/*  69 */     Object np = JOptionPane.showInputDialog(null, "Seleccione el tipo de matricula", "Tipos de matricula", 3, null, (Object[])this.TIPO_MATRICULAS, "TODAS");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (np != null)
/*     */     {
/*  81 */       if (np.equals("Seleccione")) {
/*     */ 
/*     */         
/*  84 */         reporteJSON("");
/*     */       } else {
/*  86 */         reporteJSON(np.toString());
/*     */       }  } 
/*     */   }
/*     */   
/*     */   private void reporteJSON(String tipo) {
/*  91 */     String ids = getIDSelect();
/*     */ 
/*     */     
/*  94 */     String nombre = tipo + LocalDate.now().toString().replace(":", "|").replace(".", "");
/*     */     
/*  96 */     List<List<String>> alumnos = this.AMBD.getPorTipoMatricula(ids, tipo);
/*  97 */     List<String> cols = new ArrayList<>();
/*  98 */     cols.add("Cedula/Identificacion");
/*  99 */     cols.add("Primer Nombre");
/* 100 */     cols.add("Seguno Nombre");
/* 101 */     cols.add("Primer Apellido");
/* 102 */     cols.add("Segundo Apellido");
/* 103 */     cols.add("Fecha Matricula");
/* 104 */     cols.add("Tipo Matricula");
/* 105 */     ToExcel excel = new ToExcel();
/* 106 */     excel.exportarExcel(cols, alumnos, nombre);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickReporteEgresados() {
/* 115 */     String ids = getIDSelect();
/* 116 */     List<List<String>> alumnos = this.EBD.getReportesEgresadosExcel(ids);
/* 117 */     List<String> cols = new ArrayList<>();
/* 118 */     cols.add("CÓDIGO DEL IST");
/* 119 */     cols.add("NOMBRE DEL INSTITUTO");
/* 120 */     cols.add("PROVINCIA");
/* 121 */     cols.add("CÓDIGO DE LA CARRERA");
/* 122 */     cols.add("CARRERA");
/* 123 */     cols.add("MODALIDAD DE ESTUDIOS");
/* 124 */     cols.add("TIPO DE IDENTIFICACIÓN");
/* 125 */     cols.add("NRO. DE IDENTIFICACIÓN");
/* 126 */     cols.add("APELLIDOS Y NOMBRES");
/* 127 */     cols.add("NACIONALIDAD");
/* 128 */     cols.add("TRABAJO DE TITULACIÓN FINALIZADO S/N");
/* 129 */     ToExcel excel = new ToExcel();
/* 130 */     excel.exportarExcel(cols, alumnos, "Alumnos Egresados " + ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickReporteNumeroAlumnos() {
/* 138 */     String ids = getIDSelect();
/* 139 */     List<List<String>> alumnos = this.AMBD.getNumeroAlumnos(ids);
/* 140 */     List<String> cols = new ArrayList<>();
/* 141 */     cols.add("CARRERA");
/* 142 */     cols.add("PERIODO");
/* 143 */     cols.add("HOMBRES");
/* 144 */     cols.add("MUJERES");
/* 145 */     cols.add("TOTAL");
/* 146 */     ToExcel excel = new ToExcel();
/* 147 */     excel.exportarExcel(cols, alumnos, "Número Alumnos " + ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickReporteNumeroAlumnosJornada() {
/* 155 */     String ids = getIDSelect();
/* 156 */     List<List<String>> alumnos = this.AMBD.getNumeroAlumnosPorJornada(ids);
/* 157 */     List<String> cols = new ArrayList<>();
/* 158 */     cols.add("CARRERA");
/* 159 */     cols.add("PERIODO");
/* 160 */     cols.add("CURSOS MATUTINA");
/* 161 */     cols.add("HOMBRES MATUTINA");
/* 162 */     cols.add("MUJERES MATUTINA");
/* 163 */     cols.add("ALUMNOS MATUTINA");
/* 164 */     cols.add("CURSOS VESPERTINA");
/* 165 */     cols.add("HOMBRES VESPERTINA");
/* 166 */     cols.add("MUJERES VESPERTINA");
/* 167 */     cols.add("ALUMNOS VESPERTINA");
/* 168 */     cols.add("CURSOS NOCTURNA");
/* 169 */     cols.add("HOMBRES NOCTURNA");
/* 170 */     cols.add("MUJERES NOCTURNA");
/* 171 */     cols.add("ALUMNOS NOCTURNA");
/* 172 */     ToExcel excel = new ToExcel();
/* 173 */     excel.exportarExcel(cols, alumnos, "Número Alumnos Jornada " + ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getIDSelect() {
/* 181 */     int[] ss = this.vtn.getTblPeriodos().getSelectedRows();
/* 182 */     String ids = "";
/* 183 */     for (int s : ss) {
/* 184 */       ids = ids + ((PeriodoLectivoMD)this.periodos.get(s)).getID() + ",";
/*     */     }
/* 186 */     ids = ids.substring(0, ids.length() - 1);
/* 187 */     return ids;
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDReporteTipoMatriculaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */