package com.xhw.togglebutton.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View
{

	// �ṩ���ⲿ����
	public interface OnToggleStateChangeListener
	{
		/**
		 * ��������״̬�����仯ʱ������ô˷���
		 */
		public void onToggleStateChange(ToggleState state);
	}

	/**
	 * ��������״̬��ö��
	 *
	 */
	public enum ToggleState {
		Open, 
		Close
	}

	/**
	 * ��ǰ���ص�״̬
	 */
	private ToggleState toggleState = ToggleState.Open;

	/**
	 * �������صı���ͼƬ
	 */
	private Bitmap switchBg;

	/**
	 * ������ı���ͼƬ
	 */
	private Bitmap slideBg;

	/**
	 * ��ǰ�������X����(�����View�ؼ�)
	 */
	private int currentX;

	/**
	 * �������Ƿ��ڻ����ı��
	 */
	private boolean isSliding = false;

	//�����Լ�ά����һ����������
	private OnToggleStateChangeListener listener;

	/**
	 * ���View����java�����ж�̬�������ߵ���������췽��
	 * 
	 * @param context
	 */
	public ToggleButton(Context context)
	{
		super(context);
	}

	/**
	 * ���View���ڲ����ļ���ʹ�ã��ߵ���������췽��
	 */
	public ToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * ��ȡ��ǰ�������صĿ���״̬
	 * @return �������صĿ���״̬
	 */
	public ToggleState getToggleState()
	{
		return this.toggleState;
	}

	// ������ʾ����Ļ�ϵ�����
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 1�����ƿ��ر���ͼƬ
		// left:ͼƬ��ߵ�x����
		// top:ͼƬ������y����
		canvas.drawBitmap(switchBg, 0, 0, null);

		// 2�����ƻ�����ͼƬ
		// 2.1����ʱ���߼�
		if (isSliding)
		{
			int slideLeft = currentX - slideBg.getWidth() / 2;// ��֤�������������λ�õ�����
			// �߽紦��
			if (slideLeft < 0)
			{
				slideLeft = 0;
			}
			if (slideLeft > switchBg.getWidth() - slideBg.getWidth())
			{
				slideLeft = switchBg.getWidth() - slideBg.getWidth();
			}
			canvas.drawBitmap(slideBg, slideLeft, 0, null);
		}
		// 2.2�ǻ���ʱ���߼�
		// ����״̬�����ƻ������λ��
		if (!isSliding) // ��дelseΪ�˴���ĸ����Ķ���
		{
			if (this.toggleState == ToggleState.Open)
			{
				// Сѧ��ѧ
				canvas.drawBitmap(slideBg,
						switchBg.getWidth() - slideBg.getWidth(), 0, null);
			} else
			{
				canvas.drawBitmap(slideBg, 0, 0, null);
			}
		}
	}

	// �����Լ���ʾ����Ļ�ϵĿ��
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ���ò����ĳߴ�
		setMeasuredDimension(switchBg.getWidth(), switchBg.getHeight());// �������صĿ�߾��ǿ��ر���ͼƬ�Ŀ��
	}

	/**
	 * �Լ�����������¼� ò�Ƶ���¼�Ҳ�����������
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// event.getRawX(); ������Ļ����ϵ
		currentX = (int) event.getX();// ����View����ϵ��Ҳ��������
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			this.isSliding = true;
			break;
		case MotionEvent.ACTION_MOVE:
			this.isSliding = true;
			break;
		case MotionEvent.ACTION_UP:
			this.isSliding = false;
			// ����̧����˲�䣬��ָλ���Ƿ���˿ؼ��м��ߣ����ı�ؼ��Ŀ���״̬
			int midPosition = switchBg.getWidth() / 2;
			if (currentX < midPosition)
			{
				//���ԭ�����ǹصģ�û��Ҫ��ʺû�¸ɣ���ȥ�����˼ҵĹر�״̬����Ϊ״̬����û�ı�
				if (this.toggleState != ToggleState.Close)
				{
					// λ�ڹ�״̬
					this.toggleState = ToggleState.Close;
					//�����ⲿ�����Ļص�����
					if (this.listener != null)
					{
						listener.onToggleStateChange(this.toggleState);
					}
				}
			} else
			{
				if (this.toggleState != ToggleState.Open)
				{
					// λ�ڿ�״̬
					this.toggleState = ToggleState.Open;
					if (this.listener != null)
					{
						listener.onToggleStateChange(this.toggleState);
					}
				}
			}
			break;
		}
		// �����ػ棬onDraw������
		invalidate();
		return true;
	}

	/**
	 * ���Ҫ�����������ص�״̬�仯�����������������������������״̬
	 * ���ͱ仯ʱ�����������������onToggleStateChange����
	 * @param listener OnToggleStateChangeListener����
	 */
	public void setOnToggleStateChangeListener(
			OnToggleStateChangeListener listener)
	{
		this.listener = listener;
	}

	/**
	 * ���û�����ı���ͼƬ
	 * 
	 * @param slideButtonBackground
	 *            ������ı���ͼƬ
	 */
	public void setSlideBgResource(int slideButtonBackground)
	{
		slideBg = BitmapFactory.decodeResource(this.getResources(),
				slideButtonBackground);
	}

	/**
	 * ���û������صı���ͼƬ
	 * 
	 * @param switchBackground
	 *            �������صı���ͼƬ
	 */
	public void setSwitchBgResource(int switchBackground)
	{
		switchBg = BitmapFactory.decodeResource(this.getResources(),
				switchBackground);
	}

	/**
	 * ���û������ص�״̬
	 * 
	 * @param state
	 *            �������ص�״̬
	 */
	public void setToggleState(ToggleState state)
	{
		this.toggleState = state;
	}
}
