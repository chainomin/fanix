/*     */ package controlador.materia;
/*     */ 
/*     */ import controlador.principal.DVtnCTR;
/*     */ import controlador.principal.VtnPrincipalCTR;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import modelo.materia.MateriaBD;
/*     */ import modelo.materia.MateriaMD;
/*     */ import modelo.materia.MateriaRequisitoBD;
/*     */ import modelo.materia.MateriaRequisitoMD;
/*     */ import vista.materia.FrmRequisitos;
/*     */ 
/*     */ 
/*     */ public class VtnRequisitosCTR
/*     */   extends DVtnCTR
/*     */ {
/*     */   private final FrmRequisitos frmreq;
/*  20 */   private final MateriaBD MTBD = MateriaBD.single();
/*     */   
/*  22 */   private final MateriaRequisitoBD MTRBD = MateriaRequisitoBD.single();
/*  23 */   private int idRequisito = -1;
/*     */ 
/*     */   
/*     */   private boolean editar = false;
/*     */ 
/*     */   
/*     */   private MateriaMD materia;
/*     */   
/*     */   private ArrayList<MateriaMD> materias;
/*     */ 
/*     */   
/*     */   public VtnRequisitosCTR(VtnPrincipalCTR ctrPrin, FrmRequisitos frmreq, MateriaMD materia) {
/*  35 */     super(ctrPrin);
/*  36 */     this.frmreq = frmreq;
/*  37 */     this.materia = materia;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void iniciar() {
/*  45 */     this.frmreq.getLblNombreMateria().setText(this.materia.getNombre());
/*     */     
/*  47 */     this.frmreq.getBtnGuardar().addActionListener(e -> guardarMateriaRequisito());
/*  48 */     this.ctrPrin.agregarVtn((JInternalFrame)this.frmreq);
/*     */ 
/*     */     
/*  51 */     this.frmreq.getJrbCoRequisito().addActionListener(e -> cargarComboMaterias());
/*  52 */     this.frmreq.getJrbPrerequisito().addActionListener(e -> cargarComboMaterias());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cargarComboMaterias() {
/*  59 */     boolean co = this.frmreq.getJrbCoRequisito().isSelected();
/*  60 */     boolean pre = this.frmreq.getJrbPrerequisito().isSelected();
/*     */     
/*  62 */     if (co) {
/*  63 */       this.materias = this.MTBD.getMateriasParaCorequisito(this.materia.getId());
/*     */     }
/*     */     
/*  66 */     if (pre) {
/*  67 */       this.materias = this.MTBD.getMateriasParaPrequisitos(this.materia.getId());
/*     */     }
/*     */ 
/*     */     
/*  71 */     this.frmreq.getCmbrequisitos().removeAllItems();
/*  72 */     this.frmreq.getCmbrequisitos().addItem("Seleccione");
/*     */     
/*  74 */     this.materias.forEach(m -> this.frmreq.getCmbrequisitos().addItem(m.getNombre()));
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
/*     */   public void guardarMateriaRequisito() {
/*  87 */     boolean guardar = true;
/*     */ 
/*     */ 
/*     */     
/*  91 */     int posicion = this.frmreq.getCmbrequisitos().getSelectedIndex();
/*     */ 
/*     */     
/*  94 */     MateriaRequisitoMD mr = new MateriaRequisitoMD();
/*  95 */     mr.setMateria(this.materia);
/*     */     
/*  97 */     if (posicion > 0) {
/*  98 */       this.materia = this.materias.get(posicion - 1);
/*  99 */       mr.setMateriaRequisito(this.materia);
/*     */     } else {
/* 101 */       guardar = false;
/* 102 */       JOptionPane.showMessageDialog(null, "Seleccione los datos");
/*     */     } 
/*     */ 
/*     */     
/* 106 */     if (this.frmreq.getJrbCoRequisito().isSelected()) {
/* 107 */       String tipo = "C";
/* 108 */       mr.setTipo(tipo);
/* 109 */     } else if (this.frmreq.getJrbPrerequisito().isSelected()) {
/* 110 */       String tipo = "P";
/* 111 */       mr.setTipo(tipo);
/*     */     } else {
/* 113 */       guardar = false;
/*     */     } 
/* 115 */     if (guardar)
/*     */     {
/* 117 */       if (this.editar) {
/* 118 */         mr.setId(this.idRequisito);
/* 119 */         this.MTRBD.editar(mr);
/*     */       }
/* 121 */       else if (this.MTRBD.insertarMateriaRequisito(mr)) {
/* 122 */         JOptionPane.showMessageDialog(null, "Datos guardados correctamente");
/* 123 */         this.frmreq.dispose();
/*     */       } else {
/* 125 */         JOptionPane.showMessageDialog(null, "La Informacion existe, Por favor seleccione otros datos");
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
/*     */   public void editar(MateriaRequisitoMD mr) {
/* 138 */     this.editar = true;
/* 139 */     this.idRequisito = mr.getId();
/* 140 */     if (mr.getTipo().equalsIgnoreCase("C")) {
/* 141 */       this.frmreq.getJrbCoRequisito().setSelected(true);
/*     */     } else {
/* 143 */       this.frmreq.getJrbPrerequisito().setSelected(true);
/*     */     } 
/*     */     
/* 146 */     this.frmreq.getCmbrequisitos().setSelectedItem(mr.getMateria().getNombre());
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\materia\VtnRequisitosCTR.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */