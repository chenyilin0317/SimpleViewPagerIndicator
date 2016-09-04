package com.example.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerIndicator extends LinearLayout implements  OnPageChangeListener {

	private int mTriangleWight;

	private Path mTriangle;

	private Paint mPaint;

	private int mTriangleHeight;

	private static final float RADIO_TRIANGLE_WIDTH = 1/6F;

	private int mInitTranslationX;

	private int mTranslationX;
	
	private final int DEF_VIS_COUNT=3;
	
	private int Vis_Count;

	private String[] titles;

	public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.vis_count);
		
		int integer = a.getInteger(R.styleable.vis_count_visCount, DEF_VIS_COUNT);
		
		if(integer<0){
			Vis_Count=DEF_VIS_COUNT;
		}else{
			Vis_Count=integer;
		}
		
		
		a.recycle();
		
		mPaint=new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#ffffffff"));
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));
		
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.save();
		
		canvas.translate(mInitTranslationX+mTranslationX,getHeight()+2);
		canvas.drawPath(mTriangle, mPaint);
		
		canvas.restore();
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWight = (int) (w / 3 * RADIO_TRIANGLE_WIDTH);
		mTriangleHeight = mTriangleWight / 2;
		mInitTranslationX = w / Vis_Count / 2 - mTriangleWight / 2;
		initTriangle();
	}

	private void initTriangle() {
		mTriangle=new Path();
		mTriangle.moveTo(0, 0);
		mTriangle.lineTo(mTriangleWight,0);
		mTriangle.lineTo(mTriangleWight/2,-mTriangleHeight);
		mTriangle.close();
	}

	public void scroll(int position, float positionOffect) {
		int screen=getWidth()/Vis_Count;
		mTranslationX=(int) (screen*position+positionOffect*screen);
		
		if(position>=(Vis_Count-2)&&positionOffect!=0&& getChildCount()>Vis_Count){
			if(Vis_Count!=1)
			this.scrollTo((int)(positionOffect*screen) + (position-Vis_Count+2)*screen,0);
			else
			this.scrollTo((int)(positionOffect*screen)+position*screen, 0);
		}
		
		invalidate();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		int childCount = getChildCount();
		if(childCount==0)return;
		int childWight=getScreenWight()/Vis_Count;
		for(int i=0;i<childCount;i++){
			View childAt = getChildAt(i);
			LinearLayout.LayoutParams lp = (LayoutParams) childAt.getLayoutParams();
			lp.weight=0;
			lp.width=childWight;
			childAt.setLayoutParams(lp);
		}
		
	}
	
	public void setTitleTable(String titles[]){
		this.titles=titles;
		if(titles!=null && titles.length>0){
			for (String string : titles) {
				addView(getTextView(string));
			}
		}
	}
	
	private static final int NORN_COLOR=0x99FFFFFF;
	private static final int DOWN_COLOR=0xFFFFFFFF;
	
	private View getTextView(String string) {
		TextView textView = new TextView(getContext());
		LinearLayout.LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.width=getScreenWight()/Vis_Count;
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		textView.setLayoutParams(lp);
		textView.setText(string);
		textView.setTextColor(NORN_COLOR);
		return textView;
	}

	public void setVisCount(int visCount){
		this.Vis_Count=visCount;
	}

	private int getScreenWight() {
		WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}
	
	public void setViewPager(ViewPager viewPager,int pos){
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(pos);
		higlTextView(pos);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		scroll(arg0, arg1);
	}

	@Override
	public void onPageSelected(int arg0) {
		higlTextView(arg0);
	}
	
	private void higlTextView(int pos){
		initTextViewColor();
		View childAt = getChildAt(pos);
		if(childAt instanceof TextView){
			((TextView) childAt).setTextColor(DOWN_COLOR);
		}
	}

	private void initTextViewColor() {
		for(int i=0;i<getChildCount();i++){
			View childAt = getChildAt(i);
			if(childAt instanceof TextView){
				((TextView) childAt).setTextColor(NORN_COLOR);
			}
		}
	}
	
}
