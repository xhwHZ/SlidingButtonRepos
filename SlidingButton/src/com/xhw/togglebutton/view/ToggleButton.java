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

	// 提供给外部调用
	public interface OnToggleStateChangeListener
	{
		/**
		 * 滑动开关状态发生变化时，会调用此方法
		 */
		public void onToggleStateChange(ToggleState state);
	}

	/**
	 * 描述开关状态的枚举
	 *
	 */
	public enum ToggleState {
		Open, 
		Close
	}

	/**
	 * 当前开关的状态
	 */
	private ToggleState toggleState = ToggleState.Open;

	/**
	 * 滑动开关的背景图片
	 */
	private Bitmap switchBg;

	/**
	 * 滑动块的背景图片
	 */
	private Bitmap slideBg;

	/**
	 * 当前滑动块的X坐标(相对于View控件)
	 */
	private int currentX;

	/**
	 * 滑动块是否在滑动的标记
	 */
	private boolean isSliding = false;

	//本类自己维护的一个监听对象
	private OnToggleStateChangeListener listener;

	/**
	 * 如果View是在java代码中动态创建，走的是这个构造方法
	 * 
	 * @param context
	 */
	public ToggleButton(Context context)
	{
		super(context);
	}

	/**
	 * 如果View是在布局文件中使用，走的是这个构造方法
	 */
	public ToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 获取当前滑动开关的开关状态
	 * @return 滑动开关的开关状态
	 */
	public ToggleState getToggleState()
	{
		return this.toggleState;
	}

	// 控制显示在屏幕上的样子
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		// 1、绘制开关背景图片
		// left:图片左边的x坐标
		// top:图片顶部的y坐标
		canvas.drawBitmap(switchBg, 0, 0, null);

		// 2、绘制滑动块图片
		// 2.1滑动时的逻辑
		if (isSliding)
		{
			int slideLeft = currentX - slideBg.getWidth() / 2;// 保证滑动块在鼠标点击位置的中央
			// 边界处理
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
		// 2.2非滑动时的逻辑
		// 根据状态来绘制滑动块的位置
		if (!isSliding) // 不写else为了代码的更易阅读性
		{
			if (this.toggleState == ToggleState.Open)
			{
				// 小学数学
				canvas.drawBitmap(slideBg,
						switchBg.getWidth() - slideBg.getWidth(), 0, null);
			} else
			{
				canvas.drawBitmap(slideBg, 0, 0, null);
			}
		}
	}

	// 设置自己显示在屏幕上的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 设置测量的尺寸
		setMeasuredDimension(switchBg.getWidth(), switchBg.getHeight());// 滑动开关的宽高就是开关背景图片的宽高
	}

	/**
	 * 自己处理掉触摸事件 貌似点击事件也被这货捕获了
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// event.getRawX(); 参照屏幕坐标系
		currentX = (int) event.getX();// 参照View坐标系，也就是自身
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
			// 根据抬起手瞬间，手指位置是否过了控件中间线，来改变控件的开关状态
			int midPosition = switchBg.getWidth() / 2;
			if (currentX < midPosition)
			{
				//如果原来就是关的，没必要拉屎没事干，再去设置人家的关闭状态，因为状态根本没改变
				if (this.toggleState != ToggleState.Close)
				{
					// 位于关状态
					this.toggleState = ToggleState.Close;
					//供给外部监听的回调方法
					if (this.listener != null)
					{
						listener.onToggleStateChange(this.toggleState);
					}
				}
			} else
			{
				if (this.toggleState != ToggleState.Open)
				{
					// 位于开状态
					this.toggleState = ToggleState.Open;
					if (this.listener != null)
					{
						listener.onToggleStateChange(this.toggleState);
					}
				}
			}
			break;
		}
		// 引起重绘，onDraw被调用
		invalidate();
		return true;
	}

	/**
	 * 如果要监听滑动开关的状态变化，请设置这个监听器，当滑动开关状态
	 * 发送变化时，将会调用这个对象的onToggleStateChange方法
	 * @param listener OnToggleStateChangeListener对象
	 */
	public void setOnToggleStateChangeListener(
			OnToggleStateChangeListener listener)
	{
		this.listener = listener;
	}

	/**
	 * 设置滑动块的背景图片
	 * 
	 * @param slideButtonBackground
	 *            滑动块的背景图片
	 */
	public void setSlideBgResource(int slideButtonBackground)
	{
		slideBg = BitmapFactory.decodeResource(this.getResources(),
				slideButtonBackground);
	}

	/**
	 * 设置滑动开关的背景图片
	 * 
	 * @param switchBackground
	 *            滑动开关的背景图片
	 */
	public void setSwitchBgResource(int switchBackground)
	{
		switchBg = BitmapFactory.decodeResource(this.getResources(),
				switchBackground);
	}

	/**
	 * 设置滑动开关的状态
	 * 
	 * @param state
	 *            滑动开关的状态
	 */
	public void setToggleState(ToggleState state)
	{
		this.toggleState = state;
	}
}
