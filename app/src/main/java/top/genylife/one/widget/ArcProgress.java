package top.genylife.one.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanqi on 2016/10/18.
 *
 * @since 1.0.0
 */

public class ArcProgress extends View
{
    private static final String TAG = "ArcProgress";
    /**
     * 进度环背景色画笔
     */
    private Paint mBackPaint;
    /**
     * 进度环背景Stroke,by px
     */
    private int mBackStroke;
    /**
     * 进度环前景色画笔
     */
    private Paint mFrontPaint;
    /**
     * 进度环前景Stroke,by px
     */
    private int mFrontStroke;
    /**
     * 绘制进度环的位置
     */
    private RectF mArcRectF;
    /**
     * 绘制整个控件的位置
     */
    private RectF mRectF;
    /**
     * 进度环圆心的位置
     */
    private Point mArcCenter;
    /**
     * 进度环的半径
     */
    private float mArcRadius;
    /**
     * 相对进度环位置的向内偏移距离
     */
    private int mBroadWidth;
    /**
     * 进度环开始的角度
     */
    private float mStartAngle;
    /**
     * 进度环需要绘制的角度
     */
    private float mSweepAngle;
    /**
     * 进度环刻度点的坐标
     */
    private List<DialPoint> mDialPoints;
    /**
     * 进度环刻度点绘制文本的锚点坐标
     */
    private List<DialPoint> mDialTextPoints;
    /**
     * 进度环刻度点的数量
     */
    private int mDialNumber;
    /**
     * 进度环刻度点的画笔
     */
    private Paint mDialPaint;

    /**
     * 绘制区域（正方形）的Size
     */
    private int mRectFSize;


    private Paint mPaint;

    private float mAvgAngle;

    private int mMaxProgress;
    private int mProgress;

    public ArcProgress(Context context)
    {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mArcRectF = new RectF();

        mArcCenter = new Point();

        mRectF = new RectF();

        mDialPoints = new ArrayList<>();
        mDialTextPoints = new ArrayList<>();

        mBackStroke = 30;
        mFrontStroke = 20;

        mBackPaint = new Paint();
        mBackPaint.setColor(Color.parseColor("#E48680"));
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStrokeWidth(mBackStroke);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(Color.parseColor("#5CC7D8"));
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStrokeWidth(mFrontStroke);
        mFrontPaint.setStyle(Paint.Style.STROKE);
        mFrontPaint.setStrokeCap(Paint.Cap.ROUND);

        mDialPaint = new Paint();
        mDialPaint.setColor(Color.BLACK);
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(mFrontStroke - 6);
        mDialPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDialPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(20);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mBroadWidth = 70;

        mStartAngle = 0;
        mSweepAngle = 360;

        mDialNumber = 12;
        //        setDialNumber(6);
        mAvgAngle = 0;

        mMaxProgress = 60;
        mProgress = 0;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //        Log.d(TAG, "widthMeasureSpec:  " + widthMeasureSpec);
        //        Log.d(TAG, "heightMeasureSpec:  " + heightMeasureSpec);
        //        Log.d(TAG, "widthMode:  " + widthMode);
        //        Log.d(TAG, "widthSize:  " + widthSize);
        //        Log.d(TAG, "heightMode:  " + heightMode);
        //        Log.d(TAG, "heightSize:  " + heightSize);
        mRectFSize = Math.min(widthSize, heightSize);
        int left = ((widthSize) / 2 - (mRectFSize) / 2);
        mRectF.set(left, 0, left + mRectFSize, mRectFSize);
        mArcRectF.set(left + mBroadWidth, mBroadWidth, left + mRectFSize - mBroadWidth, mBroadWidth + mRectFSize - 2 * mBroadWidth);
        mArcCenter.set(left + mRectFSize / 2, mRectFSize / 2);
        mArcRadius = mRectFSize / 2 - mBroadWidth;

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.rotate(270, mArcCenter.x, mArcCenter.y);

        canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mBackPaint);
        canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle * (mProgress * 1f / mMaxProgress), false, mFrontPaint);

        mDialPoints.clear();
        mDialTextPoints.clear();

        setDialNumber(mDialNumber);
        drawDialPoints(canvas, mDialPaint);
        //画刻度文本
        drawDialTexts(canvas, mPaint, new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"});
    }

    private void drawDialPoint(Canvas canvas, DialPoint point, Paint paint)
    {
        canvas.drawCircle(point.x, point.y, 0, paint);
    }

    /**
     * 生成刻度点和文本锚点的位置
     *
     * @param num 需要绘制的刻度数量
     */
    public void setDialNumber(int num)
    {
        mDialNumber = num;
        mAvgAngle = mSweepAngle / (num);
        for (int i = 0; i < num; i++)
        {
            double y = mArcCenter.y + mArcRadius * Math.sin(Math.PI * (mAvgAngle * i) / 180);
            double x = mArcCenter.x + mArcRadius * Math.cos(Math.PI * (mAvgAngle * i) / 180);
            DialPoint point = new DialPoint((float) x, (float) y);
            mDialPoints.add(point);
            double ty = mArcCenter.y + (mArcRadius + 20) * Math.sin(Math.PI * (mAvgAngle * i) / 180);
            double tx = mArcCenter.x + (mArcRadius + 20) * Math.cos(Math.PI * (mAvgAngle * i) / 180);
            DialPoint tPoint = new DialPoint((float) tx, (float) ty);
            mDialTextPoints.add(tPoint);
        }
    }

    /**
     * 绘制所有的刻度点
     *
     * @param canvas
     * @param paint
     */
    private void drawDialPoints(Canvas canvas, Paint paint)
    {
        for (DialPoint point : mDialPoints)
        {
            drawDialPoint(canvas, point, paint);
        }
    }

    /**
     * 绘制刻度文本
     *
     * @param canvas
     * @param point
     * @param paint
     */
    private void drawDialText(Canvas canvas, DialPoint point, Paint paint, String text)
    {
        int index = mDialTextPoints.indexOf(point);
        int save = canvas.save();
        canvas.rotate(90 + index * mAvgAngle, point.x, point.y);
        canvas.drawText(text, point.x, point.y, paint);
        canvas.restoreToCount(save);
    }

    /**
     * 绘制所有的刻度文本
     *
     * @param canvas
     * @param paint
     */
    private void drawDialTexts(Canvas canvas, Paint paint, String[] texts)
    {
        for (int i = 0; i < mDialTextPoints.size(); i++)
        {
            drawDialText(canvas, mDialTextPoints.get(i), paint, texts[i]);
        }
    }

    public void setMaxProgress(int maxProgress)
    {
        mMaxProgress = maxProgress;
    }

    public void setProgress(int progress)
    {
        mProgress = progress;
        invalidate();
    }

    class DialPoint
    {
        float x;
        float y;

        public DialPoint()
        {

        }

        public DialPoint(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
