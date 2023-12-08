/*     */ package controlador.curso;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.event.InternalFrameAdapter;
/*     */ import javax.swing.event.InternalFrameEvent;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.jornada.JornadaBD;
/*     */ import modelo.jornada.JornadaMD;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.curso.FrmCurso;
/*     */ 
/*     */ 
/*     */ public class FrmCursoCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final FrmCurso frmCurso;
/*     */   private final VtnCursoCTR ctrCurso;
/*  30 */   private final CursoBD CBD = CursoBD.single();
/*     */   
/*     */   private boolean editando = false;
/*  33 */   private int idCurso = 0;
/*     */ 
/*     */   
/*  36 */   private final DocenteBD DBD = DocenteBD.single();
/*     */   
/*     */   private ArrayList<DocenteMD> docentes;
/*  39 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*     */   
/*     */   private ArrayList<PeriodoLectivoMD> periodos;
/*  42 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */   
/*     */   private ArrayList<MateriaMD> materias;
/*  45 */   private final JornadaBD JBD = JornadaBD.single();
/*     */   
/*     */   private List<JornadaMD> jornadas;
/*     */   private ArrayList<Integer> ciclos;
/*     */   
/*     */   public FrmCursoCTR(FrmCurso frmCurso, VtnPrincipalCTR ctrPrin) {
/*  51 */     super(ctrPrin);
/*  52 */     this.frmCurso = frmCurso;
/*  53 */     this.ctrCurso = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public FrmCursoCTR(FrmCurso frmCurso, VtnPrincipalCTR ctrPrin, VtnCursoCTR ctrCurso) {
/*  58 */     super(ctrPrin);
/*  59 */     this.frmCurso = frmCurso;
/*  60 */     this.ctrCurso = ctrCurso;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  68 */     ocultarErrores();
/*     */     
/*  70 */     cargarCmbPrdLectivo();
/*  71 */     actualizarCmbMaterias();
/*  72 */     actualizarCmbDocentes();
/*  73 */     cargarCmbJornada();
/*     */     
/*  75 */     this.frmCurso.getCbxPeriodoLectivo().addActionListener(e -> clickCmbPrd());
/*  76 */     this.frmCurso.getCbxCiclo().addActionListener(e -> actualizarCmbMaterias());
/*  77 */     this.frmCurso.getCbxMateria().addActionListener(e -> actualizarCmbDocentes());
/*     */     
/*  79 */     this.frmCurso.getBtnGuardar().addActionListener(e -> guardarYSalir());
/*  80 */     this.frmCurso.getBtnGuardarContinuar().addActionListener(e -> guardarSeguirIngresando());
/*     */ 
/*     */     
/*  83 */     this.frmCurso.addInternalFrameListener(new InternalFrameAdapter()
/*     */         {
/*     */           public void internalFrameClosed(InternalFrameEvent e) {
/*  86 */             if (FrmCursoCTR.this.ctrCurso != null) {
/*  87 */               FrmCursoCTR.this.ctrCurso.actualizarVtn();
/*     */             }
/*     */           }
/*     */         });
/*     */     
/*  92 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmCurso);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ocultarErrores() {
/*  99 */     this.frmCurso.getLblError().setVisible(false);
/* 100 */     this.frmCurso.getLblErrorCapacidad().setVisible(false);
/* 101 */     this.frmCurso.getLblErrorCiclo().setVisible(false);
/* 102 */     this.frmCurso.getLblErrorDocente().setVisible(false);
/* 103 */     this.frmCurso.getLblErrorJornada().setVisible(false);
/* 104 */     this.frmCurso.getLblErrorMateria().setVisible(false);
/* 105 */     this.frmCurso.getLblErrorParalelo().setVisible(false);
/* 106 */     this.frmCurso.getLblErrorPrdLectivo().setVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbPrdLectivo() {
/* 113 */     this.periodos = this.PLBD.cargarPrdParaCmbFrm();
/* 114 */     if (this.periodos != null) {
/* 115 */       this.frmCurso.getCbxPeriodoLectivo().removeAllItems();
/* 116 */       this.frmCurso.getCbxPeriodoLectivo().addItem("Seleccione");
/* 117 */       this.periodos.forEach(p -> this.frmCurso.getCbxPeriodoLectivo().addItem(p.getNombre()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clickCmbPrd() {
/* 127 */     int posPr = this.frmCurso.getCbxPeriodoLectivo().getSelectedIndex();
/* 128 */     llenarCmbCiclos(posPr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void actualizarCmbMaterias() {
/* 138 */     this.frmCurso.getCbxMateria().setEnabled(true);
/* 139 */     int posPr = this.frmCurso.getCbxPeriodoLectivo().getSelectedIndex();
/* 140 */     int posCi = this.frmCurso.getCbxCiclo().getSelectedIndex();
/* 141 */     if (posPr > 0 && posCi > 0) {
/* 142 */       int ciclo = Integer.parseInt(this.frmCurso.getCbxCiclo().getSelectedItem().toString());
/*     */       
/* 144 */       this.materias = this.MTBD.cargarMateriaPorCarreraCiclo(((PeriodoLectivoMD)this.periodos.get(posPr - 1)).getCarrera().getId(), ciclo);
/* 145 */       cargarCmbMaterias(this.materias);
/*     */     } else {
/* 147 */       this.frmCurso.getCbxMateria().removeAllItems();
/* 148 */       this.frmCurso.getCbxMateria().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void llenarCmbCiclos(int posPrd) {
/* 159 */     this.ciclos = this.MTBD.cargarCiclosCarrera(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getCarrera().getId());
/* 160 */     this.frmCurso.getCbxCiclo().removeAllItems();
/* 161 */     if (this.ciclos != null) {
/* 162 */       this.frmCurso.getCbxCiclo().addItem("Seleccione");
/* 163 */       this.ciclos.forEach(c -> this.frmCurso.getCbxCiclo().addItem(c + ""));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void actualizarCmbDocentes() {
/* 174 */     int posMat = this.frmCurso.getCbxMateria().getSelectedIndex();
/* 175 */     this.frmCurso.getCbxDocente().setEnabled(true);
/* 176 */     if (posMat > 0) {
/* 177 */       this.docentes = this.DBD.cargarDocentesPorMateria(((MateriaMD)this.materias.get(posMat - 1)).getId());
/* 178 */       cargarCmbDocente(this.docentes);
/*     */     } else {
/* 180 */       this.frmCurso.getCbxDocente().setEnabled(false);
/* 181 */       this.frmCurso.getCbxDocente().removeAllItems();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbMaterias(ArrayList<MateriaMD> materias) {
/* 191 */     if (materias != null) {
/* 192 */       this.frmCurso.getCbxMateria().removeAllItems();
/* 193 */       this.frmCurso.getCbxMateria().addItem("Seleccione");
/* 194 */       materias.forEach(m -> this.frmCurso.getCbxMateria().addItem(m.getNombre()));
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
/*     */   private void cargarCmbDocente(ArrayList<DocenteMD> docentes) {
/* 207 */     if (docentes != null) {
/* 208 */       this.frmCurso.getCbxDocente().removeAllItems();
/* 209 */       this.frmCurso.getCbxDocente().addItem("Seleccione");
/* 210 */       docentes.forEach(d -> this.frmCurso.getCbxDocente().addItem(d.getPrimerNombre() + " " + d.getPrimerApellido()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarCmbJornada() {
/* 221 */     this.jornadas = JornadaBD.cargarJornadas();
/* 222 */     if (this.jornadas != null) {
/* 223 */       this.frmCurso.getCbxJornada().removeAllItems();
/* 224 */       this.frmCurso.getCbxJornada().addItem("Seleccione");
/* 225 */       this.jornadas.forEach(j -> this.frmCurso.getCbxJornada().addItem(j.getNombre()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void guardarSeguirIngresando() {
/* 235 */     if (guardar()) {
/* 236 */       actualizarCmbMaterias();
/* 237 */       actualizarCmbDocentes();
/* 238 */       this.frmCurso.getLblError().setVisible(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void guardarYSalir() {
/* 246 */     if (guardar()) {
/* 247 */       this.frmCurso.dispose();
/* 248 */       this.ctrPrin.cerradoJIF();
/* 249 */       if (this.ctrCurso != null) {
/* 250 */         this.ctrCurso.actualizarVtn();
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
/*     */   private boolean guardar() {
/* 263 */     boolean guardar = true;
/*     */     
/* 265 */     int posPrd = this.frmCurso.getCbxPeriodoLectivo().getSelectedIndex();
/* 266 */     int posJrd = this.frmCurso.getCbxJornada().getSelectedIndex();
/* 267 */     int posCic = this.frmCurso.getCbxCiclo().getSelectedIndex();
/* 268 */     int posPrl = this.frmCurso.getCbxParalelo().getSelectedIndex();
/* 269 */     int posMat = this.frmCurso.getCbxMateria().getSelectedIndex();
/* 270 */     int posDoc = this.frmCurso.getCbxDocente().getSelectedIndex();
/* 271 */     String capacidad = this.frmCurso.getTxtCapacidad().getText();
/* 272 */     int ciclo = 0;
/* 273 */     String paralelo = "SP";
/*     */     
/* 275 */     if (!Validar.esNumeros(capacidad)) {
/* 276 */       guardar = false;
/* 277 */       this.frmCurso.getLblErrorCapacidad().setVisible(true);
/*     */     } else {
/* 279 */       this.frmCurso.getLblErrorCapacidad().setVisible(false);
/*     */     } 
/*     */     
/* 282 */     if (posPrd < 1 || posJrd < 1 || posCic < 1 || posPrl < 1 || posMat < 1 || posDoc < 1) {
/* 283 */       guardar = false;
/* 284 */       this.frmCurso.getLblError().setText("Todos los campos son obligatorios.");
/* 285 */       this.frmCurso.getLblError().setVisible(true);
/*     */     } else {
/* 287 */       this.frmCurso.getLblError().setVisible(false);
/* 288 */       ciclo = Integer.parseInt(this.frmCurso.getCbxCiclo().getSelectedItem().toString());
/* 289 */       paralelo = this.frmCurso.getCbxParalelo().getSelectedItem().toString();
/*     */       
/* 291 */       if (!this.editando) {
/* 292 */         CursoMD existeCurso = this.CBD.existeDocenteMateria(((MateriaMD)this.materias.get(posMat - 1)).getId(), ((DocenteMD)this.docentes
/* 293 */             .get(posDoc - 1)).getIdDocente(), ((JornadaMD)this.jornadas.get(posJrd - 1)).getId(), ((PeriodoLectivoMD)this.periodos
/* 294 */             .get(posPrd - 1)).getID(), ciclo, paralelo);
/*     */         
/* 296 */         if (existeCurso != null) {
/* 297 */           guardar = false;
/* 298 */           this.frmCurso.getLblError().setText("Datos ya guardados.");
/* 299 */           this.frmCurso.getLblError().setVisible(true);
/*     */         } else {
/* 301 */           this.frmCurso.getLblError().setVisible(false);
/*     */           
/* 303 */           existeCurso = this.CBD.existeMateriaCursoJornada(((MateriaMD)this.materias.get(posMat - 1)).getId(), ciclo, ((JornadaMD)this.jornadas
/* 304 */               .get(posJrd - 1)).getId(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID(), paralelo);
/*     */           
/* 306 */           if (existeCurso != null) {
/* 307 */             guardar = false;
/* 308 */             this.frmCurso.getLblError().setText("Este curso ya tiene guardado: " + ((MateriaMD)this.materias.get(posMat - 1)).getNombre() + ".");
/* 309 */             this.frmCurso.getLblError().setVisible(true);
/*     */           } else {
/* 311 */             this.frmCurso.getLblError().setVisible(false);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 318 */     if (guardar) {
/* 319 */       CursoMD curso = new CursoMD();
/* 320 */       String nombre = ((JornadaMD)this.jornadas.get(posJrd - 1)).getNombre().charAt(0) + "" + ciclo + "" + paralelo;
/*     */       
/* 322 */       curso.setCapacidad(Integer.parseInt(capacidad));
/* 323 */       curso.setCiclo(ciclo);
/* 324 */       curso.setJornada(this.jornadas.get(posJrd - 1));
/* 325 */       curso.setNombre(nombre);
/* 326 */       curso.setDocente(this.docentes.get(posDoc - 1));
/* 327 */       curso.setMateria(this.materias.get(posMat - 1));
/* 328 */       curso.setPeriodo(this.periodos.get(posPrd - 1));
/* 329 */       curso.setParalelo(paralelo);
/*     */       
/* 331 */       if (!this.editando) {
/* 332 */         this.CBD.guardarCurso(curso);
/*     */       }
/* 334 */       else if (this.idCurso > 0) {
/*     */         
/* 336 */         curso.setId(this.idCurso);
/* 337 */         this.CBD.editarCurso(curso);
/* 338 */         this.editando = false;
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     return guardar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void editar(CursoMD c) {
/* 352 */     this.editando = true;
/* 353 */     this.idCurso = c.getId();
/* 354 */     int j = 0;
/* 355 */     switch (c.getNombre().charAt(0)) {
/*     */       case 'M':
/* 357 */         j = 1;
/*     */         break;
/*     */       case 'V':
/* 360 */         j = 2;
/*     */         break;
/*     */       case 'N':
/* 363 */         j = 3;
/*     */         break;
/*     */     } 
/*     */     
/* 367 */     this.frmCurso.getBtnGuardarContinuar().setVisible(false);
/*     */     
/* 369 */     this.frmCurso.getCbxPeriodoLectivo().setSelectedItem(c.getPeriodo().getNombre());
/* 370 */     this.frmCurso.getCbxJornada().setSelectedIndex(j);
/* 371 */     this.frmCurso.getCbxCiclo().setSelectedItem(c.getCiclo() + "");
/* 372 */     this.frmCurso.getCbxParalelo().setSelectedItem(c.getNombre().charAt(2) + "");
/* 373 */     this.frmCurso.getCbxMateria().setSelectedItem(c.getMateria().getNombre());
/* 374 */     this.frmCurso.getCbxDocente().setSelectedItem(c.getDocente().getPrimerNombre() + " " + c
/* 375 */         .getDocente().getPrimerApellido());
/* 376 */     this.frmCurso.getTxtCapacidad().setText(c.getCapacidad() + "");
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\curso\FrmCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */