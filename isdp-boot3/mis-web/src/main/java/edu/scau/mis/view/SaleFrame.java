package edu.scau.mis.view;

import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.domain.SaleItem;
import edu.scau.mis.view.vo.SaleItemVo;
import org.springframework.stereotype.Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * NextGenPos案例GUI界面
 * 仅供OOAD课程测试使用
 */

@Component
public class SaleFrame extends JFrame {
    private final Register register;
    private static final long serialVersionUID = 1L;
    private JMenuItem jItemExit;
    private JPanel jToolBarPane,jMainPane,jPayPane;
    private JScrollPane jDataListPane;
    private JTextField jItemIdTextField,jItemQuantityTextField,jPayableTextField,jPaidTextField,jChangeTextField;
    private JButton jEnterItemButton, jEndSaleButton, jStartButton, jPayButton;
    private final Font fontLabel = new Font("微软雅黑",Font.PLAIN,18);
    private final Font fontButton = new Font("微软雅黑",Font.PLAIN,16);
    private final Font fontText = new Font("微软雅黑",Font.PLAIN,28);

    private JTable dataTable;
    private DefaultTableModel tableModel;

    /**
     * 初始化Frame
     */
    public SaleFrame(Register register) {
        this.setSize(900, 730);
        this.setLocationRelativeTo(null);
        this.setJMenuBar(getJMenuBar());
        this.setContentPane(getJContentPane());
        this.setTitle("OOAD课程案例:NextGenPOS");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.register = register;
    }

