/*      */ package controlador.alumno;
/*      */ import controlador.curso.PnlHorarioCursoCTR;
/*      */ import controlador.estilo.CambioPnlCTR;
/*      */ import controlador.estilo.TblRenderNumMatricula;
/*      */ import controlador.principal.DCTR;
/*      */ import controlador.principal.VtnPrincipalCTR;
/*      */ import controlador.ventanas.VtnLblToolTip;
/*      */ import java.awt.Component;
/*      */ import java.awt.Frame;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.swing.JDialog;
/*      */ import javax.swing.JInternalFrame;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.table.DefaultTableModel;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import modelo.alumno.AlumnoCarreraBD;
/*      */ import modelo.alumno.AlumnoCarreraMD;
/*      */ import modelo.alumno.AlumnoCursoBD;
/*      */ import modelo.alumno.MallaAlumnoBD;
/*      */ import modelo.alumno.MallaAlumnoMD;
/*      */ import modelo.alumno.MatriculaBD;
/*      */ import modelo.alumno.MatriculaMD;
/*      */ import modelo.alumno.UtilEgresadoBD;
/*      */ import modelo.carrera.CarreraBD;
/*      */ import modelo.carrera.CarreraMD;
/*      */ import modelo.curso.CursoBD;
/*      */ import modelo.curso.CursoMD;
/*      */ import modelo.curso.SesionClaseBD;
/*      */ import modelo.curso.SesionClaseMD;
/*      */ import modelo.estilo.TblEstilo;
/*      */ import modelo.materia.MateriaBD;
/*      */ import modelo.materia.MateriaMD;
/*      */ import modelo.materia.MateriaRequisitoBD;
/*      */ import modelo.materia.MateriaRequisitoMD;
/*      */ import modelo.periodolectivo.PeriodoLectivoBD;
/*      */ import modelo.periodolectivo.PeriodoLectivoMD;
/*      */ import modelo.validaciones.CmbValidar;
/*      */ import modelo.validaciones.TxtVBuscador;
/*      */ import modelo.validaciones.Validar;
/*      */ import net.sf.jasperreports.engine.JRException;
/*      */ import net.sf.jasperreports.engine.JasperReport;
/*      */ import utils.CONS;
/*      */ import vista.alumno.FrmAlumnoCurso;
/*      */ import vista.curso.JDInfoHorario;
/*      */ import vista.curso.PnlHorarioClase;
/*      */ 
/*      */ public class FrmAlumnoCursoCTR extends DCTR {
/*      */   private final FrmAlumnoCurso frmAlmCurso;
/*   59 */   private final AlumnoCursoBD ALCBD = AlumnoCursoBD.single();
/*      */ 
/*      */   
/*   62 */   private int cicloCursado = 0;
/*   63 */   private int cicloReprobado = 0;
/*   64 */   private String materiasMatricula = "";
/*      */   
/*      */   DefaultTableModel mdMatPen;
/*      */   
/*      */   DefaultTableModel mdMatSelec;
/*      */   DefaultTableModel mdAlm;
/*   70 */   private final PeriodoLectivoBD PLBD = PeriodoLectivoBD.single();
/*      */   
/*      */   private ArrayList<PeriodoLectivoMD> periodos;
/*   73 */   private final AlumnoCarreraBD ALCRBD = AlumnoCarreraBD.single();
/*      */   
/*      */   private ArrayList<AlumnoCarreraMD> alumnosCarrera;
/*   76 */   private final CursoBD CBD = CursoBD.single();
/*      */   
/*      */   private ArrayList<CursoMD> cursosPen;
/*      */   private ArrayList<String> nombreCursos;
/*   80 */   private ArrayList<CursoMD> cursosSelec = new ArrayList<>();
/*      */   
/*   82 */   private final MallaAlumnoBD MABD = MallaAlumnoBD.single(); private ArrayList<MallaAlumnoMD> mallaCompleta;
/*      */   private ArrayList<MallaAlumnoMD> mallaPerdidas;
/*      */   private ArrayList<MallaAlumnoMD> mallaMatriculadas;
/*      */   private ArrayList<MallaAlumnoMD> mallaCursadas;
/*      */   private ArrayList<MallaAlumnoMD> mallaAnuladas;
/*      */   private ArrayList<MallaAlumnoMD> mallaPendientes;
/*      */   private ArrayList<MateriaRequisitoMD> requisitos;
/*      */   private ArrayList<SesionClaseMD> horarioAlmn;
/*      */   private ArrayList<SesionClaseMD> horario;
/*      */   private ArrayList<SesionClaseMD> hcurso;
/*   92 */   private final SesionClaseBD SCBD = SesionClaseBD.single();
/*      */   
/*   94 */   private final MateriaRequisitoBD MRBD = MateriaRequisitoBD.single();
/*      */   
/*   96 */   private final MatriculaBD MTBD = MatriculaBD.single();
/*      */   
/*   98 */   private int numMateria = 0;
/*      */   
/*  100 */   private final MateriaBD MBD = MateriaBD.single();
/*      */   private ArrayList<MateriaMD> materias;
/*      */   private Boolean perdioNE;
/*      */   private Boolean tieneTerceraMatricula;
/*      */   private CarreraMD carrera;
/*  105 */   private final CarreraBD CRBD = CarreraBD.single();
/*      */   
/*      */   private List<MallaAlumnoMD> mallaAlmnNoPagadas;
/*  108 */   private final UtilEgresadoBD UEBD = UtilEgresadoBD.single(); private boolean choque; private String choques; private String estadoMateria; public void iniciar() { cargarCmbPrdLectivo(); ocultarErrores(); iniciarTbls(); iniciarAcciones(); buscadoresEstado(false); iniciarBuscador(); this.frmAlmCurso.getBtnAnuladas().setVisible(false); inicarValidaciones(); VtnLblToolTip.agregarTooltipsLblJI((JInternalFrame)this.frmAlmCurso); this.ctrPrin.agregarVtn((JInternalFrame)this.frmAlmCurso); this.frmAlmCurso.setTitle("Matriculado por: " + CONS.USUARIO.getUsername()); } private void iniciarTbls() { String[] titulo1 = { "Materias no seleccionadas", "M", "#" }; String[] titulo2 = { "Materias seleccionadas", "C" }; String[] tituloAlmn = { "Cédula", "Alumnos" }; String[][] datos1 = new String[0][]; this.mdMatPen = TblEstilo.modelTblSinEditar(datos1, titulo1); this.mdMatSelec = TblEstilo.modelTblSinEditar(datos1, titulo2); this.mdAlm = TblEstilo.modelTblSinEditar(datos1, tituloAlmn); this.frmAlmCurso.getTblMateriasPen().setModel(this.mdMatPen); this.frmAlmCurso.getTblMateriasSelec().setModel(this.mdMatSelec); this.frmAlmCurso.getTblAlumnos().setModel(this.mdAlm); TblEstilo.formatoTblMatricula(this.frmAlmCurso.getTblMateriasPen()); TblEstilo.formatoTblMatricula(this.frmAlmCurso.getTblMateriasSelec()); TblEstilo.formatoTbl(this.frmAlmCurso.getTblAlumnos()); TblEstilo.columnaMedida(this.frmAlmCurso.getTblAlumnos(), 0, 100); TblEstilo.columnaMedida(this.frmAlmCurso.getTblMateriasSelec(), 1, 40); TblEstilo.columnaMedida(this.frmAlmCurso.getTblMateriasPen(), 1, 40); TblEstilo.columnaMedida(this.frmAlmCurso.getTblMateriasPen(), 2, 20); this.frmAlmCurso.getTblMateriasPen().getColumnModel().getColumn(2).setCellRenderer((TableCellRenderer)new TblRenderNumMatricula(2)); } private void iniciarAcciones() { this.frmAlmCurso.getTblAlumnos().addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { FrmAlumnoCursoCTR.this.clickTblAlumnos(); } }
/*      */       ); this.frmAlmCurso.getCmbPrdLectivo().addActionListener(e -> clickPrdLectivo()); this.frmAlmCurso.getCmbCurso().addActionListener(e -> cargarMaterias()); this.frmAlmCurso.getBtnPasar1().addActionListener(e -> pasarUnaMateria()); this.frmAlmCurso.getBtnPasarTodos().addActionListener(e -> pasarTodasMaterias()); this.frmAlmCurso.getBtnRegresar1().addActionListener(e -> regresarUnaMateria()); this.frmAlmCurso.getBtnRegresarTodos().addActionListener(e -> regresarTodasMaterias()); this.frmAlmCurso.getBtnReprobadas().addActionListener(e -> mostrarInformacion("R")); this.frmAlmCurso.getBtnHorarioCurso().addActionListener(e -> clickHorario()); this.frmAlmCurso.getBtnBuscar().addActionListener(e -> buscarAlumnos(this.frmAlmCurso.getTxtBuscar().getText().trim())); this.frmAlmCurso.getBtnMtCursadas().addActionListener(e -> mostrarInformacion("C")); this.frmAlmCurso.getBtnAnuladas().addActionListener(e -> mostrarInformacion("A")); this.frmAlmCurso.getBtnPendientes().addActionListener(e -> mostrarInformacion("P")); this.frmAlmCurso.getBtnHorarioAlmn().addActionListener(e -> horarioAlmn()); this.frmAlmCurso.getBtnGuardar().addActionListener(e -> guardar()); this.frmAlmCurso.getBtnChoques().addActionListener(e -> mostrarChoque()); }
/*      */   private void inicarValidaciones() { this.frmAlmCurso.getTxtBuscar().addKeyListener((KeyListener)new TxtVBuscador(this.frmAlmCurso.getTxtBuscar(), this.frmAlmCurso.getLblErrorBuscar(), this.frmAlmCurso.getBtnBuscar())); this.frmAlmCurso.getCmbPrdLectivo().addActionListener((ActionListener)new CmbValidar(this.frmAlmCurso.getCmbPrdLectivo(), this.frmAlmCurso.getLblErrorPrdLectivo())); }
/*      */   private void iniciarBuscador() { this.frmAlmCurso.getTxtBuscar().addKeyListener(new KeyAdapter() { public void keyReleased(KeyEvent e) { String aguja = FrmAlumnoCursoCTR.this.frmAlmCurso.getTxtBuscar().getText().trim(); if (e.getKeyCode() == 10) { FrmAlumnoCursoCTR.this.buscarAlumnos(aguja); } else if (aguja.length() == 0) { FrmAlumnoCursoCTR.this.limpiarFrm(); }  } }
/*      */       ); }
/*      */   private void guardar() { boolean guardar = true; String error = "El formulario contiene errores."; if (this.cursosSelec.isEmpty()) { guardar = false; JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Debe seleccionar materias."); }  int posAlm = this.frmAlmCurso.getTblAlumnos().getSelectedRow(); if (posAlm < 0) guardar = false;  int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); if (posPrd < 1) guardar = false;  int posTipo = this.frmAlmCurso.getCmbTipoMatricula().getSelectedIndex(); if (posTipo < 1) guardar = false;  if (this.tieneTerceraMatricula.booleanValue()) { guardar = validarTercerasMatriculas(); if (!guardar) error = "Debe matricularse en su tercera matricula.";  }  borrarChoques(this.cursosSelec); if (guardar) guardar = validarCoRequisitos();  if (guardar) { this.ALCBD.borrarMatricula(); this.materiasMatricula = ""; this.numMateria = 1; this.cursosSelec.forEach(c -> { this.ALCBD.agregarMatricula(((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno().getId_Alumno(), c.getId(), c.getNumMatricula()); this.materiasMatricula += this.numMateria + ":   Curso: " + c.getNombre() + "  Matricula: " + c.getNumMatricula() + "  Materia: " + c.getMateria().getNombre() + "    \n"; this.numMateria++; }); int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Se matricula a: \n" + ((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno().getNombreCorto() + "\nPeriodo: \n" + ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getNombre() + "\nEn las siguientes materias: \n" + this.materiasMatricula); if (r == 0) { MatriculaMD m = this.MTBD.buscarMatriculaAlmnPrd(((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno().getId_Alumno(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()); if (m == null) { MatriculaMD matricula = new MatriculaMD(); matricula.setAlumno(((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno()); matricula.setPeriodo(this.periodos.get(posPrd - 1)); matricula.setTipo(this.frmAlmCurso.getCmbTipoMatricula().getSelectedItem().toString()); this.MTBD.ingresar(matricula); }  if (this.ALCBD.guardarAlmnCurso()) { limpiarFrm(); int c = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Desea imprimir la matricula."); if (c == 0) llamaReporteMatricula(((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno().getIdentificacion(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID());  }  }  } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), error); }  }
/*      */   private void limpiarFrm() { this.frmAlmCurso.getTxtBuscar().setText(""); this.frmAlmCurso.getCmbCurso().removeAllItems(); this.mdAlm.setRowCount(0); this.mdMatPen.setRowCount(0); this.mdMatSelec.setRowCount(0); this.cursosSelec = new ArrayList<>(); this.horarioAlmn = new ArrayList<>(); this.frmAlmCurso.getBtnReprobadas().setVisible(false); this.frmAlmCurso.getBtnAnuladas().setVisible(false); this.frmAlmCurso.getBtnPendientes().setEnabled(false); this.frmAlmCurso.getBtnMtCursadas().setEnabled(false); }
/*      */   private ArrayList<CursoMD> borrarChoques(ArrayList<CursoMD> cursos) { int[] posElim = new int[cursos.size()]; int i; for (i = 0; i < cursos.size(); i++) { if (((CursoMD)cursos.get(i)).getNombre().charAt(0) == 'C') posElim[i] = i + 1;  }  for (i = 0; i < posElim.length; i++) { if (posElim[i] > 0) { cursos.remove(posElim[i] - 1); posElim = posElim(posElim); }  }  return cursos; }
/*      */   private void ocultarErrores() { this.frmAlmCurso.getLblErrorBuscar().setVisible(false); this.frmAlmCurso.getLblErrorPrdLectivo().setVisible(false); this.frmAlmCurso.getBtnReprobadas().setVisible(false); }
/*      */   private void buscadoresEstado(boolean estado) { this.frmAlmCurso.getTxtBuscar().setEnabled(estado); this.frmAlmCurso.getBtnBuscar().setEnabled(estado); this.frmAlmCurso.getBtnMtCursadas().setEnabled(estado); this.frmAlmCurso.getBtnPendientes().setEnabled(estado); }
/*      */   private void cargarCmbPrdLectivo() { this.periodos = this.PLBD.cargarPrdParaCmbFrm(); if (this.periodos != null) { this.frmAlmCurso.getCmbPrdLectivo().removeAllItems(); this.frmAlmCurso.getCmbPrdLectivo().addItem("Seleccione"); this.periodos.forEach(p -> this.frmAlmCurso.getCmbPrdLectivo().addItem(p.getNombre())); }  }
/*  119 */   public FrmAlumnoCursoCTR(FrmAlumnoCurso frmAlmCurso, VtnPrincipalCTR ctrPrin) { super(ctrPrin);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1234 */     this.choques = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1350 */     this.estadoMateria = ""; this.frmAlmCurso = frmAlmCurso; } private void clickPrdLectivo() { int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); if (posPrd > 0) { buscadoresEstado(true); this.frmAlmCurso.getLblNumMatriculas().setText(this.MTBD.numMaticulados(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()) + ""); this.frmAlmCurso.getLblNumMatriculasClases().setText(this.MTBD.numMaticuladosClases(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()) + ""); this.carrera = this.CRBD.buscarPorId(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getCarrera().getId()); mostrarInfoCarrera(this.carrera); limpiarFrm(); } else { buscadoresEstado(false); }  } private void mostrarInfoCarrera(CarreraMD carrera) { if (carrera != null) { this.frmAlmCurso.setTitle("Matricula | " + carrera.getNombre() + " - " + carrera.getModalidad()); } else { this.frmAlmCurso.setTitle("Matricula "); }  } private void buscarAlumnos(String aguja) { int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); if (posPrd > 0 && Validar.esLetrasYNumeros(aguja)) { this.alumnosCarrera = this.ALCRBD.buscarAlumnoCarreraParaFrm(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getCarrera().getId(), aguja); llenarTblAlumnos(this.alumnosCarrera); }  } private void clickTblAlumnos() { int posAl = this.frmAlmCurso.getTblAlumnos().getSelectedRow(); int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); if (posAl >= 0) { this.mdMatPen.setRowCount(0); this.mdMatSelec.setRowCount(0); this.cursosSelec = new ArrayList<>(); this.horarioAlmn = new ArrayList<>(); this.tieneTerceraMatricula = Boolean.valueOf(false); this.frmAlmCurso.getBtnPendientes().setEnabled(true); this.frmAlmCurso.getBtnMtCursadas().setEnabled(true); this.mallaCompleta = this.MABD.buscarMallaAlumnoParaEstado(((AlumnoCarreraMD)this.alumnosCarrera.get(posAl)).getId()); this.mallaMatriculadas = filtrarMalla(this.mallaCompleta, "M"); String msg = ""; this.mallaAlmnNoPagadas = this.UEBD.getMateriasNoPagadas(((AlumnoCarreraMD)this.alumnosCarrera.get(posAl)).getId()); msg = this.mallaAlmnNoPagadas.stream().map(ma -> "Ciclo: " + ma.getMateria().getCiclo() + "  # Matricula: " + ma.getMallaNumMatricula() + "  Materia: " + ma.getMateria().getNombre() + " \n").reduce(msg, String::concat); if (msg.length() > 0) msg = "Matricula que tiene pendiente su pago:\n" + msg;  String matriculasPagar = this.MTBD.getMatriculasAPagar(((AlumnoCarreraMD)this.alumnosCarrera.get(posAl)).getId()); if (matriculasPagar.length() > 0) msg = msg + "\nMatriculas a pagar: \n" + matriculasPagar;  if (msg.length() > 0) JOptionPane.showMessageDialog((Component)this.frmAlmCurso, msg);  if (!this.mallaMatriculadas.isEmpty()) { this.frmAlmCurso.getCmbCurso().removeAllItems(); int s = JOptionPane.showOptionDialog((Component)this.ctrPrin.getVtnPrin(), "Alumno matriculado en " + ((MallaAlumnoMD)this.mallaMatriculadas.get(0)).getMallaCiclo() + " ciclo. \n¿Ver materias en las que se encuentra matriculado?", "Alumno matriculado", 1, 1, null, new Object[] { "Ingresar otro alumno", "Ingresar en otro curso", "Ver materias", "Cancelar" }, "Ver materias"); switch (s) { case 0: this.frmAlmCurso.getTxtBuscar().setText(""); this.mdAlm.setRowCount(0); break;case 1: clasificarMateriasAlmn(posPrd); break;case 2: mostrarInformacion("M"); break; }  } else { clasificarMateriasAlmn(posPrd); }  }  } private void clasificarMateriasAlmn(int posPrd) { this.perdioNE = Boolean.valueOf(false); this.ctrPrin.getVtnPrin().getLblEstado().setText("Clasificando cursos... "); this.cicloCursado = 0; this.mallaCursadas = filtrarMalla(this.mallaCompleta, "C"); if (this.mallaCursadas != null) for (int i = 0; i < this.mallaCursadas.size(); i++) { if (((MallaAlumnoMD)this.mallaCursadas.get(i)).getMallaCiclo() > this.cicloCursado) this.cicloCursado = ((MallaAlumnoMD)this.mallaCursadas.get(i)).getMallaCiclo();  }   this.cicloReprobado = this.cicloCursado; this.cicloCursado++; this.mallaPerdidas = filtrarMalla(this.mallaCompleta, "R"); this.mallaMatriculadas = filtrarMalla(this.mallaCompleta, "M"); this.mallaAnuladas = filtrarMalla(this.mallaCompleta, "A"); this.mallaPendientes = filtrarMalla(this.mallaCompleta, "P"); this.materias = this.MBD.cargarMateriaPorCarreraFrm(this.carrera.getId()); if (this.cicloCursado == this.cicloReprobado) this.cicloReprobado++;  if (this.mallaPerdidas.size() > 0) { this.frmAlmCurso.getBtnReprobadas().setVisible(true); this.mallaPerdidas.forEach(m -> { if (this.carrera.getModalidad().equals("DUAL")) { boolean p = perdioNucleoEstruncturante(m.getMateria().getId()); if (!this.perdioNE.booleanValue()) this.perdioNE = Boolean.valueOf(p);  }  if (m.getMallaCiclo() < this.cicloReprobado) this.cicloReprobado = m.getMallaCiclo();  }); } else { this.frmAlmCurso.getBtnReprobadas().setVisible(false); }  if (this.mallaAnuladas.size() > 0) { this.frmAlmCurso.getBtnAnuladas().setVisible(true); this.mallaAnuladas.forEach(m -> { if (m.getMallaCiclo() < this.cicloReprobado) this.cicloReprobado = m.getMallaCiclo();  }); } else { this.frmAlmCurso.getBtnAnuladas().setVisible(false); }  if (this.mallaPendientes.size() > 0) { this.frmAlmCurso.getBtnPendientes().setVisible(true); this.mallaPendientes.forEach(m -> { if (m.getMallaCiclo() < this.cicloReprobado) this.cicloReprobado = m.getMallaCiclo();  }); } else { this.frmAlmCurso.getBtnPendientes().setVisible(false); }  if (this.perdioNE.booleanValue()) { System.out.println("Ciclo en el que curso antes: " + this.cicloCursado + " /// Reprobo: " + this.cicloReprobado); System.out.println("Ciclo en el que curso: " + this.cicloCursado + " /// Reprobo: " + this.cicloReprobado); }  ArrayList<MallaAlumnoMD> tm = filtrarTercerasMatriculas(this.mallaPerdidas); if (tm.size() > 0) { this.tieneTerceraMatricula = Boolean.valueOf(true); int s = JOptionPane.showOptionDialog((Component)this.ctrPrin.getVtnPrin(), "El alumno tiene terceras matriculas \n¿Ver materias en las que debe realizar tercera matricula?", "Alumno con tercera matricula", 1, 1, null, new Object[] { "Permitir matricula", "Ver materias", "Cancelar" }, "Ver materias"); switch (s) { case 0: tm.forEach(m -> { if (m.getMallaCiclo() < this.cicloCursado) this.cicloCursado = m.getMallaCiclo();  }); this.cicloCursado--; cargarCmbCursos(posPrd, this.cicloCursado, this.cicloReprobado); break;case 1: mostrarTercerasMatriculas(); break; }  } else { cargarCmbCursos(posPrd, this.cicloCursado, this.cicloReprobado); }  }
/*      */   private void mostrarInformacion(String estado) { int posAlm = this.frmAlmCurso.getTblAlumnos().getSelectedRow(); if (posAlm >= 0) { JDMateriasInformacionCTR jdCtr = new JDMateriasInformacionCTR(this.alumnosCarrera.get(posAlm), this.MABD, estado, this.ctrPrin); jdCtr.iniciar(); jdCtr.cargarMateriasEstado(); } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Primero debe seleccionar un alumno."); }  }
/*      */   private void mostrarTercerasMatriculas() { int posAlm = this.frmAlmCurso.getTblAlumnos().getSelectedRow(); if (posAlm >= 0) { JDMateriasInformacionCTR jdCtr = new JDMateriasInformacionCTR(this.alumnosCarrera.get(posAlm), this.MABD, "R", this.ctrPrin); jdCtr.iniciar(); jdCtr.cargarTercerasMatriculas(); } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Primero debe seleccionar un alumno."); }  }
/*      */   private void llenarTblAlumnos(ArrayList<AlumnoCarreraMD> alumnos) { this.mdAlm.setRowCount(0); if (alumnos != null) alumnos.forEach(a -> { Object[] valores = { a.getAlumno().getIdentificacion(), a.getAlumno().getPrimerApellido() + " " + a.getAlumno().getSegundoApellido() + " " + a.getAlumno().getPrimerNombre() + " " + a.getAlumno().getSegundoNombre() }; this.mdAlm.addRow(valores); });  }
/*      */   private void cargarCmbCursos(int posPrd, int cicloCursado, int cicloReprobado) { this.frmAlmCurso.getCmbCurso().removeAllItems(); this.nombreCursos = this.CBD.cargarNombreCursosPorPeriodo(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID(), cicloReprobado, cicloCursado); if (this.nombreCursos != null) { this.frmAlmCurso.getCmbCurso().addItem("Seleccione"); this.nombreCursos.forEach(c -> this.frmAlmCurso.getCmbCurso().addItem(c)); this.ctrPrin.getVtnPrin().getLblEstado().setText("Cursos clasificados correctamente."); } else { this.ctrPrin.getVtnPrin().getLblEstado().setText("No se pudieron clasificar los cursos, por favor vuelva a seleccionar un alumno."); }  }
/*      */   private void cargarMaterias() { int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); int posCurso = this.frmAlmCurso.getCmbCurso().getSelectedIndex(); int posAlm = this.frmAlmCurso.getTblAlumnos().getSelectedRow(); if (posPrd > 0 && posCurso > 0 && posAlm >= 0) { this.cursosPen = this.CBD.buscarCursosPorNombreYPrdLectivo(this.frmAlmCurso.getCmbCurso().getSelectedItem().toString(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID(), ((AlumnoCarreraMD)this.alumnosCarrera.get(posAlm)).getAlumno().getId_Alumno()); this.requisitos = this.MRBD.buscarRequisitosPorCarrera(((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getCarrera().getId()); this.hcurso = this.SCBD.cargarHorarioCurso(this.frmAlmCurso.getCmbCurso().getSelectedItem().toString(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()); clasificarMateriasPendientes(this.cursosPen); } else { this.mdMatPen.setRowCount(0); }  }
/* 1356 */   private String estadoMateriaEnMalla(int idMateria) { this.estadoMateria = null;
/* 1357 */     this.mallaCompleta.forEach(m -> {
/*      */           if (m.getMateria().getId() == idMateria) {
/*      */             this.estadoMateria = m.getEstado();
/*      */           }
/*      */         });
/* 1362 */     return this.estadoMateria; }
/*      */   private void clasificarMateriasPendientes(ArrayList<CursoMD> cursos) { this.mdMatPen.setRowCount(0); if (cursos != null) { int i; for (i = 0; i < this.cursosSelec.size(); i++) { for (int j = 0; j < cursos.size(); j++) { if (((CursoMD)this.cursosSelec.get(i)).getMateria().getId() == ((CursoMD)cursos.get(j)).getMateria().getId()) { cursos.remove(j); break; }  }  }  for (i = 0; i < this.mallaMatriculadas.size(); i++) { for (int j = 0; j < cursos.size(); j++) { if (((MallaAlumnoMD)this.mallaMatriculadas.get(i)).getMateria().getId() == ((CursoMD)cursos.get(j)).getMateria().getId()) { cursos.remove(j); break; }  }  }  if (!this.perdioNE.booleanValue()) { for (i = 0; i < this.mallaCursadas.size(); i++) { for (int j = 0; j < cursos.size(); j++) { if (((MallaAlumnoMD)this.mallaCursadas.get(i)).getMateria().getId() == ((CursoMD)cursos.get(j)).getMateria().getId()) { cursos.remove(j); break; }  }  }  } else { for (i = 0; i < this.mallaCursadas.size(); i++) { for (int j = 0; j < cursos.size(); j++) { if (((MallaAlumnoMD)this.mallaCursadas.get(i)).getMateria().getId() == ((CursoMD)cursos.get(j)).getMateria().getId() && !perdioNucleoEstruncturante(((MallaAlumnoMD)this.mallaCursadas.get(i)).getMateria().getId())) { cursos.remove(j); break; }  }  }  }  if (!cursos.isEmpty()) if (((CursoMD)cursos.get(0)).getCiclo() > 1) { llenarTblConRequisitosPasados(cursos); } else { llenarTblMateriasPendientes(cursos); }   }  }
/*      */   private boolean perdioNucleoEstruncturante(int idMateria) { boolean perdio = false; if (this.materias != null) for (int i = 0; i < this.materias.size(); i++) { if (((MateriaMD)this.materias.get(i)).getId() == idMateria && ((MateriaMD)this.materias.get(i)).isMateriaNucleo()) { perdio = true; break; }  }   return perdio; }
/*      */   private void llenarTblConRequisitosPasados(ArrayList<CursoMD> cursos) { int[] posElim = new int[cursos.size()]; int i; for (i = 0; i < cursos.size(); i++) { ArrayList<MateriaRequisitoMD> requisitosFiltrados = filtrarRequisitos(((CursoMD)cursos.get(i)).getMateria().getId(), "P"); for (int j = 0; j < requisitosFiltrados.size(); j++) { this.estadoMateria = estadoMateriaEnMalla(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getId()); if (this.estadoMateria != null && !this.estadoMateria.equals("C")) posElim[i] = i + 1;  }  }  for (i = 0; i < posElim.length; i++) { if (posElim[i] > 0) { cursos.remove(posElim[i] - 1); posElim = posElim(posElim); }  }  this.cursosPen = cursos; if (cursos.size() > 0) llenarTblConCoRequisitos(cursos);  }
/*      */   public int[] posElim(int[] posElim) { int[] pos = new int[posElim.length]; for (int i = 0; i < posElim.length; i++)
/*      */       pos[i] = posElim[i] - 1;  return pos; }
/*      */   private void llenarTblConCoRequisitos(ArrayList<CursoMD> cursos) { int[] posElim = new int[cursos.size()]; int i; for (i = 0; i < cursos.size(); i++) { ArrayList<MateriaRequisitoMD> requisitosFiltrados = filtrarRequisitos(((CursoMD)cursos.get(i)).getMateria().getId(), "C"); boolean matricula = true; if (requisitosFiltrados.size() > 0)
/*      */         matricula = false;  for (int j = 0; j < requisitosFiltrados.size(); j++) { this.estadoMateria = estadoMateriaEnMalla(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getId()); if (!this.estadoMateria.equals("C") && !this.estadoMateria.equals("R") && !this.estadoMateria.equals("M")) { int k; for (k = 0; k < cursos.size(); k++) { if (((CursoMD)cursos.get(k)).getMateria().getNombre().equals(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getNombre())) { matricula = true; break; }  }  for (k = 0; k < this.cursosSelec.size(); k++) { if (((CursoMD)this.cursosSelec.get(k)).getMateria().getNombre().equals(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getNombre())) { matricula = true; break; }  }  } else { matricula = true; }  }  if (!matricula)
/* 1370 */         posElim[i] = i + 1;  }  for (i = 0; i < posElim.length; i++) { if (posElim[i] > 0) { cursos.remove(posElim[i] - 1); posElim = posElim(posElim); }  }  llenarTblMateriasPendientes(cursos); } private ArrayList<MateriaRequisitoMD> filtrarRequisitos(int idMateria, String tipo) { ArrayList<MateriaRequisitoMD> filtrados = new ArrayList<>();
/* 1371 */     this.requisitos.forEach(m -> {
/*      */           if (m.getMateria().getId() == idMateria && m.getTipo().equals(tipo)) {
/*      */             filtrados.add(m);
/*      */           }
/*      */         });
/* 1376 */     return filtrados; }
/*      */   private boolean validarCoRequisitos() { boolean matricula = true; String mensaje = ""; for (int i = 0; i < this.cursosSelec.size(); i++) { ArrayList<MateriaRequisitoMD> requisitosFiltrados = filtrarRequisitos(((CursoMD)this.cursosSelec.get(i)).getMateria().getId(), "C"); matricula = true; if (requisitosFiltrados.size() > 0) matricula = false;  for (int j = 0; j < requisitosFiltrados.size(); j++) { this.estadoMateria = estadoMateriaEnMalla(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getId()); if (!this.estadoMateria.equals("C") && !this.estadoMateria.equals("M")) { for (int k = 0; k < this.cursosSelec.size(); k++) { if (((CursoMD)this.cursosSelec.get(k)).getMateria().getNombre().equals(((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getNombre())) { matricula = true; break; }  mensaje = mensaje + ((CursoMD)this.cursosSelec.get(k)).getMateria().getNombre() + " tiene como co-requisito: \n" + ((MateriaRequisitoMD)requisitosFiltrados.get(j)).getMateriaRequisito().getNombre() + "\n"; }  } else { matricula = true; }  }  }  if (!matricula) { mensaje = mensaje + "\nDebe matricularse en las materias con su co-requisito correspondiente."; JOptionPane.showMessageDialog(null, mensaje); return false; }  return true; }
/*      */   public boolean validarTercerasMatriculas() { boolean guardar = false; for (int i = 0; i < this.cursosSelec.size(); i++) { if (((CursoMD)this.cursosSelec.get(i)).getNumMatricula() == 3) { guardar = true; break; }  }  return guardar; }
/*      */   public void llenarTblMateriasPendientes(ArrayList<CursoMD> cursos) { this.cursosPen = cursos; this.mdMatPen.setRowCount(0); if (!cursos.isEmpty()) cursos.forEach(c -> { c.setNumMatricula(buscarNumeroMatricula(c.getMateria().getId())); Object[] valores = { c.getMateria().getNombre(), Integer.valueOf(c.getCapaciadActual()), Integer.valueOf(c.getNumMatricula()) }; this.mdMatPen.addRow(valores); });  }
/*      */   private void llenarTblMatSelec(ArrayList<CursoMD> cursosSelec) { this.mdMatSelec.setRowCount(0); if (cursosSelec != null) cursosSelec.forEach(c -> { Object[] valores = { c.getMateria().getNombre(), c.getNombre() }; this.mdMatSelec.addRow(valores); });  }
/*      */   private void pasarUnaMateria() { int posMat = this.frmAlmCurso.getTblMateriasPen().getSelectedRow(); if (posMat >= 0) { if (((CursoMD)this.cursosPen.get(posMat)).getCapaciadActual() > 0) { this.horario = buscarHorarioCurso(this.cursosPen.get(posMat)); if (((CursoMD)this.cursosPen.get(posMat)).getNombre().charAt(0) != 'C') if (chocanHoras(this.horario)) { ((CursoMD)this.cursosPen.get(posMat)).setNombre("C-" + ((CursoMD)this.cursosPen.get(posMat)).getNombre()); } else { llenarHorarioAlmn(this.horario); }   this.cursosSelec.add(this.cursosPen.get(posMat)); this.cursosPen.remove(posMat); llenarTblMateriasPendientes(this.cursosPen); llenarTblMatSelec(this.cursosSelec); } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "No puede matricular en este curso, debido \na que no se disponen de mas cupos."); }  } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Seleecione una materia."); }  }
/*      */   private void pasarTodasMaterias() { ArrayList<CursoMD> csc = new ArrayList<>(); this.cursosPen.forEach(c -> { if (c.getCapaciadActual() > 0) { this.horario = buscarHorarioCurso(c); if (c.getNombre().charAt(0) != 'C') if (chocanHoras(this.horario)) { c.setNombre("C-" + c.getNombre()); } else { llenarHorarioAlmn(this.horario); }   this.cursosSelec.add(c); } else { csc.add(c); }  }); this.cursosPen = csc; llenarTblMateriasPendientes(this.cursosPen); llenarTblMatSelec(this.cursosSelec); }
/*      */   private void regresarUnaMateria() { int posMat = this.frmAlmCurso.getTblMateriasSelec().getSelectedRow(); if (posMat >= 0) { this.cursosPen.add(this.cursosSelec.get(posMat)); if (((CursoMD)this.cursosSelec.get(posMat)).getNombre().charAt(0) != 'C') { this.horario = buscarHorarioCurso(this.cursosSelec.get(posMat)); quitarHorarioAlmn(this.horario); }  this.cursosSelec.remove(posMat); llenarTblMateriasPendientes(this.cursosPen); llenarTblMatSelec(this.cursosSelec); } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Seleecione una materia."); }  }
/* 1384 */   private void regresarTodasMaterias() { this.cursosSelec = new ArrayList<>(); this.horarioAlmn = new ArrayList<>(); llenarTblMatSelec(this.cursosSelec); cargarMaterias(); this.choques = ""; } public void clickHorario() { int posPrd = this.frmAlmCurso.getCmbPrdLectivo().getSelectedIndex(); int posCurso = this.frmAlmCurso.getCmbCurso().getSelectedIndex(); if (posPrd > 0 && posCurso > 0) { JDInfoHorario jd = new JDInfoHorario((Frame)this.ctrPrin.getVtnPrin(), false); PnlHorarioClase pnl = new PnlHorarioClase(); CambioPnlCTR.cambioPnl(jd.getPnlHorario(), (JPanel)pnl); PnlHorarioCursoCTR ctr = new PnlHorarioCursoCTR(pnl, this.frmAlmCurso.getCmbCurso().getSelectedItem().toString(), ((PeriodoLectivoMD)this.periodos.get(posPrd - 1)).getID()); ctr.iniciar(); jd.setLocationRelativeTo((Component)this.ctrPrin.getVtnPrin()); jd.setVisible(true); jd.setTitle("Horario de " + this.frmAlmCurso.getCmbCurso().getSelectedItem().toString()); this.ctrPrin.eventoJDCerrar((JDialog)jd); } else { JOptionPane.showMessageDialog((Component)this.ctrPrin.getVtnPrin(), "Seleccione un curso primero."); }  } public boolean chocanHoras(ArrayList<SesionClaseMD> horario) { this.choque = false; this.horarioAlmn.forEach(h -> horario.forEach(())); return this.choque; } public void llenarHorarioAlmn(ArrayList<SesionClaseMD> horario) { horario.forEach(h -> this.horarioAlmn.add(h)); } public void quitarHorarioAlmn(ArrayList<SesionClaseMD> horario) { horario.forEach(h -> { for (int i = 0; i < this.horarioAlmn.size(); i++) { if (h.getId() == ((SesionClaseMD)this.horarioAlmn.get(i)).getId()) { this.horarioAlmn.remove(i); break; }  }  }); } public void horarioAlmn() { PnlHorarioClase pnl = new PnlHorarioClase(); JDInfoHorario jd = new JDInfoHorario((Frame)this.ctrPrin.getVtnPrin(), false); CambioPnlCTR.cambioPnl(jd.getPnlHorario(), (JPanel)pnl); PnlHorarioAlmnCTR ctr = new PnlHorarioAlmnCTR(this.horarioAlmn, pnl); jd.setTitle("Horario Alumno "); ctr.iniciar(); jd.setVisible(true); this.ctrPrin.eventoJDCerrar((JDialog)jd); } private void llamaReporteMatricula(String cedula, int idPrd) { try { JasperReport jr = (JasperReport)JRLoader.loadObject(getClass().getResource("/vista/reportes/repImpresionMatricula.jasper")); Map<Object, Object> parametro = new HashMap<>(); parametro.put("cedula", cedula); parametro.put("idPeriodo", Integer.valueOf(idPrd)); parametro.put("usuario", CONS.USUARIO.getUsername()); CON.mostrarReporte(jr, parametro, "Reporte de Matricula"); } catch (JRException ex) { JOptionPane.showMessageDialog(null, "error" + ex); }  } private ArrayList<MallaAlumnoMD> filtrarMalla(ArrayList<MallaAlumnoMD> mallaCompleta, String estado) { ArrayList<MallaAlumnoMD> mf = new ArrayList<>(); mallaCompleta.forEach(m -> { if (m.getEstado().equals(estado)) mf.add(m);  }); return mf; } private ArrayList<MallaAlumnoMD> filtrarTercerasMatriculas(ArrayList<MallaAlumnoMD> mallaPerdidas) { ArrayList<MallaAlumnoMD> mf = new ArrayList<>(); mallaPerdidas.forEach(m -> { if (m.getMallaNumMatricula() == 2) mf.add(m);  }); return mf; } private int buscarNumeroMatricula(int idMateria) { int num = -1;
/* 1385 */     for (int i = 0; i < this.mallaCompleta.size(); i++) {
/* 1386 */       if (((MallaAlumnoMD)this.mallaCompleta.get(i)).getMateria().getId() == idMateria) {
/* 1387 */         num = ((MallaAlumnoMD)this.mallaCompleta.get(i)).getMallaNumMatricula();
/*      */         break;
/*      */       } 
/*      */     } 
/* 1391 */     num++;
/* 1392 */     return num; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayList<SesionClaseMD> buscarHorarioCurso(CursoMD curso) {
/* 1402 */     ArrayList<SesionClaseMD> hc = new ArrayList<>();
/* 1403 */     if (this.hcurso != null) {
/* 1404 */       this.hcurso.forEach(h -> {
/*      */             if (h.getCurso().getId() == curso.getId()) {
/*      */               h.setCurso(curso);
/*      */               hc.add(h);
/*      */             } 
/*      */           });
/*      */     }
/* 1411 */     return hc;
/*      */   }
/*      */   
/*      */   private void mostrarChoque() {
/* 1415 */     JOptionPane.showMessageDialog((Component)this.frmAlmCurso, "Choches: \n" + this.choques);
/*      */   }
/*      */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\FrmAlumnoCursoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */