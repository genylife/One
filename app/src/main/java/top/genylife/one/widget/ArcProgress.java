package top.genylife.one.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.genylife.one.R;

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
    private float mBackStroke;
    /**
     * 进度环背景颜色
     */
    private int mBackColor;
    /**
     * 进度环前景颜色
     */
    private int mFrontColor;
    /**
     * 刻度文本的颜色
     */
    private int mDialTextColor;
    /**
     * 刻度点颜色
     */
    private int mDialPointColor;
    /**
     * 进度环前景色画笔
     */
    private Paint mFrontPaint;
    /**
     * 进度环前景Stroke,by px
     */
    private float mFrontStroke;
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
     * 刻度点StrokeWidth
     */
    private float mDialStrokeWidth;

    /**
     * 绘制区域（正方形）的Size
     */
    private int mRectFSize;
    /**
     * 文本绘制的画笔
     */
    private Paint mTextPaint;
    /**
     * 绘制文本的Size
     */
    private float mDialTextSize;
    /**
     * 每一个刻度的平均角度
     */
    private float mAvgAngle;
    /**
     *
     */
    private float mDrawBackAngle = 0.5f;
    private float mDrawFrontAngle = 0.5f;
    /**
     * 进度最大值
     */
    private int mMaxProgress;
    /**
     * 当前进度
     */
    private int mProgress;
    private List<String> mDialTexts;

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

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initAttributeSet(a);
        a.recycle();


        mArcRectF = new RectF();

        mArcCenter = new Point();

        mRectF = new RectF();

        mDialPoints = new ArrayList<>();
        mDialTextPoints = new ArrayList<>();

        mBackStroke = 10;
        mFrontStroke = 10;

        mBackPaint = new Paint();
        mBackPaint.setColor(mBackColor);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStrokeWidth(mBackStroke);
        mBackPaint.setStyle(Paint.Style.STROKE);
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);

        mFrontPaint = new Paint();
        mFrontPaint.setColor(mFrontColor);
        mFrontPaint.setAntiAlias(true);
        mFrontPaint.setStrokeWidth(mFrontStroke);
        mFrontPaint.setStyle(Paint.Style.STROKE);
        mFrontPaint.setStrokeCap(Paint.Cap.ROUND);

        mDialPaint = new Paint();
        mDialPaint.setColor(mDialPointColor);
        mDialPaint.setAntiAlias(true);
        mDialPaint.setStrokeWidth(mDialStrokeWidth);
        mDialPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDialPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setColor(mDialTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(mDialTextSize);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBroadWidth = 70;

        //        mStartAngle = 0;
        //        mSweepAngle = 270;

        //        mDialNumber = 5;
        if (mSweepAngle > 360)
        {
            mSweepAngle = 360;
        }
        mAvgAngle = mSweepAngle / mDialNumber;
        if (mSweepAngle < 360)
        {
            mDialNumber++;
        }

        mDialTexts = new ArrayList<>(mDialNumber + 1);

        //        mMaxProgress = 60;
        //        mProgress = 0;

    }

    private void initAttributeSet(TypedArray typedArray)
    {
        mBackColor = typedArray.getColor(R.styleable.ArcProgress_arc_backColor,
                getResources().getColor(R.color.arcProgressDefBackColor));
        mFrontColor = typedArray.getColor(R.styleable.ArcProgress_arc_frontColor,
                getResources().getColor(R.color.arcProgressDefFrontColor));

        mDialPointColor = typedArray.getColor(R.styleable.ArcProgress_arc_dialPointColor, Color.GRAY);
        mDialTextColor = typedArray.getColor(R.styleable.ArcProgress_arc_dialTextColor, Color.GRAY);

        mBackStroke = typedArray.getDimension(R.styleable.ArcProgress_arc_backStroke, 30);
        mFrontStroke = typedArray.getDimension(R.styleable.ArcProgress_arc_frontStroke, 20);

        mStartAngle = typedArray.getInt(R.styleable.ArcProgress_arc_startAngle, 90 + 45);
        mSweepAngle = typedArray.getInt(R.styleable.ArcProgress_arc_sweepAngle, 270);

        mMaxProgress = typedArray.getInt(R.styleable.ArcProgress_arc_maxProgress, 100);
        mProgress = typedArray.getInt(R.styleable.ArcProgress_arc_progress, 0);

        mDialNumber = typedArray.getInt(R.styleable.ArcProgress_arc_dialNumber, 0);
        mDialStrokeWidth = typedArray.getDimension(R.styleable.ArcProgress_arc_dialStrokeWidth, 15);
        mDialTextSize = typedArray.getDimension(R.styleable.ArcProgress_arc_dialTextSize, 20);
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
        //        canvas.rotate(mStartAngle, mArcCenter.x, mArcCenter.y);
        float angle = mDrawBackAngle += 5;
        if (angle <= mSweepAngle)
            canvas.drawArc(mArcRectF, mStartAngle, angle, false, mBackPaint);
        else
        {
            canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle, false, mBackPaint);
            angle = mDrawFrontAngle += 2;
            if (angle <= mSweepAngle * (mProgress * 1f / mMaxProgress))
                canvas.drawArc(mArcRectF, mStartAngle, angle, false, mFrontPaint);
            else
                canvas.drawArc(mArcRectF, mStartAngle, mSweepAngle * (mProgress * 1f / mMaxProgress), false, mFrontPaint);
            mDialPoints.clear();
            mDialTextPoints.clear();

            genDialPoint();
            canvas.rotate(mStartAngle, mArcCenter.x, mArcCenter.y);
            drawDialPoints(canvas, mDialPaint);
            //画刻度文本
            drawDialTexts(canvas, mTextPaint, mDialTexts);
        }
        invalidate();
    }

    private void drawDialPoint(Canvas canvas, DialPoint point, Paint paint)
    {
        canvas.drawCircle(point.x, point.y, 0, paint);
    }

    /**
     * 设置刻度点对应的文本
     *
     * @param textList
     */
    public void setDialTexts(List<String> textList)
    {
        mDialTexts = textList;
        invalidate();
    }

    /**
     * 设置可读点的数量
     *
     * @param num
     */
    public void setDialPointNum(int num)
    {
        mDialNumber = num;
        mAvgAngle = mSweepAngle / mDialNumber;
        if (mSweepAngle < 360)
        {
            mDialNumber++;
        }
        genDialPoint();
        invalidate();
    }

    /**
     * 设置进度，通过角度
     *
     * @param angle
     */
    public void setProgressAngle(float angle)
    {
        int progress = (int) (mMaxProgress * angle / mSweepAngle);
        setProgress(progress);
    }

    /**
     * 设置进度，通过刻度,刻度从0开始
     *
     * @param dial
     */
    public void setDialProgress(int dial)
    {
        int tempDial = mDialNumber;
        if (mSweepAngle < 360)
            tempDial = mDialNumber - 1;
        if (dial >= tempDial)
        {
            setProgressAngle(tempDial * mAvgAngle);
        } else
        {
            setProgressAngle(dial * mAvgAngle);
        }
    }

    /**
     * 生成刻度点和文本锚点的位置
     */
    private void genDialPoint()
    {
        mDialTextPoints.clear();
        mDialPoints.clear();
        for (int i = 0; i < mDialNumber; i++)
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
    private void drawDialTexts(Canvas canvas, Paint paint, List<String> texts)
    {
        for (int i = 0; i < mDialTextPoints.size(); i++)
        {
            try
            {
                if (mSweepAngle < 360)
                {
                    try
                    {
                        drawDialText(canvas, mDialTextPoints.get(i), paint, texts.get(i % (mDialTextPoints.size())));
                    } catch (IndexOutOfBoundsException e)
                    {
                        drawDialText(canvas, mDialTextPoints.get(i), paint, texts.get(i % (mDialTextPoints.size() - 1)));
                    }
                } else
                    drawDialText(canvas, mDialTextPoints.get(i), paint, texts.get(i % (mDialTextPoints.size())));
            } catch (IndexOutOfBoundsException e)
            {

            }
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

    private float dp2px(float dp)
    {
        Resources resources = getResources();
        float density = resources.getDisplayMetrics().density;
        return dp * density + 0.5f;
    }

    private float sp2px(float sp)
    {
        Resources resources = getResources();
        float density = resources.getDisplayMetrics().scaledDensity;
        return sp * density + 0.5f;
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