    /**
     * 菜单面板
     * @return 面板
     */
    public JMenuBar getJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jSystemMenu = new JMenu("系统");
        jSystemMenu.setFont(fontLabel);
        JMenu jVIPMenu = new JMenu("会员");
        jVIPMenu.setFont(fontLabel);
        JMenu jPaymentMenu = new JMenu("支付");
        jPaymentMenu.setFont(fontLabel);
        jItemExit = new JMenuItem("退出");
        jItemExit.setFont(fontLabel);
        jItemExit.addActionListener(e -> System.exit(0));
        JMenuItem jItemVipCard = new JMenuItem("会员管理");
        jItemVipCard.setFont(fontLabel);
        JMenuItem jItemCreditPayment = new JMenuItem("信用卡支付");
        jItemCreditPayment.setFont(fontLabel);
        jSystemMenu.add(jItemExit);
        jVIPMenu.add(jItemVipCard);
        jPaymentMenu.add(jItemCreditPayment);
        jMenuBar.add(jSystemMenu);
        jMenuBar.add(jVIPMenu);
        jMenuBar.add(jPaymentMenu);
        return jMenuBar;
    }

    /**
     * 窗口面板
     * @return 面板
     */
    public JPanel getJContentPane() {
        JPanel jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(getJToolBarPane(), BorderLayout.NORTH);
        jContentPane.add(getJMainPane(), BorderLayout.CENTER);
        jContentPane.add(getJPayPane(), BorderLayout.WEST);
        return jContentPane;
    }

    /**
     * 工具栏面板
     * @return 面板
     */
    public JPanel getJToolBarPane() {
        if (jToolBarPane == null) {
            jToolBarPane = new JPanel();
        }
        JToolBar jToolBar = new JToolBar();
        jToolBar.setPreferredSize(new Dimension(880, 49));
        jToolBar.setLayout(new GridLayout(1, 6));
        jItemIdTextField = new JTextField();
        jItemIdTextField.setFont(fontText);
        jItemIdTextField.setHorizontalAlignment(JTextField.CENTER);
        jItemIdTextField.setEditable(false);
        jItemIdTextField.addActionListener(e -> enterItem());
        jItemQuantityTextField = new JTextField("1");
        jItemQuantityTextField.setHorizontalAlignment(JTextField.CENTER);
        jItemQuantityTextField.setFont(fontText);
        jItemQuantityTextField.setEditable(false);
        jEnterItemButton = new JButton("EnterItem");
        jEnterItemButton.setEnabled(false);
        jEnterItemButton.addActionListener(e -> enterItem());
        jEndSaleButton = new JButton("EndSale");
        jEndSaleButton.setEnabled(false);
        jEndSaleButton.addActionListener(e -> endSale());
        JLabel jSnLabel = new JLabel("产品编码",JLabel.CENTER);
        jSnLabel.setFont(fontLabel);
        jToolBar.add(jSnLabel);
        jToolBar.add(jItemIdTextField);
        JLabel jQuantityLabel = new JLabel("数量",JLabel.CENTER);
        jQuantityLabel.setFont(fontLabel);
        jToolBar.add(jQuantityLabel);
        jEndSaleButton.setFont(fontButton);
        jEnterItemButton.setFont(fontButton);
        jToolBar.add(jItemQuantityTextField);
        jToolBar.add(jEnterItemButton);
        jToolBar.add(jEndSaleButton);
        jToolBarPane.add(jToolBar);
        return jToolBarPane;
    }

    /**
     * 主内容面板
     * @return 面板
     */
    public JPanel getJMainPane() {
        if (jMainPane == null) {
            jMainPane = new JPanel(new BorderLayout());
            jMainPane.add(getJDataListPane(), BorderLayout.CENTER); // 无参
        }
        return jMainPane;
    }

    /**
     * 初始化并返回数据表格面板（只创建一次）
     */
    public JScrollPane getJDataListPane() {
        if (jDataListPane == null) {
            // 初始化列
            Object[] title = { "商品编码", "商品名称", "单价", "数量" };
            tableModel = new DefaultTableModel(title, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            dataTable = new JTable(tableModel);

            // 渲染居中
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            dataTable.setDefaultRenderer(Object.class, centerRenderer);

            // 样式
            dataTable.setRowHeight(60);
            dataTable.setFont(fontLabel);
            JTableHeader header = dataTable.getTableHeader();
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 61));
            header.setFont(fontLabel);

            jDataListPane = new JScrollPane(dataTable);
        }
        return jDataListPane;
    }

    /**
     * 左侧支付面板
     * @return 面板
     */
    public JPanel getJPayPane() {
        if (jPayPane == null) {
            jPayPane = new JPanel();
            jPayPane.setPreferredSize(new Dimension(200, 500));
            jPayPane.setLayout(new GridLayout(10, 1));
        }
        JLabel jPayableLabel = new JLabel("总金额", JLabel.CENTER);
        jPayableLabel.setFont(fontLabel);
        JLabel jPaidLabel = new JLabel("支付金额", JLabel.CENTER);
        jPaidLabel.setFont(fontLabel);
        JLabel jChangeLabel = new JLabel("找零", JLabel.CENTER);
        jChangeLabel.setFont(fontLabel);
        jPayableTextField = new JTextField("0.00");
        jPayableTextField.setHorizontalAlignment(JTextField.RIGHT);
        jPayableTextField.setForeground(Color.RED);
        jPayableTextField.setFont(fontText);
        jPayableTextField.setEditable(false);
        jPaidTextField = new JTextField("0.00");
        jPaidTextField.setHorizontalAlignment(JTextField.RIGHT);
        jPaidTextField.setForeground(Color.BLUE);
        jPaidTextField.setFont(fontText);
        jPaidTextField.setEditable(false);
        jChangeTextField = new JTextField("0.00");
        jChangeTextField.setHorizontalAlignment(JTextField.RIGHT);
        jChangeTextField.setFont(fontText);
        jChangeTextField.setForeground(Color.GREEN);
        jChangeTextField.setEditable(false);
        jStartButton = new JButton("MakeNewSale");
        jStartButton.addActionListener(e -> makeNewSale());
        jPayButton = new JButton("MakePayment");
        jPayButton.setEnabled(false);
        jPayButton.addActionListener(e -> makePayment());
        jStartButton.setFont(fontButton);
        jPayButton.setFont(fontButton);
        jPayPane.add(jStartButton);
        jPayPane.add(jPayableLabel);
        jPayPane.add(jPayableTextField);
        jPayPane.add(jPaidLabel);
        jPayPane.add(jPaidTextField);
        jPayPane.add(jChangeLabel);
        jPayPane.add(jChangeTextField);
        jPayPane.add(jPayButton);
        return jPayPane;
    }

    /**
     * 更新表格模型，刷新表格面板
     */
    public void updateTableModelAndRefreshTablePanel(List<SaleItemVo> list) {
        // 清空现有数据
        tableModel.setRowCount(0);

        // 添加新数据
        for (SaleItemVo vo : list) {
            tableModel.addRow(new Object[]{
                    vo.getItemSn(),
                    vo.getProductName(),
                    vo.getPrice().toString(),
                    vo.getQuantity()
            });
        }
    }

    /**
     * 新的销售清除界面
     */
    private void clear() {
        updateTableModelAndRefreshTablePanel(Collections.emptyList());
        jItemIdTextField.setText(null);
        jItemQuantityTextField.setText("1");
        jPayableTextField.setText("0.00");
        jPaidTextField.setText("0.00");
        jChangeTextField.setText("0.00");
        jItemIdTextField.setEditable(true);
        jItemQuantityTextField.setEditable(true);
        jEnterItemButton.setEnabled(true);
        jEndSaleButton.setEnabled(true);
        jPaidTextField.setEditable(false);
        jPayButton.setEnabled(false);
    }


    /**
     * 开始一次新的销售
     */
    public void makeNewSale() {
        register.makeNewSale();
        this.clear();
        jStartButton.setEnabled(false);
    }

    /**
     * 输入商品
     */
    public void enterItem() {
        String itemSn = jItemIdTextField.getText();
        int quantity;
        String regex = "^\\d*[1-9]\\d*$";
        String quantityText = jItemQuantityTextField.getText();
        if(quantityText.matches(regex)) {
            quantity = Integer.parseInt(quantityText);
            Sale sale = register.enterItem(itemSn, quantity);  // ✅ 改为接收Sale
            if (sale != null) {
                // 从Sale对象中提取saleItems并转换为SaleItemVo列表
                List<SaleItemVo> list = convertSaleToItemVoList(sale);
                this.updateTableModelAndRefreshTablePanel(list);

                // 更新总金额显示
                if (sale.getTotal() != null) {
                    jPayableTextField.setText(sale.getTotal().toString());
                }
            } else {
                JOptionPane.showMessageDialog(this,"输入编码错误，未找到商品","警告", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,"输入数量格式有误","错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 添加转换方法
    private List<SaleItemVo> convertSaleToItemVoList(Sale sale) {
        if (sale.getSaleItems() == null) {
            return Collections.emptyList();
        }
        List<SaleItemVo> list = new ArrayList<>();
        for (SaleItem item : sale.getSaleItems()) {
            SaleItemVo vo = new SaleItemVo(
                    item.getProduct().getProductSn(),
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity()
            );
            list.add(vo);
        }
        return list;
    }

    /**
     * 结束销售
     */
    public void endSale() {
        Sale sale = register.endSale();  // ✅ 改为接收Sale
        if (sale != null && sale.getTotal() != null) {
            jPayableTextField.setText(sale.getTotal().toString());
        }
        jEnterItemButton.setEnabled(false);
        jPaidTextField.setEditable(true);
        jItemIdTextField.setEditable(false);
        jItemQuantityTextField.setEditable(false);
        jPayButton.setEnabled(true);
        jEndSaleButton.setEnabled(false);
    }

    /**
     * 支付
     */
    public void makePayment() {
        BigDecimal total = new BigDecimal(jPayableTextField.getText().trim());
        String cashText = jPaidTextField.getText().trim();
        BigDecimal cash = new BigDecimal("0.00");
        try {
            cash = new BigDecimal(cashText);
            if (cash.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException("金额必须大于0");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"输入支付金额格式有误","错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(cash.compareTo(total) >= 0) {
            BigDecimal change = register.makePayment(cash);
            jChangeTextField.setText(change.toString());
            showReceipt(printBill(total, cash, change).toString());
            this.jPaidTextField.setEditable(false);
            this.jStartButton.setEnabled(true);
            this.jPayButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this,"支付金额小于总金额","警告", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 显示小票
     * @param billText
     */
    private void showReceipt(String billText) {
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setText(billText);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "购物凭证", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 打印小票
     * @param total 总金额
     * @param cash 支付金额
     * @param change 找零
     */
    private StringBuffer printBill(BigDecimal total, BigDecimal cash, BigDecimal change) {
        List<SaleItemVo> items = register.getSaleItems(); // 👈 新增方法
        StringBuffer bill = new StringBuffer("收银成功，清单如下: \n\n");
        bill.append("编码       名称      单价        数量 \n");
        bill.append("-----------------------------------\n");
        for (SaleItemVo si : items) {
            bill.append(String.format("%-10s %-8s %-8s %d\n",
                    si.getItemSn(),
                    si.getProductName(),
                    si.getPrice(),
                    si.getQuantity()));
        }
        bill.append("-----------------------------------\n");
        bill.append(String.format("%25s %s元\n", "合计:", total));
        bill.append(String.format("%25s %s元\n", "收银:", cash));
        bill.append(String.format("%25s %s元\n", "找零:", change));
        return bill;
    }
}

