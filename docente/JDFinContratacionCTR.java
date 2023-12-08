/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoMD;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.validaciones.CmbValidar;
/*     */ import modelo.validaciones.Validar;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import vista.docente.JDFinContratacion;
/*     */ 
/*     */ public class JDFinContratacionCTR extends DVtnCTR {
/*     */   private PeriodoLectivoBD periodoBD;
/*  42 */   private final DocenteBD DBD = DocenteBD.single();
/*     */   
/*     */   private DocenteMD docenteMD;
/*     */   
/*     */   private final int ID;
/*     */   private int periodo;
/*     */   private final JDFinContratacion frmFinContrato;
/*     */   private static LocalDate fechaInicio;
/*     */   private boolean guardar = false;
/*     */   private List<CursoMD> lista;
/*     */   
/*     */   public JDFinContratacionCTR(VtnPrincipalCTR ctrPrin, String cedula, int ID) {
/*  54 */     super(ctrPrin);
/*  55 */     this.ID = ID;
/*  56 */     this.frmFinContrato = new JDFinContratacion((Frame)ctrPrin.getVtnPrin(), false);
/*  57 */     this.frmFinContrato.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*  58 */     this.frmFinContrato.setVisible(true);
/*  59 */     this.frmFinContrato.setTitle("Fin de Contrato");
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  63 */     this.docenteMD = this.DBD.buscarDocente(this.ID);
/*  64 */     this.frmFinContrato.getBtn_Cancelar().addActionListener(e -> cancelar());
/*  65 */     iniciarFinContrato();
/*  66 */     iniciarPeriodosDocente();
/*     */   }
/*     */   
/*     */   public void cancelar() {
/*  70 */     this.frmFinContrato.dispose();
/*     */   }
/*     */   
/*     */   public void iniciarPeriodosDocente() {
/*  74 */     this.frmFinContrato.getLbl_ErrPeriodos().setVisible(false);
/*  75 */     this.frmFinContrato.getJcbPeriodos().addActionListener(e -> {
/*     */           int pos = this.frmFinContrato.getJcbPeriodos().getSelectedIndex();
/*     */           
/*     */           if (pos > 0) {
/*     */             this.frmFinContrato.getJcbPeriodos().setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
/*     */             
/*     */             if (this.frmFinContrato.getLbl_ErrPeriodos() != null) {
/*     */               CursoBD CBD = CursoBD.single();
/*     */               this.frmFinContrato.getLbl_ErrPeriodos().setVisible(false);
/*     */               String periodo1 = this.frmFinContrato.getJcbPeriodos().getSelectedItem().toString();
/*     */               List<Integer> IDs = CBD.consultaCursos();
/*     */               filtrarMaterias(periodo1, IDs);
/*     */               llenarTabla();
/*     */               int num = this.frmFinContrato.getCbx_Periodos().getItemCount();
/*     */               if (num < 2) {
/*     */                 JOptionPane.showMessageDialog(null, "No se filtró ningún Período Lectivo de este Docente");
/*     */               }
/*     */             } 
/*     */           } else {
/*     */             this.frmFinContrato.getJcbPeriodos().setBorder(BorderFactory.createLineBorder(new Color(204, 0, 0)));
/*     */             if (this.frmFinContrato.getLbl_ErrPeriodos() != null) {
/*     */               this.frmFinContrato.getLbl_ErrPeriodos().setVisible(true);
/*     */               DefaultTableModel modelo_Tabla = (DefaultTableModel)this.frmFinContrato.getTblMateriasCursos().getModel();
/*     */               for (int i = this.frmFinContrato.getTblMateriasCursos().getRowCount() - 1; i >= 0; i--) {
/*     */                 modelo_Tabla.removeRow(i);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           habilitarGuardar();
/*     */         });
/* 105 */     listaPeriodos();
/*     */   }
/*     */   
/*     */   public void listaPeriodos() {
/* 109 */     this.periodoBD = PeriodoLectivoBD.single();
/* 110 */     List<PeriodoLectivoMD> listaPeriodos = this.periodoBD.periodoDocente(this.ID);
/* 111 */     for (int i = 0; i < listaPeriodos.size(); i++) {
/* 112 */       this.frmFinContrato.getJcbPeriodos().addItem(((PeriodoLectivoMD)listaPeriodos.get(i)).getNombre());
/*     */     }
/*     */   }
/*     */   
/*     */   public void filtrarMaterias(String nombre_Periodo, List<Integer> num) {
/* 117 */     DocenteBD DBD = DocenteBD.single();
/* 118 */     PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/* 119 */     this.lista = DBD.capturarMaterias(PLBD.capturarIdPeriodo(nombre_Periodo).getID(), this.docenteMD.getIdDocente());
/*     */     
/* 121 */     for (int x = 0; x < num.size(); x++) {
/* 122 */       for (int i = 0; i < this.lista.size(); i++) {
/* 123 */         if (((Integer)num.get(x)).intValue() == ((CursoMD)this.lista.get(i)).getMateria().getId()) {
/* 124 */           this.lista.remove(i);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 129 */     if (this.lista.isEmpty()) {
/* 130 */       JOptionPane.showMessageDialog(null, "Todas las materias ya fueron reasignadas");
/*     */     }
/* 132 */     this.periodo = PLBD.capturarIdPeriodo(nombre_Periodo).getID();
/*     */   }
/*     */ 
/*     */   
/*     */   public void llenarTabla() {
/* 137 */     DefaultTableModel modelo_Tabla = (DefaultTableModel)this.frmFinContrato.getTblMateriasCursos().getModel();
/* 138 */     for (int i = this.frmFinContrato.getTblMateriasCursos().getRowCount() - 1; i >= 0; i--) {
/* 139 */       modelo_Tabla.removeRow(i);
/*     */     }
/* 141 */     int columnas = modelo_Tabla.getColumnCount();
/* 142 */     for (int j = 0; j < this.lista.size(); j++) {
/* 143 */       modelo_Tabla.addRow(new Object[columnas]);
/* 144 */       this.frmFinContrato.getTblMateriasCursos().setValueAt(((CursoMD)this.lista.get(j)).getMateria().getNombre(), j, 0);
/* 145 */       this.frmFinContrato.getTblMateriasCursos().setValueAt(((CursoMD)this.lista.get(j)).getNombre(), j, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void aceptar() {
/* 150 */     this.frmFinContrato.getJcbPeriodos().getSelectedItem();
/*     */   }
/*     */   
/*     */   private void pnlAnterior() {
/* 154 */     this.frmFinContrato.getTpFrm().setSelectedIndex(0);
/* 155 */     this.frmFinContrato.getBtnGuardar().setEnabled(true);
/* 156 */     this.frmFinContrato.getBtnGuardar().setText("Siguiente");
/* 157 */     habilitarGuardar();
/*     */   }
/*     */   
/*     */   public void iniciarFinContrato() {
/* 161 */     this.frmFinContrato.getLblErrorFechaFinContratacion().setVisible(false);
/* 162 */     this.frmFinContrato.getLblErrorObservacion().setVisible(false);
/* 163 */     if (!this.docenteMD.isDocenteEnFuncion()) {
/* 164 */       this.frmFinContrato.getJdcFinContratacion().setEnabled(false);
/* 165 */       this.frmFinContrato.getTxtObservacion().setEnabled(false);
/* 166 */       this.frmFinContrato.getBtnGuardar().setEnabled(false);
/* 167 */       this.frmFinContrato.getBtnReasignarMateria().setEnabled(true);
/* 168 */       this.frmFinContrato.getBtnReasignarMateria().addActionListener(e -> reasignarMateria());
/*     */     } else {
/* 170 */       this.frmFinContrato.getBtnGuardar().setEnabled(false);
/* 171 */       this.frmFinContrato.getBtnReasignarMateria().setEnabled(false);
/* 172 */       this.frmFinContrato.getBtnGuardar().addActionListener(e -> guardarFinContratacion());
/* 173 */       this.frmFinContrato.getBtnReasignarMateria().addActionListener(e -> reasignarMateria());
/*     */     } 
/*     */     
/* 176 */     this.frmFinContrato.getTxtObservacion().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e)
/*     */           {
/* 180 */             String Observacion = JDFinContratacionCTR.this.frmFinContrato.getTxtObservacion().getText();
/* 181 */             if (!Validar.esObservacion(Observacion)) {
/*     */               
/* 183 */               JDFinContratacionCTR.this.frmFinContrato.getLblErrorObservacion().setVisible(true);
/*     */             } else {
/*     */               
/* 186 */               JDFinContratacionCTR.this.frmFinContrato.getLblErrorObservacion().setVisible(false);
/*     */             } 
/* 188 */             JDFinContratacionCTR.this.habilitarGuardar();
/*     */           }
/*     */         });
/*     */     
/* 192 */     this.frmFinContrato.getJdcFinContratacion().addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked() {
/* 194 */             if (JDFinContratacionCTR.this.validarFecha() == true) {
/* 195 */               JDFinContratacionCTR.this.frmFinContrato.getLblErrorFechaFinContratacion().setVisible(false);
/*     */             } else {
/* 197 */               JDFinContratacionCTR.this.frmFinContrato.getLblErrorFechaFinContratacion().setText("El inicio de contrato no puede ser \n mayor al de finalización");
/* 198 */               JDFinContratacionCTR.this.frmFinContrato.getLblErrorFechaFinContratacion().setVisible(true);
/*     */             } 
/* 200 */             JDFinContratacionCTR.this.habilitarGuardar();
/*     */           }
/*     */         });
/*     */     
/* 204 */     PropertyChangeListener habilitar = evt -> habilitarGuardar();
/*     */ 
/*     */ 
/*     */     
/* 208 */     this.frmFinContrato.getCbx_Periodos().addActionListener((ActionListener)new CmbValidar(this.frmFinContrato
/* 209 */           .getCbx_Periodos(), this.frmFinContrato.getLbl_ErrPeriodos()));
/* 210 */     this.frmFinContrato.getCbx_Periodos().addPropertyChangeListener(habilitar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void habilitarGuardar() {
/* 216 */     String observacion = this.frmFinContrato.getTxtObservacion().getText();
/* 217 */     Date fecha = this.frmFinContrato.getJdcFinContratacion().getDate();
/* 218 */     int posPrd = this.frmFinContrato.getCbx_Periodos().getSelectedIndex();
/* 219 */     int pos = this.frmFinContrato.getTpFrm().getSelectedIndex();
/*     */     
/* 221 */     if (!observacion.equals("") && fecha != null && posPrd > 0) {
/*     */       
/* 223 */       if (!this.frmFinContrato.getLblErrorObservacion().isVisible() && 
/* 224 */         !this.frmFinContrato.getLbl_ErrPeriodos().isVisible() && this.lista != null) {
/*     */         
/* 226 */         this.frmFinContrato.getBtnGuardar().setEnabled(true);
/* 227 */         this.guardar = true;
/*     */       } else {
/* 229 */         this.frmFinContrato.getBtnGuardar().setEnabled(false);
/*     */       } 
/*     */     } else {
/* 232 */       this.frmFinContrato.getBtnGuardar().setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void guardarFinContratacion() {
/* 238 */     String Observacion = "", periodo = "";
/*     */ 
/*     */     
/* 241 */     Observacion = this.frmFinContrato.getTxtObservacion().getText().trim().toUpperCase();
/* 242 */     Date fecha = this.frmFinContrato.getJdcFinContratacion().getDate();
/* 243 */     periodo = this.frmFinContrato.getCbx_Periodos().getSelectedItem().toString();
/* 244 */     if (!this.docenteMD.getFechaInicioContratacion().isAfter(convertirDate(fecha)) && 
/* 245 */       !this.docenteMD.getFechaInicioContratacion().isEqual(convertirDate(fecha))) {
/* 246 */       this.guardar = true;
/* 247 */       this.frmFinContrato.getLblErrorFechaFinContratacion().setVisible(false);
/*     */     } else {
/* 249 */       this.guardar = false;
/* 250 */       this.frmFinContrato.getLblErrorFechaFinContratacion().setText("La fecha de inicio de contrato no puede ser mayor a la de finalización");
/* 251 */       this.frmFinContrato.getLblErrorFechaFinContratacion().setVisible(true);
/* 252 */       JOptionPane.showMessageDialog(null, "No se puede guardar, revise la Fecha de Culminación de Contrato");
/*     */     } 
/* 254 */     if (this.guardar == true) {
/* 255 */       DocenteMD docente = new DocenteMD();
/* 256 */       CursoMD curso = new CursoMD();
/* 257 */       PeriodoLectivoMD periodoMD = new PeriodoLectivoMD();
/* 258 */       docente.setObservacion(Observacion);
/* 259 */       docente.setFechaFinContratacion(convertirDate(fecha));
/* 260 */       docente.setIdDocente(this.docenteMD.getIdDocente());
/* 261 */       periodoMD.setID(this.periodoBD.capturarIdPeriodo(periodo).getID());
/*     */       
/* 263 */       curso.setPeriodo(periodoMD);
/* 264 */       curso.setDocente(docente);
/* 265 */       if (this.DBD.terminarContrato(docente) == true) {
/* 266 */         System.out.println("Se finalizó contrato");
/* 267 */         if (this.lista != null) {
/* 268 */           int cont = 0;
/* 269 */           for (int i = 0; i < this.lista.size(); i++) {
/* 270 */             MateriaMD materia = new MateriaMD();
/* 271 */             materia.setId(((CursoMD)this.lista.get(i)).getMateria().getId());
/* 272 */             curso.setMateria(materia);
/* 273 */             curso.setNombre(((CursoMD)this.lista.get(i)).getNombre());
/* 274 */             if (this.DBD.deshabilitarCursos(curso) == true) {
/* 275 */               cont++;
/*     */             }
/*     */           } 
/* 278 */           if (cont == this.lista.size()) {
/* 279 */             JOptionPane.showMessageDialog(null, "Se finalizó el contrato del Docente con éxito");
/* 280 */             this.frmFinContrato.getBtnReasignarMateria().setEnabled(true);
/* 281 */             this.periodo = periodoMD.getID();
/* 282 */             botoninformeDocente();
/*     */           } else {
/* 284 */             JOptionPane.showMessageDialog(null, "No se pudo finalizar el contrato de este Docente");
/*     */           } 
/*     */         } else {
/* 287 */           JOptionPane.showMessageDialog(null, "Se finalizó el contrato del Docente");
/* 288 */           this.frmFinContrato.getBtnReasignarMateria().setEnabled(true);
/* 289 */           this.periodo = periodoMD.getID();
/* 290 */           botoninformeDocente();
/*     */         } 
/*     */       } else {
/* 293 */         JOptionPane.showMessageDialog(null, "No se pudo finalizar el contrato de este Docente");
/*     */       } 
/*     */       
/* 296 */       System.out.println("Se guarda en base de datos");
/*     */     } else {
/*     */       
/* 299 */       this.frmFinContrato.getTpFrm().setSelectedIndex(1);
/* 300 */       habilitarGuardar();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validarFecha() {
/* 308 */     Date fecha = this.frmFinContrato.getJdcFinContratacion().getDate();
/* 309 */     return (!this.docenteMD.getFechaInicioContratacion().isAfter(convertirDate(fecha)) && 
/* 310 */       !this.docenteMD.getFechaInicioContratacion().isEqual(convertirDate(fecha)));
/*     */   }
/*     */   
/*     */   public LocalDate convertirDate(Date fecha) {
/* 314 */     return Instant.ofEpochMilli(fecha.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void llamainformeDocente() {
/* 320 */     String path = "/vista/reportes/INFORME_DOCENTE_RETIRADO.jasper";
/*     */ 
/*     */     
/*     */     try {
/* 324 */       Map<Object, Object> parametro = new HashMap<>();
/* 325 */       parametro.put("iddocente", Integer.valueOf(this.docenteMD.getIdDocente()));
/*     */       
/* 327 */       parametro.put("periodolectivo", this.frmFinContrato.getJcbPeriodos());
/* 328 */       parametro.put("periodolectivo", this.frmFinContrato.getCbx_Periodos().getSelectedItem().toString());
/* 329 */       JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource(path));
/* 330 */       CON.mostrarReporte(jr, parametro, "Informe de Retiro");
/*     */     }
/* 332 */     catch (JRException ex) {
/* 333 */       JOptionPane.showMessageDialog(null, "error" + ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void botoninformeDocente() {
/* 338 */     int s = JOptionPane.showOptionDialog((Component)this.ctrPrin.getVtnPrin(), "Registro de persona \n¿Desea Imprimir el Registro realizado ?", "Informe de Retiro", 1, 1, null, new Object[] { "SI", "NO" }, "NO");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 345 */     switch (s) {
/*     */       case 0:
/* 347 */         llamainformeDocente();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reasignarMateria() {
/* 358 */     CursoBD bdCurso = CursoBD.single();
/* 359 */     CursoMD c = new CursoMD();
/* 360 */     MateriaBD bdMateria = MateriaBD.single();
/* 361 */     this.posFila = this.frmFinContrato.getTblMateriasCursos().getSelectedRow();
/* 362 */     if (this.posFila >= 0) {
/*     */       
/* 364 */       boolean activo = bdCurso.atraparCurso(bdMateria.buscarMateria(this.frmFinContrato.getTblMateriasCursos().getValueAt(this.posFila, 0).toString()).getId(), this.periodo, this.docenteMD.getIdDocente(), this.frmFinContrato.getTblMateriasCursos().getValueAt(this.posFila, 1).toString()).isActivo();
/* 365 */       if (activo) {
/* 366 */         JOptionPane.showMessageDialog(null, "Este curso está activo, para reasignarlo a un nuevo docente debe eliminarlo");
/*     */       } else {
/*     */         
/* 369 */         JDReasignarMateriasCTR ctr = new JDReasignarMateriasCTR(this.ctrPrin, this.frmFinContrato.getTblMateriasCursos().getValueAt(this.posFila, 0).toString(), this.frmFinContrato.getTblMateriasCursos().getValueAt(this.posFila, 1).toString(), this.periodo, this.docenteMD.getIdDocente());
/* 370 */         ctr.iniciar();
/*     */       } 
/*     */     } else {
/*     */       
/* 374 */       JOptionPane.showMessageDialog(null, "Debe seleccionar una fila ");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\JDFinContratacionCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */