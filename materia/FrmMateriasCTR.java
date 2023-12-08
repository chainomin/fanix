/*     */ package controlador.materia;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.carrera.CarreraBD;
/*     */ import modelo.carrera.CarreraMD;
/*     */ import modelo.materia.AreaBD;
/*     */ import modelo.materia.AreaMD;
/*     */ import modelo.materia.EjeFormacionBD;
/*     */ import modelo.materia.EjeFormacionMD;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.validaciones.CmbValidar;
/*     */ import modelo.validaciones.TxtVNumeros_2;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.materia.FrmMaterias;
/*     */ 
/*     */ public class FrmMateriasCTR extends DCTR {
/*     */   private final FrmMaterias frmMaterias;
/*  39 */   private final MateriaBD MTBD = MateriaBD.single();
/*  40 */   private final AreaBD areaBD = AreaBD.single();
/*     */   private boolean guardar = false;
/*  42 */   private int acceso = 0;
/*     */   private boolean editar = false;
/*     */   private String nombre_Materia;
/*     */   private ArrayList<CarreraMD> listaCarrera;
/*     */   private ArrayList<EjeFormacionMD> listaEje;
/*  47 */   private final CarreraBD carBD = null;
/*  48 */   private final EjeFormacionBD ejeBD = null;
/*  49 */   private int idEditar = 0;
/*     */   
/*     */   private final VtnMateriaCTR ctrVtnMat;
/*     */ 
/*     */   
/*     */   public FrmMateriasCTR(FrmMaterias frmMaterias, VtnPrincipalCTR ctrPrin) {
/*  55 */     super(ctrPrin);
/*  56 */     this.frmMaterias = frmMaterias;
/*  57 */     this.ctrVtnMat = null;
/*     */   }
/*     */   
/*     */   public FrmMateriasCTR(FrmMaterias frmMaterias, VtnPrincipalCTR ctrPrin, VtnMateriaCTR ctrVtnMat) {
/*  61 */     super(ctrPrin);
/*  62 */     this.frmMaterias = frmMaterias;
/*  63 */     this.ctrVtnMat = ctrVtnMat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  68 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmMaterias);
/*     */     
/*  70 */     this.frmMaterias.getCbCarrera().addActionListener(e -> {
/*     */           this.acceso = 0;
/*     */           
/*     */           String nombre = this.frmMaterias.getCbCarrera().getSelectedItem().toString();
/*     */           
/*     */           List<EjeFormacionMD> ejes = this.MTBD.cargarEjes(this.MTBD.filtrarIdCarrera(nombre, 0).getId());
/*     */           
/*     */           this.frmMaterias.getCbEjeFormacion().removeAllItems();
/*     */           
/*     */           this.frmMaterias.getCbEjeFormacion().addItem("SELECCIONE");
/*     */           
/*     */           this.frmMaterias.getCbx_OrgCurricular().removeAllItems();
/*     */           
/*     */           this.frmMaterias.getCbx_OrgCurricular().addItem("SELECCIONE");
/*     */           
/*     */           if (!nombre.equals("SELECCIONE")) {
/*     */             this.frmMaterias.getCbEjeFormacion().setEnabled(true);
/*     */             for (int i = 0; i < ejes.size(); i++) {
/*     */               this.frmMaterias.getCbEjeFormacion().addItem(((EjeFormacionMD)ejes.get(i)).getNombre());
/*     */               this.frmMaterias.getCbx_OrgCurricular().addItem(((EjeFormacionMD)ejes.get(i)).getNombre());
/*     */             } 
/*     */             List<AreaMD> areas = this.areaBD.obtenerAreasPorCarrera(this.MTBD.filtrarIdCarrera(nombre, 0).getId());
/*     */             for (int j = 0; j < areas.size(); j++) {
/*     */               this.frmMaterias.getCbx_AreaAcademica().addItem(((AreaMD)areas.get(j)).getNombreArea());
/*     */             }
/*     */             habilitarGuardar();
/*     */           } 
/*     */           int pos = this.frmMaterias.getCbCarrera().getSelectedIndex();
/*     */           if (pos > 0) {
/*     */             this.frmMaterias.getCbCarrera().setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
/*     */             if (this.frmMaterias.getLblErrorCarrera() != null) {
/*     */               this.frmMaterias.getLblErrorCarrera().setVisible(false);
/*     */             }
/*     */           } else {
/*     */             this.frmMaterias.getCbCarrera().setBorder(BorderFactory.createLineBorder(new Color(204, 0, 0)));
/*     */             if (this.frmMaterias.getLblErrorCarrera() != null) {
/*     */               this.frmMaterias.getLblErrorCarrera().setVisible(true);
/*     */             }
/*     */           } 
/*     */         });
/* 110 */     this.frmMaterias.getCbEjeFormacion().addActionListener(e -> {
/*     */           this.acceso++;
/*     */           if (this.acceso > 1) {
/*     */             String nombre = this.frmMaterias.getCbEjeFormacion().getSelectedItem().toString();
/*     */             switch (nombre) {
/*     */               case "BÁSICA":
/*     */               case "FORMACIÓN BÁSICA":
/*     */               case "UNIDAD BÁSICA":
/*     */                 this.frmMaterias.getCbx_OrgCurricular().setSelectedIndex(1);
/*     */                 break;
/*     */ 
/*     */               
/*     */               case "PROFESIONAL":
/*     */               case "FORMACIÓN PROFESIONAL":
/*     */               case "UNIDAD PROFESIONAL":
/*     */                 this.frmMaterias.getCbx_OrgCurricular().setSelectedIndex(2);
/*     */                 break;
/*     */ 
/*     */               
/*     */               case "TITULACIÓN":
/*     */                 this.frmMaterias.getCbx_OrgCurricular().setSelectedIndex(3);
/*     */                 break;
/*     */             } 
/*     */             
/*     */             habilitarGuardar();
/*     */           } 
/*     */           int pos = this.frmMaterias.getCbCarrera().getSelectedIndex();
/*     */           if (pos > 0) {
/*     */             this.frmMaterias.getCbCarrera().setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
/*     */             if (this.frmMaterias.getLblErrorCarrera() != null) {
/*     */               this.frmMaterias.getLblErrorCarrera().setVisible(false);
/*     */             }
/*     */           } else {
/*     */             this.frmMaterias.getCbCarrera().setBorder(BorderFactory.createLineBorder(new Color(204, 0, 0)));
/*     */             if (this.frmMaterias.getLblErrorCarrera() != null) {
/*     */               this.frmMaterias.getLblErrorCarrera().setVisible(true);
/*     */             }
/*     */           } 
/*     */         });
/* 149 */     this.frmMaterias.getBtnGuardar().addActionListener(e -> {
/*     */           if (this.guardar == true) {
/*     */             guardarMateria();
/*     */           }
/*     */         });
/* 154 */     this.frmMaterias.getBtnCancelar().addActionListener(e -> cancelar());
/* 155 */     iniciarValidaciones();
/* 156 */     iniciarComponentes();
/* 157 */     iniciarCarreras();
/*     */   }
/*     */ 
/*     */   
/*     */   public void iniciarCarreras() {
/* 162 */     List<CarreraMD> carreras = this.MTBD.cargarCarreras();
/* 163 */     for (int i = 0; i < carreras.size(); i++) {
/* 164 */       this.frmMaterias.getCbCarrera().addItem(((CarreraMD)carreras.get(i)).getNombre());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void habilitarGuardar() {
/* 173 */     String Carrera = "SELECCIONE", Eje = "SELECCIONE";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     Carrera = this.frmMaterias.getCbCarrera().getSelectedItem().toString();
/* 180 */     Eje = this.frmMaterias.getCbEjeFormacion().getSelectedItem().toString();
/* 181 */     String materiaCodigo = this.frmMaterias.getTxt_CodMateria().getText();
/* 182 */     String materiaNombre = this.frmMaterias.getTxtNombreMateria().getText();
/* 183 */     String materiaCiclo = this.frmMaterias.getCbx_Ciclo().getSelectedItem().toString();
/* 184 */     String tipoAcreditacion = this.frmMaterias.getCbTipoAcreditacion().getSelectedItem().toString();
/* 185 */     String creditos = this.frmMaterias.getTxtCreditos().getText();
/* 186 */     String horasDocencia = this.frmMaterias.getTxtHorasDocencia().getText();
/* 187 */     String horasPracticas = this.frmMaterias.getTxtHorasPracticas().getText();
/* 188 */     String horasPresenciales = this.frmMaterias.getTxtHorasPresenciales().getText();
/* 189 */     String horasAutoEstudio = this.frmMaterias.getTxtHorasAutoEstudio().getText();
/* 190 */     String totalHoras = this.frmMaterias.getTxtTotalHoras().getText();
/* 191 */     String objetivoGeneral = this.frmMaterias.getTxtObjetivoGeneral().getText();
/* 192 */     String objetivoEspecifico = this.frmMaterias.getTxtObjetivoEspecifico().getText();
/* 193 */     String descripcionMateria = this.frmMaterias.getTxtDescripcionMateria().getText();
/* 194 */     if (!Carrera.equals("SELECCIONE") && !Eje.equals("SELECCIONE") && 
/* 195 */       !materiaCodigo.equals("") && !materiaNombre.equals("") && 
/* 196 */       !materiaCiclo.equals("SELECCIONE") && !tipoAcreditacion.equals("SELECCIONE") && 
/* 197 */       !creditos.equals("") && !horasDocencia.equals("") && !horasPracticas.equals("") && 
/* 198 */       !horasPresenciales.equals("") && !horasAutoEstudio.equals("") && 
/* 199 */       !totalHoras.equals("") && !objetivoGeneral.equals("") && !objetivoEspecifico.equals("") && 
/* 200 */       !descripcionMateria.equals("")) {
/* 201 */       if (!this.frmMaterias.getLblErrorCarrera().isVisible() && !this.frmMaterias.getLblErrorEjeFormacion().isVisible() && 
/* 202 */         !this.frmMaterias.getLblErrorCodigoMateria().isVisible() && !this.frmMaterias.getLblErrorNombreMateria().isVisible() && 
/* 203 */         !this.frmMaterias.getLblErrorTipoAcreditacion().isVisible() && !this.frmMaterias.getLblErrorMateriaCiclo().isVisible() && 
/* 204 */         !this.frmMaterias.getLblErrorCreditos().isVisible() && !this.frmMaterias.getLblErrorCampoFormacion().isVisible() && 
/* 205 */         !this.frmMaterias.getLblErrorOrganizacionCurricular().isVisible()) {
/* 206 */         this.frmMaterias.getBtnGuardar().setEnabled(true);
/* 207 */         this.guardar = true;
/*     */       } else {
/*     */         
/* 210 */         this.frmMaterias.getBtnGuardar().setEnabled(false);
/*     */       } 
/*     */     } else {
/* 213 */       this.frmMaterias.getBtnGuardar().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciarComponentes() {
/* 220 */     this.frmMaterias.getCbCarrera().setToolTipText("Seleccione un Carrera");
/* 221 */     this.frmMaterias.getCbEjeFormacion().setToolTipText("Seleccione un Eje de Formación");
/* 222 */     this.frmMaterias.getTxt_CodMateria().setToolTipText("Escriba el Código perteneciente a la Materia, este Código debe ser único");
/* 223 */     this.frmMaterias.getTxtNombreMateria().setToolTipText("Escriba el Nombre de la Materia");
/* 224 */     this.frmMaterias.getCbMateriaTipo().setToolTipText("Seleccione el Tipo de la Materia de la siguiente lista");
/* 225 */     this.frmMaterias.getCbTipoAcreditacion().setToolTipText("Seleccione un Tipo de Acreditación de la siguiente lista");
/* 226 */     this.frmMaterias.getCbx_Ciclo().setToolTipText("Seleccione el Ciclo al que pertenece esta materia");
/* 227 */     this.frmMaterias.getTxtCreditos().setToolTipText("Escriba el total de horas o créditos de la Materia");
/* 228 */     this.frmMaterias.getTxtHorasDocencia().setToolTipText("Escriba el número de horas de Docencia de la materia");
/* 229 */     this.frmMaterias.getTxtHorasAutoEstudio().setToolTipText("Escriba el número de horas Autoestudio de la materia");
/* 230 */     this.frmMaterias.getTxtHorasPracticas().setToolTipText("Escriba el número de horas Prácticas de la materia");
/* 231 */     this.frmMaterias.getTxtObjetivoGeneral().setToolTipText("Escriba el Objetivo General de la materia");
/* 232 */     this.frmMaterias.getTxtObjetivoEspecifico().setToolTipText("Escriba el Objetivo Específico de la materia");
/* 233 */     this.frmMaterias.getCbx_OrgCurricular().setToolTipText("Seleccione la Unidad de Organización Curricular de la materia");
/* 234 */     this.frmMaterias.getTxtDescripcionMateria().setToolTipText("Escriba la Descripción de la materia");
/* 235 */     this.frmMaterias.getCbTipoAcreditacion().setToolTipText("Seleccione la Organización Curricular de la materia");
/*     */     
/* 237 */     this.frmMaterias.getLblErrorCarrera().setVisible(false);
/* 238 */     this.frmMaterias.getLblErrorEjeFormacion().setVisible(false);
/* 239 */     this.frmMaterias.getLblErrorCodigoMateria().setVisible(false);
/* 240 */     this.frmMaterias.getLblErrorMateriaTipo().setVisible(false);
/* 241 */     this.frmMaterias.getLblErrorNombreMateria().setVisible(false);
/* 242 */     this.frmMaterias.getLblErrorMateriaCiclo().setVisible(false);
/* 243 */     this.frmMaterias.getLblErrorTipoAcreditacion().setVisible(false);
/* 244 */     this.frmMaterias.getLblErrorCreditos().setVisible(false);
/* 245 */     this.frmMaterias.getLblErrorHorasDocencia().setVisible(false);
/* 246 */     this.frmMaterias.getLblErrorHorasPracticas().setVisible(false);
/* 247 */     this.frmMaterias.getLblErrorHorasPresenciales().setVisible(false);
/* 248 */     this.frmMaterias.getLblErrorHorasAutoEstudio().setVisible(false);
/* 249 */     this.frmMaterias.getLblErrorTotalHoras().setVisible(false);
/* 250 */     this.frmMaterias.getLblErrorObjetivoGeneral().setVisible(false);
/* 251 */     this.frmMaterias.getLblErrorObjetivoEspecifico().setVisible(false);
/* 252 */     this.frmMaterias.getLblErrorDescripcionMateria().setVisible(false);
/* 253 */     this.frmMaterias.getLblErrorOrganizacionCurricular().setVisible(false);
/* 254 */     this.frmMaterias.getLblErrorCampoFormacion().setVisible(false);
/* 255 */     this.frmMaterias.getBtnGuardar().setText("Guardar");
/* 256 */     this.frmMaterias.getCbEjeFormacion().setEnabled(false);
/* 257 */     this.frmMaterias.getBtnGuardar().setEnabled(false);
/* 258 */     this.frmMaterias.getTxtObjetivoGeneral().setLineWrap(true);
/* 259 */     this.frmMaterias.getTxtObjetivoGeneral().setWrapStyleWord(true);
/* 260 */     this.frmMaterias.getTxtObjetivoEspecifico().setLineWrap(true);
/* 261 */     this.frmMaterias.getTxtObjetivoEspecifico().setWrapStyleWord(true);
/* 262 */     this.frmMaterias.getTxtDescripcionMateria().setLineWrap(true);
/* 263 */     this.frmMaterias.getTxtDescripcionMateria().setWrapStyleWord(true);
/*     */ 
/*     */     
/* 266 */     this.frmMaterias.getLblErrorMateriaModalidad().setVisible(false);
/* 267 */     this.frmMaterias.getLblErrorItinerario().setVisible(false);
/* 268 */     this.frmMaterias.getLblErrorApp().setVisible(false);
/* 269 */     this.frmMaterias.getLblErrorHorasTutoria().setVisible(false);
/*     */     
/* 271 */     this.frmMaterias.getLblErrorFormaEvide().setVisible(false);
/* 272 */     this.frmMaterias.getLblErrorAreaAcademica().setVisible(false);
/* 273 */     this.frmMaterias.getLblErrorEstrategias().setVisible(false);
/* 274 */     this.frmMaterias.getLblErrorRecursos().setVisible(false);
/* 275 */     this.frmMaterias.getLblErrorResultados().setVisible(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void borrarCampos() {
/* 280 */     this.frmMaterias.getTxt_CodMateria().setText("");
/* 281 */     this.frmMaterias.getTxtCreditos().setText("");
/* 282 */     this.frmMaterias.getTxtDescripcionMateria().setText("");
/* 283 */     this.frmMaterias.getTxtHorasAutoEstudio().setText("");
/* 284 */     this.frmMaterias.getTxtHorasDocencia().setText("");
/* 285 */     this.frmMaterias.getTxtHorasPracticas().setText("");
/* 286 */     this.frmMaterias.getTxtHorasPresenciales().setText("");
/* 287 */     this.frmMaterias.getTxtNombreMateria().setText("");
/* 288 */     this.frmMaterias.getTxtObjetivoEspecifico().setText("");
/* 289 */     this.frmMaterias.getTxtObjetivoGeneral().setText("");
/* 290 */     this.frmMaterias.getTxtTotalHoras().setText("");
/* 291 */     this.frmMaterias.getCbCarrera().setSelectedIndex(0);
/* 292 */     this.frmMaterias.getCbEjeFormacion().setSelectedIndex(0);
/* 293 */     this.frmMaterias.getCbMateriaTipo().setSelectedIndex(0);
/* 294 */     this.frmMaterias.getCbTipoAcreditacion().setSelectedIndex(0);
/* 295 */     this.frmMaterias.getCbx_CamFormacion().setSelectedIndex(0);
/* 296 */     this.frmMaterias.getCbx_Ciclo().setSelectedIndex(0);
/* 297 */     this.frmMaterias.getCbx_OrgCurricular().setSelectedIndex(0);
/* 298 */     this.frmMaterias.getChBNucleo().setSelected(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void guardarMateria() {
/* 305 */     String modalidad = this.frmMaterias.getCbx_Modalidad().getSelectedItem().toString();
/* 306 */     String itinerario = this.frmMaterias.getTxtItinerario().getText();
/* 307 */     String areaAcademica = this.frmMaterias.getCbx_AreaAcademica().getSelectedItem().toString();
/* 308 */     int horasTutoria = Integer.parseInt(this.frmMaterias.getTxtHorasTutoria().getText());
/* 309 */     String perfilEgreso = this.frmMaterias.getTxtPerfilEgreso().getText();
/* 310 */     String Evaluacion = this.frmMaterias.getTxtFormaEvidencia().getText();
/* 311 */     String actividadesPracticasProgramadas = this.frmMaterias.getTxtApp().getText();
/* 312 */     String estrategiasMetodologicas = this.frmMaterias.getTxtEstrategias().getText();
/* 313 */     String recursosDidacticos = this.frmMaterias.getTxtRecursos().getText();
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
/*     */     
/* 327 */     int materiaCiclo = 0;
/*     */     
/* 329 */     CarreraMD carreraMD = new CarreraMD();
/* 330 */     EjeFormacionMD ejeMD = new EjeFormacionMD();
/*     */     
/* 332 */     String carrera = this.frmMaterias.getCbCarrera().getSelectedItem().toString();
/* 333 */     String eje = this.frmMaterias.getCbEjeFormacion().getSelectedItem().toString();
/* 334 */     String materiaCodigo = this.frmMaterias.getTxt_CodMateria().getText().trim().toUpperCase();
/* 335 */     String materiaNombre = this.frmMaterias.getTxtNombreMateria().getText().trim().toUpperCase();
/*     */     
/* 337 */     switch (this.frmMaterias.getCbx_Ciclo().getSelectedItem().toString()) {
/*     */       case "SELECCIONE":
/* 339 */         materiaCiclo = 0;
/*     */         break;
/*     */       case "Ciclo 1":
/* 342 */         materiaCiclo = 1;
/*     */         break;
/*     */       case "Ciclo 2":
/* 345 */         materiaCiclo = 2;
/*     */         break;
/*     */       case "Ciclo 3":
/* 348 */         materiaCiclo = 3;
/*     */         break;
/*     */       case "Ciclo 4":
/* 351 */         materiaCiclo = 4;
/*     */         break;
/*     */       case "Ciclo 5":
/* 354 */         materiaCiclo = 5;
/*     */         break;
/*     */       case "Ciclo 6":
/* 357 */         materiaCiclo = 6;
/*     */         break;
/*     */     } 
/*     */     
/* 361 */     String materiaTipo = this.frmMaterias.getCbMateriaTipo().getSelectedItem().toString();
/* 362 */     String tipoAcreditacion = this.frmMaterias.getCbTipoAcreditacion().getSelectedItem().toString();
/* 363 */     String creditos = this.frmMaterias.getTxtCreditos().getText();
/* 364 */     boolean materiaNucleo = this.frmMaterias.getChBNucleo().isSelected();
/* 365 */     int horasDocencia = Integer.parseInt(this.frmMaterias.getTxtHorasDocencia().getText().trim());
/* 366 */     int horasPracticas = Integer.parseInt(this.frmMaterias.getTxtHorasPracticas().getText().trim());
/* 367 */     int horasPresenciales = Integer.parseInt(this.frmMaterias.getTxtHorasPresenciales().getText().trim());
/* 368 */     int horasAutoEstudio = Integer.parseInt(this.frmMaterias.getTxtHorasAutoEstudio().getText().trim());
/* 369 */     int totalHoras = Integer.parseInt(this.frmMaterias.getTxtTotalHoras().getText().trim());
/* 370 */     String objetivoGeneral = this.frmMaterias.getTxtObjetivoGeneral().getText().trim();
/* 371 */     String objetivoEspecifico = this.frmMaterias.getTxtObjetivoEspecifico().getText().trim();
/* 372 */     String descripcionMateria = this.frmMaterias.getTxtDescripcionMateria().getText().trim();
/* 373 */     String organizacionCurricular = this.frmMaterias.getCbx_OrgCurricular().getSelectedItem().toString();
/* 374 */     String campoFormacion = this.frmMaterias.getCbx_CamFormacion().getSelectedItem().toString();
/*     */     
/* 376 */     if (this.guardar) {
/*     */       
/* 378 */       MateriaMD materia = new MateriaMD();
/* 379 */       carreraMD.setId(this.MTBD.filtrarIdCarrera(carrera, 0).getId());
/* 380 */       ejeMD.setId(this.MTBD.filtrarIdEje(eje, 0).getId());
/* 381 */       materia.setCarrera(carreraMD);
/* 382 */       materia.setEje(ejeMD);
/* 383 */       materia.setCodigo(materiaCodigo);
/* 384 */       materia.setNombre(materiaNombre);
/* 385 */       materia.setCiclo(materiaCiclo);
/* 386 */       materia.setCreditos(creditos);
/*     */       
/* 388 */       if (materiaTipo.equals("SELECCIONE")) {
/* 389 */         materia.setTipo(' ');
/*     */       } else {
/* 391 */         materia.setTipo(materiaTipo.charAt(0));
/*     */       } 
/*     */       
/* 394 */       materia.setTipoAcreditacion(tipoAcreditacion.charAt(0));
/* 395 */       materia.setHorasDocencia(horasDocencia);
/* 396 */       materia.setHorasPracticas(horasPracticas);
/* 397 */       materia.setHorasPresenciales(horasPresenciales);
/* 398 */       materia.setHorasAutoEstudio(horasAutoEstudio);
/* 399 */       materia.setTotalHoras(totalHoras);
/* 400 */       materia.setObjetivo(objetivoGeneral);
/* 401 */       materia.setObjetivoespecifico(objetivoEspecifico);
/* 402 */       materia.setDescripcion(descripcionMateria);
/* 403 */       materia.setOrganizacioncurricular(organizacionCurricular);
/* 404 */       materia.setMateriacampoformacion(campoFormacion);
/* 405 */       materia.setMateriaNucleo(materiaNucleo);
/*     */       
/* 407 */       materia.setModalidad(modalidad);
/* 408 */       materia.setItinerario(itinerario);
/* 409 */       materia.setAreaAcademica(areaAcademica);
/* 410 */       materia.setHorasTutoria(horasTutoria);
/* 411 */       materia.setPerfilEgreso(perfilEgreso);
/* 412 */       materia.setEvaluacion(Evaluacion);
/* 413 */       materia.setActividadesPracticasProgramadas(actividadesPracticasProgramadas);
/* 414 */       materia.setEstrategiasMetodologicas(estrategiasMetodologicas);
/* 415 */       materia.setRecursosDidacticos(recursosDidacticos);
/*     */       
/* 417 */       if (this.editar) {
/* 418 */         materia.setId(this.idEditar);
/* 419 */         if (this.MTBD.editarMateria(materia)) {
/* 420 */           JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Datos Editados Correctamente");
/* 421 */           actualizarVtnMaterias();
/* 422 */           this.frmMaterias.dispose();
/*     */         } else {
/* 424 */           JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Los no se pudieron Editar Correctamente");
/*     */         } 
/* 426 */         this.idEditar = 0;
/* 427 */         this.editar = false;
/*     */       
/*     */       }
/* 430 */       else if (this.MTBD.insertarMateria(materia)) {
/* 431 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Datos Guardados Correctamente");
/* 432 */         actualizarVtnMaterias();
/* 433 */         this.frmMaterias.dispose();
/*     */       } else {
/* 435 */         JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Los datos no se pudieron Guardar Correctamente");
/*     */       } 
/*     */       
/* 438 */       this.ctrPrin.cerradoJIF();
/*     */     } else {
/* 440 */       JOptionPane.showMessageDialog(null, "Existen errores en los campos\nRevise su información!!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancelar() {
/* 445 */     this.frmMaterias.dispose();
/*     */   }
/*     */   
/*     */   public void buscarMateria() {
/* 449 */     String codigo = this.frmMaterias.getTxt_CodMateria().getText().trim();
/* 450 */     if (!codigo.equals("")) {
/*     */       
/* 452 */       MateriaMD materia = this.MTBD.buscarMateriaxCodigo(codigo);
/* 453 */       this.editar = true;
/* 454 */       if (materia == null) {
/* 455 */         this.editar = false;
/* 456 */         iniciarComponentes();
/* 457 */         borrarCampos();
/* 458 */         iniciarValidaciones();
/* 459 */         JOptionPane.showMessageDialog(null, "No se encuentra a la Materia");
/*     */       } else {
/* 461 */         editarMaterias(materia);
/* 462 */         iniciarValidaciones();
/* 463 */         habilitarGuardar();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void iniciarValidaciones() {
/* 470 */     PropertyChangeListener habilitar = evt -> habilitarGuardar();
/*     */ 
/*     */ 
/*     */     
/* 474 */     KeyListener validarPalabras = new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {
/* 477 */           char car = e.getKeyChar();
/* 478 */           if (!Validar.esLetrasYNumeros2(car + "")) {
/* 479 */             e.consume();
/*     */           }
/* 481 */           FrmMateriasCTR.this.habilitarGuardar();
/*     */         }
/*     */       };
/*     */     
/* 485 */     FocusListener horas = new FocusAdapter()
/*     */       {
/*     */         public void focusLost(FocusEvent e) {
/* 488 */           String docencia = FrmMateriasCTR.this.frmMaterias.getTxtHorasDocencia().getText();
/* 489 */           String auto = FrmMateriasCTR.this.frmMaterias.getTxtHorasAutoEstudio().getText();
/* 490 */           String practicas = FrmMateriasCTR.this.frmMaterias.getTxtHorasPracticas().getText();
/* 491 */           if (!docencia.equals("") && !practicas.equals("")) {
/* 492 */             int horas_Docencia = Integer.valueOf(FrmMateriasCTR.this.frmMaterias.getTxtHorasDocencia().getText()).intValue();
/* 493 */             int horas_Practicas = Integer.valueOf(FrmMateriasCTR.this.frmMaterias.getTxtHorasPracticas().getText()).intValue();
/* 494 */             int horasPresenciales = horas_Docencia + horas_Practicas;
/* 495 */             FrmMateriasCTR.this.frmMaterias.getTxtHorasPresenciales().setText(String.valueOf(horasPresenciales));
/*     */           } 
/* 497 */           if (!docencia.equals("") && !practicas.equals("") && !auto.equals("")) {
/* 498 */             int horas_Docencia = Integer.valueOf(FrmMateriasCTR.this.frmMaterias.getTxtHorasDocencia().getText()).intValue();
/* 499 */             int horas_Auto = Integer.valueOf(FrmMateriasCTR.this.frmMaterias.getTxtHorasAutoEstudio().getText()).intValue();
/* 500 */             int horas_Practicas = Integer.valueOf(FrmMateriasCTR.this.frmMaterias.getTxtHorasPracticas().getText()).intValue();
/* 501 */             int horasTotales = horas_Docencia + horas_Practicas + horas_Auto;
/* 502 */             FrmMateriasCTR.this.frmMaterias.getTxtTotalHoras().setText(String.valueOf(horasTotales));
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 507 */     KeyListener validarNombre = new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {
/* 510 */           char car = e.getKeyChar();
/* 511 */           if (!Validar.esLetras2(car + "")) {
/* 512 */             e.consume();
/*     */           }
/* 514 */           FrmMateriasCTR.this.habilitarGuardar();
/*     */         }
/*     */       };
/*     */     
/* 518 */     KeyListener validarHorasDoc = new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {
/* 521 */           String palabra = FrmMateriasCTR.this.frmMaterias.getTxtHorasDocencia().getText().trim();
/* 522 */           char car = e.getKeyChar();
/* 523 */           if (!Validar.esNumeros(car + "")) {
/* 524 */             e.consume();
/*     */           }
/* 526 */           if (palabra != null && 
/* 527 */             palabra.length() >= 3) {
/* 528 */             e.consume();
/*     */           }
/*     */           
/* 531 */           FrmMateriasCTR.this.habilitarGuardar();
/*     */         }
/*     */       };
/*     */     
/* 535 */     this.frmMaterias.getCbEjeFormacion()
/* 536 */       .addPropertyChangeListener(habilitar);
/* 537 */     this.frmMaterias.getCbMateriaTipo()
/* 538 */       .addActionListener((ActionListener)new CmbValidar(this.frmMaterias
/* 539 */           .getCbMateriaTipo(), this.frmMaterias.getLblErrorMateriaTipo()));
/* 540 */     this.frmMaterias.getCbMateriaTipo()
/* 541 */       .addPropertyChangeListener(habilitar);
/* 542 */     this.frmMaterias.getCbTipoAcreditacion()
/* 543 */       .addActionListener((ActionListener)new CmbValidar(this.frmMaterias
/* 544 */           .getCbTipoAcreditacion(), this.frmMaterias.getLblErrorTipoAcreditacion()));
/* 545 */     this.frmMaterias.getCbTipoAcreditacion()
/* 546 */       .addPropertyChangeListener(habilitar);
/* 547 */     this.frmMaterias.getCbx_Ciclo()
/* 548 */       .addActionListener((ActionListener)new CmbValidar(this.frmMaterias
/* 549 */           .getCbx_Ciclo(), this.frmMaterias.getLblErrorMateriaCiclo()));
/* 550 */     this.frmMaterias.getCbx_Ciclo()
/* 551 */       .addPropertyChangeListener(habilitar);
/* 552 */     this.frmMaterias.getCbx_OrgCurricular()
/* 553 */       .addActionListener((ActionListener)new CmbValidar(this.frmMaterias
/* 554 */           .getCbx_OrgCurricular(), this.frmMaterias.getLblErrorOrganizacionCurricular()));
/* 555 */     this.frmMaterias.getCbx_OrgCurricular()
/* 556 */       .addPropertyChangeListener(habilitar);
/* 557 */     this.frmMaterias.getCbx_CamFormacion()
/* 558 */       .addActionListener((ActionListener)new CmbValidar(this.frmMaterias
/* 559 */           .getCbx_CamFormacion(), this.frmMaterias.getLblErrorCampoFormacion()));
/* 560 */     this.frmMaterias.getCbx_CamFormacion()
/* 561 */       .addPropertyChangeListener(habilitar);
/*     */ 
/*     */     
/* 564 */     this.frmMaterias.getTxt_CodMateria()
/* 565 */       .addKeyListener(validarPalabras);
/* 566 */     this.frmMaterias.getTxtNombreMateria()
/* 567 */       .addKeyListener(validarNombre);
/*     */ 
/*     */     
/* 570 */     this.frmMaterias.getTxtCreditos()
/* 571 */       .addPropertyChangeListener(habilitar);
/* 572 */     this.frmMaterias.getTxtHorasDocencia()
/* 573 */       .addKeyListener((KeyListener)new TxtVNumeros_2(this.frmMaterias.getTxtHorasDocencia()));
/* 574 */     this.frmMaterias.getTxtHorasDocencia()
/* 575 */       .addPropertyChangeListener(habilitar);
/* 576 */     this.frmMaterias.getTxtHorasPracticas()
/* 577 */       .addKeyListener((KeyListener)new TxtVNumeros_2(this.frmMaterias.getTxtHorasPracticas()));
/* 578 */     this.frmMaterias.getTxtHorasPracticas()
/* 579 */       .addPropertyChangeListener(habilitar);
/* 580 */     this.frmMaterias.getTxtHorasPresenciales()
/* 581 */       .addKeyListener((KeyListener)new TxtVNumeros_2(this.frmMaterias.getTxtHorasPresenciales()));
/* 582 */     this.frmMaterias.getTxtHorasPresenciales()
/* 583 */       .addPropertyChangeListener(habilitar);
/* 584 */     this.frmMaterias.getTxtHorasAutoEstudio()
/* 585 */       .addKeyListener((KeyListener)new TxtVNumeros_2(this.frmMaterias.getTxtHorasAutoEstudio()));
/* 586 */     this.frmMaterias.getTxtHorasAutoEstudio()
/* 587 */       .addPropertyChangeListener(habilitar);
/* 588 */     this.frmMaterias.getTxtTotalHoras()
/* 589 */       .addKeyListener((KeyListener)new TxtVNumeros_2(this.frmMaterias.getTxtTotalHoras()));
/* 590 */     this.frmMaterias.getTxtTotalHoras()
/* 591 */       .addPropertyChangeListener(habilitar);
/* 592 */     this.frmMaterias.getTxtHorasDocencia().addFocusListener(horas);
/* 593 */     this.frmMaterias.getTxtHorasAutoEstudio().addFocusListener(horas);
/* 594 */     this.frmMaterias.getTxtHorasPracticas().addFocusListener(horas);
/* 595 */     this.frmMaterias.getTxtHorasPresenciales().setEnabled(false);
/* 596 */     this.frmMaterias.getTxtTotalHoras().setEnabled(false);
/* 597 */     Font negrita = new Font("Tahoma", 1, 13);
/* 598 */     this.frmMaterias.getTxtHorasPresenciales().setFont(negrita);
/* 599 */     this.frmMaterias.getTxtTotalHoras().setFont(negrita);
/*     */ 
/*     */     
/* 602 */     KeyListener validar = new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {
/* 605 */           char car = e.getKeyChar();
/* 606 */           if (!Validar.esObservacion(car + "")) {
/* 607 */             e.consume();
/*     */           }
/* 609 */           FrmMateriasCTR.this.habilitarGuardar();
/*     */         }
/*     */       };
/*     */     
/* 613 */     this.frmMaterias.getTxtObjetivoGeneral()
/* 614 */       .addKeyListener(validar);
/* 615 */     this.frmMaterias.getTxtObjetivoEspecifico()
/* 616 */       .addKeyListener(validar);
/* 617 */     this.frmMaterias.getTxtDescripcionMateria()
/* 618 */       .addKeyListener(validar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void editarMaterias(MateriaMD matEditar) {
/* 624 */     this.editar = true;
/* 625 */     this.idEditar = matEditar.getId();
/* 626 */     this.nombre_Materia = matEditar.getNombre();
/*     */ 
/*     */ 
/*     */     
/* 630 */     this.frmMaterias.getCbx_Modalidad().setSelectedItem(matEditar.getModalidad());
/* 631 */     this.frmMaterias.getTxtItinerario().setText(matEditar.getItinerario());
/* 632 */     this.frmMaterias.getCbx_AreaAcademica().setSelectedItem(matEditar.getAreaAcademica());
/* 633 */     this.frmMaterias.getTxtHorasTutoria().setText(String.valueOf(matEditar.getHorasTutoria()));
/* 634 */     this.frmMaterias.getTxtPerfilEgreso().setText(matEditar.getPerfilEgreso());
/* 635 */     this.frmMaterias.getTxtFormaEvidencia().setText(matEditar.getEvaluacion());
/* 636 */     this.frmMaterias.getTxtApp().setText(matEditar.getActividadesPracticasProgramadas());
/* 637 */     this.frmMaterias.getTxtEstrategias().setText(matEditar.getEstrategiasMetodologicas());
/* 638 */     this.frmMaterias.getTxtRecursos().setText(matEditar.getRecursosDidacticos());
/*     */     
/* 640 */     this.frmMaterias.getTxt_CodMateria().setText(matEditar.getCodigo());
/* 641 */     this.frmMaterias.getTxtNombreMateria().setText(matEditar.getNombre());
/*     */     
/* 643 */     this.frmMaterias.getTxtCreditos().setText(matEditar.getCreditos() + "");
/* 644 */     this.frmMaterias.getTxtDescripcionMateria().setText(matEditar.getDescripcion());
/* 645 */     this.frmMaterias.getTxtHorasAutoEstudio().setText(matEditar.getHorasAutoEstudio() + "");
/*     */     
/* 647 */     this.frmMaterias.getTxtHorasDocencia().setText(matEditar.getHorasDocencia() + "");
/* 648 */     this.frmMaterias.getTxtHorasPracticas().setText(matEditar.getHorasPracticas() + "");
/* 649 */     this.frmMaterias.getTxtHorasPresenciales().setText(matEditar.getHorasPresenciales() + "");
/* 650 */     this.frmMaterias.getTxtObjetivoEspecifico().setText(matEditar.getObjetivoespecifico());
/* 651 */     this.frmMaterias.getTxtObjetivoGeneral().setText(matEditar.getObjetivo());
/*     */     
/* 653 */     this.frmMaterias.getTxtTotalHoras().setText(matEditar.getTotalHoras() + "");
/* 654 */     this.frmMaterias.getChBNucleo().setSelected(matEditar.isMateriaNucleo());
/*     */     
/* 656 */     if (matEditar.getCarrera() == null) {
/* 657 */       this.frmMaterias.getCbCarrera().setSelectedItem("SELECCIONE");
/*     */     } else {
/* 659 */       this.frmMaterias.getCbCarrera().setSelectedItem(this.MTBD
/* 660 */           .filtrarIdCarrera("", matEditar.getCarrera().getId()).getNombre());
/*     */ 
/*     */       
/* 663 */       this.frmMaterias.getCbCarrera().setEnabled(false);
/*     */     } 
/*     */ 
/*     */     
/* 667 */     List<AreaMD> areas = this.areaBD.obtenerAreasPorCarrera(matEditar.getCarrera().getId());
/* 668 */     this.frmMaterias.getCbx_AreaAcademica().removeAllItems();
/* 669 */     for (int i = 0; i < areas.size(); i++) {
/* 670 */       this.frmMaterias.getCbx_AreaAcademica().addItem(((AreaMD)areas.get(i)).getNombreArea());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 679 */     DefaultComboBoxModel<String> modelo = (DefaultComboBoxModel)this.frmMaterias.getCbx_AreaAcademica().getModel();
/*     */ 
/*     */     
/* 682 */     if (modelo.getIndexOf(matEditar.getAreaAcademica()) != -1) {
/*     */       
/* 684 */       this.frmMaterias.getCbx_AreaAcademica().setSelectedItem(matEditar.getAreaAcademica());
/*     */     } else {
/*     */       
/* 687 */       modelo.addElement(matEditar.getAreaAcademica());
/* 688 */       this.frmMaterias.getCbx_AreaAcademica().setSelectedItem(matEditar.getAreaAcademica());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 693 */     if (matEditar.getEje().getId() > 40) {
/* 694 */       this.frmMaterias.getCbEjeFormacion().setSelectedItem(this.MTBD.filtrarIdEje("", matEditar.getEje().getId()).getNombre());
/*     */     } else {
/* 696 */       this.frmMaterias.getCbEjeFormacion().setSelectedItem("SELECCIONE");
/*     */     } 
/*     */     
/* 699 */     int ciclo = matEditar.getCiclo();
/* 700 */     switch (ciclo) {
/*     */       
/*     */       case 1:
/* 703 */         this.frmMaterias.getCbx_Ciclo().setSelectedItem("Ciclo 1");
/*     */         break;
/*     */       case 2:
/* 706 */         this.frmMaterias.getCbx_Ciclo().setSelectedItem("Ciclo 2");
/*     */         break;
/*     */       case 3:
/* 709 */         this.frmMaterias.getCbx_Ciclo().setSelectedItem("Ciclo 3");
/*     */         break;
/*     */       case 4:
/* 712 */         this.frmMaterias.getCbx_Ciclo().setSelectedItem("Ciclo 4");
/*     */         break;
/*     */       case 5:
/* 715 */         this.frmMaterias.getCbx_Ciclo().setSelectedItem("Ciclo 5");
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 720 */     String materiaTipo = matEditar.getTipo() + "";
/* 721 */     if (null == materiaTipo) {
/* 722 */       materiaTipo = "H";
/*     */     } else {
/* 724 */       switch (materiaTipo) {
/*     */         case "M":
/* 726 */           materiaTipo = "M";
/*     */           break;
/*     */         case "C":
/* 729 */           materiaTipo = "C";
/*     */           break;
/*     */         case "H":
/* 732 */           materiaTipo = "H";
/*     */           break;
/*     */         default:
/* 735 */           materiaTipo = "SELECCIONE";
/*     */           break;
/*     */       } 
/*     */     } 
/* 739 */     this.frmMaterias.getCbMateriaTipo().setSelectedItem(materiaTipo);
/*     */     
/* 741 */     String tipoAcreditacion = matEditar.getTipoAcreditacion() + "";
/* 742 */     if (null == tipoAcreditacion) {
/* 743 */       tipoAcreditacion = "T";
/*     */     } else {
/* 745 */       switch (tipoAcreditacion) {
/*     */         case "H":
/* 747 */           tipoAcreditacion = "H";
/*     */           break;
/*     */         case "C":
/* 750 */           tipoAcreditacion = "C";
/*     */           break;
/*     */         case "T":
/* 753 */           tipoAcreditacion = "T";
/*     */           break;
/*     */         default:
/* 756 */           tipoAcreditacion = "SELECCIONE";
/*     */           break;
/*     */       } 
/*     */     } 
/* 760 */     this.frmMaterias.getCbTipoAcreditacion().setSelectedItem(tipoAcreditacion);
/* 761 */     this.frmMaterias.getCbx_OrgCurricular().setSelectedItem(matEditar.getOrganizacioncurricular());
/* 762 */     this.frmMaterias.getCbx_CamFormacion().setSelectedItem(matEditar.getMateriacampoformacion());
/*     */     
/* 764 */     iniciarValidaciones();
/*     */   }
/*     */   
/*     */   private void actualizarVtnMaterias() {
/* 768 */     if (this.ctrVtnMat != null)
/* 769 */       this.ctrVtnMat.actualizarVtn(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\materia\FrmMateriasCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */