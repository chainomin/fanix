/*    */ package controlador.alumno;
/*    */ 
/*    */ import controlador.principal.VtnPrincipalCTR;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.KeyAdapter;
/*    */ import java.awt.event.KeyEvent;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JInternalFrame;
/*    */ import modelo.alumno.Egresado;
/*    */ 
/*    */ public class VtnAlumnoGraduadoCTR
/*    */   extends AVtnAlumnoEgresadoCTR
/*    */   implements IAlumnoEgresadoVTNCTR
/*    */ {
/* 16 */   private static final String[] TITULO = new String[] { "Carrera", "Periodo", "Cédula", "Alumno", "Fecha Graduacion" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final JDEgresarAlumnoCTR ECTR;
/*    */ 
/*    */ 
/*    */   
/*    */   public VtnAlumnoGraduadoCTR(VtnPrincipalCTR ctrPrin) {
/* 25 */     super(ctrPrin);
/* 26 */     this.ECTR = new JDEgresarAlumnoCTR(ctrPrin);
/*    */   }
/*    */   
/*    */   public void iniciar() {
/* 30 */     iniciarVtn(TITULO, this);
/* 31 */     cargarDatos();
/* 32 */     iniciarBuscador();
/* 33 */     iniciarAcciones();
/* 34 */     this.vtn.setTitle("Alumnos Graduados");
/* 35 */     this.ctrPrin.agregarVtn((JInternalFrame)this.vtn);
/* 36 */     this.vtnCargada = true;
/*    */   }
/*    */   
/*    */   private void cargarDatos() {
/* 40 */     this.todosEgresados = this.EBD.getAllGraduados();
/* 41 */     this.egresados = this.todosEgresados;
/* 42 */     llenarTbl(this.egresados);
/*    */   }
/*    */   
/*    */   private void iniciarBuscador() {
/* 46 */     this.vtn.getBtnBuscar().addActionListener(e -> buscar(this.vtn.getTxtBuscar().getText().trim()));
/*    */ 
/*    */     
/* 49 */     this.vtn.getTxtBuscar().addKeyListener(new KeyAdapter()
/*    */         {
/*    */           public void keyReleased(KeyEvent e) {
/* 52 */             VtnAlumnoGraduadoCTR.this.buscar(VtnAlumnoGraduadoCTR.this.vtn.getTxtBuscar().getText().trim());
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void buscar(String aguja) {
/* 58 */     this.egresados = new ArrayList<>();
/* 59 */     this.todosEgresados.forEach(e -> {
/*    */           if (e.getAlmnCarrera().getAlumno().getNombreCompleto().toLowerCase().contains(aguja.toLowerCase())) {
/*    */             this.egresados.add(e);
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 66 */     llenarTbl(this.egresados);
/*    */   }
/*    */ 
/*    */   
/*    */   public void llenarTbl(List<Egresado> egresados) {
/* 71 */     this.mdTbl.setRowCount(0);
/* 72 */     if (egresados != null) {
/* 73 */       egresados.forEach(r -> {
/*    */             Object[] valores = { r.getAlmnCarrera().getCarrera().getCodigo(), r.getPeriodo().getNombre(), r.getAlmnCarrera().getAlumno().getIdentificacion(), r.getAlmnCarrera().getAlumno().getPrimerApellido() + " " + r.getAlmnCarrera().getAlumno().getSegundoApellido() + " " + r.getAlmnCarrera().getAlumno().getPrimerNombre() + " " + r.getAlmnCarrera().getAlumno().getSegundoNombre(), r.getFechaGraduacion().toString() };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             this.mdTbl.addRow(valores);
/*    */           });
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 86 */       this.vtn.getLblResultados().setText(egresados.size() + " Resultados obtenidos.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\alumno\VtnAlumnoGraduadoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */