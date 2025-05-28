/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista;
import Controlador.VentaControlador;
import Modelo.Venta;
import Controlador.DetalleVentaControlador;
import Modelo.DetalleVenta;
import Controlador.ClienteControlador;
import Modelo.Cliente;
import Controlador.EmpleadoControlador;
import Modelo.Empleado;
import Controlador.ProductoControlador;
import Modelo.Producto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author kreiv
 */
public class VistaVenta extends javax.swing.JPanel {
    private final VentaControlador VentaControlador;
private final DetalleVentaControlador detalleVentaControlador;
private final EmpleadoControlador empleadoControlador;
private final ClienteControlador clienteControlador;
private final ProductoControlador productoControlador;
private Integer idEmpleadoSeleccionado = null;
private Integer idClienteSeleccionado = null;
private Integer idProductoSeleccionado = null;
private Timer timer; // Variable de instancia para el Timer
private boolean horabd = false;

   
    /**
     * Creates new form VistaVenta
     */
    public VistaVenta() {
        initComponents();
         this.VentaControlador = new VentaControlador();
    this.detalleVentaControlador = new DetalleVentaControlador();
    this.empleadoControlador = new EmpleadoControlador();
    this.clienteControlador = new ClienteControlador();
    this.productoControlador = new ProductoControlador();
    eventoComboProductos();
    
    selectorfechaVenta.setDate(new Date());
    ((JTextField) selectorfechaVenta.getDateEditor().getUiComponent()).setEditable(false);
    
    // Limpiar las filas vacías de tablaDetalles
    DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
    modelDetalles.setRowCount(0);
    
    cargarDatosTablaVentas();
    cargarClientes();
    cargarEmpleados();
    cargarProductos();
    actualizarHora();
    }
    
    private void limpiar() {
    textBuscar.setText("");
    idEmpleadoSeleccionado = null;
    selectorfechaVenta.setDate(new Date());
    
    // Limpiar la tabla de detalles
    tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));
    
    cargarDatosTablaVentas();
    cargarClientes();
    cargarEmpleados();
    cargarProductos();
    
    btnEliminar.setEnabled(true);
    btnGuardar.setEnabled(true);
    
    horabd = false; // Restablece para mostrar hora actual
    actualizarHora(); // Vuelve a iniciar el timer
}
    
    private void actualizarHora() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("America/Managua"));

    // Detener el timer anterior si existe
    if (timer != null) {
        timer.stop();
    }

    if (horabd) {
        return; // No actualiza la hora si está cargada desde la base de datos
    }

    timer = new Timer(1000, e -> {
        Date now = new Date();
        hora.setText(sdf.format(now));
    });
    timer.start();
}

