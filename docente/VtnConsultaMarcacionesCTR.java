/*     */ package controlador.docente;
/*     */ 
/*     */ import controlador.Libraries.Effects;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.LocalTime;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.ConnDBPool;
/*     */ import modelo.persona.DocenteBD;
/*     */ import modelo.persona.DocenteMD;
/*     */ import modelo.usuario.HistorialUsuarioBD;
/*     */ import modelo.usuario.ListaMarcacionesBD;
/*     */ import modelo.usuario.ListaMarcacionesMD;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JasperFillManager;
/*     */ import net.sf.jasperreports.engine.JasperPrint;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import net.sf.jasperreports.view.JasperViewer;
/*     */ import utils.CONS;
/*     */ import vista.docente.VtnConsultaMarcaciones;
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
/*     */ public class VtnConsultaMarcacionesCTR
/*     */ {
/*     */   private final VtnPrincipalCTR desktop;
/*     */   private final VtnConsultaMarcaciones vista;
/*     */   private List<ListaMarcacionesMD> listaMarcaciones;
/*     */   private static DefaultTableModel tabla;
/*     */   private boolean cargarTabla = true;
/*     */   String consulta;
/*     */   Date fechaI;
/*     */   Date fechaF;
/*     */   String cedula;
/*     */   
/*     */   public VtnConsultaMarcacionesCTR(VtnPrincipalCTR desktop, VtnConsultaMarcaciones vista) {
/*  57 */     this.desktop = desktop;
/*  58 */     this.vista = vista;
/*     */   }
/*     */ 
/*     */   
/*     */   public void Init() {
/*  63 */     Effects.addInDesktopPane((JInternalFrame)this.vista, this.desktop.getVtnPrin().getDpnlPrincipal());
/*     */     
/*  65 */     this.vista.setTitle("Reporte de Marcaciones");
/*     */     
/*  67 */     tabla = (DefaultTableModel)this.vista.getTblMarcaciones().getModel();
/*     */     
/*  69 */     this.vista.getDtcDesde().setDate(new Date());
/*  70 */     this.vista.getDtcHasta().setDate(new Date());
/*  71 */     initListeners();
/*  72 */     InitPermisos();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initListeners() {
/*  77 */     KeyListener txtlistener = new KeyListener()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void keyPressed(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void keyReleased(KeyEvent e) {
/*  91 */           VtnConsultaMarcacionesCTR.this.cargaDocente();
/*     */         }
/*     */       };
/*  94 */     this.vista.getBtnAplicar().addActionListener(l -> cargarTabla());
/*  95 */     this.vista.getTxtCedulaDocente().addKeyListener(txtlistener);
/*  96 */     this.vista.getBtnImprimr().addActionListener(l -> reporteMarcaciones());
/*     */   }
/*     */   private void cargaDocente() {
/*  99 */     int largo = this.vista.getTxtCedulaDocente().getText().length();
/* 100 */     if (largo == 10) {
/* 101 */       String cedula = this.vista.getTxtCedulaDocente().getText();
/* 102 */       DocenteMD docente = DocenteBD.single().buscarDocenteNombres(cedula);
/* 103 */       if (docente != null) {
/* 104 */         this.vista.getLblNombreDocente().setText(docente.getNombreCompleto());
/* 105 */         this.vista.getBtnAplicar().requestFocus();
/*     */       } else {
/* 107 */         this.vista.getLblNombreDocente().setText("TODOS");
/*     */       } 
/*     */     } else {
/* 110 */       this.vista.getLblNombreDocente().setText("TODOS");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cargarTabla() {
/* 115 */     HistorialUsuarioBD.single().registroMarca(CONS.USUARIO.getUsername(), this.vista.getLblNombreDocente().getText());
/* 116 */     tabla.setRowCount(0);
/* 117 */     this.fechaI = this.vista.getDtcDesde().getDate();
/* 118 */     this.fechaF = this.vista.getDtcHasta().getDate();
/* 119 */     this.cedula = this.vista.getTxtCedulaDocente().getText();
/* 120 */     Effects.setLoadCursor((Container)this.vista);
/* 121 */     this.listaMarcaciones = ListaMarcacionesBD.listadoMarcaciones(this.fechaI, this.fechaF, this.cedula);
/*     */     
/* 123 */     this.listaMarcaciones.stream().forEach(agregaFilas());
/* 124 */     Effects.setDefaultCursor((Container)this.vista);
/*     */   }
/*     */   
/*     */   private Consumer<ListaMarcacionesMD> agregaFilas() {
/* 128 */     return obj -> tabla.addRow(new Object[] { obj.getFechaMarcacion(), obj.getCedulaDocente(), obj.getNombreDocente(), obj.getEntradaUno(), obj.getSalidaUno(), obj.getEntradaDos(), obj.getSalidaDos(), Integer.valueOf(obj.getHoras()), Integer.valueOf(obj.getMinutos()), calculaTiempo(obj.getHoras(), obj.getMinutos(), obj.getTiempoDedicacion()) });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String calculaTiempo(int hour, int minutes, int limite) {
/* 152 */     LocalTime currentTime = LocalTime.of(hour, minutes);
/*     */ 
/*     */     
/* 155 */     LocalTime targetTime = LocalTime.of(limite, 0);
/*     */ 
/*     */     
/* 158 */     long minutesDifference = currentTime.until(targetTime, ChronoUnit.MINUTES);
/*     */ 
/*     */     
/* 161 */     if (minutesDifference <= 0L) {
/* 162 */       return "Alcanzado las " + limite + " horas.";
/*     */     }
/*     */     
/* 165 */     long hours = minutesDifference / 60L;
/* 166 */     long remainingMinutes = minutesDifference % 60L;
/*     */     
/* 168 */     return "Faltan " + hours + " horas y " + remainingMinutes + " minutos para " + limite + " horas.";
/*     */   }
/*     */ 
/*     */   
/*     */   private void reporteMarcaciones() {
/* 173 */     cargarTabla();
/* 174 */     String path = "/vista/reportes/repMarcaciones2.jasper";
/*     */ 
/*     */     
/*     */     try {
/* 178 */       ConnDBPool cpg = new ConnDBPool();
/* 179 */       JasperReport jr = (JasperReport)JRLoader.loadObject(
/* 180 */           getClass().getResource(path));
/* 181 */       Map<String, Object> parametros = new HashMap<>();
/* 182 */       parametros.put("fechai", fechaFormato(this.fechaI));
/* 183 */       parametros.put("fechaf", fechaFormato(this.fechaF));
/*     */ 
/*     */       
/* 186 */       String filtroDocente = " ";
/* 187 */       if (this.vista.getLblNombreDocente().getText() != "TODOS") {
/* 188 */         filtroDocente = " and docente_id=(select id_docente from \"Docentes\" where docente_codigo='" + this.vista.getTxtCedulaDocente().getText() + "')";
/*     */       }
/* 190 */       parametros.put("filtro_docente", filtroDocente);
/* 191 */       parametros.put("consulta", " AND fecha_hora between '" + fechaFormato(this.fechaI) + " 00:00:00' and '" + fechaFormato(this.fechaF) + " 23:59:59'");
/*     */       
/* 193 */       JasperPrint jp = JasperFillManager.fillReport(jr, parametros, cpg
/*     */ 
/*     */           
/* 196 */           .getConnection());
/* 197 */       jp.setName("Reporte de Marcaciones del " + fechaFormato(this.fechaI) + " al " + fechaFormato(this.fechaF) + " " + this.vista.getLblNombreDocente().getText());
/* 198 */       JasperViewer jv = new JasperViewer(jp, false);
/* 199 */       jv.setVisible(true);
/*     */     }
/* 201 */     catch (JRException ex) {
/* 202 */       JOptionPane.showMessageDialog(null, "Error en reporte: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String fechaFormato(Date fecha) {
/* 208 */     SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
/* 209 */     String fechaHoraFormateada = formato.format(fecha);
/* 210 */     return fechaHoraFormateada;
/*     */   }
/*     */   
/*     */   private void InitPermisos() {}
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\docente\VtnConsultaMarcacionesCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */