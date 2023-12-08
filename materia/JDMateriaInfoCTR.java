/*     */ package controlador.materia;
/*     */ 
/*     */ import controlador.principal.DCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import modelo.estilo.TblEstilo;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.materia.MateriaRequisitoBD;
/*     */ import modelo.materia.MateriaRequisitoMD;
/*     */ import vista.materia.FrmRequisitos;
/*     */ import vista.materia.JDMateriaInfo;
/*     */ 
/*     */ public class JDMateriaInfoCTR
/*     */   extends DCTR
/*     */ {
/*     */   private final JDMateriaInfo vtnInfo;
/*  26 */   private final MateriaRequisitoBD MTRBD = MateriaRequisitoBD.single();
/*     */   
/*     */   private final MateriaMD m;
/*     */   private ArrayList<MateriaRequisitoMD> preRequisitos;
/*     */   private ArrayList<MateriaRequisitoMD> coRequisitos;
/*  31 */   private final MateriaBD MTBD = MateriaBD.single(); private int posFila; private String tipo;
/*     */   private String materia;
/*     */   private DefaultTableModel mdTblPre;
/*     */   private DefaultTableModel mdTblCo;
/*     */   
/*     */   public JDMateriaInfoCTR(MateriaMD m, VtnPrincipalCTR ctrPrin) {
/*  37 */     super(ctrPrin);
/*  38 */     this.m = m;
/*  39 */     this.vtnInfo = new JDMateriaInfo((Frame)ctrPrin.getVtnPrin(), false);
/*  40 */     this.vtnInfo.setLocationRelativeTo((Component)ctrPrin.getVtnPrin());
/*     */   }
/*     */   
/*     */   public void iniciar() {
/*  44 */     this.vtnInfo.getLblMateria().setText(this.m.getNombre());
/*  45 */     this.vtnInfo.getLblCarrera().setText(this.m.getCarrera().getNombre());
/*     */ 
/*     */     
/*  48 */     String[] titulo1 = { "Co-Requisitos" };
/*  49 */     String[] titulo2 = { "Pre-Requisitos" };
/*  50 */     String[][] datos1 = new String[0][];
/*     */     
/*  52 */     this.mdTblCo = TblEstilo.modelTblSinEditar(datos1, titulo1);
/*  53 */     this.mdTblPre = TblEstilo.modelTblSinEditar(datos1, titulo2);
/*     */     
/*  55 */     TblEstilo.formatoTblConColor(this.vtnInfo.getTblCoRequisitos());
/*  56 */     TblEstilo.formatoTblConColor(this.vtnInfo.getTblPreRequisitos());
/*     */     
/*  58 */     this.vtnInfo.getTblCoRequisitos().setModel(this.mdTblCo);
/*  59 */     this.vtnInfo.getTblPreRequisitos().setModel(this.mdTblPre);
/*     */     
/*  61 */     llenarTblCoRequisitos();
/*  62 */     llenarTblPreRequisitos();
/*  63 */     this.vtnInfo.setVisible(true);
/*     */     
/*  65 */     this.vtnInfo.getTblCoRequisitos().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  68 */             JDMateriaInfoCTR.this.clickCoRequisitos();
/*     */           }
/*     */         });
/*     */     
/*  72 */     this.vtnInfo.getTblPreRequisitos().addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  75 */             JDMateriaInfoCTR.this.clickPreRequisitos();
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.vtnInfo.getBtnEliminar().addActionListener(e -> eliminar());
/*  82 */     this.vtnInfo.getBtnEditar().addActionListener(e -> editar());
/*     */   }
/*     */ 
/*     */   
/*     */   private void editar() {
/*     */     try {
/*  88 */       if (this.posFila >= 0) {
/*  89 */         FrmRequisitos frmreq = new FrmRequisitos();
/*  90 */         VtnRequisitosCTR req = new VtnRequisitosCTR(this.ctrPrin, frmreq, this.m);
/*  91 */         req.iniciar();
/*  92 */         switch (this.tipo) {
/*     */           case "co-requisito":
/*  94 */             req.editar(this.coRequisitos.get(this.posFila));
/*     */             break;
/*     */           case "pre-requisito":
/*  97 */             req.editar(this.preRequisitos.get(this.posFila));
/*     */             break;
/*     */         } 
/*     */       
/*     */       } else {
/* 102 */         JOptionPane.showMessageDialog(null, "Por favor seleccione una materia para continuar");
/*     */       } 
/* 104 */     } catch (HeadlessException e) {
/* 105 */       System.out.println(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void eliminar() {
/*     */     try {
/* 114 */       if (this.posFila >= 0) {
/*     */         
/* 116 */         int r = JOptionPane.showConfirmDialog((Component)this.ctrPrin.getVtnPrin(), "Esta seguro que desea eliminar " + this.materia + "\ncomo " + this.tipo);
/*     */         
/* 118 */         if (r == 0) {
/* 119 */           switch (this.tipo) {
/*     */             case "co-requisito":
/* 121 */               this.MTRBD.eliminar(((MateriaRequisitoMD)this.coRequisitos.get(this.posFila)).getId());
/* 122 */               this.mdTblCo.removeRow(this.posFila);
/*     */               break;
/*     */             case "pre-requisito":
/* 125 */               this.MTRBD.eliminar(((MateriaRequisitoMD)this.preRequisitos.get(this.posFila)).getId());
/* 126 */               this.mdTblPre.removeRow(this.posFila);
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         }
/*     */       } else {
/* 133 */         JOptionPane.showMessageDialog(null, "Por favor seleccione una materia para continuar");
/*     */       } 
/* 135 */     } catch (HeadlessException e) {
/* 136 */       System.out.println(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickCoRequisitos() {
/* 141 */     this.posFila = this.vtnInfo.getTblCoRequisitos().getSelectedRow();
/* 142 */     if (this.posFila >= 0) {
/* 143 */       this.tipo = "co-requisito";
/* 144 */       this.materia = this.vtnInfo.getTblCoRequisitos().getValueAt(this.posFila, 0).toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clickPreRequisitos() {
/* 149 */     this.posFila = this.vtnInfo.getTblPreRequisitos().getSelectedRow();
/* 150 */     if (this.posFila >= 0) {
/* 151 */       this.tipo = "pre-requisito";
/* 152 */       this.materia = this.vtnInfo.getTblPreRequisitos().getValueAt(this.posFila, 0).toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void llenarTblCoRequisitos() {
/* 157 */     this.coRequisitos = this.MTRBD.buscarCoRequisitosDe(this.m.getId());
/* 158 */     this.mdTblCo.setRowCount(0);
/* 159 */     if (this.coRequisitos != null) {
/* 160 */       this.coRequisitos.forEach(mt -> {
/*     */             Object[] valores = { mt.getMateriaRequisito().getNombre() };
/*     */             this.mdTblCo.addRow(valores);
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private void llenarTblPreRequisitos() {
/* 168 */     this.preRequisitos = this.MTRBD.buscarPreRequisitosDe(this.m.getId());
/* 169 */     this.mdTblPre.setRowCount(0);
/* 170 */     if (this.preRequisitos != null)
/* 171 */       this.preRequisitos.forEach(mt -> {
/*     */             Object[] valores = { mt.getMateriaRequisito().getNombre() };
/*     */             this.mdTblPre.addRow(valores);
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\materia\JDMateriaInfoCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */