# AdCycle
适用于广告轮播和引导页


原始思想来源于任玉刚的这篇博客http://blog.csdn.net/singwhatiwanna/article/details/46541225。

在该基础上实现了图片的网络加载，加载采用Volley+三级缓存，并揉进了Trinea关于广告轮播的一些控制参数。

通过Adapter.setCycle(boolean)方法，实现了无限轮播适用于广告栏和引导页非循环的自由切换。


参考如下大神的设计思想
   http://www.trinea.cn/android/auto-scroll-view-pager/
   http://blog.csdn.net/singwhatiwanna/article/details/46541225

后期改进：
  调试了2天做出来的效果。时间仓促指示器和标题显示还没处理。
  后期可将MyAdPagerAdapter.java中不变的部分进一步的封装起来，特别是instantiateItem函数里面的那块内容。
  

效果图：
![image](https://github.com/ztoh/AdCycle/blob/master/adTest2.gif)
