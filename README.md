3DNumberRotate
==============


api demo中的Rotate3DAnimation 演示了如何使用camera来实现3D翻转动画，本文讲解如何将这种动画运用到数字的翻转中。

数字翻转最常见的是在日历应用中，当日期改变，数字将翻转到另外一个值，和一般的翻转不同，这种翻转有以下几个特征：

1.数字增加或者减少翻转的方向不同，一般增加是顺时针，减少是逆时针。

2.数字的翻转需要同时改变数字的值：当翻转进度过半时需要更新其显示内容。

3.这点不重要。我们需要一个能美化数字的字体。

4.为了使翻转更生动需要在翻转过程中按照一定的规律的改变透明度。

![](https://github.com/jianghejie/3DNumberRotate/blob/master/screenshots/rotate3d.mp4_1413125161.gif)

 实现:

我们仿照api demo中的Rotate3DAnimation 写了一个Rotate3DNumberAnimation：

        Rotate3DNumberAnimation的构造函数需有三个参数，分别说明动画组件的中心点位置及旋转方向。 
        Rotate3DNumberAnimation.initialize()将初始化动画组件及其父容器的宽高；通常亦可进行另外的初始化工作，本例中用于执行对camera进行实例化赋值。 
        Rotate3DNumberAnimation.applyTransformation()第一个参数为动画的进度时间值，取值范围为[0.0f,1.0f]，第二个参数Transformation记录着动画某一帧中变形的原始数据。该方法在动画的每一帧显示过程中都会被调用。 
         在翻转过程中，为了避免在动画下半部分时图像为镜面效果影响数字的阅读，应将翻转角度做180度的减法。代码为Rotate3DNumberAnimation.applyTransformation()中的：

	
if (overHalf) {
    // 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。
    degree = degree - 180;
}

动画翻转到一半后，应更新数字内容。为了得知翻转进度，于Rotate3DNumberAnimation中设计一内部静态接口类"InterpolatedTimeListener"，该接口只有一个方法"interpolatedTime(float interpolatedTime)"可以将动画进度传递给监听发起者。除了在翻转一半后更新数字内容外，我们还需根据这个Listener改变数字的透明度。

@SuppressLint("NewApi")
@Override
public void interpolatedTime(float interpolatedTime) {
    // 监听到翻转进度过半时，更新txtNumber显示内容。
    if (enableRefresh && interpolatedTime > 0.5f) {
        txtNumber.setText(Integer.toString(number));
        Log.d("ANDROID_LAB", "setNumber:" + number);
        enableRefresh = false;
    }
    //改变透明度
    if(interpolatedTime > 0.5f) {
        txtNumber.setAlpha((interpolatedTime -0.5f) * 2);
    } else {
        txtNumber.setAlpha(1-interpolatedTime * 2);
    }
}

当翻转一半透明度由1变0，然后在完成后续的翻转过程中透明度由0变1。我们使用了@SuppressLint("NewApi")是因为view.setAlpha方法不支持api 11以下，如果你想兼容2.x的设备，请用nineoldandroids库，并用ViewHelper.setAlpha()替代。

为了美化数字，我们将字体 HelveticaNeueLTPro-ThEx.otf放到Assets/fonts目录下。设置字体的代码如下：
1
2
	
Typeface localTypeface = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTPro-ThEx.otf");
txtNumber.setTypeface(localTypeface);

同时为字体加上阴影效果：

<style name="text_shadow_style">
     <item name="android:shadowColor">#80000000</item>
     <item name="android:shadowDx">1.0</item>
     <item name="android:shadowDy">1.0</item>
     <item name="android:shadowRadius">0.5</item>
 </style>
