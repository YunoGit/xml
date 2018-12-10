package xml;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;

import dao.BaseDao;
import pojo.InformationSchema;

public class Main {
	XMLBuilder xml;
	Map<String, String> tables = new HashMap<String, String>();
	protected Shell shell;
	private Combo combo;
	private Text text_1;
	private Label lblNewLabel;
	private Button btnNewButton;
	private Button button_1;
	private Label lblNewLabel_1;
	private Label label;
	private Button button_2;
	private Button button_3;
	private Button button_4;
	private Text text;
	private Button button_5;
	private Label label_1;
	private Button button_6;
	private Button button_7;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Label label_3;
	private Label label_4;
	private Label label_5;
	private Button button_8;

	public static void main(String[] args) {
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell();
		shell.setText("CreateMapper生成工具");
		shell.setSize(634, 317);
		shell.setLocation(
				Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
		shell.setLayout(null);

		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(139, 32, 273, 25);

		button_2 = new Button(shell, SWT.NONE);
		button_2.setEnabled(false);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveXml();
			}
		});
		button_2.setBounds(139, 225, 80, 27);
		button_2.setText("保存文件");

		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				readFile();
			}
		});
		button.setBounds(28, 27, 105, 27);
		button.setText("选择文件");

		combo = new Combo(shell, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = combo.getText();
				text_1.setText(tables.get(key));
			}
		});
		combo.setBounds(69, 75, 150, 25);

		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(69, 106, 150, 25);

		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = combo.getText();
				String value = text_1.getText();
				addTable(key, value);
			}
		});
		btnNewButton.setBounds(249, 75, 75, 25);
		btnNewButton.setText("新增/修改");

		button_1 = new Button(shell, SWT.NONE);
		button_1.setEnabled(false);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = combo.getText();
				removeTable(key);
			}
		});
		button_1.setText("删除");
		button_1.setBounds(249, 106, 75, 25);

		lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(28, 78, 35, 25);
		lblNewLabel_1.setText("表名：");

		label = new Label(shell, SWT.NONE);
		label.setText("类名：");
		label.setBounds(28, 109, 35, 25);

		button_3 = new Button(shell, SWT.NONE);
		button_3.setEnabled(false);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeAllTable();
			}
		});
		button_3.setText("删除全部");
		button_3.setBounds(337, 105, 75, 25);

		button_4 = new Button(shell, SWT.NONE);
		button_4.setEnabled(false);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = combo.getText();
				addAutoTable(key);
			}
		});
		button_4.setText("自动增加");
		button_4.setBounds(337, 75, 75, 25);

		label_1 = new Label(shell, SWT.NONE);
		label_1.setText("按数据库录入：");
		label_1.setBounds(30, 153, 84, 20);

		text = new Text(shell, SWT.BORDER);
		text.setBounds(30, 176, 189, 23);

		button_5 = new Button(shell, SWT.NONE);
		button_5.setEnabled(false);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String database = text.getText();
				addDatabase(database);
			}
		});
		button_5.setText("追加录入");
		button_5.setBounds(249, 175, 74, 25);

		button_6 = new Button(shell, SWT.NONE);
		button_6.setEnabled(false);
		button_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeAllTable();
				String database = text.getText();
				addDatabase(database);
			}
		});
		button_6.setText("覆盖录入");
		button_6.setBounds(337, 175, 74, 25);

		button_7 = new Button(shell, SWT.NONE);
		button_7.setEnabled(false);
		button_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Desktop.getDesktop().open(new File(xml.getFile()));
				} catch (IOException e1) {
					error(e1.getMessage());
				}
			}
		});
		button_7.setText("打开文件");
		button_7.setBounds(231, 225, 80, 27);

		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(445, 65, 150, 25);

		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(445, 176, 150, 25);

		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(445, 122, 150, 25);

		label_3 = new Label(shell, SWT.NONE);
		label_3.setText("数据库名称：");
		label_3.setBounds(445, 39, 84, 20);

		label_4 = new Label(shell, SWT.NONE);
		label_4.setText("数据库用户名：");
		label_4.setBounds(445, 96, 84, 20);

		label_5 = new Label(shell, SWT.NONE);
		label_5.setText("数据库密码：");
		label_5.setBounds(445, 153, 84, 20);

		button_8 = new Button(shell, SWT.NONE);
		button_8.setEnabled(false);
		button_8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String database = text_2.getText();
				String username = text_4.getText();
				String userpwd = text_3.getText();
				setDatasources(database, username, userpwd);
			}
		});
		button_8.setText("保存修改");
		button_8.setBounds(444, 212, 80, 25);
	}

	public void addTable(String key, String value) {
		tables.put(key, value);
		createCombo();
	}

	public void addAutoTable(String key) {
		if (key.length() < 1)
			return;
		String value = key.substring(0, 1).toUpperCase() + key.substring(1);
		addTable(key, value);
	}

	public void removeTable(String key) {
		tables.remove(key);
		createCombo();
	}

	public void removeAllTable() {
		tables.clear();
		createCombo();
	}

	public void addDatabase(String database) {
		try {
			BaseDao dao = new BaseDao();
			List<InformationSchema> list = dao.executeQuery(
					"SELECT table_name FROM information_schema.tables WHERE table_schema='" + database + "'",
					InformationSchema.class);
			for (InformationSchema is : list) {
				String key = is.getTable_name();
				addAutoTable(key);
			}
			Element element = xml.getElements("jdbcConnection").get(0);
			element.setAttribute("connectionURL", "jdbc:mysql://localhost:3306/" + database);
		} catch (Exception e) {
			error(e.getMessage());
		}
	}

	public void setDatasources(String database, String username, String userpwd) {
		try {
			Element element = xml.getElements("jdbcConnection").get(0);
			if (!database.isEmpty())
				element.setAttribute("connectionURL", "jdbc:mysql://localhost:3306/" + database);
			if (!username.isEmpty())
				element.setAttribute("userId", username);
			if (!userpwd.isEmpty())
				element.setAttribute("password", userpwd);
		} catch (Exception e) {
			error(e.getMessage());
		}
	}

	public void createCombo() {
		combo.removeAll();
		text_1.setText("");
		if (tables.size() < 1)
			return;
		for (String key : tables.keySet()) {
			combo.add(key);
		}
		combo.select(0);
		text_1.setText(tables.get(combo.getText()));
	}

	public void readFile() {
		try {
			FileDialog fileDialog = new FileDialog(shell);
			String file = fileDialog.open();
			if (file == null || file.isEmpty())
				return;
			xml = new XMLBuilder(file);
			btnNewButton.setEnabled(true);
			button_1.setEnabled(true);
			button_2.setEnabled(true);
			button_3.setEnabled(true);
			button_4.setEnabled(true);
			button_5.setEnabled(true);
			button_6.setEnabled(true);
			button_7.setEnabled(true);
			button_8.setEnabled(true);
			lblNewLabel.setText(xml.getFile());
			tables = new HashMap<String, String>();
			List<Element> elements = xml.getElements("table");
			for (Element element : elements) {
				String key = xml.getAttributes(element, "tableName");
				String value = xml.getAttributes(element, "domainObjectName");
				this.tables.put(key, value);
			}
			createCombo();
		} catch (Exception e) {
			error(e.getMessage());
		}
	}

	public void saveXml() {
		try {
			xml.removeElements(xml.getElements("table"));
			Element parent = xml.getElements("context").get(0);
			for (String key : tables.keySet()) {
				Map<String, String> attributes = setAttributes(key, tables.get(key));
				xml.saveElement(parent, "table", attributes);
			}
			xml.saveXML();
			info("保存成功");
		} catch (Exception e) {
			error(e.getMessage());
		}
	}

	public Map<String, String> setAttributes(String tableName, String domainObjectName) {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("enableCountByExample", "false");
		attributes.put("enableUpdateByExample", "false");
		attributes.put("enableDeleteByExample", "false");
		attributes.put("enableSelectByExample", "false");
		attributes.put("selectByExampleQueryId", "false");
		attributes.put("tableName", tableName);
		attributes.put("domainObjectName", domainObjectName);
		return attributes;
	}

	public int info(String message) {
		MessageBox box = new MessageBox(shell);
		box.setMessage(message);
		return box.open();
	}

	public int error(String message) {
		MessageBox box = new MessageBox(shell, SWT.ERROR);
		box.setMessage(message);
		return box.open();
	}
}