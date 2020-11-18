# TargetClick

#### 思路总结：

​		点击事件经常会面临增量业务的冲击，下级事件也经常会和上级做交互，导致流程不清晰管理混乱。

​		TargetClick使用APT和Kotlinpoet在编译期为每个主体生成一个BindingClickImpl作为点击事件总线管理，通过开放的局域和全域的生成模板，保持应对几乎任何增量业务的能力。

*全域已生成快速点击的逻辑判断

##### 切面生成思路：

![target_click](C:\Users\VULCAN\Desktop\安顺丰丰\agroa\target_click.png)

##### 流程思路：

![target_click_view](C:\Users\VULCAN\Desktop\安顺丰丰\agroa\target_click_view.png)

##### 模板类：

![](C:\Users\VULCAN\Desktop\安顺丰丰\agroa\target_class.png)

##### 使用：

```
@TargetClick
class MainActivity : AppCompatActivity(),TargetClickImplCallback {
    private val bindingClickImpl=MainActivityTargetClickImpl(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
	
    override fun viewOnclick(view: Any, index: Int?, data: Any?) {

    }
}
```