/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.curso.CursoBD;
/*     */ import modelo.curso.CursoMD;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.periodolectivo.PeriodoLectivoBD;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.validaciones.Validar;
/*     */ import vista.docente.JDReasignarMaterias;
/*     */ 
/*     */ public class JDReasignarMateriasCTR
/*     */   extends DVtnCTR
/*     */ {
/*  27 */   private final DocenteBD DBD = DocenteBD.single();
/*     */   
/*     */   private CursoMD cursoMD;
/*     */   private DocenteMD docenteMD;
/*     */   private final String materia;
/*     */   private final String curso;
/*     */   private final int periodo;
/*     */   private final int docente;
/*     */   private final JDReasignarMaterias frmReasignarMateria;
/*     */   private ArrayList<DocenteMD> docentesMD;
/*     */   
/*     */   public JDReasignarMateriasCTR(VtnPrincipalCTR ctrPrin, String materia, String curso, int periodo, int docente) {
/*  39 */     super(ctrPrin);
/*  40 */     this.materia = materia;
/*  41 */     this.curso = curso;
/*  42 */     this.periodo = periodo;
/*  43 */     this.docente = docente;
/*  44 */     this.frmReasignarMateria = new JDReasignarMaterias((Frame)ctrPrin.getVtnPrin(), false);
/*  45 */     this.frmReasignarMateria.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*  46 */     this.frmReasignarMateria.setVisible(true);
/*  47 */     this.frmReasignarMateria.setTitle("Reasignar Materias");
/*     */   }
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  52 */     this.frmReasignarMateria.getLblMateria().setText(this.materia);
/*  53 */     this.frmReasignarMateria.getBtnCancelar().addActionListener(e -> salir());
/*  54 */     this.frmReasignarMateria.getBtnReasignarMateria().addActionListener(e -> reasignarMaterias());
/*  55 */     String[] titulo = { "Cedula", "Nombres Completos", "Celular", "Correo", "Tipo Contrato" };
/*  56 */     String[][] datos = new String[0][];
/*  57 */     this.mdTbl = TblEstilo.modelTblSinEditar(datos, titulo);
/*  58 */     this.frmReasignarMateria.getTblDocentesDisponibles().setModel(this.mdTbl);
/*  59 */     TblEstilo.formatoTbl(this.frmReasignarMateria.getTblDocentesDisponibles());
/*  60 */     TblEstilo.columnaMedida(this.frmReasignarMateria.getTblDocentesDisponibles(), 0, 85);
/*  61 */     TblEstilo.columnaMedida(this.frmReasignarMateria.getTblDocentesDisponibles(), 1, 250);
/*  62 */     TblEstilo.columnaMedida(this.frmReasignarMateria.getTblDocentesDisponibles(), 2, 90);
/*  63 */     TblEstilo.columnaMedida(this.frmReasignarMateria.getTblDocentesDisponibles(), 3, 230);
/*  64 */     TblEstilo.columnaMedida(this.frmReasignarMateria.getTblDocentesDisponibles(), 4, 125);
/*  65 */     cargarDocentes();
/*  66 */     this.frmReasignarMateria.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  69 */             String b = JDReasignarMateriasCTR.this.frmReasignarMateria.getTxtBuscar().getText().toUpperCase().trim();
/*  70 */             if (e.getKeyCode() == 10) {
/*  71 */               JDReasignarMateriasCTR.this.buscaIncremental(b);
/*  72 */             } else if (b.length() == 0) {
/*  73 */               JDReasignarMateriasCTR.this.cargarDocentes();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void salir() {
/*  80 */     this.frmReasignarMateria.dispose();
/*     */   }
/*     */   
/*     */   private void reasignarMaterias() {
/*  84 */     this.posFila = this.frmReasignarMateria.getTblDocentesDisponibles().getSelectedRow();
/*  85 */     if (this.posFila >= 0) {
/*  86 */       CursoBD bdCurso = CursoBD.single();
/*  87 */       DocenteMD d = new DocenteMD();
/*  88 */       MateriaBD bdMateria = MateriaBD.single();
/*  89 */       this.cursoMD = bdCurso.atraparCurso(bdMateria.buscarMateria(this.materia).getId(), this.periodo, this.docente, this.curso);
/*  90 */       d.setIdDocente(this.DBD.buscarDocente(this.frmReasignarMateria.getTblDocentesDisponibles().getValueAt(this.posFila, 0).toString()).getIdDocente());
/*  91 */       this.cursoMD.setDocente(d);
/*  92 */       System.out.println("docente " + d.getIdDocente());
/*  93 */       if (bdCurso.nuevoCurso(this.cursoMD) == true) {
/*  94 */         int curso_New = bdCurso.atraparCurso(bdMateria.buscarMateria(this.materia).getId(), this.periodo, d.getIdDocente(), this.curso).getId();
/*  95 */         if (this.DBD.reasignarAlumnoCurso(this.cursoMD.getId(), curso_New)) {
/*  96 */           if (this.DBD.reasignarNotas(this.cursoMD.getId(), curso_New)) {
/*  97 */             JOptionPane.showMessageDialog(null, "Se reasignó con éxito las materias y notas al docente seleccionado");
/*  98 */             this.frmReasignarMateria.dispose();
/*     */           } else {
/* 100 */             JOptionPane.showMessageDialog(null, "No se pudo reasignar las notas de esas materias al nuevo docente seleccionado");
/*     */           } 
/*     */         } else {
/* 103 */           JOptionPane.showMessageDialog(null, "No se pudo reasignar las materias al docente seleccionado");
/*     */         } 
/*     */       } else {
/* 106 */         JOptionPane.showMessageDialog(null, "No se pudo finalizar el contrato de este docente");
/*     */       } 
/*     */     } else {
/* 109 */       JOptionPane.showMessageDialog(null, "Debe seleccionar una fila ");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void buscaIncremental(String aguja) {
/* 114 */     if (Validar.esLetrasYNumeros(aguja)) {
/* 115 */       this.docentesMD = this.DBD.buscarReasignarMateria(aguja);
/* 116 */       llenarTabla(this.docentesMD);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cargarTabla(String periodo) {
/* 121 */     DocenteBD d = DocenteBD.single();
/* 122 */     PeriodoLectivoBD p = PeriodoLectivoBD.single();
/*     */     
/* 124 */     DefaultTableModel modelo_Tabla = (DefaultTableModel)this.frmReasignarMateria.getTblDocentesDisponibles().getModel();
/* 125 */     for (int i = this.frmReasignarMateria.getTblDocentesDisponibles().getRowCount() - 1; i >= 0; i--) {
/* 126 */       modelo_Tabla.removeRow(i);
/*     */     }
/* 128 */     List<CursoMD> lista = d.capturarMaterias(p.buscarPeriodo(periodo).getID(), this.docenteMD.getIdDocente());
/* 129 */     int columnas = modelo_Tabla.getColumnCount();
/* 130 */     for (int j = 0; j < lista.size(); j++) {
/* 131 */       modelo_Tabla.addRow(new Object[columnas]);
/* 132 */       this.frmReasignarMateria.getTblDocentesDisponibles().setValueAt(((CursoMD)lista.get(j)).getDocente().getNombreCompleto(), j, 0);
/*     */     } 
/* 134 */     this.frmReasignarMateria.getLblResultados().setText(lista.size() + "resultados obtenidos.");
/*     */   }
/*     */ 
/*     */   
/*     */   private void cargarDocentes() {
/* 139 */     this.docentesMD = this.DBD.cargarDocentesParaReasignarMaterias();
/* 140 */     llenarTabla(this.docentesMD);
/*     */   }
/*     */   
/*     */   public void llenarTabla(ArrayList<DocenteMD> docentesM) {
/* 144 */     this.mdTbl.setRowCount(0);
/* 145 */     if (docentesM != null)
/* 146 */       docentesM.forEach(dc -> {
/*     */             Object[] valores = { dc.getCodigo(), dc.getPrimerApellido() + " " + dc.getSegundoApellido() + " " + dc.getPrimerNombre() + " " + dc.getSegundoNombre(), dc.getCelular(), dc.getCorreo(), dc.getDocenteTipoTiempo() };
/*     */             this.mdTbl.addRow(valores);
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\JDReasignarMateriasCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */