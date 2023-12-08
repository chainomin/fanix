/*     */ package controlador.Libraries;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Validaciones
/*     */ {
/*  19 */   public static int NUMERO_ENTERO = 0;
/*  20 */   public static int NUMERO_DECIMAL = 1;
/*  21 */   public static int NUMERO_DECIMAL_LIMIT = 2; private static final String INT = "^[-|+]{0,1}[0-9]+[ ]*";
/*     */   
/*     */   public static boolean isInt(String Number) {
/*  24 */     return Number.matches("^[0-9]*");
/*     */   }
/*     */   private static final String DECIMAL = "^[+]{0,1}[0-9]*+[.]{0,1}+[0-9]{1,2}+[ ]*"; private static final String WORD = "^[A-Za-z]*+[ ]*";
/*     */   public static boolean isDecimal(String Number) {
/*  28 */     if (Number.equalsIgnoreCase(".") || Number.equalsIgnoreCase(",")) {
/*  29 */       Number = "0";
/*     */     }
/*  31 */     return Number.matches("^[\\d]*+[.]{0,1}+[\\d]*");
/*     */   }
/*     */   
/*     */   public static boolean isDecimalLimit(String number, int minimun, int maximun) {
/*  35 */     return number.matches("^[0-9]*+[.]{0,1}+[0-9]{" + minimun + "," + maximun + "}");
/*     */   }
/*     */   
/*     */   public static void validarJtextField(final JTextComponent component, final String errorMessage, final JComponent previewComponent, final int validationType) {
/*  39 */     component.addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  42 */             String text = component.getText();
/*  43 */             if (validationType == Validaciones.NUMERO_ENTERO) {
/*     */               
/*  45 */               if (!Validaciones.isInt(text)) {
/*  46 */                 Validaciones.error(component, errorMessage, previewComponent);
/*     */               }
/*     */             }
/*  49 */             else if (validationType == Validaciones.NUMERO_DECIMAL) {
/*     */               
/*  51 */               if (!Validaciones.isDecimal(text)) {
/*  52 */                 Validaciones.error(component, errorMessage, previewComponent);
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void validarDecimalJtextField(final JTextComponent component, final String errorMessage, final JComponent previewComponent, final int mimun, final int maximun) {
/*  61 */     component.addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/*  64 */             String text = component.getText();
/*     */             
/*  66 */             if (!Validaciones.isDecimalLimit(text, mimun, maximun)) {
/*  67 */               Validaciones.error(component, errorMessage, previewComponent);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void error(JTextComponent component, String errorMessage, JComponent previewComponent) {
/*  75 */     JOptionPane.showMessageDialog(previewComponent, errorMessage);
/*  76 */     component.setText("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static String WORDS = "^[A-Za-z]*+[ ]{1}";
/*     */ 
/*     */   
/*     */   private static final String NUMBER = "^[0-9]*+[ ]*";
/*     */ 
/*     */   
/*     */   private static final String EMAIL = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInt(String Number, long Max) {
/*  96 */     if (isInt(Number)) {
/*  97 */       long number = Long.parseLong(Number);
/*  98 */       return (number <= Max);
/*     */     } 
/*     */     
/* 101 */     System.out.println("is Not a Number");
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInt(String Number, long Min, long Max) {
/* 113 */     if (isInt(Number)) {
/* 114 */       long number = Long.parseLong(Number);
/* 115 */       return (number >= Min && number <= Max);
/*     */     } 
/*     */     
/* 118 */     System.out.println("is Not a Number");
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDecimal(String Number, double Max) {
/* 128 */     if (isDecimal(Number)) {
/* 129 */       double number = Double.parseDouble(Number);
/* 130 */       return (number <= Max);
/*     */     } 
/*     */     
/* 133 */     System.out.println("is Not a Number");
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDecimal(String Number, double Min, double Max) {
/* 144 */     if (isDecimal(Number)) {
/* 145 */       double number = Double.parseDouble(Number);
/* 146 */       return (number >= Min && number <= Max);
/*     */     } 
/*     */     
/* 149 */     System.out.println("is Not a Number");
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWord(String Text) {
/* 159 */     return Text.matches("^[A-Za-z]*+[ ]*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWord(String Text, int Words) {
/* 170 */     for (int i = 0; i < Words - 1; i++) {
/* 171 */       if (i == Words - 2) {
/* 172 */         WORDS += "+[A-Za-z]*+[ ]*";
/*     */       } else {
/* 174 */         WORDS += "+[A-Za-z]*+[ ]{1}";
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 179 */     return Text.matches(WORDS);
/*     */   }
/*     */   
/*     */   public static boolean isNumber(String Text) {
/* 183 */     return Text.matches("^[0-9]*+[ ]*");
/*     */   }
/*     */   
/*     */   public static boolean isNumber(String Text, int Max) {
/* 187 */     if (isNumber(Text)) {
/* 188 */       return (Text.length() == Max);
/*     */     }
/*     */     
/* 191 */     System.out.println("Is Not a number");
/* 192 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isEmail(String Email) {
/* 196 */     return Email.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isCedula(String cedula) {
/*     */     boolean Cedula;
/*     */     try {
/* 203 */       if (cedula.length() == 10) {
/*     */         
/* 205 */         int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
/* 206 */         if (tercerDigito < 6) {
/*     */ 
/*     */           
/* 209 */           int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
/* 210 */           int verificador = Integer.parseInt(cedula.substring(9, 10));
/* 211 */           int suma = 0;
/* 212 */           int digito = 0;
/* 213 */           for (int i = 0; i < cedula.length() - 1; i++) {
/* 214 */             digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
/* 215 */             suma += digito % 10 + digito / 10;
/*     */           } 
/* 217 */           if (suma % 10 == 0 && suma % 10 == verificador) {
/* 218 */             Cedula = true;
/*     */           } else {
/* 220 */             Cedula = (10 - suma % 10 == verificador);
/*     */           } 
/*     */         } else {
/* 223 */           Cedula = false;
/*     */         } 
/*     */       } else {
/* 226 */         Cedula = false;
/*     */       } 
/* 228 */     } catch (NumberFormatException nfe) {
/* 229 */       Cedula = false;
/* 230 */     } catch (Exception err) {
/* 231 */       System.out.println("Una excepcion ocurrio en el proceso de validadcion");
/* 232 */       Cedula = false;
/*     */     } 
/*     */     
/* 235 */     if (!Cedula) {
/* 236 */       System.out.println("La Cédula ingresada es Incorrecta");
/*     */     }
/*     */     
/* 239 */     return Cedula;
/*     */   }
/*     */   
/*     */   public static boolean isRuc(String RUC) {
/* 243 */     if (RUC.length() == 13) {
/*     */       
/* 245 */       String Cedula = RUC.substring(0, 10);
/* 246 */       String LastCharacters = RUC.substring(10, 13);
/*     */       
/* 248 */       if (isCedula(Cedula)) {
/* 249 */         return LastCharacters.equals("001");
/*     */       }
/*     */       
/* 252 */       return false;
/*     */     } 
/*     */     
/* 255 */     System.out.println("Is Not RUC");
/* 256 */     return false;
/*     */   }
/*     */   
/*     */   public static Double getDoubleFromJFTXTfield(String Number) {
/* 260 */     Number = Number.replace(",", ".");
/* 261 */     return Double.valueOf(Double.parseDouble(Number));
/*     */   }
/*     */   
/*     */   public static KeyAdapter validarNumeros() {
/* 265 */     return new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e)
/*     */         {
/* 269 */           if (!Character.isDigit(e.getKeyChar())) {
/* 270 */             e.consume();
/* 271 */             Toolkit.getDefaultToolkit().beep();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static boolean validarPalabras(List<String> palabrasValidas, String palabra) {
/* 278 */     return 
/*     */ 
/*     */ 
/*     */       
/* 282 */       !((List)palabrasValidas.stream().filter(item -> item.toUpperCase().contains(palabra.toUpperCase())).collect(Collectors.toList())).isEmpty();
/*     */   }
/*     */   
/*     */   public static KeyAdapter validarSoloLetrasYnumeros() {
/* 286 */     return new KeyAdapter()
/*     */       {
/*     */         public void keyTyped(KeyEvent e) {
/* 289 */           char caracter = e.getKeyChar();
/* 290 */           if (!Character.isDigit(caracter) && !Character.isLetter(caracter))
/* 291 */             e.consume(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\medar\Desktop\.JAR FENIX\Zero.jar!\controlador\Libraries\Validaciones.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */