package mines;

import java.awt.*;

/**
 * ������
 * @author Leslie Leung
 */
public class Field {
	public static final int STYLE_COVERED = 1;	//Field����ʱ����ʽ
	public static final int STYLE_OPENED = 2;	//Field��ʱ����ʽ
	public static final int STYLE_MARKED = 3;	//Field�����ʱ����ʽ
	public static final int FIELD_SIZE = 25;	//һ�����ӵĴ�С

	private int mineValue;	//Field�ĸ�������ֵ
	private int x;	//Field�ĺ�����
	private int y;	//Field��������
	private int style;	//Field����ʽ

	/**
	 * Field�๹�췽��
	 * @param x ������
	 * @param y ������
	 */
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
		style = STYLE_COVERED;	//��ʼ����ʽΪ����
		mineValue = 0;	//��ʼ����Field��Ϊ���ף���mineValueֵΪ0����ʾ
	}

	/**
	 * ��ȡ��Field�ĺ�����
	 * @return ������
	 */
	public int getX() {
		return x;
	}

	/**
	 * ��ȡ��Field��������
	 * @return ������
	 */
	public int getY() {
		return y;
	}

	/**
	 * ����ĳ��Field����ʽ
	 * @param style ��ʽ
	 */
	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * ͨ����mineValueֵ����Ϊ-100��ʾ��FieldΪ����
	 */
	public void setMine() {
		mineValue = -100;
	}

	/**
	 * ���ظ�Field��mineValue
	 * @return mineValue
	 */
	public int getMineValue() {
		return mineValue;
	}

	/**
	 * ���ø�Field�ĵ���ֵ
	 * @param value ����ֵ
	 */
	public void setMineValue(int value) {
		mineValue = value;
	}

	/**
	 * �жϸ�Field�Ƿ����
	 * @return true���ǣ�����false
	 */
	public boolean isMine() {
		return mineValue == -100;
	}

	/**
	 * �жϸ�Field�Ƿ񱻱��
	 * @return true������ǣ�false��û�����
	 */
	public boolean isMarked() {
		return style == STYLE_MARKED;
	}

	/**
	 * �жϸ�Field�Ƿ񸲸�
	 * @return true�����ǣ�false��û����
	 */
	public boolean isCovered() {
		return style == STYLE_COVERED;
	}

	/**
	 * �жϸ�Field�Ƿ��Ѵ�
	 * @return true���Ѵ򿪣�false��û��
	 */
	public boolean isOpened() {
		return style == STYLE_OPENED;
	}

	/**
	 * ��ͼ����
	 * @param g Graphics g
	 */
	public void paintField(Graphics g) {
		int yCoordinate = x * FIELD_SIZE + 1;
		int xCoordinate = y * FIELD_SIZE + 1;
		
		if(isCovered()) {//��ʽΪ����ʱ�������Ļ�ͼ
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
		}
		
		if(isOpened()) {//��ʽΪ��ʱ�����Ļ�ͼ
			if(mineValue > 0) {//����Field�ĸ�������ֵ����0ʱ
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				g.setColor(Color.BLACK);
				g.drawString(mineValue + "", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
				
			} else if(mineValue == -100) {//����Field�ǵ���ʱ
				g.setColor(Color.RED);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			} else if(mineValue == 0) {//����Field�ĸ�������ֵΪ0
				g.setColor(Color.WHITE);
				g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
				
			}
		}

		if(isMarked()) {//��ʽΪ���ʱ�����Ļ�ͼ
			g.setColor(Color.CYAN);
			g.fillRect(xCoordinate, yCoordinate, FIELD_SIZE - 2, FIELD_SIZE - 2);
			g.setColor(Color.MAGENTA);
			g.drawString("��", y * FIELD_SIZE + FIELD_SIZE / 2, x * FIELD_SIZE + FIELD_SIZE / 2);
		}
	}
}