private void cargarDatosTablaVentas() {
    List<Venta> ventas = VentaControlador.obtenerTodasVentas();

    if (ventas != null) {
        DefaultTableModel model = (DefaultTableModel) tablaVentas.getModel();
        model.setRowCount(0);

        for (Venta v : ventas) {
            Cliente cliente = clienteControlador.obtenerClientePorId(v.getIdCliente());
            String nombreCliente = cliente.getPrimerNombre() + " " + cliente.getPrimerApellido();
                       
            Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(v.getIdEmpleado());
            String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();
            
            Object[] row = {
                v.getIdVenta(),
                v.getFechaVenta(),
                nombreCliente,
                nombreEmpleado,
                v.getTotalVenta()
            };
            model.addRow(row);
        }
    }
}
private void cargarClientes() {
    try {
        // Obtener las categorías desde el controlador
        List<Cliente> clientes = clienteControlador.obtenerTodosClientes();

        // Limpiar el combo box por si tiene datos
        ComboClientes.removeAllItems();

        // Agregar cada categoría al combo box
        for (Cliente c : clientes) {
            ComboClientes.addItem(c.getPrimerNombre() + " " +c.getPrimerApellido()); // Mostrar el nombre
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
                "Error al cargar los clientes: " + e.getMessage());
    }
}
private void cargarEmpleados() {
    try {
        // Obtener las categorías desde el controlador
        List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();

        // Limpiar el combo box por si tiene datos
        ComboEmpleados.removeAllItems();

        // Agregar cada categoría al combo box
        for (Empleado e : empleados) {
            ComboEmpleados.addItem(e.getPrimerNombre() + " " +e.getPrimerApellido()); // Mostrar el nombre
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
                "Error al cargar los empleados: " + e.getMessage());
    }
}
private void cargarProductos() {
    try {
        // Obtener las categorías desde el controlador
        List<Producto> productos = productoControlador.obtenerTodosProductos();

        // Limpiar el combo box por si tiene datos
        ComboProductos.removeAllItems();

        // Agregar cada categoría al combo box
        for (Producto p : productos) {
            ComboProductos.addItem(p.getNombreProducto()); // Mostrar el nombre
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
                "Error al cargar los productos: " + e.getMessage());
    }
}
private void eventoComboProductos() {
    ComboProductos.addActionListener(e -> {
        // Obtener el índice seleccionado
        int indiceSeleccionado = ComboProductos.getSelectedIndex();

        if (indiceSeleccionado >= 0) { // Verificar que se haya seleccionado algo
            try {
                // Obtener la lista de categorías desde el controlador o memoria
                List<Producto> productos = productoControlador.obtenerTodosProductos();

                // Obtener el objeto de categoría correspondiente al índice seleccionado
                Producto productoSeleccionado = productos.get(indiceSeleccionado);

                // Actualizar la variable global con el ID de la categoría seleccionada
                idProductoSeleccionado = productoSeleccionado.getIdProducto();
                
                // Actualizar el campo textPrecio con el precio unitario del producto
                textPrecio.setText(String.valueOf(productoSeleccionado.getPrecioUnitario()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al seleccionar categoría: " + ex.getMessage());
            }
        }
    });
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        ComboClientes = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        ComboEmpleados = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        hora = new javax.swing.JLabel();
        selectorfechaVenta = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        ComboProductos = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        textPrecio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        textCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        textBuscar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ButtonQuitarDetalle = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVentas = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        jLabel1.setText("Cliente");

        ComboClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Empleado");

        ComboEmpleados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Fecha");

        hora.setText("hora");

        jLabel4.setText("Producto");

        ComboProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Precio");

        jLabel6.setText("Cantidad");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonAgregar(evt);
            }
        });

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaDetalles);

        textBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textBuscarkeyTyped(evt);
            }
        });

        jLabel7.setText("Buscar");

        ButtonQuitarDetalle.setText("Quitar Detalle");
        ButtonQuitarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonQuitarDetalle(evt);
            }
        });

        tablaVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Venta", "Fecha/Hora", "Cliente", "Vendedor", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVentasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaVentas);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonLimpiar(evt);
            }
        });

        btnGuardar.setText("Guardar Venta");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonGuardar(evt);
            }
        });

        btnEliminar.setText("Eliminar Venta");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonEliminar(evt);
            }
        });

        btnActualizar.setText("Actualizar Venta");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accionBotonActualizar(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addComponent(ComboClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(ComboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(hora))
                    .addComponent(selectorfechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(ComboProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(textPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonQuitarDetalle)
                        .addGap(48, 48, 48))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiar)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(hora)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ComboEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(selectorfechaVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAgregar)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(ButtonQuitarDetalle))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpiar)
                    .addComponent(btnGuardar)
                    .addComponent(btnEliminar)
                    .addComponent(btnActualizar))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void accionBotonGuardar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonGuardar
        // TODO add your handling code here:                                    
    try {
        // Obtener el índice seleccionado de clientes y empleados
        int indiceCliente = ComboClientes.getSelectedIndex();
        int indiceEmpleado = ComboEmpleados.getSelectedIndex();
        if (indiceCliente < 0 || indiceEmpleado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente y un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de clientes y empleados
        List<Cliente> clientes = clienteControlador.obtenerTodosClientes();
        List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
        int idCliente = clientes.get(indiceCliente).getIdCliente();
        int idEmpleado = empleados.get(indiceEmpleado).getIdEmpleado();

        // Obtener la fecha seleccionada
        Date fechaVenta = selectorfechaVenta.getDate();
        if (fechaVenta == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los detalles de la tabla tablaDetalles
        DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
        int rowCount = modelDetalles.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear lista de detalles y calcular total
        List<DetalleVenta> detalles = new ArrayList<>();
        float totalVenta = 0;
        for (int i = 0; i < rowCount; i++) {
            int idProducto = (int) modelDetalles.getValueAt(i, 0); // ID Producto como Integer
            float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue(); // Precio Unitario como Float
            int cantidad = (int) modelDetalles.getValueAt(i, 3); // Cantidad como Integer
            float subtotal = ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Subtotal como Float

            // Crear objeto DetalleVenta
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalles.add(detalle);

            totalVenta += subtotal;
        }

        // Crear y guardar la venta
        VentaControlador.crearVenta(idCliente, idEmpleado, fechaVenta, totalVenta, detalles);

        limpiar();

        // Recargar la tabla de ventas
        cargarDatosTablaVentas();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    
}
    }//GEN-LAST:event_accionBotonGuardar

    private void accionBotonEliminar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonEliminar
        // TODO add your handling code here:
        try {
        // Obtener el índice de la fila seleccionada en tablaVentas
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el idVenta de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tablaVentas.getModel();
        int idVenta = (int) model.getValueAt(filaSeleccionada, 0);

        // Confirmar con el usuario antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar la venta con ID " + idVenta + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar la venta
            VentaControlador.eliminarVenta(idVenta);

            // Recargar la tabla de ventas
            cargarDatosTablaVentas();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } 
    }//GEN-LAST:event_accionBotonEliminar

    private void accionBotonActualizar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonActualizar
        // TODO add your handling code here:
         try {
        // Obtener el índice de la fila seleccionada en tablaVentas
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el idVenta de la fila seleccionada
        DefaultTableModel modelVentas = (DefaultTableModel) tablaVentas.getModel();
        int idVenta = (int) modelVentas.getValueAt(filaSeleccionada, 0);

        // Obtener el índice seleccionado de clientes y empleados
        int indiceCliente = ComboClientes.getSelectedIndex();
        int indiceEmpleado = ComboEmpleados.getSelectedIndex();
        if (indiceCliente < 0 || indiceEmpleado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente y un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de clientes y empleados
        List<Cliente> clientes = clienteControlador.obtenerTodosClientes();
        List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
        int idCliente = clientes.get(indiceCliente).getIdCliente();
        int idEmpleado = empleados.get(indiceEmpleado).getIdEmpleado();

        // Obtener la fecha seleccionada
        Date fechaVenta = selectorfechaVenta.getDate();
        if (fechaVenta == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una fecha.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los detalles de la tabla tablaDetalles
        DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
        int rowCount = modelDetalles.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular el total de la venta
        float totalVenta = 0;
        for (int i = 0; i < rowCount; i++) {
            totalVenta += ((Number) modelDetalles.getValueAt(i, 4)).floatValue(); // Suma los subtotales
        }

        // Actualizar la venta principal
        VentaControlador.actualizarVenta(idVenta, idCliente, idEmpleado, fechaVenta, totalVenta);

        // Eliminar los detalles antiguos de la venta
        List<DetalleVenta> detallesAntiguos = detalleVentaControlador.obtenerTodosDetallesVenta();
        if (detallesAntiguos != null) {
            for (DetalleVenta detalle : detallesAntiguos) {
                if (detalle.getIdVenta() == idVenta) {
                    detalleVentaControlador.eliminarDetalleVenta(detalle.getIdDetalleVenta());
                }
            }
        }

        // Insertar los nuevos detalles
        List<DetalleVenta> nuevosDetalles = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            int idProducto = (int) modelDetalles.getValueAt(i, 0);
            float precioUnitario = ((Number) modelDetalles.getValueAt(i, 2)).floatValue();
            int cantidad = (int) modelDetalles.getValueAt(i, 3);

            // Crear y guardar el nuevo detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdVenta(idVenta);
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            nuevosDetalles.add(detalle);
            detalleVentaControlador.crearDetalleVenta(idVenta, idProducto, cantidad, precioUnitario);
        }

        // Limpiar la tabla de detalles y el formulario
        tablaDetalles.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID Producto", "Producto", "Precio Unitario", "Cantidad", "Subtotal"}));
        limpiar();

        // Recargar la tabla de ventas
        cargarDatosTablaVentas();

        // Habilitar botones nuevamente
        btnEliminar.setEnabled(true);
        btnGuardar.setEnabled(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_accionBotonActualizar

    private void accionBotonLimpiar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonLimpiar
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_accionBotonLimpiar

    private void textBuscarkeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBuscarkeyTyped
        // TODO add your handling code here:
         String textoBusqueda = textBuscar.getText().trim().toLowerCase();
    List<Venta> ventas = VentaControlador.obtenerTodasVentas();

    DefaultTableModel modelo = (DefaultTableModel) tablaVentas.getModel();
    modelo.setRowCount(0);

    if (ventas != null) {
        for (Venta v : ventas) {
            Cliente cliente = clienteControlador.obtenerClientePorId(v.getIdCliente());
            String nombreCliente = cliente.getPrimerNombre() + " " + cliente.getPrimerApellido();
                       
            Empleado empleado = empleadoControlador.obtenerEmpleadoPorId(v.getIdEmpleado());
            String nombreEmpleado = empleado.getPrimerNombre() + " " + empleado.getPrimerApellido();
            
            if (textoBusqueda.isEmpty() ||
                nombreCliente.toLowerCase().contains(textoBusqueda) ||
                nombreEmpleado.toLowerCase().contains(textoBusqueda)) {
                Object[] fila = {
                    v.getIdVenta(),
                    v.getFechaVenta(),
                    nombreCliente,
                    nombreEmpleado,
                    v.getTotalVenta()
                };
                modelo.addRow(fila);
            }
        }
    }
    }//GEN-LAST:event_textBuscarkeyTyped

    private void tablaVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaVentasMouseClicked
        // TODO add your handling code here:
       // Verificar si es un doble clic
    if (evt.getClickCount() == 2) {
        try {
            btnEliminar.setEnabled(false);
            btnGuardar.setEnabled(false);
            
            // Obtener el índice de la fila seleccionada en tablaVentas
            int filaSeleccionada = tablaVentas.getSelectedRow();
            if (filaSeleccionada == -1) {
                return; // No hacer nada si no hay fila seleccionada
            }

            // Obtener el idVenta de la fila seleccionada
            DefaultTableModel modelVentas = (DefaultTableModel) tablaVentas.getModel();
            int idVenta = (int) modelVentas.getValueAt(filaSeleccionada, 0);

            // Obtener la venta seleccionada para acceder a sus datos
            List<Venta> ventas = VentaControlador.obtenerTodasVentas();
            Venta ventaSeleccionada = null;
            for (Venta v : ventas) {
                if (v.getIdVenta() == idVenta) {
                    ventaSeleccionada = v;
                    break;
                }
            }
            if (ventaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Venta no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar cliente en comboClientes
            List<Cliente> clientes = clienteControlador.obtenerTodosClientes();
            int indiceCliente = -1;
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getIdCliente() == ventaSeleccionada.getIdCliente()) {
                    indiceCliente = i;
                    break;
                }
            }
            if (indiceCliente != -1) {
                idClienteSeleccionado = ventaSeleccionada.getIdCliente();
                ComboClientes.setSelectedIndex(indiceCliente);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar empleado en comboEmpleados
            List<Empleado> empleados = empleadoControlador.obtenerTodosEmpleados();
            int indiceEmpleado = -1;
            for (int i = 0; i < empleados.size(); i++) {
                if (empleados.get(i).getIdEmpleado() == ventaSeleccionada.getIdEmpleado()) {
                    indiceEmpleado = i;
                    break;
                }
            }
            if (indiceEmpleado != -1) {
                idEmpleadoSeleccionado = ventaSeleccionada.getIdEmpleado();
                ComboEmpleados.setSelectedIndex(indiceEmpleado);
            } else {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Detener el timer actual
            if (timer != null) {
                timer.stop();
            }
            
            // Asignar la hora al label
            horabd = true;
            java.text.SimpleDateFormat horaFormato = new java.text.SimpleDateFormat("HH:mm:ss");
            String horaVenta = horaFormato.format(ventaSeleccionada.getFechaVenta());
            hora.setText(horaVenta); // Ajusta 'horaLabel' al nombre real de tu JLabel

            // Cargar la fecha en selectorfechaContratacion
            selectorfechaVenta.setDate(ventaSeleccionada.getFechaVenta());

            // Limpiar y cargar los detalles en tablaDetalles
            DefaultTableModel modelDetalles = (DefaultTableModel) tablaDetalles.getModel();
            modelDetalles.setRowCount(0);

            List<DetalleVenta> detalles = detalleVentaControlador.obtenerTodosDetallesVenta();
            if (detalles != null) {
                for (DetalleVenta detalle : detalles) {
                    if (detalle.getIdVenta() == idVenta) {
                        Producto producto = productoControlador.obtenerProductoPorId(detalle.getIdProducto());
                        String nombreProducto = (producto != null) ? producto.getNombreProducto() : "Desconocido";

                        Object[] row = {
                            detalle.getIdProducto(),
                            nombreProducto,
                            detalle.getPrecioUnitario(),
                            detalle.getCantidad(),
                            detalle.getPrecioUnitario() * detalle.getCantidad() // Subtotal
                        };
                        modelDetalles.addRow(row);
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_tablaVentasMouseClicked

    private void accionBotonAgregar(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonAgregar
        // TODO add your handling code here:
         try {
        // Obtener el índice seleccionado del comboProductos
        int indiceSeleccionado = ComboProductos.getSelectedIndex();
        if (indiceSeleccionado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener la lista de productos
        List<Producto> productos = productoControlador.obtenerTodosProductos();
        Producto productoSeleccionado = productos.get(indiceSeleccionado);

        // Obtener el precio unitario del producto
        float precioUnitario = productoSeleccionado.getPrecioUnitario();

        // Obtener la cantidad ingresada
        String cantidadStr = textCantidad.getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular el subtotal
        float subtotal = precioUnitario * cantidad;

        // Agregar los datos a la tabla tablaDetalles
        DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();
        Object[] row = {
            productoSeleccionado.getIdProducto(),
            productoSeleccionado.getNombreProducto(),
            precioUnitario,
            cantidad,
            subtotal
        };
        model.addRow(row);

        // Limpiar los campos después de agregar
        textCantidad.setText("");
        textPrecio.setText("");
        cargarProductos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_accionBotonAgregar

    private void accionBotonQuitarDetalle(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accionBotonQuitarDetalle
        // TODO add your handling code here:
      try {
        // Obtener el índice de la fila seleccionada en tablaDetalles
        int filaSeleccionada = tablaDetalles.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle para quitar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Eliminar la fila seleccionada del modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) tablaDetalles.getModel();
        model.removeRow(filaSeleccionada);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al quitar el detalle: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }   
    }//GEN-LAST:event_accionBotonQuitarDetalle


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonQuitarDetalle;
    private javax.swing.JComboBox<String> ComboClientes;
    private javax.swing.JComboBox<String> ComboEmpleados;
    private javax.swing.JComboBox<String> ComboProductos;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel hora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser selectorfechaVenta;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTable tablaVentas;
    private javax.swing.JTextField textBuscar;
    private javax.swing.JTextField textCantidad;
    private javax.swing.JTextField textPrecio;
    // End of variables declaration//GEN-END:variables
}
