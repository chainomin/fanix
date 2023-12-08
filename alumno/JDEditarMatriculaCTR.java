/*     */ package controlador.alumno;
/*     */ 
/*     */ import controlador.estilo.TblRenderMatricula;
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import modelo.alumno.AlumnoCursoBD;
/*     */ import modelo.alumno.AlumnoCursoMD;
/*     */ import modelo.alumno.MatriculaMD;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.alumno.JDEditarMatricula;
/*     */ 
/*     */ public class JDEditarMatriculaCTR extends DVtnCTR {
/*     */   private final JDEditarMatricula jd;
/*     */   private final MatriculaMD matricula;
/*  26 */   private final CursoBD CRBD = CursoBD.single(); private String nombreCurso; private ArrayList<String> nomCursos;
/*  27 */   private final AlumnoCursoBD ACRBD = AlumnoCursoBD.single();
/*     */   
/*     */   private ArrayList<AlumnoCursoMD> almnsCurso;
/*  30 */   private ArrayList<AlumnoCursoMD> cursosNuevos = null; private ArrayList<CursoMD> cursos;
/*     */   private DefaultTableModel mdTblA;
/*     */   private DefaultTableModel mdTblN;
/*  33 */   private String nombreCursosN = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int numMat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDEditarMatriculaCTR(VtnPrincipalCTR ctrPrin, MatriculaMD matricula) {
/*  45 */     super(ctrPrin);
/*  46 */     this.cursosNuevos = new ArrayList<>();
/*  47 */     this.matricula = matricula;
/*  48 */     this.jd = new JDEditarMatricula((Frame)ctrPrin.getVtnPrin(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  55 */     iniciarTbls();
/*  56 */     inicarInformacion();
/*  57 */     inicarAcciones();
/*  58 */     iniciarJD();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickRemover() {
/*  65 */     int[] selecs = this.jd.getTblClasesNuevas().getSelectedRows();
/*  66 */     this.posFila = this.jd.getTblClasesNuevas().getSelectedRow();
/*  67 */     if (this.posFila >= 0) {
/*     */       
/*  69 */       ArrayList<AlumnoCursoMD> acx = new ArrayList<>();
/*  70 */       for (int i = 0; i < this.cursosNuevos.size(); i++) {
/*  71 */         boolean mantener = true;
/*  72 */         for (int s : selecs) {
/*  73 */           if (i == s) {
/*  74 */             mantener = false;
/*     */             break;
/*     */           } 
/*     */         } 
/*  78 */         if (mantener) {
/*  79 */           acx.add(this.cursosNuevos.get(i));
/*     */         }
/*     */       } 
/*  82 */       this.cursosNuevos = acx;
/*  83 */       llenarTblMN(this.cursosNuevos);
/*     */     } else {
/*  85 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una fila o mas filas.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickGuardar() {
/*  94 */     if (this.cursosNuevos != null) {
/*  95 */       this.ACRBD.borrarActualizarMatricula();
/*  96 */       this.nombreCursosN = "";
/*  97 */       this.numMat = 1;
/*  98 */       this.cursosNuevos.forEach(ac -> {
/*     */             if (ac.getCurso().getCapaciadActual() > 0) {
/*     */               this.ACRBD.agregarUpdate(ac.getId(), ac.getCurso().getId());
/*     */ 
/*     */               
/*     */               this.nombreCursosN += this.numMat + ": " + ac.getCurso().getMateria().getNombre() + "   Curso: " + ac.getCurso().getNombre() + "   \n";
/*     */               
/*     */               this.numMat++;
/*     */             } 
/*     */           });
/*     */       
/* 109 */       int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Se editara la matricula de: " + this.matricula
/* 110 */           .getAlumno().getNombreCompleto() + "   \nEstos son sus nuevos cursos: \n" + this.nombreCursosN);
/*     */       
/* 112 */       if (r == 0 && 
/* 113 */         this.ACRBD.actualizarMatricula()) {
/* 114 */         this.ctrPrin.getVtnPrin().setEnabled(true);
/* 115 */         this.jd.dispose();
/* 116 */         this.ACRBD.borrarActualizarMatricula();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cambiarACurso(String curso) {
/* 133 */     this.cursos = this.CRBD.buscarCursosPorNombreYPrdLectivo(curso, this.matricula
/*     */         
/* 135 */         .getPeriodo().getID());
/*     */     
/* 137 */     int[] selecs = this.jd.getTblClasesActuales().getSelectedRows();
/* 138 */     for (int s : selecs) {
/* 139 */       for (int i = 0; i < this.cursos.size(); i++) {
/* 140 */         if (((AlumnoCursoMD)this.almnsCurso.get(s)).getCurso().getMateria().getId() == ((CursoMD)this.cursos.get(i)).getMateria().getId()) {
/* 141 */           borrarSiExisteCurso(this.cursos.get(i));
/*     */           
/* 143 */           AlumnoCursoMD ac = new AlumnoCursoMD();
/* 144 */           ac.setId(((AlumnoCursoMD)this.almnsCurso.get(s)).getId());
/* 145 */           ac.setCurso(this.cursos.get(i));
/* 146 */           this.cursosNuevos.add(ac);
/*     */           
/* 148 */           llenarTblMN(this.cursosNuevos);
/*     */           break;
/*     */         } 
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
/*     */   private void borrarSiExisteCurso(CursoMD curso) {
/* 162 */     for (int i = 0; i < this.cursosNuevos.size(); i++) {
/* 163 */       if (curso.getMateria().getId() == ((AlumnoCursoMD)this.cursosNuevos.get(i)).getCurso().getMateria().getId()) {
/* 164 */         this.cursosNuevos.remove(i);
/*     */         break;
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
/*     */   private void llenarCursosDisponibles(int ciclo) {
/* 177 */     this.nomCursos = this.CRBD.cargarNombreCursosPorPeriodoCiclo(this.matricula.getPeriodo().getID(), ciclo);
/*     */     
/* 179 */     ArrayList<String> nomAux = new ArrayList<>();
/*     */     
/* 181 */     nomAux.add("Seleccione");
/* 182 */     this.nomCursos.forEach(c -> {
/*     */           if (!c.equals(this.nombreCurso)) {
/*     */             nomAux.add(c);
/*     */           }
/*     */         });
/* 187 */     this.nomCursos = nomAux;
/*     */     
/* 189 */     Object np = JOptionPane.showInputDialog((Component)this.ctrPrin.getVtnPrin(), "Lista de cursos disponibles", "Cursos", 3, null, this.nomCursos
/*     */ 
/*     */         
/* 192 */         .toArray(), "Seleccione");
/* 193 */     if (np != null) {
/* 194 */       if (np.toString().equals("Seleccione")) {
/* 195 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar un curso.");
/* 196 */         llenarCursosDisponibles(ciclo);
/*     */       } else {
/* 198 */         cambiarACurso(np.toString());
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
/*     */   private void clickCambiar() {
/* 210 */     boolean mismoCiclo = true;
/* 211 */     this.posFila = this.jd.getTblClasesActuales().getSelectedRow();
/* 212 */     if (this.posFila >= 0) {
/* 213 */       int ciclo = ((AlumnoCursoMD)this.almnsCurso.get(this.posFila)).getCurso().getCiclo();
/* 214 */       this.nombreCurso = ((AlumnoCursoMD)this.almnsCurso.get(this.posFila)).getCurso().getNombre();
/* 215 */       int[] selecs = this.jd.getTblClasesActuales().getSelectedRows();
/*     */       
/* 217 */       for (int s : selecs) {
/* 218 */         if (((AlumnoCursoMD)this.almnsCurso.get(s)).getCurso().getCiclo() != ciclo || 
/* 219 */           !((AlumnoCursoMD)this.almnsCurso.get(s)).getCurso().getNombre().equals(this.nombreCurso)) {
/* 220 */           mismoCiclo = false;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 225 */       if (mismoCiclo) {
/* 226 */         llenarCursosDisponibles(ciclo);
/*     */       } else {
/* 228 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar clases del mismo ciclo o la misma jornada.");
/*     */       } 
/*     */     } else {
/* 231 */       JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar una o mas filas.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inicarAcciones() {
/* 240 */     this.jd.getBtnCambiar().addActionListener(e -> clickCambiar());
/* 241 */     this.jd.getBtnGuardar().addActionListener(e -> clickGuardar());
/* 242 */     this.jd.getBtnRemover().addActionListener(e -> clickRemover());
/* 243 */     this.jd.getBtnEditarNumMatricula().addActionListener(e -> editarNumMatricula());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTblMA(ArrayList<AlumnoCursoMD> almnsCurso) {
/* 250 */     if (almnsCurso != null) {
/* 251 */       almnsCurso.forEach(ac -> {
/*     */             Object[] v = { ac.getCurso().getMateria().getNombre(), ac.getCurso().getNombre() };
/*     */             this.mdTblA.addRow(v);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarTblMN(ArrayList<AlumnoCursoMD> cursosNuevos) {
/* 263 */     this.mdTblN.setRowCount(0);
/* 264 */     if (cursosNuevos != null) {
/* 265 */       cursosNuevos.forEach(ac -> {
/*     */             Object[] v = { ac.getCurso().getMateria().getNombre(), Integer.valueOf(ac.getCurso().getCapaciadActual()), ac.getCurso().getNombre() };
/*     */             this.mdTblN.addRow(v);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarTbls() {
/* 278 */     String[] t = { "Materia", "Curso" };
/* 279 */     String[] t2 = { "Materia", "M", "C" };
/* 280 */     String[][] datos = new String[0][];
/* 281 */     this.mdTblA = TblEstilo.modelTblSinEditar(datos, t);
/* 282 */     this.jd.getTblClasesActuales().setModel(this.mdTblA);
/* 283 */     TblEstilo.formatoTblMultipleSelec(this.jd.getTblClasesActuales());
/* 284 */     TblEstilo.columnaMedida(this.jd.getTblClasesActuales(), 1, 50);
/*     */     
/* 286 */     this.mdTblN = TblEstilo.modelTblSinEditar(datos, t2);
/* 287 */     TblEstilo.formatoTblMultipleSelec(this.jd.getTblClasesNuevas());
/* 288 */     this.jd.getTblClasesNuevas().setModel(this.mdTblN);
/* 289 */     TblEstilo.columnaMedida(this.jd.getTblClasesNuevas(), 1, 50);
/* 290 */     TblEstilo.columnaMedida(this.jd.getTblClasesNuevas(), 2, 50);
/*     */     
/* 292 */     this.jd.getTblClasesNuevas().getColumnModel().getColumn(1).setCellRenderer((TableCellRenderer)new TblRenderMatricula(1));
/* 293 */     this.jd.getTblClasesNuevas().getColumnModel().getColumn(2).setCellRenderer((TableCellRenderer)new TblRenderMatricula(2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inicarInformacion() {
/* 300 */     this.jd.getLblAlumno().setText(this.matricula.getAlumno().getNombreCompleto());
/* 301 */     this.jd.getLblPeriodo().setText(this.matricula.getPeriodo().getNombre());
/* 302 */     this.jd.getLblFecha().setText(this.matricula.getSoloFecha());
/* 303 */     this.almnsCurso = this.ACRBD.buscarCursosAlmPeriodo(this.matricula.getAlumno().getId_Alumno(), this.matricula
/* 304 */         .getPeriodo().getID());
/* 305 */     llenarTblMA(this.almnsCurso);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iniciarJD() {
/* 312 */     this.jd.setVisible(true);
/* 313 */     this.jd.setLocationRelativeTo((Component)this.ctrPrin.getVtnPrin());
/* 314 */     this.jd.setTitle("Editar matricula");
/* 315 */     this.ctrPrin.eventoJDCerrar((JDialog)this.jd);
/*     */   }
/*     */   
/*     */   private void editarNumMatricula() {
/* 319 */     this.posFila = this.jd.getTblClasesActuales().getSelectedRow();
/* 320 */     if (this.posFila >= 0) {
/* 321 */       int id = ((AlumnoCursoMD)this.almnsCurso.get(this.posFila)).getId();
/* 322 */       Object e = JOptionPane.showInputDialog((Component)this.jd, "Modificara el número de matricula de: \n" + ((AlumnoCursoMD)this.almnsCurso
/* 323 */           .get(this.posFila)).getCurso().getMateria().getNombre());
/* 324 */       if (Validar.esNumeros(e.toString())) {
/* 325 */         int num = Integer.parseInt(e.toString());
/* 326 */         if (num > 0 && num < 4) {
/* 327 */           this.ACRBD.editarNumMatricula(id, num);
/*     */         } else {
/* 329 */           JOptionPane.showMessageDialog((Component)this.jd, "El numero de matricula no puede ser \nmenor a 1 ni mayor a 3.");
/*     */           
/* 331 */           editarNumMatricula();
/*     */         } 
/*     */       } else {
/* 334 */         JOptionPane.showMessageDialog((Component)this.jd, "Solo debe ingresar numeros.");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\JDEditarMatriculaCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */