/*    */ package controlador.Libraries;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import java.sql.Connection;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import javax.swing.JTable;
/*    */ import modelo.ConnDBPool;
/*    */ import net.sf.jasperreports.engine.JRException;
/*    */ import net.sf.jasperreports.engine.JasperFillManager;
/*    */ import net.sf.jasperreports.engine.JasperPrint;
/*    */ import net.sf.jasperreports.engine.JasperReport;
/*    */ import net.sf.jasperreports.engine.util.JRLoader;
/*    */ import net.sf.jasperreports.view.JasperViewer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Middlewares
/*    */ {
/* 28 */   private static ConnDBPool pool = new ConnDBPool();
/*    */ 
/*    */   
/*    */   private static Connection conn;
/*    */ 
/*    */   
/*    */   public static BiFunction<JTable, String, Integer> getNombre;
/*    */ 
/*    */ 
/*    */   
/*    */   public static void generarReporte(URL path, String title, Map params) {
/*    */     try {
/* 40 */       JasperReport jasper = (JasperReport)JRLoader.loadObject(path);
/* 41 */       conn = pool.getConnection();
/* 42 */       JasperPrint print = JasperFillManager.fillReport(jasper, params, conn);
/*    */       
/* 44 */       JasperViewer view = new JasperViewer(print, false);
/*    */       
/* 46 */       view.setTitle(title);
/* 47 */       view.setVisible(true);
/*    */     }
/* 49 */     catch (JRException ex) {
/* 50 */       System.out.println(ex.getMessage());
/*    */     } finally {
/* 52 */       pool.close(conn);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String getProjectPath() {
/* 57 */     String path = (new File(".")).getAbsolutePath();
/* 58 */     return path.substring(0, path.length() - 1);
/*    */   }
/*    */   
/*    */   public static double conversor(String texto) {
/* 62 */     if (texto.isEmpty()) {
/* 63 */       texto = "99999";
/*    */     }
/* 65 */     if (texto.equalsIgnoreCase(".") || texto.equalsIgnoreCase(",")) {
/* 66 */       texto = "0";
/*    */     }
/* 68 */     return Math.round(Double.valueOf(texto).doubleValue() * 10.0D) / 10.0D;
/*    */   }
/*    */   
/*    */   public static String capitalize(String texto) {
/* 72 */     if (texto.length() > 1) {
/* 73 */       return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
/*    */     }
/* 75 */     return texto.toUpperCase();
/*    */   }
/*    */   
/*    */   static {
/* 79 */     getNombre = ((tabla, nombre) -> Integer.valueOf(tabla.getColumnModel().getColumnIndex(nombre)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static String simpleDateFormat(LocalDateTime date) {
/* 84 */     return date.getDayOfWeek().getValue() + "-" + date.getMonth().getValue() + "-" + date.getYear();
/*    */   }
/*    */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\Middlewares.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */