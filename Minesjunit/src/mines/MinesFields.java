package mines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

/**
 * ɨ�׳�����
 * @author Leslie Leung
 */
public class MinesFields extends JPanel {
	public static final int ROWS = 16;	//��������������
	public static final int COLUMNS = 16;	//��������������
	public static final int MINES_NUM = 40;		//���׵���Ŀ
	
	private Map<String, Field> fields;		//��ʾ����������Field
	private List<Field> notMineFields;		//��ʾ���в��ǵ��׵�Field�ļ���
	private GameRunScript mouseListener;	//��ʾ����¼��ļ�����
	
	/**
	 * MinesFields�๹�췽��
	 */
	public MinesFields() {
		setPreferredSize(new Dimension(ROWS * Field.FIELD_SIZE, COLUMNS * Field.FIELD_SIZE));	//����ɨ������С
		
		fields = new HashMap<String, Field>();	//�ù�ϣ���ʾ���е�Field
		notMineFields = new ArrayList<Field>();
		mouseListener = new GameRunScript();
		Random random = new Random();
		
		int mineX, mineY;	//��¼���ɵ��׵�����
		List<Field> aroundList;		//��ʾ��ȡ�ĸ�Field�ĸ�����Field�ļ���
		
		/* ���������е�ÿ��Field */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				fields.put(x + "," + y, new Field(x, y));	//����Field���󣬲��Ѷ�����ӵ�fields��
			}
		}
		
		/* �������40�����ײ����ø��������� */
		for(int i = 0; i < MINES_NUM; i ++) {
			/* ������ɵĵ��������ظ����������õ��׵����� */
			do {
				mineX = random.nextInt(COLUMNS);
				mineY = random.nextInt(ROWS);
			} while(isMine(mineX, mineY));
			
			setMine(mineX, mineY);	//������Ӧ�ļ�ֵ�Ѹ�Field����Ϊ����
			aroundList = getAround(mineX, mineY);		//��ȡ�õ��׸���������Field
			
			/* ����aroundList���Ѳ��ǵ��׵�Field�ĵ���ֵ��1 */
			for(Field field: aroundList) {
				if(!field.isMine()) {
					field.setMineValue(field.getMineValue() + 1);
				}
			}
			
		}
		
		/* �Ѳ��ǵ��׵�Field��ӵ�notMineFields�� */
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				if(!getField(x, y).isMine()) {
					notMineFields.add(getField(x, y));
				}
			}
		}
	}
	
	/**
	 * ��ȡ�ڲ����ʵ��
	 * @return �ڲ����ʵ��
	 */
	public GameRunScript getInnerInstance() {
		return mouseListener;
	}
	
	/**
	 * �ڲ��࣬����ʵ�����������Ϸ�����ϲ����Ĺ���
	 * @author Leslie Leung
	 */
	 private class GameRunScript extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();		//��ȡ�����λ�õ��Ǹ���
			int y = p.x / Field.FIELD_SIZE;		//����ȡ�ĵ�ĺ�����
			int x = p.y / Field.FIELD_SIZE;		//����ȡ�ĵ��������

			/* ������������� */
			if(e.getButton() == MouseEvent.BUTTON1) {
				open(x, y);
			} 
			/* ���˫�������� */
			if(e.getClickCount() == 2) {
				openAround(x, y);
			}
			/* �����������Ҽ�,��Ǹ����ȡ������ı�� */
			if(e.getButton() == MouseEvent.BUTTON3) {
				mark(x, y);
			}
				
		} 
	}
	
	/**
	 * �������в��ǵ��׵�Field���ж��Ƿ�ȫ�����Ѵ���
	 * @return true��ȫ�����Ѵ򿪣�false�����ֻ�ȫ����û��
	 */
	public boolean isAllOpened() {
		for(Field field: notMineFields) {
			if(!field.isOpened()) {
				return false;
			}
		}
		explode();
		return true;
	}
	 
	/**
	 * ����һ��Field����
	 * @param x ������
	 * @param y ������
	 * @return Field����
	 */
	public Field getField(int x, int y) {
		return fields.get(x + "," + y);
	}
	
	/**
	 * ���ز��ǵ��׵�����Field
	 * @param x ĳField�ĺ�����
	 * @param y ĳField��������
	 * @return ���ǵ��׵�����Field������ڼ�����
	 */
	public List<Field> getAround(int x, int y) { 
		List<Field> aroundList = new ArrayList<Field>();
		
		for(int m = -1; m <= 1; m ++) {
			for(int n = -1; n <= 1; n ++) {
				/* �����ѡ�еĶ���Ϊ���ױ�����������ѭ�� */
				if(m == 0 && n == 0) {
					continue;
				}
				
				/* ���ж��Ƿ��ȡ�����ĵ��׵������Ƿ�Խ�磬���񣬰Ѳ��ǵ��׵�����Field�Ž�aroundList���� */
				if(x + m < COLUMNS && x + m >= 0 && y + n >= 0 && y + n < ROWS) {
					aroundList.add(getField(x + m, y + n));
				}
				
			}
		}
		
		return aroundList;
		
	}
	
	/**
	 * ����Field�ĵ���ֵΪ0ʱ���ݹ���丽����Field
	 * @param x ������
	 * @param y ������
	 */
	public void open(int x, int y) {
		if(isCovered(x, y)) {
			/* ����Ǹ�Field�ǵ��׵Ļ�����ȫ��Field */
			if(isMine(x, y)) {
				explode();
				JOptionPane.showMessageDialog(null, "������");
				return;
			}			
			setOpened(x, y);	//�Ѹ�Field����Ϊ��״̬
			repaint();
			
			if(isAllOpened()) {//���ȫ���ѱ���
				JOptionPane.showMessageDialog(null, "��ϲ�㣬��Ӯ�ˣ�����");
			}	
			
			/* ����field�ĵ���ֵΪ0�Ŵ򿪸�����Field */
			if(getMineValue(x, y) == 0) {
				List<Field> aroundList = getAround(x, y);
				
				/* �ݹ���ô�ĳField����������Field */
				for(Field field: aroundList) {
					open(field.getX(), field.getY());
				}
			}
			
		}
	}
	
	/**
	 * ���ڸ�Field����������������Ǻ����ڴ����򿪸���Field���ķ���
	 * @param x ������
	 * @param y ������
	 */
	public void openAround(int x, int y) {
		if(isOpened(x, y) && getMineValue(x, y) > 0) {//�����Field�Ѵ򿪲����������ĵ���ֵ����0
			
			List<Field> aroundList = getAround(x, y);
			int mineNum = 0;
			
			for(Field field: aroundList) {
				/* �����Field�ѱ���ǣ�mineNum��1 */
				if(field.isMarked()) {
					mineNum ++;
				}
			}
			
			/* ����Field�����ĵ���ֵ��mineNum���ʱ��ִ�������Ĳ���  */
			if(getMineValue(x, y) == mineNum) {
				for(Field field: aroundList) {	
					if(field.isMarked() && !field.isMine()) {
						explode();	//�����Field������Ҹ�Field���ǵ��ף����ϴ�����Field
						JOptionPane.showMessageDialog(null, "������");
					} else if(!field.isMarked() && !field.isMine()) {
						open(field.getX(), field.getY());//��������field���ǵ���ʱ����open����
					}
				}
				
			}
		}
		
	}
	
	/**
	 * ��ĳ��Field����Ϊ��״̬
	 * @param x ������
	 * @param y ������
	 */
	public void setOpened(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_OPENED);
	}
	
	/**
	 * ���ݱ��״̬������Ϊ��ǻ�ȡ�����
	 * @param x ������
	 * @param y ������
	 */
	public void mark(int x, int y) {
		if(isCovered(x, y)) {
			setMarked(x, y);
			repaint();
		} else if(isMarked(x, y)) {
			setCovered(x, y);
			repaint();
		}
	}
	
	/**
	 * ��ĳ��Field����Ϊ�����
	 * @param x ������
	 * @param y ������
	 */
	public void setMarked(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_MARKED);
	}
	
	/**
	 * �ж�һ��Field�Ƿ񱻱��
	 * @param x ������
	 * @param y ������
	 * @return ������ǣ�true������false
	 */
	public boolean isMarked(int x, int y) {
		return getField(x, y).isMarked();
	}
	
	/**
	 * �ж�ĳ��Field�Ƿ����
	 * @param x ������
	 * @param y ������
	 * @return true���ǵ��ף�false�����ǵ���
	 */
	public boolean isMine(int x, int y) {
		return getField(x, y).isMine();
	}
		
	/**
	 * ͨ����mineValueֵ����Ϊ-100��ʾ��FieldΪ����
	 * @param x ������
	 * @param y ������
	 */
	public void setMine(int x, int y) {
		getField(x, y).setMine();
	}
	
	/**
	 * �жϸ�Field�Ƿ��Ѵ�
	 * @param x
	 * @param y
	 * @return true���ѱ��򿪣�false��û��
	 */
	public boolean isOpened(int x, int y) {
		return getField(x, y).isOpened();
	}
	
	/**
	 * ���ָ��Field�ĵ���ֵ
	 * @param x ������
	 * @param y ������
	 * @return ��Field�ĵ���ֵ
	 */
	public int getMineValue(int x, int y) {
		return getField(x, y).getMineValue();
	}
	
	/**
	 * �ж�ĳ��Field�Ƿ�Ϊ����
	 * @param x ������ 
	 * @param y ������
	 * @return ��Ϊ���ǣ�true������false
	 */
	public boolean isCovered(int x, int y) {
		return getField(x, y).isCovered();
	}
	
	/**
	 * �Ѹ�Field����ʽ����Ϊ����
	 * @param x ������
	 * @param y ������
	 */
	public void setCovered(int x, int y) {
		getField(x, y).setStyle(Field.STYLE_COVERED);
	}
	
	/**
	 * ��ȫ��Field
	 */
	public void explode() {
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				setOpened(x, y);
			}
		}
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getBounds().width, getBounds().height);
		
		for(int x = 0; x < COLUMNS; x ++) {
			for(int y = 0; y < ROWS; y ++) {
				getField(x, y).paintField(g);
			}
		}
			
	}
	
}
