import kotlinx.browser.window
import org.khronos.webgl.Uint16Array
import org.w3c.dom.CanvasRenderingContext2D

//@JsFun("function registerCtx(x) { window.myCtx = x }")
//external fun registerCtx(x: CanvasRenderingContext2D)

class Runner<in TGame: Game<*>>(private val game: TGame, private val context2D: CanvasRenderingContext2D) {
    private var running = false
    private var startTimeStamp: Double? = null
    private var lastTimeStamp: Double? = null

    private var frameCount = 0

    init {
        println("I'm a runner, I'm a weirdo")
//        registerCtx(context2D)
    }

    private fun animationFrame(timeStamp: Double) {
        if (!this.running) {
            return
        }
//        println("Frame; ts $timeStamp")

        this.lastTimeStamp?.also {
            val elapsedSinceLastFrame = timeStamp - it
            val fps = 1000 / elapsedSinceLastFrame.toFloat()
//            println("Frame; fps $fps")
        }

        this.startTimeStamp?.also {
            val elapsedSinceStart = timeStamp - it
//            println("Frame; elapsed $elapsedSinceStart")

            if (elapsedSinceStart > 10000) {
                this.stop()
            }
        }

        this.lastTimeStamp = timeStamp
        this.frameCount++
        this.step()
        this.requestNextFrame()
    }

    private fun requestNextFrame() {
        window.requestAnimationFrame(::animationFrame)
    }

    private fun step() {
        this.game.step()
        val imageData = this.context2D.createImageData(this.game.width.toDouble(), this.game.height.toDouble())
        val unsafeImageDataData = imageData.data.unsafeCast<Uint16Array>()
        this.game.render(unsafeImageDataData)
        this.context2D.putImageData(imageData, 0.0, 0.0)
    }

    fun start() {
        if (this.running) {
            return
        }

        println("Start")

        this.frameCount = 0
        this.startTimeStamp = window.performance.now()
        this.running = true
        this.requestNextFrame()
    }

    fun stop() {
        println("Stop")
        this.running = false

        val stopTimeStamp = window.performance.now()
        this.startTimeStamp?.also {
            val time = stopTimeStamp - it
            println("${this.frameCount} frames in $time ms = ${this.frameCount / time * 1000} fps avg")
        }
    }
}