/*     */ package controlador.accesos;
/*     */ 
/*     */ import controlador.Libraries.Effects;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.accesosDelRol.AccesosDelRolBD;
/*     */ import modelo.usuario.RolBD;
/*     */ import utils.CONS;
/*     */ import vista.accesos.FrmAccesosDeRol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrmAccesosAddCTR
/*     */ {
/*     */   private final VtnPrincipalCTR desktop;
/*     */   private final FrmAccesosDeRol vista;
/*     */   private final AccesosDelRolBD modelo;
/*     */   private RolBD rol;
/*     */   private List<AccesosDelRolBD> listaPermisos;
/*     */   private DefaultTableModel tblPermisos;
/*     */   
/*     */   public FrmAccesosAddCTR(VtnPrincipalCTR destop) {
/*  33 */     this.desktop = destop;
/*  34 */     this.vista = new FrmAccesosDeRol();
/*  35 */     this.modelo = new AccesosDelRolBD();
/*     */   }
/*     */   
/*     */   public void setRol(RolBD rol) {
/*  39 */     this.rol = rol;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void Init() {
/*  45 */     Effects.addInDesktopPane((JInternalFrame)this.vista, this.desktop.getVtnPrin().getDpnlPrincipal());
/*  46 */     this.tblPermisos = (DefaultTableModel)this.vista.getTabPermisos().getModel();
/*  47 */     this.listaPermisos = this.modelo.selectWhere(this.rol.getId());
/*  48 */     cargarTabla();
/*  49 */     this.vista.getLblRolSeleccionado().setText(this.rol.getNombre());
/*  50 */     InitEventos();
/*     */   }
/*     */ 
/*     */   
/*     */   private void InitEventos() {
/*  55 */     this.vista.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  58 */             FrmAccesosAddCTR.this.cargarTablaFilter(FrmAccesosAddCTR.this.vista.getTxtBuscar().getText().toLowerCase());
/*     */           }
/*     */         });
/*     */     
/*  62 */     this.tblPermisos.addTableModelListener(new TableModelListener()
/*     */         {
/*     */           boolean active = false;
/*     */           
/*     */           public void tableChanged(TableModelEvent e) {
/*  67 */             if (!this.active && e.getType() == 0) {
/*  68 */               this.active = true;
/*  69 */               FrmAccesosAddCTR.this.activarPermisos();
/*  70 */               this.active = false;
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  76 */     this.vista.getTxtBuscar().addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  79 */             FrmAccesosAddCTR.this.cargarTablaFilter(FrmAccesosAddCTR.this.vista.getTxtBuscar().getText());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getRow() {
/*  87 */     return this.vista.getTabPermisos().getSelectedRow();
/*     */   }
/*     */   
/*     */   private void cargarTabla() {
/*  91 */     this.tblPermisos.setRowCount(0);
/*  92 */     this.listaPermisos.stream().sorted(sorter()).forEach(agregarFilas());
/*     */   }
/*     */   
/*     */   private Consumer<AccesosDelRolBD> agregarFilas() {
/*  96 */     return obj -> {
/*     */         this.tblPermisos.addRow(new Object[] { Integer.valueOf(this.tblPermisos.getDataVector().size() + 1), obj.getAcceso().getNombre(), Boolean.valueOf(obj.isActivo()), Integer.valueOf(obj.getId()) });
/*     */         this.vista.getLblResultados().setText(this.tblPermisos.getDataVector().size() + " Resultados");
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private Comparator<AccesosDelRolBD> sorter() {
/* 108 */     return (item1, item2) -> item1.getAcceso().getNombre().compareTo(item2.getAcceso().getNombre());
/*     */   }
/*     */   
/*     */   private void cargarTablaFilter(String aguja) {
/* 112 */     this.tblPermisos.setRowCount(0);
/* 113 */     this.listaPermisos
/* 114 */       .stream()
/* 115 */       .filter(item -> item.getAcceso().getNombre().toLowerCase().contains(aguja.toLowerCase()))
/* 116 */       .sorted(sorter())
/* 117 */       .forEach(agregarFilas());
/*     */   }
/*     */ 
/*     */   
/*     */   private void activarPermisos() {
/* 122 */     (new Thread(() -> {
/*     */           int id = ((Integer)this.tblPermisos.getValueAt(getRow(), 3)).intValue();
/*     */           
/*     */           boolean activo = ((Boolean)this.tblPermisos.getValueAt(getRow(), 2)).booleanValue();
/*     */           
/*     */           AccesosDelRolBD acceso = this.listaPermisos.stream().filter(()).findFirst().get();
/*     */           
/*     */           acceso.setActivo(activo);
/*     */           if (acceso.editar()) {
/*     */             if (activo) {
/*     */               this.vista.getLblEstado().setText("SE HA DADO EL PERMISO: " + acceso.getAcceso().getNombre());
/*     */               this.vista.getLblEstado().setForeground(CONS.SUCCESS_COLOR);
/*     */             } else {
/*     */               this.vista.getLblEstado().setText("SE HA QUITADO EL PERMISO: " + acceso.getAcceso().getNombre());
/*     */               this.vista.getLblEstado().setForeground(CONS.ERROR_COLOR);
/*     */             } 
/*     */           }
/* 139 */         })).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\accesos\FrmAccesosAddCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */